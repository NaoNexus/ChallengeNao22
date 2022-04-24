package com.example.naocontroller.ar.rendering;

import android.content.Context;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;

import java.io.IOException;


/** Renders an augmented image. */
public class AugmentedImageRenderer {
  private static final String TAG = AugmentedImageRenderer.class.getSimpleName();

  private final QuadRenderer imageBorderUpperLeft =  new QuadRenderer(0.1f, 0.1f, 0);
  private final QuadRenderer imageBorderUpperRight = new QuadRenderer(0.1f, 0.1f, 1);
  private final QuadRenderer imageBorderLowerLeft =  new QuadRenderer(0.1f, 0.1f, 2);
  private final QuadRenderer imageBorderLowerRight = new QuadRenderer(0.1f, 0.1f, 3);

  private QuadRenderer imageLocationMap;
  private QuadRenderer imageDetailsText;

  public AugmentedImageRenderer() {}

  public void createOnGlThread(Context context) throws IOException {
    setupBorder(context);
  }

  public void setupPaintingDetails (Context context, int paintingIndex) throws IOException {
    imageLocationMap = new QuadRenderer(0.5f, 0.5f, 0);
    imageDetailsText = new QuadRenderer(1.84f, 1.38f, 0);

    switch (paintingIndex) {
      case 1:
        imageLocationMap.createOnGlThread(context, "images/painting_11_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_11_description.png");
        break;
      case 2:
        imageDetailsText = new QuadRenderer(1.6f, 1.2f, 0);

        imageLocationMap.createOnGlThread(context, "images/painting_12_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_12_description.png");
        break;
      case 3:
        imageLocationMap.createOnGlThread(context, "images/painting_13_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_13_description.png");
        break;
      case 4:
        imageLocationMap.createOnGlThread(context, "images/painting_14_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_14_description.png");
        break;
      case 5:
        imageLocationMap.createOnGlThread(context, "images/painting_14_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_15_description.png");
        break;
      case 6:
        imageLocationMap.createOnGlThread(context, "images/painting_14_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_16_description.png");
        break;
      case 7:
        imageLocationMap.createOnGlThread(context, "images/painting_14_location.png");
        imageDetailsText.createOnGlThread(context, "images/painting_17_description.png");
        break;
      case 8:
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

    switch (paintingIndex) {
      case 1:
        localBoundaryPoses = new Pose[]{
              Pose.makeTranslation(
                      -1.3f * augmentedImage.getExtentX(),
                      0.0f,
                      0.0f * augmentedImage.getExtentZ()),
              Pose.makeTranslation(
                      1.0f * augmentedImage.getExtentX(),
                      0.0f,
                      -0.1f * augmentedImage.getExtentZ()),
        };
        break;
      case 2:
        localBoundaryPoses = new Pose[]{
                Pose.makeTranslation(
                        -1.3f * augmentedImage.getExtentX(),
                        0.0f,
                        -0.5f * augmentedImage.getExtentZ()),
                Pose.makeTranslation(
                        0.1f * augmentedImage.getExtentX(),
                        0.0f,
                        0.0f * augmentedImage.getExtentZ()),
        };
        break;
      case 3:
        localBoundaryPoses = new Pose[]{
                Pose.makeTranslation(
                        1.3f * augmentedImage.getExtentX(),
                        0.0f,
                        0.0f * augmentedImage.getExtentZ()),
                Pose.makeTranslation(
                        -0.1f * augmentedImage.getExtentX(),
                        0.0f,
                        0.0f * augmentedImage.getExtentZ()),
        };
        break;
      case 8:
        localBoundaryPoses = new Pose[]{
                Pose.makeTranslation(
                        -1.3f * augmentedImage.getExtentX(),
                        0.0f,
                        0.0f * augmentedImage.getExtentZ()),
                Pose.makeTranslation(
                        0.01f * augmentedImage.getExtentX(),
                        0.0f,
                        -0.1f * augmentedImage.getExtentZ()),
        };
        break;
      default:
        localBoundaryPoses = new Pose[]{
                Pose.makeTranslation(
                        -1.3f * augmentedImage.getExtentX(),
                        0.0f,
                        0.0f * augmentedImage.getExtentZ()),
                Pose.makeTranslation(
                        0.0f * augmentedImage.getExtentX(),
                        0.0f,
                        0.0f * augmentedImage.getExtentZ()),
        };
        break;
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
}
