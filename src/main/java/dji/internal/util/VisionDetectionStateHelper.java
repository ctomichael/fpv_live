package dji.internal.util;

import android.graphics.RectF;
import dji.common.flightcontroller.ObstacleDetectionSector;
import dji.common.flightcontroller.ObstacleDetectionSectorWarning;
import dji.common.flightcontroller.VisionControlState;
import dji.common.flightcontroller.VisionDetectionState;
import dji.common.flightcontroller.VisionLandingProtectionState;
import dji.common.flightcontroller.VisionSensorPosition;
import dji.common.flightcontroller.VisionSystemWarning;
import dji.common.flightcontroller.flightassistant.SmartCaptureAction;
import dji.common.flightcontroller.flightassistant.SmartCaptureFollowingMode;
import dji.common.flightcontroller.flightassistant.SmartCaptureState;
import dji.common.flightcontroller.flightassistant.SmartCaptureSystemStatus;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataEyeGetPushAdvancedPilotAssistantSystemState;
import dji.midware.data.model.P3.DataEyeGetPushAvoidanceParam;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushFlatCheck;
import dji.midware.data.model.P3.DataEyeGetPushFrontAvoidance;
import dji.midware.data.model.P3.DataEyeGetPushFunctionList;
import dji.midware.data.model.P3.DataEyeGetPushPalmControlNotification;
import dji.midware.data.model.P3.DataEyeGetPushTrackStatus;
import dji.midware.data.model.P3.DataFlycGetPushAvoidParam;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;

@EXClassNullAway
public class VisionDetectionStateHelper {
    public static void parseInformationFromDetectionPushedData(DataEyeGetPushAvoidanceParam pushAvoidanceParams, DataEyeGetPushFrontAvoidance pushFrontAvoidanceParams, VisionDetectionState.Callback stateUpdatedCallback) {
        boolean sensorBeingUsed;
        boolean dataValid;
        ObstacleDetectionSector[] detectionSectors;
        switch (pushFrontAvoidanceParams.getSensorType()) {
            case Front:
                sensorBeingUsed = pushAvoidanceParams.isAvoidFrontWork();
                break;
            case Back:
                sensorBeingUsed = pushAvoidanceParams.isAvoidBehindWork();
                break;
            case Left:
                sensorBeingUsed = pushAvoidanceParams.isAvoidLeftWork();
                break;
            case Right:
                sensorBeingUsed = pushAvoidanceParams.isAvoidRightWork();
                break;
            default:
                sensorBeingUsed = false;
                break;
        }
        double obstacleDistanceInMeters = 0.0d;
        VisionSensorPosition position = convertSensorPositionFromProtocolToSDK(pushFrontAvoidanceParams.getSensorType());
        VisionSystemWarning systemWarning = VisionSystemWarning.find(getAvoidAlertLevel(pushAvoidanceParams, position));
        int[] distances = pushFrontAvoidanceParams.getObserveValues();
        if (!isPushAvoidanceValid(pushAvoidanceParams)) {
            dataValid = false;
        } else if (VisionSystemWarning.INVALID.equals(systemWarning) || VisionSystemWarning.UNKNOWN.equals(systemWarning)) {
            dataValid = false;
        } else {
            dataValid = isDistanceDataValid(position, distances);
        }
        if (!dataValid) {
            stateUpdatedCallback.onUpdate(VisionDetectionState.createInstance(sensorBeingUsed, 0.0d, VisionSystemWarning.INVALID, null, position, isDisabled(position)));
            return;
        }
        if (position == VisionSensorPosition.NOSE || position == VisionSensorPosition.TAIL) {
            detectionSectors = getVisionSectorState(distances);
        } else {
            obstacleDistanceInMeters = ((double) distances[0]) / 100.0d;
            detectionSectors = null;
        }
        stateUpdatedCallback.onUpdate(VisionDetectionState.createInstance(sensorBeingUsed, obstacleDistanceInMeters, systemWarning, detectionSectors, position, isDisabled(position)));
    }

    private static boolean isPushAvoidanceValid(DataEyeGetPushAvoidanceParam pushAvoidanceParams) {
        return (pushAvoidanceParams.isAvoidFrontWork() || pushAvoidanceParams.isAvoidBehindWork() || pushAvoidanceParams.isAvoidLeftWork() || pushAvoidanceParams.isAvoidRightWork()) && pushAvoidanceParams.isVisualSensorWorking();
    }

