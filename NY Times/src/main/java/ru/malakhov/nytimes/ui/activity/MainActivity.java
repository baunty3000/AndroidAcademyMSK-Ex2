package ru.malakhov.nytimes.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.ui.fragments.news.adapter.ViewHolderNews;
import ru.malakhov.nytimes.ui.fragments.about.AboutFragment;
import ru.malakhov.nytimes.ui.fragments.news.NewsEditorFragment;
import ru.malakhov.nytimes.ui.fragments.news.FullNewsFragment;
import ru.malakhov.nytimes.ui.fragments.news.NewsListFragment;
import ru.malakhov.nytimes.ui.fragments.news.MessageFragmentListener;
import ru.malakhov.nytimes.ui.fragments.intro.IntroFragment;
import ru.malakhov.nytimes.ui.fragments.toolbar.MessageToolbarListener;
import ru.malakhov.nytimes.ui.fragments.toolbar.ToolbarFragment;

public class MainActivity extends AppCompatActivity implements MessageFragmentListener,
        MessageToolbarListener {
    private static final int LAYOUT = R.layout.activity_main;
    private FragmentManager mFragmentManager;
    private int mFrameList;
    private FrameLayout mFrameToolbar;
    private ToolbarFragment mToolbarFragment;
    private NewsListFragment mNewsListFragment;
    private String mNewsId;
    private boolean isTwoPanel;
    String TAG = "info";

    public static final String TAG_INTRO = "TAG_INTRO";
    public static final String TAG_LIST_NEWS = "TAG_LIST_NEWS";
    public static final String TAG_FULL_NEWS = "TAG_FULL_NEWS";
    public static final String TAG_EDITOR_NEWS = "TAG_EDITOR_NEWS";
    public static final String TAG_ABOUT = "TAG_ABOUT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (savedInstanceState == null) {
            initToolbar();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                startIntro();
            } else {
                replaceFragment(new NewsListFragment(), mFrameList, false, TAG_LIST_NEWS);
                mFrameToolbar.setVisibility(View.VISIBLE);
                mToolbarFragment.setToolbar(TAG_LIST_NEWS);
            }
        }
    }

    private void init(){
        setContentView(LAYOUT);
        mFrameList = R.id.frame_list;
        mFragmentManager = getSupportFragmentManager();
        isTwoPanel = findViewById(R.id.frame_details) != null;
    }

    private void startIntro() {
        replaceFragment(new IntroFragment(), mFrameList, false, TAG_INTRO);
    }

    private void initToolbar(){
        mFrameToolbar = findViewById(R.id.f_toolbar);
        mFrameToolbar.setVisibility(View.GONE);
        mToolbarFragment = ToolbarFragment.newInstance(TAG_INTRO);
        mFragmentManager.beginTransaction()
                .replace(R.id.f_toolbar, mToolbarFragment)
                .commit();
    }



    private void replaceFragment(Fragment fragment, int frame, boolean addToBackStack, String tag){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction()
                .replace(frame, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onNextItemSpinner(String selectedItemPosition) {
        NewsListFragment newsList = (NewsListFragment) mFragmentManager.
                findFragmentByTag(TAG_LIST_NEWS);
        newsList.setSpinnerItem(selectedItemPosition);
    }

    @Override
    public void onNewsItemClicked(String messageID, String message) {
        mFrameToolbar.setVisibility(View.VISIBLE);
        switch (messageID){
            case IntroFragment.KEY_INTRO : // запуск фрагмента с новостями
                mToolbarFragment.setToolbar(TAG_LIST_NEWS);
                mNewsListFragment = new NewsListFragment();
                replaceFragment(mNewsListFragment, mFrameList, false, TAG_LIST_NEWS);
                break;
            case ViewHolderNews.KEY_HOLDER : // запуск фрагмента с полной новостью
                int frame = isTwoPanel ? R.id.frame_details : R.id.frame_list;
                mNewsId = message; // сохраняем id новости
                mToolbarFragment.setToolbar(TAG_FULL_NEWS);
                replaceFragment(FullNewsFragment.newInstance(message), frame, true, TAG_FULL_NEWS);
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+messageID);
        }
    }

    @Override
    public void onItemToolbarClicked(String keyMenu, String message) {
        if (keyMenu.equals(ToolbarFragment.KEY_MENU_BACK_STACK)){ // обрабатываем отрисовку тулбара для каждого фрагмента, при нажатии на бекстек
            switch (message){
                case TAG_EDITOR_NEWS :
                    mToolbarFragment.setToolbar(TAG_FULL_NEWS);
                    mFragmentManager.popBackStack();
                    break;
                default:
                    mToolbarFragment.setToolbar(TAG_LIST_NEWS);
                    mFragmentManager.popBackStack();
                    break;
            }
            return;
        }

        switch (keyMenu){
            case ToolbarFragment.KEY_MENU_ABOUT :
                mToolbarFragment.setToolbar(TAG_ABOUT);
                replaceFragment(new AboutFragment(), mFrameList,true, TAG_ABOUT);
                break;
            case ToolbarFragment.KEY_MENU_DELETE:
                mToolbarFragment.setToolbar(TAG_LIST_NEWS);
                ((FullNewsFragment) mFragmentManager.findFragmentByTag(TAG_FULL_NEWS)).deleteNews(); //удаляем новость
                mFragmentManager.popBackStack();
                break;
            case ToolbarFragment.KEY_MENU_EDIT :
                mToolbarFragment.setToolbar(TAG_EDITOR_NEWS);
                replaceFragment(NewsEditorFragment.newInstance(mNewsId), mFrameList, true, TAG_EDITOR_NEWS);
                break;
            case ToolbarFragment.KEY_MENU_SAVE :
                mToolbarFragment.setToolbar(TAG_FULL_NEWS);
                ((NewsEditorFragment) mFragmentManager.findFragmentByTag(TAG_EDITOR_NEWS)).editNews(); //редактируем новость
                mFragmentManager.popBackStack();
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+keyMenu);
        }
    }
}
