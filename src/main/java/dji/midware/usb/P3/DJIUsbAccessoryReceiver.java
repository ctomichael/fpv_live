package dji.midware.usb.P3;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.receiver.DJIUsbAccessoryAccessManager;
import dji.midware.aoabridge.AoaController;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Keep
@EXClassNullAway
public class DJIUsbAccessoryReceiver extends BroadcastReceiver {
    public static final String ACTION_USB_ACCESSORY_ATTACHED = "com.dji.v4.accessory.USB_ACCESSORY_ATTACHED";
    public static final String ACTION_USB_PERMISSION = "com.dji.v4.accessory.USB";
    public static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";
    private static final int MSG_CHECK_USB_DEVICE_INITIACTIVE = 1;
    public static final String myFacturer = "DJI";
    private final String TAG = "DJIUsbAccessoryReceiver";
    private Context context;
    private UsbModel currentModel = UsbModel.UNKNOWN;
    /* access modifiers changed from: private */
    public Handler handler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
        /* class dji.midware.usb.P3.DJIUsbAccessoryReceiver.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!DJIUSBWifiSwitchManager.getInstance().isWifiConnected()) {
                        if (DJIUsbAccessoryReceiver.this.myAccessory == null && DJIUsbAccessoryReceiver.this.isAccessoryDetached && DJIUsbAccessoryReceiver.this.checkMyDevice(false)) {
                            DJIUsbAccessoryReceiver.this.connected();
                        }
                        DJIUsbAccessoryReceiver.this.printUI("check accessory handler");
                        break;
                    } else {
                        DJIUsbAccessoryReceiver.this.handler.sendEmptyMessageDelayed(1, 2000);
                        break;
                    }
            }
            return false;
        }
    });
    /* access modifiers changed from: private */
    public volatile boolean isAccessoryDetached = true;
    private boolean isWaitingForDisconnect = false;
    private ParcelFileDescriptor mFileDescriptor;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    /* access modifiers changed from: private */
    public volatile UsbAccessory myAccessory;
    private UsbManager usbManager;

    public enum UsbModel {
        AG("AG410"),
        WM160(DJICameraAbstraction.DisplayNameWM160),
        UNKNOWN("Unknown");
        
        private String value;

        private UsbModel(String value2) {
            this.value = value2;
        }

        public static UsbModel find(String modelName) {
            UsbModel result = UNKNOWN;
            if (TextUtils.isEmpty(modelName)) {
                return result;
            }
            int i = 0;
            while (true) {
                if (i >= values().length) {
                    break;
                } else if (values()[i].value.equals(modelName)) {
                    result = values()[i];
                    break;
                } else {
                    i++;
                }
            }
            return result;
        }

        public String getModel() {
            return this.value;
        }
    }

    public void start(Context context2) {
        this.context = context2;
        this.usbManager = (UsbManager) context2.getSystemService("usb");
        printUI("start accessory receiver");
        AoaController.get().init(context2);
        if (!AoaController.get().isApp()) {
            this.handler.sendEmptyMessageDelayed(1, 2000);
        } else {
            DJIEventBusUtil.register(this);
        }
    }

