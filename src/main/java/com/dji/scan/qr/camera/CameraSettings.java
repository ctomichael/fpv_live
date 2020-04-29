package com.dji.scan.qr.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class CameraSettings {
    private boolean autoFocusEnabled = true;
    private boolean autoTorchEnabled = false;
    private boolean barcodeSceneModeEnabled = false;
    private boolean continuousFocusEnabled = false;
    private boolean exposureEnabled = false;
    private FocusMode focusMode = FocusMode.AUTO;
    private boolean meteringEnabled = false;
    private int requestedCameraId = -1;
    private boolean scanInverted = false;

    public enum FocusMode {
        AUTO,
        CONTINUOUS,
        INFINITY,
        MACRO
    }

    public int getRequestedCameraId() {
        return this.requestedCameraId;
    }

    public void setRequestedCameraId(int requestedCameraId2) {
        this.requestedCameraId = requestedCameraId2;
    }

    public boolean isScanInverted() {
        return this.scanInverted;
    }

    public void setScanInverted(boolean scanInverted2) {
        this.scanInverted = scanInverted2;
    }

    public boolean isBarcodeSceneModeEnabled() {
        return this.barcodeSceneModeEnabled;
    }

    public void setBarcodeSceneModeEnabled(boolean barcodeSceneModeEnabled2) {
        this.barcodeSceneModeEnabled = barcodeSceneModeEnabled2;
    }

    public boolean isExposureEnabled() {
        return this.exposureEnabled;
    }

    public void setExposureEnabled(boolean exposureEnabled2) {
        this.exposureEnabled = exposureEnabled2;
    }

    public boolean isMeteringEnabled() {
        return this.meteringEnabled;
    }

    public void setMeteringEnabled(boolean meteringEnabled2) {
        this.meteringEnabled = meteringEnabled2;
    }

    public boolean isAutoFocusEnabled() {
        return this.autoFocusEnabled;
    }

    public void setAutoFocusEnabled(boolean autoFocusEnabled2) {
        this.autoFocusEnabled = autoFocusEnabled2;
        if (autoFocusEnabled2 && this.continuousFocusEnabled) {
            this.focusMode = FocusMode.CONTINUOUS;
        } else if (autoFocusEnabled2) {
            this.focusMode = FocusMode.AUTO;
        } else {
            this.focusMode = null;
        }
    }

    public boolean isContinuousFocusEnabled() {
        return this.continuousFocusEnabled;
    }

    public void setContinuousFocusEnabled(boolean continuousFocusEnabled2) {
        this.continuousFocusEnabled = continuousFocusEnabled2;
        if (continuousFocusEnabled2) {
            this.focusMode = FocusMode.CONTINUOUS;
        } else if (this.autoFocusEnabled) {
            this.focusMode = FocusMode.AUTO;
        } else {
            this.focusMode = null;
        }
    }

    public FocusMode getFocusMode() {
        return this.focusMode;
    }

    public void setFocusMode(FocusMode focusMode2) {
        this.focusMode = focusMode2;
    }

    public boolean isAutoTorchEnabled() {
        return this.autoTorchEnabled;
    }

    public void setAutoTorchEnabled(boolean autoTorchEnabled2) {
        this.autoTorchEnabled = autoTorchEnabled2;
    }
}
