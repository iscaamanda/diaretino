package com.example.iscaamanda.tugasakhir;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    RecyclerView recyclerView;
    PatientAdapter adapter;
    FloatingActionButton fab;
    private  Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new Session(this);
        if(!session.loggedIn()){
            logout();
        }


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

    private void logout() {
        session.setLoggedIn(false);
        finish();
        startActivity(new Intent(MainActivity.this, AuthActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_tentang:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
        }
        switch (item.getItemId()){
            case R.id.action_pengaturan:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
        }
        switch (item.getItemId()){
            case R.id.action_logout:
                logout();
        }

        return super.onOptionsItemSelected(item);
    }
}
