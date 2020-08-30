package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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

    static COURSE course;
    COURSE currCourse;
    ATTENANCE_RECORD attenance_record;

    TextView courseIdTextView;
    TextView courseNameTextView;
    TextView facultyNameTextView;
    TextView batchTextView;
    Button markAttendanceButton;
    ProgressBar progressBar;

    DatabaseReference databaseCourse;
    DatabaseReference databaseTeacher;
    DatabaseReference databaseAttendace;
    DatabaseReference databaseAttendance2;

    IntentIntegrator intentIntegrator;

    LocationManager locationManager;
    LocationListener locationListener;

    LatLng currLatLng = new LatLng(0,0);
    LatLng lastKnowLocationLatLng = new LatLng(0,0);

    boolean check = false;

    String qrCode = "";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(lastKnownLocation != null)
                    lastKnowLocationLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            }
        }
    }

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

        Intent intent = new Intent(getApplicationContext(), CheckRecordStudent.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
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
        progressBar = (ProgressBar) findViewById(R.id.progressBar5);

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseTeacher = FirebaseDatabase.getInstance().getReference("TEACHER");
        databaseAttendace = FirebaseDatabase.getInstance().getReference("ATTANDANCE_RECORD").child(courseId);

        databaseCourse.addValueEventListener(new ValueEventListener() { // displaying course id, course name, batch name and fetching course
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    COURSE tempCourse = dataSnapshot.getValue(COURSE.class);

                    if(tempCourse != null && tempCourse.getCourseId().equals(courseId)) {

                        course = tempCourse;
                        courseIdTextView.setText("Course Id: " + course.getCourseId());
                        courseNameTextView.setText("Course Name: " + course.getCourseName());
                        batchTextView.setText("Batch: " + course.getBatch());
                        teacherId = course.getTeacherId();

                        databaseTeacher.addValueEventListener(new ValueEventListener() { // displaying teacher name
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    TEACHER teacher = dataSnapshot.getValue(TEACHER.class);
                                    //Log.i("Details", teacher.toString());
                                    if(teacher != null && teacher.getTeacherId().equals(teacherId)) {
                                        facultyNameTextView.setText("Faculty Name: " + teacher.getFirstName() + " " + teacher.getLastName());

                                        progressBar.setVisibility(View.INVISIBLE);
                                        courseIdTextView.setVisibility(View.VISIBLE);
                                        courseNameTextView.setVisibility(View.VISIBLE);
                                        facultyNameTextView.setVisibility(View.VISIBLE);
                                        batchTextView.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null && result.getContents()!=null) {
            qrCode = result.getContents();
        }
        check = false;

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseCourse.addValueEventListener(new ValueEventListener() { // fetching the current course details (currDate, qrCode, lat, lng)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    COURSE tempCourse = dataSnapshot.getValue(COURSE.class);

                    if(tempCourse != null && tempCourse.getCourseId().equals(courseId)) {

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

                        if (temp != null && temp.getStudentID().equals(StudentHome.studentID)) { // got the desired attendance record

                            attenance_record = temp;

                            for(int i = 1; i < attenance_record.getPresentDates().size(); i++) { // checking if the attendance is already marked

                                if(attenance_record.getPresentDates().get(i).equals(currCourse.getCurrentDate())) { // attendance is marked
                                    qrCode = "Your attendance is already marked!";
                                    check = true;
                                }
                            }

                            if(check == false) {

                                if(currCourse.getQRCode().equals(qrCode) == false) { // QR-Code does not match
                                    qrCode = "Invalid qrCode!";
                                    check = true;
                                    //intentIntegrator.initiateScan();
                                }
                                else {
                                    locationListener = new LocationListener() { // fetching current location of the student
                                        @Override
                                        public void onLocationChanged(@NonNull Location location) {

                                            if(location != null)
                                                currLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        }
                                    };

                                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                        if(lastKnownLocation != null)
                                            lastKnowLocationLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                    }
                                    else {

                                        ActivityCompat.requestPermissions(StudentCourse.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                    }

                                    double lat = 0;
                                    double lng = 0;

                                    if(currLatLng.longitude != 0) {
                                        lat = currLatLng.latitude;
                                        lng = currLatLng.longitude;
                                    }
                                    else if(lastKnowLocationLatLng.longitude != 0) {
                                        lat = lastKnowLocationLatLng.latitude;
                                        lng = lastKnowLocationLatLng.longitude;
                                    }

                                    double dist = (course.getLat() - lat)*(course.getLat() - lat) + (course.getLng() - lng)*(course.getLng() - lng);

                                    if(dist > 10.0) {
                                        qrCode = "Distance is far!";
                                        check = true;
                                    }
                                    else { // Attendance is marked
                                        qrCode = "Attendance Marked!";
                                        attenance_record.getPresentDates().add(currCourse.getCurrentDate());

                                        databaseAttendance2 = FirebaseDatabase.getInstance().getReference("ATTENDANCE_RECORD").child(courseId).child(StudentHome.studentID);
                                        databaseAttendance2.setValue(attenance_record);

                                    }
                                }
                            }

                        }
                    }
                    Log.i("QR-Code", qrCode);
                    Toast.makeText(StudentCourse.this, qrCode, Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        super.onActivityResult(requestCode, resultCode, data);

    }

}