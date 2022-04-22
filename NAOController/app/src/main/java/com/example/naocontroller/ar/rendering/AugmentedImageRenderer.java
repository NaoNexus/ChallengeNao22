package com.example.naocontroller.ar.rendering;

import android.content.Context;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;

import java.io.IOException;


/** Renders an augmented image. */
public class AugmentedImageRenderer {
  private static final String TAG = AugmentedImageRenderer.class.getSimpleName();

  private static final float TINT_INTENSITY = 0.1f;
  private static final float TINT_ALPHA = 1.0f;
  private static final int[] TINT_COLORS_HEX = {
    0x000000, 0xF44336, 0xE91E63, 0x9C27B0, 0x673AB7, 0x3F51B5, 0x2196F3, 0x03A9F4, 0x00BCD4,
    0x009688, 0x4CAF50, 0x8BC34A, 0xCDDC39, 0xFFEB3B, 0xFFC107, 0xFF9800,
  };

  private final QuadRenderer imageBorderUpperLeft =  new QuadRenderer(0.02f, 0.02f, 0);
  private final QuadRenderer imageBorderUpperRight = new QuadRenderer(0.02f, 0.02f, 1);
  private final QuadRenderer imageBorderLowerLeft =  new QuadRenderer(0.02f, 0.02f, 2);
  private final QuadRenderer imageBorderLowerRight = new QuadRenderer(0.02f, 0.02f, 3);

  private QuadRenderer imageLocationMap;
  private QuadRenderer imageDetailsText;

  public AugmentedImageRenderer() {}

  public void createOnGlThread(Context context) throws IOException {
    setupBorder(context);
  }

  public void setupPaintingDetails (Context context, int paintingIndex) throws IOException {
    switch (paintingIndex) {
      case 1:
        imageLocationMap = new QuadRenderer(0.08f, 0.08f, 0);
        imageDetailsText = new QuadRenderer(0.32f, 0.24f, 0);

        imageLocationMap.createOnGlThread(context, "images/painting_11_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_11_description.png");
        break;
      case 2:
        imageLocationMap = new QuadRenderer(0.08f, 0.08f, 0);
        imageDetailsText = new QuadRenderer(0.32f, 0.24f, 0);

        imageLocationMap.createOnGlThread(context, "images/painting_12_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_12_description.png");
        break;
      case 3:
        imageLocationMap = new QuadRenderer(0.08f, 0.08f, 0);
        imageDetailsText = new QuadRenderer(0.32f, 0.24f, 0);

        imageLocationMap.createOnGlThread(context, "images/painting_13_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_13_description.png");
        break;
      case 4:
        imageLocationMap = new QuadRenderer(0.1f, 0.1f, 0);
        imageDetailsText = new QuadRenderer(0.32f, 0.24f, 0);

        imageLocationMap.createOnGlThread(context, "images/painting_14_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_14_description.png");
        break;
      case 5:
        imageLocationMap = new QuadRenderer(0.1f, 0.1f, 0);
        imageDetailsText = new QuadRenderer(0.32f, 0.24f, 0);

        imageLocationMap.createOnGlThread(context, "images/painting_15_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_15_description.png");
        break;
      case 6:
        imageLocationMap = new QuadRenderer(0.1f, 0.1f, 0);
        imageDetailsText = new QuadRenderer(0.32f, 0.24f, 0);

        imageLocationMap.createOnGlThread(context, "images/painting_16_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_16_description.png");
        break;
      case 7:
        imageLocationMap = new QuadRenderer(0.1f, 0.1f, 0);
        imageDetailsText = new QuadRenderer(0.32f, 0.24f, 0);

        imageLocationMap.createOnGlThread(context, "images/painting_17_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_17_description.png");
        break;
      case 8:
        imageLocationMap = new QuadRenderer(0.1f, 0.1f, 0);
        imageDetailsText = new QuadRenderer(0.32f, 0.24f, 0);

        imageLocationMap.createOnGlThread(context, "images/painting_18_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_18_description.png");
        break;
    }
  }

