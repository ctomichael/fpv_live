package dji.midware.data.model.P3;

public class DataRTKPushBeidouSnrAnt2 extends DataRTKPushSnr {
    private static DataRTKPushBeidouSnrAnt2 instance = null;

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

    public static synchronized DataRTKPushBeidouSnrAnt2 getInstance() {
        DataRTKPushBeidouSnrAnt2 dataRTKPushBeidouSnrAnt2;
        synchronized (DataRTKPushBeidouSnrAnt2.class) {
            if (instance == null) {
                instance = new DataRTKPushBeidouSnrAnt2();
            }
            dataRTKPushBeidouSnrAnt2 = instance;
        }
        return dataRTKPushBeidouSnrAnt2;
    }
}
