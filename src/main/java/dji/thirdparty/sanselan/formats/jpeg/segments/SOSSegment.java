package dji.thirdparty.sanselan.formats.jpeg.segments;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.util.Debug;
import java.io.IOException;
import java.io.InputStream;

public class SOSSegment extends Segment {
    public SOSSegment(int marker, int marker_length, InputStream is) throws ImageReadException, IOException {
        super(marker, marker_length);
        if (getDebug()) {
            System.out.println("SOSSegment marker_length: " + marker_length);
        }
        Debug.debug("SOS", marker_length);
        int number_of_components_in_scan = readByte("number_of_components_in_scan", is, "Not a Valid JPEG File");
        Debug.debug("number_of_components_in_scan", number_of_components_in_scan);
        for (int i = 0; i < number_of_components_in_scan; i++) {
            Debug.debug("scan_component_selector", readByte("scan_component_selector", is, "Not a Valid JPEG File"));
            Debug.debug("ac_dc_entrooy_coding_table_selector", readByte("ac_dc_entrooy_coding_table_selector", is, "Not a Valid JPEG File"));
        }
        Debug.debug("start_of_spectral_selection", readByte("start_of_spectral_selection", is, "Not a Valid JPEG File"));
        Debug.debug("end_of_spectral_selection", readByte("end_of_spectral_selection", is, "Not a Valid JPEG File"));
        Debug.debug("successive_approximation_bit_position", readByte("successive_approximation_bit_position", is, "Not a Valid JPEG File"));
        if (getDebug()) {
            System.out.println("");
        }
    }

    public String getDescription() {
        return "SOS (" + getSegmentType() + ")";
    }
}
