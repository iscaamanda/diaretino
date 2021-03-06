package com.example.iscaamanda.tugasakhir;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.iscaamanda.tugasakhir.sql.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    private TextView institutionName;
    private TextView institutionAddress;
    private  Session session;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        session = new Session(this);
//        User user = databaseHelper.query(textInputEditTextUserName.getText().toString().trim());
//        session.setUsername(user.getUserName());
//

        institutionName = findViewById(R.id.institusi_nama);
        institutionAddress = findViewById(R.id.institusi_alamat);

//        String intentInstitution = getIntent().getStringExtra("institution");
//        String intentAddress = getIntent().getStringExtra("address");
//        institutionName.setText(intentInstitution);
//        institutionAddress.setText(intentAddress);

        institutionName.setText(session.getInstitution());
        institutionAddress.setText(session.getAddress());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main:
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
        }
        switch (item.getItemId()){
            case R.id.action_tentang:
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
