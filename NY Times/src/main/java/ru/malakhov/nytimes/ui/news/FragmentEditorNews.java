package ru.malakhov.nytimes.ui.news;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.room.NewsEntity;
import ru.malakhov.nytimes.ui.activity.MainActivity;

public class FragmentEditorNews extends Fragment {
    private static final int LAYOUT = R.layout.fragment_editor_news;

    private EditText mDate;
    private EditText mTitle;
    private ImageView mImage;
    private EditText mCategory;
    private EditText mText;

    private NewsEntity mNewsEntity;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final static String ARGS_MESSAGE = "args:message";

    private MessageFragmentListener mListener;

    public static FragmentEditorNews newInstance(String newsID){
        FragmentEditorNews fragmentEditorNews = new FragmentEditorNews();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_MESSAGE, newsID);
        fragmentEditorNews.setArguments(bundle);
        return fragmentEditorNews;
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
        mCompositeDisposable.dispose();
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

    private void init(View view) {
        setHasOptionsMenu(true);
        setHomeButton(view);
        findViews(view);
        getNews();
    }

    private void getNews() {
        if (getArguments() != null) {
            Disposable getNews = Single.fromCallable(() -> NewsConverter
                    .getNewsById(getContext(), getArguments().getString(ARGS_MESSAGE))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(v -> {
                        mNewsEntity = v;
                        setDataViews();
                    });
            mCompositeDisposable.add(getNews);
        }
    }

    private void setHomeButton(View view) {
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        ActionBar supportActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void findViews(View view){
        mDate = view.findViewById(R.id.et_details_date);
        mTitle = view.findViewById(R.id.et_details_title);
        mImage = view.findViewById(R.id.details_image);
        mCategory = view.findViewById(R.id.et_details_category);
        mText = view.findViewById(R.id.et_details_text);
    }

    private void setDataViews() {
        Glide.with(this)
                .load(mNewsEntity.getImageUrl())
                .into(mImage);
        mDate.setText(mNewsEntity.getPublishedDate());
        mCategory.setText(mNewsEntity.getCategory());
        mText.setText(mNewsEntity.getText());
        mTitle.setText(mNewsEntity.getTitle());
    }

    private void editNews() {
        mNewsEntity.setPublishedDate(mDate.getText().toString());
        mNewsEntity.setCategory(mCategory.getText().toString());
        mNewsEntity.setTitle(mTitle.getText().toString());
        mNewsEntity.setText(mText.getText().toString());
        Disposable saveNews =Completable
                .fromCallable(() -> {
                    NewsConverter.editNewsToDb(getContext(), mNewsEntity);
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mCompositeDisposable.add(saveNews);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor_news, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backStack();
                break;
            case R.id.menu_save:
                editNews();
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
