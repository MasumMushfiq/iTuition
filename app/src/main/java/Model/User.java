package Model;

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

    public User(String userName, String name, String password,
                String contactNo, String email, String academicBackground, String gender) {
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.contactNo = contactNo;
        this.email = email;
        this.academicBackground = academicBackground;
        this.gender = gender;
    }

    public User(String data) {
        String array[] = data.split("@#@");
        userName = array[0];
        name = array[1];
        password = array[2];
        contactNo = array[3];
        academicBackground = array[4];
        email = array[5];
        gender = array[6];
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


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", contactNo='").append(contactNo).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", academicBackground='").append(academicBackground).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
