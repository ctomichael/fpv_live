package dji.thirdparty.sanselan.formats.tiff.write;

import dji.thirdparty.sanselan.formats.tiff.TiffElement;
import dji.thirdparty.sanselan.formats.tiff.write.TiffOutputItem;

class ImageDataOffsets {
    public final int[] imageDataOffsets;
    public final TiffOutputField imageDataOffsetsField;
    public final TiffOutputItem[] outputItems;

    public ImageDataOffsets(TiffElement.DataElement[] imageData, int[] imageDataOffsets2, TiffOutputField imageDataOffsetsField2) {
        this.imageDataOffsets = imageDataOffsets2;
        this.imageDataOffsetsField = imageDataOffsetsField2;
        this.outputItems = new TiffOutputItem[imageData.length];
        for (int i = 0; i < imageData.length; i++) {
            this.outputItems[i] = new TiffOutputItem.Value("TIFF image data", imageData[i].data);
        }
    }
}
