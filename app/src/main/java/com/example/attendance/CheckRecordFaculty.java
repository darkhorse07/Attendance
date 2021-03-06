package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class CheckRecordFaculty extends AppCompatActivity {

    Spinner studentSpinner;
    TextView presentTextView;
    TextView absentTextView;
    TextView dateTextView;
    TextView date2TextView;
    ListView recordListView;
    ProgressBar progressBar;

    User user;
    ArrayList<User> userList;

    ArrayList<String> studentList;
    ArrayList<STUDENT_RECORD_LIST> student_record_list;
    ArrayList<Date> totalDates;

    DatabaseReference databaseAttendance;
    DatabaseReference databaseStudent;
    DatabaseReference databaseCourse;

    public class NamesComparator implements Comparator<STUDENT_RECORD_LIST> { // for sorting names

        @Override
        public int compare(STUDENT_RECORD_LIST t1, STUDENT_RECORD_LIST t2) {
            String name1 = t1.getStudentName();
            String name2 = t2.getStudentName();
            return name1.compareTo(name2);
        }
    }

    public class DatesComparator implements Comparator<Date> { // for sorting dates

        @Override
        public int compare(Date t1, Date t2) {
            return t1.compareTo(t2);
        }
    }

    public void showSelectedRecord(int pos) {

        ArrayList<Date> attendedDates = student_record_list.get(pos).getPresentDates();
        Collections.sort(attendedDates, new DatesComparator());

        userList.clear();

        int i = 1, j = 1;
        while(i < totalDates.size()) {

            String date1 = totalDates.get(i).toString();
            String date2 = "";

            for(int k = 0; k < date1.length(); k++) {
                if(date1.charAt(k) == 'G')
                    break;
                else
                    date2 = date2 + date1.charAt(k);
            }

            if(j < attendedDates.size() && totalDates.get(i).compareTo(attendedDates.get(j)) == 1 ) {
                user = new User(date2, "Present");
                j++;
            }
            else {
                user = new User(date2, "Absent");
            }
            i++;
            userList.add(user);
        }

        TwoColumn_ListAdapter adapter = new TwoColumn_ListAdapter(getApplicationContext(), R.layout.list_adapter_view, userList);
        recordListView.setAdapter(adapter);

        int present = attendedDates.size() - 1;
        int absent = totalDates.size() - attendedDates.size();

        presentTextView.setText("Present: " + Integer.toString(present));
        absentTextView.setText("Absent: " + Integer.toString(absent));

        progressBar.setVisibility(View.INVISIBLE);
        dateTextView.setVisibility(View.VISIBLE);
        date2TextView.setVisibility(View.VISIBLE);
        recordListView.setVisibility(View.VISIBLE);
        presentTextView.setVisibility(View.VISIBLE);
        absentTextView.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record_faculty);

        studentList = new ArrayList<String>();
        student_record_list = new ArrayList<STUDENT_RECORD_LIST>();
        totalDates = new ArrayList<Date>();
        userList = new ArrayList<>();

        presentTextView = (TextView) findViewById(R.id.presentTextView);
        absentTextView = (TextView) findViewById(R.id.absentTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        date2TextView = (TextView) findViewById(R.id.dateTextView2);
        recordListView = (ListView) findViewById(R.id.recordListView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar7);

        databaseAttendance = FirebaseDatabase.getInstance().getReference("ATTENDANCE_RECORD").child(FacultyCourse.courseId);
        databaseAttendance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) { // fetching student id and present dates of the students enrolled in the course

                    ATTENANCE_RECORD attenance_record = dataSnapshot.getValue(ATTENANCE_RECORD.class);

                    if(attenance_record != null) {

                        STUDENT_RECORD_LIST temp = new STUDENT_RECORD_LIST();
                        temp.setStudentId(attenance_record.getStudentID());
                        temp.setPresentDates(attenance_record.getPresentDates());
                        student_record_list.add(temp);
                    }
                }

                databaseStudent = FirebaseDatabase.getInstance().getReference("STUDENT");
                databaseStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) { // fetching student name

                            STUDENT student = dataSnapshot.getValue(STUDENT.class);

                            if(student != null) {

                                for(int i = 0; i < student_record_list.size(); i++) {

                                    if(student_record_list.get(i).getStudentId().equals(student.getStudentId())) {

                                        String name = student.getFirstName() + " " + student.getLastName();
                                        student_record_list.get(i).setStudentName(name);
//                                        Log.i("FOUND", "!");
                                    }
                                }
                            }
                        }

                        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
                        databaseCourse.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) { // fetching the dates of total classes

                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    COURSE course = dataSnapshot.getValue(COURSE.class);

                                    if(course != null && course.getCourseId().equals(FacultyCourse.courseId)) {
                                        totalDates = course.getTotalDates();
                                    }
                                }

                                Collections.sort(totalDates, new DatesComparator()); // sorting total dates
                                Collections.sort(student_record_list, new NamesComparator()); // sorting according to the name

                                studentSpinner = (Spinner) findViewById(R.id.studentSpinner);
//                                textView = (TextView) findViewById(R.id.text_view);

                                ArrayList<String> list = new ArrayList<>();

                                list.add("Select the student name");

                                for(int i = 0; i < student_record_list.size(); i++) {
                                    list.add(student_record_list.get(i).getStudentName());
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                                studentSpinner.setVisibility(View.VISIBLE);
                                studentSpinner.setAdapter(new ArrayAdapter<>(CheckRecordFaculty.this, android.R.layout.simple_spinner_dropdown_item, list));

                                studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(i == 0) {
                                            Toast.makeText(CheckRecordFaculty.this, "Please select a number!", Toast.LENGTH_SHORT).show();

                                            dateTextView.setVisibility(View.INVISIBLE);
                                            date2TextView.setVisibility(View.INVISIBLE);
                                            recordListView.setVisibility(View.INVISIBLE);
                                            presentTextView.setVisibility(View.INVISIBLE);
                                            absentTextView.setVisibility(View.INVISIBLE);
//                                            textView.setText("");
                                        }
                                        else {
//                                            String num = adapterView.getItemAtPosition(i).toString();
                                            progressBar.setVisibility(View.VISIBLE);
                                            showSelectedRecord(i - 1);
//                                            textView.setText(num);
                                        }
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}