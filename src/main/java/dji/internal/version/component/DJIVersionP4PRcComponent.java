package dji.internal.version.component;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.DJIModelUpgradePackList;
import dji.internal.version.DJIVersionBaseComponent;
import dji.midware.data.manager.P3.DataEvent;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIVersionP4PRcComponent extends DJIVersionBaseComponent {
    /* access modifiers changed from: protected */
    public String[] getFirmwareList() {
        return new String[]{"1300", "1301", "1400", "1401", "1600", "1601", "2000", "2001", "2002"};
    }

    /* access modifiers changed from: protected */
    public ArrayList<DJIModelUpgradePackList.DJIUpgradePack> getDJIUpgradePackList(DJIModelUpgradePackList model) {
        return model.versionlistp4p;
    }

    /* access modifiers changed from: protected */
    public String getVersion(DJIModelUpgradePackList.DJIUpgradePack pack) {
        if (pack == null) {
            return null;
        }
        return pack.rcversion;
    }

    /* access modifiers changed from: protected */
    public String getComponentName() {
        return DJIVersionP4PRcComponent.class.getSimpleName();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        switch (event) {
            case ConnectOK:
                requestGetVersionDelay();
                return;
            default:
                return;
        }
    }
}
