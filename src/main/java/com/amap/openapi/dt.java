package com.amap.openapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import com.amap.location.common.HeaderConfig;
import com.amap.location.common.util.f;
import com.amap.location.uptunnel.core.db.DBProvider;
import dji.publics.LogReport.base.Fields;
import java.util.Calendar;

/* compiled from: UpTunnelContext */
public class dt {
    private Context a;
    private DBProvider b;
    private SharedPreferences c;
    private Calendar d;

    public dt(Context context) {
        this.a = context;
        try {
            this.d = Calendar.getInstance();
            this.c = context.getSharedPreferences(HeaderConfig.getProcessName() + "_tunnel", 0);
            this.b = DBProvider.a(context);
        } catch (Throwable th) {
        }
    }

    public static Uri b(int i) {
        switch (i) {
            case 1:
                return DBProvider.a("count");
            case 2:
                return DBProvider.a("event");
            case 3:
                return DBProvider.a("key_log");
            case 4:
                return DBProvider.a("log");
            case 5:
                return DBProvider.a("data_block");
            default:
                return null;
        }
    }

    public static String c(int i) {
        switch (i) {
            case 1:
                return "count";
            case 2:
                return "event";
            case 3:
                return "key_log";
            case 4:
                return "log";
            case 5:
                return "data_block";
            default:
                return "";
        }
    }

    public synchronized long a(int i, int i2) {
        long j = 0;
        synchronized (this) {
            int i3 = this.c.getInt("last_upload_day_" + i, -1);
            this.d.setTimeInMillis(System.currentTimeMillis());
            int i4 = this.d.get(6);
            if (i4 != i3) {
                SharedPreferences.Editor edit = this.c.edit();
                edit.putInt("last_upload_day_" + i, i4);
                edit.putLong("uploaded_size_" + i + "_" + i2, 0);
                edit.putLong("uploaded_size_" + i + "_" + (i2 == 0 ? 1 : 0), 0);
                edit.apply();
            } else {
                j = this.c.getLong("uploaded_size_" + i + "_" + i2, 0);
            }
        }
        return j;
    }

    public synchronized long a(int i, int i2, long j) {
        int i3 = this.c.getInt("last_upload_day_" + i, -1);
        this.d.setTimeInMillis(System.currentTimeMillis());
        int i4 = this.d.get(6);
        if (i4 != i3) {
            SharedPreferences.Editor edit = this.c.edit();
            edit.putInt("last_upload_day_" + i, i4);
            edit.putLong("uploaded_size_" + i + "_" + i2, j);
            edit.putLong("uploaded_size_" + i + "_" + (i2 == 0 ? 1 : 0), 0);
            edit.apply();
        } else {
            long j2 = this.c.getLong("uploaded_size_" + i + "_" + i2, 0);
            this.c.edit().putLong("uploaded_size_" + i + "_" + i2, j2 + j).apply();
            j += j2;
        }
        return j;
    }

    public synchronized Context a() {
        return this.a;
    }

    public synchronized String a(int i) {
        StringBuilder sb;
        sb = new StringBuilder();
        if (dl.a) {
            sb.append("http://aps.testing.amap.com/dataPipeline/uploadData");
        } else {
            sb.append("http://cgicol.amap.com/dataPipeline/uploadData");
        }
        sb.append("?");
        sb.append("channel=").append(i == 1 ? "statistics" : Fields.Dgo_appset.EVENT_IS_ENABLE_REPORT);
        sb.append("&version=v1");
        return sb.toString();
    }

    public synchronized DBProvider b() {
        return this.b;
    }

    public synchronized int c() {
        return f.a(this.a);
    }
}
