package dji.midware.data.model.P3;

public class DataRTKPushGalileoSnrAntB extends DataRTKPushSnr {
    private static DataRTKPushGalileoSnrAntB instance = null;

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

    public static synchronized DataRTKPushGalileoSnrAntB getInstance() {
        DataRTKPushGalileoSnrAntB dataRTKPushGalileoSnrAntB;
        synchronized (DataRTKPushGalileoSnrAntB.class) {
            if (instance == null) {
                instance = new DataRTKPushGalileoSnrAntB();
            }
            dataRTKPushGalileoSnrAntB = instance;
        }
        return dataRTKPushGalileoSnrAntB;
    }
}
