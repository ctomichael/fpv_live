package dji.component.flysafe.unlock.model;

public enum AccountStateBeforeUnlock {
    CURRENT_UAV_LICENSE_TOO_MORE,
    CURRENT_UAV_FIRMWARE_VERSION_TOO_LOW,
    CURRENT_UAV_LICENSE_STATE_OK,
    ACCOUNT_NOT_VERIFY,
    ALLOW_TO_UNLOCK,
    SERVER_RESULT_FAIL
}
