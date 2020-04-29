package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class OlympusFocusInfoMakernoteDirectory extends Directory {
    public static final int TagAfInfo = 808;
    public static final int TagAfPoint = 776;
    public static final int TagAutoFocus = 521;
    public static final int TagExternalFlash = 4609;
    public static final int TagExternalFlashBounce = 4612;
    public static final int TagExternalFlashGuideNumber = 4611;
    public static final int TagExternalFlashZoom = 4613;
    public static final int TagFocusDistance = 773;
    public static final int TagFocusInfoVersion = 0;
    public static final int TagFocusStepCount = 769;
    public static final int TagFocusStepInfinity = 771;
    public static final int TagFocusStepNear = 772;
    public static final int TagImageStabilization = 5632;
    public static final int TagInternalFlash = 4616;
    public static final int TagMacroLed = 4618;
    public static final int TagManualFlash = 4617;
    public static final int TagSceneArea = 529;
    public static final int TagSceneDetect = 528;
    public static final int TagSceneDetectData = 530;
    public static final int TagSensorTemperature = 5376;
    public static final int TagZoomStepCount = 768;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Focus Info Version");
        _tagNameMap.put(521, "Auto Focus");
        _tagNameMap.put(528, "Scene Detect");
        _tagNameMap.put(529, "Scene Area");
        _tagNameMap.put(530, "Scene Detect Data");
        _tagNameMap.put(768, "Zoom Step Count");
        _tagNameMap.put(769, "Focus Step Count");
        _tagNameMap.put(771, "Focus Step Infinity");
        _tagNameMap.put(772, "Focus Step Near");
        _tagNameMap.put(773, "Focus Distance");
        _tagNameMap.put(Integer.valueOf((int) TagAfPoint), "AF Point");
        _tagNameMap.put(Integer.valueOf((int) TagAfInfo), "AF Info");
        _tagNameMap.put(4609, "External Flash");
        _tagNameMap.put(4611, "External Flash Guide Number");
        _tagNameMap.put(Integer.valueOf((int) TagExternalFlashBounce), "External Flash Bounce");
        _tagNameMap.put(Integer.valueOf((int) TagExternalFlashZoom), "External Flash Zoom");
        _tagNameMap.put(Integer.valueOf((int) TagInternalFlash), "Internal Flash");
        _tagNameMap.put(Integer.valueOf((int) TagManualFlash), "Manual Flash");
        _tagNameMap.put(Integer.valueOf((int) TagMacroLed), "Macro LED");
        _tagNameMap.put(Integer.valueOf((int) TagSensorTemperature), "Sensor Temperature");
        _tagNameMap.put(Integer.valueOf((int) TagImageStabilization), "Image Stabilization");
    }

    public OlympusFocusInfoMakernoteDirectory() {
        setDescriptor(new OlympusFocusInfoMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Olympus Focus Info";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
