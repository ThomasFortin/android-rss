package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ParseException;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;
import de.nava.informa.parsers.FeedParser;
import fr.unicaen.info.dnr2i.rssapplication.db.RssReaderManager;
import fr.unicaen.info.dnr2i.rssapplication.entity.RssFeed;
import fr.unicaen.info.dnr2i.rssapplication.entity.RssItem;

public class DownloadFeedTask extends AsyncTask<String, Void, Boolean> {

    private RssReaderManager dbM;

    public DownloadFeedTask(RssReaderManager dbM) {
        this.dbM = dbM;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String urlName = params[0];
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlName);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        if (is == null) {
            return false;
        }
        ChannelIF channel = null;
        try {
            channel = FeedParser.parse(new ChannelBuilder(), urlName);
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
}
