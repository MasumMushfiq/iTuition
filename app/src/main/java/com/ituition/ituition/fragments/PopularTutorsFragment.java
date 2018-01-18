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
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import app.AppController;
import model.DB;
import model.Database;
import model.Person;
import model.User;

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
        new GetPopularTutors().execute();

        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        adapter = new RVAdapter(popularTutors);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /*Person getPersonFromJSON(JSONObject jo) throws JSONException {
        Person p = new Person();
        p.setUsername(jo.getString("username"));
        p.setName(jo.getString("name"));
        p.setAcBd(jo.getString("qualifications"));
        p.setRating(jo.getDouble("rating"));

        StringBuilder subs = new StringBuilder("Subjects: ");
        JSONArray ja = jo.getJSONArray("subjects");
//        ja = ja.getJSONArray(0);
        for (int i = 0; i < ja.length(); i++){
            subs.append(ja.getString(i)).append(", ");
        }
        String s = subs.toString();
        s = s.substring(0, s.length() - 2);
        p.setSubjects(s);

        StringBuilder locs = new StringBuilder("Locations: ");
        ja = jo.getJSONArray("locations");
//        ja = ja.getJSONArray(0);
        for (int i = 0; i < ja.length(); i++){
            locs.append(ja.getString(i)).append(", ");
        }
        s = locs.toString();
        s = s.substring(0, s.length() - 2);
        p.setLocations(s);

        return p;
    }*/

    private class GetPopularTutors extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
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
            /*
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://api.androidhive.info/contacts/";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = null;
                        try {
                            c = contacts.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

         */   return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
