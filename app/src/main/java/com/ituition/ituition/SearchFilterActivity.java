package com.ituition.ituition;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.ELVAdapter;
import model.DB;

public class SearchFilterActivity extends AppCompatActivity {
    ELVAdapter listAdapter;
    ExpandableListView expListView;
    SeekBar nos;
    TextView nos_progress;
    int nos_value = 1;
    TextView salary;

    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    List<String> subjects;
    List<String> locations;
    List<String> academicLevel;
    List<String> gender;
    List<String> dept;
    List<String> institutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_filter_toolbar);
        setSupportActionBar(toolbar);


        expListView = (ExpandableListView) findViewById(R.id.elv_SearchFilter);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fab_submit_filter_req);
        nos = (SeekBar) findViewById(R.id.my_seekbar);
        nos_progress = (TextView) findViewById(R.id.my_seekbar_value);
        salary = (TextView) findViewById(R.id.sf_salary_field);


        setupListHeaders();

        setupExpandableListView();
        setupSeekBar();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);

                StringBuilder res = new StringBuilder();

                //getting the subs
                ArrayList<String> s = listAdapter.getCheckedItems(0);
                for (String x : s) {
                    res.append(x).append("#@#");
                }
                intent.putExtra("subjects", res.toString());

                //getting locations
                res = new StringBuilder();
                s = listAdapter.getCheckedItems(1);
                for (String x : s) {
                    res.append(x).append("#@#");
                }
                intent.putExtra("locations", res.toString());

                //getting academic levels
                res = new StringBuilder();
                s = listAdapter.getCheckedItems(2);
                for (String x : s) {
                    res.append(x).append("#@#");
                }
                intent.putExtra("ac_levels", res.toString());

                //getting gender
                res = new StringBuilder();
                s = listAdapter.getCheckedItems(3);
                for (String x : s) {
                    res.append(x).append("#@#");
                }
                intent.putExtra("genders", res.toString());

                res = new StringBuilder();
                s = listAdapter.getCheckedItems(4);
                for (String x:s){
                    res.append(x).append("#@#");
                }
                intent.putExtra("depts", res.toString());

                res = new StringBuilder();
                s = listAdapter.getCheckedItems(5);
                for (String x: s) {
                    res.append(x).append("#@#");
                }
                intent.putExtra("institutes", res.toString());

                intent.putExtra("nos", nos_value);


                intent.putExtra("salary", getSalary());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_search_sm:
                /*intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);*/
                return true;
            case R.id.action_account_sm:
                intent = new Intent(SearchFilterActivity.this, ProfileActivity.class);
                intent.putExtra("activity", 1);
                startActivity(intent);
                return true;
            case R.id.logout_sm:
                intent = new Intent(SearchFilterActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }

    private void setupListHeaders(){
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding header data
        listDataHeader.add("Subjects");
        listDataHeader.add("Locations");
        listDataHeader.add("Academic Levels");
        listDataHeader.add("Gender");
        listDataHeader.add("Departments");
        listDataHeader.add("Institutes");

        subjects = new ArrayList<>(DB.subjects.values());
        locations = new ArrayList<>(DB.locations.values());
        academicLevel = new ArrayList<>(DB.academicLevel.values());
        gender = new ArrayList<>(DB.gender.values());
        dept = new ArrayList<>(DB.departments.values());
        institutes = new ArrayList<>(DB.institutes.values());

        listDataChild.put(listDataHeader.get(0), subjects); // Header, Child data
        listDataChild.put(listDataHeader.get(1), locations);
        listDataChild.put(listDataHeader.get(2), academicLevel);
        listDataChild.put(listDataHeader.get(3), gender);
        listDataChild.put(listDataHeader.get(4), dept);
        listDataChild.put(listDataHeader.get(5), institutes);

    }

    private void setupExpandableListView() {

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
               /* Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        // ListView Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

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
    }

    private void setupSeekBar() {
        nos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nos_progress.setText(String.valueOf(progress + 1));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                nos_value = seekBar.getProgress() + 1;
                /*Toast.makeText(getApplicationContext(), "Seek bar progress is :" + nos_value,
                        Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    private int getSalary() {
        String s = salary.getText().toString();
        int val = 0;

        try {
            val = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            val = 2500;
        }

        return val;
    }
}
