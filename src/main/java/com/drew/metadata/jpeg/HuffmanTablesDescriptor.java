package com.drew.metadata.jpeg;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.TagDescriptor;

public class HuffmanTablesDescriptor extends TagDescriptor<HuffmanTablesDirectory> {
    public HuffmanTablesDescriptor(@NotNull HuffmanTablesDirectory directory) {
        super(directory);
    }

    @Nullable
    public String getDescription(int tagType) {
        switch (tagType) {
            case 1:
                return getNumberOfTablesDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getNumberOfTablesDescription() {
        Integer value = ((HuffmanTablesDirectory) this._directory).getInteger(1);
        if (value == null) {
            return null;
        }
        return value + (value.intValue() == 1 ? " Huffman table" : " Huffman tables");
    }
}
