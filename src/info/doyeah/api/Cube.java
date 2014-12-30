package info.doyeah.api;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.google.vrtoolkit.cardboard.EyeTransform;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Cube {
    public static final float[] COORDS = new float[] {
        // Front face
        -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,

        // Right face
        1.0f, 1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,

        // Back face
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,

        // Left face
        -1.0f, 1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f,

        // Top face
        -1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, -1.0f,

        // Bottom face
        1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f,
    };

    public static final float[] COLORS = new float[] {
        // front, green
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,

        // right, blue
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,

        // back, also green
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,

        // left, also blue
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,

        // top, red
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,

        // bottom, also red
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
    };

    public static final float[] FOUND_COLORS = new float[] {
        // front, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // right, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // back, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // left, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // top, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // bottom, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
    };

    public static final float[] NORMALS = new float[] {
        // Front face
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,

        // Right face
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,

        // Back face
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,

        // Left face
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,

        // Top face
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,

        // Bottom face
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f
    };
    private static final FloatBuffer vertices;
    private static final FloatBuffer colors;
    private static final FloatBuffer foundColors;
    private static final FloatBuffer normals;
	private static final int COORDS_PER_VERTEX = 3;
    static {
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(COORDS.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        vertices = bbVertices.asFloatBuffer();
        vertices.put(COORDS);
        vertices.position(0);

        ByteBuffer bbColors = ByteBuffer.allocateDirect(COLORS.length * 4);
        bbColors.order(ByteOrder.nativeOrder());
        colors = bbColors.asFloatBuffer();
        colors.put(COLORS);
        colors.position(0);

        ByteBuffer bbFoundColors = ByteBuffer.allocateDirect(Cube.FOUND_COLORS.length * 4);
        bbFoundColors.order(ByteOrder.nativeOrder());
        foundColors = bbFoundColors.asFloatBuffer();
        foundColors.put(FOUND_COLORS);
        foundColors.position(0);

        ByteBuffer bbNormals = ByteBuffer.allocateDirect(Cube.NORMALS.length * 4);
        bbNormals.order(ByteOrder.nativeOrder());
        normals = bbNormals.asFloatBuffer();
        normals.put(NORMALS);
        normals.position(0);
    }
    private final int isFloorParam;
    private final int modelParam;
    private final int modelViewParam;
	private final int mPositionParam;
	private final int mNormalParam;
	private final int mColorParam;
	private final int mModelViewProjectionParam;
	private float[] mModelCube = new float[16];
    public Cube(int program){
    	//this.program = program;
        isFloorParam = GLES20.glGetUniformLocation(program, "u_IsFloor");
        modelParam = GLES20.glGetUniformLocation(program, "u_Model");
        modelViewParam = GLES20.glGetUniformLocation(program, "u_MVMatrix");
        mPositionParam = GLES20.glGetAttribLocation(program, "a_Position");
        mNormalParam = GLES20.glGetAttribLocation(program, "a_Normal");
        mColorParam = GLES20.glGetAttribLocation(program, "a_Color");
        mModelViewProjectionParam = GLES20.glGetUniformLocation(program, "u_MVP");
    }
    public void setIdentity() {
    	Matrix.setIdentityM(mModelCube, 0);
    }
    public void translate(float x, float y, float z) {
    	Matrix.translateM(mModelCube, 0, x, y, z);
    }
    public void rotate(float degree, float x, float y, float z) {
    	Matrix.rotateM(mModelCube, 0, degree, x, y, z);
    }
    public void scale(float x, float y, float z) {
    	Matrix.scaleM(mModelCube, 0, x, y, z);
    }
    public void draw(float []view, EyeTransform transform) {

        GLES20.glEnableVertexAttribArray(mPositionParam);
        GLES20.glEnableVertexAttribArray(mNormalParam);
        GLES20.glEnableVertexAttribArray(mColorParam);        // This is not the floor!
        GLES20.glUniform1f(isFloorParam, 0f);
        float [] mModelView = new float[16];
        Matrix.multiplyMM(mModelView, 0, view, 0, mModelCube, 0);
        float [] mModelViewProjection = new float[16];
        Matrix.multiplyMM(mModelViewProjection, 0, transform.getPerspective(), 0, mModelView, 0);

        // Set the Model in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(modelParam, 1, false, mModelCube, 0);

        // Set the ModelView in the shader, used to calculate lighting
        GLES20.glUniformMatrix4fv(modelViewParam, 1, false, mModelView, 0);

        // Set the position of the cube
        GLES20.glVertexAttribPointer(mPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false, 0, vertices);

        // Set the ModelViewProjection matrix in the shader.
        GLES20.glUniformMatrix4fv(mModelViewProjectionParam, 1, false, mModelViewProjection, 0);

        // Set the normal positions of the cube, again for shading
        GLES20.glVertexAttribPointer(mNormalParam, 3, GLES20.GL_FLOAT,
                false, 0, normals);

        GLES20.glVertexAttribPointer(mColorParam, 4, GLES20.GL_FLOAT, false, 0, colors);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        MainActivity.checkGLError("Drawing cube");

    }
}
