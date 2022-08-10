package com.example.notes;

import androidx.cardview.widget.CardView;

import com.example.notes.Models.Notes;

public interface NotesClickListener {
    void OnClick(Notes notes);
    void OnLongClick(Notes notes, CardView cardView);
}
