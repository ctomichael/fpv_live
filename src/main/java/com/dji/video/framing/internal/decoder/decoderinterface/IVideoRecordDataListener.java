package com.dji.video.framing.internal.decoder.decoderinterface;

import com.dji.video.framing.internal.VideoFrame;

public interface IVideoRecordDataListener {
    void onVideoFrameInput(VideoFrame videoFrame);
}
