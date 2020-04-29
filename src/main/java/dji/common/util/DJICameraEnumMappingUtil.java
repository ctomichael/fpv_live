package dji.common.util;

import dji.common.camera.CameraSSDVideoLicense;
import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraGetPushRawParams;
import dji.midware.data.model.P3.DataCameraSetVOutParams;
import dji.sdksharedlib.keycatalog.extension.InternalKey;

@EXClassNullAway
public class DJICameraEnumMappingUtil {
    public static ResolutionAndFrameRate wrapResolutionAndFrameRate(int resolution, int frameRate) {
        return new ResolutionAndFrameRate(mapProtocolToResolution(resolution), mapProtocolToFrameRate(frameRate));
    }

    public static ResolutionAndFrameRate wrapResolutionAndFrameRate(int resolution, int frameRate, int fovType) {
        return new ResolutionAndFrameRate(mapProtocolToResolution(resolution), mapProtocolToFrameRate(frameRate), SettingsDefinitions.VideoFov.find(fovType));
    }

    public static SettingsDefinitions.VideoResolution mapProtocolToResolution(int resolution) {
        return SettingsDefinitions.VideoResolution.findWithCmdValue(resolution);
    }

    public static SettingsDefinitions.VideoFrameRate mapProtocolToFrameRate(int frameRate) {
        return SettingsDefinitions.VideoFrameRate.findFrameRateWithCmdValue(frameRate);
    }

    public static int getResolutionProtocolValue(SettingsDefinitions.VideoResolution videoResolution) {
        return videoResolution.cmdValue();
    }

    public static SettingsDefinitions.VideoResolution getAvailableSDResolutionFromSSD(ResolutionAndFrameRate ssdResAndRate, ResolutionAndFrameRate[] range) {
        if (range == null || range.length == 0) {
            return ssdResAndRate.getResolution();
        }
        SettingsDefinitions.VideoResolution resolution = null;
        for (ResolutionAndFrameRate sdRate : range) {
            if (sdRate.equals(ssdResAndRate)) {
                return ssdResAndRate.getResolution();
            }
            SettingsDefinitions.VideoFrameRate sdcardFps = sdRate.getFrameRate();
            SettingsDefinitions.VideoResolution sdcardResolution = sdRate.getResolution();
            if (sdcardFps.equals(ssdResAndRate.getFrameRate()) && sdcardResolution.ratio() == ssdResAndRate.getResolution().ratio()) {
                if (resolution == null) {
                    resolution = sdRate.getResolution();
                } else if (resolution.cmdValue() < sdRate.getResolution().cmdValue()) {
                    resolution = sdRate.getResolution();
                }
            }
        }
        return resolution;
    }

    public static int getFrameRateProtocolValue(SettingsDefinitions.VideoFrameRate videoFrameRate) {
        return videoFrameRate.cmdValue();
    }

    public static DataCameraGetPushRawParams.RawMode getRAWModeFromSDKLicense(CameraSSDVideoLicense license) {
        DataCameraGetPushRawParams.RawMode rawMode = DataCameraGetPushRawParams.RawMode.ProrseOFF;
        switch (license) {
            case LicenseKeyTypeCinemaDNG:
                return DataCameraGetPushRawParams.RawMode.JPEGLossLess;
            case LicenseKeyTypeProRes422HQ:
                return DataCameraGetPushRawParams.RawMode.ProresHQ422;
            case LicenseKeyTypeProRes4444XQ:
                return DataCameraGetPushRawParams.RawMode.ProresHQ444;
            default:
                return rawMode;
        }
    }

    public static CameraSSDVideoLicense getSDKLicenseFromRAWMode(DataCameraGetPushRawParams.RawMode rawMode) {
        CameraSSDVideoLicense license = CameraSSDVideoLicense.Unknown;
        switch (rawMode) {
            case JPEGLossLess:
                return CameraSSDVideoLicense.LicenseKeyTypeCinemaDNG;
            case ProresHQ422:
                return CameraSSDVideoLicense.LicenseKeyTypeProRes422HQ;
            case ProresHQ444:
                return CameraSSDVideoLicense.LicenseKeyTypeProRes4444XQ;
            default:
                return license;
        }
    }

    @InternalKey
    public static DataCameraSetVOutParams.LCDFormat mapProtocolToSecondOutputFormat(int stream) {
        DataCameraSetVOutParams.LCDFormat lCDFormat = DataCameraSetVOutParams.LCDFormat.AUTO_NO_GLASS_CONNECTED;
        switch (stream) {
            case 2:
                return DataCameraSetVOutParams.LCDFormat.HD_FORMAT;
            case 3:
                return DataCameraSetVOutParams.LCDFormat.AUTO_GLASS_CONNECTED;
            default:
                return DataCameraSetVOutParams.LCDFormat.AUTO_NO_GLASS_CONNECTED;
        }
    }

    public static int mapSecondOutputFormatToProtocol(DataCameraSetVOutParams.LCDFormat format) {
        if (format == null) {
            return 1;
        }
        switch (format) {
            case HD_FORMAT:
                return 2;
            case AUTO_GLASS_CONNECTED:
                return 3;
            default:
                return 1;
        }
    }
}
