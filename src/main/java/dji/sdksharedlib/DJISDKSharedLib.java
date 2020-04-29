package dji.sdksharedlib;

import android.content.Context;
import dji.common.camera.SettingsDefinitions;
import dji.common.remotecontroller.ProfessionalRC;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.VersionController;
import dji.log.GlobalConfig;
import dji.midware.MidWare;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataRcSetCustomFuction;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public class DJISDKSharedLib {
    public final String TAG = "DJISDKSharedLib";
    boolean isInitialized = false;
    private List<InitListener> mAfterInitListeners = new ArrayList();

    public interface InitListener {
        void doAfter();
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static DJISDKSharedLib instance = new DJISDKSharedLib();

        private SingletonHolder() {
        }
    }

    public static DJISDKSharedLib getInstance() {
        return SingletonHolder.instance;
    }

    protected DJISDKSharedLib() {
    }

    private void checkCodeLogic() {
        if (!GlobalConfig.DEBUG) {
            return;
        }
        if (ProfessionalRC.CustomizableButton.values().length != DataRcSetCustomFuction.ProCustomButton.values().length + 1) {
            throw new RuntimeException("CustomizableButton is not same with ProCustomButton!");
        } else if (ProfessionalRC.ButtonAction.values().length != DataRcSetCustomFuction.DJICustomType.values().length) {
            throw new RuntimeException("ButtonAction is not same with DJICustomType!");
        } else if (DataCameraGetPushStateInfo.CameraType.values().length != SettingsDefinitions.CameraType.values().length) {
            throw new RuntimeException("need update CameraTypes inside SettingsDefinitions!");
        }
    }

    public void init(Context ctx) {
        if (!this.isInitialized) {
            this.isInitialized = true;
            checkCodeLogic();
            MidWare.init(ctx);
            SQLiteDatabase.loadLibs(ctx);
            DJISDKCache.getInstance().init();
            VersionController.getInstance().init(ctx);
            for (InitListener initListener : this.mAfterInitListeners) {
                initListener.doAfter();
            }
            this.mAfterInitListeners.clear();
        }
    }

    public void addInitListener(InitListener listener) {
        if (this.isInitialized) {
            listener.doAfter();
        } else {
            this.mAfterInitListeners.add(listener);
        }
    }

    public void destroy() {
        MidWare.destroy();
        DJISDKCache.getInstance().destroy();
    }
}
