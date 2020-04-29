package com.dji.video.framing.internal.opengl.renderer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.opengl.GLUtil;
import com.dji.video.framing.internal.opengl.extra.FrameBufferTexturePair;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class GLRenderBase {
    private static boolean DEBUG = false;
    protected static final int FLOAT_SIZE_BYTES = 4;
    protected static final int INT_SIZE_BYTES = 4;
    private static String TAG = "GLRenderBase";
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_POS_DATA_STRIDE_BYTES = 12;
    private static final int TRIANGLE_VERTICES_UV_DATA_STRIDE_BYTES = 8;
    private float[] mMVPMatrix = new float[16];
    protected int mProgram;
    private float[] mSTMatrix;
    private float[] mSTMatrixInvert = new float[16];
    protected int[] mTriangleIndicesData = {0, 2, 1, 1, 2, 3};
    protected IntBuffer mTriangleVerticesIndices;
    protected FloatBuffer mTriangleVerticesPos;
    protected float[] mTriangleVerticesPosData = {-1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f};
    protected FloatBuffer mTriangleVerticesUv;
    protected float[] mTriangleVerticesUvData = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    private int maPositionHandle;
    private int maTextureHandle;
    private int muMVPMatrixHandle;
    private int muSTMatrixHandle;
    private int musTexture;
    protected int viewport_height;
    protected int viewport_width;

    /* access modifiers changed from: protected */
    public abstract String getFragmentShader();

    /* access modifiers changed from: protected */
    public abstract String getVertexShader();

    /* access modifiers changed from: protected */
    public abstract void onInit();

    /* access modifiers changed from: protected */
    public abstract void preDrawArrays();

    /* access modifiers changed from: protected */
    public synchronized void updateVerticeBuffer() {
        this.mTriangleVerticesPos = ByteBuffer.allocateDirect(this.mTriangleVerticesPosData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTriangleVerticesPos.put(this.mTriangleVerticesPosData).position(0).limit(this.mTriangleVerticesPosData.length);
        this.mTriangleVerticesUv = ByteBuffer.allocateDirect(this.mTriangleVerticesUvData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTriangleVerticesUv.put(this.mTriangleVerticesUvData).position(0).limit(this.mTriangleVerticesUvData.length);
        this.mTriangleVerticesIndices = ByteBuffer.allocateDirect(this.mTriangleIndicesData.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        this.mTriangleVerticesIndices.put(this.mTriangleIndicesData).position(0).limit(this.mTriangleIndicesData.length);
    }

    /* access modifiers changed from: protected */
    public synchronized void setVerticeUvData(float[] verticeUvData) throws Exception {
        if (verticeUvData == null) {
            throw new RuntimeException();
        }
        if (this.mTriangleVerticesUv == null) {
            this.mTriangleVerticesUv = ByteBuffer.allocateDirect(this.mTriangleVerticesUvData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        } else if (this.mTriangleVerticesUv.limit() != verticeUvData.length) {
            throw new RuntimeException();
        }
        this.mTriangleVerticesUvData = verticeUvData;
        this.mTriangleVerticesUv.position(0);
        this.mTriangleVerticesUv.put(this.mTriangleVerticesUvData).position(0).limit(this.mTriangleVerticesUvData.length);
    }

    /* access modifiers changed from: protected */
    public FloatBuffer getTriangleVerticesPosBuffer() {
        return this.mTriangleVerticesPos;
    }

    /* access modifiers changed from: protected */
    public FloatBuffer getTriangleVerticesUvBuffer() {
        return this.mTriangleVerticesUv;
    }

    /* access modifiers changed from: protected */
    public IntBuffer getTriangleVerticesIndices() {
        return this.mTriangleVerticesIndices;
    }

    /* access modifiers changed from: protected */
    public int getTriangleVerticesIndicesLength() {
        return this.mTriangleIndicesData.length;
    }

    public void init() {
        updateVerticeBuffer();
        createProgram();
        onInit();
    }

    public void drawRange(int textureID, int textureTarget, float[] mSTMatrix2, boolean invert, float rotateDegree, int viewWidth, int viewHeight, int left, int bottom, int right, int top) {
        drawFromRange(textureID, textureTarget, mSTMatrix2, invert, rotateDegree, viewWidth, viewHeight, left, bottom, right, top);
    }

    public void drawFromRange(int textureID, int textureTarget, float[] mSTMatrix2, boolean invert, float rotateDegree, int viewWidth, int viewHeight, int left, int bottom, int right, int top) {
        int vp_w = (viewWidth * viewWidth) / (right - left);
        int vp_h = (viewHeight * viewHeight) / (top - bottom);
        draw(textureID, textureTarget, mSTMatrix2, invert, rotateDegree, ((-left) * vp_w) / viewWidth, ((-bottom) * vp_h) / viewHeight, vp_w, vp_h);
    }

    public void drawToRange(int textureID, int textureTarget, float[] mSTMatrix2, boolean invert, float rotateDegree, int viewWidth, int viewHeight, int left, int bottom, int right, int top) {
        int i = textureID;
        int i2 = textureTarget;
        float[] fArr = mSTMatrix2;
        boolean z = invert;
        float f = rotateDegree;
        draw(i, i2, fArr, z, f, left, bottom, right - left, top - bottom);
    }

    public void draw(int textureID, int textureTarget, float[] mSTMatrix2, boolean invert, float rotateDegree, int viewWidth, int viewHeight) {
        draw(textureID, textureTarget, mSTMatrix2, invert, rotateDegree, 0, 0, viewWidth, viewHeight);
    }

    public void draw(int textureID, float[] mSTMatrix2, float rotateDegree, int vp_x, int vp_y, int vp_w, int vp_h) {
        if (textureID >= 0) {
            draw(textureID, 3553, mSTMatrix2, false, rotateDegree, vp_x, vp_y, vp_w, vp_h);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.opengl.GLES20.glVertexAttribPointer(int, int, int, boolean, int, java.nio.Buffer):void}
     arg types: [int, int, ?, int, int, java.nio.FloatBuffer]
     candidates:
      ClspMth{android.opengl.GLES20.glVertexAttribPointer(int, int, int, boolean, int, int):void}
      ClspMth{android.opengl.GLES20.glVertexAttribPointer(int, int, int, boolean, int, java.nio.Buffer):void} */
    public void draw(int textureID, int textureTarget, float[] stMatrix, boolean invert, float rotateDegree, int vp_x, int vp_y, int vp_w, int vp_h) {
        if (textureID >= 0) {
            if (stMatrix == null || stMatrix.length != 16) {
                stMatrix = new float[16];
                Matrix.setIdentityM(stMatrix, 0);
            }
            if (stMatrix != this.mSTMatrix) {
                this.mSTMatrix = stMatrix;
                System.arraycopy(this.mSTMatrix, 0, this.mSTMatrixInvert, 0, 16);
                float a_t = this.mSTMatrixInvert[5];
                float b_t = this.mSTMatrixInvert[13];
                this.mSTMatrixInvert[5] = -a_t;
                this.mSTMatrixInvert[13] = a_t + b_t;
            }
            GLES20.glViewport(vp_x, vp_y, vp_w, vp_h);
            this.viewport_width = vp_w;
            this.viewport_height = vp_h;
            GLUtil.checkGlError("GLRenderBase draw: set viewPort");
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            GLUtil.checkGlError("GLRenderBase draw: after clear");
            GLES20.glUseProgram(this.mProgram);
            GLUtil.checkGlError("GLRenderBase draw: after glUseProgram");
            this.mTriangleVerticesPos.position(0);
            GLES20.glVertexAttribPointer(this.maPositionHandle, 3, (int) FujifilmMakernoteDirectory.TAG_MAX_APERTURE_AT_MIN_FOCAL, false, 12, (Buffer) getTriangleVerticesPosBuffer());
            GLUtil.checkGlError("glVertexAttribPointer maPosition");
            GLES20.glEnableVertexAttribArray(this.maPositionHandle);
            GLUtil.checkGlError("glEnableVertexAttribArray maPositionHandle");
            this.mTriangleVerticesUv.position(0);
            GLES20.glVertexAttribPointer(this.maTextureHandle, 2, (int) FujifilmMakernoteDirectory.TAG_MAX_APERTURE_AT_MIN_FOCAL, false, 8, (Buffer) getTriangleVerticesUvBuffer());
            GLUtil.checkGlError("glVertexAttribPointer maTextureHandle");
            GLES20.glEnableVertexAttribArray(this.maTextureHandle);
            GLUtil.checkGlError("glEnableVertexAttribArray maTextureHandle");
            Matrix.setRotateM(this.mMVPMatrix, 0, rotateDegree, 0.0f, 0.0f, 1.0f);
            GLES20.glUniformMatrix4fv(this.muMVPMatrixHandle, 1, false, this.mMVPMatrix, 0);
            if (invert) {
            }
            GLES20.glUniformMatrix4fv(this.muSTMatrixHandle, 1, false, invert ? this.mSTMatrixInvert : this.mSTMatrix, 0);
            bindTexture(33984, textureTarget, textureID);
            GLES20.glUniform1i(this.musTexture, 0);
            preDrawArrays();
            if (getTriangleVerticesIndices().capacity() <= 0 || getTriangleVerticesIndicesLength() <= 0) {
                VideoLog.w(TAG, " GLES20.glDrawElements() invilad   GLES20.glDrawElements:" + getTriangleVerticesIndices().capacity() + " or getTriangleVerticesIndicesLength:" + getTriangleVerticesIndicesLength(), new Object[0]);
            } else {
                GLES20.glDrawElements(4, getTriangleVerticesIndicesLength(), (int) FujifilmMakernoteDirectory.TAG_MAX_FOCAL_LENGTH, getTriangleVerticesIndices());
            }
            GLUtil.checkGlError("glDrawArrays");
            bindTexture(33984, textureTarget, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void bindTexture(int activeSlot, int target, int id) {
        GLES20.glActiveTexture(activeSlot);
        GLES20.glBindTexture(target, id);
    }

    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        GLUtil.checkGlError("glCreateShader type=" + shaderType);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, 35713, compiled, 0);
        if (compiled[0] != 0) {
            return shader;
        }
        Log.e(TAG, "Could not compile shader " + shaderType + ":");
        Log.e(TAG, " " + GLES20.glGetShaderInfoLog(shader));
        GLES20.glDeleteShader(shader);
        return 0;
    }

    private void deleteShader(int shaderObject) {
        GLES20.glDeleteShader(shaderObject);
    }

    private void createProgram() {
        int vertexShader = loadShader(35633, getVertexShader());
        if (vertexShader == 0) {
            Log.e(TAG, "can't create vertex shader");
        }
        int pixelShader = loadShader(35632, getFragmentShader());
        if (pixelShader == 0) {
            Log.e(TAG, "can't create fragment shader for display");
        }
        this.mProgram = GLES20.glCreateProgram();
        if (this.mProgram == 0) {
        }
        GLES20.glAttachShader(this.mProgram, vertexShader);
        GLUtil.checkGlError("glAttachShader");
        GLES20.glAttachShader(this.mProgram, pixelShader);
        GLUtil.checkGlError("glAttachShader");
        GLES20.glLinkProgram(this.mProgram);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(this.mProgram, 35714, linkStatus, 0);
        if (linkStatus[0] != 1) {
            Log.e(TAG, "Could not link program: ");
            Log.e(TAG, GLES20.glGetProgramInfoLog(this.mProgram));
            GLES20.glDeleteProgram(this.mProgram);
            this.mProgram = 0;
        }
        if (this.mProgram == 0) {
            throw new RuntimeException("failed creating program");
        }
        if (vertexShader != 0) {
            deleteShader(vertexShader);
        }
        if (pixelShader != 0) {
            deleteShader(pixelShader);
        }
        this.maPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "aPosition");
        checkLocation(this.maPositionHandle, "aPosition");
        this.maTextureHandle = GLES20.glGetAttribLocation(this.mProgram, "aTextureCoord");
        checkLocation(this.maTextureHandle, "aTextureCoord");
        this.muMVPMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uMVPMatrix");
        checkLocation(this.muMVPMatrixHandle, "uMVPMatrix");
        this.muSTMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uSTMatrix");
        checkLocation(this.muSTMatrixHandle, "uSTMatrix");
        this.musTexture = GLES20.glGetUniformLocation(this.mProgram, "sTexture");
        checkLocation(this.musTexture, "sTexture");
    }

    /* access modifiers changed from: protected */
    public void checkLocation(int location, String label) {
        if (location < 0) {
            throw new RuntimeException("Unable to locate '" + label + "' in program");
        }
    }

    public void release() {
        if (this.mProgram != 0) {
            GLES20.glDeleteProgram(this.mProgram);
        }
    }

    public void readRGBAData(ByteBuffer buffer, int width, int height) {
        GLES20.glPixelStorei(3333, 4);
        buffer.clear();
        GLES20.glReadPixels(0, 0, width, height, 6408, FujifilmMakernoteDirectory.TAG_FILM_MODE, buffer);
    }

    public void draw(FrameBufferTexturePair fbo, int textureID, int textureTarget, float[] mSTMatrix2, boolean invert, float rotateDegree, int vp_x, int vp_y, int vp_w, int vp_h) {
        if (textureID >= 0 && fbo != null && fbo.texture >= 0) {
            GLUtil.bindFrameBuffer(fbo.frameBuffer);
            draw(textureID, textureTarget, mSTMatrix2, invert, rotateDegree, vp_x, vp_y, vp_w, vp_h);
            GLUtil.bindFrameBuffer(0);
        }
    }
}
