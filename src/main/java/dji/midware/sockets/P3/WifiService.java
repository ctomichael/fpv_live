package dji.midware.sockets.P3;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.logic.utils.DJIProductSupportUtil;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.natives.UDT;
import dji.midware.sockets.Mammoth.MammothCmdServices;
import dji.midware.sockets.Mammoth.MammothStreamServices;
import dji.publics.DJIExecutor;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class WifiService implements DJIServiceInterface {
    private static WifiService instance = null;
    private static volatile P3CCameraService mCameraService = null;
    private static MammothCmdServices mCmdInstancce = null;
    private static volatile P3CGroundService mGroundInstance = null;
    /* access modifiers changed from: private */
    public static CountDownLatch mInitLatch = new CountDownLatch(2);
    private static Executor mInitService = DJIExecutor.getExecutorFor(DJIExecutor.Purpose.URGENT);
    private static volatile P3CLiveStreamService mLiveStreamInstance = null;
    private static volatile P3CRemoteService mRemoteInstance = null;
    private static MammothStreamServices mStreamInstance = null;
    /* access modifiers changed from: private */
    public static volatile SwUdpService mSwUdpService = null;
    private int connectCount = 0;

    private WifiService() {
        doInit();
        startStream();
    }

    private static class WifiInitWorker implements Runnable {
        private boolean isMammothFamily = false;

        public WifiInitWorker(boolean isMMTF) {
            this.isMammothFamily = isMMTF;
        }

        public void run() {
            try {
                if (this.isMammothFamily) {
                    initMammothFamily();
                } else {
                    initOtherFamily();
                }
            } finally {
                WifiService.mInitLatch.countDown();
            }
        }

        private void initMammothFamily() {
        }

        private void initOtherFamily() {
            SwUdpService unused = WifiService.mSwUdpService = SwUdpService.getInstance();
        }
    }

    private static void doInit() {
        WifiInitWorker mammothWorker = new WifiInitWorker(true);
        WifiInitWorker otherWorker = new WifiInitWorker(false);
        mInitService.execute(mammothWorker);
        mInitService.execute(otherWorker);
        try {
            mInitLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized WifiService getInstance() {
        WifiService wifiService;
        synchronized (WifiService.class) {
            if (instance == null) {
                instance = new WifiService();
            }
            wifiService = instance;
        }
        return wifiService;
    }

    public static void Destroy() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public static void DestroyFinal() {
        Destroy();
        new Thread(new Runnable() {
            /* class dji.midware.sockets.P3.WifiService.AnonymousClass1 */

            public void run() {
                UDT.cleanup();
            }
        }).start();
    }

    public void destroy() {
        if (mRemoteInstance != null) {
            mRemoteInstance.destroy();
            mRemoteInstance = null;
        }
        Log.e("daemon", "destroy wifi 1");
        if (mGroundInstance != null) {
            mGroundInstance.destroy();
            mGroundInstance = null;
        }
        Log.e("daemon", "destroy wifi 2");
        if (mLiveStreamInstance != null) {
            mLiveStreamInstance.destroy();
            mLiveStreamInstance = null;
        }
        Log.e("daemon", "destroy wifi 3");
        if (mCameraService != null) {
            mCameraService.destroy();
            mCameraService = null;
        }
        Log.e("daemon", "destroy wifi 4");
        if (mSwUdpService != null) {
            mSwUdpService.destroy();
            mSwUdpService = null;
        }
        Log.e("daemon", "destroy wifi 5");
        if (mStreamInstance != null) {
            mStreamInstance.destroy();
            mStreamInstance = null;
        }
        Log.e("daemon", "destroy wifi 6");
        if (mCmdInstancce != null) {
            mCmdInstancce.destroy();
            mCmdInstancce = null;
        }
        Log.e("daemon", "destroy wifi 7");
        instance = null;
    }

    public void sendmessage(SendPack buffer) {
        if (DJIProductSupportUtil.isLonganSeries(null)) {
            if (mGroundInstance != null && mGroundInstance.isOK()) {
                mGroundInstance.sendmessage(buffer);
            } else if (mRemoteInstance != null) {
                mRemoteInstance.sendmessage(buffer);
            }
        } else if (mSwUdpService != null && mSwUdpService.isConnected()) {
            mSwUdpService.sendmessage(buffer);
        } else if (mCmdInstancce != null && mCmdInstancce.isConnected()) {
            mCmdInstancce.sendmessage(buffer);
        } else if (mGroundInstance == null) {
        } else {
            if (DeviceType.isGround(buffer.buffer[5] & 31)) {
                if (mGroundInstance != null) {
                    mGroundInstance.sendmessage(buffer);
                }
            } else if (mRemoteInstance != null) {
                mRemoteInstance.sendmessage(buffer);
            }
        }
    }

    public void startStream() {
    }

    public void stopStream() {
    }

    public boolean isConnected() {
        if (mRemoteInstance != null && mRemoteInstance.isConnected()) {
            return true;
        }
        if (mGroundInstance != null && mGroundInstance.isConnected()) {
            return true;
        }
        if (mSwUdpService != null && mSwUdpService.isConnected()) {
            return true;
        }
        if (mCmdInstancce == null || !mCmdInstancce.isConnected()) {
            return false;
        }
        return true;
    }

    public boolean isOK() {
        return isConnected();
    }

    public boolean isRemoteOK() {
        if (mSwUdpService != null && mSwUdpService.isConnected()) {
            return mSwUdpService.isRemoteOK();
        }
        if (mCmdInstancce != null && mCmdInstancce.isConnected()) {
            return DJIPackManager.getInstance().isRemoteConnected();
        }
        if (DJIProductSupportUtil.isLonganSeries(null)) {
            return isOK();
        }
        if (mRemoteInstance != null) {
            return mRemoteInstance.isOK();
        }
        return false;
    }

    public void setDataMode(boolean dataMode) {
    }

    public void pauseRecvThread() {
    }

    public void resumeRecvThread() {
    }

    public void pauseParseThread() {
    }

    public void resumeParseThread() {
    }

    public void pauseService(boolean isPause) {
    }

    public synchronized void onDisconnect() {
        if (ServiceManager.isAlive() && this.connectCount > 0) {
            this.connectCount--;
            if (this.connectCount == 0) {
                DJILinkDaemonService.getInstance().setLinkType(DJILinkType.NON);
                if (ServiceManager.isAlive()) {
                    DJILog.saveConnectDebug("wifi service post connect lose!");
                    EventBus.getDefault().post(DataEvent.ConnectLose);
                }
            }
        }
    }

    public synchronized void onConnect() {
        if (ServiceManager.isAlive()) {
            if (this.connectCount == 0) {
                DJILinkDaemonService.getInstance().setLinkType(DJILinkType.WIFI);
                EventBus.getDefault().post(DataEvent.ConnectOK);
            }
            this.connectCount++;
        }
    }

    public boolean isRcConnect() {
        if (mSwUdpService != null && mSwUdpService.isConnected()) {
            return mSwUdpService.isRcConnect();
        }
        if (mCmdInstancce != null) {
            return mCmdInstancce.isConnected();
        }
        return false;
    }

    public boolean isSwUdpCanUse() {
        return mSwUdpService != null && mSwUdpService.isConnected();
    }
}
