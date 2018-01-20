package model;

/**
 * Created by mushfiq on 1/19/18.
 */

public class TuitionItem {
    int tuitionId;
    String name;
    String subjects;
    String address;
    String aclev;

    public TuitionItem() {
    }

    public TuitionItem(int tuitionId, String name, String subjects, String address, String aclev) {
        this.tuitionId = tuitionId;
        this.name = name;
        this.subjects = subjects;
        this.address = address;
        this.aclev = aclev;
    }

    public int getTuitionId() {
        return tuitionId;
    }

    public void setTuitionId(int tuitionId) {
        this.tuitionId = tuitionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAclev() {
        return aclev;
    }

    public void setAclev(String aclev) {
        this.aclev = aclev;
    }
}
