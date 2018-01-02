package com.ituition.ituition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import adapters.RVAdapter;
import model.Database;
import model.Location;
import model.Person;
import model.SubSpec;

public class SearchActivity extends AppCompatActivity {
    final String TAG = "Mushfiq_SA";

    String query = "";
    String subjectQuery = "";
    String locationQuery = "";
    String genderQuery = "";
    String ac_levelQuery = "";
    int nosQuery = 0;
    int salaryQuery = 0;

    RecyclerView rv;
    Button btn_filter;
    Button display_query;
    RVAdapter adapter;
    LinearLayoutManager llm;
    ArrayList<Person> resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rv = (RecyclerView) findViewById(R.id.rv_search_result);
        btn_filter = (Button) findViewById(R.id.btn_filter);
        display_query = (Button) findViewById(R.id.btn_query_display);

        resultData = new ArrayList<>();

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
                salaryQuery = bundle.getInt("salary");
                nosQuery = bundle.getInt("nos");
            }
        }

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
        String s = String.format("Query: \"%s\"   No. of Results: %d", query, resultData.size());
        display_query.setText(s);
    }

}
