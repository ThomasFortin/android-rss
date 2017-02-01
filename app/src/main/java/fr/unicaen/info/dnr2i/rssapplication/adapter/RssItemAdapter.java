package fr.unicaen.info.dnr2i.rssapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.unicaen.info.dnr2i.rssapplication.R;
import fr.unicaen.info.dnr2i.rssapplication.entity.RssItem;

/**
 * Created by thomas on 25/01/17.
 */

public class RssItemAdapter extends ArrayAdapter<RssItem> {

    public RssItemAdapter(Context context, int resource, List<RssItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rss_item_list, parent, false);
        }

        RssItemViewHolder rssItemViewHolder = (RssItemViewHolder) convertView.getTag();

        if (rssItemViewHolder == null) {
            rssItemViewHolder = new RssItemViewHolder();
            rssItemViewHolder.title = (TextView) convertView.findViewById(R.id.textViewRssItemName);
            rssItemViewHolder.link = (TextView) convertView.findViewById(R.id.textViewRssItemLink);
            rssItemViewHolder.description = (TextView) convertView.findViewById(R.id.textViewRssItemDescription);

            convertView.setTag(rssItemViewHolder);
        }

        RssItem rssItem = getItem(position);

        rssItemViewHolder.title.setText(rssItem.getTitle());
        rssItemViewHolder.link.setText(rssItem.getLink());
        rssItemViewHolder.description.setText(rssItem.getDescription());

        return convertView;
    }

    public class RssItemViewHolder {
        public TextView title;
        public TextView link;
        public TextView description;
    }
}
