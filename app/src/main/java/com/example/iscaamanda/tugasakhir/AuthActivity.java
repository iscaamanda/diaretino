package com.example.iscaamanda.tugasakhir;

import android.arch.persistence.room.Database;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.iscaamanda.tugasakhir.helper.InputValidation;
import com.example.iscaamanda.tugasakhir.model.User;
import com.example.iscaamanda.tugasakhir.sql.DatabaseHelper;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = AuthActivity.this;

    private ScrollView scrollView;
    private TextInputLayout textInputLayoutUserName;
    private TextInputLayout textInputLayoutUserPass;
    private TextInputEditText textInputEditTextUserName;
    private TextInputEditText textInputEditTextUserPass;
    private Button buttonAuth;
    private TextView linkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initViews();
        initListeners();
        initObjects();

        if(session.loggedIn()){
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
            finish();
        }
    }


    private void initViews(){
        scrollView = findViewById(R.id.scroll_view);
        textInputLayoutUserName = findViewById(R.id.layout_username);
        textInputLayoutUserPass = findViewById(R.id.layout_userpass);
        textInputEditTextUserName = findViewById(R.id.input_username);
        textInputEditTextUserPass =  findViewById(R.id.input_userpass);
        buttonAuth = findViewById(R.id.button_auth);
        linkRegister = findViewById(R.id.link_register);
    }

    private void initListeners() {
        buttonAuth.setOnClickListener(this);
        linkRegister.setOnClickListener(this);
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
        session = new Session(activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_auth:
                verifyFromSQLite();
                break;
            case R.id.link_register:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void verifyFromSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserName,
                textInputLayoutUserName, getString(R.string.error_message_username))){
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUserPass,
                textInputLayoutUserPass, getString(R.string.error_message_password))){
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextUserName.getText().toString().trim())){
//          textInputEditTextUserPass.getText().toString().trim())){
            session.setLoggedIn(true);
            Intent accountsIntent = new Intent(activity, MainActivity.class);
            accountsIntent.putExtra("userName",textInputEditTextUserName.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        }else{
            Snackbar.make(scrollView,getString(R.string.error_invalid_username_userpass), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText(){
        textInputEditTextUserName.setText(null);
        textInputEditTextUserPass.setText(null);
    }

}
