package dji.midware.wifi;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.broadcastReceivers.DJINetWorkReceiver;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.NetworkUtils;
import it.sauronsoftware.ftp4j.SocketProvider;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import javax.net.SocketFactory;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
@TargetApi(23)
public class DJIMultiNetworkMgr implements SocketProvider {
    private static boolean DEBUG = true;
    private static final int DELAY_NORMAL = 2000;
    private static final int DELAY_SHORT = 500;
    public static String LOG_FILE_NAME = "MultiNetworkLog";
    private static final int MSG_GET_MOBILE_NETWORK = 3;
    private static final int MSG_RETRY_DETECT_ENABLE = 1;
    private static final int MSG_RETRY_DETECT_ENABLE_TIMEOUT = 2;
    /* access modifiers changed from: private */
    public static final String TAG = DJIMultiNetworkMgr.class.getSimpleName();
    private NetworkRequest.Builder builder;
    /* access modifiers changed from: private */
    public volatile boolean isCheckingMobileData;
    /* access modifiers changed from: private */
    public Handler mBackHandler;
    private boolean mCallbackRegistered;
    private ConnectivityManager mConnectMgr;
    private Context mContext;
    /* access modifiers changed from: private */
    public boolean mEnabled;
    /* access modifiers changed from: private */
    public boolean mIsMobileEnabled;
    private MobileRunnable mMobileDataChecker;
    /* access modifiers changed from: private */
    public Network mMobileNetwork;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    int mRetryTime;
    private SocketFactory mSf;
    private Network mWifiNetwork;
    private NetworkRequest networkRequest;

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIMultiNetworkMgr INSTANCE = new DJIMultiNetworkMgr();

