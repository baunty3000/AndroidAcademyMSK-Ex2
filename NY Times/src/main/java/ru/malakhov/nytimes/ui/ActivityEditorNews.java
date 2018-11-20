package ru.malakhov.nytimes.ui;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.room.NewsEntity;

public class ActivityEditorNews extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_editor_news;
    private static final String EXTRA_ID = "EXTRA_ID";

    private Context mContext;
    private EditText mDate;
    private EditText mCategory;
    private EditText mText;
    private EditText mTitle;

    private NewsEntity mNewsEntity;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static void start(@NonNull Context context, @NonNull String newsId) {
        context.startActivity(new Intent(context, ActivityEditorNews.class)
                .putExtra(EXTRA_ID, newsId));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeDisposable.dispose();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        init();
    }

    private void init() {
        mContext = this;
        setHomeButton();
        findViews();
        setHomeButton();
        Disposable getNews = Single.fromCallable(() -> ConverterNews.getNewsById(mContext, getIntent().getStringExtra(EXTRA_ID)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> {
                    mNewsEntity = v;
                    setDataViews();
                });
        mCompositeDisposable.add(getNews);
    }

    private void setHomeButton() {
        final Toolbar toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void findViews(){
        mDate = findViewById(R.id.et_details_date);
        mCategory = findViewById(R.id.et_details_category);
        mText = findViewById(R.id.et_details_text);
        mTitle = findViewById(R.id.et_details_title);
    }

    private void setDataViews() {
        Glide.with(this).load(mNewsEntity.getImageUrl()).into(((ImageView) findViewById(R.id.details_image)));
        mDate.setText(mNewsEntity.getPublishedDate());
        mCategory.setText(mNewsEntity.getSubsection());
        mText.setText(mNewsEntity.getAbstract());
        mTitle.setText(mNewsEntity.getTitle());
    }

    private void editNews() {
        mNewsEntity.setPublishedDate(mDate.getText().toString());
        mNewsEntity.setSubsection(mCategory.getText().toString());
        mNewsEntity.setAbstract(mText.getText().toString());
        mNewsEntity.setTitle(mTitle.getText().toString());
        Disposable saveNews =Completable
                .fromCallable(() -> {
                    ConverterNews.editNewsToDb(mContext, mNewsEntity);
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mCompositeDisposable.add(saveNews);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                editNews();
                finish();
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }
}
