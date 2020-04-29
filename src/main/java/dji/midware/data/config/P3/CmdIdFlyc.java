package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import com.adobe.xmp.XMPError;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataA2PushCommom;
import dji.midware.data.model.P3.DataFlycDetection;
import dji.midware.data.model.P3.DataFlycGetPushActiveRequest;
import dji.midware.data.model.P3.DataFlycGetPushAgpsStatus;
import dji.midware.data.model.P3.DataFlycGetPushAvoid;
import dji.midware.data.model.P3.DataFlycGetPushAvoidParam;
import dji.midware.data.model.P3.DataFlycGetPushBoardRecv;
import dji.midware.data.model.P3.DataFlycGetPushDeformStatus;
import dji.midware.data.model.P3.DataFlycGetPushFaultInject;
import dji.midware.data.model.P3.DataFlycGetPushFlightRecord;
import dji.midware.data.model.P3.DataFlycGetPushFlycInstallError;
import dji.midware.data.model.P3.DataFlycGetPushForbidStatus;
import dji.midware.data.model.P3.DataFlycGetPushGoHomeCountDown;
import dji.midware.data.model.P3.DataFlycGetPushGpsSnr;
import dji.midware.data.model.P3.DataFlycGetPushLedStatus;
import dji.midware.data.model.P3.DataFlycGetPushLimitState;
import dji.midware.data.model.P3.DataFlycGetPushParamsByHash;
import dji.midware.data.model.P3.DataFlycGetPushPowerParam;
import dji.midware.data.model.P3.DataFlycGetPushRTKLocationData;
import dji.midware.data.model.P3.DataFlycGetPushRequestLimitUpdate;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.data.model.P3.DataFlycGetPushUnlimitState;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionCurrentEvent;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;
import dji.midware.data.model.P3.DataFlycPushForbidDataInfos;
import dji.midware.data.model.P3.DataFlycPushRedundancyStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.interfaces.CmdIdInterface;
import it.sauronsoftware.ftp4j.FTPCodes;

