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

import Adapters.RVAdapter;
import Model.Database;
import Model.Location;
import Model.Person;
import Model.SubSpec;

public class SearchActivity extends AppCompatActivity {
    String query = "";
    RecyclerView rv;
    Button btn_filter;
    RVAdapter adapter;
    LinearLayoutManager llm;
    ArrayList<Person> resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        resultData = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            query = (String) bundle.get("query");
            processQuery(query);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        rv = (RecyclerView) findViewById(R.id.rv_search_result);
        btn_filter = (Button) findViewById(R.id.btn_filter);

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
                if (entry.getValue().getSubjectsAsString().toUpperCase().contains(s.toUpperCase())){
                    users.add(entry.getKey());
                }
            }
        }
        for (String s : locs) {
            for (Map.Entry<String, Location> entry: Database.locations.entrySet()){
                if (entry.getValue().getLocationAsString().toUpperCase().contains(s.toUpperCase())){
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
    }

}
