package com.ituition.ituition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import adapters.RVAdapter;
import app.AppController;
import model.DB;
import model.Database;
import model.Location;
import model.Person;
import model.SubSpec;
import utilities.QueryCreator;

public class SearchActivity extends AppCompatActivity {
    final String TAG = "Mushfiq_SA";

    //original queries are stored here
    String query = "";
    String subjectQuery = "";
    String locationQuery = "";
    String genderQuery = "";
    String ac_levelQuery = "";
    String deptQuery = "";
    String instQuery = "";
    int nosQuery = -1;
    int salaryQuery = 0;

    RecyclerView rv;
    Button btn_filter;
    Button display_query;
    RVAdapter adapter;
    LinearLayoutManager llm;

    ArrayList<Person> resultData;

    //for parsing query
    ArrayList<String> subjects;
    ArrayList<String> locations;
    ArrayList<String> acLevels;
    ArrayList<String> gender;
    ArrayList<String> departments;
    ArrayList<String> institutes;

    //these are the parameters for building the query
    String[] s, l, a, g, d, i;
    int ns = -1;    // ns is used so that nosquery can be kept unchanged

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rv = (RecyclerView) findViewById(R.id.rv_search_result);
        btn_filter = (Button) findViewById(R.id.btn_filter);
        display_query = (Button) findViewById(R.id.btn_query_display);

        resultData = new ArrayList<>();
        subjects = new ArrayList<>();
        locations = new ArrayList<>();
        acLevels = new ArrayList<>();
        gender = new ArrayList<>();
        departments = new ArrayList<>();
        institutes = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        setupRecyclerView();

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchFilterActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            query = bundle.getString("query");
            if (query != null){
                processQuery(query);
            } else {
                subjectQuery = bundle.getString("subjects");
                locationQuery = bundle.getString("locations");
                ac_levelQuery = bundle.getString("ac_levels");
                genderQuery = bundle.getString("genders");
                deptQuery = bundle.getString("depts");
                instQuery = bundle.getString("institutes");
                salaryQuery = bundle.getInt("salary");
                nosQuery = bundle.getInt("nos");
                processQuery();
            }
            executeQuery();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.action_search_sm).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processQuery(query);
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void setupRecyclerView() {
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        adapter = new RVAdapter(resultData);
        rv.setAdapter(adapter);

        rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void processQuery(String query) {
        query = query.replaceAll(",", " ");
        query = query.replaceAll("#", " ").replaceAll("\\.", "");

        ArrayList<String> tempWords = new ArrayList<>(Arrays.asList(query.split(" ")));
        ArrayList<String> words = new ArrayList<>();

        tempWords.removeAll(DB.ignore);

        for (int i = 0; i < tempWords.size(); i++) {
            words.add(tempWords.get(i).toUpperCase());
        }

        for (String word : words) {
            if (DB.subjects.containsKey(word)){
                subjects.add(word);
            } else if (DB.locations.containsKey(word)){
                locations.add(word);
            } else if (DB.academicLevel.containsKey(word)){
                acLevels.add(word);
            } else if (DB.gender.containsKey(word)) {
                gender.add(word);
            } else if (DB.departments.containsKey(word)){
                departments.add(word);
            } else if (DB.institutes.containsKey(word)){
                institutes.add(word);
            } else if (isInteger(word)){
                salaryQuery = Integer.parseInt(word);
            }
        }

        s = subjects.toArray(new String[subjects.size()]);
        l = locations.toArray(new String[locations.size()]);
        a = acLevels.toArray(new String[acLevels.size()]);
        g = gender.toArray(new String[gender.size()]);
        d = departments.toArray(new String[departments.size()]);
        i = institutes.toArray(new String[institutes.size()]);
        if (salaryQuery == 0)
            nosQuery = -1;
    }

    private void processQuery(){
        s = subjectQuery.isEmpty() ? new String[0] : subjectQuery.split(" ");
        l = locationQuery.isEmpty() ? new String[0] : locationQuery.split(" ");
        a = ac_levelQuery.isEmpty() ? new String[0] : ac_levelQuery.split(" ");
        g = genderQuery.isEmpty() ? new String[0] : genderQuery.split(" ");
        d = deptQuery.isEmpty() ? new String[0] : deptQuery.split(" ");
        i = instQuery.isEmpty() ? new String[0] : instQuery.split(" ");

        if (salaryQuery == 0){
            ns = -1;
        }

    }

    private void executeQuery(){
        final String newQuery = QueryCreator.createQuery(s, l, a, d, i, g, ns, salaryQuery);
        Log.d(TAG, newQuery);

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.103/Test/include/324/general_query.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            Log.d("Mushfiq_sa_or", response);
                            JSONArray users = jo.getJSONArray("users");
                            for (int i = 0; i < users.length(); i++) {
                                resultData.add(getPersonFromJSON(users.getJSONObject(i)));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Mushfiq_sa_oer", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("query", newQuery);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);

        display_query.setText(TAG + newQuery);
    }

    Person getPersonFromJSON(JSONObject jo) throws JSONException {
        Person p = new Person();
        p.setUsername(jo.getString("username"));
        p.setName(jo.getString("name"));
        p.setAcBd(jo.getString("qualifications"));
        p.setRating(jo.getDouble("rating"));

        StringBuilder subs = new StringBuilder("Subjects: ");
        JSONArray ja = jo.getJSONArray("subjects");
//        ja = ja.getJSONArray(0);
        for (int i = 0; i < ja.length(); i++){
            subs.append(ja.getString(i)).append(", ");
        }
        String s = subs.toString();
        s = s.substring(0, s.length() - 2);
        p.setSubjects(s);

        StringBuilder locs = new StringBuilder("Locations: ");
        ja = jo.getJSONArray("locations");
//        ja = ja.getJSONArray(0);
        for (int i = 0; i < ja.length(); i++){
            locs.append(ja.getString(i)).append(", ");
        }
        s = locs.toString();
        s = s.substring(0, s.length() - 2);
        p.setLocations(s);

        return p;
    }


    /*
        String[] words = query.split(" ");
        ArrayList<String> subs = new ArrayList<>();
        ArrayList<String> locs = new ArrayList<>();
        HashSet<String> users = new HashSet<>();
        HashSet<String> users1 = new HashSet<>();
        for (String s : words) {
            if (Database.locationSet.contains(s)) {
                locs.add(s);
            }
            if (Database.subjectSet.contains(s)) {
                subs.add(s);
            }
        }

        for (String s : subs) {
            for (Map.Entry<String, SubSpec> entry: Database.subSpecs.entrySet()){
                if (entry.getValue().getSubjectsAsString().contains(s)){
                    users.add(entry.getKey());
                }
            }
        }
        for (String s : locs) {
            for (Map.Entry<String, Location> entry: Database.locations.entrySet()){
                if (entry.getValue().getLocationAsString().contains(s)){
                    users1.add(entry.getKey());
                }
            }
        }

        if (users.size() > 0 && users1.size() > 0)
            users.retainAll(users1);
        resultData.clear();
        for (String s: users) {
            resultData.add(new Person(s));
        }
        String s = String.format("Query: \"%s\" \nNo. of Results: %d", query, resultData.size());
        display_query.setText(s);*/

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}

