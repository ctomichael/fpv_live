package dji.component.playback.model.video;

import com.drew.metadata.exif.makernotes.OlympusImageProcessingMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import dji.pilot.publics.model.ICameraResMode;

public enum PlaybackFileResolution {
    Size_640_480p(0, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, 480),
    Size_1280_640p(2, 1280, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE),
    Size_1280_720p(4, 1280, 720),
    Size_1280_960p(6, 1280, 960),
    Size_1920_960p(8, 1920, 960),
    Size_1920_1080p(10, 1920, PhotoshopDirectory.TAG_COUNT_INFORMATION),
    Size_1920_1440p(12, 1920, 1440),
    Size_3840_1920p(14, 3840, 1920),
    Size_3840_2160p(16, 3840, 2160),
    Size_3840_2880p(18, 3840, 2880),
    Size_4096_2048p(20, 4096, 2048),
    Size_4096_2160p(22, 4096, 2160),
    Size_2704_1520p(24, 2704, 1520),
    Size_640_512p(26, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, 512),
    Size_4608_2160p(27, OlympusImageProcessingMakernoteDirectory.TagFacesDetected, 2160),
    Size_4608_2592p(28, OlympusImageProcessingMakernoteDirectory.TagFacesDetected, 2592),
    Size_848_480p(29, 848, 480),
    Size_2720_1530p(31, 2720, 1530),
    Size_5280_2160p(32, 5280, 2160),
    Size_5280_2970p(33, 5280, 2970),
    Size_336_256p(38, IptcDirectory.TAG_TIME_SENT, 256),
    Size_640_480i(1, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, 480),
    Size_1280_640i(3, 1280, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE),
    Size_1280_720i(5, 1280, 720),
    Size_1280_960i(7, 1280, 960),
    Size_1920_960i(9, 1920, 960),
    Size_1920_1080i(11, 1920, PhotoshopDirectory.TAG_COUNT_INFORMATION),
    Size_1920_1440i(13, 1920, 1440),
    Size_3840_1920i(15, 3840, 1920),
    Size_3840_2160i(17, 3840, 2160),
    Size_3840_2880i(19, 3840, 2880),
    Size_4096_2048i(21, 4096, 2048),
    Size_4096_2160i(23, 4096, 2160),
    Size_2704_1520i(25, 2704, 1520),
    Size_848_480i(30, 848, 480),
    R_2720x1530p(31, 2720, 1530),
    R_5280x2160p(32, 5280, 2160),
    R_5280x2970p(33, 5280, 2972),
    R_3840x1572p(34, 3840, 1572),
    R_2688x1512p(45, 2688, 1512),
    R_MAX(ICameraResMode.ICameraVideoResolutionRes.VR_MAX, 5280, 2972),
    UNDEFINED(100),
    UNSET(254),
    UNKNOWN(255);
    
    private int data;
    private int height;
    private int relValue;
    private int width;

    private PlaybackFileResolution(int _data) {
        this.data = _data;
    }

    private PlaybackFileResolution(int _data, int width2, int height2) {
        this.data = _data;
        this.width = width2;
        this.height = height2;
    }

    public int value() {
        return this.data;
    }

    public int relValue() {
        return this.relValue;
    }

    private void setRelValue(int relValue2) {
        this.relValue = relValue2;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PlaybackFileResolution find(int b) {
        PlaybackFileResolution result = UNDEFINED;
        int i = 0;
        while (true) {
            if (i >= values().length) {
                break;
            } else if (values()[i]._equals(b)) {
                result = values()[i];
                break;
            } else {
                i++;
            }
        }
        result.setRelValue(b);
        return result;
    }

    public static PlaybackFileResolution find(String ext) {
        PlaybackFileResolution result = UNDEFINED;
        for (int i = 0; i < values().length; i++) {
            if (values()[i].toString().equals(ext)) {
                return values()[i];
            }
        }
        return result;
    }

    public boolean is4K() {
        return this.width > Size_1920_1440i.getWidth() && this.height > Size_1920_1440i.getHeight();
    }

    public boolean isLowerThan(PlaybackFileResolution resolution) {
        return this.width < resolution.getWidth() || this.height < resolution.getHeight();
    }
}
