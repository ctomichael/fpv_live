package dji.thirdparty.sanselan.formats.jpeg.segments;

import dji.thirdparty.sanselan.ImageReadException;
import java.io.IOException;
import java.io.InputStream;

public class APPNSegment extends GenericSegment {
    public APPNSegment(int marker, int marker_length, InputStream is) throws ImageReadException, IOException {
        super(marker, marker_length, is);
    }

    public String getDescription() {
        return "APPN (APP" + (this.marker - 65504) + ") (" + getSegmentType() + ")";
    }
}
