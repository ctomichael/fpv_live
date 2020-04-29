package com.google.zxing.pdf417.encoder;

import android.support.v4.app.FrameMetricsAggregator;
import com.adobe.xmp.XMPError;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusCameraSettingsMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusFocusInfoMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusImageProcessingMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusRawInfoMakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SanyoMakernoteDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.google.zxing.WriterException;
import com.google.zxing.pdf417.PDF417Common;
import it.sauronsoftware.ftp4j.FTPCodes;
import org.bouncycastle.crypto.tls.CipherSuite;

final class PDF417ErrorCorrection {
    private static final int[][] EC_COEFFICIENTS = {new int[]{27, 917}, new int[]{522, 568, 723, 809}, new int[]{237, 308, 436, 284, 646, 653, 428, 379}, new int[]{274, IptcDirectory.TAG_REFERENCE_NUMBER, 232, 755, 599, 524, LeicaMakernoteDirectory.TAG_COLOR_TEMPERATURE, 132, 295, 116, 442, 428, 295, 42, 176, 65}, new int[]{361, IptcDirectory.TAG_DIGITAL_TIME_CREATED, 922, OlympusMakernoteDirectory.TAG_ORIGINAL_MANUFACTURER_MODEL, 176, 586, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, 321, SanyoMakernoteDirectory.TAG_FLICKER_REDUCE, 742, 677, 742, 687, 284, CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256, 517, 273, 494, 263, 147, 593, LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE, 571, 320, LeicaMakernoteDirectory.TAG_WB_GREEN_LEVEL, 133, 231, 390, 685, ExifDirectoryBase.TAG_SUB_IFD_OFFSET, 63, 410}, new int[]{539, 422, 6, 93, 862, 771, 453, 106, 610, OlympusImageProcessingMakernoteDirectory.TagWbGLevel, 107, 505, 733, 877, 381, IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_CODE, 723, 476, 462, 172, 430, 609, 858, 822, SanyoMakernoteDirectory.TAG_SCENE_SELECT, IptcDirectory.TAG_ARM_IDENTIFIER, FrameMetricsAggregator.EVERY_DURATION, 400, 672, 762, 283, 184, 440, 35, 519, 31, 460, 594, FTPCodes.DATA_CONNECTION_OPEN, SanyoMakernoteDirectory.TAG_RECORD_SHUTTER_RELEASE, 517, 352, 605, 158, 651, XMPError.BADXML, 488, FTPCodes.COMMAND_NOT_IMPLEMENTED, 648, 733, 717, 83, 404, 97, 280, 771, 840, 629, 4, 381, 843, 623, 264, SanyoMakernoteDirectory.TAG_SCENE_SELECT}, new int[]{521, 310, 864, 547, 858, 580, 296, 379, 53, 779, 897, 444, 400, 925, 749, 415, 822, 93, 217, CanonMakernoteDirectory.TAG_VRD_OFFSET, PDF417Common.MAX_CODEWORDS_IN_BARCODE, 244, 583, 620, 246, 148, 447, 631, OlympusRawInfoMakernoteDirectory.TagWbRbLevelsEveningSunlight, 908, 490, 704, 516, 258, 457, 907, 594, 723, 674, OlympusRawInfoMakernoteDirectory.TagWbRbLevelsEveningSunlight, 272, 96, 684, 432, 686, 606, 860, 569, CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256, 219, 129, CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256, 236, OlympusImageProcessingMakernoteDirectory.TagWbGLevel, 192, OlympusCameraSettingsMakernoteDirectory.TagAfFineTuneAdj, 278, 173, 40, 379, IptcDirectory.TAG_OBJECT_PREVIEW_FILE_FORMAT, 463, 646, OlympusFocusInfoMakernoteDirectory.TagAfPoint, 171, 491, ExifDirectoryBase.TAG_PAGE_NUMBER, 763, 156, 732, 95, 270, 447, 90, 507, 48, 228, 821, OlympusFocusInfoMakernoteDirectory.TagAfInfo, 898, 784, IptcDirectory.TAG_AUDIO_SAMPLING_RATE, IptcDirectory.TAG_SOURCE, IptcDirectory.TAG_ARM_VERSION, 382, 262, 380, IptcDirectory.TAG_CITY, 754, IptcDirectory.TAG_TIME_SENT, 89, 614, 87, 432, 670, 616, 157, 374, 242, 726, 600, 269, 375, 898, 845, 454, 354, NikonType2MakernoteDirectory.TAG_ADAPTER, 814, 587, LeicaMakernoteDirectory.TAG_WB_BLUE_LEVEL, 34, 211, ExifDirectoryBase.TAG_SUB_IFD_OFFSET, 539, ExifDirectoryBase.TAG_PAGE_NUMBER, 827, 865, 37, 517, 834, 315, 550, 86, LeicaMakernoteDirectory.TAG_COLOR_TEMPERATURE, 4, 108, 539}, new int[]{524, 894, 75, 766, 882, 857, 74, XMPError.BADSTREAM, 82, 586, 708, 250, 905, LeicaMakernoteDirectory.TAG_MEASURED_LV, 138, 720, 858, CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256, 311, 913, 275, CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256, 375, 850, 438, 733, CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256, 280, XMPError.BADXML, 280, 828, 757, 710, 814, 919, 89, 68, 569, 11, XMPError.BADSTREAM, 796, 605, 540, 913, LeicaMakernoteDirectory.TAG_COLOR_TEMPERATURE, 700, 799, 137, 439, 418, IptcDirectory.TAG_BY_LINE, 668, 353, 859, 370, 694, ExifDirectoryBase.TAG_TILE_BYTE_COUNTS, 240, 216, 257, 284, 549, 209, 884, 315, 70, 329, 793, 490, 274, 877, 162, 749, 812, 684, 461, 334, IptcDirectory.TAG_ARM_IDENTIFIER, 849, 521, 307, 291, LeicaMakernoteDirectory.TAG_WB_GREEN_LEVEL, IptcDirectory.TAG_OBJECT_PREVIEW_FILE_FORMAT, 19, 358, 399, 908, 103, FrameMetricsAggregator.EVERY_DURATION, 51, 8, 517, FTPCodes.DATA_CONNECTION_OPEN, 289, 470, IptcDirectory.TAG_RASTERIZED_CAPTION, 731, 66, 255, 917, 269, 463, 830, 730, 433, 848, 585, 136, IptcDirectory.TAG_CONTENT_LOCATION_CODE, 906, 90, 2, OlympusRawInfoMakernoteDirectory.TagWbRbLevelsFineWeather, 743, 199, 655, 903, 329, 49, LeicaMakernoteDirectory.TAG_WB_RED_LEVEL, 580, 355, 588, 188, 462, 10, 134, IptcDirectory.TAG_COPYRIGHT_NOTICE, 320, 479, NikonType2MakernoteDirectory.TAG_ADAPTER, 739, 71, 263, ExifDirectoryBase.TAG_WHITE_POINT, 374, 601, 192, 605, 142, 673, 687, 234, 722, 384, 177, 752, IptcDirectory.TAG_PROVINCE_OR_STATE, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, 455, CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256, 689, 707, 805, 641, 48, 60, 732, 621, 895, 544, 261, 852, 655, 309, IptcDirectory.TAG_MASTER_DOCUMENT_ID, 755, 756, 60, 231, 773, 434, FTPCodes.SERVICE_NOT_AVAILABLE, 726, 528, FTPCodes.BAD_SEQUENCE_OF_COMMANDS, 118, 49, 795, 32, 144, 500, 238, 836, 394, 280, 566, ExifDirectoryBase.TAG_PRIMARY_CHROMATICITIES, 9, IptcDirectory.TAG_LANGUAGE_IDENTIFIER, 550, 73, 914, ExifDirectoryBase.TAG_TRANSFER_RANGE, 126, 32, 681, FTPCodes.USERNAME_OK, 792, 620, 60, 609, 441, 180, 791, 893, 754, 605, 383, 228, 749, 760, FTPCodes.FILE_STATUS, 54, ExifDirectoryBase.TAG_PAGE_NUMBER, 134, 54, 834, 299, 922, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256, 910, 532, 609, 829, 189, 20, 167, 29, 872, 449, 83, 402, 41, 656, 505, 579, 481, 173, 404, 251, 688, 95, 497, 555, IptcDirectory.TAG_IMAGE_TYPE, SanyoMakernoteDirectory.TAG_SCENE_SELECT, 307, 159, 924, 558, 648, 55, 497, 10}, new int[]{352, 77, 373, FTPCodes.COMMAND_PARAMETER_NOT_IMPLEMENTED, 35, 599, 428, 207, 409, IptcDirectory.TAG_DIGITAL_DATE_CREATED, 118, 498, 285, 380, FTPCodes.PENDING_FURTHER_INFORMATION, 492, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256, 265, 920, 155, 914, 299, 229, IptcDirectory.TAG_IMAGE_ORIENTATION, 294, 871, 306, 88, 87, CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256, 352, 781, 846, 75, 327, 520, 435, SanyoMakernoteDirectory.TAG_SCENE_SELECT, XMPError.BADXMP, IptcDirectory.TAG_AUDIO_OUTCUE, 249, IptcDirectory.TAG_CODED_CHARACTER_SET, 781, 621, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, 268, 794, 534, 539, 781, 408, 390, 644, 102, 476, 499, OlympusRawInfoMakernoteDirectory.TagWbRbLevelsFineWeather, IptcDirectory.TAG_CAPTION, 545, 37, 858, 916, 552, 41, 542, 289, 122, 272, 383, LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE, 485, 98, 752, 472, 761, 107, 784, 860, 658, 741, OlympusRawInfoMakernoteDirectory.TagWbRbLevelsFineWeather, XMPError.BADSTREAM, 681, 407, 855, 85, 99, 62, 482, 180, 20, ExifDirectoryBase.TAG_PAGE_NUMBER, FTPCodes.LOCAL_ERROR_IN_PROCESSING, 593, 913, 142, OlympusFocusInfoMakernoteDirectory.TagAfInfo, 684, OlympusImageProcessingMakernoteDirectory.TagWbGLevel, SanyoMakernoteDirectory.TAG_FLICKER_REDUCE, 561, 76, 653, 899, 729, IptcDirectory.TAG_DATE_CREATED, 744, 390, 513, 192, 516, 258, 240, 518, 794, 395, 768, 848, 51, 610, 384, 168, CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256, 826, 328, 596, LeicaMakernoteDirectory.TAG_MEASURED_LV, 303, 570, 381, 415, 641, 156, 237, 151, 429, 531, 207, 676, 710, 89, 168, OlympusRawInfoMakernoteDirectory.TagWbRbLevelsDaylightFluor, 402, 40, 708, IptcDirectory.TAG_DIGITAL_TIME_CREATED, 162, 864, 229, 65, 861, 841, 512, 164, 477, FTPCodes.SERVICE_CLOSING_CONTROL_CONNECTION, 92, 358, 785, 288, 357, 850, 836, 827, 736, 707, 94, 8, 494, 114, 521, 2, 499, 851, SanyoMakernoteDirectory.TAG_SCENE_SELECT, 152, 729, 771, 95, 248, 361, 578, ExifDirectoryBase.TAG_TILE_LENGTH, 856, 797, 289, 51, 684, 466, 533, 820, 669, 45, 902, FTPCodes.FILE_UNAVAILABLE, 167, ExifDirectoryBase.TAG_TRANSFER_RANGE, 244, 173, 35, 463, 651, 51, IptcDirectory.TAG_UNIQUE_DOCUMENT_ID, 591, FTPCodes.FILE_UNAVAILABLE, 578, 37, PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH, 298, FTPCodes.NEED_ACCOUNT, 552, 43, 427, 119, IptcDirectory.TAG_AUDIO_TYPE, 777, 475, 850, 764, 364, 578, 911, 283, 711, 472, 420, 245, 288, 594, 394, FrameMetricsAggregator.EVERY_DURATION, 327, 589, 777, IptcDirectory.TAG_UNIQUE_DOCUMENT_ID, 688, 43, 408, 842, 383, 721, 521, 560, 644, IptcDirectory.TAG_OBJECT_PREVIEW_DATA, 559, 62, 145, 873, IptcDirectory.TAG_AUDIO_SAMPLING_RATE, IptcDirectory.TAG_OBJECT_PREVIEW_FILE_FORMAT_VERSION, 159, 672, 729, 624, 59, CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256, 417, 158, 209, 563, 564, 343, 693, 109, 608, 563, 365, 181, 772, 677, 310, 248, 353, 708, 410, 579, 870, IptcDirectory.TAG_HEADLINE, 841, IptcDirectory.TAG_CAPTION, 860, 289, SanyoMakernoteDirectory.TAG_FLICKER_REDUCE, 35, 777, 618, 586, 424, 833, 77, IptcDirectory.TAG_BY_LINE_TITLE, IptcDirectory.TAG_CODED_CHARACTER_SET, 269, 757, IptcDirectory.TAG_CAPTION, 695, 751, FTPCodes.USERNAME_OK, 247, 184, 45, LeicaMakernoteDirectory.TAG_APPROXIMATE_F_NUMBER, 680, 18, 66, 407, 369, 54, 492, 228, IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME, 830, 922, 437, 519, 644, 905, 789, 420, 305, 441, 207, 300, 892, 827, 141, 537, 381, IptcDirectory.TAG_AUDIO_TYPE, 513, 56, 252, 341, 242, 797, 838, 837, 720, 224, 307, 631, 61, 87, 560, 310, 756, IptcDirectory.TAG_AUDIO_DURATION, 397, OlympusFocusInfoMakernoteDirectory.TagAfInfo, 851, 309, 473, 795, IptcDirectory.TAG_ARM_VERSION, 31, IptcDirectory.TAG_LANGUAGE_IDENTIFIER, 915, 459, 806, 590, 731, FTPCodes.CANNOT_OPEN_DATA_CONNECTION, 216, SanyoMakernoteDirectory.TAG_SEQUENCE_SHOT_INTERVAL, 249, 321, 881, IptcDirectory.TAG_UNIQUE_DOCUMENT_ID, SanyoMakernoteDirectory.TAG_RECORD_SHUTTER_RELEASE, 673, 782, 210, 815, 905, 303, 843, 922, 281, 73, 469, 791, 660, 162, 498, 308, 155, 422, 907, LeicaMakernoteDirectory.TAG_CCD_BOARD_VERSION, 187, 62, 16, FTPCodes.CANNOT_OPEN_DATA_CONNECTION, SanyoMakernoteDirectory.TAG_RECORD_SHUTTER_RELEASE, IptcDirectory.TAG_TIME_SENT, 286, 437, 375, 273, 610, 296, 183, 923, 116, 667, 751, 353, 62, 366, 691, 379, 687, 842, 37, 357, 720, 742, ExifDirectoryBase.TAG_SUB_IFD_OFFSET, 5, 39, 923, 311, 424, 242, 749, 321, 54, 669, 316, ExifDirectoryBase.TAG_TRANSFER_RANGE, 299, 534, 105, 667, 488, OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE, 672, 576, 540, 316, 486, 721, 610, 46, 656, 447, 171, 616, 464, CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256, 531, ExifDirectoryBase.TAG_PAGE_NUMBER, 321, 762, 752, 533, 175, 134, 14, 381, 433, 717, 45, 111, 20, 596, 284, 736, 138, 646, 411, 877, 669, 141, 919, 45, 780, 407, 164, FTPCodes.NEED_ACCOUNT, 899, 165, 726, 600, ExifDirectoryBase.TAG_TILE_BYTE_COUNTS, 498, 655, 357, 752, 768, 223, 849, IptcDirectory.TAG_LANGUAGE_IDENTIFIER, 63, 310, 863, 251, 366, OlympusRawInfoMakernoteDirectory.TagWbRbLevelsDaylightFluor, 282, 738, 675, 410, 389, 244, 31, PanasonicMakernoteDirectory.TAG_INTELLIGENT_D_RANGE, 303, 263}};

