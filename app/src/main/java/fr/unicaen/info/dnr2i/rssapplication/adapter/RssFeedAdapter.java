package fr.unicaen.info.dnr2i.rssapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.unicaen.info.dnr2i.rssapplication.EditActivity;
import fr.unicaen.info.dnr2i.rssapplication.R;
import fr.unicaen.info.dnr2i.rssapplication.entity.RssFeed;
import fr.unicaen.info.dnr2i.rssapplication.db.RssReaderManager;

import static fr.unicaen.info.dnr2i.rssapplication.R.mipmap.ic_launcher;

public class RssFeedAdapter extends ArrayAdapter<RssFeed> {

    private RssReaderManager dbM;
    private Context context;

    public RssFeedAdapter(Context context, int resource, List<RssFeed> feeds) {
        super(context, resource, feeds);
        this.dbM = new RssReaderManager(context);
        this.context = context;
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
        this.handleFeedEdition(convertView, position);

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

    private void handleFeedEdition(View convertView, int position) {
        ImageButton editButton = (ImageButton) convertView.findViewById(R.id.imgBtnEdit);
        editButton.setTag(position);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                RssFeed currentFeed = getItem(position);

                startNewEditActivity(currentFeed);

            }
        });
    }

    public void startNewEditActivity(RssFeed currentFeed) {
        Intent intent = new Intent(this.context, EditActivity.class);
        intent.putExtra("url", currentFeed.getUrl());
        this.context.startActivity(intent);
    }

    public RssReaderManager getDbMInstance() {
        return this.dbM;
    }

    public class RssFeedViewHolder {
        public ImageView image;
        public TextView name;
    }
}
