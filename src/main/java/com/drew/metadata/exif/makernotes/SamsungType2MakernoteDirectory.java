package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class SamsungType2MakernoteDirectory extends Directory {
    public static final int TagCameraTemperature = 67;
    public static final int TagDeviceType = 2;
    public static final int TagFaceDetect = 256;
    public static final int TagFaceName = 291;
    public static final int TagFaceRecognition = 288;
    public static final int TagFirmwareName = 40961;
    public static final int TagMakerNoteVersion = 1;
    public static final int TagSamsungModelId = 3;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Maker Note Version");
        _tagNameMap.put(2, "Device Type");
        _tagNameMap.put(3, "Model Id");
        _tagNameMap.put(67, "Camera Temperature");
        _tagNameMap.put(256, "Face Detect");
        _tagNameMap.put(288, "Face Recognition");
        _tagNameMap.put(291, "Face Name");
        _tagNameMap.put(40961, "Firmware Name");
    }

    public SamsungType2MakernoteDirectory() {
        setDescriptor(new SamsungType2MakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Samsung Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
