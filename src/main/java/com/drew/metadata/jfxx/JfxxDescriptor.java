package com.drew.metadata.jfxx;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class JfxxDescriptor extends TagDescriptor<JfxxDirectory> {
    public JfxxDescriptor(@NotNull JfxxDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 5:
                return getExtensionCodeDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getExtensionCodeDescription() {
        Integer value = ((JfxxDirectory) this._directory).getInteger(5);
        if (value == null) {
            return null;
        }
        switch (value.intValue()) {
            case 16:
                return "Thumbnail coded using JPEG";
            case 17:
                return "Thumbnail stored using 1 byte/pixel";
            case 18:
            default:
                return "Unknown extension code " + value;
            case 19:
                return "Thumbnail stored using 3 bytes/pixel";
        }
    }
}
