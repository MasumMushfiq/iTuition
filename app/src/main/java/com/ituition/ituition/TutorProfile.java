package com.ituition.ituition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.AppController;
import model.DB;

public class TutorProfile extends AppCompatActivity {
    private final static String TAG = "Mushfiq_TP";
    private String userName;
    private TextView acLevField, subjectField;
    private TextView nameField, qualificationField, contactNoField, emailField, locationField;
    private TextView oneSalary, twoSalary, threeSalary, moreSalary, ratingVal;
    private Button sendRequest;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userName = bundle.getString("username");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.tp_toolbar);
        setSupportActionBar(toolbar);

        sendRequest = (Button) findViewById(R.id.tp_send_req_btn);
        acLevField = (TextView) findViewById(R.id.tp_aclev_field);
        subjectField = (TextView) findViewById(R.id.tp_subjects_field);
        nameField = (TextView) findViewById(R.id.tp_name_field);
        qualificationField = (TextView) findViewById(R.id.tp_qualification_field);
        locationField = (TextView) findViewById(R.id.tp_location_field);
        contactNoField = (TextView) findViewById(R.id.tp_contact_field);
        //profilePicture = (ImageView) findViewById(R.id.profilePicture);
        emailField = (TextView) findViewById(R.id.tp_email_field);
        oneSalary = (TextView) findViewById(R.id.tp_one_person_salary);
        twoSalary = (TextView) findViewById(R.id.tp_two_person_salary);
        threeSalary = (TextView) findViewById(R.id.tp_three_person_salary);
        moreSalary = (TextView) findViewById(R.id.tp_more_person_salary);
        ratingBar = (RatingBar) findViewById(R.id.tp_rating_bar);
        ratingVal = (TextView) findViewById(R.id.tp_rating_bar_value);

        getProfileData();

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfile.this, RequestTuition.class);
                intent.putExtra("tutorname", userName);
                startActivity(intent);
            }
        });

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
            case R.id.action_go_home:
                intent = new Intent(getApplicationContext(), UserHome.class);
                startActivity(intent);
                return true;
            case R.id.action_account_sm:
                intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("activity", 1);
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

    private void getProfileData() {
        String url = DB.SERVER + "Test/include/324/get_tutor_data.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    setupUser(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Mushfiq_pa", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", userName);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void setupUser(String x) throws JSONException {
        JSONObject user = new JSONObject(x);
        user = user.getJSONObject("user");
        String name = user.getString("name");
        String qualifications = user.getString("qualifications");
        double rating = user.getDouble("rating");
        String contact_no = user.getString("contact-no");
        String email_id = user.getString("email-id");
        String gender = user.getString("gender");
        String sal1 = user.getString("salary_1");
        String sal2 = user.getString("salary_2");
        String sal3 = user.getString("salary_3");
        String sal4 = user.getString("salary_more");
        JSONArray subs = user.getJSONArray("subjects");
        JSONArray locs = user.getJSONArray("locations");
        JSONArray acprfs = user.getJSONArray("aclevels");
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < subs.length(); i++) {
            sb.append(subs.getString(i)).append(", ");
        }
        String sub = sb.toString().substring(0, sb.length() - 2);

        sb = new StringBuilder("");

        for (int i = 0; i < locs.length(); i++) {
            sb.append(locs.getString(i)).append(", ");
        }
        String loc = sb.toString().substring(0, sb.length() - 2);

        sb = new StringBuilder("");

        for (int i = 0; i < acprfs.length(); i++) {
            sb.append(acprfs.getString(i)).append(", ");
        }
        String ac = sb.toString().substring(0, sb.length() - 2);

        ratingBar.setRating((float)rating);
        acLevField.setText(ac);
        subjectField.setText(sub);
        nameField.setText(name);
        qualificationField.setText(qualifications);
        contactNoField.setText(contact_no);
        emailField.setText(email_id);
        locationField.setText(loc);
        oneSalary.setText(sal1);
        twoSalary.setText(sal2);
        threeSalary.setText(sal3);
        moreSalary.setText(sal4);
        ratingVal.setText(String.format(Locale.ENGLISH, "%.1f", rating));
    }
}
