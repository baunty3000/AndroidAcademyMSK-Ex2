
package ru.malakhov.nytimes.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.network.RestAPI;
import ru.malakhov.nytimes.data.network.dto.DataNewsDTO;
import ru.malakhov.nytimes.data.room.NewsEntity;
import ru.malakhov.nytimes.ui.adapter.AdapterRecyclerNews;

public class ActivityRecyclerNews extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_recycler_news;
    private final static int SPINNER_DEFAULT_ITEM = 0;
    private TextView mTvError;
    private TextView mTvNoData;
    private Button mBtnError;
    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;
    private Disposable mDisposableNews;
    private Spinner mSpinner;
    private Context mContext;

    private static List<NewsEntity> mNewsItems = new ArrayList<>();
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private AdapterRecyclerNews mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        init();
        setItemOrientation(mRecycler); // отображение элементов в разных ориентациях
        mAdapter = new AdapterRecyclerNews(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadItems(getNewsCategory());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeDisposable.clear();
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

    private void findViews(){
        mRecycler = findViewById(R.id.rv_news);
        mTvError = findViewById(R.id.tv_network_error);
        mBtnError = findViewById(R.id.btn_try_again);
        mTvNoData = findViewById(R.id.tv_no_data);
        mProgressBar = findViewById(R.id.pr_bar_recycler);
        mSpinner = findViewById(R.id.spinner);
    }

    private void init() {
        mContext = this;
        findViews();
        setToolbar();
        setFab();
        mBtnError.setOnClickListener(v->loadItems(RestAPI.mSections[mSpinner.getSelectedItemPosition()]));
    }

    private void setToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        setSpinner();
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, RestAPI.mSections);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(SPINNER_DEFAULT_ITEM);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadItems(RestAPI.mSections[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setFab() {
        findViewById(R.id.fab).setOnClickListener(v -> {
            mNewsItems.clear();
            loadFromApi(getNewsCategory());
        });
    }

    private void loadItems(String section) {
        if (mNewsItems != null) mNewsItems.clear();
        showState(State.Loading);
        loadFromDb(section);
    }

    private void loadFromDb(String section){
         Disposable loadFromDb = Single.fromCallable(() -> ConverterNews.loadNewsFromDb(mContext, section))
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(this::checkResponseDb, this::handleError);
        mCompositeDisposable.add(loadFromDb);
    }

    private void checkResponseDb(List<NewsEntity> newsEntityList) {
        if (newsEntityList.size()==0){
            loadFromApi(getNewsCategory());
        } else {
            mNewsItems.addAll(newsEntityList);
            mAdapter.setNewsItems(mNewsItems);
            showState(State.HasData);
        }
    }

    private void loadFromApi(String section){
        mDisposableNews = RestAPI.getInstance()
                .getDataNewsEndpoint()
                .getNews(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::checkResponseApi, this::handleError);
        mCompositeDisposable.add(mDisposableNews);
    }

    private void checkResponseApi(DataNewsDTO dataNewsDTO) {
        if (dataNewsDTO.getResults().size()==0){
            showState(State.HasNoData);
        }
        else {
            Disposable saveNewsToDb = Single.fromCallable(dataNewsDTO::getResults)
                    .subscribeOn(Schedulers.io())
                    .map(listResultDto -> {
                        ConverterNews.saveAllNewsToDb(mContext, ConverterNews.dtoToDao(listResultDto, getNewsCategory()), getNewsCategory());
                        return ConverterNews.loadNewsFromDb(mContext, getNewsCategory());
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newsEntityList -> {
                        mNewsItems.addAll(newsEntityList);
                        mAdapter.setNewsItems(mNewsItems);
                    });
            mCompositeDisposable.add(saveNewsToDb);
            showState(State.HasData);
        }
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof IOException){
            showState(State.NetworkError);
            return;
        }
        showState(State.ServerError);
    }

    private void showState(State state) {
        switch (state){
            case HasData:
                mProgressBar.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);
                mTvError.setVisibility(View.GONE);
                mBtnError.setVisibility(View.GONE);

                mRecycler.setVisibility(View.VISIBLE);
                break;
            case HasNoData:
                mRecycler.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mTvError.setVisibility(View.GONE);
                mBtnError.setVisibility(View.GONE);

                mTvNoData.setText(getText(R.string.tv_no_data));
                mTvNoData.setVisibility(View.VISIBLE);
                break;
            case Loading:
                mRecycler.setVisibility(View.GONE);
                mTvError.setVisibility(View.GONE);
                mBtnError.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);

                mProgressBar.setVisibility(View.VISIBLE);
            break;
            case NetworkError:
                mRecycler.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);

                mTvError.setText(getText(R.string.tv_network_error));
                mTvError.setVisibility(View.VISIBLE);
                mBtnError.setVisibility(View.VISIBLE);
            break;
            case ServerError:
                mRecycler.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.GONE);
                mBtnError.setVisibility(View.GONE);

                mTvError.setText(getText(R.string.tv_server_error));
                mTvError.setVisibility(View.VISIBLE);
            break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+state);
        }
    }

    private String getNewsCategory(){
        return RestAPI.mSections[mSpinner.getSelectedItemPosition()];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                startActivity(new Intent(this, ActivityAbout.class));
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }
}
