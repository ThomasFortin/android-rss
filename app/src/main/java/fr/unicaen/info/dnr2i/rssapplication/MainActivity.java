package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_to_add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_menu_add:
                startActivity(new Intent(this, AddActivity.class));
                return true;
            case R.id.action_menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * Method used to generate to get all the feeds name in an array
     * @return titles <String[]> Array containing every feeds name
     */
    private String[] getAllFeedsName() {
        List<RssFeed> feeds = this.dbM.getAllFeeds();
        String[] titles = new String[feeds.size()];
        int i= feeds.size() - 1;
        for (RssFeed feed : feeds) {
            titles[i] = feed.getName();
            i--;
        }
        return titles;
    }

/*    public void deleteFeed(View view) {
        Log.d("test", "success");

        ImageButton delButton = (ImageButton) view.findViewById(R.id.imgBtnEdit);
        // Cache row position inside the button using `setTag`
        delButton.get
    }*/

    /*bah t'as juste à créer la fonction dans la MainActivity et foutre ce qu'il faut dedans, en rajoutant bien le "onClick" sur le boutons
    mais je crois que là différent y a une histoire de "OnItemClicked" ou je sais pas quoi sur la liste*/

}
