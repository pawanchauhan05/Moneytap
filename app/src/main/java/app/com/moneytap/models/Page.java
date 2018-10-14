package app.com.moneytap.models;

/**
 * Created by pawansingh on 13/10/18.
 */

public class Page {
    public int pageid;
    public int ns;
    public String title;
    public int index;
    public Thumbnail thumbnail;
    public Terms terms;

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public Terms getTerms() {
        return terms;
    }

    public String getTitle() {
        return title;
    }
}
