package com.dji.analytics.b;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.dji.analytics.DJIA;
import com.dji.analytics.ReportConfig;
import dji.publics.LogReport.base.Fields;
import dji.publics.protocol.ResponseBase;
import java.util.HashMap;

/* compiled from: ReportDbHelper */
public class a {
    private b a;
    private ReportConfig b;

    /* renamed from: com.dji.analytics.b.a$a  reason: collision with other inner class name */
    /* compiled from: ReportDbHelper */
    private static final class C0022a {
        /* access modifiers changed from: private */
        public static final a a = new a();
    }

    public static a a() {
        return C0022a.a;
    }

    private a() {
        this.a = null;
        this.b = null;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Integer):void}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Byte):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Float):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.String):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Long):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Boolean):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, byte[]):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Double):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Short):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Integer):void} */
    public void b() {
        if (this.a != null) {
            synchronized (this) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ResponseBase.STRING_STATE, (Integer) 0);
                this.a.a("dji_analytics_reports", contentValues, "state = 1", null);
            }
        }
    }

    public void a(Context context) {
        this.a = new b(context);
        this.b = DJIA.getConfig();
        if (DJIA.DEV_FLAG) {
            DJIA.log.a(DJIA.LOG_TAG, a.class.getSimpleName() + "init success");
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Integer):void}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Byte):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Float):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.String):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Long):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Boolean):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, byte[]):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Double):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Short):void}
      ClspMth{android.content.ContentValues.put(java.lang.String, java.lang.Integer):void} */
    public synchronized boolean a(com.dji.analytics.a.a aVar) {
        boolean z = false;
        synchronized (this) {
            if (this.a != null) {
                ContentValues contentValues = new ContentValues();
                aVar.e();
                contentValues.put(Fields.Dgo_appset.EVENT_IS_ENABLE_REPORT, aVar.c());
                contentValues.put("reportid", aVar.b());
                contentValues.put(ResponseBase.STRING_STATE, (Integer) 0);
                z = this.a.a("dji_analytics_reports", (String) null, contentValues);
            }
        }
        return z;
    }

    public synchronized boolean a(HashMap<String, com.dji.analytics.a.a> hashMap) {
        boolean z = false;
        synchronized (this) {
            if (hashMap.size() > 0) {
                if (this.a != null) {
                    Object[] array = hashMap.keySet().toArray();
                    String[] strArr = new String[array.length];
                    for (int i = 0; i < array.length; i++) {
                        strArr[i] = (String) array[i];
                    }
                    z = this.a.a("dji_analytics_reports", "reportid in (" + a(strArr) + ")", (String[]) null);
                }
            }
        }
        return z;
    }

    public synchronized boolean c() {
        return this.a != null && this.a.a("dji_analytics_reports", "", null);
    }

    public synchronized HashMap<String, com.dji.analytics.a.a> d() {
        HashMap<String, com.dji.analytics.a.a> hashMap = null;
        synchronized (this) {
            if (this.a != null) {
                String str = "select reportid,report from dji_analytics_reports where state = 0 limit " + this.b.getSendItemCount() + " offset 0";
                synchronized (this) {
                    Cursor a2 = this.a.a(str);
                    if (a2 != null) {
                        int count = a2.getCount();
                        if (count > 0) {
                            hashMap = new HashMap<>();
                            String[] strArr = new String[count];
                            for (int i = 0; i < count; i++) {
                                String string = a2.getString(a2.getColumnIndex("reportid"));
                                com.dji.analytics.a.a aVar = new com.dji.analytics.a.a(a2.getBlob(a2.getColumnIndex(Fields.Dgo_appset.EVENT_IS_ENABLE_REPORT)));
                                aVar.d();
                                hashMap.put(string, aVar);
                                strArr[i] = string;
                                if (a2.isLast()) {
                                    break;
                                }
                                a2.moveToNext();
                            }
                            a(strArr, 1);
                            if (!a2.isClosed()) {
                                a2.close();
                            }
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    private static String a(String[] strArr) {
        if (strArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(strArr[0]);
        for (int i = 1; i < strArr.length; i++) {
            sb.append("','");
            sb.append(strArr[i]);
        }
        sb.append("'");
        return sb.toString();
    }

    private boolean a(String[] strArr, int i) {
        boolean a2;
        if (this.a == null) {
            return false;
        }
        synchronized (this) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ResponseBase.STRING_STATE, Integer.valueOf(i));
            a2 = this.a.a("dji_analytics_reports", contentValues, "reportid in (" + a(strArr) + ")", null);
        }
        return a2;
    }
}
