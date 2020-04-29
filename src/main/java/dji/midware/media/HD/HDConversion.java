package dji.midware.media.HD;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.manager.litchis.DJIFileType;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraControlTransCode;
import dji.midware.data.model.P3.DataCameraGetFileParams;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraSetMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoRecordInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

@EXClassNullAway
public class HDConversion {
    private static final boolean DEBUG = true;
    private static final long DOWNLOAD_TIME_OUT = 5000;
    private static final String TAG = "HDConversion";
    private static HDConversion instance = null;
    CallBackGetHD callback;
    DJIClipFile clipFile;
    DJIClipFileLoader clipFileLoader = new DJIClipFileLoader();
    DJIClipInfoListLoader clipListLoader = new DJIClipInfoListLoader();
    DJIClipInfoList clipListState;
    int commitID;
    /* access modifiers changed from: private */
    public Controller controller;
    DataCameraControlTransCode ctrTransCode = DataCameraControlTransCode.getInstance();
    DataCameraGetFileParams dcGetFileParams = DataCameraGetFileParams.getInstance();
    int downloadingSegmentIndex;
    long lastbyteRecievedTime;
    int numFinishedDownload;
    int numFinishedError;
    int numPendingTranscodeResult;
    DataCameraGetMode.MODE originMode;
    int querySourceFileID;
    /* access modifiers changed from: private */
    public long[] segmentReceivedBytes;
    /* access modifiers changed from: private */
    public SegmentStatus[] segmentStatus;
    /* access modifiers changed from: private */
    public long[] segmentTotalBytes;
    /* access modifiers changed from: private */
    public State state = State.Uninitialized;
    String targetDirectory;
    /* access modifiers changed from: private */
    public HandlerThread thread = new HandlerThread("HDConversionThread");
    Vector<VideoRecordInfo> vecSegmentMix;

    public interface CallBackGetHD {
        void onFail(Exception exc);

        void onProgress(int i);

        void onStart();

        void onSuccess();
    }

    private enum SegmentStatus {
        PendingTranscodeResult,
        ToDownload,
        HasDownloaded,
        Failure
    }

    private enum ErrorType {
        TimeOut,
        Disconnected,
        INVALID_PARAM_OR_ERR_UNSPECIFIED,
        DownloadTimeOut
    }

    public enum State {
        Uninitialized,
        Started,
        SentGetOriginalMode,
        SentSwithToTranscode,
        SentGetFileParams,
        SentRequestTranscoding,
        SentQueryTranscodeStatus,
        WaitForDownloading,
        AllFileDownloaded,
        SentSwitchBack,
        Success,
        Error
    }

    public HDConversion() {
        this.thread.start();
        MediaLogger.i(TAG, "thread started");
        this.controller = new Controller(this.thread.getLooper());
    }

    public static class EventType {
        static final int AnalyzeTranscodeStatus = 6;
        static final int GetFileParams = 3;
        static final int GetOriginalMode = 1;
        static final int NUM_ACTIONTYPE = 11;
        static final int OnError = 8;
        static final int OnSuccess = 10;
        static final int QueryTranscodeStatus = 5;
        static final int RequestTranscoding = 4;
        static final int RestoreToOriginalMode = 7;
        static final int Start = 0;
        static final int SwitchToTranscode = 2;
        static final int WaitForDownloading = 9;

        public static String getName(int value) {
            switch (value) {
                case 0:
                    return "Start";
                case 1:
                    return "GetOriginalMode";
                case 2:
                    return "SwitchToTranscoding";
                case 3:
                    return "GetFileParams";
                case 4:
                    return "RequestTranscoding";
                case 5:
                    return "QueryTranscodingStatus";
                case 6:
                    return "AnalyzeTranscodeStatus";
                case 7:
                    return "RestoreToOriginalMode";
                case 8:
                    return "OnError";
                case 9:
                    return "WaitForDownloading";
                case 10:
                    return "OnSuccess";
                default:
                    return "Unrecognised";
            }
        }
    }

