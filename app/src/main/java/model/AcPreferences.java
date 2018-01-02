package model;

import java.util.ArrayList;

/**
 * Created by mushfiq on 11/17/17.
 */

public class AcPreferences {
    String username;
    ArrayList<String> acPrefs;

    public AcPreferences(String username) {
        this.username = username;
        acPrefs = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getAcPrefs() {
        return acPrefs;
    }

    public void setAcPrefs(ArrayList<String> acPrefs) {
        this.acPrefs = acPrefs;
    }

    public boolean addPref(String s) {
        return acPrefs.add(s);
    }
}
