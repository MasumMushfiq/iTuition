package com.ituition.ituition.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ituition.ituition.R;

import Adapters.RVAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Model.Database;
import Model.Person;
import Model.User;

/**
 * Created by mushfiq on 12/14/17.
 */

public class PopularTutorsFragment extends Fragment {
    ArrayList<Person> popularTutors;
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
        popularTutors = new ArrayList<>();
        final int i = 0;
        for (HashMap.Entry<String, User> entry : Database.users.entrySet()) {
            popularTutors.add(new Person(entry.getKey()));
        }
        Collections.sort(popularTutors, new Comparator<Person>() {
            @Override
            public int compare(Person person, Person t1) {
                if (person.getRating() > t1.getRating())
                    return 1;
                else if (person.getRating() < t1.getRating())
                    return -1;
                return 0;
            }
        });
        Collections.reverse(popularTutors);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_tutors, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.popular_tutors_rv);

        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        adapter = new RVAdapter(popularTutors);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
