package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fr.unicaen.info.dnr2i.rssapplication.db.RssReaderManager;
import fr.unicaen.info.dnr2i.rssapplication.entity.RssFeed;

public class EditActivity extends AppCompatActivity {

    private RssReaderManager dbM;
    private RssFeed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //get the feed instance
        this.dbM = new RssReaderManager(this);
        String feedUrl = getIntent().getExtras().getString("url");
        this.feed = this.dbM.getOneFeedByUrl(feedUrl);

        //prefill form
        EditText viewFeedUrl = (EditText) findViewById(R.id.editTextEditFeedURL);
        viewFeedUrl.setText(this.feed.getUrl());
        EditText viewFeedName = (EditText) findViewById(R.id.editTextEditFeedName);
        viewFeedName.setText(this.feed.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_to_list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_menu_list) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void editRssFeed(View view) {
        EditText viewFeedUrl = (EditText) findViewById(R.id.editTextEditFeedURL);
        String url = viewFeedUrl.getText().toString();

        EditText viewFeedName = (EditText) findViewById(R.id.editTextEditFeedName);
        String name = viewFeedName.getText().toString();

        //if the key change, we have to destroy the feed and create a new one
        if (this.feed.getUrl() != url) {
            this.dbM.deleteFeed(this.feed.getUrl());
            RssFeed newFeed = new RssFeed(url, name, null, null);
            this.dbM.addFeed(newFeed);
        } else {
            //if just the name change we can edit the feed
            if (this.feed.getName() != name) {
                this.dbM.updateNameOfFeed(url, name);
            }
        }

        // Once the RSS feed is added to DB, we redirect to the list
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Display of a toast to confirm the add
        Context context = getApplicationContext();
        String confirmText = "RSS feed edited.";
        int duration = Toast.LENGTH_SHORT;

        Toast confirmToast = Toast.makeText(context, confirmText, duration);
        confirmToast.show();
    }
}
