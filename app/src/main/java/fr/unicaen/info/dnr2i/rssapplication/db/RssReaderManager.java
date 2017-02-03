package fr.unicaen.info.dnr2i.rssapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fr.unicaen.info.dnr2i.rssapplication.db.RssReaderDbHelper;
import fr.unicaen.info.dnr2i.rssapplication.entity.RssFeed;
import fr.unicaen.info.dnr2i.rssapplication.entity.RssItem;

import static fr.unicaen.info.dnr2i.rssapplication.db.RssReaderContract.FeedEntry;

/**
 * A class to manage the database with CRUD operations
 * @author Pierre Labadille
 * @since 2016-01-18
 */

public class RssReaderManager {

    private SQLiteDatabase db;
    private RssReaderDbHelper rDbHelper;
    // DATA TEST USED by generateDevBd()
    private String[] titles = new String[]{
            "Chèvre", "Bouquetin", "Cygne", "Tigre",
            "Écureuil", "Ratel", "Chien", "Paresseux",
            "Pie", "Chat", "Lion", "Dindon"
    };

    public RssReaderManager(Context context) {
        this.rDbHelper = RssReaderDbHelper.getInstance(context);
        this.db = this.rDbHelper.getWritableDatabase();
        //first time we generate a test DB (for test only)
        if (this.countFeed() == 0){
            this.generateDevBd();
        }
    }

    //-----------------------------------------------------
    //-----------------------------------------------------
    //INIT
    //-----------------------------------------------------
    //-----------------------------------------------------

    /**
     * Method used to generate a test db when none already exist
     * @deprecated This method should not exist in final build, it's only purpose is to test the apps.
     */
    private void generateDevBd() {
        String url;
        String name;
        String description;
        String link;
        RssFeed feed;
        for (int i=0; i<this.titles.length; i++) {
            name = this.titles[i];
            url = "http://"+name+".com/rss/"+name+".xml";
            description = "All the news on the beautiful life of "+name;
            link = "http://"+name+".com";
            feed = new RssFeed(url, name, description, link);
            this.addFeed(feed);
        }
    }

    //-----------------------------------------------------
    //-----------------------------------------------------
    // TOOLS
    //-----------------------------------------------------
    //-----------------------------------------------------

    /**
     * Method used to transform a Cursor in a list of RssFeed
     * @param c <Cursor> contain the Cursor iterator result of a query
     * @return List<RssFeed> the result of the query as a RssFeed list
     */
    private List<RssFeed> makeFeedsArrayWithCursor(Cursor c) {
        //init feeds variable
        String url;
        String name;
        String description;
        String link;
        RssFeed feed;
        //init list
        List<RssItem> items;
        List<RssFeed> feeds = new ArrayList<>();

        if (c.moveToFirst()) {
            int i = 0;
            do {
                //get the feed
                url = c.getString(c.getColumnIndex(FeedEntry.FEED_CNAME_URL));
                name = c.getString(c.getColumnIndex(FeedEntry.FEED_CNAME_NAME));
                description = c.getString(c.getColumnIndex(FeedEntry.FEED_CNAME_DESC));
                link = c.getString(c.getColumnIndex(FeedEntry.FEED_CNAME_LINK));
                feed = new RssFeed(url, name, description, link);
                feeds.add(feed);

                //get the associated item
                items = this.getAllItemFromFeed(url);
                feeds.get(i).setItems(items);

                i++;
            } while (c.moveToNext());
        }
        c.close();
        return feeds;
    }

