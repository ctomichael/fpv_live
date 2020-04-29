package com.google.zxing.datamatrix.decoder;

import com.google.zxing.datamatrix.decoder.Version;

final class DataBlock {
    private final byte[] codewords;
    private final int numDataCodewords;

    private DataBlock(int numDataCodewords2, byte[] codewords2) {
        this.numDataCodewords = numDataCodewords2;
        this.codewords = codewords2;
    }

    static DataBlock[] getDataBlocks(byte[] rawCodewords, Version version) {
        int numLongerBlocks;
        int jOffset;
        int iOffset;
        Version.ECBlocks ecBlocks = version.getECBlocks();
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
                result[numResultBlocks] = new DataBlock(numDataCodewords2, new byte[(ecBlocks.getECCodewords() + numDataCodewords2)]);
                i3++;
                numResultBlocks++;
            }
        }
        int longerBlocksNumDataCodewords = result[0].codewords.length - ecBlocks.getECCodewords();
        int shorterBlocksNumDataCodewords = longerBlocksNumDataCodewords - 1;
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
        boolean specialVersion = version.getVersionNumber() == 24;
        if (specialVersion) {
            numLongerBlocks = 8;
        } else {
            numLongerBlocks = numResultBlocks;
        }
        int j2 = 0;
        int rawCodewordsOffset3 = rawCodewordsOffset;
        while (j2 < numLongerBlocks) {
            result[j2].codewords[longerBlocksNumDataCodewords - 1] = rawCodewords[rawCodewordsOffset3];
            j2++;
            rawCodewordsOffset3++;
        }
        int max = result[0].codewords.length;
        int i5 = longerBlocksNumDataCodewords;
        int rawCodewordsOffset4 = rawCodewordsOffset3;
        while (i5 < max) {
            int j3 = 0;
            int rawCodewordsOffset5 = rawCodewordsOffset4;
            while (j3 < numResultBlocks) {
                if (specialVersion) {
                    jOffset = (j3 + 8) % numResultBlocks;
                } else {
                    jOffset = j3;
                }
                if (!specialVersion || jOffset <= 7) {
                    iOffset = i5;
                } else {
                    iOffset = i5 - 1;
                }
                result[jOffset].codewords[iOffset] = rawCodewords[rawCodewordsOffset5];
                j3++;
                rawCodewordsOffset5++;
            }
            i5++;
            rawCodewordsOffset4 = rawCodewordsOffset5;
        }
        if (rawCodewordsOffset4 == rawCodewords.length) {
            return result;
        }
        throw new IllegalArgumentException();
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
