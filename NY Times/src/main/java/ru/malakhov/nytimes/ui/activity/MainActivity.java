package ru.malakhov.nytimes.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.ui.news.adapter.ViewHolderNews;
import ru.malakhov.nytimes.ui.about.AboutFragment;
import ru.malakhov.nytimes.ui.news.FragmentEditorNews;
import ru.malakhov.nytimes.ui.news.FragmentFullNews;
import ru.malakhov.nytimes.ui.news.FragmentNewsList;
import ru.malakhov.nytimes.ui.news.MessageFragmentListener;
import ru.malakhov.nytimes.ui.intro.IntroFragment;

public class MainActivity extends AppCompatActivity implements MessageFragmentListener{
    private static final int LAYOUT = R.layout.activity_main;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private int mFrame;
    public final static String MESSAGE_BACK_STACK = "TAG_BACK_STACK";
    private boolean mIsTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        mIsTablet = findViewById(R.id.frame_details) != null;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT & savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .replace(mFrame, new IntroFragment())
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(mFrame, new FragmentNewsList())
                    .commit();
            }
        }

    private void init(){
        setContentView(LAYOUT);
        mFragmentManager = getSupportFragmentManager();
        mFrame = R.id.frame_list;
    }

    @Override
    public void onNextMessageClicked(String messageID, String message) {
        if (messageID.equals(MainActivity.MESSAGE_BACK_STACK)){ // вернуть предыдущий фрагмент
            mFragmentManager.popBackStack();
            return;
        }
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (messageID){
            case IntroFragment.MESSAGE_ID : // запуск фрагмента с новостями
                mFragmentTransaction
                        .replace(mFrame, new FragmentNewsList());
                break;
            case ViewHolderNews.MESSAGE_ID : // запуск фрагмента с полной новостью
                if (mIsTablet){
                    mFragmentTransaction
                            .addToBackStack(null)
                            .replace(R.id.frame_details, FragmentFullNews.newInstance(message));
                } else{
                    mFragmentTransaction
                            .addToBackStack(null)
                            .replace(mFrame, FragmentFullNews.newInstance(message));
                }
                break;
            case FragmentFullNews.MESSAGE_ID : // запуск фрагмента с редактированием новости
                mFragmentTransaction
                        .addToBackStack(null)
                        .replace(mFrame, FragmentEditorNews.newInstance(message));
                break;
            case FragmentNewsList.MESSAGE_ID : // запуск фрагмента About
                mFragmentTransaction
                        .addToBackStack(null)
                        .replace(mFrame, new AboutFragment());
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+messageID);
        }
        mFragmentTransaction.commit();
    }
}
