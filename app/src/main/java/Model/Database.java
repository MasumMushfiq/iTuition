package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by mushfiq on 11/17/17.
 */

public final class Database {
    private static String currentUsername = "";

    public static HashMap<String, User> users = new HashMap<>();

    public static HashMap<String, AcPreferences> acPreferences = new HashMap<>();

    public static HashMap<String, SubSpec> subSpecs = new HashMap<>();

    public static HashMap<String, Location> locations = new HashMap<>();

    public static HashSet<String> locationSet = new HashSet<>();

    public static HashSet<String> subjectSet = new HashSet<>();

    public static ArrayList<TuitionRequest> tuitionRequests = new ArrayList<>();

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUsername(String currentUsername) {
        Database.currentUsername = currentUsername;
    }
}
