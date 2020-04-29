package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

public class DataRTKGetPushStateInfo extends DataBase {
    private static final String TAG = "DataRTKGetPushStateInfo";
    private static DataRTKGetPushStateInfo instance = null;

    public static synchronized DataRTKGetPushStateInfo getInstance() {
        DataRTKGetPushStateInfo dataRTKGetPushStateInfo;
        synchronized (DataRTKGetPushStateInfo.class) {
            if (instance == null) {
                instance = new DataRTKGetPushStateInfo();
                instance.isNeedPushLosed = true;
            }
            dataRTKGetPushStateInfo = instance;
        }
        return dataRTKGetPushStateInfo;
    }

    public int getState() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
