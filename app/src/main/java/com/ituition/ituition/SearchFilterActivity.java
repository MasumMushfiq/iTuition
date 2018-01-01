package com.ituition.ituition;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapters.ELVAdapter;
import Model.Database;

public class SearchFilterActivity extends AppCompatActivity {
    ELVAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_filter_toolbar);
        setSupportActionBar(toolbar);


        expListView = (ExpandableListView) findViewById(R.id.elv_SearchFilter);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fab_submit_filter_req);
        final TextView textView = (TextView) findViewById(R.id.textView);

        prepareListData();

        listAdapter = new ELVAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        //expListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // ListView Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ListView Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // ListView on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StringBuilder res = new StringBuilder();
                for (int mGroupPosition = 0; mGroupPosition < listAdapter.getGroupCount(); mGroupPosition++) {
                    ArrayList<String> s = listAdapter.getCheckedItems(mGroupPosition);
                    for (String x : s ) {
                        res.append(x).append(" ");
                    }
                }
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("query", res.toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding header data
        listDataHeader.add("Subjects");
        listDataHeader.add("Locations");
        listDataHeader.add("Academic Levels");

        // Adding child data
        List<String> subjects = new ArrayList<>();
        subjects.addAll(Database.subjectSet);

        List<String> locations = new ArrayList<>();
        locations.addAll(Database.locationSet);

        List<String> academicLevel = new ArrayList<>();
        academicLevel.addAll(Database.academicLevelSet);


        listDataChild.put(listDataHeader.get(0), subjects); // Header, Child data
        listDataChild.put(listDataHeader.get(1), locations);
        listDataChild.put(listDataHeader.get(2), academicLevel);
    }
}
