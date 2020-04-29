package dji.midware.ar.min3d.interfaces;

import android.os.Handler;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface ISceneController {
    Handler getInitSceneHandler();

    Runnable getInitSceneRunnable();

    Handler getUpdateSceneHandler();

    Runnable getUpdateSceneRunnable();

    void initScene();

    void updateScene();
}