    public void onReceive(Context context2, Intent intent) {
        Bundle bundle;
        String action = intent.getAction();
        printUI("receive action: " + action);
        if (ACTION_USB_STATE.equals(action) && (bundle = intent.getExtras()) != null) {
            if (bundle.getBoolean("connected")) {
                printUI(action + ": is connected");
            } else {
                printUI(action + ": no connected");
            }
        }
        if (DJIUSBWifiSwitchManager.getInstance().isWifiConnected() && !action.equals("android.hardware.usb.action.USB_ACCESSORY_DETACHED")) {
            printUI("*****in WifiService connect");
        } else if (action.equals(ACTION_USB_PERMISSION)) {
            if (!this.usbManager.hasPermission(this.myAccessory)) {
                printUI("no usbAccessory permission, try to check delay");
                this.handler.sendEmptyMessageDelayed(1, 300);
                return;
            }
            printUI("has usbAccessory permission");
            connected();
        } else if (action.equals(ACTION_USB_ACCESSORY_ATTACHED)) {
            toConnect(true);
        } else if (action.equals("android.hardware.usb.action.USB_ACCESSORY_ATTACHED")) {
            toConnect(true);
        } else if (action.equals("com.dji.accessory.USB_ACCESSORY_ATTACHED")) {
            toConnect(true);
        } else if (action.equals("android.hardware.usb.action.USB_ACCESSORY_DETACHED")) {
            triggerDisconnect();
            toConnect(false);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void toConnect(boolean toRequestPermission) {
        if (this.mFileDescriptor == null && checkMyDevice(toRequestPermission)) {
            connected();
        }
    }

    /* access modifiers changed from: protected */
    public void disconnected() {
        DJIUSBWifiSwitchManager.getInstance().resetSwitchFromWifiFlag();
        if (!DJIUSBWifiSwitchManager.getInstance().isWifiConnected()) {
            destroySession();
            UsbAccessoryService.getInstance().onDisconnect();
            EventBus.getDefault().post(DataEvent.ConnectLose);
            printUI("receiver disconnected");
        }
    }

    private synchronized void connectedToAoaBright() {
        try {
            this.handler.removeMessages(1);
            this.isAccessoryDetached = false;
            this.mInputStream = AoaController.get().getInputStream();
            this.mOutputStream = AoaController.get().getOutputStream();
            UsbAccessoryService.getInstance().startThreads();
            EventBus.getDefault().post(DataEvent.ConnectOK);
        } catch (Exception e) {
            printUI("aoa connect error " + e.getMessage());
        }
        return;
    }

    /* access modifiers changed from: private */
    public synchronized void connected() {
        if (this.myAccessory == null || this.isAccessoryDetached || this.mFileDescriptor == null) {
            try {
                printUI("openAccessory: " + this.myAccessory);
                this.mFileDescriptor = this.usbManager.openAccessory(this.myAccessory);
                if (this.mFileDescriptor != null) {
                    this.handler.removeMessages(1);
                    this.isAccessoryDetached = false;
                    FileDescriptor fd = this.mFileDescriptor.getFileDescriptor();
                    this.mInputStream = new FileInputStream(fd);
                    this.mOutputStream = new FileOutputStream(fd);
                    printUI("mFileDescriptor: mInputStream=" + this.mInputStream);
                    printUI("mFileDescriptor: FileDescriptor=" + fd.valid());
                    UsbAccessoryService.getInstance().startThreads();
                    EventBus.getDefault().post(DataEvent.ConnectOK);
                    this.isWaitingForDisconnect = false;
                } else {
                    printUI("mFileDescriptor: null");
                    if (!this.isWaitingForDisconnect) {
                        this.context.sendBroadcast(new Intent(DJIUsbAccessoryAccessManager.ACTION_SDK_ACCESS_REQUESTED));
                        this.isWaitingForDisconnect = true;
                    }
                }
            } catch (Exception e) {
                printUI("aoa connect error " + e.getMessage());
            }
        }
        return;
    }

    /* access modifiers changed from: private */
    public synchronized boolean checkMyDevice(boolean toRequestPermission) {
        boolean z = false;
        synchronized (this) {
            if (AoaController.get().isApp()) {
                z = true;
            } else if (this.myAccessory == null || this.isAccessoryDetached) {
                UsbAccessory[] list = null;
                try {
                    list = this.usbManager.getAccessoryList();
                } catch (Exception e) {
                    printUI("usbManager.getAccessoryList exception");
                }
                if (list != null) {
                    if (list.length > 0) {
                        UsbAccessory accessory = list[0];
                        if (accessory == null) {
                            printUI("accessory null");
                        } else {
                            z = checkPremission(accessory, toRequestPermission);
                        }
                    }
                }
            }
        }
        return z;
    }

    private boolean checkPremission(UsbAccessory accessory, boolean toRequest) {
        String model = accessory.getModel();
        String facturer = accessory.getManufacturer();
        this.currentModel = UsbModel.find(model);
        printUI("getModel: " + model);
        printUI("currentModel saved: " + this.currentModel);
        printUI("getManufacturer: " + facturer);
        if (UsbModel.UNKNOWN.getModel().equals(model) || !myFacturer.equals(facturer)) {
            printUI("不匹配 ");
        } else if (this.usbManager.hasPermission(accessory)) {
            this.myAccessory = accessory;
            printUI("hasPermission ");
            return true;
        } else {
            printUI("requestPermission ");
            if (toRequest) {
                requestPremission(accessory);
            }
        }
        return false;
    }

    private void requestPremission(UsbAccessory accessory) {
        this.usbManager.requestPermission(accessory, PendingIntent.getBroadcast(this.context, 0, new Intent(ACTION_USB_PERMISSION), 0));
    }

    /* access modifiers changed from: protected */
    public InputStream getInputStream() {
        return this.mInputStream;
    }

    /* access modifiers changed from: protected */
    public OutputStream getOutputStream() {
        return this.mOutputStream;
    }

    /* access modifiers changed from: protected */
    public UsbModel getCurrentModel() {
        return this.currentModel;
    }

    /* access modifiers changed from: protected */
    public synchronized void triggerDisconnect() {
        if (!this.isAccessoryDetached) {
            this.isAccessoryDetached = true;
            disconnected();
            if (this.myAccessory == null) {
                clearTimer();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void destroy() {
        print("receiver destroy");
        clearTimer();
        destroySession();
    }

    /* access modifiers changed from: protected */
    public void clearTimer() {
        if (this.handler != null) {
            this.handler.removeMessages(1);
        }
    }

    /* access modifiers changed from: protected */
    public void destroySession() {
        DJIUSBWifiSwitchManager.getInstance().resetSwitchFromWifiFlag();
        if (!DJIUSBWifiSwitchManager.getInstance().isWifiConnected()) {
            try {
                if (this.mOutputStream != null) {
                    if (!AoaController.get().isApp()) {
                        this.mOutputStream.close();
                    }
                    this.mOutputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (this.mInputStream != null) {
                    if (!AoaController.get().isApp()) {
                        this.mInputStream.close();
                    }
                    this.mInputStream = null;
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            try {
                if (this.mFileDescriptor != null) {
                    this.mFileDescriptor.close();
                    this.mFileDescriptor = null;
                    this.context.sendBroadcast(new Intent(DJIUsbAccessoryAccessManager.ACTION_SDK_ACCESS_RELEASED));
                }
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            this.myAccessory = null;
            this.isAccessoryDetached = true;
            if (this.handler != null) {
                clearTimer();
            }
            printUI("receiver destroySession");
        }
    }

    public void unregisterReceiver() {
        if (this.context != null) {
            try {
                this.context.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                print(e.getMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public void printUI(String s) {
        DJILogHelper.getInstance().LOGE("DJIUsbAccessoryReceiver", s, true, true);
        DJILog.saveConnectDebug("DJIUsbAccessoryReceiver " + s);
    }

    private void print(String s) {
        DJILogHelper.getInstance().LOGE("DJIUsbAccessoryReceiver", s, false, false);
    }

    public boolean isGetedConnection() {
        if (AoaController.get().isApp()) {
            return AoaController.get().isRcConnected();
        }
        return this.mFileDescriptor != null;
    }

    public boolean isWaitingForDisconnect() {
        return this.isWaitingForDisconnect;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(AoaController.RcEvent event) {
        Log.d("AppEventClient", "event:" + event);
        if (AoaController.get().isEnable() && AoaController.get().isApp()) {
            if (event == AoaController.RcEvent.Connected) {
                connectedToAoaBright();
                Log.d("AppEventClient", "event: connectedToAoaBright");
                return;
            }
            disconnected();
            Log.d("AppEventClient", "event: disconnected");
        }
    }
}
