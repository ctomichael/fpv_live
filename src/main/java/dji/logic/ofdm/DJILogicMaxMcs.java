package dji.logic.ofdm;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdGetPushMaxMcs;
import dji.midware.data.model.P3.DataOsdSetMaxMcs;
import dji.midware.interfaces.DJIDataCallBack;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJILogicMaxMcs {
    private static DJILogicMaxMcs mInstance = null;
    private int curMcs = 0;

    public static synchronized DJILogicMaxMcs getInstance() {
        DJILogicMaxMcs dJILogicMaxMcs;
        synchronized (DJILogicMaxMcs.class) {
            if (mInstance == null) {
                mInstance = new DJILogicMaxMcs();
            }
            dJILogicMaxMcs = mInstance;
        }
        return dJILogicMaxMcs;
    }

    private DJILogicMaxMcs() {
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushMaxMcs pushMaxMcs) {
        int mcs = pushMaxMcs.getMaxMcs();
        if (this.curMcs != mcs) {
            this.curMcs = mcs;
            DJILogHelper.getInstance().LOGD("", "++++++++++++++++++pushMaxMcs=" + mcs, true, true);
            pushMaxMcs.start();
            if (mcs > 4) {
                DataOsdSetMaxMcs.getInstance().setMaxMcs(4).start(new DJIDataCallBack() {
                    /* class dji.logic.ofdm.DJILogicMaxMcs.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        DJILogHelper.getInstance().LOGD("", "++++++++++++++++++setMaxMcs ok", true, true);
                    }

                    public void onFailure(Ccode ccode) {
                        DJILogHelper.getInstance().LOGD("", "++++++++++++++++++setMaxMcs " + ccode, true, true);
                    }
                });
            }
        }
    }
}
