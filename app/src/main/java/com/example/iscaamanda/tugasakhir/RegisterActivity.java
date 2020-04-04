package com.example.iscaamanda.tugasakhir;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.iscaamanda.tugasakhir.helper.InputValidation;
import com.example.iscaamanda.tugasakhir.model.User;
import com.example.iscaamanda.tugasakhir.sql.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutInstitution;
    private TextInputLayout textInputLayoutAddress;
    private TextInputLayout textInputLayoutUserName;
    private TextInputLayout textInputLayoutUserPass;
    private TextInputLayout textInputLayoutConfirmPass;
    private TextInputLayout textInputLayoutPinAdmin;

    private TextInputEditText textInputEditTextInstitution;
    private TextInputEditText textInputEditTextAddress;
    private TextInputEditText textInputEditTextUserName;
    private TextInputEditText textInputEditTextUserPass;
    private TextInputEditText textInputEditTextConfirmPass;
    private TextInputEditText textInputEditTextPinAdmin;

    private Button buttonRegister;
    private TextView linkLogin;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputLayoutInstitution = findViewById(R.id.layout_institusi);
        textInputLayoutAddress = findViewById(R.id.layout_alamat);
        textInputLayoutUserName = findViewById(R.id.layout_username);
        textInputLayoutUserPass = findViewById(R.id.layout_userpass);
        textInputLayoutConfirmPass = findViewById(R.id.layout_confirmpass);
        textInputLayoutPinAdmin = findViewById(R.id.layout_pinadmin);

        textInputEditTextInstitution = findViewById(R.id.input_institusi);
        textInputEditTextAddress = findViewById(R.id.input_alamat);
        textInputEditTextUserName = findViewById(R.id.input_username);
        textInputEditTextUserPass = findViewById(R.id.input_userpass);
        textInputEditTextConfirmPass = findViewById(R.id.input_confirmpass);
        textInputEditTextPinAdmin = findViewById(R.id.input_pinadmin);

        buttonRegister = findViewById(R.id.button_regist);
        linkLogin = findViewById(R.id.link_login);
    }

    private void initListeners() {
        buttonRegister.setOnClickListener(this);
        linkLogin.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User ();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_regist:
                postDataToSQLite();
                break;
            case R.id.link_login:
                finish();
                break;
        }
    }

    private void postDataToSQLite(){
        if(!inputValidation.isInputEditTextFilled(textInputEditTextInstitution,
                textInputLayoutInstitution, getString(R.string.error_field_required))){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextAddress,
                textInputLayoutAddress, getString(R.string.error_field_required))){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextUserName,
                textInputLayoutUserName, getString(R.string.error_field_required))){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextUserPass,
                textInputLayoutUserPass, getString(R.string.error_field_required))){
            return;
        }
        if(!inputValidation.isInputEditTextMatches(textInputEditTextUserPass,
                textInputEditTextConfirmPass, textInputLayoutConfirmPass, getString(R.string.error_incorrect_password))) {
            return;
        }
        if(!inputValidation.isInputPinMatches(textInputEditTextPinAdmin,
                textInputLayoutPinAdmin, getString(R.string.error_pinadmin))){
            return;
        }
        if (!databaseHelper.checkUser(textInputEditTextUserName.getText().toString().trim(),textInputEditTextUserPass.getText().toString().trim()))
        {
            user.setUserName(textInputEditTextUserName.getText().toString().trim());
            user.setUserPass(textInputEditTextUserPass.getText().toString().trim());
            user.setInstitution(textInputEditTextInstitution.getText().toString().trim());
            user.setAddress(textInputEditTextAddress.getText().toString().trim());

            databaseHelper.addUser(user);

            //Snack Bar to show success message that record is saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message),
                    Snackbar.LENGTH_LONG).show();
            emptyInputEditText();

        } else{
            //Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_username_exists), Snackbar.LENGTH_LONG).show();
        }

    }

    private void emptyInputEditText() {
        textInputEditTextUserName.setText(null);
        textInputEditTextUserPass.setText(null);
        textInputEditTextInstitution.setText(null);
        textInputEditTextAddress.setText(null);
        textInputEditTextConfirmPass.setText(null);
    }


}
