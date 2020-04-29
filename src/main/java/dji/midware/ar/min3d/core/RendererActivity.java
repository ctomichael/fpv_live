package dji.midware.ar.min3d.core;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.interfaces.ISceneController;

@Keep
@EXClassNullAway
public class RendererActivity extends Activity implements ISceneController {
    protected GLSurfaceView _glSurfaceView;
    protected Handler _initSceneHander;
    final Runnable _initSceneRunnable = new Runnable() {
        /* class dji.midware.ar.min3d.core.RendererActivity.AnonymousClass1 */

        public void run() {
            RendererActivity.this.onInitScene();
        }
    };
    private boolean _renderContinuously;
    protected Handler _updateSceneHander;
    final Runnable _updateSceneRunnable = new Runnable() {
        /* class dji.midware.ar.min3d.core.RendererActivity.AnonymousClass2 */

        public void run() {
            RendererActivity.this.onUpdateScene();
        }
    };
    public Scene scene;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this._initSceneHander = new Handler();
        this._updateSceneHander = new Handler();
        Shared.context(this);
        this.scene = new Scene(this);
        Renderer r = new Renderer(this.scene);
        Shared.renderer(r);
        this._glSurfaceView = new GLSurfaceView(this);
        glSurfaceViewConfig();
        this._glSurfaceView.setRenderer(r);
        this._glSurfaceView.setRenderMode(1);
        onCreateSetContentView();
    }

    /* access modifiers changed from: protected */
    public void glSurfaceViewConfig() {
    }

    /* access modifiers changed from: protected */
    public GLSurfaceView glSurfaceView() {
        return this._glSurfaceView;
    }

    /* access modifiers changed from: protected */
    public void onCreateSetContentView() {
        setContentView(this._glSurfaceView);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this._glSurfaceView.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this._glSurfaceView.onPause();
    }

    public void initScene() {
    }

    public void updateScene() {
    }

    public void onInitScene() {
    }

    public void onUpdateScene() {
    }

    public void renderContinuously(boolean $b) {
        this._renderContinuously = $b;
        if (this._renderContinuously) {
            this._glSurfaceView.setRenderMode(1);
        } else {
            this._glSurfaceView.setRenderMode(0);
        }
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
