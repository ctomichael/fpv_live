package dji.component.flysafe.listener;

import android.support.annotation.Keep;

@Keep
public class JNIUnlockCommonCallbacks {
    /* access modifiers changed from: private */
    public static int DATABASE_FILE_UNKNOWN_ERROR = 400;
    /* access modifiers changed from: private */
    public static int NETWORKING_ERROR_START = 100;
    /* access modifiers changed from: private */
    public static int NETWORKING_UNKNOWN_ERROR = 200;
    /* access modifiers changed from: private */
    public static int PACK_MANAGER_UNKNOWN_ERROR = 300;

    @Keep
    public interface JNIUnlockCallback {
        void onFailure(int i);

        void onSuccess();
    }

    @Keep
    public interface UnlockCallbackWithErrorEnum {
        void onFailure(JNIUnlockError jNIUnlockError);

        void onSuccess();
    }

    @Keep
    public interface UnlockCommonCallbackWithInfo<T> {
        void onFailure(JNIUnlockError jNIUnlockError);

        void onSuccess(T t);
    }

    @Keep
    public interface JNIUnlockCommonCallbackWith<T> {
        void onFailure(int i);

        void onSuccess(T t);
    }

    @Keep
    public interface JNICheckWillApplyTFRSCallback {
        void onFailure();

        void onSuccess();
    }

    static /* synthetic */ int access$004() {
        int i = NETWORKING_ERROR_START + 1;
        NETWORKING_ERROR_START = i;
        return i;
    }

    static /* synthetic */ int access$104() {
        int i = NETWORKING_UNKNOWN_ERROR + 1;
        NETWORKING_UNKNOWN_ERROR = i;
        return i;
    }

    static /* synthetic */ int access$204() {
        int i = PACK_MANAGER_UNKNOWN_ERROR + 1;
        PACK_MANAGER_UNKNOWN_ERROR = i;
        return i;
    }

    static /* synthetic */ int access$304() {
        int i = DATABASE_FILE_UNKNOWN_ERROR + 1;
        DATABASE_FILE_UNKNOWN_ERROR = i;
        return i;
    }

    @Keep
    public enum JNIUnlockError {
        NoError(0),
        LocalUserTokenIsInvalid(1),
        NetworkingErrorStart(JNIUnlockCommonCallbacks.NETWORKING_ERROR_START),
        NetworkingNoNetworking(JNIUnlockCommonCallbacks.access$004()),
        NetworkingServerDataError(JNIUnlockCommonCallbacks.access$004()),
        NetworkingSignatureError(JNIUnlockCommonCallbacks.access$004()),
        NetworkingCheckSignatureError(JNIUnlockCommonCallbacks.access$004()),
        NetworkingInvalidRequeset(JNIUnlockCommonCallbacks.access$004()),
        NetworkingPageNotFound(JNIUnlockCommonCallbacks.access$004()),
        NetworkingUserIsNotLogin(JNIUnlockCommonCallbacks.access$004()),
        NetworkingOperationCancelByUser(JNIUnlockCommonCallbacks.access$004()),
        NetworkingUnknownError(JNIUnlockCommonCallbacks.NETWORKING_UNKNOWN_ERROR),
        PackManagerErrorStart(JNIUnlockCommonCallbacks.access$104()),
        PackManagerTimeout(JNIUnlockCommonCallbacks.access$104()),
        PackManagerWrongUnlockVersion(JNIUnlockCommonCallbacks.access$104()),
        PackManagerWrongDeviceId(JNIUnlockCommonCallbacks.access$104()),
        PackManagerWrongSerialNumber(JNIUnlockCommonCallbacks.access$104()),
        PackManagerUnknownError(JNIUnlockCommonCallbacks.PACK_MANAGER_UNKNOWN_ERROR),
        DataBaseErrorStart(JNIUnlockCommonCallbacks.access$204()),
        DataBaseFileError(JNIUnlockCommonCallbacks.access$204()),
        DataBaseInvalidParams(JNIUnlockCommonCallbacks.access$204()),
        DataBaseNotReady(JNIUnlockCommonCallbacks.access$204()),
        DataBaseFileUnknownError(JNIUnlockCommonCallbacks.DATABASE_FILE_UNKNOWN_ERROR),
        LicenseUnlockErrorStart(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockInvalidAreaIds(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockFCOpQueryFailed(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockFCOpSetEnableFailed(JNIUnlockCommonCallbacks.access$304()),
        SetLicenseEnableUserIdError(JNIUnlockCommonCallbacks.access$304()),
        SetLicenseEnableNoSuchLicense(JNIUnlockCommonCallbacks.access$304()),
        SetLicenseEnableVersionNotMatch(JNIUnlockCommonCallbacks.access$304()),
        SetLicenseEnableParamError(JNIUnlockCommonCallbacks.access$304()),
        SetLicenseEnableDroneInSky(JNIUnlockCommonCallbacks.access$304()),
        UploadLicenseDataUserIdError(JNIUnlockCommonCallbacks.access$304()),
        UploadLicenseDataIndexError(JNIUnlockCommonCallbacks.access$304()),
        UploadLicenseDataError(JNIUnlockCommonCallbacks.access$304()),
        UploadLicenseDataCrcCheckFailed(JNIUnlockCommonCallbacks.access$304()),
        UploadLicenseDataSNCheckFailed(JNIUnlockCommonCallbacks.access$304()),
        UploadLicenseDataVersionCheckError(JNIUnlockCommonCallbacks.access$304()),
        UploadLicenseDataDroneInSky(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockNotSupported(JNIUnlockCommonCallbacks.access$304()),
        FetchLicenseDataNoDataError(JNIUnlockCommonCallbacks.access$304()),
        FetchLicenseDataLowFirmwareVersionError(JNIUnlockCommonCallbacks.access$304()),
        FetchLicenseDataUnlockVersionError(JNIUnlockCommonCallbacks.access$304()),
        FetchLicenseDataOldDataError(JNIUnlockCommonCallbacks.access$304()),
        FetchLicenseDataInvalidKeyVersionError(JNIUnlockCommonCallbacks.access$304()),
        FetchLicenseIdNoLicenseError(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockServerInvalidTokenError(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockServerUnidentifiedPhoneError(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockServerNotUnlockableAreaError(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockServerUnlockTooManyAreasError(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockServerTooMuchLicenseError(JNIUnlockCommonCallbacks.access$304()),
        LicenseUnlockServerJsonDataParseError(JNIUnlockCommonCallbacks.access$304());
        
        private static volatile JNIUnlockError[] sValues = null;
        int mValue = -1;

        private JNIUnlockError(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }

        public static JNIUnlockError ofValue(int value) {
            if (sValues == null) {
                sValues = values();
            }
            JNIUnlockError[] jNIUnlockErrorArr = sValues;
            for (JNIUnlockError ts : jNIUnlockErrorArr) {
                if (ts.mValue == value) {
                    return ts;
                }
            }
            return DataBaseFileUnknownError;
        }
    }
}
