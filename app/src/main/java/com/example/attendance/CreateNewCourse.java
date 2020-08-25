 package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

 public class CreateNewCourse extends AppCompatActivity {

    EditText courseNameTextView;
    EditText batchTextView;

    String teacherID;

    DatabaseReference databaseReference;

    public void createCourse(View view) {

        /**/
        String id = databaseReference.push().getKey();
        String courseName = courseNameTextView.getText().toString();
        String batch = batchTextView.getText().toString();
        Date currentDate = new Date();
        ArrayList<Date> totalDates = new ArrayList<Date>();

        COURSE course = new COURSE(id, courseName, batch, teacherID, "QR Code", 0, 0, currentDate, totalDates);
        databaseReference.child(id).setValue(course);

        Toast.makeText(this, courseName + " created successfully!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), FacultyHome.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_course);

        courseNameTextView = (EditText) findViewById(R.id.courseNameTextView2);
        batchTextView = (EditText) findViewById(R.id.batchTextView2);

        databaseReference = FirebaseDatabase.getInstance().getReference("COURSE");

        teacherID = "***";
    }
}