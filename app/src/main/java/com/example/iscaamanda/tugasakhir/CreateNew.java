package com.example.iscaamanda.tugasakhir;

import android.Manifest;
import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        patientId = findViewById(R.id.patient_id);
        addBirthday = findViewById(R.id.add_birthday);
        addDate = findViewById(R.id.add_date);
        radioGroup = findViewById(R.id.radio_group);
        buttonNew = findViewById(R.id.bNew);
//        imageView = findViewById(R.id.image);


        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "datapasien")
                .allowMainThreadQueries()
                .build();


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
//                dispatchTakePictureIntent();


                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String eyePosition = radioButton.getText().toString();

                String imageLoc = photoURI.toString();

                Patient patient = new Patient(firstName.getText().toString(),
                        lastName.getText().toString(),
                        patientId.getText().toString(),
                        addBirthday.getText().toString(),
                        addDate.getText().toString(),
                        eyePosition,
                        imageLoc);
                db.patientDao().insertAll(patient);
//                startActivity(new Intent(CreateNew.this, MainActivity.class));
            }
        });

    }

    //fungsi nyimpen gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
//                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
//                imageView.setImageBitmap(bitmap);
                startActivity(new Intent(this, MainActivity.class));
            }
        }
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


//    static final int REQUEST_TAKE_PHOTO = 1;
//
//    //take photo with camera app
//    private void dispatchTakePictureIntent(){
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
//            //Create the file where the photo should go
//            File photoFile = null;
//            try{
//                photoFile = createImageFile();
//            }catch (IOException ex){
//                //bikin toast
//            }
//            //continue only if the file was successfully created
//            if (photoFile != null){
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.iscaamanda.tugasakhir.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//
//        }
//        galleryAddPic();
//    }
//
//    //save full size photo
//    String currentPhotoPath;
//
//    private File createImageFile() throws IOException{
//        //create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getFilesDir();
//        File image = File.createTempFile(
//                imageFileName, /*prefix*/
//                ".jpg", /*suffix*/
//                storageDir /*directory*/
//         );
//
//        //save a file; path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    //add phto to gallery
//    private void galleryAddPic(){
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }


    //fungsi radio button
    public void onRadioButtonClicked(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

    }

}