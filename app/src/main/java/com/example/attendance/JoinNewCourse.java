package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class JoinNewCourse extends AppCompatActivity {

    EditText courseIdTextView;

    public void joinCourse(View view) {

        String courseId = courseIdTextView.getText().toString();
        Toast.makeText(this, courseId + " added successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), StudentHome.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_new_course);

        courseIdTextView = (EditText) findViewById(R.id.courseIdTextView2);

    }
}