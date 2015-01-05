package info.doyeah.api;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.google.vrtoolkit.cardboard.EyeTransform;

public class Object3D {
	private final FloatBuffer verticesBuffer;
	private final FloatBuffer colorsBuffer;
	private final FloatBuffer normalsBuffer;
	private static final int COORDS_PER_VERTEX = 3;
	private final int isFloorParam;
	private final int modelParam;
	private final int modelViewParam;
	private final int positionParam;
	private final int normalParam;
	private final int colorParam;
	private final int modelViewProjectionParam;
	protected float[] model = new float[16];
	private final float isFloorValue; 
	private final int num;
	private final String name;
	
	public Object3D(int program, float [] vertices, float [] colors, float [] normals, boolean isFloor, String name) {
		super();
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(vertices.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        verticesBuffer = bbVertices.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        ByteBuffer bbColors = ByteBuffer.allocateDirect(colors.length * 4);
        bbColors.order(ByteOrder.nativeOrder());
        colorsBuffer = bbColors.asFloatBuffer();
        colorsBuffer.put(colors);
        colorsBuffer.position(0);

        ByteBuffer bbNormals = ByteBuffer.allocateDirect(normals.length * 4);
        bbNormals.order(ByteOrder.nativeOrder());
        normalsBuffer = bbNormals.asFloatBuffer();
        normalsBuffer.put(normals);
        normalsBuffer.position(0);

        isFloorParam = GLES20.glGetUniformLocation(program, "u_IsFloor");
        modelParam = GLES20.glGetUniformLocation(program, "u_Model");
        modelViewParam = GLES20.glGetUniformLocation(program, "u_MVMatrix");
        positionParam = GLES20.glGetAttribLocation(program, "a_Position");
        normalParam = GLES20.glGetAttribLocation(program, "a_Normal");
        colorParam = GLES20.glGetAttribLocation(program, "a_Color");
        modelViewProjectionParam = GLES20.glGetUniformLocation(program, "u_MVP");
        
        this.isFloorValue = isFloor ? 1f : 0f;
        this.num = vertices.length / COORDS_PER_VERTEX;
        this.name = name;
	}

	public void setIdentity() {
		Matrix.setIdentityM(model, 0);
	}

	public void translate(float x, float y, float z) {
		Matrix.translateM(model, 0, x, y, z);
	}

	public void rotate(float degree, float x, float y, float z) {
		Matrix.rotateM(model, 0, degree, x, y, z);
	}

	public void scale(float x, float y, float z) {
		Matrix.scaleM(model, 0, x, y, z);
	}

	public void draw(float []view, EyeTransform transform) {
	
	    GLES20.glEnableVertexAttribArray(positionParam);
	    GLES20.glEnableVertexAttribArray(normalParam);
	    GLES20.glEnableVertexAttribArray(colorParam);

	    GLES20.glUniform1f(isFloorParam, isFloorValue);

	    float [] mModelView = new float[16];
	    Matrix.multiplyMM(mModelView, 0, view, 0, model, 0);
	    float [] mModelViewProjection = new float[16];
	    Matrix.multiplyMM(mModelViewProjection, 0, transform.getPerspective(), 0, mModelView, 0);
	
	    // Set the Model in the shader, used to calculate lighting
	    GLES20.glUniformMatrix4fv(modelParam, 1, false, model, 0);
	
	    // Set the ModelView in the shader, used to calculate lighting
	    GLES20.glUniformMatrix4fv(modelViewParam, 1, false, mModelView, 0);
	
	    // Set the ModelViewProjection matrix in the shader.
	    GLES20.glUniformMatrix4fv(modelViewProjectionParam, 1, false, mModelViewProjection, 0);

	    // Set the position of the cube
	    GLES20.glVertexAttribPointer(positionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, verticesBuffer);
	
	    // Set the normal positions of the cube, again for shading
	    GLES20.glVertexAttribPointer(normalParam, 3, GLES20.GL_FLOAT, false, 0, normalsBuffer);
	
	    GLES20.glVertexAttribPointer(colorParam, 4, GLES20.GL_FLOAT, false, 0, colorsBuffer);
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, num);
	    MainActivity.checkGLError("Drawing " + name);
	}
	public boolean include(float [] location) {
		return false;
	};
	
}