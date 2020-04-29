package dji.midware.data.model.P3;

public class DataRTKPushGlonassSnrAnt1 extends DataRTKPushSnr {
    private static DataRTKPushGlonassSnrAnt1 instance = null;

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

    public static synchronized DataRTKPushGlonassSnrAnt1 getInstance() {
        DataRTKPushGlonassSnrAnt1 dataRTKPushGlonassSnrAnt1;
        synchronized (DataRTKPushGlonassSnrAnt1.class) {
            if (instance == null) {
                instance = new DataRTKPushGlonassSnrAnt1();
            }
            dataRTKPushGlonassSnrAnt1 = instance;
        }
        return dataRTKPushGlonassSnrAnt1;
    }
}
