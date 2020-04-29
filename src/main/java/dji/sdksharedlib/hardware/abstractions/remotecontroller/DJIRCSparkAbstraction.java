package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.sdksharedlib.util.abstractions.DJIRCAbstractionUtil;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIRCSparkAbstraction extends DJIRCAbstraction {
    private static final String TAG = "SparkRCAbstraction";

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNameSpark;
    }

    public DJIRCSparkAbstraction() {
        this.hardwareState.getFunctionButton().setPresent(true);
        this.hardwareState.getC2Button().setPresent(true);
        this.hardwareState.getGoHomeButton().setPresent(true);
        this.hardwareState.getPauseButton().setPresent(true);
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(false);
        this.hardwareState.getShutterButton().setPresent(true);
        this.hardwareState.getTransformationSwitch().setPresent(false);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushParams params) {
        super.onEvent3BackgroundThread(params);
        DJIRCAbstractionUtil.executeCameraAction(params, this.index);
    }
}
