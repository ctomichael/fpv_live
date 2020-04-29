package com.dji.video.framing.internal.opengl.extra;

import android.opengl.GLES20;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.opengl.GLUtil;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;

public class FrameBufferTexturePair {
    public static final int GLCOLORFORMAT = 6408;
    public static final String TAG = "FrameBufferTexturePair";
    public int frameBuffer = -1;
    public int texture = -1;

    public FrameBufferTexturePair() {
    }

    public FrameBufferTexturePair(int w, int h) {
        create(w, h);
    }

    public void create(int w, int h) {
        int[] out = new int[1];
        GLES20.glGenFramebuffers(1, out, 0);
        int fb = out[0];
        GLES20.glGenTextures(1, out, 0);
        int tx = out[0];
        GLES20.glBindFramebuffer(36160, fb);
        GLES20.glBindTexture(3553, tx);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexImage2D(3553, 0, 6408, w, h, 0, 6408, FujifilmMakernoteDirectory.TAG_FILM_MODE, null);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, tx, 0);
        VideoLog.d(TAG, "fb : " + fb, new Object[0]);
        VideoLog.d(TAG, "tx : " + tx, new Object[0]);
        int status = GLES20.glCheckFramebufferStatus(36160);
        if (status != 36053) {
            VideoLog.e(TAG, "framebuffer's status: " + status, new Object[0]);
            return;
        }
        this.frameBuffer = fb;
        this.texture = tx;
    }

    public void destroy() {
        if (this.texture >= 0) {
            GLUtil.destroyTexture(this.texture);
        }
        if (this.frameBuffer >= 0) {
            GLUtil.destroyFrameBuffer(this.frameBuffer);
        }
    }
}
