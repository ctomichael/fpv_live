package dji.midware.ar;

import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.core.Scene;

@EXClassNullAway
public abstract class ArControllerBase {
    protected Context context;
    protected Min3dView displayView;
    protected Scene mScene;

    public Scene getScene() {
        return this.mScene;
    }

    public Min3dView getDisplayView() {
        return this.displayView;
    }
}
