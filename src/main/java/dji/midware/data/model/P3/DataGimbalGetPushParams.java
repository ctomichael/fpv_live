package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataGimbalControl;
import dji.midware.data.packages.P3.RecvPack;

@Keep
@EXClassNullAway
public class DataGimbalGetPushParams extends DataBase {
    private static DataGimbalGetPushParams instance = null;
    private long mainGimbalLastUpdateTime = 0;

    public static synchronized DataGimbalGetPushParams getInstance() {
        DataGimbalGetPushParams dataGimbalGetPushParams;
        synchronized (DataGimbalGetPushParams.class) {
            if (instance == null) {
                instance = new DataGimbalGetPushParams();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = true;
            }
            dataGimbalGetPushParams = instance;
        }
        return dataGimbalGetPushParams;
    }

    public DataGimbalGetPushParams() {
    }

    public DataGimbalGetPushParams(boolean isRegist) {
        super(isRegist);
    }

    public int getPitch(int index) {
        return ((Short) get(0, 2, Short.class, index)).shortValue();
    }

    public int getPitch() {
        return ((Short) get(0, 2, Short.class)).shortValue();
    }

    public int getRoll(int index) {
        return ((Short) get(2, 2, Short.class, index)).shortValue();
    }

    public int getRoll() {
        return ((Short) get(2, 2, Short.class)).shortValue();
    }

    public int getYaw(int index) {
        return ((Short) get(4, 2, Short.class, index)).shortValue();
    }

    public int getYaw() {
        return ((Short) get(4, 2, Short.class)).shortValue();
    }

    public int getErrorStatus() {
        return ((Short) get(6, 1, Short.class)).shortValue() & 1;
    }

    public byte getRollAdjust() {
        return (byte) ((Short) get(7, 1, Short.class)).shortValue();
    }

    public byte getRollAdjust(int index) {
        return (byte) ((Short) get(7, 1, Short.class, index)).shortValue();
    }

    public int getYawAngle() {
        return getYawAngle(-1);
    }

    public int getYawAngle(int index) {
        return ((Short) get(8, 2, Short.class, index)).shortValue();
    }

    public int getJoystickVerDirection() {
        return ((Integer) get(8, 1, Integer.class)).intValue() & 3;
    }

    public int getJoystickHorDirection() {
        return (((Integer) get(8, 1, Integer.class)).intValue() >> 2) & 3;
    }

    public boolean isAutoCalibration() {
        return (((Integer) get(10, 1, Integer.class)).intValue() & 8) != 0;
    }

    public boolean isAutoCalibration(int index) {
        return (((Integer) get(10, 1, Integer.class, index)).intValue() & 8) != 0;
    }

    public boolean autoCalibrationResult() {
        return (((Integer) get(10, 1, Integer.class)).intValue() & 16) != 0;
    }

    public boolean isPitchInLimit() {
        return (((Integer) get(10, 1, Integer.class)).intValue() & 1) != 0;
    }

    public boolean isPitchInLimit(int index) {
        return (((Integer) get(10, 1, Integer.class, index)).intValue() & 1) != 0;
    }

    public boolean isRollInLimit() {
        return (((Integer) get(10, 1, Integer.class)).intValue() & 2) != 0;
    }

    public boolean isRollInLimit(int index) {
        return (((Integer) get(10, 1, Integer.class, index)).intValue() & 2) != 0;
    }

    public boolean isYawInLimit() {
        return (((Integer) get(10, 1, Integer.class)).intValue() & 4) != 0;
    }

    public boolean isYawInLimit(int index) {
        return (((Integer) get(10, 1, Integer.class, index)).intValue() & 4) != 0;
    }

    public boolean isTopPosition() {
        return (((Integer) get(10, 1, Integer.class)).intValue() & 32) != 0;
    }

    public boolean isTopPosition(int index) {
        return (((Integer) get(10, 1, Integer.class, index)).intValue() & 32) != 0;
    }

    public boolean isStuck() {
        return (((Integer) get(10, 1, Integer.class)).intValue() & 64) != 0;
    }

    public boolean isStuck(int index) {
        return (((Integer) get(10, 1, Integer.class, index)).intValue() & 64) != 0;
    }

    public DataGimbalControl.MODE getMode() {
        return DataGimbalControl.MODE.find(((Integer) get(6, 1, Integer.class)).intValue() >> 6);
    }

    public DataGimbalControl.MODE getMode(int index) {
        return DataGimbalControl.MODE.find(((Integer) get(6, 1, Integer.class, index)).intValue() >> 6);
    }

    public int getSubMode() {
        return (((Integer) get(6, 1, Integer.class)).intValue() >> 5) & 1;
    }

    public int getSubMode(int index) {
        return (((Integer) get(6, 1, Integer.class, index)).intValue() >> 5) & 1;
    }

    public int getVersion() {
        return ((Short) get(11, 1, Short.class)).shortValue() & 15;
    }

    public boolean getVerticalFlag() {
        return (((Short) get(11, 1, Short.class)).shortValue() & 16) != 0;
    }

    public boolean isDoubleClick() {
        return (((Short) get(11, 1, Short.class)).shortValue() & 32) != 0;
    }

    public boolean isTripleClick() {
        return (((Short) get(11, 1, Short.class)).shortValue() & 64) != 0;
    }

    public boolean isSingleClick() {
        return (((Short) get(11, 1, Short.class)).shortValue() & 128) != 0;
    }

    public int getTimeStamp() {
        return ((Integer) get(12, 4, Integer.class)).intValue();
    }

    public byte getPitchAdjust() {
        return (byte) ((Short) get(18, 1, Short.class)).shortValue();
    }

    public byte getPitchAdjust(int index) {
        return (byte) ((Short) get(18, 1, Short.class, index)).shortValue();
    }

    public byte getYawAdjustDegree() {
        return (byte) ((Short) get(19, 1, Short.class)).shortValue();
    }

    public byte getYawAdjustDegree(int index) {
        return (byte) ((Short) get(19, 1, Short.class, index)).shortValue();
    }

    public boolean isFpvGimbal() {
        if (this.pack == null || !GimbalRole.FPV_GIMBAL.isMyRole()) {
            return false;
        }
        return true;
    }

    public GimbalRole getGimbalRole() {
        if (!isGetted() || this.pack == null) {
            return GimbalRole.OTHER;
        }
        return GimbalRole.find(this.pack.senderId);
    }

    @Keep
    public enum GimbalRole {
        MAIN_GIMBAL(0),
        FPV_GIMBAL(4),
        OTHER(255) {
            public boolean isMyRole() {
                return false;
            }
        };
        
        int data;

        private GimbalRole(int _senderId) {
            this.data = 0;
            this.data = _senderId;
        }

        public boolean isMyRole() {
            DataGimbalGetPushParams gimbalPush = DataGimbalGetPushParams.getInstance();
            return gimbalPush.isGetted() && gimbalPush.getSenderId() == this.data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GimbalRole find(int b) {
            GimbalRole result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public void onRecvPackSeted(RecvPack recvPack) {
        super.onRecvPackSeted(recvPack);
        if (!isFpvGimbal()) {
            this.mainGimbalLastUpdateTime = System.currentTimeMillis();
        }
    }

    public boolean isMainGimbalLose() {
        return System.currentTimeMillis() - this.mainGimbalLastUpdateTime > 3000;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
