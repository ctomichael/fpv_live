package dji.midware;

import android.content.Context;
import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.Handheld.DJIHandheldHelper;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DJIProductSupportModel;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoDataRecver;
import dji.midware.natives.FPVController;
import dji.midware.sdk.InternalDataProtectionGuard;
import dji.midware.upgradeComponent.DJIUpgradeComponentManager;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.wsbridge.BridgeWSConnectionManager;
import java.lang.ref.WeakReference;

@EXClassNullAway
public class MidWare {
    public static final boolean GO = true;
    public static final boolean Pilot = false;
    public static String bridgeIP = "";
    public static WeakReference<Context> context;
    public final String TAG = "MidWare";

    public static void init(Context ctx) {
        context = new WeakReference<>(ctx);
        FPVController.loadLibrary();
        ServiceManager.setContext(ctx);
        DJIHandheldHelper.getInstance().setContext(ctx);
        ServiceManager.createInstance();
        ServiceManager.getInstance().init();
        DJIVideoDataRecver.getInstance().setDecoderType(DJIVideoDataRecver.DJIDecoderType.Hardware);
        DJIProductManager.build(ctx);
        UsbAccessoryService.registerAoaReceiver(ctx);
        ServiceManager.getInstance().start();
        DJIComponentManager.getInstance().init(ctx);
        DJIUpgradeComponentManager.getInstance().init();
        DJIProductSupportModel.getInstance();
        Lifecycle.broadcastCreate(ctx, Lifecycle.ACTION_APPLICATION);
        if (isBridgeEnabled()) {
            BridgeWSConnectionManager.getInstance();
        }
        InternalDataProtectionGuard.getInstance();
    }

    public static void destroy() {
        DJIUpgradeComponentManager.getInstance().destroy();
        DJIComponentManager.getInstance().destroy();
        DJIProductManager.getInstance().destroy();
        if (context != null) {
            context.clear();
        }
    }

    public static boolean isBridgeEnabled() {
        return !TextUtils.isEmpty("") || !TextUtils.isEmpty(bridgeIP);
    }
}
