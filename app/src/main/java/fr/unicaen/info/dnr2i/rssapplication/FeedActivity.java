package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.InetAddress;
import java.util.List;

import fr.unicaen.info.dnr2i.rssapplication.adapter.RssItemAdapter;
import fr.unicaen.info.dnr2i.rssapplication.db.RssReaderManager;
import fr.unicaen.info.dnr2i.rssapplication.entity.RssItem;

public class FeedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView rssItemsListView;
    private RssReaderManager dbM;
    SwipeRefreshLayout mSwipeRefresh;
    RssItemAdapter adapter;
    String url;
    List<RssItem> rssItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setOnRefreshListener(this);
        rssItemsListView = (ListView) findViewById(R.id.listViewRssItems);

        this.dbM = new RssReaderManager(this);
        this.url = getIntent().getExtras().getString("url");

        if (canAutomaticallyLoad()) {
            download();
        }

        rssItems = dbM.getAllItemFromFeed(url);
        adapter = new RssItemAdapter(FeedActivity.this, android.R.layout.simple_list_item_1, rssItems);
        rssItemsListView.setAdapter(adapter);
        rssItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RssItem rssItem = (RssItem) parent.getItemAtPosition(position);
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(rssItem.getLink()));
                startActivity(browser);
            }
        });
    }

    @Override
    public void onRefresh() {
        download();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.listViewRssItems);
        list.setEmptyView(empty);
    }

    protected void refreshList() {
        rssItems.addAll(dbM.getAllItemFromFeed(url));
        adapter.notifyDataSetChanged();
    }

    protected void download() {
        new DownloadFeedTask(dbM) {
            @Override
            protected void onPostExecute(Boolean result) {
                refreshList();
                mSwipeRefresh.setRefreshing(false);
            }
        }.execute(url);
        mSwipeRefresh.setRefreshing(true);
    }

    protected boolean canAutomaticallyLoad() {
        boolean hasInternet;
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            hasInternet = !ipAddr.equals("");
        } catch (Exception e) {
            hasInternet = false;
        }

        if (!hasInternet) {
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConnected = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnected();
        SharedPreferences sharedPref = getSharedPreferences("network", 0);
        int networkChoice = sharedPref.getInt("networkPreferences", NetworkPreferences.AUTOMATICALLY.ordinal());
        return (networkChoice == NetworkPreferences.AUTOMATICALLY.ordinal()
                || networkChoice == NetworkPreferences.WIFI_ONLY.ordinal() && isWifiConnected
        );
    }
}
