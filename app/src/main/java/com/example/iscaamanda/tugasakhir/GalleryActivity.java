package com.example.iscaamanda.tugasakhir;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Log.d(TAG, "onCreate: started.");

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming");

        if(getIntent().hasExtra("first_name")
                && getIntent().hasExtra("last_name")
                && getIntent().hasExtra("patient_id")
                && getIntent().hasExtra("add_birthday")
                && getIntent().hasExtra("add_date")
                && getIntent().hasExtra("eye_position")
                && getIntent().hasExtra("image_loc")){
            Log.d(TAG, "getIncomingIntent: found intent extras.");

             String firstName = getIntent().getStringExtra("first_name");
             String lastName = getIntent().getStringExtra("last_name");
             String patientId = getIntent().getStringExtra("patient_id");
             String addBirthday = getIntent().getStringExtra("add_birthday");
             String addDate = getIntent().getStringExtra("add_date");
             String eyePosition = getIntent().getStringExtra("eye_position");
             String imageLoc = getIntent().getStringExtra("image_loc");

             setData(firstName, lastName, patientId, addBirthday, addDate, eyePosition, imageLoc);

        }

    }

    private void setData(String firstName, String lastName, String patientId,
                         String addBirthday, String addDate, String eyePosition, String imageLoc){
        Log.d(TAG, "setData: setting the data to widgets.");

        TextView mfirstName = findViewById(R.id.first_name);
        TextView mlastName = findViewById(R.id.last_name);
        TextView mpatientId = findViewById(R.id.patient_id);
        TextView maddBirthday = findViewById(R.id.add_birthday);
        TextView maddDate = findViewById(R.id.add_date);
        TextView meyePosition = findViewById(R.id.eye_position);
        ImageView mimageLoc = findViewById(R.id.image);

        Uri imgUri = Uri.parse(imageLoc);
        mfirstName.setText(firstName);
        mlastName.setText(lastName);
        mpatientId.setText(patientId);
        maddBirthday.setText(addBirthday);
        maddDate.setText(addDate);
        meyePosition.setText(eyePosition);
        mimageLoc.setImageURI(imgUri);


    }

}
