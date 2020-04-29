package dji.midware.media.transcode.offline;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoRecordInfo;
import java.io.File;

@EXClassNullAway
public abstract class TranscoderBase implements TranscoderInterface {
    protected static boolean deleteH264AfterTranscode = true;
    protected int curProgress;
    protected VideoRecordInfo info = null;
    protected String inputFileName = "";
    protected TranscoderListener listener = null;
    protected Object listenerChangeSync = new Object();
    protected String outputFileName = "";
    protected TranscoderStatus status = TranscoderStatus.STANDBY;
    protected Object transcoderStatusSync = new Object();

    protected enum TranscoderStatus {
        STANDBY,
        TRANSCODING
    }

    /* access modifiers changed from: package-private */
    public abstract String getTAG();

    /* access modifiers changed from: protected */
    public abstract void onStart();

    /* access modifiers changed from: protected */
    public abstract void onStopByForce();

    public int getCurProgress() {
        return this.curProgress;
    }

    public String getInputFileName() {
        return this.inputFileName;
    }

    /* access modifiers changed from: protected */
    public void saveInfoFile() {
        this.info.setFrameJumpped(120);
        this.info.store(this.inputFileName.replace(".h264", ".info"));
    }

    /* access modifiers changed from: protected */
    public void addToMediaLibrary() {
        try {
            Uri contentUri = Uri.fromFile(new File(this.outputFileName));
            ServiceManager.getInstance();
            Context context = ServiceManager.getContext();
            if (Build.VERSION.SDK_INT >= 19) {
                Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", contentUri));
            }
        } catch (Exception e) {
            MediaLogger.e(getTAG(), e);
        }
        MediaLogger.show("have add " + this.outputFileName + " into the library");
    }

    /* access modifiers changed from: protected */
    public void initVideoInfo(String inputFileName2, String outputFileName2) {
        this.inputFileName = inputFileName2;
        this.outputFileName = outputFileName2;
        this.info = new VideoRecordInfo();
        try {
            this.info.load(inputFileName2.replaceAll(".h264", ".info"));
        } catch (Exception e1) {
            MediaLogger.show(e1);
            synchronized (this.listenerChangeSync) {
                if (this.listener != null) {
                    this.listener.onFailure(e1);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void deinitVideoInfo() {
        this.info = null;
    }

    /* access modifiers changed from: protected */
    public void tryDeleteH264File() {
        if (!deleteH264AfterTranscode) {
            return;
        }
        if (!Boolean.valueOf(new File(this.inputFileName).delete()).booleanValue()) {
            Log.e(getTAG(), "H264 File not deleted");
        } else {
            Log.i(getTAG(), "H264 File has been deleted");
        }
    }

    public void start(String inputFileName2, String outputFileName2, TranscoderListener listener2) {
        synchronized (this.transcoderStatusSync) {
            if (this.status == TranscoderStatus.STANDBY) {
                MediaLogger.show(getTAG(), "status=" + this.status + " event=START");
                this.status = TranscoderStatus.TRANSCODING;
                initVideoInfo(inputFileName2, outputFileName2);
                synchronized (this.listenerChangeSync) {
                    this.listener = listener2;
                }
                onStart();
            }
        }
    }

    public void stop() {
        synchronized (this.transcoderStatusSync) {
            if (this.status == TranscoderStatus.TRANSCODING) {
                MediaLogger.show(getTAG(), "status=" + this.status + " event=STOP");
                onStopByForce();
                this.status = TranscoderStatus.STANDBY;
            }
        }
        synchronized (this.listenerChangeSync) {
            if (this.listener == null) {
                MediaLogger.show("Transcoder is stopped when UI is inactive");
            } else {
                MediaLogger.show("Transcoder is stopped when UI is active");
            }
        }
    }

    public void onDestroy() {
        MediaLogger.show(getTAG(), "event=DESTROY");
        stop();
    }

    public void rebindListener(TranscoderListener listener2) {
        synchronized (this.listenerChangeSync) {
            this.listener = listener2;
        }
    }

    public boolean isTranscoding() {
        return this.status == TranscoderStatus.TRANSCODING;
    }

    /* access modifiers changed from: protected */
    public void tryDeleteThumbFile() {
        if (this.inputFileName != null) {
            new File(this.inputFileName.replace(".h264", ".jpg")).delete();
        }
    }
}
