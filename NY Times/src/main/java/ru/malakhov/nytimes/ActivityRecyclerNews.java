package ru.malakhov.nytimes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ru.malakhov.nytimes.data.DataUtils;
import ru.malakhov.nytimes.data.NewsItem;

public class ActivityRecyclerNews extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_recycler_news;

    private final String TAG = "info";
    private static List<NewsItem> mNewsItems;
    CompositeDisposable mCompositeDisposable;
    AdapterRecyclerNews mAdapter;
    ProgressBar mProgressBar;

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeDisposable.dispose();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadItems();
    }

    private void loadItems() {
        mNewsItems = new ArrayList<>();
        mProgressBar.setVisibility(View.VISIBLE);
        mCompositeDisposable = new CompositeDisposable();
        Observable<Long> sleep = Observable.interval(1, TimeUnit.SECONDS);

        Disposable disposable = Observable
                .fromArray(DataUtils.generateNews()) // Формирую Observable<List<NewsItem>>
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .flatMap((Function<List<NewsItem>, ObservableSource<NewsItem>>) newsItems -> Observable.fromIterable(newsItems)) // достаю все NewsItem
                .zipWith(sleep, (newsItem, aLong) -> newsItem) // создаю скундную задержку
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> {
                            mNewsItems.add(v);
                            mAdapter.setNewsItems(mNewsItems);
                        },
                        e -> Log.d(TAG, "onError: " + e),
                        () -> mProgressBar.setVisibility(View.GONE));
        mCompositeDisposable.add(disposable);
        Log.d(TAG, "loadItems: ");
        }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        final RecyclerView recycler = findViewById(R.id.rv_news);
        mProgressBar = findViewById(R.id.progress_bar);

        setItemOrientation(recycler); // отображение элементов в разных ориентациях

        mAdapter = new AdapterRecyclerNews(this);
        recycler.setAdapter(mAdapter);
    }

    private void setItemOrientation(RecyclerView rvNews) {
        int px = getResources().getDimensionPixelSize(R.dimen.spacing_small);
        int columns = getResources().getInteger(R.integer.landscape_news_columns_count);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvNews.setLayoutManager(new LinearLayoutManager(this));
            rvNews.addItemDecoration(new ItemDecorationNews(px, Configuration.ORIENTATION_PORTRAIT));
        } else {
            rvNews.setLayoutManager(new GridLayoutManager(this, columns));
            rvNews.addItemDecoration(new ItemDecorationNews(columns , px, Configuration.ORIENTATION_LANDSCAPE));
        }
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
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_not_id)+item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }
}
