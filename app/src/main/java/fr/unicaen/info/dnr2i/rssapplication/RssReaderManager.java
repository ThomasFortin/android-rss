package fr.unicaen.info.dnr2i.rssapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static fr.unicaen.info.dnr2i.rssapplication.RssReaderContract.FeedEntry;

/**
 * A class to manage the database with CRUD operations
 * @author Pierre Labadille
 * @since 2016-01-18
 */

public final class RssReaderManager {

    //CRUD OPERATION IN THE DATABASE

    //C OPERATIONS  ---------------------------------------

    /**
     * Method used to add a new Feed in the database
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @param url <String> The url of the new Feed (Primary Key)
     * @param name <String> The name of the new Feed
     * @param description <String> The description of the new Feed
     * @param link <String> The link of the new Feed
     */
    public static void addFeed(SQLiteDatabase db, String url, String name, String description, String link) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.T2_C1_NAME, url);
        values.put(FeedEntry.T2_C2_NAME, name);
        values.put(FeedEntry.T2_C3_NAME, description);
        values.put(FeedEntry.T2_C4_NAME, link);

        db.insert(FeedEntry.TABLE2_NAME, null, values);
    }

    /**
     * Method used to add a new Item in the database
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @param title <String> The title of the new item
     * @param description <String> The description of the new item
     * @param link <String> The link of the new item
     * @param pubDate <String> The publication date of the new item
     * @param feed <String> The url of the associated Feed (Foreign Key)
     */
    public static void addItem(SQLiteDatabase db, String title, String description, String link, String pubDate, String feed) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.T1_C1_NAME, title);
        values.put(FeedEntry.T1_C2_NAME, description);
        values.put(FeedEntry.T1_C3_NAME, link);
        values.put(FeedEntry.T1_C4_NAME, pubDate);
        values.put(FeedEntry.T1_C5_NAME, feed);

        db.insert(FeedEntry.TABLE1_NAME, null, values);
    }

    //R OPERATIONS  ---------------------------------------


    /**
     * Method used to get a feed by url from the database
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @param url <String> The url of the feed (Primary Key)
     * @return Cursor Object containing the response of the query
     */
    public static Cursor getOneFeedByUrl(SQLiteDatabase db, String url) {
        //not sure it works... this is a test
        String query = "SELECT * from " + FeedEntry.TABLE2_NAME + " WHERE " + FeedEntry.T2_C1_NAME + " = " + url + ";";
        return db.rawQuery(query, null);
    }

    /**
     * Method used to get all the feeds from the database
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @return Cursor Object containing the response of the query
     */
    public static Cursor getAllFeed(SQLiteDatabase db) {
        String query = "SELECT * from " + FeedEntry.TABLE2_NAME;
        return db.rawQuery(query, null);
    }

    /**
     * Method used to get an item by is Id from the database
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @param id <int> The id of the item (Primary Key)
     * @return Cursor Object containing the response of the query
     */
    public static Cursor getOneItemById(SQLiteDatabase db, int id) {
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.T1_C1_NAME
        };

        String selection = FeedEntry._ID;
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
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
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @param url <String> The url of the associated feed
     * @return Cursor Object containing the response of the query
     */
    public static Cursor getAllItemFromFeed(SQLiteDatabase db, String url) {
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.T1_C1_NAME,
                FeedEntry.T1_C2_NAME,
                FeedEntry.T1_C3_NAME,
                FeedEntry.T1_C4_NAME,
                FeedEntry.T1_C5_NAME
        };

        String selection = FeedEntry.T1_C5_NAME;
        String[] selectionArgs = { url };

        Cursor cursor = db.query(
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

    //U OPERATIONS  ---------------------------------------

    /**
     * Method used to update a Feed
     * The unchanged param have to be set to null when calling. Update the URL WILL delete every associated item.
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @param actualUrl <String> The URL of the Feed to update
     * @param newUrl <String> [OPTIONAL, null if not updated] The new url of the feed
     * @param name <String> [OPTIONAL, null if not updated] The new name of the feed
     * @param description <String> [OPTIONAL, null if not updated] The new description of the feed
     * @param link <String> [OPTIONAL, null if not updated] The new link of the feed
     */
    public static void updateFeed(SQLiteDatabase db, String actualUrl, String newUrl, String name, String description, String link) {
        ContentValues values = new ContentValues();
        if (newUrl != null) {
            deleteItemOfFeed(db, newUrl);
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
        db.update(FeedEntry.TABLE2_NAME, values, selection, null);
    }

    //D OPERATIONS  ---------------------------------------

    /**
     * Method used to delete a feed
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @param url <String> The url of the feed to delete
     */
    public static void deleteFeed(SQLiteDatabase db, String url) {
        //first we need to delete associated item
        deleteItemOfFeed(db, url);
        //then we can delete the feed
        String selection = FeedEntry.T2_C2_NAME + " = ?";
        String[] selectionArgs = { url };
        db.delete(FeedEntry.TABLE2_NAME, selection, selectionArgs);
    }

    /**
     * Method used to delete an item
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @param id <int> The id of the item to delete
     */
    public static void deleteItem(SQLiteDatabase db, int id) {
        String selection = FeedEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.delete(FeedEntry.TABLE1_NAME, selection, selectionArgs);
    }

    /**
     * Method used to delete all items of a field
     * @param db <SQLiteDatabase> Containing the instance of the writable database
     * @param url <String> The url of the associated feed
     */
    public static void deleteItemOfFeed(SQLiteDatabase db, String url) {
        String selection = FeedEntry.T1_C5_NAME + " = ?";
        String[] selectionArgs = {url};
        db.delete(FeedEntry.TABLE1_NAME, selection, selectionArgs);
    }

}
