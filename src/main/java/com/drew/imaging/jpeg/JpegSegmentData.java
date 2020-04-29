package com.drew.imaging.jpeg;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JpegSegmentData {
    @NotNull
    private final HashMap<Byte, List<byte[]>> _segmentDataMap = new HashMap<>(10);

    public void addSegment(byte segmentType, @NotNull byte[] segmentBytes) {
        getOrCreateSegmentList(segmentType).add(segmentBytes);
    }

    public Iterable<JpegSegmentType> getSegmentTypes() {
        Set<JpegSegmentType> segmentTypes = new HashSet<>();
        for (Byte segmentTypeByte : this._segmentDataMap.keySet()) {
            JpegSegmentType segmentType = JpegSegmentType.fromByte(segmentTypeByte.byteValue());
            if (segmentType == null) {
                throw new IllegalStateException("Should not have a segmentTypeByte that is not in the enum: " + Integer.toHexString(segmentTypeByte.byteValue()));
            }
            segmentTypes.add(segmentType);
        }
        return segmentTypes;
    }

    @Nullable
    public byte[] getSegment(byte segmentType) {
        return getSegment(segmentType, 0);
    }

    @Nullable
    public byte[] getSegment(@NotNull JpegSegmentType segmentType) {
        return getSegment(segmentType.byteValue, 0);
    }

    @Nullable
    public byte[] getSegment(@NotNull JpegSegmentType segmentType, int occurrence) {
        return getSegment(segmentType.byteValue, occurrence);
    }

    @Nullable
    public byte[] getSegment(byte segmentType, int occurrence) {
        List<byte[]> segmentList = getSegmentList(segmentType);
        if (segmentList == null || segmentList.size() <= occurrence) {
            return null;
        }
        return segmentList.get(occurrence);
    }

    @NotNull
    public Iterable<byte[]> getSegments(@NotNull JpegSegmentType segmentType) {
        return getSegments(segmentType.byteValue);
    }

    @NotNull
    public Iterable<byte[]> getSegments(byte segmentType) {
        List<byte[]> segmentList = getSegmentList(segmentType);
        return segmentList == null ? new ArrayList<>() : segmentList;
    }

    @Nullable
    private List<byte[]> getSegmentList(byte segmentType) {
        return this._segmentDataMap.get(Byte.valueOf(segmentType));
    }

    @NotNull
    private List<byte[]> getOrCreateSegmentList(byte segmentType) {
        if (this._segmentDataMap.containsKey(Byte.valueOf(segmentType))) {
            return this._segmentDataMap.get(Byte.valueOf(segmentType));
        }
        List<byte[]> segmentList = new ArrayList<>();
        this._segmentDataMap.put(Byte.valueOf(segmentType), segmentList);
        return segmentList;
    }

    public int getSegmentCount(@NotNull JpegSegmentType segmentType) {
        return getSegmentCount(segmentType.byteValue);
    }

    public int getSegmentCount(byte segmentType) {
        List<byte[]> segmentList = getSegmentList(segmentType);
        if (segmentList == null) {
            return 0;
        }
        return segmentList.size();
    }

    public void removeSegmentOccurrence(@NotNull JpegSegmentType segmentType, int occurrence) {
        removeSegmentOccurrence(segmentType.byteValue, occurrence);
    }

    public void removeSegmentOccurrence(byte segmentType, int occurrence) {
        this._segmentDataMap.get(Byte.valueOf(segmentType)).remove(occurrence);
    }

    public void removeSegment(@NotNull JpegSegmentType segmentType) {
        removeSegment(segmentType.byteValue);
    }

    public void removeSegment(byte segmentType) {
        this._segmentDataMap.remove(Byte.valueOf(segmentType));
    }

    public boolean containsSegment(@NotNull JpegSegmentType segmentType) {
        return containsSegment(segmentType.byteValue);
    }

    public boolean containsSegment(byte segmentType) {
        return this._segmentDataMap.containsKey(Byte.valueOf(segmentType));
    }
}
