package com.amap.openapi;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.amap.location.collection.CollectionConfig;
import com.amap.location.common.a;
import com.amap.location.common.util.d;
import com.amap.location.common.util.f;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import com.loc.fc;

/* compiled from: UploadBufferBuilder */
public class k extends g {
    public k() {
        super(FujifilmMakernoteDirectory.TAG_DYNAMIC_RANGE);
    }

    private byte[] a(Context context, CollectionConfig collectionConfig) {
        super.a();
        this.a.h(bl.a(this.a, collectionConfig.getProductId(), this.a.a(az.a(context)), this.a.a(collectionConfig.getProductVersion()), (byte) Build.VERSION.SDK_INT, this.a.a(a.c(context)), this.a.a(collectionConfig.getUtdid()), this.a.a(bd.a(a.a(context))), this.a.a(bd.a(a.d(context))), f.a(a.f(context)), this.a.a(a.c()), this.a.a(a.b()), this.a.a(collectionConfig.getLicense()), this.a.a(collectionConfig.getMapkey())));
        try {
            return aw.a(az.a(context), this.a.f());
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.openapi.ad.a(com.loc.fc, byte[]):int
     arg types: [com.amap.openapi.i, byte[]]
     candidates:
      com.amap.openapi.ad.a(com.loc.fc, int[]):int
      com.amap.openapi.ad.a(com.loc.fc, int):void
      com.amap.openapi.ad.a(com.loc.fc, byte[]):int */
    @Nullable
    public byte[] a(@NonNull Context context, @NonNull CollectionConfig collectionConfig, @NonNull au auVar) {
        try {
            byte[] a = bb.a(aw.a(az.a(context)));
            byte[] a2 = a(context, collectionConfig);
            int size = auVar.b.size();
            if (size > 0 && a != null) {
                a();
                int a3 = ad.a((fc) this.a, a);
                int[] iArr = new int[size];
                for (int i = 0; i < size; i++) {
                    s sVar = auVar.b.get(i);
                    iArr[i] = ai.a(this.a, (byte) sVar.b(), ai.a(this.a, sVar.c()));
                }
                this.a.h(ad.a(this.a, a3, a2 != null ? ad.b(this.a, a2) : 0, ad.a(this.a, iArr)));
                return d.a(this.a.f());
            }
        } catch (Throwable th) {
        }
        return null;
    }
}
