package dji.component.flysafe.dbupgrade.model;

import dji.component.flysafe.dbupgrade.listener.DJIDataUpgradeListener;
import dji.fieldAnnotation.EXClassNullAway;

public class FlysafeDataUpgradeModelWrapper {

    public enum CheckException {
        SERVER_EXCEPTION,
        CHECK_TIMEOUT,
        NONE
    }

    public enum DataUpgradeCheckAllFinish {
        TRUE
    }

    public enum DbCacheState {
        EXIST,
        NOT_EXIST,
        UNKNOWN_SERVER_VERSION
    }

    public enum DbUpgradeState {
        IDLE,
        DOWNLOADING,
        DOWNLOAD_SUCCESS,
        UPGRADING,
        DOWNLOAD_FAIL,
        UPGRADE_FAIL,
        UPGRADE_SUCCESS
    }

    @EXClassNullAway
    public enum TypeDataUpgrade {
        APP_POLYGON_LOCAL,
        REMOTE,
        APP_CIRCLE_OLD
    }

    public static class DbUpgradeStateWrapper {
        public static final DbUpgradeStateWrapper DEFAULT_DB_UPGRADE_STATE = new DbUpgradeStateWrapper(DbUpgradeState.IDLE, DJIDataUpgradeListener.DataFailReason.NONE);
        private DbUpgradeState mDbUpgradeState;
        private DJIDataUpgradeListener.DataFailReason mFailReason;

        public DbUpgradeStateWrapper(DbUpgradeState dbUpgradeState, DJIDataUpgradeListener.DataFailReason failReason) {
            this.mDbUpgradeState = dbUpgradeState;
            this.mFailReason = failReason;
        }

        public DbUpgradeStateWrapper(DbUpgradeState dbUpgradeState) {
            this.mDbUpgradeState = dbUpgradeState;
            this.mFailReason = DJIDataUpgradeListener.DataFailReason.NONE;
        }

        public DbUpgradeState getDbUpgradeState() {
            return this.mDbUpgradeState;
        }

        public DJIDataUpgradeListener.DataFailReason getFailReason() {
            return this.mFailReason;
        }

        public void setDbUpgradeState(DbUpgradeState dbUpgradeState) {
            this.mDbUpgradeState = dbUpgradeState;
        }

        public void setFailReason(DJIDataUpgradeListener.DataFailReason failReason) {
            this.mFailReason = failReason;
        }
    }

    public static class DbUpgradeCheckStateWrapper {
        public static final DbUpgradeCheckStateWrapper DEFAULT_DB_UPGRADE_CHECK_STATE = new DbUpgradeCheckStateWrapper(DatabaseUpgradeCheckState.UNKNOWN, "");
        private CheckException mCheckException = CheckException.NONE;
        private DatabaseUpgradeCheckState mCheckState;
        private String mLatestVersion = "";

        public DbUpgradeCheckStateWrapper(DatabaseUpgradeCheckState state, String ver) {
            this.mCheckState = state;
            this.mLatestVersion = ver;
        }

        public DbUpgradeCheckStateWrapper(DatabaseUpgradeCheckState state) {
            this.mCheckState = state;
            this.mLatestVersion = "";
        }

        public DbUpgradeCheckStateWrapper(DatabaseUpgradeCheckState checkState, CheckException checkException) {
            this.mCheckState = checkState;
            this.mCheckException = checkException;
        }

        public DatabaseUpgradeCheckState getCheckState() {
            return this.mCheckState;
        }

        public String getLatestVersion() {
            return this.mLatestVersion;
        }

        public CheckException getCheckException() {
            return this.mCheckException;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer("DbUpgradeCheckStateWrapper{");
            sb.append("mCheckState=").append(this.mCheckState);
            sb.append(", mLatestVersion='").append(this.mLatestVersion).append('\'');
            sb.append(", mCheckException=").append(this.mCheckException);
            sb.append('}');
            return sb.toString();
        }
    }
}
