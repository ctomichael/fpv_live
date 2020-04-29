package dji.common.util;

import dji.common.airlink.WiFiFrequencyBand;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@EXClassNullAway
public class AirLinkUtils {
    private static final List<Integer> CHANNEL_2_4G_LIST = Arrays.asList(CHANNEL_2_4G_VALUES);
    private static final Integer[] CHANNEL_2_4G_VALUES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
    private static final List<Integer> CHANNEL_5G_LIST = Arrays.asList(CHANNEL_5G_VALUES);
    private static final Integer[] CHANNEL_5G_VALUES = {149, 153, 157, 161, 165};
    private static final List<Integer> CHANNEL_DUAL_LIST = Arrays.asList(CHANNEL_DUAL_VALUES);
    private static final Integer[] CHANNEL_DUAL_VALUES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 149, 153, 157, 161, 165};
    private static final float ORIGINAL_NF_START_INDEX = 2400.5f;

    public static boolean verifySSID(String str) {
        if (str == null || str.length() > 30 || !Pattern.compile("[A-Za-z0-9-_]{1,32}").matcher(str).matches()) {
            return true;
        }
        return false;
    }

    public static int transformRadioSignal(int value) {
        if (value == 0) {
            return 0;
        }
        int percent = 101 - ((int) Math.sqrt(Math.pow(10.0d, (double) (((float) (Math.abs(value) - 53)) / 10.0f))));
        if (percent > 100) {
            return 100;
        }
        if (percent < 5) {
            return 5;
        }
        return percent;
    }

    public static float convertSDRFrequencyFromFrequencyPointIndex(int index) {
        if (index < 1001 || index > 1084) {
            return -1.0f;
        }
        return ORIGINAL_NF_START_INDEX + ((float) (index - 1001));
    }

    public static List<Integer> getValidChannelsForFrequencyBand(WiFiFrequencyBand band) {
        switch (band) {
            case FREQUENCY_BAND_2_DOT_4_GHZ:
                return CHANNEL_2_4G_LIST;
            case FREQUENCY_BAND_5_GHZ:
                return CHANNEL_5G_LIST;
            case FREQUENCY_BAND_DUAL:
                return CHANNEL_DUAL_LIST;
            default:
                return null;
        }
    }

    public static WiFiFrequencyBand getValidFrequencyBandForChannel(int channel) {
        if (CHANNEL_2_4G_LIST.indexOf(Integer.valueOf(channel)) >= 0) {
            return WiFiFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ;
        }
        if (CHANNEL_5G_LIST.indexOf(Integer.valueOf(channel)) >= 0) {
            return WiFiFrequencyBand.FREQUENCY_BAND_5_GHZ;
        }
        return WiFiFrequencyBand.UNKNOWN;
    }

    public static int convertOcuSyncSignalQuality(int input) {
        float output;
        if (input <= 6) {
            output = 0.0f;
        } else if (input <= 16) {
            output = (float) (((input - 7) * 2) + 1);
        } else if (input <= 30) {
            output = (1.4615384f * ((float) (input - 17))) + 21.0f;
        } else if (input <= 55) {
            output = (0.7916667f * ((float) (input - 31))) + 41.0f;
        } else if (input <= 70) {
            output = (1.3571428f * ((float) (input - 56))) + 61.0f;
        } else if (input <= 90) {
            output = (float) (input + 10);
        } else {
            output = 100.0f;
        }
        return (int) output;
    }
}
