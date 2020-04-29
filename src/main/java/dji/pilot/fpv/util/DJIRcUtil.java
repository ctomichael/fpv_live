package dji.pilot.fpv.util;

import android.content.Context;
import dji.common.remotecontroller.CustomButtonTags;
import dji.common.remotecontroller.RCMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.narrowband.NarrowBandSlaveMode;
import dji.log.DJILogHelper;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.midware.data.model.P3.DataOsdGetPushCheckStatus;
import dji.midware.data.model.P3.DataRcGetCustomFuction;
import dji.midware.data.model.P3.DataRcGetPushCheckStatus;
import dji.midware.data.model.P3.DataRcSetCustomFuction;
import dji.midware.data.model.P3.DataWifiGetPushElecSignal;
import dji.pilot.fpv.define.IRcDefine;
import dji.pilot.publics.objects.DjiSharedPreferencesManager;
import dji.pilot.publics.util.DJICommonUtils;
import dji.pilot.publics.util.DJIPublicUtils;
import dji.pilot.publics.version.IVersionRc;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public class DJIRcUtil {
    private static DataRcSetCustomFuction.DJICustomType mCustomKey1SpValue = null;
    private static DataRcSetCustomFuction.DJICustomType mCustomKey2SpValue = null;
    private static final DataRcSetCustomFuction.DJICustomType[] mShieldFunctionInAssistant = {DataRcSetCustomFuction.DJICustomType.GimbalRecenter, DataRcSetCustomFuction.DJICustomType.QuickCircle, DataRcSetCustomFuction.DJICustomType.FlyExp, DataRcSetCustomFuction.DJICustomType.TorsGyro, DataRcSetCustomFuction.DJICustomType.VertVelocity, DataRcSetCustomFuction.DJICustomType.CloseDownVPS, DataRcSetCustomFuction.DJICustomType.CloseFrontAss, DataRcSetCustomFuction.DJICustomType.Vision1, DataRcSetCustomFuction.DJICustomType.CloseRadarMap, DataRcSetCustomFuction.DJICustomType.SwitchFrequence, DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.SmartGear, DataRcSetCustomFuction.DJICustomType.Transformation, DataRcSetCustomFuction.DJICustomType.CompositionMode, DataRcSetCustomFuction.DJICustomType.LiveViewExpand, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.GimbalPitch, DataRcSetCustomFuction.DJICustomType.GimbalYaw, DataRcSetCustomFuction.DJICustomType.GimbalRoll, DataRcSetCustomFuction.DJICustomType.GetGimbalControl, DataRcSetCustomFuction.DJICustomType.GimbalMode, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.GimbalPitchYawCenter};
    private static final DataRcSetCustomFuction.DJICustomType[] mShieldFunctionInGimbalController = {DataRcSetCustomFuction.DJICustomType.GimbalRecenter, DataRcSetCustomFuction.DJICustomType.QuickCircle, DataRcSetCustomFuction.DJICustomType.FlyExp, DataRcSetCustomFuction.DJICustomType.TorsGyro, DataRcSetCustomFuction.DJICustomType.VertVelocity, DataRcSetCustomFuction.DJICustomType.CloseDownVPS, DataRcSetCustomFuction.DJICustomType.CloseFrontAss, DataRcSetCustomFuction.DJICustomType.Vision1, DataRcSetCustomFuction.DJICustomType.CloseRadarMap, DataRcSetCustomFuction.DJICustomType.SwitchFrequence, DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.SmartGear, DataRcSetCustomFuction.DJICustomType.Transformation};

    public enum RcCalibrationMethod {
        Normal,
        OldHall,
        NewHall
    }

    public static int transformRadioSignal(int value) {
        if (value == 0) {
            return 0;
        }
        int percent = 101 - ((int) Math.sqrt(Math.pow(10.0d, (double) (((float) (Math.abs(value) - 53)) / 10.0f))));
        if (percent > 100) {
            return 100;
        }
        if (percent < 5) {
            return 5;
        }
        return percent;
    }

    public static boolean isChannelPoor(DataOsdGetPushChannalStatus.CHANNEL_STATUS status) {
        return (DataOsdGetPushChannalStatus.CHANNEL_STATUS.Excellent == status || DataOsdGetPushChannalStatus.CHANNEL_STATUS.Good == status || DataOsdGetPushChannalStatus.CHANNEL_STATUS.Medium == status) ? false : true;
    }

    public static boolean isWifiElecSignalPoor(DataWifiGetPushElecSignal.SIGNAL_STATUS status) {
        return (DataWifiGetPushElecSignal.SIGNAL_STATUS.Good == status || DataWifiGetPushElecSignal.SIGNAL_STATUS.Medium == status) ? false : true;
    }

    public static DataRcSetCustomFuction.DJICustomType[] getC1C2Setting(Boolean isMaster, Context ctx) {
        return DJICommonUtils.getC1C2Setting(isMaster, ctx);
    }

    public static RcCalibrationMethod getRcCalibrationMethod() {
        if (DJICommonUtils.isKumquatSeries(DJIProductManager.getInstance().getType()) || DJICommonUtils.isConnectedToMammothRC()) {
            return RcCalibrationMethod.OldHall;
        }
        if (DJIComponentManager.getInstance().isRcRM500()) {
            return RcCalibrationMethod.NewHall;
        }
        return RcCalibrationMethod.Normal;
    }

    public static boolean supportRCC1C2(ProductType type) {
        return DJICommonUtils.supportRCC1C2(type);
    }

    public static boolean supportRCC3() {
        return DJIComponentManager.getInstance().getRcComponentType() == DJIComponentManager.RcComponentType.RM500;
    }

    public static boolean rcHandleCustomKeySelf(ProductType rcType) {
        return DJICommonUtils.rcHandleCustomKeySelf(rcType);
    }

    public static boolean rcHandledCustomKey(DataRcSetCustomFuction.DJICustomType keyType) {
        return (DataRcSetCustomFuction.DJICustomType.CameraSetting == keyType || DataRcSetCustomFuction.DJICustomType.MapSwitch == keyType || DataRcSetCustomFuction.DJICustomType.ClearRoute == keyType || DataRcSetCustomFuction.DJICustomType.Battery == keyType || DataRcSetCustomFuction.DJICustomType.Navigation == keyType || DataRcSetCustomFuction.DJICustomType.LiveViewExpand == keyType || keyType == DataRcSetCustomFuction.DJICustomType.CenterFocus || keyType == DataRcSetCustomFuction.DJICustomType.ColorOscilloScope || DataRcSetCustomFuction.DJICustomType.MasterSlaveGroup == keyType) ? false : true;
    }

    public static boolean rcHasHomeKey(ProductType rcType) {
        return (ProductType.litchiC == rcType || ProductType.KumquatX == rcType || ProductType.KumquatS == rcType) ? false : true;
    }

    public static boolean supportShieldC3C4() {
        if (DJICommonUtils.isRcPro() && DJICommonUtils.formatVersion(new DataCommonGetVersion().setDeviceType(DeviceType.OSD).getFirmVer(".")) >= DJICommonUtils.formatVersion(IVersionRc.SUPPORT_SHIELD_C3C4_CENDENCE)) {
            return true;
        }
        return false;
    }

    public static boolean checkRcBatteryLow(int power) {
        DataOsdGetPushCheckStatus checkStatus = DataOsdGetPushCheckStatus.getInstance();
        if (checkStatus.isGetted()) {
            return checkStatus.getPowerStatus();
        }
        if (DJICommonUtils.isWM240() && DataRcGetPushCheckStatus.getInstance().isGetted()) {
            return DataRcGetPushCheckStatus.getInstance().getIsRcBatteryTooLow();
        }
        if (DJICommonUtils.isKumquatSeries(null) || DJICommonUtils.isWM240()) {
            if (power >= 15) {
                return false;
            }
            return true;
        } else if (power >= 30) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNeedSendRcCmd() {
        DJIComponentManager.RcComponentType rcComponentType = DJIComponentManager.getInstance().getRcComponentType();
        if (DJICommonUtils.isMammoth() || DJICommonUtils.isWM230() || DJICommonUtils.isWM240() || DJICommonUtils.isWM160() || rcComponentType == DJIComponentManager.RcComponentType.RM500) {
            return true;
        }
        return false;
    }

    public static DataRcSetCustomFuction.DJICustomType getCustomKey1SpValue(Context context) {
        int value;
        if (mCustomKey1SpValue == null && 32767 != (value = DjiSharedPreferencesManager.getInt(context, IRcDefine.RcSPKeys.KEY_RCSP_CUSTOM1_VALUE, 32767))) {
            mCustomKey1SpValue = DataRcSetCustomFuction.DJICustomType.find(value);
        }
        if (mCustomKey1SpValue != null) {
            return mCustomKey1SpValue;
        }
        return DataRcSetCustomFuction.DJICustomType.find(DJICommonUtils.getDefaultCustomTypes()[0]);
    }

    public static void saveCustomKey1SpValue(Context context) {
        int value = getCustomKey1Value();
        DataRcSetCustomFuction.DJICustomType type = DataRcSetCustomFuction.DJICustomType.find(value);
        if (type != mCustomKey1SpValue) {
            DjiSharedPreferencesManager.putInt(context, IRcDefine.RcSPKeys.KEY_RCSP_CUSTOM1_VALUE, value);
            mCustomKey1SpValue = type;
        }
    }

    public static DataRcSetCustomFuction.DJICustomType getCustomKey2SpValue(Context context) {
        int value;
        if (mCustomKey2SpValue == null && 32767 != (value = DjiSharedPreferencesManager.getInt(context, IRcDefine.RcSPKeys.KEY_RCSP_CUSTOM2_VALUE, 32767))) {
            mCustomKey2SpValue = DataRcSetCustomFuction.DJICustomType.find(value);
        }
        if (mCustomKey2SpValue != null) {
            return mCustomKey2SpValue;
        }
        return DataRcSetCustomFuction.DJICustomType.find(DJICommonUtils.getDefaultCustomTypes()[1]);
    }

    public static void saveCustomKey2SpValue(Context context) {
        int value = getCustomKey2Value();
        DataRcSetCustomFuction.DJICustomType type = DataRcSetCustomFuction.DJICustomType.find(value);
        if (type != mCustomKey2SpValue) {
            DjiSharedPreferencesManager.putInt(context, IRcDefine.RcSPKeys.KEY_RCSP_CUSTOM2_VALUE, value);
            mCustomKey2SpValue = type;
        }
    }

    public static int getCustomKey1Value() {
        CustomButtonTags keyParam = (CustomButtonTags) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.CUSTOM_BUTTON_TAGS), null);
        if (keyParam != null) {
            return keyParam.getC1ButtonTag();
        }
        return DataRcGetCustomFuction.getInstance().getC1();
    }

    public static int getCustomKey2Value() {
        CustomButtonTags keyParam = (CustomButtonTags) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.CUSTOM_BUTTON_TAGS), null);
        if (keyParam != null) {
            return keyParam.getC2ButtonTag();
        }
        return DataRcGetCustomFuction.getInstance().getC2();
    }

    public static int getCustomKey3Value() {
        CustomButtonTags keyParam = (CustomButtonTags) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.CUSTOM_BUTTON_TAGS), null);
        if (keyParam != null) {
            return keyParam.getC3ButtonTag();
        }
        return DataRcGetCustomFuction.getInstance().getC3();
    }

    public static boolean canSetLinkedAircraftType() {
        ProductType rcType = DJIProductManager.getInstance().getRcType();
        return ProductType.Pomato == rcType || ProductType.Potato == rcType;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeAircraftType(dji.midware.data.model.P3.DataRcSetAppSpecialControl.RcAircraftType r6) {
        /*
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.util.Date r4 = new java.util.Date
            r4.<init>()
            java.lang.String r5 = "yyyy-MM-dd HH:mm:ss"
            java.lang.String r4 = com.dji.frame.util.V_StringUtils.formatDate(r4, r5)
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "##"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r6.toString()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r2 = r3.toString()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()
            java.lang.String r4 = r4.getLogParentDir()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = java.io.File.separator
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "RcAircraftType"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = java.io.File.separator
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "rcaircrafttype.txt"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r1 = r3.toString()
            java.lang.String r3 = "UTF-8"
            byte[] r0 = r2.getBytes(r3)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
            r3 = 1
            dji.pilot.publics.util.DJIPublicUtils.writeSomeLog(r3, r0, r1)     // Catch:{ Exception -> 0x0065, all -> 0x0063 }
        L_0x0062:
            return
        L_0x0063:
            r3 = move-exception
            throw r3
        L_0x0065:
            r3 = move-exception
            goto L_0x0062
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.fpv.util.DJIRcUtil.writeAircraftType(dji.midware.data.model.P3.DataRcSetAppSpecialControl$RcAircraftType):void");
    }

    public static List<String> readAircraftType() {
        ArrayList<String> logs = new ArrayList<>();
        List<byte[]> logBytes = DJIPublicUtils.readSomeLog(1, DJILogHelper.getInstance().getLogParentDir() + File.separator + "RcAircraftType" + File.separator + IRcDefine.RcLogFilePath.FILE_RC_AIRCRAFT_TYPE);
        if (logBytes != null && !logBytes.isEmpty()) {
            for (byte[] bytes : logBytes) {
                logs.add(new String(bytes, Charset.forName("UTF-8")));
            }
        }
        return logs;
    }

    public static boolean needShowGimbalControlHint() {
        return !DJICommonUtils.isGettedGimbalControl() && !isNarrowBandWorkAsAssistant();
    }

    public static boolean isNarrowBandWorkAsAssistant() {
        boolean isNBConnected = CacheHelper.toBool(CacheHelper.getRemoteController(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED));
        NarrowBandSlaveMode slaveMode = (NarrowBandSlaveMode) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.NARROW_BAND_SLAVE_MODE));
        return isNBConnected && CacheHelper.toBool(CacheHelper.getRemoteController(RemoteControllerKeys.NARROW_BAND_CONNECTION_STATE)) && (slaveMode == NarrowBandSlaveMode.ASSISTANT_1 || slaveMode == NarrowBandSlaveMode.ASSISTANT_2);
    }

    public static boolean isNarrowBandWorkAsMaster() {
        return CacheHelper.toBool(CacheHelper.getRemoteController(RemoteControllerKeys.NARROW_BAND_CONNECTION_STATE)) && CacheHelper.toBool(CacheHelper.getRemoteController(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED)) && ((NarrowBandSlaveMode) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.NARROW_BAND_SLAVE_MODE))) == NarrowBandSlaveMode.MASTER;
    }

    public static boolean isNarrowBandMonitorMode() {
        boolean shield = CacheHelper.toBool(CacheHelper.getRemoteController(RemoteControllerKeys.NARROW_BAND_SHIELD_UP_CMD), false);
        if (!isNarrowBandWorkAsAssistant() || !shield) {
            return false;
        }
        return true;
    }

    public static boolean supportMonitorMode() {
        return ProductType.Orange2 == DJIProductManager.getInstance().getRcType();
    }

    public static boolean isShieldFunctionInRCMode(String functionName, RCMode mode, NarrowBandSlaveMode narrowBandSlaveMode) {
        DataRcSetCustomFuction.DJICustomType function = DataRcSetCustomFuction.DJICustomType.find(functionName);
        if (mode != RCMode.SLAVE) {
            return false;
        }
        if (narrowBandSlaveMode == NarrowBandSlaveMode.ASSISTANT_1 || narrowBandSlaveMode == NarrowBandSlaveMode.ASSISTANT_2) {
            for (DataRcSetCustomFuction.DJICustomType tmp : mShieldFunctionInAssistant) {
                if (tmp == function) {
                    return true;
                }
            }
            return false;
        }
        for (DataRcSetCustomFuction.DJICustomType tmp2 : mShieldFunctionInGimbalController) {
            if (tmp2 == function) {
                return true;
            }
        }
        return false;
    }

    private DJIRcUtil() {
    }
}
