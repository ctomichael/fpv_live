package com.amap.opensdk.co;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import com.amap.location.BasicLocateConfig;
import com.amap.location.BasicLocateManager;
import com.amap.location.collection.CollectionManagerProxy;
import com.loc.aa;
import com.loc.ch;
import com.loc.ci;
import com.loc.cj;
import com.loc.cm;
import com.loc.cp;
import com.loc.cr;
import dji.midware.Lifecycle;
import java.util.Arrays;
import org.json.JSONObject;

public class CoManager {
    boolean a = false;
    boolean b = false;
    private Context c = null;
    private volatile boolean d = false;
    private ci e = null;
    private cr f = null;
    private ch g = null;
    private cp h = null;
    private boolean i = false;

    public CoManager(Context context) {
        if (context != null) {
            try {
                this.c = context;
            } catch (Throwable th) {
                cm.a(th, "CoManager", "<init>");
            }
        }
    }

    private void a() {
        try {
            if (!this.b && this.i) {
                BasicLocateConfig basicLocateConfig = new BasicLocateConfig();
                basicLocateConfig.setAdiu(this.e.f());
                basicLocateConfig.setHttpClient(this.f);
                basicLocateConfig.setLicense(this.e.c());
                basicLocateConfig.setMapkey(this.e.d());
                basicLocateConfig.setProductId((byte) 4);
                basicLocateConfig.setProductVersion(this.e.b());
                basicLocateConfig.setUtdid(this.e.g());
                basicLocateConfig.enableGetPrivateID(this.e.a());
                BasicLocateManager.getInstance().init(this.c, basicLocateConfig);
                this.b = true;
            }
        } catch (Throwable th) {
        }
    }

    private void b() {
        try {
            if (this.h == null) {
                this.h = new cp(this.c);
                this.h.a(this.e, this.f);
            }
        } catch (Throwable th) {
            cm.a(th, "CoManager", "initOfflineManager");
        }
    }

    public void correctOfflineLocation(String str, ScanResult[] scanResultArr, double d2, double d3) {
        try {
            if (this.d && !this.a) {
                a();
                b();
                cp.a(str, scanResultArr, d2, d3);
            }
        } catch (Throwable th) {
            new String[1][0] = "correctOfflineLocation error!!!!";
            cm.a(th, "CoManager", "correctOfflineLocation");
        }
    }

    public void destroy() {
        try {
            destroyCollect();
            destroyOfflineLoc();
            BasicLocateManager.getInstance().destroy();
            this.b = false;
            this.a = true;
            this.c = null;
            this.d = false;
            this.e = null;
            this.f = null;
            this.a = false;
            this.i = false;
        } catch (Throwable th) {
            cm.a(th, "CoManager", Lifecycle.STATUS_DESTROY);
        }
    }

    public void destroyCollect() {
        try {
            if (this.g != null) {
                this.g.a();
            }
            this.g = null;
        } catch (Throwable th) {
            cm.a(th, "CoManager", "stopCollect");
        }
    }

    public void destroyOfflineLoc() {
        try {
            if (this.h != null) {
                this.h.a();
            }
            this.h = null;
        } catch (Throwable th) {
            cm.a(th, "CoManager", "destroyOfflineLoc");
        }
    }

    public String getCollectVersion() {
        try {
            return CollectionManagerProxy.getVersion();
        } catch (Throwable th) {
            cm.a(th, "CoManager", "getCollectVersion");
            return null;
        }
    }

    public String getOfflineLoc(String str, ScanResult[] scanResultArr, boolean z) {
        try {
            if (!this.d || this.a) {
                return null;
            }
            a();
            b();
            return this.h.a(str, scanResultArr, z);
        } catch (Throwable th) {
            new String[1][0] = "getOfflineLocation error!!!!";
            cm.a(th, "CoManager", "getOfflineLoc");
            return null;
        }
    }

    public void init(String str) {
        if (!this.i) {
            try {
                if (this.e == null) {
                    this.e = new ci();
                }
                if (str != null) {
                    JSONObject jSONObject = new JSONObject(str);
                    this.e.b(jSONObject.optString("als", ""));
                    this.e.a(jSONObject.optString("sv", ""));
                    this.e.d(jSONObject.optString("pn", ""));
                    this.e.c(jSONObject.optString("ak", ""));
                    this.e.e(jSONObject.optString("au", ""));
                    this.e.f(jSONObject.optString("ud", ""));
                    this.e.a(jSONObject.optBoolean("isimei", true));
                }
            } catch (Throwable th) {
                cm.a(th, "CoManager", "init");
                return;
            }
            if (this.f == null) {
                this.f = new cr(this.c);
            }
            this.f.a(this.e);
            this.i = true;
        }
    }

    public void loadLocalSo() {
        try {
            if (!this.d) {
                System.loadLibrary("apssdk");
                this.d = true;
            }
        } catch (Throwable th) {
        }
    }

    public void loadSo(String str) {
        try {
            if (!TextUtils.isEmpty(str) && !this.d) {
                if (Arrays.asList(cj.a).contains(aa.a(str))) {
                    System.load(str);
                    this.d = true;
                }
            }
        } catch (Throwable th) {
            cm.a(th, "CoManager", "loadSo");
        }
    }

    public void setCloudConfigVersion(int i2) {
        if (this.f != null) {
            this.f.a(i2);
        }
    }

    public void startCollect() {
        try {
            if (this.d && !this.a) {
                if (this.g == null) {
                    this.g = new ch(this.c);
                }
                a();
                this.g.a(this.e, this.f);
            }
        } catch (Throwable th) {
            cm.a(th, "CoManager", "startCollect");
        }
    }

    public void trainingFps(String str, ScanResult[] scanResultArr) {
        try {
            if (this.d && !this.a) {
                a();
                b();
                cp.a(str, scanResultArr);
            }
        } catch (Throwable th) {
            new String[1][0] = "correctOfflineLocation error!!!!";
            cm.a(th, "CoManager", "correctOfflineLocation");
        }
    }
}
