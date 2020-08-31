package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentHome extends AppCompatActivity {

    TextView welcomeTextView;
    ListView courseListView;
    ProgressBar progressBar;

    static String studentID;

    STUDENT student;

    static COURSE currCourse;

    static ArrayList<String> courseList = new ArrayList<String>();
    static ArrayList<String> courseId = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;

    DatabaseReference databaseStudent;
    DatabaseReference databaseCourse;

    @Override
    public void onBackPressed() {

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400); // vibrate the device
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.student_home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.joinNewCourse : {
                Intent intent = new Intent(getApplicationContext(), JoinNewCourse.class);
                intent.putExtra("id", studentID);
                startActivity(intent);
                break;
            }

            case R.id.help: {
                /**/
                break;
            }

            case R.id.logout: {
                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            }

            default: return false;
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView2);
        courseListView = (ListView) findViewById(R.id.courseListView2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        Intent intent = getIntent();
        studentID = intent.getStringExtra("id");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, courseList);

        databaseStudent = FirebaseDatabase.getInstance().getReference("STUDENT");
        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");

        databaseStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot :snapshot.getChildren()) {

                    STUDENT tempStudent = dataSnapshot.getValue(STUDENT.class);

                    if(tempStudent != null && tempStudent.getStudentId().equals(studentID)) {
                        student = tempStudent;
                        welcomeTextView.setText("Welcome, " + student.getFirstName());
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                courseList.clear();
                courseId.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    COURSE course = dataSnapshot.getValue(COURSE.class);

                    if(course != null) {

                        for (int i = 1; i < student.getCourseId().size(); i++) {
                            if (student.getCourseId().get(i).equals(course.getCourseId())) {

                                courseList.add(course.getCourseName() + " (" + course.getBatch() + ")");
                                courseId.add(course.getCourseId());
                            }
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.INVISIBLE);
                courseListView.setVisibility(View.VISIBLE);
                welcomeTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.i("OK", "OK");

        courseListView.setAdapter(arrayAdapter);

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final String id = courseId.get(i);

                databaseCourse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            COURSE temp = dataSnapshot.getValue(COURSE.class);
                            if(temp != null && temp.getCourseId().equals(id)) {
                                currCourse = temp;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(getApplicationContext(), StudentCourse.class);
                //Log.i("id", id);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        courseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                final String courseIdToBeDeleted = courseId.get(pos);

                new AlertDialog.Builder(StudentHome.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("All the attendance record of this course will be erased. Do you want to delete this course?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DatabaseReference databaseAttendance = FirebaseDatabase.getInstance().getReference("ATTENDANCE_RECORD").child(courseIdToBeDeleted).child(studentID);
                                databaseAttendance.removeValue();
                                student.getCourseId().remove(courseIdToBeDeleted);
                                courseId.remove(courseIdToBeDeleted);
                                courseList.remove(pos);
                                arrayAdapter.notifyDataSetChanged();

                                DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference("STUDENT").child(studentID);
                                databaseStudent.setValue(student);

                                Toast.makeText(StudentHome.this, "Course deleted successfully!", Toast.LENGTH_SHORT).show();
                                /*Intent intent = new Intent(getApplicationContext(), StudentHome.class);
                                intent.putExtra("id", studentID);
                                startActivity(intent);*/


                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }
}