  public void drawPaintingDetails(
      int paintingIndex,
      float[] viewMatrix,
      float[] projectionMatrix,
      AugmentedImage augmentedImage,
      Anchor centerAnchor) {

    Pose[] localBoundaryPoses;

    if (paintingIndex == 1) {
      localBoundaryPoses = new Pose[]{
              Pose.makeTranslation(
                      -1.1f * augmentedImage.getExtentX(),
                      0.0f,
                      0.0f * augmentedImage.getExtentZ()),
              Pose.makeTranslation(
                      0.8f * augmentedImage.getExtentX(),
                      0.0f,
                      0.0f * augmentedImage.getExtentZ()),
      };
    } else {
      localBoundaryPoses = new Pose[]{
              Pose.makeTranslation(
                      -1.1f * augmentedImage.getExtentX(),
                      0.0f,
                      0.0f * augmentedImage.getExtentZ()),
              Pose.makeTranslation(
                      0.0f * augmentedImage.getExtentX(),
                      0.0f,
                      0.0f * augmentedImage.getExtentZ()),
      };
    }

    Pose anchorPose = centerAnchor.getPose();
    Pose[] worldBoundaryPoses = new Pose[4];
    for (int i = 0; i < 2; ++i) {
      worldBoundaryPoses[i] = anchorPose.compose(localBoundaryPoses[i]);
    }

    float scaleFactor = 1.0f;
    float[] modelMatrix = new float[16];

    worldBoundaryPoses[0].toMatrix(modelMatrix, 0);
    imageLocationMap.updateModelMatrix(modelMatrix, scaleFactor);
    imageLocationMap.draw(viewMatrix, projectionMatrix);

    worldBoundaryPoses[1].toMatrix(modelMatrix, 0);
    imageDetailsText.updateModelMatrix(modelMatrix, scaleFactor);
    imageDetailsText.draw(viewMatrix, projectionMatrix);
  }

  void setupBorder (Context context) {
    imageBorderUpperLeft.createOnGlThread(context, "images/border_corner.png");
    imageBorderUpperRight.createOnGlThread(context, "images/border_corner.png");
    imageBorderLowerLeft.createOnGlThread(context, "images/border_corner.png");
    imageBorderLowerRight.createOnGlThread(context, "images/border_corner.png");
  }

  public void drawBorder (float[] viewMatrix,
                   float[] projectionMatrix,
                   AugmentedImage augmentedImage,
                   Anchor centerAnchor) {
    Pose[] localBoundaryPoses = {
            Pose.makeTranslation(
                    -0.42f * augmentedImage.getExtentX(),
                    0.0f,
                    -0.42f * augmentedImage.getExtentZ()), // upper left
            Pose.makeTranslation(
                    0.42f * augmentedImage.getExtentX(),
                    0.0f,
                    -0.42f * augmentedImage.getExtentZ()), // upper right
            Pose.makeTranslation(
                    0.42f * augmentedImage.getExtentX(),
                    0.0f,
                    0.42f * augmentedImage.getExtentZ()), // lower right
            Pose.makeTranslation(
                    -0.42f * augmentedImage.getExtentX(),
                    0.0f,
                    0.42f * augmentedImage.getExtentZ()),// lower left
    };

    Pose anchorPose = centerAnchor.getPose();
    Pose[] worldBoundaryPoses = new Pose[4];
    for (int i = 0; i < 4; ++i) {
      worldBoundaryPoses[i] = anchorPose.compose(localBoundaryPoses[i]);
    }

    float scaleFactor = 1.0f;
    float[] modelMatrix = new float[16];

    worldBoundaryPoses[0].toMatrix(modelMatrix, 0);
    imageBorderUpperLeft.updateModelMatrix(modelMatrix, scaleFactor);
    imageBorderUpperLeft.draw(viewMatrix, projectionMatrix);

    worldBoundaryPoses[1].toMatrix(modelMatrix, 0);
    imageBorderUpperRight.updateModelMatrix(modelMatrix, scaleFactor);
    imageBorderUpperRight.draw(viewMatrix, projectionMatrix);

    worldBoundaryPoses[2].toMatrix(modelMatrix, 0);
    imageBorderLowerLeft.updateModelMatrix(modelMatrix, scaleFactor);
    imageBorderLowerLeft.draw(viewMatrix, projectionMatrix);

    worldBoundaryPoses[3].toMatrix(modelMatrix, 0);
    imageBorderLowerRight.updateModelMatrix(modelMatrix, scaleFactor);
    imageBorderLowerRight.draw(viewMatrix, projectionMatrix);
  }

  private static float[] convertHexToColor(int colorHex) {
    // colorHex is in 0xRRGGBB format
    float red = ((colorHex & 0xFF0000) >> 16) / 255.0f * TINT_INTENSITY;
    float green = ((colorHex & 0x00FF00) >> 8) / 255.0f * TINT_INTENSITY;
    float blue = (colorHex & 0x0000FF) / 255.0f * TINT_INTENSITY;
    return new float[] {red, green, blue, TINT_ALPHA};
  }
}
