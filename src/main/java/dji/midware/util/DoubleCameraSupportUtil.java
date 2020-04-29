package dji.midware.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.DJIComponentManager;
import dji.midware.parser.plugins.DJIPluginLBChanneParser;
import dji.midware.usb.P3.SdrLteVideoController;

@EXClassNullAway
public class DoubleCameraSupportUtil {
    public static final int MAIN_CAMERA_ID = 0;
    public static final int MAIN_GIMBAL_ID = 0;
    public static final int MAIN_PAYLOAD_ID = 0;
    private static final int MAX_CAMERA_BANDWIDTH_PERCENT = 10;
    private static final int MIN_CAMERA_BANDWIDTH_PERCENT = 0;
    public static final int SECONDARY_CAMERA_ID = 2;
    public static final int SECONDARY_GIMBAL_ID = 2;
    public static final int SECONDARY_PAYLOAD_ID = 2;
    public static volatile boolean SupportDoubleCamera = false;
    public static final String USER_SET_MAIN_CAMERA_BANDWIDTH_PERCENT = "UserSetMainCameraBandwidthPercent";
    public static final int XT2_INFRARED_CAMERA_ID = 1;
    private static volatile int mainCameraBandwidthPercent = 10;
    private static volatile int mainCameraChannelId = -1;
    private static volatile DJIComponentManager.CameraComponentType mainCameraType = DJIComponentManager.CameraComponentType.None;
    private static volatile int secondaryCameraChannelId = -1;
    private static volatile DJIComponentManager.CameraComponentType secondaryCameraType = DJIComponentManager.CameraComponentType.None;
    private static volatile int trackMissionCameraID = 0;

    public static int getTrackMissionCameraID() {
        return trackMissionCameraID;
    }

    public static void setTrackMissionCameraID(int trackMissionCameraID2) {
        trackMissionCameraID = trackMissionCameraID2;
    }

    public static int getMainCameraBandwidthPercent() {
        return mainCameraBandwidthPercent;
    }

    public static void setMainCameraBandwidthPercent(int mainCameraBandwidthPercent2) {
        mainCameraBandwidthPercent = mainCameraBandwidthPercent2;
        reset();
    }

    public static DJIComponentManager.CameraComponentType getMainCameraType() {
        return mainCameraType;
    }

    public static void setMainCameraType(DJIComponentManager.CameraComponentType mainCameraType2) {
        mainCameraType = mainCameraType2;
    }

    public static DJIComponentManager.CameraComponentType getSecondaryCameraType() {
        return secondaryCameraType;
    }

    public static void setSecondaryCameraType(DJIComponentManager.CameraComponentType secondaryCameraType2) {
        secondaryCameraType = secondaryCameraType2;
    }

    public static final int getCameraIdInProtocol(int index) {
        switch (index) {
            case 0:
            default:
                return 0;
            case 1:
                return 2;
            case 2:
                return 1;
        }
    }

    public static final int getGimbalIdInProtocol(int index) {
        switch (index) {
            case 0:
            default:
                return 0;
            case 1:
                return 2;
        }
    }

    public static final int getPayloadIdInProtocol(int index) {
        switch (index) {
            case 0:
            default:
                return 0;
            case 1:
                return 2;
        }
    }

    public static final int getCameraIdInAbstration(int protocolIndex) {
        switch (protocolIndex) {
            case 0:
            default:
                return 0;
            case 1:
                return 2;
            case 2:
                return 1;
        }
    }

    public static final synchronized int getMainCameraChannelID() {
        int i;
        synchronized (DoubleCameraSupportUtil.class) {
            if (mainCameraChannelId == -1) {
                if (mainCameraBandwidthPercent <= 0) {
                    switch (secondaryCameraType) {
                        case X4S:
                        case X5S:
                            mainCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.LiveView.value();
                            i = mainCameraChannelId;
                            break;
                        case XT2:
                        default:
                            i = DJIPluginLBChanneParser.DJILBChannelID.LiveView.value();
                            break;
                        case GD600:
                            mainCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.ThirdLiveViewZ30.value();
                            i = mainCameraChannelId;
                            break;
                        case TAU336:
                        case TAU640:
                            mainCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.FourthLiveViewXT.value();
                            i = mainCameraChannelId;
                            break;
                        case PayloadCamera:
                            mainCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.PayloadLiveView.value();
                            i = mainCameraChannelId;
                            break;
                    }
                } else {
                    switch (mainCameraType) {
                        case X4S:
                        case X5S:
                        case XT2:
                            mainCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.LiveView.value();
                            i = mainCameraChannelId;
                            break;
                        case GD600:
                            mainCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.ThirdLiveViewZ30.value();
                            i = mainCameraChannelId;
                            break;
                        case TAU336:
                        case TAU640:
                            mainCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.FourthLiveViewXT.value();
                            i = mainCameraChannelId;
                            break;
                        case PayloadCamera:
                            mainCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.PayloadLiveView.value();
                            i = mainCameraChannelId;
                            break;
                        default:
                            i = DJIPluginLBChanneParser.DJILBChannelID.LiveView.value();
                            break;
                    }
                }
            } else {
                i = mainCameraChannelId;
            }
        }
        return i;
    }

    public static final synchronized int getSecondaryCameraId() {
        int i;
        synchronized (DoubleCameraSupportUtil.class) {
            if (secondaryCameraChannelId == -1) {
                if (mainCameraBandwidthPercent != 0 && mainCameraBandwidthPercent != 10) {
                    switch (secondaryCameraType) {
                        case X4S:
                        case X5S:
                            secondaryCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.LiveView.value();
                            i = secondaryCameraChannelId;
                            break;
                        case XT2:
                        default:
                            i = DJIPluginLBChanneParser.DJILBChannelID.SecondaryLiveView.value();
                            break;
                        case GD600:
                            secondaryCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.ThirdLiveViewZ30.value();
                            i = secondaryCameraChannelId;
                            break;
                        case TAU336:
                        case TAU640:
                            secondaryCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.FourthLiveViewXT.value();
                            i = secondaryCameraChannelId;
                            break;
                        case PayloadCamera:
                            secondaryCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.PayloadLiveView.value();
                            i = secondaryCameraChannelId;
                            break;
                    }
                } else {
                    secondaryCameraChannelId = DJIPluginLBChanneParser.DJILBChannelID.SecondaryLiveView.value();
                    i = secondaryCameraChannelId;
                }
            } else {
                i = secondaryCameraChannelId;
            }
        }
        return i;
    }

    public static final synchronized void reset() {
        synchronized (DoubleCameraSupportUtil.class) {
            secondaryCameraChannelId = -1;
            mainCameraChannelId = -1;
        }
    }

    public static boolean isPrimaryVideoFromPrimaryCamera() {
        if (!SupportDoubleCamera || getMainCameraBandwidthPercent() != 0 || SdrLteVideoController.getInstance().isPrimaryVideoFromPrimaryCamera()) {
            return true;
        }
        return false;
    }

    public static boolean isSecondaryVideoFromSecondaryCamera() {
        if (!SupportDoubleCamera) {
            return false;
        }
        int mainCameraBandwidthPercent2 = getMainCameraBandwidthPercent();
        if ((mainCameraBandwidthPercent2 == 0 || mainCameraBandwidthPercent2 == 10) && !SdrLteVideoController.getInstance().isSecondaryVideoFromSecondaryCamera()) {
            return false;
        }
        return true;
    }
}
