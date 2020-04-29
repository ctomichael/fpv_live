package dji.sdksharedlib.hardware.abstractions.airlink.lb;

import dji.common.VideoDataChannel;
import dji.common.error.DJIAirLinkError;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataDm368GetPushStatus;
import dji.midware.usb.P3.LB2VideoController;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class LB2AbstractionHelper {
    private static final int EXT_VIDEO_SOURCE = 0;
    private static final int LB_VIDEO_SOURCE = 1;
    private static final int NORMAL_VIDEO_SOURCE = 2;
    private static final int RETRY_TIMES_MAX = 3;
    private static final String TAG = "DJILB2Helper";
    private final int INVALID_PERCENT = -1;
    int dualPercentCache;
    boolean isEXTVideoInputPortEnabled;
    private Lightbridge2Abstraction lb2AirLink;
    int lbPercentCache;
    private DataDm368GetPushStatus pushStatus = DataDm368GetPushStatus.getInstance();
    private int retryTimes;

    public LB2AbstractionHelper(Lightbridge2Abstraction lb2AirLink2) {
        this.lb2AirLink = lb2AirLink2;
    }

    public void setup() {
        DJIEventBusUtil.register(this);
        this.lbPercentCache = -1;
        this.dualPercentCache = -1;
        this.lb2AirLink.videoChannel = VideoDataChannel.UNKNOWN;
        this.retryTimes = 0;
        refreshCacheAndUpdateVideoChannel();
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(LB2VideoController.EncodeMode mode) {
        if (!this.lb2AirLink.isDualEncodeModeSupported()) {
            return;
        }
        if (mode == LB2VideoController.EncodeMode.SINGLE) {
            onEXTVideoInputPortEnabledChange(true);
        } else if (mode == LB2VideoController.EncodeMode.DUAL) {
            onEXTVideoInputPortEnabledChange(false);
        }
    }

    public void onEXTVideoInputPortEnabledChange(boolean enabled) {
        if (enabled != this.isEXTVideoInputPortEnabled) {
            this.isEXTVideoInputPortEnabled = enabled;
            if (this.lb2AirLink == null) {
                return;
            }
            if (enabled) {
                encodeModeChangeToSingle();
            } else {
                encodeModeChangeToDual();
            }
        }
    }

    private void encodeModeChangeToSingle() {
        Float lbPercent;
        if (this.lbPercentCache == -1 && (lbPercent = (Float) CacheHelper.getLightbridgeLink(LightbridgeLinkKeys.BANDWIDTH_ALLOCATION_FOR_LB_VIDEO_INPUT_PORT)) != null) {
            this.lbPercentCache = (int) (lbPercent.floatValue() * 10.0f);
        }
        updateVideoChannel();
    }

    private void encodeModeChangeToDual() {
        this.lb2AirLink.setFPVVideoBandwidthPercent(1.0f, null);
        updateVideoChannel();
    }

    public void onLBPercentChange(int lbPercent) {
        if (lbPercent != this.lbPercentCache) {
            this.lbPercentCache = lbPercent;
            updateVideoChannel();
        }
    }

    public void onDualPercentChange(int dualPercent) {
        if (dualPercent != this.dualPercentCache) {
            this.dualPercentCache = dualPercent;
            updateVideoChannel();
        }
    }

    public DJIError refreshCache() {
        if (this.lb2AirLink == null) {
            return DJIAirLinkError.COMMON_DISCONNECTED;
        }
        if (!this.lb2AirLink.isSecondaryVideoOutputSupported()) {
            return DJIAirLinkError.COMMON_UNSUPPORTED;
        }
        if (this.lb2AirLink.isDualEncodeModeSupported()) {
            this.dualPercentCache = this.pushStatus.getDualEncodeModePercentage();
        }
        Boolean enabled = (Boolean) CacheHelper.getLightbridgeLink(LightbridgeLinkKeys.IS_EXT_VIDEO_INPUT_PORT_ENABLED);
        if (enabled != null) {
            this.isEXTVideoInputPortEnabled = enabled.booleanValue();
        }
        Float lbBandwidth = (Float) CacheHelper.getLightbridgeLink(LightbridgeLinkKeys.BANDWIDTH_ALLOCATION_FOR_LB_VIDEO_INPUT_PORT);
        if (lbBandwidth != null) {
            this.lbPercentCache = (int) (lbBandwidth.floatValue() * 10.0f);
        }
        return null;
    }

    private void refreshCacheAndUpdateVideoChannel() {
        if (this.lb2AirLink != null) {
            DJIError refreshError = refreshCache();
            if (refreshError != null && this.retryTimes < 3) {
                this.retryTimes++;
                refreshCacheAndUpdateVideoChannel();
            } else if (refreshError != null && this.retryTimes >= 3) {
                this.retryTimes = 0;
            } else if (refreshError == null) {
                updateVideoChannel();
            }
        }
    }

    private void updateVideoChannel() {
        if (this.lb2AirLink == null) {
        }
    }
}
