package dji.midware.media.demuxer;

import android.media.MediaFormat;
import dji.component.mediaprovider.DJIMediaStore;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIAudioUtil;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.natives.FPVController;
import dji.midware.util.BytesUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

@EXClassNullAway
public class FFMpegDemuxer extends DemuxerBase implements DemuxerInterface {
    private static final boolean DEBUG = false;
    public static final String TAG = "FFMpegDemuxer";
    private int filter_in_size;
    private int filter_out_size;
    private Vector<MediaFormat> formats = new Vector<>();
    private ByteBuffer frameData;
    private int frameDataOffset;
    private long handle = 0;
    private boolean hasAdvance = false;
    private boolean isEOS;
    int max_video_height = 0;
    int max_video_width = 0;
    private ByteBuffer sBuffer = ByteBuffer.allocateDirect(2048);
    private int sampleFlags;
    private int sampleOffset;
    private int sampleSize;
    private long sampleTime;
    private int sampleTrackIndex;
    private int sei_start;
    private Set<Integer> selectedTracks = new HashSet();
    private int trackCount = -1;

    public FFMpegDemuxer() {
        MediaLogger.i(TAG, "create a FFMpegDemuxer");
    }

    public boolean advance() {
        checkError();
        int srcFrameMaxSize = DJIVideoUtil.getH264FrameMaxSize(this.max_video_width, this.max_video_height, 0);
        if (srcFrameMaxSize < 102400) {
            srcFrameMaxSize = DJIAudioUtil.AAC_PACKET_MAX_SIZE;
        }
        if (this.frameData == null || this.frameData.capacity() < srcFrameMaxSize) {
            this.frameData = ByteBuffer.allocateDirect(srcFrameMaxSize);
        }
        this.sampleTrackIndex = -1;
        this.isEOS = false;
        while (!this.isEOS && !this.selectedTracks.contains(Integer.valueOf(this.sampleTrackIndex))) {
            this.isEOS = FPVController.jni_demuxer_readSample(this.handle, this.sBuffer, this.frameData, false);
            if (this.isEOS) {
                break;
            }
            this.sBuffer.clear();
            this.sBuffer.order(ByteOrder.LITTLE_ENDIAN);
            this.sampleTime = this.sBuffer.getLong();
            int flags_tmp = this.sBuffer.getInt();
            this.sampleFlags = 0;
            if ((flags_tmp & 1) != 0) {
                this.sampleFlags |= 1;
            }
            this.filter_in_size = this.sBuffer.getInt();
            int i = this.sBuffer.getInt();
            this.filter_out_size = i;
            this.sampleSize = i;
            if (this.sampleSize > this.frameData.capacity()) {
                MediaLogger.e(TAG, "the input H264 frame is larger than the allocated bytebuffer. sampleSize=" + this.sampleSize + " bytebuffer's capacity=" + this.frameData.capacity());
                this.sampleOffset = 0;
                this.sampleSize = 0;
            }
            this.sampleTrackIndex = this.sBuffer.getInt();
        }
        if (!this.isEOS) {
            this.sei_start = -1;
            this.frameDataOffset = 0;
            MediaFormat mediaFormat = this.formats.get(this.sampleTrackIndex);
            while (true) {
                int mark = this.sBuffer.getInt();
                if (mark != 0) {
                    switch (mark) {
                        case 1:
                            int blockOffset = this.sBuffer.getInt();
                            int blockSize = this.sBuffer.getInt();
                            MediaLogger.i(TAG, "sps offset=" + blockOffset + ", size=" + blockSize);
                            byte[] block = new byte[blockSize];
                            this.frameData.position(blockOffset);
                            this.frameData.get(block, 0, blockSize);
                            MediaLogger.i(TAG, "csd-0=" + BytesUtil.byte2hex(block));
                            break;
                        case 2:
                            int blockOffset2 = this.sBuffer.getInt();
                            int blockSize2 = this.sBuffer.getInt();
                            MediaLogger.i(TAG, "pps offset=" + blockOffset2 + ", size=" + blockSize2);
                            byte[] block2 = new byte[blockSize2];
                            this.frameData.position(blockOffset2);
                            this.frameData.get(block2, 0, blockSize2);
                            MediaLogger.i(TAG, "csd-1=" + BytesUtil.byte2hex(block2));
                            break;
                        case 3:
                            this.sei_start = this.sBuffer.getInt();
                            break;
                        case 4:
                            this.frameDataOffset = this.sBuffer.getInt();
                            break;
                    }
                } else {
                    switch (this.formats.get(this.sampleTrackIndex).getInteger("mediaType")) {
                        case 1:
                            if (this.sei_start > 0 && this.sei_start < this.frameDataOffset) {
                                this.sampleOffset = this.sei_start;
                                break;
                            } else {
                                this.sampleOffset = this.frameDataOffset;
                                break;
                            }
                        default:
                            this.sampleOffset = 0;
                            break;
                    }
                    if (this.sampleSize > 0 && this.sampleSize >= this.sampleOffset) {
                        this.sampleSize -= this.sampleOffset;
                    }
                    MediaLogger.i(false, TAG, String.format(Locale.US, "sei_offset=%d frameDataOffset=%d sampleOffset=%d filter_in=%d filter_out=%d sampleSize=%d", Integer.valueOf(this.sei_start), Integer.valueOf(this.frameDataOffset), Integer.valueOf(this.sampleOffset), Integer.valueOf(this.filter_in_size), Integer.valueOf(this.filter_out_size), Integer.valueOf(this.sampleSize)));
                }
            }
        }
        if (!this.isEOS) {
            return true;
        }
        return false;
    }

