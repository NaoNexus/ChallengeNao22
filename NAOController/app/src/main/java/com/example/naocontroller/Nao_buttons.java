package com.example.naocontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class Nao_buttons extends AppCompatActivity {

    CardView btn_opera_1,
            btn_opera_2,
            btn_opera_3,
            btn_opera_4,
            btn_opera_5,
            btn_opera_6;

    Button btn_settings;

    TextInputEditText ip_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ACTION BAR CUSTOMISATION\\
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //ACTION BAR CUSTOMISATION\\

        setContentView(R.layout.activity_nao_buttons);


        btn_opera_1 = findViewById(R.id.btn_1);
        btn_opera_2 = findViewById(R.id.btn_2);
        btn_opera_3 = findViewById(R.id.btn_3);
        btn_opera_4 = findViewById(R.id.btn_4);
        btn_opera_5 = findViewById(R.id.btn_5);
        btn_opera_6 = findViewById(R.id.btn_6);
        btn_settings = findViewById(R.id.btn_settings);



        btn_settings.setOnClickListener(view -> {
            Intent intent = new Intent(Nao_buttons.this, Settings_activity.class);
            startActivity(intent);
            finish();
        });

        btn_opera_1.setOnClickListener(v ->
                data_sender("app_1_nao", Objects.requireNonNull(ip_text.getText()).toString())
        );
        btn_opera_2.setOnClickListener(v ->
                data_sender("app_2_nao", Objects.requireNonNull(ip_text.getText()).toString())
        );
        btn_opera_3.setOnClickListener(v ->
                data_sender("app_3_nao", Objects.requireNonNull(ip_text.getText()).toString())
        );
        btn_opera_4.setOnClickListener(v ->
                data_sender("app_4_nao", Objects.requireNonNull(ip_text.getText()).toString())
        );
        btn_opera_5.setOnClickListener(v ->
                data_sender("app_5_nao", Objects.requireNonNull(ip_text.getText()).toString())
        );
        btn_opera_6.setOnClickListener(v ->
                data_sender("app_6_nao", Objects.requireNonNull(ip_text.getText()).toString())
        );
    }


    private void data_sender(String message, String ip) {

        Message_sender bg_void = new Message_sender();
        bg_void.execute(message, ip);
    }
}