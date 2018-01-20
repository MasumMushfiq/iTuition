package com.ituition.ituition;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

public class Profile extends AppCompatActivity {
    private final String TAG = "Mushfiq_PA";
    private final int[] countLength = new int[2];

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
    TextView oneSalary;
    TextView twoSalary;
    TextView threeSalary;
    TextView moreSalary;

    private TextView textCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.pa_toolbar);
        setSupportActionBar(toolbar);

        userName = Database.getCurrentUsername();

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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Update.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_pr).getActionView();
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


        final View notifications = menu.findItem(R.id.action_notification_pr).getActionView();

        textCartItemCount = (TextView) notifications.findViewById(R.id.txtCount);

        /*notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "notifications " + DB.notificationCount);
                textCartItemCount.setVisibility(View.INVISIBLE);
                getTuitions();
            }
        });*/

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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search_pr:
                Log.d(TAG, "Action Search Pressed");
                return true;
            case R.id.action_notification_pr:
                getTuitions();
                return true;
            case R.id.action_home_pr:
                intent = new Intent(getApplicationContext(), UserHome.class);
                startActivity(intent);
                return true;
            case R.id.action_my_tuition_pr:
                intent = new Intent(getApplicationContext(), TuitionList.class);
                intent.putExtra("from", 0); // status 2, 4 tutor me
                startActivity(intent);
                return true;
            case R.id.action_my_tutors_pr:
                intent = new Intent(getApplicationContext(), TuitionList.class);
                intent.putExtra("from", 1); // status 2, 4 student me
                startActivity(intent);
                return true;
            case R.id.action_req_tuition_pr:
                intent = new Intent(getApplicationContext(), TuitionList.class);
                intent.putExtra("from", 2); // status 0, 1 student me
                startActivity(intent);
                return true;
            case R.id.logout_pr:
                intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                return true;
        }
        return true;
    }


    private void getTuitions() {
        final String[] tuitionNotices = new String[20];
        final int[] tuitionIDs = new int[20];
        final AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        String url = DB.SERVER + "Test/include/324/get_pending_tuitions.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response " + response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            JSONArray pendingArray = jo.getJSONArray("pendingtuitions");
                            JSONArray acceptedArray = jo.getJSONArray("acceptedtuitions");
                            countLength[0] = acceptedArray.length();
                            countLength[1] = pendingArray.length();
                            for (int i = 0; i < acceptedArray.length(); ++i) {
                                tuitionIDs[i] = acceptedArray.getJSONObject(i).getInt("tuitionid");
                                tuitionNotices[i] = acceptedArray.getJSONObject(i).getString("tutorname");
                            }
                            for (int i = countLength[0]; i < countLength[0] + pendingArray.length(); ++i) {
                                tuitionIDs[i] = pendingArray.getJSONObject(i - countLength[0]).getInt("tuitionid");
                                tuitionNotices[i] = pendingArray.getJSONObject(i - countLength[0]).getString("studentname");
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String[] notices = new String[countLength[0] + countLength[1]];
                                    for (int i = 0; i < notices.length; ++i) {
                                        if (i < countLength[0]) {
                                            //in accepted tuitions
                                            notices[i] = tuitionNotices[i] + " has accepted your tuition request";
                                        } else {
                                            //notices[i] = "You have a request from " + tuitionNotices[i];
                                            if (countLength[0] + i < DB.pendingReq) {
                                                notices[i] = "(New) You have a new request from " + tuitionNotices[i];
                                            } else {
                                                notices[i] = "You have a request from " + tuitionNotices[i];
                                            }
                                        }
                                    }

                                    builder.setTitle("Notifications");
                                    builder.setItems(notices, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int item) {
                                            // TODO distinguish between accepted tuitions and pending tuitions
                                            Log.d(TAG, String.format("Item %d selected\n", tuitionIDs[item]));
                                            Intent intent = new Intent(Profile.this, ShowTuitionRequest.class);
                                            intent.putExtra("tuitionId", tuitionIDs[item]);
                                            if (item < countLength[0]) {
                                                intent.putExtra("from", 2); //accepted request
                                            } else {
                                                intent.putExtra("from", 0); //requested to me
                                            }
                                            startActivity(intent);
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });

                            //publishProgress();
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
        AppController.getInstance().addToRequestQueue(request);
    }

}
