package ru.malakhov.nytimes.ui.fragments.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.malakhov.nytimes.R;

public class ScreenSlidePageFragment extends Fragment {

    private final static String ARGS_MESSAGE = "args:message";
    public final static int IMAGE_LIST = R.mipmap.news_list;
    public final static int IMAGE_DETAILS = R.mipmap.news_details;
    public final static int IMAGE_ABOUT = R.mipmap.about;

    public static ScreenSlidePageFragment newInstance(int imageKey){
        ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_MESSAGE, imageKey);
        screenSlidePageFragment.setArguments(bundle);
        return screenSlidePageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_pager_intro, container, false);
        if (getArguments() != null) {
            ((ImageView)view.findViewById(R.id.image_intro)).setImageResource(getArguments().getInt(ARGS_MESSAGE));
        }
        return view;
    }
}