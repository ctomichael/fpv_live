package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataFlycFunctionControl;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataAppOperation extends DataBase {
    public static final int length = 5;
    public int operationId;
    public APP_OPERATION_STATE operationState;

    public DataAppOperation(boolean isRegist) {
        super(isRegist);
    }

    public void setData(int id, APP_OPERATION_STATE state) {
        this.operationId = id;
        this.operationState = state;
    }

    public void setData(DataFlycFunctionControl.FLYC_COMMAND id, APP_OPERATION_STATE state) {
        this.operationId = id.value();
        this.operationState = state;
    }

    public void setData(int id, APP_OPERATION_STATE state, boolean transform) {
        switch (id) {
            case 0:
                this.operationId = APP_OPERATION_ID.SET_HOMEPOINT_TYPE_AIRCRAFT.value();
                break;
            case 1:
                this.operationId = APP_OPERATION_ID.SET_HOMEPOINT_TYPE_RC.value();
                break;
            case 2:
                this.operationId = APP_OPERATION_ID.SET_HOMEPOINT_TYPE_MOBILE.value();
                break;
            case 3:
                this.operationId = APP_OPERATION_ID.SET_HOMEPOINT_TYPE_TRACKING.value();
                break;
            default:
                this.operationId = APP_OPERATION_ID.SET_HOMEPOINT_TYPE_RC.value();
                break;
        }
        this.operationState = state;
    }

    public byte[] getRecData() {
        byte[] buffer = new byte[5];
        System.arraycopy(BytesUtil.getBytes(this.operationId), 0, buffer, 0, 4);
        buffer[0 + 4] = (byte) this.operationState.value();
        return buffer;
    }

    public void setRecData(byte[] data) {
        this.operationId = BytesUtil.getInt(data);
        this.operationState = APP_OPERATION_STATE.find(data[0 + 4]);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum APP_OPERATION_ID {
        SPECIAL_CMD_GOHOME(256),
        SPECIAL_CMD_STOPHOME(257),
        SET_HOMEPOINT_TYPE_AIRCRAFT(258),
        SET_HOMEPOINT_TYPE_MOBILE(259),
        SET_HOMEPOINT_TYPE_RC(260),
        SET_HOMEPOINT_TYPE_TRACKING(261),
        SET_HOMEPOINT_TYPE_STOPTRACKING(262),
        OTHER(263);
        
        private int data;

        private APP_OPERATION_ID(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static APP_OPERATION_ID find(int b) {
            APP_OPERATION_ID result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum APP_OPERATION_STATE {
        NORMAL(1),
        SEND(2),
        ACK_SUCCESS(3),
        ACK_FAILED(4),
        OTHER(100);
        
        private int data;

        private APP_OPERATION_STATE(int data2) {
            this.data = data2;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static APP_OPERATION_STATE find(int b) {
            APP_OPERATION_STATE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
