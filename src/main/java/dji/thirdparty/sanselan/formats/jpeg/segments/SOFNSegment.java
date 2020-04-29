package dji.thirdparty.sanselan.formats.jpeg.segments;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.formats.jpeg.JpegConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SOFNSegment extends Segment {
    public final int height;
    public final int numberOfComponents;
    public final int precision;
    public final int width;

    public SOFNSegment(int marker, byte[] segmentData) throws ImageReadException, IOException {
        this(marker, segmentData.length, new ByteArrayInputStream(segmentData));
    }

    public SOFNSegment(int marker, int marker_length, InputStream is) throws ImageReadException, IOException {
        super(marker, marker_length);
        if (getDebug()) {
            System.out.println("SOF0Segment marker_length: " + marker_length);
        }
        this.precision = readByte("Data_precision", is, "Not a Valid JPEG File");
        this.height = read2Bytes("Image_height", is, "Not a Valid JPEG File");
        this.width = read2Bytes("Image_Width", is, "Not a Valid JPEG File");
        this.numberOfComponents = readByte("Number_of_components", is, "Not a Valid JPEG File");
        skipBytes(is, marker_length - 6, "Not a Valid JPEG File: SOF0 Segment");
        if (getDebug()) {
            System.out.println("");
        }
    }

    public String getDescription() {
        return "SOFN (SOF" + (this.marker - JpegConstants.SOF0Marker) + ") (" + getSegmentType() + ")";
    }
}
