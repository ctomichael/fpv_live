package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataWifiGetPushSweepFrequency extends DataBase {
    private static final int CHN_SET_HEADER_LENGTH = 6;
    private static final int CHN_SET_LENGTH_BYTE_LENGTH = 2;
    private static final int CHN_SET_ONE_ITEM_INDEX_LENGTH = 4;
    private static final int CHN_SET_ONE_ITEM_LENGTH = 8;
    private static final int CHN_SET_ONE_ITEM_RSSI_LENGTH = 4;
    private static final int CHN_SET_TYPE_FREQ_LENGTH = 4;
    private static final int TOTAL_HEADER_LENGTH = 4;
    private static DataWifiGetPushSweepFrequency instance = null;

    public static synchronized DataWifiGetPushSweepFrequency getInstance() {
        DataWifiGetPushSweepFrequency dataWifiGetPushSweepFrequency;
        synchronized (DataWifiGetPushSweepFrequency.class) {
            if (instance == null) {
                instance = new DataWifiGetPushSweepFrequency();
            }
            dataWifiGetPushSweepFrequency = instance;
        }
        return dataWifiGetPushSweepFrequency;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public int[] get24GRssiList() {
        int length = 0;
        int firstChnSetType = getFirstChannelType();
        if (firstChnSetType == 0) {
            length = getLength24G();
        } else if (firstChnSetType == 1) {
            length = 0;
        }
        int[] rssiList = new int[length];
        for (int i = 0; i < length; i++) {
            rssiList[i] = ((Integer) get((i * 8) + 10 + 4, 4, Integer.class)).intValue();
        }
        return rssiList;
    }

    private int getLength24G() {
        return ((Integer) get(8, 2, Integer.class)).intValue();
    }

    public int[] get5GRssiList() {
        int len24GChnSet = 0;
        int firstChnSetType = getFirstChannelType();
        if (firstChnSetType == 0) {
            len24GChnSet = (getLength24G() * 8) + 6;
        } else if (firstChnSetType == 1) {
            len24GChnSet = 0;
        }
        int len58G = ((Integer) get(len24GChnSet + 4 + 4, 2, Integer.class)).intValue();
        int[] rssiList = new int[len58G];
        int before58GRssiLength = len24GChnSet + 4 + 6;
        for (int i = 0; i < len58G; i++) {
            rssiList[i] = ((Integer) get((i * 8) + before58GRssiLength + 4, 4, Integer.class)).intValue();
        }
        return rssiList;
    }

    public int getTotal() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    private int getFirstChannelType() {
        return ((Integer) get(4, 4, Integer.class)).intValue();
    }
}
