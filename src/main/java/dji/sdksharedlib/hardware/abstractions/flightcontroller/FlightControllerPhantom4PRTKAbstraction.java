package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIFlightControllerError;
import dji.common.error.DJIMissionError;
import dji.common.mission.waypoint.WaypointMissionInterruption;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CallbackUtils;
import dji.internal.logics.SalesStrategicLogic;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlyc2RequestSalesStrategy;
import dji.midware.data.model.P3.DataFlycGetWaypointInterruption;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPhantom4RTKAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;

public class FlightControllerPhantom4PRTKAbstraction extends FlightControllerPhantom4PAbstraction {
    private static final long RETRY_INTERVAL = 2000;
    private static final int RETRY_TIMES = 10;
    /* access modifiers changed from: private */
    public int UNLOCK_RETRY_TIMES = 10;
    private DJIParamAccessListener connectionListener = new DJIParamAccessListener() {
        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction.AnonymousClass2 */

        public void onValueChange(@Nullable DJISDKCacheKey key, DJISDKCacheParamValue oldValue, @Nullable DJISDKCacheParamValue newValue) {
            if (key != null && key.getParamKey().equals(DJISDKCacheKeys.CONNECTION) && newValue != null && ((Boolean) newValue.getData()).booleanValue() && !FlightControllerPhantom4PRTKAbstraction.this.hasMotorStarted) {
                FlightControllerPhantom4PRTKAbstraction.this.handler.post(FlightControllerPhantom4PRTKAbstraction.this.unlockMotors);
            }
        }
    };
    protected Handler handler;
    /* access modifiers changed from: private */
    public boolean hasMotorStarted = false;
    /* access modifiers changed from: private */
    public int retryGetSalesStrategyRemaingTimes = 10;
    Runnable unlockMotors = new Runnable() {
        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction.AnonymousClass1 */

        public void run() {
            if (!DJIFlycParamInfoManager.isInited()) {
                FlightControllerPhantom4PRTKAbstraction.access$010(FlightControllerPhantom4PRTKAbstraction.this);
                FlightControllerPhantom4PRTKAbstraction.this.handler.postDelayed(FlightControllerPhantom4PRTKAbstraction.this.unlockMotors, FlightControllerPhantom4PRTKAbstraction.RETRY_INTERVAL);
                return;
            }
            DataFlycSetParams setParams = new DataFlycSetParams();
            setParams.setIndexs("g_status.farm.app_stop_motor_start_0");
            setParams.setValues(0);
            setParams.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction.AnonymousClass1.AnonymousClass1 */

                /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                 method: dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction.access$102(dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction, boolean):boolean
                 arg types: [dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction, int]
                 candidates:
                  dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction.access$102(dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction, dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction$SimulatorInternalState):dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerAbstraction$SimulatorInternalState
                  dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction.access$102(dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction, boolean):boolean */
                public void onSuccess(Object model) {
                    boolean unused = FlightControllerPhantom4PRTKAbstraction.this.hasMotorStarted = true;
                    int unused2 = FlightControllerPhantom4PRTKAbstraction.this.UNLOCK_RETRY_TIMES = 10;
                }

                public void onFailure(Ccode ccode) {
                    if (FlightControllerPhantom4PRTKAbstraction.this.UNLOCK_RETRY_TIMES > 0) {
                        FlightControllerPhantom4PRTKAbstraction.access$010(FlightControllerPhantom4PRTKAbstraction.this);
                        FlightControllerPhantom4PRTKAbstraction.this.handler.postDelayed(FlightControllerPhantom4PRTKAbstraction.this.unlockMotors, FlightControllerPhantom4PRTKAbstraction.RETRY_INTERVAL);
                    }
                }
            });
        }
    };

    static /* synthetic */ int access$010(FlightControllerPhantom4PRTKAbstraction x0) {
        int i = x0.UNLOCK_RETRY_TIMES;
        x0.UNLOCK_RETRY_TIMES = i - 1;
        return i;
    }

    static /* synthetic */ int access$210(FlightControllerPhantom4PRTKAbstraction x0) {
        int i = x0.retryGetSalesStrategyRemaingTimes;
        x0.retryGetSalesStrategyRemaingTimes = i - 1;
        return i;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.handler = new Handler(BackgroundLooper.getLooper());
        startListen();
    }

    /* access modifiers changed from: protected */
    public RTKAbstraction getRTKAbstractionIfSupported() {
        return new RTKPhantom4RTKAbstraction();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    /* access modifiers changed from: protected */
    public void initFlightControllerSupportParameter() {
        super.initFlightControllerSupportParameter();
        notifyValueChangeForKeyPath((Object) true, convertKeyToPath(FlightControllerKeys.IS_RTK_SUPPORTED));
        SalesStrategicLogic.getInstance().init();
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
    }

    private void startListen() {
        CacheHelper.addProductListener(this.connectionListener, DJISDKCacheKeys.CONNECTION);
    }

    public void destroy() {
        CacheHelper.removeListener(this.connectionListener);
        SalesStrategicLogic.getInstance().destroy();
    }

    @Getter(FlightControllerKeys.SALES_STRATEGY)
    public void getSalesStrategy(@NonNull final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataFlyc2RequestSalesStrategy strategyGetter = new DataFlyc2RequestSalesStrategy();
        strategyGetter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                SalesStrategicLogic.SalesStrategy.Builder salesStrategyBuilder = new SalesStrategicLogic.SalesStrategy.Builder().isSupported(strategyGetter.getResult());
                if (strategyGetter.getResult()) {
                    salesStrategyBuilder.version(strategyGetter.getVersion()).strategy(strategyGetter.getStrategy()).isTakeOffAllowed(!strategyGetter.isTakeOffRejected()).currentAreaCode(strategyGetter.getCurrentAreaCode()).allowToFlyAreaNumbers(strategyGetter.getAllowToFlyAreaNumbers()).areaCodes((ArrayList) strategyGetter.getAllowToFlyAreaCodes()).build();
                }
                CallbackUtils.onSuccess(callback, salesStrategyBuilder.build());
                int unused = FlightControllerPhantom4PRTKAbstraction.this.retryGetSalesStrategyRemaingTimes = 10;
            }

            public void onFailure(Ccode ccode) {
                FlightControllerPhantom4PRTKAbstraction.access$210(FlightControllerPhantom4PRTKAbstraction.this);
                if (FlightControllerPhantom4PRTKAbstraction.this.retryGetSalesStrategyRemaingTimes <= 0) {
                    CallbackUtils.onSuccess(callback, new SalesStrategicLogic.SalesStrategy.Builder().isSupported(false).build());
                    return;
                }
                CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
            }
        });
    }

    @Getter(FlightControllerKeys.WAYPOINT_MISSION_INTERRUPTION)
    public void getPreviousInterruption(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataFlycGetWaypointInterruption waypointInterruption = new DataFlycGetWaypointInterruption();
        waypointInterruption.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerPhantom4PRTKAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                if (waypointInterruption.getResult() == DataFlycGetWaypointInterruption.InterruptionResult.VALID.value()) {
                    CallbackUtils.onSuccess(callback, WaypointMissionInterruption.newBuilder().coordinate(new LocationCoordinate2D(waypointInterruption.getLatitude(), waypointInterruption.getLongitude())).index(waypointInterruption.getCurrentIndex()).missionID(waypointInterruption.getMissionId()).altitude(waypointInterruption.getAltitude()).build());
                } else if (waypointInterruption.getResult() == DataFlycGetWaypointInterruption.InterruptionResult.INVALID_MISSION_COMPLETED.value()) {
                    CallbackUtils.onFailure(callback, DJIMissionError.WAYPOINT_GET_INTERRUPTION_FAILURE_FOR_COMPLETE_MISSION);
                } else if (waypointInterruption.getResult() == DataFlycGetWaypointInterruption.InterruptionResult.INVALID_BEFORE_FIRST_WAYPOINT.value()) {
                    CallbackUtils.onFailure(callback, DJIMissionError.WAYPOINT_GET_INTERRUPTION_FAILURE_WITHOUT_REACHING_FIRST_WAYPOINT);
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_UNKNOWN);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
            }
        });
    }
}
