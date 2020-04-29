package dji.midware.ar;

import android.content.Context;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.core.Scene;
import dji.midware.ar.min3d.parser.Parser;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Light;
import dji.midware.data.model.P3.DataEyeObjectDetectionPushInfo;
import dji.midware.util.DJIEventBusUtil;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class ArPathController {
    private static final float ARROW_SCALE_X = 0.11363637f;
    private static final float ARROW_SCALE_Y = 0.19999999f;
    private static final float ARROW_SCALE_Z = 0.18181819f;
    private static final String TAG = "ArPathController";
    private static ArPathController instance;
    private static float[] rotateQuat;
    /* access modifiers changed from: private */
    public List<Object3dContainer> arrowNodes = new LinkedList();
    private Min3dView displayView;
    private int extraNodeNum = 0;
    /* access modifiers changed from: private */
    public GL10 gl;
    /* access modifiers changed from: private */
    public boolean isPaused;
    /* access modifiers changed from: private */
    public boolean isResume;
    private Context mContext;
    /* access modifiers changed from: private */
    public Scene mScene;
    private Color4[] newColors;
    private float[] posX;
    private float[] posY;
    private float[] posZ;
    private Object3dContainer prototype;
    private float[] rotationX;
    private float[] rotationY;
    private float[] rotationZ;

    static {
        rotateQuat = null;
        float val = (float) (Math.sqrt(2.0d) / 2.0d);
        rotateQuat = new float[]{val, val, 0.0f, 0.0f};
    }

    private ArPathController() {
    }

    public static ArPathController getInstance() {
        if (instance == null) {
            instance = new ArPathController();
        }
        return instance;
    }

    public void init(Context context, InputStream inputStream, final float verFov, final float aspect) {
        Log.d(TAG, "init: ");
        this.mContext = context.getApplicationContext();
        DJIEventBusUtil.register(this);
        this.prototype = ArUtil.loadModel(Parser.Type.MAX_3DS, context.getResources(), inputStream, false, 0.07f);
        this.prototype.scale().setAll(ARROW_SCALE_X, ARROW_SCALE_Y, ARROW_SCALE_Z);
        this.prototype.colorMaterialEnabled(true);
        this.prototype.defaultColor(new Color4(255, 0, 0, (int) CertificateBody.profileType));
        this.displayView = new Min3dView(context) {
            /* class dji.midware.ar.ArPathController.AnonymousClass1 */

            public void initScene() {
                Scene unused = ArPathController.this.mScene = this.scene;
                GL10 unused2 = ArPathController.this.gl = getRenderer().gl();
                this.scene.backgroundColor().setAll(0);
                this.scene.lights().add(new Light());
                this.scene.camera().position.setAll(0.0f, 0.0f, 0.0f);
                this.scene.camera().target.z = -1.0f;
                this.scene.camera().frustum.zNear(0.1f);
                ArPathController.this.updateVerticalFov(verFov, aspect);
            }

            public void updateScene() {
                for (Object3dContainer node : ArPathController.this.arrowNodes) {
                    this.scene.addChild(node);
                }
                boolean unused = ArPathController.this.isResume = false;
                if (ArPathController.this.isPaused) {
                    for (Object3dContainer node2 : ArPathController.this.arrowNodes) {
                        this.scene.removeChild(node2);
                    }
                    boolean unused2 = ArPathController.this.isPaused = false;
                }
                ArPathController.this.updateNodesNum();
                ArPathController.this.updateNodesPos();
                ArPathController.this.updateNodesRotation();
                ArPathController.this.updateNodesColor();
            }
        };
        this.displayView.init();
    }

    public void onResume() {
        this.isResume = true;
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        if (this.displayView != null) {
            this.displayView.onPause();
            this.displayView.unInit();
        }
        this.isPaused = true;
        this.displayView = null;
    }

    public Min3dView getDisplayView() {
        return this.displayView;
    }

    /* access modifiers changed from: private */
    public void updateNodesNum() {
        if (this.displayView != null && this.extraNodeNum > 0) {
            for (int i = 0; i < this.extraNodeNum; i++) {
                Object3dContainer newNode = this.prototype.clone();
                newNode.colorMaterialEnabled(true);
                this.arrowNodes.add(newNode);
                this.displayView.scene.addChild(newNode);
            }
            this.extraNodeNum = 0;
        }
    }

    /* access modifiers changed from: private */
    public void updateNodesPos() {
        if (this.displayView != null) {
            if (this.posX != null) {
                for (int i = 0; i < Math.min(this.arrowNodes.size(), this.posX.length); i++) {
                    this.arrowNodes.get(i).position().x = this.posX[i];
                }
                this.posX = null;
            }
            if (this.posY != null) {
                for (int i2 = 0; i2 < Math.min(this.arrowNodes.size(), this.posY.length); i2++) {
                    this.arrowNodes.get(i2).position().y = this.posY[i2];
                }
                this.posY = null;
            }
            if (this.posZ != null) {
                for (int i3 = 0; i3 < Math.min(this.arrowNodes.size(), this.posZ.length); i3++) {
                    this.arrowNodes.get(i3).position().z = this.posZ[i3];
                }
                this.posZ = null;
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateNodesRotation() {
        if (this.displayView != null) {
            if (this.rotationX != null) {
                for (int i = 0; i < Math.min(this.arrowNodes.size(), this.rotationX.length); i++) {
                    this.arrowNodes.get(i).rotation().x = this.rotationX[i];
                }
                this.rotationX = null;
            }
            if (this.rotationY != null) {
                for (int i2 = 0; i2 < Math.min(this.arrowNodes.size(), this.rotationY.length); i2++) {
                    this.arrowNodes.get(i2).rotation().y = this.rotationY[i2];
                }
                this.rotationY = null;
            }
            if (this.rotationZ != null) {
                for (int i3 = 0; i3 < Math.min(this.arrowNodes.size(), this.rotationZ.length); i3++) {
                    this.arrowNodes.get(i3).rotation().z = this.rotationZ[i3];
                }
                this.rotationZ = null;
            }
        }
    }

    public void updateNodesNum(int num) {
        this.extraNodeNum = Math.max(0, num - this.arrowNodes.size());
    }

    public void updateNodesPosition(float[] x, float[] y, float[] z) {
        if (x != null) {
            this.posX = new float[x.length];
            System.arraycopy(x, 0, this.posX, 0, x.length);
        }
        if (y != null) {
            this.posY = new float[y.length];
            System.arraycopy(y, 0, this.posY, 0, y.length);
        }
        if (z != null) {
            this.posZ = new float[z.length];
            System.arraycopy(z, 0, this.posZ, 0, z.length);
        }
    }

    public void updateNodesRotation(float[] x, float[] y, float[] z) {
        if (x != null) {
            this.rotationX = new float[x.length];
            System.arraycopy(x, 0, this.rotationX, 0, x.length);
        }
        if (y != null) {
            this.rotationY = new float[y.length];
            System.arraycopy(y, 0, this.rotationY, 0, y.length);
        }
        if (z != null) {
            this.rotationZ = new float[z.length];
            System.arraycopy(z, 0, this.rotationZ, 0, z.length);
        }
    }

    /* access modifiers changed from: private */
    public void updateNodesColor() {
        if (this.newColors != null) {
            for (int i = 0; i < this.arrowNodes.size(); i++) {
                if (i >= this.newColors.length || this.newColors[i] == null) {
                    this.arrowNodes.get(i).defaultColor(new Color4(0, 0, 0, 0));
                } else {
                    this.arrowNodes.get(i).defaultColor(this.newColors[i]);
                }
            }
            this.newColors = null;
        }
    }

    public void updateNodesColor(Color4[] color) {
        if (color != null) {
            this.newColors = new Color4[color.length];
            System.arraycopy(color, 0, this.newColors, 0, color.length);
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
    public void onEvent3BackgroundThread(DataEyeObjectDetectionPushInfo info) {
        if (info != null && info.getRecData() != null && info.getRecData().length > 0) {
            updateNodesNum(info.getArrorNum());
            List<DataEyeObjectDetectionPushInfo.ArrowInfo> arrowInfoList = info.getArrowInfoList();
            float[] posx = new float[info.getArrorNum()];
            float[] posy = new float[info.getArrorNum()];
            float[] posz = new float[info.getArrorNum()];
            float[] rotationx = new float[info.getArrorNum()];
            float[] rotationy = new float[info.getArrorNum()];
            float[] rotationz = new float[info.getArrorNum()];
            Color4[] colors = new Color4[info.getArrorNum()];
            for (int i = 0; i < arrowInfoList.size(); i++) {
                DataEyeObjectDetectionPushInfo.ArrowInfo arrowInfo = arrowInfoList.get(i);
                float[] eularAngles = new float[3];
                float[] quat = new float[4];
                System.arraycopy(arrowInfo.orientation, 3, quat, 0, 4);
                ArUtil.quaternions2EularAngles(ArUtil.quaternionsMultiply(quat, rotateQuat), 0, eularAngles);
                posx[i] = arrowInfo.orientation[0];
                posy[i] = arrowInfo.orientation[1];
                posz[i] = arrowInfo.orientation[2];
                rotationx[i] = eularAngles[0];
                rotationy[i] = eularAngles[1];
                rotationz[i] = eularAngles[2];
                switch (arrowInfo.type) {
                    case 0:
                        colors[i] = new Color4(10, 238, 142, arrowInfo.alpha);
                        break;
                    case 1:
                        colors[i] = new Color4(0, 240, 255, arrowInfo.alpha);
                        break;
                    case 2:
                        colors[i] = new Color4(231, 1, 2, arrowInfo.alpha);
                        break;
                    default:
                        colors[i] = new Color4(10, 238, 142, arrowInfo.alpha);
                        break;
                }
            }
            updateNodesPosition(posx, posy, posz);
            updateNodesRotation(rotationx, rotationy, rotationz);
            updateNodesColor(colors);
        }
    }

    public void updateViewport(int w, int h) {
        if (this.gl != null) {
            this.gl.glViewport(0, 0, w, h);
        }
    }
}
