package se.jayway.opengl.tutorial;

/**
 * Created by jianping_wu on 2014/10/14.
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Circle {
    protected static final float PI = 3.1415926f;
    protected static final int pointCount = 100;
    protected FloatBuffer mVertexBuffer;
    protected ShortBuffer mIndexBuffer;
    protected short[] mIndices;

    public Circle() {
        float angle;
        float[] buffer = new float[(pointCount+2)*3];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        for (int i = 0; i <= pointCount; ++i) {
            angle = 2*PI*i/pointCount;
            buffer[3*i+3] = (float)Math.cos(angle);
            buffer[3*i+4] = (float)Math.sin(angle);
            buffer[3*i+5] = 0;
        }

        mIndices = new short[pointCount+2];
        for (short i = 0; i <= pointCount+1; ++i) {
            mIndices[i] = i;
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect((pointCount+2) * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(buffer);
        mVertexBuffer.position(0);

        ByteBuffer ibb = ByteBuffer.allocateDirect((pointCount+2) * 2);
        ibb.order(ByteOrder.nativeOrder());
        mIndexBuffer = ibb.asShortBuffer();
        mIndexBuffer.put(mIndices);
        mIndexBuffer.position(0);
    }

    public void draw(float[] mvpmatrix) {
        /*
        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);

        // Enabled the vertices buffer for writing
        //and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of
        //an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glDrawElements(GL10.GL_TRIANGLE_FAN, mIndices.length, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
        */
    }
}
