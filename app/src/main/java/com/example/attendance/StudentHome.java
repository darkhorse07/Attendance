package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentHome extends AppCompatActivity {

    TextView welcomeTextView;
    ListView courseListView;

    static String studentID;

    STUDENT student;

    static COURSE currCourse;

    static ArrayList<String> courseList = new ArrayList<String>();
    static ArrayList<String> courseId = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;

    DatabaseReference databaseStudent;
    DatabaseReference databaseCourse;

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

            case R.id.help: /**/ break;

            case R.id.logout: {
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
                    if(tempStudent.getStudentId().equals(studentID))
                        student = tempStudent;
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

                    for(int i = 1; i < student.getCourseId().size(); i++) {
                        if(student.getCourseId().get(i).equals(course.getCourseId())) {
                            //Log.i("COURSE name", course.getCourseName());
                            //Log.i("COURSE id", course.getCourseId());
                            courseList.add(course.getCourseName() + " (" + course.getBatch() + ")");
                            courseId.add(course.getCourseId());
                        }
                    }
                }

                arrayAdapter.notifyDataSetChanged();
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
                            if(temp.getCourseId().equals(id)) {
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
    }
}