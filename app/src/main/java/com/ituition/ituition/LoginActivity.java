package com.ituition.ituition;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.AppController;
import model.AcPreferences;
import model.DB;
import model.Database;
import model.Location;
import model.SubSpec;
import model.User;

public class LoginActivity extends AppCompatActivity {
    final String TAG = "Mushfiq_LA";
    ArrayList<LatLng> latLngs = new ArrayList<>();
    Button signInBtn;
    Button registerBtn;
    TextView userNameField;
    TextView passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadDatabase();

        DB.getInstance();


        signInBtn = (Button) findViewById(R.id.signInBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        userNameField = (TextView) findViewById(R.id.userNameField);
        passwordField = (TextView) findViewById(R.id.passwordField);


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameField.getText().toString();
                String password = passwordField.getText().toString();
                verifyLogin(userName, password);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }

    private void loadDatabase() {
        new Thread(new Runnable() {
            public void run() {
                loadUsers();
                loadAcPreferences();
                loadSubSpec();
                loadLocations();
                /*for (String s : Database.locationSet) {
                    LatLng latLng = getLocationFromAddress(getApplicationContext(), s + ", Dhaka, Bangladesh");
                    latLngs.add(latLng);
                }
                for (int i = 0; i < latLngs.size() ; i++){
                    for (int j = i+1; j < latLngs.size(); j++) {
                        float results[] = new float[1];
                        android.location.Location.distanceBetween(
                                latLngs.get(i).latitude, latLngs.get(i).longitude, latLngs.get(j).latitude, latLngs.get(j).longitude, results);
                        Log.d("Mushfiq_l", String.valueOf(results[0]));
                    }
                }*/
            }
        }).start();
    }

    private void loadUsers() {
        Database.users.clear();
        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("users", "raw", getPackageName()));

        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receivedString = "";

                bufferedReader.readLine();
                while ((receivedString = bufferedReader.readLine()) != null) {
                    User user = new User(receivedString);
                    Database.users.put(user.getUserName(), user);
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAcPreferences() {
        Database.acPreferences.clear();
        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("academic_levels", "raw", getPackageName()));

        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String input = "";
                bufferedReader.readLine();
                while ((input = bufferedReader.readLine()) != null) {
                    //System.out.println(receiveString);
                    String data[] = input.split(",");
                    if (Database.acPreferences.containsKey(data[0])) {
                        Database.acPreferences.get(data[0]).addPref(data[1]);
                    } else {
                        AcPreferences acPreferences = new AcPreferences(data[0]);
                        acPreferences.addPref(data[1]);
                        Database.acPreferences.put(data[0], acPreferences);
                    }
                    Database.academicLevelSet.add(data[1]);
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSubSpec() {
        Database.subSpecs.clear();
        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("subjects", "raw", getPackageName()));

        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String input = "";
                bufferedReader.readLine();
                while ((input = bufferedReader.readLine()) != null) {
                    //System.out.println(receiveString);
                    String data[] = input.split(",");
                    if (Database.subSpecs.containsKey(data[0])) {
                        Database.subSpecs.get(data[0]).addSubSpec(data[1]);
                    } else {
                        SubSpec subSpec = new SubSpec(data[0]);
                        subSpec.addSubSpec(data[1]);
                        Database.subSpecs.put(data[0], subSpec);
                    }
                    Database.subjectSet.add(data[1]);
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocations() {
        Database.locations.clear();
        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("locations", "raw", getPackageName()));

        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String input = "";
                bufferedReader.readLine();
                while ((input = bufferedReader.readLine()) != null) {
                    //System.out.println(receiveString);
                    String data[] = input.split(",");
                    if (Database.locations.containsKey(data[0])) {
                        Database.locations.get(data[0]).addLocation(data[1]);
                    } else {
                        Location location = new Location(data[0]);
                        location.addLocation(data[1]);
                        Database.locations.put(data[0], location);
                    }
                    Database.locationSet.add(data[1]);
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void verifyLogin(final String userName, final String password) {
        final int[] result1 = {-1};
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.103/Test/include/324/verify_login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("volley", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            result1[0] = object.getInt("success");
                            int result = result1[0];
                            if (result == 1) {
                                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                                Database.setCurrentUsername(userName);
                                startActivity(intent);
                            } else if (result == 2) {
                                //wrong password
                                Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                            } else if (result == 3) {
                                //username is not registered
                                Toast.makeText(getApplicationContext(), "Invalid username", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volleyError", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", password);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}
