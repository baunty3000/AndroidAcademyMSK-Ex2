package ru.malakhov.nytimes.ui.fragments.intro;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.ui.fragments.news.MessageFragmentListener;

public class IntroFragment extends Fragment {

    public static final String KEY_INTRO = "KEY_INTRO";
    private MessageFragmentListener mListener;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        FragmentManager childFragmentManager = getChildFragmentManager();
        IntroPagerAdapter pagerAdapter = new IntroPagerAdapter(childFragmentManager);
        viewPager.setAdapter(pagerAdapter);
        CircleIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        view.findViewById(R.id.intro_text).setOnClickListener(v -> {
            if (mListener != null){
                mListener.onNewsItemClicked(KEY_INTRO, null);
            }
        });
        return view;
    }
}
