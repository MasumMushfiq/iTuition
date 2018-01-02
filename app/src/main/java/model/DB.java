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
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.103/Test/include/324/get_unique.php",
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
}
