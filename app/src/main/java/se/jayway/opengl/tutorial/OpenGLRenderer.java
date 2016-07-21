package se.jayway.opengl.tutorial;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

public class OpenGLRenderer implements Renderer {

    public static final String Tag = "OpenGLII";

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private float mAngle = 1;

	// Initialize our circle.
	// circle circle = new circle();
    private ColoredCircle circle;
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.
         * microedition.khronos.opengles.GL10, javax.microedition.khronos.
         * egl.EGLConfig)
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(Tag, "onSurfaceCreated");
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        circle = new ColoredCircle();
	}

	public void onDrawFrame(GL10 gl) {
        // Log.i(Tag, "onDrawFrame" + mAngle);
        float[] matrix2 = new float[16];
        float[] matrix3 = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 10, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        // draw red circle
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.scaleM(mMVPMatrix, 0, 1.5f, 1.5f, 1f);
        float[] red = {1, 0, 0, 1};
        circle.draw(mMVPMatrix, red);

        // set green circle matrix2 base on mMVPMatrix
        Matrix.setRotateM(mRotationMatrix, 0, -mAngle, 0, 0, 1.0f);
        Matrix.multiplyMM(matrix2, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        Matrix.translateM(matrix2, 0, 0, 3, 0);
        Matrix.scaleM(matrix2, 0, 0.5f, 0.5f, 1f);
        // draw green circle
        float[] green = {0, 1, 0, 1};
        circle.draw(matrix2, green);

        // set blue circle matrix3 base on matrix2
        Matrix.setRotateM(mRotationMatrix, 0, mAngle*5, 0, 0, 1.0f);
        Matrix.multiplyMM(matrix3, 0, matrix2, 0,  mRotationMatrix, 0);
        Matrix.translateM(matrix3, 0, 0, 2, 0);
        Matrix.scaleM(matrix3, 0, 0.5f, 0.5f, 1f);
        // draw blue circle
        float[] blue = {0, 0, 1, 1};
        circle.draw(matrix3, blue);

        ++mAngle;
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(Tag, "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1f, 10);
	}

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(Tag, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
