package com.adamgamesstudio.tictactoegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AppInfoActivity extends AppCompatActivity {

    private Button playNowButton, contactButton, reportBugButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        getSupportActionBar().hide();

        playNowButton = (Button) findViewById(R.id.playButton);
        contactButton = (Button) findViewById(R.id.contactButton);
        reportBugButton = (Button) findViewById(R.id.reportBugButton);

        playNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AppInfoActivity.this, "Have a nice Game!", Toast.LENGTH_SHORT).show();
                Intent appInfoActivity = new Intent(AppInfoActivity.this, MainActivity.class);
                startActivity(appInfoActivity);
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri sendFeedbackURI = Uri.parse("https://adamgamesstudio.github.io/devs/contact.htm");
                startActivity(new Intent(Intent.ACTION_VIEW, sendFeedbackURI));
            }
        });

        reportBugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri sendFeedbackURI = Uri.parse("https://adamgamesstudio.github.io/tictactoegame/feedbackmail.htm");
                startActivity(new Intent(Intent.ACTION_VIEW, sendFeedbackURI));
            }
        });
    }
}