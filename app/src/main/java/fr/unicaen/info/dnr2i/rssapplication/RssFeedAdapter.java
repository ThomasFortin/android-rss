package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;

import fr.unicaen.info.dnr2i.rssapplication.R;
import fr.unicaen.info.dnr2i.rssapplication.RssFeed;
import fr.unicaen.info.dnr2i.rssapplication.RssItem;
import fr.unicaen.info.dnr2i.rssapplication.RssItemAdapter;

import static fr.unicaen.info.dnr2i.rssapplication.R.mipmap.ic_launcher;

/**
 * Created by thomas on 01/02/17.
 */

public class RssFeedAdapter extends ArrayAdapter<RssFeed> {

    private RssReaderManager dbM;

    public RssFeedAdapter(Context context, int resource, List<RssFeed> feeds) {
        super(context, resource, feeds);
        this.dbM = new RssReaderManager(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rss_feed_list, parent, false);
        }

        RssFeedAdapter.RssFeedViewHolder rssFeedViewHolder = (RssFeedAdapter.RssFeedViewHolder) convertView.getTag();

        if (rssFeedViewHolder == null) {
            rssFeedViewHolder = new RssFeedAdapter.RssFeedViewHolder();
            rssFeedViewHolder.image = (ImageView) convertView.findViewById(R.id.imageRssItemImage);
            rssFeedViewHolder.name = (TextView) convertView.findViewById(R.id.textViewRssFeedName);

            convertView.setTag(rssFeedViewHolder);
        }

        RssFeed rssFeed = getItem(position);

        rssFeedViewHolder.image.setImageResource(ic_launcher);
        rssFeedViewHolder.name.setText(rssFeed.getName());

        //button listener:
        this.handleFeedDeletion(convertView, position);

        return convertView;
    }

    private void handleFeedDeletion(View convertView, int position) {
        ImageButton delButton = (ImageButton) convertView.findViewById(R.id.imgBtnRemove);
        delButton.setTag(position);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                RssFeed currentFeed = getItem(position);
                // Delete feed from db:
                getDbMInstance().deleteFeed(currentFeed.getUrl());
                //delete feed from list:
                remove(currentFeed);
            }
        });
    }

    public RssReaderManager getDbMInstance() {
        return this.dbM;
    }

    public class RssFeedViewHolder {
        public ImageView image;
        public TextView name;
    }
}
