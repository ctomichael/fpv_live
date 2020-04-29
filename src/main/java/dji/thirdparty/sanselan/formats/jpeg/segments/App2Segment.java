package dji.thirdparty.sanselan.formats.jpeg.segments;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.formats.jpeg.JpegImageParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class App2Segment extends APPNSegment implements Comparable {
    public final int cur_marker;
    public final byte[] icc_bytes;
    public final int num_markers;

    public App2Segment(int marker, byte[] segmentData) throws ImageReadException, IOException {
        this(marker, segmentData.length, new ByteArrayInputStream(segmentData));
    }

    public App2Segment(int marker, int marker_length, InputStream is2) throws ImageReadException, IOException {
        super(marker, marker_length, is2);
        if (startsWith(this.bytes, JpegImageParser.icc_profile_label)) {
            InputStream is = new ByteArrayInputStream(this.bytes);
            readAndVerifyBytes(is, JpegImageParser.icc_profile_label, "Not a Valid App2 Segment: missing ICC Profile label");
            this.cur_marker = readByte("cur_marker", is, "Not a valid App2 Marker");
            this.num_markers = readByte("num_markers", is, "Not a valid App2 Marker");
            this.icc_bytes = readByteArray("App2 Data", (marker_length - JpegImageParser.icc_profile_label.length) - 2, is, "Invalid App2 Segment: insufficient data");
            return;
        }
        this.cur_marker = -1;
        this.num_markers = -1;
        this.icc_bytes = null;
    }

    public int compareTo(Object o) {
        return this.cur_marker - ((App2Segment) o).cur_marker;
    }
}
