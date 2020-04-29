package dji.thirdparty.sanselan.formats.tiff.write;

import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.common.BinaryOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TiffImageWriterLossy extends TiffImageWriterBase {
    public TiffImageWriterLossy() {
    }

    public TiffImageWriterLossy(int byteOrder) {
        super(byteOrder);
    }

    public void write(OutputStream os, TiffOutputSet outputSet) throws IOException, ImageWriteException {
        TiffOutputSummary outputSummary = validateDirectories(outputSet);
        List outputItems = outputSet.getOutputItems(outputSummary);
        updateOffsetsStep(outputItems);
        outputSummary.updateOffsets(this.byteOrder);
        writeStep(new BinaryOutputStream(os, this.byteOrder), outputItems);
    }

    private void updateOffsetsStep(List outputItems) throws IOException, ImageWriteException {
        int offset = 8;
        for (int i = 0; i < outputItems.size(); i++) {
            TiffOutputItem outputItem = (TiffOutputItem) outputItems.get(i);
            outputItem.setOffset(offset);
            int itemLength = outputItem.getItemLength();
            offset = offset + itemLength + imageDataPaddingLength(itemLength);
        }
    }

    private void writeStep(BinaryOutputStream bos, List outputItems) throws IOException, ImageWriteException {
        writeImageFileHeader(bos);
        for (int i = 0; i < outputItems.size(); i++) {
            TiffOutputItem outputItem = (TiffOutputItem) outputItems.get(i);
            outputItem.writeItem(bos);
            int remainder = imageDataPaddingLength(outputItem.getItemLength());
            for (int j = 0; j < remainder; j++) {
                bos.write(0);
            }
        }
    }
}
