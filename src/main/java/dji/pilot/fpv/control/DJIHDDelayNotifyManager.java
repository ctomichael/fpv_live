package dji.pilot.fpv.control;

import android.os.Build;
import android.os.Handler;
import dji.apppublic.reflect.AppPubInjectManager;
import dji.common.error.DJIError;
import dji.log.DJILog;
import dji.midware.data.model.P3.DataCameraSetVOutParams;
import dji.midware.parser.plugins.DJIPluginRingBufferAsyncParser;
import dji.midware.usb.P3.AoaReportHelper;
import dji.midware.util.ContextUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.pilot.fpv.model.IEventObjects;
import dji.pilot.publics.R;
import dji.publics.utils.EventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIHDDelayNotifyManager implements DJIParamAccessListener {
    private static final String KEY_HAS_SET_1080_HD = "key_has_set_1080_hd";
    private static boolean hasDownTo720p = false;
    private static boolean hasSet1080p = false;

    private enum TipType {
        JUST_INTO_FPV,
        RUNNING_FPV
    }

    public DJIHDDelayNotifyManager() {
        EventBusUtil.regist(this);
        CacheHelper.addCameraListener(this, CameraKeys.STREAM_QUALITY);
        hasSet1080p = DjiSharedPreferencesManager.getBoolean(ContextUtil.getContext(), KEY_HAS_SET_1080_HD, false);
        if (isSystemTooLowNotSupport1080() && !hasSet1080p) {
            new Handler().postDelayed(new Runnable() {
                /* class dji.pilot.fpv.control.DJIHDDelayNotifyManager.AnonymousClass1 */

                public void run() {
                    DJIHDDelayNotifyManager.this.downTo720HdAndTip(TipType.JUST_INTO_FPV);
                }
            }, 1000);
        }
    }

    /* access modifiers changed from: private */
    public void downTo720HdAndTip(final TipType tipType) {
        if (((DataCameraSetVOutParams.LCDFormat) CacheHelper.getCamera(CameraKeys.STREAM_QUALITY)) == DataCameraSetVOutParams.LCDFormat.HD_FORMAT) {
            if (!AppPubInjectManager.getAppPubToP3Injectable().isInFpv()) {
                DJILog.logWriteD(AoaReportHelper.TAG_CONNECT_DEBUG, "DJIHDDelayNotifyManager set 1080 not in fpv", AoaReportHelper.TAG_CONNECT_DEBUG, new Object[0]);
                return;
            }
            if (tipType == TipType.RUNNING_FPV) {
                hasDownTo720p = true;
                new IEventObjects.PopViewItem.ErrorModel.FakeBuilder().setTitle(R.string.hd_liveview_stuck_to720p_tip).setMessageType(IEventObjects.PopViewItem.MessageType.NOTIFY).postEvent();
            }
            CacheHelper.setCamera(CameraKeys.STREAM_QUALITY, DataCameraSetVOutParams.LCDFormat.AUTO_NO_GLASS_CONNECTED, new DJISetCallback() {
                /* class dji.pilot.fpv.control.DJIHDDelayNotifyManager.AnonymousClass2 */

                public void onSuccess() {
                    if (tipType == TipType.JUST_INTO_FPV) {
                        new IEventObjects.PopViewItem.ErrorModel.FakeBuilder().setTitle(R.string.hd_liveview_system_too_low_tip).setMessageType(IEventObjects.PopViewItem.MessageType.NOTIFY).postEvent();
                    }
                }

                public void onFails(DJIError error) {
                    DJILog.logWriteD(AoaReportHelper.TAG_CONNECT_DEBUG, "DJIHDDelayNotifyManager set 1080 fail", AoaReportHelper.TAG_CONNECT_DEBUG, new Object[0]);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3BackgroundThread(DJIPluginRingBufferAsyncParser.AOA_BUFFER_EVENT event) {
        downTo720HdAndTip(TipType.RUNNING_FPV);
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
    }

    public void onDestroy() {
        EventBusUtil.unregist(this);
        CacheHelper.removeListener(this);
    }

    public static boolean isHasDownTo720p() {
        return hasDownTo720p;
    }

    public static void setHasSet1080p(boolean hasSet) {
        hasSet1080p = hasSet;
        DjiSharedPreferencesManager.putBoolean(ContextUtil.getContext(), KEY_HAS_SET_1080_HD, hasSet);
    }

    public static boolean isSystemTooLowNotSupport1080() {
        return Build.VERSION.SDK_INT < 24;
    }
}
