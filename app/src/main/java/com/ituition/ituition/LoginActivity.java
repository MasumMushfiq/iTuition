package com.ituition.ituition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Model.AcPreferences;
import Model.Database;
import Model.Location;
import Model.SubSpec;
import Model.User;

public class LoginActivity extends AppCompatActivity {
    Button signInBtn;
    Button registerBtn;
    TextView userNameField;
    TextView passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        loadUsers();
        loadAcPreferences();
        loadSubSpec();
        loadLocations();

        signInBtn = (Button) findViewById(R.id.signInBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        userNameField = (TextView) findViewById(R.id.userNameField);
        passwordField = (TextView) findViewById(R.id.passwordField);


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameField.getText().toString();
                String password = passwordField.getText().toString();
                int result = verifyLogin(userName, password);
                if (result == 1) {
                    Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                    intent.putExtra("username", userName);
                    startActivity(intent);
                } else if (result == 2) {
                    //wrong password
                    Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                } else if (result == 3) {
                    //username is not registered
                    Toast.makeText(getApplicationContext(), "Invalid username", Toast.LENGTH_SHORT).show();

                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }

    private void loadUsers() {
        Database.users.clear();
        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("user", "raw", getPackageName()));

        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    //System.out.println(receiveString);
                    User user = new User(receiveString);
                    Database.users.put(user.getUserName(), user);
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAcPreferences() {
        Database.acPreferences.clear();
        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("ac_pref", "raw", getPackageName()));

        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String input = "";

                while ((input = bufferedReader.readLine()) != null) {
                    //System.out.println(receiveString);
                    String data[] = input.split("@#@");
                    if (Database.acPreferences.containsKey(data[0])) {
                        Database.acPreferences.get(data[0]).addPref(data[1]);
                    } else {
                        AcPreferences acPreferences = new AcPreferences(data[0]);
                        acPreferences.addPref(data[1]);
                        Database.acPreferences.put(data[0], acPreferences);
                    }
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSubSpec() {
        Database.subSpecs.clear();
        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("sub_spec", "raw", getPackageName()));

        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String input = "";

                while ((input = bufferedReader.readLine()) != null) {
                    //System.out.println(receiveString);
                    String data[] = input.split("@#@");
                    if (Database.subSpecs.containsKey(data[0])) {
                        Database.subSpecs.get(data[0]).addSubSpec(data[1]);
                    } else {
                        SubSpec subSpec = new SubSpec(data[0]);
                        subSpec.addSubSpec(data[1]);
                        Database.subSpecs.put(data[0], subSpec);
                    }
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocations() {
        Database.locations.clear();
        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("location", "raw", getPackageName()));

        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String input = "";

                while ((input = bufferedReader.readLine()) != null) {
                    //System.out.println(receiveString);
                    System.out.println(input);
                    String data[] = input.split("@#@");
                    if (Database.locations.containsKey(data[0])) {
                        Database.locations.get(data[0]).addLocation(data[1]);
                    } else {
                        Location location = new Location(data[0]);
                        location.addLocation(data[1]);
                        Database.locations.put(data[0], location);
                    }
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public int verifyLogin(String userName, String password) {
        if (Database.users.containsKey(userName))
            if (Database.users.get(userName).getPassword().equals(password)) {
                return 1;
            } else {
                return 2;
            }
        return 3;
    }
}
