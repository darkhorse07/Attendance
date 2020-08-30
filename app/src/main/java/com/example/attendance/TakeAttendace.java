package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class TakeAttendace extends AppCompatActivity {

    ImageView qrCodeImageView;
    Button generateButton;
    Button stopButton;

    String courseId;
    COURSE course;

    int cnt = 1;

    DatabaseReference databaseCourse;

    LocationManager locationManager;
    LocationListener locationListener;

    CountDownTimer countDownTimer;

    LatLng currLatLng = new LatLng(0,0);
    LatLng lastKnowLocationLatLng = new LatLng(0,0);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(lastKnownLocation != null)
                    lastKnowLocationLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            }
        }
    }

    public void generateCode() {

        String code = UUID.randomUUID().toString();
        Log.i("CODE", code);

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE").child(courseId);

        if(code!=null && code.length()>0) {

            try {

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE, 300, 300);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrCodeImageView.setImageBitmap(bitmap);

                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {

                        if(location != null)
                            currLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                };

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if(lastKnownLocation != null)
                        lastKnowLocationLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                } else {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

                if(currLatLng.longitude != 0) {
                    course.setLat(currLatLng.latitude);
                    course.setLng(currLatLng.longitude);
                }
                else if(lastKnowLocationLatLng.longitude != 0) {
                    course.setLat(lastKnowLocationLatLng.latitude);
                    course.setLng(lastKnowLocationLatLng.longitude);
                }
                else {
                    course.setLat(1);
                    course.setLng(1);
                }
                course.setQRCode(code);

                databaseCourse.setValue(course);

            } catch (WriterException e) {

                e.printStackTrace();
            }
        }
    }

    public void generate(View view) {

        /*if(cnt == 1) {
            course.setCurrentDate(new Date());
            course.getTotalDates().add(new Date());
            cnt++;
        }*/

        if(FacultyCourse.dateInd == 0) {
            if(cnt == 1) {
                course.setCurrentDate(new Date());
                course.getTotalDates().add(new Date());
                cnt++;
            }
        }
        else {
            int ind = FacultyCourse.dates.size() - FacultyCourse.dateInd;
            course.setCurrentDate(FacultyCourse.dates.get(ind));
        }


        generateButton.setClickable(false);
        generateButton.setEnabled(false);
        generateButton.setVisibility(View.INVISIBLE);

        stopButton.setClickable(true);
        stopButton.setEnabled(true);
        stopButton.setVisibility(View.VISIBLE);

        qrCodeImageView.setVisibility(View.VISIBLE);

        Log.i("CLICKED", "!");
        countDownTimer = new CountDownTimer(50000, 8000) {
            @Override
            public void onTick(long l) {
                Log.i("Inside", "Timer");
                generateCode();
            }
            @Override
            public void onFinish() {
                start();
            }
        }.start();
    }

    public void stop(View view) {

        stopButton.setClickable(false);
        stopButton.setEnabled(false);
        stopButton.setVisibility(View.INVISIBLE);

        generateButton.setClickable(true);
        generateButton.setEnabled(true);
        generateButton.setVisibility(View.VISIBLE);

        countDownTimer.cancel();
        course.setLat(0);
        course.setLng(0);
        course.setQRCode("0");

        qrCodeImageView.setVisibility(View.INVISIBLE);

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE").child(courseId);
        databaseCourse.setValue(course);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendace);

        cnt = 1;

        qrCodeImageView = (ImageView) findViewById(R.id.qrCodeImageView);
        generateButton = (Button) findViewById(R.id.generateButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        stopButton.setVisibility(View.INVISIBLE);
        stopButton.setClickable(false);
        stopButton.setEnabled(false);

        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");

        databaseCourse = FirebaseDatabase.getInstance().getReference("COURSE");
        databaseCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    COURSE tempCourse = dataSnapshot.getValue(COURSE.class);

                    if(tempCourse != null && tempCourse.getCourseId().equals(courseId))
                        course = tempCourse;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

}
