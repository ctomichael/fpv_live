package com.dji.scan.zxing.camera;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import com.adobe.xmp.XMPConst;
import com.dji.scan.qr.camera.CameraSettings;
import dji.fieldAnnotation.EXClassNullAway;
import dji.publics.LogReport.base.Fields;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@EXClassNullAway
public final class CameraConfigurationUtils {
    private static final int AREA_PER_1000 = 400;
    private static final float MAX_EXPOSURE_COMPENSATION = 1.5f;
    private static final int MAX_FPS = 20;
    private static final float MIN_EXPOSURE_COMPENSATION = 0.0f;
    private static final int MIN_FPS = 10;
    private static final Pattern SEMICOLON = Pattern.compile(";");
    private static final String TAG = "CameraConfiguration";

    private CameraConfigurationUtils() {
    }

    public static void setFocus(Camera.Parameters parameters, CameraSettings.FocusMode focusModeSetting, boolean safeMode) {
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        String focusMode = null;
        if (safeMode || focusModeSetting == CameraSettings.FocusMode.AUTO) {
            focusMode = findSettableValue("focus mode", supportedFocusModes, "auto");
        } else if (focusModeSetting == CameraSettings.FocusMode.CONTINUOUS) {
            focusMode = findSettableValue("focus mode", supportedFocusModes, "continuous-picture", "continuous-video", "auto");
        } else if (focusModeSetting == CameraSettings.FocusMode.INFINITY) {
            focusMode = findSettableValue("focus mode", supportedFocusModes, "infinity");
        } else if (focusModeSetting == CameraSettings.FocusMode.MACRO) {
            focusMode = findSettableValue("focus mode", supportedFocusModes, "macro");
        }
        if (!safeMode && focusMode == null) {
            focusMode = findSettableValue("focus mode", supportedFocusModes, "macro", "edof");
        }
        if (focusMode == null) {
            return;
        }
        if (focusMode.equals(parameters.getFocusMode())) {
            Log.i(TAG, "Focus mode already set to " + focusMode);
        } else {
            parameters.setFocusMode(focusMode);
        }
    }

    public static void setTorch(Camera.Parameters parameters, boolean on) {
        String flashMode;
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (on) {
            flashMode = findSettableValue("flash mode", supportedFlashModes, "torch", "on");
        } else {
            flashMode = findSettableValue("flash mode", supportedFlashModes, "off");
        }
        if (flashMode == null) {
            return;
        }
        if (flashMode.equals(parameters.getFlashMode())) {
            Log.i(TAG, "Flash mode already set to " + flashMode);
            return;
        }
        Log.i(TAG, "Setting flash mode to " + flashMode);
        parameters.setFlashMode(flashMode);
    }

    public static void setBestExposure(Camera.Parameters parameters, boolean lightOn) {
        float targetCompensation = 0.0f;
        int minExposure = parameters.getMinExposureCompensation();
        int maxExposure = parameters.getMaxExposureCompensation();
        float step = parameters.getExposureCompensationStep();
        if (!(minExposure == 0 && maxExposure == 0) && step > 0.0f) {
            if (!lightOn) {
                targetCompensation = 1.5f;
            }
            int compensationSteps = Math.round(targetCompensation / step);
            float actualCompensation = step * ((float) compensationSteps);
            int compensationSteps2 = Math.max(Math.min(compensationSteps, maxExposure), minExposure);
            if (parameters.getExposureCompensation() == compensationSteps2) {
                Log.i(TAG, "Exposure compensation already set to " + compensationSteps2 + " / " + actualCompensation);
                return;
            }
            Log.i(TAG, "Setting exposure compensation to " + compensationSteps2 + " / " + actualCompensation);
            parameters.setExposureCompensation(compensationSteps2);
            return;
        }
        Log.i(TAG, "Camera does not support exposure compensation");
    }

    public static void setBestPreviewFPS(Camera.Parameters parameters) {
        setBestPreviewFPS(parameters, 10, 20);
    }

