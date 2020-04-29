package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import java.util.HashMap;

public final class ErrorDirectory extends Directory {
    public ErrorDirectory() {
    }

    public ErrorDirectory(String error) {
        super.addError(error);
    }

    @NotNull
    public String getName() {
        return "Error";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return new HashMap<>();
    }

    @NotNull
    public String getTagName(int tagType) {
        return "";
    }

    public boolean hasTagName(int tagType) {
        return false;
    }

    public void setObject(int tagType, @NotNull Object value) {
        throw new UnsupportedOperationException(String.format("Cannot add value to %s.", ErrorDirectory.class.getName()));
    }
}
