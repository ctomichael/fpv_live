package dji.midware.media;

public class DJIContinuousDataTestUtil {
    private long[] lastDataArr;

    public long[] testValue(long... values) {
        long[] rst = null;
        if (this.lastDataArr == null) {
            this.lastDataArr = new long[values.length];
        } else {
            for (int i = 0; i < values.length; i++) {
                if (!(values[i] == this.lastDataArr[i] + 1 || values[i] == 0)) {
                    rst = this.lastDataArr;
                }
            }
        }
        System.arraycopy(values, 0, this.lastDataArr, 0, values.length);
        return rst;
    }
}
