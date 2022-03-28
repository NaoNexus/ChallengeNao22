package com.example.naocontroller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class Nao_description extends Activity  {
    TextView authorText,
             descriptionText,
             titleText,
             songText;

    ImageView paintingView;

    int paintingNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        paintingNumber = b.getInt("painting");

        setContentView(R.layout.activity_nao_description);

        titleText = findViewById(R.id.txt_painting_title);
        authorText = findViewById(R.id.txt_painting_author);
        songText = findViewById(R.id.txt_painting_song);
        descriptionText = findViewById(R.id.txt_painting_description);
        paintingView = findViewById(R.id.painting_image);

        setTexts();
        messageReceiver();
    }

    private void messageReceiver() {
        Message_receiver message_receiver = new Message_receiver(message_received -> {

            Toast.makeText(getApplicationContext(), "Messaggio ricevuto: " + message_received, Toast.LENGTH_LONG).show();
            finish();
        });
        message_receiver.execute();
    }

    private void setTexts () {
        switch (paintingNumber) {
            case 1:
                titleText.setText(R.string.title_painting_1);
                authorText.setText(R.string.location_painting_1);
                songText.setText(R.string.song_painting_1);
                paintingView.setBackgroundResource(R.drawable.opera_11);
                break;
            case 2:
                titleText.setText(R.string.title_painting_2);
                authorText.setText(R.string.location_painting_2);
                songText.setText(R.string.song_painting_2);
                paintingView.setBackgroundResource(R.drawable.opera_12);
                break;
            case 3:
                titleText.setText(R.string.title_painting_3);
                authorText.setText(R.string.location_painting_3);
                songText.setText(R.string.song_painting_3);
                paintingView.setBackgroundResource(R.drawable.opera_13);
                break;
            case 4:
                titleText.setText(R.string.title_painting_4);
                authorText.setText(R.string.location_painting_4);
                songText.setText(R.string.song_painting_4);
                paintingView.setBackgroundResource(R.drawable.opera_14);
                break;
            case 5:
                titleText.setText(R.string.title_painting_5);
                authorText.setText(R.string.location_painting_5);
                songText.setText(R.string.song_painting_5);
                paintingView.setBackgroundResource(R.drawable.opera_15);
                break;
            case 6:
                titleText.setText(R.string.title_painting_6);
                authorText.setText(R.string.location_painting_6);
                songText.setText(R.string.song_painting_6);
                paintingView.setBackgroundResource(R.drawable.opera_16);
                break;
            case 7:
                titleText.setText(R.string.title_painting_7);
                authorText.setText(R.string.location_painting_7);
                songText.setText(R.string.song_painting_7);
                paintingView.setBackgroundResource(R.drawable.opera_17);
                break;
            case 8:
                titleText.setText(R.string.title_painting_8);
                authorText.setText(R.string.location_painting_8);
                songText.setText(R.string.song_painting_8);
                paintingView.setBackgroundResource(R.drawable.opera_18);
                break;
        }
    }
}
