package dji.internal.version.component;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.DJIModelUpgradePackList;
import dji.internal.version.DJIVersionBaseComponent;
import java.util.ArrayList;

@EXClassNullAway
public class DJIVersionM100Component extends DJIVersionBaseComponent {
    /* access modifiers changed from: protected */
    public String[] getFirmwareList() {
        return new String[]{"0306", "0500", "0900", "1200", "1201", "1202", "1203", "1500", "1900", "0100", "0101", "0400", "0800"};
    }

    /* access modifiers changed from: protected */
    public ArrayList<DJIModelUpgradePackList.DJIUpgradePack> getDJIUpgradePackList(DJIModelUpgradePackList model) {
        return model.versionlistm100;
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
        return "DJIVersionM100Component";
    }
}
