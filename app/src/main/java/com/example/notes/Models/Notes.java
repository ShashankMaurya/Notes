package com.example.notes.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Notes implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int ID=0;

    @ColumnInfo(name="title")
    public String title="";

    @ColumnInfo(name="note")
    public String note="";

    @ColumnInfo(name="date")
    public String date="";

    @ColumnInfo(name="pinned")
    public boolean pinned=false;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return note;
    }

    public void setNotes(String notes) {
        this.note = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
