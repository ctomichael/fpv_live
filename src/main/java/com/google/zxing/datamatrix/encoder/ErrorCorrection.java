package com.google.zxing.datamatrix.encoder;

import com.adobe.xmp.XMPError;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import dji.pilot.publics.model.ICameraResMode;
import it.sauronsoftware.ftp4j.FTPCodes;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.crypto.tls.CipherSuite;

public final class ErrorCorrection {
    private static final int[] ALOG = new int[255];
    private static final int[][] FACTORS = {new int[]{228, 48, 15, 111, 62}, new int[]{23, 68, 144, 134, 240, 92, 254}, new int[]{28, 24, 185, 166, 223, 248, 116, 255, 110, 61}, new int[]{175, 138, 205, 12, CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256, 168, 39, 245, 60, 97, 120}, new int[]{41, 153, 158, 91, 61, 42, 142, FTPCodes.FILE_STATUS, 97, 178, 100, 242}, new int[]{156, 97, 192, 252, 95, 9, 157, 119, 138, 45, 18, CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256, 83, 185}, new int[]{83, CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256, 100, 39, 188, 75, 66, 61, 241, FTPCodes.FILE_STATUS, 109, 129, 94, 254, FTPCodes.DATA_CONNECTION_OPEN, 48, 90, 188}, new int[]{15, CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256, 244, 9, 233, 71, 168, 2, 188, 160, 153, 145, ICameraResMode.ICameraVideoResolutionRes.VR_MAX, 79, 108, 82, 27, 174, CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256, 172}, new int[]{52, CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256, 88, 205, 109, 39, 176, 21, 155, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256, 251, 223, 155, 21, 5, 172, 254, PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH, 12, 181, 184, 96, 50, CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256}, new int[]{211, 231, 43, 97, 71, 96, 103, 174, 37, 151, 170, 53, 75, 34, 249, PanasonicMakernoteDirectory.TAG_INTELLIGENT_D_RANGE, 17, 138, 110, FTPCodes.FILE_STATUS, 141, 136, 120, 151, 233, 168, 93, 255}, new int[]{245, CertificateBody.profileType, 242, 218, NikonType2MakernoteDirectory.TAG_ADAPTER, 250, 162, 181, 102, 120, 84, 179, FTPCodes.SERVICE_READY_FOR_NEW_USER, 251, 80, 182, 229, 18, 2, 4, 68, 33, 101, 137, 95, 119, 115, 44, 175, 184, 59, 25, FTPCodes.DATA_CONNECTION_OPEN, 98, 81, 112}, new int[]{77, CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256, 137, 31, 19, 38, 22, 153, 247, 105, 122, 2, 245, 133, 242, 8, 175, 95, 100, 9, 167, 105, FTPCodes.HELP_MESSAGE, 111, 57, PanasonicMakernoteDirectory.TAG_INTELLIGENT_D_RANGE, 21, 1, ICameraResMode.ICameraVideoResolutionRes.VR_MAX, 57, 54, 101, 248, 202, 69, 50, 150, 177, FTPCodes.DATA_CONNECTION_CLOSING, 5, 9, 5}, new int[]{245, 132, 172, 223, 96, 32, 117, 22, 238, 133, 238, 231, 205, 188, 237, 87, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256, 106, 16, 147, 118, 23, 37, 90, 170, 205, 131, 88, 120, 100, 66, 138, CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256, 240, 82, 44, 176, 87, 187, 147, 160, 175, 69, FTPCodes.FILE_STATUS, 92, ICameraResMode.ICameraVideoResolutionRes.VR_MAX, FTPCodes.DATA_CONNECTION_OPEN, 19}, new int[]{175, 9, 223, 238, 12, 17, FTPCodes.SERVICE_READY_FOR_NEW_USER, CanonMakernoteDirectory.TAG_VRD_OFFSET, 100, 29, 175, 170, FTPCodes.USER_LOGGED_IN, 192, FTPCodes.NAME_SYSTEM_TIME, 235, 150, 159, 36, 223, 38, 200, 132, 54, 228, 146, 218, 234, 117, XMPError.BADXMP, 29, 232, 144, 238, 22, 150, XMPError.BADXML, 117, 62, 207, 164, 13, 137, 245, CertificateBody.profileType, 67, 247, 28, 155, 43, XMPError.BADXMP, 107, 233, 53, 143, 46}, new int[]{242, 93, 169, 50, 144, 210, 39, 118, 202, 188, XMPError.BADXML, 189, 143, 108, CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256, 37, 185, 112, 134, FTPCodes.USER_LOGGED_IN, 245, 63, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256, CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256, 250, 106, 185, FTPCodes.SERVICE_CLOSING_CONTROL_CONNECTION, 175, 64, 114, 71, 161, 44, 147, 6, 27, 218, 51, 63, 87, 10, 40, NikonType2MakernoteDirectory.TAG_ADAPTER, 188, 17, 163, 31, 176, 170, 4, 107, 232, 7, 94, 166, 224, PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH, 86, 47, 11, XMPError.BADSTREAM}, new int[]{FTPCodes.SERVICE_READY_FOR_NEW_USER, 228, 173, 89, 251, 149, 159, 56, 89, 33, 147, 244, 154, 36, 73, CertificateBody.profileType, FTPCodes.FILE_STATUS, 136, 248, 180, 234, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256, 158, 177, 68, 122, 93, FTPCodes.FILE_STATUS, 15, 160, FTPCodes.ENTER_PASSIVE_MODE, 236, 66, 139, 153, 185, 202, 167, 179, 25, FTPCodes.SERVICE_READY_FOR_NEW_USER, 232, 96, 210, 231, 136, 223, 239, 181, 241, 59, 52, 172, 25, 49, 232, 211, 189, 64, 54, 108, 153, 132, 63, 96, 103, 82, CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256}};
    private static final int[] FACTOR_SETS = {5, 7, 10, 11, 12, 14, 18, 20, 24, 28, 36, 42, 48, 56, 62, 68};
    private static final int[] LOG = new int[256];
    private static final int MODULO_VALUE = 301;

