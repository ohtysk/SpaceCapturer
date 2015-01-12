/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.doyeah.api;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import com.google.vrtoolkit.cardboard.*;

import javax.microedition.khronos.egl.EGLConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A Cardboard sample application.
 */
public class MainActivity extends CardboardActivity implements CardboardView.StereoRenderer {

    private static final String TAG = "MainActivity";

    private static final float CAMERA_Z = 0.01f;

    // We keep the light always position just above the user.
    private final float[] mLightPosInWorldSpace = new float[] {0.0f, 2.0f, 0.0f, 1.0f};
    private final float[] mLightPosInEyeSpace = new float[4];

    private int mGlProgram;
    private int mLightPosParam;
    Object3D cube1;
    private Object3D floor;
    private float[] mCamera;
    private float[] mView;
    private float[] mHeadView;
    float mObjectDistance = 10f;
    private float mFloorDepth = 20f;
    Vibrator mVibrator;
    CardboardOverlayView mOverlayView;
    private float eyex = 0;
    private float eyey = 0;
    private float eyez = 0;
    public String log;
    public float speed = 0;
    Game game;
    final float UNIT_SPEED = 0.08f;
    Handler mHandler = new Handler();
    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader
     * @param type The type of shader we will be creating.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return
     */
    private int loadGLShader(int type, int resId) {
        String code = readRawTextFile(resId);
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
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

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     * @param func
     */
    public static void checkGLError(String func) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, func + ": glError " + error);
            throw new RuntimeException(func + ": glError " + error);
        }
    }

    /**
     * Sets the view to our CardboardView and initializes the transformation matrices we will use
     * to render our scene.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.common_ui);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);

        mCamera = new float[16];
        mView = new float[16];
        mHeadView = new float[16];
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mOverlayView = (CardboardOverlayView) findViewById(R.id.overlay);
        mOverlayView.setMain(this);
        
        game = new Game(this);
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }

    /**
     * Creates the buffers we use to store information about the 3D world. OpenGL doesn't use Java
     * arrays, but rather needs data in a format it can understand. Hence we use ByteBuffers.
     * @param config The EGL configuration used when creating the surface.
     */
    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well

        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, R.raw.light_vertex);
        int gridShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, R.raw.grid_fragment);

        mGlProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mGlProgram, vertexShader);
        GLES20.glAttachShader(mGlProgram, gridShader);
        GLES20.glLinkProgram(mGlProgram);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        cube1 = new Cube(mGlProgram, "cube1");
        floor = new Floor(mGlProgram, "floor");
        floor.setIdentity();
        floor.translate(0, -mFloorDepth, 0);
        game.start();
        checkGLError("onSurfaceCreated");
    }

    /**
     * Converts a raw text file into a string.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return
     */
    private String readRawTextFile(int resId) {
        InputStream inputStream = getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
   	float [] location = new float[4];
    /**
     * Prepares OpenGL ES before we draw a frame.
     * @param headTransform The head transformation in the new frame.
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        GLES20.glUseProgram(mGlProgram);
        mLightPosParam = GLES20.glGetUniformLocation(mGlProgram, "u_LightPos");
        
        float [] forward = new float[3];
        headTransform.getForwardVector(forward, 0);
        float [] up = new float[3];
        headTransform.getUpVector(up, 0);
        float [] right = new float[3];
        headTransform.getRightVector(right, 0);
       	eyex += speed * right[2];
       	eyey += speed * up[2];
       	eyez -= speed * forward[2];
        

       	location[0] = -eyex;
       	location[1] = -eyey;
       	location[2] = -eyez;
       	location[3] = 1;

       	game.update();

       	// Build the camera matrix and apply it to the ModelView.
        Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        headTransform.getHeadView(mHeadView, 0);
        checkGLError("onReadyToDraw");
    }

    /**
     * Draws a frame for an eye. The transformation for that eye (from the camera) is passed in as
     * a parameter.
     * @param transform The transformations to apply to render this eye.
     */
    @Override
    public void onDrawEye(EyeTransform transform) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        float [] eyeview = new float[16];//
        eyeview = transform.getEyeView();//
        Matrix.translateM(eyeview, 0, eyex, eyey, eyez);//
        // Apply the eye transformation to the camera.
        Matrix.multiplyMM(mView, 0, eyeview, 0, mCamera, 0);

        // Set the position of the light
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mView, 0, mLightPosInWorldSpace, 0);
        GLES20.glUniform3f(mLightPosParam, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1],
                mLightPosInEyeSpace[2]);

        if (game.started()) {
        	cube1.draw(mView, transform);
            floor.draw(mView, transform);
        }
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }

    /**
     * Increment the score, hide the object, and give feedback if the user pulls the magnet while
     * looking at the object. Otherwise, remind the user what to do.
     */
    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");
        mVibrator.vibrate(50);
        if (!game.started()) {
        	game.start();
        	eyex = 0;
        	eyey = 0;
        	eyez = 0;
        	speed = 0;
        	return;
        }
        if (speed != 0.0f) {
			speed = 0.0f;
			mOverlayView.show3DToast("stopped!");
		} else {
			speed = UNIT_SPEED;
			mOverlayView.show3DToast("started to go ahead!");
		}
    }
    public void onDoubleTap() {
        Log.i(TAG, "onDoubleTap");
		mVibrator.vibrate(50);
        if (!game.started()) {
        	game.start();
        	eyex = 0;
        	eyey = 0;
        	eyez = 0;
        	speed = 0;
        	return;
        }
        
		speed = - UNIT_SPEED;
		mOverlayView.show3DToast("started to go back!");
    }
}
