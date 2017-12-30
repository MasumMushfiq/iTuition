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
import android.widget.SeekBar;
import android.widget.TextView;

import Model.Database;
import Model.TuitionRequest;

public class RequestTuition extends AppCompatActivity {
    String userName;
    private Toolbar toolbar;
    private TextView subjects;
    private TextView address;
    private SeekBar nStudents;
    private TextView salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tuition);

        userName = Database.getCurrentUsername();


        toolbar = (Toolbar)findViewById(R.id.reqTuitionToolbar);
        setSupportActionBar(toolbar);



        Button button = (Button)findViewById(R.id.submitRequest);
        subjects = (TextView)findViewById(R.id.reqTuitionSubjectsField);
        address = (TextView)findViewById(R.id.reqTuitionAddressField);
        nStudents = (SeekBar)findViewById(R.id.nof_studentsSeekbar);
        salary = (TextView)findViewById(R.id.reqTuitionSalaryField);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                Database.tuitionRequests.add(makeRequest());
                Intent intent = new Intent(RequestTuition.this, UserHomeActivity.class);
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
        int n = nStudents.getProgress();
        int sal = Integer.parseInt(salary.getText().toString());

        TuitionRequest tr = new TuitionRequest(s, a, n, sal);
        Log.d("ReqTut", tr.toString());
        return tr;
    }
}
