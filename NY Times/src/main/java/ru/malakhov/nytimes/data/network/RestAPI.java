
package ru.malakhov.nytimes.data.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.malakhov.nytimes.data.network.endpoints.DataNewsEndpoint;

public class RestAPI {

    private static final RestAPI mOurInstance = new RestAPI();

    private static final String URL = "http://api.nytimes.com";
    private static final int TIMEOUT_IN_SECONDS = 2;
    private final String mApiKey = "b057ed89db9a434c8ad0a6d38188ec83";

    private DataNewsEndpoint mDataNewsEndpoint;

    public static final String mSections[] = {
            "home", "arts", "automobiles", "books",
            "business", "fashion", "food", "health",
            "insider", "magazine", "movies", "national",
            "nyregion", "obituaries", "opinion", "politics",
            "realestate", "science", "sports", "sundayreview",
            "technology", "theater", "tmagazine", "travel",
            "upshot", "world"};

    public static RestAPI getInstance() {
        return mOurInstance;
    }
    public DataNewsEndpoint getDataNewsEndpoint() {
        return mDataNewsEndpoint;
    }

    private RestAPI() {
        OkHttpClient client = buildOkHttpClient();
        Retrofit retrofit = buildRetrofit(client);
        mDataNewsEndpoint = retrofit.create(DataNewsEndpoint.class);
    }

    private HttpLoggingInterceptor buildHttpLoggingInterceptor(){
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
    }

    private OkHttpClient buildOkHttpClient(){
         return new OkHttpClient.Builder()
                 .addInterceptor(ApiKeyInterceptor.create(mApiKey))
                 .addInterceptor(buildHttpLoggingInterceptor())
                 .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                 .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                 .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                 .build();
    }

    private Retrofit buildRetrofit(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
