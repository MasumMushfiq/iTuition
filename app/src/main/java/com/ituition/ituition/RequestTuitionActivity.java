package com.ituition.ituition;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppController;
import model.DB;
import model.Database;
import model.TuitionRequest;

public class RequestTuitionActivity extends AppCompatActivity {
    String userName;
    private TextView subjects;
    private TextView address;
    private TextView nos;
    private TextView nod;
    private TextView salary;
    private String tutorName;
    ProgressDialog progressDialog;
    private TuitionRequest t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tuition);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tutorName = bundle.getString("tutorname");
        }

        userName = Database.getCurrentUsername();


        Toolbar toolbar = (Toolbar) findViewById(R.id.rt_toolbar);
        setSupportActionBar(toolbar);


        Button button = (Button) findViewById(R.id.submitRequest);
        subjects = (TextView) findViewById(R.id.rt_subjects_field);
        address = (TextView) findViewById(R.id.rt_address_field);
        nos = (TextView) findViewById(R.id.rt_nos_field);
        nod = (TextView) findViewById(R.id.rt_nod_field);
        salary = (TextView) findViewById(R.id.rt_salary_field);
        progressDialog = new ProgressDialog(RequestTuitionActivity.this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
                progressDialog.show();
                t = makeRequest();
                postTuition();
                /*Intent intent = new Intent(RequestTuitionActivity.this, UserHomeActivity.class);
                startActivity(intent);*/
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
        String s = "\"" + subjects.getText().toString() + "\"";
        String a = "\"" + address.getText().toString() + "\"";
        int nstdnt = 0;
        int nday = 0;
        int sal = 0;
        try {
            nstdnt = Integer.parseInt(nos.getText().toString());
        } catch (NumberFormatException e) {
            nstdnt = 1;
        }

        try {
            nday = Integer.parseInt(nod.getText().toString());
        } catch (NumberFormatException e) {
            nday = 3;
        }

        try {
            sal = Integer.parseInt(salary.getText().toString());
        } catch (NumberFormatException e) {
            sal = 500;
        }
        String tn = "\"" + tutorName + "\"";
        String un = "\"" + userName + "\"";
        TuitionRequest tr = new TuitionRequest(tn, un, s, a, nstdnt, nday, sal);
        Log.d("ReqTut", tr.toString());
        return tr;
    }

    private void postTuition() {
        String url = DB.SERVER + "Test/include/324/insert_tuition_request.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(RequestTuitionActivity.this, UserHomeActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ReqTut_error", error.toString());
                progressDialog.dismiss();

                // Showing error message if something goes wrong.
                Toast.makeText(RequestTuitionActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("subjects", t.getSubjects());
                map.put("salary", String.valueOf(t.getSalary()));
                map.put("tutorusername", t.getTutorUsername());
                map.put("studentusername", t.getStudentUsername());
                map.put("address", t.getAddress());
                map.put("number_of_students", String.valueOf(t.getNo_of_students()));
                map.put("days_per_week", String.valueOf(t.getDaysPerWeek()));
                map.put("status", String.valueOf(0));
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);

    }
}
