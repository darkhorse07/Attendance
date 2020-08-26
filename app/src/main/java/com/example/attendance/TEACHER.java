package com.example.attendance;

import java.util.ArrayList;

public class TEACHER {

    String teacherId;
    String firstName;
    String lastName;
    String email;
    ArrayList<String> courseId;

    public TEACHER () {

    }

    public TEACHER(String teacherId, String firstName, String lastName, String email, ArrayList<String> courseId) {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.courseId = courseId;
        courseId.add("0");
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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
