package ru.malakhov.nytimes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAbout extends AppCompatActivity implements View.OnClickListener {
    private static final int LAYOUT = R.layout.activity_about;

    private EditText mPtMessage;
    private Button mBtnAboutSend;
    private ImageButton mIbAboutTelICO;
    private ImageButton mIbAboutVkICO;
    private ImageButton mIboutInstICO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mPtMessage = findViewById(R.id.ptAboutMessage);
        mBtnAboutSend = findViewById(R.id.btnAboutSend);
        mIbAboutTelICO = findViewById(R.id.ibAboutTelICO);
        mIbAboutVkICO = findViewById(R.id.ibAboutVkICO);
        mIboutInstICO = findViewById(R.id.ibAboutInstICO);

        mBtnAboutSend.setOnClickListener(this);
        mIbAboutTelICO.setOnClickListener(this);
        mIbAboutVkICO.setOnClickListener(this);
        mIboutInstICO.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAboutSend:
                sendEmail(new String[]{"baunty3000@gmail.com"}, "Привет! Я из приложения !",
                        mPtMessage.getText().toString());
                break;
            case R.id.ibAboutTelICO:
                openURL("https://telegram.me/jaymsk");
                break;
            case R.id.ibAboutVkICO:
                openURL("https://vk.com/s6000");
                break;
            case R.id.ibAboutInstICO:
                openURL("https://instagram.com/malakhov5");
                break;
        }
    }

    private void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Установите браузер", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(String[] to, String subject, String body) {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO);
        sendEmailIntent.setData(Uri.parse("mailto:"));
        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, body);
        if (sendEmailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendEmailIntent);
        } else {
            Toast.makeText(this, "Для отправки почты нужен почтовый клиент", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
