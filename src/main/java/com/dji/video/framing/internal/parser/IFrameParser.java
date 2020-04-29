package com.dji.video.framing.internal.parser;

import android.os.Environment;
import java.io.File;

public interface IFrameParser {
    public static final String MOCK_DJI_VIDEO_FILE_1080P = (Environment.getExternalStorageDirectory() + File.separator + "dji_videostream1080gdr_rawStream.h264");
    public static final String MOCK_DJI_VIDEO_FILE_720P = (Environment.getExternalStorageDirectory() + File.separator + "dji_videostream_720.h264");
    public static final String MOCK_MAVIC_VIDEO_FILE = (Environment.getExternalStorageDirectory() + File.separator + "mavic_pro.video");
    public static final String MOCK_YUV_STREAM_FILE_1080P = (Environment.getExternalStorageDirectory() + File.separator + "dji_videostream1080.yuv");
    public static final String MOCK_YUV_STREAM_FILE_720P = (Environment.getExternalStorageDirectory() + File.separator + "dji_videostream_720.yuv");

    public interface OnFrameParserListener {
        void onRecvData(byte[] bArr, int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, boolean z2);
    }

    void feedData(byte[] bArr, int i, int i2);

    void resetCheckStatus();

    void setFrameParserListener(OnFrameParserListener onFrameParserListener);

    void stop();

    public static class CREATOR {
        public static IFrameParser create(boolean isHevcMode) {
            return new VideoFrameParser(isHevcMode);
        }
    }
}
