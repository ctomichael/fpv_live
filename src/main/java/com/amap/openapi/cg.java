package com.amap.openapi;

import android.support.annotation.Nullable;
import com.loc.fc;
import java.util.List;

/* compiled from: RequestBufferBuilder */
public class cg extends cf {
    public cg() {
        super(1000);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.bl.a(com.loc.fc, byte):void
     arg types: [com.amap.openapi.ch, byte]
     candidates:
      com.amap.openapi.bl.a(com.loc.fc, int):void
      com.amap.openapi.bl.a(com.loc.fc, long):void
      com.amap.openapi.bl.a(com.loc.fc, byte):void */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.bl.a(com.loc.fc, int):void
     arg types: [com.amap.openapi.ch, int]
     candidates:
      com.amap.openapi.bl.a(com.loc.fc, byte):void
      com.amap.openapi.bl.a(com.loc.fc, long):void
      com.amap.openapi.bl.a(com.loc.fc, int):void */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.bl.b(com.loc.fc, byte):void
     arg types: [com.amap.openapi.ch, byte]
     candidates:
      com.amap.openapi.bl.b(com.loc.fc, int):void
      com.amap.openapi.bl.b(com.loc.fc, byte):void */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.cj.a(com.loc.fc, int[]):int
     arg types: [com.amap.openapi.ch, int[]]
     candidates:
      com.amap.openapi.cj.a(com.loc.fc, long[]):int
      com.amap.openapi.cj.a(com.loc.fc, byte):void
      com.amap.openapi.cj.a(com.loc.fc, int):void
      com.amap.openapi.cj.a(com.loc.fc, long):void
      com.amap.openapi.cj.a(com.loc.fc, int[]):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.cj.a(com.loc.fc, byte):void
     arg types: [com.amap.openapi.ch, byte]
     candidates:
      com.amap.openapi.cj.a(com.loc.fc, int[]):int
      com.amap.openapi.cj.a(com.loc.fc, long[]):int
      com.amap.openapi.cj.a(com.loc.fc, int):void
      com.amap.openapi.cj.a(com.loc.fc, long):void
      com.amap.openapi.cj.a(com.loc.fc, byte):void */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.cj.a(com.loc.fc, int):void
     arg types: [com.amap.openapi.ch, int]
     candidates:
      com.amap.openapi.cj.a(com.loc.fc, int[]):int
      com.amap.openapi.cj.a(com.loc.fc, long[]):int
      com.amap.openapi.cj.a(com.loc.fc, byte):void
      com.amap.openapi.cj.a(com.loc.fc, long):void
      com.amap.openapi.cj.a(com.loc.fc, int):void */
    @Nullable
    public byte[] a(byte b, String str, byte b2, String str2, String str3, byte b3, String str4, String str5, String str6, String str7, long j, String str8, String str9, String str10, String str11, List<Long> list, List<String> list2) {
        int i;
        super.a();
        try {
            int a = this.a.a(str);
            int a2 = this.a.a(str2);
            int a3 = this.a.a(str3);
            int a4 = this.a.a(str4);
            int a5 = this.a.a(str5);
            int a6 = this.a.a(str6);
            int a7 = this.a.a(str7);
            int a8 = this.a.a(str8);
            int a9 = this.a.a(str9);
            int a10 = this.a.a(str10);
            int a11 = this.a.a(str11);
            bl.a(this.a);
            bl.a((fc) this.a, b2);
            bl.a((fc) this.a, a2);
            bl.b(this.a, a3);
            bl.b((fc) this.a, b3);
            bl.e(this.a, a4);
            bl.f(this.a, a5);
            bl.d(this.a, a6);
            bl.c(this.a, a7);
            bl.a(this.a, j);
            bl.g(this.a, a8);
            bl.h(this.a, a9);
            bl.i(this.a, a10);
            bl.j(this.a, a11);
            int b4 = bl.b(this.a);
            if (list == null || list.size() <= 0) {
                i = 0;
            } else {
                long[] jArr = new long[list.size()];
                for (int i2 = 0; i2 < list.size(); i2++) {
                    Long l = list.get(i2);
                    if (l != null) {
                        jArr[i2] = l.longValue();
                    } else {
                        jArr[i2] = 0;
                    }
                }
                i = cj.a(this.a, jArr);
            }
            int i3 = 0;
            if (list2 != null && list2.size() > 0) {
                int[] iArr = new int[list2.size()];
                for (int i4 = 0; i4 < list2.size(); i4++) {
                    iArr[i4] = this.a.a(list2.get(i4));
                }
                i3 = cj.a((fc) this.a, iArr);
            }
            cj.a(this.a);
            cj.a((fc) this.a, b);
            cj.a((fc) this.a, a);
            cj.a(this.a, System.currentTimeMillis() / 1000);
            cj.b(this.a, b4);
            if (i > 0) {
                cj.c(this.a, i);
            }
            if (i3 > 0) {
                cj.d(this.a, i3);
            }
            cj.e(this.a, cj.b(this.a));
            return this.a.f();
        } catch (Throwable th) {
            return null;
        }
    }
}
