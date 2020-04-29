package dji.component.flysafe.dbupgrade.listener;

import dji.component.flysafe.dbupgrade.model.FlysafeDataUpgradeModelWrapper;

public interface DJIDbUpgradeStateChangeListener {
    void onUpgradeStateChange(FlysafeDataUpgradeModelWrapper.DbUpgradeStateWrapper dbUpgradeStateWrapper);
}
