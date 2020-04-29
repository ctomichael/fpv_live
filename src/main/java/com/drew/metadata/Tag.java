package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import dji.component.accountcenter.IMemberProtocol;

public class Tag {
    @NotNull
    private final Directory _directory;
    private final int _tagType;

    public Tag(int tagType, @NotNull Directory directory) {
        this._tagType = tagType;
        this._directory = directory;
    }

    public int getTagType() {
        return this._tagType;
    }

    @NotNull
    public String getTagTypeHex() {
        return String.format("0x%04x", Integer.valueOf(this._tagType));
    }

    @Nullable
    public String getDescription() {
        return this._directory.getDescription(this._tagType);
    }

    public boolean hasTagName() {
        return this._directory.hasTagName(this._tagType);
    }

    @NotNull
    public String getTagName() {
        return this._directory.getTagName(this._tagType);
    }

    @NotNull
    public String getDirectoryName() {
        return this._directory.getName();
    }

    @NotNull
    public String toString() {
        String description = getDescription();
        if (description == null) {
            description = this._directory.getString(getTagType()) + " (unable to formulate description)";
        }
        return IMemberProtocol.STRING_SEPERATOR_LEFT + this._directory.getName() + "] " + getTagName() + " - " + description;
    }
}
