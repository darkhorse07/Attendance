package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void studentLogin(View view) {

        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.putExtra("code", 1);
        startActivity(intent);
    }

    public void facultyLogin(View view) {

        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.putExtra("code", 2);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}