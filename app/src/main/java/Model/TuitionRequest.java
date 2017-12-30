package Model;

/**
 * Created by mushfiq on 12/30/17.
 */

public class TuitionRequest {
    String tutorUsername;
    String studentUsername;
    String subjects;
    String address;
    int no_of_students;
    int salary;

    public TuitionRequest(String subjects, String address, int no_of_students, int salary) {
        this.subjects = subjects;
        this.address = address;
        this.no_of_students = no_of_students;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "TuitionRequest{" +
                "subjects='" + subjects + '\'' +
                ", address='" + address + '\'' +
                ", no_of_students=" + no_of_students +
                ", salary=" + salary +
                '}';
    }
}
