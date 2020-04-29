package dji.midware.data.model.P3;

public class DataRTKPushGalileoSnrAnt1 extends DataRTKPushSnr {
    private static DataRTKPushGalileoSnrAnt1 instance = null;

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

    public static synchronized DataRTKPushGalileoSnrAnt1 getInstance() {
        DataRTKPushGalileoSnrAnt1 dataRTKPushGalileoSnrAnt1;
        synchronized (DataRTKPushGalileoSnrAnt1.class) {
            if (instance == null) {
                instance = new DataRTKPushGalileoSnrAnt1();
            }
            dataRTKPushGalileoSnrAnt1 = instance;
        }
        return dataRTKPushGalileoSnrAnt1;
    }
}
