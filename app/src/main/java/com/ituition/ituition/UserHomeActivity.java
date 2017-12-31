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

import com.ituition.ituition.Fragments.LatestReviewsFragment;
import com.ituition.ituition.Fragments.PopularTutorsFragment;

import Adapters.VPAdapter;

public class UserHomeActivity extends AppCompatActivity {
    private ViewPager viewPager;

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

        /*SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_search:
                /*intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);*/
                return true;
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