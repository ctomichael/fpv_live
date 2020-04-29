package com.drew.imaging.jpeg;

import com.drew.lang.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.msgpack.core.MessagePack;

public enum JpegSegmentType {
    APP0(MessagePack.Code.NEGFIXINT_PREFIX, true),
    APP1((byte) -31, true),
    APP2((byte) -30, true),
    APP3((byte) -29, true),
    APP4((byte) -28, true),
    APP5((byte) -27, true),
    APP6((byte) -26, true),
    APP7((byte) -25, true),
    APP8((byte) -24, true),
    APP9((byte) -23, true),
    APPA((byte) -22, true),
    APPB((byte) -21, true),
    APPC((byte) -20, true),
    APPD((byte) -19, true),
    APPE((byte) -18, true),
    APPF((byte) -17, true),
    SOI(MessagePack.Code.FIXEXT16, false),
    DQT(MessagePack.Code.STR32, false),
    DNL(MessagePack.Code.ARRAY16, false),
    DRI(MessagePack.Code.ARRAY32, false),
    DHP(MessagePack.Code.MAP16, false),
    EXP(MessagePack.Code.MAP32, false),
    DHT(MessagePack.Code.BIN8, false),
    DAC(MessagePack.Code.UINT8, false),
    SOF0(MessagePack.Code.NIL, true),
    SOF1(MessagePack.Code.NEVER_USED, true),
    SOF2(MessagePack.Code.FALSE, true),
    SOF3(MessagePack.Code.TRUE, true),
    SOF5(MessagePack.Code.BIN16, true),
    SOF6(MessagePack.Code.BIN32, true),
    SOF7(MessagePack.Code.EXT8, true),
    JPG(MessagePack.Code.EXT16, true),
    SOF9(MessagePack.Code.EXT32, true),
    SOF10(MessagePack.Code.FLOAT32, true),
    SOF11(MessagePack.Code.FLOAT64, true),
    SOF13(MessagePack.Code.UINT16, true),
    SOF14(MessagePack.Code.UINT32, true),
    SOF15(MessagePack.Code.UINT64, true),
    COM((byte) -2, true);
    
    public static final Collection<JpegSegmentType> canContainMetadataTypes;
    public final byte byteValue;
    public final boolean canContainMetadata;

    static {
        List<JpegSegmentType> segmentTypes = new ArrayList<>();
        JpegSegmentType[] arr$ = (JpegSegmentType[]) JpegSegmentType.class.getEnumConstants();
        for (JpegSegmentType segmentType : arr$) {
            if (segmentType.canContainMetadata) {
                segmentTypes.add(segmentType);
            }
        }
        canContainMetadataTypes = segmentTypes;
    }

    private JpegSegmentType(byte byteValue2, boolean canContainMetadata2) {
        this.byteValue = byteValue2;
        this.canContainMetadata = canContainMetadata2;
    }

    @Nullable
    public static JpegSegmentType fromByte(byte segmentTypeByte) {
        JpegSegmentType[] arr$ = (JpegSegmentType[]) JpegSegmentType.class.getEnumConstants();
        for (JpegSegmentType segmentType : arr$) {
            if (segmentType.byteValue == segmentTypeByte) {
                return segmentType;
            }
        }
        return null;
    }
}
