package ru.malakhov.nytimes.ui.news;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.room.NewsEntity;
import ru.malakhov.nytimes.ui.activity.MainActivity;

public class FragmentFullNews extends Fragment {

    private static final int LAYOUT = R.layout.fragment_full_news;
    private final static String ARGS_MESSAGE = "args:message";
    public final static String MESSAGE_ID = "NewsEdit";


    private NewsEntity mNewsEntity;
    private Disposable mGetNews;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private TextView mPublishedDate;
    private TextView mTitle;
    private ImageView mImage;
    private CollapsingToolbarLayout mCategory;
    private TextView mText;

    private MessageFragmentListener mListener;

    public static FragmentFullNews newInstance(String newsID){
        FragmentFullNews fragmentFullNews = new FragmentFullNews();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_MESSAGE, newsID);
        fragmentFullNews.setArguments(bundle);
        return fragmentFullNews;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageFragmentListener){
            mListener = (MessageFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        init(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getNews();
    }

    private void init(View view) {
        setHasOptionsMenu(true);
        setHomeButton(view);
        findViews(view);
    }

    private void findViews(View view){
        mTitle = view.findViewById(R.id.details_title);
        mImage = view.findViewById(R.id.details_image);
        mCategory = view.findViewById(R.id.details_category);
        mPublishedDate = view.findViewById(R.id.details_date);
        mText = view.findViewById(R.id.details_text);
    }

    private void getNews(){
        if (getArguments() != null) {
            mGetNews = Single.fromCallable(() -> NewsConverter
                    .getNewsById(getContext(), getArguments().getString(ARGS_MESSAGE))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(v -> {
                        mNewsEntity = v;
                        setDataViews();
                    });
            mCompositeDisposable.add(mGetNews);
        }
    }

    private void setHomeButton(View view) {
        ((AppCompatActivity)getContext()).setSupportActionBar(view.findViewById(R.id.toolbar));
        ActionBar supportActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setDataViews() {
        mTitle.setText(mNewsEntity.getTitle());
        setImage(mNewsEntity.getImageUrl());
        mCategory.setTitle(mNewsEntity.getCategory());
        mPublishedDate.setText(mNewsEntity.getPublishedDate());
        mText.setText(mNewsEntity.getText());
    }

    private void setImage(String url) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.image_placeholder);
        if (url.isEmpty()){ // если нет картинок, ставим дефолтную картинку
            Glide.with(this)
                    .load(R.drawable.no_image)
                    .into(mImage);
            return;
        }
        Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(mImage);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_full_news, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backStack();
                break;
            case R.id.menu_edit:
                if (mListener != null & getArguments() != null) {
                    mListener.onNextMessageClicked(MESSAGE_ID, getArguments().getString(ARGS_MESSAGE));
                }
                break;
            case R.id.menu_delete:
                Disposable deleteNews = Single.fromCallable(() -> {
                    NewsConverter.deleteNewsFromDb(getContext(), mNewsEntity);
                    return true; })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
                mCompositeDisposable.add(deleteNews);
                backStack();
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    private void backStack(){
        if (mListener != null) {
            mListener.onNextMessageClicked(MainActivity.MESSAGE_BACK_STACK, null);
        }
    }
}
