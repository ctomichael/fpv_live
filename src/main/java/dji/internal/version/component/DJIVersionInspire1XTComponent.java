package dji.internal.version.component;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.DJIModelUpgradePackList;
import dji.internal.version.DJIVersionBaseComponent;
import java.util.ArrayList;

@EXClassNullAway
public class DJIVersionInspire1XTComponent extends DJIVersionBaseComponent {
    /* access modifiers changed from: protected */
    public String[] getFirmwareList() {
        return new String[]{"0400", "0100", "0807"};
    }

    /* access modifiers changed from: protected */
    public ArrayList<DJIModelUpgradePackList.DJIUpgradePack> getDJIUpgradePackList(DJIModelUpgradePackList model) {
        return model.versionlistxt;
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
        return "DJIVersionInspire1XTComponent";
    }
}
