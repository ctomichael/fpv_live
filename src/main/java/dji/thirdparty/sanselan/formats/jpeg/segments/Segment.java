package dji.thirdparty.sanselan.formats.jpeg.segments;

import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.sanselan.common.BinaryFileParser;
import dji.thirdparty.sanselan.formats.jpeg.JpegConstants;
import java.io.PrintWriter;

public abstract class Segment extends BinaryFileParser {
    public final int length;
    public final int marker;

    public abstract String getDescription();

    public Segment(int marker2, int length2) {
        this.marker = marker2;
        this.length = length2;
    }

    public void dump(PrintWriter pw) {
    }

    public String toString() {
        return "[Segment: " + getDescription() + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    public String getSegmentType() {
        switch (this.marker) {
            case 65281:
                return "For temporary private use in arithmetic coding";
            case JpegConstants.SOF0Marker:
                return "Start Of Frame, Baseline DCT, Huffman coding";
            case JpegConstants.SOF1Marker:
                return "Start Of Frame, Extended sequential DCT, Huffman coding";
            case JpegConstants.SOF2Marker:
                return "Start Of Frame, Progressive DCT, Huffman coding";
            case JpegConstants.SOF3Marker:
                return "Start Of Frame, Lossless (sequential), Huffman coding";
            case JpegConstants.SOF4Marker:
                return "Define Huffman table(s)";
            case JpegConstants.SOF5Marker:
                return "Start Of Frame, Differential sequential DCT, Huffman coding";
            case JpegConstants.SOF6Marker:
                return "Start Of Frame, Differential progressive DCT, Huffman coding";
            case JpegConstants.SOF7Marker:
                return "Start Of Frame, Differential lossless (sequential), Huffman coding";
            case JpegConstants.SOF8Marker:
                return "Start Of Frame, Reserved for JPEG extensions, arithmetic coding";
            case JpegConstants.SOF9Marker:
                return "Start Of Frame, Extended sequential DCT, arithmetic coding";
            case JpegConstants.SOF10Marker:
                return "Start Of Frame, Progressive DCT, arithmetic coding";
            case JpegConstants.SOF11Marker:
                return "Start Of Frame, Lossless (sequential), arithmetic coding";
            case JpegConstants.SOF12Marker:
                return "Define arithmetic coding conditioning(s)";
            case JpegConstants.SOF13Marker:
                return "Start Of Frame, Differential sequential DCT, arithmetic coding";
            case JpegConstants.SOF14Marker:
                return "Start Of Frame, Differential progressive DCT, arithmetic coding";
            case JpegConstants.SOF15Marker:
                return "Start Of Frame, Differential lossless (sequential), arithmetic coding";
            case 65488:
                return "Restart with modulo 8 count 0";
            case 65489:
                return "Restart with modulo 8 count 1";
            case 65490:
                return "Restart with modulo 8 count 2";
            case 65491:
                return "Restart with modulo 8 count 3";
            case 65492:
                return "Restart with modulo 8 count 4";
            case 65493:
                return "Restart with modulo 8 count 5";
            case 65494:
                return "Restart with modulo 8 count 6";
            case 65495:
                return "Restart with modulo 8 count 7";
            case 65496:
                return "Start of image";
            case 65497:
                return "End of image";
            case JpegConstants.SOS_Marker:
                return "Start of scan";
            case 65499:
                return "Define quantization table(s)";
            case 65500:
                return "Define number of lines";
            case 65501:
                return "Define restart interval";
            case 65502:
                return "Define hierarchical progression";
            case 65503:
                return "Expand reference component(s)";
            case 65534:
                return "Comment";
            default:
                if (this.marker >= 65282 && this.marker <= 65471) {
                    return "Reserved";
                }
                if (this.marker >= 65504 && this.marker <= 65519) {
                    return "APP" + (this.marker - 65504);
                }
                if (this.marker < 65520 || this.marker > 65533) {
                    return "Unknown";
                }
                return "JPG" + (this.marker - 65504);
        }
    }
}
