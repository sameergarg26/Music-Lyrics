package com.example.sameer1.getlyrics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.R.attr.button;
import static com.example.sameer1.getlyrics.R.layout.dialog;

public class SearchLyrics extends AppCompatActivity {

    Toolbar toolbar;
    EditText song, album, artist;
    Button search;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lyrics);

        toolbar = (Toolbar) findViewById(R.id.about_app_toobar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Search for Lyrics");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater2 = getLayoutInflater();
        builder.setView(inflater2.inflate(R.layout.dialog, null));
        dialog = builder.create();

        song = (EditText) findViewById(R.id.songName);
        artist = (EditText) findViewById(R.id.artistName);
        album = (EditText) findViewById(R.id.albumName);

        song.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable et) {

                if(song.getText().length()>0)
                {
                    search.setAlpha(1);
                    search.setClickable(true);
                }
                else{
                    search.setAlpha(Float.valueOf("0.3"));
                    search.setClickable(false);
                }


            }
        });


        search = (Button) findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String song_name = song.getText().toString();
                String album_name = album.getText().toString();
                String artist_name = artist.getText().toString();
                String[] array = {song_name, album_name, artist_name};

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;

                if(connected){
                    NewTask task = new NewTask();
                    task.execute(array);
                }
                else{
                    Snackbar.make(v,"Please Connect to the Internet !!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
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

    public class NewTask extends AsyncTask<String,Void,String> {
        String Song = " ", Album = " ", Artist = " ";
        String Fsong, Falbum, Fartist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            Song = params[0];
            Album = params[1];
            Artist = params[2];

            Song = Song.replaceAll("-"," ");
            Song = Song.replaceAll("_"," ");
            Song = Song.replaceAll("'","");
            Song = Song.replaceAll("\\(.*\\)", "");
            Song = Song.replaceAll("[-+.^:,';]","");
            Fsong = Song;
            //Log.d("Song", Song);
            Artist = Artist.replaceAll("-"," ");
            Artist = Artist.replaceAll("_"," ");
            Artist = Artist.replaceAll("\\(.*\\)", "");
            Artist = Artist.replaceAll("[-+.^:,';]","");
            Artist = Artist.replaceAll("<[^>]*>", " ");
            Fartist = Artist;
            //Log.d("Artist", Artist);
            Song = Song.concat(" " + Artist);
            Song = Song.replaceAll(" ","+");

            String question = new String();
            String url = "https://search.letssingit.com/?a=search&artist_id=&l=archive&s="+Song;
            //Log.d("ÜRL", url);
            //String url = "http://www.metrolyrics.com/search.html?search=flares";
            //url="https://www.newstelecom.info/2016/08/jiofi-complete-setup-faq/#.WOfSWFV95PZ";
            try {
                Document doc = Jsoup.connect(url).get();
                //question = doc.select("table").first().text();
                //Log.d("Table", question);

                Elements links = doc.select("a");
                int i=0;
                for (Element link : links) {
                    i++;
                    //Log.d("Link", String.valueOf(i) + " " + link.attr("href"));
                    if(i>72){
                        question = link.attr("href");
                        if(question.charAt(0) == 'h'){
                            break;
                        }
                    }
                }

                if(question.equals("#")){
                    return "Nothing Found";
                }

                Document finalDoc = Jsoup.connect(question).get();
                Elements element = finalDoc.select("div#lyrics");
                question = element.html();
                question = question.replaceAll("<[^>]*>", " ");
                //Log.d("Lyrics", element.text());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return question;
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.d("value",result);
            dialog.dismiss();
            if(result.equals("Nothing Found") || result.equals("")){
                Toast.makeText(getApplicationContext(), "No Results Found", Toast.LENGTH_LONG).show();
                return;
            }

            final DBHandler db = new DBHandler(getApplicationContext());
            db.addRecords(Fsong, Fartist, Album, result);
            db.close();



            Intent intent = new Intent(getApplicationContext(), lyrics.class);
            intent.putExtra("SongName",Fsong);
            intent.putExtra("ArtistName",Fartist);
            intent.putExtra("AlbumName",Album);
            intent.putExtra("Lyrics",result);
            startActivity(intent);
        }
    }

}
