package com.ituition.ituition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import Model.Database;
import Model.Person;
import Model.User;

public class UserHomeActivity extends AppCompatActivity {
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userName = (String) bundle.get("username");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Person> users = new ArrayList<>();
        for (HashMap.Entry<String, User> entry: Database.users.entrySet()){
            users.add(new Person(entry.getKey()));
        }
        System.out.println(users.size());
        RVAdapter adapter = new RVAdapter(users);
        recyclerView.setAdapter(adapter);

        Button testCard = (Button)findViewById(R.id.testCard);

        testCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, PersonCard.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_account:
                Intent intent = new Intent(UserHomeActivity.this, ProfileActivity.class);
                intent.putExtra("username", userName);
                startActivity(intent);
                return true;
        }
        return true;
    }
}