package com.example.attendance;

import java.util.ArrayList;
import java.util.Date;

public class ATTENANCE_RECORD {

    ArrayList<Date> presentDates;

    public ATTENANCE_RECORD() {

    }

    public ATTENANCE_RECORD(ArrayList<Date> presentDates) {
        this.presentDates = presentDates;
    }

    public ArrayList<Date> getPresentDates() {
        return presentDates;
    }

    public void setPresentDates(ArrayList<Date> presentDates) {
        this.presentDates = presentDates;
    }
}
