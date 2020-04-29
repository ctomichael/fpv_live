package com.drew.metadata.iptc;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.StringValue;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

public class IptcReader implements JpegSegmentMetadataReader {
    private static final byte IptcMarkerByte = 28;

    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APPD);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        for (byte[] segmentBytes : segments) {
            if (segmentBytes.length != 0 && segmentBytes[0] == 28) {
                extract(new SequentialByteArrayReader(segmentBytes), metadata, (long) segmentBytes.length);
            }
        }
    }

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata, long length) {
        extract(reader, metadata, length, null);
    }

    public void extract(@NotNull SequentialReader reader, @NotNull Metadata metadata, long length, @Nullable Directory parentDirectory) {
        IptcDirectory directory = new IptcDirectory();
        metadata.addDirectory(directory);
        if (parentDirectory != null) {
            directory.setParent(parentDirectory);
        }
        int offset = 0;
        while (((long) offset) < length) {
            try {
                short startByte = reader.getUInt8();
                int offset2 = offset + 1;
                if (startByte != 28) {
                    if (((long) offset2) != length) {
                        directory.addError("Invalid IPTC tag marker at offset " + (offset2 - 1) + ". Expected '0x" + Integer.toHexString(28) + "' but got '0x" + Integer.toHexString(startByte) + "'.");
                        return;
                    }
                    return;
                } else if (((long) (offset2 + 5)) > length) {
                    directory.addError("Too few bytes remain for a valid IPTC tag");
                    return;
                } else {
                    try {
                        int directoryType = reader.getUInt8();
                        int tagType = reader.getUInt8();
                        int tagByteCount = reader.getUInt16();
                        int offset3 = offset2 + 4;
                        if (((long) (offset3 + tagByteCount)) > length) {
                            directory.addError("Data for tag extends beyond end of IPTC segment");
                            return;
                        }
                        try {
                            processTag(reader, directory, directoryType, tagType, tagByteCount);
                            offset = offset3 + tagByteCount;
                        } catch (IOException e) {
                            directory.addError("Error processing IPTC tag");
                            return;
                        }
                    } catch (IOException e2) {
                        directory.addError("IPTC data segment ended mid-way through tag descriptor");
                        return;
                    }
                }
            } catch (IOException e3) {
                directory.addError("Unable to read starting byte of IPTC tag");
                return;
            }
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private void processTag(@NotNull SequentialReader reader, @NotNull Directory directory, int directoryType, int tagType, int tagByteCount) throws IOException {
        StringValue string;
        StringValue[] newStrings;
        int tagIdentifier = tagType | (directoryType << 8);
        if (tagByteCount == 0) {
            directory.setString(tagIdentifier, "");
            return;
        }
        switch (tagIdentifier) {
            case 256:
            case 278:
            case IptcDirectory.TAG_ARM_VERSION:
            case 512:
            case IptcDirectory.TAG_PROGRAM_VERSION:
                if (tagByteCount >= 2) {
                    int shortValue = reader.getUInt16();
                    reader.skip((long) (tagByteCount - 2));
                    directory.setInt(tagIdentifier, shortValue);
                    return;
                }
                break;
            case IptcDirectory.TAG_CODED_CHARACTER_SET:
                byte[] bytes = reader.getBytes(tagByteCount);
                String charsetName = Iso2022Converter.convertISO2022CharsetToJavaCharset(bytes);
                if (charsetName == null) {
                    charsetName = new String(bytes);
                }
                directory.setString(tagIdentifier, charsetName);
                return;
            case 522:
                directory.setInt(tagIdentifier, reader.getUInt8());
                reader.skip((long) (tagByteCount - 1));
                return;
        }
        String charSetName = directory.getString(IptcDirectory.TAG_CODED_CHARACTER_SET);
        Charset charset = null;
        if (charSetName != null) {
            try {
                charset = Charset.forName(charSetName);
            } catch (Throwable th) {
            }
        }
        if (charSetName != null) {
            string = reader.getStringValue(tagByteCount, charset);
        } else {
            byte[] bytes2 = reader.getBytes(tagByteCount);
            Charset charSet = Iso2022Converter.guessCharSet(bytes2);
            if (charSet != null) {
                string = new StringValue(bytes2, charSet);
            } else {
                string = new StringValue(bytes2, null);
            }
        }
        if (directory.containsTag(tagIdentifier)) {
            StringValue[] oldStrings = directory.getStringValueArray(tagIdentifier);
            if (oldStrings == null) {
                newStrings = new StringValue[1];
            } else {
                newStrings = new StringValue[(oldStrings.length + 1)];
                System.arraycopy(oldStrings, 0, newStrings, 0, oldStrings.length);
            }
            newStrings[newStrings.length - 1] = string;
            directory.setStringValueArray(tagIdentifier, newStrings);
            return;
        }
        directory.setStringValue(tagIdentifier, string);
    }
}
