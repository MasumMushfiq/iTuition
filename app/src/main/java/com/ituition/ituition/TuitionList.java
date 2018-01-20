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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.LVAdapter;
import app.AppController;
import model.DB;
import model.Database;
import model.TuitionItem;

public class TuitionList extends AppCompatActivity {
    /*
     * got from tuition menu
     * my tuition = 0   status 2, 4 tutor me
     * my tutor = 1     status 2, 4 student me
     * my request = 2   status 0, 1 student me
     */
    private int from = 0;

    /*
    * sent to show tuition details
    *
    */
    private int to = 0;

    final static String TAG = "Mushfiq_MT";
    ArrayList<TuitionItem> tuitions = new ArrayList<>();
    LVAdapter lvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tuitions);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            from = bundle.getInt("from");
            Log.d(TAG, "From " + from);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.mt_toolbar);
        setSupportActionBar(toolbar);

        Button label = (Button) findViewById(R.id.mt_label);

        ListView lv = (ListView) findViewById(R.id.mt_tuition_list);
        lvAdapter = new LVAdapter(this, R.layout.tuition_card, tuitions);
        lv.setAdapter(lvAdapter);
        if (from == 0) {
            getMyTuitionDetails();
            label.setText("My Tuitions");
            to = 1;
        } else if (from == 1) {
            getMyTutors();
            label.setText("My Tutors");
            to = 1;
            to = 2;
        } else if (from == 2) {
            getMyRequests();
            label.setText("Request Sent");
            to = 3;
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShowTuitionRequest.class);
                intent.putExtra("tuitionId", tuitions.get(i).getTuitionId());
                intent.putExtra("from", to);
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

    //my tuition
    private void getMyTuitionDetails() {
        String url = DB.SERVER + "Test/include/324/get_accepted_tuitions.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("volley", response);
                        try {
                            Log.d(TAG, response);
                            JSONObject jo = new JSONObject(response);
                            JSONArray ja = jo.getJSONArray("tuitions");
                            tuitions.clear();
                            for (int i = 0; i < ja.length(); i++) {
                                Log.d(TAG, ja.getJSONObject(i).getString("studentname"));
                                TuitionItem t = new TuitionItem();
                                t.setName(ja.getJSONObject(i).getString("studentname"));
                                t.setSubjects(ja.getJSONObject(i).getString("subjects"));
                                t.setAddress(ja.getJSONObject(i).getString("address"));
                                t.setAclev(ja.getJSONObject(i).getString("aclev"));
                                t.setTuitionId(ja.getJSONObject(i).getInt("tuitionid"));
                                tuitions.add(t);
                            }
                            lvAdapter.notifyDataSetChanged();

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
                params.put("username", String.valueOf(Database.getCurrentUsername()));

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    //my tutors
    private void getMyTutors() {
        String url = DB.SERVER + "Test/include/324/get_approved_tuitions.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("volley", response);
                        try {
                            Log.d(TAG, response);
                            JSONObject jo = new JSONObject(response);
                            JSONArray ja = jo.getJSONArray("tuitions");
                            tuitions.clear();
                            for (int i = 0; i < ja.length(); i++) {
                                Log.d(TAG, ja.getJSONObject(i).getString("tutorname"));
                                TuitionItem t = new TuitionItem();
                                t.setName(ja.getJSONObject(i).getString("tutorname"));
                                t.setSubjects(ja.getJSONObject(i).getString("subjects"));
                                t.setAddress(ja.getJSONObject(i).getString("address"));
                                t.setAclev(ja.getJSONObject(i).getString("aclev"));
                                t.setTuitionId(ja.getJSONObject(i).getInt("tuitionid"));
                                tuitions.add(t);
                            }
                            lvAdapter.notifyDataSetChanged();

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
                params.put("username", String.valueOf(Database.getCurrentUsername()));

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    //my sent requests
    private void getMyRequests() {
        String url = DB.SERVER + "Test/include/324/get_sent_requests.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("volley", response);
                        try {
                            Log.d(TAG, response);
                            JSONObject jo = new JSONObject(response);
                            JSONArray ja = jo.getJSONArray("tuitions");
                            tuitions.clear();
                            for (int i = 0; i < ja.length(); i++) {
                                Log.d(TAG, ja.getJSONObject(i).getString("tutorname"));
                                TuitionItem t = new TuitionItem();
                                t.setName(ja.getJSONObject(i).getString("tutorname"));
                                t.setSubjects(ja.getJSONObject(i).getString("subjects"));
                                t.setAddress(ja.getJSONObject(i).getString("address"));
                                t.setAclev(ja.getJSONObject(i).getString("aclev"));
                                t.setTuitionId(ja.getJSONObject(i).getInt("tuitionid"));
                                tuitions.add(t);
                            }
                            lvAdapter.notifyDataSetChanged();

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
                params.put("username", String.valueOf(Database.getCurrentUsername()));

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
