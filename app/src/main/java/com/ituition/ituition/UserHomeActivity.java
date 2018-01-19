package com.ituition.ituition;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ituition.ituition.fragments.LatestReviewsFragment;
import com.ituition.ituition.fragments.PopularTutorsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import adapters.VPAdapter;
import app.AppController;
import model.DB;
import model.Database;

public class UserHomeActivity extends AppCompatActivity {
    private final static String TAG = "Mushfiq_UHA";
    private ViewPager viewPager;
    private TextView textCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        setupViewPager();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tabs);
        tabLayout.setupWithViewPager(viewPager);

        new pollForNotification().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void setupViewPager() {
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        //adapter.addFragment(new TutorsNearYouFragment(), "TUTORS NEAR YOU");
        adapter.addFragment(new PopularTutorsFragment(), "POPULAR TUTORS");
        adapter.addFragment(new LatestReviewsFragment(), "LATEST REVIEWS");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_home_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
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
        //updateHotCount(mCartItemCount++);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "notifications " + DB.notificationCount);
                textCartItemCount.setVisibility(View.INVISIBLE);
                getTuitions();
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search:

                return true;
            case R.id.action_notification:

                return false;
            case R.id.action_account:
                intent = new Intent(UserHomeActivity.this, ProfileActivity.class);
                intent.putExtra("activity", 1);
                startActivity(intent);
                return true;
            case R.id.action_my_tuition:
                return false;
            case R.id.action_my_tutors:
                return false;
            case R.id.action_req_tuition:
                return false;
            case R.id.logout:
                intent = new Intent(UserHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
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
                                    int pendingReq = jo.getInt("pending");
                                    int acceptedReq = jo.getInt("accepted");
                                    DB.notificationCount = pendingReq + acceptedReq;
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

    @Override
    public void onBackPressed() {

    }

    private void getTuitions() {
        final String[] tuitionNotices = new String[20];
        final int[] tuitionIDs = new int[20];
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserHomeActivity.this);
        final int[] countLength = new int[1];
        String url = DB.SERVER + "Test/include/324/get_pending_tuitions.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response " + response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            JSONArray ja = jo.getJSONArray("tuitions");
                            countLength[0] = ja.length();
                            for (int i = 0; i < ja.length(); ++i) {
                                tuitionIDs[i] = ja.getJSONObject(i).getInt("tuitionid");
                                tuitionNotices[i] = ja.getJSONObject(i).getString("studentname");
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String[] notices = new String[countLength[0]];
                                    for (int i = 0; i < notices.length; ++i) {
                                        if (i < DB.notificationCount) {
                                            notices[i] = "(New) You have a new request from " + tuitionNotices[i];
                                        } else {
                                            notices[i] = "You have a request from " + tuitionNotices[i];
                                        }
                                    }

                                    builder.setTitle("Notifications");
                                    builder.setItems(notices, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int item) {
                                            // Do something with the selection
                                            //mDoneButton.setText(items[item]);
                                            Log.d(TAG, String.format("Item %d selected\n", tuitionIDs[item]));
                                            Intent intent = new Intent(UserHomeActivity.this, ShowTuitionRequestActivity.class);
                                            intent.putExtra("tuitionId", tuitionIDs[item]);
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

