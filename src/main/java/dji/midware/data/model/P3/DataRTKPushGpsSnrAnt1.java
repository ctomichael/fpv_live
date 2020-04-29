package dji.midware.data.model.P3;

public class DataRTKPushGpsSnrAnt1 extends DataRTKPushSnr {
    private static DataRTKPushGpsSnrAnt1 instance = null;

    public /* bridge */ /* synthetic */ long getRecTime() {
        return super.getRecTime();
    }

    public /* bridge */ /* synthetic */ int[] getSnrValues() {
        return super.getSnrValues();
    }

    public /* bridge */ /* synthetic */ int[] getSnrValues2() {
        return super.getSnrValues2();
    }

    public /* bridge */ /* synthetic */ void setRecData(byte[] bArr) {
        super.setRecData(bArr);
    }

    public static synchronized DataRTKPushGpsSnrAnt1 getInstance() {
        DataRTKPushGpsSnrAnt1 dataRTKPushGpsSnrAnt1;
        synchronized (DataRTKPushGpsSnrAnt1.class) {
            if (instance == null) {
                instance = new DataRTKPushGpsSnrAnt1();
            }
            dataRTKPushGpsSnrAnt1 = instance;
        }
        return dataRTKPushGpsSnrAnt1;
    }
}
