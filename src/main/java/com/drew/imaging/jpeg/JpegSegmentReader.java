package com.drew.imaging.jpeg;

import com.drew.lang.SequentialReader;
import com.drew.lang.StreamReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JpegSegmentReader {
    static final /* synthetic */ boolean $assertionsDisabled = (!JpegSegmentReader.class.desiredAssertionStatus());
    private static final byte MARKER_EOI = -39;
    private static final byte SEGMENT_IDENTIFIER = -1;
    private static final byte SEGMENT_SOS = -38;

    @NotNull
    public static JpegSegmentData readSegments(@NotNull File file, @Nullable Iterable<JpegSegmentType> segmentTypes) throws JpegProcessingException, IOException {
        FileInputStream stream = null;
        try {
            FileInputStream stream2 = new FileInputStream(file);
            try {
                JpegSegmentData readSegments = readSegments(new StreamReader(stream2), segmentTypes);
                if (stream2 != null) {
                    stream2.close();
                }
                return readSegments;
            } catch (Throwable th) {
                th = th;
                stream = stream2;
            }
        } catch (Throwable th2) {
            th = th2;
            if (stream != null) {
                stream.close();
            }
            throw th;
        }
    }

    @NotNull
    public static JpegSegmentData readSegments(@NotNull SequentialReader reader, @Nullable Iterable<JpegSegmentType> segmentTypes) throws JpegProcessingException, IOException {
        if ($assertionsDisabled || reader.isMotorolaByteOrder()) {
            int magicNumber = reader.getUInt16();
            if (magicNumber != 65496) {
                throw new JpegProcessingException("JPEG data is expected to begin with 0xFFD8 (ÿØ) not 0x" + Integer.toHexString(magicNumber));
            }
            Set<Byte> segmentTypeBytes = null;
            if (segmentTypes != null) {
                segmentTypeBytes = new HashSet<>();
                for (JpegSegmentType segmentType : segmentTypes) {
                    segmentTypeBytes.add(Byte.valueOf(segmentType.byteValue));
                }
            }
            JpegSegmentData segmentData = new JpegSegmentData();
            while (true) {
                byte segmentIdentifier = reader.getInt8();
                byte segmentType2 = reader.getInt8();
                while (true) {
                    if (segmentIdentifier == -1 && segmentType2 != -1 && segmentType2 != 0) {
                        break;
                    }
                    segmentIdentifier = segmentType2;
                    segmentType2 = reader.getInt8();
                }
                if (segmentType2 == -38 || segmentType2 == -39) {
                    break;
                }
                int segmentLength = reader.getUInt16() - 2;
                if (segmentLength < 0) {
                    throw new JpegProcessingException("JPEG segment size would be less than zero");
                } else if (segmentTypeBytes == null || segmentTypeBytes.contains(Byte.valueOf(segmentType2))) {
                    byte[] segmentBytes = reader.getBytes(segmentLength);
                    if ($assertionsDisabled || segmentLength == segmentBytes.length) {
                        segmentData.addSegment(segmentType2, segmentBytes);
                    } else {
                        throw new AssertionError();
                    }
                } else if (!reader.trySkip((long) segmentLength)) {
                    break;
                }
            }
            return segmentData;
        }
        throw new AssertionError();
    }

    private JpegSegmentReader() throws Exception {
        throw new Exception("Not intended for instantiation.");
    }
}
