package dji.midware.data.manager.P3;

import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IProductSupportModel {
    public static final DataCameraGetPushStateInfo.CameraType[] GO_CAMERATYPE_SUPPORT_LIST = {DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC260, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300S, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300X, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC330X, DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau640, DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau336, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300XW, DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC65XXUnknown, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6532, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1102, DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310A, DataCameraGetPushStateInfo.CameraType.DJICameraTypeP3SE, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC230, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1705, DataCameraGetPushStateInfo.CameraType.DJICameraTypeHG330, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S, DataCameraGetPushStateInfo.CameraType.DJICameraTypeHasselH6D50C, DataCameraGetPushStateInfo.CameraType.DJICameraTypeHasselH6D100C, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1, DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC160};
    public static final DataOsdGetPushCommon.DroneType[] GO_DRONETYPE_SUPPORT_LIST = {DataOsdGetPushCommon.DroneType.Inspire, DataOsdGetPushCommon.DroneType.P3S, DataOsdGetPushCommon.DroneType.P3X, DataOsdGetPushCommon.DroneType.P3C, DataOsdGetPushCommon.DroneType.OpenFrame, DataOsdGetPushCommon.DroneType.A2, DataOsdGetPushCommon.DroneType.A3, DataOsdGetPushCommon.DroneType.P4, DataOsdGetPushCommon.DroneType.PM820, DataOsdGetPushCommon.DroneType.P34K, DataOsdGetPushCommon.DroneType.wm220, DataOsdGetPushCommon.DroneType.Orange2, DataOsdGetPushCommon.DroneType.Pomato, DataOsdGetPushCommon.DroneType.N3, DataOsdGetPushCommon.DroneType.Mammoth, DataOsdGetPushCommon.DroneType.PM820PRO, DataOsdGetPushCommon.DroneType.WM230, DataOsdGetPushCommon.DroneType.M200, DataOsdGetPushCommon.DroneType.Potato, DataOsdGetPushCommon.DroneType.M210, DataOsdGetPushCommon.DroneType.P3SE, DataOsdGetPushCommon.DroneType.M210RTK, DataOsdGetPushCommon.DroneType.PomatoSdr, DataOsdGetPushCommon.DroneType.WM240};
    public static final ProductType[] GO_PRODUCT_NOTSUPPORT_LIST = {ProductType.None, ProductType.AG405, ProductType.PomatoRTK, ProductType.OTHER};
    public static final ProductType[] GO_RC_NOTSUPPORT_LIST = {ProductType.None, ProductType.AG405, ProductType.PomatoRTK, ProductType.OTHER};
    public static final int TYPE_NOTSUPPORT_ALL = 255;
    public static final int TYPE_NOTSUPPORT_CAMERA = 2;
    public static final int TYPE_NOTSUPPORT_DRONE = 1;
    public static final int TYPE_NOTSUPPORT_NONE = 0;
    public static final int TYPE_NOTSUPPORT_PRODUCT = 8;
    public static final int TYPE_NOTSUPPORT_RC = 4;

    @Retention(RetentionPolicy.SOURCE)
    public @interface NotSupportType {
    }

    public enum ProductSupportEvent {
        SUPPORT_CHANGED
    }
}
