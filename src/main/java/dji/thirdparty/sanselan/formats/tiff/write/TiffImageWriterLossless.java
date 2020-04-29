package dji.thirdparty.sanselan.formats.tiff.write;

import dji.thirdparty.sanselan.FormatCompliance;
import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.common.BinaryFileFunctions;
import dji.thirdparty.sanselan.common.BinaryOutputStream;
import dji.thirdparty.sanselan.common.byteSources.ByteSource;
import dji.thirdparty.sanselan.common.byteSources.ByteSourceArray;
import dji.thirdparty.sanselan.formats.tiff.JpegImageData;
import dji.thirdparty.sanselan.formats.tiff.TiffContents;
import dji.thirdparty.sanselan.formats.tiff.TiffDirectory;
import dji.thirdparty.sanselan.formats.tiff.TiffElement;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.formats.tiff.TiffReader;
import dji.thirdparty.sanselan.util.Debug;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TiffImageWriterLossless extends TiffImageWriterBase {
    private static final Comparator ELEMENT_SIZE_COMPARATOR = new Comparator() {
        /* class dji.thirdparty.sanselan.formats.tiff.write.TiffImageWriterLossless.AnonymousClass1 */

        public int compare(Object o1, Object o2) {
            return ((TiffElement) o1).length - ((TiffElement) o2).length;
        }
    };
    private static final Comparator ITEM_SIZE_COMPARATOR = new Comparator() {
        /* class dji.thirdparty.sanselan.formats.tiff.write.TiffImageWriterLossless.AnonymousClass2 */

        public int compare(Object o1, Object o2) {
            return ((TiffOutputItem) o1).getItemLength() - ((TiffOutputItem) o2).getItemLength();
        }
    };
    private final byte[] exifBytes;

    public TiffImageWriterLossless(byte[] exifBytes2) {
        this.exifBytes = exifBytes2;
    }

    public TiffImageWriterLossless(int byteOrder, byte[] exifBytes2) {
        super(byteOrder);
        this.exifBytes = exifBytes2;
    }

    private void dumpElements(List elements) throws IOException {
        dumpElements(new ByteSourceArray(this.exifBytes), elements);
    }

    private void dumpElements(ByteSource byteSource, List elements) throws IOException {
        int last = 8;
        for (int i = 0; i < elements.size(); i++) {
            TiffElement element = (TiffElement) elements.get(i);
            if (element.offset > last) {
                int gepLength = element.offset - last;
                Debug.debug("gap of " + gepLength + " bytes.");
                byte[] bytes = byteSource.getBlock(last, gepLength);
                if (bytes.length > 64) {
                    Debug.debug("\thead", BinaryFileFunctions.head(bytes, 32));
                    Debug.debug("\ttail", BinaryFileFunctions.tail(bytes, 32));
                } else {
                    Debug.debug("\tbytes", bytes);
                }
            }
            Debug.debug("element[" + i + "]:" + element.getElementDescription() + " (" + element.offset + " + " + element.length + " = " + (element.offset + element.length) + ")");
            if (element instanceof TiffDirectory) {
                Debug.debug("\tnext Directory Offset: " + ((TiffDirectory) element).nextDirectoryOffset);
            }
            last = element.offset + element.length;
        }
        Debug.debug();
    }

    private List analyzeOldTiff() throws ImageWriteException, IOException {
        try {
            TiffContents contents = new TiffReader(false).readContents(new ByteSourceArray(this.exifBytes), null, FormatCompliance.getDefault());
            ArrayList elements = new ArrayList();
            List directories = contents.directories;
            for (int d = 0; d < directories.size(); d++) {
                TiffDirectory directory = (TiffDirectory) directories.get(d);
                elements.add(directory);
                List fields = directory.getDirectoryEntrys();
                for (int f = 0; f < fields.size(); f++) {
                    TiffElement oversizeValue = ((TiffField) fields.get(f)).getOversizeValueElement();
                    if (oversizeValue != null) {
                        elements.add(oversizeValue);
                    }
                }
                JpegImageData jpegImageData = directory.getJpegImageData();
                if (jpegImageData != null) {
                    elements.add(jpegImageData);
                }
            }
            Collections.sort(elements, TiffElement.COMPARATOR);
            ArrayList arrayList = new ArrayList();
            TiffElement start = null;
            int index = -1;
            for (int i = 0; i < elements.size(); i++) {
                TiffElement element = (TiffElement) elements.get(i);
                int lastElementByte = element.offset + element.length;
                if (start == null) {
                    start = element;
                    index = lastElementByte;
                } else if (element.offset - index > 3) {
                    arrayList.add(new TiffElement.Stub(start.offset, index - start.offset));
                    start = element;
                    index = lastElementByte;
                } else {
                    index = lastElementByte;
                }
            }
            if (start != null) {
                arrayList.add(new TiffElement.Stub(start.offset, index - start.offset));
            }
            return arrayList;
        } catch (ImageReadException e) {
            throw new ImageWriteException(e.getMessage(), e);
        }
    }

    public void write(OutputStream os, TiffOutputSet outputSet) throws IOException, ImageWriteException {
        List analysis = analyzeOldTiff();
        int oldLength = this.exifBytes.length;
        if (analysis.size() < 1) {
            throw new ImageWriteException("Couldn't analyze old tiff data.");
        }
        if (analysis.size() == 1) {
            TiffElement onlyElement = (TiffElement) analysis.get(0);
            if (onlyElement.offset == 8 && onlyElement.offset + onlyElement.length + 8 == oldLength) {
                new TiffImageWriterLossy(this.byteOrder).write(os, outputSet);
                return;
            }
        }
        TiffOutputSummary outputSummary = validateDirectories(outputSet);
        List outputItems = outputSet.getOutputItems(outputSummary);
        int outputLength = updateOffsetsStep(analysis, outputItems);
        outputSummary.updateOffsets(this.byteOrder);
        writeStep(os, outputSet, analysis, outputItems, outputLength);
    }

    private int updateOffsetsStep(List analysis, List outputItems) throws IOException, ImageWriteException {
        int overflowIndex = this.exifBytes.length;
        List unusedElements = new ArrayList(analysis);
        Collections.sort(unusedElements, TiffElement.COMPARATOR);
        Collections.reverse(unusedElements);
        while (unusedElements.size() > 0) {
            TiffElement element = (TiffElement) unusedElements.get(0);
            if (element.offset + element.length != overflowIndex) {
                break;
            }
            overflowIndex -= element.length;
            unusedElements.remove(0);
        }
        Collections.sort(unusedElements, ELEMENT_SIZE_COMPARATOR);
        Collections.reverse(unusedElements);
        List unplacedItems = new ArrayList(outputItems);
        Collections.sort(unplacedItems, ITEM_SIZE_COMPARATOR);
        Collections.reverse(unplacedItems);
        while (unplacedItems.size() > 0) {
            TiffOutputItem outputItem = (TiffOutputItem) unplacedItems.remove(0);
            int outputItemLength = outputItem.getItemLength();
            TiffElement bestFit = null;
            for (int i = 0; i < unusedElements.size(); i++) {
                TiffElement element2 = (TiffElement) unusedElements.get(i);
                if (element2.length < outputItemLength) {
                    break;
                }
                bestFit = element2;
            }
            if (bestFit == null) {
                outputItem.setOffset(overflowIndex);
                overflowIndex += outputItemLength;
            } else {
                outputItem.setOffset(bestFit.offset);
                unusedElements.remove(bestFit);
                if (bestFit.length > outputItemLength) {
                    unusedElements.add(new TiffElement.Stub(bestFit.offset + outputItemLength, bestFit.length - outputItemLength));
                    Collections.sort(unusedElements, ELEMENT_SIZE_COMPARATOR);
                    Collections.reverse(unusedElements);
                }
            }
        }
        return overflowIndex;
    }

    private static class BufferOutputStream extends OutputStream {
        private final byte[] buffer;
        private int index;

        public BufferOutputStream(byte[] buffer2, int index2) {
            this.buffer = buffer2;
            this.index = index2;
        }

        public void write(int b) throws IOException {
            if (this.index >= this.buffer.length) {
                throw new IOException("Buffer overflow.");
            }
            byte[] bArr = this.buffer;
            int i = this.index;
            this.index = i + 1;
            bArr[i] = (byte) b;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            if (this.index + len > this.buffer.length) {
                throw new IOException("Buffer overflow.");
            }
            System.arraycopy(b, off, this.buffer, this.index, len);
            this.index += len;
        }
    }

    private void writeStep(OutputStream os, TiffOutputSet outputSet, List analysis, List outputItems, int outputLength) throws IOException, ImageWriteException {
        TiffOutputDirectory rootDirectory = outputSet.getRootDirectory();
        byte[] output = new byte[outputLength];
        System.arraycopy(this.exifBytes, 0, output, 0, Math.min(this.exifBytes.length, output.length));
        writeImageFileHeader(new BinaryOutputStream(new BufferOutputStream(output, 0), this.byteOrder), rootDirectory.getOffset());
        for (int i = 0; i < analysis.size(); i++) {
            TiffElement element = (TiffElement) analysis.get(i);
            for (int j = 0; j < element.length; j++) {
                int index = element.offset + j;
                if (index < output.length) {
                    output[index] = 0;
                }
            }
        }
        for (int i2 = 0; i2 < outputItems.size(); i2++) {
            TiffOutputItem outputItem = (TiffOutputItem) outputItems.get(i2);
            outputItem.writeItem(new BinaryOutputStream(new BufferOutputStream(output, outputItem.getOffset()), this.byteOrder));
        }
        os.write(output);
    }
}
