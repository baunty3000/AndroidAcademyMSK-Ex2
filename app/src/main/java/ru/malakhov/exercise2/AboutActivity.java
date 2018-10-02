package ru.malakhov.exercise2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mPtMessage;

    private Button mBtnSend;

    private ImageButton mIbTelIcon;

    private ImageButton mIbVkIcon;

    private ImageButton mIbInstIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPtMessage = findViewById(R.id.ptMessage);
        mBtnSend = findViewById(R.id.btnSend);
        mIbTelIcon = findViewById(R.id.ibTelIcon);
        mIbVkIcon = findViewById(R.id.ibVkIcon);
        mIbInstIcon = findViewById(R.id.ibInstIcon);

        mBtnSend.setOnClickListener(this);
        mIbTelIcon.setOnClickListener(this);
        mIbVkIcon.setOnClickListener(this);
        mIbInstIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                sendEmail(new String[]{"baunty3000@gmail.com"}, "Привет! Я из приложения !",
                        mPtMessage.getText().toString());
                break;
            case R.id.ibTelIcon:
                openURL("https://telegram.me/jaymsk");
                break;
            case R.id.ibVkIcon:
                openURL("https://vk.com/s6000");
                break;
            case R.id.ibInstIcon:
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
}
