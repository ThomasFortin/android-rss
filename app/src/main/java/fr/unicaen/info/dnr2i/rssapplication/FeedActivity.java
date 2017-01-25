package fr.unicaen.info.dnr2i.rssapplication;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView rssItemsListView;
    SwipeRefreshLayout mSwipeRefresh;
    RssItemAdapter adapter;

    List<RssItem> rssItems = this.generateTestRssItems();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefresh.setOnRefreshListener(this);
        rssItemsListView = (ListView) findViewById(R.id.listViewRssItems);

        // mAdapter = new ArrayAdapter<RssItem>(FeedActivity.this, android.R.layout.simple_list_item_1, rssItems);
        // rssItemsListView.setAdapter(mAdapter);

        adapter = new RssItemAdapter(FeedActivity.this, android.R.layout.simple_list_item_1, rssItems);
        rssItemsListView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {

                rssItems.clear();

                rssItems.add(new RssItem("Abricot", "L'abricot bien juteux du Sud de la France comme aime Evrard", "http://www.abricot.fr/rss.xml", "28/01/2017"));

                adapter.notifyDataSetChanged();

                mSwipeRefresh.setRefreshing(false);
            }
        }, 1000);
    }

    public static List<RssItem> generateTestRssItems(){
        List<RssItem> items = new ArrayList<RssItem>();
        items.add(new RssItem("Carotte", "La carotte rend aimable, paraît-il !", "http://www.carotte.fr/rss.xml", "21/12/2016"));
        items.add(new RssItem("Pomme de terre", "La pomme de terre, c'est aussi bon en purée qu'à la poêle !", "http://www.patate.com/rss.xml", "29/12/2016"));
        items.add(new RssItem("Salade", "Plutôt laitue ? Mâche, Roquette ? Il y en a pour tous les goûts !", "http://www.salade.fr/rss.xml", "07/01/2017"));
        items.add(new RssItem("Tomate", "La tomate est-elle un fruit ou un légume ? Telle est la question..", "http://www.tomate.fr/rss.xml", "16/01/2017"));
        items.add(new RssItem("Radis", "Le radis, c'est bon avec du pain beurre.. Parce que ça a goût de pain beurre, c'est tout.", "http://www.radis.fr/rss.xml", "26/01/2017"));
        items.add(new RssItem("Courgette", "Manger de la courgette, c'est comme manger de l'eau !", "http://www.courgette.fr/rss.xml", "26/01/2017"));
        items.add(new RssItem("Haricot", "C'est bon les haricots, avec un peu de flageollets c'est pas mauvais hein.", "http://www.haricot.fr/rss.xml", "27/01/2017"));
        items.add(new RssItem("Citrouille", "Une bonne soupe de citrouille un soir d'hiver en face de la cheminée, c'est toujours bien plaisant.", "http://www.citrouille.fr/rss.xml", "28/01/2017"));
        return items;
    }
}
