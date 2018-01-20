package com.ituition.ituition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
     * my tuition = 0
     * my tutor = 1
     * my request = 2
     */
    private int from = 0;
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

        ListView lv = (ListView) findViewById(R.id.mt_tuition_list);
        lvAdapter = new LVAdapter(this, R.layout.tuition_card, tuitions);
        lv.setAdapter(lvAdapter);
        if (from == 0) {
            getMyTuitionDetails();
        } else if (from == 1) {

        } else if (from == 2) {

        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShowTuitionRequest.class);
                intent.putExtra("tuitionId", tuitions.get(i).getTuitionId());
                intent.putExtra("from", 1);
                startActivity(intent);
            }
        });
    }

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
}
