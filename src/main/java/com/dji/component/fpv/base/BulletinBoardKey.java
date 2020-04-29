package com.dji.component.fpv.base;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import java.io.Serializable;
import java.util.List;

public interface BulletinBoardKey {

    public interface GeneralSetting {
        public static final String ACTION_CLOSE_SETTING = "action_close_setting";
        public static final String ACTION_OPEN_SETTING = "action_open_setting";
        public static final String STATE_IS_VISIBLE = "general_setting_state_is_visible";
    }

    public interface LeftBar {
        public static final String ACTION_HIDE_TAKE_OFF_AND_LAND_DIALOG = "action_hide_take_off_and_land_dialog";
        public static final String ACTION_SHOW_TAKE_OFF_AND_LAND_DIALOG = "action_show_take_off_and_land_dialog";
    }

    public interface Menu {

        public interface Key {
            public static final String ACTION_REMOVE_ITEM_SELF = "action_remove_item_self";
            public static final String ITEM_SOURCE = "menu_item_source";
        }

        public interface Value {
            public static final String MENU_SOURCE_CAMERA_CONTROL = "CameraControlMenu";
            public static final String MENU_SOURCE_CAMERA_OSD = "CameraOsdMenu";
            public static final String MENU_SOURCE_CHECK_LIST = "ChecklistShell";
            public static final String MENU_SOURCE_COMPASS_CALIBRATION = "CompassCalibrationShell";
            public static final String MENU_SOURCE_FLIGHT_OSD = "FlightOsdMenu";
            public static final String MENU_SOURCE_FLIGHT_TOP_OSD = "FlightTopOsdMenu";
            public static final String MENU_SOURCE_GIMBAL_CALIBRATION = "GimbalCalibrationShell";
            public static final String MENU_SOURCE_IMU_CALIBRATION = "IMUCalibrationShell";
            public static final String MENU_SOURCE_LICENSE_MANAGE = "LicenseManageShell";
            public static final String MENU_SOURCE_NEWBIE_CONTROL_ENTRANCE = "NewbieGuideControlEntranceShell";
            public static final String MENU_SOURCE_NEWBIE_DONE = "NewbieGuideDoneShell";
            public static final String MENU_SOURCE_NEWBIE_EXPERIENCE = "NewbieGuideExperienceShell";
            public static final String MENU_SOURCE_NEWBIE_PROGRESS_HELP = "NewbieGuideProgressShell";
            public static final String MENU_SOURCE_NEWBIE_STICK_GUIDE = "NewbieGuideStickGuideShell";
            public static final String MENU_SOURCE_NONE = "item_none";
            public static final String MENU_SOURCE_RC_CALIBRATION = "RcCalibrationShell";
            public static final String MENU_SOURCE_SETTING = "generalSettingShell";
            public static final String MENU_SOURCE_TAKEOFF_LANDING_DIALOG = "TakeOffAndLandDialog";
        }
    }

    public interface GimbalCalibration {
        public static final String ACTION_CLOSE_GIMBAL_CALIBRATION = "action_close_gimbal_calibration";
        public static final String ACTION_OPEN_GIMBAL_CALIBRATION = "action_open_gimbal_calibration";
        public static final String STATE_IS_VISIBLE = "gimbal_calibration_is_visible";
    }

    public interface Map {
        public static final String POSITION = "map_position";
        public static final String STATE = "state_map";

        public interface State {
            public static final String COLLAPSE = "map_state_hide";
            public static final String LARGE = "map_state_big";
            public static final String SHOW = "map_state_show";
            public static final String SMALL = "map_state_small";
            public static final String SWITCHING_TO_COLLAPSE = "map_state_switching_to_collapse";
            public static final String SWITCHING_TO_EXPAND = "map_state_switching_to_expand";
            public static final String SWITCHING_TO_LARGE = "map_state_switching_to_large";
            public static final String SWITCHING_TO_SMALL = "map_state_switching_to_small";
        }
    }

