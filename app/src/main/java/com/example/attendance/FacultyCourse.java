package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultyCourse extends AppCompatActivity {

    String courseId;

    TextView courseIdTextView;
    TextView courseNameTextView;
    TextView batchTextView;

    DatabaseReference databaseCourse;

    public void attendance(View view) {

        Intent intent = new Intent(getApplicationContext(), TakeAttendace.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);

        /**/
    }

    public void checkRecord(View view) {

        /**/
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