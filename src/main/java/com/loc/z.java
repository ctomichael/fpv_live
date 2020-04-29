package com.loc;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

/* compiled from: HttpsDecisionUtil */
public final class z {
    private volatile b a = new b((byte) 0);
    private bb b = new bb("HttpsDecisionUtil");

    /* compiled from: HttpsDecisionUtil */
    private static class a {
        static z a = new z();
    }

    /* compiled from: HttpsDecisionUtil */
    private static class b {
        protected boolean a;
        private int b;
        private final boolean c;
        private boolean d;

        private b() {
            this.b = 0;
            this.a = true;
            this.c = true;
            this.d = false;
        }

        /* synthetic */ b(byte b2) {
            this();
        }

        public final void a(Context context) {
            if (context != null && this.b <= 0 && Build.VERSION.SDK_INT >= 4) {
                this.b = context.getApplicationContext().getApplicationInfo().targetSdkVersion;
            }
        }

        public final void a(boolean z) {
            this.a = z;
        }

        /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean a() {
            /*
                r5 = this;
                r4 = 28
                r1 = 1
                r2 = 0
                boolean r0 = r5.d
                if (r0 != 0) goto L_0x0023
                int r0 = android.os.Build.VERSION.SDK_INT
                if (r0 < r4) goto L_0x0025
                r0 = r1
            L_0x000d:
                boolean r3 = r5.a
                if (r3 == 0) goto L_0x001b
                int r3 = r5.b
                if (r3 > 0) goto L_0x0027
                r3 = r4
            L_0x0016:
                if (r3 < r4) goto L_0x002a
                r3 = r1
            L_0x0019:
                if (r3 == 0) goto L_0x002c
            L_0x001b:
                r3 = r1
            L_0x001c:
                if (r0 == 0) goto L_0x002e
                if (r3 == 0) goto L_0x002e
                r0 = r1
            L_0x0021:
                if (r0 == 0) goto L_0x0024
            L_0x0023:
                r2 = r1
            L_0x0024:
                return r2
            L_0x0025:
                r0 = r2
                goto L_0x000d
            L_0x0027:
                int r3 = r5.b
                goto L_0x0016
            L_0x002a:
                r3 = r2
                goto L_0x0019
            L_0x002c:
                r3 = r2
                goto L_0x001c
            L_0x002e:
                r0 = r2
                goto L_0x0021
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.z.b.a():boolean");
        }

        public final void b(boolean z) {
            this.d = z;
        }
    }

    public static z a() {
        return a.a;
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str) || str.startsWith("https")) {
            return str;
        }
        try {
            Uri.Builder buildUpon = Uri.parse(str).buildUpon();
            buildUpon.scheme("https");
            return buildUpon.build().toString();
        } catch (Throwable th) {
            return str;
        }
    }

    public static boolean b() {
        return Build.VERSION.SDK_INT == 19;
    }

    public final void a(Context context) {
        if (this.a == null) {
            this.a = new b((byte) 0);
        }
        this.a.a(this.b.a(context, "isTargetRequired"));
        this.a.a(context);
    }

    /* access modifiers changed from: package-private */
    public final void a(Context context, boolean z) {
        if (this.a == null) {
            this.a = new b((byte) 0);
        }
        this.b.a(context, "isTargetRequired", z);
        this.a.a(z);
    }

    public final void a(boolean z) {
        if (this.a == null) {
            this.a = new b((byte) 0);
        }
        this.a.b(z);
    }

    public final void b(Context context) {
        this.b.a(context, "isTargetRequired", true);
    }

    public final boolean b(boolean z) {
        if (b()) {
            return false;
        }
        if (!z) {
            if (this.a == null) {
                this.a = new b((byte) 0);
            }
            return this.a.a();
        }
    }
}
