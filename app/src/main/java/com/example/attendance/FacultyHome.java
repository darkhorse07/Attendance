package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FacultyHome extends AppCompatActivity {

    TextView welcomeTextView;
    ListView courseListView;

    String teacherID;

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
                intent.putExtra("id", teacherID);
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
        setContentView(R.layout.activity_faculty_home);

        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView2);
        courseListView = (ListView) findViewById(R.id.courseListView2);

        Intent intent = getIntent();
        teacherID = intent.getStringExtra("id");

        ArrayList<String> courseName = new ArrayList<String>();

        courseName.add("DMBS");
        courseName.add("DS");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, courseName);

        courseListView.setAdapter(arrayAdapter);

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), FacultyCourse.class);
                startActivity(intent);

            }
        });

    }
}