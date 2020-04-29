package dji.common.mission;

import android.support.annotation.NonNull;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.mission.MissionMsgModel;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionGotoWaypointMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointTurnMode;
import dji.common.model.LocationCoordinate2D;
import dji.common.product.Model;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataFlycUploadWayPointMissionMsg;
import dji.midware.data.model.P3.DataFlycUploadWayPointMsgByIndex;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MissionUtils {
    public static final float ACCURACY_ERROR = 5.0E-6f;
    public static int LOCK_RC_PARAM = 259;
    public static int START_GROUPING_MEDIA = 1;
    public static int STOP_GROUPING_MEDIA = 2;
    public static int UNLOCK_RC_PARAM = 3;

    public static boolean checkValidGPSCoordinate(double homeLat, double homeLng) {
        return homeLat > -90.0d && homeLat < 90.0d && homeLng > -180.0d && homeLng < 180.0d;
    }

    public static boolean isGPSCoordinateValid(@NonNull LocationCoordinate2D locationCoordinate) {
        return locationCoordinate != null && locationCoordinate.getLatitude() > -90.0d && locationCoordinate.getLatitude() < 90.0d && locationCoordinate.getLongitude() > -180.0d && locationCoordinate.getLongitude() < 180.0d;
    }

    public static boolean isGPSCoordinateValid(@NonNull LocationCoordinate3D locationCoordinate) {
        return locationCoordinate != null && locationCoordinate.getLatitude() > -90.0d && locationCoordinate.getLatitude() < 90.0d && locationCoordinate.getLongitude() > -180.0d && locationCoordinate.getLongitude() < 180.0d;
    }

    public static double Radian(double x) {
        return (3.141592653589793d * x) / 180.0d;
    }

    public static double Degree(double x) {
        return (180.0d * x) / 3.141592653589793d;
    }

    public static boolean isWaypointMissionComplete(WaypointMission mission) {
        return (mission.getWaypointCount() == 0 || mission.getWaypointList() == null || mission.getWaypointCount() != mission.getWaypointList().size()) ? false : true;
    }

    @NonNull
    public static DataFlycUploadWayPointMissionMsg generateUploadWayPointMissionMsg(@NonNull WaypointMission.Builder missionBuilder) {
        int value;
        int value2;
        int value3;
        int value4;
        DataFlycUploadWayPointMissionMsg uploadWayPointMissionMsg = new DataFlycUploadWayPointMissionMsg();
        uploadWayPointMissionMsg.setWayPointCount(missionBuilder.getWaypointList().size());
        uploadWayPointMissionMsg.setCmdSpeed(missionBuilder.getMaxFlightSpeed());
        uploadWayPointMissionMsg.setIdleSpeed(missionBuilder.getAutoFlightSpeed());
        if (missionBuilder.getFinishedAction() == null) {
            value = WaypointMissionFinishedAction.NO_ACTION.value();
        } else {
            value = missionBuilder.getFinishedAction().value();
        }
        uploadWayPointMissionMsg.setFinishAction(DataFlycUploadWayPointMissionMsg.FINISH_ACTION.find(value));
        if (missionBuilder.getHeadingMode() == null) {
            value2 = WaypointMissionHeadingMode.AUTO.value();
        } else {
            value2 = missionBuilder.getHeadingMode().value();
        }
        uploadWayPointMissionMsg.setYawMode(DataFlycUploadWayPointMissionMsg.YAW_MODE.find(value2));
        if (missionBuilder.getFlightPathMode() == null) {
            value3 = WaypointMissionFlightPathMode.NORMAL.value();
        } else {
            value3 = missionBuilder.getFlightPathMode().value();
        }
        uploadWayPointMissionMsg.setTraceMOde(DataFlycUploadWayPointMissionMsg.TRACE_MODE.find(value3));
        uploadWayPointMissionMsg.setRepeatNum(missionBuilder.getRepeatTimes());
        if (missionBuilder.getGotoFirstWaypointMode() == null) {
            value4 = WaypointMissionGotoWaypointMode.SAFELY.value();
        } else {
            value4 = missionBuilder.getGotoFirstWaypointMode().value();
        }
        uploadWayPointMissionMsg.seGotoFirstFlag(DataFlycUploadWayPointMissionMsg.GOTO_FIRST_POINT_ACTION.find(value4));
        uploadWayPointMissionMsg.setActionOnRCLost(missionBuilder.isExitMissionOnRCSignalLostEnabled() ? DataFlycUploadWayPointMissionMsg.ACTION_ON_RC_LOST.EXIT_WP : DataFlycUploadWayPointMissionMsg.ACTION_ON_RC_LOST.CONTINUE_WP);
        uploadWayPointMissionMsg.seGimbalPitchMode(missionBuilder.isGimbalPitchRotationEnabled() ? DataFlycUploadWayPointMissionMsg.GIMBAL_PITCH_MODE.PITCH_SMOOTH : DataFlycUploadWayPointMissionMsg.GIMBAL_PITCH_MODE.PITCH_FREE);
        if (isGPSCoordinateValid(missionBuilder.getPointOfInterest())) {
            uploadWayPointMissionMsg.seHPLat(Radian(missionBuilder.getPointOfInterest().getLatitude()));
            uploadWayPointMissionMsg.seHPLng(Radian(missionBuilder.getPointOfInterest().getLongitude()));
        }
        uploadWayPointMissionMsg.setMissionID(missionBuilder.getMissionID());
        return uploadWayPointMissionMsg;
    }

    @NonNull
    public static DataFlycUploadWayPointMsgByIndex getWayPointMsgByIndex(Waypoint waypoint, int index) {
        DataFlycUploadWayPointMsgByIndex waypointProtocol = new DataFlycUploadWayPointMsgByIndex();
        waypointProtocol.setAltitude(waypoint.altitude);
        waypointProtocol.setIndex(index);
        waypointProtocol.setLatitude(Radian(waypoint.coordinate.getLatitude()));
        waypointProtocol.setLongtitude(Radian(waypoint.coordinate.getLongitude()));
        waypointProtocol.setDampingDis(waypoint.cornerRadiusInMeters);
        waypointProtocol.setTurnMode(waypoint.turnMode == WaypointTurnMode.CLOCKWISE ? DataFlycUploadWayPointMsgByIndex.TURNMODE.CLOCKWISE : DataFlycUploadWayPointMsgByIndex.TURNMODE.ANTI_CLOSEWISE);
        waypointProtocol.setActionTimeTimit((short) waypoint.actionTimeoutInSeconds);
        waypointProtocol.setTgtPitch((short) ((int) (waypoint.gimbalPitch * 10.0f)));
        if (waypoint.speed > 0.0f) {
            waypointProtocol.isHasSpeed(true);
            waypointProtocol.setWpSpeed((int) (waypoint.speed * 100.0f));
        } else {
            waypointProtocol.isHasSpeed(false);
            waypointProtocol.setWpSpeed(0);
        }
        if (waypoint.shootPhotoTimeInterval > 0.0f) {
            if (CacheHelper.getProduct(ProductKeys.MODEL_NAME) == Model.PHANTOM_4_RTK) {
                waypointProtocol.setCameraActionType(3);
            } else {
                waypointProtocol.setCameraActionType(2);
            }
            waypointProtocol.setCameraActionInterval((int) (waypoint.shootPhotoTimeInterval * 10.0f));
        }
        if (waypoint.shootPhotoDistanceInterval > 0.0f) {
            waypointProtocol.setCameraActionType(1);
            waypointProtocol.setCameraActionInterval((int) (waypoint.shootPhotoDistanceInterval * 10.0f));
        }
        waypointProtocol.setHasAction(false);
        if (waypoint.waypointActions.size() > 0) {
            waypointProtocol.setHasAction(true);
            waypointProtocol.setActionNum(waypoint.waypointActions.size());
            waypointProtocol.setActionRepeat(waypoint.actionRepeatTimes);
            ArrayList<Integer> paramsList = new ArrayList<>();
            ArrayList<DataFlycUploadWayPointMsgByIndex.ACTION> actionList = new ArrayList<>();
            for (int j = 0; j < waypoint.waypointActions.size(); j++) {
                DataFlycUploadWayPointMsgByIndex.ACTION action = DataFlycUploadWayPointMsgByIndex.ACTION.find(waypoint.waypointActions.get(j).actionType.value());
                actionList.add(action);
                int param = waypoint.waypointActions.get(j).actionParam;
                if (action.value() == DataFlycUploadWayPointMsgByIndex.ACTION.WP_ACTION_GIMBAL_PITCH.value()) {
                    if (param < -90) {
                        param = -90;
                    }
                    if (param > 30) {
                        param = 30;
                    }
                }
                if (action.value() == DataFlycUploadWayPointMsgByIndex.ACTION.WP_ACTION_CRAFT_YAW.value() && (param = param % 360) > 180) {
                    param = 180 - param;
                }
                if (action.value() == DataFlycUploadWayPointMsgByIndex.ACTION.WP_ACTION_STAY.value()) {
                    if (param < 0) {
                        param = 0;
                    } else if (param > 32767) {
                        param = 32767;
                    }
                }
                paramsList.add(Integer.valueOf(param));
            }
            waypointProtocol.setActionList(actionList);
            waypointProtocol.setParamsList(paramsList);
        }
        waypointProtocol.setTgtYaw((short) waypoint.heading);
        return waypointProtocol;
    }

    public static MissionMsgModel.DownloadedWPSummaryEvent createDownloadedWPSummaryEvent(byte[] data) {
        MissionMsgModel.DownloadedWPSummaryEvent downloadedWPSummaryEvent = new MissionMsgModel.DownloadedWPSummaryEvent();
        downloadedWPSummaryEvent.setData(data);
        return downloadedWPSummaryEvent;
    }

    public static MissionMsgModel.DownloadedWPDetailedEvent createDownloadedWPDetailedEvent(byte[] data) {
        MissionMsgModel.DownloadedWPDetailedEvent downloadedWPDetailedEvent = new MissionMsgModel.DownloadedWPDetailedEvent();
        downloadedWPDetailedEvent.setData(data);
        return downloadedWPDetailedEvent;
    }

    public static DJIError checkWaypointMissionParameters(float maxFlightSpeed, float autoFlightSpeed, int repeatTimes, int waypointCount, List<Waypoint> waypointList) {
        if (maxFlightSpeed < 2.0f || maxFlightSpeed > 15.0f) {
            return DJIMissionError.MAX_FLIGHT_SPEED_NOT_VALID;
        }
        if (autoFlightSpeed < -15.0f || autoFlightSpeed > 15.0f || Math.abs(autoFlightSpeed) > 15.0f) {
            return DJIMissionError.MAX_FLIGHT_SPEED_NOT_VALID;
        }
        if (repeatTimes < 0) {
            return DJIMissionError.REPEAT_TIME_NOT_VALID;
        }
        DJIError error = getMaxWaypointNumSynchronously(autoFlightSpeed, waypointCount, waypointList);
        if (error == null) {
            return checkEachWaypointValid(waypointList);
        }
        return error;
    }

    public static DJIError getMaxWaypointNumSynchronously(float autoFlightSpeed, int waypointCount, List<Waypoint> waypointList) {
        final int[] maxWaypointCount = {0};
        maxWaypointCount[0] = CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.MAX_WAYPOINT_NUM));
        if (maxWaypointCount[0] != 0) {
            if (maxWaypointCount[0] < 0) {
                maxWaypointCount[0] = 99;
            }
            return checkWaypointCountValid(autoFlightSpeed, maxWaypointCount[0], waypointCount, waypointList);
        }
        final CountDownLatch cdl = new CountDownLatch(1);
        CacheHelper.getFlightController(FlightControllerKeys.MAX_WAYPOINT_NUM, new DJIGetCallback() {
            /* class dji.common.mission.MissionUtils.AnonymousClass1 */

            public void onSuccess(DJISDKCacheParamValue value) {
                maxWaypointCount[0] = ((Integer) value.getData()).intValue();
                cdl.countDown();
            }

            public void onFails(DJIError error) {
                maxWaypointCount[0] = -1;
                cdl.countDown();
            }
        });
        try {
            cdl.await(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
        if (maxWaypointCount[0] < 0) {
            maxWaypointCount[0] = 99;
        }
        return checkWaypointCountValid(autoFlightSpeed, maxWaypointCount[0], waypointCount, waypointList);
    }

    private static DJIError checkWaypointCountValid(float autoFlightSpeed, int maxWaypointCount, int waypointCount, List<Waypoint> waypointList) {
        if (maxWaypointCount <= 0) {
            maxWaypointCount = 99;
        }
        if (waypointCount < 2 || waypointCount > maxWaypointCount || Math.abs(autoFlightSpeed) > 15.0f) {
            return DJIMissionError.WAYPOINT_COUNT_NOT_VALID;
        }
        if (waypointList == null || waypointList.size() > maxWaypointCount || waypointList.size() < 2 || waypointList.size() != waypointCount) {
            return DJIMissionError.WAYPOINT_LIST_SIZE_NOT_VALID;
        }
        return null;
    }

    private static DJIError checkEachWaypointValid(List<Waypoint> waypointList) {
        Waypoint lastWayPoint = null;
        for (Waypoint eachWaypoint : waypointList) {
            DJIError error = eachWaypoint.checkParameters();
            if (error != null) {
                return error;
            }
            if (lastWayPoint != null && eachWaypoint.isEqualPosition(lastWayPoint)) {
                return DJIMissionError.WAYPOINT_DISTANCE_TOO_CLOSE;
            }
            lastWayPoint = eachWaypoint;
        }
        return null;
    }
}
