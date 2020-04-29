package dji.thirdparty.sanselan.formats.tiff.write;

import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class TiffOutputSummary implements TiffConstants {
    public final int byteOrder;
    public final Map directoryTypeMap;
    private List imageDataItems = new ArrayList();
    private List offsetItems = new ArrayList();
    public final TiffOutputDirectory rootDirectory;

    public TiffOutputSummary(int byteOrder2, TiffOutputDirectory rootDirectory2, Map directoryTypeMap2) {
        this.byteOrder = byteOrder2;
        this.rootDirectory = rootDirectory2;
        this.directoryTypeMap = directoryTypeMap2;
    }

    private static class OffsetItem {
        public final TiffOutputItem item;
        public final TiffOutputField itemOffsetField;

        public OffsetItem(TiffOutputItem item2, TiffOutputField itemOffsetField2) {
            this.itemOffsetField = itemOffsetField2;
            this.item = item2;
        }
    }

    public void add(TiffOutputItem item, TiffOutputField itemOffsetField) {
        this.offsetItems.add(new OffsetItem(item, itemOffsetField));
    }

    public void updateOffsets(int byteOrder2) throws ImageWriteException {
        for (int i = 0; i < this.offsetItems.size(); i++) {
            OffsetItem offset = (OffsetItem) this.offsetItems.get(i);
            offset.itemOffsetField.setData(FIELD_TYPE_LONG.writeData(new int[]{offset.item.getOffset()}, byteOrder2));
        }
        for (int i2 = 0; i2 < this.imageDataItems.size(); i2++) {
            ImageDataOffsets imageDataInfo = (ImageDataOffsets) this.imageDataItems.get(i2);
            for (int j = 0; j < imageDataInfo.outputItems.length; j++) {
                imageDataInfo.imageDataOffsets[j] = imageDataInfo.outputItems[j].getOffset();
            }
            imageDataInfo.imageDataOffsetsField.setData(FIELD_TYPE_LONG.writeData(imageDataInfo.imageDataOffsets, byteOrder2));
        }
    }

    public void addTiffImageData(ImageDataOffsets imageDataInfo) {
        this.imageDataItems.add(imageDataInfo);
    }
}
