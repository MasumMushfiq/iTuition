package model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.AppController;

/**
 * Created by mushfiq on 1/2/18.
 */

public class DB {
    public static final String SERVER = "http://192.168.0.102/";
    private final static String TAG = "Mushfiq_DB";
    public static final Map<String, String> subjects = new HashMap<>();
    public static final Map<String, String> locations = new HashMap<>();
    public static final Map<String, String> academicLevel = new HashMap<>();
    public static final Map<String, String> gender = new HashMap<>();
    public static final Map<String, String> departments = new HashMap<>();
    public static final Map<String, String> institutes = new HashMap<>();

    private static String[] ignoreArray = { "tutor", "students", "student", "near", "below", "under", "from", "tutors","", "for", "salary" };
    public static final List<String> ignore = new ArrayList<>(Arrays.asList(ignoreArray));

    private static DB instance;

    private DB() {
        String url = SERVER +  "Test/include/324/get_unique.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            prepareListData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                });

        AppController.getInstance().addToRequestQueue(request);
    }

    public static DB getInstance(){ return instance == null ? new DB() : instance; }

    private static void prepareListData(String response) throws JSONException {
        Log.d(TAG, "Hello prepare list");
        JSONObject jsonObject = new JSONObject(response);
        JSONArray subs = jsonObject.getJSONArray("subjects");
        JSONArray locs = jsonObject.getJSONArray("locations");
        JSONArray al = jsonObject.getJSONArray("academic_levels");
        JSONArray depts = jsonObject.getJSONArray("departments");
        JSONArray inst = jsonObject.getJSONArray("institutes");

        // Adding child data
        for (int i = 0; i < subs.length(); i++) {
            subjects.put(subs.getString(i).toUpperCase(), subs.getString(i));
        }
        //subjects.addAll(Database.subjectSet);

        for (int i = 0; i < locs.length(); i++) {
            locations.put(locs.getString(i).toUpperCase(),locs.getString(i));
        }
        //locations.addAll(Database.locationSet);

        for (int i = 0; i < al.length(); i++) {
            academicLevel.put(al.getString(i).toUpperCase(), al.getString(i));
        }
        //academicLevel.addAll(Database.academicLevelSet);

        gender.put("MALE", "Male");
        gender.put("FEMALE", "Female");

        for (int i = 0; i < depts.length(); i++) {
            departments.put(depts.getString(i).toUpperCase(), depts.getString(i));
        }

        for (int i = 0; i < inst.length(); i++) {
            institutes.put(inst.getString(i).toUpperCase(), inst.getString(i));
        }

    }

    public static Person getPersonFromJSON(JSONObject jo) throws JSONException {
        Person p = new Person();
        p.setUsername(jo.getString("username"));
        p.setName(jo.getString("name"));
        p.setAcBd(jo.getString("qualifications"));
        p.setRating(jo.getDouble("rating"));
        int sal = jo.getInt("salary");
        sal = sal / 10;
        sal = sal * 10;
        p.setSalaryVal(sal);
        //p.setSalary("Avg. Salary: " + String.valueOf(sal) + "/day");

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
    }

    public void cleanDB() {
        subjects.clear();
        locations.clear();
        academicLevel.clear();
        gender.clear();
        departments.clear();
        institutes.clear();
    }
}
