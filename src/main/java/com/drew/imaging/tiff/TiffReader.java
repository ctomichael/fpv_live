package com.drew.imaging.tiff;

import com.drew.lang.RandomAccessReader;
import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TiffReader {
    public void processTiff(@NotNull RandomAccessReader reader, @NotNull TiffHandler handler, int tiffHeaderOffset) throws TiffProcessingException, IOException {
        short byteOrderIdentifier = reader.getInt16(tiffHeaderOffset);
        if (byteOrderIdentifier == 19789) {
            reader.setMotorolaByteOrder(true);
        } else if (byteOrderIdentifier == 18761) {
            reader.setMotorolaByteOrder(false);
        } else {
            throw new TiffProcessingException("Unclear distinction between Motorola/Intel byte ordering: " + ((int) byteOrderIdentifier));
        }
        handler.setTiffMarker(reader.getUInt16(tiffHeaderOffset + 2));
        int firstIfdOffset = reader.getInt32(tiffHeaderOffset + 4) + tiffHeaderOffset;
        if (((long) firstIfdOffset) >= reader.getLength() - 1) {
            handler.warn("First IFD offset is beyond the end of the TIFF data segment -- trying default offset");
            firstIfdOffset = tiffHeaderOffset + 2 + 2 + 4;
        }
        processIfd(handler, reader, new HashSet<>(), firstIfdOffset, tiffHeaderOffset);
    }

    public static void processIfd(@NotNull TiffHandler handler, @NotNull RandomAccessReader reader, @NotNull Set<Integer> processedIfdOffsets, int ifdOffset, int tiffHeaderOffset) throws IOException {
        long byteCount;
        long tagValueOffset;
        Boolean resetByteOrder = null;
        try {
            if (!processedIfdOffsets.contains(Integer.valueOf(ifdOffset))) {
                processedIfdOffsets.add(Integer.valueOf(ifdOffset));
                if (((long) ifdOffset) >= reader.getLength() || ifdOffset < 0) {
                    handler.error("Ignored IFD marked to start outside data segment");
                    handler.endingIFD();
                    if (resetByteOrder != null) {
                        reader.setMotorolaByteOrder(resetByteOrder.booleanValue());
                        return;
                    }
                    return;
                }
                int dirTagCount = reader.getUInt16(ifdOffset);
                if (dirTagCount > 255 && (dirTagCount & 255) == 0) {
                    resetByteOrder = Boolean.valueOf(reader.isMotorolaByteOrder());
                    dirTagCount >>= 8;
                    reader.setMotorolaByteOrder(!reader.isMotorolaByteOrder());
                }
                if (((long) ((dirTagCount * 12) + 2 + 4 + ifdOffset)) > reader.getLength()) {
                    handler.error("Illegally sized IFD");
                    handler.endingIFD();
                    if (resetByteOrder != null) {
                        reader.setMotorolaByteOrder(resetByteOrder.booleanValue());
                        return;
                    }
                    return;
                }
                int invalidTiffFormatCodeCount = 0;
                int tagNumber = 0;
                while (tagNumber < dirTagCount) {
                    int tagOffset = calculateTagOffset(ifdOffset, tagNumber);
                    int tagId = reader.getUInt16(tagOffset);
                    int formatCode = reader.getUInt16(tagOffset + 2);
                    TiffDataFormat format = TiffDataFormat.fromTiffFormatCode(formatCode);
                    long componentCount = reader.getUInt32(tagOffset + 4);
                    if (format == null) {
                        Long byteCountOverride = handler.tryCustomProcessFormat(tagId, formatCode, componentCount);
                        if (byteCountOverride == null) {
                            handler.error(String.format("Invalid TIFF tag format code %d for tag 0x%04X", Integer.valueOf(formatCode), Integer.valueOf(tagId)));
                            invalidTiffFormatCodeCount++;
                            if (invalidTiffFormatCodeCount > 5) {
                                handler.error("Stopping processing as too many errors seen in TIFF IFD");
                                handler.endingIFD();
                                if (resetByteOrder != null) {
                                    reader.setMotorolaByteOrder(resetByteOrder.booleanValue());
                                    return;
                                }
                                return;
                            }
                            tagNumber++;
                        } else {
                            byteCount = byteCountOverride.longValue();
                        }
                    } else {
                        byteCount = componentCount * ((long) format.getComponentSizeBytes());
                    }
                    if (byteCount > 4) {
                        long offsetVal = reader.getUInt32(tagOffset + 8);
                        if (offsetVal + byteCount > reader.getLength()) {
                            handler.error("Illegal TIFF tag pointer offset");
                            tagNumber++;
                        } else {
                            tagValueOffset = ((long) tiffHeaderOffset) + offsetVal;
                        }
                    } else {
                        tagValueOffset = (long) (tagOffset + 8);
                    }
                    if (tagValueOffset < 0 || tagValueOffset > reader.getLength()) {
                        handler.error("Illegal TIFF tag pointer offset");
                        tagNumber++;
                    } else {
                        if (byteCount >= 0) {
                            if (tagValueOffset + byteCount <= reader.getLength()) {
                                boolean isIfdPointer = false;
                                if (byteCount == 4 * componentCount) {
                                    for (int i = 0; ((long) i) < componentCount; i++) {
                                        if (handler.tryEnterSubIfd(tagId)) {
                                            isIfdPointer = true;
                                            processIfd(handler, reader, processedIfdOffsets, tiffHeaderOffset + reader.getInt32((int) (((long) (i * 4)) + tagValueOffset)), tiffHeaderOffset);
                                        }
                                    }
                                }
                                if (!isIfdPointer) {
                                    if (!handler.customProcessTag((int) tagValueOffset, processedIfdOffsets, tiffHeaderOffset, reader, tagId, (int) byteCount)) {
                                        processTag(handler, tagId, (int) tagValueOffset, (int) componentCount, formatCode, reader);
                                    }
                                }
                                tagNumber++;
                            }
                        }
                        handler.error("Illegal number of bytes for TIFF tag data: " + byteCount);
                        tagNumber++;
                    }
                }
                int nextIfdOffset = reader.getInt32(calculateTagOffset(ifdOffset, dirTagCount));
                if (nextIfdOffset != 0) {
                    int nextIfdOffset2 = nextIfdOffset + tiffHeaderOffset;
                    if (((long) nextIfdOffset2) >= reader.getLength()) {
                        handler.endingIFD();
                        if (resetByteOrder != null) {
                            reader.setMotorolaByteOrder(resetByteOrder.booleanValue());
                            return;
                        }
                        return;
                    } else if (nextIfdOffset2 < ifdOffset) {
                        handler.endingIFD();
                        if (resetByteOrder != null) {
                            reader.setMotorolaByteOrder(resetByteOrder.booleanValue());
                            return;
                        }
                        return;
                    } else if (handler.hasFollowerIfd()) {
                        processIfd(handler, reader, processedIfdOffsets, nextIfdOffset2, tiffHeaderOffset);
                    }
                }
                handler.endingIFD();
                if (resetByteOrder != null) {
                    reader.setMotorolaByteOrder(resetByteOrder.booleanValue());
                }
            }
        } finally {
            handler.endingIFD();
            if (resetByteOrder != null) {
                reader.setMotorolaByteOrder(resetByteOrder.booleanValue());
            }
        }
    }

    private static void processTag(@NotNull TiffHandler handler, int tagId, int tagValueOffset, int componentCount, int formatCode, @NotNull RandomAccessReader reader) throws IOException {
        switch (formatCode) {
            case 1:
                if (componentCount == 1) {
                    handler.setInt8u(tagId, reader.getUInt8(tagValueOffset));
                    return;
                }
                short[] array = new short[componentCount];
                for (int i = 0; i < componentCount; i++) {
                    array[i] = reader.getUInt8(tagValueOffset + i);
                }
                handler.setInt8uArray(tagId, array);
                return;
            case 2:
                handler.setString(tagId, reader.getNullTerminatedStringValue(tagValueOffset, componentCount, null));
                return;
            case 3:
                if (componentCount == 1) {
                    handler.setInt16u(tagId, reader.getUInt16(tagValueOffset));
                    return;
                }
                int[] array2 = new int[componentCount];
                for (int i2 = 0; i2 < componentCount; i2++) {
                    array2[i2] = reader.getUInt16((i2 * 2) + tagValueOffset);
                }
                handler.setInt16uArray(tagId, array2);
                return;
            case 4:
                if (componentCount == 1) {
                    handler.setInt32u(tagId, reader.getUInt32(tagValueOffset));
                    return;
                }
                long[] array3 = new long[componentCount];
                for (int i3 = 0; i3 < componentCount; i3++) {
                    array3[i3] = reader.getUInt32((i3 * 4) + tagValueOffset);
                }
                handler.setInt32uArray(tagId, array3);
                return;
            case 5:
                if (componentCount == 1) {
                    handler.setRational(tagId, new Rational(reader.getUInt32(tagValueOffset), reader.getUInt32(tagValueOffset + 4)));
                    return;
                } else if (componentCount > 1) {
                    Rational[] array4 = new Rational[componentCount];
                    for (int i4 = 0; i4 < componentCount; i4++) {
                        array4[i4] = new Rational(reader.getUInt32((i4 * 8) + tagValueOffset), reader.getUInt32(tagValueOffset + 4 + (i4 * 8)));
                    }
                    handler.setRationalArray(tagId, array4);
                    return;
                } else {
                    return;
                }
            case 6:
                if (componentCount == 1) {
                    handler.setInt8s(tagId, reader.getInt8(tagValueOffset));
                    return;
                }
                byte[] array5 = new byte[componentCount];
                for (int i5 = 0; i5 < componentCount; i5++) {
                    array5[i5] = reader.getInt8(tagValueOffset + i5);
                }
                handler.setInt8sArray(tagId, array5);
                return;
            case 7:
                handler.setByteArray(tagId, reader.getBytes(tagValueOffset, componentCount));
                return;
            case 8:
                if (componentCount == 1) {
                    handler.setInt16s(tagId, reader.getInt16(tagValueOffset));
                    return;
                }
                short[] array6 = new short[componentCount];
                for (int i6 = 0; i6 < componentCount; i6++) {
                    array6[i6] = reader.getInt16((i6 * 2) + tagValueOffset);
                }
                handler.setInt16sArray(tagId, array6);
                return;
            case 9:
                if (componentCount == 1) {
                    handler.setInt32s(tagId, reader.getInt32(tagValueOffset));
                    return;
                }
                int[] array7 = new int[componentCount];
                for (int i7 = 0; i7 < componentCount; i7++) {
                    array7[i7] = reader.getInt32((i7 * 4) + tagValueOffset);
                }
                handler.setInt32sArray(tagId, array7);
                return;
            case 10:
                if (componentCount == 1) {
                    handler.setRational(tagId, new Rational((long) reader.getInt32(tagValueOffset), (long) reader.getInt32(tagValueOffset + 4)));
                    return;
                } else if (componentCount > 1) {
                    Rational[] array8 = new Rational[componentCount];
                    for (int i8 = 0; i8 < componentCount; i8++) {
                        array8[i8] = new Rational((long) reader.getInt32((i8 * 8) + tagValueOffset), (long) reader.getInt32(tagValueOffset + 4 + (i8 * 8)));
                    }
                    handler.setRationalArray(tagId, array8);
                    return;
                } else {
                    return;
                }
            case 11:
                if (componentCount == 1) {
                    handler.setFloat(tagId, reader.getFloat32(tagValueOffset));
                    return;
                }
                float[] array9 = new float[componentCount];
                for (int i9 = 0; i9 < componentCount; i9++) {
                    array9[i9] = reader.getFloat32((i9 * 4) + tagValueOffset);
                }
                handler.setFloatArray(tagId, array9);
                return;
            case 12:
                if (componentCount == 1) {
                    handler.setDouble(tagId, reader.getDouble64(tagValueOffset));
                    return;
                }
                double[] array10 = new double[componentCount];
                for (int i10 = 0; i10 < componentCount; i10++) {
                    array10[i10] = reader.getDouble64((i10 * 4) + tagValueOffset);
                }
                handler.setDoubleArray(tagId, array10);
                return;
            default:
                handler.error(String.format("Invalid TIFF tag format code %d for tag 0x%04X", Integer.valueOf(formatCode), Integer.valueOf(tagId)));
                return;
        }
    }

    private static int calculateTagOffset(int ifdStartOffset, int entryNumber) {
        return ifdStartOffset + 2 + (entryNumber * 12);
    }
}
