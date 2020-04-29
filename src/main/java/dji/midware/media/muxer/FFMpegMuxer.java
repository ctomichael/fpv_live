package dji.midware.media.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraGetCameraRotationMode;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraSetCameraRotationMode;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.datacontainer.DynamicByteBuffer;
import dji.midware.natives.FPVController;
import dji.midware.util.BytesUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

@EXClassNullAway
public class FFMpegMuxer implements DJIMuxerInterface {
    private static boolean DEBUG = true;
    private static String TAG = "FFMpegMuxer";
    AudioTrackFormat aTrack = null;
    private int curTrackNum = 0;
    private String filePath;
    private DynamicByteBuffer jniBuffer = new DynamicByteBuffer(138240, true);
    private Status status = Status.StandBy;
    VideoTrackFormat vTrack = null;

    private enum Status {
        StandBy,
        Initialized,
        TrackAdded,
        Muxing,
        Stopped
    }

    static class AudioTrackFormat {
        int bitRate;
        int channels;
        byte[] csd;
        int csdSize;
        long durationUs;
        int sampleRate;
        int trackIndex;

        AudioTrackFormat() {
        }
    }

    static class VideoTrackFormat {
        long durationUs;
        int height;
        int sizeSpspps;
        byte[] spspps;
        int trackIndex;
        int width;

        VideoTrackFormat() {
        }
    }

    public synchronized void init(String filePath2) throws IOException {
        if (this.status != Status.StandBy) {
            MediaLogger.e(TAG, "Current status=" + this.status + ", op=Init()");
        } else {
            MediaLogger.i(DEBUG, TAG, "init");
            this.filePath = filePath2;
            this.curTrackNum = 0;
            this.status = Status.Initialized;
        }
    }

    public int getNumTrack() {
        return this.curTrackNum;
    }

    public synchronized int addTrack(MediaFormat format) {
        int i;
        if (this.status == Status.Initialized || this.status == Status.TrackAdded) {
            MediaLogger.i(DEBUG, TAG, "addTrack");
            i = 1;
            if (format.getString("mime").equals(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0])) {
                ByteBuffer sps = format.getByteBuffer("csd-0");
                ByteBuffer pps = format.getByteBuffer("csd-1");
                Log.i(TAG, "sps = " + BytesUtil.byte2hex(sps.array()));
                Log.i(TAG, "pps = " + BytesUtil.byte2hex(pps.array()));
                byte[] spsppsArray = new byte[(sps.capacity() + pps.capacity())];
                sps.clear();
                sps.get(spsppsArray, 0, sps.capacity());
                pps.clear();
                pps.get(spsppsArray, sps.capacity(), pps.capacity());
                this.vTrack = new VideoTrackFormat();
                this.vTrack.trackIndex = this.curTrackNum;
                this.vTrack.width = format.getInteger("width");
                this.vTrack.height = format.getInteger("height");
                this.vTrack.spspps = spsppsArray;
                this.vTrack.sizeSpspps = spsppsArray.length;
                if (format.containsKey("durationUs")) {
                    this.vTrack.durationUs = format.getLong("durationUs") * 1000;
                } else {
                    this.vTrack.durationUs = 1000;
                }
            } else if (format.getString("mime").equals(DJIVideoUtil.AUDIO_ENCODING_FORMAT[0])) {
                ByteBuffer csd = format.getByteBuffer("csd-0");
                Log.i(TAG, "csd = " + BytesUtil.byte2hex(csd.array()));
                byte[] csdArray = new byte[csd.capacity()];
                csd.clear();
                csd.get(csdArray);
                MediaLogger.i(DEBUG, TAG, "csdArray=" + Arrays.toString(csdArray));
                this.aTrack = new AudioTrackFormat();
                this.aTrack.trackIndex = this.curTrackNum;
                this.aTrack.bitRate = 128000;
                this.aTrack.sampleRate = format.getInteger("sample-rate");
                this.aTrack.channels = format.getInteger("channel-count");
                this.aTrack.durationUs = format.getLong("durationUs") * 1000;
                this.aTrack.csd = csdArray;
                this.aTrack.csdSize = csdArray.length;
            }
            this.curTrackNum++;
            this.status = Status.TrackAdded;
        } else {
            MediaLogger.e(TAG, "Current status=" + this.status + ", op=addTrack()");
            i = -1;
        }
        return i;
    }

    public synchronized void start() {
        int i = 1;
        synchronized (this) {
            if (this.status != Status.TrackAdded) {
                MediaLogger.e(TAG, "Current status=" + this.status + ", op=start()");
            } else {
                MediaLogger.i(DEBUG, TAG, "start");
                FPVController.native_mp4MuxerInit(this.curTrackNum);
                MediaLogger.i(DEBUG, TAG, "isRotated: " + DataCameraGetCameraRotationMode.getInstance().getRotationAngleType(0).toString());
                if (!(DataCameraGetPushShotParams.getInstance().getRotationAngleType() == DataCameraSetCameraRotationMode.RotationAngleType.Rotate90)) {
                    i = 0;
                }
                FPVController.native_mp4MuxerSetIsRotated(i);
                if (this.vTrack != null) {
                    FPVController.native_mp4MuxerAddVideoTrack(this.vTrack.trackIndex, this.vTrack.width, this.vTrack.height, this.vTrack.spspps, this.vTrack.sizeSpspps, this.vTrack.durationUs);
                }
                if (this.aTrack != null) {
                    FPVController.native_mp4MuxerAddAudioTrack(this.aTrack.trackIndex, this.aTrack.bitRate, this.aTrack.sampleRate, this.aTrack.channels, this.aTrack.durationUs, this.aTrack.csd, this.aTrack.csdSize);
                }
                FPVController.native_mp4MuxerStart(this.filePath);
                this.status = Status.Muxing;
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public synchronized void writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo, long durationUs) {
        if (this.status != Status.Muxing) {
            MediaLogger.e(TAG, "Current status=" + this.status + ", op=writeSampleData()");
        } else {
            MediaLogger.i(DEBUG, TAG, "writeSampleData");
            int muxerflags = 0;
            if ((bufferInfo.flags & 1) != 0) {
                muxerflags = 0 | 1;
            }
            byteBuf.position(bufferInfo.offset);
            byteBuf.limit(bufferInfo.offset + bufferInfo.size);
            this.jniBuffer.setData(byteBuf);
            try {
                if (FPVController.native_mp4MuxerWrite(trackIndex, this.jniBuffer.lockAndReadData(), bufferInfo.size, muxerflags, bufferInfo.presentationTimeUs, durationUs) != 0) {
                    Log.e(TAG, "write error: re");
                }
                this.jniBuffer.unLock();
            } catch (Exception e) {
                this.jniBuffer.unLock();
            } catch (Throwable th) {
                this.jniBuffer.unLock();
                throw th;
            }
        }
    }

    public synchronized void stop() {
        if (this.status != Status.Muxing) {
            MediaLogger.e(TAG, "Current status=" + this.status + ", op=stop()");
        } else {
            MediaLogger.i(DEBUG, TAG, "stop");
            if (FPVController.native_mp4MuxerStop() != 0) {
                Log.e(TAG, "write error: re");
            }
            this.status = Status.Stopped;
        }
    }

    public synchronized void release() {
        if (this.status != Status.Stopped) {
            MediaLogger.e(TAG, "Current status=" + this.status + ", op=release()");
        } else {
            MediaLogger.i(DEBUG, TAG, "FFMpegMuxer release");
            this.status = Status.StandBy;
        }
    }
}
