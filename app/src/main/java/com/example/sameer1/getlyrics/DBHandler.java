package com.example.sameer1.getlyrics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sameer1 on 16-04-2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MusicLyrics";

    private static final String TABLE_SONGS = "Songs";

    private static final String KEY_SONGNAME = "SongName";
    private static final String KEY_ARTISTNAME = "ArtistName";
    private static final String KEY_ALBUMNAME = "AlbumName";
    private static final String KEY_LYRICS = "Lyrics";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
                + KEY_SONGNAME + " TEXT UNIQUE PRIMARY KEY,"
                + KEY_ARTISTNAME + " TEXT,"
                + KEY_ALBUMNAME + " TEXT,"
                + KEY_LYRICS + " TEXT" + ")";

        db.execSQL(CREATE_SONGS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        onCreate(db);
    }




    public void addRecords(String SongName, String ArtistName, String AlbumName, String Lyrics) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SONGNAME, SongName);
        values.put(KEY_ARTISTNAME, ArtistName);
        values.put(KEY_ALBUMNAME, AlbumName);
        values.put(KEY_LYRICS, Lyrics);


        db.insert(TABLE_SONGS, null, values);
        db.close();
    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
        db.close();

        return rowCount;
    }

    public String[][] getSongs() {
        String data [][] ;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_SONGS;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        data = new String[4][count];
        int i = 0;
        while(count > 0) {
            data[0][i] = cursor.getString(0);
            data[1][i] = cursor.getString(1);
            data[2][i] = cursor.getString(2);
            data[3][i] = cursor.getString(3);
            i++;
            cursor.moveToNext();
            count--;
        }
        cursor.close();
        db.close();
        return data;
    }


    public void resetTable_Misc() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SONGS, null, null);
        db.close();
    }


}
