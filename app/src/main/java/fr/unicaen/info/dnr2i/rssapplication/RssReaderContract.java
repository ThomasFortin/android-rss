package fr.unicaen.info.dnr2i.rssapplication;

import android.provider.BaseColumns;

/**
 * Created by plabadille on 18/01/17.
 */

public final class RssReaderContract {

    private RssReaderContract() {}

    public static class FeedEntry implements BaseColumns {

        public static final String TNAME_ITEM = "RssItem";
        public static final String TNAME_FEED = "RssFeed";

        public static final String ITEM_CNAME_TITLE = "title";
        public static final String ITEM_CNAME_DESC = "description";
        public static final String ITEM_CNAME_LINK = "link";
        public static final String ITEM_CNAME_DATE = "pubDate";
        public static final String ITEM_CNAME_FEED = "feed";

        public static final String FEED_CNAME_URL = "url";
        public static final String FEED_CNAME_NAME = "name";
        public static final String FEED_CNAME_DESC = "description";
        public static final String FEED_CNAME_LINK = "link";

    }
}
