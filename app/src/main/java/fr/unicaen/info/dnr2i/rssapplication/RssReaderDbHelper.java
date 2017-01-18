package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static fr.unicaen.info.dnr2i.rssapplication.RssReaderContract.FeedEntry;

/**
 * Created by plabadille on 18/01/17.
 */

public class RssReaderDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_FEED =
            "CREATE TABLE " + FeedEntry.TABLE2_NAME + " (" +
            FeedEntry.T2_C1_NAME + " VARCHAR(255) PRIMARY KEY," +
            FeedEntry.T2_C2_NAME + " VARCHAR(255)," +
            FeedEntry.T2_C3_NAME + " VARCHAR(255)," +
            FeedEntry.T2_C4_NAME + " VARCHAR(255));";

    private static final String SQL_CREATE_ITEM =
            "CREATE TABLE " + FeedEntry.TABLE1_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.T1_C1_NAME + " VARCHAR(255)," +
                    FeedEntry.T1_C2_NAME + " TEXT," +
                    FeedEntry.T1_C3_NAME + " VARCHAR(255)," +
                    FeedEntry.T1_C4_NAME + " VARCHAR(255))," +
                    FeedEntry.T1_C5_NAME + " VARCHAR(255)," +
                    "FOREIGN KEY(" + FeedEntry.T1_C5_NAME + ") REFERENCES " + FeedEntry.TABLE2_NAME + "(" + FeedEntry.T2_C1_NAME + "));";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE1_NAME + "; " +
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE2_NAME + ";";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rssApp.db";

    public RssReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FEED);
        db.execSQL(SQL_CREATE_ITEM);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //no upgrade, it's just cache for online data if update discard then start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
