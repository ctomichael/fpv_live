package dji.thirdparty.sanselan.formats.tiff;

import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.formats.tiff.constants.TagInfo;
import dji.thirdparty.sanselan.util.Debug;
import java.util.ArrayList;
import java.util.Collections;

public class TiffContents {
    public final ArrayList directories;
    public final TiffHeader header;

    public TiffContents(TiffHeader tiffHeader, ArrayList directories2) {
        this.header = tiffHeader;
        this.directories = directories2;
    }

    public ArrayList getElements() throws ImageReadException {
        ArrayList result = new ArrayList();
        result.add(this.header);
        for (int i = 0; i < this.directories.size(); i++) {
            TiffDirectory directory = (TiffDirectory) this.directories.get(i);
            result.add(directory);
            ArrayList fields = directory.entries;
            for (int j = 0; j < fields.size(); j++) {
                TiffElement oversizeValue = ((TiffField) fields.get(j)).getOversizeValueElement();
                if (oversizeValue != null) {
                    result.add(oversizeValue);
                }
            }
            if (directory.hasTiffImageData()) {
                result.addAll(directory.getTiffRawImageDataElements());
            }
            if (directory.hasJpegImageData()) {
                result.add(directory.getJpegRawImageDataElement());
            }
        }
        return result;
    }

    public TiffField findField(TagInfo tag) throws ImageReadException {
        for (int i = 0; i < this.directories.size(); i++) {
            TiffField field = ((TiffDirectory) this.directories.get(i)).findField(tag);
            if (field != null) {
                return field;
            }
        }
        return null;
    }

    public void dissect(boolean verbose) throws ImageReadException {
        String verbosity;
        ArrayList elements = getElements();
        Collections.sort(elements, TiffElement.COMPARATOR);
        int lastEnd = 0;
        for (int i = 0; i < elements.size(); i++) {
            TiffElement element = (TiffElement) elements.get(i);
            if (element.offset > lastEnd) {
                Debug.debug("\tgap: " + (element.offset - lastEnd));
            }
            if (element.offset < lastEnd) {
                Debug.debug("\toverlap");
            }
            Debug.debug("element, start: " + element.offset + ", length: " + element.length + ", end: " + (element.offset + element.length) + ": " + element.getElementDescription(false));
            if (verbose && (verbosity = element.getElementDescription(true)) != null) {
                Debug.debug(verbosity);
            }
            lastEnd = element.offset + element.length;
        }
        Debug.debug("end: " + lastEnd);
        Debug.debug();
    }
}
