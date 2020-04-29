package dji.midware.media.muxer;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.MediaLogger;

@EXClassNullAway
public class MuxerManager {

    public enum MuxerType {
        NATIVE,
        FFMPEG,
        FFMPEG_NEW
    }

    public static DJIMuxerInterface createMuxer(MuxerType type) {
        switch (type) {
            case NATIVE:
                DJIMuxerInterface muxer = new AndroidMuxer();
                MediaLogger.show("Using Android native Mp4 muxer");
                return muxer;
            case FFMPEG:
                DJIMuxerInterface muxer2 = new FFMpegMuxer();
                MediaLogger.show("Using FFMpeg-based Mp4 muxer");
                return muxer2;
            default:
                DJIMuxerInterface muxer3 = new FFMpegNewMuxer();
                MediaLogger.show("Using FFMpeg new muxer");
                return muxer3;
        }
    }
}
