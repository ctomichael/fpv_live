package com.amap.location.offline.upload;

import android.content.Context;
import com.amap.location.common.HeaderConfig;
import com.amap.location.offline.IOfflineCloudConfig;
import com.amap.location.offline.OfflineConfig;
import com.amap.openapi.dk;
import com.amap.openapi.dl;
import com.amap.openapi.dp;
import com.amap.openapi.dq;
import com.mapzen.android.lost.internal.FusionEngine;

/* compiled from: UpTunnelWrapper */
public class a {
    public static void a(int i) {
        dl.a(i);
    }

    public static void a(int i, byte[] bArr) {
        dl.a(i, bArr);
    }

    public static void a(Context context, final OfflineConfig offlineConfig, IOfflineCloudConfig iOfflineCloudConfig) {
        if (offlineConfig.productId == 4 && offlineConfig.locEnable && iOfflineCloudConfig.isEnable()) {
            HeaderConfig.setProductId((byte) 4);
            HeaderConfig.setProductVerion(offlineConfig.productVersion);
            HeaderConfig.setProcessName(offlineConfig.packageName);
            com.amap.location.common.a.b(context, offlineConfig.adiu);
            HeaderConfig.setMapkey(offlineConfig.mapKey);
            HeaderConfig.setLicense(offlineConfig.license);
            dk dkVar = new dk();
            dkVar.f = offlineConfig.httpClient;
            dkVar.b = new dq() {
                /* class com.amap.location.offline.upload.a.AnonymousClass1 */

                public final int a() {
                    return 10;
                }

                public final long a(int i) {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.sizePerRequest;
                    }
                    return 1000;
                }

                public final long b(int i) {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.maxSizePerDay;
                    }
                    return 5000;
                }

                public final void b() {
                }

                public final long c() {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.bufferSize;
                    }
                    return 100;
                }

                public final boolean c(int i) {
                    if (i == 1) {
                        return true;
                    }
                    if (i != 0 || offlineConfig.uploadConfig == null) {
                        return false;
                    }
                    return offlineConfig.uploadConfig.nonWifiEnable;
                }

                public final long d() {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.uploadPeriod;
                    }
                    return 300000;
                }

                public final long e() {
                    return offlineConfig.uploadConfig != null ? offlineConfig.uploadConfig.storePeriod : FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS;
                }

                public final int f() {
                    return 10000;
                }

                public final long g() {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.maxDbSize;
                    }
                    return 100000;
                }

                public final long h() {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.expireTimeInDb;
                    }
                    return 864000000;
                }
            };
            dkVar.a = new dp() {
                /* class com.amap.location.offline.upload.a.AnonymousClass2 */

                public final long a() {
                    return 10;
                }

                public final long a(int i) {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.sizePerRequest;
                    }
                    return 1000;
                }

                public final long b(int i) {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.maxSizePerDay;
                    }
                    return 5000;
                }

                public final void b() {
                }

                public final long c() {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.bufferSize;
                    }
                    return 100;
                }

                public final boolean c(int i) {
                    if (i == 1) {
                        return true;
                    }
                    if (i != 0 || offlineConfig.uploadConfig == null) {
                        return false;
                    }
                    return offlineConfig.uploadConfig.nonWifiEnable;
                }

                public final long d() {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.uploadPeriod;
                    }
                    return 300000;
                }

                public final long e() {
                    return offlineConfig.uploadConfig != null ? offlineConfig.uploadConfig.storePeriod : FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS;
                }

                public final int f() {
                    return 10000;
                }

                public final long g() {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.maxDbSize;
                    }
                    return 100000;
                }

                public final long h() {
                    if (offlineConfig.uploadConfig != null) {
                        return offlineConfig.uploadConfig.expireTimeInDb;
                    }
                    return 864000000;
                }
            };
            dl.a(context, dkVar);
        }
    }

    public static void a(OfflineConfig offlineConfig) {
        if (offlineConfig != null && offlineConfig.productId == 4) {
            dl.a();
        }
    }
}
