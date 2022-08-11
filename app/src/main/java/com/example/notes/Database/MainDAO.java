package com.example.notes.Database;


import static androidx.room.OnConflictStrategy.REPLACE;

import android.provider.ContactsContract;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.notes.Models.Notes;

import java.util.List;

@Dao
public interface MainDAO {

    @Insert(onConflict=REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY ID DESC")
    List<Notes> getAll();

    @Query("UPDATE notes SET title= :title, note=:notes WHERE ID= :id")
    void update(int id, String title, String notes);

    @Query("UPDATE notes SET pinned= :pin WHERE ID = :id")
    void pin(int id, boolean pin);

    @Delete
    void delete(Notes notes);

}
