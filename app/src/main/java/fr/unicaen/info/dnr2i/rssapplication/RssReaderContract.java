package fr.unicaen.info.dnr2i.rssapplication;

import android.provider.BaseColumns;

/**
 * Created by plabadille on 18/01/17.
 */

public final class RssReaderContract {

    private RssReaderContract() {}

    public static class FeedEntry implements BaseColumns {

        public static final String TABLE1_NAME = "RssItem";
        public static final String TABLE2_NAME = "RssFeed";

        public static final String T1_C1_NAME = "title";
        public static final String T1_C2_NAME = "description";
        public static final String T1_C3_NAME = "link";
        public static final String T1_C4_NAME = "pubDate";
        public static final String T1_C5_NAME = "feed";

        public static final String T2_C1_NAME = "url";
        public static final String T2_C2_NAME = "name";
        public static final String T2_C3_NAME = "description";
        public static final String T2_C4_NAME = "link";
    }
}
