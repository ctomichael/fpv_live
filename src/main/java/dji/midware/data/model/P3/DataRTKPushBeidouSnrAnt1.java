package dji.midware.data.model.P3;

public class DataRTKPushBeidouSnrAnt1 extends DataRTKPushSnr {
    private static DataRTKPushBeidouSnrAnt1 instance = null;

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

    public static synchronized DataRTKPushBeidouSnrAnt1 getInstance() {
        DataRTKPushBeidouSnrAnt1 dataRTKPushBeidouSnrAnt1;
        synchronized (DataRTKPushBeidouSnrAnt1.class) {
            if (instance == null) {
                instance = new DataRTKPushBeidouSnrAnt1();
            }
            dataRTKPushBeidouSnrAnt1 = instance;
        }
        return dataRTKPushBeidouSnrAnt1;
    }
}
