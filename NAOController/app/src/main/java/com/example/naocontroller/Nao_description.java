package com.example.naocontroller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class Nao_description extends Activity {

    TextView authorText,
             descriptionText,
             titleText,
             songText;

    int paintingNumber;

    public Nao_description (int paintingNumber) {
        this.paintingNumber = paintingNumber;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nao_description);

        titleText = findViewById(R.id.txt_painting_title);
        authorText = findViewById(R.id.txt_painting_author);
        songText = findViewById(R.id.txt_painting_song);
        descriptionText = findViewById(R.id.txt_painting_description);

        setTexts();

        //TODO: receive the command that nao returned to base and display buttons again.
    }

    private void setTexts () {
        switch (paintingNumber) {
            case 1:
                titleText.setText(R.string.title_painting_1);
                authorText.setText(R.string.author_painting_1);
                songText.setText(R.string.song_painting_1);
                break;
            case 2:
                titleText.setText(R.string.title_painting_2);
                authorText.setText(R.string.author_painting_2);
                songText.setText(R.string.song_painting_2);
                break;
            case 3:
                titleText.setText(R.string.title_painting_3);
                authorText.setText(R.string.author_painting_3);
                songText.setText(R.string.song_painting_3);
                break;
            case 4:
                titleText.setText(R.string.title_painting_4);
                authorText.setText(R.string.author_painting_4);
                songText.setText(R.string.song_painting_4);
                break;
            case 5:
                titleText.setText(R.string.title_painting_5);
                authorText.setText(R.string.author_painting_5);
                songText.setText(R.string.song_painting_5);
                break;
            case 6:
                titleText.setText(R.string.title_painting_6);
                authorText.setText(R.string.author_painting_6);
                songText.setText(R.string.song_painting_6);
                break;
            case 7:
                titleText.setText(R.string.title_painting_7);
                authorText.setText(R.string.author_painting_7);
                songText.setText(R.string.song_painting_7);
                break;
            case 8:
                titleText.setText(R.string.title_painting_8);
                authorText.setText(R.string.author_painting_8);
                songText.setText(R.string.song_painting_8);
                break;
        }
    }
}
