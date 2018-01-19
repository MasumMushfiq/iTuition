package model;

/**
 * Created by mushfiq on 12/30/17.
 */


public class TuitionRequest {
    private int tuitionId;
    private String tutorUsername;
    private String studentUsername;
    private String subjects;
    private String address;
    private String acLev;
    private int no_of_students;
    private int daysPerWeek;
    private int salary;
    private int status;
    private String review;
    private double rating;


    /*
    *   Tuition status
    *
    *   pending = 0
    *   seen = 1
    *   accepted = 2
    *   rejected = 3
    *   confirmation_seen = 4
    *   done = 5
    *
    * */

    public TuitionRequest(String tutorUsername, String studentUsername,
                          String subjects, String address, String acLev, int no_of_students,
                          int daysPerWeek, int salary) {
        this.tutorUsername = tutorUsername;
        this.studentUsername = studentUsername;
        this.subjects = subjects;
        this.address = address;
        this.acLev = acLev;
        this.no_of_students = no_of_students;
        this.daysPerWeek = daysPerWeek;
        this.salary = salary;
        status = 0;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TuitionRequest{" +
                "tuitionId=" + tuitionId +
                ", tutorUsername='" + tutorUsername + '\'' +
                ", studentUsername='" + studentUsername + '\'' +
                ", subjects='" + subjects + '\'' +
                ", address='" + address + '\'' +
                ", no_of_students=" + no_of_students +
                ", daysPerWeek=" + daysPerWeek +
                ", salary=" + salary +
                ", status=" + status +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                '}';
    }

    public int getTuitionId() {
        return tuitionId;
    }

    public String getTutorUsername() {
        return tutorUsername;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public String getSubjects() {
        return subjects;
    }

    public String getAddress() {
        return address;
    }

    public int getNo_of_students() {
        return no_of_students;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public String getAcLev() {
        return acLev;
    }

    public void setAcLev(String acLev) {
        this.acLev = acLev;
    }

    public int getSalary() {
        return salary;
    }

    public String getRequestNotification() {
        return "You have a new request from " + studentUsername;
    }
}
