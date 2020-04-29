package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import com.loc.dy;
import java.io.File;
import java.util.Map;

/* compiled from: PersistentConfiguration */
public final class dz {
    private String a;
    private String b;
    private boolean c;
    private boolean d;
    private boolean e;
    private SharedPreferences f;
    private dy g;
    private SharedPreferences.Editor h;
    private dy.a i;
    private Context j;
    private ea k;
    private boolean l;

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0072 A[SYNTHETIC, Splitter:B:17:0x0072] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00e6 A[Catch:{ Exception -> 0x016c }] */
    /* JADX WARNING: Removed duplicated region for block: B:91:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public dz(android.content.Context r11, java.lang.String r12, java.lang.String r13) {
        /*
            r10 = this;
            r7 = 1
            r1 = 0
            r6 = 0
            r2 = 0
            r10.<init>()
            java.lang.String r0 = ""
            r10.a = r0
            java.lang.String r0 = ""
            r10.b = r0
            r10.c = r6
            r10.d = r6
            r10.e = r6
            r10.f = r1
            r10.g = r1
            r10.h = r1
            r10.i = r1
            r10.j = r1
            r10.k = r1
            r10.l = r6
            r10.c = r6
            r10.l = r7
            r10.a = r13
            r10.b = r12
            r10.j = r11
            if (r11 == 0) goto L_0x0182
            android.content.SharedPreferences r0 = r11.getSharedPreferences(r13, r6)
            r10.f = r0
            android.content.SharedPreferences r0 = r10.f
            java.lang.String r1 = "t"
            long r0 = r0.getLong(r1, r2)
        L_0x0041:
            java.lang.String r4 = android.os.Environment.getExternalStorageState()
            boolean r5 = com.loc.dw.a(r4)
            if (r5 != 0) goto L_0x0105
            java.lang.String r5 = "mounted"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L_0x00f6
            r10.e = r7
            r10.d = r7
        L_0x0058:
            boolean r4 = r10.d
            if (r4 != 0) goto L_0x0060
            boolean r4 = r10.e
            if (r4 == 0) goto L_0x017e
        L_0x0060:
            if (r11 == 0) goto L_0x017e
            boolean r4 = com.loc.dw.a(r12)
            if (r4 != 0) goto L_0x017e
            com.loc.ea r4 = r10.b(r12)
            r10.k = r4
            com.loc.ea r4 = r10.k
            if (r4 == 0) goto L_0x017e
            com.loc.ea r4 = r10.k     // Catch:{ Exception -> 0x016e }
            com.loc.dy r4 = r4.a(r13)     // Catch:{ Exception -> 0x016e }
            r10.g = r4     // Catch:{ Exception -> 0x016e }
            com.loc.dy r4 = r10.g     // Catch:{ Exception -> 0x016e }
            java.lang.String r5 = "t"
            long r4 = r4.a(r5)     // Catch:{ Exception -> 0x016e }
            android.content.SharedPreferences r6 = r10.f     // Catch:{ Exception -> 0x0173 }
            java.lang.String r7 = "t2"
            r8 = 0
            long r6 = r6.getLong(r7, r8)     // Catch:{ Exception -> 0x0173 }
            com.loc.dy r0 = r10.g     // Catch:{ Exception -> 0x0177 }
            java.lang.String r1 = "t2"
            long r0 = r0.a(r1)     // Catch:{ Exception -> 0x0177 }
            int r4 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r4 >= 0) goto L_0x010b
            int r4 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r4 <= 0) goto L_0x010b
            android.content.SharedPreferences r4 = r10.f     // Catch:{ Exception -> 0x017a }
            com.loc.dy r5 = r10.g     // Catch:{ Exception -> 0x017a }
            a(r4, r5)     // Catch:{ Exception -> 0x017a }
            com.loc.ea r4 = r10.k     // Catch:{ Exception -> 0x017a }
            com.loc.dy r4 = r4.a(r13)     // Catch:{ Exception -> 0x017a }
            r10.g = r4     // Catch:{ Exception -> 0x017a }
            r4 = r0
        L_0x00af:
            int r0 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x00bb
            int r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x00f5
            int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x00f5
        L_0x00bb:
            long r0 = java.lang.System.currentTimeMillis()
            boolean r8 = r10.l
            if (r8 == 0) goto L_0x00cf
            boolean r8 = r10.l
            if (r8 == 0) goto L_0x00f5
            int r6 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r6 != 0) goto L_0x00f5
            int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r2 != 0) goto L_0x00f5
        L_0x00cf:
            android.content.SharedPreferences r2 = r10.f
            if (r2 == 0) goto L_0x00e2
            android.content.SharedPreferences r2 = r10.f
            android.content.SharedPreferences$Editor r2 = r2.edit()
            java.lang.String r3 = "t2"
            r2.putLong(r3, r0)
            r2.commit()
        L_0x00e2:
            com.loc.dy r2 = r10.g     // Catch:{ Exception -> 0x016c }
            if (r2 == 0) goto L_0x00f5
            com.loc.dy r2 = r10.g     // Catch:{ Exception -> 0x016c }
            com.loc.dy$a r2 = r2.c()     // Catch:{ Exception -> 0x016c }
            java.lang.String r3 = "t2"
            r2.a(r3, r0)     // Catch:{ Exception -> 0x016c }
            r2.b()     // Catch:{ Exception -> 0x016c }
        L_0x00f5:
            return
        L_0x00f6:
            java.lang.String r5 = "mounted_ro"
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L_0x0105
            r10.d = r7
            r10.e = r6
            goto L_0x0058
        L_0x0105:
            r10.e = r6
            r10.d = r6
            goto L_0x0058
        L_0x010b:
            int r4 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r4 <= 0) goto L_0x0123
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 <= 0) goto L_0x0123
            com.loc.dy r4 = r10.g     // Catch:{ Exception -> 0x017a }
            android.content.SharedPreferences r5 = r10.f     // Catch:{ Exception -> 0x017a }
            a(r4, r5)     // Catch:{ Exception -> 0x017a }
            r4 = 0
            android.content.SharedPreferences r4 = r11.getSharedPreferences(r13, r4)     // Catch:{ Exception -> 0x017a }
            r10.f = r4     // Catch:{ Exception -> 0x017a }
            r4 = r0
            goto L_0x00af
        L_0x0123:
            int r4 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x013c
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 <= 0) goto L_0x013c
            com.loc.dy r4 = r10.g     // Catch:{ Exception -> 0x017a }
            android.content.SharedPreferences r5 = r10.f     // Catch:{ Exception -> 0x017a }
            a(r4, r5)     // Catch:{ Exception -> 0x017a }
            r4 = 0
            android.content.SharedPreferences r4 = r11.getSharedPreferences(r13, r4)     // Catch:{ Exception -> 0x017a }
            r10.f = r4     // Catch:{ Exception -> 0x017a }
            r4 = r0
            goto L_0x00af
        L_0x013c:
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x0156
            int r4 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r4 <= 0) goto L_0x0156
            android.content.SharedPreferences r4 = r10.f     // Catch:{ Exception -> 0x017a }
            com.loc.dy r5 = r10.g     // Catch:{ Exception -> 0x017a }
            a(r4, r5)     // Catch:{ Exception -> 0x017a }
            com.loc.ea r4 = r10.k     // Catch:{ Exception -> 0x017a }
            com.loc.dy r4 = r4.a(r13)     // Catch:{ Exception -> 0x017a }
            r10.g = r4     // Catch:{ Exception -> 0x017a }
            r4 = r0
            goto L_0x00af
        L_0x0156:
            int r4 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r4 != 0) goto L_0x0169
            android.content.SharedPreferences r4 = r10.f     // Catch:{ Exception -> 0x017a }
            com.loc.dy r5 = r10.g     // Catch:{ Exception -> 0x017a }
            a(r4, r5)     // Catch:{ Exception -> 0x017a }
            com.loc.ea r4 = r10.k     // Catch:{ Exception -> 0x017a }
            com.loc.dy r4 = r4.a(r13)     // Catch:{ Exception -> 0x017a }
            r10.g = r4     // Catch:{ Exception -> 0x017a }
        L_0x0169:
            r4 = r0
            goto L_0x00af
        L_0x016c:
            r0 = move-exception
            goto L_0x00f5
        L_0x016e:
            r4 = move-exception
            r4 = r2
            r6 = r0
            goto L_0x00af
        L_0x0173:
            r6 = move-exception
            r6 = r0
            goto L_0x00af
        L_0x0177:
            r0 = move-exception
            goto L_0x00af
        L_0x017a:
            r4 = move-exception
            r4 = r0
            goto L_0x00af
        L_0x017e:
            r4 = r2
            r6 = r0
            goto L_0x00af
        L_0x0182:
            r0 = r2
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.dz.<init>(android.content.Context, java.lang.String, java.lang.String):void");
    }

    private static void a(SharedPreferences sharedPreferences, dy dyVar) {
        if (sharedPreferences != null && dyVar != null) {
            dy.a c2 = dyVar.c();
            c2.a();
            for (Map.Entry entry : sharedPreferences.getAll().entrySet()) {
                String str = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    c2.a(str, (String) value);
                } else if (value instanceof Integer) {
                    c2.a(str, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    c2.a(str, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    c2.a(str, ((Float) value).floatValue());
                } else if (value instanceof Boolean) {
                    c2.a(str, ((Boolean) value).booleanValue());
                }
            }
            c2.b();
        }
    }

    private static void a(dy dyVar, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit;
        if (dyVar != null && sharedPreferences != null && (edit = sharedPreferences.edit()) != null) {
            edit.clear();
            for (Map.Entry entry : dyVar.b().entrySet()) {
                String str = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    edit.putString(str, (String) value);
                } else if (value instanceof Integer) {
                    edit.putInt(str, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    edit.putLong(str, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    edit.putFloat(str, ((Float) value).floatValue());
                } else if (value instanceof Boolean) {
                    edit.putBoolean(str, ((Boolean) value).booleanValue());
                }
            }
            edit.commit();
        }
    }

    private ea b(String str) {
        File file;
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            file = new File(String.format("%s%s%s", externalStorageDirectory.getAbsolutePath(), File.separator, str));
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            file = null;
        }
        if (file == null) {
            return null;
        }
        this.k = new ea(file.getAbsolutePath());
        return this.k;
    }

    private boolean b() {
        if (this.g == null) {
            return false;
        }
        boolean a2 = this.g.a();
        if (a2) {
            return a2;
        }
        a();
        return a2;
    }

    private void c() {
        if (this.h == null && this.f != null) {
            this.h = this.f.edit();
        }
        if (this.e && this.i == null && this.g != null) {
            this.i = this.g.c();
        }
        b();
    }

    public final String a(String str) {
        b();
        if (this.f != null) {
            String string = this.f.getString(str, "");
            if (!dw.a(string)) {
                return string;
            }
        }
        return this.g != null ? this.g.a(str, "") : "";
    }

    public final void a(String str, long j2) {
        if (!dw.a(str) && !str.equals("t")) {
            c();
            if (this.h != null) {
                this.h.putLong(str, j2);
            }
            if (this.i != null) {
                this.i.a(str, j2);
            }
        }
    }

    public final void a(String str, String str2) {
        if (!dw.a(str) && !str.equals("t")) {
            c();
            if (this.h != null) {
                this.h.putString(str, str2);
            }
            if (this.i != null) {
                this.i.a(str, str2);
            }
        }
    }

    public final boolean a() {
        boolean z = true;
        long currentTimeMillis = System.currentTimeMillis();
        if (this.h != null) {
            if (!this.l && this.f != null) {
                this.h.putLong("t", currentTimeMillis);
            }
            if (!this.h.commit()) {
                z = false;
            }
        }
        if (!(this.f == null || this.j == null)) {
            this.f = this.j.getSharedPreferences(this.a, 0);
        }
        String externalStorageState = Environment.getExternalStorageState();
        if (!dw.a(externalStorageState)) {
            if (externalStorageState.equals("mounted")) {
                if (this.g == null) {
                    ea b2 = b(this.b);
                    if (b2 != null) {
                        this.g = b2.a(this.a);
                        if (!this.l) {
                            a(this.f, this.g);
                        } else {
                            a(this.g, this.f);
                        }
                        this.i = this.g.c();
                    }
                } else if (this.i != null && !this.i.b()) {
                    z = false;
                }
            }
            if (externalStorageState.equals("mounted") || (externalStorageState.equals("mounted_ro") && this.g != null)) {
                try {
                    if (this.k != null) {
                        this.g = this.k.a(this.a);
                    }
                } catch (Exception e2) {
                }
            }
        }
        return z;
    }
}
