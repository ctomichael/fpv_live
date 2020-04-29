package dji.internal.network;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.gdpr.DJIPrivacyManager;
import dji.internal.network.BaseRemoteService;
import dji.midware.util.NetworkUtils;
import java.util.List;

@EXClassNullAway
public class RemoteServerController {
    private static final String TAG = "RemoteServerController";
    private static RemoteService remoteService;

    protected RemoteServerController() {
        remoteService = RemoteService.getInstance();
    }

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final RemoteServerController INSTANCE = new RemoteServerController();

        private LazyHolder() {
        }
    }

    public static RemoteServerController getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void sendToServer(List<DJIAnalyticsEvent> events, BaseRemoteService.SDKRemoteServiceCallback callback) {
        if (!NetworkUtils.isOnline() || !DJIPrivacyManager.getInstance().hasAnalyticsEventPermission()) {
            callback.onFailure();
        } else {
            remoteService.postAnalyticsEvents(events, callback);
        }
    }

    public void getFlagsFromServer(String appKey, String installID, String displayName, String sdkVersion, String locale, BaseRemoteService.SDKRemoteServiceCallback callback) {
        if (!NetworkUtils.isOnline() || !DJIPrivacyManager.getInstance().hasAnalyticsEventPermission()) {
            callback.onFailure();
        } else {
            remoteService.getFeatureFlags(appKey, installID, displayName, sdkVersion, locale, callback);
        }
    }

    public void uploadLogZipFile(String filePath, String installId, String fileHash, BaseRemoteService.SDKRemoteServiceCallback callback) {
        if (NetworkUtils.isOnline()) {
            remoteService.uploadLogZipFile(filePath, installId, fileHash, callback);
        } else {
            callback.onFailure();
        }
    }

    public void getBindingStateFromServer(String sn, int mcc, String countryCode, BaseRemoteService.SDKRemoteServiceCallback callback) {
        if (!NetworkUtils.isOnline() || !DJIPrivacyManager.getInstance().hasDeviceInfoPermission()) {
            callback.onFailure();
        } else {
            remoteService.getBindingStateFromServer(sn, mcc, countryCode, callback);
        }
    }

    public void deleteBindingInfo(String sn, BaseRemoteService.SDKRemoteServiceCallback callback) {
        if (!NetworkUtils.isOnline() || !DJIPrivacyManager.getInstance().hasDeviceInfoPermission()) {
            callback.onFailure();
        } else {
            remoteService.deleteBindingInfo(sn, callback);
        }
    }

    public void destroy() {
        if (remoteService != null) {
            remoteService.destroy();
        }
    }
}
