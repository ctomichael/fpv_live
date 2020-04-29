package dji.thirdparty.sanselan.formats.jpeg.segments;

import dji.thirdparty.sanselan.ImageReadException;
import java.io.IOException;
import java.io.InputStream;

public class UnknownSegment extends GenericSegment {
    public UnknownSegment(int marker, int marker_length, InputStream is) throws ImageReadException, IOException {
        super(marker, marker_length, is);
    }

    public UnknownSegment(int marker, byte[] bytes) throws ImageReadException, IOException {
        super(marker, bytes);
    }

    public String getDescription() {
        return "Unknown (" + getSegmentType() + ")";
    }
}
