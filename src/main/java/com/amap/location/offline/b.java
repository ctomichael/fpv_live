package com.amap.location.offline;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.FPS;
import com.amap.openapi.co;
import java.util.LinkedList;
import java.util.List;

/* compiled from: OfflineRemoteProxy */
public class b {
    private Context a;
    private IOfflineCloudConfig b;
    private List<String> c = new LinkedList();
    private ProviderInfo d;
    private ContentValues e = new ContentValues();

    public b(Context context, OfflineConfig offlineConfig, IOfflineCloudConfig iOfflineCloudConfig) {
        this.a = context;
        this.b = iOfflineCloudConfig;
        a(offlineConfig, iOfflineCloudConfig);
    }

    private void a() {
        this.d = null;
        if (!this.c.isEmpty()) {
            this.c.remove(0);
        }
    }

    private void a(OfflineConfig offlineConfig, IOfflineCloudConfig iOfflineCloudConfig) {
        int i = 0;
        this.c.clear();
        if (iOfflineCloudConfig != null && iOfflineCloudConfig.getContentProviderList() != null) {
            String[] contentProviderList = iOfflineCloudConfig.getContentProviderList();
            int length = contentProviderList.length;
            while (i < length) {
                this.c.add(contentProviderList[i]);
                i++;
            }
        } else if (offlineConfig != null && offlineConfig.contentProviderList != null) {
            String[] strArr = offlineConfig.contentProviderList;
            int length2 = strArr.length;
            while (i < length2) {
                this.c.add(strArr[i]);
                i++;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0055 A[SYNTHETIC, Splitter:B:27:0x0055] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.amap.openapi.co.a a(com.amap.location.common.model.FPS r8, int r9, java.lang.String r10) {
        /*
            r7 = this;
            r6 = 0
        L_0x0001:
            boolean r0 = r7.a(r10)
            if (r0 == 0) goto L_0x005d
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            java.lang.String r1 = "content://"
            r0.<init>(r1)     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            android.content.pm.ProviderInfo r1 = r7.d     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            java.lang.String r1 = r1.authority     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            android.net.Uri r1 = android.net.Uri.parse(r0)     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            r0 = 0
            java.lang.String[] r4 = com.amap.openapi.co.a(r10, r8, r0, r9)     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            android.content.Context r0 = r7.a     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            android.content.ContentResolver r0 = r0.getContentResolver()     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            r2 = 0
            r3 = 0
            r5 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x0045, all -> 0x0051 }
            com.amap.openapi.co$a r1 = com.amap.openapi.co.a(r0)     // Catch:{ Exception -> 0x0068, all -> 0x0065 }
            if (r0 == 0) goto L_0x006c
            r0.close()     // Catch:{ Exception -> 0x0042 }
            r0 = r1
        L_0x003b:
            if (r0 == 0) goto L_0x0059
            boolean r1 = r0.a
            if (r1 == 0) goto L_0x0059
        L_0x0041:
            return r0
        L_0x0042:
            r0 = move-exception
            r0 = r1
            goto L_0x003b
        L_0x0045:
            r0 = move-exception
            r0 = r6
        L_0x0047:
            if (r0 == 0) goto L_0x006a
            r0.close()     // Catch:{ Exception -> 0x004e }
            r0 = r6
            goto L_0x003b
        L_0x004e:
            r0 = move-exception
            r0 = r6
            goto L_0x003b
        L_0x0051:
            r0 = move-exception
            r1 = r0
        L_0x0053:
            if (r6 == 0) goto L_0x0058
            r6.close()     // Catch:{ Exception -> 0x0063 }
        L_0x0058:
            throw r1
        L_0x0059:
            r7.a()
            goto L_0x0001
        L_0x005d:
            com.amap.openapi.co$a r0 = new com.amap.openapi.co$a
            r0.<init>()
            goto L_0x0041
        L_0x0063:
            r0 = move-exception
            goto L_0x0058
        L_0x0065:
            r1 = move-exception
            r6 = r0
            goto L_0x0053
        L_0x0068:
            r1 = move-exception
            goto L_0x0047
        L_0x006a:
            r0 = r6
            goto L_0x003b
        L_0x006c:
            r0 = r1
            goto L_0x003b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.location.offline.b.a(com.amap.location.common.model.FPS, int, java.lang.String):com.amap.openapi.co$a");
    }

    public void a(OfflineConfig offlineConfig) {
        a(offlineConfig, this.b);
    }

    public boolean a(FPS fps, AmapLoc amapLoc, String str) {
        boolean z = false;
        if (!a(str)) {
            return false;
        }
        try {
            if (this.a.getContentResolver().update(Uri.parse("content://" + this.d.authority), this.e, null, co.a(str, fps, amapLoc, 0)) == 1) {
                z = true;
            }
        } catch (Exception e2) {
        }
        if (z) {
            return true;
        }
        a();
        return a(str);
    }

    public boolean a(String str) {
        if (this.d != null) {
            if (str == null || !str.equals(this.d.packageName)) {
                return true;
            }
            a();
        }
        while (!this.c.isEmpty()) {
            try {
                ProviderInfo resolveContentProvider = this.a.getPackageManager().resolveContentProvider(this.c.get(0), 0);
                if (resolveContentProvider != null && (str == null || !str.equals(resolveContentProvider.packageName))) {
                    this.d = resolveContentProvider;
                    return true;
                }
            } catch (Exception e2) {
            }
            this.c.remove(0);
        }
        return false;
    }
}
