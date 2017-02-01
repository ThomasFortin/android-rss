package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    public RssFeedAdapter(Context context, int resource, List<RssFeed> feeds) {
        super(context, resource, feeds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rss_feed_list, parent, false);
        }

        RssFeedAdapter.RssFeedViewHolder rssFeedViewHolder = (RssFeedAdapter.RssFeedViewHolder) convertView.getTag();

        if (rssFeedViewHolder == null) {
            rssFeedViewHolder = new RssFeedAdapter.RssFeedViewHolder();
            rssFeedViewHolder.name = (TextView) convertView.findViewById(R.id.textViewRssFeedName);

            convertView.setTag(rssFeedViewHolder);
        }

        RssFeed rssFeed = getItem(position);

        rssFeedViewHolder.name.setText(rssFeed.getName());

        return convertView;
    }

    public class RssFeedViewHolder {
        public TextView name;
    }
}
