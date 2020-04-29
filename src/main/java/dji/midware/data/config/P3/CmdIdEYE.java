package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataEyeGetPushAdvancedPilotAssistantSystemState;
import dji.midware.data.model.P3.DataEyeGetPushAvoidanceParam;
import dji.midware.data.model.P3.DataEyeGetPushDrawState;
import dji.midware.data.model.P3.DataEyeGetPushEasySelfCalibration;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushFaceDetectionTakeOffState;
import dji.midware.data.model.P3.DataEyeGetPushFixedWingState;
import dji.midware.data.model.P3.DataEyeGetPushFlatCheck;
import dji.midware.data.model.P3.DataEyeGetPushFrontAvoidance;
import dji.midware.data.model.P3.DataEyeGetPushFunctionList;
import dji.midware.data.model.P3.DataEyeGetPushLog;
import dji.midware.data.model.P3.DataEyeGetPushMultiTrackingInformation;
import dji.midware.data.model.P3.DataEyeGetPushMultiTrackingState;
import dji.midware.data.model.P3.DataEyeGetPushOmniAvoidance;
import dji.midware.data.model.P3.DataEyeGetPushPOIExecutionParams;
import dji.midware.data.model.P3.DataEyeGetPushPOITargetInformation;
import dji.midware.data.model.P3.DataEyeGetPushPalmControlNotification;
import dji.midware.data.model.P3.DataEyeGetPushPalmControlState;
import dji.midware.data.model.P3.DataEyeGetPushPanoramaInformation;
import dji.midware.data.model.P3.DataEyeGetPushPerceptionInformation;
import dji.midware.data.model.P3.DataEyeGetPushPointAvoidance;
import dji.midware.data.model.P3.DataEyeGetPushPointLog;
import dji.midware.data.model.P3.DataEyeGetPushPointState;
import dji.midware.data.model.P3.DataEyeGetPushPreciseLandingEnergy;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraParams;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraShotState;
import dji.midware.data.model.P3.DataEyeGetPushSensorException;
import dji.midware.data.model.P3.DataEyeGetPushStabilizationState;
import dji.midware.data.model.P3.DataEyeGetPushTimeLapseKeyFrame;
import dji.midware.data.model.P3.DataEyeGetPushTimeLapseOverallData;
import dji.midware.data.model.P3.DataEyeGetPushTrackLog;
import dji.midware.data.model.P3.DataEyeGetPushTrackStatus;
import dji.midware.data.model.P3.DataEyeGetPushTrajectory;
import dji.midware.data.model.P3.DataEyeGetPushUAVState;
import dji.midware.data.model.P3.DataEyeObjectDetectionPushInfo;
import dji.midware.data.model.P3.DataEyePushVisionTip;
import dji.midware.data.model.P3.DataSingleGetPushPointPosValid;
import dji.midware.interfaces.CmdIdInterface;
import org.bouncycastle.crypto.tls.CipherSuite;

