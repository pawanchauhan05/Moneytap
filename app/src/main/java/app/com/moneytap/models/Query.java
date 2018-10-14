package app.com.moneytap.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawansingh on 13/10/18.
 */

public class Query {
    public List<Redirect> redirects = new ArrayList<>();
    public List<Page> pages = new ArrayList<>();

    public List<Page> getPages() {
        return pages;
    }
}