    public interface NewbieGuide {
        public static final String ACTION_HIDE_CONTROL_GUIDE = "action_hide_control_guide";
        public static final String ACTION_HIDE_ENTRANCE = "action_hide_entrance";
        public static final String ACTION_HIDE_EXPERIENCE_SELECT = "action_hide_experience_select";
        public static final String ACTION_HIDE_HARDWARE_PREPARE = "action_hide_hardware_prepare";
        public static final String ACTION_HIDE_LANDING_PREPARE = "action_hide_landing_prepare";
        public static final String ACTION_HIDE_MASK_GUIDE = "action_hide_mask_guide";
        public static final String ACTION_SHOW_CHECK_GUIDE = "action_show_check_guide";
        public static final String ACTION_SHOW_CONTROL_GUIDE = "action_show_control_guide";
        public static final String ACTION_SHOW_HARDWARE_PREPARE = "action_show_hardware_prepare";
        public static final String ACTION_SHOW_LANDING_PREPARE = "action_show_landing_prepare";
        public static final String ACTION_SHOW_MASK_GUIDE = "action_show_mask_guide";
        public static final String ACTION_SHOW_MASK_GUIDE_FOR_TEST = "action_show_mask_guide_for_test";
        public static final String EXPERIENCE = "key_experience_select";
        public static final String STATE_IS_PROGRESS_CHANGE_ANIMATION_PLAYING = "state_is_progress_change_animation_playing";
        public static final String STATE_IS_VISIBLE = "newbie_guide_menu_is_visible";
        public static final String STATE_LANDING_PREPARE_IS_VISIBLE = "state_landing_prepare_is_visible";

        public interface Experience {
            public static final String NORMAL = "value_experience_normal";
            public static final String ROOKIE = "value_experience_rookie";
        }
    }

    public interface TouchLayer {
        public static final String TOUCH_EVENT = "touch_event";
    }

    public interface UnlockLicenseManage {
        public static final String ACTION_CLOSE_UL_MANAGE = "action_close_ul_manage";
        public static final String ACTION_OPEN_UL_MANAGE = "action_open_ul_manage";
    }

    public interface CompassCalibration {
        public static final String ACTION_CLOSE_COMPASS_CALIBRATION = "action_close_compass_calibration";
        public static final String ACTION_OPEN_COMPASS_CALIBRATION = "action_open_compass_calibration";
    }

    public interface Mission {
        public static final String ACTION_ENTER_MISSION = "action_enter_mission";
        public static final String ACTION_EXIT_MISSION = "action_exit_mission";
        public static final String CURRENT_FLIGHT_MODE = "current_flight_mode";

        public interface MultiQuickshot {
            public static final String ACTION_CLOSE_SELECT_TARGET_GUIDE = "ACTION_CLOSE_SELECT_TARGET_GUIDE";
            public static final String ACTION_CLOSE_VIDEO_GUIDE = "action_close_video_guide";
            public static final String ACTION_PROPOSAL_CLICKED = "ACTION_PROPOSAL_CLICKED";
            public static final String ACTION_START_QUICK_SHOT = "action_start_quick_shot";
            public static final String ACTION_STOP_COUNTING_DOWN = "action_stop_counting_down";
            public static final String ACTION_STOP_QUICK_SHOT = "action_stop_quick_shot";
            public static final String ACTION_STRETCH_RECT_DONE = "ACTION_STRETCH_RECT_DONE";
            public static final String DISMISS_START_QUICK_SHOT_BUBBLE_GUIDE = "action_dismiss_start_quick_shot_bubble_guide";
            public static final String STATE = "multi_quickshot_state";
        }
    }

    public interface TopBar {
        public static final String ACTION_CLOSE_CHECK_LIST = "action_close_check_list";
        public static final String ACTION_OPEN_CHECK_LIST = "action_open_check_list";
    }

    public interface AttitudeBar {
        public static final String STATE_IS_VISIBLE = "state_attitude_bar_is_visible";
    }