    public static void setBestPreviewFPS(Camera.Parameters parameters, int minFPS, int maxFPS) {
        List<int[]> supportedPreviewFpsRanges = parameters.getSupportedPreviewFpsRange();
        Log.i(TAG, "Supported FPS ranges: " + toString((Collection<int[]>) supportedPreviewFpsRanges));
        if (supportedPreviewFpsRanges != null && !supportedPreviewFpsRanges.isEmpty()) {
            int[] suitableFPSRange = null;
            Iterator<int[]> it2 = supportedPreviewFpsRanges.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                int[] fpsRange = it2.next();
                int thisMin = fpsRange[0];
                int thisMax = fpsRange[1];
                if (thisMin >= minFPS * 1000 && thisMax <= maxFPS * 1000) {
                    suitableFPSRange = fpsRange;
                    break;
                }
            }
            if (suitableFPSRange == null) {
                Log.i(TAG, "No suitable FPS range?");
                return;
            }
            int[] currentFpsRange = new int[2];
            parameters.getPreviewFpsRange(currentFpsRange);
            if (Arrays.equals(currentFpsRange, suitableFPSRange)) {
                Log.i(TAG, "FPS range already set to " + Arrays.toString(suitableFPSRange));
                return;
            }
            Log.i(TAG, "Setting FPS range to " + Arrays.toString(suitableFPSRange));
            parameters.setPreviewFpsRange(suitableFPSRange[0], suitableFPSRange[1]);
        }
    }

    @TargetApi(15)
    public static void setFocusArea(Camera.Parameters parameters) {
        if (parameters.getMaxNumFocusAreas() > 0) {
            Log.i(TAG, "Old focus areas: " + toString((Iterable<Camera.Area>) parameters.getFocusAreas()));
            List<Camera.Area> middleArea = buildMiddleArea(AREA_PER_1000);
            Log.i(TAG, "Setting focus area to : " + toString((Iterable<Camera.Area>) middleArea));
            parameters.setFocusAreas(middleArea);
            return;
        }
        Log.i(TAG, "Device does not support focus areas");
    }

    @TargetApi(15)
    public static void setMetering(Camera.Parameters parameters) {
        if (parameters.getMaxNumMeteringAreas() > 0) {
            Log.i(TAG, "Old metering areas: " + parameters.getMeteringAreas());
            List<Camera.Area> middleArea = buildMiddleArea(AREA_PER_1000);
            Log.i(TAG, "Setting metering area to : " + toString((Iterable<Camera.Area>) middleArea));
            parameters.setMeteringAreas(middleArea);
            return;
        }
        Log.i(TAG, "Device does not support metering areas");
    }

    @TargetApi(15)
    private static List<Camera.Area> buildMiddleArea(int areaPer1000) {
        return Collections.singletonList(new Camera.Area(new Rect(-areaPer1000, -areaPer1000, areaPer1000, areaPer1000), 1));
    }

    @TargetApi(15)
    public static void setVideoStabilization(Camera.Parameters parameters) {
        if (!parameters.isVideoStabilizationSupported()) {
            Log.i(TAG, "This device does not support video stabilization");
        } else if (parameters.getVideoStabilization()) {
            Log.i(TAG, "Video stabilization already enabled");
        } else {
            Log.i(TAG, "Enabling video stabilization...");
            parameters.setVideoStabilization(true);
        }
    }

    public static void setBarcodeSceneMode(Camera.Parameters parameters) {
        if (Fields.Dgo_in2_battery.barcode.equals(parameters.getSceneMode())) {
            Log.i(TAG, "Barcode scene mode already set");
            return;
        }
        String sceneMode = findSettableValue("scene mode", parameters.getSupportedSceneModes(), Fields.Dgo_in2_battery.barcode);
        if (sceneMode != null) {
            parameters.setSceneMode(sceneMode);
        }
    }

    public static void setZoom(Camera.Parameters parameters, double targetZoomRatio) {
        if (parameters.isZoomSupported()) {
            Integer zoom = indexOfClosestZoom(parameters, targetZoomRatio);
            if (zoom != null) {
                if (parameters.getZoom() == zoom.intValue()) {
                    Log.i(TAG, "Zoom is already set to " + zoom);
                    return;
                }
                Log.i(TAG, "Setting zoom to " + zoom);
                parameters.setZoom(zoom.intValue());
                return;
            }
            return;
        }
        Log.i(TAG, "Zoom is not supported");
    }

    private static Integer indexOfClosestZoom(Camera.Parameters parameters, double targetZoomRatio) {
        List<Integer> ratios = parameters.getZoomRatios();
        Log.i(TAG, "Zoom ratios: " + ratios);
        int maxZoom = parameters.getMaxZoom();
        if (ratios == null || ratios.isEmpty() || ratios.size() != maxZoom + 1) {
            Log.w(TAG, "Invalid zoom ratios!");
            return null;
        }
        double target100 = 100.0d * targetZoomRatio;
        double smallestDiff = Double.POSITIVE_INFINITY;
        int closestIndex = 0;
        for (int i = 0; i < ratios.size(); i++) {
            double diff = Math.abs(((double) ratios.get(i).intValue()) - target100);
            if (diff < smallestDiff) {
                smallestDiff = diff;
                closestIndex = i;
            }
        }
        Log.i(TAG, "Chose zoom ratio of " + (((double) ratios.get(closestIndex).intValue()) / 100.0d));
        return Integer.valueOf(closestIndex);
    }

    public static void setInvertColor(Camera.Parameters parameters) {
        if ("negative".equals(parameters.getColorEffect())) {
            Log.i(TAG, "Negative effect already set");
            return;
        }
        String colorMode = findSettableValue("color effect", parameters.getSupportedColorEffects(), "negative");
        if (colorMode != null) {
            parameters.setColorEffect(colorMode);
        }
    }

    private static String findSettableValue(String name, Collection<String> supportedValues, String... desiredValues) {
        Log.i(TAG, "Requesting " + name + " value from among: " + Arrays.toString(desiredValues));
        Log.i(TAG, "Supported " + name + " values: " + supportedValues);
        if (supportedValues != null) {
            for (String desiredValue : desiredValues) {
                if (supportedValues.contains(desiredValue)) {
                    Log.i(TAG, "Can set " + name + " to: " + desiredValue);
                    return desiredValue;
                }
            }
        }
        Log.i(TAG, "No supported values match");
        return null;
    }

    private static String toString(Collection<int[]> arrays) {
        if (arrays == null || arrays.isEmpty()) {
            return XMPConst.ARRAY_ITEM_NAME;
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('[');
        Iterator<int[]> it2 = arrays.iterator();
        while (it2.hasNext()) {
            buffer.append(Arrays.toString(it2.next()));
            if (it2.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(']');
        return buffer.toString();
    }

    @TargetApi(15)
    private static String toString(Iterable<Camera.Area> areas) {
        if (areas == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (Camera.Area area : areas) {
            result.append(area.rect).append(':').append(area.weight).append(' ');
        }
        return result.toString();
    }

    public static String collectStats(Camera.Parameters parameters) {
        return collectStats(parameters.flatten());
    }

    public static String collectStats(CharSequence flattenedParams) {
        StringBuilder result = new StringBuilder(1000);
        result.append("BOARD=").append(Build.BOARD).append(10);
        result.append("BRAND=").append(Build.BRAND).append(10);
        result.append("CPU_ABI=").append(Build.CPU_ABI).append(10);
        result.append("DEVICE=").append(Build.DEVICE).append(10);
        result.append("DISPLAY=").append(Build.DISPLAY).append(10);
        result.append("FINGERPRINT=").append(Build.FINGERPRINT).append(10);
        result.append("HOST=").append(Build.HOST).append(10);
        result.append("ID=").append(Build.ID).append(10);
        result.append("MANUFACTURER=").append(Build.MANUFACTURER).append(10);
        result.append("MODEL=").append(Build.MODEL).append(10);
        result.append("PRODUCT=").append(Build.PRODUCT).append(10);
        result.append("TAGS=").append(Build.TAGS).append(10);
        result.append("TIME=").append(Build.TIME).append(10);
        result.append("TYPE=").append(Build.TYPE).append(10);
        result.append("USER=").append(Build.USER).append(10);
        result.append("VERSION.CODENAME=").append(Build.VERSION.CODENAME).append(10);
        result.append("VERSION.INCREMENTAL=").append(Build.VERSION.INCREMENTAL).append(10);
        result.append("VERSION.RELEASE=").append(Build.VERSION.RELEASE).append(10);
        result.append("VERSION.SDK_INT=").append(Build.VERSION.SDK_INT).append(10);
        if (flattenedParams != null) {
            String[] params = SEMICOLON.split(flattenedParams);
            Arrays.sort(params);
            for (String param : params) {
                result.append(param).append(10);
            }
        }
        return result.toString();
    }
}
