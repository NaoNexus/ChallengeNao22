package com.example.naocontroller.ar.rendering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class QuadRenderer {
    private static final String TAG = QuadRenderer.class.getSimpleName();

    private final int[] mTextures = new int[1];

    private float[] quadCoords;

    private float[] quadTexCoords;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int TEXCOORDS_PER_VERTEX = 2;

    private static final String VERTEX_SHADER =
            "uniform mat4 u_ModelViewProjection;\n"
            + "attribute vec4 a_Position;\n"
            + "attribute vec2 a_TexCoord;\n"
            + "\n"
            + "varying vec2 v_TexCoord;\n"
            + "\n"
            + "void main() {\n"
            + "gl_Position = u_ModelViewProjection * a_Position;\n"
            + "   v_TexCoord = a_TexCoord;\n"
            + "}";

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n"
            + "uniform sampler2D u_Texture;\n"
            + "varying vec2 v_TexCoord;\n"
            + "\n"
            + "void main() {\n"
            + "   gl_FragColor = texture2D(u_Texture, v_TexCoord);\n"
            + "}\n";

    private FloatBuffer mQuadVertices;
    private FloatBuffer mQuadTexCoord;
    private int mQuadProgram;
    private int mQuadPositionParam;
    private int mQuadTexCoordParam;
    private int mTextureUniform;
    private int mModelViewProjectionUniform;

    // Temporary matrices allocated here to reduce number of allocations for each frame.
    private float[] mModelMatrix = new float[16];
    private float[] mModelViewMatrix = new float[16];
    private float[] mModelViewProjectionMatrix = new float[16];

    public void createOnGlThread(Context context, String pngName) {
        // Read the texture.
        Bitmap textureBitmap = null;

        try {
            textureBitmap = BitmapFactory.decodeStream(context.getAssets().open(pngName));
        } catch (IOException e) {
            Log.e(TAG, "Exception reading texture", e);
            return;
        }

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, mTextures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        textureBitmap.recycle();

        ShaderUtil.checkGLError(TAG, "Texture loading");

        // Build the geometry of a simple quad.

        int numVertices = 4;
        if (numVertices != quadCoords.length / COORDS_PER_VERTEX) {
            throw new RuntimeException("Unexpected number of vertices in BackgroundRenderer.");
        }

        ByteBuffer bbVertices = ByteBuffer.allocateDirect(quadCoords.length * Float.BYTES);
        bbVertices.order(ByteOrder.nativeOrder());
        mQuadVertices = bbVertices.asFloatBuffer();
        mQuadVertices.put(quadCoords);
        mQuadVertices.position(0);

        ByteBuffer bbTexCoords =
                ByteBuffer.allocateDirect(numVertices * TEXCOORDS_PER_VERTEX * Float.BYTES);
        bbTexCoords.order(ByteOrder.nativeOrder());
        mQuadTexCoord = bbTexCoords.asFloatBuffer();
        mQuadTexCoord.put(quadTexCoords);
        mQuadTexCoord.position(0);

        ByteBuffer bbTexCoordsTransformed =
                ByteBuffer.allocateDirect(numVertices * TEXCOORDS_PER_VERTEX * Float.BYTES);
        bbTexCoordsTransformed.order(ByteOrder.nativeOrder());

        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER);
        int fragmentShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);

        mQuadProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mQuadProgram, vertexShader);
        GLES20.glAttachShader(mQuadProgram, fragmentShader);
        GLES20.glLinkProgram(mQuadProgram);
        GLES20.glUseProgram(mQuadProgram);

        ShaderUtil.checkGLError(TAG, "Program creation");

        mQuadPositionParam = GLES20.glGetAttribLocation(mQuadProgram, "a_Position");
        mQuadTexCoordParam = GLES20.glGetAttribLocation(mQuadProgram, "a_TexCoord");
        mTextureUniform = GLES20.glGetUniformLocation(mQuadProgram, "u_Texture");
        mModelViewProjectionUniform =
                GLES20.glGetUniformLocation(mQuadProgram, "u_ModelViewProjection");

        ShaderUtil.checkGLError(TAG, "Program parameters");

        Matrix.setIdentityM(mModelMatrix, 0);
    }

    private int loadGLShader(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    public void updateModelMatrix(float[] modelMatrix, float scaleFactor) {
        float[] scaleMatrix = new float[16];
        Matrix.setIdentityM(scaleMatrix, 0);
        scaleMatrix[0] = scaleFactor;
        scaleMatrix[5] = scaleFactor;
        scaleMatrix[10] = scaleFactor;
        Matrix.multiplyMM(mModelMatrix, 0, modelMatrix, 0, scaleMatrix, 0);
    }

    public void draw(float[] cameraView, float[] cameraPerspective) {
        ShaderUtil.checkGLError(TAG, "Before draw");
        Matrix.multiplyMM(mModelViewMatrix, 0, cameraView, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mModelViewProjectionMatrix, 0, cameraPerspective, 0, mModelViewMatrix, 0);

        GLES20.glUseProgram(mQuadProgram);

        // Attach the object texture.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLES20.glUniform1i(mTextureUniform, 0);
        GLES20.glUniformMatrix4fv(mModelViewProjectionUniform, 1, false, mModelViewProjectionMatrix, 0);

        // Set the vertex positions.
        GLES20.glVertexAttribPointer(
                mQuadPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, mQuadVertices);

        // Set the texture coordinates.
        GLES20.glVertexAttribPointer(
                mQuadTexCoordParam, TEXCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, mQuadTexCoord);

        // Enable vertex arrays
        GLES20.glEnableVertexAttribArray(mQuadPositionParam);
        GLES20.glEnableVertexAttribArray(mQuadTexCoordParam);

        // Make background of pngs transparent
        GLES20.glEnable(GLES20.GL_BLEND);

        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        // Disable vertex arrays
        GLES20.glDisableVertexAttribArray(mQuadPositionParam);
        GLES20.glDisableVertexAttribArray(mQuadTexCoordParam);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);

        ShaderUtil.checkGLError(TAG, "After draw");
    }

    /**
     * Constructor of the image renderer.
     * @param width: Width of the image.
     * @param height: Height of the image.
     * @param rotation: Rotation of the image (0, 1, 2, 3).
     */
    public QuadRenderer (float width, float height, int rotation) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        quadCoords = new float[]{
                /* X    Y      Z  */
                        -halfWidth, 0.0f, -halfHeight,
                        -halfWidth, 0.0f, +halfHeight,
                        +halfWidth, 0.0f, -halfHeight,
                        +halfWidth, 0.0f, +halfHeight,
        };

        switch (rotation) {
            case 0:
                quadTexCoords = new float[]
                    /*X    Y */
                    {0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,};
                break;
            case 1:
                quadTexCoords = new float[]
                    /*X     Y */
                    {0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f, 0.0f,
                    1.0f, 0.0f,};
                break;
            case 2:
                quadTexCoords = new float[]
                    /*X     Y */
                    {1.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    0.0f, 0.0f,};

                break;
            case 3:
                quadTexCoords = new float[]
                    /*X     Y */
                    {1.0f, 0.0f,
                    0.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 1.0f,};
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rotation);
        }
    }
}
