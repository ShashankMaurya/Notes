package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notes.Adapters.NotesListAdapter;
import com.example.notes.Database.RoomDB;
import com.example.notes.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes=new ArrayList<>();
    RoomDB database;
    SearchView searchView_home;
    Notes selectedNote, selectedNote_2;

    FloatingActionButton fab_add;
    String deletedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_home);
        fab_add=findViewById(R.id.fab_add);
        searchView_home=findViewById(R.id.searchView_home);

        database=RoomDB.getInstance(this);
        notes=database.mainDAO().getAll();

         updateRecycler(notes);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, NotesTakerActivity.class), 101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            selectedNote_2=new Notes();
            selectedNote_2=notes.get(position);

            switch (direction)
            {
                case ItemTouchHelper.LEFT:
//                    deletedNote=selectedNote_2.getTitle();
//                    database.mainDAO().delete(selectedNote_2);
//                    notes.remove(selectedNote_2);
//                    notesListAdapter.notifyDataSetChanged();
                    deleteNote(selectedNote_2);
//                    Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();

//                    Snackbar.make(recyclerView, "Note Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            database.mainDAO().insert(selectedNote_2);
//                            notes.clear();
//                            notes.addAll(database.mainDAO().getAll());
//                            notesListAdapter.notifyDataSetChanged();
//                        }
//                    }).show();
                    break;

                case ItemTouchHelper.RIGHT:
                    pinNote(selectedNote_2);
//                    if (selectedNote_2.isPinned()) {
//                        database.mainDAO().pin(selectedNote_2.getID(), false);
//                        Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        database.mainDAO().pin(selectedNote_2.getID(), true);
//                        Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
//                    }
//                    notes.clear();
//                    notes.addAll(database.mainDAO().getAll());
//                    notesListAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    private void filter(String newText) {
        List<Notes> filteredList=new ArrayList<>();
        for(Notes singleNote : notes)
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase()))
                filteredList.add(singleNote);
        notesListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101)
        {
            if(resultCode== Activity.RESULT_OK){
                Notes new_notes=(Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode==102)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                Notes new_notes= (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter=new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);

    }

    private final NotesClickListener notesClickListener=new NotesClickListener() {
        @Override
        public void OnClick(Notes notes) {
            Intent intent=new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void OnLongClick(Notes notes, CardView cardView) {
            selectedNote=new Notes();
            selectedNote=notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.pin:
//                if (selectedNote.isPinned()) {
//                    database.mainDAO().pin(selectedNote.getID(), false);
//                    Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    database.mainDAO().pin(selectedNote.getID(), true);
//                    Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
//                }
//                notes.clear();
//                notes.addAll(database.mainDAO().getAll());
//                notesListAdapter.notifyDataSetChanged();
                pinNote(selectedNote);
                return true;

            case R.id.delete:
//                database.mainDAO().delete(selectedNote);
//                notes.remove(selectedNote);
//                notesListAdapter.notifyDataSetChanged();
//                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                deleteNote(selectedNote);
                return true;

            default: return false;
        }
//        return false;
    }

    public void pinNote(Notes notes)
    {
        if (notes.isPinned()) {
            database.mainDAO().pin(notes.getID(), false);
            Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
        }
        else
        {
            database.mainDAO().pin(notes.getID(), true);
            Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
        }
        this.notes.clear();
        this.notes.addAll(database.mainDAO().getAll());
        notesListAdapter.notifyDataSetChanged();
    }

    public void deleteNote(Notes notes)
    {
        database.mainDAO().delete(notes);
        this.notes.remove(notes);
        notesListAdapter.notifyDataSetChanged();
//        Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
        Snackbar.make(recyclerView, "Note Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                database.mainDAO().insert(notes);
//                this.notes.clear();
//                this.notes.addAll(database.mainDAO().getAll());
//                notesListAdapter.notifyDataSetChanged();
                undoDelete(notes);
            }
        }).show();
    }

    public void undoDelete(Notes notes)
    {
        database.mainDAO().insert(notes);
        this.notes.clear();
        this.notes.addAll(database.mainDAO().getAll());
        notesListAdapter.notifyDataSetChanged();
    }
}