    private PDF417ErrorCorrection() {
    }

    static int getErrorCorrectionCodewordCount(int errorCorrectionLevel) {
        if (errorCorrectionLevel >= 0 && errorCorrectionLevel <= 8) {
            return 1 << (errorCorrectionLevel + 1);
        }
        throw new IllegalArgumentException("Error correction level must be between 0 and 8!");
    }

    static int getRecommendedMinimumErrorCorrectionLevel(int n) throws WriterException {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        } else if (n <= 40) {
            return 2;
        } else {
            if (n <= 160) {
                return 3;
            }
            if (n <= 320) {
                return 4;
            }
            if (n <= 863) {
                return 5;
            }
            throw new WriterException("No recommendation possible");
        }
    }

    static String generateErrorCorrection(CharSequence dataCodewords, int errorCorrectionLevel) {
        int k = getErrorCorrectionCodewordCount(errorCorrectionLevel);
        char[] e = new char[k];
        int sld = dataCodewords.length();
        for (int i = 0; i < sld; i++) {
            int t1 = (dataCodewords.charAt(i) + e[k - 1]) % PDF417Common.NUMBER_OF_CODEWORDS;
            for (int j = k - 1; j > 0; j--) {
                e[j] = (char) ((e[j - 1] + (929 - ((EC_COEFFICIENTS[errorCorrectionLevel][j] * t1) % PDF417Common.NUMBER_OF_CODEWORDS))) % PDF417Common.NUMBER_OF_CODEWORDS);
            }
            e[0] = (char) ((929 - ((EC_COEFFICIENTS[errorCorrectionLevel][0] * t1) % PDF417Common.NUMBER_OF_CODEWORDS)) % PDF417Common.NUMBER_OF_CODEWORDS);
        }
        StringBuilder sb = new StringBuilder(k);
        for (int j2 = k - 1; j2 >= 0; j2--) {
            if (e[j2] != 0) {
                e[j2] = (char) (929 - e[j2]);
            }
            sb.append(e[j2]);
        }
        return sb.toString();
    }
}
