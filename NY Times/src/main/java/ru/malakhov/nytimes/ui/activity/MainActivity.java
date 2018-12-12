package ru.malakhov.nytimes.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.ui.fragments.MessageFragmentListener;
import ru.malakhov.nytimes.ui.fragments.about.AboutFragment;
import ru.malakhov.nytimes.ui.fragments.intro.IntroFragment;
import ru.malakhov.nytimes.ui.fragments.news.EditorNewsFragment;
import ru.malakhov.nytimes.ui.fragments.news.FullNewsFragment;
import ru.malakhov.nytimes.ui.fragments.news.NewsListFragment;

public class MainActivity extends AppCompatActivity implements MessageFragmentListener {

    private static final int LAYOUT = R.layout.activity_main;
    private FragmentManager mFragmentManager;

    private static final String SHARED_PREF = "INTRO_COUNTER";
    private static final String SHARED_PREF_KEY_COUNTER = "COUNTER";
    private int mCounter = 1;

    public static final String BACK_STACK = "BACK_STACK";
    public static final String TAG_INTRO = "TAG_INTRO";
    public static final String TAG_LIST_NEWS = "TAG_LIST_NEWS";
    public static final String TAG_FULL_NEWS = "TAG_FULL_NEWS";
    public static final String TAG_EDITOR_NEWS = "TAG_EDITOR_NEWS";
    public static final String TAG_ABOUT = "TAG_ABOUT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (savedInstanceState == null){
            if (needToShowIntro()) { // через раз показываем интро
                startIntro();
            } else {
                startNewsList();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePrefCounter();
    }

    private void replaceFragment(Fragment fragment, int frame, boolean addToBackStack, String tag){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction()
                .replace(frame, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    private void startNewsList() {
        replaceFragment(new NewsListFragment(), R.id.frame_list, false, TAG_LIST_NEWS);
    }

    private void startIntro() {
        replaceFragment(new IntroFragment(), R.id.frame_list, false, TAG_INTRO);
    }

    private void init(){
        setContentView(LAYOUT);
        mFragmentManager = getSupportFragmentManager();
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

    private boolean needToShowIntro() {
        int loadCounter = loadPrefCounter();
        mCounter = loadCounter == 0 ? mCounter : loadCounter;
        return mCounter % 2 != 0;
    }

    @Override
    public void onNewsItemClicked(String fragmentTag, String message) {
        switch (fragmentTag){
            case BACK_STACK :
                mFragmentManager.popBackStack();
                break;
            case TAG_LIST_NEWS :
                startNewsList();
                break;
            case TAG_FULL_NEWS :
                replaceFragment(FullNewsFragment.newInstance(message), R.id.frame_list, true, TAG_FULL_NEWS);
                break;
            case TAG_EDITOR_NEWS :
                replaceFragment(EditorNewsFragment.newInstance(message), R.id.frame_list, true, TAG_EDITOR_NEWS);
                break;
            case TAG_ABOUT :
                replaceFragment(new AboutFragment(), R.id.frame_list, true, TAG_ABOUT);
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+fragmentTag);
        }
    }
}
