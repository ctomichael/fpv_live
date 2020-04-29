package com.google.zxing.qrcode.decoder;

import com.google.zxing.qrcode.decoder.Version;

final class DataBlock {
    private final byte[] codewords;
    private final int numDataCodewords;

    private DataBlock(int numDataCodewords2, byte[] codewords2) {
        this.numDataCodewords = numDataCodewords2;
        this.codewords = codewords2;
    }

    static DataBlock[] getDataBlocks(byte[] rawCodewords, Version version, ErrorCorrectionLevel ecLevel) {
        if (rawCodewords.length != version.getTotalCodewords()) {
            throw new IllegalArgumentException();
        }
        Version.ECBlocks ecBlocks = version.getECBlocksForLevel(ecLevel);
        int totalBlocks = 0;
        Version.ECB[] ecBlockArray = ecBlocks.getECBlocks();
        int length = ecBlockArray.length;
        for (int i = 0; i < length; i++) {
            totalBlocks += ecBlockArray[i].getCount();
        }
        DataBlock[] result = new DataBlock[totalBlocks];
        int numResultBlocks = 0;
        int length2 = ecBlockArray.length;
        for (int i2 = 0; i2 < length2; i2++) {
            Version.ECB ecBlock = ecBlockArray[i2];
            int i3 = 0;
            while (i3 < ecBlock.getCount()) {
                int numDataCodewords2 = ecBlock.getDataCodewords();
                result[numResultBlocks] = new DataBlock(numDataCodewords2, new byte[(ecBlocks.getECCodewordsPerBlock() + numDataCodewords2)]);
                i3++;
                numResultBlocks++;
            }
        }
        int shorterBlocksTotalCodewords = result[0].codewords.length;
        int longerBlocksStartAt = result.length - 1;
        while (longerBlocksStartAt >= 0 && result[longerBlocksStartAt].codewords.length != shorterBlocksTotalCodewords) {
            longerBlocksStartAt--;
        }
        int longerBlocksStartAt2 = longerBlocksStartAt + 1;
        int shorterBlocksNumDataCodewords = shorterBlocksTotalCodewords - ecBlocks.getECCodewordsPerBlock();
        int rawCodewordsOffset = 0;
        int i4 = 0;
        while (i4 < shorterBlocksNumDataCodewords) {
            int j = 0;
            int rawCodewordsOffset2 = rawCodewordsOffset;
            while (j < numResultBlocks) {
                result[j].codewords[i4] = rawCodewords[rawCodewordsOffset2];
                j++;
                rawCodewordsOffset2++;
            }
            i4++;
            rawCodewordsOffset = rawCodewordsOffset2;
        }
        int j2 = longerBlocksStartAt2;
        int rawCodewordsOffset3 = rawCodewordsOffset;
        while (j2 < numResultBlocks) {
            result[j2].codewords[shorterBlocksNumDataCodewords] = rawCodewords[rawCodewordsOffset3];
            j2++;
            rawCodewordsOffset3++;
        }
        int max = result[0].codewords.length;
        int i5 = shorterBlocksNumDataCodewords;
        int rawCodewordsOffset4 = rawCodewordsOffset3;
        while (i5 < max) {
            int j3 = 0;
            int rawCodewordsOffset5 = rawCodewordsOffset4;
            while (j3 < numResultBlocks) {
                result[j3].codewords[j3 < longerBlocksStartAt2 ? i5 : i5 + 1] = rawCodewords[rawCodewordsOffset5];
                j3++;
                rawCodewordsOffset5++;
            }
            i5++;
            rawCodewordsOffset4 = rawCodewordsOffset5;
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public int getNumDataCodewords() {
        return this.numDataCodewords;
    }

    /* access modifiers changed from: package-private */
    public byte[] getCodewords() {
        return this.codewords;
    }
}
