/*
package com.example.patt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class RemindersActivity extends AppCompatActivity {

    private EditText reminderTitle, reminderDescription, reminderDate, reminderTime;
    private Button addReminder;
    private ListView reminderListView;
    private ArrayList<Reminder> reminderList;
    private CustomAdapter customAdapter;
    private SharedPreferences sharedPreferences;
    private static final String REMINDER_PREFS = "RemindersPrefs";
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        // Initialize UI components
        reminderTitle = findViewById(R.id.reminderTitle);
        reminderDescription = findViewById(R.id.reminderDescription);
        reminderDate = findViewById(R.id.reminderDate);
        reminderTime = findViewById(R.id.reminderTime);
        addReminder = findViewById(R.id.addReminder);
        reminderListView = findViewById(R.id.reminderListView);

        // Initialize shared preferences and reminder list
        sharedPreferences = getSharedPreferences(REMINDER_PREFS, MODE_PRIVATE);
        reminderList = loadReminders();
        if (reminderList == null) {
            reminderList = new ArrayList<>();
        }

        // Use the custom adapter
        customAdapter = new CustomAdapter();
        reminderListView.setAdapter(customAdapter);

        // Add reminder logic
        addReminder.setOnClickListener(v -> {
            String title = reminderTitle.getText().toString();
            String description = reminderDescription.getText().toString();
            String date = reminderDate.getText().toString();
            String time = reminderTime.getText().toString();

            if (!title.isEmpty() && !description.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                Reminder reminder = new Reminder(title, description, date, time);
                reminderList.add(reminder);
                saveReminders();
                customAdapter.notifyDataSetChanged();
                reminderTitle.setText("");
                reminderDescription.setText("");
                reminderDate.setText("");
                reminderTime.setText("");
                Toast.makeText(RemindersActivity.this, "Reminder added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RemindersActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            }
        });

        // Date picker for selecting date
        reminderDate.setOnClickListener(v -> {
            new DatePickerDialog(RemindersActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> reminderDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time picker for selecting time
        reminderTime.setOnClickListener(v -> {
            new TimePickerDialog(RemindersActivity.this,
                    (view, hourOfDay, minute) -> {
                        String ampm = (hourOfDay >= 12) ? "PM" : "AM";
                        int hour = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                        reminderTime.setText(hour + ":" + String.format("%02d", minute) + " " + ampm);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        });
    }

    // Save reminders to SharedPreferences
    private void saveReminders() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(reminderList);
        editor.putString("reminders", json);
        editor.apply();
    }

    // Load saved reminders from SharedPreferences
    private ArrayList<Reminder> loadReminders() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("reminders", null);
        Type type = new TypeToken<ArrayList<Reminder>>(){}.getType();
        return gson.fromJson(json, type);
    }

    // Reminder class to represent reminder objects
    public static class Reminder {
        String title, description, date, time;

        public Reminder(String title, String description, String date, String time) {
            this.title = title;
            this.description = description;
            this.date = date;
            this.time = time;
        }

        @Override
        public String toString() {
            return title + "\n" + description + "\n" + date + " " + time;
        }
    }

    // Custom adapter for reminder list
    // Inner class for the CustomAdapter
    private class CustomAdapter extends ArrayAdapter<Reminder> {

        CustomAdapter() {
            super(RemindersActivity.this, R.layout.reminder_item, reminderList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.reminder_item, parent, false);
            }

            TextView reminderText = convertView.findViewById(R.id.reminderText);
            Button deleteReminder = convertView.findViewById(R.id.deleteReminder);

            Reminder reminder = reminderList.get(position);
            reminderText.setText(reminder.toString());

            // Handle delete button click
            deleteReminder.setOnClickListener(v -> {
                reminderList.remove(position);
                saveReminders();
                notifyDataSetChanged();
                Toast.makeText(RemindersActivity.this, "Reminder deleted!", Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }
    }
}
*/

package com.example.patt;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import com.example.patt.AlarmReceiver;

public class RemindersActivity extends AppCompatActivity {

    private EditText reminderTitle, reminderDescription, reminderDate, reminderTime;
    private Button addReminder;
    private ListView reminderListView;
    private ArrayList<Reminder> reminderList;
    private CustomAdapter customAdapter;
    private SharedPreferences sharedPreferences;
    private static final String REMINDER_PREFS = "RemindersPrefs";
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        // Initialize UI components
        reminderTitle = findViewById(R.id.reminderTitle);
        reminderDescription = findViewById(R.id.reminderDescription);
        reminderDate = findViewById(R.id.reminderDate);
        reminderTime = findViewById(R.id.reminderTime);
        addReminder = findViewById(R.id.addReminder);
        reminderListView = findViewById(R.id.reminderListView);

        // Initialize shared preferences and reminder list
        sharedPreferences = getSharedPreferences(REMINDER_PREFS, MODE_PRIVATE);
        reminderList = loadReminders();
        if (reminderList == null) {
            reminderList = new ArrayList<>();
        }