    /**
     * Method used to transform a Cursor in a list of RssItem
     * @param c <Cursor> contain the Cursor iterator result of a query
     * @return List<RssItem> the result of the query as a RssItem list
     */
    private List<RssItem> makeItemArrayWithCursor(Cursor c) {
        //init items variable
        RssItem item;
        String title;
        String descItem;
        String linkItem;
        String pubDate;
        //init list
        List<RssItem> items = new ArrayList<>();

        if (c.moveToFirst()) {
            int i = 0;
            do {
                title = c.getString(c.getColumnIndex(FeedEntry.ITEM_CNAME_TITLE));
                descItem = c.getString(c.getColumnIndex(FeedEntry.ITEM_CNAME_DESC));
                linkItem = c.getString(c.getColumnIndex(FeedEntry.ITEM_CNAME_LINK));
                pubDate = c.getString(c.getColumnIndex(FeedEntry.ITEM_CNAME_DATE));
                item = new RssItem(title, descItem, linkItem, pubDate);
                items.add(item);
            } while (c.moveToNext());
        }
        c.close();
        return items;
    }

    //-----------------------------------------------------
    //-----------------------------------------------------
    //CRUD OPERATION IN THE DATABASE
    //-----------------------------------------------------
    //-----------------------------------------------------

    //C OPERATIONS  ---------------------------------------

