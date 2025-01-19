package com.example.patt;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView tasksImage = findViewById(R.id.tasksImage);
        ImageView remindersImage = findViewById(R.id.remindersImage);
        ImageView activityLogImage = findViewById(R.id.activityLogImage);
        ImageView analyticsImage = findViewById(R.id.analyticsImage);
        ImageView notesImage = findViewById(R.id.notesImage);
        ImageView infoImage = findViewById(R.id.infoImage);

        tasksImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.patt.TasksActivity.class);
                startActivity(intent);
            }
        });

        remindersImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemindersActivity.class);
                startActivity(intent);
            }
        });

        /*activityLogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityLogActivity.class);
                startActivity(intent);
            }
        });

        analyticsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnalyticsActivity.class);
                startActivity(intent);
            }
        });*/

        activityLogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast message instead of starting an activity
                Toast.makeText(MainActivity.this, "This feature is under development", Toast.LENGTH_SHORT).show();
            }
        });

        analyticsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast message instead of starting an activity
                Toast.makeText(MainActivity.this, "This feature is under development", Toast.LENGTH_SHORT).show();
            }
        });


        notesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });

        infoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