    public static class Action {
        public final long param;
        public final Object param2;
        public final int type;

        public Action(int type2, long param3) {
            this.type = type2;
            this.param = param3;
            this.param2 = null;
        }

        public Action(int type2, long param3, Object param22) {
            this.type = type2;
            this.param = param3;
            this.param2 = param22;
        }

        public int what() {
            return this.type;
        }
    }

    private class Controller extends Handler {
        private Action act = null;
        private boolean processed = true;

        public Controller(Looper looper) {
            super(looper);
        }

        /* access modifiers changed from: private */
        public void asyncCommand(int actionType) {
            Action act2 = new Action(actionType, 0, null);
            HDConversion.this.controller.sendMessage(HDConversion.this.controller.obtainMessage(act2.what(), act2));
            MediaLogger.i(true, HDConversion.TAG, "Signal to Controller. CMD: " + EventType.getName(actionType));
        }

        /* access modifiers changed from: private */
        public void asyncCommand(int actionType, long param1, Object param2) {
            Action act2 = new Action(actionType, param1, param2);
            HDConversion.this.controller.sendMessage(HDConversion.this.controller.obtainMessage(act2.what(), act2));
            MediaLogger.i(true, HDConversion.TAG, "Signal to Controller. CMD: " + EventType.getName(actionType) + " param 1=" + param1 + " param2=" + param2);
        }

        /* access modifiers changed from: private */
        public void asyncCommandDelayed(int actionType, long delayMs, long param1, Object param2) {
            Action act2 = new Action(actionType, param1, param2);
            HDConversion.this.controller.sendMessageDelayed(HDConversion.this.controller.obtainMessage(act2.what(), act2), delayMs);
            MediaLogger.i(true, HDConversion.TAG, "Signal to Controller. CMD: " + EventType.getName(actionType) + " param 1=" + param1 + " param2=" + param2);
        }

        private void asyncCommandDelayedAbsolute(int actionType, long delayMs, long param1, Object param2) {
            Action act2 = new Action(actionType, param1, param2);
            HDConversion.this.controller.sendMessageAtTime(HDConversion.this.controller.obtainMessage(act2.what(), act2), delayMs);
            MediaLogger.i(true, HDConversion.TAG, "Signal to Controller. CMD: " + EventType.getName(actionType) + " param 1=" + param1 + " param2=" + param2);
        }

