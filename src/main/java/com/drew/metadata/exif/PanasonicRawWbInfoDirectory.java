package com.drew.metadata.exif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class PanasonicRawWbInfoDirectory extends Directory {
    public static final int TagNumWbEntries = 0;
    public static final int TagWbRbLevels1 = 2;
    public static final int TagWbRbLevels2 = 5;
    public static final int TagWbRbLevels3 = 8;
    public static final int TagWbRbLevels4 = 11;
    public static final int TagWbRbLevels5 = 14;
    public static final int TagWbRbLevels6 = 17;
    public static final int TagWbRbLevels7 = 20;
    public static final int TagWbType1 = 1;
    public static final int TagWbType2 = 4;
    public static final int TagWbType3 = 7;
    public static final int TagWbType4 = 10;
    public static final int TagWbType5 = 13;
    public static final int TagWbType6 = 16;
    public static final int TagWbType7 = 19;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(0, "Num WB Entries");
        _tagNameMap.put(1, "WB Type 1");
        _tagNameMap.put(2, "WB RGB Levels 1");
        _tagNameMap.put(4, "WB Type 2");
        _tagNameMap.put(5, "WB RGB Levels 2");
        _tagNameMap.put(7, "WB Type 3");
        _tagNameMap.put(8, "WB RGB Levels 3");
        _tagNameMap.put(10, "WB Type 4");
        _tagNameMap.put(11, "WB RGB Levels 4");
        _tagNameMap.put(13, "WB Type 5");
        _tagNameMap.put(14, "WB RGB Levels 5");
        _tagNameMap.put(16, "WB Type 6");
        _tagNameMap.put(17, "WB RGB Levels 6");
        _tagNameMap.put(19, "WB Type 7");
        _tagNameMap.put(20, "WB RGB Levels 7");
    }

    public PanasonicRawWbInfoDirectory() {
        setDescriptor(new PanasonicRawWbInfoDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "PanasonicRaw WbInfo";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
