package com.ituition.ituition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import model.Database;
import model.TuitionRequest;

public class RequestTuitionActivity extends AppCompatActivity {
    String userName;
    private Toolbar toolbar;
    private TextView subjects;
    private TextView address;
    private TextView nos;
    private TextView nod;
    private TextView salary;
    private String tutorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tuition);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            tutorName = bundle.getString("tutorname");
        }

        userName = Database.getCurrentUsername();


        toolbar = (Toolbar)findViewById(R.id.rt_toolbar);
        setSupportActionBar(toolbar);



        Button button = (Button)findViewById(R.id.submitRequest);
        subjects = (TextView)findViewById(R.id.rt_subjects_field);
        address = (TextView)findViewById(R.id.rt_address_field);
        nos = (TextView)findViewById(R.id.rt_nos_field);
        nod = (TextView)findViewById(R.id.rt_nod_field);
        salary = (TextView)findViewById(R.id.rt_salary_field);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                Database.tuitionRequests.add(makeRequest());
                Intent intent = new Intent(RequestTuitionActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_home_menu, menu);
        return true;
    }

    private TuitionRequest makeRequest() {
        String s = subjects.getText().toString();
        String a = address.getText().toString();
        int nstdnt = 0;
        int nday = 0;
        int sal = 0;
        try {
            nstdnt = Integer.parseInt(nos.getText().toString());
            nday = Integer.parseInt(nod.getText().toString());
            sal = Integer.parseInt(salary.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        TuitionRequest tr = new TuitionRequest(tutorName, userName, s, a, nstdnt, nday, sal);
        Log.d("ReqTut", tr.toString());
        return tr;
    }
}
