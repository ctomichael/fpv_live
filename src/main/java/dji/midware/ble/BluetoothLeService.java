package dji.midware.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.logic.Handheld.DJIHandheldHelper;
import dji.logic.utils.DJIProductSupportUtil;
import dji.midware.ble.BLE;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DJIPackManager;
import dji.midware.data.manager.P3.DJIServiceInterface;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataOsdSetLED;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.util.BytesUtil;
import java.util.Timer;
import org.greenrobot.eventbus.EventBus;

public class BluetoothLeService implements DJIServiceInterface, BLE.BLEListener {
    private static final String BLE_STATE_ACTION = "android.bluetooth.adapter.action.STATE_CHANGED";
    private static final int SCAN_DURATION = 10;
    private static final String TAG = "BluetoothLeService";
    private static Context mCtx;
    private static BluetoothLeService mInstance = null;
    final DataOsdSetLED ledSetter;
    private BLE mBLE = null;
    private Timer mHeartTimer;
    boolean mIsReceiverRegistered;
    private ActionReceiver mReceiver;
    private boolean needDelayed = true;
    private DJIPackManager packManager = DJIPackManager.getInstance();

    public enum BLEStateNotification {
        ON,
        OFF
    }

    private class ActionReceiver extends BroadcastReceiver {
        private ActionReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 10);
            if (state == 10) {
                DJILogHelper.getInstance().LOGD(BluetoothLeService.TAG, "onReceive:STATE_OFF", false, true);
                EventBus.getDefault().post(BLEStateNotification.OFF);
                BluetoothLeService.this.onBLEClosed();
            } else if (state == 12) {
                DJILogHelper.getInstance().LOGD(BluetoothLeService.TAG, "onReceive:STATE_ON", false, true);
                EventBus.getDefault().post(BLEStateNotification.ON);
                BluetoothLeService.this.onBLEOpened();
            }
        }
    }

    private void initBroadcastReceiver() {
        if (!this.mIsReceiverRegistered) {
            this.mReceiver = new ActionReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BLE_STATE_ACTION);
            mCtx.registerReceiver(this.mReceiver, filter);
            this.mIsReceiverRegistered = true;
        }
    }

    public static synchronized BluetoothLeService getInstance() {
        BluetoothLeService bluetoothLeService;
        synchronized (BluetoothLeService.class) {
            if (mInstance == null) {
                mInstance = new BluetoothLeService();
            }
            bluetoothLeService = mInstance;
        }
        return bluetoothLeService;
    }

    public static void setContext(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            mCtx = context;
            BLE.setContext(context);
        }
    }

    private BluetoothLeService() {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mBLE = BLE.getInstance();
        }
        this.ledSetter = new DataOsdSetLED();
    }

    /* access modifiers changed from: private */
    public void onBLEOpened() {
        start(10);
    }

    /* access modifiers changed from: private */
    public void onBLEClosed() {
        if (!this.mBLE.isEnabled()) {
            onDisconnect();
            destroy();
        }
    }

    public void start(int duration) {
        if (Build.VERSION.SDK_INT >= 19) {
            initBroadcastReceiver();
            this.mBLE.init(this);
            if (this.mBLE.isEnabled()) {
                this.mBLE.startScan(duration);
            }
        }
    }

    public void tryReConnect(BLE.BLEReconnectListener listener) {
        if (this.mBLE != null) {
            this.mBLE.tryReconnect(listener);
        }
    }

    public void removeBleReconnectListener(BLE.BLEReconnectListener listener) {
        if (this.mBLE != null) {
            this.mBLE.removeReconnectListener(listener);
        }
    }

    public boolean isBleError() {
        if (this.mBLE != null) {
            return this.mBLE.isLinkError();
        }
        return false;
    }

    public boolean enableBLE() {
        return this.mBLE.enable();
    }

    public boolean isBleEnabled() {
        return this.mBLE.isEnabled();
    }

    public void stopScan() {
        this.mBLE.stopScan();
    }

    public void startScan(int duration) {
        if (this.mBLE.isEnabled()) {
            this.mBLE.startScan(duration);
        } else {
            enableBLE();
        }
    }

    public void connect(String address) {
        this.mBLE.connect(address);
    }

    public void connect(String address, BLE.BLEDeviceConnectionCallback callback) {
        this.mBLE.connect(address, callback);
    }

    public void disconnect() {
        this.mBLE.disconnect();
    }

    public void disconnect(BLE.BLEDeviceConnectionCallback callback) {
        this.mBLE.disconnect(callback);
    }

    public BLE getBLE() {
        return this.mBLE;
    }

    public void setCurDeviceType(DJIBLEDeviceType type) {
        if (this.mBLE != null) {
            this.mBLE.setCurDeviceType(type);
        }
    }

    public void sendmessage(SendPack pack) {
        int length;
        if (this.needDelayed) {
            try {
                Thread.sleep(1500);
                this.needDelayed = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int left = pack.getLength();
        int start = 0;
        while (left > 0) {
            if (left >= 20) {
                length = 20;
            } else {
                length = left;
            }
            this.mBLE.writeCharacteristicNonBlock(BytesUtil.readBytes(pack.buffer, start, length));
            left -= length;
            start += length;
        }
        pack.bufferObject.noUsed();
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == 2) {
            onConnect();
        } else if (newState == 0) {
            onDisconnect();
        }
    }

    @TargetApi(18)
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        byte[] data = characteristic.getValue();
        this.packManager.parse(data, 0, data.length);
    }

    public void refreshCache() {
        this.mBLE.refreshDeviceCache();
    }

    public void startStream() {
    }

    public void stopStream() {
    }

    public boolean isConnected() {
        return this.mBLE.isConnected();
    }

    public void onDisconnect() {
        this.needDelayed = true;
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.NON);
        DJILog.saveConnectDebug("bluetooth service post connect lose!");
        EventBus.getDefault().post(DataEvent.ConnectLose);
    }

    public void onConnect() {
        this.needDelayed = true;
        DJIHandheldHelper.getInstance().sendHeartPack();
        if (DJIProductSupportUtil.isLonganMobile(null)) {
            DJIHandheldHelper.getInstance().sendLEDControlCMD();
        }
        DJIHandheldHelper.getInstance().sendHeartPack(DeviceType.CENTER, 7);
        DJILinkDaemonService.getInstance().setLinkType(DJILinkType.BLE);
        EventBus.getDefault().post(DataEvent.ConnectOK);
    }

    public void destroy() {
        if (this.mBLE != null) {
            this.mBLE.destroy();
        }
    }

    public static void Destroy() {
        if (mInstance != null) {
            mInstance.destroy();
        }
    }

    public boolean isOK() {
        if (this.mBLE != null) {
            return this.mBLE.isConnected();
        }
        return false;
    }

    public boolean isRemoteOK() {
        return isConnected();
    }

    public boolean isOnGimbal() {
        return this.mBLE.getLastConnectedAddress().equals(this.mBLE.getAddressOnGimbalDevice());
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

    public void setInUpgradeMode(boolean upgrade) {
        this.mBLE.setHighSpeedMode(upgrade);
    }
}
