package com.example.sameer1.getlyrics;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class lyrics extends AppCompatActivity {
    Toolbar toolbar;
    TextView Song, Artist, Lyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        toolbar = (Toolbar) findViewById(R.id.about_app_toobar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Saved Lyrics");

        Song = (TextView) findViewById(R.id.SongName);
        Artist = (TextView) findViewById(R.id.ArtistName);
        Lyrics = (TextView) findViewById(R.id.SongLyrics);

        String s = getIntent().getExtras().getString("SongName");
        String a = getIntent().getExtras().getString("ArtistName");
        String l = getIntent().getExtras().getString("Lyrics");

        Song.setText(s);
        Artist.setText(a);
        Lyrics.setText(l);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

