package se.jayway.opengl.tutorial;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.util.Log;

/**
 * Created by jianping_wu on 2014/10/14.
 */
public class ColoredCircle extends Circle {
    private static final String Tag = "OpenGLII";
    private FloatBuffer mColorBuffer;

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            //"out vec3 vPos; " +
            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            //"  vPos.xyz = gl_Position.xyz;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            // "out vec4 frag;" +
            "void main() {" +
            //"  vec4 color = vec4(1.0, 1.0, 0.0, 1.0);" +
            "  gl_FragColor = vColor;" +
            "}";

    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private int mVertexShader;
    private int mFragmentShader;
    float[] mColors;

    public ColoredCircle() {
        super();
        mColors = new float[(pointCount+2)*4];
        for (int i = 0; i < pointCount+2; ++i) {
            mColors[4*i] =  i % 10 < 2 ? 1.0f : 0;
            mColors[4*i+1] = (i % 10 >= 2 && i % 10 < 4) ? 1.0f : 0;
            mColors[4*i+2] = (i % 10 >= 4 && i % 10 < 6) ? 1.0f : 0;
            mColors[4*i+3] = 1.0f;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(mColors.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mColorBuffer = bb.asFloatBuffer();
        mColorBuffer.put(mColors);
        mColorBuffer.position(0);

        mVertexShader = OpenGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        mFragmentShader = OpenGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, mVertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, mFragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public void draw(float[] mvpMatrix, float[] color) {
        GLES20.glUseProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);

        // Set color for drawing the triangle
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        OpenGLRenderer.checkGlError("glGetUniformLocation");

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        OpenGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        OpenGLRenderer.checkGlError("glUniformMatrix4fv");

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, mIndices.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        // Disable the color buffer.
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