    public int getTrackCount() {
        checkError();
        if (this.trackCount == -1) {
            this.trackCount = FPVController.jni_demuxer_getTrackCount(this.handle);
            MediaLogger.e(TAG, "trackCount=" + this.trackCount);
        }
        return this.trackCount;
    }

    public int getSampleFlags() {
        checkError();
        if (!this.hasAdvance) {
            this.hasAdvance = true;
            advance();
        }
        return this.sampleFlags;
    }

    public long getSampleTime() {
        checkError();
        if (!this.hasAdvance) {
            this.hasAdvance = true;
            advance();
        }
        return this.sampleTime;
    }

    public MediaFormat getTrackFormat(int index) {
        checkError();
        if (index < this.formats.size()) {
            return this.formats.get(index);
        }
        throw new RuntimeException("invalid track index. index=" + index + " formats.size()=" + this.formats.size());
    }

    public void setDataSource(String filePath) throws IOException {
        MediaLogger.i(TAG, "setDataSource");
        long re = FPVController.jni_demuxer_init(filePath);
        if (re == 0) {
            MediaLogger.e(TAG, "file " + filePath + " is not opened successfully");
            throw new IOException("file " + filePath + " is not opened successfully");
        }
        this.handle = re;
        for (int i = 0; i < getTrackCount(); i++) {
            FPVController.jni_demuxer_getTrackFormat(this.handle, i, this.sBuffer);
            this.sBuffer.clear();
            this.sBuffer.order(ByteOrder.LITTLE_ENDIAN);
            MediaFormat format = new MediaFormat();
            int mime_len = this.sBuffer.getInt();
            byte[] mime = new byte[96];
            this.sBuffer.get(mime);
            String str_mime = new String(mime, 0, mime_len, "UTF-8");
            if (str_mime.equalsIgnoreCase("video/h264")) {
                str_mime = DJIVideoUtil.VIDEO_ENCODING_FORMAT[0];
            } else if (str_mime.equalsIgnoreCase("video/mpeg4")) {
                str_mime = DJIVideoUtil.VIDEO_ENCODING_FORMAT[1];
            }
            if (str_mime.equalsIgnoreCase("audio/aac")) {
                str_mime = DJIVideoUtil.AUDIO_ENCODING_FORMAT[0];
            }
            if (str_mime.equalsIgnoreCase("audio/ffmpeg")) {
                str_mime = DJIVideoUtil.AUDIO_ENCODING_FORMAT[0];
            }
            format.setString("mime", str_mime);
            int mediaType = this.sBuffer.getInt();
            format.setInteger("mediaType", mediaType);
            if (mediaType == 1) {
                int width = this.sBuffer.getInt();
                int height = this.sBuffer.getInt();
                if (width > this.max_video_width) {
                    this.max_video_width = width;
                }
                if (height > this.max_video_height) {
                    this.max_video_height = height;
                }
                long duration = this.sBuffer.getLong();
                int rotation = this.sBuffer.getInt();
                format.setInteger("width", width);
                format.setInteger("height", height);
                format.setLong("durationUs", duration);
                format.setInteger(DJIMediaStore.FileColumns.ROTATION, rotation);
                MediaLogger.i(TAG, "width=" + width);
                MediaLogger.i(TAG, "height=" + height);
                MediaLogger.i(TAG, "duration=" + duration);
                MediaLogger.i(TAG, "rotation=" + rotation);
                this.sBuffer.getInt();
                this.sBuffer.getInt();
                this.sBuffer.getInt();
                this.sBuffer.getInt();
                int sps_size = this.sBuffer.getInt();
                if (sps_size != 0) {
                    byte[] sps = new byte[sps_size];
                    this.sBuffer.get(sps);
                    this.sBuffer.position(this.sBuffer.position() + (200 - sps_size));
                    ByteBuffer csd0 = ByteBuffer.wrap(sps, 0, sps_size);
                    format.setByteBuffer("csd-0", csd0);
                    MediaLogger.i(TAG, "csd-0=" + BytesUtil.byte2hex(sps));
                    MediaLogger.i(TAG, "FFMpegDemuxer gets csd-0: " + csd0);
                } else {
                    this.sBuffer.position(this.sBuffer.position() + 200);
                }
                int pps_size = this.sBuffer.getInt();
                if (pps_size != 0) {
                    byte[] pps = new byte[pps_size];
                    this.sBuffer.get(pps);
                    this.sBuffer.position(this.sBuffer.position() + (100 - pps_size));
                    ByteBuffer csd1 = ByteBuffer.wrap(pps, 0, pps_size);
                    format.setByteBuffer("csd-1", csd1);
                    MediaLogger.i(TAG, "csd-1=" + BytesUtil.byte2hex(pps));
                    MediaLogger.i(TAG, "FFMpegDemuxer gets csd-1: " + csd1);
                } else {
                    this.sBuffer.position(this.sBuffer.position() + 100);
                }
            } else if (mediaType == 2) {
                this.sBuffer.getInt();
                this.sBuffer.getInt();
                format.setLong("durationUs", this.sBuffer.getLong());
                this.sBuffer.getInt();
                int channelCount = this.sBuffer.getInt();
                int sampleRate = this.sBuffer.getInt();
                int profile = this.sBuffer.getInt();
                int sample_format = this.sBuffer.getInt();
                format.setInteger("channel-count", channelCount);
                format.setInteger("sample-rate", sampleRate);
                format.setInteger("aac-profile", profile);
                format.setInteger("sample_format", sample_format);
                format.setByteBuffer("csd-0", DJIAudioUtil.makeAudioCsd0(profile, sampleRate, channelCount));
            }
            this.formats.add(format);
        }
        for (int i2 = 0; i2 < this.formats.size(); i2++) {
            MediaLogger.i(TAG, "format No. " + i2 + " : " + this.formats.get(i2));
        }
        this.hasAdvance = false;
    }

