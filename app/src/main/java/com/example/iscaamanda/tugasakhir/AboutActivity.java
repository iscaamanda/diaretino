package com.example.iscaamanda.tugasakhir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main:
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
        }
        switch (item.getItemId()){
            case R.id.action_pengaturan:
                Intent intent = new Intent(AboutActivity.this, SettingsActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
