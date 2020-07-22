package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    int code;

    EditText emailTextView;
    EditText passwordTextView;
    EditText firstNameTextView;
    EditText secondNameTextView;

    public void register(View view) {

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


    }
}