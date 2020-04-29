package com.drew.metadata.photoshop;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.ByteArrayReader;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.icc.IccReader;
import com.drew.metadata.iptc.IptcReader;
import com.drew.metadata.xmp.XmpReader;
import java.util.Collections;

public class PhotoshopReader implements JpegSegmentMetadataReader {
    @NotNull
    private static final String JPEG_SEGMENT_PREAMBLE = "Photoshop 3.0";

    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APPD);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        int preambleLength = JPEG_SEGMENT_PREAMBLE.length();
        for (byte[] segmentBytes : segments) {
            if (segmentBytes.length >= preambleLength + 1 && JPEG_SEGMENT_PREAMBLE.equals(new String(segmentBytes, 0, preambleLength))) {
                extract(new SequentialByteArrayReader(segmentBytes, preambleLength + 1), (segmentBytes.length - preambleLength) - 1, metadata);
            }
        }
    }

    public void extract(@NotNull SequentialReader reader, int length, @NotNull Metadata metadata) {
        PhotoshopDirectory directory = new PhotoshopDirectory();
        metadata.addDirectory(directory);
        int pos = 0;
        while (pos < length) {
            try {
                String signature = reader.getString(4);
                int tagType = reader.getUInt16();
                short descriptionLength = reader.getUInt8();
                int pos2 = pos + 4 + 2 + 1;
                if (descriptionLength < 0 || descriptionLength + pos2 > length) {
                    throw new ImageProcessingException("Invalid string length");
                }
                reader.skip((long) descriptionLength);
                int pos3 = pos2 + descriptionLength;
                if (pos3 % 2 != 0) {
                    reader.skip(1);
                    pos3++;
                }
                int byteCount = reader.getInt32();
                byte[] tagBytes = reader.getBytes(byteCount);
                pos = pos3 + 4 + byteCount;
                if (pos % 2 != 0) {
                    reader.skip(1);
                    pos++;
                }
                if (signature.equals("8BIM")) {
                    if (tagType == 1028) {
                        new IptcReader().extract(new SequentialByteArrayReader(tagBytes), metadata, (long) tagBytes.length, directory);
                    } else if (tagType == 1039) {
                        new IccReader().extract(new ByteArrayReader(tagBytes), metadata, directory);
                    } else if (tagType == 1058 || tagType == 1059) {
                        new ExifReader().extract(new ByteArrayReader(tagBytes), metadata, 0, directory);
                    } else if (tagType == 1060) {
                        new XmpReader().extract(tagBytes, metadata, directory);
                    } else {
                        directory.setByteArray(tagType, tagBytes);
                    }
                    if (tagType >= 4000 && tagType <= 4999) {
                        PhotoshopDirectory._tagNameMap.put(Integer.valueOf(tagType), String.format("Plug-in %d Data", Integer.valueOf((tagType - 4000) + 1)));
                    }
                }
            } catch (Exception ex) {
                directory.addError(ex.getMessage());
                return;
            }
        }
    }
}
