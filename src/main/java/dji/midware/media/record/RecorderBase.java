package dji.midware.media.record;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogPaths;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.external.DJIExternalStorageController;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.metadata.VideoRecordInfo;
import dji.midware.media.metadata.VideoRecordInfoBuild;
import dji.midware.media.record.RecorderManager;
import dji.midware.reflect.MidwareInjectManager;
import java.io.File;
import java.io.IOException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public abstract class RecorderBase implements RecorderInterface {
    private static final int CHECK_DECODER_INTERVAL = 200;
    private static final int CHECK_DECODER_RETRY_NUM = 20;
    protected static final int MAX_FRAME_DURATION = 41667;
    private static final int MIN_INTERVAL_TO_DECODER_INIT = 2000;
    protected long audioSampleWriteCount = 0;
    protected RecorderStatus currentStatus = RecorderStatus.STANDBY;
    private int droneStartTimeMsec;
    protected ExternalSdRecordingHelper externalSdRecordingHelper;
    private long[] lastLogDataTimeArr = new long[LogDataMode.values().length];
    private int lastRecordSplitTime;
    private int lastSynchedFrameIndex;
    private long lastSynchedTime;
    private long localAbsBeginTime;
    protected String mainFileName = "";
    public int numFrameWritten;
    protected DocumentFile recordDirDf;
    protected Object recordStatusSwitchSync = new Object();
    protected VideoRecordInfo videoRecordInfo = null;
    protected VideoRecordInfoBuild videoRecordInfoSetter = null;

    public enum LogDataMode {
        Video,
        Audio
    }

    public enum RecorderStatus {
        STANDBY,
        RECORDING
    }

    /* access modifiers changed from: protected */
    public abstract String getTAG();

    /* access modifiers changed from: protected */
    public abstract void onEndRecord();

    /* access modifiers changed from: protected */
    public abstract void onStartRecord();

    public static class EventVideoCacheCompletion {
        private final String filePath;

        public EventVideoCacheCompletion(String filePath2) {
            this.filePath = filePath2;
        }

        public String getFilePath() {
            return this.filePath;
        }
    }

    public int getNumFrameWritten() {
        return this.numFrameWritten;
    }

    /* access modifiers changed from: private */
    public boolean checkIsDecoderReady(int retryNum, int intervalInMillis) {
        return DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface() != null;
    }

    private class ServiceEventHandler extends Thread {
        private RecorderManager.Service_Action event = null;

        public ServiceEventHandler(RecorderManager.Service_Action event2) {
            this.event = event2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x009d, code lost:
            dji.midware.media.MediaLogger.toPhoneAndFile(r4.this$0.getTAG(), "NEW state=" + r4.this$0.getCurrentStatus());
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r4 = this;
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this
                java.lang.String r0 = r0.getTAG()
                dji.midware.media.record.RecorderManager$Service_Action r1 = r4.event
                java.lang.String r1 = r1.toString()
                dji.midware.media.MediaLogger.toPhoneAndFile(r0, r1)
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this
                java.lang.String r0 = r0.getTAG()
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "an event is received from the bus: "
                java.lang.StringBuilder r1 = r1.append(r2)
                dji.midware.media.record.RecorderManager$Service_Action r2 = r4.event
                java.lang.String r2 = r2.toString()
                java.lang.StringBuilder r1 = r1.append(r2)
                java.lang.String r1 = r1.toString()
                android.util.Log.i(r0, r1)
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this
                java.lang.Object r1 = r0.recordStatusSwitchSync
                monitor-enter(r1)
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d7 }
                r0.<init>()     // Catch:{ all -> 0x00d7 }
                java.lang.String r2 = "Status="
                java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r2 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase$RecorderStatus r2 = r2.currentStatus     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x00d7 }
                java.lang.String r2 = " event="
                java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderManager$Service_Action r2 = r4.event     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x00d7 }
                java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00d7 }
                dji.midware.media.MediaLogger.show(r0)     // Catch:{ all -> 0x00d7 }
                int[] r0 = dji.midware.media.record.RecorderBase.AnonymousClass2.$SwitchMap$dji$midware$media$record$RecorderBase$RecorderStatus     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r2 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase$RecorderStatus r2 = r2.currentStatus     // Catch:{ all -> 0x00d7 }
                int r2 = r2.ordinal()     // Catch:{ all -> 0x00d7 }
                r0 = r0[r2]     // Catch:{ all -> 0x00d7 }
                switch(r0) {
                    case 1: goto L_0x00c1;
                    case 2: goto L_0x0130;
                    default: goto L_0x006e;
                }     // Catch:{ all -> 0x00d7 }
            L_0x006e:
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                java.lang.String r0 = r0.getTAG()     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d7 }
                r2.<init>()     // Catch:{ all -> 0x00d7 }
                java.lang.String r3 = "error in state transition: state="
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r3 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase$RecorderStatus r3 = r3.currentStatus     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                java.lang.String r3 = " action="
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderManager$Service_Action r3 = r4.event     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00d7 }
                dji.midware.media.MediaLogger.toPhoneAndFile(r0, r2)     // Catch:{ all -> 0x00d7 }
            L_0x009c:
                monitor-exit(r1)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this
                java.lang.String r0 = r0.getTAG()
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "NEW state="
                java.lang.StringBuilder r1 = r1.append(r2)
                dji.midware.media.record.RecorderBase r2 = dji.midware.media.record.RecorderBase.this
                dji.midware.media.record.RecorderBase$RecorderStatus r2 = r2.getCurrentStatus()
                java.lang.StringBuilder r1 = r1.append(r2)
                java.lang.String r1 = r1.toString()
                dji.midware.media.MediaLogger.toPhoneAndFile(r0, r1)
            L_0x00c0:
                return
            L_0x00c1:
                dji.midware.media.record.RecorderManager$Service_Action r0 = r4.event     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderManager$Service_Action r2 = dji.midware.media.record.RecorderManager.Service_Action.START_RECORD     // Catch:{ all -> 0x00d7 }
                if (r0 != r2) goto L_0x0100
                boolean r0 = dji.midware.media.record.RecorderManager.checkAndReleaseBuffer()     // Catch:{ all -> 0x00d7 }
                if (r0 != 0) goto L_0x00da
                org.greenrobot.eventbus.EventBus r0 = org.greenrobot.eventbus.EventBus.getDefault()     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderManager$Video_Buffer_Notify r2 = dji.midware.media.record.RecorderManager.Video_Buffer_Notify.NO_SPACE     // Catch:{ all -> 0x00d7 }
                r0.post(r2)     // Catch:{ all -> 0x00d7 }
                goto L_0x009c
            L_0x00d7:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x00d7 }
                throw r0
            L_0x00da:
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                r2 = 20
                r3 = 200(0xc8, float:2.8E-43)
                boolean r0 = r0.checkIsDecoderReady(r2, r3)     // Catch:{ all -> 0x00d7 }
                if (r0 != 0) goto L_0x00e8
                monitor-exit(r1)     // Catch:{ all -> 0x00d7 }
                goto L_0x00c0
            L_0x00e8:
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                r0.onStartRecord()     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase$RecorderStatus r2 = dji.midware.media.record.RecorderBase.RecorderStatus.RECORDING     // Catch:{ all -> 0x00d7 }
                r0.setNewStatus(r2)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase$ServiceEventHandler$1 r0 = new dji.midware.media.record.RecorderBase$ServiceEventHandler$1     // Catch:{ all -> 0x00d7 }
                java.lang.String r2 = "recorde"
                r0.<init>(r2)     // Catch:{ all -> 0x00d7 }
                r0.start()     // Catch:{ all -> 0x00d7 }
                goto L_0x009c
            L_0x0100:
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                java.lang.String r0 = r0.getTAG()     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d7 }
                r2.<init>()     // Catch:{ all -> 0x00d7 }
                java.lang.String r3 = "error in state transition: state="
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r3 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase$RecorderStatus r3 = r3.currentStatus     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                java.lang.String r3 = " action="
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderManager$Service_Action r3 = r4.event     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00d7 }
                dji.midware.media.MediaLogger.toPhoneAndFile(r0, r2)     // Catch:{ all -> 0x00d7 }
                goto L_0x009c
            L_0x0130:
                dji.midware.media.record.RecorderManager$Service_Action r0 = r4.event     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderManager$Service_Action r2 = dji.midware.media.record.RecorderManager.Service_Action.END_RECORD     // Catch:{ all -> 0x00d7 }
                if (r0 != r2) goto L_0x0144
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                r0.onEndRecord()     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase$RecorderStatus r2 = dji.midware.media.record.RecorderBase.RecorderStatus.STANDBY     // Catch:{ all -> 0x00d7 }
                r0.setNewStatus(r2)     // Catch:{ all -> 0x00d7 }
                goto L_0x009c
            L_0x0144:
                dji.midware.media.record.RecorderManager$Service_Action r0 = r4.event     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderManager$Service_Action r2 = dji.midware.media.record.RecorderManager.Service_Action.START_RECORD     // Catch:{ all -> 0x00d7 }
                if (r0 != r2) goto L_0x016c
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                r2 = 20
                r3 = 200(0xc8, float:2.8E-43)
                boolean r0 = r0.checkIsDecoderReady(r2, r3)     // Catch:{ all -> 0x00d7 }
                if (r0 != 0) goto L_0x0159
                monitor-exit(r1)     // Catch:{ all -> 0x00d7 }
                goto L_0x00c0
            L_0x0159:
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                r0.onEndRecord()     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                r0.onStartRecord()     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase$RecorderStatus r2 = dji.midware.media.record.RecorderBase.RecorderStatus.RECORDING     // Catch:{ all -> 0x00d7 }
                r0.setNewStatus(r2)     // Catch:{ all -> 0x00d7 }
                goto L_0x009c
            L_0x016c:
                dji.midware.media.record.RecorderBase r0 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                java.lang.String r0 = r0.getTAG()     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d7 }
                r2.<init>()     // Catch:{ all -> 0x00d7 }
                java.lang.String r3 = "error in state transition: state="
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase r3 = dji.midware.media.record.RecorderBase.this     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderBase$RecorderStatus r3 = r3.currentStatus     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                java.lang.String r3 = " action="
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                dji.midware.media.record.RecorderManager$Service_Action r3 = r4.event     // Catch:{ all -> 0x00d7 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x00d7 }
                java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00d7 }
                dji.midware.media.MediaLogger.toPhoneAndFile(r0, r2)     // Catch:{ all -> 0x00d7 }
                goto L_0x009c
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.record.RecorderBase.ServiceEventHandler.run():void");
        }
    }

    /* renamed from: dji.midware.media.record.RecorderBase$2  reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$dji$midware$media$record$RecorderBase$RecorderStatus = new int[RecorderStatus.values().length];

        static {
            try {
                $SwitchMap$dji$midware$media$record$RecorderBase$RecorderStatus[RecorderStatus.STANDBY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$dji$midware$media$record$RecorderBase$RecorderStatus[RecorderStatus.RECORDING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo stateInfo) {
        if (stateInfo.getRecordState() == DataCameraGetPushStateInfo.RecordType.STARTING && stateInfo.getRecordSplitTime() == 0 && this.lastRecordSplitTime > 0) {
            new ServiceEventHandler(RecorderManager.Service_Action.START_RECORD).start();
        }
        this.lastRecordSplitTime = stateInfo.getRecordSplitTime();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public synchronized void onEvent3BackgroundThread(RecorderManager.Service_Action event) {
        Log.d("recode", "start");
        new ServiceEventHandler(event).start();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public synchronized void onEvent3BackgroundThread(DJIExternalStorageController.ExternalStorageEvent event) {
        if (isRecordingToExternalSd() && event.getEventAction() == 2) {
            this.externalSdRecordingHelper.forceStop();
            this.externalSdRecordingHelper = null;
        }
    }

    /* access modifiers changed from: protected */
    public int[] getRecordWidthHeight() {
        return new int[]{ServiceManager.getInstance().getDecoder().width, ServiceManager.getInstance().getDecoder().height};
    }

    public RecorderStatus getCurrentStatus() {
        return this.currentStatus;
    }

    /* access modifiers changed from: protected */
    public void setNewStatus(RecorderStatus newStatus) {
        if (this.currentStatus != newStatus) {
            this.currentStatus = newStatus;
            EventBus.getDefault().post(this.currentStatus);
        }
    }

    /* access modifiers changed from: protected */
    public void initExternalSdRecordingHelper() {
        if (!ExternalSdRecordingHelper.getVideoCacheExternalStorageEnable() || !ExternalSdRecordingHelper.isExteranSDGranted()) {
            this.externalSdRecordingHelper = null;
            Log.d("", "createFile: external sd not granted");
            return;
        }
        Log.d("", "createFile: external sd granted");
        this.recordDirDf = ExternalSdRecordingHelper.getExternalSdRecordingDirDf();
        DocumentFile recordFileDf = ExternalSdRecordingHelper.checkAndCreateFile(this.recordDirDf, "video/mp4", this.mainFileName);
        if (recordFileDf != null) {
            this.externalSdRecordingHelper = new ExternalSdRecordingHelper(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".mp4", recordFileDf);
            this.externalSdRecordingHelper.start();
            return;
        }
        EventBus.getDefault().post(new DJIExternalStorageController.ExternalStorageEvent(3));
    }

    /* access modifiers changed from: protected */
    public String getRecordingFilePath() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void stopExternalSdRecordingHelper() {
        if (this.externalSdRecordingHelper != null) {
            this.externalSdRecordingHelper.stop(true);
            this.externalSdRecordingHelper = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        new Thread("record2") {
            /* class dji.midware.media.record.RecorderBase.AnonymousClass1 */

            public void run() {
                synchronized (RecorderBase.this.recordStatusSwitchSync) {
                    MediaLogger.show("Status=" + RecorderBase.this.currentStatus + " event=ON_DESTROY");
                    if (RecorderBase.this.currentStatus == RecorderStatus.RECORDING) {
                        try {
                            RecorderBase.this.onEndRecord();
                        } catch (Exception e) {
                            MediaLogger.show(e);
                        }
                        RecorderBase.this.setNewStatus(RecorderStatus.STANDBY);
                    }
                }
            }
        }.start();
        EventBus.getDefault().unregister(this);
    }

    /* access modifiers changed from: protected */
    public boolean checkIsCurrentRecorder() {
        return this == RecorderManager.getCurrentRecorder();
    }

    /* access modifiers changed from: protected */
    public void startRecordVideoInfo() {
        MediaLogger.i(getTAG(), "going to create VideoRecordInfoBuild");
        if (getRecordWidthHeight() == null) {
            int[] widthHeight = {ServiceManager.getInstance().getDecoder().width, ServiceManager.getInstance().getDecoder().height};
        }
        this.videoRecordInfo = new VideoRecordInfo();
        MediaLogger.i(getTAG(), "video record info setter is created");
        if (MidwareInjectManager.getMidwareToVideoLibInjectable() != null) {
            MidwareInjectManager.getMidwareToVideoLibInjectable().startRecordFlightAnalytics(this, this.mainFileName);
        }
        if (MidwareInjectManager.getMidwareToP3Injectable() != null) {
            MidwareInjectManager.getMidwareToP3Injectable().startRecordInfo();
        }
    }

    /* access modifiers changed from: protected */
    public void endRecordVideoInfo() {
        if (MidwareInjectManager.getMidwareToP3Injectable() != null) {
            MidwareInjectManager.getMidwareToP3Injectable().endRecordInfo();
        }
        MediaLogger.i(getTAG(), "video record info setter is closed");
        if (this.videoRecordInfoSetter != null) {
            this.videoRecordInfoSetter.setEndTimeMsec((int) (((double) this.numFrameWritten) * DJIVideoUtil.getDurationPerFrame()));
            this.videoRecordInfoSetter.onDestroy();
            this.videoRecordInfoSetter = null;
        }
        if (!(this.externalSdRecordingHelper == null || this.recordDirDf == null || !this.recordDirDf.isDirectory())) {
            try {
                ExternalSdRecordingHelper.copyToExternalFile(new File(VideoMetadataManager.getSourceVideoDirectory() + this.mainFileName + ".info"), ExternalSdRecordingHelper.checkAndCreateFile(this.recordDirDf, "", this.mainFileName + ".info"), true);
            } catch (IOException e) {
                Log.e("", "endRecordVideoInfo: ", e);
            }
        }
        if (MidwareInjectManager.getMidwareToVideoLibInjectable() != null) {
            MidwareInjectManager.getMidwareToVideoLibInjectable().stopRecordFlightAnalytics(this.mainFileName);
        }
        this.videoRecordInfo = null;
    }

    /* access modifiers changed from: protected */
    public void setMainFileName() {
        this.mainFileName = DJIVideoUtil.getOutputFileNameWithoutSuffix();
    }

    /* access modifiers changed from: protected */
    public void initPTSSync() {
        long currentTimeMillis = System.currentTimeMillis();
        this.localAbsBeginTime = currentTimeMillis;
        this.lastSynchedTime = currentTimeMillis;
        this.droneStartTimeMsec = DataCameraGetPushStateInfo.getInstance().getVideoRecordTime() * 1000;
        this.lastSynchedFrameIndex = 0;
    }

    /* access modifiers changed from: protected */
    public void syncPTS() {
        long currentAbsTime = System.currentTimeMillis();
        if (((double) (currentAbsTime - this.lastSynchedTime)) > DJIVideoUtil.getDurationPerFrame() * ((double) ((this.numFrameWritten - this.lastSynchedFrameIndex) + DJIVideoUtil.getFPS()))) {
            this.videoRecordInfoSetter.addSyncPoint((int) (((double) this.numFrameWritten) * DJIVideoUtil.getDurationPerFrame()), ((int) (currentAbsTime - this.localAbsBeginTime)) + this.droneStartTimeMsec);
            this.lastSynchedTime = currentAbsTime;
            this.lastSynchedFrameIndex = this.numFrameWritten;
        }
    }

    /* access modifiers changed from: protected */
    public String getMainFileName() {
        return this.mainFileName;
    }

    /* access modifiers changed from: protected */
    public void addToMediaLibrary(String fullFilePath) {
        try {
            Uri contentUri = Uri.fromFile(new File(fullFilePath));
            ServiceManager.getInstance();
            Context context = ServiceManager.getContext();
            if (Build.VERSION.SDK_INT >= 19) {
                Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", contentUri));
            }
            EventBus.getDefault().post(new EventVideoCacheCompletion(fullFilePath));
        } catch (Exception e) {
            MediaLogger.e(getTAG(), e);
        }
    }

    public String getRecordingFileName() {
        String str;
        synchronized (this.recordStatusSwitchSync) {
            if (RecorderStatus.RECORDING == this.currentStatus) {
                str = this.mainFileName;
            } else {
                str = null;
            }
        }
        return str;
    }

    public boolean isRecordingToExternalSd() {
        return this.externalSdRecordingHelper != null;
    }

    public static void log2File(String log) {
        DJILog.saveLog(log, DJILogPaths.LOG_RECORD);
    }

    /* access modifiers changed from: protected */
    public void logDataInput(String log, int minInterval, LogDataMode dataMode) {
        long time = System.currentTimeMillis();
        if (time - this.lastLogDataTimeArr[dataMode.ordinal()] > ((long) minInterval)) {
            log2File(log);
            this.lastLogDataTimeArr[dataMode.ordinal()] = time;
        }
    }
}
