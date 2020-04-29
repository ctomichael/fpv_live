package com.google.zxing.datamatrix.encoder;

final class X12Encoder extends C40Encoder {
    X12Encoder() {
    }

    public int getEncodingMode() {
        return 3;
    }

    public void encode(EncoderContext context) {
        StringBuilder buffer = new StringBuilder();
        while (true) {
            if (!context.hasMoreCharacters()) {
                break;
            }
            char c = context.getCurrentChar();
            context.pos++;
            encodeChar(c, buffer);
            if (buffer.length() % 3 == 0) {
                writeNextTriplet(context, buffer);
                if (HighLevelEncoder.lookAheadTest(context.getMessage(), context.pos, getEncodingMode()) != getEncodingMode()) {
                    context.signalEncoderChange(0);
                    break;
                }
            }
        }
        handleEOD(context, buffer);
    }

    /* access modifiers changed from: package-private */
    public int encodeChar(char c, StringBuilder sb) {
        switch (c) {
            case 13:
                sb.append(0);
                break;
            case ' ':
                sb.append(3);
                break;
            case '*':
                sb.append(1);
                break;
            case '>':
                sb.append(2);
                break;
            default:
                if (c < '0' || c > '9') {
                    if (c >= 'A' && c <= 'Z') {
                        sb.append((char) ((c - 'A') + 14));
                        break;
                    } else {
                        HighLevelEncoder.illegalCharacter(c);
                        break;
                    }
                } else {
                    sb.append((char) ((c - '0') + 4));
                    break;
                }
                break;
        }
        return 1;
    }

    /* access modifiers changed from: package-private */
    public void handleEOD(EncoderContext context, StringBuilder buffer) {
        context.updateSymbolInfo();
        int available = context.getSymbolInfo().getDataCapacity() - context.getCodewordCount();
        context.pos -= buffer.length();
        if (context.getRemainingCharacters() > 1 || available > 1 || context.getRemainingCharacters() != available) {
            context.writeCodeword(254);
        }
        if (context.getNewEncoding() < 0) {
            context.signalEncoderChange(0);
        }
    }
}