        // Use the custom adapter
        customAdapter = new CustomAdapter();
        reminderListView.setAdapter(customAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("Please grant permission to set exact alarms for reminders.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        }

        // Add reminder logic
        addReminder.setOnClickListener(v -> {
            String title = reminderTitle.getText().toString();
            String description = reminderDescription.getText().toString();
            String date = reminderDate.getText().toString();
            String time = reminderTime.getText().toString();

            if (!title.isEmpty() && !description.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                // Convert date and time to Calendar object
                Calendar calendar = Calendar.getInstance();
                String[] dateParts = date.split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1; // month is 0-indexed
                int year = Integer.parseInt(dateParts[2]);

                String[] timeParts = time.split(":");
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1].split(" ")[0]); // removing AM/PM

                // Adjust hour for AM/PM
                if (time.contains("PM") && hour != 12) {
                    hour += 12; // Convert PM to 24-hour format
                } else if (time.contains("AM") && hour == 12) {
                    hour = 0; // Convert 12 AM to 00:00
                }

                calendar.set(year, month, day, hour, minute, 0);

                // Create a new reminder
                Reminder reminder = new Reminder(title, description, calendar);
                reminderList.add(reminder);
                saveReminders();
                customAdapter.notifyDataSetChanged();
                reminderTitle.setText("");
                reminderDescription.setText("");
                reminderDate.setText("");
                reminderTime.setText("");
                Toast.makeText(RemindersActivity.this, "Reminder added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RemindersActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            }

        });


        // Date picker for selecting date
        reminderDate.setOnClickListener(v -> {
            new DatePickerDialog(RemindersActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> reminderDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time picker for selecting time
        reminderTime.setOnClickListener(v -> {
            new TimePickerDialog(RemindersActivity.this,
                    (view, hourOfDay, minute) -> {
                        String ampm = (hourOfDay >= 12) ? "PM" : "AM";
                        int hour = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                        reminderTime.setText(hour + ":" + String.format("%02d", minute) + " " + ampm);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        });
    }

    // Save reminders to SharedPreferences
    private void saveReminders() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(reminderList);
        editor.putString("reminders", json);
        editor.apply();
    }

    // Load saved reminders from SharedPreferences
    private ArrayList<Reminder> loadReminders() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("reminders", null);
        Type type = new TypeToken<ArrayList<Reminder>>(){}.getType();
        return gson.fromJson(json, type);
    }

    // Reminder class to represent reminder objects
    public static class Reminder {
        String title, description;
        Calendar calendar;

        public Reminder(String title, String description, Calendar calendar) {
            this.title = title;
            this.description = description;
            this.calendar = calendar;
        }

        @Override
        public String toString() {
            // Check if calendar is null before calling getTime()
            if (calendar != null) {
                return title + "\n" + description + "\n" + calendar.getTime().toString();
            } else {
                return title + "\n" + description + "\n" + "Invalid Date";
            }
        }

        public Calendar getCalendar() {
            return calendar;
        }
    }


    // Custom adapter for reminder list
    // Inner class for the CustomAdapter
    private class CustomAdapter extends ArrayAdapter<Reminder> {

        CustomAdapter() {
            super(RemindersActivity.this, R.layout.reminder_item, reminderList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.reminder_item, parent, false);
            }

            TextView reminderText = convertView.findViewById(R.id.reminderText);
            Button deleteReminder = convertView.findViewById(R.id.deleteReminder);

            Reminder reminder = reminderList.get(position);
            reminderText.setText(reminder.toString());

            // Handle delete button click
            deleteReminder.setOnClickListener(v -> {
                reminderList.remove(position);
                saveReminders();
                notifyDataSetChanged();
                Toast.makeText(RemindersActivity.this, "Reminder deleted!", Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }
    }

    /*private void setAlarm(Reminder reminder) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(RemindersActivity.this, AlarmReceiver.class); // Create an intent for your alarm receiver
        PendingIntent pendingIntent = PendingIntent.getBroadcast(RemindersActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.getCalendar().getTimeInMillis(), pendingIntent);
        }

        // Optionally, you can show a toast or notification when the alarm is set
        Toast.makeText(this, "Alarm set for: " + reminder.getCalendar().getTime().toString(), Toast.LENGTH_SHORT).show();
    }*/

    private void setAlarm(Reminder reminder) {
        // Check for permission before setting the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(RemindersActivity.this, AlarmReceiver.class); // Intent for the alarm receiver
                PendingIntent pendingIntent = PendingIntent.getBroadcast(RemindersActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Set the alarm to trigger at the reminder's time
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.getCalendar().getTimeInMillis(), pendingIntent);
                }

                // Optionally, you can show a toast or perform other actions
                Toast.makeText(this, "Reminder set!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission not granted for setting alarms", Toast.LENGTH_SHORT).show();
            }
        } else {
            // For devices below Android 12, we can proceed without checking the permission
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(RemindersActivity.this, AlarmReceiver.class); // Intent for the alarm receiver
            PendingIntent pendingIntent = PendingIntent.getBroadcast(RemindersActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Set the alarm to trigger at the reminder's time
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.getCalendar().getTimeInMillis(), pendingIntent);
            }

            // Optionally, you can show a toast or perform other actions
            Toast.makeText(this, "Reminder set!", Toast.LENGTH_SHORT).show();
        }
    }
}

// Currently the notification or alarm set on the date and time collected is not working.
// Even after running on mobile, no use. Need to fix scrollview.
// Font appear lighter on mobile as compared to dark on emulator. Need to fix.