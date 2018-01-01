package com.ituition.ituition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import Model.Database;
import Model.User;

public class RegistrationActivity extends AppCompatActivity {
    EditText name;
    EditText username;
    EditText contactNo;
    EditText email;
    EditText password;
    EditText confirmPassword;
    RadioGroup gender;
    RadioButton male;
    RadioButton female;
    Button btn_signup;
    Button btn_linkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //getSupportActionBar().hide();

        name = (EditText) findViewById(R.id.input_name);
        username = (EditText) findViewById(R.id.inpu_username);
        contactNo = (EditText) findViewById(R.id.input_contactNo);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        gender = (RadioGroup) findViewById(R.id.gender_radio_group);
        male = (RadioButton) findViewById(R.id.male_radio_btn);
        female = (RadioButton) findViewById(R.id.female_radio_btn);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_linkLogin = (Button) findViewById(R.id.link_login);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString();
                String usernameText = username.getText().toString();
                String contactNoText = contactNo.getText().toString();
                String emailText = email.getText().toString();
                String pwText = password.getText().toString();
                String confirmPwText = confirmPassword.getText().toString();
                String genderText = "Male";
                int checkId = gender.getCheckedRadioButtonId();
                if  (checkId == female.getId()){
                    genderText = "Female";
                }

                boolean validation = isNameValid(nameText) && isUsernameValid(usernameText)
                        && isEmailValid(emailText) && isContactNo(contactNoText) && isPasswordValid(pwText, confirmPwText);
                if (validation) {
                    Intent intent = new Intent(RegistrationActivity.this, UserHomeActivity.class);
                    {
                        User user = new User(usernameText, nameText, pwText, contactNoText, emailText, "", genderText, 0);
                        Database.users.put(usernameText, user);
                    }
                    Database.setCurrentUsername(usernameText);
                    intent.putExtra("activity", 1);
                    startActivity(intent);
                }
            }
        });

        btn_linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isNameValid(String name) {
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Name field cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isUsernameValid(String username) {
        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Username must be provided", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Database.users.containsKey(username)) {
            Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!username.matches("[A-Za-z0-9_\\.]+")) {
            Toast.makeText(getApplicationContext(),
                    "Username should contain only letters, digits,_ and .", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isContactNo(String contactNo) {
        if (contactNo.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Contact no must be provided", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        boolean b = email != null && !email.isEmpty();
        if (!b) {
            Toast.makeText(getApplicationContext(), "Email must be provided", Toast.LENGTH_SHORT).show();
            return false;
        }
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!pattern.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Invalid Email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String pw, String cpw) {
        boolean b = pw.length() >= 3 && cpw.length() >= 3;
        if (!b) {
            Toast.makeText(getApplicationContext(), "The password should be atleast 3 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pw.equals(cpw)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
