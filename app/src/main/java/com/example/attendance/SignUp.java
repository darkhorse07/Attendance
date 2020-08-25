package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    int code;

    EditText emailTextView;
    EditText passwordTextView;
    EditText firstNameTextView;
    EditText secondNameTextView;

    DatabaseReference databaseReference;

    public void register(View view) {

        String id = databaseReference.push().getKey();
        String firstName = firstNameTextView.getText().toString();
        String lastName = secondNameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        ArrayList<String> courseID = new ArrayList<String>();

        if(code == 1) {

            STUDENT student = new STUDENT(id, firstName, lastName, email, courseID);
            databaseReference.child(id).setValue(student);
        }
        else {

            TEACHER teacher = new TEACHER(id, firstName, lastName, email, courseID);
            databaseReference.child(id).setValue(teacher);
        }

        /**/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailTextView = (EditText) findViewById(R.id.emailTextView);
        passwordTextView = (EditText) findViewById(R.id.passwordTextView);
        firstNameTextView = (EditText) findViewById(R.id.firstNameTextView);
        secondNameTextView = (EditText) findViewById(R.id.secondNameTextView);

        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);

        if(code == 1) {

            databaseReference = FirebaseDatabase.getInstance().getReference("STUDENT");
        }
        else {

            databaseReference = FirebaseDatabase.getInstance().getReference("TEACHER");
        }


    }
}