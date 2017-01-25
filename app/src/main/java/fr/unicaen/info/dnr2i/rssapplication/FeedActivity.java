package fr.unicaen.info.dnr2i.rssapplication;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // Data test
    List<String> dataTest = new ArrayList<String>();

    ListView newsListView;
    SwipeRefreshLayout mSwipeRefresh;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefresh.setOnRefreshListener(this);
        newsListView = (ListView) findViewById(R.id.listViewFeedNews);

        for (int i = 0; i < 25; i++) {
            dataTest.add("Data test n°" + i);
        }

        mAdapter = new ArrayAdapter<String>(FeedActivity.this, android.R.layout.simple_list_item_1, dataTest);
        newsListView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                dataTest.clear();

                for (int i = 0; i < 25; i++) {
                    dataTest.add("New data test n°" + i);
                }

                mAdapter.notifyDataSetChanged();

                mSwipeRefresh.setRefreshing(false);
            }
        }, 1000);
    }
}
