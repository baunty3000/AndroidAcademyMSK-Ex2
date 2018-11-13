
package ru.malakhov.nytimes.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import ru.malakhov.nytimes.R;

public class ActivityAbout extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_about;
    private static final String MAIL_TO_URI = "mailto:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startInit();
        setHomeButton();
        setLinkViews();
        setEmailSending();
    }

    private void startInit() {
        setContentView(LAYOUT);
        getSupportActionBar().setTitle(getString(R.string.about_title));
    }

    private void setLinkViews(){
        findViewById(R.id.telegram_ico).setOnClickListener(v -> openURL(getString(R.string.url_telegram)));
        findViewById(R.id.vk_ico).setOnClickListener(v -> openURL(getString(R.string.url_vk)));
        findViewById(R.id.instagram_ico).setOnClickListener(v -> openURL(getString(R.string.url_instagram)));
    }

    private void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.error_no_browser), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(String message) {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(MAIL_TO_URI));
        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.my_email)});
        sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject);
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, message);
        if (sendEmailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendEmailIntent);
        } else {
            Toast.makeText(this, getString(R.string.error_mail_client), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void setEmailSending(){
        TextView ptMessage = findViewById(R.id.message_edit);
        findViewById(R.id.message_send).setOnClickListener(v -> sendEmail(ptMessage.getText().toString()));
    }

    private void setHomeButton(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                throw new IllegalArgumentException(getString(R.string.error_no_id)+": "+item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }
}
