package com.example.naocontroller;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;
import java.util.Objects;


public class Nao_buttons extends AppCompatActivity {
    CardView btn_opera_1,
             btn_opera_2,
             btn_opera_3,
             btn_opera_4,
             btn_opera_5,
             btn_opera_6,
             btn_opera_7,
             btn_opera_8,
             btn_settings;


    AlertDialog dialog;
    AlertDialog.Builder dialog_Builder;
    String ip, port = "";


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
        btn_opera_7 = findViewById(R.id.btn_7);
        btn_opera_8 = findViewById(R.id.btn_8);
        btn_settings = findViewById(R.id.btn_settings);


        btn_settings.setOnClickListener(view ->
                createNewContactDialog()
        );

        btn_opera_1.setOnClickListener(v ->
                data_sender(1, this.ip, this.port)
        );
        btn_opera_2.setOnClickListener(v ->
                data_sender(2, this.ip, this.port)
        );
        btn_opera_3.setOnClickListener(v ->
                data_sender(3, this.ip, this.port)
        );
        btn_opera_4.setOnClickListener(v ->
                data_sender(4, this.ip, this.port)
        );
        btn_opera_5.setOnClickListener(v ->
                data_sender(5, this.ip, this.port)
        );
        btn_opera_6.setOnClickListener(v ->
                data_sender(6, this.ip, this.port)
        );
        btn_opera_7.setOnClickListener(v ->
                data_sender(7, this.ip, this.port)
        );
        btn_opera_8.setOnClickListener(v ->
                data_sender(8, this.ip, this.port)
        );
    }


    private void data_sender(int paintingIndex, String ip, String port) {
        Message_sender bg_void = new Message_sender();
        bg_void.execute(String.format(Locale.ITALIAN, "app_%d_nao", paintingIndex), ip, port);

        Intent intent = new Intent(Nao_buttons.this, Nao_description.class);
        intent.putExtra("painting", paintingIndex);
        startActivity(intent);
    }


    private void createNewContactDialog() {
        dialog_Builder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_settings_menu, null);

        TextInputEditText ip_edit_text = contactPopupView.findViewById(R.id.ip_edit_text);
        TextInputEditText port_edit_text = contactPopupView.findViewById(R.id.port_edit_text);
        Button btn_ok = contactPopupView.findViewById(R.id.btn_ok);
        Button btn_annulla = contactPopupView.findViewById(R.id.btn_annulla);

        ip_edit_text.setText(this.ip);
        port_edit_text.setText(this.port);

        dialog_Builder.setView(contactPopupView);
        dialog = dialog_Builder.create();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();

        btn_ok.setOnClickListener(view -> {
            this.ip = Objects.requireNonNull(ip_edit_text.getText()).toString();
            this.port = Objects.requireNonNull(port_edit_text.getText()).toString();
            dialog.dismiss();
        });

        btn_annulla.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }
}