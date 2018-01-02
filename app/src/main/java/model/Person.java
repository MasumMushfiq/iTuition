package model;

/**
 * Created by tahmid on 12/5/17.
 */

public class Person {
    private String username;
    private String name;
    private String acBd;
    private String locations;
    private String subjects;
    private double rating;

    public Person() {
    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAcBd(String acBd) {
        this.acBd = acBd;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public void setRating(double rating) {
        this.rating = rating;
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

    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", acBd='" + acBd + '\'' +
                ", locations='" + locations + '\'' +
                ", subjects='" + subjects + '\'' +
                ", rating=" + rating +
                '}';
    }
}
