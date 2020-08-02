package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class MainActivity extends AppCompatActivity {

    TextView aboutTextView;

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

        aboutTextView = (TextView) findViewById(R.id.aboutTextView);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            String text = getString(R.string.about_MarkME);
            Spanned styledText = Html.fromHtml(text, FROM_HTML_MODE_LEGACY);
            aboutTextView.setText(styledText);
            aboutTextView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        }
    }
}