        /* JADX WARNING: Removed duplicated region for block: B:7:0x004f A[Catch:{ Exception -> 0x0098, all -> 0x00b9 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r6) {
            /*
                r5 = this;
                java.lang.Object r1 = r6.obj
                dji.midware.media.HD.HDConversion$Action r1 = (dji.midware.media.HD.HDConversion.Action) r1
                dji.midware.media.HD.HDConversion$Action r1 = (dji.midware.media.HD.HDConversion.Action) r1
                r5.act = r1
                r1 = 1
                java.lang.String r2 = "HDConversion"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0098 }
                r3.<init>()     // Catch:{ Exception -> 0x0098 }
                java.lang.String r4 = "Start to process CMD "
                java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$Action r4 = r5.act     // Catch:{ Exception -> 0x0098 }
                int r4 = r4.type     // Catch:{ Exception -> 0x0098 }
                java.lang.String r4 = dji.midware.media.HD.HDConversion.EventType.getName(r4)     // Catch:{ Exception -> 0x0098 }
                java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0098 }
                java.lang.String r4 = " at state "
                java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion r4 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r4 = r4.state     // Catch:{ Exception -> 0x0098 }
                java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0098 }
                java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.MediaLogger.i(r1, r2, r3)     // Catch:{ Exception -> 0x0098 }
                r1 = 1
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$Action r1 = r5.act     // Catch:{ Exception -> 0x0098 }
                int r1 = r1.what()     // Catch:{ Exception -> 0x0098 }
                switch(r1) {
                    case 0: goto L_0x0088;
                    case 1: goto L_0x00c0;
                    case 2: goto L_0x00d6;
                    case 3: goto L_0x00ec;
                    case 4: goto L_0x010c;
                    case 5: goto L_0x0122;
                    case 6: goto L_0x0138;
                    case 7: goto L_0x0164;
                    case 8: goto L_0x0190;
                    case 9: goto L_0x014e;
                    case 10: goto L_0x017a;
                    default: goto L_0x0048;
                }     // Catch:{ Exception -> 0x0098 }
            L_0x0048:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
            L_0x004b:
                boolean r1 = r5.processed     // Catch:{ Exception -> 0x0098 }
                if (r1 != 0) goto L_0x0082
                java.lang.String r1 = "HDConversion"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0098 }
                r2.<init>()     // Catch:{ Exception -> 0x0098 }
                java.lang.String r3 = "Ignore CMD "
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$Action r3 = r5.act     // Catch:{ Exception -> 0x0098 }
                int r3 = r3.type     // Catch:{ Exception -> 0x0098 }
                java.lang.String r3 = dji.midware.media.HD.HDConversion.EventType.getName(r3)     // Catch:{ Exception -> 0x0098 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x0098 }
                java.lang.String r3 = " at state "
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion r3 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r3 = r3.state     // Catch:{ Exception -> 0x0098 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x0098 }
                java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.MediaLogger.e(r1, r2)     // Catch:{ Exception -> 0x0098 }
            L_0x0082:
                dji.midware.media.HD.HDConversion$Action r1 = r5.act
                r5.notifyCaller(r1)
            L_0x0087:
                return
            L_0x0088:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.Uninitialized     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x00b5
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doStart()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x0098:
                r0 = move-exception
                java.lang.String r1 = "HDConversion"
                dji.midware.media.MediaLogger.e(r1, r0)     // Catch:{ all -> 0x00b9 }
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ all -> 0x00b9 }
                dji.midware.media.HD.HDConversion$CallBackGetHD r1 = r1.callback     // Catch:{ all -> 0x00b9 }
                if (r1 == 0) goto L_0x00ac
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ all -> 0x00b9 }
                dji.midware.media.HD.HDConversion$CallBackGetHD r1 = r1.callback     // Catch:{ all -> 0x00b9 }
                r1.onFail(r0)     // Catch:{ all -> 0x00b9 }
            L_0x00ac:
                r5.release()     // Catch:{ all -> 0x00b9 }
                dji.midware.media.HD.HDConversion$Action r1 = r5.act
                r5.notifyCaller(r1)
                goto L_0x0087
            L_0x00b5:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x00b9:
                r1 = move-exception
                dji.midware.media.HD.HDConversion$Action r2 = r5.act
                r5.notifyCaller(r2)
                throw r1
            L_0x00c0:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.Started     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x00d1
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doGetOriginalMode()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x00d1:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x00d6:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.SentGetOriginalMode     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x00e7
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doSwitchToTranscode()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x00e7:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x00ec:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.SentGetOriginalMode     // Catch:{ Exception -> 0x0098 }
                if (r1 == r2) goto L_0x0100
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.SentSwithToTranscode     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x0107
            L_0x0100:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doGetFileParams()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x0107:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x010c:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.SentGetFileParams     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x011d
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doRequestTranscoding()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x011d:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x0122:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.SentRequestTranscoding     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x0133
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doQueryTranscodeStatus()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x0133:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x0138:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.SentQueryTranscodeStatus     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x0149
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doAnalyzeTranscodeStatus()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x0149:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x014e:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.WaitForDownloading     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x015f
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doWaitForDownloading()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x015f:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x0164:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.AllFileDownloaded     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x0175
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doRestoreToOriginalMode()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x0175:
                r1 = 0
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x017a:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r1 = r1.state     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$State r2 = dji.midware.media.HD.HDConversion.State.SentSwitchBack     // Catch:{ Exception -> 0x0098 }
                if (r1 != r2) goto L_0x018b
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                r1.doOnSuccess()     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x018b:
                r1 = 1
                r5.processed = r1     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            L_0x0190:
                dji.midware.media.HD.HDConversion r1 = dji.midware.media.HD.HDConversion.this     // Catch:{ Exception -> 0x0098 }
                dji.midware.media.HD.HDConversion$Action r2 = r5.act     // Catch:{ Exception -> 0x0098 }
                java.lang.Object r2 = r2.param2     // Catch:{ Exception -> 0x0098 }
                r1.doOnError(r2)     // Catch:{ Exception -> 0x0098 }
                goto L_0x004b
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.HD.HDConversion.Controller.handleMessage(android.os.Message):void");
        }

        @SuppressLint({"NewApi"})
        public void release() {
            removeCallbacksAndMessages(null);
            if (DJIVideoUtil.getSDKVersion() >= 18) {
                HDConversion.this.thread.quitSafely();
            } else {
                HDConversion.this.thread.quit();
            }
        }

        private void notifyCaller(Action act2) {
            synchronized (act2) {
                act2.notifyAll();
            }
        }
    }

    public static class HDSegment {
        private final int end;
        private final int start;

        public HDSegment(int start2, int end2) {
            this.start = start2;
            this.end = end2;
        }

        public int getStartTime() {
            return this.start;
        }

        public int getEndTime() {
            return this.end;
        }
    }

    public static HDConversion getInstance() {
        if (instance == null) {
            instance = new HDConversion();
        }
        return instance;
    }

    public void getHDSegments(Vector<VideoRecordInfo> vecSegmentMix2, String targetDirectory2, CallBackGetHD callback2) throws Exception {
        if (vecSegmentMix2 != null && vecSegmentMix2.size() != 0) {
            this.targetDirectory = targetDirectory2;
            this.callback = callback2;
            this.vecSegmentMix = vecSegmentMix2;
            this.controller.asyncCommand(0);
            try {
                this.thread.join();
            } catch (Exception e) {
                MediaLogger.e(TAG, e);
            }
            MediaLogger.d(TAG, "getHDSegments() done");
        }
    }

    /* access modifiers changed from: private */
    public void doRestoreToOriginalMode() {
        MediaLogger.i(TAG, "try to switch to mode " + this.originMode);
        DataCameraSetMode.getInstance().setMode(this.originMode).start(new DJIDataCallBack() {
            /* class dji.midware.media.HD.HDConversion.AnonymousClass1 */

            public void onSuccess(Object model) {
                MediaLogger.i(HDConversion.TAG, "have successfully switched to MODE: " + HDConversion.this.originMode);
                HDConversion.this.controller.asyncCommand(10);
            }

            public void onFailure(Ccode ccode) {
                HDConversion.this.controller.asyncCommand(8, 0, ccode);
            }
        });
        this.state = State.SentSwitchBack;
    }

