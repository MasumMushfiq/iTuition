package com.ituition.ituition.Fragments;

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
import com.ituition.ituition.Adapters.RVAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Model.Database;
import Model.Person;
import Model.User;

/**
 * Created by mushfiq on 12/14/17.
 */

public class TutorsNearYouFragment extends Fragment {
    ArrayList<Person> tutorsNearU;
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
        tutorsNearU = new ArrayList<>();
        for (HashMap.Entry<String, User> entry: Database.users.entrySet()){
            tutorsNearU.add(new Person(entry.getKey()));
        }
        Collections.shuffle(tutorsNearU);
        Log.d("MyRV", "Tutors near you size" + tutorsNearU.size());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tutors_near_you_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.tutors_near_you_rv);

        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        adapter = new RVAdapter(tutorsNearU);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
