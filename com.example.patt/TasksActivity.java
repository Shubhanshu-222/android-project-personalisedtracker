package com.example.patt;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TasksActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "tasks_prefs";
    private static final String TASKS_KEY = "tasks_list";
    private static final String COMPLETED_TASKS_KEY = "completed_tasks_list";

    private EditText taskTitle, taskDescription, taskDueDate;
    private Button addTaskButton;
    private ListView taskListView, completedTasksListView;
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Task> completedTasks = new ArrayList<>();
    private TaskAdapter taskAdapter, completedTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        // Initialize views
        taskTitle = findViewById(R.id.taskTitle);
        taskDescription = findViewById(R.id.taskDescription);
        taskDueDate = findViewById(R.id.taskDueDate);
        addTaskButton = findViewById(R.id.addTaskButton);
        taskListView = findViewById(R.id.taskListView);
        completedTasksListView = findViewById(R.id.completedTasksListView);

        // Load saved tasks
        loadTasks();

        // Setup Adapters
        taskAdapter = new TaskAdapter(tasks, false);
        completedTaskAdapter = new TaskAdapter(completedTasks, true);

        taskListView.setAdapter(taskAdapter);
        completedTasksListView.setAdapter(completedTaskAdapter);

        // DatePicker for Due Date
        taskDueDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TasksActivity.this, (datePicker, year, month, dayOfMonth) -> {
                String dueDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                taskDueDate.setText(dueDate);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Add Task Button Listener
        addTaskButton.setOnClickListener(view -> {
            String title = taskTitle.getText().toString().trim();
            String description = taskDescription.getText().toString().trim();
            String dueDate = taskDueDate.getText().toString().trim();

            // Validate that all fields are filled
            if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
                Toast.makeText(TasksActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create and add new task
            Task newTask = new Task(title, description, dueDate, getCurrentDate(), "Not Completed", "0:0:0:0");
            tasks.add(newTask);
            taskAdapter.notifyDataSetChanged();
            taskTitle.setText("");
            taskDescription.setText("");
            taskDueDate.setText("");

            // Save tasks after adding
            saveTasks();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save tasks when the activity is paused
        saveTasks();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save tasks when the activity is stopped
        saveTasks();
    }

    // Save tasks and completed tasks to SharedPreferences
    private void saveTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert tasks list to JSON using Gson
        Gson gson = new Gson();
        String tasksJson = gson.toJson(tasks);
        String completedTasksJson = gson.toJson(completedTasks);

        // Save JSON strings in SharedPreferences
        editor.putString(TASKS_KEY, tasksJson);
        editor.putString(COMPLETED_TASKS_KEY, completedTasksJson);
        editor.apply();
    }

    // Load tasks and completed tasks from SharedPreferences
    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Retrieve saved JSON strings
        String tasksJson = sharedPreferences.getString(TASKS_KEY, "[]");
        String completedTasksJson = sharedPreferences.getString(COMPLETED_TASKS_KEY, "[]");

        // Convert JSON strings back to lists using Gson
        Gson gson = new Gson();
        Type taskListType = new TypeToken<ArrayList<Task>>() {}.getType();
        tasks = gson.fromJson(tasksJson, taskListType);
        completedTasks = gson.fromJson(completedTasksJson, taskListType);
    }

    public void toggleCompletedTasksVisibility(View view) {
        LinearLayout completedTasksContainer = findViewById(R.id.completedTasksContainer);

        // Toggle the visibility of the completedTasksContainer
        if (completedTasksContainer.getVisibility() == View.GONE) {
            completedTasksContainer.setVisibility(View.VISIBLE);  // Expand
        } else {
            completedTasksContainer.setVisibility(View.GONE);  // Collapse
        }
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    // Custom Adapter for ListView
    private class TaskAdapter extends ArrayAdapter<Task> {

        private boolean isCompletedSection;

        public TaskAdapter(ArrayList<Task> items, boolean isCompletedSection) {
            super(TasksActivity.this, R.layout.task_item, items);
            this.isCompletedSection = isCompletedSection;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
            }

            Task task = getItem(position);

            if (task != null) {
                // Get views
                CheckBox taskCheck = convertView.findViewById(R.id.taskCheckbox);
                TextView taskText = convertView.findViewById(R.id.taskText);
                TextView taskDescriptionText = convertView.findViewById(R.id.taskDescriptionText);
                TextView taskDueDateText = convertView.findViewById(R.id.taskDueDateText);
                TextView taskDateCreatedText = convertView.findViewById(R.id.taskDateCreatedText);
                TextView taskDateCompletedText = convertView.findViewById(R.id.taskDateCompletedText);
                TextView taskTimeTakenText = convertView.findViewById(R.id.taskTimeTakenText);
                Button deleteTaskButton = convertView.findViewById(R.id.deleteTaskButton);

                // Task Title and Description
                taskText.setText(task.getTitle());
                taskDescriptionText.setText("Description: " + task.getDescription());
                taskDueDateText.setText("Due Date: " + task.getDueDate());

                if (isCompletedSection) {
                    taskCheck.setVisibility(View.VISIBLE);
                    taskCheck.setChecked(true); // Keep checkbox checked in completed section
                    taskDateCreatedText.setVisibility(View.VISIBLE);
                    taskDateCompletedText.setVisibility(View.VISIBLE);
                    taskTimeTakenText.setVisibility(View.VISIBLE);

                    taskDateCreatedText.setText("Created On: " + task.getDateCreated());
                    taskDateCompletedText.setText("Completed On: " + task.getDateCompleted());
                    taskTimeTakenText.setText("Time Taken: " + task.calculateTimeTaken(task.getDateCreated()));

                } else {
                    taskCheck.setVisibility(View.VISIBLE);
                    taskCheck.setChecked(false); // Ensure checkbox is unchecked for non-completed tasks
                    taskDateCreatedText.setVisibility(View.GONE);
                    taskDateCompletedText.setVisibility(View.GONE);
                    taskTimeTakenText.setVisibility(View.GONE);
                }

                taskCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked && !isCompletedSection) {
                        task.setStatus("Completed");
                        task.setDateCompleted(getCurrentDate());
                        task.setTimeTaken(task.calculateTimeTaken(task.getDateCreated()));
                        completedTasks.add(task);
                        tasks.remove(task);
                    } else if (!isChecked && isCompletedSection) {
                        task.setStatus("Not Completed");
                        task.setDateCompleted("");
                        task.setTimeTaken(String.valueOf(0));
                        tasks.add(task);
                        completedTasks.remove(task);
                    }

                    // Update the UI after status change
                    taskAdapter.notifyDataSetChanged();
                    completedTaskAdapter.notifyDataSetChanged();
                });

                // Delete Task Button Listener
                deleteTaskButton.setOnClickListener(v -> {
                    if (isCompletedSection) {
                        completedTasks.remove(task);
                        completedTaskAdapter.notifyDataSetChanged();
                    } else {
                        tasks.remove(task);
                        taskAdapter.notifyDataSetChanged();
                    }
                });
            }

            return convertView;
        }

    }

    public class Task {
        private String title;
        private String description;
        private String dueDate;
        private String dateCreated;
        private String dateCompleted;
        private String status;
        private String timeTaken; // Updated to String

        public Task(String title, String description, String dueDate, String dateCreated, String status, String timeTaken) {
            this.title = title;
            this.description = description;
            this.dueDate = dueDate;
            this.dateCreated = dateCreated;
            this.status = status;
            this.timeTaken = timeTaken;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getDateCompleted() {
            return dateCompleted;
        }

        public void setDateCompleted(String dateCompleted) {
            this.dateCompleted = dateCompleted;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
            if ("Completed".equals(status)) {
                setDateCompleted(getCurrentDate());
                this.timeTaken = calculateTimeTaken(dateCreated);
            } else {
                // Reset timeTaken and dateCompleted for tasks that are not completed
                this.dateCompleted = "";
                this.timeTaken = "0:0:0:0";
            }
        }

        public String getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(String timeTaken) {
            this.timeTaken = timeTaken;
        }

        private String getCurrentDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }

        private String calculateTimeTaken(String dateCreated) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date createdDate = sdf.parse(dateCreated);
                Date completedDate = new Date();

                long duration = completedDate.getTime() - createdDate.getTime();

                long days = duration / (1000 * 60 * 60 * 24);
                long hours = (duration / (1000 * 60 * 60)) % 24;
                long minutes = (duration / (1000 * 60)) % 60;
                long seconds = (duration / 1000) % 60;

                return days + ":" + hours + ":" + minutes + ":" + seconds;
            } catch (Exception e) {
                e.printStackTrace();
                return "0:0:0:0";
            }
        }
    }
}

//Need to fix the scroll view. Font appear white on mobile. Need to fix.
