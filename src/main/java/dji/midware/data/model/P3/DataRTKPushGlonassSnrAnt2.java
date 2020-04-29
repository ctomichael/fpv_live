package dji.midware.data.model.P3;

public class DataRTKPushGlonassSnrAnt2 extends DataRTKPushSnr {
    private static DataRTKPushGlonassSnrAnt2 instance = null;

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

    public static synchronized DataRTKPushGlonassSnrAnt2 getInstance() {
        DataRTKPushGlonassSnrAnt2 dataRTKPushGlonassSnrAnt2;
        synchronized (DataRTKPushGlonassSnrAnt2.class) {
            if (instance == null) {
                instance = new DataRTKPushGlonassSnrAnt2();
            }
            dataRTKPushGlonassSnrAnt2 = instance;
        }
        return dataRTKPushGlonassSnrAnt2;
    }
}
