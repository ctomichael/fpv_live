package dji.pilot.fpv.util;

import dji.common.flightcontroller.FlightMode;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.logic.vision.IVisionResDefine;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataA2PushCommom;
import dji.midware.data.model.P3.DataAppUIOperateSetHome;
import dji.midware.data.model.P3.DataAppUIOperation;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataFlycFunctionControl;
import dji.midware.data.model.P3.DataFlycSetHomePoint;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.pilot.publics.R;
import dji.pilot.publics.util.DJICommonUtils;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.BatteryKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJIFlycUtil {
    private static final String TAG = "DJIFlycUtil";

    public static boolean checkGpsNumValid(int gpsNum) {
        return DJICommonUtils.checkGpsNumValid(gpsNum);
    }

    public static boolean checkGpsValid() {
        return DJICommonUtils.checkGpsValid();
    }

    public static boolean checkGpsValid(DataOsdGetPushCommon.DroneType type, int flycVersion, int gpsNum, int gpsLevel) {
        if (DJICommonUtils.isOldMC(type) || flycVersion < 6) {
            return checkGpsNumValid(gpsNum);
        }
        return gpsLevel >= 3;
    }

    public static int getGpsLevel(int gpsNum) {
        if (gpsNum == 0 || gpsNum >= 50) {
            return 0;
        }
        if (gpsNum <= 7) {
            return 1;
        }
        if (gpsNum > 10) {
            return 5;
        }
        return gpsNum - 6;
    }

    public static int getSignalLevel(int signal, int numLevels) {
        int level = 0;
        if (signal > 0 && signal <= 100) {
            level = ((int) ((((float) (signal - 1)) * ((float) numLevels)) / 100.0f)) + 1;
        } else if (signal > 100) {
            level = numLevels;
        }
        if (!DJICommonUtils.isSdrProducts(null)) {
            return level;
        }
        if (signal == 5 || signal == 6) {
            return 0;
        }
        return level;
    }

    public static boolean isMultipleModeOpen(boolean novice, boolean open) {
        return !novice && open;
    }

    public static boolean checkUseNewModeChannel() {
        return DJICommonUtils.checkUseNewModeChannel(DataOsdGetPushCommon.getInstance());
    }

    public static boolean checkUseNewModeChannel(DataOsdGetPushCommon common) {
        return DJICommonUtils.checkUseNewModeChannel(common);
    }

    public static boolean useModePToSmart(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Orange2 || type == ProductType.Tomato || DJICommonUtils.isPomatoSeries(type) || DJICommonUtils.isKumquatSeries(type) || DJICommonUtils.isNewA3FlightMode() || type == ProductType.Potato || DJICommonUtils.isM200Product(type) || type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM240;
    }

    public static boolean cantEnterSmart(ProductType type, boolean multiOpen, DataOsdGetPushCommon.RcModeChannel channel) {
        boolean z = false;
        if (useModePToSmart(type)) {
            if (!multiOpen || channel == DataOsdGetPushCommon.RcModeChannel.CHANNEL_P) {
                return false;
            }
            return true;
        } else if (type.equals(ProductType.A2) && DataA2PushCommom.getInstance().getIntelligenceOrientationEnabled() == 1) {
            return false;
        } else {
            if (!checkUseNewModeChannel()) {
                return true;
            }
            if (!multiOpen || channel != DataOsdGetPushCommon.RcModeChannel.CHANNEL_F) {
                z = true;
            }
            return z;
        }
    }

    public static int getMotorFailReason(DataOsdGetPushCommon.MotorFailReason reason) {
        if (reason == DataOsdGetPushCommon.MotorFailReason.TAKEOFF_EXCEPTION) {
            return R.string.motor_stop_reason_takeoff_exception;
        }
        if (reason == DataOsdGetPushCommon.MotorFailReason.ESC_STALL_NEAR_GROUND) {
            return R.string.motor_stop_reason_esc_stall_near_gound;
        }
        if (reason == DataOsdGetPushCommon.MotorFailReason.ESC_UNBALANCE_ON_GRD) {
            return R.string.motor_stop_reason_esc_unbalance_on_grd;
        }
        if (reason == DataOsdGetPushCommon.MotorFailReason.ESC_PART_EMPTY_ON_GRD) {
            return R.string.motor_stop_reason_esc_part_empty_on_grd;
        }
        if (reason == DataOsdGetPushCommon.MotorFailReason.ENGINE_START_FAILED) {
            return R.string.motor_stop_reason_engine_start_failed;
        }
        if (reason == DataOsdGetPushCommon.MotorFailReason.AUTO_TAKEOFF_LANCH_FAILED) {
            return R.string.motor_stop_reason_auto_takeoff_lanch_faile;
        }
        if (reason == DataOsdGetPushCommon.MotorFailReason.ROLL_OVER_ON_GRD) {
            return R.string.motor_stop_reason_roll_over_on_grd;
        }
        return 0;
    }

    public static boolean checkIsAttiMode(DataOsdGetPushCommon.FLYC_STATE state) {
        if (state == DataOsdGetPushCommon.FLYC_STATE.Atti || state == DataOsdGetPushCommon.FLYC_STATE.Atti_CL || state == DataOsdGetPushCommon.FLYC_STATE.Atti_Hover || state == DataOsdGetPushCommon.FLYC_STATE.Atti_Limited || state == DataOsdGetPushCommon.FLYC_STATE.AttiLangding) {
            return true;
        }
        return false;
    }

    public static boolean checkIsBusy(DataOsdGetPushCommon.FLYC_STATE state) {
        if (state == DataOsdGetPushCommon.FLYC_STATE.AutoTakeoff || state == DataOsdGetPushCommon.FLYC_STATE.AttiLangding || state == DataOsdGetPushCommon.FLYC_STATE.AutoLanding || state == DataOsdGetPushCommon.FLYC_STATE.AssitedTakeoff || state == DataOsdGetPushCommon.FLYC_STATE.GoHome) {
            return true;
        }
        return false;
    }

    public static boolean checkEscmOk(DataOsdGetPushHome.MotorEscmState state) {
        return state == DataOsdGetPushHome.MotorEscmState.NON_SMART || state == DataOsdGetPushHome.MotorEscmState.MOTOR_IDLE || state == DataOsdGetPushHome.MotorEscmState.MOTOR_UP || state == DataOsdGetPushHome.MotorEscmState.MOTOR_OFF || state == DataOsdGetPushHome.MotorEscmState.NON_CONNECT;
    }

    public static int getTripodStatusTitle(DataOsdGetPushCommon.TRIPOD_STATUS status) {
        int resId = R.string.fpv_errorpop_tripod_unknown;
        if (DataOsdGetPushCommon.TRIPOD_STATUS.UNKNOWN == status) {
            return R.string.fpv_errorpop_tripod_unknown;
        }
        if (DataOsdGetPushCommon.TRIPOD_STATUS.FOLD_COMPELTE == status) {
            return R.string.fpv_errorpop_tripod_fold_complete;
        }
        if (DataOsdGetPushCommon.TRIPOD_STATUS.FOLOING == status) {
            return R.string.fpv_errorpop_tripod_foloing;
        }
        if (DataOsdGetPushCommon.TRIPOD_STATUS.STRETCH_COMPLETE == status) {
            return R.string.fpv_errorpop_tripod_stretch_complete;
        }
        if (DataOsdGetPushCommon.TRIPOD_STATUS.STRETCHING == status) {
            return R.string.fpv_errorpop_tripod_stretching;
        }
        if (DataOsdGetPushCommon.TRIPOD_STATUS.STOP_DEFORMATION == status) {
            return R.string.fpv_errorpop_tripod_stop_deformation;
        }
        return resId;
    }

    public static int[] getFlighcActionResId(DataOsdGetPushCommon.FLIGHT_ACTION action) {
        int[] resId = {0, 0, 0};
        if (DataOsdGetPushCommon.FLIGHT_ACTION.WARNING_POWER_GOHOME == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_warning_power_gohome;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.WARNING_POWER_LANDING == action) {
            if (DJICommonUtils.isSupportNewLandingProtocol()) {
                resId[0] = R.string.mc_count_down_excute_urgent_landing;
            } else {
                resId[0] = R.string.fpv_errorpop_flightaction_warning_power_landing;
            }
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.SMART_POWER_GOHOME == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_smart_power_gohome;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.SMART_POWER_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_smart_power_landing;
            resId[1] = 1;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.LOW_VOLTAGE_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_low_voltage_landing;
            resId[1] = 1;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.LOW_VOLTAGE_GOHOME == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_low_voltage_gohome;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.SERIOUS_LOW_VOLTAGE_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_serious_low_voltage_landing;
            resId[1] = 1;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.RC_ONEKEY_GOHOME == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_rc_onekey_gohome;
            resId[2] = 1;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.RC_AUTO_TAKEOFF == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_rc_onekey_takeoff;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.RC_AUTO_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_rc_onekey_landing;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.APP_AUTO_GOHOME == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_app_auto_gohome;
            resId[2] = 1;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.OUTOF_CONTROL_GOHOME == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_outof_control_gohome;
            resId[1] = 1;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.API_AUTO_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_api_auto_landing;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.API_AUTO_GOHOME == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_api_auto_gohome;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.AVOID_GROUND_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_avoid_ground_auto_landing;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.AIRPORT_AVOID_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_airport_avoid_auto_landing;
            resId[1] = 1;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.TOO_CLOSE_GOHOME_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_too_close_auto_landing;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.TOO_FAR_GOHOME_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_too_far_auto_landing;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.MOTORBLOCK_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_app_auto_landing;
            resId[1] = 1;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.APP_REQUEST_FORCE_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_app_request_forcelanding;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.FAKEBATTERY_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_fakebattery_landing;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.RTH_COMING_OBSTACLE_LANDING == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_rth_obstacle_landing;
        } else if (DataOsdGetPushCommon.FLIGHT_ACTION.IMUERROR_RTH == action) {
            resId[0] = R.string.fpv_errorpop_flightaction_imuerror_rth;
        }
        return resId;
    }

    @Deprecated
    public static int[] getFlycModeResId(DataOsdGetPushCommon.FLYC_STATE mode, boolean isVisualWork, ProductType pType, DataOsdGetPushCommon.DroneType droneType, DataOsdGetPushCommon.RcModeChannel channel, boolean open, boolean isFlightRecord) {
        return getFlycModeResId(FlightMode.find(mode.value()), isVisualWork, pType, droneType, channel, open, isFlightRecord);
    }

    public static int[] getFlycModeResId(DataOsdGetPushCommon.FLYC_STATE mode, boolean isVisualWork, boolean isFlightRecord) {
        return getFlycModeResId(FlightMode.find(mode.value()), isVisualWork, isFlightRecord);
    }

    public static int[] getFlycModeResId(FlightMode flightMode, boolean isVisualWork, boolean isFlightRecord) {
        DataOsdGetPushCommon osdCommon = DataOsdGetPushCommon.getInstance();
        return getFlycModeResId(flightMode, isVisualWork, DJIProductManager.getInstance().getType(), osdCommon.getDroneType(), osdCommon.getModeChannel(), isMultipleModeOpen(), isFlightRecord);
    }

    public static boolean isMultipleModeOpen() {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.get(FlightControllerKeys.COMPONENT_KEY, FlightControllerKeys.MULTI_MODE_OPEN));
        if (value != null) {
            return ((Boolean) value.getData()).booleanValue();
        }
        return false;
    }

    public static int[] getFlycModeResId(FlightMode mode, boolean isVisualWork, ProductType pType, DataOsdGetPushCommon.DroneType droneType, DataOsdGetPushCommon.RcModeChannel channel, boolean open, boolean isFlightRecord) {
        int[] resIds = {R.string.fpv_default_str, R.string.fpv_off};
        if (isFlightRecord || (DataOsdGetPushCommon.getInstance().isGetted() && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.NoFlyc)) {
            boolean useP = useModePToSmart(pType);
            if (FlightMode.MANUAL == mode) {
                resIds[0] = R.string.ctrl_mode_manual;
            } else if (FlightMode.ATTI == mode) {
                resIds[0] = R.string.ctrl_mode_atti;
            } else if (FlightMode.ATTI_COURSE_LOCK == mode) {
                resIds[0] = R.string.ctrl_mode_atti;
                resIds[1] = R.string.direct_lock_cl;
            } else if (FlightMode.ATTI_HOVER == mode) {
                resIds[0] = R.string.ctrl_mode_atti;
            } else if (FlightMode.ATTI_LIMITED == mode) {
                resIds[0] = R.string.ctrl_mode_atti;
            } else if (FlightMode.ATTI_LANDING == mode) {
                resIds[0] = R.string.ctrl_mode_landing;
            } else if (FlightMode.AUTO_LANDING == mode) {
                resIds[0] = R.string.ctrl_mode_landing;
            } else if (FlightMode.ASSISTED_TAKEOFF == mode) {
                resIds[0] = R.string.ctrl_mode_takeoff;
            } else if (FlightMode.AUTO_TAKEOFF == mode) {
                resIds[0] = R.string.ctrl_mode_takeoff;
            } else if (FlightMode.GO_HOME == mode) {
                resIds[0] = R.string.ctrl_mode_gohome;
            } else if (FlightMode.JOYSTICK == mode) {
                resIds[0] = R.string.ctrl_mode_joystick;
            } else if (FlightMode.GPS_ATTI == mode) {
                resIds[0] = R.string.ctrl_mode_pgps;
            } else if (FlightMode.GPS_BLAKE == mode) {
                resIds[0] = R.string.ctrl_mode_pgps;
            } else if (FlightMode.HOVER == mode) {
                resIds[0] = R.string.ctrl_mode_pgps;
            } else if (FlightMode.GPS_COURSE_LOCK == mode) {
                if (useP) {
                    resIds[0] = R.string.ctrl_mode_cl;
                } else if (DataOsdGetPushCommon.DroneType.A2 == droneType) {
                    resIds[0] = R.string.ctrl_mode_pcl;
                } else {
                    resIds[0] = R.string.ctrl_mode_fcl;
                }
                resIds[1] = R.string.direct_lock_cl;
            } else if (FlightMode.GPS_HOME_LOCK == mode) {
                if (useP) {
                    resIds[0] = R.string.ctrl_mode_hl;
                } else if (DataOsdGetPushCommon.DroneType.A2 == droneType) {
                    resIds[0] = R.string.ctrl_mode_phl;
                } else {
                    resIds[0] = R.string.ctrl_mode_fhl;
                }
                resIds[1] = R.string.direct_lock_hl;
            } else if (FlightMode.GPS_HOT_POINT == mode) {
                if (useP) {
                    resIds[0] = R.string.ctrl_mode_poi;
                } else {
                    resIds[0] = R.string.ctrl_mode_fpoi;
                }
                resIds[1] = R.string.direct_lock_pl;
            } else if (FlightMode.GPS_WAYPOINT == mode) {
                if (useP) {
                    resIds[0] = R.string.ctrl_mode_way_point;
                } else {
                    resIds[0] = R.string.ctrl_mode_fway_point;
                }
            } else if (FlightMode.CLICK_GO == mode) {
                resIds[0] = R.string.ctrl_mode_pgps;
            } else if (FlightMode.GPS_FOLLOW_ME == mode) {
                if (useP) {
                    resIds[0] = R.string.ctrl_mode_follow_me;
                } else {
                    resIds[0] = R.string.ctrl_mode_ffollow_me;
                }
            } else if (FlightMode.ACTIVE_TRACK == mode || FlightMode.TRACK_SPOTLIGHT == mode) {
                if ((FlightMode.ACTIVE_TRACK != mode || !DataEyeGetPushException.getInstance().isMultiQuickShotEnabled()) && !DataEyeGetPushException.getInstance().isQuickMovieExecuting()) {
                    resIds[0] = R.string.ctrl_mode_ftracking;
                } else {
                    resIds[0] = R.string.ctrl_mode_quick_shot;
                }
            } else if (FlightMode.TAP_FLY == mode) {
                if (DataEyeGetPushException.getInstance().isInDraw()) {
                    resIds[0] = R.string.ctrl_mode_draw;
                } else {
                    resIds[0] = R.string.ctrl_mode_fpointing;
                }
            } else if (FlightMode.PANO == mode) {
                resIds[0] = R.string.ctrl_mode_pano;
            } else if (FlightMode.FARMING == mode) {
                resIds[0] = R.string.ctrl_mode_farm;
            } else if (FlightMode.FPV == mode) {
                resIds[0] = R.string.ctrl_mode_pgps;
            } else if (FlightMode.GPS_SPORT == mode) {
                resIds[0] = R.string.ctrl_mode_sport;
            } else if (FlightMode.GPS_NOVICE == mode) {
                resIds[0] = R.string.ctrl_mode_novice;
            } else if (FlightMode.CONFIRM_LANDING == mode) {
                resIds[0] = R.string.ctrl_mode_landing;
            } else if (FlightMode.TERRAIN_FOLLOW == mode) {
                resIds[0] = R.string.ctrl_mode_terrain_tracking;
            } else if (FlightMode.PALM_CONTROL == mode) {
                resIds[0] = R.string.ctrl_mode_palm_control;
            } else if (FlightMode.QUICK_SHOT == mode) {
                resIds[0] = R.string.ctrl_mode_quick_shot;
            } else if (FlightMode.TRIPOD == mode) {
                resIds[0] = R.string.ctrl_mode_tripod_gps;
            } else if (FlightMode.DETOUR == mode) {
                resIds[0] = R.string.ctrl_mode_apas;
            } else if (FlightMode.MOTORS_JUST_STARTED == mode) {
                resIds[0] = R.string.ctrl_mode_takeoff;
            } else if (FlightMode.DRAW == mode) {
                resIds[0] = R.string.ctrl_mode_draw;
            } else if (FlightMode.CINEMATIC == mode) {
                resIds[0] = R.string.ctrl_mode_cinematic;
            } else if (FlightMode.TIME_LAPSE == mode) {
                resIds[0] = R.string.ctrl_mode_time_lapse;
            } else if (FlightMode.POI2 == mode) {
                resIds[0] = R.string.ctrl_mode_poi2;
            }
            if (R.string.ctrl_mode_pgps == resIds[0]) {
                if (isVisualWork) {
                    resIds[0] = R.string.ctrl_mode_opti;
                } else if (useP) {
                    resIds[0] = R.string.ctrl_mode_gps;
                } else if (open && DataOsdGetPushCommon.RcModeChannel.CHANNEL_F == channel) {
                    resIds[0] = R.string.ctrl_mode_fexit;
                }
            } else if (R.string.ctrl_mode_atti == resIds[0] && !useP && (!open || channel != DataOsdGetPushCommon.RcModeChannel.CHANNEL_A)) {
                resIds[0] = R.string.ctrl_mode_patti;
            }
            if (isRequiredToHardCodeForPosition() && !isFlightRecord && (resIds[0] == R.string.ctrl_mode_opti || resIds[0] == R.string.ctrl_mode_gps)) {
                resIds[0] = R.string.ctrl_mode_p;
            }
        }
        return resIds;
    }

    private static boolean isRequiredToHardCodeForPosition() {
        return DJICommonUtils.isKumquatSeries(null) || DJICommonUtils.isWM240();
    }

    public static boolean isCompassDisturb(DataOsdGetPushCommon.NON_GPS_CAUSE cause) {
        return cause == DataOsdGetPushCommon.NON_GPS_CAUSE.COMPASS_ERROR_LARGE || cause == DataOsdGetPushCommon.NON_GPS_CAUSE.SPEED_ERROR_LARGE || cause == DataOsdGetPushCommon.NON_GPS_CAUSE.YAW_ERROR_LARGE;
    }

    public static boolean checkIsLanding(DataOsdGetPushCommon.FLYC_STATE state) {
        return DataOsdGetPushCommon.FLYC_STATE.AutoLanding == state || DataOsdGetPushCommon.FLYC_STATE.AttiLangding == state || DataOsdGetPushCommon.FLYC_STATE.FORCE_LANDING == state;
    }

    public static boolean checkIsLanding(FlightMode state) {
        return FlightMode.AUTO_LANDING == state || FlightMode.ATTI_LANDING == state || FlightMode.CONFIRM_LANDING == state;
    }

    public static boolean supportDynamicHome(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        long version = DataEyeGetPushException.getInstance().getVisionVersion();
        return (ProductType.Mammoth == type && IVisionResDefine.SupportVersion.VERSION_VISION_SPARK_DYNAMIC_HOME <= version) || (ProductType.KumquatX == type && IVisionResDefine.SupportVersion.VERSION_VISION_KUMAUATX_DYNAMIC_HOME <= version);
    }

    public static boolean checkMCInitialing(DataOsdGetPushCommon common) {
        return !common.isGetted() || common.getFlycState() == DataOsdGetPushCommon.FLYC_STATE.Manula;
    }

    public static void goHomeCmd(DJIDataCallBack callBack) {
        if (DJICommonUtils.isMammoth()) {
            new DataFlycFunctionControl().setCommand(DataFlycFunctionControl.FLYC_COMMAND.GOHOME).start(callBack);
        } else {
            DataSpecialControl.getInstance().setFlyGoHomeStatus(DataSpecialControl.FlyGoHomeStaus.START).start(20);
        }
    }

    public static void dropGoHomeCmd(DJIDataCallBack callBack) {
        if (DJICommonUtils.isMammoth()) {
            new DataFlycFunctionControl().setCommand(DataFlycFunctionControl.FLYC_COMMAND.DropGohome).start(callBack);
        } else {
            DataSpecialControl.getInstance().setFlyGoHomeStatus(DataSpecialControl.FlyGoHomeStaus.EXIT).start(20);
        }
    }

    public static int getMinimumHeightForRTHAtCurrentAltitude() {
        if (DJICommonUtils.isMammoth()) {
            return 3;
        }
        if (DJICommonUtils.isWM230()) {
        }
        return 5;
    }

    public static boolean isTakingOffAction() {
        DataOsdGetPushCommon.FLIGHT_ACTION flightAction = DataOsdGetPushCommon.getInstance().getFlightAction();
        return DataOsdGetPushCommon.FLIGHT_ACTION.API_AUTO_TAKEOFF == flightAction || DataOsdGetPushCommon.FLIGHT_ACTION.APP_AUTO_TAKEOFF == flightAction || DataOsdGetPushCommon.FLIGHT_ACTION.RC_ASSISTANT_TAKEOFF == flightAction || DataOsdGetPushCommon.FLIGHT_ACTION.RC_AUTO_TAKEOFF == flightAction || DataOsdGetPushCommon.FLIGHT_ACTION.WP_AUTO_TAKEOFF == flightAction;
    }

    public static int getMaximumHeightForRTHAtCurrentAltitude() {
        if (DJICommonUtils.isMammoth() || DJICommonUtils.isWM230() || DJICommonUtils.isWM240()) {
            return 20;
        }
        return 0;
    }

    public static int[] getMotorStartFailedDesc(DataOsdGetPushCommon.MotorStartFailedCause motorStartFailedCause) {
        int[] res = {0, 0};
        switch (motorStartFailedCause) {
            case CompassError:
                res[0] = R.string.takeoff_failed_tips_compass_error;
                break;
            case AssistantProtected:
                res[0] = R.string.takeoff_failed_tips_assistant_protected;
                break;
            case DeviceLocked:
                res[0] = R.string.takeoff_failed_tips_device_locked;
                break;
            case DistanceLimit:
                res[0] = R.string.takeoff_failed_tips_distance_limit;
                break;
            case IMUNeedCalibration:
                res[0] = R.string.takeoff_failed_tips_imu_need_calibration;
                break;
            case IMUSNError:
                res[0] = R.string.takeoff_failed_tips_imu_sn_error;
                break;
            case IMUWarning:
                res[0] = R.string.takeoff_failed_tips_imu_warning;
                break;
            case CompassCalibrating:
                res[0] = R.string.takeoff_failed_tips_compass_calibrating;
                break;
            case AttiError:
                res[0] = R.string.takeoff_failed_tips_atti_error;
                break;
            case NoviceProtected:
                res[0] = R.string.takeoff_failed_tips_novice_protected;
                break;
            case BatteryCellError:
                res[0] = R.string.check_battery_broken;
                res[1] = R.string.takeoff_failed_tips_battery_cell_error;
                break;
            case BatteryCommuniteError:
                res[0] = R.string.takeoff_failed_tips_battery_communite_error;
                break;
            case SeriouLowVoltage:
                res[0] = R.string.takeoff_failed_tips_seriou_low_voltage;
                break;
            case SeriouLowPower:
                res[0] = R.string.takeoff_failed_tips_seriou_low_power;
                break;
            case LowVoltage:
                res[0] = R.string.takeoff_failed_tips_low_voltage;
                break;
            case TempureVolLow:
                ProductType productType = DJIProductManager.getInstance().getType();
                float tempture = CacheHelper.toFloat(CacheHelper.getBattery(BatteryKeys.TEMPERATURE));
                switch (productType) {
                    case KumquatX:
                    case WM230:
                    case WM240:
                        if (tempture >= -10.0f) {
                            if (tempture >= 15.0f) {
                                res[0] = R.string.takeoff_failed_tips_tempure_too_low_three;
                                break;
                            } else {
                                res[0] = R.string.takeoff_failed_tips_tempure_too_low_two;
                                break;
                            }
                        } else {
                            res[0] = R.string.takeoff_failed_tips_tempure_too_low_one;
                            break;
                        }
                    case Mammoth:
                        if (tempture >= 0.0f) {
                            if (tempture >= 15.0f) {
                                res[0] = R.string.takeoff_failed_tips_tempure_too_low_three;
                                break;
                            } else {
                                res[0] = R.string.takeoff_failed_tips_tempure_too_low_two;
                                break;
                            }
                        } else {
                            res[0] = R.string.takeoff_failed_tips_tempure_too_low_one;
                            break;
                        }
                    case Pomato:
                    case Tomato:
                    case Potato:
                        if (tempture >= 0.0f) {
                            res[0] = R.string.takeoff_failed_tips_tempure_too_low_three;
                            break;
                        } else {
                            res[0] = R.string.takeoff_failed_tips_tempure_too_low_one;
                            break;
                        }
                    default:
                        res[0] = R.string.takeoff_failed_tips_tempure_vol_low;
                        break;
                }
            case SmartLowToLand:
                res[0] = R.string.takeoff_failed_tips_smart_low_to_land;
                break;
            case BatteryNotReady:
                res[0] = R.string.takeoff_failed_tips_battery_not_ready;
                break;
            case SimulatorMode:
                res[0] = R.string.takeoff_failed_tips_simulator_mode;
                break;
            case PackMode:
                res[0] = R.string.takeoff_failed_tips_pack_mode;
                break;
            case AttitudeAbNormal:
                res[0] = R.string.takeoff_failed_tips_attitude_abnormal;
                break;
            case UnActive:
                res[0] = R.string.takeoff_failed_tips_unactive;
                break;
            case FlyForbiddenError:
                res[0] = R.string.takeoff_failed_fly_limit_zone_check_list;
                break;
            case BiasError:
                res[0] = R.string.takeoff_failed_tips_bias_limit;
                break;
            case EscError:
                res[0] = R.string.takeoff_failed_tips_esc_error;
                break;
            case ImuInitError:
                res[0] = R.string.takeoff_failed_tips_imu_initing;
                break;
            case SystemUpgrade:
                res[0] = R.string.takeoff_failed_tips_system_upgrade;
                break;
            case SimulatorStarted:
                res[0] = R.string.takeoff_failed_tips_simulator_started;
                break;
            case ImuingError:
                res[0] = R.string.takeoff_failed_tips_imuing;
                break;
            case AttiAngleOver:
                res[0] = R.string.takeoff_failed_tips_attiangle;
                break;
            case GyroscopeError:
                res[0] = R.string.takeoff_failed_tips_gyroscope_error;
                break;
            case AcceletorError:
                res[0] = R.string.takeoff_failed_tips_acceletor_error;
                break;
            case CompassFailed:
                res[0] = R.string.takeoff_failed_tips_compass_failed;
                break;
            case BarometerError:
                res[0] = R.string.takeoff_failed_tips_barometer_error;
                break;
            case BarometerNegative:
                res[0] = R.string.takeoff_failed_tips_barometer_negative;
                break;
            case CompassBig:
                res[0] = R.string.takeoff_failed_tips_compass_big;
                break;
            case GyroscopeBiasBig:
                res[0] = R.string.takeoff_failed_tips_gyroscope_bias_big;
                break;
            case AcceletorBiasBig:
                res[0] = R.string.takeoff_failed_tips_acceletor_bias_big;
                break;
            case CompassNoiseBig:
                res[0] = R.string.takeoff_failed_tips_compass_noise_big;
                break;
            case BarometerNoiseBig:
                res[0] = R.string.takeoff_failed_tips_barometer_noise_big;
                break;
            case AircraftTypeMismatch:
                res[0] = R.string.error_aircraft_type_mismatch_title;
                res[1] = R.string.error_aircraft_type_mismatch_desc;
                break;
            case M600_BAT_AUTH_ERR:
                res[0] = R.string.error_battery_authentication_exception_title;
                res[1] = R.string.error_battery_authentication_exception_desc;
                break;
            case M600_BAT_COMM_ERR:
                res[0] = R.string.error_battery_communication_exception_title;
                res[1] = R.string.error_battery_communication_exception_desc;
                break;
            case M600_BAT_TOO_LITTLE:
                if (DJICommonUtils.isProductOrange2() || DJICommonUtils.isM200Product(null)) {
                    res[0] = R.string.fpv_battery_num_less_in2;
                } else {
                    res[0] = R.string.error_battery_num_not_enough_title;
                }
                res[1] = R.string.error_battery_num_not_enough_desc;
                break;
            case M600_BAT_DIF_VOLT_LARGE_1:
                res[0] = R.string.error_battery_voltage_difference_large_title;
                res[1] = R.string.error_battery_voltage_difference_large_desc;
                break;
            case M600_BAT_DIF_VOLT_LARGE_2:
                res[0] = R.string.error_battery_voltage_difference_very_large_title;
                res[1] = R.string.error_battery_voltage_difference_very_large_desc;
                break;
            case TOPOLOGY_ABNORMAL:
                res[0] = R.string.error_device_topology_exception_title;
                res[1] = R.string.error_device_topology_exception_desc;
                break;
            case FoundUnfinishedModule:
                res[0] = R.string.error_found_unfinished_module_title;
                res[1] = R.string.error_found_unfinished_module_desc;
                break;
            case IMUNoconnection:
                res[0] = R.string.error_imu_noconnection_title;
                res[1] = R.string.error_imu_noconnection_desc;
                break;
            case IMUcCalibrationFinished:
                res[0] = R.string.error_imu_calibration_finished_title;
                res[1] = R.string.error_imu_calibration_finished_desc;
                break;
            case NS_ABNORMAL:
                res[0] = R.string.error_ns_exception_title;
                res[1] = R.string.error_ns_exception_desc;
                break;
            case RCCalibration:
                res[0] = R.string.error_rc_calibration_title;
                res[1] = R.string.error_rc_calibration_desc;
                break;
            case RCCalibrationException:
                res[0] = R.string.error_rc_calibration_exception_title;
                res[1] = R.string.error_rc_calibration_exception_desc;
                break;
            case RCCalibrationException2:
                res[0] = R.string.error_rc_calibration_exception2_title;
                res[1] = R.string.error_rc_calibration_exception2_desc;
                break;
            case RCCalibrationException3:
                res[0] = R.string.error_rc_calibration_exception3_title;
                res[1] = R.string.error_rc_calibration_exception3_desc;
                break;
            case RC_NEED_CALI:
                res[0] = R.string.error_rc_calibration_exception4_title;
                res[1] = R.string.error_rc_calibration_exception4_desc;
                break;
            case RCCalibrationUnfinished:
                res[0] = R.string.error_rc_calibration_unfinished_title;
                res[1] = R.string.error_rc_calibration_unfinished_desc;
                break;
            case SDCardException:
                res[0] = R.string.error_sdcard_exception_title;
                res[1] = R.string.error_sdcard_exception_desc;
                break;
            case INVALID_FLOAT:
                res[0] = R.string.error_software_data_invalid_title;
                res[1] = R.string.error_software_data_invalid_desc;
                break;
            case INVALID_VERSION:
                res[0] = R.string.error_version_mismatch_title;
                res[1] = R.string.error_version_mismatch_desc;
                break;
            case BARO_ABNORMAL:
                res[0] = R.string.error_baro_abnormal;
                break;
            case COMPASS_ABNORMAL:
                res[0] = R.string.error_compass_abnormal;
                break;
            case FLASH_OPERATING:
                res[0] = R.string.error_flash_operating;
                break;
            case GPS_ABNORMAL:
                res[0] = R.string.error_gps_abnormal;
                break;
            case GPS_DISCONNECT:
                res[0] = R.string.error_gps_disconnect;
                break;
            case GIMBAL_GYRO_ABNORMAL:
                res[0] = R.string.error_gimbal_abnormal;
                res[1] = R.string.error_gimbal_abnormal_desc;
                break;
            case GIMBAL_ESC_PITCH_NON_DATA:
                res[0] = R.string.error_gimbal_esc_pitch_error;
                res[1] = R.string.error_gimbal_esc_pitch_error_desc;
                break;
            case GIMBAL_ESC_ROLL_NON_DATA:
                res[0] = R.string.error_gimbal_esc_roll_error;
                res[1] = R.string.error_gimbal_esc_roll_error_desc;
                break;
            case GIMBAL_ESC_YAW_NON_DATA:
                res[0] = R.string.error_gimbal_esc_yaw_error;
                res[1] = R.string.error_gimbal_esc_yaw_error_desc;
                break;
            case GIMBAL_FIRM_IS_UPDATING:
                res[0] = R.string.error_gimbal_is_updating;
                res[1] = R.string.error_gimbal_is_updating_desc;
                break;
            case GIMBAL_DISORDER:
                res[0] = R.string.error_gimbal_disorder;
                res[1] = R.string.error_gimbal_disorder_desc;
                break;
            case GIMBAL_PITCH_SHOCK:
                res[0] = R.string.error_gimbal_pitch_shock;
                res[1] = R.string.error_gimbal_pitch_shock_desc;
                break;
            case GIMBAL_ROLL_SHOCK:
                res[0] = R.string.error_gimbal_roll_shock;
                res[1] = R.string.error_gimbal_roll_shock_desc;
                break;
            case GIMBAL_YAW_SHOCK:
                res[0] = R.string.error_gimbal_yaw_shock;
                res[1] = R.string.error_gimbal_yaw_shock_desc;
                break;
            case BatVersionError:
                res[0] = R.string.error_battery_version_error;
                break;
            case RTK_BAD_SIGNAL:
                res[0] = R.string.error_rtk_bad_signal;
                break;
            case RTK_DEVIATION_ERROR:
                res[0] = R.string.error_rtk_deviation;
                break;
            case GIMBAL_IS_CALIBRATING:
                res[0] = R.string.error_gimbal_is_calibrating;
                break;
            case ESC_CALIBRATING:
                res[0] = R.string.error_esc_calibrating_desc;
                res[1] = R.string.error_esc_calibrating_soul;
                break;
            case GPS_SIGN_INVALID:
                res[0] = R.string.error_gps_sign_invalid_desc;
                break;
            case LOCK_BY_APP:
                res[0] = DJIUpStatusHelper.isNeedShieldUpgrade() ? R.string.error_lock_by_app_desc_but_need_ : R.string.error_lock_by_app_desc;
                break;
            case START_FLY_HEIGHT_ERROR:
                res[0] = R.string.error_start_fly_height_error_desc;
                res[1] = R.string.error_start_fly_height_error_soul;
                break;
            case ESC_VERSION_NOT_MATCH:
                res[0] = R.string.error_esc_version_not_match_desc;
                res[1] = R.string.error_esc_version_not_match_soul;
                break;
            case IMU_ORI_NOT_MATCH:
                res[0] = R.string.error_imu_ori_not_match_desc;
                res[1] = R.string.error_imu_ori_not_match_soul;
                break;
            case STOP_BY_APP:
                res[0] = R.string.error_stop_by_app_desc;
                break;
            case COMPASS_IMU_ORI_NOT_MATCH:
                res[0] = R.string.error_compass_imu_ori_not_match_desc;
                res[1] = R.string.error_compass_imu_ori_not_match_soul;
                break;
            case ESC_ECHOING:
                res[0] = R.string.error_esc_echoing;
                break;
            case BATTERY_EMBED_ERROR:
                res[0] = R.string.error_battery_embed_error;
                break;
            case RC_THROTTLE_IS_NOT_IN_MIDDLE:
                res[0] = R.string.error_throttle_rocker_not_middle;
                break;
            case COOLING_FAN_EXCEPTION:
                res[0] = R.string.error_eagle_temp_too_high;
                break;
            case LOW_VERSION_OF_BATTERY:
                res[0] = R.string.error_battery_is_too_low_for_wm100;
                break;
            case BATTERY_INSTALL_ERROR:
                res[0] = R.string.error_battery_install_error;
                break;
            case CRASH:
                res[0] = R.string.error_crash_the_aircraft;
                break;
        }
        return res;
    }

    public static void postHomePointRecord(DataAppUIOperateSetHome.HomeSourceFrom sourceFrom, DataFlycSetHomePoint.HOMETYPE type, double latitude, double longitude, byte interval) {
        StringBuilder log = new StringBuilder();
        log.append("HOMETYPE:").append(type).append("latitude：").append(latitude).append("longitude:").append(longitude);
        DJILog.logWriteD(TAG, log.toString(), "HomeSetLog", new Object[0]);
        DataAppUIOperateSetHome homeInfo = new DataAppUIOperateSetHome(false);
        homeInfo.setSourceFrom(sourceFrom).setHomeType(type).setGpsInfo(latitude, longitude).setInterval(interval);
        DataAppUIOperation uiOperation = new DataAppUIOperation(false);
        uiOperation.setSubType(DataAppUIOperation.AppUIOperateSubType.AppSetHomePoint).setHomeInfo(homeInfo);
        EventBus.getDefault().post(uiOperation);
    }
}
