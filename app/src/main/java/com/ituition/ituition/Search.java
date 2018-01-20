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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import adapters.RVAdapter;
import app.AppController;
import model.DB;
import model.Person;
import utilities.QueryCreator;

public class Search extends AppCompatActivity {
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
    Spinner spinner_sort;
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
    String search_tags = "";
    int ns = -1;    // ns is used so that nosquery can be kept unchanged

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rv = (RecyclerView) findViewById(R.id.rv_search_result);
        btn_filter = (Button) findViewById(R.id.btn_filter);
        display_query = (Button) findViewById(R.id.btn_query_display);
        spinner_sort = (Spinner) findViewById(R.id.spn_sort_result);

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
                Intent intent = new Intent(getApplicationContext(), SearchFilter.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            query = bundle.getString("query");
            if (query != null) {
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

        spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        //sort by rating(default)
                        Collections.sort(resultData, Person.PersonRatingComparator);
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        //sort by salary
                        Collections.sort(resultData, Person.PersonSalaryComparator);
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        //Toast.makeText(parent.getContext(), "Spinner item 3!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.common_menu, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search_sm).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                processQuery(query);
                executeQuery();
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
    private void setupRecyclerView() {
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        adapter = new RVAdapter(resultData);
        rv.setAdapter(adapter);

        rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void processQuery(String query) {
        clearQueryDetails();
        query = query.replaceAll(",", " ");
        query = query.replaceAll("#", " ").replaceAll("\\.", "");

        ArrayList<String> tempWords = new ArrayList<>(Arrays.asList(query.split(" ")));
        ArrayList<String> words = new ArrayList<>();

        tempWords.removeAll(DB.ignore);

        for (int i = 0; i < tempWords.size(); i++) {
            words.add(tempWords.get(i).toUpperCase());
        }

        for (String word : words) {
            if (DB.subjects.containsKey(word)) {
                subjects.add(word);
            } else if (DB.locations.containsKey(word)) {
                locations.add(word);
            } else if (DB.academicLevel.containsKey(word)) {
                acLevels.add(word);
            } else if (DB.gender.containsKey(word)) {
                gender.add(word);
            } else if (DB.departments.containsKey(word)) {
                departments.add(word);
            } else if (DB.institutes.containsKey(word)) {
                institutes.add(word);
            } else if (isInteger(word)) {
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

    private void processQuery() {
        clearQueryDetails();
        s = subjectQuery.isEmpty() ? new String[0] : subjectQuery.split("#@#");
        l = locationQuery.isEmpty() ? new String[0] : locationQuery.split("#@#");
        a = ac_levelQuery.isEmpty() ? new String[0] : ac_levelQuery.split("#@#");
        g = genderQuery.isEmpty() ? new String[0] : genderQuery.split("#@#");
        d = deptQuery.isEmpty() ? new String[0] : deptQuery.split("#@#");
        i = instQuery.isEmpty() ? new String[0] : instQuery.split("#@#");

        if (salaryQuery == 0) {
            ns = -1;
        } else {
            ns = nosQuery;
        }

    }

    private void executeQuery() {
        final String newQuery = QueryCreator.createQuery(s, l, a, d, i, g, ns, salaryQuery);
        Log.d(TAG, newQuery);
        String url = DB.SERVER + "Test/include/324/general_query.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            Log.d("Mushfiq_sa_or", response);
                            JSONArray users = jo.getJSONArray("users");
                            resultData.clear();
                            for (int i = 0; i < users.length(); i++) {
                                resultData.add(DB.getPersonFromJSON(users.getJSONObject(i)));
                            }
                            display_query.setText(display_query.getText().toString() + "\nNumber of Tutors Found: " + resultData.size());
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("query", newQuery);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);

        makeSearchTags();
        display_query.setText(search_tags);
    }

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

    private void clearQueryDetails() {
        subjects.clear();
        locations.clear();
        acLevels.clear();
        gender.clear();
        departments.clear();
        institutes.clear();
    }

    private void makeSearchTags() {
        search_tags = "Search Tags: ";
        for(String ms: s) search_tags += ms + " ";
        for(String ms: l) search_tags += ms + " ";
        for(String ms: a) search_tags += ms + " ";
        for(String ms: g) search_tags += ms + " ";
        for(String ms: d) search_tags += ms + " ";
        for(String ms: i) search_tags += ms + " ";
    }
}

