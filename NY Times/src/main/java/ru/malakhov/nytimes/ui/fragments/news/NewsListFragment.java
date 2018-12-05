
package ru.malakhov.nytimes.ui.fragments.news;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.network.RestApi;
import ru.malakhov.nytimes.data.network.dto.DataNewsDto;
import ru.malakhov.nytimes.data.room.NewsEntity;
import ru.malakhov.nytimes.ui.fragments.news.adapter.AdapterRecyclerNews;

public class NewsListFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_news_list;
    private TextView mTvError;
    private TextView mTvNoData;
    private Button mBtnError;
    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;
    private Disposable mDisposableNews;
    private String mSpinnerItem;

    private MessageFragmentListener mListener;

    private List<NewsEntity> mNewsItems = new ArrayList<>();
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private AdapterRecyclerNews mAdapter;

    String TAG = "info";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        if (context instanceof MessageFragmentListener){
            mListener = (MessageFragmentListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
    
    

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView news: ");
        View view = inflater.inflate(LAYOUT, container, false);
        init(view);
        setItemOrientation(mRecycler); // отображение элементов в разных ориентациях
        mAdapter = new AdapterRecyclerNews(getContext());
        mRecycler.setAdapter(mAdapter);

        if (mSpinnerItem != null && !mSpinnerItem.isEmpty()){ // если есть элемент категории, делаем инициализацию спиннера
            loadItems(mSpinnerItem);
        }
        return view;
    }

    private void findViews(View view){
        mRecycler = view.findViewById(R.id.rv_news);
        mTvError = view.findViewById(R.id.tv_network_error);
        mBtnError = view.findViewById(R.id.btn_try_again);
        mTvNoData = view.findViewById(R.id.tv_no_data);
        mProgressBar = view.findViewById(R.id.pr_bar_recycler);
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    public void setSpinnerItem(String spinnerItem) {
        mSpinnerItem = spinnerItem;
        loadItems(spinnerItem);
    }

    private void setItemOrientation(RecyclerView rvNews) {
        int px = getResources().getDimensionPixelSize(R.dimen.spacing_small);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNews.addItemDecoration(new NewsItemDecoration(px, Configuration.ORIENTATION_PORTRAIT));
    }

    private void init(View view) {
        findViews(view);
        setFab(view);
        mBtnError.setOnClickListener(v -> loadItems(mSpinnerItem));
    }

    private void setFab(View view) {
        view.findViewById(R.id.fab).setOnClickListener(v -> {
            mNewsItems.clear();
            loadFromApi(mSpinnerItem);
        });
    }

    private void loadItems(String section) {
        mNewsItems.clear();
        showState(State.Loading);
        loadFromDb(section);
    }

    private void loadFromDb(String section){
        Disposable loadFromDb = Single.fromCallable(() -> NewsConverter
                .loadNewsFromDb(getContext(), section))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::checkResponseDb, this::handleError);
        mCompositeDisposable.add(loadFromDb);
    }

    private void checkResponseDb(List<NewsEntity> newsEntityList) {
        if (newsEntityList.size()==0){
            loadFromApi(mSpinnerItem);
        } else {
            mNewsItems.addAll(newsEntityList);
            mAdapter.setNewsItems(mNewsItems);
            showState(State.HasData);
        }
    }

    private void loadFromApi(String section){
        mDisposableNews = RestApi.getInstance()
                .getDataNewsEndpoint()
                .getNews(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::checkResponseApi, this::handleError);
        mCompositeDisposable.add(mDisposableNews);
    }

    private void checkResponseApi(DataNewsDto dataNewsDTO) {
        if (dataNewsDTO.getResults().size()==0){
            showState(State.HasNoData);
        }
        else {
            Disposable saveNewsToDb = Single.fromCallable(dataNewsDTO::getResults)
                    .subscribeOn(Schedulers.io())
                    .map(listResultDto -> {
                        NewsConverter.saveAllNewsToDb(getContext(), NewsConverter
                                .dtoToDao(listResultDto, mSpinnerItem), mSpinnerItem);
                        return NewsConverter.loadNewsFromDb(getContext(), mSpinnerItem);
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
}
