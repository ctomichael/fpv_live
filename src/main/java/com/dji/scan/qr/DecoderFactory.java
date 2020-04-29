package com.dji.scan.qr;

import com.google.zxing.DecodeHintType;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.Map;

@EXClassNullAway
public interface DecoderFactory {
    Decoder createDecoder(Map<DecodeHintType, ?> map);
}
