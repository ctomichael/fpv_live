package dji.internal.version;

import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.util.DJIEventBusUtil;
import java.util.LinkedList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class VersionController {
    private static final String TAG = "VersionController";
    private static VersionController instance = null;
    private DJIVersionCamera djiVersionCamera;
    private DJIVersionRC djiVersionRC;
    private String lastVersion;
    private List<VersionChangeObserver> listenerList = new LinkedList();
    private DJIVersionPlatform versionPlatform;

    public interface VersionChangeObserver {
        void onVersionChange(String str, String str2);
    }

    public enum Key {
        Product
    }

    public String getCameraVersion() {
        if (this.djiVersionCamera != null) {
            return this.djiVersionCamera.getVersion();
        }
        return null;
    }

    public boolean addListener(VersionChangeObserver observer) {
        return this.listenerList.add(observer);
    }

    public boolean removeListener(VersionChangeObserver observer) {
        return this.listenerList.remove(observer);
    }

    public void cleanListeners() {
        this.listenerList.clear();
    }

    public static synchronized VersionController getInstance() {
        VersionController versionController;
        synchronized (VersionController.class) {
            if (instance == null) {
                instance = new VersionController();
            }
            versionController = instance;
        }
        return versionController;
    }

    private VersionController() {
    }

    public void init(Context ctx) {
        DJIEventBusUtil.register(this);
        this.versionPlatform = new DJIVersionPlatform();
        this.versionPlatform.init(ctx);
        this.djiVersionRC = new DJIVersionRC();
        this.djiVersionRC.init(ctx);
        this.djiVersionCamera = new DJIVersionCamera();
        this.djiVersionCamera.init(ctx);
    }

    public void destroy() {
        if (this.versionPlatform != null) {
            this.versionPlatform.destroy();
            this.versionPlatform = null;
        }
        if (this.djiVersionRC != null) {
            this.djiVersionRC.uninit();
            this.djiVersionRC = null;
        }
        if (this.djiVersionCamera != null) {
            this.djiVersionCamera.uninit();
            this.djiVersionCamera = null;
        }
        DJIEventBusUtil.unRegister(this);
    }

    private void invokeListeners(String oldVersion, String newVersion) {
        for (VersionChangeObserver observer : this.listenerList) {
            observer.onVersionChange(oldVersion, newVersion);
        }
    }

    private void checkVersion(String newVersion) {
        if (newVersion != null || this.lastVersion != null) {
            if (this.lastVersion == null || !this.lastVersion.equals(newVersion)) {
                invokeListeners(this.lastVersion, newVersion);
                this.lastVersion = newVersion;
            }
        }
    }

    public DJIVersionRC getDJIVersionRC() {
        return this.djiVersionRC;
    }

    public String getFirmwarePackageVersion() {
        return this.lastVersion;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIVersionPlatform event) {
        if (this.versionPlatform != null) {
            checkVersion(this.versionPlatform.getVersion());
            logD(TAG, "Receive new package version:" + this.versionPlatform.getVersion());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIVersionRC event) {
        logD(TAG, "Receive new remote controller version:" + event.getVersion());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIVersionCamera event) {
    }

    public static void logD(String tag, String msg) {
        DJILog.logWriteD(tag, msg, TAG, new Object[0]);
    }
}
