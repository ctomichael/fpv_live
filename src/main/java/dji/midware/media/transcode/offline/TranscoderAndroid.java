package dji.midware.media.transcode.offline;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.colors.ColorFormatConvFactory;
import dji.midware.media.transcode.offline.H264FileLoader;
import dji.midware.natives.FPVController;
import java.io.File;

@EXClassNullAway
@TargetApi(18)
public class TranscoderAndroid extends TranscoderBase implements TranscoderInterface, Runnable {
    public static String TAG = "TranscoderAndroid";
    private EncoderMuxer encoderMuxer = null;
    private H264FileLoader h264FileLoader = null;
    public OfflineDecoder offlineDecoder = null;

    /* access modifiers changed from: protected */
    public void onStopByForce() {
        try {
            releaseComponents();
            Log.i(TranscoderManager.TAG, "stop stage 5: " + System.currentTimeMillis());
            deleteTmpOutputFile();
            Log.i(TranscoderManager.TAG, "stop stage 6: " + System.currentTimeMillis());
            Log.i(TAG, "Transcoder has been successfully stopped by the user");
        } catch (Exception e) {
            MediaLogger.eToView(TAG, e);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        new Thread(this).start();
    }

    private void deleteTmpOutputFile() {
        File f = new File(this.outputFileName + ".tmp");
        if (!f.exists()) {
            return;
        }
        if (!Boolean.valueOf(f.delete()).booleanValue()) {
            Log.e(TAG, "Temp output File not deleted");
        } else {
            Log.i(TAG, "Temp output File has been deleted");
        }
    }

    private boolean changeFileName() {
        boolean success = false;
        File file = new File(this.outputFileName + ".tmp");
        File file2 = new File(this.outputFileName);
        if (file2.exists()) {
            Log.e(TAG, this.outputFileName + " has already existed");
        } else {
            for (int i = 0; i < 5 && !success; i++) {
                success = file.renameTo(file2);
                if (!success) {
                    try {
                        Log.e(TAG, "waiting 1 more second and try renaming file at the " + i + " time");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }
        if (success) {
            MediaLogger.show("rename file success");
        } else {
            MediaLogger.show("rename file fails");
        }
        return success;
    }

    private boolean prepareFile() {
        if (!new File(this.inputFileName).exists()) {
            Exception e = new Exception("input file " + this.inputFileName + "not exist");
            synchronized (this.listenerChangeSync) {
                if (this.listener != null) {
                    this.listener.onFailure(e);
                }
            }
            MediaLogger.show(e);
            return false;
        }
        File outputFile = new File(this.outputFileName);
        if (!outputFile.exists() || outputFile.delete()) {
            File tempFile = new File(this.outputFileName + ".tmp");
            if (!tempFile.exists() || tempFile.delete()) {
                return true;
            }
            Exception e2 = new Exception("the temp file exists and it can't be deleted. ");
            synchronized (this.listenerChangeSync) {
                if (this.listener != null) {
                    this.listener.onFailure(e2);
                }
            }
            MediaLogger.show(e2);
            return false;
        }
        Exception e3 = new Exception("the output file exists and it can't be deleted.");
        synchronized (this.listenerChangeSync) {
            if (this.listener != null) {
                this.listener.onFailure(e3);
            }
        }
        MediaLogger.show(e3);
        return false;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r5 = this;
            java.lang.String r1 = dji.midware.media.transcode.offline.TranscoderAndroid.TAG     // Catch:{ Exception -> 0x0066 }
            java.lang.String r2 = "starting"
            android.util.Log.i(r1, r2)     // Catch:{ Exception -> 0x0066 }
            r5.stopLiveView()     // Catch:{ Exception -> 0x0066 }
            boolean r1 = r5.prepareFile()     // Catch:{ Exception -> 0x0066 }
            if (r1 != 0) goto L_0x0012
        L_0x0011:
            return
        L_0x0012:
            java.lang.Object r2 = r5.listenerChangeSync     // Catch:{ Exception -> 0x0066 }
            monitor-enter(r2)     // Catch:{ Exception -> 0x0066 }
            dji.midware.media.transcode.offline.TranscoderListener r1 = r5.listener     // Catch:{ all -> 0x007b }
            if (r1 == 0) goto L_0x001e
            dji.midware.media.transcode.offline.TranscoderListener r1 = r5.listener     // Catch:{ all -> 0x007b }
            r1.onStart()     // Catch:{ all -> 0x007b }
        L_0x001e:
            monitor-exit(r2)     // Catch:{ all -> 0x007b }
            r5.initComponents()     // Catch:{ Exception -> 0x0066 }
            dji.midware.media.transcode.offline.H264FileLoader r1 = r5.h264FileLoader     // Catch:{ Exception -> 0x0066 }
            r1.join()     // Catch:{ Exception -> 0x0066 }
            java.lang.Object r2 = r5.transcoderStatusSync     // Catch:{ Exception -> 0x0066 }
            monitor-enter(r2)     // Catch:{ Exception -> 0x0066 }
            dji.midware.media.transcode.offline.TranscoderBase$TranscoderStatus r1 = r5.status     // Catch:{ all -> 0x0063 }
            dji.midware.media.transcode.offline.TranscoderBase$TranscoderStatus r3 = dji.midware.media.transcode.offline.TranscoderBase.TranscoderStatus.TRANSCODING     // Catch:{ all -> 0x0063 }
            if (r1 != r3) goto L_0x0061
            java.lang.String r1 = dji.midware.media.transcode.offline.TranscoderAndroid.TAG     // Catch:{ all -> 0x0063 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0063 }
            r3.<init>()     // Catch:{ all -> 0x0063 }
            java.lang.String r4 = "status="
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x0063 }
            dji.midware.media.transcode.offline.TranscoderBase$TranscoderStatus r4 = r5.status     // Catch:{ all -> 0x0063 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x0063 }
            java.lang.String r4 = " event=COMPLETION"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x0063 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0063 }
            dji.midware.media.MediaLogger.show(r1, r3)     // Catch:{ all -> 0x0063 }
            r5.onComplete()     // Catch:{ all -> 0x0063 }
            java.lang.String r1 = dji.midware.media.transcode.offline.TranscoderAndroid.TAG     // Catch:{ all -> 0x0063 }
            java.lang.String r3 = "Offline Transcoder has been successfully ended due to completion"
            android.util.Log.i(r1, r3)     // Catch:{ all -> 0x0063 }
            dji.midware.media.transcode.offline.TranscoderBase$TranscoderStatus r1 = dji.midware.media.transcode.offline.TranscoderBase.TranscoderStatus.STANDBY     // Catch:{ all -> 0x0063 }
            r5.status = r1     // Catch:{ all -> 0x0063 }
        L_0x0061:
            monitor-exit(r2)     // Catch:{ all -> 0x0063 }
            goto L_0x0011
        L_0x0063:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0063 }
            throw r1     // Catch:{ Exception -> 0x0066 }
        L_0x0066:
            r0 = move-exception
            dji.midware.media.MediaLogger.show(r0)
            java.lang.Object r2 = r5.listenerChangeSync
            monitor-enter(r2)
            dji.midware.media.transcode.offline.TranscoderListener r1 = r5.listener     // Catch:{ all -> 0x0078 }
            if (r1 == 0) goto L_0x0076
            dji.midware.media.transcode.offline.TranscoderListener r1 = r5.listener     // Catch:{ all -> 0x0078 }
            r1.onFailure(r0)     // Catch:{ all -> 0x0078 }
        L_0x0076:
            monitor-exit(r2)     // Catch:{ all -> 0x0078 }
            goto L_0x0011
        L_0x0078:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0078 }
            throw r1
        L_0x007b:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x007b }
            throw r1     // Catch:{ Exception -> 0x0066 }
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.transcode.offline.TranscoderAndroid.run():void");
    }

    private void initComponents() {
        MediaCodec encoder = null;
        try {
            encoder = MediaCodec.createEncoderByType(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaCodec decoder = null;
        try {
            decoder = MediaCodec.createDecoderByType(DJIVideoUtil.VIDEO_ENCODING_FORMAT[0]);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        int deColorFormat = DJIVideoUtil.findBestColorFormat(encoder, decoder);
        int enColorFormat = deColorFormat;
        if (deColorFormat == -1) {
            deColorFormat = DJIVideoUtil.findBestColorFormat(decoder);
        }
        if (enColorFormat == -1) {
            enColorFormat = DJIVideoUtil.findBestColorFormat(encoder);
        }
        this.encoderMuxer = new EncoderMuxer();
        this.encoderMuxer.setOutputFileName(this.outputFileName + ".tmp");
        this.encoderMuxer.setFrameJumped(120);
        this.encoderMuxer.start(encoder, enColorFormat, this.info.getPixelXDimension_app(), this.info.getPixelYDimension_app());
        if (enColorFormat != deColorFormat) {
            this.encoderMuxer.setColorFormatConv(ColorFormatConvFactory.createColorFormatConv(deColorFormat, enColorFormat));
        }
        this.offlineDecoder = new OfflineDecoder();
        this.offlineDecoder.setVideoDataListener(this.encoderMuxer);
        this.offlineDecoder.start(decoder, deColorFormat, this.info.getPixelXDimension_app(), this.info.getPixelYDimension_app());
        DJIVideoDataRecver.getInstance().setH264FrameListener(true, this.offlineDecoder);
        this.h264FileLoader = new H264FileLoader();
        this.h264FileLoader.setProgressListener(new H264FileLoader.ProgressListener() {
            /* class dji.midware.media.transcode.offline.TranscoderAndroid.AnonymousClass1 */

            public void onProgress(int progress) {
                TranscoderAndroid.this.curProgress = progress;
                Log.i(TranscoderAndroid.TAG, "progress: " + progress);
                synchronized (TranscoderAndroid.this.listenerChangeSync) {
                    if (TranscoderAndroid.this.listener != null) {
                        TranscoderAndroid.this.listener.onProgress(progress);
                    }
                }
            }
        });
        this.h264FileLoader.LoadFile(this.inputFileName);
    }

    private void stopLiveView() {
        ServiceManager.getInstance().pauseRecvThread();
        FPVController.native_clear();
        while (FPVController.native_getQueueSize() != 0) {
            try {
                Log.i(TAG, "waiting for stopping video stream from the drone");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void releaseComponents() {
        Log.i(TranscoderManager.TAG, "stop stage 1: " + System.currentTimeMillis());
        if (this.h264FileLoader != null) {
            this.h264FileLoader.stop();
            this.h264FileLoader = null;
        }
        Log.i(TAG, "H264 File loader has stopped");
        Log.i(TranscoderManager.TAG, "stop stage 2: " + System.currentTimeMillis());
        DJIVideoDataRecver.getInstance().setH264FrameListener(true, null);
        Log.i(TAG, "parser has stopped");
        ServiceManager.getInstance().resumeRecvThread();
        Log.i(TAG, "has asked ServiceManager to resume recv thread");
        Log.i(TranscoderManager.TAG, "stop stage 3: " + System.currentTimeMillis());
        if (this.offlineDecoder != null) {
            this.offlineDecoder.stopOutputMonitor();
            this.offlineDecoder.stopAndReleaseDecoder();
            this.offlineDecoder = null;
        }
        Log.i(TAG, "offlineDecoderhas stopped");
        Log.i(TranscoderManager.TAG, "stop stage 4: " + System.currentTimeMillis());
        if (this.encoderMuxer != null) {
            this.encoderMuxer.stopOutputMonitor();
            this.encoderMuxer.stopAndReleaseEncoderMuxer();
            this.encoderMuxer = null;
        }
        Log.i(TAG, "encoderMuxer has stopped");
        deinitVideoInfo();
        Log.i(TAG, "Video Info is set to be null");
    }

    private void onComplete() {
        while (FPVController.native_getQueueSize() != 0) {
            Log.i(TAG, "the queue size of FPVController is " + FPVController.native_getQueueSize());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Log.e(TAG, "sleeping thread is interrupted when waiting for the FPVController to flush its queue");
            }
        }
        saveInfoFile();
        releaseComponents();
        synchronized (this.listenerChangeSync) {
            if (this.listener != null) {
                this.listener.onProgress(100);
            }
        }
        if (changeFileName()) {
            tryDeleteH264File();
            tryDeleteThumbFile();
            addToMediaLibrary();
        } else {
            MediaLogger.show("change file name failure");
        }
        synchronized (this.listenerChangeSync) {
            if (this.listener != null) {
                this.listener.onSuccess();
                MediaLogger.show("Background transcode", "transcoding completion when UI is active");
            } else {
                MediaLogger.show("Background transcode", "transcoding completion when UI is Inactive");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String getTAG() {
        return TAG;
    }
}
