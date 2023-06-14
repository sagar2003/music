package com.spy.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter_songs extends RecyclerView.Adapter<adapter_songs.Holder>{

    ArrayList<String> names,artist,paths;
    player player = new player().getInstance();
    Context  context;
    public adapter_songs(Context context,ArrayList<String> names,ArrayList<String> artist,ArrayList<String> paths){
        this.names=names;
        this.artist=artist;
        this.paths=paths;
        this.context=context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.adapter_list_song, parent, false);
        return new Holder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.name.setText(names.get(holder.getAdapterPosition()));
        holder.artist.setText(artist.get(holder.getAdapterPosition()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.play(context,paths.get(holder.getAdapterPosition()),holder.getAdapterPosition());
                MainActivity.updatecurrent(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    public class Holder extends RecyclerView.ViewHolder{

        TextView name,artist;
        LinearLayout layout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.txt_name);
            artist=itemView.findViewById(R.id.txt_artist);
            layout=itemView.findViewById(R.id.layout_song_perticular);

        }
    }
}
