package dji.thirdparty.sanselan.formats.jpeg.segments;

import dji.thirdparty.sanselan.ImageReadException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public abstract class GenericSegment extends Segment {
    public final byte[] bytes;

    public GenericSegment(int marker, int marker_length, InputStream is) throws ImageReadException, IOException {
        super(marker, marker_length);
        this.bytes = readByteArray("Segment Data", marker_length, is, "Invalid Segment: insufficient data");
    }

    public GenericSegment(int marker, byte[] bytes2) throws ImageReadException, IOException {
        super(marker, bytes2.length);
        this.bytes = bytes2;
    }

    public void dump(PrintWriter pw) {
        dump(pw, 0);
    }

    public void dump(PrintWriter pw, int start) {
        int i = 0;
        while (i < 50 && i + start < this.bytes.length) {
            debugNumber(pw, "\t" + (i + start), this.bytes[i + start]);
            i++;
        }
    }
}
