package com.example.sameer1.getlyrics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Sameer1 on 06-04-2017.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<Model> demoData;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView song, album;

        public MyViewHolder(View view) {
            super(view);
            song = (TextView) view.findViewById(R.id.song_name);
            album = (TextView) view.findViewById(R.id.album_name);

        }

    }


    public MyAdapter(Context mContext, List<Model> demoData){
        this.mContext = mContext;
        this.demoData = demoData;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        final MyViewHolder mViewHolder = new MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Model SONG = demoData.get(position);
        holder.song.setText(SONG.getSongName());
        holder.album.setText(SONG.getAlbumName());
        holder.song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, lyrics.class);
                intent.putExtra("SongName",SONG.getSongName());
                intent.putExtra("ArtistName",SONG.getArtist_name());
                intent.putExtra("AlbumName",SONG.getAlbumName());
                intent.putExtra("Lyrics",SONG.getSongLyrics());
                v.getContext().startActivity(intent);
            }
        });
        holder.song.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final DBHandler db = new DBHandler(mContext);
                db.deleteSong(SONG.getSongName());
                db.close();
                Toast.makeText(mContext, "Song Deleted !!", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return demoData.size();
    }
}
