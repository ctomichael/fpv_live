package dji.midware.aoabridge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.dji.configassistant.DJIConfigAssistantHelper;
import com.dji.configassistant.DJIInnerProperty;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.MidWare;
import dji.midware.aoabridge.AppClient;
import dji.midware.util.DJIEventBusUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class AoaController {
    private static final String AoaBridgeApp = "com.dji.aoabridge";
    private static final String AoaServiceApp = "com.dji.aoaservice";
    private static final String AoaServiceClientAction = "com.dji.aoaservice.client.connected";
    @DJIInnerProperty("aoabridge.server_ip")
    private static String SERVER_IP = "192.168.1.101";
    @DJIInnerProperty("aoabridge.enable")
    private static boolean isEnable = false;
    @DJIInnerProperty("aoabridge.is_server")
    private static boolean isProxy = false;
    public static String sdkIp = null;
    private int SERVER_DATA_PORT = DJIDiagnosticsError.AirLink.ERROR_WITH_DETACHABLE_CAMERA;
    private int SERVER_EVENT_PORT = 7006;
    private AppClient appClient;
    private AppEventClient appEventClient;
    private BroadcastReceiver clientReceiver = new BroadcastReceiver() {
        /* class dji.midware.aoabridge.AoaController.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            boolean isEvent = data.getBoolean("isEvent");
            String packageName = data.getString("packageName");
            int port = data.getInt("port");
            if (isEvent) {
                AoaController.this.proxyEventServer.updateClientInfo(packageName, port);
            } else {
                AoaController.this.proxyServer.updateClientInfo(packageName, port);
            }
            if (AoaController.this.lastSelectedAppPackageName != null && packageName.equals(AoaController.this.lastSelectedAppPackageName) && System.currentTimeMillis() - AoaController.this.lastSelectedAppTime < 300000 && AoaController.this.proxyServer.containApp(packageName) && AoaController.this.proxyEventServer.containApp(packageName) && !AoaController.this.lastSelectedAppPackageName.equals(AoaController.this.proxyServer.getCurrentSelectedPackageName())) {
                AoaController.this.proxyServer.switchClient(packageName);
                AoaController.this.proxyEventServer.switchClient(packageName);
            }
        }
    };
    private Context ctx;
    /* access modifiers changed from: private */
    public String lastSelectedAppPackageName;
    /* access modifiers changed from: private */
    public long lastSelectedAppTime;
    /* access modifiers changed from: private */
    public ProxyEventServer proxyEventServer;
    /* access modifiers changed from: private */
    public ProxyServer proxyServer;

    public enum RcEvent {
        Connected,
        DisConnected
    }

    public boolean isProxy() {
        return isProxy && isEnable;
    }

    public boolean isApp() {
        return !isProxy && isEnable();
    }

    public boolean isEnable() {
        return isEnable;
    }

    public static AoaController get() {
        return SingleHolder.instance;
    }

    public void init(Context ctx2) {
        this.ctx = ctx2;
        DJIConfigAssistantHelper.get().inject(AoaController.class);
        if (isAoaServiceApp()) {
            isEnable = true;
            isProxy = true;
        }
        if (isAoaServiceAppInstalled()) {
            isEnable = true;
            isProxy = false;
            SERVER_IP = Utils.getIp();
        }
        if (isAoaBridgeApp()) {
            isEnable = true;
            isProxy = true;
        }
        if (sdkIp != null) {
            isEnable = true;
            isProxy = false;
            SERVER_IP = sdkIp;
        }
        if (isEnable) {
            if (isProxy) {
                this.proxyServer = new ProxyServer(this.SERVER_DATA_PORT);
                this.proxyServer.init();
                this.proxyEventServer = new ProxyEventServer(this.SERVER_EVENT_PORT);
                this.proxyEventServer.init();
            } else {
                this.appClient = new AppClient(SERVER_IP, this.SERVER_DATA_PORT);
                this.appClient.init();
            }
        }
        if (MidWare.isBridgeEnabled() && this.proxyServer == null) {
            this.proxyServer = new ProxyServer(this.SERVER_DATA_PORT);
            this.proxyServer.init();
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (isAoaServiceApp()) {
            IntentFilter clientFilter = new IntentFilter();
            clientFilter.addAction(AoaServiceClientAction);
            ctx2.registerReceiver(this.clientReceiver, clientFilter);
        }
    }

    public void unit() {
        if (this.proxyServer != null) {
            this.proxyServer.uninit();
        }
        if (this.appClient != null) {
            this.appClient.uninit();
        }
        DJIEventBusUtil.unRegister(this);
        if (isAoaServiceApp()) {
            this.ctx.unregisterReceiver(this.clientReceiver);
        }
    }

    private boolean isAoaBridgeApp() {
        return Utils.getAppContext().getPackageName().equals(AoaBridgeApp);
    }

    private boolean isAoaServiceApp() {
        return Utils.getAppContext().getPackageName().equals(AoaServiceApp);
    }

    private boolean isAoaServiceAppInstalled() {
        if (isAoaServiceApp()) {
            return false;
        }
        return Utils.isAppInstalled(AoaServiceApp);
    }

    public InputStream getInputStream() {
        return this.appClient.getInputStream();
    }

    public OutputStream getOutputStream() {
        return this.appClient.getOuputStream();
    }

    public void reset() {
        this.proxyServer.uninit();
        this.proxyServer.init();
        this.proxyEventServer.uninit();
        this.proxyEventServer.init();
    }

    private static class SingleHolder {
        public static AoaController instance = new AoaController();

        private SingleHolder() {
        }
    }

    public void sendByte(byte[] data, int len) {
        if (this.proxyServer != null) {
            this.proxyServer.sendByte(data, len);
        }
    }

    public int getClientCount() {
        return this.proxyServer.getClientCount();
    }

    public List<String> getAllClientIp() {
        return this.proxyServer.getAllClientIp();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(AppClient.Status event) {
        if (event == AppClient.Status.Connected) {
            this.appEventClient = new AppEventClient(SERVER_IP, this.SERVER_EVENT_PORT);
            this.appEventClient.init();
        } else if (this.appEventClient != null) {
            this.appEventClient.uninit();
            this.appEventClient = null;
        }
    }

    public boolean isRcConnected() {
        if (this.appEventClient != null) {
            return this.appEventClient.isRcConnected();
        }
        return false;
    }

    public void switchClient(String packageName) {
        this.lastSelectedAppPackageName = packageName;
        this.lastSelectedAppTime = System.currentTimeMillis();
        this.proxyServer.switchClient(packageName);
        this.proxyEventServer.switchClient(packageName);
    }

    public void broadcastClientConnected(boolean isEvent, int port) {
        Intent intent = new Intent(AoaServiceClientAction);
        intent.putExtra("isEvent", isEvent);
        intent.putExtra("port", port);
        intent.putExtra("packageName", this.ctx.getApplicationInfo().packageName);
        this.ctx.sendBroadcast(intent);
    }

    public String getCurrentSelectedPackageName() {
        return this.proxyServer.getCurrentSelectedPackageName();
    }
}
