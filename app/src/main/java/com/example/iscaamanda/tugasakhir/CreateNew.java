package com.example.iscaamanda.tugasakhir;

import android.Manifest;
import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class CreateNew extends AppCompatActivity {

    private static final String TAG = "CreateNew";

    //deklarasi variabel
    EditText firstName;
    EditText lastName;
    EditText patientId;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String pathToFile;
    ImageView imageView;
    Uri photoURI;
    private EditText addBirthday;
    private DatePickerDialog.OnDateSetListener addBirthDateSetListener;
    private EditText addDate;
    private DatePickerDialog.OnDateSetListener addDateSetListener;
    Button buttonNew;

    //PRESETS FOR CLASSIFICATION

    // presets for rgb conversion
    private static final int RESULTS_TO_SHOW = 3;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    // options for model interpreter
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    // tflite graph
    private Interpreter tflite;
    // holds all the possible labels for model
    private List<String> labelList;
    // holds the selected image data as bytes
    private ByteBuffer imgData = null;
    // holds the probabilities of each label for non-quantized graphs
    private float[][] labelProbArray = null;
    // array that holds the labels with the highest probabilities
    private String[] topLables = null;
    // array that holds the highest probabilities
    private String[] topConfidence = null;
    // classifier information
    private String classifier;

    // input image dimensions for the MobileNet Model
    private int DIM_IMG_SIZE_X = 224;
    private int DIM_IMG_SIZE_Y = 224;
    private int DIM_PIXEL_SIZE = 3;

    // int array to hold image data
    private int[] intValues;

    // priority queue that will hold the top results from the CNN
    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });
    private String eyePosition;
    private String imageLoc;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        intValues = new int[DIM_IMG_SIZE_Y*DIM_IMG_SIZE_X];
        classifier = "eyemessidor3_graph.lite";


        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_new);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        patientId = findViewById(R.id.patient_id);
        addBirthday = findViewById(R.id.add_birthday);
        addDate = findViewById(R.id.add_date);
        radioGroup = findViewById(R.id.radio_group);
        buttonNew = findViewById(R.id.bNew);


        //initilize graph and labels
        try{
            tflite = new Interpreter(loadModelFile(), tfliteOptions);
            labelList = loadLabelList();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        // initialize byte array. The size depends if the input data needs to be quantized or not
        imgData =
                    ByteBuffer.allocateDirect(
                            4 * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());

        // initialize probabilities array. The datatypes that array holds depends if the input data needs to be quantized or not
        labelProbArray = new float[1][labelList.size()];

        // initialize array to hold top labels
        topLables = new String[RESULTS_TO_SHOW];

        // initialize array to hold top probabilities
        topConfidence = new String[RESULTS_TO_SHOW];



        //DATE PICKER 1

        addBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateNew.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        addBirthDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        addBirthDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: date: " + year + "/" + month + "/" + dayOfMonth);
                String patientBirthDate = dayOfMonth + "/" + month + "/" + year;
                addBirthday.setText(patientBirthDate);
            }
        };

        //DATE PICKER 2

        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateNew.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        addDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        addDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: date: " + year + "/" + month + "/" + dayOfMonth);
                String patientDate = dayOfMonth + "/" + month + "/" + year;
                addDate.setText(patientDate);

            }
        };

        if(Build.VERSION.SDK_INT>=25){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }

        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open camera

                dispatchPictureTakerAction();


                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                eyePosition = radioButton.getText().toString();
                imageLoc = photoURI.toString();
                String imageLabel = topLables[0];
                String imageConfidence = topConfidence[0];

//                Patient patient = new Patient(firstName.getText().toString(),
//                        lastName.getText().toString(),
//                        patientId.getText().toString(),
//                        addBirthday.getText().toString(),
//                        addDate.getText().toString(),
//                        eyePosition,
//                        imageLoc,
//                        topLables[0],
//                        topConfidence[0]);
//                db.patientDao().insertAll(patient);
//                startActivity(new Intent(CreateNew.this, MainActivity.class));
            }
        });

    }

    //fungsi radio button
    public void onRadioButtonClicked(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }


    //fungsi dispatch kamera
    private void dispatchPictureTakerAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null){
           File photoFile = null;
           photoFile = createPhotoFile();

           if (photoFile!=null){
               String pathToFile = photoFile.getAbsolutePath();
               photoURI = FileProvider.getUriForFile(CreateNew.this,"com.example.iscaamanda.tugasakhir.fileprovider", photoFile);
               takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
               startActivityForResult(takePic, 1);
           }
//           else{
//               Toast.makeText(getApplicationContext(),"BOO",Toast.LENGTH_LONG).show();
//           }

        }
    }


    //fungsi membuat file foto
    private File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_MMmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File storageDir = new File(getFilesDir(),"images");
//        storageDir.getParentFile().mkdirs();
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg",storageDir);
        } catch (IOException e) {
//            Log.d("myLog", "Excep: " + e.toString());
            Toast.makeText(getApplicationContext(),"No Image Created:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return image;
    }


    //fungsi menyimpan gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
//                Uri imageUri = data.getData();
                   Bitmap photo = null;
                   try {
                       photo = MediaStore.Images.Media.getBitmap(this.getContentResolver() , photoURI);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   imageClassifier(photo);

                final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "datapasien")
                        .allowMainThreadQueries()
                        .build();

                Patient patient = new Patient(firstName.getText().toString(),
                        lastName.getText().toString(),
                        patientId.getText().toString(),
                        addBirthday.getText().toString(),
                        addDate.getText().toString(),
                        eyePosition,
                        imageLoc,
                        topLables[0],
                        topConfidence[0]);
                db.patientDao().insertAll(patient);

                startActivity(new Intent(this, MainActivity.class));

            }
        }
    }

    //fungsi imageClassifier
    private void imageClassifier(Bitmap photo) {
        // get current bitmap from imageUri
//            Bitmap bitmap_orig = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        // resize the bitmap to the required input size to the CNN
        Bitmap bitmap = getResizedBitmap(photo, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
        // convert bitmap to byte array
        convertBitmapToByteBuffer(bitmap);
        // pass byte data to the graph
        tflite.run(imgData, labelProbArray);
        topLables[0] = labelList.get(1);
        topConfidence[0] = String.format("%.0f%%", labelProbArray[0][1] * 100);
        Log.d(TAG, "imageClassifier: "+ topLables[0] + " " + topConfidence[0]);
    }


    // resizes bitmap to given dimensions
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    // converts bitmap to byte array which is passed in the tflite graph
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // loop through all pixels
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                // get rgb values from intValues where each int holds the rgb values for a pixel.
                    imgData.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                    imgData.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                    imgData.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
            }
        }
    }

    // loads tflite graph from file
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd(classifier);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // loads the labels from the label txt file in assets into a string array
    private List<String> loadLabelList() throws IOException {
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(this.getAssets().open("eyemessidor3_labels.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }


}