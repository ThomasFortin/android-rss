package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView rssFeedsListView;
    private RssReaderManager dbM;
    RssFeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open the db manager
        this.dbM = new RssReaderManager(this);

        List<RssFeed> feeds = this.dbM.getAllFeeds();

        rssFeedsListView = (ListView) findViewById(R.id.listViewFeeds);

        //display the feeds
        adapter = new RssFeedAdapter(MainActivity.this, android.R.layout.simple_list_item_1, feeds);
        rssFeedsListView.setAdapter(adapter);
        rssFeedsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RssFeed rssFeed = (RssFeed) parent.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), FeedActivity.class);
                i.putExtra("url", rssFeed.getUrl());
                startActivity(i);
            }
        });
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

}