    /* access modifiers changed from: private */
    public void doQueryTranscodeStatus() {
        if (ServiceManager.getInstance().isRemoteOK()) {
            this.clipListLoader.setGroupID(this.commitID);
            this.clipListLoader.setListener(new DJIAlbumInterface.DJIAlbumPullListener<DJIClipInfoList>() {
                /* class dji.midware.media.HD.HDConversion.AnonymousClass2 */

                public void onStart() {
                    MediaLogger.i(HDConversion.TAG, "clipListLoader onStart");
                }

                public void onRateUpdate(long total, long current, long persize) {
                    MediaLogger.i(HDConversion.TAG, "clipListLoader onRateUpdate: total=" + total + " current=" + current + " persize=" + persize);
                }

                public void onProgress(long total, long current) {
                    MediaLogger.i(HDConversion.TAG, "clipListLoader onProgress total=" + total + " current=" + current);
                }

                public void onSuccess(DJIClipInfoList data) {
                    MediaLogger.i(HDConversion.TAG, "clipListLoader onSuccess");
                    HDConversion.this.clipListState = data;
                    HDConversion.this.controller.asyncCommand(6);
                }

                public void onFailure(DJIAlbumPullErrorType error) {
                    MediaLogger.i(HDConversion.TAG, "clipListLoader: onFailure");
                    HDConversion.this.controller.asyncCommand(8, 0, error);
                }
            });
            this.clipListLoader.start();
            this.state = State.SentQueryTranscodeStatus;
            return;
        }
        this.controller.asyncCommand(8, 0, ErrorType.Disconnected);
    }

