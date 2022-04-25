package com.example.naocontroller;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.naocontroller.ar.helpers.CameraPermissionHelper;
import com.example.naocontroller.ar.helpers.DisplayRotationHelper;
import com.example.naocontroller.ar.helpers.FullScreenHelper;
import com.example.naocontroller.ar.helpers.SnackbarHelper;

import com.example.naocontroller.ar.helpers.TrackingStateHelper;
import com.example.naocontroller.ar.rendering.AugmentedImageRenderer;
import com.example.naocontroller.ar.rendering.BackgroundRenderer;

import com.example.naocontroller.socket.MessageReceiver;
import com.example.naocontroller.socket.MessageSender;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;

import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class ArNaoDescription extends AppCompatActivity implements GLSurfaceView.Renderer {
    private static final String TAG = ArNaoDescription.class.getSimpleName();

    // SOCKET

    private String ip, port;
    
    // GRAPHICS AND LOGIC

    private boolean recognisePainting;
    private int paintingIndex;

    private CardView paintingRecognisedCard, paintingActionsCard, speakButton;
    private TextView paintingRecognisedTitle, paintingRecognisedLocation;

    private Button followButton, waitButton;

    private ObjectAnimator cardSlideUpAnimation,
            cardSlideDownAnimation,
            cardPaintingActionsSlideOutAnimation;

    // AR

    private GLSurfaceView surfaceView;
    private Session session;

    private AugmentedImageDatabase imageDatabase;

    private final SnackbarHelper messageSnackbarHelper = new SnackbarHelper();
    private final TrackingStateHelper trackingStateHelper = new TrackingStateHelper(this);
    private DisplayRotationHelper displayRotationHelper;

    private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
    private final AugmentedImageRenderer augmentedImageRenderer = new AugmentedImageRenderer();

    private boolean installRequested;
    private boolean shouldConfigureSession = false;

    private final Map<Integer, Pair<AugmentedImage, Anchor>> augmentedImageMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_description);
        Bundle b = getIntent().getExtras();
        recognisePainting = b.getBoolean("recognisePainting");
        port = b.getString("port");
        ip = b.getString("ip");

        if (!recognisePainting) {
            paintingIndex = b.getInt("painting");
        }
        setup();
    }

    private void setup() {
        setupGraphics();

        displayRotationHelper = new DisplayRotationHelper(/*context=*/ this);

        // Set up renderer.
        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        surfaceView.setRenderer(this);
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        surfaceView.setWillNotDraw(false);
    }

    public void setupGraphics () {
        //ACTION BAR CUSTOMISATION\\
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //ACTION BAR CUSTOMISATION\\

        surfaceView = findViewById(R.id.surface_view);
        speakButton = findViewById(R.id.btn_speak);
        paintingRecognisedCard = findViewById(R.id.painting_recognised_card);
        paintingActionsCard = findViewById(R.id.painting_actions_card);

        waitButton = findViewById(R.id.btn_wait);
        followButton = findViewById(R.id.btn_follow);

        speakButton.setOnClickListener(v -> {
            if (paintingRecognisedCard.getTranslationY() == 0 && recognisePainting) {
                paintingIndex = Utilities.getPaintingIndexFromTitle(paintingRecognisedTitle.getText().toString());
                recognisePainting = false;
                StatsManager.increaseARPaintings();
                cardSlideDownAnimation.start();
                dataSender(String.valueOf(paintingIndex), ip, port);
                Intent intent = getIntent();
                intent.removeExtra("recognisePainting");
                intent.putExtra("recognisePainting", false);
                intent.putExtra("painting", paintingIndex);
                new Handler().postDelayed(() -> {
                    finish();
                    startActivity(intent);
                }, 200); //Wait for animation to finish
            }
        });

        if (recognisePainting) {
            followButton.setVisibility(View.GONE);
            waitButton.setVisibility(View.GONE);
        } else {
            followButton.setVisibility(View.VISIBLE);
            waitButton.setVisibility(View.VISIBLE);

            followButton.setOnClickListener(view -> new MessageSender().execute("app_error_nao", ip, port));

            waitButton.setOnClickListener(view -> {
                cardPaintingActionsSlideOutAnimation.start();
                messageReceiver();
            });
        }

        cardSlideUpAnimation = ObjectAnimator.ofFloat(paintingRecognisedCard, "translationY", 0f);
        cardSlideDownAnimation = ObjectAnimator.ofFloat(paintingRecognisedCard, "translationY", Utilities.getDP(this, 140));
        cardPaintingActionsSlideOutAnimation = ObjectAnimator.ofFloat(paintingActionsCard, "translationX", Utilities.getDP(this, 320));

        paintingRecognisedLocation = findViewById(R.id.painting_recognised_location_text);
        paintingRecognisedTitle = findViewById(R.id.painting_recognised_title_text);

        cardSlideDownAnimation.setDuration(200);
        cardSlideUpAnimation.setDuration(200);
        cardPaintingActionsSlideOutAnimation.setDuration(200);

        cardSlideDownAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        cardSlideUpAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        cardPaintingActionsSlideOutAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    protected void onDestroy() {
        if (session != null) {
            session.close();
            session = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (session == null) {
            Exception exception = null;
            String message = null;
            try {
                switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
                    case INSTALL_REQUESTED:
                        installRequested = true;
                        return;
                    case INSTALLED:
                        break;
                }

                // ARCore requires camera permissions to operate. If we did not yet obtain runtime
                // permission on Android M and above, now is a good time to ask the user for it.
                if (!CameraPermissionHelper.hasCameraPermission(this)) {
                    CameraPermissionHelper.requestCameraPermission(this);
                    return;
                }

                session = new Session(/* context = */ this);
            } catch (UnavailableArcoreNotInstalledException
                    | UnavailableUserDeclinedInstallationException e) {
                message = "Please install ARCore";
                exception = e;
            } catch (UnavailableApkTooOldException e) {
                message = "Please update ARCore";
                exception = e;
            } catch (UnavailableSdkTooOldException e) {
                message = "Please update this app";
                exception = e;
            } catch (Exception e) {
                message = "This device does not support AR";
                exception = e;
            }

            if (message != null) {
                messageSnackbarHelper.showError(this, message);
                Log.e(TAG, "Exception creating session", exception);
                return;
            }

            shouldConfigureSession = true;
        }

        if (shouldConfigureSession) {
            configureSession();
            shouldConfigureSession = false;
        }

        // Note that order matters - see the note in onPause(), the reverse applies here.
        try {
            session.resume();
        } catch (CameraNotAvailableException e) {
            messageSnackbarHelper.showError(this, "Camera not available. Try restarting the app.");
            session = null;
            return;
        }

        displayRotationHelper.onResume();
        surfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (session != null) {
            displayRotationHelper.onPause();
            surfaceView.onPause();
            session.pause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(
                    this, "Camera permissions are needed to run this application", Toast.LENGTH_LONG)
                    .show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        displayRotationHelper.onSurfaceChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
        try {
            // Create the texture and pass it to ARCore session to be filled during update().
            backgroundRenderer.createOnGlThread(/*context=*/ this);
            augmentedImageRenderer.createOnGlThread(/*context=*/ this);

            if (!recognisePainting) {
                Log.i(TAG, "onSurfaceCreated: creation");
                augmentedImageRenderer.setupPaintingDetails(this, paintingIndex);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to read an asset file", e);
        }
    }

    void configureSession () {
        try {
            session = new Session(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!loadImageDatabase()) {
            Log.e(TAG, "Failed to load database");
            finish();
        }

        Config config =  new Config(session);
        // Setup config in here
        config.setFocusMode(Config.FocusMode.AUTO);
        config.setAugmentedImageDatabase(imageDatabase);

        session.configure(config);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear screen to notify driver it should not load any pixels from previous frame.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (session == null) {
            return;
        }

        displayRotationHelper.updateSessionIfNeeded(session);

        try {
            session.setCameraTextureName(backgroundRenderer.getTextureId());

            // Obtain the current frame from ARSession. When the configuration is set to
            // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
            // camera framerate.
            Frame frame = session.update();
            Camera camera = frame.getCamera();

            trackingStateHelper.updateKeepScreenOnFlag(camera.getTrackingState());

            // If frame is ready, render camera preview image to the GL surface.
            backgroundRenderer.draw(frame);

            // Get projection matrix.
            float[] projmtx = new float[16];
            camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f);

            // Get camera matrix and draw.
            float[] viewmtx = new float[16];
            camera.getViewMatrix(viewmtx, 0);

            // Compute lighting from average intensity of the image.
            //final float[] colorCorrectionRgba = new float[4];
            //frame.getLightEstimate().getColorCorrection(colorCorrectionRgba, 0);

            // Visualize augmented images.
            drawAugmentedImages(frame, projmtx, viewmtx);
        } catch (Throwable t) {
            // Avoid crashing the application due to unhandled exceptions.
            Log.e(TAG, "Exception on the OpenGL thread", t);
        }
    }

    private void drawAugmentedImages(
            Frame frame, float[] projmtx, float[] viewmtx) {
        Collection<AugmentedImage> updatedAugmentedImages =
                frame.getUpdatedTrackables(AugmentedImage.class);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Iterate to update augmentedImageMap, remove elements we cannot draw.
        for (AugmentedImage augmentedImage : updatedAugmentedImages) {

            float[] objmtx = new float[16];
            augmentedImage.getCenterPose().toMatrix(objmtx, 0);

            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    break;

                case TRACKING:
                    // Create a new anchor for newly found images.
                    if (!augmentedImageMap.containsKey(augmentedImage.getIndex())) {
                        Anchor centerPoseAnchor = augmentedImage.createAnchor(augmentedImage.getCenterPose());
                        augmentedImageMap.put(
                                augmentedImage.getIndex(), Pair.create(augmentedImage, centerPoseAnchor));
                    }

                    if (recognisePainting) {
                        if (Utilities.isInScreen(objmtx, viewmtx, projmtx, displayMetrics)) {
                            if (paintingRecognisedCard.getTranslationY() == Utilities.getDP(this, 140)) {
                                Log.i(TAG, "Image tracked: " + augmentedImage.getIndex());
                                StatsManager.increasePaintingsRecognised();
                                this.runOnUiThread(() -> {
                                    cardSlideUpAnimation.start();
                                    Utilities.setTexts(augmentedImage.getIndex() + 1,
                                            paintingRecognisedTitle,
                                            paintingRecognisedLocation);
                                });
                            }
                        } else if (paintingRecognisedCard.getTranslationY() == 0) {
                            this.runOnUiThread(() -> cardSlideDownAnimation.start());
                        }
                    }
                    break;

                case STOPPED:
                    if (paintingRecognisedCard.getTranslationY() == 0)
                        this.runOnUiThread(() -> cardSlideDownAnimation.start());
                    augmentedImageMap.remove(augmentedImage.getIndex());
                    break;

                default:
                    break;
            }
        }

        // Draw all images in augmentedImageMap
        for (Pair<AugmentedImage, Anchor> pair : augmentedImageMap.values()) {
            AugmentedImage augmentedImage = pair.first;
            Anchor centerAnchor = Objects.requireNonNull(augmentedImageMap.get(augmentedImage.getIndex())).second;
            if (augmentedImage.getTrackingState() == TrackingState.TRACKING) {
                Log.e(TAG, "paintingIndex: " + paintingIndex);
                if (!recognisePainting) {
                    if (augmentedImage.getIndex() == paintingIndex - 1)
                        augmentedImageRenderer.drawPaintingDetails(paintingIndex, viewmtx, projmtx, augmentedImage, centerAnchor);
                } else {
                    augmentedImageRenderer.drawBorder(viewmtx, projmtx, augmentedImage, centerAnchor);
                }
            }
        }
    }

    private boolean loadImageDatabase() {
        try {
            InputStream stream = this.getAssets().open("paintings.imgdb");
            imageDatabase = AugmentedImageDatabase.deserialize(session, stream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void messageReceiver() {
        MessageReceiver messageReceiver = new MessageReceiver(messageReceived -> finish());
        messageReceiver.execute(String.valueOf(port));
    }

    private void dataSender(String paintingIndex, String ip, String port) {
        MessageSender sender = new MessageSender();
        sender.execute("app_" + paintingIndex + "_nao", ip, port);
    }
}
