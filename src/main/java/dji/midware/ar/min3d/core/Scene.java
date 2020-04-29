package dji.midware.ar.min3d.core;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Min3d;
import dji.midware.ar.min3d.interfaces.IDirtyParent;
import dji.midware.ar.min3d.interfaces.IObject3dContainer;
import dji.midware.ar.min3d.interfaces.ISceneController;
import dji.midware.ar.min3d.vos.CameraVo;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Color4Managed;
import dji.midware.ar.min3d.vos.FogType;
import java.util.ArrayList;

@EXClassNullAway
public class Scene implements IObject3dContainer, IDirtyParent {
    private Color4Managed _backgroundColor;
    private CameraVo _camera;
    private ArrayList<Object3d> _children = new ArrayList<>();
    private Color4 _fogColor;
    private boolean _fogEnabled;
    private float _fogFar;
    private float _fogNear;
    private FogType _fogType;
    private boolean _lightingEnabled;
    private ManagedLightList _lights;
    private ISceneController _sceneController;

    public Scene(ISceneController $sceneController) {
        this._sceneController = $sceneController;
        this._lights = new ManagedLightList();
        this._fogColor = new Color4(255, 255, 255, 255);
        this._fogNear = 0.0f;
        this._fogFar = 10.0f;
        this._fogType = FogType.LINEAR;
        this._fogEnabled = false;
    }

    public ISceneController sceneController() {
        return this._sceneController;
    }

    public void sceneController(ISceneController $sceneController) {
        this._sceneController = $sceneController;
    }

    public void reset() {
        clearChildren(this);
        this._children = new ArrayList<>();
        this._camera = new CameraVo();
        this._backgroundColor = new Color4Managed(0, 0, 0, 255, this);
        this._lights = new ManagedLightList();
        lightingEnabled(true);
    }

    public void addChild(Object3d $o) {
        if (!this._children.contains($o)) {
            this._children.add($o);
            $o.parent(this);
            $o.scene(this);
        }
    }

    public void addChildAt(Object3d $o, int $index) {
        if (!this._children.contains($o)) {
            this._children.add($index, $o);
        }
    }

    public boolean removeChild(Object3d $o) {
        $o.parent(null);
        $o.scene(null);
        return this._children.remove($o);
    }

    public Object3d removeChildAt(int $index) {
        Object3d o = this._children.remove($index);
        if (o != null) {
            o.parent(null);
            o.scene(null);
        }
        return o;
    }

    public Object3d getChildAt(int $index) {
        return this._children.get($index);
    }

    public Object3d getChildByName(String $name) {
        for (int i = 0; i < this._children.size(); i++) {
            if (this._children.get(0).name() == $name) {
                return this._children.get(0);
            }
        }
        return null;
    }

    public int getChildIndexOf(Object3d $o) {
        return this._children.indexOf($o);
    }

    public int numChildren() {
        return this._children.size();
    }

    public CameraVo camera() {
        return this._camera;
    }

    public void camera(CameraVo $camera) {
        this._camera = $camera;
    }

    public Color4Managed backgroundColor() {
        return this._backgroundColor;
    }

    public ManagedLightList lights() {
        return this._lights;
    }

    public boolean lightingEnabled() {
        return this._lightingEnabled;
    }

    public void lightingEnabled(boolean $b) {
        this._lightingEnabled = $b;
    }

    public Color4 fogColor() {
        return this._fogColor;
    }

    public void fogColor(Color4 _fogColor2) {
        this._fogColor = _fogColor2;
    }

    public float fogFar() {
        return this._fogFar;
    }

    public void fogFar(float _fogFar2) {
        this._fogFar = _fogFar2;
    }

    public float fogNear() {
        return this._fogNear;
    }

    public void fogNear(float _fogNear2) {
        this._fogNear = _fogNear2;
    }

    public FogType fogType() {
        return this._fogType;
    }

    public void fogType(FogType _fogType2) {
        this._fogType = _fogType2;
    }

    public boolean fogEnabled() {
        return this._fogEnabled;
    }

    public void fogEnabled(boolean _fogEnabled2) {
        this._fogEnabled = _fogEnabled2;
    }

    /* access modifiers changed from: package-private */
    public void init() {
        Log.i(Min3d.TAG, "Scene.init()");
        reset();
        this._sceneController.initScene();
        this._sceneController.getInitSceneHandler().post(this._sceneController.getInitSceneRunnable());
    }

    /* access modifiers changed from: package-private */
    public void update() {
        this._sceneController.updateScene();
        this._sceneController.getUpdateSceneHandler().post(this._sceneController.getUpdateSceneRunnable());
    }

    /* access modifiers changed from: package-private */
    public ArrayList<Object3d> children() {
        return this._children;
    }

    private void clearChildren(IObject3dContainer $c) {
        for (int i = $c.numChildren() - 1; i >= 0; i--) {
            Object3d o = $c.getChildAt(i);
            o.clear();
            if (o instanceof Object3dContainer) {
                clearChildren((Object3dContainer) o);
            }
        }
    }

    public void onDirty() {
    }
}
