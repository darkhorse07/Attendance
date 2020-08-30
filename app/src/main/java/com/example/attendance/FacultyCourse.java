package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class FacultyCourse extends AppCompatActivity {

    static String courseId;
    static int dateInd = 0;

    static ArrayList<Date> dates;

    ArrayList<String> dateList;

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    Spinner selectDateSpinner;
    Button cancelButton;
    Button generateQRCodeButton;

    TextView courseIdTextView;
    TextView courseNameTextView;
    TextView batchTextView;

    DatabaseReference databaseCourse;

    public class DatesComparator implements Comparator<Date> { // for sorting dates

        @Override
        public int compare(Date t1, Date t2) {
            return t1.compareTo(t2);
        }
    }

    public void attendance(View view) {

        selectDateDialog();

//        Intent intent = new Intent(getApplicationContext(), TakeAttendace.class);
//        intent.putExtra("courseId", courseId);
//        startActivity(intent);

        /**/
    }

    public void checkRecord(View view) {

        Intent intent = new Intent(getApplicationContext(), CheckRecordFaculty.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);

        /**/
    }

    public void selectDateDialog() {

        dialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
        final View selectDatePopUpView = getLayoutInflater().inflate(R.layout.popup_select_date, null);

        selectDateSpinner = (Spinner) selectDatePopUpView.findViewById(R.id.selectDateSpinner);
        cancelButton = (Button) selectDatePopUpView.findViewById(R.id.cancelButton);
        generateQRCodeButton = (Button) selectDatePopUpView.findViewById(R.id.generateQRCodeButton);

        dialogBuilder.setView(selectDatePopUpView);

        alertDialog = dialogBuilder.create();
        alertDialog.show();

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) { // fetching dates list

                    COURSE course = dataSnapshot.getValue(COURSE.class);

                    if(course != null && course.getCourseId().equals(courseId)) {

                        dates = course.getTotalDates();
                        Collections.sort(dates, new DatesComparator()); // sorting dates

                        dateList = new ArrayList<String>();

                        dateList.add("New Class");
                        for(int i = dates.size()-1; i > 0; i--) {

                            String date1 = dates.get(i).toString();
                            String date2 = "";

                            for(int k = 0; k < date1.length(); k++) {
                                if(date1.charAt(k) == 'G')
                                    break;
                                else
                                    date2 = date2 + date1.charAt(k);
                            }

                            dateList.add(date2);
                        }

                        selectDateSpinner.setAdapter(new ArrayAdapter<>(FacultyCourse.this, android.R.layout.simple_spinner_dropdown_item, dateList));

                        selectDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                dateInd = i;

                                generateQRCodeButton.setOnClickListener(new View.OnClickListener() { // generate QR-Code button
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(), TakeAttendace.class);
                                        intent.putExtra("courseId", courseId);
                                        startActivity(intent);
                                    }
                                });



                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() { // cancel button
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_course);

        Intent intent = getIntent();
        courseId = intent.getStringExtra("id");

        courseIdTextView = (TextView) findViewById(R.id.courseIDTextView);
        courseNameTextView = (TextView) findViewById(R.id.courseNameTextView2);
        batchTextView = (TextView) findViewById(R.id.batchTextView2);

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    COURSE course = dataSnapshot.getValue(COURSE.class);

                    if(course != null && course.getCourseId().equals(courseId)) {
                        courseIdTextView.setText("Course Id: " + course.getCourseId());
                        courseNameTextView.setText("Course Name: " + course.getCourseName());
                        batchTextView.setText("Batch: " + course.getBatch());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}