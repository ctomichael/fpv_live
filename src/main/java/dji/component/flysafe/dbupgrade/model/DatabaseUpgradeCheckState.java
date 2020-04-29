package dji.component.flysafe.dbupgrade.model;

public enum DatabaseUpgradeCheckState {
    INITIALIZING,
    CHECKING,
    LOCAL_GREATER_THAN_FC,
    LOCAL_LESS_THAN_OR_EQUAL_FC,
    NEED_UPDATE,
    NEED_FORCE_UPDATE,
    UP_TO_DATE,
    CHECK_EXCEPTION,
    UPGRADE_SUCCESS,
    UNKNOWN
}
