package dji.midware.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.text.TextUtils;
import com.dji.frame.util.V_StringUtils;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.util.BackgroundLooper;
import dji.pilot.fpv.model.IEventObjects;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.eventbus.EventBus;

@TargetApi(18)
public class BLE {
    private static final String BLE_CONNECTED_ADDRESS = "ble_last_address";
    private static final String BLE_CONNECTED_DEVICE_TYPE = "ble_last_type";
    private static final int CALLBACK_TIMEOUT = 10000;
    private static final boolean DEBUG = false;
    private static final String DEBUG_MESSAGE = "DJIBLE DEBUG:";
    private static final String ERROR_MESSAGE = "DJIBLE ERROR:";
    private static final String LOGNAME = "BLELog";
    private static String MAC = "D0:B5:C2:B0:B5:C4";
    private static final int MSG_CONNECTED = 2;
    private static final int MSG_DISCONNECTED = 3;
    private static final int MSG_RECONNECT_TIMEOUT = 6;
    private static final int MSG_RECONNECT_WAITING = 5;
    private static final int MSG_RETRY = 4;
    private static final int MSG_TIMEOUT = 1;
    private static final int MSG_WAITTING = 0;
    private static final int RECONNECT_TIMEOUT_LONG = 30000;
    private static final int RECONNECT_TIMEOUT_SHORT = 10000;
    private static final int RETRY_WRITE_DESCRIPTOR = 1;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    private static final String TAG = "DJIBLE";
    public static String UUID_SERVICE = "0000FFF0-0000-1000-8000-00805F9B34FB";
    /* access modifiers changed from: private */
    public static BluetoothAdapter mBluetoothAdapter;
    /* access modifiers changed from: private */
    public static volatile BluetoothManager mBluetoothManager;
    /* access modifiers changed from: private */
    public static Context mContex = null;
    public static ArrayList<BLEObject> mDevicesList = new ArrayList<>();
    private static final BLE mInstance = new BLE();
    private static BluetoothLeScanner mScanner;
    private final int BLT_TIMEOUT = (this.mSleepTime * 500);
    String UUID_CHAR_READ = "0000FFF4-0000-1000-8000-00805F9B34FB";
    String UUID_CHAR_WRITE = "0000FFF5-0000-1000-8000-00805F9B34FB";
    String UUID_DESCRIPTOR_READ = "00002902-0000-1000-8000-00805f9b34fb";
    /* access modifiers changed from: private */
    public CallbackHandler callbackHandler = new CallbackHandler(BackgroundLooper.getLooper());
    /* access modifiers changed from: private */
    public BLEDeviceConnectionCallback connectionCallback = null;
    private final Lock lock = new ReentrantLock();
    /* access modifiers changed from: private */
    public String mAddressOfOnGimbalDevice;
    /* access modifiers changed from: private */
    public ScanResult mAutoConnectScanResult;
    private Timer mAutoScanTimer;
    /* access modifiers changed from: private */
    public BLEListener mBLEListener = null;
    /* access modifiers changed from: private */
    public String mBluetoothDeviceAddress;
    /* access modifiers changed from: private */
    public BluetoothGatt mBluetoothGatt;
    /* access modifiers changed from: private */
    public volatile int mConnectionState = 0;
    private DJIBLEDeviceType mCurDeviceType = DJIBLEDeviceType.OTHER;
    private List<ScanFilter> mDefaultScanFilters;
    /* access modifiers changed from: private */
    public final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        /* class dji.midware.ble.BLE.AnonymousClass9 */

        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == 2) {
                int unused = BLE.this.mConnectionState = 2;
                BLE.this.stopScan();
                BLE.this.cancleAutoScan();
                BLE.this.stopUserScanTimer();
                if (BLE.this.mBluetoothGatt == null) {
                    BluetoothGatt unused2 = BLE.this.mBluetoothGatt = gatt;
                }
                BLE.this.mBluetoothGatt.discoverServices();
            } else if (newState == 0) {
                if (BLE.this.mConnectionState != 0) {
                    BLE.this.onDisconnected();
                }
                BLE.this.mBLEListener.onConnectionStateChange(gatt, status, newState);
            }
            BLE.this.logD("onConnectionStateChange:" + newState);
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            BLE.this.logD("onServicesDiscovered:" + status);
            if (status == 0) {
                int totalCharacteristics = 0;
                for (BluetoothGattService s : BLE.this.mBluetoothGatt.getServices()) {
                    totalCharacteristics += s.getCharacteristics().size();
                }
                if (totalCharacteristics == 0) {
                    BLE.this.logE("mothod : onServicesDiscovered -> totalCharacteristics == 0");
                    BLE.this.tryReconnect(null);
                    return;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean unused = BLE.this.enableNotification();
                if (BLE.this.mGattService != null) {
                    BluetoothGattCharacteristic unused2 = BLE.this.mWriteCharacteristic = BLE.this.mGattService.getCharacteristic(UUID.fromString(BLE.this.UUID_CHAR_WRITE));
                    if (BLE.this.mWriteCharacteristic != null) {
                        BLE.this.mWriteCharacteristic.setWriteType(1);
                    } else {
                        BLE.this.logE("mothod : onServicesDiscovered -> mWriteCharacteristic == null");
                    }
                } else {
                    BLE.this.logE("mothod : onServicesDiscovered -> mGattService == null");
                }
            }
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            BLE.this.mBLEListener.onCharacteristicChanged(gatt, characteristic);
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            DJILogHelper.getInstance().LOGD(BLE.TAG, "DJIMethod : onDescriptorWrite (807)" + status, false, true);
        }
    };
    /* access modifiers changed from: private */
    public BluetoothGattService mGattService;
    /* access modifiers changed from: private */
    public boolean mIsAutoScanning = false;
    /* access modifiers changed from: private */
    public boolean mIsScanningFromUser = false;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private LinkDataState mLinkStatus = LinkDataState.NORMAL;
    private long mPreTime = 0;
    private List<BLEReconnectListener> mReconnectListeners = new ArrayList();
    private Object mReconnectLock = new Object();
    private ScanCallback mScanCallback;
    /* access modifiers changed from: private */
    public WeakReference<BLEScanListener> mScanResultListener;
    private int mSendBits = 0;
    public int mSleepTime = 7;
    private Object mTimerLock = new Object();
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic mWriteCharacteristic;
    private volatile LinkedList<bleRequest> procQueue;
    /* access modifiers changed from: private */
    public Timer scanTimer;
    private int sendResult;
    private Timer stopTimer;

    public enum BLEConnectionError {
        BLE_CONNECTION_TIMEOUT,
        BLE_CONNECTION_CONNECTED,
        BLE_CONNECTION_DISCONNECTED
    }

    public interface BLEListener {
        void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2);
    }

    public interface BLEReconnectListener {
        void onConnected();

        void onError(int i);

        void onTimeout();
    }

    public enum LinkDataState {
        NORMAL,
        ERROR
    }

    public enum BLEEvent {
        BLE_FIND_DEVICE,
        BLE_DEVICE_CONNECTED,
        BLE_DEVICE_DISCONNECTED,
        BLE_DEVICE_CONNECTING
    }

    public interface BLEScanListener {
        void onScanResultUpdate(ArrayList<BLEObject> arrayList);
    }

    public interface BLEDeviceConnectionCallback {
        void onResult(BLEConnectionError bLEConnectionError);
    }

    public static BLE getInstance() {
        return mInstance;
    }

    private BLE() {
        initScanCallback();
    }

    public static void setContext(Context context) {
        mContex = context;
        if (mBluetoothManager == null) {
            synchronized (BLE.class) {
                mBluetoothManager = (BluetoothManager) mContex.getSystemService("bluetooth");
                if (mBluetoothManager == null) {
                    DJILogHelper.getInstance().LOGE(TAG, "DJIBLE ERROR::mothod : setContext -> mBluetoothManager == null", LOGNAME);
                    return;
                }
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            DJILogHelper.getInstance().LOGE(TAG, "DJIBLE ERROR::mothod : setContext -> mBluetoothAdapter == null", LOGNAME);
        }
    }

    public void setCurDeviceType(DJIBLEDeviceType type) {
        if (type != this.mCurDeviceType) {
            this.mCurDeviceType = type;
            mContex.getSharedPreferences(mContex.getPackageName(), 0).edit().putString(BLE_CONNECTED_DEVICE_TYPE, this.mCurDeviceType.name()).commit();
        }
    }

    public DJIBLEDeviceType getLastConnectedDeviceType() {
        return DJIBLEDeviceType.valueOf(mContex.getSharedPreferences(mContex.getPackageName(), 0).getString(BLE_CONNECTED_DEVICE_TYPE, DJIBLEDeviceType.OTHER.name()));
    }

    @TargetApi(21)
    private void initScanCallback() {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mScanCallback = new ScanCallback() {
                /* class dji.midware.ble.BLE.AnonymousClass1 */

                public void onScanResult(int callbackType, ScanResult result) {
                    boolean z = true;
                    super.onScanResult(callbackType, result);
                    ScanRecord record = result.getScanRecord();
                    if (BLE.this.byteFilter(record.getBytes())) {
                        BluetoothDevice device = result.getDevice();
                        BLEEvent event = BLEEvent.BLE_DEVICE_DISCONNECTED;
                        BLE.this.logD("find DJI device, name:" + device.getName() + "state:" + BLE.this.mConnectionState);
                        if (BLE.this.mConnectionState == 1) {
                            if (device.getAddress().equals(BLE.this.mBluetoothDeviceAddress)) {
                                event = BLEEvent.BLE_DEVICE_CONNECTING;
                            }
                        } else if (BLE.this.mConnectionState == 2 && device.getAddress().equals(BLE.this.mBluetoothDeviceAddress)) {
                            event = BLEEvent.BLE_DEVICE_CONNECTED;
                        }
                        BLEObject resultObject = BLE.this.generateBleObject(device, event, result.getRssi());
                        DJILog.d(BLE.TAG, "device:" + device.getName() + " id:" + ((record.getBytes()[11] & 240) >> 4), new Object[0]);
                        if (!BLE.mDevicesList.contains(resultObject)) {
                            byte[] manufactureData = record.getManufacturerSpecificData(58816);
                            if (manufactureData != null && manufactureData[0] == 0) {
                                if (BLE.this.mAutoConnectScanResult == null) {
                                    ScanResult unused = BLE.this.mAutoConnectScanResult = result;
                                } else if (result.getRssi() > BLE.this.mAutoConnectScanResult.getRssi()) {
                                    ScanResult unused2 = BLE.this.mAutoConnectScanResult = result;
                                }
                                resultObject.isOnGimbal = true;
                                String unused3 = BLE.this.mAddressOfOnGimbalDevice = device.getAddress();
                                DJILogHelper.getInstance().LOGD(BLE.TAG, "placed on gimbal");
                            }
                            if (device.getBondState() != 12) {
                                z = false;
                            }
                            resultObject.isBonded = z;
                            BLE.mDevicesList.add(resultObject);
                            EventBus.getDefault().post(BLEEvent.BLE_FIND_DEVICE);
                            BLE.this.updateScanResultToListener();
                            DJILogHelper.getInstance().LOGD(BLE.TAG, "find a dji ble device");
                        }
                    }
                }
            };
            ScanFilter filter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(UUID_SERVICE)).build();
            this.mDefaultScanFilters = new ArrayList(1);
            this.mDefaultScanFilters.add(filter);
            return;
        }
        this.mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
            /* class dji.midware.ble.BLE.AnonymousClass2 */

            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (device.getName() != null && !device.getName().isEmpty() && BLE.this.byteFilter(scanRecord)) {
                    BLE.this.addDevice(device, rssi);
                }
            }
        };
    }

    /* access modifiers changed from: private */
    public boolean byteFilter(byte[] rawScanRecord) {
        if ((rawScanRecord[9] & 255) == 192 && (rawScanRecord[10] & 255) == 229) {
            return true;
        }
        return false;
    }

    public boolean init(BLEListener callback) {
        this.mCurDeviceType = getLastConnectedDeviceType();
        if (mBluetoothAdapter == null) {
            return false;
        }
        this.mBLEListener = callback;
        if (Build.VERSION.SDK_INT >= 21) {
            mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (Build.VERSION.SDK_INT < 18) {
            return true;
        }
        findSystemConnectedDevice(true);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean findSystemConnectedDevice(final boolean connect) {
        int hid = mBluetoothAdapter.getProfileConnectionState(4);
        if (hid == 2) {
            mBluetoothAdapter.getProfileProxy(mContex, new BluetoothProfile.ServiceListener() {
                /* class dji.midware.ble.BLE.AnonymousClass3 */

                public void onServiceDisconnected(int profile) {
                }

                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    boolean z = true;
                    List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                    if (mDevices == null || mDevices.size() <= 0) {
                        DJILog.d(BLE.TAG, "mDevices is null", new Object[0]);
                        return;
                    }
                    BluetoothDevice device = mDevices.get(0);
                    if (connect) {
                        BluetoothGatt unused = BLE.this.mBluetoothGatt = device.connectGatt(BLE.mContex, true, BLE.this.mGattCallback);
                        String address = device.getAddress();
                        DJILog.d(BLE.TAG, "device address:" + address, new Object[0]);
                        BLE.this.onConnect(address);
                        return;
                    }
                    BLEObject resultObject = BLE.this.generateBleObject(device, BLEEvent.BLE_DEVICE_DISCONNECTED, 0);
                    if (device.getBondState() != 12) {
                        z = false;
                    }
                    resultObject.isBonded = z;
                    DJILog.d(BLE.TAG, "resultObject:" + resultObject, new Object[0]);
                    if (!BLE.mDevicesList.contains(resultObject)) {
                        DJILog.d(BLE.TAG, "mDevicesList not contains resultObject", new Object[0]);
                        BLE.mDevicesList.add(resultObject);
                        if (BLE.this.isScanListenerAlive()) {
                            ((BLEScanListener) BLE.this.mScanResultListener.get()).onScanResultUpdate(BLE.mDevicesList);
                        }
                    }
                }
            }, 4);
            return true;
        }
        DJILog.d(TAG, "hid=" + hid + " hid != BluetoothProfile.STATE_CONNECTED", new Object[0]);
        return false;
    }

    public boolean isEnabled() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    public boolean enable() {
        if (mBluetoothAdapter == null || mBluetoothAdapter.isEnabled()) {
            return false;
        }
        mBluetoothAdapter.enable();
        return true;
    }

    public void setScanResultListener(BLEScanListener listener) {
        this.mScanResultListener = new WeakReference<>(listener);
    }

    public boolean isScanning() {
        return this.mIsScanningFromUser;
    }

    public void startScan(int duration) {
        startScan(duration, null);
    }

    public void stopScan() {
        if (isEnabled()) {
            if (!this.mIsAutoScanning && !this.mIsScanningFromUser) {
                return;
            }
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
            } else if (mScanner != null) {
                mScanner.stopScan(this.mScanCallback);
            }
        }
    }

    public void startScan(int duration, List<ScanFilter> scanFilters) {
        logD("startScan");
        if (mBluetoothAdapter.isEnabled() || mBluetoothAdapter.enable()) {
            if (this.mIsScanningFromUser) {
                stopScan();
                mDevicesList.clear();
                this.mIsScanningFromUser = false;
            }
            stopUserScanTimer();
            if (this.callbackHandler.hasMessages(4)) {
                this.callbackHandler.removeMessages(4);
            }
            if (this.mConnectionState == 1) {
                if (this.mBluetoothGatt != null) {
                    disconnect();
                }
                EventBus.getDefault().post(BLEEvent.BLE_DEVICE_DISCONNECTED);
            }
            if (scanFilters == null) {
                scanFilters = this.mDefaultScanFilters;
            }
            final List<ScanFilter> finalFilters = scanFilters;
            synchronized (this.mTimerLock) {
                this.scanTimer = new Timer();
                this.scanTimer.schedule(new TimerTask() {
                    /* class dji.midware.ble.BLE.AnonymousClass4 */

                    public void run() {
                        BluetoothDevice device;
                        if (BLE.this.mIsScanningFromUser) {
                            BLE.this.updateScanResultToListener();
                            DJILog.d(BLE.TAG, "mIsScanningFromUser = true ", new Object[0]);
                        } else {
                            BLE.this.simplyScan(finalFilters);
                            boolean unused = BLE.this.mIsScanningFromUser = true;
                            DJILog.d(BLE.TAG, "mIsScanningFromUser = false ", new Object[0]);
                        }
                        BLE.mDevicesList.clear();
                        boolean unused2 = BLE.this.findSystemConnectedDevice(false);
                        if (BLE.this.mBluetoothDeviceAddress != null && (device = BLE.mBluetoothAdapter.getRemoteDevice(BLE.this.mBluetoothDeviceAddress)) != null && BLE.this.mConnectionState == 2 && BLE.mBluetoothManager.getConnectedDevices(7).contains(device)) {
                            BLEObject resultObject = BLE.this.generateBleObject(device, BLEEvent.BLE_DEVICE_CONNECTED, 0);
                            if (!BLE.mDevicesList.contains(resultObject)) {
                                BLE.mDevicesList.add(resultObject);
                            }
                        }
                    }
                }, 0, 2000);
                this.stopTimer = new Timer();
                this.stopTimer.schedule(new TimerTask() {
                    /* class dji.midware.ble.BLE.AnonymousClass5 */

                    public void run() {
                        if (BLE.this.mIsScanningFromUser) {
                            BLE.this.stopScan();
                            boolean unused = BLE.this.mIsScanningFromUser = false;
                            DJILog.d(BLE.TAG, "stop user Scan", new Object[0]);
                            if (BLE.this.scanTimer != null) {
                                BLE.this.scanTimer.cancel();
                            }
                        }
                    }
                }, (long) (duration * 1000));
            }
        }
    }

    /* access modifiers changed from: private */
    public void simplyScan(List<ScanFilter> filters) {
        if (isEnabled()) {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(this.mLeScanCallback);
                return;
            }
            ScanSettings settings = new ScanSettings.Builder().setScanMode(2).build();
            if (mScanner == null) {
                mScanner = mBluetoothAdapter.getBluetoothLeScanner();
            }
            BluetoothLeScanner bluetoothLeScanner = mScanner;
            if (filters == null) {
                filters = this.mDefaultScanFilters;
            }
            bluetoothLeScanner.startScan(filters, settings, this.mScanCallback);
        }
    }

    /* access modifiers changed from: private */
    public void stopUserScanTimer() {
        synchronized (this.mTimerLock) {
            if (this.scanTimer != null) {
                this.scanTimer.cancel();
                this.mIsScanningFromUser = false;
            }
            if (this.stopTimer != null) {
                this.stopTimer.cancel();
            }
        }
    }

    private void updateConnectingResult() {
        if (this.mConnectionState != 1) {
            return;
        }
        if (mDevicesList.isEmpty()) {
            disconnect();
            EventBus.getDefault().post(BLEEvent.BLE_DEVICE_DISCONNECTED);
            return;
        }
        if (!mDevicesList.contains(generateBleObject(mBluetoothAdapter.getRemoteDevice(this.mBluetoothDeviceAddress), BLEEvent.BLE_DEVICE_DISCONNECTED, 0))) {
            DJILogHelper.getInstance().LOGD(TAG, "timeout", false, true);
            disconnect();
            EventBus.getDefault().post(BLEEvent.BLE_DEVICE_DISCONNECTED);
        }
    }

    /* access modifiers changed from: private */
    public void updateScanResultToListener() {
        if (isScanListenerAlive()) {
            if (!mDevicesList.isEmpty()) {
                Collections.sort(mDevicesList);
                DJILog.d(TAG, "mDevicesList is not empty", new Object[0]);
            } else {
                DJILog.d(TAG, "mDevicesList is empty", new Object[0]);
            }
            if (isScanListenerAlive()) {
                this.mScanResultListener.get().onScanResultUpdate(mDevicesList);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isScanListenerAlive() {
        return (this.mScanResultListener == null || this.mScanResultListener.get() == null) ? false : true;
    }

    public ArrayList<BLEObject> getDevices() {
        return mDevicesList;
    }

    public void startAutoScan() {
        if (this.mAutoScanTimer == null) {
            this.mAutoScanTimer = new Timer();
            this.mAutoScanTimer.schedule(new TimerTask() {
                /* class dji.midware.ble.BLE.AnonymousClass6 */

                public void run() {
                    if (!BLE.this.mIsScanningFromUser) {
                        DJILogHelper.getInstance().LOGD(BLE.TAG, "startAuto Scan", false, false);
                        BLE.mDevicesList.clear();
                        for (BluetoothDevice d : BLE.mBluetoothManager.getConnectedDevices(7)) {
                            BLE.mDevicesList.add(BLE.this.generateBleObject(d, BLEEvent.BLE_DEVICE_CONNECTED, 0));
                        }
                        BLE.this.simplyScan(null);
                        boolean unused = BLE.this.mIsAutoScanning = true;
                    }
                    new Timer().schedule(new TimerTask() {
                        /* class dji.midware.ble.BLE.AnonymousClass6.AnonymousClass1 */

                        public void run() {
                            if (BLE.this.mIsAutoScanning && !BLE.this.mIsScanningFromUser) {
                                BLE.this.stopScan();
                                boolean unused = BLE.this.mIsAutoScanning = false;
                                if (BLE.this.isScanListenerAlive()) {
                                    BLE.this.updateScanResultToListener();
                                }
                                DJILogHelper.getInstance().LOGD(BLE.TAG, "BLE auto scan stop", false, false);
                            }
                        }
                    }, (long) IEventObjects.PopViewItem.DURATION_DISAPPEAR);
                }
            }, 1000, 10000);
        }
    }

    public void cancleAutoScan() {
        if (this.mAutoScanTimer != null) {
            this.mAutoScanTimer.cancel();
            this.mIsAutoScanning = false;
            this.mAutoScanTimer = null;
        }
    }

    public String getAddress() {
        return this.mBluetoothDeviceAddress;
    }

    public String getLastConnectedAddress() {
        return mContex.getSharedPreferences(mContex.getPackageName(), 0).getString(BLE_CONNECTED_ADDRESS, "");
    }

    public String getAddressOnGimbalDevice() {
        return this.mAddressOfOnGimbalDevice;
    }

    public String getNameofAddress(String address) {
        String name = mBluetoothAdapter.getRemoteDevice(address).getName();
        return name == null ? "" : name;
    }

    public String getCurConnectedName() {
        String name;
        if (!isConnected() || TextUtils.isEmpty(this.mBluetoothDeviceAddress) || (name = mBluetoothAdapter.getRemoteDevice(this.mBluetoothDeviceAddress).getName()) == null) {
            return "";
        }
        return name;
    }

    /* access modifiers changed from: private */
    public void addDevice(BluetoothDevice device, int rssi) {
        BLEObject resultObject = generateBleObject(device, BLEEvent.BLE_DEVICE_DISCONNECTED, rssi);
        if (!mDevicesList.contains(resultObject)) {
            mDevicesList.add(resultObject);
        }
        EventBus.getDefault().post(BLEEvent.BLE_FIND_DEVICE);
    }

    public void connect(String adrress, BLEDeviceConnectionCallback callback) {
        this.connectionCallback = callback;
        this.callbackHandler.sendEmptyMessage(0);
        connect(adrress);
    }

    public boolean connect(String address) {
        logD("start connect");
        if (mBluetoothAdapter == null) {
            logE("mothod : connectBluetoothAdapter null");
        }
        if (V_StringUtils.isEmpty(address)) {
            logE("mothod : connectaddress empty");
        }
        stopScan();
        stopUserScanTimer();
        cancleAutoScan();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        int connectionState = mBluetoothManager.getConnectionState(device, 7);
        if (connectionState == 0) {
            if (this.mBluetoothDeviceAddress != null && address.equals(this.mBluetoothDeviceAddress) && this.mBluetoothGatt != null) {
                DJILog.d(TAG, "Trying to use an existing mBluetoothGatt for connection.", new Object[0]);
                if (this.mBluetoothGatt.connect()) {
                    onConnect(address);
                    return true;
                }
                logE("mothod : connect -> connect failed!");
                return false;
            } else if (device == null) {
                logE("mothod : connect -> device is null");
                return false;
            } else {
                this.mBluetoothGatt = device.connectGatt(mContex, true, this.mGattCallback);
                if (this.mBluetoothGatt == null) {
                    logE("mothod : connect -> mBluetoothGatt == null");
                    return true;
                }
                onConnect(address);
                return true;
            }
        } else if (connectionState == 1) {
            onConnect(address);
            logE("mothod : connect -> connectionState == BluetoothProfile.STATE_CONNECTING");
            return true;
        } else {
            if (this.mBluetoothGatt != null) {
                disconnect();
            }
            onConnect(address);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                /* class dji.midware.ble.BLE.AnonymousClass7 */

                public void run() {
                    BluetoothGatt unused = BLE.this.mBluetoothGatt = device.connectGatt(BLE.mContex, true, BLE.this.mGattCallback);
                }
            }, 2000);
            logE("mothod : connect -> connectionState == BluetoothProfile.STATE_CONNECTED");
            return true;
        }
    }

    public static BLEObject getAutoConnectableOne(ArrayList<BLEObject> list, boolean isSorted) {
        BLEObject rightObject = new BLEObject();
        if (list.size() == 1) {
            return list.get(0);
        }
        if (list.size() <= 1) {
            return rightObject;
        }
        BLEObject onGimbalObject = null;
        BLEObject bondedObject = null;
        Iterator<BLEObject> it2 = list.iterator();
        while (it2.hasNext()) {
            BLEObject o = it2.next();
            if (o.isOnGimbal) {
                onGimbalObject = o;
            }
            if (o.isBonded) {
                bondedObject = o;
            }
        }
        if (bondedObject != null) {
            return onGimbalObject;
        }
        if (onGimbalObject != null) {
            return bondedObject;
        }
        if (!isSorted) {
            Collections.sort(list);
        }
        return list.get(0);
    }

    public void disconnect(BLEDeviceConnectionCallback callback) {
        this.connectionCallback = callback;
        this.callbackHandler.sendEmptyMessage(0);
        disconnect();
    }

    public void disconnect() {
        logD("disconnect");
        if (mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            DJILog.d(TAG, "BluetoothAdapter not initialized", new Object[0]);
            return;
        }
        this.mBluetoothGatt.disconnect();
        this.mBluetoothGatt.close();
        onDisconnected();
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            device.getClass().getMethod("removeBond", null).invoke(device, null);
        } catch (Exception e) {
            DJILog.e(TAG, e.getMessage(), new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    public boolean enableNotification() {
        BluetoothGattService service;
        if (this.mBluetoothGatt == null) {
            service = null;
        } else {
            service = this.mBluetoothGatt.getService(UUID.fromString(UUID_SERVICE));
        }
        this.mGattService = service;
        if (this.mGattService != null) {
            BluetoothGattCharacteristic readData = this.mGattService.getCharacteristic(UUID.fromString(this.UUID_CHAR_READ));
            if (readData != null) {
                this.mBluetoothGatt.setCharacteristicNotification(readData, true);
                BluetoothGattDescriptor config = readData.getDescriptor(UUID.fromString(this.UUID_DESCRIPTOR_READ));
                if (config != null) {
                    config.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    if (!this.mBluetoothGatt.writeDescriptor(config)) {
                        this.callbackHandler.sendMessageDelayed(this.callbackHandler.obtainMessage(4, 1, 0, config), 50);
                        this.callbackHandler.postDelayed(new Runnable() {
                            /* class dji.midware.ble.BLE.AnonymousClass8 */

                            public void run() {
                                BLE.this.logE("mothod : enableNotification - > timeout fail");
                                BLE.this.callbackHandler.removeMessages(4);
                                BLE.this.disconnect();
                            }
                        }, 15000);
                        logE("mothod : enableNotification -> writeResult false");
                    } else {
                        onConnected(this.mBluetoothGatt, 0, 2);
                        return true;
                    }
                } else {
                    logE("mothod : enableNotification -> config == null");
                    disconnect();
                }
            } else {
                logE("mothod : enableNotification -> readData == null");
                disconnect();
            }
        } else {
            logE("mothod : enableNotification -> mGattService == null");
            disconnect();
        }
        return false;
    }

    public boolean writeCharacteristic(byte[] data) {
        try {
            if (this.mWriteCharacteristic != null) {
                this.mWriteCharacteristic.setValue(data);
                return this.mBluetoothGatt.writeCharacteristic(this.mWriteCharacteristic);
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean writeCharacteristicNonBlock(byte[] data) {
        if (this.procQueue == null) {
            return false;
        }
        bleRequest req = new bleRequest();
        req.data = data;
        req.time = System.currentTimeMillis();
        addRequestToQueue(req);
        return true;
    }

    /* access modifiers changed from: private */
    public void onConnected(BluetoothGatt gatt, int status, int newState) {
        this.mLinkStatus = LinkDataState.NORMAL;
        startSendtaskThread();
        EventBus.getDefault().post(BLEEvent.BLE_DEVICE_CONNECTED);
        if (this.mBluetoothDeviceAddress == null) {
            this.mBluetoothDeviceAddress = getLastConnectedAddress();
            if (V_StringUtils.isEmpty(this.mBluetoothDeviceAddress)) {
                this.mBluetoothDeviceAddress = gatt.getDevice().getAddress();
                mContex.getSharedPreferences(mContex.getPackageName(), 0).edit().putString(BLE_CONNECTED_ADDRESS, this.mBluetoothDeviceAddress).commit();
            }
        }
        notifyReconnectConnected();
        if (this.callbackHandler != null && this.callbackHandler.hasMessages(5)) {
            this.callbackHandler.removeMessages(5);
        }
        if (this.callbackHandler != null && this.callbackHandler.hasMessages(6)) {
            this.callbackHandler.removeMessages(6);
        }
        if (this.callbackHandler != null && this.callbackHandler.hasMessages(1)) {
            this.callbackHandler.sendEmptyMessage(2);
        }
        this.mBLEListener.onConnectionStateChange(gatt, status, newState);
    }

    /* access modifiers changed from: private */
    public BLEObject generateBleObject(BluetoothDevice device, BLEEvent event, int rssi) {
        BLEObject o = new BLEObject();
        o.address = device.getAddress();
        o.name = device.getName();
        o.event = event;
        o.rssi = rssi;
        o.isBonded = device.getBondState() == 12;
        return o;
    }

    public boolean isConnected() {
        return this.mConnectionState == 2;
    }

    /* access modifiers changed from: private */
    public void onConnect(String address) {
        this.mBluetoothDeviceAddress = address;
        this.mConnectionState = 1;
        EventBus.getDefault().post(BLEEvent.BLE_DEVICE_CONNECTING);
        mContex.getSharedPreferences(mContex.getPackageName(), 0).edit().putString(BLE_CONNECTED_ADDRESS, this.mBluetoothDeviceAddress).commit();
    }

    /* access modifiers changed from: private */
    public void onDisconnected() {
        this.mBluetoothDeviceAddress = null;
        this.mConnectionState = 0;
        cancleScan();
        mDevicesList.clear();
        this.mBLEListener.onConnectionStateChange(this.mBluetoothGatt, 3, 0);
        EventBus.getDefault().post(BLEEvent.BLE_DEVICE_DISCONNECTED);
        DJILogHelper.getInstance().LOGD(TAG, "STATE_DISCONNECTED", false, false);
        if (this.callbackHandler != null && this.callbackHandler.hasMessages(1)) {
            this.callbackHandler.sendEmptyMessage(3);
        }
    }

    public boolean checkGatt() {
        if (mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            return false;
        }
        return true;
    }

    public void destroy() {
        stopScan();
        mDevicesList.clear();
        cancleScan();
        this.mIsScanningFromUser = false;
        this.mIsAutoScanning = false;
        mDevicesList.clear();
        if (this.mBluetoothGatt != null) {
            this.mBluetoothGatt.close();
            this.mBluetoothGatt = null;
        }
        this.mConnectionState = 0;
        this.callbackHandler.removeCallbacksAndMessages(null);
    }

    public void cancleScan() {
        cancleAutoScan();
        stopScan();
        stopUserScanTimer();
    }

    public boolean refreshDeviceCache() {
        if (this.mBluetoothGatt != null) {
            try {
                Method localMethod = this.mBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
                if (localMethod != null) {
                    return ((Boolean) localMethod.invoke(this.mBluetoothGatt, new Object[0])).booleanValue();
                }
                return false;
            } catch (Exception e) {
                DJILog.e(TAG, "An exception occured while refreshing device", new Object[0]);
                return false;
            }
        } else {
            logE("mothod : refreshDeviceCache -> mBluetoothGatt == null");
            return false;
        }
    }

    private void startSendtaskThread() {
        this.procQueue = new LinkedList<>();
        new Thread() {
            /* class dji.midware.ble.BLE.AnonymousClass10 */

            public void run() {
                DJILog.d(BLE.TAG, "queueThread started", new Object[0]);
                while (BLE.this.isConnected()) {
                    BLE.this.executeQueue();
                    try {
                        Thread.sleep((long) BLE.this.mSleepTime, 500000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                DJILog.d(BLE.TAG, "queueThread finished", new Object[0]);
            }
        }.start();
    }

    public class bleRequest {
        public byte[] data;
        public long time;

        public bleRequest() {
        }
    }

    /* access modifiers changed from: private */
    public void executeQueue() {
        this.lock.lock();
        try {
            if (this.procQueue != null) {
                if (this.procQueue.size() == 0) {
                    this.lock.unlock();
                    return;
                }
                bleRequest procReq = this.procQueue.peekFirst();
                this.sendResult = sendNonBlockingWriteRequest(procReq);
                if (this.sendResult == 0) {
                    this.procQueue.removeFirst();
                    this.mLinkStatus = LinkDataState.NORMAL;
                } else if (System.currentTimeMillis() - procReq.time > ((long) this.BLT_TIMEOUT)) {
                    logE("remove form queue for timeout,cur queue size:" + this.procQueue.size());
                    this.procQueue.clear();
                    this.mLinkStatus = LinkDataState.ERROR;
                }
                if (this.procQueue.size() > 500) {
                    logE("procQueue.size() too large(500)");
                }
                this.lock.unlock();
            }
        } finally {
            this.lock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean addRequestToQueue(bleRequest req) {
        if (!checkGatt()) {
            return false;
        }
        this.lock.lock();
        try {
            this.procQueue.add(req);
            this.lock.unlock();
            return true;
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    public int sendNonBlockingWriteRequest(bleRequest request) {
        if (!checkGatt()) {
            return -2;
        }
        if (writeCharacteristic(request.data)) {
            return 0;
        }
        return -1;
    }

    private void countSendSpeed() {
        if (System.currentTimeMillis() - this.mPreTime > 5000) {
            logD("send speed:" + (((float) this.mSendBits) / 5000.0f) + "kb/s");
            this.mSendBits = 0;
            this.mPreTime = System.currentTimeMillis();
        }
    }

    private class CallbackHandler extends Handler {
        public CallbackHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    BLE.this.callbackHandler.sendEmptyMessageDelayed(1, 10000);
                    return;
                case 1:
                    if (BLE.this.connectionCallback != null) {
                        BLE.this.connectionCallback.onResult(BLEConnectionError.BLE_CONNECTION_TIMEOUT);
                        return;
                    }
                    return;
                case 2:
                    BLE.this.callbackHandler.removeMessages(1);
                    if (BLE.this.connectionCallback != null) {
                        BLE.this.connectionCallback.onResult(BLEConnectionError.BLE_CONNECTION_CONNECTED);
                        return;
                    }
                    return;
                case 3:
                    BLE.this.callbackHandler.removeMessages(1);
                    if (BLE.this.connectionCallback != null) {
                        BLE.this.connectionCallback.onResult(BLEConnectionError.BLE_CONNECTION_DISCONNECTED);
                        return;
                    }
                    return;
                case 4:
                    BLE.this.logD("retry to writeDescriptor");
                    if (msg.arg1 == 1 && BLE.this.mBluetoothGatt != null) {
                        if (!BLE.this.mBluetoothGatt.writeDescriptor((BluetoothGattDescriptor) msg.obj)) {
                            BLE.this.callbackHandler.sendMessageDelayed(BLE.this.callbackHandler.obtainMessage(4, 1, 0, msg.obj), 200);
                            return;
                        } else {
                            BLE.this.onConnected(BLE.this.mBluetoothGatt, 0, 2);
                            return;
                        }
                    } else {
                        return;
                    }
                case 5:
                    if (BLE.this.isConnected()) {
                        BLE.this.notifyReconnectConnected();
                        return;
                    } else if (BLE.this.isConnecting()) {
                        BLE.this.callbackHandler.sendMessageDelayed(BLE.this.callbackHandler.obtainMessage(6), 30000);
                        return;
                    } else {
                        BLE.this.notifyReconnectTimeout();
                        return;
                    }
                case 6:
                    if (BLE.this.isConnected()) {
                        BLE.this.notifyReconnectConnected();
                        return;
                    } else if (BLE.this.isConnecting()) {
                        BLE.this.disconnect();
                        BLE.this.notifyReconnectTimeout();
                        return;
                    } else {
                        BLE.this.notifyReconnectTimeout();
                        return;
                    }
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void notifyReconnectConnected() {
        synchronized (this.mReconnectLock) {
            if (!(this.mReconnectListeners == null || this.mReconnectListeners.size() == 0)) {
                for (BLEReconnectListener reconnectListener : this.mReconnectListeners) {
                    reconnectListener.onConnected();
                }
                this.mReconnectListeners.clear();
            }
        }
    }

    /* access modifiers changed from: private */
    public void notifyReconnectTimeout() {
        synchronized (this.mReconnectLock) {
            if (!(this.mReconnectListeners == null || this.mReconnectListeners.size() == 0)) {
                for (BLEReconnectListener reconnectListener : this.mReconnectListeners) {
                    reconnectListener.onTimeout();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void logD(String message) {
        DJILogHelper.getInstance().LOGD(TAG, "DJIBLE DEBUG::" + message, LOGNAME);
    }

    /* access modifiers changed from: private */
    public void logE(String message) {
        DJILogHelper.getInstance().LOGE(TAG, "DJIBLE ERROR::" + message, LOGNAME);
    }

    public int getConnectingStatus(String address) {
        if (mBluetoothAdapter == null || mBluetoothManager == null || TextUtils.isEmpty(address)) {
            return 0;
        }
        return mBluetoothManager.getConnectionState(mBluetoothAdapter.getRemoteDevice(address), 7);
    }

    public void setHighSpeedMode(boolean enable) {
        if (Build.VERSION.SDK_INT < 21 || enable) {
        }
    }

    public void tryReconnect(BLEReconnectListener listener) {
        DJILog.d("tryReconnect", "start tryReconnect", new Object[0]);
        synchronized (this.mReconnectLock) {
            if (!(this.mReconnectListeners == null || listener == null || this.mReconnectListeners.contains(listener))) {
                this.mReconnectListeners.add(listener);
            }
        }
        if (this.callbackHandler != null && !this.callbackHandler.hasMessages(5) && !this.callbackHandler.hasMessages(6)) {
            this.callbackHandler.sendMessageDelayed(this.callbackHandler.obtainMessage(5), 10000);
            if (!isBusy()) {
                setScanResultListener(new BLEScanListener() {
                    /* class dji.midware.ble.BLE.AnonymousClass11 */

                    public void onScanResultUpdate(ArrayList<BLEObject> list) {
                        if (!BLE.this.isConnected() && !list.isEmpty()) {
                            Iterator<BLEObject> it2 = list.iterator();
                            while (it2.hasNext()) {
                                BLEObject object = it2.next();
                                if (BLE.this.getLastConnectedAddress().equals(object.address)) {
                                    BLE.this.connect(object.address);
                                    return;
                                }
                            }
                        }
                    }
                });
                startScan(5);
                return;
            }
            DJILog.d("tryReconnect", "isBusy:" + isBusy() + "mConnectionState: " + this.mConnectionState, new Object[0]);
        }
    }

    public void removeReconnectListener(BLEReconnectListener listener) {
        synchronized (this.mReconnectLock) {
            if (this.mReconnectListeners != null && this.mReconnectListeners.contains(listener)) {
                this.mReconnectListeners.remove(listener);
            }
        }
    }

    public boolean isBusy() {
        return isScanning() || this.mConnectionState == 2 || this.mConnectionState == 1;
    }

    public boolean isConnecting() {
        return this.mConnectionState == 1;
    }

    public boolean isLinkError() {
        return isConnected() && this.mLinkStatus == LinkDataState.ERROR;
    }
}
