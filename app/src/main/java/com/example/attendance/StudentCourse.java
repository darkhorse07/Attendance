package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.concurrent.CountDownLatch;

public class StudentCourse extends AppCompatActivity {

    String courseId;
    String teacherId;

    COURSE course;
    COURSE currCourse;
    ATTENANCE_RECORD attenance_record;

    TextView courseIdTextView;
    TextView courseNameTextView;
    TextView facultyNameTextView;
    TextView batchTextView;
    Button markAttendanceButton;

    DatabaseReference databaseCourse;
    DatabaseReference databaseTeacher;
    DatabaseReference databaseAttendace;

    IntentIntegrator intentIntegrator;

    boolean check = false;

    String qrCode;

    public void markAttendance(View view) {

        intentIntegrator = new IntentIntegrator(StudentCourse.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scanning...");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();

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
        //Log.i("COURSEID", courseId);

        courseIdTextView = (TextView) findViewById(R.id.courseIDTextView3);
        courseNameTextView = (TextView) findViewById(R.id.courseNameTextView4);
        facultyNameTextView = (TextView) findViewById(R.id.faultyNameTextView);
        batchTextView = (TextView) findViewById(R.id.batchTextView3);
        markAttendanceButton = (Button) findViewById(R.id.markAttendanceButton);

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseTeacher = FirebaseDatabase.getInstance().getReference("TEACHER");
        databaseAttendace = FirebaseDatabase.getInstance().getReference("ATTANDANCE_RECORD").child(courseId);

        databaseCourse.addValueEventListener(new ValueEventListener() { // displaying course id, course name, batch name and fetching course
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    COURSE tempCourse = dataSnapshot.getValue(COURSE.class);

                    if(tempCourse.getCourseId().equals(courseId)) {

                        course = tempCourse;
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

        databaseTeacher.addValueEventListener(new ValueEventListener() { // displaying teacher name
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    TEACHER teacher = dataSnapshot.getValue(TEACHER.class);
                    //Log.i("Details", teacher.toString());
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
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        qrCode = result.getContents();
        check = false;

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseCourse.addValueEventListener(new ValueEventListener() { // fetching the current course details (currDate, qrCode, lat, lng)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    COURSE tempCourse = dataSnapshot.getValue(COURSE.class);

                    if(tempCourse.getCourseId().equals(courseId)) {

                        currCourse = tempCourse;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseAttendace = FirebaseDatabase.getInstance().getReference("ATTENDANCE_RECORD").child(courseId);
            databaseAttendace.addListenerForSingleValueEvent(new ValueEventListener() { // fetching previous attendance record of that course of the student
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        ATTENANCE_RECORD temp = dataSnapshot.getValue(ATTENANCE_RECORD.class);
                        //Log.i("Inside", "Loop");
                        if (temp.getStudentID().equals(StudentHome.studentID)) {

                            attenance_record = temp;
                            //attenance_record.getPresentDates().add(course.getCurrentDate());
                            //databaseAttendace.child(StudentHome.studentID).setValue(attenance_record);

                            for(int i = 1; i < attenance_record.getPresentDates().size(); i++) { // checking if the attendance is already marked

                                //Log.i("SIZE", Integer.toString(attenance_record.getPresentDates().size()));
                                if(attenance_record.getPresentDates().get(i).equals(currCourse.getCurrentDate())) { // attendance is marked
                                    qrCode = "Your attendance is already marked!";
                                    check = true;
                                }
                            }

                            if(check == false) { //QR-Code does not match

                                if(currCourse.getQRCode().equals(qrCode) == false) {
                                    qrCode = "Invalid qrCode!";
                                    check = true;
                                    intentIntegrator.initiateScan();

                                    //restart();
                                    //markAttendance(findViewById(R.id.markAttendanceButton));
                                }
                            }

                        }
                    }
                    if(result!=null && result.getContents()!=null) {
                        Toast.makeText(StudentCourse.this, qrCode, Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        Log.i("QR-Code", qrCode);
        super.onActivityResult(requestCode, resultCode, data);





        /*for(int i = 1; i < attenance_record.getPresentDates().size(); i++) { // checking if the attendance is already marked
            if(attenance_record.getPresentDates().get(i).equals(course.getCurrentDate())) {
                Toast.makeText(this, "Attendance is already marked!", Toast.LENGTH_SHORT).show();
                return;
            }
        }*/
        //Log.i("SIZE", Integer.toString(attenance_record.getPresentDates().size()));
        //attenance_record.getPresentDates().add(course.getCurrentDate());
        //databaseAttendace.setValue(attenance_record);

        //Log.i("INFO", course.getCurrentDate().toString());

        /*if(result!=null && result.getContents()!=null) {

            databaseAttendace.child(StudentHome.studentID).setValue(attenance_record);
            Toast.makeText(this, result.getContents().toString(), Toast.LENGTH_SHORT).show();
        }*/


    }

}