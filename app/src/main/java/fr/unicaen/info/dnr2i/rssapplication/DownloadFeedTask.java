package fr.unicaen.info.dnr2i.rssapplication;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.core.ParseException;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;
import de.nava.informa.parsers.FeedParser;

public class DownloadFeedTask extends AsyncTask<String, Void, Boolean> {

    private RssReaderManager dbM;

    public DownloadFeedTask(RssReaderManager dbM) {
        this.dbM = dbM;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String urlName = params[0];
        InputStream is = null;
        try {
            is = downloadUrl(urlName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (is == null) {
            return false;
        }
        ChannelIF channel = null;
        try {
            channel = FeedParser.parse(new ChannelBuilder(), is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RssFeed rssFeed = dbM.getOneFeedByUrl(urlName);
        rssFeed.setDescription(channel.getDescription());
        rssFeed.setLink(channel.getSite().toString());
        ArrayList<RssItem> items = new ArrayList<RssItem>();
        for (Object i : channel.getItems()) {
            Item item = (Item) i;
            items.add(new RssItem(
                item.getTitle(),
                item.getDescription(),
                item.getLink().toString(),
                item.getDate().toString()
            ));
        }
        rssFeed.setItems(items);
        dbM.updateFeed(rssFeed);
        return true;
    }
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
}
