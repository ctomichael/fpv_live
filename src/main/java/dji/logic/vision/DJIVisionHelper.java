package dji.logic.vision;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.model.P3.DataEyeGetPushFrontAvoidance;
import dji.midware.data.model.P3.DataEyeGetPushOmniAvoidance;
import dji.midware.util.DJIEventBusUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIVisionHelper {
    private final HashMap<DataEyeGetPushFrontAvoidance.SensorType, DataEyeGetPushFrontAvoidance> mAvoidDatas;
    private final HashMap<DataEyeGetPushFrontAvoidance.SensorType, VisionEvent> mEventMap;
    private final Map<DataEyeGetPushOmniAvoidance.Direction, OmniVisionEvent> mOmniAdvoidanceEventMap;
    private final Map<DataEyeGetPushOmniAvoidance.Direction, DataEyeGetPushOmniAvoidance> mOmniAvoidanceDatas;

    public enum VisionEvent {
        RADAR_FRONT_CHANGED,
        RADAR_BACK_CHANGED,
        RADAR_LEFT_CHANGED,
        RADAR_RIGHT_CHANGED,
        RADAR_TOP_CHANGED,
        RADAR_BOTTOM_CHANGED,
        RADAR_ALL_CHANGED
    }

    public enum OmniVisionEvent {
        OMNI_FRONT_CHANGED,
        OMNI_BACK_CHANGED,
        OMNI_LEFT_CHANGED,
        OMNI_RIGHT_CHANGED,
        OMNI_TOP_CHANGED,
        OMNI_BOTTOM_CHANGED,
        OMNI_ALL_CHANGED
    }

    public static DJIVisionHelper getInstance() {
        if (!EventBus.getDefault().isRegistered(SingletonHolder.mInstance)) {
            SingletonHolder.mInstance.init();
        }
        return SingletonHolder.mInstance;
    }

    public void updateAvoidDatas(DataEyeGetPushFrontAvoidance data) {
        if (data != null && data.getRecData() != null) {
            synchronized (this.mAvoidDatas) {
                DataEyeGetPushFrontAvoidance.SensorType type = data.getSensorType();
                DataEyeGetPushFrontAvoidance typeData = this.mAvoidDatas.get(type);
                if (typeData == null) {
                    typeData = new DataEyeGetPushFrontAvoidance(false);
                    this.mAvoidDatas.put(type, typeData);
                }
                if (!Arrays.equals(typeData.getRecData(), data.getRecData())) {
                    typeData.setRecData(data.getRecData());
                    EventBus.getDefault().post(this.mEventMap.get(type));
                }
            }
        }
    }

    public void updateOmniAvoidanceDatas(DataEyeGetPushOmniAvoidance data) {
        if (data == null || data.getRecData() == null) {
        }
    }

    public HashMap<DataEyeGetPushFrontAvoidance.SensorType, DataEyeGetPushFrontAvoidance> getDatas() {
        HashMap<DataEyeGetPushFrontAvoidance.SensorType, DataEyeGetPushFrontAvoidance> hashMap;
        synchronized (this.mAvoidDatas) {
            hashMap = this.mAvoidDatas;
        }
        return hashMap;
    }

    public DataEyeGetPushFrontAvoidance getData(DataEyeGetPushFrontAvoidance.SensorType type) {
        DataEyeGetPushFrontAvoidance dataEyeGetPushFrontAvoidance;
        synchronized (this.mAvoidDatas) {
            dataEyeGetPushFrontAvoidance = this.mAvoidDatas.get(type);
        }
        return dataEyeGetPushFrontAvoidance;
    }

    public DataEyeGetPushOmniAvoidance getOmniAdvoidanceData(DataEyeGetPushOmniAvoidance.Direction direction) {
        DataEyeGetPushOmniAvoidance dataEyeGetPushOmniAvoidance;
        synchronized (this.mOmniAvoidanceDatas) {
            dataEyeGetPushOmniAvoidance = this.mOmniAvoidanceDatas.get(direction);
        }
        return dataEyeGetPushOmniAvoidance;
    }

    private void clearAllDatas() {
        synchronized (this.mAvoidDatas) {
            this.mAvoidDatas.clear();
        }
        EventBus.getDefault().post(VisionEvent.RADAR_ALL_CHANGED);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (DataCameraEvent.ConnectLose == event) {
            clearAllDatas();
        }
    }

    private DJIVisionHelper() {
        this.mEventMap = new HashMap<>(6);
        this.mOmniAdvoidanceEventMap = new HashMap(6);
        this.mAvoidDatas = new HashMap<>(6);
        this.mOmniAvoidanceDatas = new HashMap(6);
        this.mEventMap.put(DataEyeGetPushFrontAvoidance.SensorType.Front, VisionEvent.RADAR_FRONT_CHANGED);
        this.mEventMap.put(DataEyeGetPushFrontAvoidance.SensorType.Back, VisionEvent.RADAR_BACK_CHANGED);
        this.mEventMap.put(DataEyeGetPushFrontAvoidance.SensorType.Left, VisionEvent.RADAR_LEFT_CHANGED);
        this.mEventMap.put(DataEyeGetPushFrontAvoidance.SensorType.Right, VisionEvent.RADAR_RIGHT_CHANGED);
        this.mEventMap.put(DataEyeGetPushFrontAvoidance.SensorType.Top, VisionEvent.RADAR_TOP_CHANGED);
        this.mEventMap.put(DataEyeGetPushFrontAvoidance.SensorType.Bottom, VisionEvent.RADAR_BOTTOM_CHANGED);
        this.mOmniAdvoidanceEventMap.put(DataEyeGetPushOmniAvoidance.Direction.FRONT, OmniVisionEvent.OMNI_FRONT_CHANGED);
        this.mOmniAdvoidanceEventMap.put(DataEyeGetPushOmniAvoidance.Direction.BACK, OmniVisionEvent.OMNI_BACK_CHANGED);
        this.mOmniAdvoidanceEventMap.put(DataEyeGetPushOmniAvoidance.Direction.LEFT, OmniVisionEvent.OMNI_LEFT_CHANGED);
        this.mOmniAdvoidanceEventMap.put(DataEyeGetPushOmniAvoidance.Direction.RIGHT, OmniVisionEvent.OMNI_RIGHT_CHANGED);
        this.mOmniAdvoidanceEventMap.put(DataEyeGetPushOmniAvoidance.Direction.TOP, OmniVisionEvent.OMNI_TOP_CHANGED);
        this.mOmniAdvoidanceEventMap.put(DataEyeGetPushOmniAvoidance.Direction.BOTTOM, OmniVisionEvent.OMNI_BOTTOM_CHANGED);
    }

    public void init() {
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIVisionHelper mInstance = new DJIVisionHelper();

        private SingletonHolder() {
        }
    }
}
