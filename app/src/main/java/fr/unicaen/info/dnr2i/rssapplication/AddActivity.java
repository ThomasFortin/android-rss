package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    private RssReaderManager dbM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        this.dbM = new RssReaderManager(this);
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

    public void addRssFeed(View view) {
        EditText viewFeedUrl = (EditText) findViewById(R.id.editTextNewFeedURL);
        String url = viewFeedUrl.getText().toString();

        EditText viewFeedName = (EditText) findViewById(R.id.editTextNewFeedName);
        String name = viewFeedName.getText().toString();

        //temporary: to change with rss wrapper response
        String description = "All the news on "+name;
        String link = "http://"+name+".com";

        RssFeed feed = new RssFeed(url, name, description, link);
        this.dbM.addFeed(feed);

        // Once the RSS feed is added to DB, we redirect to the list
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Display of a toast to confirm the add
        Context context = getApplicationContext();
        String confirmText = getString(R.string.confirm_add_toast);
        int duration = Toast.LENGTH_SHORT;

        Toast confirmToast = Toast.makeText(context, confirmText, duration);
        confirmToast.show();
    }
}
