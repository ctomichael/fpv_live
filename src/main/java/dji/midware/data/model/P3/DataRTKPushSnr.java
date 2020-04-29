package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;

class DataRTKPushSnr extends DataBase {
    public static final int MAX_LENGTH = 32;
    private long mRecTime;
    private final int[] mSnrValue = new int[32];
    private final int[] mSnrValue2 = new int[32];

    DataRTKPushSnr() {
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        this.mRecTime = System.currentTimeMillis();
    }

    public long getRecTime() {
        return this.mRecTime;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: int[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int[] getSnrValues() {
        /*
            r4 = this;
            byte[] r2 = r4._recData
            if (r2 != 0) goto L_0x0006
            r2 = 0
        L_0x0005:
            return r2
        L_0x0006:
            int[] r2 = r4.mSnrValue
            r3 = 0
            java.util.Arrays.fill(r2, r3)
            byte[] r2 = r4._recData
            if (r2 == 0) goto L_0x002c
            byte[] r2 = r4._recData
            int r2 = r2.length
            if (r2 <= 0) goto L_0x002c
            byte[] r2 = r4._recData
            int r2 = r2.length
            r3 = 32
            int r1 = java.lang.Math.min(r2, r3)
            r0 = 0
        L_0x001f:
            if (r0 >= r1) goto L_0x002c
            int[] r2 = r4.mSnrValue
            byte[] r3 = r4._recData
            byte r3 = r3[r0]
            r2[r0] = r3
            int r0 = r0 + 1
            goto L_0x001f
        L_0x002c:
            int[] r2 = r4.mSnrValue
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.model.P3.DataRTKPushSnr.getSnrValues():int[]");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: int[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int[] getSnrValues2() {
        /*
            r5 = this;
            byte[] r2 = r5._recData
            if (r2 != 0) goto L_0x0006
            r2 = 0
        L_0x0005:
            return r2
        L_0x0006:
            int[] r2 = r5.mSnrValue2
            r3 = 0
            java.util.Arrays.fill(r2, r3)
            byte[] r2 = r5._recData
            if (r2 == 0) goto L_0x0031
            byte[] r2 = r5._recData
            int r2 = r2.length
            r3 = 32
            if (r2 <= r3) goto L_0x0031
            byte[] r2 = r5._recData
            int r2 = r2.length
            r3 = 64
            int r1 = java.lang.Math.min(r2, r3)
            r0 = 32
        L_0x0022:
            if (r0 >= r1) goto L_0x0031
            int[] r2 = r5.mSnrValue2
            int r3 = r0 + -32
            byte[] r4 = r5._recData
            byte r4 = r4[r0]
            r2[r3] = r4
            int r0 = r0 + 1
            goto L_0x0022
        L_0x0031:
            int[] r2 = r5.mSnrValue2
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.model.P3.DataRTKPushSnr.getSnrValues2():int[]");
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}
