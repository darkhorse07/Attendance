package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    int code;

    EditText emailTextView;
    EditText passwordTextView;

    private FirebaseAuth mAuth;

    public void login(View view) {

        final String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (email.isEmpty()) {
            emailTextView.setError("Email is required!");
            emailTextView.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextView.setError("Please enter a valid email!");
            emailTextView.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordTextView.setError("Passport is required");
            passwordTextView.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordTextView.setError("Minimum length of password should be 6");
            passwordTextView.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                });

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent;
                            if (code == 1) {
                                intent = new Intent(getApplicationContext(), StudentHome.class);
                            } else {
                                intent = new Intent(getApplicationContext(), FacultyHome.class);
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            startActivity(intent);
                        }
                    }
                });
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

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);

    }
}