@EXClassNullAway
public class CmdIdFlyc extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        SimScan(0),
        SimGetParams(1),
        SimParams(2),
        SimCommand(5),
        SetFlyForbidArea(8),
        GetPushFlyForbidStatus(9, false, DataFlycGetPushForbidStatus.class),
        SetHaveCheckedStruct(13),
        A2PushCommom(16, false, DataA2PushCommom.class),
        SimRc(17),
        SetDate(28),
        InitializeOnboardFChannel(29),
        GetOnboardFChannelOutputValue(30),
        SetOnboardFChannelOutputValue(31),
        SimStatus(22),
        SendGpsToFlyc(32),
        ExecFly(39),
        FunctionControl(42, true),
        SetIoc(43),
        GetIoc(44),
        SetLimits(45),
        GetLimits(46),
        SetVoltageWarnning(47),
        GetVoltageWarnning(48),
        SetHomePoint(49),
        GetPushDeformStatus(50, false, DataFlycGetPushDeformStatus.class),
        GetPlaneName(52, true),
        SetPlaneName(51, true),
        SetReadFlyDataMode(57, true),
        FormatDataRecorder(58, true),
        SetFsAction(59),
        GetFsAction(60),
        SetTimeZone(61),
        GetPushRequestLimitUpdate(62, false, DataFlycGetPushRequestLimitUpdate.class),
        SetFlyForbidAreaData(63),
        UploadUnlimitAreas(65),
        EnableUnlimitAreas(71),
        GetPushUnlimitAreas(66, false, DataFlycGetPushUnlimitState.class),
        GetPushCommon(67, false, DataOsdGetPushCommon.class),
        GetPushHome(68, false, DataOsdGetPushHome.class),
        GetPushGpsSnr(69, false, DataFlycGetPushGpsSnr.class),
        SetPushGpsSnr(70),
        GetPushSmartBattery(81, false, DataFlycGetPushSmartBattery.class),
        SmartLowBatteryAck(82),
        GetPushAvoidParam(83, false, DataFlycGetPushAvoidParam.class),
        GetPushLimitState(85, false, DataFlycGetPushLimitState.class),
        GetPushLedStatus(86, false, DataFlycGetPushLedStatus.class),
        GetPushActiveRequest(97, false, DataFlycGetPushActiveRequest.class),
        SetActiveResult(98),
        GetPushOnBoardRecv(99, false, DataFlycGetPushBoardRecv.class),
        SetSendOnBoard(100),
        RTKSwitch(105),
        GetPowerParam(103, false, DataFlycGetPushPowerParam.class),
        SetRTKState(105),
        GetPushAvoid(106, false, DataFlycGetPushAvoid.class),
        GetPushRTKLocationData(108, false, DataFlycGetPushRTKLocationData.class),
        SetBatteryValidStste(106),
        NavigationSwitch(128),
        UploadWayPointMissionMsg(NikonType2MakernoteDirectory.TAG_ADAPTER),
        DownloadWayPointMissionMsg(131),
        UploadWayPointMsgByIndex(132),
        DownloadWayPointMsgByIndex(133),
        WayPointMissionSwitch(134),
        NoeMissionPauseOrResume(143),
        WayPointMissionPauseOrResume(135),
        StartNoeMission(148),
        StopNoeMission(149),
        WayPointMissionSetIdleSpeed(156),
        GetPushWayPointMissionInfo(136, false, DataFlycGetPushWayPointMissionInfo.class),
        GetPushWayPointMissionCurrentEvent(137, false, DataFlycGetPushWayPointMissionCurrentEvent.class),
        StartHotPointMissionWithInfo(138),
        CancelHotPointMission(139),
        HotPointMissionSwitch(140),
        HotPointMissionDownload(150),
        HotPointResetParams(153),
        HotPointResetRadius(154),
        HotPointResetCamera(155),
        GetHPMaxAngularVelocity(33),
        JoyStick(142),
        StartFollowMeWithInfo(144),
        CancelFollowMeMission(145),
        FollowMeMissionSwitch(146),
        SendGpsInfo(147),
        StartIoc(151),
        StopIoc(152),
        SendAgpsData(160, true),
        GetPushAgpsState(161, false, DataFlycGetPushAgpsStatus.class),
        GetWaypointInterruption(163),
        GetPushFlycInstallError(173, false, DataFlycGetPushFlycInstallError.class),
        SetFlightIdleSpeed(156),
        GetAreaCode(175),
        GetBatteryGroupsSingleInfo(176),
        FaultInject(181),
        GetPushFaultInject(182, false, DataFlycGetPushFaultInject.class),
        SetAndGetRedundancyIMUIndex(183),
        RedundancyStatus(184),
        PushRedundancyStatus(185, false, DataFlycPushRedundancyStatus.class),
        GetSetWarningAreaEnable(XMPError.BADSTREAM),
        UpdateFlyforbidArea(205),
        PushForbidDataInfos(206, false, DataFlycPushForbidDataInfos.class),
        GetNewFlyforbidArea(207),
        GetPushFlightRecord(FTPCodes.NAME_SYSTEM_TIME, false, true, false, DataFlycGetPushFlightRecord.class),
        Detection(218, false, true, false, DataFlycDetection.class),
        SetFlyforbidData(233),
        SetEscEcho(237),
        GetPushGoHomeCountDown(238, false, true, DataFlycGetPushGoHomeCountDown.class),
        GetParamInfoByIndex(240),
        GetParamsByIndex(241),
        SetParamsByIndex(242),
        ResetParamsByIndex(243),
        GetPushParamsByIndex(244, false, null),
        GetSetVerPhone(245),
        GetParamInfoByHash(247),
        GetParamsByHash(248),
        SetParamsByHash(249),
        ResetParamsByHash(250),
        GetPushParamsByHash(251, false, DataFlycGetPushParamsByHash.class),
        SetPushParams(252),
        SetMotorForceDisable(254),
        Other(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock = true;
        private boolean isMix = false;
        private boolean isNeedCcode = false;

        private CmdIdType(int _data) {
            this.data = _data;
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isBlock2, boolean isMix2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isMix = isMix2;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isBlock2, boolean isMix2, boolean isNeedCcode2, Class<? extends DataBase> cls2) {
            this.data = _data;
            this.isMix = isMix2;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.isNeedCcode = isNeedCcode2;
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

        public boolean isMix() {
            return this.isMix;
        }

        public Class<? extends DataBase> getDataModel() {
            return this.cls;
        }

        public boolean isNeedCcode() {
            return this.isNeedCcode;
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