    /* access modifiers changed from: private */
    public void doAnalyzeTranscodeStatus() {
        DJIClipInfo clipInfo;
        if (this.clipListState != null) {
            MediaLogger.show(this.clipListState.toString());
            for (int i = 0; i < this.clipListState.clipList.size(); i++) {
                if (this.segmentStatus[i] == SegmentStatus.PendingTranscodeResult) {
                    switch (this.clipListState.clipList.get(i).transcodeState) {
                        case SUECCESS:
                        case ERR_INCOMPLETE:
                            this.segmentStatus[i] = SegmentStatus.ToDownload;
                            this.numPendingTranscodeResult--;
                            continue;
                        case UNDO:
                            break;
                        case INVALID_PARAM:
                            this.segmentStatus[i] = SegmentStatus.Failure;
                            this.numPendingTranscodeResult--;
                            this.numFinishedError++;
                            continue;
                        default:
                            this.controller.asyncCommand(8, 0, ErrorType.INVALID_PARAM_OR_ERR_UNSPECIFIED);
                            VideoRecordInfo segment = this.vecSegmentMix.get(i);
                            MediaLogger.show(TAG, "Clip conversion error: Source File ID=" + segment.fileId_drone + " Clip No. " + i + " Time:" + segment.getStartTimeMsec() + "-" + segment.getEndTimeMsec() + " Error type:" + clipInfo.transcodeState);
                            return;
                    }
                }
            }
            updateProgress();
            if (this.numPendingTranscodeResult > 0) {
                this.state = State.SentRequestTranscoding;
                this.controller.asyncCommandDelayed(5, 200, 0, null);
            } else if (downloadNext()) {
                this.lastbyteRecievedTime = SystemClock.uptimeMillis();
                this.state = State.WaitForDownloading;
                this.controller.asyncCommand(9);
            } else {
                for (int i2 = 0; i2 < this.vecSegmentMix.size(); i2++) {
                    MediaLogger.i(TAG, "segmentStatus " + i2 + " =" + this.segmentStatus[i2]);
                }
                MediaLogger.i(TAG, "numFinishedDownload=" + this.numFinishedDownload);
                MediaLogger.i(TAG, "numFinishedError=" + this.numFinishedError);
                this.state = State.AllFileDownloaded;
                this.controller.asyncCommand(7);
            }
        }
    }

    private boolean downloadNext() {
        int next = -1;
        int i = 0;
        while (true) {
            if (i >= this.vecSegmentMix.size()) {
                break;
            } else if (this.segmentStatus[i] == SegmentStatus.ToDownload) {
                next = i;
                break;
            } else {
                i++;
            }
        }
        if (next == -1) {
            return false;
        }
        downloadFile(next);
        return true;
    }

