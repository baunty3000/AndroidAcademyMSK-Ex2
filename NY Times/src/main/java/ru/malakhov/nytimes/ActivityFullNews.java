package ru.malakhov.nytimes;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ru.malakhov.nytimes.data.DataUtils;
import ru.malakhov.nytimes.data.NewsItem;

public class ActivityFullNews extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_full_news;
    private static final String EXTRA_KEY_NEWS = "EXTRA_KEY_NEWS";

    private TextView mTitle;
    private ImageView mImageURL;
    private CollapsingToolbarLayout mCategory;
    private TextView mPublishDate;
    private TextView mFullText;

    public static void start(@NonNull Context context, @NonNull NewsItem newsItem) {
        context.startActivity(new Intent(context, ActivityFullNews.class).putExtra(EXTRA_KEY_NEWS, newsItem));
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
    }

    private void setHomeButton() {
        final Toolbar toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void findViews() {
        mTitle = findViewById(R.id.details_title);
        mImageURL = findViewById(R.id.details_image);
        mCategory = findViewById(R.id.details_category);
        mPublishDate = findViewById(R.id.details_date);
        mFullText = findViewById(R.id.details_text);
    }

    private void setDataViews() {
        NewsItem newsItem = getIntent().getParcelableExtra(EXTRA_KEY_NEWS);
        mTitle.setText(newsItem.getTitle());
        Glide.with(this).load(newsItem.getImageUrl()).into(mImageURL);
        mCategory.setTitle(newsItem.getCategory().getName());
        mPublishDate.setText(DataUtils.formatDateTime(this,newsItem.getPublishDate()));
        mFullText.setText(newsItem.getFullText());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_not_id)+item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }
}
