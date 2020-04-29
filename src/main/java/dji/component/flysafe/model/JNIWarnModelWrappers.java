package dji.component.flysafe.model;

import android.support.annotation.Keep;
import dji.component.flysafe.util.NFZLogUtil;
import dji.component.flysafe.util.ProtobufHelper;
import dji.flysafe.CDLWarningEventType;
import dji.flysafe.LimitArea;
import dji.flysafe.ShowCDLWarningContext;
import dji.flysafe.ShowTipsContext;
import dji.flysafe.ShowWarningContext;
import java.util.ArrayList;
import java.util.List;

@Keep
public class JNIWarnModelWrappers {
    private static final String TAG = "JNI warning wrappers-";

    @Keep
    public static class ShowWrapperBase {
        public int mLimitHeight;
        public List<FlyfrbAreaJniElement> mWarnElements = new ArrayList();
    }

    public enum NewFlyFrbUIAction {
        WARN_IN_LIMIT(0),
        WARN_SEEM_IN_LIMIT(1),
        OUTSIDE_WHITE_LIST_AREA(2),
        SEEM_IN_AUTHORIZE_HAS_NO_LICENSE(3),
        IN_AUTHORIZE_HAS_NO_LICENSE(4),
        IN_AUTHORIZE_HAS_LICENSE(5),
        NEAR_SPECIAL_WARNING(6),
        NEAR_LIMIT(7),
        NEAR_LIMIT_HEIGHT(8),
        NEAR_AUTHORIZE_HAS_NO_LICENSE(9),
        NEAR_AUTHORIZE_HAS_LICENSE(10),
        NEAR_MULTI_LIMIT_TYPE(11),
        FLY_TOUCH_AUTHORIZE_HAS_LICENSE(12),
        WARN_UAV_SEEM_IN_LIMIT(13),
        HAVE_ONE_HOUR_WILL_APPLY_TFRS(14),
        ACTION_UNKNOWN(255);
        
        private static volatile NewFlyFrbUIAction[] sValues = null;
        private int mValue = 0;

        private NewFlyFrbUIAction(int i) {
            this.mValue = i;
        }

        public static NewFlyFrbUIAction ofValue(int value) {
            if (sValues == null) {
                sValues = values();
            }
            NewFlyFrbUIAction[] newFlyFrbUIActionArr = sValues;
            for (NewFlyFrbUIAction ts : newFlyFrbUIActionArr) {
                if (ts.mValue == value) {
                    return ts;
                }
            }
            return ACTION_UNKNOWN;
        }
    }

    public enum AirportWarningAreaTakeoffState {
        INSIDE(0),
        OUTSIDE(1);
        
        private int mValue;

        private AirportWarningAreaTakeoffState(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }

        public static AirportWarningAreaTakeoffState find(int v) {
            AirportWarningAreaTakeoffState[] values = values();
            for (int i = 0; i != values.length; i++) {
                if (values[i].getValue() == v) {
                    return values[i];
                }
            }
            return OUTSIDE;
        }
    }

    @Keep
    public static class ShowWarningWrapper extends ShowWrapperBase {
        public NewFlyFrbUIAction mWarnActionType = NewFlyFrbUIAction.ACTION_UNKNOWN;

        public static ShowWarningWrapper convertFromJniModel(ShowWarningContext warningContext) {
            ShowWarningWrapper warningWrapper = new ShowWarningWrapper();
            if (warningContext.type == null) {
                warningWrapper.mWarnActionType = NewFlyFrbUIAction.ofValue(0);
                NFZLogUtil.savedLOGD("JNI warning wrappers-protoBuf warning context type null!!!");
            } else {
                warningWrapper.mWarnActionType = NewFlyFrbUIAction.ofValue(warningContext.type.getValue());
                NFZLogUtil.savedLOGD("JNI warning wrappers-protoBuf warning type: " + warningContext.type.getValue() + " translated type: " + warningWrapper.mWarnActionType);
            }
            warningWrapper.mLimitHeight = (int) ProtobufHelper.toDouble(warningContext.limitHeight);
            NFZLogUtil.savedLOGD("JNI warning wrappers-translated limit height: " + warningWrapper.mLimitHeight);
            if (warningContext.areas != null) {
                NFZLogUtil.savedLOGD("JNI warning wrappers-limit areas size: " + warningContext.areas.size());
                for (LimitArea area : warningContext.areas) {
                    warningWrapper.mWarnElements.add(FlyfrbAreaJniElement.convertFromProtobuf(area));
                }
                NFZLogUtil.printIndex0AreaPos(warningContext.areas);
            }
            return warningWrapper;
        }

