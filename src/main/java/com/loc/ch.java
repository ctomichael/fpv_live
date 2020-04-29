package com.loc;

import android.content.Context;
import android.os.Build;
import com.amap.location.collection.CollectionConfig;
import com.amap.location.collection.CollectionManagerProxy;
import com.amap.location.common.network.IHttpClient;
import dji.midware.Lifecycle;

/* compiled from: CollectionManager */
public final class ch {
    private Context a = null;
    private CollectionManagerProxy b;

    public ch(Context context) {
        this.a = context.getApplicationContext();
    }

    private CollectionConfig a(ci ciVar) {
        CollectionConfig collectionConfig = new CollectionConfig();
        collectionConfig.setProductId((byte) 4);
        if (ciVar != null) {
            try {
                collectionConfig.setProductVersion(ciVar.b());
                collectionConfig.setLicense(ciVar.c());
                collectionConfig.setMapkey(ciVar.d());
                collectionConfig.setUtdid(ciVar.g());
            } catch (Throwable th) {
                cm.a(th, "", "");
            }
        } else {
            collectionConfig.setProductVersion("");
            collectionConfig.setLicense("S128DF1572465B890OE3F7A13167KLEI");
            collectionConfig.setMapkey(u.f(this.a));
            collectionConfig.setUtdid(x.g(this.a));
        }
        collectionConfig.getFpsCollectorConfig().setEnabled(true);
        collectionConfig.getFpsCollectorConfig().setScanActiveAllowed(false);
        collectionConfig.getTrackCollectorConfig().setEnabled(false);
        collectionConfig.getUploadConfig().setUploadWithLocatorEnabled(false);
        collectionConfig.getUploadConfig().setNonWifiUploadEnabled(false);
        collectionConfig.getUploadConfig().setMaxUploadFailCount(0);
        collectionConfig.getUploadConfig().setMaxMobileUploadSizePerDay(0);
        collectionConfig.getUploadConfig().setMaxWifiUploadSizePerDay(10485760);
        if (Build.VERSION.SDK_INT >= 28) {
            collectionConfig.getFpsCollectorConfig().setScanWifiAllowed(false);
        }
        collectionConfig.setStopCollectionWhenScreenOff(true);
        return collectionConfig;
    }

    public final void a() {
        try {
            if (this.b != null) {
                this.b.destroy();
            }
        } catch (Throwable th) {
            cm.a(th, "CollectionManager", Lifecycle.STATUS_DESTROY);
        }
        this.b = null;
    }

    public final void a(ci ciVar, IHttpClient iHttpClient) {
        try {
            if (this.b == null) {
                this.b = new CollectionManagerProxy();
                this.b.init(this.a, a(ciVar), iHttpClient);
            }
        } catch (Throwable th) {
            cm.a(th, "CollectionManager", "start");
        }
    }
}
