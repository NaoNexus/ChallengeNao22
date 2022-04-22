package com.example.naocontroller;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.naocontroller.ar.helpers.CameraPermissionHelper;
import com.example.naocontroller.ar.helpers.SnackbarHelper;
import com.example.naocontroller.socket.MessageSender;
import com.google.android.material.textfield.TextInputEditText;

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


    AlertDialog dialogSettings, dialogStats;
    AlertDialog.Builder dialogBuilderSettings, dialogBuilderStats;

    String ip = "";
    String port = "";

    private final SnackbarHelper messageSnackbarHelper = new SnackbarHelper();

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
                if (ip.equals("") || port.equals("")) {
                    messageSnackbarHelper.showMessage(this, "Imposta l'ip e la porta per riuscire a comunicare con il NAO");
                    return;
                }

                Intent intent = new Intent(NaoButtons.this, ArNaoDescription.class);
                intent.putExtra("recognisePainting", true);
                intent.putExtra("port", port);
                startActivity(intent);
            } else
                CameraPermissionHelper.requestCameraPermission(this);
        });

        painting1Button.setOnClickListener(v ->
                dataSender("1", this.ip, this.port)
        );
        painting2Button.setOnClickListener(v ->
                dataSender("2", this.ip, this.port)
        );
        painting3Button.setOnClickListener(v ->
                dataSender("3", this.ip, this.port)
        );
        painting4button.setOnClickListener(v ->
                dataSender("4", this.ip, this.port)
        );
        painting5Button.setOnClickListener(v ->
                dataSender("5", this.ip, this.port)
        );
        painting6Button.setOnClickListener(v ->
                dataSender("6", this.ip, this.port)
        );
        painting7Button.setOnClickListener(v ->
                dataSender("7", this.ip, this.port)
        );
        painting8Button.setOnClickListener(v ->
                dataSender("8", this.ip, this.port)
        );
    }


    private void dataSender(String paintingIndex, String ip, String port) {
        if (ip.equals("") || port.equals("")) {
            messageSnackbarHelper.showMessage(this, "Imposta l'ip e la porta per riuscire a comunicare con il NAO");
            return;
        }

        MessageSender sender = new MessageSender();
        sender.execute("app_" + paintingIndex + "_nao", ip, port);

        StatsManager.increaseNormalPaintings();

        if (!paintingIndex.equals("error")) {

            Intent intent = new Intent(NaoButtons.this, NaoDescription.class);
            intent.putExtra("painting", Integer.parseInt(paintingIndex));
            intent.putExtra("port", port);
            intent.putExtra("ip", ip);
            startActivity(intent);
        }
    }


    private void createSettingsDialog() {
        dialogBuilderSettings = new AlertDialog.Builder(this);

        final View settingsPopupView = getLayoutInflater().inflate(R.layout.popup_settings, null);


        TextInputEditText ipEditText = settingsPopupView.findViewById(R.id.ip_edit_text);
        TextInputEditText portEditText = settingsPopupView.findViewById(R.id.port_edit_text);
        Button okButton = settingsPopupView.findViewById(R.id.btn_ok);
        Button cancelButton = settingsPopupView.findViewById(R.id.btn_annulla);
        Button followButton = settingsPopupView.findViewById(R.id.btn_follow_mode);
        ImageButton statsButton = settingsPopupView.findViewById(R.id.btn_stats);

        ipEditText.setText(this.ip);
        portEditText.setText(this.port);

        dialogBuilderSettings.setView(settingsPopupView);
        dialogSettings = dialogBuilderSettings.create();
        dialogSettings.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogSettings.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogSettings.show();

        followButton.setOnClickListener(view -> {
            if (ip.equals("") || port.equals("")) {
                messageSnackbarHelper.showMessage(this, "Imposta l'ip e la porta per riuscire a comunicare con il NAO");
                return;
            }
            MessageSender sender = new MessageSender();
            sender.execute("app_follow_nao", ip, port);
        });

        okButton.setOnClickListener(view -> {
            this.ip = Objects.requireNonNull(ipEditText.getText()).toString();
            this.port = Objects.requireNonNull(portEditText.getText()).toString();
            dialogSettings.dismiss();
        });

        cancelButton.setOnClickListener(view -> dialogSettings.dismiss());


        followButton.setOnClickListener(view -> dataSender("error", ip, port));


        statsButton.setOnClickListener(view -> {
            dialogBuilderStats = new AlertDialog.Builder(this);
            final View statsPopupView = getLayoutInflater().inflate(R.layout.popup_stats, null);

            Button backButton = statsPopupView.findViewById(R.id.btn_back_to_settings);
            TextView arPaintingsDescribed = statsPopupView.findViewById(R.id.text_NumArDescripted);
            TextView PaintingsDescribed = statsPopupView.findViewById(R.id.text_NumNormalDescripted);
            TextView numPaintingsRecognised = statsPopupView.findViewById(R.id.text_NumPaintingsRecognised);

            dialogBuilderStats.setView(statsPopupView);
            dialogStats = dialogBuilderStats.create();
            dialogStats.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialogStats.getWindow().setBackgroundDrawableResource(R.color.transparent);
            dialogStats.show();

            backButton.setOnClickListener(view1 -> dialogStats.dismiss());

            arPaintingsDescribed.setText(String.valueOf(StatsManager.getNARPaintings()));
            PaintingsDescribed.setText(String.valueOf(StatsManager.getNNormalPaintings()));
            numPaintingsRecognised.setText(String.valueOf(StatsManager.getNPaintingsRecognised()));

        });
    }
}