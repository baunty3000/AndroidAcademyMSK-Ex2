package ru.malakhov.nytimes.ui.fragments.intro;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class IntroPagerAdapter extends FragmentStatePagerAdapter {

    private List<ScreenSlidePageFragment> mFragmentIntroList;

    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentIntroList = new ArrayList<>();
        mFragmentIntroList.add(ScreenSlidePageFragment.newInstance(ScreenSlidePageFragment.IMAGE_LIST));
        mFragmentIntroList.add(ScreenSlidePageFragment.newInstance(ScreenSlidePageFragment.IMAGE_DETAILS));
        mFragmentIntroList.add(ScreenSlidePageFragment.newInstance(ScreenSlidePageFragment.IMAGE_ABOUT));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentIntroList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentIntroList.size();
    }
}
