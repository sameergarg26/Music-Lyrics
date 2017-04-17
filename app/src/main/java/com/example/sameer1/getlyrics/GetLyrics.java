package com.example.sameer1.getlyrics;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GetLyrics extends Fragment {


    TextView lyrics;
    FloatingActionButton refresh;
    LinearLayout nothingLayout;
    String song, artist, album;
    AlertDialog dialog;
    public GetLyrics() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get_lyrics, container, false);
        lyrics = (TextView) v.findViewById(R.id.lyrics);
        lyrics.setVisibility(View.GONE);
        nothingLayout = (LinearLayout) v.findViewById(R.id.nothingLayout);
        MainActivity activity = (MainActivity) getActivity();


        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");

        getActivity().registerReceiver(mReceiver, iF);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater2 = getActivity().getLayoutInflater();
        builder.setView(inflater2.inflate(R.layout.dialog, null));
        dialog = builder.create();


        Log.v("IN On CREATE", artist + ":" + album + ":" + song);

        refresh = activity.getButton();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;

                if(connected){
                    //Log.d("SONG", song);
                    TheTask to_execute= new TheTask();
                    to_execute.execute("");

                }
                else{
                    Snackbar.make(v,"Please Connect to the Internet !!",Snackbar.LENGTH_SHORT).show();
                }



            }
        });

        return v;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");
            //Log.v("tag ", action + " / " + cmd);

                artist = intent.getStringExtra("artist");
                album = intent.getStringExtra("album");
                song = intent.getStringExtra("track");

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.current_song), song);
            editor.putString(getString(R.string.current_album), album);
            editor.putString(getString(R.string.current_artist), artist);
            editor.commit();
            //Log.v("IN On RECEIVE", artist + ":" + album + ":" + song);
        }

    };

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();

    }


    public class TheTask extends AsyncTask<String,Void,String> {
        String Song, Album, Artist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            Song = sharedPref.getString(getString(R.string.current_song), "");
            Album = sharedPref.getString(getString(R.string.current_album), "");
            Artist = sharedPref.getString(getString(R.string.current_artist), "");
            //String Song = params[0];
            Song = Song.replaceAll("-"," ");
            Song = Song.replaceAll("_"," ");
            Song = Song.replaceAll("\\(.*\\)", "");
            Song = Song.replaceAll("[-+.^:,';]","");

            Artist = Artist.replaceAll("-"," ");
            Artist = Artist.replaceAll("_"," ");
            Artist = Artist.replaceAll("\\(.*\\)", "");
            Artist = Artist.replaceAll("[-+.^:,';]","");
            String temp = Song.concat(" " + Artist);
            temp = temp.replaceAll(" ","+");

            String question = new String();
            String url = "https://search.letssingit.com/?a=search&artist_id=&l=archive&s="+temp;
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
            if(result != ""){
                final DBHandler db = new DBHandler(getActivity().getApplicationContext());
                db.addRecords(Song, Artist, Album, result);
                db.close();
            }

            nothingLayout.setVisibility(View.GONE);
            lyrics.setVisibility(View.VISIBLE);
            dialog.cancel();
            lyrics.setText(result);
        }
    }


}
