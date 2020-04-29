package dji.internal.version.component;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.DJIModelUpgradePackList;
import dji.internal.version.DJIVersionBaseComponent;
import java.util.ArrayList;

@EXClassNullAway
public class DJIVersionP3sComponent extends DJIVersionBaseComponent {
    /* access modifiers changed from: protected */
    public String[] getFirmwareList() {
        return new String[]{"0305", "0306", "0400", "0900", "1100", "1200", "1201", "1202", "1203", "1500", "1700", "1701", "1900", "0100", "0101"};
    }

    /* access modifiers changed from: protected */
    public ArrayList<DJIModelUpgradePackList.DJIUpgradePack> getDJIUpgradePackList(DJIModelUpgradePackList model) {
        return model.versionlists;
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
        return "DJIVersionP3sComponent";
    }
}
