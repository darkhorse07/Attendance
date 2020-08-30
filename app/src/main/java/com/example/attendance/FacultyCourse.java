package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultyCourse extends AppCompatActivity {

    static String courseId;

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    Spinner selectDateSpinner;
    Button cancelButton;
    Button generateQRCodeButton;

    TextView courseIdTextView;
    TextView courseNameTextView;
    TextView batchTextView;

    DatabaseReference databaseCourse;

    public void attendance(View view) {

        selectDateDialog();

//        Intent intent = new Intent(getApplicationContext(), TakeAttendace.class);
//        intent.putExtra("courseId", courseId);
//        startActivity(intent);

        /**/
    }

    public void checkRecord(View view) {

        Intent intent = new Intent(getApplicationContext(), CheckRecordFaculty.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);

        /**/
    }

    public void selectDateDialog() {

        dialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
        final View selectDatePopUpView = getLayoutInflater().inflate(R.layout.popup_select_date, null);

        selectDateSpinner = (Spinner) selectDatePopUpView.findViewById(R.id.selectDateSpinner);
        cancelButton = (Button) selectDatePopUpView.findViewById(R.id.cancelButton);
        generateQRCodeButton = (Button) selectDatePopUpView.findViewById(R.id.generateQRCodeButton);

        dialogBuilder.setView(selectDatePopUpView);
        //dialogBuilder.setTitle("Select Attendance Date");

        alertDialog = dialogBuilder.create();


//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(); // fixing the size of alert dialog window
//        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;


        alertDialog.show();
//        alertDialog.getWindow().setAttributes(layoutParams);

        if(cancelButton == null) {
            Log.i("Cancel", "NULL");
        }
        else {
            Log.i("Cancel", "NOT NULL");
        }

        cancelButton.setOnClickListener(new View.OnClickListener() { // cancel button
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        generateQRCodeButton.setOnClickListener(new View.OnClickListener() { // generate QR-Code button
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TakeAttendace.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_course);

        Intent intent = getIntent();
        courseId = intent.getStringExtra("id");

        courseIdTextView = (TextView) findViewById(R.id.courseIDTextView);
        courseNameTextView = (TextView) findViewById(R.id.courseNameTextView2);
        batchTextView = (TextView) findViewById(R.id.batchTextView2);

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    COURSE course = dataSnapshot.getValue(COURSE.class);

                    if(course != null && course.getCourseId().equals(courseId)) {
                        courseIdTextView.setText("Course Id: " + course.getCourseId());
                        courseNameTextView.setText("Course Name: " + course.getCourseName());
                        batchTextView.setText("Batch: " + course.getBatch());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}