        private SingletonHolder() {
        }
    }

    private DJIMultiNetworkMgr() {
        this.mBackHandler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
            /* class dji.midware.wifi.DJIMultiNetworkMgr.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                Network network;
                if (msg.what == 1) {
                    DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "receive msg MSG_RETRY_DETECT_ENABLE");
                    if (DJIMultiNetworkMgr.this.canOpen()) {
                        if (DJIMultiNetworkMgr.this.mBackHandler.hasMessages(2)) {
                            DJIMultiNetworkMgr.this.mBackHandler.removeMessages(2);
                        }
                        DJIMultiNetworkMgr.this.checkForEnable();
                    } else {
                        DJIMultiNetworkMgr.this.mBackHandler.sendEmptyMessageDelayed(1, 2000);
                        DJIMultiNetworkMgr.log("send MSG_RETRY_DETECT_ENABLE");
                    }
                } else if (msg.what == 2) {
                    DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "receive msg MSG_RETRY_DETECT_ENABLE_TIMEOUT");
                    if (DJIMultiNetworkMgr.this.mBackHandler.hasMessages(1)) {
                        DJIMultiNetworkMgr.this.mBackHandler.removeMessages(1);
                    }
                } else if (msg.what == 3 && (network = DJIMultiNetworkMgr.this.getNetworkObject(0)) != null) {
                    DJIMultiNetworkMgr.this.unregisterNetworkCallback();
                    DJIMultiNetworkMgr.log("got network, start checker thread");
                    Network unused = DJIMultiNetworkMgr.this.mMobileNetwork = network;
                    DJIMultiNetworkMgr.this.startMobileCheckerThread();
                }
                return false;
            }
        });
        this.mRetryTime = 0;
    }

    public static final DJIMultiNetworkMgr getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        if (isVersionSupported()) {
            this.mContext = context;
            this.mConnectMgr = (ConnectivityManager) this.mContext.getSystemService("connectivity");
            this.builder = new NetworkRequest.Builder();
            this.builder.addCapability(12);
            this.builder.addTransportType(0);
            this.networkRequest = this.builder.build();
            this.mContext.getContentResolver().registerContentObserver(Settings.Global.CONTENT_URI, true, new SettingsObserver());
            this.mIsMobileEnabled = isMobileEnabled();
            initNetworkCallback();
            boolean canOpen = canOpen();
            if (canOpen) {
                checkForEnable();
            }
            log("init state, canOpen:" + canOpen + "mobile:" + this.mIsMobileEnabled);
            DJIEventBusUtil.register(this);
        }
    }

    private void initNetworkCallback() {
        this.mNetworkCallback = new ConnectivityManager.NetworkCallback() {
            /* class dji.midware.wifi.DJIMultiNetworkMgr.AnonymousClass2 */

            public void onAvailable(Network network) {
                super.onAvailable(network);
                DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, " mobile onAvailable");
                DJIMultiNetworkMgr.log("request network start: " + System.currentTimeMillis());
                if (network != null) {
                    if (DJIMultiNetworkMgr.this.mBackHandler.hasMessages(3)) {
                        DJIMultiNetworkMgr.this.mBackHandler.removeMessages(3);
                    }
                    Network unused = DJIMultiNetworkMgr.this.mMobileNetwork = network;
                    if (!DJIMultiNetworkMgr.this.isCheckingMobileData && DJIMultiNetworkMgr.this.canOpen()) {
                        DJIMultiNetworkMgr.this.startMobileCheckerThread();
                    }
                }
            }

            public void onLost(Network network) {
                super.onLost(network);
                DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, " mobile onLost: ");
                Network unused = DJIMultiNetworkMgr.this.mMobileNetwork = null;
                DJIMultiNetworkMgr.this.unregisterNetworkCallback();
                DJIMultiNetworkMgr.this.close();
            }
        };
    }

    public static boolean isVersionSupported() {
        return Build.VERSION.SDK_INT >= 23 && !Build.VERSION.RELEASE.equals("6.0");
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void bindSocketWithWIFI(Socket socket) {
        Network network;
        if (this.mEnabled && (network = getWifiNetwork()) != null) {
            log("start bind socket fd");
            try {
                network.bindSocket(socket);
                log("bind socket succeed");
            } catch (IOException e) {
                e.printStackTrace();
                logE("bind exception:" + DJILog.exceptionToString(e));
            }
        }
    }

    public void bindSocketWithWIFI(int socketFd) {
        Network network = getWifiNetwork();
        if (network != null) {
            log("start bind socket fd");
            FileDescriptor fileDescriptor = new FileDescriptor();
            try {
                Field field = FileDescriptor.class.getDeclaredField("descriptor");
                field.setAccessible(true);
                field.setInt(fileDescriptor, socketFd);
            } catch (NoSuchFieldException e) {
                logE("get fd exception:" + DJILog.exceptionToString(e));
            } catch (IllegalAccessException e2) {
                logE("get fd exception:" + DJILog.exceptionToString(e2));
            }
            try {
                network.bindSocket(fileDescriptor);
                log("bind socket succeed");
            } catch (IOException e3) {
                logE("bind exception:" + DJILog.exceptionToString(e3));
            }
        }
    }

    public Socket getSocket() {
        if (this.mEnabled && this.mSf != null) {
            try {
                return this.mSf.createSocket();
            } catch (IOException e) {
                e.printStackTrace();
                logE("getSocket exception:" + DJILog.exceptionToString(e));
            }
        }
        return new Socket();
    }

    public void checkForDisable() {
        log(TAG, "start checkForDisable");
        if (!this.mEnabled) {
            log(TAG, "disabled, return");
            return;
        }
        this.mConnectMgr = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        if (this.mConnectMgr == null) {
            log(TAG, "return for null cm");
            return;
        }
        if (!isMobileDataConnected() || !isWifiConnectedOrConnecting()) {
            close();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void handleDataEvent(DataEvent event) {
        log(TAG, "event:" + event);
        if (event == DataEvent.ConnectOK) {
            if (this.mBackHandler.hasMessages(1)) {
                this.mBackHandler.removeMessages(1);
            }
            if (this.mBackHandler.hasMessages(2)) {
                this.mBackHandler.removeMessages(2);
            }
            this.mBackHandler.sendEmptyMessage(1);
            this.mBackHandler.sendEmptyMessageDelayed(2, 10000);
            log(TAG, "send MSG_RETRY_DETECT_ENABLE");
        }
    }

    /* access modifiers changed from: private */
    public void checkForEnable() {
        boolean isAndroid6WithBug;
        boolean enableCondition = false;
        log(TAG, "start checkForEnable");
        if (isVersionSupported()) {
            if (this.mEnabled) {
                log(TAG, "enabled, return");
            } else if (this.isCheckingMobileData) {
                log(TAG, "CheckingMobileData, return");
            } else {
                if (Build.VERSION.SDK_INT == 23) {
                    isAndroid6WithBug = true;
                } else {
                    isAndroid6WithBug = false;
                }
                if (!isAndroid6WithBug || Settings.System.canWrite(this.mContext)) {
                    this.mConnectMgr = (ConnectivityManager) this.mContext.getSystemService("connectivity");
                    if (this.mConnectMgr == null) {
                        log(TAG, "return for null cm");
                        return;
                    }
                    boolean isMobileOk = isMobileDataConnected();
                    boolean isWifiOk = isWifiConnectedOrConnecting();
                    if (isMobileOk && isWifiOk) {
                        enableCondition = true;
                    }
                    log(TAG, "isMobileOk:" + isMobileOk + "isWifiOk:" + isWifiOk + " mEnable:" + this.mEnabled);
                    if (enableCondition) {
                        unregisterNetworkCallback();
                        this.mConnectMgr.requestNetwork(this.networkRequest, this.mNetworkCallback);
                        this.mCallbackRegistered = true;
                        log("requestNetwork");
                        this.mBackHandler.sendEmptyMessageDelayed(3, 2000);
                        return;
                    }
                    return;
                }
                log(TAG, "No permission, return");
            }
        }
    }

    public Network getWifiNetwork() {
        return getNetworkObject(1);
    }

    /* access modifiers changed from: private */
    public boolean isMobileDataConnected() {
        DJILog.i(TAG, "isMobileDataConnected : " + this.mIsMobileEnabled, new Object[0]);
        return this.mIsMobileEnabled;
    }

    private boolean isWifiConnected() {
        if (this.mConnectMgr == null) {
            return false;
        }
        NetworkInfo wifiNetInfo = this.mConnectMgr.getNetworkInfo(1);
        if (wifiNetInfo == null || !wifiNetInfo.isConnected()) {
            DJILog.i(TAG, "isWifiConnected or connecting : false", new Object[0]);
            return false;
        }
        DJILog.i(TAG, "isWifiConnected or connecting : true", new Object[0]);
        return true;
    }

    private boolean isWifiConnectedOrConnecting() {
        if (this.mConnectMgr == null || this.mContext == null) {
            return false;
        }
        if (DJINetWorkReceiver.isNetworkConnectedOrConnectingByType(this.mContext, 1)) {
            DJILog.i(TAG, "isWifiConnected : connect", new Object[0]);
            return true;
        }
        DJILog.i(TAG, "isWifiConnected : unconnect", new Object[0]);
        return false;
    }

    /* access modifiers changed from: private */
    public void setNetwork(Network network) {
        log(TAG, "setNetwork:" + network);
        this.mWifiNetwork = network;
        if (network != null) {
            this.mSf = network.getSocketFactory();
        } else {
            this.mSf = null;
        }
    }

    /* access modifiers changed from: private */
    public void close() {
        log(TAG, "start close");
        if (this.mEnabled) {
            log(TAG, "start close confirm");
            this.mEnabled = false;
            this.mRetryTime = 0;
            bindNetwork(null);
            setNetwork(null);
            log("---DJI Multi-Network disabled---");
            return;
        }
        log(TAG, "start close return: already closed");
    }

    /* access modifiers changed from: private */
    public Network getNetworkObject(int networkType) {
        if (this.mConnectMgr == null) {
            logE("getNetworkObject return null for null cm");
            return null;
        }
        Network[] networks = this.mConnectMgr.getAllNetworks();
        if (networks != null && networks.length >= 1) {
            for (Network network : networks) {
                NetworkInfo info = this.mConnectMgr.getNetworkInfo(network);
                if (info != null) {
                    int type = info.getType();
                    log("getNetworkObject", "network type:" + type);
                    if (type == networkType) {
                        return network;
                    }
                }
            }
        }
        return null;
    }

    private class MobileRunnable implements Runnable {
        public MobileRunnable() {
            DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "new MobileRunnable");
        }

        public void run() {
            boolean unused = DJIMultiNetworkMgr.this.isCheckingMobileData = true;
            DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "MobileRunnable run");
            boolean isMobileDataOk = DJIMultiNetworkMgr.this.isMobileDataConnected();
            DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, " isMobileDataOk:" + isMobileDataOk);
            if (isMobileDataOk) {
                Network network = DJIMultiNetworkMgr.this.mMobileNetwork;
                DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "mobileNetwork:" + network);
                if (network != null && NetworkUtils.shouldAllowNetworkAccess()) {
                    boolean pingResult = pingURL(network, "https://www.dji.com", 5000);
                    DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "pingResult:" + pingResult);
                    if (pingResult) {
                        boolean bindResult = DJIMultiNetworkMgr.this.bindNetwork(network);
                        DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "bindResult:" + bindResult);
                        if (bindResult) {
                            Network wifiNetwork = DJIMultiNetworkMgr.this.getNetworkObject(1);
                            if (wifiNetwork != null) {
                                DJIMultiNetworkMgr.this.setNetwork(wifiNetwork);
                                boolean unused2 = DJIMultiNetworkMgr.this.mEnabled = true;
                                DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "+++DJI Multi-Network enabled+++");
                                DJIMultiNetworkMgr.this.mRetryTime = 0;
                                boolean unused3 = DJIMultiNetworkMgr.this.isCheckingMobileData = false;
                                return;
                            }
                            DJIMultiNetworkMgr.logE(DJIMultiNetworkMgr.TAG, "couldn't enable for getNetworkObject return null");
                            boolean unused4 = DJIMultiNetworkMgr.this.bindNetwork(null);
                            boolean unused5 = DJIMultiNetworkMgr.this.mEnabled = false;
                        }
                    }
                }
            }
            boolean unused6 = DJIMultiNetworkMgr.this.isCheckingMobileData = false;
            DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "ping failed");
            if (DJIMultiNetworkMgr.this.mRetryTime < 3) {
                DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "retry for ping failed");
                DJIMultiNetworkMgr.this.mRetryTime++;
                DJIMultiNetworkMgr.this.checkForEnable();
                return;
            }
            DJIMultiNetworkMgr.this.mRetryTime = 0;
            DJIMultiNetworkMgr.this.unregisterNetworkCallback();
        }

        private boolean pingURL(Network network, String url, int timeout) {
            try {
                HttpURLConnection connection = (HttpURLConnection) network.openConnection(new URL(url.replaceFirst("^https", "http")));
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setRequestMethod("HEAD");
                long start = System.currentTimeMillis();
                int responseCode = connection.getResponseCode();
                DJIMultiNetworkMgr.log(DJIMultiNetworkMgr.TAG, "response code : " + responseCode + " time:" + (System.currentTimeMillis() - start));
                if (200 > responseCode || responseCode > 399) {
                    return false;
                }
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
                boolean unused = DJIMultiNetworkMgr.this.isCheckingMobileData = false;
                return false;
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean bindNetwork(Network network) {
        log(TAG, "bindNetwork:" + network);
        if (Build.VERSION.SDK_INT < 23) {
            return ConnectivityManager.setProcessDefaultNetwork(network);
        }
        if (this.mConnectMgr != null) {
            return this.mConnectMgr.bindProcessToNetwork(network);
        }
        logE("bind failed for null cm");
        return false;
    }

    /* access modifiers changed from: private */
    public boolean canOpen() {
        boolean isWifiLink;
        if (DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.WIFI) {
            isWifiLink = true;
        } else {
            isWifiLink = false;
        }
        boolean isWifiConnected = isWifiConnectedOrConnecting();
        log(TAG, "isWifiLink:" + isWifiLink + " isWifiConnected:" + isWifiConnected + " isMobileDataEnabled:" + this.mIsMobileEnabled);
        return isWifiLink && isWifiConnected && this.mIsMobileEnabled;
    }

    /* access modifiers changed from: private */
    public void startMobileCheckerThread() {
        if (this.mMobileDataChecker == null) {
            this.mMobileDataChecker = new MobileRunnable();
        }
        new Thread(this.mMobileDataChecker, "multiNet").start();
    }

    public class SettingsObserver extends ContentObserver {
        public SettingsObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        public void onChange(boolean selfChange) {
            boolean isMobileEnabled = DJIMultiNetworkMgr.this.isMobileEnabled();
            if (isMobileEnabled != DJIMultiNetworkMgr.this.mIsMobileEnabled) {
                boolean unused = DJIMultiNetworkMgr.this.mIsMobileEnabled = isMobileEnabled;
                if (!DJIMultiNetworkMgr.this.mIsMobileEnabled) {
                    DJIMultiNetworkMgr.this.close();
                } else if (DJIMultiNetworkMgr.this.canOpen()) {
                    DJIMultiNetworkMgr.this.checkForEnable();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isMobileEnabled() {
        boolean reflectResult = reflectMobileEnabled();
        boolean detectResult = detectMobileEnabled();
        log("isMobileEnabled, reflectResult:" + reflectResult + " detectResult:" + detectResult);
        return reflectResult || detectResult;
    }

    private boolean reflectMobileEnabled() {
        if (this.mConnectMgr == null) {
            logE("detectMobileEnabled return for null ConnectMgr");
            return false;
        }
        if (isSimReady()) {
            try {
                Method method = Class.forName(this.mConnectMgr.getClass().getName()).getDeclaredMethod("getMobileDataEnabled", new Class[0]);
                method.setAccessible(true);
                return ((Boolean) method.invoke(this.mConnectMgr, new Object[0])).booleanValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean detectMobileEnabled() {
        boolean z = true;
        if (this.mContext == null) {
            logE("detectMobileEnabled return for null context");
            return false;
        } else if (!isSimReady()) {
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= 17) {
                if (Settings.Global.getInt(this.mContext.getContentResolver(), "mobile_data", 1) != 1) {
                    z = false;
                }
                return z;
            }
            if (Settings.Secure.getInt(this.mContext.getContentResolver(), "mobile_data", 1) != 1) {
                z = false;
            }
            return z;
        }
    }

    private boolean isSimReady() {
        return ((TelephonyManager) this.mContext.getSystemService("phone")).getSimState() == 5;
    }

    /* access modifiers changed from: private */
    public void unregisterNetworkCallback() {
        if (this.mConnectMgr != null && this.mCallbackRegistered) {
            this.mCallbackRegistered = false;
            this.mConnectMgr.unregisterNetworkCallback(this.mNetworkCallback);
            log("unregisterNetworkCallback");
        }
    }

    /* access modifiers changed from: private */
    public static void log(String tag, String msg) {
        if (DEBUG) {
            logE(tag, msg);
        } else {
            DJILogHelper.getInstance().LOGD(tag, msg);
        }
    }

    /* access modifiers changed from: private */
    public static void log(String msg) {
        if (DEBUG) {
            DJILogHelper.getInstance().LOGD(TAG, msg);
        } else {
            logE(msg);
        }
    }

    private static void logE(String msg) {
        DJILogHelper.getInstance().LOGE(TAG, msg, LOG_FILE_NAME);
    }

    /* access modifiers changed from: private */
    public static void logE(String tag, String msg) {
        DJILogHelper.getInstance().LOGE(tag, msg, LOG_FILE_NAME);
    }
}
