package com.drew.metadata.exif;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.imaging.tiff.TiffProcessingException;
import com.drew.imaging.tiff.TiffReader;
import com.drew.lang.ByteArrayReader;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import java.io.IOException;
import java.util.Collections;

public class ExifReader implements JpegSegmentMetadataReader {
    static final /* synthetic */ boolean $assertionsDisabled = (!ExifReader.class.desiredAssertionStatus());
    public static final String JPEG_SEGMENT_PREAMBLE = "Exif\u0000\u0000";

    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APP1);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        if ($assertionsDisabled || segmentType == JpegSegmentType.APP1) {
            for (byte[] segmentBytes : segments) {
                if (segmentBytes.length >= JPEG_SEGMENT_PREAMBLE.length() && new String(segmentBytes, 0, JPEG_SEGMENT_PREAMBLE.length()).equals(JPEG_SEGMENT_PREAMBLE)) {
                    extract(new ByteArrayReader(segmentBytes), metadata, JPEG_SEGMENT_PREAMBLE.length());
                }
            }
            return;
        }
        throw new AssertionError();
    }

    public void extract(@NotNull RandomAccessReader reader, @NotNull Metadata metadata) {
        extract(reader, metadata, 0);
    }

    public void extract(@NotNull RandomAccessReader reader, @NotNull Metadata metadata, int readerOffset) {
        extract(reader, metadata, readerOffset, null);
    }

    public void extract(@NotNull RandomAccessReader reader, @NotNull Metadata metadata, int readerOffset, @Nullable Directory parentDirectory) {
        ExifTiffHandler exifTiffHandler = new ExifTiffHandler(metadata, parentDirectory);
        try {
            new TiffReader().processTiff(reader, exifTiffHandler, readerOffset);
        } catch (TiffProcessingException e) {
            exifTiffHandler.error("Exception processing TIFF data: " + e.getMessage());
            e.printStackTrace(System.err);
        } catch (IOException e2) {
            exifTiffHandler.error("Exception processing TIFF data: " + e2.getMessage());
            e2.printStackTrace(System.err);
        }
    }
}