    static {
        int p = 1;
        for (int i = 0; i < 255; i++) {
            ALOG[i] = p;
            LOG[p] = i;
            p <<= 1;
            if (p >= 256) {
                p ^= 301;
            }
        }
    }

    private ErrorCorrection() {
    }

    public static String encodeECC200(String codewords, SymbolInfo symbolInfo) {
        if (codewords.length() != symbolInfo.getDataCapacity()) {
            throw new IllegalArgumentException("The number of codewords does not match the selected symbol");
        }
        StringBuilder sb = new StringBuilder(symbolInfo.getDataCapacity() + symbolInfo.getErrorCodewords());
        sb.append(codewords);
        int blockCount = symbolInfo.getInterleavedBlockCount();
        if (blockCount == 1) {
            sb.append(createECCBlock(codewords, symbolInfo.getErrorCodewords()));
        } else {
            sb.setLength(sb.capacity());
            int[] dataSizes = new int[blockCount];
            int[] errorSizes = new int[blockCount];
            int[] startPos = new int[blockCount];
            for (int i = 0; i < blockCount; i++) {
                dataSizes[i] = symbolInfo.getDataLengthForInterleavedBlock(i + 1);
                errorSizes[i] = symbolInfo.getErrorLengthForInterleavedBlock(i + 1);
                startPos[i] = 0;
                if (i > 0) {
                    startPos[i] = startPos[i - 1] + dataSizes[i];
                }
            }
            for (int block = 0; block < blockCount; block++) {
                StringBuilder temp = new StringBuilder(dataSizes[block]);
                for (int d = block; d < symbolInfo.getDataCapacity(); d += blockCount) {
                    temp.append(codewords.charAt(d));
                }
                String ecc = createECCBlock(temp.toString(), errorSizes[block]);
                int pos = 0;
                int e = block;
                while (e < errorSizes[block] * blockCount) {
                    sb.setCharAt(symbolInfo.getDataCapacity() + e, ecc.charAt(pos));
                    e += blockCount;
                    pos++;
                }
            }
        }
        return sb.toString();
    }

    private static String createECCBlock(CharSequence codewords, int numECWords) {
        return createECCBlock(codewords, 0, codewords.length(), numECWords);
    }

    private static String createECCBlock(CharSequence codewords, int start, int len, int numECWords) {
        int table = -1;
        int i = 0;
        while (true) {
            if (i >= FACTOR_SETS.length) {
                break;
            } else if (FACTOR_SETS[i] == numECWords) {
                table = i;
                break;
            } else {
                i++;
            }
        }
        if (table < 0) {
            throw new IllegalArgumentException("Illegal number of error correction codewords specified: " + numECWords);
        }
        int[] poly = FACTORS[table];
        char[] ecc = new char[numECWords];
        for (int i2 = 0; i2 < numECWords; i2++) {
            ecc[i2] = 0;
        }
        for (int i3 = start; i3 < start + len; i3++) {
            int m = ecc[numECWords - 1] ^ codewords.charAt(i3);
            for (int k = numECWords - 1; k > 0; k--) {
                if (m == 0 || poly[k] == 0) {
                    ecc[k] = ecc[k - 1];
                } else {
                    ecc[k] = (char) (ecc[k - 1] ^ ALOG[(LOG[m] + LOG[poly[k]]) % 255]);
                }
            }
            if (m == 0 || poly[0] == 0) {
                ecc[0] = 0;
            } else {
                ecc[0] = (char) ALOG[(LOG[m] + LOG[poly[0]]) % 255];
            }
        }
        char[] eccReversed = new char[numECWords];
        for (int i4 = 0; i4 < numECWords; i4++) {
            eccReversed[i4] = ecc[(numECWords - i4) - 1];
        }
        return String.valueOf(eccReversed);
    }
}
