package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notes.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {

    EditText edittext_title, edittext_notes;
    ImageView imageview_save;
    Notes notes;
    boolean isOldNote=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        imageview_save=findViewById(R.id.imageview_save);
        edittext_title=findViewById(R.id.edittext_title);
        edittext_notes=findViewById(R.id.edittext_notes);

        notes=new Notes();
//        getSupportActionBar().setHomeButtonEnabled(true);

        try {
            notes= (Notes) getIntent().getSerializableExtra("old_note");
            edittext_title.setText(notes.getTitle());
            edittext_notes.setText(notes.getNotes());
            isOldNote=true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        imageview_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title=edittext_title.getText().toString();
                String description=edittext_notes.getText().toString();

                if(description.isEmpty()) {
                    Toast.makeText(NotesTakerActivity.this, "Please add some notes", Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat sdf=new SimpleDateFormat("EEE d MMM yyyy HH:mm a");
                Date date=new Date();

                if(!isOldNote)
                notes=new Notes();

                notes.setNotes(description);
                notes.setTitle(title);
                notes.setDate(sdf.format(date));

                Intent intent=new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}