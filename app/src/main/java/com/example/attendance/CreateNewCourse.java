 package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

 public class CreateNewCourse extends AppCompatActivity {

    EditText courseNameTextView;
    EditText batchTextView;

    public void createCourse(View view) {

        /**/
        String course = courseNameTextView.getText().toString();

        Toast.makeText(this, course + " created successfully!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), FacultyHome.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_course);

        courseNameTextView = (EditText) findViewById(R.id.courseNameTextView2);
        batchTextView = (EditText) findViewById(R.id.batchTextView2);
    }
}