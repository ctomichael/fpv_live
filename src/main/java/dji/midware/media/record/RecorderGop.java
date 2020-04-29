package dji.midware.media.record;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataDm368SetParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.record.RecorderBase;
import dji.midware.util.DJIEventBusUtil;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

@EXClassNullAway
@TargetApi(18)
public class RecorderGop extends RecorderBase implements RecorderInterface, H264FrameListener {
    private static String TAG = "RecorderGop";
    private static RecorderGop instance = null;
    /* access modifiers changed from: private */
    public long cmdOpDelay;
    /* access modifiers changed from: private */
    public long cmdStartTime;
    /* access modifiers changed from: private */
    public boolean cmdSuccess;
    /* access modifiers changed from: private */
    public Object cmdSync = new Object();
    private BufferedOutputStream h264BufferedOutputStream = null;
    private OutputStream h264OutputStream = null;
    private MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
    private MediaMuxer muxer = null;
    private int numFrameJumped;
    boolean start = true;
    private boolean writeH264File = false;

    public static synchronized RecorderGop getInstance() {
        RecorderGop recorderGop;
        synchronized (RecorderGop.class) {
            if (instance == null) {
                instance = new RecorderGop();
                DJIEventBusUtil.register(instance);
            }
            recorderGop = instance;
        }
        return recorderGop;
    }

    public static synchronized void destroy() {
        synchronized (RecorderGop.class) {
            MediaLogger.show("RecorderGop is destroyed");
            if (instance != null) {
                instance.onDestroy();
                instance = null;
            }
        }
    }

    private void createFile() {
        try {
            this.muxer = new MediaMuxer(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4", 0);
            MediaLogger.show("successfully created muxer");
            if (this.writeH264File) {
                this.h264OutputStream = new FileOutputStream(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".h264");
                if (this.h264OutputStream != null) {
                    this.h264BufferedOutputStream = new BufferedOutputStream(this.h264OutputStream);
                    Log.i(TAG, "An H264 File has been opened");
                    return;
                }
                Log.e(TAG, "error in creating H264 File");
            }
        } catch (IOException e2) {
            MediaLogger.show(e2);
        }
    }

    private boolean isIFrame(byte[] videoBuffer) {
        if (videoBuffer.length < 11) {
            return false;
        }
        int index = 10;
        while ((videoBuffer[index] & 31) == 9) {
            index += 6;
        }
        if ((videoBuffer[index] & 31) == 7) {
            return true;
        }
        return false;
    }

    public void onH264FrameInput(byte[] videoBuffer, int size, long frameIndex, boolean isKeyFrame) {
        try {
            if (this.currentStatus == RecorderBase.RecorderStatus.RECORDING) {
                boolean isIFrame = isIFrame(videoBuffer);
                if (this.start) {
                    if (!isIFrame) {
                        this.numFrameJumped++;
                        return;
                    }
                    this.videoRecordInfo.setFrameJumpped(this.numFrameJumped);
                    this.start = false;
                    MediaFormat format = MediaFormat.createVideoFormat(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0], 1280, 720);
                    format.setInteger("frame-rate", 30);
                    format.setByteBuffer("csd-0", ByteBuffer.wrap(videoBuffer, 6, 38));
                    format.setByteBuffer("csd-1", ByteBuffer.wrap(videoBuffer, 44, 8));
                    this.muxer.addTrack(format);
                    this.muxer.start();
                }
                try {
                    syncPTS();
                    if (this.writeH264File && this.h264BufferedOutputStream != null) {
                        this.h264BufferedOutputStream.write(videoBuffer, 0, size);
                        if (this.numFrameWritten > 0 && this.numFrameWritten % 15 == 0) {
                            this.h264BufferedOutputStream.flush();
                        }
                    }
                    this.info.offset = 0;
                    this.info.size = size;
                    this.info.presentationTimeUs = (long) ((int) (((double) (this.numFrameWritten + 1)) * DJIVideoUtil.getDurationPerFrame() * 1000.0d));
                    this.info.flags = 0;
                    if (isIFrame) {
                        this.info.flags |= 2;
                        this.info.flags |= 1;
                    }
                    this.muxer.writeSampleData(0, ByteBuffer.wrap(videoBuffer, 0, size), this.info);
                    this.numFrameWritten++;
                } catch (Exception e) {
                    Log.e(TAG, "error when writing H264 frames to File");
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            MediaLogger.show(e2);
        }
    }

    public void attachListenerToUpstream() {
        DJIVideoDataRecver.getInstance().setH264FrameListener(true, this);
    }

    public void detachListenerToUpstream() {
        DJIVideoDataRecver.getInstance().setH264FrameListener(true, null);
    }

    private void closeOrDeleteFile() {
        try {
            if (this.muxer != null) {
                this.muxer.stop();
                this.muxer.release();
                this.muxer = null;
                addToMediaLibrary(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4");
            }
            Log.i(TAG, "muxer has been closed");
        } catch (Exception e) {
            Log.e(TAG, "error when closing muxer");
            e.printStackTrace();
        }
        if (this.writeH264File) {
            try {
                if (this.h264BufferedOutputStream != null) {
                    this.h264BufferedOutputStream.close();
                    this.h264BufferedOutputStream = null;
                }
                if (this.h264OutputStream != null) {
                    this.h264OutputStream.close();
                    this.h264OutputStream = null;
                }
                Log.i(TAG, "H264 file has been closed");
            } catch (Exception e2) {
                Log.e(TAG, "error when closing H264 file");
                e2.printStackTrace();
            }
        }
        if (this.numFrameWritten < 30) {
            Log.i(TAG, "need to delete the related file because it has fewer frames than the threshold");
            if (new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4").delete()) {
                Log.i(TAG, "has deleted mp4 file");
            } else {
                Log.e(TAG, "failed to delete the short mp4 file");
            }
            if (this.writeH264File) {
                if (new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".h264").delete()) {
                    Log.i(TAG, "has deleted h264 file");
                } else {
                    Log.e(TAG, "failed to delete the short h264 file");
                }
            }
            if (new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".info").delete()) {
                Log.i(TAG, "has deleted the .info file");
            } else {
                Log.e(TAG, "failed to delete the .info file");
            }
        }
    }

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        try {
            activeGOP();
            initPTSSync();
            setMainFileName();
            createFile();
            this.numFrameWritten = 0;
            this.numFrameJumped = 0;
            startRecordVideoInfo();
            attachListenerToUpstream();
        } catch (Exception e) {
            MediaLogger.show(e);
        }
    }

