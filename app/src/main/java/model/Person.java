package model;

import java.util.Comparator;

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
    private int salaryVal;

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

    public void setSalaryVal(int salaryVal) {
        this.salaryVal = salaryVal;
    }

    public int getSalaryVal() {
        return salaryVal;
    }

    public String getSalary() {
        return "Salary: " + String.valueOf(salaryVal) + "/day";
    }
   /* public static Comparator<Student> StuNameComparator = new Comparator<Student>() {

        public int compare(Student s1, Student s2) {
            String StudentName1 = s1.getStudentname().toUpperCase();
            String StudentName2 = s2.getStudentname().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };*/

    public static Comparator<Person> PersonRatingComparator = new Comparator<Person>() {
        @Override
        public int compare(Person person, Person otherPerson) {
            if (person.getRating() > otherPerson.getRating()) {
                return -1;
            } else if (person.getRating() < otherPerson.getRating()) {
                return 1;
            } else {
                return person.salaryVal - otherPerson.salaryVal;
            }
        }
    };

    public static Comparator<Person> PersonSalaryComparator = new Comparator<Person>() {
        @Override
        public int compare(Person person, Person t1) {
            if (person.salaryVal > t1.salaryVal) {
                return 1;
            } else if (person.salaryVal < t1.salaryVal) {
                return -1;
            } else {
                if (person.getRating() > t1.getRating()) {
                    return -1;
                } else if (person.getRating() < t1.getRating()) {
                    return 1;
                }
                return 0;
            }
        }
    };

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", acBd='" + acBd + '\'' +
                ", locations='" + locations + '\'' +
                ", subjects='" + subjects + '\'' +
                ", rating=" + rating + '\'' +
                ", salary=" + String.valueOf(salaryVal) +
                '}';
    }
}
