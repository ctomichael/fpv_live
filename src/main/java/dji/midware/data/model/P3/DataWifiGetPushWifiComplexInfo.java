package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

public class DataWifiGetPushWifiComplexInfo extends DataBase {
    private static final int CHN_NUM_LENGTH = 4;
    private static final int CHN_SET_HEADER_LENGTH = 8;
    private static final int FREQ_TYPE_LENGTH = 4;
    private static final int ONE_INDEX_LENGTH = 2;
    private static final int TOTAL_HEADER_LENGTH = 8;
    private static DataWifiGetPushWifiComplexInfo mInstance = null;

    public static synchronized DataWifiGetPushWifiComplexInfo getInstance() {
        DataWifiGetPushWifiComplexInfo dataWifiGetPushWifiComplexInfo;
        synchronized (DataWifiGetPushWifiComplexInfo.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetPushWifiComplexInfo();
            }
            dataWifiGetPushWifiComplexInfo = mInstance;
        }
        return dataWifiGetPushWifiComplexInfo;
    }

    public int getBoardType() {
        return ((Integer) get(0, 1, Integer.class)).intValue() & 3;
    }

    public int getWorkMode() {
        return (((Integer) get(0, 1, Integer.class)).intValue() >> 3) & 3;
    }

    public boolean getCaliState() {
        return ((((Integer) get(0, 1, Integer.class)).intValue() >> 6) & 1) == 1;
    }

    public int getChannelMode() {
        return (((Integer) get(0, 1, Integer.class)).intValue() >> 7) & 1;
    }

    public int getIntfStatus() {
        return ((Byte) get(1, 1, Byte.class)).byteValue();
    }

    public int getTotoalChannelContentLength() {
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    public int getCurChannel() {
        return ((Short) get(6, 1, Short.class)).shortValue();
    }

    public int getChnannelNumberMode() {
        return ((Short) get(7, 1, Short.class)).shortValue();
    }

    public int[] get2dot4GChannleList() {
        int length = 0;
        int firstChnSetType = getFirstChannelType();
        if (firstChnSetType == 1) {
            length = getLength24G();
        } else if (firstChnSetType == 2) {
            length = 0;
        }
        int[] indexList = new int[length];
        for (int i = 0; i < length; i++) {
            indexList[i] = ((Integer) get((i * 2) + 16, 2, Integer.class)).intValue();
        }
        return indexList;
    }

    private int getLength24G() {
        return ((Integer) get(12, 4, Integer.class)).intValue();
    }

    public int[] get5GChannelList() {
        int len24GChnSet = 0;
        int firstChnSetType = getFirstChannelType();
        if (firstChnSetType == 1) {
            len24GChnSet = (getLength24G() * 2) + 8;
        } else if (firstChnSetType == 2) {
            len24GChnSet = 0;
        }
        int len58G = ((Integer) get(len24GChnSet + 8 + 4, 4, Integer.class)).intValue();
        int[] indexList = new int[len58G];
        int before58GIndexLength = len24GChnSet + 8 + 8;
        for (int i = 0; i < len58G; i++) {
            indexList[i] = ((Integer) get((i * 2) + before58GIndexLength, 2, Integer.class)).intValue();
        }
        return indexList;
    }

    private int getFirstChannelType() {
        return ((Integer) get(8, 4, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