@EXClassNullAway
public class CmdIdEYE extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        GetEyePushLog(1, false, DataEyeGetPushLog.class),
        GetPushAvoidance(6, false, DataEyeGetPushAvoidanceParam.class),
        GetPushFrontAvoidance(7, false, DataEyeGetPushFrontAvoidance.class),
        GetPushOmniAvoidance(160, false, DataEyeGetPushOmniAvoidance.class),
        GetPushPalmControlNotification(162, false, DataEyeGetPushPalmControlNotification.class),
        GetSmartCaptureStatisticsData(163),
        GetPushPointAvoidance(8, false, DataEyeGetPushPointAvoidance.class),
        DebugCtrlParam(9),
        GetPushTrackLog(13, false, DataEyeGetPushTrackLog.class),
        GetPushPointLog(14, false, DataEyeGetPushPointLog.class),
        GetPushTrajectory(19, false, DataEyeGetPushTrajectory.class),
        GetPushUAVState(20, false, DataEyeGetPushUAVState.class),
        GetHandGestureEnabled(22),
        SetHandGestureEnabled(23),
        GetPushObjectDetect(24, false, DataEyeObjectDetectionPushInfo.class),
        GetPushFlatCheck(25, false, DataEyeGetPushFlatCheck.class),
        FixedWingCtrl(29),
        GetPushFixWingState(30, false, DataEyeGetPushFixedWingState.class),
        SetTrackSelect(32),
        CtrlTrackSelect(33),
        MoveTrackSelect(34),
        GetPushTrackStatus(35, false, DataEyeGetPushTrackStatus.class),
        SetPointPos(36),
        SetFlyYaw(37),
        GetPushPointState(38, false, DataEyeGetPushPointState.class),
        CommonCtrl(39),
        GetParams(40),
        SetParam(41),
        GetPushException(42, false, DataEyeGetPushException.class),
        GetPushFunctionList(46, false, DataEyeGetPushFunctionList.class),
        GetPushSensorException(47, false, DataEyeGetPushSensorException.class),
        EasySelfCal(48),
        SendTrackingUserLocation(49),
        GetPushEasySelfCal(50, false, DataEyeGetPushEasySelfCalibration.class),
        PushVisionTips(57, false, DataEyePushVisionTip.class),
        GetPushPreciseLandingEnergy(58, false, DataEyeGetPushPreciseLandingEnergy.class),
        Stabilization(96),
        GetPushStabilizationState(97, false, DataEyeGetPushStabilizationState.class),
        AppState(98),
        SendDrawTrajectory(59),
        DrawOperation(60),
        GetPushDrawState(61, false, DataEyeGetPushDrawState.class),
        SetPseudoCameraControlMode(62),
        SendGPSInfo(73),
        SetQuickMovieParams(74),
        GetQuickMovieParams(75),
        GetPushPseudoCameraShotState(77, false, DataEyeGetPushPseudoCameraShotState.class),
        GetPushAdvancedPilotAssistantSystemState(78, false, DataEyeGetPushAdvancedPilotAssistantSystemState.class),
        GetPushPseudoCameraParams(84, false, DataEyeGetPushPseudoCameraParams.class),
        SetPerceptionGesture(86),
        GetPerceptionGesture(88),
        GetPushPerceptionInformation(87, false, DataEyeGetPushPerceptionInformation.class),
        SetPanoramaEnabled(107),
        GetPushPanoramaInformation(108, false, DataEyeGetPushPanoramaInformation.class),
        GetPushFaceDetectionTakeOffState(112, false, DataEyeGetPushFaceDetectionTakeOffState.class),
        GetPushPalmControlState(113, false, DataEyeGetPushPalmControlState.class),
        GetPushTimeLapseOverallData(PanasonicMakernoteDirectory.TAG_INTELLIGENT_D_RANGE, false, DataEyeGetPushTimeLapseOverallData.class),
        SetTimeLapseSubMode(116),
        SetTimeLapseLoadTask(PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH),
        GetPushTimeLapseKeyFrame(117, false, DataEyeGetPushTimeLapseKeyFrame.class),
        SetTimeLapseKeyFrame(118),
        GetTimeLapseKeyFrameInfoByIndex(119),
        SetTimeLapseStart(120),
        SetTimeLapseAction(122),
        SetTimeLapseParams(123),
        UavStatePushSwitch(129),
        CheckPointPosValid(NikonType2MakernoteDirectory.TAG_ADAPTER),
        StartMultiTracking(148),
        GetPushMultiTrackingInformation(149, false, DataEyeGetPushMultiTrackingInformation.class),
        GetPushMultiTrackingState(150, false, DataEyeGetPushMultiTrackingState.class),
        StopMultiTracking(151),
        GetPushPointPosValid(131, false, DataSingleGetPushPointPosValid.class),
        SetPOIStartWithGPS(192),
        SetPOIInitialTarget(CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256),
        GetPushPOITargetInformation(CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256, false, DataEyeGetPushPOITargetInformation.class),
        SetPOIAction(CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256),
        SetPOIParams(CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256),
        GetPushPOIExecutionParams(CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256, false, DataEyeGetPushPOIExecutionParams.class),
        SetTrackingBox(231),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isNeedCcode = true;

        private CmdIdType(int _data) {
            this.data = _data;
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isNeedCcode2) {
            this.data = _data;
            this.isNeedCcode = isNeedCcode2;
        }

        public int value() {
            return this.data;
        }

        public boolean isBlock() {
            return this.isBlock;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
        }

        public boolean isNeedCcode() {
            return this.isNeedCcode;
        }

        public boolean isMix() {
            return false;
        }

        public DataBase getDataBase() {
            return this.dataBase;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdIdType find(int b) {
            CmdIdType result = Other;
            for (int i = 0; i < items.length; i++) {
                if (items[i]._equals(b)) {
                    return items[i];
                }
            }
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public CmdIdInterface[] getCurCmdIdValues() {
        return CmdIdType.values();
    }

    public String getDataModelName(int deviceType, int cmdId) {
        return DataUtil.getDataModelName(DeviceType.find(deviceType).toString(), CmdIdType.find(cmdId).toString());
    }

    public String getDataModelName(int deviceType, int deviceId, int cmdId) {
        return getDataModelName(deviceType, cmdId);
    }
}
