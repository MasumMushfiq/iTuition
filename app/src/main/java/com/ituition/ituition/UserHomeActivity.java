package com.ituition.ituition;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ituition.ituition.fragments.LatestReviewsFragment;
import com.ituition.ituition.fragments.PopularTutorsFragment;

import adapters.VPAdapter;

public class UserHomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TextView textCartItemCount;
    private int mCartItemCount = 5;

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

        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
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


        final View notificaitons = menu.findItem(R.id.action_notification).getActionView();

        textCartItemCount = (TextView) notificaitons.findViewById(R.id.txtCount);
        updateHotCount(mCartItemCount++);
        textCartItemCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHotCount(mCartItemCount++);
            }
        });
        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    TODO
            }
        });

        return true;
    }

    public void updateHotCount(final int new_hot_number) {
        mCartItemCount = new_hot_number;
        if (mCartItemCount < 0) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCartItemCount == 0)
                    textCartItemCount.setVisibility(View.GONE);
                else {
                    textCartItemCount.setVisibility(View.VISIBLE);
                    textCartItemCount.setText(Integer.toString(mCartItemCount));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_search:
                /*intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);*/
                return true;
            case R.id.action_notification:

                return false;
            case R.id.action_account:
                intent = new Intent(UserHomeActivity.this, ProfileActivity.class);
                intent.putExtra("activity", 1);
                startActivity(intent);
                return true;
            case R.id.logout:
                intent = new Intent(UserHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }

}

