package ru.malakhov.nytimes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.malakhov.nytimes.data.DataUtils;
import ru.malakhov.nytimes.data.NewsItem;

public class ActivityRecyclerNews extends AppCompatActivity {

    private final String TAG = "info";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_news);
        RecyclerView rvNews = findViewById(R.id.rvNews);
        List<NewsItem> newsItems = DataUtils.generateNews();
        rvNews.setAdapter(new AdapterRecyclerNews(this, newsItems));
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvNews.setLayoutManager(new LinearLayoutManager(this));
            rvNews.addItemDecoration(new ItemDecorationNews(getDp(10), Configuration.ORIENTATION_PORTRAIT));
        } else {
            rvNews.setLayoutManager(new GridLayoutManager(this, 2));
            rvNews.addItemDecoration(new ItemDecorationNews(2 , getDp(10), Configuration.ORIENTATION_LANDSCAPE));
        }
    }

    private int getDp(int i) {
        return (int) getResources().getDisplayMetrics().density * i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                startActivity(new Intent(this, ActivityAbout.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