    private static boolean isDistanceDataValid(VisionSensorPosition position, int[] distances) {
        if (distances == null || distances.length == 0 || distances.length > 15) {
            return false;
        }
        switch (position) {
            case RIGHT:
            case LEFT:
                if (((float) distances[0]) / 100.0f <= 10.0f) {
                    return true;
                }
                return false;
            case TAIL:
            case NOSE:
                int failCount = 0;
                for (int i : distances) {
                    if (((float) i) / 100.0f > 15.0f) {
                        failCount++;
                    }
                }
                if (failCount == distances.length) {
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private static ObstacleDetectionSector[] getVisionSectorState(int[] distances) {
        if (distances == null || distances.length == 0 || distances.length > 15) {
            return null;
        }
        ObstacleDetectionSector[] obstacleInfo = new ObstacleDetectionSector[distances.length];
        for (int i = 0; i < distances.length; i++) {
            obstacleInfo[i] = new ObstacleDetectionSector(getSectorWarningLevel(distances[i]), ((float) distances[i]) / 100.0f);
        }
        return obstacleInfo;
    }

    private static ObstacleDetectionSectorWarning getSectorWarningLevel(int distanceInCm) {
        ProductType productType = DJIProductManager.getInstance().getType();
        if (ProductType.Tomato == productType || ProductType.KumquatS == productType || ProductType.KumquatX == productType) {
            if (distanceInCm < 0) {
                return ObstacleDetectionSectorWarning.UNKNOWN;
            }
            if (distanceInCm >= 7000) {
                return ObstacleDetectionSectorWarning.INVALID;
            }
            if (distanceInCm <= 200) {
                return ObstacleDetectionSectorWarning.LEVEL_6;
            }
            if (distanceInCm <= 400) {
                return ObstacleDetectionSectorWarning.LEVEL_5;
            }
            if (distanceInCm <= 600) {
                return ObstacleDetectionSectorWarning.LEVEL_4;
            }
            if (distanceInCm <= 800) {
                return ObstacleDetectionSectorWarning.LEVEL_3;
            }
            if (distanceInCm <= 1000) {
                return ObstacleDetectionSectorWarning.LEVEL_2;
            }
            return ObstacleDetectionSectorWarning.LEVEL_1;
        } else if (ProductType.Mammoth == productType) {
            if (distanceInCm >= 7000) {
                return ObstacleDetectionSectorWarning.INVALID;
            }
            if (distanceInCm <= 200) {
                return ObstacleDetectionSectorWarning.LEVEL_3;
            }
            if (distanceInCm <= 400) {
                return ObstacleDetectionSectorWarning.LEVEL_2;
            }
            return ObstacleDetectionSectorWarning.LEVEL_1;
        } else if (CommonUtil.isM200Product(productType)) {
            if (distanceInCm < 0) {
                return ObstacleDetectionSectorWarning.UNKNOWN;
            }
            if (distanceInCm >= 7000) {
                return ObstacleDetectionSectorWarning.INVALID;
            }
            if (distanceInCm <= 300) {
                return ObstacleDetectionSectorWarning.LEVEL_6;
            }
            if (distanceInCm <= 600) {
                return ObstacleDetectionSectorWarning.LEVEL_5;
            }
            if (distanceInCm <= 1200) {
                return ObstacleDetectionSectorWarning.LEVEL_4;
            }
            if (distanceInCm <= 2000) {
                return ObstacleDetectionSectorWarning.LEVEL_3;
            }
            if (distanceInCm <= 3000) {
                return ObstacleDetectionSectorWarning.LEVEL_2;
            }
            return ObstacleDetectionSectorWarning.LEVEL_1;
        } else if (distanceInCm < 0) {
            return ObstacleDetectionSectorWarning.UNKNOWN;
        } else {
            if (distanceInCm >= 7000) {
                return ObstacleDetectionSectorWarning.INVALID;
            }
            if (distanceInCm <= 300) {
                return ObstacleDetectionSectorWarning.LEVEL_6;
            }
            if (distanceInCm <= 600) {
                return ObstacleDetectionSectorWarning.LEVEL_5;
            }
            if (distanceInCm <= 1000) {
                return ObstacleDetectionSectorWarning.LEVEL_4;
            }
            if (distanceInCm <= 1500) {
                return ObstacleDetectionSectorWarning.LEVEL_3;
            }
            if (distanceInCm <= 2000) {
                return ObstacleDetectionSectorWarning.LEVEL_2;
            }
            return ObstacleDetectionSectorWarning.LEVEL_1;
        }
    }

    private static VisionSensorPosition convertSensorPositionFromProtocolToSDK(DataEyeGetPushFrontAvoidance.SensorType sensorType) {
        switch (sensorType) {
            case Front:
                return VisionSensorPosition.NOSE;
            case Back:
                return VisionSensorPosition.TAIL;
            case Left:
                return VisionSensorPosition.LEFT;
            case Right:
                return VisionSensorPosition.RIGHT;
            default:
                return VisionSensorPosition.UNKNOWN;
        }
    }

    private static int getAvoidAlertLevel(DataEyeGetPushAvoidanceParam pushAvoidanceParams, VisionSensorPosition position) {
        switch (position) {
            case RIGHT:
                return pushAvoidanceParams.getAvoidRightAlertLevel();
            case LEFT:
                return pushAvoidanceParams.getAvoidLeftAlertLevel();
            case TAIL:
                return pushAvoidanceParams.getAvoidBehindAlertLevel();
            case NOSE:
                return pushAvoidanceParams.getAvoidFrontAlertLevel();
            default:
                return VisionSystemWarning.UNKNOWN.value();
        }
    }

    private static boolean isDisabled(VisionSensorPosition position) {
        DataEyeGetPushFunctionList func = DataEyeGetPushFunctionList.getInstance();
        switch (position) {
            case RIGHT:
                return func.isRightDisable();
            case LEFT:
                return func.isLeftDisable();
            case TAIL:
                return func.isBackDisable();
            case NOSE:
                return func.isFrontDisable();
            default:
                return false;
        }
    }

    public static SmartCaptureState getSmartCaptureState() {
        DataEyeGetPushPalmControlNotification notification = DataEyeGetPushPalmControlNotification.getInstance();
        DataEyeGetPushTrackStatus trackStatus = DataEyeGetPushTrackStatus.getInstance();
        SmartCaptureState.Builder smartCaptureStateBuilder = new SmartCaptureState.Builder();
        SmartCaptureFollowingMode smartCaptureFollowingMode = (SmartCaptureFollowingMode) CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.SMART_CAPTURE_FOLLOWING_MODE);
        if (smartCaptureFollowingMode == null) {
            smartCaptureFollowingMode = SmartCaptureFollowingMode.UNKNOWN;
        }
        if (notification.isGetted()) {
            int shootPhotoCounting = notification.getShootPhotoCounting();
            smartCaptureStateBuilder.action(SmartCaptureAction.convertNotificationToAction(notification)).systemStatus(SmartCaptureSystemStatus.convertNotificationToSystemStatus(notification)).photoCountdown(shootPhotoCounting == 255 ? 0.0f : ((float) shootPhotoCounting) / 10.0f);
        }
        if (isRectDimissionValid(trackStatus)) {
            smartCaptureStateBuilder.targetRect(new RectF(trackStatus.getCenterX() - (trackStatus.getWidth() / 2.0f), trackStatus.getCenterY() - (trackStatus.getHeight() / 2.0f), trackStatus.getCenterX() + (trackStatus.getWidth() / 2.0f), trackStatus.getCenterY() + (trackStatus.getHeight() / 2.0f)));
        }
        return smartCaptureStateBuilder.followingMode(smartCaptureFollowingMode).build();
    }

    public static VisionControlState getVisionControlState() {
        boolean isBraking = DataEyeGetPushAvoidanceParam.getInstance().isBraking();
        boolean isAscentLimitedByObstacle = DataFlycGetPushAvoidParam.getInstance().roofLimitWorkFlag();
        boolean isAvoidingActiveObstacleCollision = DataFlycGetPushAvoidParam.getInstance().isAvoidOvershotAct();
        VisionLandingProtectionState currentState = VisionLandingProtectionState.find(DataEyeGetPushFlatCheck.getInstance().getFlatStatus().value());
        return new VisionControlState.Builder().landingProtectionState(currentState).isAdvancedPilotAssistanceSystemActive(DataEyeGetPushAdvancedPilotAssistantSystemState.getInstance().isAPASOn() && DataEyeGetPushAdvancedPilotAssistantSystemState.getInstance().isAPASWorking()).isAscentLimitedByObstacle(isAscentLimitedByObstacle).isAvoidingActiveObstacleCollision(isAvoidingActiveObstacleCollision).isBraking(isBraking).isPerformingPrecisionLanding(DataEyeGetPushException.getInstance().isInPreciseLanding()).build();
    }

    private static boolean isRectDimissionValid(DataEyeGetPushTrackStatus status) {
        return (status.getCenterX() == 0.0f && status.getCenterY() == 0.0f && status.getWidth() == 0.0f && status.getHeight() == 0.0f) ? false : true;
    }
}