    /* access modifiers changed from: private */
    public void doRequestTranscoding() {
        int foldId = this.vecSegmentMix.get(0).getFolderID_drone();
        int fileId = this.vecSegmentMix.get(0).getFileId_drone();
        int uuid = (int) this.vecSegmentMix.get(0).getUuid_drone();
        MediaLogger.i(TAG, "foldId=" + foldId + " fileId=" + fileId + " uuid=" + uuid);
        this.ctrTransCode.setControlType(DataCameraControlTransCode.ControlType.START);
        this.ctrTransCode.setFolderId(foldId);
        this.ctrTransCode.setFileId(fileId);
        this.ctrTransCode.setUuid(uuid);
        this.ctrTransCode.setGroupId(this.commitID);
        this.ctrTransCode.setResolution(DataCameraControlTransCode.ToResolution.R1280_720_16_9);
        this.ctrTransCode.setFps(DataCameraControlTransCode.ToFps.fps30);
        this.ctrTransCode.setBps(60);
        this.ctrTransCode.setFragmentId(0);
        ArrayList<DataCameraControlTransCode.DJIFragmentModel> segList = new ArrayList<>();
        for (int i = 0; i < this.vecSegmentMix.size(); i++) {
            DataCameraControlTransCode dataCameraControlTransCode = this.ctrTransCode;
            dataCameraControlTransCode.getClass();
            DataCameraControlTransCode.DJIFragmentModel seg = new DataCameraControlTransCode.DJIFragmentModel();
            seg.startTime = this.vecSegmentMix.get(i).getStartTimeMsec();
            seg.endTime = this.vecSegmentMix.get(i).getEndTimeMsec();
            MediaLogger.i(TAG, "making request to convert segment from " + seg.startTime + " to " + seg.endTime);
            segList.add(seg);
        }
        this.ctrTransCode.setFragments(segList);
        this.ctrTransCode.start(new DJIDataCallBack() {
            /* class dji.midware.media.HD.HDConversion.AnonymousClass3 */

            public void onSuccess(Object model) {
                HDConversion.this.controller.asyncCommand(5);
            }

            public void onFailure(Ccode ccode) {
                HDConversion.this.controller.asyncCommand(8, 0, ccode);
            }
        });
        this.state = State.SentRequestTranscoding;
        MediaLogger.i(TAG, "have sent RequestTranscoding. start to query state");
    }

    /* access modifiers changed from: private */
    public void doGetFileParams() {
        this.dcGetFileParams.setType(DataCameraGetFileParams.ParamsType.CLIP);
        this.dcGetFileParams.start(new DJIDataCallBack() {
            /* class dji.midware.media.HD.HDConversion.AnonymousClass4 */

            public void onSuccess(Object model) {
                MediaLogger.show("Returned folderID=" + HDConversion.this.dcGetFileParams.getFolderId());
                HDConversion.this.commitID = HDConversion.this.dcGetFileParams.getFolderId();
                HDConversion.this.controller.asyncCommand(4);
            }

            public void onFailure(Ccode ccode) {
                HDConversion.this.controller.asyncCommand(8, 0, ccode);
            }
        });
        this.state = State.SentGetFileParams;
    }

    /* access modifiers changed from: private */
    public void doSwitchToTranscode() {
        DataCameraSetMode.getInstance().setMode(DataCameraGetMode.MODE.TRANSCODE).start(new DJIDataCallBack() {
            /* class dji.midware.media.HD.HDConversion.AnonymousClass5 */

            public void onSuccess(Object model) {
                MediaLogger.show("have switched to MODE: " + DataCameraGetMode.MODE.TRANSCODE);
                HDConversion.this.controller.asyncCommand(3);
            }

            public void onFailure(Ccode ccode) {
                HDConversion.this.controller.asyncCommand(8, 0, ccode);
            }
        });
        this.state = State.SentSwithToTranscode;
    }

    /* access modifiers changed from: private */
    public void doGetOriginalMode() {
        this.originMode = null;
        DataCameraGetMode.getInstance().start(new DJIDataCallBack() {
            /* class dji.midware.media.HD.HDConversion.AnonymousClass6 */

            public void onSuccess(Object model) {
                HDConversion.this.originMode = DataCameraGetMode.getInstance().getMode();
                if (HDConversion.this.originMode == null) {
                    MediaLogger.show("Can't get camera's original mode");
                } else {
                    MediaLogger.show("Original mode is " + HDConversion.this.originMode.toString());
                }
                if (HDConversion.this.originMode == DataCameraGetMode.MODE.TRANSCODE) {
                    HDConversion.this.originMode = DataCameraGetMode.MODE.TAKEPHOTO;
                    HDConversion.this.controller.asyncCommand(3);
                    return;
                }
                HDConversion.this.controller.asyncCommand(2);
            }

            public void onFailure(Ccode ccode) {
                HDConversion.this.controller.asyncCommand(8, 0, ccode);
            }
        });
        MediaLogger.d(TAG, "switch to SentGetOriginalMode");
        this.state = State.SentGetOriginalMode;
    }

