package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FacultyHome extends AppCompatActivity {

    TextView welcomeTextView;
    ListView courseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);

        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        courseListView = (ListView) findViewById(R.id.courseListView);

        ArrayList<String> courseName = new ArrayList<String>();

        courseName.add("Maths II");
        courseName.add("AI");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, courseName);

        courseListView.setAdapter(arrayAdapter);

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /**/

            }
        });

    }
}