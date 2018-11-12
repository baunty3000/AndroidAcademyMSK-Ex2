
package ru.malakhov.nytimes.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.network.dto.ResultDTO;

public class ActivityFullNews extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_full_news;
    private static final String EXTRA_KEY_URL = "EXTRA_KEY_URL";
    private static final String EXTRA_KEY_TITLE = "EXTRA_KEY_TITLE";

    private WebView mWebView;

    public static void start(@NonNull Context context, @NonNull ResultDTO newsUrl) {
        context.startActivity(new Intent(context, ActivityFullNews.class)
                .putExtra(EXTRA_KEY_URL, newsUrl.getUrl())
                .putExtra(EXTRA_KEY_TITLE, newsUrl.getTitle()));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startInit();
        setHomeButton();
        findViews();
        setDataViews();
    }

    private void startInit() {
        setContentView(LAYOUT);
        setTitle(getIntent().getStringExtra(EXTRA_KEY_TITLE));
    }

    private void setHomeButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void findViews() {
        mWebView = findViewById(R.id.web_browser);
    }

    private void setDataViews() {
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(getIntent().getStringExtra(EXTRA_KEY_URL));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }
}
