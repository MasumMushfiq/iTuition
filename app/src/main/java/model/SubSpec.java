package model;

import java.util.ArrayList;

/**
 * Created by mushfiq on 11/18/17.
 */

public class SubSpec {
    private String userName;
    private ArrayList<String> subSpec;

    public SubSpec(String userName) {
        this.userName = userName;
        subSpec = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getSubSpec() {
        return subSpec;
    }

    public void setSubSpec(ArrayList<String> subSpec) {
        this.subSpec = subSpec;
    }

    public boolean addSubSpec(String s) {
        return subSpec.add(s);
    }

    public String getSubjectsAsString() {
        StringBuilder sb = new StringBuilder("Subjects: ");
        for (String s :
                subSpec) {
            sb.append(s).append(", ");
        }
        String s = sb.toString();
        s = s.substring(0, s.length() - 2);
        return s;
    }

    public boolean contains(String subject) {
        return subSpec.contains(subject);
    }
}