    public interface CameraControl {
        public static final String ACTION_CLOSE_MODE_MENU = "action_close_mode_menu";
        public static final String ACTION_OPEN_MODE_MENU = "action_open_mode_menu";
        public static final String EVENT_MODE_MENU = "event_mode_menu";
        public static final String STATE_IS_SHOWN_OPERATOR_UNAVAILABLE_TIP = "state_is_shown_operator_unavailable_tip";
    }

    public interface Checklist {
        public static final String GOTO_LOGIN_PAGE = "goto_login_page";
        public static final String GOTO_REAL_NAME_PAGE = "goto_real_name_page";
        public static final String SET_UNLOCK_DARK_NEED_GPS = "set_unlock_dark_need_gps";
        public static final String SHOW_TOAST = "goto_show_toast";
        public static final String STATE_CHECKLIST_FLIGHT_LEVEL = "checklist_flight_level";
        public static final String STATE_IS_VISIBLE = "checklist_state_is_visible";
        public static final String VALUE_CHECKLIST_SCROLL_VERTICAL = "checklist_scroll_vertical_value";

        public interface FlightLevel {
            public static final int ERROR = 2;
            public static final int NORMAL = 0;
            public static final int UNDEFINED = -1;
            public static final int WARN = 1;
        }
    }

    public interface LatLng {
        public static final String DJI_PILOT_LAT_LNG = "dji_pilot_lat_lng";
    }

    public interface MapSwitch {
        public static final String ACTION_ATTITUDE_BAR_SWITCH_TO_MAP = "action_switch_to_small_map";
        public static final String ACTION_MAP_COLLAPSE = "action_map_collapse";
        public static final String ACTION_MAP_EXPAND = "action_map_expand";
        public static final String ACTION_MAP_PREVIEW_SWITCH = "action_map_preview_switch";
        public static final String ACTION_MAP_SWITCH_TO_ATTITUDE_BAR = "action_map_switch_to_attitude_bar";
    }

    public interface RcCalibration {
        public static final String ACTION_CLOSE_RC_CALIBRATION = "action_close_rc_calibration";
        public static final String ACTION_OPEN_RC_CALIBRATION = "action_open_rc_calibration";
    }

    public interface UpdateHome {
        public static final String ACTION_CLOSE_UPDATE_HOME = "action_close_update_home";
        public static final String ACTION_OPEN_UPDATE_HOME = "action_open_update_home";
    }

    public interface Fpv {
        public static final String SCREEN_SIZE = "screen_size";
    }

    public interface IMUCalibration {
        public static final String ACTION_CLOSE_IMU_CALIBRATION = "action_close_imu_calibration";
        public static final String ACTION_OPEN_IMU_CALIBRATION = "action_open_imu_calibration";
    }

    public interface OSD {
        public static final String ACTION_EXPAND_OSD = "action_expand_osd";
        public static final String STATE_IS_EXPAND = "state_is_expand";
    }

    public interface Preview {
        public static final String COUNT_DOWN = "count_down";
        public static final String EVENT_FRAME_BITMAP_READY = "event_frame_bitmap_ready";
        public static final String POSITION_VIDEO = "size_video";
        public static final String POSITION_VIEW = "size_view";
        public static final String STATE = "state_preview";

        public interface State {
            public static final String LARGE = "state_preview_big";
            public static final String SMALL = "state_preview_small";
            public static final String SWITCHING = "state_preview_switching";
        }

        public static class CountDownEvent implements Serializable {
            private final String content;
            private final int duration;

            public CountDownEvent(@NonNull String content2, int duration2) {
                this.content = content2;
                this.duration = duration2;
            }

            public String getContent() {
                return this.content;
            }

            public int getDuration() {
                return this.duration;
            }
        }
    }

    public interface CameraOsd {

        public interface Key {
            public static final String ACTION_OSD_MENU_AUTO = "action_osd_menu_auto";
            public static final String EVENT_OSD_MENU = "event_osd_menu";
            public static final String EVENT_OSD_MENU_ADJUSTMENT = "event_osd_menu_adjustment";
            public static final String EVENT_OSD_MENU_HIDE = "event_osd_menu_hide";
            public static final String STATE_OSD_MENU_IS_AUTO = "event_osd_auto";
        }

