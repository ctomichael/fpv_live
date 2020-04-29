package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class LeicaType5MakernoteDirectory extends Directory {
    public static final int TagExposureMode = 1037;
    public static final int TagFilmMode = 1042;
    public static final int TagLensModel = 771;
    public static final int TagOriginalDirectory = 1032;
    public static final int TagOriginalFileName = 1031;
    public static final int TagShotInfo = 1040;
    public static final int TagWbRgbLevels = 1043;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(771, "Lens Model");
        _tagNameMap.put(1031, "Original File Name");
        _tagNameMap.put(1032, "Original Directory");
        _tagNameMap.put(1037, "Exposure Mode");
        _tagNameMap.put(1040, "Shot Info");
        _tagNameMap.put(1042, "Film Mode");
        _tagNameMap.put(1043, "WB RGB Levels");
    }

    public LeicaType5MakernoteDirectory() {
        setDescriptor(new LeicaType5MakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Leica Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
