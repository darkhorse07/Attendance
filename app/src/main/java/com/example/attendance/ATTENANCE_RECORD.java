package com.example.attendance;

import java.util.ArrayList;
import java.util.Date;

public class ATTENANCE_RECORD {

    ArrayList<Date> presentDates;
    String studentID;

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public ATTENANCE_RECORD() {

    }

    public ATTENANCE_RECORD(ArrayList<Date> presentDates, String studentID) {
        this.presentDates = presentDates;
        this.studentID = studentID;
        presentDates.add(new Date(0));
    }

    public ArrayList<Date> getPresentDates() {
        return presentDates;
    }

    public void setPresentDates(ArrayList<Date> presentDates) {
        this.presentDates = presentDates;
    }
}
