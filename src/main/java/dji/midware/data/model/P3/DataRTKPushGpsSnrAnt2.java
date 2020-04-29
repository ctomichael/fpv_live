package dji.midware.data.model.P3;

public class DataRTKPushGpsSnrAnt2 extends DataRTKPushSnr {
    private static DataRTKPushGpsSnrAnt2 instance = null;

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

    public static synchronized DataRTKPushGpsSnrAnt2 getInstance() {
        DataRTKPushGpsSnrAnt2 dataRTKPushGpsSnrAnt2;
        synchronized (DataRTKPushGpsSnrAnt2.class) {
            if (instance == null) {
                instance = new DataRTKPushGpsSnrAnt2();
            }
            dataRTKPushGpsSnrAnt2 = instance;
        }
        return dataRTKPushGpsSnrAnt2;
    }
}