    /* access modifiers changed from: private */
    public void doOnError(Object error) {
        if (error instanceof Exception) {
            this.callback.onFail((Exception) error);
        } else {
            this.callback.onFail(new Exception("State=" + this.state.toString() + " Error=" + String.valueOf(error.toString())));
        }
        if (this.originMode != null) {
            MediaLogger.i(TAG, "try to switch to mode " + this.originMode);
            DataCameraSetMode.getInstance().setMode(this.originMode).start(new DJIDataCallBack() {
                /* class dji.midware.media.HD.HDConversion.AnonymousClass7 */

                public void onSuccess(Object model) {
                    MediaLogger.i(HDConversion.TAG, "have successfully switched to MODE: " + HDConversion.this.originMode);
                }

                public void onFailure(Ccode ccode) {
                    MediaLogger.i(HDConversion.TAG, "failed to switch to MODE: " + HDConversion.this.originMode + " ccode=" + ccode);
                }
            });
        }
        this.controller.release();
    }

    /* access modifiers changed from: private */
    public void doOnSuccess() {
        if (this.callback != null) {
            this.callback.onProgress(100);
            this.callback.onSuccess();
        }
        DJILogHelper.getInstance().LOGD("", "done", false, true);
        this.controller.release();
    }

    /* access modifiers changed from: private */
    public void doStart() {
        File dir = new File(this.targetDirectory);
        if (dir.exists() || dir.mkdirs()) {
            if (this.callback != null) {
                this.callback.onStart();
            }
            this.segmentStatus = new SegmentStatus[this.vecSegmentMix.size()];
            this.segmentTotalBytes = new long[this.vecSegmentMix.size()];
            this.segmentReceivedBytes = new long[this.vecSegmentMix.size()];
            for (int i = 0; i < this.vecSegmentMix.size(); i++) {
                this.segmentStatus[i] = SegmentStatus.PendingTranscodeResult;
                this.segmentTotalBytes[i] = 0;
                this.segmentReceivedBytes[i] = 0;
            }
            this.numPendingTranscodeResult = this.vecSegmentMix.size();
            this.numFinishedError = 0;
            this.numFinishedDownload = 0;
            this.downloadingSegmentIndex = -1;
            this.state = State.Started;
            this.controller.asyncCommand(1);
            return;
        }
        Exception e = new Exception("can't create the directory for saving target HD segments");
        MediaLogger.show(e);
        if (this.callback != null) {
            this.callback.onFail(e);
        }
        this.controller.asyncCommand(8);
    }

    /* access modifiers changed from: private */
    public void doWaitForDownloading() {
        if (this.state == State.WaitForDownloading) {
            updateProgress();
            if (this.downloadingSegmentIndex == -1) {
                downloadNext();
            }
            if (this.numFinishedDownload + this.numFinishedError == this.vecSegmentMix.size()) {
                for (int i = 0; i < this.vecSegmentMix.size(); i++) {
                    MediaLogger.i(TAG, "segmentStatus " + i + " =" + this.segmentStatus[i]);
                }
                MediaLogger.i(TAG, "numFinishedDownload=" + this.numFinishedDownload);
                MediaLogger.i(TAG, "numFinishedError=" + this.numFinishedError);
                this.state = State.AllFileDownloaded;
                this.controller.asyncCommand(7);
            } else if (SystemClock.uptimeMillis() - this.lastbyteRecievedTime > DOWNLOAD_TIME_OUT) {
                this.controller.asyncCommand(8, 0, ErrorType.DownloadTimeOut);
            } else {
                this.controller.asyncCommandDelayed(9, 200, 0, null);
            }
        }
    }

