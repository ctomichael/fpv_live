package dji.midware.media.newframing;

import android.support.v4.util.Pools;
import com.dji.dynamic_assets.DynamicAssetsHelper;
import com.dji.video.framing.internal.decoder.common.FrameFovType;
import com.dji.video.framing.internal.opengl.renderer.GLIdentityRender;
import dji.component.accountcenter.IMemberProtocol;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.util.save.StreamSaver;
import dji.publics.DJIExecutor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DistortionCorrectionRender extends GLIdentityRender {
    private static final int BLOCK_HEIGHT = 8;
    private static final int BLOCK_WIDTH = 8;
    private static final boolean DEBUG = false;
    private static final int LUT_ASYNC_PARSE_WAIT_NUM = 2;
    private static final String LUT_DIR_NAME = "distrotion_correction";
    private static final int LUT_SYNC_PARSE_MAX_NUM = 1;
    private static final int MAX_LUT_NUM = 64;
    private static final int[] RENDER_ORDER = {0, 1, 2, 2, 1, 3};
    private DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.CameraType.OTHER;
    private boolean enableAntiDistortion = true;
    private FrameFovType fovType = FrameFovType.NoGdc;
    private int height = 0;
    private String lutRootDirPath;
    private int[] originVerticeIndicesData = this.mTriangleIndicesData;
    private float[] originVerticePosData = this.mTriangleVerticesPosData;
    private float[] originVerticeUvData = this.mTriangleVerticesUvData;
    /* access modifiers changed from: private */
    public CountDownLatch parseLutCdl;
    /* access modifiers changed from: private */
    public final Pools.SynchronizedPool<ParseLutTask> parseLutTaskPool = new Pools.SynchronizedPool<>(64);
    private Object updateFrameInfoLock = new Object();
    /* access modifiers changed from: private */
    public ArrayList<float[]> uvDataList = new ArrayList<>(64);
    private int width = 0;
    private int zoomIndex = -1;

    /* access modifiers changed from: private */
    public void logd(String tag, String log) {
        if (StreamSaver.videoDebugEnabledUsedInSDK) {
            DJILogHelper.getInstance().LOGD(tag, log);
        }
    }

    public void setEnableAntiDistortion(boolean enableAntiDistortion2) {
        this.enableAntiDistortion = enableAntiDistortion2;
    }

    public DistortionCorrectionRender(boolean isExternal) {
        super(isExternal);
        for (int i = 0; i < 64; i++) {
            this.uvDataList.add(null);
        }
        this.lutRootDirPath = DynamicAssetsHelper.getInternalPath() + IMemberProtocol.PARAM_SEPERATOR + LUT_DIR_NAME;
        DJILog.d(TAG, "create DistortionCorrectionRender", new Object[0]);
    }

    public void init() {
        super.init();
    }

    private String getSensorName(DataCameraGetPushStateInfo.CameraType cameraType2) {
        if (cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 || cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC245_IMX477) {
            return "imx477";
        }
        if (cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1) {
            return "imx283";
        }
        return null;
    }

    private int getLutNum(DataCameraGetPushStateInfo.CameraType cameraType2) {
        if (cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 || cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC245_IMX477) {
            return 64;
        }
        if (cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1) {
            return 1;
        }
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:103:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x020c, code lost:
        if (r17 == false) goto L_0x0211;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x020e, code lost:
        updateVerticeBuffer();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0211, code lost:
        logd(dji.midware.media.newframing.DistortionCorrectionRender.TAG, "updateFrameInfo: timeCost=" + (java.lang.System.currentTimeMillis() - r20));
     */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateFrameInfo(dji.midware.data.model.P3.DataCameraGetPushStateInfo.CameraType r29, int r30, int r31, int r32, com.dji.video.framing.internal.decoder.common.FrameFovType r33) {
        /*
            r28 = this;
            r4 = 1088(0x440, float:1.525E-42)
            r0 = r31
            if (r0 != r4) goto L_0x0008
            r31 = 1080(0x438, float:1.513E-42)
        L_0x0008:
            dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType r4 = dji.midware.data.model.P3.DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1
            r0 = r29
            if (r0 != r4) goto L_0x0010
            r32 = 0
        L_0x0010:
            long r20 = java.lang.System.currentTimeMillis()
            r19 = 0
            r18 = 0
            r17 = 0
            r0 = r28
            java.lang.Object r0 = r0.updateFrameInfoLock
            r26 = r0
            monitor-enter(r26)
            r0 = r28
            dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType r4 = r0.cameraType     // Catch:{ all -> 0x0240 }
            r0 = r29
            if (r4 != r0) goto L_0x0041
            r0 = r28
            int r4 = r0.width     // Catch:{ all -> 0x0240 }
            r0 = r30
            if (r4 != r0) goto L_0x0041
            r0 = r28
            int r4 = r0.height     // Catch:{ all -> 0x0240 }
            r0 = r31
            if (r4 != r0) goto L_0x0041
            r0 = r28
            com.dji.video.framing.internal.decoder.common.FrameFovType r4 = r0.fovType     // Catch:{ all -> 0x0240 }
            r0 = r33
            if (r4 == r0) goto L_0x0051
        L_0x0041:
            r13 = 1
        L_0x0042:
            r0 = r28
            int r4 = r0.zoomIndex     // Catch:{ all -> 0x0240 }
            r0 = r32
            if (r4 == r0) goto L_0x0053
            r14 = 1
        L_0x004b:
            if (r13 != 0) goto L_0x0055
            if (r14 != 0) goto L_0x0055
            monitor-exit(r26)     // Catch:{ all -> 0x0240 }
        L_0x0050:
            return
        L_0x0051:
            r13 = 0
            goto L_0x0042
        L_0x0053:
            r14 = 0
            goto L_0x004b
        L_0x0055:
            r0 = r28
            float[] r4 = r0.mTriangleVerticesPosData     // Catch:{ all -> 0x0240 }
            r0 = r28
            float[] r6 = r0.originVerticePosData     // Catch:{ all -> 0x0240 }
            if (r4 == r6) goto L_0x0236
            r12 = 1
        L_0x0060:
            java.lang.String r8 = r28.getSensorName(r29)     // Catch:{ all -> 0x0240 }
            r0 = r28
            r1 = r30
            r2 = r31
            r3 = r33
            java.lang.String r5 = r0.getLutDir(r8, r1, r2, r3)     // Catch:{ all -> 0x0240 }
            if (r8 != 0) goto L_0x0239
            r15 = 0
        L_0x0073:
            r0 = r28
            boolean r4 = r0.enableAntiDistortion     // Catch:{ all -> 0x0240 }
            if (r4 == 0) goto L_0x0243
            if (r32 < 0) goto L_0x0243
            com.dji.video.framing.internal.decoder.common.FrameFovType r4 = com.dji.video.framing.internal.decoder.common.FrameFovType.NoGdc     // Catch:{ all -> 0x0240 }
            r0 = r33
            if (r0 == r4) goto L_0x0243
            if (r15 == 0) goto L_0x0243
            boolean r4 = r15.exists()     // Catch:{ all -> 0x0240 }
            if (r4 == 0) goto L_0x0243
            boolean r4 = r15.isDirectory()     // Catch:{ all -> 0x0240 }
            if (r4 == 0) goto L_0x0243
            r16 = 1
        L_0x0091:
            r0 = r16
            if (r0 == r12) goto L_0x009d
            r0 = r28
            int r4 = r0.zoomIndex     // Catch:{ all -> 0x0240 }
            r0 = r32
            if (r4 != r0) goto L_0x009f
        L_0x009d:
            if (r13 == 0) goto L_0x0247
        L_0x009f:
            r19 = 1
        L_0x00a1:
            if (r12 == 0) goto L_0x024b
            r0 = r28
            int r4 = r0.zoomIndex     // Catch:{ all -> 0x0240 }
            r0 = r32
            if (r4 == r0) goto L_0x024b
            r18 = 1
        L_0x00ad:
            java.lang.String r4 = dji.midware.media.newframing.DistortionCorrectionRender.TAG     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0240 }
            r6.<init>()     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = "updateFrameInfo: streamChange="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = r6.append(r13)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " zoomChange="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = r6.append(r14)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " isCor="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = r6.append(r12)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " needCor="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            r0 = r16
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " needUpdateVer="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            r0 = r19
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " needUpdateUv="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            r0 = r18
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " lutDir="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = r6.append(r15)     // Catch:{ all -> 0x0240 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0240 }
            r0 = r28
            r0.logd(r4, r6)     // Catch:{ all -> 0x0240 }
            java.lang.String r4 = dji.midware.media.newframing.DistortionCorrectionRender.TAG     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0240 }
            r6.<init>()     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = "updateFrameInfo: streamChange="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = r6.append(r13)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " zoomChange="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = r6.append(r14)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " isCorrecting="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = r6.append(r12)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " needCor="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            r0 = r16
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " enableAntiDistortion:"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            r0 = r28
            boolean r7 = r0.enableAntiDistortion     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0240 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0240 }
            r7 = 0
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0240 }
            dji.log.DJILog.d(r4, r6, r7)     // Catch:{ all -> 0x0240 }
            if (r19 == 0) goto L_0x0313
            if (r16 == 0) goto L_0x02cc
            int r10 = r28.getLutNum(r29)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r4 = r28
            r6 = r30
            r7 = r31
            r9 = r32
            r4.updateUvDataList(r5, r6, r7, r8, r9, r10)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r28
            r1 = r30
            r2 = r31
            float[] r24 = r0.genVerticesPos(r1, r2)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r28
            java.util.ArrayList<float[]> r4 = r0.uvDataList     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r32
            java.lang.Object r25 = r4.get(r0)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            float[] r25 = (float[]) r25     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r28
            r1 = r30
            r2 = r31
            int[] r23 = r0.genVerticesIndices(r1, r2)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.String r4 = dji.midware.media.newframing.DistortionCorrectionRender.TAG     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r6.<init>()     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.String r7 = "updateFrameInfo: posLen="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r24
            int r7 = r0.length     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.String r7 = " uvLen="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r25
            int r7 = r0.length     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.String r7 = " indicesLen="
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r23
            int r7 = r0.length     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r28
            r0.logd(r4, r6)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r28
            r1 = r24
            r2 = r25
            r3 = r23
            boolean r4 = r0.checkVerticePosAndUv(r1, r2, r3)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            if (r4 == 0) goto L_0x024f
            r0 = r24
            r1 = r28
            r1.mTriangleVerticesPosData = r0     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r25
            r1 = r28
            r1.mTriangleVerticesUvData = r0     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r0 = r23
            r1 = r28
            r1.mTriangleIndicesData = r0     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            r17 = 1
        L_0x01ed:
            r0 = r29
            r1 = r28
            r1.cameraType = r0     // Catch:{ all -> 0x0240 }
            r0 = r30
            r1 = r28
            r1.width = r0     // Catch:{ all -> 0x0240 }
            r0 = r31
            r1 = r28
            r1.height = r0     // Catch:{ all -> 0x0240 }
            r0 = r32
            r1 = r28
            r1.zoomIndex = r0     // Catch:{ all -> 0x0240 }
            r0 = r33
            r1 = r28
            r1.fovType = r0     // Catch:{ all -> 0x0240 }
            monitor-exit(r26)     // Catch:{ all -> 0x0240 }
            if (r17 == 0) goto L_0x0211
            r28.updateVerticeBuffer()
        L_0x0211:
            java.lang.String r4 = dji.midware.media.newframing.DistortionCorrectionRender.TAG
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "updateFrameInfo: timeCost="
            java.lang.StringBuilder r6 = r6.append(r7)
            long r26 = java.lang.System.currentTimeMillis()
            long r26 = r26 - r20
            r0 = r26
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            r0 = r28
            r0.logd(r4, r6)
            goto L_0x0050
        L_0x0236:
            r12 = 0
            goto L_0x0060
        L_0x0239:
            java.io.File r15 = new java.io.File     // Catch:{ all -> 0x0240 }
            r15.<init>(r5)     // Catch:{ all -> 0x0240 }
            goto L_0x0073
        L_0x0240:
            r4 = move-exception
            monitor-exit(r26)     // Catch:{ all -> 0x0240 }
            throw r4
        L_0x0243:
            r16 = 0
            goto L_0x0091
        L_0x0247:
            r19 = 0
            goto L_0x00a1
        L_0x024b:
            r18 = 0
            goto L_0x00ad
        L_0x024f:
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.String r6 = dji.midware.media.newframing.DistortionCorrectionRender.TAG     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            java.lang.String r7 = "updateFrameInfo: gen vertices failed!"
            r4.LOGE(r6, r7)     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            boolean r17 = r28.resetVertice()     // Catch:{ IOException -> 0x0260, RuntimeException -> 0x0284, InterruptedException -> 0x02a8 }
            goto L_0x01ed
        L_0x0260:
            r11 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x0240 }
            java.lang.String r6 = dji.midware.media.newframing.DistortionCorrectionRender.TAG     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0240 }
            r7.<init>()     // Catch:{ all -> 0x0240 }
            java.lang.String r9 = "updateFrameInfo: read lut file error: "
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0240 }
            r4.LOGE(r6, r7)     // Catch:{ all -> 0x0240 }
            boolean r17 = r28.resetVertice()     // Catch:{ all -> 0x0240 }
            goto L_0x01ed
        L_0x0284:
            r11 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x0240 }
            java.lang.String r6 = dji.midware.media.newframing.DistortionCorrectionRender.TAG     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0240 }
            r7.<init>()     // Catch:{ all -> 0x0240 }
            java.lang.String r9 = "updateFrameInfo: index error: "
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0240 }
            r4.LOGE(r6, r7)     // Catch:{ all -> 0x0240 }
            boolean r17 = r28.resetVertice()     // Catch:{ all -> 0x0240 }
            goto L_0x01ed
        L_0x02a8:
            r11 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x0240 }
            java.lang.String r6 = dji.midware.media.newframing.DistortionCorrectionRender.TAG     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0240 }
            r7.<init>()     // Catch:{ all -> 0x0240 }
            java.lang.String r9 = "updateFrameInfo: interrupted error: "
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0240 }
            r4.LOGE(r6, r7)     // Catch:{ all -> 0x0240 }
            boolean r17 = r28.resetVertice()     // Catch:{ all -> 0x0240 }
            goto L_0x01ed
        L_0x02cc:
            java.lang.String r6 = dji.midware.media.newframing.DistortionCorrectionRender.TAG     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0240 }
            r4.<init>()     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = "updateFrameInfo: zoomIndex="
            java.lang.StringBuilder r4 = r4.append(r7)     // Catch:{ all -> 0x0240 }
            r0 = r32
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " lutDir="
            java.lang.StringBuilder r4 = r4.append(r7)     // Catch:{ all -> 0x0240 }
            java.lang.StringBuilder r4 = r4.append(r15)     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = " exist="
            java.lang.StringBuilder r7 = r4.append(r7)     // Catch:{ all -> 0x0240 }
            if (r15 != 0) goto L_0x030a
            java.lang.String r4 = "NA"
        L_0x02f7:
            java.lang.StringBuilder r4 = r7.append(r4)     // Catch:{ all -> 0x0240 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0240 }
            r0 = r28
            r0.logd(r6, r4)     // Catch:{ all -> 0x0240 }
            boolean r17 = r28.resetVertice()     // Catch:{ all -> 0x0240 }
            goto L_0x01ed
        L_0x030a:
            boolean r4 = r15.exists()     // Catch:{ all -> 0x0240 }
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch:{ all -> 0x0240 }
            goto L_0x02f7
        L_0x0313:
            if (r18 == 0) goto L_0x01ed
            r0 = r28
            java.util.ArrayList<float[]> r4 = r0.uvDataList     // Catch:{ all -> 0x0240 }
            r0 = r32
            java.lang.Object r22 = r4.get(r0)     // Catch:{ all -> 0x0240 }
            float[] r22 = (float[]) r22     // Catch:{ all -> 0x0240 }
            if (r22 == 0) goto L_0x033f
            r0 = r28
            r1 = r22
            r0.setVerticeUvData(r1)     // Catch:{ Exception -> 0x032c }
            goto L_0x01ed
        L_0x032c:
            r11 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x0240 }
            java.lang.String r6 = dji.midware.media.newframing.DistortionCorrectionRender.TAG     // Catch:{ all -> 0x0240 }
            java.lang.String r7 = "updateFrameInfo: update uv failed!"
            r4.LOGE(r6, r7)     // Catch:{ all -> 0x0240 }
            boolean r17 = r28.resetVertice()     // Catch:{ all -> 0x0240 }
            goto L_0x01ed
        L_0x033f:
            r0 = r28
            int r0 = r0.zoomIndex     // Catch:{ all -> 0x0240 }
            r32 = r0
            goto L_0x01ed
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.newframing.DistortionCorrectionRender.updateFrameInfo(dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType, int, int, int, com.dji.video.framing.internal.decoder.common.FrameFovType):void");
    }

    private void clearUvDataList() {
        Collections.fill(this.uvDataList, null);
    }

    private boolean checkUvDataList() {
        if (this.uvDataList == null || this.uvDataList.size() < 64) {
            return false;
        }
        Iterator<float[]> it2 = this.uvDataList.iterator();
        while (it2.hasNext()) {
            if (it2.next() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean resetVertice() {
        clearUvDataList();
        boolean needUpdateBuffer = this.mTriangleVerticesPosData != this.originVerticePosData;
        this.mTriangleVerticesPosData = this.originVerticePosData;
        this.mTriangleVerticesUvData = this.originVerticeUvData;
        this.mTriangleIndicesData = this.originVerticeIndicesData;
        return needUpdateBuffer;
    }

    private boolean checkVerticePosAndUv(float[] verticePos, float[] verticeUv, int[] verticeIndices) {
        return (verticePos == null || verticeUv == null || verticeIndices == null || verticePos.length / 3 != verticeUv.length / 2) ? false : true;
    }

    private String getLutDir(String sensorName, int width2, int height2, FrameFovType fovType2) {
        if (sensorName == null || this.lutRootDirPath == null) {
            return null;
        }
        if (!needCheckFrameFov(width2, height2)) {
            return this.lutRootDirPath + IMemberProtocol.PARAM_SEPERATOR + sensorName + IMemberProtocol.PARAM_SEPERATOR + width2 + "x" + height2;
        }
        switch (fovType2) {
            case Wide:
                return this.lutRootDirPath + IMemberProtocol.PARAM_SEPERATOR + sensorName + IMemberProtocol.PARAM_SEPERATOR + width2 + "x" + height2 + "/wide";
            case Narrow:
                return this.lutRootDirPath + IMemberProtocol.PARAM_SEPERATOR + sensorName + IMemberProtocol.PARAM_SEPERATOR + width2 + "x" + height2 + "/narrow";
            default:
                return this.lutRootDirPath + IMemberProtocol.PARAM_SEPERATOR + sensorName + IMemberProtocol.PARAM_SEPERATOR + width2 + "x" + height2;
        }
    }

    private boolean needCheckFrameFov(int width2, int height2) {
        return (width2 == 1280 && height2 == 720) || (width2 == 1920 && height2 == 1080) || (width2 == 1920 && height2 == 1088);
    }

    private ParseLutTask obtainParseLutTask(File lutFile, int width2, int height2) {
        ParseLutTask task = this.parseLutTaskPool.acquire();
        if (task == null) {
            return new ParseLutTask(lutFile, width2, height2);
        }
        task.update(lutFile, width2, height2);
        return task;
    }

    private class ParseLutTask implements Runnable {
        private int height;
        private File lutFile;
        private int width;

        private ParseLutTask(File lutFile2, int width2, int height2) {
            update(lutFile2, width2, height2);
        }

        /* access modifiers changed from: private */
        public void update(File lutFile2, int width2, int height2) {
            this.lutFile = lutFile2;
            this.width = width2;
            this.height = height2;
        }

        private void recycle() {
            if (DistortionCorrectionRender.this.parseLutCdl != null) {
                DistortionCorrectionRender.this.parseLutCdl.countDown();
            }
            DistortionCorrectionRender.this.parseLutTaskPool.release(this);
        }

        public void run() {
            DistortionCorrectionRender.this.logd(GLIdentityRender.TAG, "updateUvDataList: lutFileName=" + this.lutFile.getPath());
            try {
                int index = Integer.parseInt(this.lutFile.getName().split("\\.")[0].split("_")[4]);
                if (index > 63 || index < 0 || DistortionCorrectionRender.this.uvDataList.get(index) != null) {
                    DJILog.saveLog("updateUvDataList: index=" + index + " is error!", DistortionCorrectionRender.LUT_DIR_NAME);
                    recycle();
                    return;
                }
                try {
                    DistortionCorrectionRender.this.uvDataList.set(index, DistortionCorrectionRender.this.getLut(this.lutFile, this.width, this.height));
                    recycle();
                } catch (IOException e) {
                    DJILog.saveLog("updateUvDataList: getLut IO error!", DistortionCorrectionRender.LUT_DIR_NAME);
                    recycle();
                }
            } catch (NumberFormatException e2) {
                DJILog.saveLog("updateUvDataList: num format error" + e2, DistortionCorrectionRender.LUT_DIR_NAME);
                recycle();
            } catch (ArrayIndexOutOfBoundsException e3) {
                DJILog.saveLog("updateUvDataList: array out of bounds error" + e3, DistortionCorrectionRender.LUT_DIR_NAME);
                recycle();
            }
        }
    }

    private String getLutFileName(String sensorName, int width2, int height2, int zoomIndex2) {
        return sensorName + "_" + width2 + "x" + height2 + "_to_" + width2 + "x" + height2 + "_" + zoomIndex2 + ".bin";
    }

    private void updateUvDataList(String lutDirPath, int width2, int height2, String sensorName, int zoomIndex2, int lutNum) throws IOException, RuntimeException, InterruptedException {
        clearUvDataList();
        File lutDir = new File(lutDirPath);
        File[] lutFileArr = lutDir.listFiles();
        long timeBeforeParseLut = System.currentTimeMillis();
        if (lutFileArr.length <= 1) {
            for (File lutFile : lutFileArr) {
                obtainParseLutTask(lutFile, width2, height2).run();
            }
        } else {
            int firstLoadMin = Math.max(0, zoomIndex2 - 2);
            int firstLoadMax = Math.min(lutFileArr.length - 1, zoomIndex2 + 2);
            this.parseLutCdl = new CountDownLatch((firstLoadMax - firstLoadMin) + 1);
            for (int i = firstLoadMin; i <= firstLoadMax; i++) {
                File lutFile2 = new File(lutDirPath + IMemberProtocol.PARAM_SEPERATOR + getLutFileName(sensorName, width2, height2, i));
                if (!lutFile2.exists() || !lutFile2.isFile()) {
                    DJILog.saveLog("first load error, file:" + lutFile2.getPath() + "exist=" + lutFile2.exists() + " isFile=" + lutFile2.isFile(), LUT_DIR_NAME);
                    this.parseLutCdl.countDown();
                } else {
                    DJIExecutor.getExecutor().execute(obtainParseLutTask(lutFile2, width2, height2));
                }
            }
            this.parseLutCdl.await(2, TimeUnit.SECONDS);
            for (int i2 = 0; i2 < lutNum; i2++) {
                if (i2 < firstLoadMin || i2 > firstLoadMax) {
                    File lutFile3 = new File(lutDirPath + IMemberProtocol.PARAM_SEPERATOR + getLutFileName(sensorName, width2, height2, i2));
                    if (!lutFile3.exists() || !lutFile3.isFile()) {
                        DJILog.saveLog("second load error, file:" + lutFile3.getPath() + "exist=" + lutFile3.exists() + " isFile=" + lutFile3.isFile(), LUT_DIR_NAME);
                    } else {
                        DJIExecutor.getExecutor().execute(obtainParseLutTask(lutFile3, width2, height2));
                    }
                }
            }
        }
        long parseCost = System.currentTimeMillis() - timeBeforeParseLut;
        logd(TAG, "updateUvDataList: parseCost=" + parseCost);
        DJILog.saveLog("width=" + width2 + " height=" + height2 + " dir=" + lutDir.getPath() + " parseCost=" + parseCost, LUT_DIR_NAME);
    }

    private float[] genVerticesPos(int width2, int height2) {
        int colNum = (width2 / 8) + 1;
        int rowNum = (height2 / 8) + 1;
        float[] rst = new float[(rowNum * colNum * 3)];
        float blockXYWidth = 2.0f / ((float) (colNum - 1));
        float blockXYHeight = 2.0f / ((float) (rowNum - 1));
        int index = 0;
        float curHorizontalOffset = 0.0f;
        float curVerticalOffset = 0.0f;
        long timeBeforeLoop = System.currentTimeMillis();
        int i = 0;
        while (i < rowNum) {
            int j = 0;
            int index2 = index;
            while (j < colNum) {
                int index3 = index2 + 1;
                rst[index2] = curHorizontalOffset - 1.0f;
                int index4 = index3 + 1;
                rst[index3] = curVerticalOffset - 1.0f;
                rst[index4] = 0.0f;
                curHorizontalOffset += blockXYWidth;
                j++;
                index2 = index4 + 1;
            }
            curVerticalOffset += blockXYHeight;
            curHorizontalOffset = 0.0f;
            i++;
            index = index2;
        }
        logd(TAG, "genVerticesPos: loopCost=" + (System.currentTimeMillis() - timeBeforeLoop));
        return rst;
    }

    private int[] genVerticesIndices(int width2, int height2) {
        int colNum = width2 / 8;
        int rowNum = height2 / 8;
        int[] rst = new int[(colNum * rowNum * RENDER_ORDER.length)];
        int index = 0;
        int iOffsetDiff = colNum + 1;
        long timeBeforeLoop = System.currentTimeMillis();
        int i = 0;
        while (i < rowNum) {
            int iOffset = i * iOffsetDiff;
            int index2 = index;
            for (int j = 0; j < colNum; j++) {
                int i0 = iOffset + j;
                int i1 = i0 + 1;
                int i2 = i0 + iOffsetDiff;
                int index3 = index2 + 1;
                rst[index2] = i0;
                int index4 = index3 + 1;
                rst[index3] = i1;
                int index5 = index4 + 1;
                rst[index4] = i2;
                int index6 = index5 + 1;
                rst[index5] = i2;
                int index7 = index6 + 1;
                rst[index6] = i1;
                index2 = index7 + 1;
                rst[index7] = i2 + 1;
            }
            i++;
            index = index2;
        }
        logd(TAG, "genVerticesIndices: loopCost=" + (System.currentTimeMillis() - timeBeforeLoop));
        return rst;
    }

    private String getHexString(byte b) {
        return Integer.toHexString(b & 255);
    }

    private String getHexString(short s) {
        return Integer.toHexString(65535 & s);
    }

    private String getHexString(int s) {
        return Integer.toHexString(s & -1);
    }

    /* access modifiers changed from: private */
    public float[] getLut(File lutFile, int width2, int height2) throws IOException {
        float[] rst = new float[((int) (lutFile.length() / 2))];
        int rstIndex = 1;
        FileInputStream fis = null;
        try {
            FileInputStream fis2 = new FileInputStream(lutFile);
            boolean needContinue = false;
            try {
                byte[] buf = new byte[204800];
                int readLen = fis2.read(buf);
                while (readLen > 0) {
                    int dataLen = readLen + (needContinue ? 1 : 0);
                    needContinue = false;
                    int i = 0;
                    while (true) {
                        if (i >= dataLen) {
                            break;
                        } else if (i == dataLen - 1) {
                            buf[0] = buf[i];
                            needContinue = true;
                            break;
                        } else {
                            byte curData = buf[i];
                            float curRst = ((float) (((curData >> 5) & 7) + ((buf[i + 1] & 255) << 3))) + (((float) (curData & 31)) / 32.0f);
                            if (rstIndex % 2 != 0) {
                                rst[rstIndex] = curRst / ((float) height2);
                                rstIndex--;
                            } else {
                                rst[rstIndex] = curRst / ((float) width2);
                                rstIndex += 3;
                            }
                            i += 2;
                        }
                    }
                    if (needContinue) {
                        readLen = fis2.read(buf, 1, buf.length - 1);
                    } else {
                        readLen = fis2.read(buf);
                    }
                }
                fis2.close();
                return rst;
            } catch (Throwable th) {
                th = th;
                fis = fis2;
                fis.close();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fis.close();
            throw th;
        }
    }
}
