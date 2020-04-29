package com.dji.scan.zxing.camera.open;

import android.hardware.Camera;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class OpenCameraInterface {
    public static final int NO_REQUESTED_CAMERA = -1;
    private static final String TAG = OpenCameraInterface.class.getName();

    private OpenCameraInterface() {
    }

    public static int getCameraId(int requestedId) {
        boolean explicitRequest;
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            Log.w(TAG, "No cameras!");
            return -1;
        }
        int cameraId = requestedId;
        if (cameraId >= 0) {
            explicitRequest = true;
        } else {
            explicitRequest = false;
        }
        if (!explicitRequest) {
            int index = 0;
            while (index < numCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == 0) {
                    break;
                }
                index++;
            }
            cameraId = index;
        }
        if (cameraId < numCameras) {
            return cameraId;
        }
        if (explicitRequest) {
            return -1;
        }
        return 0;
    }

    public static Camera open(int requestedId) {
        int cameraId = getCameraId(requestedId);
        if (cameraId == -1) {
            return null;
        }
        return Camera.open(cameraId);
    }
}
