package com.drew.metadata.exif.makernotes;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class RicohMakernoteDirectory extends Directory {
    public static final int TAG_MAKERNOTE_DATA_TYPE = 1;
    public static final int TAG_PRINT_IMAGE_MATCHING_INFO = 3584;
    public static final int TAG_RICOH_CAMERA_INFO_MAKERNOTE_SUB_IFD_POINTER = 8193;
    public static final int TAG_VERSION = 2;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Makernote Data Type");
        _tagNameMap.put(2, "Version");
        _tagNameMap.put(3584, "Print Image Matching (PIM) Info");
        _tagNameMap.put(8193, "Ricoh Camera Info Makernote Sub-IFD");
    }

    public RicohMakernoteDirectory() {
        setDescriptor(new RicohMakernoteDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "Ricoh Makernote";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }
}