        public String toString() {
            return "mWarnActionType " + this.mWarnActionType + "\nmLimitHeight " + this.mLimitHeight + "\nmWarnElements size " + this.mWarnElements.size();
        }
    }

    @Keep
    public static class ShowCDLWarningWrapper extends ShowWrapperBase {
        public int mCDTime = -1;
        public CDLWarningEventType mEventType;

        public static ShowCDLWarningWrapper convertFromJniModel(ShowCDLWarningContext warningContext) {
            ShowCDLWarningWrapper warningWrapper = new ShowCDLWarningWrapper();
            warningWrapper.mEventType = convertFromJniType(warningContext.type);
            warningWrapper.mCDTime = ProtobufHelper.toInt(warningContext.countdown);
            warningWrapper.mLimitHeight = (int) ProtobufHelper.toDouble(warningContext.limitHeight);
            if (warningContext.areas != null) {
                for (LimitArea area : warningContext.areas) {
                    warningWrapper.mWarnElements.add(FlyfrbAreaJniElement.convertFromProtobuf(area));
                }
                NFZLogUtil.printIndex0AreaPos(warningContext.areas);
            }
            return warningWrapper;
        }

        public static CDLWarningEventType convertFromJniType(CDLWarningEventType type) {
            if (type == null) {
                return CDLWarningEventType.InLimitArea;
            }
            return type;
        }

        public String toString() {
            return "mEventType " + this.mEventType + "\nmCDTime " + this.mCDTime + "\nmWarnElements size " + this.mWarnElements.size();
        }
    }

    @Keep
    public static class ShowTipsWrapper {
        public int mLimitHeight;
        public NormalTipsType mTipsType = NormalTipsType.OTHER;
        public List<FlyfrbAreaJniElement> mWarnElements = new ArrayList();

        public static ShowTipsWrapper convertFromJniModel(ShowTipsContext warningContext) {
            ShowTipsWrapper warningWrapper = new ShowTipsWrapper();
            if (warningContext.type == null) {
                NFZLogUtil.savedLOGD("JNI warning wrappers-protoBuf tip context type null!!!");
                warningWrapper.mTipsType = NormalTipsType.fromValue(0);
            } else {
                warningWrapper.mTipsType = NormalTipsType.fromValue(warningContext.type.getValue());
                NFZLogUtil.savedLOGD("JNI warning wrappers-protoBuf tip type: " + warningContext.type.getValue() + " translated type: " + warningWrapper.mTipsType);
            }
            warningWrapper.mLimitHeight = (int) ProtobufHelper.toDouble(warningContext.limitHeight);
            NFZLogUtil.savedLOGD("JNI warning wrappers-tip translated limit height: " + warningWrapper.mLimitHeight);
            if (warningContext.areas != null) {
                NFZLogUtil.savedLOGD("JNI warning wrappers-tip areas size: " + warningContext.areas.size());
                for (LimitArea area : warningContext.areas) {
                    warningWrapper.mWarnElements.add(FlyfrbAreaJniElement.convertFromProtobuf(area));
                }
                NFZLogUtil.printIndex0AreaPos(warningContext.areas);
            }
            return warningWrapper;
        }

        public String toString() {
            return "mTipsType " + this.mTipsType + "\nmLimitHeight " + this.mLimitHeight + "\nmWarnElements size " + this.mWarnElements.size();
        }

        public enum NormalTipsType {
            TakeOffWithLimitHeightWithoutGPS(0),
            TakeOffUnderLimitHeightArea(1),
            TakeOffUnderWarningArea(2),
            CollisionWithLimitArea(3),
            CollisionWithAuthAreaWithoutLicense(4),
            CollisionWithLimitHeightArea(5),
            CollisionWithGoHomeMode(6),
            CollisionWithSmartMode(7),
            CollisionWithSpecialUnlockArea(8),
            OTHER(255);
            
            private static volatile NormalTipsType[] sValues = null;
            private final int value;

            private NormalTipsType(int value2) {
                this.value = value2;
            }

            public static NormalTipsType fromValue(int value2) {
                if (sValues == null) {
                    sValues = values();
                }
                NormalTipsType[] normalTipsTypeArr = sValues;
                for (NormalTipsType ts : normalTipsTypeArr) {
                    if (ts.value == value2) {
                        return ts;
                    }
                }
                return OTHER;
            }
        }
    }
}
