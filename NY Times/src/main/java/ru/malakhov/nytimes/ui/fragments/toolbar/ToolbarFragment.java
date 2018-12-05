package ru.malakhov.nytimes.ui.fragments.toolbar;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.network.RestApi;
import ru.malakhov.nytimes.ui.activity.MainActivity;
import ru.malakhov.nytimes.ui.fragments.about.AboutFragment;
import ru.malakhov.nytimes.ui.fragments.intro.IntroFragment;
import ru.malakhov.nytimes.ui.fragments.news.FullNewsFragment;
import ru.malakhov.nytimes.ui.fragments.news.MessageFragmentListener;
import ru.malakhov.nytimes.ui.fragments.news.NewsConverter;
import ru.malakhov.nytimes.ui.fragments.news.NewsEditorFragment;
import ru.malakhov.nytimes.ui.fragments.news.NewsListFragment;

public class ToolbarFragment extends Fragment {

    public final static String KEY_MENU_BACK_STACK = "KEY_MENU_BACK_STACK";
    public final static String KEY_MENU_ABOUT = "KEY_MENU_ABOUT";
    public final static String KEY_MENU_DELETE = "KEY_MENU_DELETE";
    public final static String KEY_MENU_EDIT = "KEY_MENU_EDIT";
    public final static String KEY_MENU_SAVE = "KEY_MENU_SAVE";

    private static final int LAYOUT = R.layout.fragment_toolbar;
    private final static String ARGS_MESSAGE = "args:message";
    private Toolbar mToolbar;
    private Spinner mSpinner;
    private ActionBar mSupportActionBar;
    private final static int SPINNER_DEFAULT_ITEM = 0;
    private Menu mMenu;
    private MenuItem mMenuAbout;
    private MenuItem mMenuDelete;
    private MenuItem mMenuEdit;
    private MenuItem mMenuSave;

    private int mItemPositionSpinner;
String TAG = "info";

    private MessageToolbarListener mListener;

    public static ToolbarFragment newInstance(String fragmentID){
        ToolbarFragment toolBarFragment = new ToolbarFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_MESSAGE, fragmentID);
        toolBarFragment.setArguments(bundle);
        return toolBarFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageFragmentListener){
            mListener = (MessageToolbarListener) context;
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
        View view = inflater.inflate(LAYOUT, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        initSpinner(view);
        mToolbar = view.findViewById(R.id.toolbar);
        initToolbar();
    }

    private void initToolbar(){
        setHasOptionsMenu(true);
        ((AppCompatActivity) getContext()).setSupportActionBar(mToolbar);
        mSupportActionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
    }

    private void setHomeButton(boolean visibility) {
        if (mSupportActionBar != null) {
            mSupportActionBar.setDisplayHomeAsUpEnabled(visibility);
        }
    }

    private void initSpinner(View view){
        mSpinner = view.findViewById(R.id.spinner);
        mSpinner.setVisibility(View.GONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, RestApi.mSections);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(SPINNER_DEFAULT_ITEM);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mItemPositionSpinner = position;
                mListener.onNextItemSpinner(RestApi.mSections[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String getArgument(){
        return getArguments().getString(ARGS_MESSAGE);
    }

    public void setToolbar(String tag){
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_MESSAGE, tag);
        this.setArguments(bundle);
        if (mMenu != null){
            onPrepareOptionsMenu(mMenu);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        findViews(menu);
        goneVisibilityMenuItems();

        switch (getArgument()){
            case MainActivity.TAG_INTRO :
                break;
            case MainActivity.TAG_ABOUT :
                setHomeButton(true);
                break;
            case MainActivity.TAG_LIST_NEWS :
                setHomeButton(false);
                mSpinner.setVisibility(View.VISIBLE);
                mMenuAbout.setVisible(true);
                break;
            case MainActivity.TAG_FULL_NEWS :
                setHomeButton(true);
                mMenuEdit.setVisible(true);
                mMenuDelete.setVisible(true);
                break;
            case MainActivity.TAG_EDITOR_NEWS :
                setHomeButton(true);
                mMenuSave.setVisible(true);
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+getArgument());
        }
    }

    private void goneVisibilityMenuItems() {
        mMenuAbout.setVisible(false);
        mMenuDelete.setVisible(false);
        mMenuEdit.setVisible(false);
        mMenuSave.setVisible(false);
        mSpinner.setVisibility(View.GONE);
    }

    private void findViews(Menu menu){
        mMenuAbout = menu.findItem(R.id.menu_about);
        mMenuDelete = menu.findItem(R.id.menu_delete);
        mMenuEdit = menu.findItem(R.id.menu_edit);
        mMenuSave = menu.findItem(R.id.menu_save);
    }

        @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mListener.onItemToolbarClicked(KEY_MENU_BACK_STACK, getArgument());
                break;
            case R.id.menu_about:
                mListener.onItemToolbarClicked(KEY_MENU_ABOUT, null);
                break;
            case R.id.menu_edit:
                mListener.onItemToolbarClicked(KEY_MENU_EDIT, null);
                break;
            case R.id.menu_delete:
                mListener.onItemToolbarClicked(KEY_MENU_DELETE, null);
                break;
            case R.id.menu_save:
                mListener.onItemToolbarClicked(KEY_MENU_SAVE, null);
                break;
            default: throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

}
