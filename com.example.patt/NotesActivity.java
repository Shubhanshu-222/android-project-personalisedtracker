package com.example.patt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private EditText notesTitle, noteBody;
    private Button addNote;
    private ListView notesList;
    private NotesAdapter notesAdapter;
    private ArrayList<Note> notes;

    private static final String PREFS_NAME = "NotesAppPrefs";
    private static final String NOTES_KEY = "NotesList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Initialize views
        notesTitle = findViewById(R.id.notesTitle);
        noteBody = findViewById(R.id.noteBody);
        addNote = findViewById(R.id.addNote);
        notesList = findViewById(R.id.notesList);

        // Load notes from SharedPreferences
        notes = loadNotes();
        if (notes == null) {
            notes = new ArrayList<>();
        }

        // Initialize the adapter
        notesAdapter = new NotesAdapter(this, notes);
        notesList.setAdapter(notesAdapter);

        // Add note button action
        addNote.setOnClickListener(v -> {
            String title = notesTitle.getText().toString().trim();
            String body = noteBody.getText().toString().trim();

            if (title.isEmpty() || body.isEmpty()) {
                Toast.makeText(this, "Title and body cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                notes.add(new Note(title, body));
                notesAdapter.notifyDataSetChanged();
                saveNotes();
                notesTitle.setText("");
                noteBody.setText("");
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Save notes to SharedPreferences
    private void saveNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(notes);
        editor.putString(NOTES_KEY, json);
        editor.apply();
    }

    // Load notes from SharedPreferences
    private ArrayList<Note> loadNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(NOTES_KEY, null);
        Type type = new TypeToken<ArrayList<Note>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Note model class
    public static class Note {
        private final String title;
        private final String body;

        public Note(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }
    }

    // Custom adapter for the notes
    public static class NotesAdapter extends android.widget.BaseAdapter {
        private final Context context;
        private final ArrayList<Note> notes;

        public NotesAdapter(Context context, ArrayList<Note> notes) {
            this.context = context;
            this.notes = notes;
        }

        @Override
        public int getCount() {
            return notes.size();
        }

        @Override
        public Object getItem(int position) {
            return notes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.notes_item, parent, false);
            }

            // Get the current note
            Note currentNote = notes.get(position);

            // Initialize views in the item layout
            TextView noteText = convertView.findViewById(R.id.noteText);
            Button deleteNote = convertView.findViewById(R.id.deleteNote);

            // Set note data
            noteText.setText(String.format("%s\n%s", currentNote.getTitle(), currentNote.getBody()));

            // Delete button action
            deleteNote.setOnClickListener(v -> {
                notes.remove(position);
                notifyDataSetChanged();
                ((NotesActivity) context).saveNotes();
                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }
    }
}
