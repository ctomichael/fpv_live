package com.dji.video.framing.internal.decoder;

import android.content.Context;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.VideoFrame;
import com.dji.video.framing.internal.decoder.decoderinterface.IBlackKeyFrameGenerator;
import com.dji.video.framing.utils.DJIVideoUtil;
import java.io.InputStream;

public class GdrKeyFrameGenerator implements IBlackKeyFrameGenerator {
    private static final String TAG = "GdrKeyFrameGenerator";
    private Context mContext;
    private KeyFrameResCallback mKeyFrameResCallback;

    public interface KeyFrameResCallback {
        int getKeyFrameResID(int i, int i2);
    }

    public GdrKeyFrameGenerator(Context context, KeyFrameResCallback callback) {
        this.mContext = context.getApplicationContext();
        this.mKeyFrameResCallback = callback;
    }

    public boolean needGenFakeKeyFrame(VideoFrame referenceFrame) {
        return isGdrStartFrame(referenceFrame) && !referenceFrame.isKeyFrame;
    }

    public VideoFrame genFakeKeyFrame(VideoFrame frame) {
        byte[] defaultKeyFrame = getDefaultKeyFrame(frame.width, frame.height);
        return VideoFrame.obtain(defaultKeyFrame, defaultKeyFrame.length, frame.pts, frame.isKeyFrame, frame.width, frame.height, 0, frame.frameIndex - 1, -1, 0, -1, 0, 0, false);
    }

    public boolean isGdrStartFrame(VideoFrame frame) {
        boolean z = true;
        if (frame == null) {
            return false;
        }
        if (frame.frameNum != 1) {
            z = false;
        }
        return z;
    }

    private byte[] getDefaultKeyFrame(int width, int height) {
        InputStream inputStream = null;
        try {
            int iFrameId = this.mKeyFrameResCallback.getKeyFrameResID(width, height);
            if (iFrameId >= 0) {
                inputStream = this.mContext.getResources().openRawResource(iFrameId);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                return buffer;
            }
            DJIVideoUtil.closeSafely(inputStream);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            VideoLog.e(TAG, "getDefaultKeyFrame() error:" + e, new Object[0]);
        } finally {
            DJIVideoUtil.closeSafely(inputStream);
        }
    }
}
