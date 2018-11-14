package ru.malakhov.nytimes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.malakhov.nytimes.R;

public class ActivityIntro extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_intro;
    private static final String SHARED_PREF = "INTRO_COUNTER";
    private static final String SHARED_PREF_KEY_COUNTER = "COUNTER";
    private static final int START_DELAY_SECOND_ACTIVITY = 3;
    private int mCounter = 1;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        if (needToShowIntro()) {
            Disposable disposable = Completable.complete()
                    .delay(START_DELAY_SECOND_ACTIVITY, TimeUnit.SECONDS)
                    .subscribe(this::startSecondActivity);
            mDisposable.add(disposable);
        } else {
            startSecondActivity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePrefCounter();
    }

    private void savePrefCounter() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHARED_PREF_KEY_COUNTER, ++mCounter);
        editor.apply();
    }

    private int loadPrefCounter() {
        return getSharedPreferences(SHARED_PREF, MODE_PRIVATE).getInt(SHARED_PREF_KEY_COUNTER, 0);
    }

    private void startSecondActivity() {
        startActivity(new Intent(this, ActivityRecyclerNews.class));
        finish();
    }

    private boolean needToShowIntro() {
        int loadCounter = loadPrefCounter();
        mCounter = loadCounter == 0 ? mCounter : loadCounter;
        return mCounter % 2 == 0;
    }
}
