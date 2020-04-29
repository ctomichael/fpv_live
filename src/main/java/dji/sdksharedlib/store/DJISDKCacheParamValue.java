package dji.sdksharedlib.store;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import java.util.Arrays;

@EXClassNullAway
public class DJISDKCacheParamValue {
    private static final String TAG = "DJISDKCacheParamValue";
    private long createdAt;
    private Object data;
    private long expireDurationInMillis = -1;
    private Source source;
    private Status status;

    public enum Source {
        Push,
        Get,
        Set
    }

    public enum Status {
        Empty,
        Valid,
        Conflicted
    }

    @Deprecated
    public DJISDKCacheParamValue(Object data2) {
        this.data = data2;
    }

    public DJISDKCacheParamValue(Object data2, Status status2, Source source2) {
        this.data = data2;
        this.status = status2;
        this.source = source2;
        this.createdAt = System.currentTimeMillis();
    }

    public DJISDKCacheParamValue(Object data2, Status status2, Source source2, long expireDurationInMilliseconds) {
        this.data = data2;
        this.status = status2;
        this.source = source2;
        this.createdAt = System.currentTimeMillis();
        this.expireDurationInMillis = expireDurationInMilliseconds;
    }

    public Source getSource() {
        return this.source;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void updateCreatedAt() {
        this.createdAt = System.currentTimeMillis();
    }

    public long getExpireDurationInMillis() {
        return this.expireDurationInMillis;
    }

    public Object getData() {
        return this.data;
    }

    public int hashCode() {
        if (this.data != null) {
            return this.data.hashCode();
        }
        return 0;
    }

    public boolean equals(Object o) {
        if (o == null && this.data == null) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof DJISDKCacheParamValue) || !o.equals(this.data)) {
            return false;
        }
        return true;
    }

    public boolean isDataEquals(Object o) {
        if (this.data == null && o == null) {
            return true;
        }
        if (this.data == null || o == null) {
            return false;
        }
        if (!this.data.getClass().isArray() || !o.getClass().isArray()) {
            return this.data.equals(o);
        }
        return arrayEquals(this.data, o);
    }

    public boolean isValid() {
        if (this.status == Status.Empty || this.status == Status.Conflicted) {
            return false;
        }
        if (this.expireDurationInMillis == -1 || System.currentTimeMillis() - this.createdAt <= this.expireDurationInMillis) {
            return true;
        }
        return false;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean} */
    private boolean arrayEquals(Object a, Object b) {
        if (a.getClass() == int[].class) {
            return Arrays.equals((int[]) ((int[]) a), (int[]) ((int[]) b));
        }
        if (a.getClass() == boolean[].class) {
            return Arrays.equals((boolean[]) ((boolean[]) a), (boolean[]) ((boolean[]) b));
        }
        if (a.getClass() == byte[].class) {
            return Arrays.equals((byte[]) ((byte[]) a), (byte[]) ((byte[]) b));
        }
        if (a.getClass() == char[].class) {
            return Arrays.equals((char[]) ((char[]) a), (char[]) ((char[]) b));
        }
        if (a.getClass() == long[].class) {
            return Arrays.equals((long[]) ((long[]) a), (long[]) ((long[]) b));
        }
        if (a.getClass() == short[].class) {
            return Arrays.equals((short[]) ((short[]) a), (short[]) ((short[]) b));
        }
        if (a.getClass() == float[].class) {
            return Arrays.equals((float[]) ((float[]) a), (float[]) ((float[]) b));
        }
        if (a.getClass() == double[].class) {
            return Arrays.equals((double[]) ((double[]) a), (double[]) ((double[]) b));
        }
        try {
            return Arrays.equals((Object[]) ((Object[]) a), (Object[]) ((Object[]) b));
        } catch (Exception e) {
            DJILog.e(TAG, "", new Object[0]);
            return false;
        }
    }
}
