package ru.malakhov.nytimes.ui.fragments.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.ui.fragments.news.MessageFragmentListener;

public class AboutFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_about;
    private static final String MAIL_TO_URI = "mailto:";

    private Toolbar mToolbar;
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
        View view = inflater.inflate(LAYOUT, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
//        setHasOptionsMenu(true);
//        setHomeButton(view);
        setLinkViews(view);
        setEmailSending(view);
    }

    private void setHomeButton(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.about_title));

        ActionBar supportActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setLinkViews(View view){
        view.findViewById(R.id.telegram_ico).setOnClickListener(v -> openURL(getString(R.string.url_telegram)));
        view.findViewById(R.id.vk_ico).setOnClickListener(v -> openURL(getString(R.string.url_vk)));
        view.findViewById(R.id.instagram_ico).setOnClickListener(v -> openURL(getString(R.string.url_instagram)));
    }

    private void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), getString(R.string.error_no_browser), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(String message) {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(MAIL_TO_URI));
        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.my_email)});
        sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject);
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, message);
        if (sendEmailIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(sendEmailIntent);
        } else {
            Toast.makeText(getContext(), getString(R.string.error_mail_client), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void setEmailSending(View view){
        TextView ptMessage = view.findViewById(R.id.message_edit);
        view.findViewById(R.id.message_send).setOnClickListener(v -> sendEmail(ptMessage.getText().toString()));
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backStack();
                break;
            default:
                throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    private void backStack(){
        if (mListener != null) {
            mListener.onNextMessageClicked(MainActivity.MESSAGE_BACK_STACK, null);
        }
    }*/
}

