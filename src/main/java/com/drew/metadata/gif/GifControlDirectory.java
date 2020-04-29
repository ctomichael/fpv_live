package com.drew.metadata.gif;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import java.util.HashMap;

public class GifControlDirectory extends Directory {
    public static final int TAG_DELAY = 1;
    public static final int TAG_DISPOSAL_METHOD = 2;
    public static final int TAG_TRANSPARENT_COLOR_FLAG = 4;
    public static final int TAG_TRANSPARENT_COLOR_INDEX = 5;
    public static final int TAG_USER_INPUT_FLAG = 3;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();

    static {
        _tagNameMap.put(1, "Delay");
        _tagNameMap.put(2, "Disposal Method");
        _tagNameMap.put(3, "User Input Flag");
        _tagNameMap.put(4, "Transparent Color Flag");
        _tagNameMap.put(5, "Transparent Color Index");
    }

    public GifControlDirectory() {
        setDescriptor(new GifControlDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "GIF Control";
    }

    @NotNull
    public DisposalMethod getDisposalMethod() {
        return (DisposalMethod) getObject(2);
    }

    public boolean isTransparent() {
        Boolean transparent = getBooleanObject(4);
        if (transparent != null) {
            return transparent.booleanValue();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }

    public enum DisposalMethod {
        NOT_SPECIFIED,
        DO_NOT_DISPOSE,
        RESTORE_TO_BACKGROUND_COLOR,
        RESTORE_TO_PREVIOUS,
        TO_BE_DEFINED,
        INVALID;

        public static DisposalMethod typeOf(int value) {
            switch (value) {
                case 0:
                    return NOT_SPECIFIED;
                case 1:
                    return DO_NOT_DISPOSE;
                case 2:
                    return RESTORE_TO_BACKGROUND_COLOR;
                case 3:
                    return RESTORE_TO_PREVIOUS;
                case 4:
                case 5:
                case 6:
                case 7:
                    return TO_BE_DEFINED;
                default:
                    return INVALID;
            }
        }

        public String toString() {
            switch (this) {
                case DO_NOT_DISPOSE:
                    return "Don't Dispose";
                case INVALID:
                    return "Invalid value";
                case NOT_SPECIFIED:
                    return "Not Specified";
                case RESTORE_TO_BACKGROUND_COLOR:
                    return "Restore to Background Color";
                case RESTORE_TO_PREVIOUS:
                    return "Restore to Previous";
                case TO_BE_DEFINED:
                    return "To Be Defined";
                default:
                    return super.toString();
            }
        }
    }
}
