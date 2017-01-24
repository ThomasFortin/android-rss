package fr.unicaen.info.dnr2i.rssapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import static fr.unicaen.info.dnr2i.rssapplication.RssReaderContract.FeedEntry;

/**
 * A class to manage the database with CRUD operations
 * @author Pierre Labadille
 * @since 2016-01-18
 */

public class RssReaderManager {

    SQLiteDatabase db;
    RssReaderDbHelper rDbHelper;

    public RssReaderManager(Context context) {
        this.rDbHelper = new RssReaderDbHelper(context);
    }

    //ACCES OPERATION
    public void open() {
        this.db = this.rDbHelper.getWritableDatabase();
    }

    public void close() {
        this.db.close();
    }

    //CRUD OPERATION IN THE DATABASE

    //C OPERATIONS  ---------------------------------------

    /**
     * Method used to add a new Feed in the database
     */
    public void addFeed(RssFeed feed) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.T2_C1_NAME, feed.getUrl());
        values.put(FeedEntry.T2_C2_NAME, feed.getName());
        values.put(FeedEntry.T2_C3_NAME, feed.getDescription());
        values.put(FeedEntry.T2_C4_NAME, feed.getLink());

        this.db.insert(FeedEntry.TABLE2_NAME, null, values);
    }

    /**
     * Method used to add a new Item in the database
     * @param title <String> The title of the new item
     * @param description <String> The description of the new item
     * @param link <String> The link of the new item
     * @param pubDate <String> The publication date of the new item
     * @param feed <String> The url of the associated Feed (Foreign Key)
     */
    public void addItem(String title, String description, String link, String pubDate, String feed) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.T1_C1_NAME, title);
        values.put(FeedEntry.T1_C2_NAME, description);
        values.put(FeedEntry.T1_C3_NAME, link);
        values.put(FeedEntry.T1_C4_NAME, pubDate);
        values.put(FeedEntry.T1_C5_NAME, feed);

        this.db.insert(FeedEntry.TABLE1_NAME, null, values);
    }

    //R OPERATIONS  ---------------------------------------


    /**
     * Method used to get a feed by url from the database
     * @param url <String> The url of the feed (Primary Key)
     * @return Cursor Object containing the response of the query
     */
    public Cursor getOneFeedByUrl(String url) {
        //not sure it works... this is a test
        String query = "SELECT * from " + FeedEntry.TABLE2_NAME + " WHERE " + FeedEntry.T2_C1_NAME + " = " + url + ";";
        return this.db.rawQuery(query, null);
    }

    /**
     * Method used to get all the feeds from the database
     * @return Cursor Object containing the response of the query
     */
    public Cursor getAllFeed() {
        String query = "SELECT * from " + FeedEntry.TABLE2_NAME;
        return this.db.rawQuery(query, null);
    }

    /**
     * Method used to get an item by is Id from the database
     * @param id <int> The id of the item (Primary Key)
     * @return Cursor Object containing the response of the query
     */
    public Cursor getOneItemById(int id) {
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.T1_C1_NAME
        };

        String selection = FeedEntry._ID;
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = this.db.query(
                FeedEntry.TABLE1_NAME,   //table to query
                projection,             //columns to return
                selection,              //columns for the WHERE clause
                selectionArgs,          //The values for the WHERE clause
                null,                   //don't group the rows
                null,                   //don't filter by row groups
                null                    //The sort order
        );

        return cursor;
    }

    /**
     * Method used to get all items associated with a specific feed from the database
     * @param url <String> The url of the associated feed
     * @return Cursor Object containing the response of the query
     */
    public Cursor getAllItemFromFeed(String url) {
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.T1_C1_NAME,
                FeedEntry.T1_C2_NAME,
                FeedEntry.T1_C3_NAME,
                FeedEntry.T1_C4_NAME,
                FeedEntry.T1_C5_NAME
        };

        String selection = FeedEntry.T1_C5_NAME + "=?";
        String[] selectionArgs = { url };

        Cursor cursor = this.db.query(
                FeedEntry.TABLE1_NAME,   //table to query
                projection,             //columns to return
                selection,              //columns for the WHERE clause
                selectionArgs,          //The values for the WHERE clause
                null,                   //don't group the rows
                null,                   //don't filter by row groups
                null                    //The sort order
        );

        return cursor;
    }

    public long countFeed() {
        long cnt  = DatabaseUtils.queryNumEntries(this.db, FeedEntry.TABLE2_NAME);
        db.close();
        return cnt;
    }

    public long countItem() {
        long cnt  = DatabaseUtils.queryNumEntries(this.db, FeedEntry.TABLE1_NAME);
        db.close();
        return cnt;
    }

    //U OPERATIONS  ---------------------------------------

    /**
     * Method used to update a Feed
     * The unchanged param have to be set to null when calling. Update the URL WILL delete every associated item.
     * @param actualUrl <String> The URL of the Feed to update
     * @param newUrl <String> [OPTIONAL, null if not updated] The new url of the feed
     * @param name <String> [OPTIONAL, null if not updated] The new name of the feed
     * @param description <String> [OPTIONAL, null if not updated] The new description of the feed
     * @param link <String> [OPTIONAL, null if not updated] The new link of the feed
     */
    public void updateFeed(String actualUrl, String newUrl, String name, String description, String link) {
        ContentValues values = new ContentValues();
        if (newUrl != null) {
            deleteItemOfFeed(newUrl);
            values.put(FeedEntry.T2_C1_NAME, newUrl);
        }
        if (name != null) {
            values.put(FeedEntry.T2_C2_NAME, name);
        }
        if (description != null) {
            values.put(FeedEntry.T2_C3_NAME, description);
        }
        if (link != null) {
            values.put(FeedEntry.T2_C4_NAME, link);
        }

        String selection = FeedEntry.T2_C1_NAME + "=" + actualUrl;
        this.db.update(FeedEntry.TABLE2_NAME, values, selection, null);
    }

    //D OPERATIONS  ---------------------------------------

    /**
     * Method used to delete a feed
     * @param url <String> The url of the feed to delete
     */
    public void deleteFeed(String url) {
        //first we need to delete associated item
        deleteItemOfFeed(url);
        //then we can delete the feed
        String selection = FeedEntry.T2_C2_NAME + " = ?";
        String[] selectionArgs = { url };
        this.db.delete(FeedEntry.TABLE2_NAME, selection, selectionArgs);
    }

    /**
     * Method used to delete an item
     * @param id <int> The id of the item to delete
     */
    public void deleteItem(int id) {
        String selection = FeedEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        this.db.delete(FeedEntry.TABLE1_NAME, selection, selectionArgs);
    }

    /**
     * Method used to delete all items of a field
     * @param url <String> The url of the associated feed
     */
    public void deleteItemOfFeed(String url) {
        String selection = FeedEntry.T1_C5_NAME + " = ?";
        String[] selectionArgs = {url};
        this.db.delete(FeedEntry.TABLE1_NAME, selection, selectionArgs);
    }

}