    /**
     * Method used to add a new Feed in the database
     * @param feed <RssFeed> contain the instance of the new feed to add in db
     */
    public void addFeed(RssFeed feed) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.FEED_CNAME_URL, feed.getUrl());
        values.put(FeedEntry.FEED_CNAME_NAME, feed.getName());
        values.put(FeedEntry.FEED_CNAME_DESC, feed.getDescription());
        values.put(FeedEntry.FEED_CNAME_LINK, feed.getLink());

        this.db.insert(FeedEntry.TNAME_FEED, null, values);

        //add the new items
        this.addItemsToFeed(feed);
    }

    /**
     * Method used to add a new Item in the database
     * @param item <RssItem> contain the instance of the new item to add in db
     * @param feed <String> url of the feed where the item belong
     *
     */
    public void addItem(RssItem item, String feed) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.ITEM_CNAME_TITLE, item.getTitle());
        values.put(FeedEntry.ITEM_CNAME_DESC, item.getDescription());
        values.put(FeedEntry.ITEM_CNAME_LINK, item.getLink());
        values.put(FeedEntry.ITEM_CNAME_DATE, item.getPubDate());
        values.put(FeedEntry.ITEM_CNAME_FEED, feed);

        this.db.insert(FeedEntry.TNAME_ITEM, null, values);
    }

    public void addItemsToFeed(RssFeed feed) {
        List<RssItem> items = feed.getItems();
        if (items != null) {
            for (RssItem item : items) {
                this.addItem(item, feed.getUrl());
            }
        }
    }

    //R OPERATIONS  ---------------------------------------

    /**
     * Method used to get a feed by url from the database
     * @param url <String> The url of the feed (Primary Key)
     * @return RssFeed the answer of the query
     */
    public RssFeed getOneFeedByUrl(String url) {
        String query = "SELECT * from " + FeedEntry.TNAME_FEED + " WHERE " + FeedEntry.FEED_CNAME_URL + " = \"" + url + "\";";

        Cursor cursor = this.db.rawQuery(query, null);

        return this.makeFeedsArrayWithCursor(cursor).get(0);
    }

    /**
     * Method used to get all the feeds from the database
     * @return List<RssFeed> the answer of the query
     */
    public List<RssFeed> getAllFeeds() {
        String query = "SELECT * from " + FeedEntry.TNAME_FEED;

        Cursor cursor = this.db.rawQuery(query, null);
        List<RssFeed> feeds = this.makeFeedsArrayWithCursor(cursor);

        return feeds;
    }

    /**
     * Method used to check if a feed exist in the db
     * @param url <String> The url of the feed (Primary Key)
     * @return <boolean>
     */
    public boolean feedExist(String url) {
        String query = "SELECT * from " + FeedEntry.TNAME_FEED + " WHERE " + FeedEntry.FEED_CNAME_URL + " = " + url + ";";

        Cursor cursor = this.db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    /**
     * Method used to get an item by is Id from the database
     * @param id <int> The id of the item (Primary Key)
     * @return RssItem the answer of the query
     */
    public RssItem getOneItemById(int id) {
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.ITEM_CNAME_TITLE
        };

        String selection = FeedEntry._ID;
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = this.db.query(
                FeedEntry.TNAME_ITEM,   //table to query
                projection,             //columns to return
                selection,              //columns for the WHERE clause
                selectionArgs,          //The values for the WHERE clause
                null,                   //don't group the rows
                null,                   //don't filter by row groups
                null                    //The sort order
        );

        return this.makeItemArrayWithCursor(cursor).get(0);
    }

    /**
     * Method used to get all items associated with a specific feed from the database
     * @param url <String> The url of the associated feed
     * List<RssItem> the answer of the query
     */
    public List<RssItem> getAllItemFromFeed(String url) {
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.ITEM_CNAME_TITLE,
                FeedEntry.ITEM_CNAME_DESC,
                FeedEntry.ITEM_CNAME_LINK,
                FeedEntry.ITEM_CNAME_DATE,
                FeedEntry.ITEM_CNAME_FEED
        };

        String selection = FeedEntry.ITEM_CNAME_FEED + "=?";
        String[] selectionArgs = { url };

        Cursor cursor = this.db.query(
                FeedEntry.TNAME_ITEM,   //table to query
                projection,             //columns to return
                selection,              //columns for the WHERE clause
                selectionArgs,          //The values for the WHERE clause
                null,                   //don't group the rows
                null,                   //don't filter by row groups
                null                    //The sort order
        );

        return this.makeItemArrayWithCursor(cursor);
    }

    public long countFeed() {
        long cnt  = DatabaseUtils.queryNumEntries(this.db, FeedEntry.TNAME_FEED);

        return cnt;
    }

    //U OPERATIONS  ---------------------------------------

    /**
     * Method used to update a Feed
     * The unchanged param have to be set to null when calling. Update the URL WILL delete every associated item.
     * @param feed <RssFeed> The isntance of the new RssFeed
     */
    public void updateFeed(RssFeed feed) {
        ContentValues values = new ContentValues();
        deleteItemOfFeed(feed.getUrl());

        values.put(FeedEntry.FEED_CNAME_URL, feed.getUrl());
        values.put(FeedEntry.FEED_CNAME_NAME, feed.getName());
        values.put(FeedEntry.FEED_CNAME_DESC, feed.getDescription());
        values.put(FeedEntry.FEED_CNAME_LINK, feed.getLink());

        String selection = FeedEntry.FEED_CNAME_URL + "='" + feed.getUrl() + "'";

        this.db.update(FeedEntry.TNAME_FEED, values, selection, null);

        //add the new items
        this.addItemsToFeed(feed);
    }

    public void updateNameOfFeed(String url, String name) {
        ContentValues values = new ContentValues();

        values.put(FeedEntry.FEED_CNAME_NAME, name);

        String selection = FeedEntry.FEED_CNAME_URL + "=" + url;

        this.db.update(FeedEntry.TNAME_FEED, values, selection, null);
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
        String selection = FeedEntry.FEED_CNAME_URL + " = ?";
        String[] selectionArgs = { url };

        this.db.delete(FeedEntry.TNAME_FEED, selection, selectionArgs);
    }

    /**
     * Method used to delete an item
     * @param id <int> The id of the item to delete
     */
    public void deleteItem(int id) {
        String selection = FeedEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        this.db.delete(FeedEntry.TNAME_ITEM, selection, selectionArgs);
    }

    /**
     * Method used to delete all items of a field
     * @param url <String> The url of the associated feed
     */
    public void deleteItemOfFeed(String url) {
        String selection = FeedEntry.ITEM_CNAME_FEED + " = ?";
        String[] selectionArgs = {url};

        this.db.delete(FeedEntry.TNAME_ITEM, selection, selectionArgs);
    }

}
