package model;

/**
 * Created by mushfiq on 12/30/17.
 */



public class TuitionRequest {
    private enum TuitionStatus {
        PENDING,
        SEEN,
        APPROVED,
        REJECTED
    }

    private String tutorUsername;
    private String studentUsername;
    private String subjects;
    private String address;
    private int no_of_students;
    private int no_of_days;
    private int salary;
    private TuitionStatus status;

    public TuitionRequest(String tutorUsername, String studentUsername,
                          String subjects, String address, int no_of_students,
                          int no_of_days, int salary) {
        this.tutorUsername = tutorUsername;
        this.studentUsername = studentUsername;
        this.subjects = subjects;
        this.address = address;
        this.no_of_students = no_of_students;
        this.no_of_days = no_of_days;
        this.salary = salary;
        status = TuitionStatus.PENDING;
    }

    public TuitionStatus getStatus() {
        return status;
    }

    public void setStatus(TuitionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TuitionRequest{" +
                "tutorUsername='" + tutorUsername + '\'' +
                ", studentUsername='" + studentUsername + '\'' +
                ", subjects='" + subjects + '\'' +
                ", address='" + address + '\'' +
                ", no_of_students=" + no_of_students +
                ", no_of_days=" + no_of_days +
                ", salary=" + salary +
                '}';
    }
}
