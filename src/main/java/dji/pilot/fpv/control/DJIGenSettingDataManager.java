package dji.pilot.fpv.control;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Keep;
import android.support.v4.provider.DocumentFile;
import com.dji.video.framing.internal.opengl.renderer.OverExposureWarner;
import dji.apppublic.reflect.AppPubInjectManager;
import dji.apppublic.reflect.AppPubToP3Injectable;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraFormatSDCard;
import dji.midware.data.model.P3.DataCameraFormatSSD;
import dji.midware.data.model.P3.DataCameraLoadParams;
import dji.midware.data.model.P3.DataCameraSaveParams;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.model.P3.DataDm368SetGParams;
import dji.midware.data.model.P3.DataGimbalAutoCalibration;
import dji.midware.data.model.P3.DataGimbalRollFinetune;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.record.ExternalSdRecordingHelper;
import dji.midware.media.record.RecorderManager;
import dji.pilot.fpv.util.DJIFlurryUtil;
import dji.pilot.publics.R;
import dji.pilot.publics.objects.DjiSharedPreferencesManager;
import dji.pilot.publics.util.DJIUnitUtil;
import dji.publics.utils.LanguageUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import kotlin.jvm.internal.LongCompanionObject;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class DJIGenSettingDataManager {
    public static final int FAIL_DATA = 1;
    public static final int GRID_CENTER = 3;
    public static final int GRID_DIAGONAL = 2;
    public static final int GRID_NONE = 0;
    public static final int GRID_SUDOKU = 1;
    public static final float[] INTERVAL_LIMIT_LOCK = {0.2f, 3.0f};
    public static final String KEY_AMAP_AVAILABILITY = "key_amap_availability";
    private static final String KEY_CAMERA_PANO_SAVE_PRG = "key_camera_save_pano";
    private static final String KEY_CAMERA_SHOW_OSD = "key_camera_show_osd";
    private static final String KEY_FIXWING_SWITCH = "key_fixwing_switch";
    private static final String KEY_FPV_GIMBAL_FINETUNE = "key_fpv_gimbal_finetune";
    private static final String KEY_GIMBAL_FINETUNE = "key_gimbal_finetune";
    public static final String KEY_HERE_AVAILABILITY = "key_here_availability";
    public static final String KEY_MAPBOX_AVAILABILITY = "key_maobox_availability";
    private static final String KEY_NINE_GRID = "key_new_grid";
    private static final String KEY_NINE_GRID_LAST = "key_new_grid_last";
    private static final String KEY_OPEN_BG_DOWNLOAD = "key_open_bg_download";
    public static final String KEY_OPEN_HYPER_LAPSE_FRAME_VIEW = "key_open_hyper_lapse_view";
    private static final String KEY_OVER_EXPOSURE_WARNER = "key_over_exposure_warner";
    private static final String KEY_PITCH_FINETUNE = "key_pitch_finetune";
    private static final String KEY_ROLL_FINETUNE = "key_roll_finetune";
    private static final String KEY_SHOW_ARPATH = "key_show_arpath";
    private static final String KEY_SHOW_ROUTE = "key_show_route";
    private static final String KEY_TEMPERATURE = "key_temperature_unit";
    public static final String KEY_USE_AMAP = "key_use_amap";
    public static final String KEY_USE_HERE = "key_use_here";
    private static final String KEY_VIDEO_CACHE_AUTO_CLEAN_SWITCH = "key_limit_video_buffer_space";
    private static final String KEY_VIDEO_CACHE_OPEN_SWITCH = "key_open_video_buffer";
    private static final String KEY_VIDEO_CACHE_SIZE_INDEX = "key_video_cache_size_index";
    private static final String KEY_VISION_ADVANCED_PILOT_ASSISTANT_SYSTEM = "key_vision_advanced_pilot_assistant_system";
    private static final String KEY_VISION_RADAR = "key_vision_radar";
    private static final String KEY_WIFI_SETTING_TYPE = "key_wifi_setting_type";
    private static final int MSG_ID_GETDATA = 4096;
    private static final int MSG_ID_SETDATA = 4097;
    public static final int SUCCESS_DATA = 0;
    public static final int TEMPERATURE_CELSIUS = 1;
    public static final int TEMPERATURE_FAHRENHEIT = 0;
    public static final int TEMPERATURE_KELVIN = 2;
    public static final int TYPE_CLEAR_ROUTE = 15;
    public static final int TYPE_COORDINATE_CALIBRATION = 16;
    public static final int TYPE_FORMAT_ROM = 20;
    public static final int TYPE_FORMAT_SDCARD = 2;
    public static final int TYPE_FORMAT_SSD = 3;
    public static final int TYPE_FPV_GIMBAL_FINETUNE_VALUE = 12;
    public static final int TYPE_GIMBAL_AUTO_CALIBRATION = 13;
    public static final int TYPE_GIMBAL_FINETUNE = 6;
    public static final int TYPE_GIMBAL_FINETUNE_VALUE = 5;
    public static final int TYPE_GIMBAL_PITCH_FINETUNE = 10;
    public static final int TYPE_GIMBAL_PITCH_FINETUNE_VALUE = 9;
    public static final int TYPE_GIMBAL_ROLL_FINETUNE = 8;
    public static final int TYPE_GIMBAL_ROLL_FINETUNE_VALUE = 7;
    public static final int TYPE_GIMBAL_YAW_FINETUNE = 11;
    public static final int TYPE_GRID_LINES = 4;
    public static final int TYPE_OPEN_BG_DOWNLOAD = 19;
    public static final int TYPE_OVER_EXPOSURE_WARNER = 18;
    public static final int TYPE_PARAMETER_UNIT = 0;
    public static final int TYPE_RESET_CAMERA_SETTING = 1;
    public static final int TYPE_SHOW_ROUTE = 14;
    public static final int TYPE_USE_AMAP = 17;
    public static final int UNIT_ALL = 3;
    public static final int UNIT_IMPERIAL = 0;
    public static final int UNIT_METRIC = 1;
    public static final int UNIT_METRIC_KM = 2;
    private boolean amapAvailability;
    private boolean hereAvailability;
    private DJIDataCallBack mAutoCalibrationDataCB;
    private final DataGimbalAutoCalibration mAutoCalibrationSetter;
    private final ArrayList<OnSettingChangeListener> mChangeListeners;
    private Context mContext;
    private DJIDataCallBack mFormatRomDataCB;
    private DJIDataCallBack mFormatSDCardDataCB;
    private final DataCameraFormatSDCard mFormatSDCardSetter;
    private DJIDataCallBack mFormatSSDDataCB;
    private final DataCameraFormatSSD mFormatSSDSetter;
    /* access modifiers changed from: private */
    public final GenHandler mGenHandler;
    private int mGridLineMode;
    private boolean mInit;
    private int mParameterUnit;
    private DJIDataCallBack mPitchFinetuneDataCB;
    private DJIDataCallBack mResetCameraSettingDataCB;
    private final DataCameraLoadParams mResetCameraSettingSetter;
    private DJIDataCallBack mRollFinetuneDataCB;
    private final DataGimbalRollFinetune mRollFinetuneSetter;
    private boolean mShowVerticalMode;
    private int mTemperatureUnit;
    private final float[] mTmpLimitLockInterval;
    private String mUnitDistStr;
    private String mUnitSpeedStr;
    private String mUnitSpeedStrIgnoreKm;
    private boolean mVideoCacheAutoCleanSwitch;
    private boolean mVideoCacheOpenSwitch;
    private int mVideoCacheSizeIndex;
    private boolean mVisionAdvancedPilotAssistantSystem;
    private boolean mVisionRadar;
    private DJIDataCallBack mYawFinetuneDataCB;
    private boolean mapboxAvailability;
    private boolean mbFPVGimbalFinetune;
    private boolean mbFixWingOpen;
    private boolean mbGimbalFinetune;
    private boolean mbGimbalPitchFinetune;
    private boolean mbGimbalRollFinetune;
    private boolean mbOpenBGDownload;
    private boolean mbOpenHyperLapseFrameView;
    private boolean mbOverExposureWarner;
    private boolean mbShowArPath;
    private boolean mbShowRoute;
    private boolean mbUseAmap;

    public interface OnSettingChangeListener {
        void onChanged(int i);

        void onClear(int i);

        void onDataOperateEnd(int i, boolean z, int i2, Ccode ccode);

        void onDataOperateStart(int i, boolean z);
    }

    @Keep
    public enum GenSettingEvent {
        PARAMETER_UNIT_CHANGED,
        TEMPERATURE_UNIT_CHANGED,
        VISION_RADAR_CHANGED,
        ARPATH_CHANGED,
        FIXWING_CHANGED,
        VISION_ADVANCED_PILOT_ASSISTANT_SYSTEM,
        HYPER_LAPSE_FRAME_VIEW_CHANGE
    }

    public String getUnitSpeedStr() {
        return this.mUnitSpeedStr;
    }

    public String getUnitSpeedStrIgnoreKm() {
        return this.mUnitSpeedStrIgnoreKm;
    }

    public String getUnitDistStr() {
        return this.mUnitDistStr;
    }

    private void updateUnitStrs() {
        if (this.mParameterUnit == 1) {
            this.mUnitSpeedStr = "m/s";
            this.mUnitSpeedStrIgnoreKm = "m/s";
            this.mUnitDistStr = "m";
        } else if (this.mParameterUnit == 2) {
            this.mUnitSpeedStr = "km/h";
            this.mUnitSpeedStrIgnoreKm = "m/s";
            this.mUnitDistStr = "m";
        } else {
            this.mUnitSpeedStr = "mph";
            this.mUnitSpeedStrIgnoreKm = "mph";
            this.mUnitDistStr = "ft";
        }
    }

    public static DJIGenSettingDataManager getInstance() {
        return SingletonHolder.mInstance;
    }

    public void initializeManager(Context context) {
        boolean z;
        this.mContext = context;
        if (!this.mInit) {
            this.mGridLineMode = DjiSharedPreferencesManager.getInt(context, KEY_NINE_GRID, 0);
            int unit = DjiSharedPreferencesManager.getInt(context, "DjiFormat", 3);
            if (!(unit == 0 || unit == 1)) {
                unit = unit == 2 ? 2 : Locale.US.getCountry().equals(context.getResources().getConfiguration().locale.getCountry()) ? 0 : 1;
            }
            this.mParameterUnit = unit;
            updateUnitStrs();
            AppPubToP3Injectable appPubToP3Injectable = AppPubInjectManager.getAppPubToP3Injectable();
            if (this.mParameterUnit == 0) {
                z = true;
            } else {
                z = false;
            }
            appPubToP3Injectable.dji_gs_Config_setUnitFT(z);
            if (this.mParameterUnit == 1 || this.mParameterUnit == 2) {
                this.mTmpLimitLockInterval[0] = INTERVAL_LIMIT_LOCK[0];
                this.mTmpLimitLockInterval[1] = INTERVAL_LIMIT_LOCK[1];
            } else {
                this.mTmpLimitLockInterval[0] = transformLength(INTERVAL_LIMIT_LOCK[0]);
                this.mTmpLimitLockInterval[1] = transformLength(INTERVAL_LIMIT_LOCK[1]);
            }
            this.mbGimbalFinetune = DjiSharedPreferencesManager.getBoolean(context, KEY_GIMBAL_FINETUNE, this.mbGimbalFinetune);
            this.mbGimbalRollFinetune = DjiSharedPreferencesManager.getBoolean(context, KEY_ROLL_FINETUNE, this.mbGimbalRollFinetune);
            this.mbGimbalPitchFinetune = DjiSharedPreferencesManager.getBoolean(context, KEY_PITCH_FINETUNE, this.mbGimbalPitchFinetune);
            this.mbFPVGimbalFinetune = DjiSharedPreferencesManager.getBoolean(context, KEY_FPV_GIMBAL_FINETUNE, this.mbFPVGimbalFinetune);
            this.mbShowRoute = DjiSharedPreferencesManager.getBoolean(context, KEY_SHOW_ROUTE, this.mbShowRoute);
            this.mbUseAmap = DjiSharedPreferencesManager.getBoolean(context, KEY_USE_AMAP, getDefaultAmapValue());
            this.hereAvailability = DjiSharedPreferencesManager.getBoolean(context, KEY_HERE_AVAILABILITY, false);
            setOpenBGDownload(DjiSharedPreferencesManager.getBoolean(context, KEY_OPEN_BG_DOWNLOAD, true));
            this.mbOpenHyperLapseFrameView = DjiSharedPreferencesManager.getBoolean(context, KEY_OPEN_HYPER_LAPSE_FRAME_VIEW, false);
            this.mVideoCacheOpenSwitch = DjiSharedPreferencesManager.getBoolean(context, KEY_VIDEO_CACHE_OPEN_SWITCH, true);
            this.mVideoCacheSizeIndex = DjiSharedPreferencesManager.getInt(context, KEY_VIDEO_CACHE_SIZE_INDEX, 1);
            RecorderManager.setMaxBufferSpace(((long) (this.mVideoCacheSizeIndex + 1)) * RecorderManager.GB);
            this.mVideoCacheAutoCleanSwitch = DjiSharedPreferencesManager.getBoolean(context, KEY_VIDEO_CACHE_AUTO_CLEAN_SWITCH, false);
            setOverExposureWarner(DjiSharedPreferencesManager.getBoolean(context, KEY_OVER_EXPOSURE_WARNER, false));
            this.mTemperatureUnit = DjiSharedPreferencesManager.getInt(context, KEY_TEMPERATURE, 1);
            this.mbFixWingOpen = DjiSharedPreferencesManager.getBoolean(context, KEY_FIXWING_SWITCH, this.mbFixWingOpen);
            this.mVisionRadar = DjiSharedPreferencesManager.getBoolean(context, KEY_VISION_RADAR, this.mVisionRadar);
            this.mbShowArPath = DjiSharedPreferencesManager.getBoolean(context, KEY_SHOW_ARPATH, this.mbShowArPath);
            this.mVisionAdvancedPilotAssistantSystem = DjiSharedPreferencesManager.getBoolean(context, KEY_VISION_ADVANCED_PILOT_ASSISTANT_SYSTEM, this.mVisionAdvancedPilotAssistantSystem);
            this.mInit = true;
        }
    }

    private boolean getDefaultAmapValue() {
        String lan = Locale.getDefault().getLanguage();
        return lan.contains(LanguageUtils.ZH_DJI_LANG_CODE) || lan.contains(LanguageUtils.CN_DJI_LANG_CODE) || this.mbUseAmap;
    }

    private boolean getDefaultAMapAvailability() {
        return true;
    }

    public void finalizeManager() {
    }

    public boolean registerListener(OnSettingChangeListener listener) {
        if (listener == null || this.mChangeListeners.contains(listener)) {
            return false;
        }
        this.mChangeListeners.add(listener);
        return true;
    }

    public boolean unregisterListener(OnSettingChangeListener listener) {
        if (listener != null) {
            return this.mChangeListeners.remove(listener);
        }
        return false;
    }

    public void clearData() {
        this.mGenHandler.removeMessages(4096);
        this.mGenHandler.removeMessages(4097);
        notifySettingClear(0);
    }

    public void resetCameraSetting() {
        resetLocalData();
        notifyDataStart(1, false);
        this.mResetCameraSettingSetter.setMode(DataCameraSaveParams.USER.DEFAULT);
        this.mResetCameraSettingSetter.start(this.mResetCameraSettingDataCB);
    }

    public void formatSDCard() {
        notifyDataStart(2, false);
        this.mFormatSDCardSetter.setStorageLocation(DataCameraSetStorageInfo.Storage.SDCARD);
        this.mFormatSDCardSetter.start(this.mFormatSDCardDataCB);
    }

    public void formatSSD() {
        notifyDataStart(3, false);
        this.mFormatSSDSetter.start(this.mFormatSSDDataCB);
    }

    public void formatRom() {
        notifyDataStart(20, false);
        this.mFormatSDCardSetter.setStorageLocation(DataCameraSetStorageInfo.Storage.INNER_STORAGE);
        this.mFormatSDCardSetter.start(this.mFormatRomDataCB);
    }

    public boolean getPanoramaSaveOrg() {
        return DjiSharedPreferencesManager.getBoolean(this.mContext, KEY_CAMERA_PANO_SAVE_PRG, false);
    }

    public void updatePanoramaSaveOrg(boolean enable) {
        DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_CAMERA_PANO_SAVE_PRG, enable);
    }

    public boolean getOsdVisibility() {
        return DjiSharedPreferencesManager.getBoolean(this.mContext, KEY_CAMERA_SHOW_OSD, false);
    }

    public void updateOsdVisibility(boolean visible) {
        DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_CAMERA_SHOW_OSD, visible);
    }

    public int getGridMode() {
        if (3 == this.mGridLineMode) {
            return 0;
        }
        return this.mGridLineMode;
    }

    public void switchGridLine() {
        updateShowGrid(this.mGridLineMode == 2 ? 0 : this.mGridLineMode + 1);
    }

    public void updateShowGrid(int mode) {
        if (mode != this.mGridLineMode) {
            DjiSharedPreferencesManager.putInt(this.mContext, KEY_NINE_GRID_LAST, this.mGridLineMode);
            this.mGridLineMode = mode;
            switch (mode) {
                case 0:
                    DJIFlurryUtil.logEvent("FPV_GeneralSettings_Camera_PullDownView_ShowGrid_OFF");
                    break;
                case 1:
                    DJIFlurryUtil.logEvent("FPV_GeneralSettings_Camera_PullDownView_ShowGrid_GridLines");
                    break;
                case 2:
                    DJIFlurryUtil.logEvent("FPV_GeneralSettings_Camera_PullDownView_ShowGrid_Grid+Diagnoals");
                    break;
            }
            DjiSharedPreferencesManager.putInt(this.mContext, KEY_NINE_GRID, mode);
            notifySettingChanged(4);
        }
    }

    public void setShowVerticalMode(boolean show) {
        this.mShowVerticalMode = show;
    }

    public boolean isShowVerticalMode() {
        return this.mShowVerticalMode;
    }

    public boolean getGimbalFinetune() {
        return this.mbGimbalFinetune;
    }

    public void updateGimbalFinetune(boolean value) {
        if (this.mbGimbalFinetune != value) {
            this.mbGimbalFinetune = value;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_GIMBAL_FINETUNE, value);
            notifySettingChanged(5);
        }
    }

    public boolean getFPVGimbalFinetune() {
        return this.mbFPVGimbalFinetune;
    }

    public void updateFPVGimbalFinetune(boolean value) {
        if (this.mbFPVGimbalFinetune != value) {
            this.mbFPVGimbalFinetune = value;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_FPV_GIMBAL_FINETUNE, value);
            notifySettingChanged(12);
        }
    }

    public boolean getGimbalRollFinetune() {
        return this.mbGimbalRollFinetune;
    }

    public void updateGimbalRollFinetune(boolean value) {
        if (this.mbGimbalRollFinetune != value) {
            this.mbGimbalRollFinetune = value;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_ROLL_FINETUNE, value);
            notifySettingChanged(7);
        }
    }

    public boolean getGimbalPitchFinetune() {
        return this.mbGimbalPitchFinetune;
    }

    public void updateGimbalPitchFinetune(boolean value) {
        if (this.mbGimbalPitchFinetune != value) {
            this.mbGimbalPitchFinetune = value;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_PITCH_FINETUNE, value);
            notifySettingChanged(9);
        }
    }

    public void enableGimbalAutoCalibration() {
        notifyDataStart(13, false);
        this.mAutoCalibrationSetter.start(this.mAutoCalibrationDataCB);
    }

    public void finetuneGimbalRoll(byte value, boolean isFpVCamera) {
        updateCameraGimablType(isFpVCamera);
        notifyDataStart(8, false);
        this.mRollFinetuneSetter.setFineTuneAxis(DataGimbalRollFinetune.FineTuneAxis.ROLL).setFineTuneValue(value).start(this.mRollFinetuneDataCB);
    }

    public void finetuneGimbalPitch(byte value, boolean isFpVCamera) {
        updateCameraGimablType(isFpVCamera);
        notifyDataStart(10, false);
        this.mRollFinetuneSetter.setFineTuneAxis(DataGimbalRollFinetune.FineTuneAxis.PITCH).setFineTuneValue(value).start(this.mPitchFinetuneDataCB);
    }

    public void finetuneGimbalYaw(byte value, boolean isFpVCamera) {
        updateCameraGimablType(isFpVCamera);
        notifyDataStart(11, false);
        this.mRollFinetuneSetter.setFineTuneAxis(DataGimbalRollFinetune.FineTuneAxis.YAW).setFineTuneValue(value).start(this.mYawFinetuneDataCB);
    }

    private void updateCameraGimablType(boolean isFpVCamera) {
        if (isFpVCamera) {
            this.mRollFinetuneSetter.setCameraGimbalType(1);
        } else {
            this.mRollFinetuneSetter.setCameraGimbalType(0);
        }
    }

    public boolean canShowArPath() {
        return this.mbShowArPath;
    }

    public void updateShowArPath(boolean show) {
        if (this.mbShowArPath != show) {
            this.mbShowArPath = show;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_SHOW_ARPATH, show);
            EventBus.getDefault().post(GenSettingEvent.ARPATH_CHANGED);
        }
    }

    public boolean isVisionRadarShow() {
        return this.mVisionRadar;
    }

    public void updateVisionRadar(boolean show) {
        if (this.mVisionRadar != show) {
            this.mVisionRadar = show;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_VISION_RADAR, show);
            EventBus.getDefault().post(GenSettingEvent.VISION_RADAR_CHANGED);
        }
    }

    public boolean isVisionAdvancedPilotAssistantSystemShow() {
        return this.mVisionAdvancedPilotAssistantSystem;
    }

    public void updateVisionAdvancedPilotAssistantSystem(boolean show) {
        if (this.mVisionAdvancedPilotAssistantSystem != show) {
            this.mVisionAdvancedPilotAssistantSystem = show;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_VISION_ADVANCED_PILOT_ASSISTANT_SYSTEM, show);
            EventBus.getDefault().post(GenSettingEvent.VISION_ADVANCED_PILOT_ASSISTANT_SYSTEM);
        }
    }

    public void updateUserAmap(boolean value) {
        if (this.mbUseAmap != value) {
            this.mbUseAmap = value;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_USE_AMAP, value);
            notifySettingChanged(17);
        }
    }

    public boolean isAMapAvailable() {
        return this.amapAvailability;
    }

    public boolean isMapboxAvailable() {
        return this.mapboxAvailability;
    }

    public void changeMapboxAvailability(boolean availability) {
        if (this.mapboxAvailability != availability) {
            this.mapboxAvailability = availability;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_MAPBOX_AVAILABILITY, availability);
        }
    }

    public boolean isHereAvailable() {
        return this.hereAvailability;
    }

    public void changeHereAvailability(boolean availability) {
        if (this.hereAvailability != availability) {
            this.hereAvailability = availability;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_HERE_AVAILABILITY, availability);
        }
    }

    public boolean isShowRoute() {
        return this.mbShowRoute;
    }

    public void updateShowRoute(boolean value) {
        if (this.mbShowRoute != value) {
            this.mbShowRoute = value;
            if (value) {
                DJIFlurryUtil.logEvent("FPV_GeneralSettings_Map_Switcher_ShowFlightRoute_ON");
            } else {
                DJIFlurryUtil.logEvent("FPV_GeneralSettings_Map_Switcher_ShowFlightRoute_OFF");
            }
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_SHOW_ROUTE, value);
            notifySettingChanged(14);
        }
    }

    public void updateOpenBgDownload(boolean value) {
        if (this.mbOpenBGDownload != value) {
            this.mbOpenBGDownload = value;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_OPEN_BG_DOWNLOAD, value);
            notifySettingChanged(19);
        }
    }

    public void clearRoute() {
        notifyDataEnd(15, false, 0, null);
    }

    public int getParameterUnit() {
        return this.mParameterUnit;
    }

    public void updateParameterUnit(int value) {
        if (3 > value && value >= 0 && value != this.mParameterUnit) {
            DjiSharedPreferencesManager.putInt(this.mContext, "DjiFormat", value);
            this.mParameterUnit = value;
            updateUnitStrs();
            AppPubInjectManager.getAppPubToP3Injectable().dji_gs_Config_setUnitFT(this.mParameterUnit == 0);
            updateHdmiUnit();
            if (value == 1) {
                this.mTmpLimitLockInterval[0] = INTERVAL_LIMIT_LOCK[0];
                this.mTmpLimitLockInterval[1] = INTERVAL_LIMIT_LOCK[1];
            } else {
                this.mTmpLimitLockInterval[0] = transformLength(INTERVAL_LIMIT_LOCK[0]);
                this.mTmpLimitLockInterval[1] = transformLength(INTERVAL_LIMIT_LOCK[1]);
            }
            notifySettingChanged(0);
            EventBus.getDefault().post(GenSettingEvent.PARAMETER_UNIT_CHANGED);
        }
    }

    public int getTemperatureUnit() {
        return this.mTemperatureUnit;
    }

    public void updateTemperatureUnit(int value) {
        if (this.mTemperatureUnit != value) {
            DjiSharedPreferencesManager.putInt(this.mContext, KEY_TEMPERATURE, value);
            this.mTemperatureUnit = value;
            EventBus.getDefault().post(GenSettingEvent.TEMPERATURE_UNIT_CHANGED);
        }
    }

    public boolean isFixWingOpen() {
        return this.mbFixWingOpen;
    }

    public void updateFixWingSwitch(boolean checked) {
        if (this.mbFixWingOpen != checked) {
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_FIXWING_SWITCH, checked);
            this.mbFixWingOpen = checked;
            EventBus.getDefault().post(GenSettingEvent.FIXWING_CHANGED);
        }
    }

    public void resetLocalData() {
        updateShowGrid(0);
        AppPubToP3Injectable pubToP3Injectable = AppPubInjectManager.getAppPubToP3Injectable();
        if (pubToP3Injectable != null) {
            pubToP3Injectable.resetCenterPoint();
        }
        updateOverExposureWarner(false);
    }

    public float[] getIocLimitLockInterval() {
        return this.mTmpLimitLockInterval;
    }

    public boolean isMetric() {
        if (this.mParameterUnit == 0) {
            return false;
        }
        return true;
    }

    public float transformSpeed(float speed) {
        if (this.mParameterUnit == 0) {
            return DJIUnitUtil.metricToImperialBySpeed(speed);
        }
        if (this.mParameterUnit == 2) {
            return DJIUnitUtil.fromMeterPerSecondToKiloMeterPerHour(speed);
        }
        return speed;
    }

    public String formatSpeedIgnoreKm(float speed) {
        if (this.mParameterUnit == 0) {
            float speed2 = DJIUnitUtil.metricToImperialBySpeed(speed);
            return this.mContext.getResources().getString(R.string.common_speed_imperial_format_f, Float.valueOf(speed2));
        }
        return this.mContext.getResources().getString(R.string.common_speed_metric_format_f, Float.valueOf(speed));
    }

    public float transformLength(float length) {
        if (this.mParameterUnit == 0) {
            return DJIUnitUtil.metricToImperialByLength(length);
        }
        return length;
    }

    public float transFormSpeedIntoDifferentUnit(float speed) {
        if (this.mParameterUnit == 0) {
            return DJIUnitUtil.metricToImperialBySpeed(speed);
        }
        if (2 == this.mParameterUnit) {
            return DJIUnitUtil.fromMeterPerSecondToKiloMeterPerHour(speed);
        }
        return speed;
    }

    public float transformSpeedIgnoreKm(float speed) {
        if (this.mParameterUnit == 0) {
            return DJIUnitUtil.metricToImperialBySpeed(speed);
        }
        return speed;
    }

    public float transformToMetric(float length) {
        if (this.mParameterUnit == 0) {
            return DJIUnitUtil.imperialToMetricByLength(length);
        }
        return length;
    }

    public void updateHdmiUnit() {
        int i = 1;
        if (ServiceManager.getInstance().isRemoteOK()) {
            DataDm368SetGParams dataDm368SetGParams = new DataDm368SetGParams();
            DataDm368SetGParams.CmdId cmdId = DataDm368SetGParams.CmdId.ShowUnit;
            if (this.mParameterUnit != 1) {
                i = 0;
            }
            dataDm368SetGParams.set(cmdId, i).start((DJIDataCallBack) null);
        }
    }

    /* access modifiers changed from: private */
    public void handleGetDataCB(int type, int result, Object code) {
        if (result == 0) {
        }
        notifyDataEnd(type, true, result, code instanceof Ccode ? (Ccode) code : null);
    }

    /* access modifiers changed from: private */
    public void handleSetDataCB(int type, int result, Object code) {
        if (result == 0) {
        }
        notifyDataEnd(type, false, result, code instanceof Ccode ? (Ccode) code : null);
    }

    private DJIGenSettingDataManager() {
        this.mParameterUnit = 1;
        this.mTemperatureUnit = 1;
        this.mUnitSpeedStr = "m/s";
        this.mUnitSpeedStrIgnoreKm = "m/s";
        this.mUnitDistStr = "m";
        this.mGridLineMode = 0;
        this.mShowVerticalMode = false;
        this.mVisionRadar = true;
        this.mbShowArPath = true;
        this.mVisionAdvancedPilotAssistantSystem = true;
        this.mbGimbalFinetune = false;
        this.mbGimbalRollFinetune = false;
        this.mbGimbalPitchFinetune = false;
        this.mbFPVGimbalFinetune = false;
        this.mbShowRoute = true;
        this.mbUseAmap = false;
        this.mapboxAvailability = true;
        this.amapAvailability = false;
        this.hereAvailability = false;
        this.mVideoCacheOpenSwitch = true;
        this.mVideoCacheSizeIndex = 1;
        this.mVideoCacheAutoCleanSwitch = false;
        this.mbOverExposureWarner = false;
        this.mbOpenBGDownload = true;
        this.mbOpenHyperLapseFrameView = true;
        this.mbFixWingOpen = false;
        this.mTmpLimitLockInterval = new float[2];
        this.mInit = false;
        this.mAutoCalibrationDataCB = null;
        this.mRollFinetuneDataCB = null;
        this.mPitchFinetuneDataCB = null;
        this.mYawFinetuneDataCB = null;
        this.mResetCameraSettingDataCB = null;
        this.mFormatSSDDataCB = null;
        this.mFormatSDCardDataCB = null;
        this.mFormatRomDataCB = null;
        this.mChangeListeners = new ArrayList<>(3);
        this.mResetCameraSettingSetter = DataCameraLoadParams.getInstance();
        this.mFormatSDCardSetter = DataCameraFormatSDCard.getInstance();
        this.mFormatSSDSetter = DataCameraFormatSSD.getInstance();
        this.mAutoCalibrationSetter = DataGimbalAutoCalibration.getInstance();
        this.mRollFinetuneSetter = DataGimbalRollFinetune.getInstance();
        this.mGenHandler = new GenHandler(this);
        initOperateCBs();
    }

    private void initOperateCBs() {
        this.mResetCameraSettingDataCB = new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIGenSettingDataManager.AnonymousClass1 */

            public void onSuccess(Object model) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 1, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 1, 1, ccode).sendToTarget();
            }
        };
        this.mFormatSDCardDataCB = new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIGenSettingDataManager.AnonymousClass2 */

            public void onSuccess(Object model) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 2, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 2, 1, ccode).sendToTarget();
            }
        };
        this.mFormatRomDataCB = new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIGenSettingDataManager.AnonymousClass3 */

            public void onSuccess(Object model) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 20, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 20, 1, ccode).sendToTarget();
            }
        };
        this.mFormatSSDDataCB = new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIGenSettingDataManager.AnonymousClass4 */

            public void onSuccess(Object model) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 3, 0).sendToTarget();
                DJILogHelper.getInstance().LOGD("", "format ssd success");
            }

            public void onFailure(Ccode ccode) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 3, 1, ccode).sendToTarget();
                DJILogHelper.getInstance().LOGD("", "format ssd failed");
            }
        };
        this.mAutoCalibrationDataCB = new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIGenSettingDataManager.AnonymousClass5 */

            public void onSuccess(Object model) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 13, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 13, 1, ccode).sendToTarget();
            }
        };
        this.mRollFinetuneDataCB = new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIGenSettingDataManager.AnonymousClass6 */

            public void onSuccess(Object model) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 8, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 8, 1, ccode).sendToTarget();
            }
        };
        this.mPitchFinetuneDataCB = new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIGenSettingDataManager.AnonymousClass7 */

            public void onSuccess(Object model) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 10, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 10, 1, ccode).sendToTarget();
            }
        };
        this.mYawFinetuneDataCB = new DJIDataCallBack() {
            /* class dji.pilot.fpv.control.DJIGenSettingDataManager.AnonymousClass8 */

            public void onSuccess(Object model) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 11, 0).sendToTarget();
            }

            public void onFailure(Ccode ccode) {
                DJIGenSettingDataManager.this.mGenHandler.obtainMessage(4097, 11, 1, ccode).sendToTarget();
            }
        };
    }

    private void notifySettingChanged(int type) {
        Iterator<OnSettingChangeListener> iter = this.mChangeListeners.iterator();
        while (iter.hasNext()) {
            iter.next().onChanged(type);
        }
    }

    private void notifySettingClear(int arg) {
        Iterator<OnSettingChangeListener> iter = this.mChangeListeners.iterator();
        while (iter.hasNext()) {
            iter.next().onClear(arg);
        }
    }

    private void notifyDataStart(int type, boolean getOrSet) {
        Iterator<OnSettingChangeListener> iter = this.mChangeListeners.iterator();
        while (iter.hasNext()) {
            iter.next().onDataOperateStart(type, getOrSet);
        }
    }

    private void notifyDataEnd(int type, boolean getOrSet, int result, Ccode code) {
        Iterator<OnSettingChangeListener> iter = this.mChangeListeners.iterator();
        while (iter.hasNext()) {
            iter.next().onDataOperateEnd(type, getOrSet, result, code);
        }
    }

    public boolean getOpenBGDownload() {
        return this.mbOpenBGDownload;
    }

    public boolean getOpenHyperLapseFrameView() {
        return this.mbOpenHyperLapseFrameView;
    }

    public void updateHyperLapseFrameView(boolean isChecked) {
        this.mbOpenHyperLapseFrameView = isChecked;
        DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_OPEN_HYPER_LAPSE_FRAME_VIEW, isChecked);
        EventBus.getDefault().post(GenSettingEvent.HYPER_LAPSE_FRAME_VIEW_CHANGE);
    }

    public void setOpenBGDownload(boolean mbOpenBGDownload2) {
        if (this.mbOpenBGDownload != mbOpenBGDownload2) {
            this.mbOpenBGDownload = mbOpenBGDownload2;
        }
    }

    public boolean getVideoCacheOpenSwitch() {
        return this.mVideoCacheOpenSwitch;
    }

    public void setVideoCacheOpenSwitch(boolean value) {
        if (this.mVideoCacheOpenSwitch != value) {
            this.mVideoCacheOpenSwitch = value;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_VIDEO_CACHE_OPEN_SWITCH, value);
        }
    }

    public boolean isStorageSpaceLimited() {
        return getVideoCacheSizeIndex() != ServiceManager.getContext().getResources().getStringArray(isStorageSpaceSmall() ? R.array.setting_video_cache_space_limit_small_items : R.array.setting_video_cache_space_limit_items).length + -1;
    }

    public boolean isStorageSpaceSmall() {
        return DpadProductManager.getInstance().isPomato();
    }

    public int getVideoCacheSizeIndex() {
        return this.mVideoCacheSizeIndex;
    }

    public int setVideoCacheSizeIndex(int value) {
        if (this.mVideoCacheSizeIndex != value) {
            this.mVideoCacheSizeIndex = value;
            DjiSharedPreferencesManager.putInt(this.mContext, KEY_VIDEO_CACHE_SIZE_INDEX, value);
            RecorderManager.setMaxBufferSpace(isStorageSpaceLimited() ? ((long) (value + 1)) * RecorderManager.GB : LongCompanionObject.MAX_VALUE);
        }
        if (!RecorderManager.isRecordingToExternalSD() && (!ExternalSdRecordingHelper.getVideoCacheExternalStorageEnable() || !ExternalSdRecordingHelper.isExteranSDGranted())) {
            return (int) (RecorderManager.getAvailableSpace() / RecorderManager.MB);
        }
        DocumentFile recordingDf = ExternalSdRecordingHelper.getExternalSdRecordingDirDf();
        if (recordingDf != null) {
            return (int) (ExternalSdRecordingHelper.getAvailableSpace(recordingDf) / RecorderManager.MB);
        }
        return (int) (RecorderManager.getAvailableSpace() / RecorderManager.MB);
    }

    public boolean getVideoCacheAutoCleanSwitch() {
        return this.mVideoCacheAutoCleanSwitch;
    }

    public void setVideoCacheAutoCleanSwitch(boolean value) {
        if (this.mVideoCacheAutoCleanSwitch != value) {
            this.mVideoCacheAutoCleanSwitch = value;
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_VIDEO_CACHE_AUTO_CLEAN_SWITCH, value);
        }
    }

    public boolean getOverExposureWarner() {
        return this.mbOverExposureWarner;
    }

    public void setOverExposureWarner(boolean mbOverExposureWarner2) {
        this.mbOverExposureWarner = mbOverExposureWarner2;
        EventBus.getDefault().post(new OverExposureWarner.OverExposureWarnerStatus(this.mbOverExposureWarner, R.raw.overexposure));
    }

    public void updateOverExposureWarner(boolean value) {
        if (this.mbOverExposureWarner != value) {
            setOverExposureWarner(value);
            DjiSharedPreferencesManager.putBoolean(this.mContext, KEY_OVER_EXPOSURE_WARNER, value);
            notifySettingChanged(18);
        }
    }

    @Keep
    private static final class SingletonHolder {
        public static final DJIGenSettingDataManager mInstance = new DJIGenSettingDataManager();

        private SingletonHolder() {
        }
    }

    @Keep
    private static final class GenHandler extends Handler {
        private final WeakReference<DJIGenSettingDataManager> mOutCls;

        public GenHandler(DJIGenSettingDataManager gen) {
            super(Looper.getMainLooper());
            this.mOutCls = new WeakReference<>(gen);
        }

        public void handleMessage(Message msg) {
            DJIGenSettingDataManager gen = this.mOutCls.get();
            if (gen != null) {
                switch (msg.what) {
                    case 4096:
                        gen.handleGetDataCB(msg.arg1, msg.arg2, msg.obj);
                        return;
                    case 4097:
                        gen.handleSetDataCB(msg.arg1, msg.arg2, msg.obj);
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
