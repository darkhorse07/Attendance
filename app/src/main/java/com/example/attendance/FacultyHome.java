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

public class FacultyHome extends AppCompatActivity {

    TextView welcomeTextView;
    ListView courseListView;
    ProgressBar progressBar;

    String teacherID;

    static TEACHER teacher;

    static ArrayList<String> courseList = new ArrayList<String>();
    ArrayList<String> courseIdList = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;

    DatabaseReference databaseTeacher;
    DatabaseReference databaseCourse;

    @Override
    public void onBackPressed() {

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400); // vibrate the device
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.faculty_home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.addNewCourse : {
                Intent intent = new Intent(getApplicationContext(), CreateNewCourse.class);
                startActivity(intent);
                break;
            }

            case R.id.help: /**/ break;

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
        setContentView(R.layout.activity_faculty_home);

        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView2);
        courseListView = (ListView) findViewById(R.id.courseListView2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);

        Intent intent = getIntent();
        teacherID = intent.getStringExtra("id");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, courseList);

        databaseTeacher = FirebaseDatabase.getInstance().getReference("TEACHER");
        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, courseList);

        databaseTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    TEACHER tempTeacher = dataSnapshot.getValue(TEACHER.class);

                    if(tempTeacher != null && tempTeacher.getTeacherId().equals(teacherID)) {
                        teacher = tempTeacher;
                        welcomeTextView.setText("Welcome, " + teacher.getFirstName());

                        databaseCourse.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                courseList.clear();
                                courseIdList.clear();

                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    COURSE course = dataSnapshot.getValue(COURSE.class);

                                    if (course != null) {
                                        for (int i = 1; i < teacher.getCourseId().size(); i++) {
                                            if (teacher.getCourseId().get(i).equals(course.getCourseId())) {
                                                courseList.add(course.getCourseName() + " (" + course.getBatch() + ")");
                                                courseIdList.add(course.getCourseId());
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

                        courseListView.setAdapter(arrayAdapter);


                        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override

                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                String id = courseIdList.get(i);
//                                String id = teacher.getCourseId().get(i + 1);
                                Intent intent = new Intent(getApplicationContext(), FacultyCourse.class);
                                Log.i("id", id);
                                intent.putExtra("id", id);
                                startActivity(intent);

                            }
                        });

                        courseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                                final String courseIdToBeDeleted = courseIdList.get(pos);

                                new AlertDialog.Builder(FacultyHome.this)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Are you sure?")
                                        .setMessage("All the attendance record of this course will be erased. Do you want to delete this course?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                teacher.getCourseId().remove(courseIdToBeDeleted); // updating lists
                                                courseIdList.remove(pos);
                                                courseList.remove(pos);
                                                arrayAdapter.notifyDataSetChanged();

                                                DatabaseReference databaseTeacher = FirebaseDatabase.getInstance().getReference("TEACHER").child(teacher.getTeacherId());
                                                databaseTeacher.setValue(teacher); // updating TEACHER->teacherid

                                                DatabaseReference databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE").child(courseIdToBeDeleted);
                                                databaseCourse.removeValue(); // deleting course from COURSE

                                                DatabaseReference databaseAttendance = FirebaseDatabase.getInstance().getReference("ATTENDANCE_RECORD").child(courseIdToBeDeleted);
                                                databaseAttendance.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                                        {

                                                            final ATTENANCE_RECORD attenance_record = dataSnapshot1.getValue(ATTENANCE_RECORD.class);

                                                            if(attenance_record != null)
                                                            {

                                                                DatabaseReference databaseStudent = FirebaseDatabase.getInstance().getReference("STUDENT");
                                                                databaseStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                        for(DataSnapshot dataSnapshot2 : snapshot.getChildren())
                                                                        {

                                                                            STUDENT student = dataSnapshot2.getValue(STUDENT.class);

                                                                            if(student != null && student.getStudentId().equals(attenance_record.getStudentID())) {

                                                                                student.getCourseId().remove(courseIdToBeDeleted);

                                                                                DatabaseReference databaseStudent2 = FirebaseDatabase.getInstance().getReference("STUDENT").child(student.getStudentId());
                                                                                databaseStudent2.setValue(student);

                                                                            }
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });


                                                            }
                                                        }

                                                        DatabaseReference databaseAttendance2 = FirebaseDatabase.getInstance().getReference("ATTENDANCE_RECORD").child(courseIdToBeDeleted);
                                                        databaseAttendance2.removeValue();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();

                                return true;
                            }
                        });


                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}