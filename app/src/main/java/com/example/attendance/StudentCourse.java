package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class StudentCourse extends AppCompatActivity {

    String courseId;
    String teacherId;

    TextView courseIdTextView;
    TextView courseNameTextView;
    TextView facultyNameTextView;
    TextView batchTextView;

    DatabaseReference databaseCourse;
    DatabaseReference databaseTeacher;

    public void markAttendance(View view) {

        IntentIntegrator intentIntegrator = new IntentIntegrator(StudentCourse.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scanning...");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();

        /**/

    }

    public void checkRecord2(View view) {

        /**/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);

        Intent intent = getIntent();
        courseId = intent.getStringExtra("id");

        courseIdTextView = (TextView) findViewById(R.id.courseIDTextView3);
        courseNameTextView = (TextView) findViewById(R.id.courseNameTextView4);
        facultyNameTextView = (TextView) findViewById(R.id.faultyNameTextView);
        batchTextView = (TextView) findViewById(R.id.batchTextView3);

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseTeacher = FirebaseDatabase.getInstance().getReference("TEACHER");

        databaseCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    COURSE course = dataSnapshot.getValue(COURSE.class);

                    if(course.getCourseId().equals(courseId)) {

                        courseIdTextView.setText("Course Id: " + course.getCourseId());
                        courseNameTextView.setText("Course Name: " + course.getCourseName());
                        batchTextView.setText("Batch: " + course.getBatch());
                        teacherId = course.getTeacherId();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseTeacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    TEACHER teacher = dataSnapshot.getValue(TEACHER.class);
                    if(teacher.getTeacherId().equals(teacherId)) {
                        facultyNameTextView.setText("Faculty Name: " + teacher.getFirstName() + " " + teacher.getLastName());
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result!=null && result.getContents()!=null) {

            Toast.makeText(this, result.getContents().toString(), Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}