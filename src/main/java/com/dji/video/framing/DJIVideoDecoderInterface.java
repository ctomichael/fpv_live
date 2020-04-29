package com.dji.video.framing;

import android.content.Context;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.decoder.decoderinterface.IReceiveDataCallback;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import com.dji.video.framing.internal.parser.VideoStreamParseController;

public interface DJIVideoDecoderInterface {

    public interface VideoRecordFilSavedListener {
        void onRecordingFileError(int i);

        void onRecordingFileSaved(IVideoRecordInfo iVideoRecordInfo);

        void onRecordingFileStart(IVideoRecordInfo iVideoRecordInfo);
    }

    public interface IVideoRecordInfo {
    }

    Context getContext();

    DJIVideoDecoder getDJIVideoDecoder();

    VideoStreamParseController getParseController();

    void initDJIVideoDecoder(Context context, SurfaceInterface surfaceInterface);

    void initDJIVideoDecoder(Context context, boolean z, SurfaceInterface surfaceInterface);

    void postStartDecodeEvent();

    void release();

    void setPause(boolean z);

    void setReceiverDataCallback(IReceiveDataCallback iReceiveDataCallback);

    void setRecordingCacheSizeLimit(int i);

    void setSurface(SurfaceInterface surfaceInterface);

    void setVideoDecoderEventListener(DJIVideoDecoder.VideoDecoderEventListener videoDecoderEventListener);

    void setVideoRecordFilSavedListener(VideoRecordFilSavedListener videoRecordFilSavedListener);

    void startVideoRecording(String str);

    void stopVideoRecording();
}
