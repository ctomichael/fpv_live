package com.loc;

import android.content.Context;
import java.util.Iterator;
import java.util.List;

/* compiled from: SDKDBOperation */
public final class ba {
    private av a;
    private Context b;

    public ba(Context context, boolean z) {
        this.b = context;
        this.a = a(this.b, z);
    }

    private static av a(Context context, boolean z) {
        try {
            return new av(context, av.a((Class<? extends au>) az.class));
        } catch (Throwable th) {
            if (!z) {
                aq.b(th, "sd", "gdb");
            }
            return null;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.av.a(java.lang.String, java.lang.Class, boolean):java.util.List<T>
     arg types: [java.lang.String, java.lang.Class, int]
     candidates:
      com.loc.av.a(android.database.Cursor, java.lang.Class, com.loc.aw):T
      com.loc.av.a(java.lang.String, java.lang.Class, boolean):java.util.List<T> */
    public final List<ac> a() {
        try {
            return this.a.a(ac.g(), ac.class, true);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.av.a(java.lang.String, java.lang.Class, boolean):java.util.List<T>
     arg types: [java.lang.String, java.lang.Class, int]
     candidates:
      com.loc.av.a(android.database.Cursor, java.lang.Class, com.loc.aw):T
      com.loc.av.a(java.lang.String, java.lang.Class, boolean):java.util.List<T> */
    public final void a(ac acVar) {
        boolean z;
        if (acVar != null) {
            try {
                if (this.a == null) {
                    this.a = a(this.b, false);
                }
                String a2 = ac.a(acVar.a());
                List a3 = this.a.a(a2, ac.class, false);
                if (a3.size() == 0) {
                    this.a.a(acVar);
                    return;
                }
                Iterator it2 = a3.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        if (((ac) it2.next()).equals(acVar)) {
                            z = false;
                            break;
                        }
                    } else {
                        z = true;
                        break;
                    }
                }
                if (z) {
                    this.a.a(a2, acVar);
                }
            } catch (Throwable th) {
                aq.b(th, "sd", "it");
            }
        }
    }
}
