package dji.midware.data.config.P3;

import android.support.v4.app.FrameMetricsAggregator;
import com.adobe.xmp.XMPError;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetPushChartInfo;
import dji.midware.data.model.P3.DataCameraGetPushCurPanoFileName;
import dji.midware.data.model.P3.DataCameraGetPushDCFInfo;
import dji.midware.data.model.P3.DataCameraGetPushFovParam;
import dji.midware.data.model.P3.DataCameraGetPushPlayBackParams;
import dji.midware.data.model.P3.DataCameraGetPushPrepareOpenFan;
import dji.midware.data.model.P3.DataCameraGetPushRawNewParam;
import dji.midware.data.model.P3.DataCameraGetPushRawParams;
import dji.midware.data.model.P3.DataCameraGetPushRecordingName;
import dji.midware.data.model.P3.DataCameraGetPushShotInfo;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushShutterCmd;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetPushStorageInfo;
import dji.midware.data.model.P3.DataCameraGetPushTapZoomStateInfo;
import dji.midware.data.model.P3.DataCameraGetPushTauParam;
import dji.midware.data.model.P3.DataCameraGetPushTimelapseParms;
import dji.midware.data.model.P3.DataCameraGetPushTrackingStatus;
import dji.midware.data.model.P3.DataCameraSetOpticsZoomMode;
import dji.midware.interfaces.CmdIdInterface;
import it.sauronsoftware.ftp4j.FTPCodes;
import org.bouncycastle.crypto.tls.CipherSuite;

@EXClassNullAway
public class CmdIdCamera extends DJICmdSetBase {

