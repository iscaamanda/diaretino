package com.example.iscaamanda.tugasakhir;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"datapasien")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        List<Patient> patients = db.patientDao().getAllPatients();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientAdapter(patients,getApplicationContext());
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: pressed!");
                startActivity(new Intent(MainActivity.this, CreateNew.class));
            }
        });


    }

//////image intent
//
//    private File imageFile;
//
//    public void process(View view){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        imageFile = new File(Environment.
//                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"test.jpg");
//        Uri tempuri = Uri.fromFile(imageFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        startActivityForResult(intent, 0);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//       //TODO Auto-generated method stub
//        if(requestCode==0)
//        {
//            switch (resultCode){
//                case Activity.RESULT_OK:
//            }
//        }
//
//    }

}
