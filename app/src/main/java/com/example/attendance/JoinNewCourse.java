package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class JoinNewCourse extends AppCompatActivity {

    EditText courseIdTextView;
    ArrayList<String> courseList;

    int flag;

    String studentID;

    STUDENT student;

    DatabaseReference databaseCourse;
    DatabaseReference databaseStudent;
    DatabaseReference databaseAttendanceRecord;


    public void joinCourse(View view) {

        final String courseId = courseIdTextView.getText().toString();
        flag = 0;

        if (courseId.isEmpty()) {
            courseIdTextView.setError("Course ID is required");
            courseIdTextView.requestFocus();
            return;
        }

        for(int i = 0; i < courseList.size(); i++) { //checking if that course exists
            if(courseList.get(i).equals(courseId))
                flag = 1;
        }
        if(flag == 0) {
            Toast.makeText(JoinNewCourse.this, courseId + " does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }

        for(int i = 0; i < student.getCourseId().size(); i++) { //already enrolled in that course
            if(student.getCourseId().get(i).equals(courseId)) {
                Toast.makeText(this, "You are already enrolled!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        student.getCourseId().add(courseId);
        databaseStudent.child(studentID).setValue(student); // enrolled

        ATTENANCE_RECORD attenance_record = new ATTENANCE_RECORD(new ArrayList<Date>());

        databaseAttendanceRecord.child(courseId).child(studentID).setValue(attenance_record);

        Toast.makeText(this, "Course added successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), StudentHome.class);
        intent.putExtra("id", studentID);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_new_course);

        courseIdTextView = (EditText) findViewById(R.id.courseIdTextView2);

        courseList = new ArrayList<String>();

        Intent intent = getIntent();
        studentID = intent.getStringExtra("id");

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseStudent = FirebaseDatabase.getInstance().getReference("STUDENT");
        databaseAttendanceRecord = FirebaseDatabase.getInstance().getReference("ATTANDANCE_RECORD");

        courseList.clear();
        databaseCourse.addValueEventListener(new ValueEventListener() { //list of courses
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    COURSE course = dataSnapshot.getValue(COURSE.class);
                    if(course.getCourseId() != null)
                    courseList.add(course.getCourseId());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseStudent.addValueEventListener(new ValueEventListener() { //student
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    STUDENT tempStudent = dataSnapshot.getValue(STUDENT.class);
                    if(tempStudent.getStudentId().equals(studentID))
                        student = tempStudent;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