    public int readSampleData(ByteBuffer byteBuf, int offset) {
        int copySize;
        checkError();
        if (this.isEOS) {
            return -1;
        }
        if (!this.hasAdvance) {
            this.hasAdvance = true;
            advance();
        }
        int target_remaining = byteBuf.capacity() - offset;
        if (this.sampleSize < target_remaining) {
            copySize = this.sampleSize;
        } else {
            copySize = target_remaining;
        }
        byteBuf.clear();
        byteBuf.position(offset);
        this.frameData.clear();
        this.frameData.position(this.sampleOffset);
        this.frameData.limit(this.sampleOffset + copySize);
        byteBuf.put(this.frameData);
        byteBuf.position(offset);
        byteBuf.limit(offset + copySize);
        return copySize;
    }

    public void seekTo(long timeUs, int mode) {
        boolean isBackward;
        checkError();
        switch (mode) {
            case 0:
                isBackward = true;
                break;
            case 1:
                isBackward = false;
                break;
            default:
                throw new RuntimeException("unsupported mode");
        }
        int track = -1;
        if (!this.selectedTracks.isEmpty()) {
            track = this.selectedTracks.iterator().next().intValue();
        }
        FPVController.jni_demuxer_seekTo(this.handle, track, timeUs, isBackward);
        this.hasAdvance = false;
    }

    public int getSampleTrackIndex() {
        if (!this.hasAdvance) {
            this.hasAdvance = true;
            advance();
        }
        return this.sampleTrackIndex;
    }

    public void selectTrack(int index) {
        if (index >= getTrackCount()) {
            throw new RuntimeException("invalid track index: index=" + index + " track count=" + getTrackCount());
        }
        this.selectedTracks.add(Integer.valueOf(index));
    }

    public void unselectTrack(int index) {
        this.selectedTracks.remove(Integer.valueOf(index));
    }

    public void release() {
        if (this.handle != 0) {
            FPVController.jni_demuxer_release(this.handle);
            this.handle = 0;
        }
    }

    private void checkError() {
        if (this.handle == 0) {
            throw new RuntimeException("the data source is not set");
        }
    }

    public boolean isEOSorError() {
        return this.isEOS;
    }
}
