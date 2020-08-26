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

 public class CreateNewCourse extends AppCompatActivity {

    EditText courseNameTextView;
    EditText batchTextView;

    String teacherID;

    DatabaseReference databaseCourse;
    DatabaseReference databaseTeacher;
    DatabaseReference databaseReferenceTeacher2;

    public void createCourse(View view) {

        final String courseId = databaseCourse.push().getKey();
        final String courseName = courseNameTextView.getText().toString();
        String batch = batchTextView.getText().toString();

        Date currentDate = new Date();
        ArrayList<Date> totalDates = new ArrayList<Date>();

        COURSE course = new COURSE(courseId, courseName, batch, teacherID, "QR Code", 0, 0, currentDate, totalDates); //adding course
        databaseCourse.child(courseId).setValue(course);

        FacultyHome.teacher.getCourseId().add(courseId); //updating the courses under the teacher
        databaseTeacher.setValue(FacultyHome.teacher);

        Toast.makeText(CreateNewCourse.this, courseName + " created!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), FacultyHome.class);
        intent.putExtra("id", teacherID);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_course);

        courseNameTextView = (EditText) findViewById(R.id.courseNameTextView2);
        batchTextView = (EditText) findViewById(R.id.batchTextView2);

        teacherID = FacultyHome.teacher.getTeacherId();

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseTeacher = FirebaseDatabase.getInstance().getReference("TEACHER").child(teacherID);
        databaseReferenceTeacher2 = FirebaseDatabase.getInstance().getReference("TEACHER").child(teacherID);

    }
}