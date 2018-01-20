package com.ituition.ituition;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import model.Database;
import model.User;

public class Profile extends AppCompatActivity {
    private final String TAG = "Mushfiq_PA";
    String userName = "";
    final User user = new User();
    Button next;
    TextView prefField;
    TextView specialtyField;
    TextView nameField;
    TextView acBdField;
    TextView contactNoField;
    TextView emailField;
    TextView locationField;
    ImageView profilePicture;
    TextView oneSalary;
    TextView twoSalary;
    TextView threeSalary;
    TextView moreSalary;
    int activity;

    private TextView textCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.pa_toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            activity = bundle.getInt("activity");
            userName = bundle.getString("username");
            if (userName == null)
                userName = Database.getCurrentUsername();
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
        oneSalary = (TextView) findViewById(R.id.onePersonSalary);
        twoSalary = (TextView) findViewById(R.id.twoPersonSalary);
        threeSalary = (TextView) findViewById(R.id.threePersonSalary);
        moreSalary = (TextView) findViewById(R.id.morePersonSalary);

        //user = Database.users.get(userName);
        profilePicture.setImageResource(R.drawable.ic_contact_picture);

        //new GetProfile().execute();
        getProfileData();
        new pollForNotification().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        if (activity == 1) {
            next.setText("  Update Info  ");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Profile.this, Update.class);
                    startActivity(intent);
                }
            });
        } else if (activity == 2) {
            next.setText("   Request Tuition  ");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Profile.this, RequestTuition.class);
                    intent.putExtra("tutorname", userName);
                    startActivity(intent);
                }
            });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_home_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
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


        final View notifications = menu.findItem(R.id.action_notification).getActionView();

        textCartItemCount = (TextView) notifications.findViewById(R.id.txtCount);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "notifications " + DB.notificationCount);
                textCartItemCount.setVisibility(View.INVISIBLE);
                //getTuitions();
            }
        });

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

    private class pollForNotification extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        /*
         *    do things before doInBackground() code runs
         *    such as preparing and showing a Dialog or ProgressBar
         */
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        /*
         *    updating data
         *    such a Dialog or ProgressBar
         */
            if (DB.notificationCount == 0)
                textCartItemCount.setVisibility(View.GONE);
            else {
                textCartItemCount.setVisibility(View.VISIBLE);
                textCartItemCount.setText(String.format(Locale.ENGLISH, "%d", DB.notificationCount));
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            final String url = DB.SERVER + "Test/include/324/get_no_pending_tuitions.php";
            while (true) {
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jo = new JSONObject(response);
                                    DB.pendingReq = jo.getInt("pending");
                                    DB.acceptedReq = jo.getInt("accepted");
                                    DB.notificationCount = DB.pendingReq + DB.acceptedReq;
                                    Log.d(TAG, String.format("%d", DB.notificationCount));
                                    publishProgress();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", Database.getCurrentUsername());
                        return params;
                    }
                };

            /*RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);*/
                AppController.getInstance().addToRequestQueue(request);
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        /*
         *    do something with data here
         *    display it or send to main activity
         *    close any dialogs/ProgressBars/etc...
         */
        }
    }


    private void setupUser(String x) throws JSONException {
        JSONObject user = new JSONObject(x);
        user = user.getJSONObject("user");
        String name = user.getString("name");
        String qualifications = user.getString("qualifications");
        Double rating = user.getDouble("rating");
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
            sb.append(subs.getString(i)).append("\n");
        }
        String sub = sb.toString().substring(0, sb.length() - 1);

        sb = new StringBuilder("");

        for (int i = 0; i < locs.length(); i++) {
            sb.append(locs.getString(i)).append("\n");
        }
        String loc = sb.toString().substring(0, sb.length() - 1);

        sb = new StringBuilder("");

        for (int i = 0; i < acprfs.length(); i++) {
            sb.append(acprfs.getString(i)).append("\n");
        }
        String ac = sb.toString().substring(0, sb.length() - 1);

        prefField.setText(ac);
        specialtyField.setText(sub);
        nameField.setText(name);
        acBdField.setText(qualifications);
        contactNoField.setText(contact_no);
        emailField.setText(email_id);
        locationField.setText(loc);
        oneSalary.setText(sal1);
        twoSalary.setText(sal2);
        threeSalary.setText(sal3);
        moreSalary.setText(sal4);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_search:
                /*intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);*/
                return true;
            case R.id.action_notification:

                return false;
            case R.id.action_account:
                /*intent = new Intent(Profile.this, Profile.class);
                intent.putExtra("activity", 1);
                startActivity(intent);*/
                return false;
            case R.id.logout:
                intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                return true;
        }
        return true;
    }
}
