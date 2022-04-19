package com.example.naocontroller;

import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Utilities {
    private Utilities() {}

    static public void setTexts(int paintingIndex, TextView titleText, TextView locationText) {
        switch (paintingIndex) {
            case 1:
                titleText.setText(R.string.title_painting_1);
                locationText.setText(R.string.location_painting_1);
                break;
            case 2:
                titleText.setText(R.string.title_painting_2);
                locationText.setText(R.string.location_painting_2);
                break;
            case 3:
                titleText.setText(R.string.title_painting_3);
                locationText.setText(R.string.location_painting_3);
                break;
            case 4:
                titleText.setText(R.string.title_painting_4);
                locationText.setText(R.string.location_painting_4);
                break;
            case 5:
                titleText.setText(R.string.title_painting_5);
                locationText.setText(R.string.location_painting_5);
                break;
            case 6:
                titleText.setText(R.string.title_painting_6);
                locationText.setText(R.string.location_painting_6);
                break;
            case 7:
                titleText.setText(R.string.title_painting_7);
                locationText.setText(R.string.location_painting_7);
                break;
            case 8:
                titleText.setText(R.string.title_painting_8);
                locationText.setText(R.string.location_painting_8);
                break;
        }
    }

    static public void setTextsAndCardsImages (
            int paintingIndex,
            TextView titleText,
            TextView authorText,
            TextView songText,
            TextView descriptionText,
            ImageView paintingView) {
        switch (paintingIndex) {
            case 1:
                titleText.setText(R.string.title_painting_1);
                authorText.setText(R.string.location_painting_1);
                songText.setText(R.string.song_painting_1);
                descriptionText.setText(R.string.description_painting_1);
                paintingView.setBackgroundResource(R.drawable.opera_11);
                break;
            case 2:
                titleText.setText(R.string.title_painting_2);
                authorText.setText(R.string.location_painting_2);
                songText.setText(R.string.song_painting_2);
                descriptionText.setText(R.string.description_painting_2);
                paintingView.setBackgroundResource(R.drawable.opera_12);
                break;
            case 3:
                titleText.setText(R.string.title_painting_3);
                authorText.setText(R.string.location_painting_3);
                songText.setText(R.string.song_painting_3);
                descriptionText.setText(R.string.description_painting_3);
                paintingView.setBackgroundResource(R.drawable.opera_13);
                break;
            case 4:
                titleText.setText(R.string.title_painting_4);
                authorText.setText(R.string.location_painting_4);
                songText.setText(R.string.song_painting_4);
                descriptionText.setText(R.string.description_painting_4);
                paintingView.setBackgroundResource(R.drawable.opera_14);
                break;
            case 5:
                titleText.setText(R.string.title_painting_5);
                authorText.setText(R.string.location_painting_5);
                songText.setText(R.string.song_painting_5);
                descriptionText.setText(R.string.description_painting_5);
                paintingView.setBackgroundResource(R.drawable.opera_15);
                break;
            case 6:
                titleText.setText(R.string.title_painting_6);
                authorText.setText(R.string.location_painting_6);
                songText.setText(R.string.song_painting_6);
                descriptionText.setText(R.string.description_painting_6);
                paintingView.setBackgroundResource(R.drawable.opera_16);
                break;
            case 7:
                titleText.setText(R.string.title_painting_7);
                authorText.setText(R.string.location_painting_7);
                songText.setText(R.string.song_painting_7);
                descriptionText.setText(R.string.description_painting_7);
                paintingView.setBackgroundResource(R.drawable.opera_17);
                break;
            case 8:
                titleText.setText(R.string.title_painting_8);
                authorText.setText(R.string.location_painting_8);
                songText.setText(R.string.song_painting_8);
                descriptionText.setText(R.string.description_painting_8);
                paintingView.setBackgroundResource(R.drawable.opera_18);
                break;
        }
    }

    static public int getPaintingIndexFromTitle(String paintingTitle) {
        switch(paintingTitle) {
            case "LA RUINA":
                return 1;
            case "I CENTAURI":
                return 2;
            case "LA SELVA DEI SUICIDI":
                return 3;
            case "CAPANEO":
                return 4;
            case "BRUNETTO LATINI":
                return 5;
            case "IL POZZO DEI GIGANTI":
                return 6;
            case "GERIONE":
                return 7;
            case "GLI ADULATORI":
                return 8;
            default:
                return 0;
        }
    }

    static public float[] calculateWorldToCameraMatrix(float[] modelmtx, float[] viewmtx, float[] prjmtx) {
        float scaleFactor = 1.0f;
        float[] scaleMatrix = new float[16];
        float[] modelXscale = new float[16];
        float[] viewXmodelXscale = new float[16];
        float[] world2screenMatrix = new float[16];

        Matrix.setIdentityM(scaleMatrix, 0);
        scaleMatrix[0] = scaleFactor;
        scaleMatrix[5] = scaleFactor;
        scaleMatrix[10] = scaleFactor;

        Matrix.multiplyMM(modelXscale, 0, modelmtx, 0, scaleMatrix, 0);
        Matrix.multiplyMM(viewXmodelXscale, 0, viewmtx, 0, modelXscale, 0);
        Matrix.multiplyMM(world2screenMatrix, 0, prjmtx, 0, viewXmodelXscale, 0);

        return world2screenMatrix;
    }

    static public double[] world2Screen(int screenWidth, int screenHeight, float[] world2cameraMatrix) {
        float[] origin = {0f, 0f, 0f, 1f};
        float[] ndcCoord = new float[4];
        Matrix.multiplyMV(ndcCoord, 0,  world2cameraMatrix, 0,  origin, 0);

        ndcCoord[0] = ndcCoord[0]/ndcCoord[3];
        ndcCoord[1] = ndcCoord[1]/ndcCoord[3];

        double[] pos_2d = new double[]{0,0};
        pos_2d[0] = screenWidth  * ((ndcCoord[0] + 1.0)/2.0);
        pos_2d[1] = screenHeight * (( 1.0 - ndcCoord[1])/2.0);

        return pos_2d;
    }

    static public boolean isInScreen(float[] modelmtx, float[] viewmtx, float[] projmtx, DisplayMetrics displayMetrics) {
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float[] anchorMatrix = new float[16];
        float[] world2screenMatrix = Utilities.calculateWorldToCameraMatrix(modelmtx, viewmtx, projmtx);
        double[] anchor_2d = Utilities.world2Screen(width, height, world2screenMatrix);

        return 0 < anchor_2d[0] && anchor_2d[0] < width
                && 0 < anchor_2d[1] && anchor_2d[1] < height;
    }
}
