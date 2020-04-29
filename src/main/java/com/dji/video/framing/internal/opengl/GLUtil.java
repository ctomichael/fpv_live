package com.dji.video.framing.internal.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GLUtil {
    public static boolean DEBUG = false;
    public static final int GLCOLORFORMAT = 6408;
    public static String TAG = "GLUtil";

    public static int genTexture(int target) {
        return genTexture(target, false);
    }

    public static int genTexture(int target, boolean repeat) {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int tx = textures[0];
        GLES20.glBindTexture(target, tx);
        checkGlError("glBindTexture mTextureID");
        GLES20.glTexParameterf(target, 10241, 9728.0f);
        GLES20.glTexParameterf(target, 10240, 9729.0f);
        if (!repeat) {
            GLES20.glTexParameteri(target, 10242, 33071);
            GLES20.glTexParameteri(target, 10243, 33071);
        } else {
            GLES20.glTexParameteri(target, 10242, 10497);
            GLES20.glTexParameteri(target, 10243, 10497);
        }
        checkGlError("glTexParameter");
        return tx;
    }

    public static void loadTextureData(int textureID, int target, ByteBuffer data, int width, int height) {
        GLES20.glBindTexture(target, textureID);
        GLES20.glTexImage2D(target, 0, 6408, width, height, 0, 6408, FujifilmMakernoteDirectory.TAG_FILM_MODE, data);
    }

    public static void checkGlError(String op) {
        int error;
        do {
            error = GLES20.glGetError();
            if (error != 0) {
                Log.e(TAG, op + ": glError " + error);
            } else {
                return;
            }
        } while (!DEBUG);
        throw new RuntimeException(op + ": glError " + error);
    }

    public static void clearError() {
        GLES20.glGetError();
    }

    public static void destroyTexture(int textureID) {
        if (textureID >= 0) {
            GLES20.glDeleteTextures(1, new int[]{textureID}, 0);
        }
    }

    public static void destroyFrameBuffer(int fbID) {
        if (fbID >= 0) {
            GLES20.glDeleteFramebuffers(1, new int[]{fbID}, 0);
        }
    }

    public static int getFrameBufferBinding() {
        int[] _frameBufferID = new int[1];
        GLES20.glGetIntegerv(36006, IntBuffer.wrap(_frameBufferID));
        return _frameBufferID[0];
    }

    public static void bindFrameBuffer(int fbID) {
        GLES20.glBindFramebuffer(36160, fbID);
    }

    public static void loadRGBAData(ByteBuffer buffer, int textureID, int width, int height) {
        GLES20.glBindTexture(3553, textureID);
        GLES20.glTexImage2D(3553, 0, 6408, width, height, 0, 6408, FujifilmMakernoteDirectory.TAG_FILM_MODE, buffer);
    }

    public static void saveFrameBufferToBitmapFile(String filePath, int width, int height) {
        GLES20.glPixelStorei(3333, 4);
        ByteBuffer readByteBuffer = ByteBuffer.allocateDirect(width * height * 4);
        readByteBuffer.clear();
        GLES20.glReadPixels(0, 0, width, height, 6408, FujifilmMakernoteDirectory.TAG_FILM_MODE, readByteBuffer);
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        readByteBuffer.position(0);
        readByteBuffer.limit(width * height * 4);
        b.copyPixelsFromBuffer(readByteBuffer);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            if (fileOutputStream != null) {
                b.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                }
            }
        } catch (FileNotFoundException e2) {
        }
    }
}