        public interface Value {

            public enum Type {
                TYPE_MENU_ISO,
                TYPE_MENU_SHUTTER,
                TYPE_MENU_EV,
                TYPE_MENU_WHITE_BALANCE,
                ANY
            }

            public static class MenuEventAdvanced implements Serializable {
                private final int displayAnchorPoint;
                private final Type menuType;
                private final int position;
                private final List<String> range;

                public MenuEventAdvanced(@NonNull Type type, int position2, @NonNull List<String> range2, int displayAnchorPoint2) {
                    this.menuType = type;
                    this.position = position2;
                    this.range = range2;
                    this.displayAnchorPoint = displayAnchorPoint2;
                }

                @NonNull
                public Type getMenuType() {
                    return this.menuType;
                }

                public int getPosition() {
                    return this.position;
                }

                @NonNull
                public List<String> getRange() {
                    return this.range;
                }

                public int getDisplayAnchorPoint() {
                    return this.displayAnchorPoint;
                }
            }

            public static class MenuEvent implements Serializable {
                private final int displayAnchorPoint;
                private boolean isAutoSupport;
                private final int max;
                private final Type menuType;
                private final int progress;

                public MenuEvent(@NonNull Type type, boolean supportAuto, int progress2, int max2, int displayAnchorPoint2) {
                    this.menuType = type;
                    this.isAutoSupport = supportAuto;
                    this.progress = progress2;
                    this.max = max2;
                    this.displayAnchorPoint = displayAnchorPoint2;
                }

                public Type getMenuType() {
                    return this.menuType;
                }

                public boolean isAutoSupport() {
                    return this.isAutoSupport;
                }

                public int getProgress() {
                    return this.progress;
                }

                public int getMax() {
                    return this.max;
                }

                public int getDisplayAnchorPoint() {
                    return this.displayAnchorPoint;
                }
            }

            public static class AdjustmentEvent implements Serializable {
                private final boolean isDisplay;
                private final Type menuType;
                private final int progress;

                public AdjustmentEvent(@NonNull Type type, boolean isDisplay2, int progress2) {
                    this.menuType = type;
                    this.isDisplay = isDisplay2;
                    this.progress = progress2;
                }

                public Type getMenuType() {
                    return this.menuType;
                }

                public boolean isDisplay() {
                    return this.isDisplay;
                }

                public int getProgress() {
                    return this.progress;
                }

                public boolean equals(@Nullable Object obj) {
                    if (!(obj instanceof AdjustmentEvent)) {
                        return false;
                    }
                    AdjustmentEvent event = (AdjustmentEvent) obj;
                    if (this.menuType == event.menuType && this.isDisplay == event.isDisplay && this.progress == event.progress) {
                        return true;
                    }
                    return false;
                }
            }
        }
    }

    public interface Toast {

        public interface Key {
            public static final String ACTION_POST_TOAST = "action_post_toast";
        }

        public interface Value {

            public static class ToastEvent implements Serializable {
                final int duration;
                final int iconResId;
                final CharSequence text;
                final int textRes;

                public ToastEvent(int duration2, CharSequence text2) {
                    this.duration = duration2;
                    this.text = text2;
                    this.iconResId = -1;
                    this.textRes = -1;
                }

                private ToastEvent(int duration2, @StringRes int strRes) {
                    this.duration = duration2;
                    this.textRes = strRes;
                    this.iconResId = -1;
                    this.text = null;
                }

                public ToastEvent(int duration2, CharSequence text2, @DrawableRes int iconResId2) {
                    this.duration = duration2;
                    this.text = text2;
                    this.textRes = -1;
                    this.iconResId = iconResId2;
                }

                public int getDuration() {
                    return this.duration;
                }

                public CharSequence getText(Context context) {
                    if (this.textRes == -1) {
                        return this.text;
                    }
                    return context.getText(this.textRes);
                }

                @DrawableRes
                public int getIconResId() {
                    return this.iconResId;
                }
            }
        }
    }
}
