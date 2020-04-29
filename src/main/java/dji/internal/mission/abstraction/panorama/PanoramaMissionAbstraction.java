package dji.internal.mission.abstraction.panorama;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.bus.MissionEventBus;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.common.mission.MissionEvent;
import dji.common.mission.MissionState;
import dji.common.mission.panorama.PanoramaMissionExecutionState;
import dji.common.mission.panorama.PanoramaMode;
import dji.common.product.Model;
import dji.common.util.CallbackUtils;
import dji.common.util.CommonCallbacks;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.abstraction.BaseMissionAbstraction;
import dji.internal.mission.abstraction.panorama.PanoramaAbstractionDataHolder;
import dji.internal.mission.fsm.FiniteStateMachine;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushCurPanoFileName;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraSetMode;
import dji.midware.data.model.P3.DataCameraSetPanoMode;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetPhotoMode;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class PanoramaMissionAbstraction extends BaseMissionAbstraction {
    private static final int INVALID_FILE_INDEX = -1;
    private static final String TAG = "Pano";
    private int curFileIndex;
    /* access modifiers changed from: private */
    public SettingsDefinitions.CameraMode currentCameraMode = SettingsDefinitions.CameraMode.UNKNOWN;
    /* access modifiers changed from: private */
    public SettingsDefinitions.ShootPhotoMode currentShootPhotoMode = SettingsDefinitions.ShootPhotoMode.UNKNOWN;
    private PanoramaAbstractionDataHolder.PanoramaBuilder holder = new PanoramaAbstractionDataHolder.PanoramaBuilder();
    /* access modifiers changed from: private */
    public AtomicBoolean isSettingUp = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public AtomicBoolean isStopping = new AtomicBoolean(false);
    private long lastUpdateTime = 0;
    private DJIParamAccessListener listener = new DJIParamAccessListener() {
        /* class dji.internal.mission.abstraction.panorama.PanoramaMissionAbstraction.AnonymousClass1 */

        public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
            if (key == null || !key.getParamKey().equals("Mode")) {
                if (key == null || !key.getParamKey().equals(ProductKeys.MODEL_NAME)) {
                    if (key != null && key.getParamKey().equals(CameraKeys.SHOOT_PHOTO_MODE) && newValue != null && newValue.getData() != null) {
                        SettingsDefinitions.ShootPhotoMode unused = PanoramaMissionAbstraction.this.currentShootPhotoMode = (SettingsDefinitions.ShootPhotoMode) newValue.getData();
                    }
                } else if (newValue == null || newValue.getData() == null || newValue.getData() != Model.OSMO) {
                    DJILog.d(PanoramaMissionAbstraction.TAG, "OSMO disconnected", new Object[0]);
                    boolean unused2 = PanoramaMissionAbstraction.this.transitToState(MissionState.DISCONNECTED, new PanoramaAbstractionDataHolder.PanoramaBuilder().event(MissionEvent.DISCONNECTED));
                } else {
                    DJILog.d(PanoramaMissionAbstraction.TAG, "OSMO connected", new Object[0]);
                    boolean unused3 = PanoramaMissionAbstraction.this.transitToState(MissionState.READY_TO_SETUP, new PanoramaAbstractionDataHolder.PanoramaBuilder().event(MissionEvent.CONNECTED));
                }
            } else if (newValue != null && newValue.getData() != null) {
                SettingsDefinitions.CameraMode unused4 = PanoramaMissionAbstraction.this.currentCameraMode = (SettingsDefinitions.CameraMode) newValue.getData();
                if (newValue.getData() != SettingsDefinitions.CameraMode.SHOOT_PHOTO) {
                    boolean unused5 = PanoramaMissionAbstraction.this.transitToState(MissionState.READY_TO_SETUP, new PanoramaAbstractionDataHolder.PanoramaBuilder().event(MissionEvent.CAMERA_MODE_CHANGE));
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public DJIError returnError;
    /* access modifiers changed from: private */
    public CountDownLatch startMissionCDL = null;

    private void cdlAwait(CountDownLatch cdl, int timeout, int delay) {
        if (cdl != null) {
            if (timeout > 0) {
                try {
                    cdl.await((long) timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
                    return;
                }
            } else {
                cdl.await();
            }
            Thread.sleep((long) delay);
        }
    }

    /* access modifiers changed from: private */
    public void cdlCountDown(CountDownLatch cdl) {
        cdl.countDown();
    }

    public PanoramaMissionAbstraction() {
        tryRecoverMissionState();
        reset();
        startListen();
        refreshEventBusInformation();
    }

    private void startListen() {
        DJIEventBusUtil.register(this);
        CacheHelper.addCameraListener(this.listener, "Mode", CameraKeys.SHOOT_PHOTO_MODE);
        CacheHelper.addProductListener(this.listener, ProductKeys.MODEL_NAME);
    }

    private void refreshEventBusInformation() {
        if (DataCameraGetPushCurPanoFileName.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCameraGetPushCurPanoFileName.getInstance());
        }
    }

    public void reset() {
        this.currentCameraMode = SettingsDefinitions.CameraMode.UNKNOWN;
        this.curFileIndex = -1;
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        CacheHelper.removeListener(this.listener);
    }

    public void tryRecoverMissionState() {
        if (DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.WM160) {
            this.fsm.forceTransitTo(MissionState.NOT_SUPPORTED);
        } else if (DJIProductManager.getInstance().getType() != ProductType.Longan) {
            if (this.fsm.getState() == null) {
                this.fsm.forceTransitTo(MissionState.DISCONNECTED);
            }
            forceTransitToState(MissionState.DISCONNECTED, new PanoramaAbstractionDataHolder.PanoramaBuilder().event(MissionEvent.DISCONNECTED));
        } else {
            if (this.fsm.getState() == null) {
                this.fsm.forceTransitTo(MissionState.READY_TO_SETUP);
            }
            forceTransitToState(MissionState.READY_TO_SETUP, new PanoramaAbstractionDataHolder.PanoramaBuilder().event(MissionEvent.CONNECTED));
        }
    }

    public void setupMission(@NonNull final PanoramaMode mode, @Nullable final CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.SETTING_UP, new PanoramaAbstractionDataHolder.PanoramaBuilder(MissionEvent.UNKNOWN))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            new Thread(new Runnable() {
                /* class dji.internal.mission.abstraction.panorama.PanoramaMissionAbstraction.AnonymousClass2 */

                public void run() {
                    if (!PanoramaMissionAbstraction.this.isSettingUp.compareAndSet(false, true)) {
                        CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
                    } else {
                        PanoramaMissionAbstraction.this.settingUp(mode, callback);
                    }
                }
            }, "PanoAbs2").start();
        }
    }

    /* access modifiers changed from: private */
    public void settingUp(@NonNull final PanoramaMode mode, @Nullable CommonCallbacks.CompletionCallback callback) {
        if (this.currentCameraMode != SettingsDefinitions.CameraMode.SHOOT_PHOTO) {
            this.startMissionCDL = new CountDownLatch(1);
            DataCameraSetMode.getInstance().setMode(DataCameraGetMode.MODE.TAKEPHOTO).start(new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.panorama.PanoramaMissionAbstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    PanoramaMissionAbstraction.this.cdlCountDown(PanoramaMissionAbstraction.this.startMissionCDL);
                }

                public void onFailure(Ccode ccode) {
                    DJIError unused = PanoramaMissionAbstraction.this.returnError = DJIError.getDJIError(ccode);
                    PanoramaMissionAbstraction.this.performSetupError(PanoramaMissionAbstraction.this.returnError);
                    PanoramaMissionAbstraction.this.cdlCountDown(PanoramaMissionAbstraction.this.startMissionCDL);
                }
            });
            cdlAwait(this.startMissionCDL, DJIVideoDecoder.connectLosedelay, 0);
            if (this.returnError != null) {
                transitToState(MissionState.READY_TO_SETUP, MissionEvent.UNKNOWN);
                CallbackUtils.onFailure(callback, this.returnError);
                this.isSettingUp.set(false);
                return;
            }
        }
        if (this.currentShootPhotoMode != SettingsDefinitions.ShootPhotoMode.PANORAMA) {
            this.startMissionCDL = new CountDownLatch(1);
            DataCameraSetPhotoMode.getInstance().setType(DataCameraSetPhoto.TYPE.APP_FULLVIEW).start(new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.panorama.PanoramaMissionAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    PanoramaMissionAbstraction.this.cdlCountDown(PanoramaMissionAbstraction.this.startMissionCDL);
                }

                public void onFailure(Ccode ccode) {
                    DJIError unused = PanoramaMissionAbstraction.this.returnError = DJIError.getDJIError(ccode);
                    PanoramaMissionAbstraction.this.performSetupError(PanoramaMissionAbstraction.this.returnError);
                    PanoramaMissionAbstraction.this.cdlCountDown(PanoramaMissionAbstraction.this.startMissionCDL);
                }
            });
            cdlAwait(this.startMissionCDL, 1000, 0);
            if (this.returnError != null) {
                transitToState(MissionState.READY_TO_SETUP, MissionEvent.UNKNOWN);
                CallbackUtils.onFailure(callback, this.returnError);
                this.isSettingUp.set(false);
                return;
            }
        }
        this.startMissionCDL = new CountDownLatch(1);
        DataCameraSetPanoMode.getInstance().setMode(DataCameraGetPushShotParams.PanoMode.find(mode.value())).start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.panorama.PanoramaMissionAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                DJILog.d(PanoramaMissionAbstraction.TAG, "set pano mode " + mode.name() + " Succcess!!!!!", new Object[0]);
                PanoramaMissionAbstraction.this.cdlCountDown(PanoramaMissionAbstraction.this.startMissionCDL);
            }

            public void onFailure(Ccode ccode) {
                DJIError unused = PanoramaMissionAbstraction.this.returnError = DJIError.getDJIError(ccode);
                DJILog.d(PanoramaMissionAbstraction.TAG, "set pano mode failed: " + PanoramaMissionAbstraction.this.returnError.getDescription(), new Object[0]);
                PanoramaMissionAbstraction.this.performSetupError(PanoramaMissionAbstraction.this.returnError);
                PanoramaMissionAbstraction.this.cdlCountDown(PanoramaMissionAbstraction.this.startMissionCDL);
            }
        });
        cdlAwait(this.startMissionCDL, 1000, 1000);
        if (this.returnError != null) {
            transitToState(MissionState.READY_TO_SETUP, MissionEvent.UNKNOWN);
            CallbackUtils.onFailure(callback, this.returnError);
            this.isSettingUp.set(false);
            return;
        }
        boolean isGimbalBusy = true;
        int count = 10;
        while (true) {
            if (count != 0) {
                if (!DataCameraGetPushStateInfo.getInstance().getIsGimbalBusy()) {
                    isGimbalBusy = false;
                    break;
                }
                count--;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
                }
            } else {
                break;
            }
        }
        if (isGimbalBusy) {
            transitToState(MissionState.READY_TO_SETUP, MissionEvent.UNKNOWN);
            CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
            this.isSettingUp.set(false);
            return;
        }
        transitToState(MissionState.READY_TO_EXECUTE, MissionEvent.SETUP_DONE);
        CallbackUtils.onSuccess(callback);
        this.isSettingUp.set(false);
    }

    public void startMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_STARTING, new PanoramaAbstractionDataHolder.PanoramaBuilder(MissionEvent.UNKNOWN))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        this.returnError = null;
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setCmdId("Photo");
        setter.setValue(DataCameraSetPhoto.TYPE.APP_FULLVIEW.value());
        setter.setPackParam(0, 1);
        setter.start(getDataCallbackForTempState(PanoramaMissionAbstraction$$Lambda$0.$instance, MissionState.EXECUTION_STARTING, desiredMissionStatesHelper(MissionState.EXECUTING), 5.0d, MissionState.READY_TO_EXECUTE, new PanoramaAbstractionDataHolder.PanoramaBuilder(MissionEvent.EXECUTION_START_FAILED), callback));
    }

    static final /* synthetic */ int lambda$startMission$0$PanoramaMissionAbstraction() {
        return 0;
    }

    public void stopMission(@Nullable final CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_STOPPING, new PanoramaAbstractionDataHolder.PanoramaBuilder(MissionEvent.UNKNOWN))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            new Thread(new Runnable() {
                /* class dji.internal.mission.abstraction.panorama.PanoramaMissionAbstraction.AnonymousClass6 */

                public void run() {
                    if (!PanoramaMissionAbstraction.this.isStopping.compareAndSet(false, true)) {
                        CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
                    } else {
                        PanoramaMissionAbstraction.this.stopping(callback);
                    }
                }
            }, "PanoAbs1").start();
        }
    }

    /* access modifiers changed from: private */
    public void stopping(@Nullable CommonCallbacks.CompletionCallback callback) {
        this.returnError = null;
        this.startMissionCDL = new CountDownLatch(1);
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setCmdId("Photo");
        setter.setValue(0);
        setter.setPackParam(0, 1);
        setter.start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.panorama.PanoramaMissionAbstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                PanoramaMissionAbstraction.this.cdlCountDown(PanoramaMissionAbstraction.this.startMissionCDL);
            }

            public void onFailure(Ccode ccode) {
                DJIError unused = PanoramaMissionAbstraction.this.returnError = DJIError.getDJIError(ccode);
                PanoramaMissionAbstraction.this.cdlCountDown(PanoramaMissionAbstraction.this.startMissionCDL);
            }
        });
        cdlAwait(this.startMissionCDL, 1000, 1000);
        if (this.returnError == null) {
            try {
                Thread.sleep(1000);
                if (System.currentTimeMillis() - this.lastUpdateTime > 1000) {
                    DJILog.d(TAG, "stop > 1000", new Object[0]);
                    transitToState(MissionState.READY_TO_EXECUTE, MissionEvent.EXECUTION_STOPPED);
                    CallbackUtils.onSuccess(callback);
                } else {
                    DJILog.d(TAG, "stop < 1000", new Object[0]);
                    transitToState(MissionState.EXECUTING, MissionEvent.EXECUTION_STOP_FAILED);
                    CallbackUtils.onFailure(callback, this.returnError);
                }
            } catch (Exception e) {
                DJILog.d(TAG, "stop mission sleep exception: " + e.getMessage(), new Object[0]);
            }
        } else {
            transitToState(MissionState.EXECUTING, MissionEvent.EXECUTION_STOP_FAILED);
            CallbackUtils.onFailure(callback, this.returnError);
        }
        this.isStopping.set(false);
    }

    public void notifyListener(AbstractionDataHolder holder2) {
        if (!holder2.equals(this.previousDataHolder)) {
            DJILog.d(TAG, "post event: " + holder2.getEvent().getName(), new Object[0]);
            DJILog.d(TAG, "post dataholer: " + holder2.getClass().getName(), new Object[0]);
            this.previousDataHolder = holder2;
            MissionEventBus.getInstance().post(holder2);
        }
    }

    /* access modifiers changed from: protected */
    public FiniteStateMachine buildFSM() {
        return new FiniteStateMachine().add(MissionState.DISCONNECTED).from(MissionState.READY_TO_SETUP).to(MissionState.SETTING_UP).from(MissionState.SETTING_UP).to(MissionState.SETTING_UP, MissionState.READY_TO_EXECUTE).from(MissionState.READY_TO_EXECUTE).to(MissionState.EXECUTION_STARTING, MissionState.SETTING_UP).from(MissionState.EXECUTION_STARTING).to(MissionState.EXECUTING, MissionState.READY_TO_EXECUTE).from(MissionState.EXECUTING).to(MissionState.EXECUTING, MissionState.EXECUTION_STOPPING, MissionState.READY_TO_EXECUTE).from(MissionState.EXECUTION_STOPPING).to(MissionState.EXECUTING, MissionState.READY_TO_EXECUTE).fromAll().to(MissionState.READY_TO_SETUP).fromAll().to(MissionState.DISCONNECTED).superState(MissionState.NOT_SUPPORTED);
    }

    /* access modifiers changed from: protected */
    public boolean transitToState(@NonNull MissionState state, MissionEvent event) {
        return transitToState(state, this.holder.event(event));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushCurPanoFileName event) {
        this.lastUpdateTime = System.currentTimeMillis();
        this.curFileIndex = (int) event.getIndex();
        int totalNumber = event.getTotalNumber();
        int savedNumber = event.getCurSavedNumber();
        int shotNumber = event.getCurTakenNumber();
        if (savedNumber == totalNumber) {
            this.holder.event(MissionEvent.EXECUTION_FINISHED);
            this.holder.extra(new PanoramaMissionExecutionState(totalNumber, shotNumber, savedNumber));
            transitToState(MissionState.READY_TO_EXECUTE, this.holder);
            return;
        }
        this.holder.event(MissionEvent.EXECUTION_PROGRESS_UPDATE);
        this.holder.extra(new PanoramaMissionExecutionState(totalNumber, shotNumber, savedNumber));
        transitToState(MissionState.EXECUTING, this.holder);
    }

    public int getCurFileIndex() {
        return this.curFileIndex;
    }

    /* access modifiers changed from: private */
    public void performSetupError(DJIError error) {
        if (error != null) {
            transitToState(MissionState.READY_TO_SETUP, MissionEvent.SETUP_FAILED);
        }
    }
}
