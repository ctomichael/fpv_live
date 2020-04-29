package dji.midware.data.model.P3;

public class DataRTKPushBeidouSnrAntB extends DataRTKPushSnr {
    private static DataRTKPushBeidouSnrAntB instance = null;

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

    public static synchronized DataRTKPushBeidouSnrAntB getInstance() {
        DataRTKPushBeidouSnrAntB dataRTKPushBeidouSnrAntB;
        synchronized (DataRTKPushBeidouSnrAntB.class) {
            if (instance == null) {
                instance = new DataRTKPushBeidouSnrAntB();
            }
            dataRTKPushBeidouSnrAntB = instance;
        }
        return dataRTKPushBeidouSnrAntB;
    }
}
