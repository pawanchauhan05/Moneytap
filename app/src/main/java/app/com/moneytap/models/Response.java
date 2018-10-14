package app.com.moneytap.models;

/**
 * Created by pawansingh on 13/10/18.
 */

public class Response {
    public boolean batchcomplete;
    public Continue _continue;
    public Query query;

    public Query getQuery() {
        return query;
    }
}
