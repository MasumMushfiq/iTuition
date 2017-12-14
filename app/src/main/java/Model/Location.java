package Model;

import java.util.ArrayList;

/**
 * Created by mushfiq on 11/18/17.
 */

public class Location {
    String userName;
    ArrayList<String> locations;

    public Location(String userName) {
        this.userName = userName;
        locations = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
    }

    public boolean addLocation(String location) {
        return locations.add(location);
    }

    public String getLocationAsString() {
        StringBuilder sb = new StringBuilder("Locations: ");
        for (String s :
                locations) {
            sb.append(s).append(", ");
        }
        String s = sb.toString();
        s = s.substring(0, s.length() - 1);
        return s;
    }
}
