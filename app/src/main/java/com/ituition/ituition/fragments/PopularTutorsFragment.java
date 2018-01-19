package com.ituition.ituition.fragments;

import android.os.AsyncTask;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ituition.ituition.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapters.RVAdapter;

import java.util.ArrayList;

import app.AppController;
import model.DB;
import model.Person;

/**
 * Created by mushfiq on 12/14/17.
 */

public class PopularTutorsFragment extends Fragment {
    final ArrayList<Person> popularTutors = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    RVAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

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
        getPopTutorsData();

        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        adapter = new RVAdapter(popularTutors);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getPopTutorsData() {
        String url = DB.SERVER + "Test/include/324/get_popular_tutors.php";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            Log.d("Mushfiq_ptf", response);
                            JSONArray users = jo.getJSONArray("users");
                            for (int i = 0; i < users.length(); i++) {
                                popularTutors.add(DB.getPersonFromJSON(users.getJSONObject(i)));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Mushfiq_ptf", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}
