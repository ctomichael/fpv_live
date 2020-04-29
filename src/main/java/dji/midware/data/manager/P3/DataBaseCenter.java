package dji.midware.data.manager.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.base.DJICommonDataBase;
import dji.midware.link.DJILinkType;
import dji.midware.util.DJIEventBusUtil;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DataBaseCenter {
    private WeakHashMap<DataBase, Void> dataBaseMap;
    private ReentrantLock lock;

    private DataBaseCenter() {
        this.lock = new ReentrantLock();
        this.dataBaseMap = new WeakHashMap<>();
        DJIEventBusUtil.register(this);
    }

    public static DataBaseCenter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataBaseCenter INSTANCE = new DataBaseCenter();

        private SingletonHolder() {
        }
    }

    @Subscribe(priority = 100, threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        this.lock.lock();
        try {
            if (event == DataEvent.ConnectLose) {
                for (DataBase dataBase : this.dataBaseMap.keySet()) {
                    if (dataBase != null) {
                        dataBase.clear();
                    }
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Subscribe(priority = 100, threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        this.lock.lock();
        try {
            if (event == DataCameraEvent.ConnectLose) {
                for (DataBase dataBase : this.dataBaseMap.keySet()) {
                    if (dataBase != null && (dataBase instanceof DJICommonDataBase)) {
                        dataBase.clear();
                    }
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Subscribe(priority = 100, threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJILinkType linkType) {
        this.lock.lock();
        try {
            if (linkType != DJILinkType.NON) {
                for (DataBase dataBase : this.dataBaseMap.keySet()) {
                    if (dataBase != null && (dataBase instanceof DJICommonDataBase)) {
                        dataBase.clear();
                    }
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void addDataBase(DataBase dataBase) {
        this.lock.lock();
        if (dataBase != null) {
            this.dataBaseMap.put(dataBase, null);
        }
        this.lock.unlock();
    }
}
