package dji.thirdparty.sanselan.formats.jpeg;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.common.BinaryFileParser;
import dji.thirdparty.sanselan.common.byteSources.ByteSource;
import dji.thirdparty.sanselan.util.Debug;
import java.io.IOException;
import java.io.InputStream;

public class JpegUtils extends BinaryFileParser implements JpegConstants {

    public interface Visitor {
        boolean beginSOS();

        void visitSOS(int i, byte[] bArr, byte[] bArr2);

        boolean visitSOS(int i, byte[] bArr, InputStream inputStream);

        boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException;
    }

    public JpegUtils() {
        setByteOrder(77);
    }

    public void traverseJFIF(ByteSource byteSource, Visitor visitor) throws ImageReadException, IOException {
        byte[] markerBytes;
        int marker;
        boolean doClose;
        InputStream is = null;
        try {
            is = byteSource.getInputStream();
            readAndVerifyBytes(is, SOI, "Not a Valid JPEG File: doesn't begin with 0xffd8");
            int byteOrder = getByteOrder();
            int markerCount = 0;
            while (true) {
                markerBytes = readByteArray("markerBytes", 2, is, "markerBytes");
                marker = convertByteArrayToShort("marker", markerBytes, byteOrder);
                if (marker != 65497 && marker != 65498) {
                    byte[] segmentLengthBytes = readByteArray("segmentLengthBytes", 2, is, "segmentLengthBytes");
                    int segmentLength = convertByteArrayToShort("segmentLength", segmentLengthBytes, byteOrder);
                    if (visitor.visitSegment(marker, markerBytes, segmentLength, segmentLengthBytes, readByteArray("Segment Data", segmentLength - 2, is, "Invalid Segment: insufficient data"))) {
                        markerCount++;
                    } else if (is != null && 1 != 0) {
                        try {
                            return;
                        } catch (Exception e) {
                            Debug.debug((Throwable) e);
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
            if (visitor.beginSOS()) {
                if (!visitor.visitSOS(marker, markerBytes, is)) {
                    doClose = true;
                } else {
                    doClose = false;
                }
                if (is != null && doClose) {
                    try {
                        is.close();
                    } catch (Exception e2) {
                        Debug.debug((Throwable) e2);
                    }
                }
            } else if (is != null && 1 != 0) {
                try {
                    is.close();
                } catch (Exception e3) {
                    Debug.debug((Throwable) e3);
                }
            }
        } finally {
            if (!(is == null || 1 == 0)) {
                try {
                    is.close();
                } catch (Exception e4) {
                    Debug.debug((Throwable) e4);
                }
            }
        }
    }

    public static String getMarkerName(int marker) {
        switch (marker) {
            case JpegConstants.SOF0Marker:
                return "SOF0Marker";
            case JpegConstants.SOF1Marker:
                return "SOF1Marker";
            case JpegConstants.SOF2Marker:
                return "SOF2Marker";
            case JpegConstants.SOF3Marker:
                return "SOF3Marker";
            case JpegConstants.SOF4Marker:
                return "SOF4Marker";
            case JpegConstants.SOF5Marker:
                return "SOF5Marker";
            case JpegConstants.SOF6Marker:
                return "SOF6Marker";
            case JpegConstants.SOF7Marker:
                return "SOF7Marker";
            case JpegConstants.SOF8Marker:
                return "SOF8Marker";
            case JpegConstants.SOF9Marker:
                return "SOF9Marker";
            case JpegConstants.SOF10Marker:
                return "SOF10Marker";
            case JpegConstants.SOF11Marker:
                return "SOF11Marker";
            case JpegConstants.SOF12Marker:
                return "SOF12Marker";
            case JpegConstants.SOF13Marker:
                return "SOF13Marker";
            case JpegConstants.SOF14Marker:
                return "SOF14Marker";
            case JpegConstants.SOF15Marker:
                return "SOF15Marker";
            case 65488:
            case 65489:
            case 65490:
            case 65491:
            case 65492:
            case 65493:
            case 65494:
            case 65495:
            case 65496:
            case 65497:
            case 65499:
            case 65500:
            case 65501:
            case 65502:
            case 65503:
            case 65507:
            case 65508:
            case 65509:
            case 65510:
            case 65511:
            case 65512:
            case 65513:
            case 65514:
            case 65515:
            case 65516:
            default:
                return "Unknown";
            case JpegConstants.SOS_Marker:
                return "SOS_Marker";
            case 65504:
                return "JFIFMarker";
            case JpegConstants.JPEG_APP1_Marker:
                return "JPEG_APP1_Marker";
            case JpegConstants.JPEG_APP2_Marker:
                return "JPEG_APP2_Marker";
            case JpegConstants.JPEG_APP13_Marker:
                return "JPEG_APP13_Marker";
            case JpegConstants.JPEG_APP14_Marker:
                return "JPEG_APP14_Marker";
            case JpegConstants.JPEG_APP15_Marker:
                return "JPEG_APP15_Marker";
        }
    }

    public void dumpJFIF(ByteSource byteSource) throws ImageReadException, IOException, ImageWriteException {
        traverseJFIF(byteSource, new Visitor() {
            /* class dji.thirdparty.sanselan.formats.jpeg.JpegUtils.AnonymousClass1 */

            public boolean beginSOS() {
                return true;
            }

            public void visitSOS(int marker, byte[] markerBytes, byte[] imageData) {
                Debug.debug("SOS marker.  " + imageData.length + " bytes of image data.");
                Debug.debug("");
            }

            public boolean visitSOS(int marker, byte[] markerBytes, InputStream is) {
                return false;
            }

            public boolean visitSegment(int marker, byte[] markerBytes, int segmentLength, byte[] segmentLengthBytes, byte[] segmentData) {
                Debug.debug("Segment marker: " + Integer.toHexString(marker) + " (" + JpegUtils.getMarkerName(marker) + "), " + segmentData.length + " bytes of segment data.");
                return true;
            }
        });
    }
}
