package app.com.moneytap.services;


import java.util.Map;

import app.com.moneytap.models.Response;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by pawansingh on 08/09/18.
 */

public interface Api {

    @GET("w/api.php")
    Observable<Response> getData(@QueryMap Map<String, String> queryParams);
}
