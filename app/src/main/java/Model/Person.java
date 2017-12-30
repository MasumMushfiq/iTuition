package Model;

/**
 * Created by tahmid on 12/5/17.
 */

public class Person {
    private String username;
    private String name;
    private String acBd;
    private String locations;
    private String subjects;
    private float rating;

    public Person(String username) {
        this.username = username;
        User user = Database.users.get(username);
        name = user.getName();
        acBd = user.getAcademicBackground();
        Location l = Database.locations.get(username);
        if (l == null) {
            locations = "Area Not Available";
        } else
            locations = l.getLocationAsString();
        SubSpec s = Database.subSpecs.get(username);
        if (s == null) {
            subjects = "Subject Specialty Not Available";
        } else
            subjects = s.getSubjectsAsString();
        rating = user.getRating();
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getAcBd() {
        return acBd;
    }

    public String getLocations() {
        return locations;
    }

    public String getSubjects() {
        return subjects;
    }

    public float getRating() {
        return rating;
    }
}
