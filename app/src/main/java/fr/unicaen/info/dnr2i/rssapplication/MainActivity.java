package fr.unicaen.info.dnr2i.rssapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import static fr.unicaen.info.dnr2i.rssapplication.RssReaderContract.FeedEntry;

public class MainActivity extends AppCompatActivity {
    ListView mListView;
    RssReaderManager dbM;

    // DATA TEST
    private String[] titles = new String[]{
            "Chèvre", "Bouquetin", "Cygne", "Tigre",
            "Écureuil", "Ratel", "Chien", "Paresseux",
            "Pie", "Chat", "Lion", "Dindon"
    };

    private RssFeed[] feeds;

    /*
    private String[] descriptions = new String[]{
            "Une belle chèvre", "Un superbe bouquetin", "Un très beau cygne", "Un tigre originaire du Bengale",
            "Agile et adore les noisettes", "Blaireau intrépide et inconscient", "Le meilleur ami de l'Homme", "Heeeeuu..",
            "Y a une pie, dans le poirier..", "Trois petits chats", "Roi de la jungle", "Glou glou glou glou"
    };
    */

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Just used in first dev test
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

    public void addFeed(View view) {
        //HAVE TO GET DATA FROM EACH SPECIFIC FIELD OF THE ADD FORM
        //TextView url = (TextView) findViewById(R.id.
        String url = "http://chevre.rss.xml"; //content of a specific field
        String name = "Chèvre"; //content of a specific field
        String description = "ce flux est une chèvre"; //content of a specific field
        String link = "http://chevre.com"; //content of a specific field
        RssFeed feed = new RssFeed(url, name, description, link);

        this.dbM.open();
        feed = new RssFeed("http://cochon.rss.xml", "Cochon", "ce flux est un cochon", "http://cochon.com");
        this.dbM.addFeed(feed);
        this.dbM.close();
    }

    public void getFeedData(View view) {
        //public static Cursor getOneFeedByUrl(SQLiteDatabase db, String url) {
        this.dbM.open();
        Cursor feed = this.dbM.getAllFeed();
        this.dbM.close();
    }

    private String[] getAllFeedsName() {
        String[] titles = new String[this.feeds.length];
        int i=0;
        for (RssFeed feed : this.feeds) {
            titles[i] = feed.getName();
            i++;
        }
        return titles;
    }

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
