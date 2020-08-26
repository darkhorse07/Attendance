package com.example.attendance;

import java.util.ArrayList;
import java.util.Date;

public class COURSE {

    String courseId;
    String courseName;
    String batch;
    String teacherId;
    String QRCode;
    double lat;
    double lng;
    Date currentDate;
    ArrayList<Date> totalDates;

    public COURSE() {

    }

    public COURSE(String courseId, String courseName, String batch, String teacherId, String QRCode, double lat, double lng, Date currentDate, ArrayList<Date> totalDates) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.batch = batch;
        this.teacherId = teacherId;
        this.QRCode = QRCode;
        this.lat = lat;
        this.lng = lng;
        this.currentDate = currentDate;
        this.totalDates = totalDates;
        totalDates.add(new Date(0));
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public ArrayList<Date> getTotalDates() {
        return totalDates;
    }

    public void setTotalDates(ArrayList<Date> totalDates) {
        this.totalDates = totalDates;
    }
}
