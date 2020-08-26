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

    int flag = 0;

    String teacherID;

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceTeacher;
    DatabaseReference databaseReferenceTeacher2;

    public void createCourse(View view) {

        final String id = databaseReference.push().getKey();
        final String courseName = courseNameTextView.getText().toString();
        String batch = batchTextView.getText().toString();
        Date currentDate = new Date();
        ArrayList<Date> totalDates = new ArrayList<Date>();

        COURSE course = new COURSE(id, courseName, batch, teacherID, "QR Code", 0, 0, currentDate, totalDates);
        databaseReference.child(id).setValue(course);

        databaseReferenceTeacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    TEACHER teacher = dataSnapshot.getValue(TEACHER.class);

                    if (teacherID.equals(teacher.getTeacherId()) && flag == 0) {
                        flag = 1;
                        teacher.getCourseId().add(id);
                        databaseReferenceTeacher2.child("courseId").setValue(teacher.getCourseId());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


//        finalTeacher[0].getCourseId().add(id);
//        databaseReferenceTeacher.child(teacherID).setValue(finalTeacher[0]);
        Toast.makeText(CreateNewCourse.this, courseName + " created!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), FacultyHome.class);
        intent.putExtra("id", teacherID);
        startActivity(intent);

//        finalTeacher.getCourseId().add(id);
//        databaseReferenceTeacher.child(teacherID).setValue(finalTeacher);
//        Toast.makeText(CreateNewCourse.this, courseName + " created!", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getApplicationContext(), FacultyHome.class);
//        intent.putExtra("id", teacherID);
//        startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_course);

        courseNameTextView = (EditText) findViewById(R.id.courseNameTextView2);
        batchTextView = (EditText) findViewById(R.id.batchTextView2);

        Intent intent = getIntent();
        teacherID = intent.getStringExtra("id");

        Log.i("msg", teacherID);

        databaseReference = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseReferenceTeacher = FirebaseDatabase.getInstance().getReference("TEACHER");
        databaseReferenceTeacher2 = FirebaseDatabase.getInstance().getReference("TEACHER").child(teacherID);

    }
}