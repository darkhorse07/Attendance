package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    int code;

    EditText emailTextView;
    EditText passwordTextView;
    EditText firstNameTextView;
    EditText secondNameTextView;

    ProgressBar progressBar;

    DatabaseReference databaseReference;

    private FirebaseAuth mAuth;

    public void register(View view) {

        final String id = databaseReference.push().getKey();
        final String firstName = firstNameTextView.getText().toString().trim();
        final String lastName = secondNameTextView.getText().toString().trim();
        final String email = emailTextView.getText().toString().trim();
        final ArrayList<String> courseID = new ArrayList<String>();

        String password = passwordTextView.getText().toString();

        if(firstName.isEmpty()) {
            firstNameTextView.setError("First name is required!");
            firstNameTextView.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            emailTextView.setError("Email is required!");
            emailTextView.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextView.setError("Please enter a valid email!");
            emailTextView.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            passwordTextView.setError("Passport is required");
            passwordTextView.requestFocus();
            return;
        }
        if(password.length() < 6) {
            passwordTextView.setError("Minimum length of password should be 6");
            passwordTextView.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE)   ;
                    if(code == 1) {
                        STUDENT student = new STUDENT(id, firstName, lastName, email, courseID);
                        databaseReference.child(id).setValue(student);
                    }
                    else {
                        TEACHER teacher = new TEACHER(id, firstName, lastName, email, courseID);
                        databaseReference.child(id).setValue(teacher);
                    }
                }
            }
        });
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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

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