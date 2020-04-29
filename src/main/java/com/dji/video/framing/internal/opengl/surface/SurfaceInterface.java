package com.dji.video.framing.internal.opengl.surface;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import com.dji.video.framing.internal.decoder.common.FrameFovType;
import com.dji.video.framing.internal.decoder.decoderinterface.IDJIVideoDecoder;

public interface SurfaceInterface extends SurfaceTexture.OnFrameAvailableListener {

    public interface BitmapCallback {
        void onResult(Bitmap bitmap);
    }

    void destroy();

    void enableDlogRender(boolean z);

    void enableOverExposureWarning(boolean z, int i);

    void getBitmap(int i, int i2, BitmapCallback bitmapCallback);

    int getExtraAsyncRenderInterval(String str);

    Surface getInputSurface();

    long getLastExtraDrawTime(String str);

    boolean getPeakFocusEnable();

    float getPeakFocusThreshold();

    byte[] getRgbaData(int i, int i2);

    int getSecondaryOutputInterval(String str);

    byte[] getYuvData(int i, int i2, int i3);

    void init(Object obj, int i, int i2);

    void init(Object obj, int i, int i2, int i3, int i4);

    void init(Object obj, int i, int i2, int i3, int i4, boolean z, boolean z2);

    void reSizeSurface(int i, int i2, int i3, int i4);

    void resetSurface(Object obj);

    void resizeSurface(int i, int i2);

    void setDistortion(boolean z);

    void setExtraAsyncRenderInterval(String str, int i);

    boolean setExtraAsyncRenderSurface(String str, Object obj, int i, int i2, int i3);

    void setPeakFocusEnable(boolean z);

    void setPeakFocusThreshold(float f);

    void setSecondaryOutputInterval(String str, int i);

    void setSecondaryOutputSurface(String str, Object obj, int i, int i2, int i3, int i4);

    void setVideoDecoder(IDJIVideoDecoder iDJIVideoDecoder);

    void setYUVScale(float f);

    void toBlack();

    void toGray();

    void updateFrameInfo(int i, int i2, int i3, FrameFovType frameFovType);
}
