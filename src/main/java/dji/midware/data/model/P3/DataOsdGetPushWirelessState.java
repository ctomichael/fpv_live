package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataOsdGetPushWirelessState extends DataBase {
    private static DataOsdGetPushWirelessState instance = null;

    public static synchronized DataOsdGetPushWirelessState getInstance() {
        DataOsdGetPushWirelessState dataOsdGetPushWirelessState;
        synchronized (DataOsdGetPushWirelessState.class) {
            if (instance == null) {
                instance = new DataOsdGetPushWirelessState();
            }
            dataOsdGetPushWirelessState = instance;
        }
        return dataOsdGetPushWirelessState;
    }

    public SdrWirelessState getEventCode() {
        return SdrWirelessState.find(((Integer) get(0, 2, Integer.class)).intValue());
    }

    public String getInternalEvent() {
        StringBuilder sb = new StringBuilder();
        if (this._recData == null) {
            return sb.toString();
        }
        int i = 2;
        while (true) {
            if (i >= this._recData.length) {
                break;
            } else if (this._recData[i] == 0) {
                sb.append((char) this._recData[i]);
                break;
            } else {
                if (BytesUtil.isNumberOrLetter(this._recData[i]) || this._recData[i] == 61) {
                    sb.append((char) this._recData[i]);
                } else {
                    sb.append((int) this._recData[i]);
                }
                i++;
            }
        }
        return sb.toString();
    }

    @Keep
    public enum SdrWirelessState {
        STRONG_DISTURBANCE(0),
        VIDEO_DISTURBANCE(1),
        RC_DISTURBANCE(2),
        LOW_SIGNAL_POWER(3),
        CUSTOM_SIGNAL_DISTURBANCE(4),
        RC_TO_GLASS_DIST(5),
        UAV_HAL_RESTART(6),
        GLASS_DIST_RC_ANTENNA(7),
        DISCONNECT_RC_DISTURB(8),
        DISCONNECT_UAV_DISTURB(9),
        DISCONNECT_WEEK_SIGNAL(10),
        INTERNAL_EVENT(255),
        NONE(256);
        
        private int mValue;

        private SdrWirelessState(int v) {
            this.mValue = v;
        }

        public int value() {
            return this.mValue;
        }

        public static SdrWirelessState find(int fv) {
            SdrWirelessState[] items = values();
            int len = items.length;
            for (int i = 0; i != len; i++) {
                if (fv == items[i].value()) {
                    return items[i];
                }
            }
            return NONE;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
