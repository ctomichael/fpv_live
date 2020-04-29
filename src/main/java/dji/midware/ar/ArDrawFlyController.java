package dji.midware.ar;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import com.adobe.xmp.XMPError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.vision.DJITrajectoryHelper;
import dji.midware.R;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.core.Color4BufferList;
import dji.midware.ar.min3d.core.Object3d;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.objectPrimitives.Box;
import dji.midware.ar.min3d.objectPrimitives.Sphere;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Light;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.RenderType;
import dji.midware.ar.min3d.vos.Uv;
import dji.midware.data.model.P3.DataEyeGetPushTrajectory;
import dji.midware.data.model.P3.DataEyeGetPushUAVState;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.util.DJIEventBusUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class ArDrawFlyController extends ArControllerBase {
    private static final String ARROW_TEX_ID = "arrow";
    private static final String ROUTE_PASS_TEX_ID = "route_pass_texture";
    private static final String ROUTE_TEX_ID = "route_texture";
    private static final float SCREEN_STRIP_WIDTH_SCALE = 0.096f;
    private static final String START_POINT_TEX_ID = "startpoint";
    private static final String TAG = "ArDrawFlyController";
    private static final String TERMINAL_TEX_ID = "terminal";
    private static final String UAV_POS_ARROW_TEX = "uav_pos_arrow_texture";
    private static final String UAV_POS_BG_TEX = "uav_pos_background_texture";
    private static ArDrawFlyController instance = null;
    /* access modifiers changed from: private */
    public List<ArPointInfo> arPointInfos;
    Box box;
    /* access modifiers changed from: private */
    public GL10 gl;
    /* access modifiers changed from: private */
    public Object3d greenStrip;
    /* access modifiers changed from: private */
    public Object3d greyStrip;
    /* access modifiers changed from: private */
    public Object3dContainer grid;
    private int lastCurveIndex = -1;
    private long lastGetTrajectoryTime;
    private boolean lastHasStrip;
    private float lastRearDepth;
    private float lastRearX;
    private float lastRearY;
    private float lastSkyLineHorPos;
    private float lastStep = -1.0f;
    private ArDrawFlyControllerListener listener;
    /* access modifiers changed from: private */
    public Object listenerLock = new Object();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean needLogCamState;
    /* access modifiers changed from: private */
    public AtomicBoolean needMoveUavTag = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public AtomicBoolean needUpdateCam = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public AtomicBoolean needUpdateStrip = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public float newCamPitch;
    /* access modifiers changed from: private */
    public float newCamPosX;
    /* access modifiers changed from: private */
    public float newCamPosY;
    /* access modifiers changed from: private */
    public float newCamPosZ;
    /* access modifiers changed from: private */
    public float newCamRoll;
    /* access modifiers changed from: private */
    public float newCamYaw;
    /* access modifiers changed from: private */
    public int newCurveIndex;
    /* access modifiers changed from: private */
    public float newStep;
    /* access modifiers changed from: private */
    public float newUavPosX;
    /* access modifiers changed from: private */
    public float newUavPosY;
    /* access modifiers changed from: private */
    public float newUavRotZ;
    int screeny = 0;
    /* access modifiers changed from: private */
    public Object3dContainer strip;
    /* access modifiers changed from: private */
    public float stripWidth = 0.6f;
    private List<DataEyeGetPushTrajectory.PolynomialTrajectory> trajectoryList;
    /* access modifiers changed from: private */
    public Object3d uavPosTag;
    /* access modifiers changed from: private */
    public List<Number3d> vertices;

    public interface ArDrawFlyControllerListener {
        void onSkyLineChange(float f);

        void onStripRearScreenCoorChange(boolean z, float f, float f2, float f3);
    }

    public void setListener(ArDrawFlyControllerListener listener2) {
        synchronized (this.listenerLock) {
            this.listener = listener2;
        }
    }

    private ArDrawFlyController() {
    }

    public static synchronized ArDrawFlyController getInstance() {
        ArDrawFlyController arDrawFlyController;
        synchronized (ArDrawFlyController.class) {
            if (instance == null) {
                instance = new ArDrawFlyController();
            }
            arDrawFlyController = instance;
        }
        return arDrawFlyController;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileWriter.<init>(java.io.File, boolean):void throws java.io.IOException}
     arg types: [java.io.File, int]
     candidates:
      ClspMth{java.io.FileWriter.<init>(java.lang.String, boolean):void throws java.io.IOException}
      ClspMth{java.io.FileWriter.<init>(java.io.File, boolean):void throws java.io.IOException} */
    private void logToFile(File file, String s) throws IOException {
        if (file != null && s != null) {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(s, 0, s.length());
            writer.flush();
            writer.close();
        }
    }

    private void logcatToFile(String log) {
        ArUtil.logArMsgToFile("DrawCtrl", log);
    }

    private void logTrajectoriesToFile(File file, Collection<DJITrajectoryHelper.TrajectoryInfo> trajectoryInfos) throws IOException {
        if (file != null && trajectoryInfos != null && trajectoryInfos.size() > 0 && !file.isDirectory()) {
            StringBuffer sb = new StringBuffer();
            for (DJITrajectoryHelper.TrajectoryInfo info : trajectoryInfos) {
                DataEyeGetPushTrajectory.PolynomialTrajectory[] polynomialTrajectoryArr = info.mPolynomialTrajectories;
                for (DataEyeGetPushTrajectory.PolynomialTrajectory trajectory : polynomialTrajectoryArr) {
                    float[] fArr = trajectory.mPolyXAxis;
                    int length = fArr.length;
                    for (int i = 0; i < length; i++) {
                        sb.append(fArr[i] + " ");
                    }
                    sb.append("\n");
                    float[] fArr2 = trajectory.mPolyYAxis;
                    int length2 = fArr2.length;
                    for (int i2 = 0; i2 < length2; i2++) {
                        sb.append(fArr2[i2] + " ");
                    }
                    sb.append("\n");
                    float[] fArr3 = trajectory.mPolyZAxis;
                    int length3 = fArr3.length;
                    for (int i3 = 0; i3 < length3; i3++) {
                        sb.append(fArr3[i3] + " ");
                    }
                    sb.append("\n");
                }
                sb.append("\n");
            }
            logToFile(file, sb.toString());
        }
    }

    /* access modifiers changed from: private */
    public void addTextures() {
        if (!Shared.textureManager().contains(ROUTE_TEX_ID)) {
            Bitmap b = Utils.makeBitmapFromResourceId(this.context, R.drawable.draw_route);
            Shared.textureManager().addTextureId(b, ROUTE_TEX_ID, false);
            b.recycle();
        }
        if (!Shared.textureManager().contains(ROUTE_PASS_TEX_ID)) {
            Bitmap b2 = Utils.makeBitmapFromResourceId(this.context, R.drawable.draw_route_pass);
            Shared.textureManager().addTextureId(b2, ROUTE_PASS_TEX_ID, false);
            b2.recycle();
        }
        if (!Shared.textureManager().contains(UAV_POS_BG_TEX)) {
            Bitmap backgroundImg = Utils.makeBitmapFromResourceId(this.context, R.drawable.btn_draw_started);
            Shared.textureManager().addTextureId(backgroundImg, UAV_POS_BG_TEX);
            backgroundImg.recycle();
        }
        if (!Shared.textureManager().contains(UAV_POS_ARROW_TEX)) {
            Bitmap arrowImg = Utils.makeBitmapFromResourceId(this.context, R.drawable.btn_draw_start_plane);
            Shared.textureManager().addTextureId(arrowImg, UAV_POS_ARROW_TEX);
            arrowImg.recycle();
        }
    }

    public void init(Context context, final float verFov, final float aspect) {
        this.context = context;
        DJIEventBusUtil.register(this);
        this.displayView = new Min3dView(context) {
            /* class dji.midware.ar.ArDrawFlyController.AnonymousClass1 */
            long initTimeStamp;
            float pitch;
            float roll;
            int updateTime;
            float yaw;

            public void initScene() {
                ArDrawFlyController.this.addTextures();
                ArDrawFlyController.getInstance().mScene = this.scene;
                GL10 unused = ArDrawFlyController.this.gl = getRenderer().gl();
                this.scene.backgroundColor().setAll(0);
                this.scene.lights().add(new Light());
                ArDrawFlyController.this.mScene.camera().frustum.zNear(0.1f);
                ArDrawFlyController.this.mScene.camera().frustum.zFar(10000.0f);
                ArDrawFlyController.this.mScene.camera().upAxis.setAll(0.0f, 1.0f, 0.0f);
                Object3dContainer unused2 = ArDrawFlyController.this.grid = ArDrawFlyController.this.getGrid(2.5f, 2.5f, 30, 30, 0.0f);
                ArDrawFlyController.this.grid.isVisible(false);
                ArDrawFlyController.this.mScene.addChild(ArDrawFlyController.this.grid);
                ArDrawFlyController.this.updateVerticalFov(verFov, aspect);
            }

            public void updateScene() {
                if (ArDrawFlyController.this.displayView.isInititialized()) {
                    if (ArDrawFlyController.this.needUpdateCam.getAndSet(false)) {
                        ArDrawFlyController.this.moveCamera5(ArDrawFlyController.this.newCamPosX, ArDrawFlyController.this.newCamPosY, ArDrawFlyController.this.newCamPosZ, ArDrawFlyController.this.newCamPitch, ArDrawFlyController.this.newCamRoll, ArDrawFlyController.this.newCamYaw);
                    }
                    if (ArDrawFlyController.this.needUpdateStrip.getAndSet(false)) {
                        if (ArDrawFlyController.this.strip != null) {
                            ArDrawFlyController.this.mScene.removeChild(ArDrawFlyController.this.strip);
                        }
                        float unused = ArDrawFlyController.this.stripWidth = ArDrawFlyController.this.calcStripWidth();
                        List unused2 = ArDrawFlyController.this.arPointInfos = ArDrawFlyController.this.getArPointInfosFromCurve(DJITrajectoryHelper.getInstance().getTrajectoryInfoSet(), 0.0f, 1.0f, 0.01f);
                        List unused3 = ArDrawFlyController.this.vertices = ArDrawFlyController.this.calcVertices(ArDrawFlyController.this.arPointInfos, ArDrawFlyController.this.stripWidth);
                        new StringBuffer();
                        new StringBuffer();
                        if (ArDrawFlyController.this.vertices != null && !ArDrawFlyController.this.vertices.isEmpty()) {
                            Object3dContainer unused4 = ArDrawFlyController.this.strip = new Object3dContainer();
                            Object3d unused5 = ArDrawFlyController.this.greenStrip = ArDrawFlyController.this.getStrip(ArDrawFlyController.this.vertices, ArDrawFlyController.ROUTE_TEX_ID, 255);
                            Object3d unused6 = ArDrawFlyController.this.greyStrip = ArDrawFlyController.this.getStrip(ArDrawFlyController.this.vertices, ArDrawFlyController.ROUTE_PASS_TEX_ID, 0);
                            ArDrawFlyController.this.strip.addChild(ArDrawFlyController.this.greenStrip);
                            ArDrawFlyController.this.strip.addChild(ArDrawFlyController.this.greyStrip);
                            List<Object3d> arrowList = ArDrawFlyController.this.getArrows(R.drawable.draw_arrow, ArDrawFlyController.this.stripWidth, ArDrawFlyController.this.stripWidth / 2.0f, 20, ArDrawFlyController.this.stripWidth * 2.0f);
                            if (arrowList != null) {
                                for (Object3d arrow : arrowList) {
                                    ArDrawFlyController.this.strip.addChild(arrow);
                                }
                            }
                            List<Object3d> tagList = ArDrawFlyController.this.getStartAndTerminalPointTags(ArDrawFlyController.this.arPointInfos, R.drawable.btn_draw_starter, R.drawable.btn_draw_end);
                            if (tagList != null) {
                                for (Object3d tag : tagList) {
                                    ArDrawFlyController.this.strip.addChild(tag);
                                }
                            }
                            ArDrawFlyController.this.mScene.addChild(ArDrawFlyController.this.strip);
                            if (ArDrawFlyController.this.uavPosTag != null) {
                                ArDrawFlyController.this.mScene.removeChild(ArDrawFlyController.this.uavPosTag);
                            }
                            Object3d unused7 = ArDrawFlyController.this.uavPosTag = ArDrawFlyController.this.getUavPosTag(ArDrawFlyController.this.stripWidth * 1.33f * 1.25f);
                            ArDrawFlyController.this.mScene.addChild(ArDrawFlyController.this.uavPosTag);
                            ArDrawFlyController.this.onUavPosOnTrajectoryUpdate(0, 0.01f);
                        } else if (ArDrawFlyController.this.uavPosTag != null) {
                            ArDrawFlyController.this.mScene.removeChild(ArDrawFlyController.this.uavPosTag);
                        }
                    }
                    if (!(ArDrawFlyController.this.uavPosTag == null || ArDrawFlyController.this.strip == null || ArDrawFlyController.this.greenStrip == null || !ArDrawFlyController.this.needMoveUavTag.getAndSet(false))) {
                        ArDrawFlyController.this.uavPosTag.position().setAll(ArDrawFlyController.this.newUavPosX, ArDrawFlyController.this.newUavPosY, ArDrawFlyController.this.strip.position().z + ArDrawFlyController.this.greenStrip.position().z + 1.0f);
                        ArDrawFlyController.this.uavPosTag.rotation().z = ArDrawFlyController.this.newUavRotZ;
                        ArDrawFlyController.this.changeStripSegAlpha(ArDrawFlyController.this.greenStrip, 0, 255, ArDrawFlyController.this.calcArPointIndexByCurveIndexAndStep(ArDrawFlyController.this.newCurveIndex, ArDrawFlyController.this.newStep, 0.0f, 1.0f, 0.01f) * 2);
                        ArDrawFlyController.this.changeStripSegAlpha(ArDrawFlyController.this.greyStrip, 255, 0, ArDrawFlyController.this.calcArPointIndexByCurveIndexAndStep(ArDrawFlyController.this.newCurveIndex, ArDrawFlyController.this.newStep, 0.0f, 1.0f, 0.01f) * 2);
                    }
                    synchronized (ArDrawFlyController.this.listenerLock) {
                        ArDrawFlyController.this.checkAndInvokeStripRearListener();
                        ArDrawFlyController.this.checkAndInvokeSkyLineListener();
                    }
                    this.updateTime++;
                }
            }
        };
        this.displayView.init();
    }

    /* access modifiers changed from: private */
    public float calcStripWidth() {
        float droneHeight = (float) (DataOsdGetPushCommon.getInstance().getHeight() / 10);
        if (this.mScene.camera().fovy <= 0.0f || this.mScene.camera().aspect <= 0.0f) {
            float screenAspect = this.displayView.getRenderer().getSurfaceAspectRatio();
            float shortLength = this.mScene.camera().frustum.shortSideLength();
            float zNear = this.mScene.camera().frustum.zNear();
            logcatToFile("calc strip width: without fovy: hei: " + droneHeight + ", asp: " + screenAspect + ", sl: " + shortLength + ", zn: " + zNear);
            return (((shortLength * screenAspect) * droneHeight) / zNear) * SCREEN_STRIP_WIDTH_SCALE;
        }
        float fovx = this.mScene.camera().fovy * this.mScene.camera().aspect;
        logcatToFile("calc strip width: with fovy: hei: " + droneHeight + ", fovx: " + fovx);
        return (float) (((double) (2.0f * droneHeight)) * Math.tan(Math.toRadians((double) (fovx / 2.0f))) * 0.09600000083446503d);
    }

    private boolean checkValApproximability(float a, float b, float tolerance) {
        return Math.abs(a - b) <= tolerance;
    }

    /* access modifiers changed from: private */
    public void checkAndInvokeStripRearListener() {
        boolean hasStrip;
        if (this.listener != null) {
            if (this.arPointInfos == null || this.arPointInfos.size() <= 0) {
                hasStrip = false;
            } else {
                hasStrip = true;
            }
            float rearX = 0.0f;
            float rearY = 0.0f;
            float rearDepth = 0.0f;
            if (hasStrip) {
                ArPointInfo rear = this.arPointInfos.get(this.arPointInfos.size() - 1);
                float[] rearState = ArUtil.worldCoor2ScreenCoor((GL11) this.displayView.getRenderer().gl(), rear.posX, rear.posY, rear.posZ);
                rearX = rearState[0];
                rearY = rearState[1];
                rearDepth = (float) Math.sqrt((double) calcDistanceSquare(this.mScene.camera().position, new Number3d(rear.posX, rear.posY, rear.posZ)));
            }
            if (this.lastHasStrip != hasStrip || checkValApproximability(rearX, this.lastRearX, 1.0f) || checkValApproximability(rearY, this.lastRearY, 1.0f) || checkValApproximability(rearDepth, this.lastRearDepth, 1.0f)) {
                this.lastHasStrip = hasStrip;
                this.lastRearX = rearX;
                this.lastRearY = rearY;
                this.lastRearDepth = rearDepth;
                if (hasStrip) {
                    this.listener.onStripRearScreenCoorChange(hasStrip, rearX, rearY, rearDepth);
                } else {
                    this.listener.onStripRearScreenCoorChange(hasStrip, 0.0f, 0.0f, 0.0f);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkAndInvokeSkyLineListener() {
        if (this.listener != null) {
            float skyLineHorPos = getSkyLinePosOnScreen();
            if (!checkValApproximability(skyLineHorPos, this.lastSkyLineHorPos, 1.0f)) {
                this.lastSkyLineHorPos = skyLineHorPos;
                this.listener.onSkyLineChange(skyLineHorPos);
            }
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        if (this.displayView != null) {
            this.displayView.onPause();
            this.displayView.unInit();
        }
        this.mScene = null;
        this.strip = null;
        this.context = null;
        this.displayView = null;
    }

    public void updateViewport(int w, int h) {
        if (this.gl != null) {
            this.gl.glViewport(0, 0, w, h);
        }
    }

    public void updateVerticalFov(float verticalFov) {
        if (this.mScene != null) {
            this.mScene.camera().frustum.shortSideLength((float) ArUtil.calHeightFromFov(verticalFov, this.mScene.camera().frustum.zNear()));
        }
    }

    public void updateVerticalFov(float verticalFov, float aspect) {
        if (this.mScene != null) {
            this.mScene.camera().fovy = verticalFov;
            this.mScene.camera().aspect = aspect;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushUAVState uavState) {
        this.newCamPosX = uavState.getPosX();
        this.newCamPosY = uavState.getPosY();
        this.newCamPosZ = uavState.getPosZ();
        this.newCamPitch = uavState.getGimbalPitch();
        this.newCamRoll = uavState.getGimbalRoll();
        this.newCamYaw = uavState.getGimbalYaw();
        this.needUpdateCam.set(true);
        onUavPosOnTrajectoryUpdate(uavState.getCurrentTrajectoryIndex(), uavState.getCurrentStepInTrajectory());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJITrajectoryHelper.TrajectoryEvent trajectoryEvent) {
        if (trajectoryEvent == DJITrajectoryHelper.TrajectoryEvent.TRAJECTORY_FINISHED) {
            this.needUpdateStrip.set(true);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileWriter.<init>(java.io.File, boolean):void throws java.io.IOException}
     arg types: [java.io.File, int]
     candidates:
      ClspMth{java.io.FileWriter.<init>(java.lang.String, boolean):void throws java.io.IOException}
      ClspMth{java.io.FileWriter.<init>(java.io.File, boolean):void throws java.io.IOException} */
    private void logTrajectoryToFile(File file, Collection<DJITrajectoryHelper.TrajectoryInfo> trajectoryInfos) throws IOException {
        if (file != null && !file.isDirectory()) {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            StringBuffer sb = new StringBuffer();
            for (DJITrajectoryHelper.TrajectoryInfo info : trajectoryInfos) {
                DataEyeGetPushTrajectory.PolynomialTrajectory[] polynomialTrajectoryArr = info.mPolynomialTrajectories;
                for (DataEyeGetPushTrajectory.PolynomialTrajectory trajectories : polynomialTrajectoryArr) {
                    for (float val : trajectories.mPolyXAxis) {
                        sb.append(val).append(" ");
                    }
                    for (float val2 : trajectories.mPolyYAxis) {
                        sb.append(val2).append(" ");
                    }
                    for (float val3 : trajectories.mPolyZAxis) {
                        sb.append(val3).append(" ");
                    }
                }
                writer.write(sb.toString());
                writer.newLine();
                sb.delete(0, sb.length());
            }
            writer.flush();
            writer.close();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileWriter.<init>(java.io.File, boolean):void throws java.io.IOException}
     arg types: [java.io.File, int]
     candidates:
      ClspMth{java.io.FileWriter.<init>(java.lang.String, boolean):void throws java.io.IOException}
      ClspMth{java.io.FileWriter.<init>(java.io.File, boolean):void throws java.io.IOException} */
    private void logCameraState(File file) throws IOException {
        if (file != null && !file.isDirectory()) {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(this.newCamPosX + "");
            writer.newLine();
            writer.write(this.newCamPosY + "");
            writer.newLine();
            writer.write(this.newCamPosZ + "");
            writer.newLine();
            writer.write(this.newCamPitch + "");
            writer.newLine();
            writer.write(this.newCamRoll + "");
            writer.newLine();
            writer.write(this.newCamYaw + "");
            writer.flush();
            writer.close();
        }
    }

    static class ArPointInfo {
        public float normalX;
        public float normalY;
        public float normalZ;
        public float posX;
        public float posY;
        public float posZ;

        public ArPointInfo() {
        }

        public ArPointInfo(float posX2, float posY2, float posZ2, float normalX2, float normalY2, float normalZ2) {
            this.posX = posY2;
            this.posY = posX2;
            this.posZ = -posZ2;
            this.posZ = 0.0f;
            this.normalX = normalY2;
            this.normalY = normalX2;
            this.normalZ = -normalZ2;
            this.normalZ = 0.0f;
        }

        public Number3d getPos() {
            return new Number3d(this.posX, this.posY, this.posZ);
        }

        public String toString() {
            return "ArPointInfo: {posX: " + this.posX + ", posY: " + this.posY + ", posZ: " + this.posZ + ", normalX: " + this.normalX + ", normalY: " + this.normalY + ", normalZ: " + this.normalZ + "}\n";
        }
    }

    /* access modifiers changed from: private */
    public void onUavPosOnTrajectoryUpdate(int curveIndex, float step) {
        if (this.trajectoryList != null && this.trajectoryList.size() >= curveIndex + 1 && curveIndex >= 0 && step >= 0.0f) {
            if (curveIndex != this.lastCurveIndex || ((double) Math.abs(step - this.lastStep)) > 1.0E-4d) {
                ArPointInfo posPoint = samplingFromCurve(this.trajectoryList.get(curveIndex), step);
                float rotNormalX = posPoint.normalX;
                float rotNormalY = posPoint.normalY;
                float rot = (float) Math.toDegrees(Math.acos(((double) rotNormalY) / Math.sqrt((double) ((rotNormalX * rotNormalX) + (rotNormalY * rotNormalY)))));
                this.newUavPosX = posPoint.posX;
                this.newUavPosY = posPoint.posY;
                if (rotNormalX > 0.0f) {
                    rot = -rot;
                }
                this.newUavRotZ = rot;
                this.newCurveIndex = curveIndex;
                this.newStep = step;
                this.needMoveUavTag.set(true);
            }
            this.lastCurveIndex = curveIndex;
            this.lastStep = step;
        }
    }

    /* access modifiers changed from: private */
    public void changeStripSegAlpha(Object3d strip2, int alpha1, int alpha2, int segPointIndex) {
        int i;
        if (strip2 != null) {
            Color4BufferList colors = strip2.colors();
            for (int i2 = 0; i2 < colors.size(); i2++) {
                if (i2 < segPointIndex) {
                    i = alpha1;
                } else {
                    i = alpha2;
                }
                colors.set(i2, new Color4(255, 255, 255, i));
            }
        }
    }

    /* access modifiers changed from: private */
    public Object3d getUavPosTag(float width) {
        Object3dContainer tag = new Object3dContainer();
        Object3d bg = getRectWithTexture(UAV_POS_BG_TEX, width, width);
        Object3d arrow = getRectWithTexture(UAV_POS_ARROW_TEX, width, width);
        Number3d position = arrow.position();
        position.z = (float) (((double) position.z) + 0.01d);
        tag.addChild(bg);
        tag.addChild(arrow);
        return tag;
    }

    private ArPointInfo samplingFromCurve(DataEyeGetPushTrajectory.PolynomialTrajectory curveInfo, float step) {
        float[] xPoly = curveInfo.mPolyXAxis;
        float[] yPoly = curveInfo.mPolyYAxis;
        float[] zPoly = curveInfo.mPolyZAxis;
        float[] t = new float[6];
        for (int j = 0; j < 6; j++) {
            t[j] = (float) Math.pow((double) step, (double) j);
        }
        float posx = curveSampling(xPoly, 0, t, 0, 6);
        float posy = curveSampling(yPoly, 0, t, 0, 6);
        float curveSampling = curveSampling(zPoly, 0, t, 0, 6);
        return new ArPointInfo(posx, posy, 0.0f, curveSampling(findDerivative(xPoly), 0, t, 0, 5), curveSampling(findDerivative(yPoly), 0, t, 0, 5), curveSampling(findDerivative(zPoly), 0, t, 0, 5));
    }

    /* access modifiers changed from: private */
    public int calcArPointIndexByCurveIndexAndStep(int curveIndex, float step, float sampleFrom, float sampleTo, float sampleInterval) {
        if (this.arPointInfos == null || sampleTo < sampleFrom || curveIndex < 0) {
            return -1;
        }
        return (int) (((float) (((int) (((sampleTo - sampleFrom) / sampleInterval) + 1.0f)) * curveIndex)) + (step / sampleInterval) + 1.0f);
    }

    public static List<DataEyeGetPushTrajectory.PolynomialTrajectory> parseTrackDataFromFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        LinkedList<DataEyeGetPushTrajectory.PolynomialTrajectory> rst = new LinkedList<>();
        LinkedList<Float> floats = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            String[] numStrings = line.replace("\n", "").replace("\r", "").split(" ");
            if (numStrings != null) {
                for (String s : numStrings) {
                    floats.add(Float.valueOf(Float.parseFloat(s)));
                }
            }
        }
        reader.close();
        while (floats.size() >= 18) {
            float[] x = new float[6];
            float[] y = new float[6];
            float[] z = new float[6];
            for (int i = 0; i < 6; i++) {
                x[i] = ((Float) floats.poll()).floatValue();
            }
            for (int i2 = 0; i2 < 6; i2++) {
                y[i2] = ((Float) floats.poll()).floatValue();
            }
            for (int i3 = 0; i3 < 6; i3++) {
                z[i3] = ((Float) floats.poll()).floatValue();
            }
            rst.add(new DataEyeGetPushTrajectory.PolynomialTrajectory(x, y, z));
        }
        return rst;
    }

    public float curveSampling(float[] factors, int factorsFrom, float[] vals, int valsFrom, int length) {
        if (factors == null || vals == null || factors.length - factorsFrom < length || vals.length - valsFrom < length) {
            return 0.0f;
        }
        float rst = 0.0f;
        for (int i = 0; i < length; i++) {
            rst += factors[i + factorsFrom] * vals[i + valsFrom];
        }
        return rst;
    }

    public float[] findDerivative(float[] factors) {
        if (factors == null || factors.length < 1) {
            return null;
        }
        float[] rst = new float[(factors.length - 1)];
        for (int i = 0; i < rst.length; i++) {
            rst[i] = factors[i + 1] * ((float) (i + 1));
        }
        return rst;
    }

    public List<ArPointInfo> getArPointInfosFromCurve(Collection<DJITrajectoryHelper.TrajectoryInfo> trajectoryInfoSet, float from, float to, float interval) {
        this.trajectoryList = new LinkedList();
        for (DJITrajectoryHelper.TrajectoryInfo info : trajectoryInfoSet) {
            this.trajectoryList.addAll(Arrays.asList(info.mPolynomialTrajectories));
        }
        return getArPointInfosFromCurve(this.trajectoryList, from, to, interval);
    }

    public List<ArPointInfo> getArPointInfosFromCurve(List<DataEyeGetPushTrajectory.PolynomialTrajectory> curveInfos, float from, float to, float interval) {
        List<ArPointInfo> rst = new LinkedList<>();
        for (DataEyeGetPushTrajectory.PolynomialTrajectory ci : curveInfos) {
            rst.addAll(getArPointInfosFromCurve(ci, from, to, interval));
        }
        return rst;
    }

    public List<ArPointInfo> getArPointInfosFromCurve(DataEyeGetPushTrajectory.PolynomialTrajectory curveInfo, float from, float to, float interval) {
        if (from >= to || interval <= 0.0f) {
            return null;
        }
        float[] xPoly = curveInfo.mPolyXAxis;
        float[] yPoly = curveInfo.mPolyYAxis;
        float[] zPoly = curveInfo.mPolyZAxis;
        float[] t = new float[6];
        ArrayList arrayList = new ArrayList((int) (((to - from) / interval) + 1.0f));
        float i = from;
        while (i < to) {
            for (int j = 0; j < 6; j++) {
                t[j] = (float) Math.pow((double) i, (double) j);
            }
            ArrayList arrayList2 = arrayList;
            arrayList2.add(new ArPointInfo(curveSampling(xPoly, 0, t, 0, 6), curveSampling(yPoly, 0, t, 0, 6), curveSampling(zPoly, 0, t, 0, 6), curveSampling(findDerivative(xPoly), 0, t, 0, 5), curveSampling(findDerivative(yPoly), 0, t, 0, 5), curveSampling(findDerivative(zPoly), 0, t, 0, 5)));
            i += interval;
        }
        return arrayList;
    }

    public static boolean getStripEdgePointBaseOnZ(ArPointInfo arPointInfo, float width, Number3d leftDst, Number3d rightDst) {
        if (arPointInfo == null || leftDst == null || rightDst == null) {
            return false;
        }
        leftDst.z = 0.0f;
        rightDst.z = 0.0f;
        if ((arPointInfo.normalX * arPointInfo.normalX) + (arPointInfo.normalY * arPointInfo.normalY) == 0.0f) {
            return false;
        }
        float absWCosa = (float) Math.abs(((double) (arPointInfo.normalX * width)) / (Math.sqrt((double) ((arPointInfo.normalX * arPointInfo.normalX) + (arPointInfo.normalY * arPointInfo.normalY))) * 2.0d));
        float absWCosb = (float) Math.abs(((double) (arPointInfo.normalY * width)) / (Math.sqrt((double) ((arPointInfo.normalX * arPointInfo.normalX) + (arPointInfo.normalY * arPointInfo.normalY))) * 2.0d));
        if (arPointInfo.normalX >= 0.0f && arPointInfo.normalY >= 0.0f) {
            leftDst.x = arPointInfo.posX - absWCosb;
            leftDst.y = arPointInfo.posY + absWCosa;
            rightDst.x = arPointInfo.posX + absWCosb;
            rightDst.y = arPointInfo.posY - absWCosa;
        }
        if (arPointInfo.normalX < 0.0f && arPointInfo.normalY >= 0.0f) {
            leftDst.x = arPointInfo.posX - absWCosb;
            leftDst.y = arPointInfo.posY - absWCosa;
            rightDst.x = arPointInfo.posX + absWCosb;
            rightDst.y = arPointInfo.posY + absWCosa;
        }
        if (arPointInfo.normalX < 0.0f && arPointInfo.normalY < 0.0f) {
            leftDst.x = arPointInfo.posX + absWCosb;
            leftDst.y = arPointInfo.posY - absWCosa;
            rightDst.x = arPointInfo.posX - absWCosb;
            rightDst.y = arPointInfo.posY + absWCosa;
        }
        if (arPointInfo.normalX >= 0.0f && arPointInfo.normalY < 0.0f) {
            leftDst.x = arPointInfo.posX + absWCosb;
            leftDst.y = arPointInfo.posY + absWCosa;
            rightDst.x = arPointInfo.posX - absWCosb;
            rightDst.y = arPointInfo.posY - absWCosa;
        }
        return true;
    }

    private Object3d getRectWithTexture(String textureId, float width, float height) {
        Object3d rect = new Object3d(4, 2, true, false, false);
        rect.vertices().addVertex(new Number3d((-width) / 2.0f, (-height) / 2.0f, 0.0f), new Uv(0.0f, 1.0f), null, null);
        rect.vertices().addVertex(new Number3d((-width) / 2.0f, height / 2.0f, 0.0f), new Uv(0.0f, 0.0f), null, null);
        rect.vertices().addVertex(new Number3d(width / 2.0f, (-height) / 2.0f, 0.0f), new Uv(1.0f, 1.0f), null, null);
        rect.vertices().addVertex(new Number3d(width / 2.0f, height / 2.0f, 0.0f), new Uv(1.0f, 0.0f), null, null);
        rect.renderType(RenderType.TRIANGLE_STRIP);
        Utils.addQuad(rect, 0, 1, 3, 2);
        rect.texturesEnabled(true);
        rect.doubleSidedEnabled(true);
        rect.textures().addById(textureId);
        return rect;
    }

    /* access modifiers changed from: private */
    public List<Object3d> getStartAndTerminalPointTags(List<ArPointInfo> arPointInfoList, int startpointResId, int terminalResId) {
        float f;
        float f2;
        if (arPointInfoList == null || arPointInfoList.size() <= 1) {
            return null;
        }
        Bitmap startPointImg = Utils.makeBitmapFromResourceId(this.context, startpointResId);
        Bitmap terminalImg = Utils.makeBitmapFromResourceId(this.context, terminalResId);
        if (!Shared.textureManager().contains(START_POINT_TEX_ID)) {
            Shared.textureManager().addTextureId(startPointImg, START_POINT_TEX_ID, false);
        }
        if (!Shared.textureManager().contains(TERMINAL_TEX_ID)) {
            Shared.textureManager().addTextureId(terminalImg, TERMINAL_TEX_ID, false);
        }
        Object3d startPointUpperTag = getRectWithTexture(START_POINT_TEX_ID, this.stripWidth * 1.25f * 1.2f, this.stripWidth * 1.25f * 1.2f);
        Object3d startPointLowerTag = getRectWithTexture(START_POINT_TEX_ID, this.stripWidth * 1.25f * 1.2f, this.stripWidth * 1.25f * 1.2f);
        Object3d terminalUpperTag = getRectWithTexture(TERMINAL_TEX_ID, this.stripWidth * 1.25f * 1.2f, this.stripWidth * 1.25f * 1.2f);
        Object3d terminalLowerTag = getRectWithTexture(TERMINAL_TEX_ID, this.stripWidth * 1.25f * 1.2f, this.stripWidth * 1.25f * 1.2f);
        ArPointInfo startPoint = arPointInfoList.get(1);
        ArPointInfo terminal = arPointInfoList.get(arPointInfoList.size() - 2);
        startPointUpperTag.position().setAll(startPoint.posX, startPoint.posY, startPoint.posZ + 0.02f);
        startPointLowerTag.position().setAll(startPoint.posX, startPoint.posY, startPoint.posZ - 0.02f);
        terminalUpperTag.position().setAll(terminal.posX, terminal.posY, terminal.posZ + 0.02f);
        terminalLowerTag.position().setAll(terminal.posX, terminal.posY, terminal.posZ - 0.02f);
        float startRotNormalX = startPoint.normalX;
        float startRotNormalY = startPoint.normalY;
        float terminalRotNormalX = terminal.normalX;
        float terminalRotNormalY = terminal.normalY;
        float startPointRot = (float) Math.toDegrees(Math.acos(((double) startRotNormalY) / Math.sqrt((double) ((startRotNormalX * startRotNormalX) + (startRotNormalY * startRotNormalY)))));
        float terminalRot = (float) Math.toDegrees(Math.acos(((double) terminalRotNormalY) / Math.sqrt((double) ((terminalRotNormalX * terminalRotNormalX) + (terminalRotNormalY * terminalRotNormalY)))));
        Number3d rotation = startPointUpperTag.rotation();
        if (startRotNormalX > 0.0f) {
            f = -startPointRot;
        } else {
            f = startPointRot;
        }
        rotation.z = f;
        Number3d rotation2 = startPointLowerTag.rotation();
        if (startRotNormalX > 0.0f) {
            startPointRot = -startPointRot;
        }
        rotation2.z = startPointRot;
        Number3d rotation3 = terminalUpperTag.rotation();
        if (terminalRotNormalX > 0.0f) {
            f2 = -terminalRot;
        } else {
            f2 = terminalRot;
        }
        rotation3.z = f2;
        Number3d rotation4 = terminalLowerTag.rotation();
        if (terminalRotNormalX > 0.0f) {
            terminalRot = -terminalRot;
        }
        rotation4.z = terminalRot;
        List<Object3d> tags = new ArrayList<>(4);
        tags.add(startPointUpperTag);
        tags.add(terminalUpperTag);
        return tags;
    }

    private Object3d getHorLines(float verPos, float left, float right, float lineWidthHalf, float verOffset) {
        Object3d line = new Object3d(4, 2, false, false, false);
        line.vertices().addVertex(new Number3d(left, (verPos - lineWidthHalf) + verOffset, 0.0f), null, null, null);
        line.vertices().addVertex(new Number3d(left, verPos + lineWidthHalf + verOffset, 0.0f), null, null, null);
        line.vertices().addVertex(new Number3d(right, (verPos - lineWidthHalf) + verOffset, 0.0f), null, null, null);
        line.vertices().addVertex(new Number3d(right, verPos + lineWidthHalf + verOffset, 0.0f), null, null, null);
        line.renderType(RenderType.LINES);
        Utils.addQuad(line, 0, 1, 3, 2);
        line.doubleSidedEnabled(true);
        return line;
    }

    private Object3d getVerLines(float horPos, float top, float bottom, float lineWidthHalf, float verOffset) {
        Object3d line = new Object3d(4, 2, false, false, false);
        line.vertices().addVertex(new Number3d(horPos - lineWidthHalf, bottom + verOffset, 0.0f), null, null, null);
        line.vertices().addVertex(new Number3d(horPos - lineWidthHalf, top + verOffset, 0.0f), null, null, null);
        line.vertices().addVertex(new Number3d(horPos + lineWidthHalf, bottom + verOffset, 0.0f), null, null, null);
        line.vertices().addVertex(new Number3d(horPos + lineWidthHalf, top + verOffset, 0.0f), null, null, null);
        line.renderType(RenderType.LINES);
        line.doubleSidedEnabled(true);
        Utils.addQuad(line, 0, 1, 3, 2);
        return line;
    }

    /* access modifiers changed from: private */
    public Object3dContainer getGrid(float gridWidth, float gridHeight, int horLinesNum, int verLinesNum, float verOffset) {
        float currentVerPos;
        float currentHorPos;
        float f;
        float f2;
        float f3;
        float f4;
        Object3dContainer grid2 = new Object3dContainer();
        float left = (-(gridWidth * ((float) (verLinesNum - 1)))) / 2.0f;
        float right = -left;
        float top = (gridHeight * ((float) (horLinesNum - 1))) / 2.0f;
        float bottom = -top;
        if (horLinesNum % 2 != 0) {
            grid2.addChild(getHorLines(0.0f, left, right, 5.0E-4f, verOffset));
            currentVerPos = 0.0f + gridHeight;
        } else {
            currentVerPos = 0.0f + (gridHeight / 2.0f);
        }
        for (int i = 0; i < horLinesNum / 2; i++) {
            if (i > (horLinesNum / 2) - 2) {
                f3 = 3.0f;
            } else {
                f3 = 5.0E-4f;
            }
            grid2.addChild(getHorLines(currentVerPos, left, right, f3, verOffset));
            float f5 = -currentVerPos;
            if (i > (horLinesNum / 2) - 2) {
                f4 = 3.0f;
            } else {
                f4 = 5.0E-4f;
            }
            grid2.addChild(getHorLines(f5, left, right, f4, verOffset));
            currentVerPos += gridHeight;
        }
        if (verLinesNum % 2 != 0) {
            grid2.addChild(getVerLines(0.0f, top, bottom, 5.0E-4f, verOffset));
            currentHorPos = 0.0f + gridWidth;
        } else {
            currentHorPos = 0.0f + (gridWidth / 2.0f);
        }
        for (int i2 = 0; i2 < verLinesNum / 2; i2++) {
            if (i2 == verLinesNum / 2) {
                f = 3.0f;
            } else {
                f = 5.0E-4f;
            }
            grid2.addChild(getVerLines(currentHorPos, top, bottom, f, verOffset));
            float f6 = -currentHorPos;
            if (i2 == verLinesNum / 2) {
                f2 = 3.0f;
            } else {
                f2 = 5.0E-4f;
            }
            grid2.addChild(getVerLines(f6, top, bottom, f2, verOffset));
            currentHorPos += gridWidth;
        }
        return grid2;
    }

    public static boolean getStripEdgePointBaseOnY(ArPointInfo arPointInfo, float width, Number3d leftDst, Number3d rightDst) {
        if (arPointInfo == null || leftDst == null || rightDst == null || (arPointInfo.normalX * arPointInfo.normalX) + (arPointInfo.normalZ * arPointInfo.normalZ) == 0.0f) {
            return false;
        }
        leftDst.y = arPointInfo.posY;
        rightDst.y = arPointInfo.posY;
        float absWCosa = (float) Math.abs(((double) (arPointInfo.normalX * width)) / (Math.sqrt((double) ((arPointInfo.normalX * arPointInfo.normalX) + (arPointInfo.normalZ * arPointInfo.normalZ))) * 2.0d));
        float absWCosb = (float) Math.abs(((double) (arPointInfo.normalZ * width)) / (Math.sqrt((double) ((arPointInfo.normalX * arPointInfo.normalX) + (arPointInfo.normalZ * arPointInfo.normalZ))) * 2.0d));
        if (arPointInfo.normalX >= 0.0f && arPointInfo.normalZ >= 0.0f) {
            leftDst.x = arPointInfo.posX - absWCosb;
            leftDst.z = arPointInfo.posZ + absWCosa;
            rightDst.x = arPointInfo.posX + absWCosb;
            rightDst.z = arPointInfo.posZ - absWCosa;
        }
        if (arPointInfo.normalX < 0.0f && arPointInfo.normalZ >= 0.0f) {
            leftDst.x = arPointInfo.posX - absWCosb;
            leftDst.z = arPointInfo.posZ - absWCosa;
            rightDst.x = arPointInfo.posX + absWCosb;
            rightDst.z = arPointInfo.posZ + absWCosa;
        }
        if (arPointInfo.normalX < 0.0f && arPointInfo.normalZ < 0.0f) {
            leftDst.x = arPointInfo.posX + absWCosb;
            leftDst.z = arPointInfo.posZ - absWCosa;
            rightDst.x = arPointInfo.posX - absWCosb;
            rightDst.z = arPointInfo.posZ + absWCosa;
        }
        if (arPointInfo.normalX >= 0.0f && arPointInfo.normalZ < 0.0f) {
            leftDst.x = arPointInfo.posX + absWCosb;
            leftDst.z = arPointInfo.posZ + absWCosa;
            rightDst.x = arPointInfo.posX - absWCosb;
            rightDst.z = arPointInfo.posZ - absWCosa;
        }
        return true;
    }

    private static float determinant(float v1, float v2, float v3, float v4) {
        return (v1 * v3) - (v2 * v4);
    }

    private static boolean isVerticeIntersectional(Number3d leftRear, Number3d rightRear, Number3d leftFront, Number3d rightFront) {
        float delta = determinant(rightRear.x - leftRear.x, leftFront.x - rightFront.x, rightRear.y - leftRear.y, leftFront.y - rightFront.y);
        if (((double) delta) <= 1.0E-6d && ((double) delta) >= -1.0E-6d) {
            return false;
        }
        float namenda = determinant(leftFront.x - leftRear.x, leftFront.x - rightFront.x, leftFront.y - leftRear.y, leftFront.y - rightFront.y) / delta;
        if (namenda > 1.0f || namenda < 0.0f) {
            return false;
        }
        float miu = determinant(rightRear.x - leftRear.x, leftFront.x - leftRear.x, rightRear.y - leftRear.y, leftFront.y - leftRear.y) / delta;
        if (miu > 1.0f || miu < 0.0f) {
            return false;
        }
        return true;
    }

    private void regulateVertices(List<Number3d> vertices2) {
        if (vertices2 != null) {
            int pairNum = vertices2.size() / 2;
            for (int i = 1; i < pairNum; i++) {
                Number3d leftRear = vertices2.get((i - 1) * 2);
                Number3d rightRear = vertices2.get(((i - 1) * 2) + 1);
                Number3d leftFront = vertices2.get(i * 2);
                Number3d rightFront = vertices2.get((i * 2) + 1);
                if (isVerticeIntersectional(leftRear, rightRear, leftFront, rightFront)) {
                    if (calcDistanceSquare(leftFront, leftRear) < calcDistanceSquare(rightFront, rightRear)) {
                        leftFront.setAllFrom(leftRear);
                    } else {
                        rightFront.setAllFrom(rightRear);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public List<Number3d> calcVertices(List<ArPointInfo> arPointInfos2, float width) {
        ArrayList<Number3d> vertices2 = new ArrayList<>(arPointInfos2.size() * 2);
        for (ArPointInfo arPointInfo : arPointInfos2) {
            Number3d leftVertice = new Number3d();
            Number3d rightVertice = new Number3d();
            if (getStripEdgePointBaseOnZ(arPointInfo, width, leftVertice, rightVertice)) {
                vertices2.add(leftVertice);
                vertices2.add(rightVertice);
            }
        }
        return vertices2;
    }

    public static float calcDistanceSquare(Number3d p1, Number3d p2) {
        if (p1 == null || p2 == null) {
            return -1.0f;
        }
        return (float) (Math.pow((double) (p1.x - p2.x), 2.0d) + Math.pow((double) (p1.y - p2.y), 2.0d) + Math.pow((double) (p1.z - p2.z), 2.0d));
    }

    public Object3d getSingleArrow(String textureId, Number3d leftRear, Number3d rightRear, Number3d leftFront, Number3d rightFront) {
        Object3d arrow = new Object3d(4, 2, true, false, false);
        arrow.vertices().addVertex(leftRear, new Uv(0.0f, 1.0f), null, null);
        arrow.vertices().addVertex(rightRear, new Uv(1.0f, 1.0f), null, null);
        arrow.vertices().addVertex(leftFront, new Uv(0.0f, 0.0f), null, null);
        arrow.vertices().addVertex(rightFront, new Uv(1.0f, 0.0f), null, null);
        arrow.renderType(RenderType.TRIANGLE_STRIP);
        Utils.addQuad(arrow, 0, 1, 3, 2);
        arrow.texturesEnabled(true);
        arrow.textures().addById(textureId);
        return arrow;
    }

    private ArPointInfo[] calcArrowPoint(ArPointInfo originPoint, float distance) {
        float k = (float) (((double) distance) / Math.sqrt((double) ((originPoint.normalX * originPoint.normalX) + (originPoint.normalY * originPoint.normalY))));
        ArPointInfo frontPoint = new ArPointInfo();
        frontPoint.posX = (originPoint.normalX * k) + originPoint.posX;
        frontPoint.posY = (originPoint.normalY * k) + originPoint.posY;
        frontPoint.posZ = originPoint.posZ;
        frontPoint.normalX = originPoint.normalX;
        frontPoint.normalY = originPoint.normalY;
        frontPoint.normalZ = originPoint.normalZ;
        ArPointInfo rearPoint = new ArPointInfo();
        rearPoint.posX = ((-k) * originPoint.normalX) + originPoint.posX;
        rearPoint.posY = ((-k) * originPoint.normalY) + originPoint.posY;
        rearPoint.posZ = originPoint.posZ;
        rearPoint.normalX = originPoint.normalX;
        rearPoint.normalY = originPoint.normalY;
        rearPoint.normalZ = originPoint.normalZ;
        return new ArPointInfo[]{frontPoint, rearPoint};
    }

    /* access modifiers changed from: private */
    public List<Object3d> getArrows(int drawableId, float arrowWidth, float arrowHeight, int interval, float minArrowDis) {
        float disSquareToLastArrow;
        if (this.arPointInfos == null || this.arPointInfos.size() < interval || this.vertices == null || this.vertices.size() < interval * 2) {
            return null;
        }
        LinkedList linkedList = new LinkedList();
        Bitmap b = Utils.makeBitmapFromResourceId(this.context, drawableId);
        if (!Shared.textureManager().contains(ARROW_TEX_ID) && Shared.textureManager().addTextureId(b, ARROW_TEX_ID, false) == null) {
            return null;
        }
        int lastCenterPointIndex = -1;
        int i = interval;
        while (i < this.arPointInfos.size() - 1) {
            ArPointInfo[] arrowPoints = calcArrowPoint(this.arPointInfos.get(i), arrowHeight / 2.0f);
            ArPointInfo rearPoint = arrowPoints[1];
            ArPointInfo frontPoint = arrowPoints[0];
            Number3d number3d = new Number3d(rearPoint.posX, rearPoint.posY, rearPoint.posZ);
            if (lastCenterPointIndex >= 0) {
                disSquareToLastArrow = calcDistanceSquare(number3d, this.arPointInfos.get(lastCenterPointIndex).getPos());
            } else {
                disSquareToLastArrow = -1.0f;
            }
            if ((disSquareToLastArrow <= 0.0f || disSquareToLastArrow >= minArrowDis * minArrowDis) && !(rearPoint.normalX == 0.0f && rearPoint.normalY == 0.0f)) {
                Number3d leftRear = new Number3d();
                Number3d rightRear = new Number3d();
                Number3d leftFront = new Number3d();
                Number3d rightFront = new Number3d();
                getStripEdgePointBaseOnZ(rearPoint, arrowWidth, leftRear, rightRear);
                getStripEdgePointBaseOnZ(frontPoint, arrowWidth, leftFront, rightFront);
                if (lastCenterPointIndex < 0 || (calcDistanceSquare(leftRear, this.vertices.get(lastCenterPointIndex * 2)) >= 4.0f * arrowHeight * arrowHeight && calcDistanceSquare(rightRear, this.vertices.get((lastCenterPointIndex * 2) + 1)) >= 4.0f * arrowHeight * arrowHeight)) {
                    lastCenterPointIndex = i;
                    leftRear.z = (float) (((double) leftRear.z) + 0.01d);
                    rightRear.z = (float) (((double) rightRear.z) + 0.01d);
                    leftFront.z = (float) (((double) leftFront.z) + 0.01d);
                    rightFront.z = (float) (((double) rightFront.z) + 0.01d);
                    linkedList.add(getSingleArrow(ARROW_TEX_ID, leftRear, rightRear, leftFront, rightFront));
                } else {
                    i -= interval - 1;
                }
            } else {
                i -= interval - 1;
            }
            i += interval;
        }
        b.recycle();
        return linkedList;
    }

    private Object3d getStrip(List<Number3d> number3ds) {
        int pointPairNum = number3ds.size() / 2;
        Object3d strip2 = new Object3d(pointPairNum * 2, (pointPairNum - 1) * 2, false, false, true);
        for (int i = 0; i < pointPairNum; i++) {
            strip2.vertices().addVertex(number3ds.get(i * 2), null, null, new Color4(10, 238, 139, (int) XMPError.BADSTREAM));
            strip2.vertices().addVertex(number3ds.get((i * 2) + 1), null, null, new Color4(10, 238, 139, (int) XMPError.BADSTREAM));
            if (i > 0) {
                Utils.addQuad(strip2, (i - 1) * 2, ((i - 1) * 2) + 1, (i * 2) + 1, i * 2);
            }
        }
        strip2.doubleSidedEnabled(true);
        return strip2;
    }

    /* access modifiers changed from: private */
    public Object3d getStrip(List<Number3d> number3ds, String texId, int alpha) {
        int pointPairNum = number3ds.size() / 2;
        Object3d strip2 = new Object3d(pointPairNum * 2, (pointPairNum - 1) * 2, true, false, true);
        for (int i = 0; i < pointPairNum; i++) {
            strip2.vertices().addVertex(number3ds.get(i * 2), new Uv(0.0f, ((float) i) / ((float) (pointPairNum - 1))), null, new Color4(255, 255, 255, alpha));
            strip2.vertices().addVertex(number3ds.get((i * 2) + 1), new Uv(1.0f, ((float) i) / ((float) (pointPairNum - 1))), null, new Color4(255, 255, 255, alpha));
            if (i > 0) {
                Utils.addQuad(strip2, (i - 1) * 2, ((i - 1) * 2) + 1, (i * 2) + 1, i * 2);
            }
        }
        strip2.doubleSidedEnabled(true);
        strip2.texturesEnabled(true);
        strip2.textures().addById(texId);
        return strip2;
    }

    public float getSkyLinePosOnScreen() {
        float camPosX = this.mScene.camera().position.x;
        float camPosY = this.mScene.camera().position.y;
        float f = this.mScene.camera().position.z;
        float camTarX = this.mScene.camera().target.x;
        float camTarY = this.mScene.camera().target.y;
        float f2 = this.mScene.camera().target.z;
        float dirX = camTarX - camPosX;
        float dirY = camTarY - camPosY;
        float k = (float) (((double) 5000.0f) / Math.sqrt((double) ((dirX * dirX) + (dirY * dirY))));
        return ArUtil.worldCoor2ScreenCoor((GL11) this.displayView.getRenderer().gl(), (k * dirX) + camPosX, (k * dirY) + camPosY, 0.0f)[1];
    }

    private boolean checkPolynomialTrajectory(DataEyeGetPushTrajectory.PolynomialTrajectory trajectory) {
        if (trajectory == null) {
            return false;
        }
        for (int i = 1; i < trajectory.mPolyXAxis.length; i++) {
            if (trajectory.mPolyXAxis[i] != 0.0f || trajectory.mPolyYAxis[i] != 0.0f) {
                return true;
            }
        }
        return false;
    }

    private void gridMove(float posX, float posY, float posZ, float camTargetX, float camTargetY) {
        if (this.grid != null) {
            this.grid.position().setAll(posX, posY, 0.0f);
            this.grid.scale().x = posZ;
            this.grid.scale().y = posZ;
            float vecX = camTargetX - posX;
            float vecY = camTargetY - posY;
            if (vecX != 0.0f || vecY != 0.0f) {
                float rot = (float) Math.toDegrees(Math.acos(((double) vecY) / Math.sqrt((double) ((vecX * vecX) + (vecY * vecY)))));
                Number3d rotation = this.grid.rotation();
                if (vecX > 0.0f) {
                    rot = -rot;
                }
                rotation.z = rot;
            }
        }
    }

    public void setGridVisibility(boolean visibility) {
        this.grid.isVisible(Boolean.valueOf(visibility));
        this.mainHandler.post(new Runnable() {
            /* class dji.midware.ar.ArDrawFlyController.AnonymousClass2 */

            public void run() {
                ArDrawFlyController.this.displayView.invalidate();
            }
        });
    }

    public boolean getGridVisibility() {
        return this.grid.isVisible();
    }

    private Object3d getBallByArPointInfo(float radius, Color4 color) {
        Object3d ball = new Sphere(radius, 6, 6, color);
        ball.colorMaterialEnabled(true);
        return ball;
    }

    private Object3d getBallByArPointInfo(ArPointInfo arpointInfo, float radius, Color4 color) {
        Object3d ball = getBallByArPointInfo(radius, color);
        ball.position().setAll(arpointInfo.posX, arpointInfo.posY, arpointInfo.posZ);
        return ball;
    }

    public void moveCamera(float posX, float posY, float posZ, float pitch, float roll, float yaw) {
        if (this.mScene != null && this.mScene.camera() != null) {
            double theta = 1.5707963267948966d - Math.toRadians((double) yaw);
            double phi = Math.toRadians((double) pitch);
            float dx = (float) (Math.cos(theta) * Math.cos(phi));
            float dy = (float) Math.sin(phi);
            float dz = (float) (Math.sin(theta) * Math.cos(phi));
            this.mScene.camera().position.setAll(posX, posY, posZ);
            this.mScene.camera().target.setAll(posX + dx, posY + dy, posZ - dz);
        }
    }

    public void moveCamera2(float posX, float posY, float posZ, float pitch, float roll, float yaw) {
        if (this.mScene != null && this.mScene.camera() != null) {
            double theta = -Math.toRadians((double) pitch);
            double phi = -Math.toRadians((double) yaw);
            float dx = (float) (0.0d - Math.sin(phi));
            float dy = (float) (0.0d - (Math.cos(phi) * Math.sin(theta)));
            float dz = (float) (0.0d - (Math.cos(theta) * Math.cos(phi)));
            this.mScene.camera().position.setAll(posX, posY, posZ);
            this.mScene.camera().target.setAll(posX + dx, posY + dy, posZ + dz);
        }
    }

    public void moveCamera3(float posX, float posY, float posZ, float pitch, float roll, float yaw) {
        if (this.mScene != null && this.mScene.camera() != null) {
            if (this.mScene == null || this.vertices != null) {
            }
            float camPosX = posY;
            float camPosY = -posZ;
            float camPosZ = -posX;
            double p = Math.toRadians((double) (90.0f + pitch));
            double y = Math.toRadians((double) yaw);
            float dx = (float) Math.sin(y);
            float dy = (float) (Math.sin(p) * Math.cos(y));
            float dz = (float) ((-Math.cos(p)) * Math.cos(y));
            this.mScene.camera().position.setAll(camPosX, camPosY, camPosZ);
            this.mScene.camera().target.setAll(camPosX + dx, camPosY + dy, camPosZ + dz);
        }
    }

    public void moveCamera4(float posX, float posY, float posZ, float pitch, float roll, float yaw) {
        if (this.mScene != null && this.mScene.camera() != null) {
            if (this.mScene == null || this.vertices != null) {
            }
            float[][] dcm = (float[][]) Array.newInstance(Float.TYPE, 3, 3);
            ArUtil.eularAngles2Dcm(new float[]{yaw, pitch, roll}, dcm);
            float[] x = ArUtil.matrixMultiplyVector(dcm, new float[]{1.0f, 0.0f, 0.0f});
            x[0] = x[0] + posX;
            x[1] = x[1] + posY;
            x[2] = x[2] + posX;
            float[][] trans = {new float[]{0.0f, 1.0f, 0.0f}, new float[]{0.0f, 0.0f, -1.0f}, new float[]{-1.0f, 0.0f, 0.0f}};
            float[] target = ArUtil.matrixMultiplyVector(trans, x);
            float[] pos = ArUtil.matrixMultiplyVector(trans, new float[]{posX, posY, posZ});
            this.mScene.camera().position.setAll(pos[0], pos[1], pos[2]);
            this.mScene.camera().target.setAll(target[0], target[1], target[2]);
        }
    }

    public void moveCamera5(float posX, float posY, float posZ, float pitch, float roll, float yaw) {
        float dx;
        float dy;
        this.mScene.camera();
        float[] rM = ArMatrixUtils.rotateM(pitch, -roll, yaw);
        float[] target = ArMatrixUtils.getVectorTarget(ArMatrixUtils.getViewM(rM, posY, posX, -posZ));
        float[] up = ArMatrixUtils.getVectorUp(rM);
        if (pitch < -89.0f) {
            float x = target[0] - posY;
            float y = target[1] - posX;
            float ss = (float) Math.sqrt((double) ((x * x) + (y * y)));
            dx = (x / ss) * 5000.0f;
            dy = (y / ss) * 5000.0f;
        } else {
            float x2 = up[0];
            float y2 = up[1];
            float ss2 = (float) Math.sqrt((double) ((x2 * x2) + (y2 * y2)));
            dx = (x2 / ss2) * 5000.0f;
            dy = (y2 / ss2) * 5000.0f;
        }
        float[] currentModelViewMatrix = new float[16];
        float[] currentProjectMatrix = new float[16];
        ((GL11) this.gl).glGetFloatv(2982, currentModelViewMatrix, 0);
        ((GL11) this.gl).glGetFloatv(2983, currentProjectMatrix, 0);
        float[] dm = new float[16];
        Matrix.multiplyMM(dm, 0, currentModelViewMatrix, 0, currentProjectMatrix, 0);
        Matrix.multiplyMV(new float[4], 0, dm, 0, new float[]{dx, dy, 0.0f, 1.0f}, 0);
        this.mScene.camera().position.setAll(posY, posX, -posZ);
        this.mScene.camera().target.setAll(target[0], target[1], target[2]);
        this.mScene.camera().upAxis.setAll(up[0], up[1], up[2]);
        if (this.box != null) {
        }
        gridMove(this.mScene.camera().position.x, this.mScene.camera().position.y, this.mScene.camera().position.z, this.mScene.camera().target.x, this.mScene.camera().target.y);
    }

    private void updatePos() {
    }

    private void addTestObject() {
    }

    private float[] gox(float[] neg) {
        return new float[]{neg[1], -neg[2], -neg[0]};
    }

    private void addBox() {
        this.box = new Box(0.3f, 0.3f, 0.3f);
        this.box.position().setAll(1.0f, 1.0f, 0.0f);
        this.displayView.scene.addChild(this.box);
    }

    private void addMarkBall() {
        Sphere _earth = new Sphere(0.2f, 6, 6, new Color4(0, 0, 0, 255));
        _earth.colorMaterialEnabled(true);
        this.displayView.scene.addChild(_earth);
        Sphere _earthx = new Sphere(0.2f, 8, 8, new Color4(255, 0, 0, 255));
        _earthx.colorMaterialEnabled(true);
        _earthx.position().z = -1.0f;
        this.displayView.scene.addChild(_earthx);
        Sphere _earthy = new Sphere(0.2f, 10, 10, new Color4(0, 255, 0, 255));
        _earthy.colorMaterialEnabled(true);
        _earthy.position().x = 1.0f;
        this.displayView.scene.addChild(_earthy);
        Sphere _earthz = new Sphere(0.2f, 30, 30, new Color4(0, 0, 255, 255));
        _earthz.colorMaterialEnabled(true);
        _earthz.position().y = 1.0f;
        this.displayView.scene.addChild(_earthz);
    }

    private Object3dContainer getTestingBalls(List<ArPointInfo> arPointInfoList, int interval, float radius, Color4 color) {
        Object3dContainer balls = new Object3dContainer();
        int i = 0;
        while (i < arPointInfoList.size()) {
            balls.addChild(getBallByArPointInfo(arPointInfoList.get(i), radius, color));
            i += interval;
        }
        return balls;
    }
}
