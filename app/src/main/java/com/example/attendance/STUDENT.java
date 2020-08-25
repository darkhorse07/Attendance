package com.example.attendance;

import java.util.ArrayList;

public class STUDENT {

    String studentId;
    String firstName;
    String lastName;
    String email;
    ArrayList<String> courseId;

    public STUDENT () {

    }

    public STUDENT(String studentId, String firstName, String lastName, String email, ArrayList<String> courseId) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getCourseId() {
        return courseId;
    }

    public void setCourseId(ArrayList<String> courseId) {
        this.courseId = courseId;
    }
}
