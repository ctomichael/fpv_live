package com.amap.openapi;

import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import com.amap.location.common.network.IHttpClient;
import java.nio.charset.Charset;

/* compiled from: UpTunnelContainer */
public class ds {
    private static Charset a = Charset.forName("UTF-8");
    private ec b;
    private ee c;
    private ee d;
    private ee e;
    private ee f;
    private dt g;
    private IHttpClient h;

    public void a() {
        int c2 = this.g.c();
        if (this.b != null) {
            this.b.b(c2);
        }
        if (this.c != null) {
            this.c.a(c2);
        }
        if (this.e != null) {
            this.e.a(c2);
        }
        if (this.f != null) {
            this.f.a(c2);
        }
        if (this.d != null) {
            this.d.a(c2);
        }
    }

    public void a(Message message) {
        switch (message.arg1) {
            case 1:
                if (this.b != null) {
                    this.b.a(message.arg2);
                    return;
                }
                return;
            case 2:
                if (this.c != null) {
                    this.c.a(message.arg2, (byte[]) message.obj);
                    return;
                }
                return;
            case 3:
                if (this.e != null) {
                    this.e.a(message.arg2, (byte[]) message.obj);
                    return;
                }
                return;
            case 4:
                if (this.f != null) {
                    this.f.a(message.arg2, ((String) message.obj).getBytes(a));
                    return;
                }
                return;
            case 5:
                if (this.d != null) {
                    this.d.a(message.arg2, (byte[]) message.obj);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void a(@NonNull dm dmVar) {
        if ((dmVar instanceof dn) && this.g.c() != -1) {
            dn dnVar = (dn) dmVar;
            switch (dnVar.b()) {
                case 3:
                    if (this.e != null) {
                        this.e.b();
                        break;
                    }
                    break;
                case 4:
                    if (this.f != null) {
                        this.f.b();
                        break;
                    }
                    break;
            }
            new Thread(new Cdo(this.g, dnVar, this.h), "command_thread").start();
        }
    }

    public void a(@NonNull dt dtVar, @NonNull dk dkVar, @NonNull Looper looper) {
        this.g = dtVar;
        this.h = dkVar.f;
        if (dkVar.a != null) {
            this.b = new ec();
            this.b.a(this.g, dkVar.a, this.h, looper);
        }
        if (dkVar.b != null) {
            this.c = new ee();
            this.c.a(this.g, dkVar.b, this.h, 2, looper);
        }
        if (dkVar.c != null) {
            this.d = new ee();
            this.d.a(this.g, dkVar.c, this.h, 5, looper);
        }
        if (dkVar.d != null) {
            this.e = new ee();
            this.e.a(this.g, dkVar.d, this.h, 3, looper);
        }
        if (dkVar.e != null) {
            this.f = new ee();
            this.f.a(this.g, dkVar.e, this.h, 4, looper);
        }
    }

    public void b() {
        if (this.b != null) {
            this.b.a();
        }
        if (this.c != null) {
            this.c.a();
        }
        if (this.e != null) {
            this.e.a();
        }
        if (this.f != null) {
            this.f.a();
        }
        if (this.d != null) {
            this.d.a();
        }
    }
}
