package com.example.naocontroller;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.naocontroller.socket.MessageReceiver;

import java.util.Objects;


public class NaoDescription extends AppCompatActivity {
    private final static String TAG = NaoDescription.class.getSimpleName();

    private TextView authorText,
             descriptionText,
             titleText,
             songText;

    private ImageView paintingView;

    private int paintingIndex;

    private int port;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        paintingIndex = b.getInt("painting");
        port = b.getInt("port");

        //ACTION BAR CUSTOMISATION\\
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //ACTION BAR CUSTOMISATION\\

        setContentView(R.layout.activity_nao_description);

        setupGraphics();

        Utilities.setTextsAndCardsImages(paintingIndex, getResources(), titleText, authorText, songText, descriptionText, paintingView);
        messageReceiver();
    }

    private void setupGraphics() {
        titleText = findViewById(R.id.txt_painting_title);
        authorText = findViewById(R.id.txt_painting_author);
        songText = findViewById(R.id.txt_painting_song);
        descriptionText = findViewById(R.id.txt_painting_description);
        paintingView = findViewById(R.id.painting_image);
    }

    private void messageReceiver() {
        MessageReceiver messageReceiver = new MessageReceiver(messageReceived -> {
            Log.e(TAG, "Message received: " + messageReceived);
            finish();
        });
        messageReceiver.execute(String.valueOf(port));
    }
}
