package dji.midware.media.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraSetCameraRotationMode;
import dji.midware.natives.FPVController;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

@EXClassNullAway
public class FFMpegNewMuxer implements DJIMuxerInterface {
    private static final int MUXER_STATE_INITIALIZED = 0;
    private static final int MUXER_STATE_STARTED = 1;
    private static final int MUXER_STATE_STOPPED = 2;
    private static final int MUXER_STATE_UNINITIALIZED = -1;
    private static final String TAG = "FFMpegNewMuxer";
    private Object addTrackLock = new Object();
    private Object directBufLock = new Object();
    private String[] mKeyArray = {"width", "height", "csd-0", "csd-1", "bitrate", "sample-rate", "channel-count", "mime"};
    private int mLastTrackIndex = -1;
    private long mNativeObject;
    private int mState = -1;
    private ByteBuffer temporaryDirectBuf;

    public int getNumTrack() {
        return 0;
    }

    public void init(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("filepath must not be null");
        }
        this.mNativeObject = FPVController.native_newMuxerSetup(filePath);
        if (this.mNativeObject != 0) {
            this.mState = 0;
        }
    }

    public int addTrack(MediaFormat format) {
        int trackIndex;
        if (format == null) {
            throw new IllegalArgumentException("format must not be null.");
        } else if (this.mState != 0) {
            throw new IllegalStateException("Muxer is not initialized.");
        } else if (this.mNativeObject == 0) {
            throw new IllegalStateException("Muxer has been released!");
        } else {
            if (DataCameraGetPushShotParams.getInstance().getRotationAngleType() == DataCameraSetCameraRotationMode.RotationAngleType.Rotate90) {
                FPVController.native_newMuxerSetOrientationHint(this.mNativeObject, 90);
            }
            Map<String, Object> formatMap = getFormatMap(format);
            int mapSize = formatMap.size();
            synchronized (this.addTrackLock) {
                if (mapSize > 0) {
                    String[] keys = new String[mapSize];
                    Object[] values = new Object[mapSize];
                    int i = 0;
                    for (Map.Entry<String, Object> entry : formatMap.entrySet()) {
                        keys[i] = (String) entry.getKey();
                        values[i] = entry.getValue();
                        i++;
                    }
                    trackIndex = FPVController.native_newMuxerAddTrack(this.mNativeObject, keys, values);
                    if (this.mLastTrackIndex >= trackIndex) {
                        throw new IllegalArgumentException("Invalid format.");
                    }
                    this.mLastTrackIndex = trackIndex;
                } else {
                    throw new IllegalArgumentException("format must not be empty.");
                }
            }
            return trackIndex;
        }
    }

    public void release() {
        if (this.mState == 1) {
            stop();
        }
        if (this.mNativeObject != 0) {
            FPVController.native_newMuxerRelease(this.mNativeObject);
            this.mNativeObject = 0;
        }
        this.mState = -1;
    }

    public void start() {
        if (this.mNativeObject == 0) {
            throw new IllegalStateException("Muxer has been released!");
        } else if (this.mState == 0) {
            FPVController.native_newMuxerStart(this.mNativeObject);
            this.mState = 1;
        } else {
            throw new IllegalStateException("Can't start due to wrong state.");
        }
    }

    public void stop() {
        if (this.mState == 1) {
            FPVController.native_newMuxerStop(this.mNativeObject);
            this.mState = 2;
            return;
        }
        throw new IllegalStateException("Can't stop due to wrong state.");
    }

    public void writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo, long durationUs) {
        if (trackIndex < 0 || trackIndex > this.mLastTrackIndex) {
            throw new IllegalArgumentException("trackIndex is invalid");
        } else if (byteBuf == null) {
            throw new IllegalArgumentException("byteBuffer must not be null");
        } else if (bufferInfo == null) {
            throw new IllegalArgumentException("bufferInfo must not be null");
        } else if (bufferInfo.size < 0 || bufferInfo.offset < 0 || bufferInfo.offset + bufferInfo.size > byteBuf.capacity() || bufferInfo.presentationTimeUs < 0) {
            throw new IllegalArgumentException("bufferInfo must specify a valid buffer offset, size and presentation time");
        } else if (this.mNativeObject == 0) {
            throw new IllegalStateException("Muxer has been released!");
        } else if (this.mState != 1) {
            throw new IllegalStateException("Can't write, muxer is not started");
        } else {
            byteBuf.position(bufferInfo.offset);
            byteBuf.limit(bufferInfo.size + bufferInfo.offset);
            if (byteBuf.isDirect()) {
                FPVController.native_newMuxerWriteSampleData(this.mNativeObject, trackIndex, byteBuf, bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
                return;
            }
            synchronized (this.directBufLock) {
                ByteBuffer directBuf = getTemporaryDirectBuf(byteBuf.position(), byteBuf.limit(), byteBuf.capacity());
                directBuf.put(byteBuf);
                FPVController.native_newMuxerWriteSampleData(this.mNativeObject, trackIndex, directBuf, bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
            }
        }
    }

    private ByteBuffer getTemporaryDirectBuf(int position, int limit, int capacity) {
        if (this.temporaryDirectBuf == null || this.temporaryDirectBuf.capacity() < capacity) {
            this.temporaryDirectBuf = ByteBuffer.allocateDirect(capacity);
        }
        this.temporaryDirectBuf.position(position);
        this.temporaryDirectBuf.limit(limit);
        return this.temporaryDirectBuf;
    }

    private Map<String, Object> getFormatMap(MediaFormat format) {
        Map<String, Object> formatMap = new HashMap<>();
        for (int i = 0; i < this.mKeyArray.length; i++) {
            if (this.mKeyArray[i].equalsIgnoreCase("width")) {
                if (format.containsKey("width")) {
                    formatMap.put("width", Integer.valueOf(format.getInteger("width")));
                }
            } else if (this.mKeyArray[i].equalsIgnoreCase("height")) {
                if (format.containsKey("height")) {
                    formatMap.put("height", Integer.valueOf(format.getInteger("height")));
                }
            } else if (this.mKeyArray[i].equalsIgnoreCase("csd-0")) {
                if (format.containsKey("csd-0")) {
                    ByteBuffer buffer = format.getByteBuffer("csd-0");
                    ByteBuffer tmpBuffer = ByteBuffer.allocateDirect(buffer.remaining());
                    tmpBuffer.put(buffer);
                    formatMap.put("csd-0", tmpBuffer);
                }
            } else if (this.mKeyArray[i].equalsIgnoreCase("csd-1")) {
                if (format.containsKey("csd-1")) {
                    ByteBuffer buffer2 = format.getByteBuffer("csd-1");
                    ByteBuffer tmpBuffer2 = ByteBuffer.allocateDirect(buffer2.remaining());
                    tmpBuffer2.put(buffer2);
                    formatMap.put("csd-1", tmpBuffer2);
                }
            } else if (this.mKeyArray[i].equalsIgnoreCase("bitrate")) {
                if (format.containsKey("bitrate")) {
                    formatMap.put("bitrate", Integer.valueOf(format.getInteger("bitrate")));
                }
            } else if (this.mKeyArray[i].equalsIgnoreCase("sample-rate")) {
                if (format.containsKey("sample-rate")) {
                    formatMap.put("sample-rate", Integer.valueOf(format.getInteger("sample-rate")));
                }
            } else if (this.mKeyArray[i].equalsIgnoreCase("channel-count")) {
                if (format.containsKey("channel-count")) {
                    formatMap.put("channel-count", Integer.valueOf(format.getInteger("channel-count")));
                }
            } else if (this.mKeyArray[i].equalsIgnoreCase("mime") && format.containsKey("mime")) {
                formatMap.put("mime", format.getString("mime"));
            }
        }
        return formatMap;
    }
}
