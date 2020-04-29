package com.dji.video.framing.internal.decoder.decoderinterface;

import com.dji.video.framing.internal.VideoFrame;

public interface IBlackKeyFrameGenerator {
    VideoFrame genFakeKeyFrame(VideoFrame videoFrame);

    boolean isGdrStartFrame(VideoFrame videoFrame);

    boolean needGenFakeKeyFrame(VideoFrame videoFrame);
}
