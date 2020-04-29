package dji.sdksharedlib.hardware.abstractions.flightcontroller.virtualfence;

import android.support.annotation.NonNull;
import dji.common.error.DJIFlightControllerError;
import dji.common.flightcontroller.virtualfence.AreaShape;
import dji.common.flightcontroller.virtualfence.FlightHeightType;
import dji.common.flightcontroller.virtualfence.VirtualCircleFenceArea;
import dji.common.flightcontroller.virtualfence.VirtualFenceArea;
import dji.common.flightcontroller.virtualfence.VirtualPolygonFenceArea;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataFlightDeleteVFenceData;
import dji.midware.data.model.P3.DataFlightGetVFenceData;
import dji.midware.data.model.P3.DataFlightSetVFenceData;
import dji.midware.data.model.P3.DataFlightSetVFenceEnabled;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.VirtualFenceKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.sdksharedlib.util.Util;
import java.util.ArrayList;
import java.util.Iterator;

public class VirtualFenceAbstraction extends DJISubComponentHWAbstraction {
    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(VirtualFenceKeys.class, getClass());
    }

    @Setter(VirtualFenceKeys.VIRTUAL_FENCE_AREA)
    public void setVirtualFenceArea(@NonNull ArrayList<VirtualFenceArea> area, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataFlightSetVFenceData vFenceData = DataFlightSetVFenceData.getInstance();
        vFenceData.resetPack().setAreaCount(area.size());
        Iterator<VirtualFenceArea> it2 = area.iterator();
        while (it2.hasNext()) {
            vFenceData.addVirtualAreaData(wrapInternalVirtualFenceArea(it2.next()));
        }
        vFenceData.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.virtualfence.VirtualFenceAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (DataFlightSetVFenceData.getInstance().getRspResult()) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.getSetVirtualFenceErrorCode(DataFlightSetVFenceData.getInstance().getRspErrorReason()));
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Getter(VirtualFenceKeys.VIRTUAL_FENCE_AREA)
    public void getVirtualFenceArea(@NonNull final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataFlightGetVFenceData.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.virtualfence.VirtualFenceAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                if (DataFlightGetVFenceData.getInstance().getRspResult()) {
                    ArrayList<DataFlightGetVFenceData.VirtualFenceArea> virtualFenceAreas = (ArrayList) DataFlightGetVFenceData.getInstance().getVirtualFenceArea();
                    ArrayList<VirtualFenceArea> areaResult = new ArrayList<>(virtualFenceAreas.size());
                    Iterator it2 = virtualFenceAreas.iterator();
                    while (it2.hasNext()) {
                        areaResult.add(VirtualFenceAbstraction.this.wrapVirtualFenceArea((DataFlightGetVFenceData.VirtualFenceArea) it2.next()));
                    }
                    CallbackUtils.onSuccess(callback, areaResult);
                    return;
                }
                CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Action(VirtualFenceKeys.DELETE_VIRTUAL_FENCE)
    public void deleteVirtualFenceArea(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataFlightDeleteVFenceData.getInstance().shouldDelete(true).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.virtualfence.VirtualFenceAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                if (DataFlightDeleteVFenceData.getInstance().getRspResult()) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    @Setter(VirtualFenceKeys.VIRTUAL_FENCE_ENABLED)
    public void setVirtualFenceAreaEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataFlightSetVFenceEnabled.getInstance().setVFenceEnabled(enabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.virtualfence.VirtualFenceAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                int errorCode = DataFlightSetVFenceEnabled.getInstance().getRspResult();
                if (errorCode == 0) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                } else {
                    CallbackUtils.onFailure(callback, DJIFlightControllerError.getSetVirtualFenceEnabledErrorCode(errorCode));
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    private DataFlightSetVFenceData.VirtualFenceArea wrapInternalVirtualFenceArea(VirtualFenceArea area) {
        return new DataFlightSetVFenceData.VirtualFenceArea.Builder().circleFenceArea(wrapInternalCircleArea(area.getCircleFenceArea())).isFlightAllowed(area.isFlightAllowed()).flightHeightType(DataFlightSetVFenceData.FlightHeightType.find(area.getFlightHeightType().value())).flightHeight(area.getFlightHeight()).areaShape(DataFlightSetVFenceData.AreaShape.find(area.getAreaShape().value())).areaId(area.getAreaId()).polygonFenceArea(wrapInternalPolygonArea(area.getPolygonFenceArea())).build();
    }

    private DataFlightSetVFenceData.VirtualCircleFenceArea wrapInternalCircleArea(VirtualCircleFenceArea circleFenceArea) {
        if (circleFenceArea != null) {
            return new DataFlightSetVFenceData.VirtualCircleFenceArea(circleFenceArea.getCenterLatitude(), circleFenceArea.getCenterLongitude(), circleFenceArea.getRadius());
        }
        return null;
    }

    private DataFlightSetVFenceData.VirtualPolygonFenceArea wrapInternalPolygonArea(VirtualPolygonFenceArea polygonFenceArea) {
        if (polygonFenceArea != null) {
            return new DataFlightSetVFenceData.VirtualPolygonFenceArea(polygonFenceArea.getTotalPointsCount(), Util.wrapLocationCoordinate(polygonFenceArea.getCoordinatePoints()));
        }
        return null;
    }

    /* access modifiers changed from: private */
    public VirtualFenceArea wrapVirtualFenceArea(DataFlightGetVFenceData.VirtualFenceArea area) {
        return new VirtualFenceArea.Builder().areaId(area.getAreaId()).areaShape(AreaShape.find(area.getAreaShape().value())).circleFenceArea(wrapCircleFenceArea(area.getCircleFenceArea())).polygonFenceArea(wrapPolygonFenceArea(area.getPolygonFenceArea())).flightHeight(area.getFlightHeight()).flightHeightType(FlightHeightType.find(area.getFlightHeightType().value())).isFlightAllowed(area.isFlightAllowed()).build();
    }

    private VirtualCircleFenceArea wrapCircleFenceArea(DataFlightSetVFenceData.VirtualCircleFenceArea circleFenceArea) {
        if (circleFenceArea != null) {
            return new VirtualCircleFenceArea(circleFenceArea.getCenterLatitude(), circleFenceArea.getCenterLongitude(), circleFenceArea.getRadius());
        }
        return null;
    }

    private VirtualPolygonFenceArea wrapPolygonFenceArea(DataFlightSetVFenceData.VirtualPolygonFenceArea polygonFenceArea) {
        if (polygonFenceArea != null) {
            return new VirtualPolygonFenceArea(polygonFenceArea.getTotalPointsCount(), Util.transformLocationCoordinate2D(polygonFenceArea.getCoordinatePoints()));
        }
        return null;
    }
}
