package dji.midware.usbhost.P3;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DataEvent;
import java.util.HashMap;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class DJIUsbReceiver extends BroadcastReceiver {
    public static final String ACTION_USB_PERMISSION = "com.dji.host.USB";
    private static final int PID = 4098;
    private static final int VID = 1351;
    private final String TAG = getClass().getSimpleName();
    private UsbDeviceConnection conn;
    private Context context;
    private UsbDevice myUsbDevice;
    private UsbEndpoint osdEndpoint;
    private UsbEndpoint outEndpoint;
    private UsbInterface usbInterface;
    private UsbManager usbManager;
    private UsbEndpoint vodEndpoint;

    public void start(Context context2) {
        this.context = context2;
        this.usbManager = (UsbManager) context2.getSystemService("usb");
        if (checkMyDevice()) {
            connected();
        }
    }

    public void onReceive(Context context2, Intent intent) {
        String action = intent.getAction();
        printUI(action);
        if (action.equals(ACTION_USB_PERMISSION)) {
            if (!this.usbManager.hasPermission(this.myUsbDevice)) {
                printUI("no usbhost permission");
                return;
            }
            printUI("has usbhost permission");
            connected();
        } else if (action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
            if (checkMyDevice()) {
                connected();
            }
        } else if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
            disconnected();
        }
    }

    private void disconnected() {
        if (this.conn != null) {
            this.conn.releaseInterface(this.usbInterface);
            this.conn.close();
            this.conn = null;
        }
        DJILog.saveConnectDebug("dji usb receiver post connect lose!");
        EventBus.getDefault().post(DataEvent.ConnectLose);
    }

    private void connected() {
        UsbDeviceConnection connection;
        if (this.conn == null && (connection = this.usbManager.openDevice(this.myUsbDevice)) != null) {
            if (this.myUsbDevice.getInterfaceCount() <= 0) {
                System.out.println("error ,could't find usb interface !!");
                return;
            }
            printUI(String.format(Locale.US, "UsbRunnable getInterfaceCount %d", Integer.valueOf(this.myUsbDevice.getInterfaceCount())));
            this.usbInterface = this.myUsbDevice.getInterface(0);
            if (connection.claimInterface(this.usbInterface, true)) {
                this.conn = connection;
                getEndPoints(this.usbInterface);
                UsbHostService.getInstance().startThreads();
                EventBus.getDefault().post(DataEvent.ConnectOK);
                return;
            }
            connection.close();
        }
    }

    private boolean checkMyDevice() {
        HashMap<String, UsbDevice> deviceList = this.usbManager.getDeviceList();
        System.out.println("usbdevice size=" + deviceList.size() + "");
        if (deviceList.size() <= 0) {
            this.myUsbDevice = null;
            return false;
        }
        printUI("usbdevice size=" + deviceList.size() + "");
        for (UsbDevice device : deviceList.values()) {
            printUI("VID=" + device.getVendorId() + " PID=" + device.getProductId() + "");
            if (device.getVendorId() == VID && device.getProductId() == 4098) {
                this.myUsbDevice = device;
                if (this.usbManager.hasPermission(this.myUsbDevice)) {
                    return true;
                }
                this.usbManager.requestPermission(this.myUsbDevice, PendingIntent.getBroadcast(this.context, 0, new Intent(ACTION_USB_PERMISSION), 0));
            }
        }
        return false;
    }

    private void getEndPoints(UsbInterface usbInterface2) {
        printUI(String.format(Locale.US, "UsbRunnable getEndpointCount %d", Integer.valueOf(usbInterface2.getEndpointCount())));
        for (int i = 0; i < usbInterface2.getEndpointCount(); i++) {
            UsbEndpoint usbEndpoint = usbInterface2.getEndpoint(i);
            if (usbEndpoint.getType() == 2) {
                int address = usbInterface2.getEndpoint(i).getAddress();
                print("endpoint getAddress=" + address);
                if (address == 134) {
                    this.vodEndpoint = usbEndpoint;
                    printUI("get vodEndpoint");
                } else if (address == 136) {
                    this.osdEndpoint = usbEndpoint;
                    printUI("get osdEndpoint");
                } else if (address == 4) {
                    this.outEndpoint = usbEndpoint;
                    printUI("get outEndpoint");
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isGetedConnection() {
        return this.conn != null;
    }

    /* access modifiers changed from: protected */
    public synchronized UsbDeviceConnection getConnection() {
        return this.conn;
    }

    /* access modifiers changed from: protected */
    public UsbEndpoint getVodEndpoint() {
        return this.vodEndpoint;
    }

    /* access modifiers changed from: protected */
    public UsbEndpoint getOsdEndpoint() {
        return this.osdEndpoint;
    }

    /* access modifiers changed from: protected */
    public UsbEndpoint getOutEndpoint() {
        return this.outEndpoint;
    }

    /* access modifiers changed from: protected */
    public void destroy() {
        if (this.conn != null) {
            this.conn.releaseInterface(this.usbInterface);
            this.conn.close();
            this.conn = null;
        }
    }

    private void printUI(String s) {
        DJILogHelper.getInstance().LOGE(this.TAG, s, false, true);
    }

    private void print(String s) {
        DJILogHelper.getInstance().LOGE(this.TAG, s, false, false);
    }
}
