package model;

/**
 * Created by mushfiq on 11/17/17.
 */

public class User {
    private String userName;
    private String name;
    private String password;
    private String contactNo;
    private String email;
    private String academicBackground;
    private String gender;

    private double rating;
    private int oneSalary, twoSalary, threeSalary, moreSalary;

    public User() {   }

    public User(String userName, String name, String password,
                String contactNo, String email, String academicBackground, String gender, float rating) {
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.contactNo = contactNo;
        this.email = email;
        this.academicBackground = academicBackground;
        this.gender = gender;
        this.rating = rating;
    }

    public User(String data) {
        String array[] = data.split(",");
        userName = array[0];
        password = array[1];
        name = array[2];
        contactNo = array[3];
        email = array[4];
        academicBackground = array[5];
        gender = array[6];
        rating = Float.parseFloat(array[7]) / 10;
        oneSalary = (int) Double.parseDouble(array[8]);
        twoSalary = (int) Double.parseDouble(array[9]);
        threeSalary = (int) Double.parseDouble(array[10]);
        moreSalary = (int) Double.parseDouble(array[11]);
        //Log.d("Salary", oneSalary + " " + twoSalary + " " + threeSalary + " " + moreSalary);
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcademicBackground() {
        return academicBackground;
    }

    public void setAcademicBackground(String academicBackground) {
        this.academicBackground = academicBackground;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public int getOneSalary() {
        return oneSalary;
    }

    public int getTwoSalary() {
        return twoSalary;
    }

    public int getThreeSalary() {
        return threeSalary;
    }

    public int getMoreSalary() {
        return moreSalary;
    }

    @Override
    public String toString() {
        return "User {" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", email='" + email + '\'' +
                ", academicBackground='" + academicBackground + '\'' +
                ", gender='" + gender + '\'' +
                ", rating=" + rating +
                '}';
    }
}
