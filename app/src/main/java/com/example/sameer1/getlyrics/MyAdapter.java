package com.example.sameer1.getlyrics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("IsWorking?",String.valueOf(mViewHolder.getPosition()));

            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Model SONG = demoData.get(position);
        holder.song.setText(SONG.getSongName());
        holder.album.setText(SONG.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return demoData.size();
    }
}
