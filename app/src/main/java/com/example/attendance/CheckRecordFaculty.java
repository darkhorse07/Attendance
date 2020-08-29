package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckRecordFaculty extends AppCompatActivity {

    Spinner studentSpinner;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record_faculty);

        studentSpinner = (Spinner) findViewById(R.id.studentSpinner);
        textView = (TextView) findViewById(R.id.text_view);

        ArrayList<String> list = new ArrayList<>();

        list.add("Select Number");
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");

        studentSpinner.setAdapter(new ArrayAdapter<>(CheckRecordFaculty.this, android.R.layout.simple_spinner_dropdown_item, list));

        studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    Toast.makeText(CheckRecordFaculty.this, "Please select a number!", Toast.LENGTH_LONG).show();
                    textView.setText("");
                }
                else {
                    String num = adapterView.getItemAtPosition(i).toString();
                    textView.setText(num);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}