package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

public class DataRTKGetReferenceStationSource extends DataBase {
    private static final String TAG = "DataRTKGetReferenceStationSource";
    private static DataRTKGetReferenceStationSource instance = null;

    public static synchronized DataRTKGetReferenceStationSource getInstance() {
        DataRTKGetReferenceStationSource dataRTKGetReferenceStationSource;
        synchronized (DataRTKGetReferenceStationSource.class) {
            if (instance == null) {
                instance = new DataRTKGetReferenceStationSource();
                instance.isNeedPushLosed = true;
            }
            dataRTKGetReferenceStationSource = instance;
        }
        return dataRTKGetReferenceStationSource;
    }

    public int getSource() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
