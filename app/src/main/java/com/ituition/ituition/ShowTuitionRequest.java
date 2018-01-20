package com.ituition.ituition;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppController;
import model.DB;

public class ShowTuitionRequest extends AppCompatActivity {
    private int tuitionId;

   /*
    *  from notification dialogue = 0
    *
    *  got from tuition list
    *
    *  from my tuitions = 1  I am the tutor
    *  from my tutors = 2    I am the student
    *  from my requests = 3  I am the student
    */
    private int from;
    private static final String TAG = "Mushfiq_STR";

    private String sname, subjects, address, aclev, semail, scontactNo;
    private int salary, daysPerWeek, nStudent;

    private TextView nameField, subjectsField, addressField, aclevField;
    private TextView salaryField, daysPerWeekField, nosField, contactField, emailField;

    private Button confirmBtn, rejectBtn, topLabel;

    private String tname, tcontactNo, temail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tuition_request);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            tuitionId = bundle.getInt("tuitionId");
            from = bundle.getInt("from");
            Log.d(TAG, "Tuition Id is " + String.valueOf(tuitionId) + " from " + from);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.str_toolbar);
        setSupportActionBar(toolbar);

        topLabel = (Button) findViewById(R.id.str_tuition_req_label);
        nameField = (TextView) findViewById(R.id.str_name_field);
        subjectsField = (TextView) findViewById(R.id.str_subjects_field);
        addressField = (TextView) findViewById(R.id.str_address_field);
        aclevField = (TextView) findViewById(R.id.str_aclev_field);
        salaryField = (TextView) findViewById(R.id.str_salary_field);
        daysPerWeekField = (TextView) findViewById(R.id.str_ndays_field);
        nosField = (TextView) findViewById(R.id.str_nos_field);
        contactField = (TextView) findViewById(R.id.str_contact_field);
        emailField = (TextView) findViewById(R.id.str_email_field);
        confirmBtn = (Button) findViewById(R.id.str_confirm_btn);
        rejectBtn = (Button) findViewById(R.id.str_reject_btn);

        if (from == 0) {
            topLabel.setText("Tuition Request");
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replyRequest(2);    //accepted
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowTuitionRequest.this);
                    builder.setTitle("Request Confirmed")
                            .setMessage("Congratulations!!! You can find your tuitions under My Tuitions menu");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), UserHome.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replyRequest(3);    //rejected
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowTuitionRequest.this);
                    builder.setTitle("Request Cancelled");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), UserHome.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else {
            rejectBtn.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            if (from == 1) {
                topLabel.setText("My Tuition");
                confirmBtn.setVisibility(View.GONE);
            } else if (from == 2) {
                topLabel.setText("My Tutor");
                confirmBtn.setGravity(Gravity.CENTER_HORIZONTAL);
                confirmBtn.setLayoutParams(lp);
                confirmBtn.setText("Feedback");
            } else if (from == 3) {
                topLabel.setText("Request Sent");
                confirmBtn.setVisibility(View.GONE);
            }
        }
        getTuitionDetails();

    }


    private void getTuitionDetails() {
        final int[] result1 = {-1};
        String url = DB.SERVER + "Test/include/324/get_tuition_details.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("volley", response);
                        try {
                            Log.d(TAG, response);
                            JSONObject jo = new JSONObject(response);
                            jo = jo.getJSONObject("tuition");
                            setTuitionData(jo);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, sname + subjects);
                                    subjectsField.setText(subjects);
                                    addressField.setText(address);
                                    aclevField.setText(aclev);
                                    nosField.setText(String.format("%d", nStudent));
                                    daysPerWeekField.setText(String.format("%d", daysPerWeek));
                                    salaryField.setText(String.format("%d", salary));
                                    if (from == 0 || from == 1) {
                                        nameField.setText(sname);
                                        contactField.setText(scontactNo);
                                        emailField.setText(semail);
                                    } else if (from == 2 || from == 3) {
                                        nameField.setText(tname);
                                        contactField.setText(tcontactNo);
                                        emailField.setText(temail);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volleyError", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tuitionid", String.valueOf(tuitionId));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void setTuitionData(JSONObject jo) throws JSONException {
        sname = jo.getString("studentname");
        tname = jo.getString("tutorname");
        subjects = jo.getString("subjects");
        address = jo.getString("address");
        aclev = jo.getString("aclev");
        nStudent = jo.getInt("number_of_students");
        daysPerWeek = jo.getInt("days_per_week");
        salary = jo.getInt("salary");
        scontactNo = jo.getString("scontact_no");
        semail = jo.getString("semail_id");
        tcontactNo = jo.getString("tcontact_no");
        temail = jo.getString("temail_id");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.common_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_sm).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search_sm:
                Log.d(TAG, "Action Search Pressed");
                return true;
            case R.id.action_account_sm:
                intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("activity", 1);
                startActivity(intent);
                return true;
            case R.id.action_go_home:
                intent = new Intent(getApplicationContext(), UserHome.class);
                startActivity(intent);
                return true;
            case R.id.action_my_tuition_sm:
                intent = new Intent(getApplicationContext(), TuitionList.class);
                intent.putExtra("from", 0); // status 2, 4 tutor me
                startActivity(intent);
                return true;
            case R.id.action_my_tutors_sm:
                intent = new Intent(getApplicationContext(), TuitionList.class);
                intent.putExtra("from", 1); // status 2, 4 student me
                startActivity(intent);
                return true;
            case R.id.action_req_tuition_sm:
                intent = new Intent(getApplicationContext(), TuitionList.class);
                intent.putExtra("from", 2); // status 0, 1 student me
                startActivity(intent);
                return true;
            case R.id.logout_sm:
                intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                return true;
        }
        return true;
    }


    private void replyRequest(final int replyStatus) {
        final String url = DB.SERVER + "Test/include/324/update_tuition_status.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("volley", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volleyError", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tuitionid", String.valueOf(tuitionId));
                params.put("status", String.valueOf(replyStatus));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}
