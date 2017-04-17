package com.example.sameer1.getlyrics;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class SavedLyrics extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static List<Model> demoData;

    public SavedLyrics() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_lyrics, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final DBHandler db = new DBHandler(getActivity().getApplicationContext());
        String[][] songDetails = db.getSongs();
        int rows = db.getRowCount();
        db.close();
        demoData = new ArrayList<Model>();
        for (int i = 0; i < rows; i++) {
            Model model = new Model();
            model.song_name = songDetails[0][i];
            model.artist_name = songDetails[1][i];
            model.album_name = songDetails[2][i];
            model.song_lyrics = songDetails[3][i];
            demoData.add(model);
        }
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(getActivity().getApplicationContext(), demoData);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

    }



}
