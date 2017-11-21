package com.ituition.ituition;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Database;

public class ProfileActivity extends AppCompatActivity {
    String userName = "";
    Button next;
    TextView prefField;
    TextView specialtyField;
    TextView nameField;
    TextView acBdField;
    TextView contactNoField;
    TextView emailField;
    TextView locationField;
    ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userName = (String) bundle.get("username");
        }


        next = (Button) findViewById(R.id.profileNext);
        prefField = (TextView) findViewById(R.id.preferencesField);
        specialtyField = (TextView) findViewById(R.id.specialtyField);
        nameField = (TextView) findViewById(R.id.nameField);
        acBdField = (TextView) findViewById(R.id.acBdField);
        locationField = (TextView) findViewById(R.id.locationField);
        contactNoField = (TextView) findViewById(R.id.contactNoField);
        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        emailField = (TextView) findViewById(R.id.emailField);

        prefField.setText(getAcademicPreferences());
        specialtyField.setText(getSubjectSpecialty());
        nameField.setText(getName());
        acBdField.setText(Database.users.get(userName).getAcademicBackground());
        contactNoField.setText(Database.users.get(userName).getContactNo());
        emailField.setText(Database.users.get(userName).getEmail());
        locationField.setText(getLocations());
        profilePicture.setImageResource(R.drawable.icon);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpdateActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getAcademicPreferences() {
        StringBuilder x = new StringBuilder();
        if (!Database.acPreferences.containsKey(userName))
            return "";
        ArrayList<String> prefs = Database.acPreferences.get(userName).getAcPrefs();
        for (String s : prefs) {
            x.append(s).append("\n");
        }
        return x.toString().substring(0, x.length() - 1);
    }

    private String getLocations() {
        StringBuilder x = new StringBuilder();
        if (!Database.locations.containsKey(userName))
            return "";
        ArrayList<String> prefs = Database.locations.get(userName).getLocations();
        for (String s : prefs) {
            x.append(s).append("\n");
        }
        System.out.println(x);
        return x.toString().substring(0, x.length() - 1);
    }


    private String getSubjectSpecialty() {
        StringBuilder x = new StringBuilder();
        if (!Database.subSpecs.containsKey(userName))
            return "";
        ArrayList <String> specs = Database.subSpecs.get(userName).getSubSpec();
        for (String s : specs) {
            x.append(s).append("\n");
        }
        return x.toString().substring(0, x.length() - 1);
    }

    private String getName() {
        return Database.users.get(userName).getName();
    }
}
