package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import static fr.unicaen.info.dnr2i.rssapplication.RssReaderContract.FeedEntry;

public class MainActivity extends AppCompatActivity {
    ListView mListView;
    RssReaderManager dbM;

    // DATA TEST USED by generateDevBd()
    private String[] titles = new String[]{
            "Chèvre", "Bouquetin", "Cygne", "Tigre",
            "Écureuil", "Ratel", "Chien", "Paresseux",
            "Pie", "Chat", "Lion", "Dindon"
    };

    private RssFeed[] feeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open the db manager
        this.dbM = new RssReaderManager(this);
        this.dbM.open();

        //first time we generate a test DB (for test only)
        if (this.dbM.countFeed() == 0){
            this.generateDevBd();
        }
        //we regenerate object from db
        this.initFeedsFromDb();
        this.dbM.close();

        mListView = (ListView) findViewById(R.id.listViewFeeds);

        //display the feeds
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, this.getAllFeedsName());
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_to_add, menu);
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_menu_add) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * Method used to generate a test db when none already exist
     * @deprecated This method should not exist in final build, it's only purpose is to test the apps.
     */
    public void generateDevBd() {
        this.dbM.open();
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
            this.dbM.addFeed(feed);
        }
        this.dbM.close();
    }

    /**
     * Method used to generate to get all the feeds name in an array
     * @return titles <String[]> Array containing every feeds name
     */
    private String[] getAllFeedsName() {
        String[] titles = new String[this.feeds.length];
        int i=0;
        for (RssFeed feed : this.feeds) {
            titles[i] = feed.getName();
            i++;
        }
        return titles;
    }

    /**
     * Method used to synchronize and create object from the content in db
     * It filed the attr this.feeds, create rssFeed object, rssItem object and link them to the corresponding rssFeed.
     */
    private void initFeedsFromDb() {
        this.dbM.open();
        //init feeds variable
        Cursor cF = this.dbM.getAllFeed();
        int count = cF.getCount();
        this.feeds = new RssFeed[count];
        String url;
        String name;
        String description;
        String link;
        RssFeed feed;
        //init items variable
        Cursor cI;
        RssItem item;
        int id;
        String title;
        String descItem;
        String linkItem;
        String pubDate;

        if (cF.moveToFirst()) {
            int i = 0;
            do {
                //get the feeds
                url = cF.getString(cF.getColumnIndex(FeedEntry.T2_C1_NAME));
                name = cF.getString(cF.getColumnIndex(FeedEntry.T2_C2_NAME));
                description = cF.getString(cF.getColumnIndex(FeedEntry.T2_C3_NAME));
                link = cF.getString(cF.getColumnIndex(FeedEntry.T2_C4_NAME));
                feed = new RssFeed(url, name, description, link);
                this.feeds[i] = feed;

                //get the associated item
                cI = this.dbM.getAllItemFromFeed(url);
                if (cI.moveToFirst()) {
                    do {
                        id = cF.getInt(cI.getColumnIndex(FeedEntry._ID));
                        title = cF.getString(cI.getColumnIndex(FeedEntry.T1_C1_NAME));
                        descItem = cF.getString(cI.getColumnIndex(FeedEntry.T1_C2_NAME));
                        linkItem = cF.getString(cI.getColumnIndex(FeedEntry.T1_C3_NAME));
                        pubDate = cF.getString(cI.getColumnIndex(FeedEntry.T1_C4_NAME));
                        item = new RssItem(id, title, descItem, linkItem, pubDate);
                        this.feeds[i].addItem(item);
                    } while (cI.moveToNext());
                    cI.close();
                }
                i++;
            } while (cF.moveToNext());
        }

        cF.close();
        this.dbM.close();
    }

}
