package dji.thirdparty.sanselan.formats.jpeg.segments;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.formats.jpeg.JpegConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JFIFSegment extends Segment implements JpegConstants {
    public final int densityUnits;
    public final int jfifMajorVersion;
    public final int jfifMinorVersion;
    public final int thumbnailSize;
    public final int xDensity;
    public final int xThumbnail;
    public final int yDensity;
    public final int yThumbnail;

    public String getDescription() {
        return "JFIF (" + getSegmentType() + ")";
    }

    public JFIFSegment(int marker, byte[] segmentData) throws ImageReadException, IOException {
        this(marker, segmentData.length, new ByteArrayInputStream(segmentData));
    }

    public JFIFSegment(int marker, int marker_length, InputStream is) throws ImageReadException, IOException {
        super(marker, marker_length);
        byte[] signature = readBytes(is, JFIF0_SIGNATURE.length);
        if (compareByteArrays(signature, JFIF0_SIGNATURE) || compareByteArrays(signature, JFIF0_SIGNATURE_ALTERNATIVE)) {
            this.jfifMajorVersion = readByte("JFIF_major_version", is, "Not a Valid JPEG File");
            this.jfifMinorVersion = readByte("JFIF_minor_version", is, "Not a Valid JPEG File");
            this.densityUnits = readByte("density_units", is, "Not a Valid JPEG File");
            this.xDensity = read2Bytes("x_density", is, "Not a Valid JPEG File");
            this.yDensity = read2Bytes("y_density", is, "Not a Valid JPEG File");
            this.xThumbnail = readByte("x_thumbnail", is, "Not a Valid JPEG File");
            this.yThumbnail = readByte("y_thumbnail", is, "Not a Valid JPEG File");
            this.thumbnailSize = this.xThumbnail * this.yThumbnail;
            if (this.thumbnailSize > 0) {
                skipBytes(is, this.thumbnailSize, "Not a Valid JPEG File: missing thumbnail");
            }
            if (getDebug()) {
                System.out.println("");
                return;
            }
            return;
        }
        throw new ImageReadException("Not a Valid JPEG File: missing JFIF string");
    }
}
