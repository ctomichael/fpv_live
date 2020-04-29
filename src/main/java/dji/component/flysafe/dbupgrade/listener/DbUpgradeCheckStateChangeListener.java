package dji.component.flysafe.dbupgrade.listener;

import dji.component.flysafe.dbupgrade.model.FlysafeDataUpgradeModelWrapper;

public interface DbUpgradeCheckStateChangeListener {
    void onCheckStateChange(FlysafeDataUpgradeModelWrapper.DbUpgradeCheckStateWrapper dbUpgradeCheckStateWrapper);
}