    public enum CmdIdType implements CmdIdInterface {
        SetPhoto(1),
        SetRecord(2),
        SetHeart(3),
        VirtualKey(5),
        SwitchUSB(4),
        GetUSB(6),
        SetMode(16),
        GetMode(17),
        SetImageSize(18),
        GetImageSize(19),
        SetImageQuality(20),
        GetImageQuality(21),
        SetImageFormat(22),
        GetImageFormat(23),
        SetVideoFormat(24),
        GetVideoFormat(25),
        SetVideoQuality(26),
        GetVideoQuality(27),
        SetVideoStoreFormat(28),
        GetVideoStoreFormat(29),
        SetExposureMode(30),
        GetExposureMode(31),
        SetSceneMode(32),
        GetSceneMode(33),
        SetMetering(34),
        GetMetering(35),
        SetFocusMode(36),
        GetFocusMode(37),
        SetAperture(38),
        GetAperture(39),
        SetShutterSpeed(40),
        GetShutterSpeed(41),
        SetIso(42),
        GetIso(43),
        SetWhiteBalance(44),
        GetWhiteBalance(45),
        SetExposureCompensation(46),
        GetExposureCompensation(47),
        SetFocusArea(48),
        SetMeteringArea(50),
        GetMeteringArea(51),
        SetFocusParam(52),
        SetZoomParams(52),
        SetSharpe(56),
        GetSharpe(57),
        SetContrast(58),
        GetContrast(59),
        SetSaturation(60),
        GetSaturation(61),
        SetTonal(62),
        GetTonal(63),
        SetDigitalFilter(66),
        GetDigitalFilter(67),
        SetAntiFlicker(70),
        GetAntiFlicker(71),
        SetContinuous(72),
        GetContinuous(73),
        SetTimeParams(74),
        GetTimeParams(75),
        SetVOutParams(76),
        GetVOutParams(77),
        SetQuickPlayBack(78),
        GetQuickPlayBack(79),
        SetDate(84),
        SetAEBParms(94),
        GetAEBParams(95),
        SetFileIndexMode(92),
        GetFileIndexMode(93),
        SetPushChart(96),
        GetPushChart(97),
        SetVideoCaption(98),
        GetVideoCaption(99),
        SetStandard(102),
        GetStandard(103),
        SetVideoRecordMode(108),
        GetVideoRecordMode(109),
        SetPanoMode(110),
        SetAELock(104),
        GetAELock(105),
        SetPhotoMode(106),
        GetPhotoMode(107),
        GetStateInfo(112),
        GetSDCardParams(113),
        FormatSDCard(114),
        SaveParams(119),
        LoadParams(120),
        DeletePhoto(PanasonicMakernoteDirectory.TAG_INTELLIGENT_D_RANGE),
        VideoControl(122),
        SingleChoice(123),
        ResponseRc(PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH, false, DataCameraGetPushShutterCmd.class),
        ScaleGesture(FTPCodes.DATA_CONNECTION_ALREADY_OPEN),
        DragGesture(126),
        GetPushStateInfo(128, false, DataCameraGetPushStateInfo.class),
        GetPushShotParams(129, false, DataCameraGetPushShotParams.class),
        GetPushPlayBackParams(NikonType2MakernoteDirectory.TAG_ADAPTER, false, DataCameraGetPushPlayBackParams.class),
        GetPushChartParams(131, false, DataCameraGetPushChartInfo.class),
        GetPushRecordingName(132, false, DataCameraGetPushRecordingName.class),
        GetPushCurPanoFileName(134, false, DataCameraGetPushCurPanoFileName.class),
        GetPushTimelapseParms(136, false, DataCameraGetPushTimelapseParms.class),
        GetPushRawParams(133, false, DataCameraGetPushRawParams.class),
        GetPushShotInfo(135, false, DataCameraGetPushShotInfo.class),
        GetPushTrackingStatus(137, false, DataCameraGetPushTrackingStatus.class),
        GetPushFovParam(138, false, DataCameraGetPushFovParam.class),
        ParamsOption(142),
        GetVideoParams(146),
        ControlTransCode(147),
        SetFocusStroke(149),
        GetFileParams(152),
        SetVideoContrastEnhance(155),
        GetVideoContrastEnhance(156),
        SetAudioParma(159),
        GetAudioParam(160),
        FormatSSD(161),
        SetCalibrationControl(163),
        GetTrackingParms(165),
        SetTrackingParms(166),
        SetAEUnlockMode(168),
        GetAEUnlockMode(169),
        GetPanoFileParams(170),
        SetVideoEncode(171),
        GetVideoEncode(172),
        SetSSDVideoFormat(175),
        GetSSDVideoFormat(176),
        GetSSensorId(181),
        GetShotInfo(153),
        SetFocusAid(154),
        SetWhiteBalanceArea(157),
        GetWhiteBalanceArea(158),
        FormatRawSSD(161),
        SetFocusDistance(162),
        SetFocusWindow(164),
        SetIris(167),
        SetMCTF(173),
        GetMCTF(174),
        SetRecordFan(177),
        GetRecordFan(178),
        RequestIFrame(179),
        GetPushPrepareOpenFan(180, false, DataCameraGetPushPrepareOpenFan.class),
        SetForeArmLed(182),
        GetForeArmLed(183),
        SetOpticsZoom(184, false, true, DataCameraSetOpticsZoomMode.class),
        SetCameraRotationMode(185),
        GetCameraRotationMode(CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256),
        SetLockGimbalWhenShot(187),
        SetRawVideoFormat(189),
        GetRawVideoFormat(CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256),
        SetFileStar(CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256),
        MFDemarcate(192),
        SetLogMode(CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256),
        SetParamName(CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256),
        GetParamName(CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256),
        SetTapZoom(CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256),
        GetTapZoom(CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256),
        SetTapZoomTarget(198),
        GetPushTapZoom(199, false, DataCameraGetPushTapZoomStateInfo.class),
        SetDefogEnabled(200),
        GetDefogEnabled(XMPError.BADXML),
        SetRawEquipInfo(202),
        SetSSDRawVideoDigitalFilter(XMPError.BADSTREAM),
        GetCalibrationControl(206),
        SetMechanicalShutter(207),
        GetMechanicalShutter(CanonMakernoteDirectory.TAG_VRD_OFFSET),
        GetPushDFCInfo(209, false, DataCameraGetPushDCFInfo.class),
        SetDustReductionState(210),
        SetNDFilter(FTPCodes.SERVICE_CLOSING_CONTROL_CONNECTION),
        SetRawNewParam(222),
        GetPushRawNewParam(223, false, DataCameraGetPushRawNewParam.class),
        GetCapabilityRange(224),
        SetVideoSync(FTPCodes.DATA_CONNECTION_CLOSING),
        SetSaveOrigPhoto(231),
        GetSaveOrigPhoto(232),
        OptStorageCfg(218),
        GetPushStorageInfo(FTPCodes.SERVICE_READY_FOR_NEW_USER, false, DataCameraGetPushStorageInfo.class),
        TauParam(241),
        GetPushTauParam(242, false, DataCameraGetPushTauParam.class),
        GetFocusInfinite(249),
        SetFocusInfinite(250),
        SetUserCustomInfo(FTPCodes.NAME_SYSTEM_TIME),
        GetUserCustomInfo(216),
        IRCEnable(219),
        Watermark(229),
        ERROR(FrameMetricsAggregator.EVERY_DURATION);
        
        private static CmdIdType[] items = values();
        private Class<? extends DataBase> cls;
        private int data;
        private DataBase dataBase;
        private boolean isBlock;
        private boolean isMix;

        private CmdIdType(int _data) {
            this.isBlock = true;
            this.isMix = false;
            this.data = _data;
        }

        private CmdIdType(int _data, boolean isBlock2, Class<? extends DataBase> cls2) {
            this.isBlock = true;
            this.isMix = false;
            this.data = _data;
            this.isBlock = isBlock2;
            this.cls = cls2;
            this.dataBase = DataUtil.getDataBaseInstRefl(cls2);
        }

        private CmdIdType(int _data, boolean isBlock2, boolean isMix2, Class<? extends DataBase> cls2) {
            this(r7, r8, _data, isBlock2, cls2);
            this.isMix = isMix2;
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

        public boolean isMix() {
            return this.isMix;
        }

        public boolean isNeedCcode() {
            return true;
        }

        public DataBase getDataBase() {
            return this.dataBase;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdIdType find(int b) {
            CmdIdType result = ERROR;
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
