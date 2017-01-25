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
            "CREATE TABLE " + FeedEntry.TNAME_FEED + " (" +
            FeedEntry.FEED_CNAME_URL + " VARCHAR(255) PRIMARY KEY," +
            FeedEntry.FEED_CNAME_NAME + " VARCHAR(255)," +
            FeedEntry.ITEM_CNAME_DESC + " VARCHAR(255)," +
            FeedEntry.ITEM_CNAME_LINK + " VARCHAR(255));";

    private static final String SQL_CREATE_ITEM =
            "CREATE TABLE " + FeedEntry.TNAME_ITEM + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.ITEM_CNAME_TITLE + " VARCHAR(255)," +
                    FeedEntry.FEED_CNAME_DESC + " TEXT," +
                    FeedEntry.FEED_CNAME_LINK + " VARCHAR(255)," +
                    FeedEntry.ITEM_CNAME_DATE + " VARCHAR(255)," +
                    FeedEntry.ITEM_CNAME_FEED + " VARCHAR(255)," +
                    "FOREIGN KEY(" + FeedEntry.ITEM_CNAME_FEED + ") REFERENCES " + FeedEntry.TNAME_FEED + "(" + FeedEntry.FEED_CNAME_URL + "));";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TNAME_ITEM + "; " +
            "DROP TABLE IF EXISTS " + FeedEntry.TNAME_FEED + ";";

    private static RssReaderDbHelper sInstance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rssApp.db";

    //singleton
    public static synchronized RssReaderDbHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new RssReaderDbHelper(context.getApplicationContext());
        }
        return sInstance;

    }

    private RssReaderDbHelper(Context context) {
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
