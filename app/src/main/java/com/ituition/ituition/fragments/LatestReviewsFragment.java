package com.ituition.ituition.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ituition.ituition.R;
import adapters.RVAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import model.Database;
import model.Person;
import model.User;

/**
 * Created by mushfiq on 12/14/17.
 */

public class LatestReviewsFragment extends Fragment {
    ArrayList<Person> latestReviewedTutors;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    RVAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        initDataset();
    }

    private void initDataset() {
        latestReviewedTutors = new ArrayList<>();
        int i = 0;
        for (HashMap.Entry<String, User> entry: Database.users.entrySet()){
            if (i++ == 5)
                break;
            latestReviewedTutors.add(new Person(entry.getKey()));
        }
        Collections.shuffle(latestReviewedTutors);
        Log.d("MyRV", "Latest Reviewed tutors size" + latestReviewedTutors.size());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.latest_reviews_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.latest_reviews_rv);

        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        adapter = new RVAdapter(latestReviewedTutors);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
