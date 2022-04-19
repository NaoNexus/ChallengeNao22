package com.example.naocontroller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


public class NaoDescription extends AppCompatActivity {
    private final static String TAG = "NaoDescription";

    TextView authorText,
             descriptionText,
             titleText,
             songText;

    ImageView paintingView;

    int paintingIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        paintingIndex = b.getInt("painting");

        //ACTION BAR CUSTOMISATION\\
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //ACTION BAR CUSTOMISATION\\

        setContentView(R.layout.activity_nao_description);

        titleText = findViewById(R.id.txt_painting_title);
        authorText = findViewById(R.id.txt_painting_author);
        songText = findViewById(R.id.txt_painting_song);
        descriptionText = findViewById(R.id.txt_painting_description);
        paintingView = findViewById(R.id.painting_image);

        Intent intent = new Intent(NaoDescription.this, ArNaoDescription.class);
        startActivity(intent);

        Utilities.setTextsAndCardsImages(paintingIndex, titleText, authorText, songText, descriptionText, paintingView);
        messageReceiver();
    }

    private void messageReceiver() {
        MessageReceiver messageReceiver = new MessageReceiver(messageReceived -> {
            Log.e(TAG, "Message received: " + messageReceived);
            finish();
        });
        messageReceiver.execute();
    }
}
