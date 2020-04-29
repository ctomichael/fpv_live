package dji.midware.media.transcode.offline;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.transcode.offline.TranscoderBase;
import java.io.File;

@EXClassNullAway
public class TranscoderFFmpeg extends TranscoderBase implements TranscoderInterface {
    public static String TAG = "TranscoderFFmpeg";
    /* access modifiers changed from: private */
    public Exception error = null;
    ThreadFFMpegMonitor ffMpegMonitor = null;
    boolean monitorRunning = false;
    TranscodeThread transcodeThread = null;

    public void onStopByForce() {
        this.monitorRunning = false;
        TranscoderManager.ffmpegTools.h264ToMp4ConvertorStop();
        File f = new File(this.outputFileName);
        if (!f.exists()) {
            return;
        }
        if (!Boolean.valueOf(f.delete()).booleanValue()) {
            Log.e(TAG, "mp4 File not deleted");
        } else {
            Log.i(TAG, "mp4 File has been deleted");
        }
    }

    public void onStart() {
        super.initVideoInfo(this.inputFileName, this.outputFileName);
        this.monitorRunning = true;
        synchronized (this.listenerChangeSync) {
            if (this.listener != null) {
                this.listener.onStart();
            }
        }
        this.transcodeThread = new TranscodeThread();
        this.transcodeThread.start();
        this.ffMpegMonitor = new ThreadFFMpegMonitor();
        this.ffMpegMonitor.start();
    }

    private class ThreadFFMpegMonitor extends Thread {
        private ThreadFFMpegMonitor() {
        }

        public void run() {
            int progressPercent;
            double totalTime = (((double) (TranscoderFFmpeg.this.info.getEndTimeMsec() - TranscoderFFmpeg.this.info.getStartTimeMsec())) - (120.0d * DJIVideoUtil.getDurationPerFrame())) / 1000.0d;
            while (TranscoderFFmpeg.this.transcodeThread.isAlive()) {
                if (totalTime == 0.0d) {
                    progressPercent = 100;
                } else {
                    progressPercent = (int) ((TranscoderManager.ffmpegTools.h264ToMp4ConvertorGetProgress() / totalTime) * 100.0d);
                }
                if (progressPercent < 0 || progressPercent > 100) {
                    MediaLogger.show("progress num error: " + progressPercent);
                } else {
                    synchronized (TranscoderFFmpeg.this.listenerChangeSync) {
                        if (TranscoderFFmpeg.this.listener != null) {
                            TranscoderFFmpeg.this.listener.onProgress(progressPercent);
                        }
                    }
                    MediaLogger.show("progress: " + progressPercent);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    if (TranscoderFFmpeg.this.monitorRunning) {
                        return;
                    }
                }
            }
            if (TranscoderFFmpeg.this.error != null) {
                synchronized (TranscoderFFmpeg.this.listenerChangeSync) {
                    if (TranscoderFFmpeg.this.listener != null) {
                        TranscoderFFmpeg.this.listener.onFailure(TranscoderFFmpeg.this.error);
                    }
                }
                return;
            }
            synchronized (TranscoderFFmpeg.this.listenerChangeSync) {
                if (TranscoderFFmpeg.this.listener != null) {
                    TranscoderFFmpeg.this.listener.onProgress(100);
                }
            }
            synchronized (TranscoderFFmpeg.this.transcoderStatusSync) {
                if (TranscoderFFmpeg.this.status == TranscoderBase.TranscoderStatus.TRANSCODING) {
                    TranscoderFFmpeg.this.tryDeleteH264File();
                    TranscoderFFmpeg.this.tryDeleteThumbFile();
                    TranscoderFFmpeg.this.addToMediaLibrary();
                    TranscoderFFmpeg.this.status = TranscoderBase.TranscoderStatus.STANDBY;
                }
            }
            synchronized (TranscoderFFmpeg.this.listenerChangeSync) {
                if (TranscoderFFmpeg.this.listener != null) {
                    TranscoderFFmpeg.this.listener.onSuccess();
                }
            }
        }
    }

    private class TranscodeThread extends Thread {
        private TranscodeThread() {
        }

        public void run() {
            try {
                TranscoderManager.ffmpegTools.h264ToMp4ConvertorInit();
                TranscoderManager.ffmpegTools.h264ToMp4ConvertorStart(TranscoderFFmpeg.this.inputFileName, TranscoderFFmpeg.this.outputFileName, 120);
            } catch (Exception e) {
                Exception unused = TranscoderFFmpeg.this.error = e;
                MediaLogger.show(e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String getTAG() {
        return TAG;
    }
}