    private void activeGOP() {
        DataDm368SetParams dm368 = new DataDm368SetParams();
        dm368.set(DataDm368SetParams.DM368CmdId.SetEncodeFormat, 1);
        this.cmdStartTime = System.currentTimeMillis();
        this.cmdSuccess = false;
        dm368.start(new DJIDataCallBack() {
            /* class dji.midware.media.record.RecorderGop.AnonymousClass1 */

            public void onSuccess(Object model) {
                boolean unused = RecorderGop.this.cmdSuccess = true;
                long unused2 = RecorderGop.this.cmdOpDelay = System.currentTimeMillis() - RecorderGop.this.cmdStartTime;
                MediaLogger.show("Gop Activation success. Delay: " + RecorderGop.this.cmdOpDelay);
                synchronized (RecorderGop.this.cmdSync) {
                    RecorderGop.this.cmdSync.notify();
                }
            }

            public void onFailure(Ccode ccode) {
                boolean unused = RecorderGop.this.cmdSuccess = false;
                MediaLogger.show(new Exception(ccode.toString()));
                synchronized (RecorderGop.this.cmdSync) {
                    RecorderGop.this.cmdSync.notify();
                }
            }
        });
        synchronized (this.cmdSync) {
            try {
                this.cmdSync.wait();
            } catch (InterruptedException e) {
                this.cmdSuccess = false;
                e.printStackTrace();
            }
        }
        this.cmdOpDelay = System.currentTimeMillis() - this.cmdStartTime;
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        try {
            detachListenerToUpstream();
            if (this.videoRecordInfoSetter != null) {
                this.videoRecordInfoSetter.setEndTimeMsec((int) (((double) this.numFrameWritten) * DJIVideoUtil.getDurationPerFrame()));
                this.videoRecordInfoSetter.onDestroy();
                this.videoRecordInfoSetter = null;
            }
            this.videoRecordInfo = null;
            closeOrDeleteFile();
        } catch (Exception e) {
            MediaLogger.show(e);
        }
    }
}
