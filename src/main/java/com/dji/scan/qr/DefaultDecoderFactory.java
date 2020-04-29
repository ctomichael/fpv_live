package com.dji.scan.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.qrcode.QRCodeReader;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

@EXClassNullAway
public class DefaultDecoderFactory implements DecoderFactory {
    private String characterSet;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> hints;
    private boolean inverted;

    public DefaultDecoderFactory() {
    }

    public DefaultDecoderFactory(Collection<BarcodeFormat> decodeFormats2, Map<DecodeHintType, ?> hints2, String characterSet2, boolean inverted2) {
        this.decodeFormats = decodeFormats2;
        this.hints = hints2;
        this.characterSet = characterSet2;
        this.inverted = inverted2;
    }

    public Decoder createDecoder(Map<DecodeHintType, ?> baseHints) {
        Map<DecodeHintType, Object> hints2 = new EnumMap<>(DecodeHintType.class);
        hints2.putAll(baseHints);
        if (this.hints != null) {
            hints2.putAll(this.hints);
        }
        if (this.decodeFormats != null) {
            hints2.put(DecodeHintType.POSSIBLE_FORMATS, this.decodeFormats);
        }
        if (this.characterSet != null) {
            hints2.put(DecodeHintType.CHARACTER_SET, this.characterSet);
        }
        QRCodeReader reader = new QRCodeReader();
        return this.inverted ? new InvertedDecoder(reader) : new Decoder(reader);
    }
}
