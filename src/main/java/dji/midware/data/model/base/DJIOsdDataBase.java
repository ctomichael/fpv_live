package dji.midware.data.model.base;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.manager.P3.DataCameraEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public abstract class DJIOsdDataBase extends DataBase {
    public DJIOsdDataBase() {
    }

    public DJIOsdDataBase(boolean isRegist) {
        super(isRegist);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (event == DataCameraEvent.ConnectLose) {
            clear();
        }
    }
}
