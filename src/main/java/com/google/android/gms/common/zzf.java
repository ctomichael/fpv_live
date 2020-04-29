package com.google.android.gms.common;

import java.util.Arrays;

final class zzf extends zze {
    private final byte[] zzu;

    zzf(byte[] bArr) {
        super(Arrays.copyOfRange(bArr, 0, 25));
        this.zzu = bArr;
    }

    /* access modifiers changed from: package-private */
    public final byte[] getBytes() {
        return this.zzu;
    }
}
