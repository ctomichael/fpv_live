package dji.logic.camera;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;

@EXClassNullAway
public class DJICameraLogic {
    public static boolean isMachineCamera(DataCameraGetPushStateInfo.CameraType cameraType) {
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC260 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300S == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300X == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC330X == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300XW == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1102 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC230 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC245_IMX477 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC2403 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC160 == cameraType;
    }
}
