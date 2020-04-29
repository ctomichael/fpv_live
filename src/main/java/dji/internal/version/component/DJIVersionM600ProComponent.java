package dji.internal.version.component;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.DJIModelUpgradePackList;
import java.util.ArrayList;

@EXClassNullAway
public class DJIVersionM600ProComponent extends DJIVersionM600Component {
    /* access modifiers changed from: protected */
    public String[] getFirmwareList() {
        return new String[]{"1100", "1101", "1102", "1103", "1104", "1105", "1106", "1200", "1201", "1202", "1203", "1204", "1205", "0305", "0306"};
    }

    /* access modifiers changed from: protected */
    public ArrayList<DJIModelUpgradePackList.DJIUpgradePack> getDJIUpgradePackList(DJIModelUpgradePackList model) {
        return model.versionlistm600pro;
    }

    /* access modifiers changed from: protected */
    public String getComponentName() {
        return "DJIVersionM600ProComponent";
    }
}
