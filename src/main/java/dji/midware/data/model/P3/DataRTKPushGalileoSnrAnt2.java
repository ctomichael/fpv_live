package dji.midware.data.model.P3;

public class DataRTKPushGalileoSnrAnt2 extends DataRTKPushSnr {
    private static DataRTKPushGalileoSnrAnt2 instance = null;

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

    public static synchronized DataRTKPushGalileoSnrAnt2 getInstance() {
        DataRTKPushGalileoSnrAnt2 dataRTKPushGalileoSnrAnt2;
        synchronized (DataRTKPushGalileoSnrAnt2.class) {
            if (instance == null) {
                instance = new DataRTKPushGalileoSnrAnt2();
            }
            dataRTKPushGalileoSnrAnt2 = instance;
        }
        return dataRTKPushGalileoSnrAnt2;
    }
}
