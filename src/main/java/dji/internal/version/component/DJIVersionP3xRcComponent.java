package dji.internal.version.component;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.DJIModelUpgradePackList;
import dji.internal.version.DJIVersionBaseComponent;
import java.util.ArrayList;

@EXClassNullAway
public class DJIVersionP3xRcComponent extends DJIVersionBaseComponent {
    /* access modifiers changed from: protected */
    public String[] getFirmwareList() {
        return new String[]{"1300", "1400", "1600", "1601", "2000", "2001", "2002"};
    }

    /* access modifiers changed from: protected */
    public ArrayList<DJIModelUpgradePackList.DJIUpgradePack> getDJIUpgradePackList(DJIModelUpgradePackList model) {
        return model.versionlistx;
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
        return "DJIVersionP3xRcComponent";
    }
}
