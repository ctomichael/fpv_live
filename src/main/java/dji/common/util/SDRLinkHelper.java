package dji.common.util;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class SDRLinkHelper {
    public static final int ORIGINAL_NF_2DOT4G_END_INDEX = 1084;
    public static final int ORIGINAL_NF_2DOT4G_START_FREQ = 2400;
    public static final int ORIGINAL_NF_2DOT4G_START_INDEX = 1001;
    public static final int ORIGINAL_NF_5DOT8G_END_INDEX = 2664;
    public static final int ORIGINAL_NF_5DOT8G_START_FREQ = 5470;
    public static final int ORIGINAL_NF_5DOT8G_START_INDEX = 2539;
    public static final int RANGE_SIZE_10MHZ = 5;
    public static final int RANGE_SIZE_20MHZ = 10;

    public static int convertFrequencyFormFrequencyPointIndex(int index) {
        if (1001 <= index && index <= 1084) {
            return (index - 1001) + ORIGINAL_NF_2DOT4G_START_FREQ;
        }
        if (2539 > index || index > 2664) {
            return -1;
        }
        return (index - 2284) + ORIGINAL_NF_5DOT8G_START_FREQ;
    }

    public static int convertFrequencyPointFromFrequencyIndex(int freqIndex) {
        if (convertFrequencyFormFrequencyPointIndex(1001) < freqIndex && freqIndex <= convertFrequencyFormFrequencyPointIndex(1084)) {
            return (freqIndex - 2400) + 1001;
        }
        if (convertFrequencyFormFrequencyPointIndex(ORIGINAL_NF_5DOT8G_START_INDEX) >= freqIndex || freqIndex > convertFrequencyFormFrequencyPointIndex(ORIGINAL_NF_5DOT8G_END_INDEX)) {
            return 0;
        }
        return (freqIndex - 5470) + 2284;
    }

    public static boolean isFrequencyIndexIn2dot4G(int freqIndex) {
        int nfIndex = convertFrequencyPointFromFrequencyIndex(freqIndex);
        if (1001 > nfIndex || nfIndex > 1084) {
            return false;
        }
        return true;
    }

    public static boolean isFrequencyIndexIn5dot8G(int freqIndex) {
        int nfIndex = convertFrequencyPointFromFrequencyIndex(freqIndex);
        if (2539 > nfIndex || nfIndex > 2664) {
            return false;
        }
        return true;
    }
}
