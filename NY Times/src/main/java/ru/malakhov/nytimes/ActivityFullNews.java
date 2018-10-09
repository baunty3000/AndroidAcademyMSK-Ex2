package ru.malakhov.nytimes;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import ru.malakhov.nytimes.data.NewsItem;

public class ActivityFullNews extends AppCompatActivity {

    private TextView title;
    private ImageView imageUrl;
    private CollapsingToolbarLayout category;
    private TextView publishDate;
    private TextView fullText;

    private static final String KEY_TITLE = "KEY_NEWS";
    private static final String KEY_IMAGE = "KEY_IMAGE";
    private static final String KEY_CATEGORY = "KEY_CATEGORY";
    private static final String KEY_DATE = "KEY_DATE";
    private static final String KEY_TEXT = "KEY_TEXT";

    static String TAG = "info";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);

        //получаем размер экрана
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        //меняем наш height AppBarLayout в зависимости от размера экрана
        AppBarLayout x = (AppBarLayout) findViewById(R.id.newsAppBar);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) x.getLayoutParams();
        lp.height=height/2;
        x.setLayoutParams(lp);

        final Toolbar toolbar = findViewById(R.id.newsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.tvNewsTitle);
        imageUrl = findViewById(R.id.ivNewsImage);
        category = findViewById(R.id.ctlNews);
        publishDate = findViewById(R.id.tvNewsDate);
        fullText = findViewById(R.id.tvNewsFullText);

        //наполняем форму данными
        title.setText(getIntent().getStringExtra(KEY_TITLE));
        Glide.with(this).load(getIntent().getStringExtra(KEY_IMAGE)).into(imageUrl);
        category.setTitle(getIntent().getStringExtra(KEY_CATEGORY));
        publishDate.setText(getIntent().getStringExtra(KEY_DATE));
        fullText.setText(getIntent().getStringExtra(KEY_TEXT));
    }

    public static void start(Context context, NewsItem newsItem){
        Intent intent = new Intent(context, ActivityFullNews.class);
        intent.putExtra(KEY_TITLE, newsItem.getTitle());
        intent.putExtra(KEY_IMAGE, newsItem.getImageUrl());
        intent.putExtra(KEY_CATEGORY, newsItem.getCategory().getName());
        intent.putExtra(KEY_TEXT, newsItem.getFullText());
        intent.putExtra(KEY_DATE, newsItem.getPublishDate().toString());
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}