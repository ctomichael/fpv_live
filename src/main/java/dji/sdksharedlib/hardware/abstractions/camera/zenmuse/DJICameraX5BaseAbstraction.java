package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.common.camera.CameraSSDVideoLicense;
import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SSDCapacity;
import dji.common.camera.SSDOperationState;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.common.util.DJICameraEnumMappingUtil;
import dji.common.util.LatchHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.model.P3.DataCameraGetPushRawParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraSetEquipInfo;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraX5BaseAbstraction extends DJICameraBaseAbstraction {
    private ArrayList<CameraSSDVideoLicense> cameraSSDVideoLicenseArrayList = new ArrayList<>(3);
    protected LatchHelper latchHelper = LatchHelper.getInstance();

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataCameraGetPushRawParams.getInstance().isGetted()) {
            DataCameraGetPushRawParams.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushRawParams.getInstance());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo pushInfo) {
        super.onEvent3BackgroundThread(pushInfo);
        updateSSDInitialingState();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushRawParams pushInfo) {
        if (pushInfo.isGetted() && isValidSender(pushInfo.getSenderId()) && isSSDSupported() && pushInfo != null) {
            SSDOperationState ssdState = SSDOperationState.find(pushInfo.getDiskStatusValue());
            boolean isConnected = pushInfo.isDiskConnected();
            if (!isConnected) {
                ssdState = SSDOperationState.NOT_FOUND;
            }
            SSDCapacity capacity = SSDCapacity.find(pushInfo.getDiskCapacity());
            int remainingTime = pushInfo.getDiskAvailableTime();
            long remainingCapacity = pushInfo.getAvailableCapacity();
            int resolutionInt = pushInfo.getResolution();
            int frameRateInt = pushInfo.getFps();
            ResolutionAndFrameRate resolutionAndFrameRate = new ResolutionAndFrameRate(DJICameraEnumMappingUtil.mapProtocolToResolution(resolutionInt), DJICameraEnumMappingUtil.mapProtocolToFrameRate(frameRateInt));
            SettingsDefinitions.SSDLegacyColor ssdDigitalFilter = SettingsDefinitions.SSDLegacyColor.find(pushInfo.getSSDDigitalFilter());
            notifyValueChangeForKeyPath(Integer.valueOf(pushInfo.getCurrentRawBurstCount()), convertKeyToPath(CameraKeys.RAW_PHOTO_BURST_COUNT));
            notifyValueChangeForKeyPath(ssdDigitalFilter, convertKeyToPath(CameraKeys.SSD_LEGACY_COLOR));
            notifyValueChangeForKeyPath(ssdState, convertKeyToPath(CameraKeys.SSD_OPERATION_STATE));
            notifyValueChangeForKeyPath(Boolean.valueOf(isConnected), convertKeyToPath(CameraKeys.SSD_IS_CONNECTED));
            notifyValueChangeForKeyPath(capacity, convertKeyToPath(CameraKeys.SSD_TOTAL_SPACE));
            notifyValueChangeForKeyPath(Integer.valueOf(remainingTime), convertKeyToPath(CameraKeys.SSD_AVAILABLE_RECORDING_TIME_IN_SECONDS));
            notifyValueChangeForKeyPath(Long.valueOf(remainingCapacity), convertKeyToPath(CameraKeys.SSD_REMAINING_SPACE_IN_MB));
            notifyValueChangeForKeyPath(resolutionAndFrameRate, convertKeyToPath(CameraKeys.SSD_VIDEO_RESOLUTION_AND_FRAME_RATE));
            notifyValueChangeForKeyPath(new SettingsDefinitions.SSDClipFileName(String.valueOf(pushInfo.getEquipLabel()), pushInfo.getRealName(), pushInfo.getClipId()), convertKeyToPath(CameraKeys.SSD_CLIP_FILE_NAME));
            if (DJICameraEnumMappingUtil.getSDKLicenseFromRAWMode(pushInfo.getRawMode()) == CameraSSDVideoLicense.LicenseKeyTypeCinemaDNG) {
                notifyValueChangeForKeyPath(SettingsDefinitions.SSDColor.RAW_COLOR, convertKeyToPath(CameraKeys.SSD_COLOR));
            } else {
                notifyValueChangeForKeyPath(SettingsDefinitions.SSDColor.find(pushInfo.getLooks()), convertKeyToPath(CameraKeys.SSD_COLOR));
            }
            DataCameraGetPushRawParams.PurchasedResolution purchasedResolution = pushInfo.getPurchasedResolution();
            CameraSSDVideoLicense[] licenses = new CameraSSDVideoLicense[0];
            if (this instanceof DJICameraX7Abstraction) {
                synchronized (this.cameraSSDVideoLicenseArrayList) {
                    this.cameraSSDVideoLicenseArrayList.clear();
                    if (purchasedResolution.isX7SupportCDNG) {
                        this.cameraSSDVideoLicenseArrayList.add(CameraSSDVideoLicense.LicenseKeyTypeCinemaDNG);
                    }
                    if (purchasedResolution.isX7SupportPRORES) {
                        this.cameraSSDVideoLicenseArrayList.add(CameraSSDVideoLicense.LicenseKeyTypeProRes422HQ);
                        this.cameraSSDVideoLicenseArrayList.add(CameraSSDVideoLicense.LicenseKeyTypeProRes4444XQ);
                    }
                }
            } else if (this instanceof DJICameraX5SAbstraction) {
                synchronized (this.cameraSSDVideoLicenseArrayList) {
                    this.cameraSSDVideoLicenseArrayList.clear();
                    if (purchasedResolution.isX5SSupportCDNG || purchasedResolution.is3840x2160JpegLosslessSupported || purchasedResolution.is4096x2160JpegLosslessSupported || purchasedResolution.is5280x2160JpegLosslessSupported) {
                        this.cameraSSDVideoLicenseArrayList.add(CameraSSDVideoLicense.LicenseKeyTypeCinemaDNG);
                    }
                    if (purchasedResolution.isX5SSupportPRORES || purchasedResolution.is3840x2160PRORES422HQSupported || purchasedResolution.is5280x2160PRORES422HQSupported) {
                        this.cameraSSDVideoLicenseArrayList.add(CameraSSDVideoLicense.LicenseKeyTypeProRes422HQ);
                    }
                    if (purchasedResolution.isX5SSupportPRORES || purchasedResolution.is3840x2160PRORES444XQSupported) {
                        this.cameraSSDVideoLicenseArrayList.add(CameraSSDVideoLicense.LicenseKeyTypeProRes4444XQ);
                    }
                }
            }
            if (this.cameraSSDVideoLicenseArrayList.size() > 0) {
                notifyValueChangeForKeyPath(this.cameraSSDVideoLicenseArrayList.toArray(licenses), convertKeyToPath(CameraKeys.SSD_VIDEO_LICENSES));
            } else {
                notifyValueChangeForKeyPath(licenses, convertKeyToPath(CameraKeys.SSD_VIDEO_LICENSES));
            }
            updateSSDInitialingState();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isAudioRecordSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableApertureSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isChangeableLensSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableFocalPointSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isHandHeldProduct() {
        return false;
    }

    /* access modifiers changed from: protected */
    public int checkColIndex(int colIndex) {
        if (!isHandHeldProduct()) {
            return colIndex;
        }
        final String[] version = new String[1];
        this.latchHelper.setUpLatch(1);
        getFirmwareVersion(new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5BaseAbstraction.AnonymousClass1 */

            public void onSuccess(Object o) {
                version[0] = (String) o;
                DJICameraX5BaseAbstraction.this.latchHelper.countDownLatch();
            }

            public void onFails(DJIError error) {
            }
        });
        this.latchHelper.waitForLatch(5);
        if (version[0] == null || version[0].compareTo("01.27.51.34") > 0) {
            return colIndex;
        }
        return 11 - colIndex;
    }

    @Setter(CameraKeys.SSD_CLIP_FILE_NAME)
    public void setSSDClipName(SettingsDefinitions.SSDClipFileName data, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DJIComponentManager.getInstance().getPlatformType() != DJIComponentManager.PlatformType.Inspire2) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else if (data == null || data.getClipID() > 999 || data.getClipID() < 0 || data.getReelID() > 999 || data.getReelID() < 0) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (data.getEquipmentLabel().charAt(0) < 'A' || data.getEquipmentLabel().charAt(0) > 'Z') {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            DataCameraSetEquipInfo setter = new DataCameraSetEquipInfo();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setEquipLabel(data.getEquipmentLabel().charAt(0)).setRealName(data.getReelID()).setClipId(data.getClipID());
            setter.start(CallbackUtils.defaultCB(callback));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0055, code lost:
        r0 = new dji.midware.data.model.P3.DataCameraSetSSDRawVideoDigitalFilter();
        r0.setReceiverId(getReceiverIdByIndex());
        r0.setLooks(r5.value());
        r0.start(dji.common.util.CallbackUtils.defaultCB(r6));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        return;
     */
    @dji.sdksharedlib.hardware.abstractions.Setter(dji.sdksharedlib.keycatalog.CameraKeys.SSD_COLOR)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSSDVideoLook(dji.common.camera.SettingsDefinitions.SSDColor r5, dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.InnerCallback r6) {
        /*
            r4 = this;
            boolean r1 = r4.isSSDSupported()
            if (r1 != 0) goto L_0x000c
            dji.common.error.DJIError r1 = dji.common.error.DJICameraError.COMMON_UNSUPPORTED
            dji.common.util.CallbackUtils.onFailure(r6, r1)
        L_0x000b:
            return
        L_0x000c:
            dji.midware.component.DJIComponentManager r1 = dji.midware.component.DJIComponentManager.getInstance()
            dji.midware.component.DJIComponentManager$PlatformType r1 = r1.getPlatformType()
            dji.midware.component.DJIComponentManager$PlatformType r2 = dji.midware.component.DJIComponentManager.PlatformType.Inspire2
            if (r1 == r2) goto L_0x001e
            dji.common.error.DJIError r1 = dji.common.error.DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE
            dji.common.util.CallbackUtils.onFailure(r6, r1)
            goto L_0x000b
        L_0x001e:
            java.util.ArrayList<dji.common.camera.CameraSSDVideoLicense> r2 = r4.cameraSSDVideoLicenseArrayList
            monitor-enter(r2)
            java.util.ArrayList<dji.common.camera.CameraSSDVideoLicense> r1 = r4.cameraSSDVideoLicenseArrayList     // Catch:{ all -> 0x004a }
            dji.common.camera.CameraSSDVideoLicense r3 = dji.common.camera.CameraSSDVideoLicense.LicenseKeyTypeProRes422HQ     // Catch:{ all -> 0x004a }
            boolean r1 = r1.contains(r3)     // Catch:{ all -> 0x004a }
            if (r1 != 0) goto L_0x0054
            java.util.ArrayList<dji.common.camera.CameraSSDVideoLicense> r1 = r4.cameraSSDVideoLicenseArrayList     // Catch:{ all -> 0x004a }
            dji.common.camera.CameraSSDVideoLicense r3 = dji.common.camera.CameraSSDVideoLicense.LicenseKeyTypeProRes4444XQ     // Catch:{ all -> 0x004a }
            boolean r1 = r1.contains(r3)     // Catch:{ all -> 0x004a }
            if (r1 != 0) goto L_0x0054
            java.util.ArrayList<dji.common.camera.CameraSSDVideoLicense> r1 = r4.cameraSSDVideoLicenseArrayList     // Catch:{ all -> 0x004a }
            dji.common.camera.CameraSSDVideoLicense r3 = dji.common.camera.CameraSSDVideoLicense.LicenseKeyTypeCinemaDNG     // Catch:{ all -> 0x004a }
            boolean r1 = r1.contains(r3)     // Catch:{ all -> 0x004a }
            if (r1 == 0) goto L_0x004d
            dji.common.camera.SettingsDefinitions$SSDColor r1 = dji.common.camera.SettingsDefinitions.SSDColor.RAW_COLOR     // Catch:{ all -> 0x004a }
            if (r5 == r1) goto L_0x004d
            dji.common.error.DJIError r1 = dji.common.error.DJICameraError.COMMON_PARAM_ILLEGAL     // Catch:{ all -> 0x004a }
            dji.common.util.CallbackUtils.onFailure(r6, r1)     // Catch:{ all -> 0x004a }
            monitor-exit(r2)     // Catch:{ all -> 0x004a }
            goto L_0x000b
        L_0x004a:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x004a }
            throw r1
        L_0x004d:
            dji.common.error.DJICameraError r1 = dji.common.error.DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE     // Catch:{ all -> 0x004a }
            dji.common.util.CallbackUtils.onFailure(r6, r1)     // Catch:{ all -> 0x004a }
            monitor-exit(r2)     // Catch:{ all -> 0x004a }
            goto L_0x000b
        L_0x0054:
            monitor-exit(r2)     // Catch:{ all -> 0x004a }
            dji.midware.data.model.P3.DataCameraSetSSDRawVideoDigitalFilter r0 = new dji.midware.data.model.P3.DataCameraSetSSDRawVideoDigitalFilter
            r0.<init>()
            int r1 = r4.getReceiverIdByIndex()
            r0.setReceiverId(r1)
            int r1 = r5.value()
            r0.setLooks(r1)
            dji.midware.interfaces.DJIDataCallBack r1 = dji.common.util.CallbackUtils.defaultCB(r6)
            r0.start(r1)
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5BaseAbstraction.setSSDVideoLook(dji.common.camera.SettingsDefinitions$SSDColor, dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback):void");
    }

    private void updateSSDInitialingState() {
        boolean ssdResult;
        boolean z = true;
        boolean licenseResult = true;
        if (DataCameraGetPushStateInfo.getInstance().getWm620CameraProtocolVersion(getExpectedSenderIdByIndex()) >= Integer.MAX_VALUE && (DataCameraGetPushRawParams.getInstance().getLicenseStatus(getExpectedSenderIdByIndex()) == DataCameraGetPushRawParams.LicenseStatus.UNINITIALIZED || DataCameraGetPushRawParams.getInstance().getLicenseStatus(getExpectedSenderIdByIndex()) == DataCameraGetPushRawParams.LicenseStatus.UNKNOWN)) {
            licenseResult = false;
        }
        if (DataCameraGetPushRawParams.getInstance().getDiskStatus(getExpectedSenderIdByIndex()) == DataCameraGetPushRawParams.DiskStatus.INITIALIZING) {
            ssdResult = true;
        } else {
            ssdResult = false;
        }
        if (!licenseResult || !ssdResult) {
            z = false;
        }
        notifyValueChangeForKeyPath(Boolean.valueOf(z), CameraKeys.IS_SSD_INITIALIZING);
    }
}
