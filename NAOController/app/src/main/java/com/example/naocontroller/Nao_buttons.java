package com.example.naocontroller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;


public class Nao_buttons extends AppCompatActivity {

    Button btn_opera_1,
            btn_opera_2,
            btn_opera_3,
            btn_opera_4,
            btn_opera_5,
            btn_opera_6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nao_buttons);


        btn_opera_1 = findViewById(R.id.btn_1);
        btn_opera_2 = findViewById(R.id.btn_2);
        btn_opera_3 = findViewById(R.id.btn_3);
        btn_opera_4 = findViewById(R.id.btn_4);
        btn_opera_5 = findViewById(R.id.btn_5);
        btn_opera_6 = findViewById(R.id.btn_6);


        btn_opera_1.setOnClickListener(v ->
                data_sender("1")
        );
        btn_opera_2.setOnClickListener(v ->
                data_sender("2")
        );
        btn_opera_3.setOnClickListener(v ->
                data_sender("3")
        );
        btn_opera_4.setOnClickListener(v ->
                data_sender("4")
        );
        btn_opera_5.setOnClickListener(v ->
                data_sender("5")
        );
        btn_opera_6.setOnClickListener(v ->
                data_sender("6")
        );
    }


    private void data_sender(String message) {

        Message_sender bg_void = new Message_sender();
        bg_void.execute(message);
    }
}