
package ru.malakhov.nytimes.data.network;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    private static final String PARAM_API_KEY = "api_key";
    private final String apiKey;

    private ApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    public static Interceptor create(@NonNull String apiKey){
        return new ApiKeyInterceptor(apiKey);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request requestWithoutApiKey = chain.request();
        HttpUrl url = requestWithoutApiKey.url()
                .newBuilder()
                .addQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        Request requestWithAttachedApiKey = chain.request()
                .newBuilder()
                .url(url)
                .build();
        return chain.proceed(requestWithAttachedApiKey);
    }
}
