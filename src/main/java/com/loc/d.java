package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.List;

/* compiled from: AdiuStorageModel */
public final class d {
    private static d e;
    private List<String> a;
    /* access modifiers changed from: private */
    public String b;
    /* access modifiers changed from: private */
    public final Context c;
    private final Handler d;

    /* compiled from: AdiuStorageModel */
    private static final class a extends Handler {
        private final WeakReference<d> a;

        a(Looper looper, d dVar) {
            super(looper);
            this.a = new WeakReference<>(dVar);
        }

        a(d dVar) {
            this.a = new WeakReference<>(dVar);
        }

        public final void handleMessage(Message message) {
            d dVar = this.a.get();
            if (dVar != null && message != null && message.obj != null) {
                dVar.a((String) message.obj, message.what);
            }
        }
    }

    private d(Context context) {
        this.c = context.getApplicationContext();
        if (Looper.myLooper() == null) {
            this.d = new a(Looper.getMainLooper(), this);
        } else {
            this.d = new a(this);
        }
    }

    public static d a(Context context) {
        if (e == null) {
            synchronized (d.class) {
                if (e == null) {
                    e = new d(context);
                }
            }
        }
        return e;
    }

    /* access modifiers changed from: private */
    public synchronized void a(final String str, final int i) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread() {
                /* class com.loc.d.AnonymousClass1 */

                public final void run() {
                    String b2 = i.b(str);
                    if (!TextUtils.isEmpty(b2)) {
                        if ((i & 1) > 0) {
                            try {
                                if (Build.VERSION.SDK_INT >= 23) {
                                    Settings.System.putString(d.this.c.getContentResolver(), d.this.b, b2);
                                } else {
                                    Settings.System.putString(d.this.c.getContentResolver(), d.this.b, b2);
                                }
                            } catch (Exception e) {
                            }
                        }
                        if ((i & 16) > 0) {
                            e.a(d.this.c, d.this.b, b2);
                        }
                        if ((i & 256) > 0) {
                            SharedPreferences.Editor edit = d.this.c.getSharedPreferences("SharedPreferenceAdiu", 0).edit();
                            edit.putString(d.this.b, b2);
                            if (Build.VERSION.SDK_INT >= 9) {
                                edit.apply();
                            } else {
                                edit.commit();
                            }
                        }
                    }
                }
            }.start();
        } else {
            String b2 = i.b(str);
            if (!TextUtils.isEmpty(b2)) {
                if ((i & 1) > 0) {
                    try {
                        if (Build.VERSION.SDK_INT >= 23) {
                            Settings.System.putString(this.c.getContentResolver(), this.b, b2);
                        } else {
                            Settings.System.putString(this.c.getContentResolver(), this.b, b2);
                        }
                    } catch (Exception e2) {
                    }
                }
                if ((i & 16) > 0) {
                    e.a(this.c, this.b, b2);
                }
                if ((i & 256) > 0) {
                    SharedPreferences.Editor edit = this.c.getSharedPreferences("SharedPreferenceAdiu", 0).edit();
                    edit.putString(this.b, b2);
                    if (Build.VERSION.SDK_INT >= 9) {
                        edit.apply();
                    } else {
                        edit.commit();
                    }
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0092  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<java.lang.String> b() {
        /*
            r7 = this;
            r5 = 0
            r3 = 0
            java.lang.String r0 = ""
            android.content.Context r1 = r7.c     // Catch:{ Exception -> 0x00d8 }
            android.content.ContentResolver r1 = r1.getContentResolver()     // Catch:{ Exception -> 0x00d8 }
            java.lang.String r2 = r7.b     // Catch:{ Exception -> 0x00d8 }
            java.lang.String r1 = android.provider.Settings.System.getString(r1, r2)     // Catch:{ Exception -> 0x00d8 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x00d8 }
            if (r2 != 0) goto L_0x001b
            java.lang.String r0 = com.loc.i.c(r1)     // Catch:{ Exception -> 0x00d8 }
        L_0x001b:
            java.lang.String r1 = ""
            android.content.Context r2 = r7.c
            java.lang.String r4 = r7.b
            java.lang.String r2 = com.loc.e.a(r2, r4)
            boolean r4 = android.text.TextUtils.isEmpty(r2)
            if (r4 != 0) goto L_0x0030
            java.lang.String r1 = com.loc.i.c(r2)
        L_0x0030:
            java.lang.String r2 = ""
            android.content.Context r4 = r7.c
            java.lang.String r6 = "SharedPreferenceAdiu"
            android.content.SharedPreferences r4 = r4.getSharedPreferences(r6, r3)
            java.lang.String r6 = r7.b
            java.lang.String r4 = r4.getString(r6, r5)
            boolean r6 = android.text.TextUtils.isEmpty(r4)
            if (r6 != 0) goto L_0x004c
            java.lang.String r2 = com.loc.i.c(r4)
        L_0x004c:
            java.util.ArrayList r4 = new java.util.ArrayList
            r6 = 3
            r4.<init>(r6)
            boolean r6 = android.text.TextUtils.isEmpty(r0)
            if (r6 != 0) goto L_0x0095
            r4.add(r0)
            boolean r5 = android.text.TextUtils.isEmpty(r1)
            if (r5 != 0) goto L_0x006a
            boolean r5 = android.text.TextUtils.equals(r1, r0)
            if (r5 != 0) goto L_0x006c
            r4.add(r1)
        L_0x006a:
            r3 = 16
        L_0x006c:
            boolean r5 = android.text.TextUtils.isEmpty(r2)
            if (r5 != 0) goto L_0x0092
            boolean r5 = android.text.TextUtils.equals(r2, r0)
            if (r5 != 0) goto L_0x0083
            boolean r1 = android.text.TextUtils.equals(r2, r1)
            if (r1 != 0) goto L_0x0081
            r4.add(r2)
        L_0x0081:
            r3 = r3 | 256(0x100, float:3.59E-43)
        L_0x0083:
            if (r3 <= 0) goto L_0x0090
            android.os.Handler r1 = r7.d
            android.os.Handler r2 = r7.d
            android.os.Message r0 = r2.obtainMessage(r3, r0)
            r1.sendMessage(r0)
        L_0x0090:
            r0 = r4
        L_0x0091:
            return r0
        L_0x0092:
            r3 = r3 | 256(0x100, float:3.59E-43)
            goto L_0x0083
        L_0x0095:
            boolean r0 = android.text.TextUtils.isEmpty(r1)
            if (r0 != 0) goto L_0x00be
            r4.add(r1)
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto L_0x00ad
            boolean r0 = android.text.TextUtils.equals(r2, r1)
            if (r0 != 0) goto L_0x00af
            r4.add(r2)
        L_0x00ad:
            r3 = 256(0x100, float:3.59E-43)
        L_0x00af:
            r0 = r3 | 1
            android.os.Handler r2 = r7.d
            android.os.Handler r3 = r7.d
            android.os.Message r0 = r3.obtainMessage(r0, r1)
            r2.sendMessage(r0)
            r0 = r4
            goto L_0x0091
        L_0x00be:
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto L_0x00d6
            r4.add(r2)
            android.os.Handler r0 = r7.d
            android.os.Handler r1 = r7.d
            r3 = 17
            android.os.Message r1 = r1.obtainMessage(r3, r2)
            r0.sendMessage(r1)
            r0 = r4
            goto L_0x0091
        L_0x00d6:
            r0 = r5
            goto L_0x0091
        L_0x00d8:
            r1 = move-exception
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.d.b():java.util.List");
    }

    public final List<String> a() {
        if (this.a != null && this.a.size() > 0 && !TextUtils.isEmpty(this.a.get(0))) {
            return this.a;
        }
        this.a = b();
        return this.a;
    }

    public final void a(String str) {
        this.b = str;
    }

    public final void b(String str) {
        if (this.a != null) {
            this.a.clear();
            this.a.add(str);
        }
        a(str, 273);
    }
}
