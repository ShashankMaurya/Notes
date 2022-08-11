package com.example.notes.Adapters;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Models.Notes;
import com.example.notes.NotesClickListener;
import com.example.notes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    Context context;
    List<Notes> list;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textview_title.setText(list.get(position).getTitle());
        holder.textview_title.setSelected(true);

        holder.textview_notes.setText(list.get(position).getNotes());

        holder.textview_date.setText(list.get(position).getDate());
        holder.textview_date.setSelected(true);

        if(list.get(position).isPinned())
            holder.imageview_pin.setImageResource(R.drawable.pin);
        else
            holder.imageview_pin.setImageResource(0);

        int color_code=getRandomColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code));

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.OnLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });
    }

    private int getRandomColor(){
        List<Integer> colorCode=new ArrayList<>();

        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);

        Random random = new Random();
        return colorCode.get(random.nextInt(colorCode.size()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder{

    CardView notes_container;
    TextView textview_title, textview_notes, textview_date;
    ImageView imageview_pin;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notes_container=itemView.findViewById(R.id.notes_container);
        textview_title=itemView.findViewById(R.id.textview_title);
        textview_notes=itemView.findViewById(R.id.textview_notes);
        textview_date=itemView.findViewById(R.id.textview_date);
        imageview_pin=itemView.findViewById(R.id.imageview_pin);

    }
}
