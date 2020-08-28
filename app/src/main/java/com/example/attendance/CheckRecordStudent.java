package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class CheckRecordStudent extends AppCompatActivity {

    User user;
    ArrayList<User> userList;

    ListView recordListView;

    ArrayList<Date> totalClasses;
    ArrayList<Date> attendedClasses;

    DatabaseReference databaseCourse;

    String courseId;

    public class DatesComparator implements Comparator<Date> { // for sorting dates

        @Override
        public int compare(Date t1, Date t2) {
            return t1.compareTo(t2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record_student);

        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");

        recordListView = (ListView) findViewById(R.id.recordListView);

        userList = new ArrayList<>();

        int numRows = StudentCourse.course.getTotalDates().size();

        Log.i("Here", "Loop");

        if(numRows == 1) {
            Toast.makeText(this, "No record to show!", Toast.LENGTH_LONG).show();
            Log.i("Outside", "Loop");
        }
        else {

            totalClasses = StudentCourse.course.getTotalDates();

            databaseCourse = FirebaseDatabase.getInstance().getReference("ATTENDANCE_RECORD").child(courseId);
            databaseCourse.addListenerForSingleValueEvent(new ValueEventListener() { //fetched the list of attended classes
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Log.i("Outside Inside", "Loop");

                        ATTENANCE_RECORD temp = dataSnapshot.getValue(ATTENANCE_RECORD.class);
//                        Log.i("ID1", temp.getStudentID());
//                        Log.i("ID2", StudentHome.studentID);
                        if(temp.getStudentID().equals(StudentHome.studentID)) {

                            attendedClasses = temp.getPresentDates();

//                            sort dates arraylist
                            Collections.sort(totalClasses, new DatesComparator());
                            Collections.sort(attendedClasses, new DatesComparator());
//                            Log.i("Inside", "Loop");
//                            Log.i("INFO", attendedClasses.get(0).toString());
//                            Log.i("SIZE", Integer.toString(attendedClasses.size()));

                            int i = 1, j = 1;
                            while(i < totalClasses.size()) { // printing record

                                Log.i("1", totalClasses.get(i).toString());
                                Log.i("2", attendedClasses.get(j).toString());

                                if(j < attendedClasses.size() && totalClasses.get(i).compareTo(attendedClasses.get(j)) == 1 ) {
                                    user = new User(totalClasses.get(i).toString(), "Present");
                                    j++;
                                }
                                else {
                                    user = new User(totalClasses.get(i).toString(), "Absent");
                                }
                                i++;
                                userList.add(user);

                                Log.i(Integer.toString(i), Integer.toString(j));
                            }

                            TwoColumn_ListAdapter adapter = new TwoColumn_ListAdapter(getApplicationContext(), R.layout.list_adapter_view, userList);
                            recordListView.setAdapter(adapter);

                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}