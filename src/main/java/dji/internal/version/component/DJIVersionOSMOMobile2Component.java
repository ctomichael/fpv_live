package dji.internal.version.component;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.DJIModelUpgradePackList;
import dji.internal.version.DJIVersionBaseComponent;
import dji.midware.data.manager.P3.ServiceManager;
import java.util.ArrayList;

@EXClassNullAway
public class DJIVersionOSMOMobile2Component extends DJIVersionBaseComponent {
    /* access modifiers changed from: protected */
    public String[] getFirmwareList() {
        return new String[]{"0400", "0700", "0900"};
    }

    /* access modifiers changed from: protected */
    public boolean isComponentConnected() {
        return ServiceManager.getInstance().isConnected();
    }

    /* access modifiers changed from: protected */
    public ArrayList<DJIModelUpgradePackList.DJIUpgradePack> getDJIUpgradePackList(DJIModelUpgradePackList model) {
        return model.versionlistHG301;
    }

    /* access modifiers changed from: protected */
    public String getVersion(DJIModelUpgradePackList.DJIUpgradePack pack) {
        if (pack == null) {
            return null;
        }
        return pack.version;
    }

    /* access modifiers changed from: protected */
    public String getComponentName() {
        return "UpgradeLonganMobile2Component";
    }
}
