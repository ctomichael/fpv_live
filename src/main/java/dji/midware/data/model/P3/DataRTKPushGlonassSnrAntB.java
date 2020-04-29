package dji.midware.data.model.P3;

public class DataRTKPushGlonassSnrAntB extends DataRTKPushSnr {
    private static DataRTKPushGlonassSnrAntB instance = null;

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

    public static synchronized DataRTKPushGlonassSnrAntB getInstance() {
        DataRTKPushGlonassSnrAntB dataRTKPushGlonassSnrAntB;
        synchronized (DataRTKPushGlonassSnrAntB.class) {
            if (instance == null) {
                instance = new DataRTKPushGlonassSnrAntB();
            }
            dataRTKPushGlonassSnrAntB = instance;
        }
        return dataRTKPushGlonassSnrAntB;
    }
}
