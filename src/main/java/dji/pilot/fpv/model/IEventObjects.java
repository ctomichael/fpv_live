package dji.pilot.fpv.model;

import android.text.TextUtils;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public interface IEventObjects {

    public enum CommonEventObj {
        LEFTMENU_OPT_CLICK,
        SWITCHING_FLIGHTMODE,
        FINISH_FLIGHTMODE,
        ARMACTION,
        LEFTMENU_JS_CLICK_START,
        LEFTMENU_JS_CLICK_STOP,
        LEFTMENU_JS_CLICK_FOR_SPARK_GAME_PAD,
        VISION_SELFCAL,
        SHOW_PRECISE_LANDING,
        RC_FIXWING_CHANGED,
        REFER_TO_VISION_SETTING_VIEW,
        OPEN_PALM_CONTROL_TUTORIAL_DIALOG,
        FOCUS_POINT,
        START_GO_BACK_TO_PREVIEW
    }

    public enum FpvEventObj {
        HIDE_VIEWS_EXCEPT_TOP,
        SHOW_VIEWS_EXCEPT_TOP,
        SHOW_GUIDANCE_USER,
        SHOW_NEW_ARMACTION_DLG,
        HIDE_VIEWS,
        SHOW_VIEWS,
        OPEN_SWITCH_BATTERY
    }

    public enum GotoItem {
        Mc_Sensor,
        Mc_Guidance,
        Flyc_Compass,
        Rc_Mode,
        Flyc,
        Wifi
    }

    public enum DJIUpgradeStatus {
        YES,
        NO,
        NoMatch,
        YES_G,
        NO_G,
        NoMatch_G
    }

    public enum DJIUpgradeStatusForLock {
        YES,
        NO,
        DIALOG,
        LOCK
    }

    public enum FpvGamePadEvent {
        ACTION_DEVICE_ADD,
        ACTION_DEVICE_REMOVE,
        ACTION_SHUTTER,
        ACTION_RECORD
    }

    public enum FlightModeEvent {
        SmartModeChanged
    }

    public interface PopViewItem {
        public static final long DEFAULT_SHIELD_TIME = 300000;
        public static final long DURATION_DISAPPEAR = 4000;
        public static final String POP_VIEW_TAG_BATTERY_ALERT = "battery_alert";
        public static final String POP_VIEW_TAG_DISTURBANCE_ALERT = "disturbance_alert";
        public static final String POP_VIEW_TAG_FLYFORBID_ALERT = "flyforbid_alert";
        public static final String POP_VIEW_TAG_GALE_ALERT = "gale_alert";
        public static final String POP_VIEW_TAG_GUIDANCE_ALERT = "guidance_alert";

        public enum MessageStatus {
            AUTODISAPPEAR,
            PUSH,
            STATIC
        }

        public enum MessageType {
            NONE,
            NOTIFY,
            WARNING,
            DANGEROUS
        }

        public enum PostAction {
            INSERT,
            REMOVE
        }

        public static class ErrorModel {
            public int mDescResId = 0;
            public String mDescString = null;
            public long mDuration = PopViewItem.DURATION_DISAPPEAR;
            public int mImgResId = 0;
            public MessageStatus mMsgStatus = MessageStatus.AUTODISAPPEAR;
            public String mParamTitleStr = "";
            public PostAction mPostAction = PostAction.INSERT;
            public boolean mShieldByUserClosed = false;
            public long mShieldTime = 0;
            public int mTitleResId = 0;
            public String mTitleString = null;
            public MessageType messageType = MessageType.WARNING;
            public String tag = "";

            public static final class FakeBuilder {
                private final ErrorModel mModel = new ErrorModel();

                public FakeBuilder setMessageType(MessageType msgType) {
                    this.mModel.messageType = msgType;
                    return this;
                }

                public FakeBuilder setImgResId(int resId) {
                    this.mModel.mImgResId = resId;
                    return this;
                }

                public FakeBuilder setTitle(int resId) {
                    this.mModel.mTitleResId = resId;
                    return this;
                }

                public FakeBuilder setTitle(String title) {
                    this.mModel.mTitleString = title;
                    return this;
                }

                public FakeBuilder setParamTitleStr(String paramStr) {
                    this.mModel.mParamTitleStr = paramStr;
                    return this;
                }

                public FakeBuilder setDesc(int resId) {
                    this.mModel.mDescResId = resId;
                    return this;
                }

                public FakeBuilder setDesc(String desc) {
                    this.mModel.mDescString = desc;
                    return this;
                }

                public FakeBuilder setMsgStatus(MessageStatus msgStatus) {
                    this.mModel.mMsgStatus = msgStatus;
                    return this;
                }

                public FakeBuilder setPostAction(PostAction action) {
                    this.mModel.mPostAction = action;
                    return this;
                }

                public FakeBuilder setTag(String tag) {
                    this.mModel.tag = tag;
                    return this;
                }

                public FakeBuilder setDuration(long duration) {
                    this.mModel.mDuration = duration;
                    return this;
                }

                public FakeBuilder setShiledTime(long shieldTime) {
                    this.mModel.mShieldTime = shieldTime;
                    return this;
                }

                public FakeBuilder setShieldByUserClosed(boolean byUserClosed) {
                    this.mModel.mShieldByUserClosed = byUserClosed;
                    return this;
                }

                public ErrorModel build() {
                    return this.mModel;
                }

                public void postEvent() {
                    EventBus.getDefault().post(this.mModel);
                }
            }

            public void copyOf(ErrorModel code) {
                this.mImgResId = code.mImgResId;
                this.messageType = code.messageType;
                this.mTitleResId = code.mTitleResId;
                this.mParamTitleStr = code.mParamTitleStr;
                this.mDescResId = code.mDescResId;
                this.mTitleString = code.mTitleString;
                this.mDescString = code.mDescString;
                this.mMsgStatus = code.mMsgStatus;
                this.mPostAction = code.mPostAction;
                this.mDuration = code.mDuration;
                this.mShieldTime = code.mShieldTime;
                this.mShieldByUserClosed = code.mShieldByUserClosed;
                this.tag = code.tag;
            }

            public void clearData() {
                this.mImgResId = 0;
                this.messageType = MessageType.WARNING;
                this.mTitleResId = 0;
                this.mParamTitleStr = "";
                this.mDescResId = 0;
                this.mTitleString = null;
                this.mDescString = null;
                this.mMsgStatus = MessageStatus.AUTODISAPPEAR;
                this.mPostAction = PostAction.INSERT;
                this.mDuration = PopViewItem.DURATION_DISAPPEAR;
                this.mShieldTime = 0;
                this.mShieldByUserClosed = false;
                this.tag = "";
            }

            public boolean equals(Object o) {
                boolean ret = super.equals(o);
                if (ret || !(o instanceof ErrorModel)) {
                    return ret;
                }
                ErrorModel cc = (ErrorModel) o;
                if (cc.mMsgStatus == MessageStatus.STATIC) {
                    if (cc.mTitleResId == 0) {
                        return TextUtils.equals(cc.mTitleString, this.mTitleString) && TextUtils.equals(cc.mDescString, this.mDescString);
                    }
                    if (cc.mTitleResId == this.mTitleResId) {
                        return true;
                    }
                    return false;
                } else if (cc.mTitleResId != this.mTitleResId || !TextUtils.equals(cc.mTitleString, this.mTitleString) || cc.mDescResId != this.mDescResId || !TextUtils.equals(cc.mDescString, this.mDescString)) {
                    return ret;
                } else {
                    return true;
                }
            }

            public int hashCode() {
                int result = ((this.mTitleResId + 527) * 31) + this.mDescResId;
                if (this.mTitleString != null) {
                    result = (result * 31) + this.mTitleString.hashCode();
                }
                if (this.mDescString != null) {
                    return (result * 31) + this.mDescString.hashCode();
                }
                return result;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder(32);
                sb.append("titleId[").append(String.valueOf(this.mTitleResId)).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
                sb.append("descId[").append(String.valueOf(this.mDescResId)).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
                sb.append("titleStr[").append(this.mTitleString).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
                sb.append("descStr[").append(this.mDescString).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
                return sb.toString();
            }
        }
    }
}
