package app.com.moneytap.retrofit;


import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.com.moneytap.retrofit.interceptors.LoggingInterceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pawansingh on 08/09/18.
 */

public class ApiClient {

    public static final String BASE_URL = "https://en.wikipedia.org/";

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        Map<String, String> headers = new HashMap<>();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(buildHTTPClient(headers))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient buildHTTPClient(final Map<String, String> headers) {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new LoggingInterceptor(headers));

        // adding logging interceptor
        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        // init cookie manager
        initCookie(httpClient);

        return httpClient.build();

    }

    private static void initCookie(OkHttpClient.Builder httpClient) {
        CookieHandler cookieHandler = new CookieManager();
        httpClient
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

    }
}
