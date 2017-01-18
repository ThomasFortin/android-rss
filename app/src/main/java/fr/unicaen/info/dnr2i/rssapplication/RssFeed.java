import java.util.List;

public class RssFeed {
    String url;
    String name;
    String description;
    String link;
    List<RssItem> items;

    public RssFeed(String url, String name, String description, String link) {
        this.url = url;
        this.name = name;
        this.description = description;
        this.link = link;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<RssItem> getItems() {
        return this.items;
    }

    public void setItems(List<RssItem> items) {
        this.items = items;
    }
}