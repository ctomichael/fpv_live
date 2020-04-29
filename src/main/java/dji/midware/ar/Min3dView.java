package dji.midware.ar;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.core.Renderer;
import dji.midware.ar.min3d.core.Scene;
import dji.midware.ar.min3d.interfaces.ISceneController;

@Keep
@EXClassNullAway
public abstract class Min3dView extends GLSurfaceView implements ISceneController {
    protected Handler _initSceneHander;
    final Runnable _initSceneRunnable = new Runnable() {
        /* class dji.midware.ar.Min3dView.AnonymousClass1 */

        public void run() {
            Min3dView.this.onInitScene();
        }
    };
    private boolean _renderContinuously;
    protected Handler _updateSceneHander;
    final Runnable _updateSceneRunnable = new Runnable() {
        /* class dji.midware.ar.Min3dView.AnonymousClass2 */

        public void run() {
            Min3dView.this.onUpdateScene();
        }
    };
    private boolean isInit = false;
    private Renderer mRender;
    public Scene scene;

    public abstract void initScene();

    public abstract void updateScene();

    public Renderer getRenderer() {
        return this.mRender;
    }

    public Min3dView(Context context) {
        super(context);
    }

    public boolean isInititialized() {
        return this.isInit;
    }

    public void init() {
        this._initSceneHander = new Handler();
        this._updateSceneHander = new Handler();
        Shared.context(getContext().getApplicationContext());
        this.scene = new Scene(this);
        this.mRender = new Renderer(this.scene);
        Shared.renderer(this.mRender);
        glSurfaceViewConfig();
        setRenderer(this.mRender);
        setRenderMode(1);
        this.isInit = true;
    }

    public void unInit() {
        this.isInit = false;
        Shared.renderer(null);
    }

    /* access modifiers changed from: protected */
    public void glSurfaceViewConfig() {
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(-3);
        setZOrderOnTop(true);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onInitScene() {
    }

    public void onUpdateScene() {
    }

    public Handler getInitSceneHandler() {
        return this._initSceneHander;
    }

    public Handler getUpdateSceneHandler() {
        return this._updateSceneHander;
    }

    public Runnable getInitSceneRunnable() {
        return this._initSceneRunnable;
    }

    public Runnable getUpdateSceneRunnable() {
        return this._updateSceneRunnable;
    }
}
