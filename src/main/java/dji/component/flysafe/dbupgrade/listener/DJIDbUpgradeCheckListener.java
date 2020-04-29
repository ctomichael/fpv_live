package dji.component.flysafe.dbupgrade.listener;

import dji.component.flysafe.dbupgrade.model.FlysafeDataUpgradeModelWrapper;
import java.util.List;

public interface DJIDbUpgradeCheckListener {
    void offlineDbNotice(boolean z);

    void toConnectMC();

    void toUpgrade(List<FlysafeDataUpgradeModelWrapper.TypeDataUpgrade> list, List<FlysafeDataUpgradeModelWrapper.TypeDataUpgrade> list2, String str, int i, boolean z);
}
