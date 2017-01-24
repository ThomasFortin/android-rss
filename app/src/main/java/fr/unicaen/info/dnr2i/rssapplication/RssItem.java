package fr.unicaen.info.dnr2i.rssapplication;

public class RssItem {
    int id;
    String title;
    String description;
    String link;
    String pubDate;

    public RssItem(int id, String title, String description, String link, String pubDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLink() {
        return this.link;
    }

    public String getPubDate() {
        return this.pubDate;
    }
}
