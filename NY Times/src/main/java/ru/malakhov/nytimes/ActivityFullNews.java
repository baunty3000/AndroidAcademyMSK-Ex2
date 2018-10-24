package ru.malakhov.nytimes;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ObjectInputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ru.malakhov.nytimes.data.NewsItem;

public class ActivityFullNews extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_full_news;
    private TextView tvTitle;
    private ImageView ivPhoto;
    private CollapsingToolbarLayout ltCaterory;
    private TextView publishDate;
    private TextView fullText;
    private static final String EXTRA_KEY_NEWS = "EXTRA_KEY_NEWS";
    static String TAG = "info";

    public static void start(@NonNull Context context, @NonNull NewsItem newsItem) {
        final Intent intent = new Intent(context, ActivityFullNews.class);
        intent.putExtra(EXTRA_KEY_NEWS, newsItem);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        //Добавление кнопки home
        final Toolbar toolbar = findViewById(R.id.newsToolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        findViews();
        setDataViews();
    }

    private void findViews() {
        tvTitle = findViewById(R.id.tvNewsTitle);
        ivPhoto = findViewById(R.id.ivNewsImage);
        ltCaterory = findViewById(R.id.ctlNews);
        publishDate = findViewById(R.id.tvNewsDate);
        fullText = findViewById(R.id.tvNewsFullText);
    }

    private void setDataViews() {
        NewsItem newsItem = (NewsItem) getIntent().getParcelableExtra(EXTRA_KEY_NEWS);
        tvTitle.setText(newsItem.getTitle());
        Glide.with(this).load(newsItem.getImageUrl()).into(ivPhoto);
        ltCaterory.setTitle(newsItem.getCategory().getName());
        publishDate.setText(newsItem.getPublishDate().toString());
        fullText.setText(newsItem.getFullText());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
                default: throw new IllegalArgumentException("id не обработан "+item.getItemId());
        }
    }
}