    private void updateProgress() {
        double progress = 0.0d;
        for (int i = 0; i < this.vecSegmentMix.size(); i++) {
            progress += this.segmentTotalBytes[i] == 0 ? 0.0d : ((double) this.segmentReceivedBytes[i]) / ((double) this.segmentTotalBytes[i]);
        }
        double progress2 = progress / ((double) this.vecSegmentMix.size());
        if (this.callback != null) {
            this.callback.onProgress((int) (progress2 * 100.0d));
        }
        MediaLogger.i(TAG, "total progress=" + (progress2 * 100.0d) + "%");
    }

    private void downloadFile(final int segmentIndex) {
        this.downloadingSegmentIndex = segmentIndex;
        DJIClipInfo clipInfo = this.clipListState.clipList.get(segmentIndex);
        final VideoRecordInfo segment = this.vecSegmentMix.get(segmentIndex);
        clipInfo.commitID = this.commitID;
        clipInfo.source_uuid = segment.getUuid_drone();
        clipInfo.startTimeMSec = segment.getStartTimeMsec();
        clipInfo.endTimeMSec = segment.getEndTimeMsec();
        clipInfo.fileType = DJIFileType.MOV;
        this.clipFileLoader.setListener(clipInfo, new DJIAlbumInterface.DJIAlbumPullListener<DJIClipFile>() {
            /* class dji.midware.media.HD.HDConversion.AnonymousClass8 */

            public void onSuccess(DJIClipFile data) {
                HDConversion.this.downloadingSegmentIndex = -1;
                MediaLogger.i(HDConversion.TAG, "clipFileLoader OnSuccess: path=" + data.cachPath);
                HDConversion.this.clipFile = data;
                try {
                    File file = new File(HDConversion.this.clipFile.cachPath);
                    File file2 = new File(HDConversion.this.targetDirectory + segment.getLocalFileName() + ".mov");
                    if (!file2.exists()) {
                        file.renameTo(file2);
                    } else if (file2.delete()) {
                        file.renameTo(file2);
                    }
                    segment.setIsHD(true);
                    HDConversion.this.segmentStatus[segmentIndex] = SegmentStatus.HasDownloaded;
                    HDConversion.this.numFinishedDownload++;
                } catch (Exception e) {
                    MediaLogger.show(e);
                }
            }

            public void onStart() {
                MediaLogger.i(HDConversion.TAG, "clipFileLoader OnStart");
            }

            public void onRateUpdate(long total, long current, long persize) {
                MediaLogger.i(HDConversion.TAG, "clipFileLoader onRateUpdate: total=" + total + " ; current=" + current + " ; persize" + persize);
            }

            public void onProgress(long total, long current) {
                MediaLogger.i(HDConversion.TAG, "file index=" + segmentIndex + " progress: total=" + total + " ; current=" + current);
                HDConversion.this.lastbyteRecievedTime = SystemClock.uptimeMillis();
                HDConversion.this.segmentReceivedBytes[segmentIndex] = current;
                HDConversion.this.segmentTotalBytes[segmentIndex] = total;
            }

            public void onFailure(DJIAlbumPullErrorType error) {
                HDConversion.this.segmentStatus[segmentIndex] = SegmentStatus.Failure;
                HDConversion.this.numFinishedError++;
                HDConversion.this.downloadingSegmentIndex = -1;
                MediaLogger.e(HDConversion.TAG, "clipFileLoader onFailure segmentIndex=" + segmentIndex);
            }
        });
        this.clipFileLoader.start();
        MediaLogger.i(TAG, "clipFileLoader start(): clipNo=" + clipInfo.clipNo);
    }
}
