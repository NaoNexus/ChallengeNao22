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

import com.example.naocontroller.ar.helpers.CameraPermissionHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;
import java.util.Objects;


public class NaoButtons extends AppCompatActivity {
    CardView painting1Button,
             painting2Button,
             painting3Button,
             painting4button,
             painting5Button,
             painting6Button,
             painting7Button,
             painting8Button,
             settingsButton,
             cameraRecognitionButton;


    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;
    String ip = "";
    String port = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ACTION BAR CUSTOMISATION\\
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //ACTION BAR CUSTOMISATION\\

        setContentView(R.layout.activity_nao_buttons);


        painting1Button = findViewById(R.id.btn_1);
        painting2Button = findViewById(R.id.btn_2);
        painting3Button = findViewById(R.id.btn_3);
        painting4button = findViewById(R.id.btn_4);
        painting5Button = findViewById(R.id.btn_5);
        painting6Button = findViewById(R.id.btn_6);
        painting7Button = findViewById(R.id.btn_7);
        painting8Button = findViewById(R.id.btn_8);
        settingsButton = findViewById(R.id.btn_settings);
        cameraRecognitionButton = findViewById(R.id.btn_camera_recognition);


        settingsButton.setOnClickListener(view ->
                createSettingsDialog()
        );

        cameraRecognitionButton.setOnClickListener(view -> {
            if (CameraPermissionHelper.hasCameraPermission(this)) {
                Intent intent = new Intent(NaoButtons.this, ArNaoDescription.class);
                intent.putExtra("recognisePainting", true);
                startActivity(intent);
            } else
                CameraPermissionHelper.requestCameraPermission(this);
        });

        painting1Button.setOnClickListener(v ->
                dataSender(1, this.ip, this.port)
        );
        painting2Button.setOnClickListener(v ->
                dataSender(2, this.ip, this.port)
        );
        painting3Button.setOnClickListener(v ->
                dataSender(3, this.ip, this.port)
        );
        painting4button.setOnClickListener(v ->
                dataSender(4, this.ip, this.port)
        );
        painting5Button.setOnClickListener(v ->
                dataSender(5, this.ip, this.port)
        );
        painting6Button.setOnClickListener(v ->
                dataSender(6, this.ip, this.port)
        );
        painting7Button.setOnClickListener(v ->
                dataSender(7, this.ip, this.port)
        );
        painting8Button.setOnClickListener(v ->
                dataSender(8, this.ip, this.port)
        );
    }


    private void dataSender(int paintingIndex, String ip, String port) {
        //IF ELSE IP ALREADY SET
        MessageSender sender = new MessageSender();
        sender.execute(String.format(Locale.ITALIAN, "app_%d_nao", paintingIndex), ip, port);

        if (CameraPermissionHelper.hasCameraPermission(this)) {
            Intent intent = new Intent(NaoButtons.this, ArNaoDescription.class);
            intent.putExtra("recognisePainting", false);
            intent.putExtra("painting", paintingIndex);
            startActivity(intent);
        } else {
            CameraPermissionHelper.requestCameraPermission(this);
            Intent intent = new Intent(NaoButtons.this, NaoDescription.class);
            intent.putExtra("painting", paintingIndex);
            startActivity(intent);
        }
    }


    private void createSettingsDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_settings_menu, null);

        TextInputEditText ipEditText = contactPopupView.findViewById(R.id.ip_edit_text);
        TextInputEditText portEditText = contactPopupView.findViewById(R.id.port_edit_text);
        Button okButton = contactPopupView.findViewById(R.id.btn_ok);
        Button cancelButton = contactPopupView.findViewById(R.id.btn_annulla);
        Button followButton = contactPopupView.findViewById(R.id.btn_follow_mode);

        ipEditText.setText(this.ip);
        portEditText.setText(this.port);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();

        okButton.setOnClickListener(view -> {
            this.ip = Objects.requireNonNull(ipEditText.getText()).toString();
            this.port = Objects.requireNonNull(portEditText.getText()).toString();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        followButton.setOnClickListener(view -> dataSender(0, ip, port));
    }
}