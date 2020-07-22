package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    int code;

    EditText emailTextView;
    EditText passwordTextView;

    public void login(View view) {

        /***/
    }

    public void signup(View view) {

        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        intent.putExtra("code", code);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextView = (EditText) findViewById(R.id.emailTextView);
        passwordTextView = (EditText) findViewById(R.id.passwordTextView);

        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);
    }
}