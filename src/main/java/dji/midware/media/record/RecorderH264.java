package dji.midware.media.record;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.record.RecorderBase;
import dji.midware.media.record.RecorderManager;
import dji.midware.util.DJIEventBusUtil;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class RecorderH264 extends RecorderBase implements RecorderInterface, H264FrameListener {
    private static String TAG = "H264Recorder";
    private static RecorderH264 instance = null;
    private BufferedOutputStream h264BufferedOutputStream = null;
    private BufferedOutputStream h264IndexBos = null;
    private OutputStream h264IndexFos = null;
    private OutputStream h264OutputStream = null;

    public static synchronized RecorderH264 getInstance() {
        RecorderH264 recorderH264;
        synchronized (RecorderH264.class) {
            if (instance == null) {
                instance = new RecorderH264();
                DJIEventBusUtil.register(instance);
            }
            recorderH264 = instance;
        }
        return recorderH264;
    }

    public static synchronized void destroy() {
        synchronized (RecorderH264.class) {
            MediaLogger.show("RecorderH264 is destroyed");
            if (instance != null) {
                instance.onDestroy();
                instance = null;
            }
        }
    }

    private RecorderH264() {
        Log.i(TAG, "An instance is created");
    }

    private void createFile() {
        try {
            this.h264OutputStream = new FileOutputStream(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".h264");
            if (this.h264OutputStream != null) {
                this.h264BufferedOutputStream = new BufferedOutputStream(this.h264OutputStream);
                Log.i(TAG, "An H264 File has been opened");
            } else {
                Log.e(TAG, "error in creating H264 File");
            }
            this.h264IndexFos = new FileOutputStream(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".index");
            if (this.h264IndexFos != null) {
                this.h264IndexBos = new BufferedOutputStream(this.h264IndexFos);
                Log.i(TAG, "An H264 Index File has been opened");
                return;
            }
            Log.e(TAG, "error in creating H264 INdex File");
        } catch (IOException e) {
            Log.e(TAG, "error in creating file " + this.mainFileName);
            e.printStackTrace();
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
            if (this.h264BufferedOutputStream != null) {
                this.h264BufferedOutputStream.close();
                this.h264BufferedOutputStream = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.h264OutputStream != null) {
                this.h264OutputStream.close();
                this.h264OutputStream = null;
            }
            Log.i(TAG, "H264 file has been closed");
        } catch (Exception e2) {
            Log.e(TAG, "error when closing H264 file");
            e2.printStackTrace();
        }
        try {
            if (this.h264IndexBos != null) {
                this.h264IndexBos.close();
                this.h264IndexBos = null;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            if (this.h264IndexFos != null) {
                this.h264IndexFos.close();
                this.h264IndexFos = null;
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        if (this.numFrameWritten <= 150) {
            Log.i(TAG, "need to delete the related file because it has fewer frames than the threshold");
            if (new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".h264").delete()) {
                Log.i(TAG, "has deleted h264 file");
            } else {
                Log.e(TAG, "failed to delete the short h264 file");
            }
            if (new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".jpg").delete()) {
                Log.i(TAG, "has deleted the thumb file");
            } else {
                Log.e(TAG, "failed to delete the thumb file");
            }
            if (new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".info").delete()) {
                Log.i(TAG, "has deleted the .info file");
            } else {
                Log.e(TAG, "failed to delete the .info file");
            }
            if (new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".index").delete()) {
                Log.i(TAG, "has deleted the .index file");
            } else {
                Log.e(TAG, "failed to delete the .index file");
            }
        }
    }

    public void onH264FrameInput(byte[] videoBuffer, int size, long pts, boolean isKeyFrame) {
        if (this.currentStatus == RecorderBase.RecorderStatus.RECORDING) {
            try {
                if (this.h264BufferedOutputStream != null) {
                    syncPTS();
                    this.h264BufferedOutputStream.write(videoBuffer, 0, size);
                    this.h264IndexBos.write(new String("size=" + size + " pts=" + pts + " system_time=" + System.currentTimeMillis() + "\n").getBytes());
                    this.numFrameWritten++;
                    if (this.numFrameWritten % 15 == 0) {
                        this.h264BufferedOutputStream.flush();
                        this.h264IndexBos.flush();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "error when writing H264 frames to File");
                e.printStackTrace();
            }
        }
    }

    private void addIFrame() throws IOException {
        int iframeId = DJIVideoDecoder.getIframeRawId(DJIProductManager.getInstance().getType(), ServiceManager.getInstance().getDecoder().width, ServiceManager.getInstance().getDecoder().height);
        if (iframeId >= 0) {
            ServiceManager.getInstance();
            InputStream inputStream = ServiceManager.getContext().getResources().openRawResource(iframeId);
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            if (this.h264BufferedOutputStream != null) {
                this.h264BufferedOutputStream.write(buffer, 0, length);
            }
            inputStream.close();
        }
    }

    /* access modifiers changed from: protected */
    public void onEndRecord() {
        detachListenerToUpstream();
        endRecordVideoInfo();
        closeOrDeleteFile();
    }

    /* access modifiers changed from: protected */
    public void onStartRecord() {
        initPTSSync();
        setMainFileName();
        createFile();
        this.numFrameWritten = 0;
        startRecordVideoInfo();
        try {
            addIFrame();
            this.numFrameWritten++;
        } catch (IOException e) {
            MediaLogger.show(e);
        }
        attachListenerToUpstream();
        EventBus.getDefault().post(new RecorderManager.SurfaceSaveEvent(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".jpg"));
        Log.i(TAG, "have posted a bus event for saving a thumb");
    }

    /* access modifiers changed from: protected */
    public String getTAG() {
        return TAG;
    }
}
