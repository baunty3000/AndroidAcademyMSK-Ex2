
package ru.malakhov.nytimes.ui;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.network.RestAPI;
import ru.malakhov.nytimes.data.network.dto.DataNewsDTO;
import ru.malakhov.nytimes.data.network.dto.ResultDTO;
import ru.malakhov.nytimes.ui.adapter.AdapterRecyclerNews;

public class ActivityRecyclerNews extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_recycler_news;
    private final int mSpinnerDefaultItem = 0;
    private TextView mTvError;
    private TextView mTvNoData;
    private Button mBtnError;
    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;
    private Disposable mDisposableNews;
    private Spinner mSpinner;

    private static List<ResultDTO> mNewsItems;
    CompositeDisposable mCompositeDisposable;
    AdapterRecyclerNews mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        mNewsItems = new ArrayList<>();
        mCompositeDisposable = new CompositeDisposable();
        init();
        setItemOrientation(mRecycler); // отображение элементов в разных ориентациях
        mAdapter = new AdapterRecyclerNews(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeDisposable.dispose();
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
        findViews();
        setToolbar();
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
        mSpinner.setSelection(mSpinnerDefaultItem);
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

    private void loadItems(String section) {
        showState(State.Loading);
        mDisposableNews = RestAPI.getInstance()
                .getDataNewsEndpoint()
                .getNews(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::checkResponse, this::handleError);
        mCompositeDisposable.add(mDisposableNews);
    }

    private void checkResponse(DataNewsDTO dataNewsDTO) {
        if (mNewsItems != null) mNewsItems.clear();
        if (dataNewsDTO.getResults().size()==0){
            showState(State.HasNoData);
        }
        else {
            mNewsItems.addAll(dataNewsDTO.getResults());
            mAdapter.setNewsItems(mNewsItems);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
