package com.example.attendance;

import java.util.ArrayList;
import java.util.Date;

public class STUDENT_RECORD_LIST {

    String studentName;
    String studentId;
    ArrayList<Date> presentDates;

    public STUDENT_RECORD_LIST() {

    }

    public STUDENT_RECORD_LIST(String studentName, String studentId, ArrayList<Date> presentDates) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.presentDates = presentDates;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public ArrayList<Date> getPresentDates() {
        return presentDates;
    }

    public void setPresentDates(ArrayList<Date> presentDates) {
        this.presentDates = presentDates;
    }
}
