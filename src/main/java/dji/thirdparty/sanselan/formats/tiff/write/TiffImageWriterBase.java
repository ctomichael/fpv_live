package dji.thirdparty.sanselan.formats.tiff.write;

import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.common.BinaryConstants;
import dji.thirdparty.sanselan.common.BinaryOutputStream;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class TiffImageWriterBase implements TiffConstants, BinaryConstants {
    protected final int byteOrder;

    public abstract void write(OutputStream outputStream, TiffOutputSet tiffOutputSet) throws IOException, ImageWriteException;

    public TiffImageWriterBase() {
        this.byteOrder = 73;
    }

    public TiffImageWriterBase(int byteOrder2) {
        this.byteOrder = byteOrder2;
    }

    protected static final int imageDataPaddingLength(int dataLength) {
        return (4 - (dataLength % 4)) % 4;
    }

    /* access modifiers changed from: protected */
    public TiffOutputSummary validateDirectories(TiffOutputSet outputSet) throws ImageWriteException {
        List directories = outputSet.getDirectories();
        if (1 > directories.size()) {
            throw new ImageWriteException("No directories.");
        }
        TiffOutputDirectory exifDirectory = null;
        TiffOutputDirectory gpsDirectory = null;
        TiffOutputDirectory interoperabilityDirectory = null;
        TiffOutputField exifDirectoryOffsetField = null;
        TiffOutputField gpsDirectoryOffsetField = null;
        TiffOutputField interoperabilityDirectoryOffsetField = null;
        ArrayList directoryIndices = new ArrayList();
        Map directoryTypeMap = new HashMap();
        for (int i = 0; i < directories.size(); i++) {
            TiffOutputDirectory directory = (TiffOutputDirectory) directories.get(i);
            int dirType = directory.type;
            Integer num = new Integer(dirType);
            directoryTypeMap.put(num, directory);
            if (dirType < 0) {
                switch (dirType) {
                    case -4:
                        if (interoperabilityDirectory == null) {
                            interoperabilityDirectory = directory;
                            break;
                        } else {
                            throw new ImageWriteException("More than one Interoperability directory.");
                        }
                    case -3:
                        if (gpsDirectory == null) {
                            gpsDirectory = directory;
                            break;
                        } else {
                            throw new ImageWriteException("More than one GPS directory.");
                        }
                    case -2:
                        if (exifDirectory == null) {
                            exifDirectory = directory;
                            break;
                        } else {
                            throw new ImageWriteException("More than one EXIF directory.");
                        }
                    default:
                        throw new ImageWriteException("Unknown directory: " + dirType);
                }
            } else if (directoryIndices.contains(num)) {
                throw new ImageWriteException("More than one directory with index: " + dirType + ".");
            } else {
                directoryIndices.add(new Integer(dirType));
            }
            HashSet fieldTags = new HashSet();
            ArrayList fields = directory.getFields();
            for (int j = 0; j < fields.size(); j++) {
                TiffOutputField field = (TiffOutputField) fields.get(j);
                Integer fieldKey = new Integer(field.tag);
                if (!fieldTags.contains(fieldKey)) {
                    fieldTags.add(fieldKey);
                    if (field.tag != EXIF_TAG_EXIF_OFFSET.tag) {
                        if (field.tag != EXIF_TAG_INTEROP_OFFSET.tag) {
                            if (field.tag != EXIF_TAG_GPSINFO.tag) {
                                continue;
                            } else if (gpsDirectoryOffsetField != null) {
                                throw new ImageWriteException("More than one GPS directory offset field.");
                            } else {
                                gpsDirectoryOffsetField = field;
                            }
                        } else if (interoperabilityDirectoryOffsetField != null) {
                            throw new ImageWriteException("More than one Interoperability directory offset field.");
                        } else {
                            interoperabilityDirectoryOffsetField = field;
                        }
                    } else if (exifDirectoryOffsetField != null) {
                        throw new ImageWriteException("More than one Exif directory offset field.");
                    } else {
                        exifDirectoryOffsetField = field;
                    }
                }
            }
        }
        if (directoryIndices.size() < 1) {
            throw new ImageWriteException("Missing root directory.");
        }
        Collections.sort(directoryIndices);
        TiffOutputDirectory previousDirectory = null;
        for (int i2 = 0; i2 < directoryIndices.size(); i2++) {
            Integer index = (Integer) directoryIndices.get(i2);
            if (index.intValue() != i2) {
                throw new ImageWriteException("Missing directory: " + i2 + ".");
            }
            TiffOutputDirectory directory2 = (TiffOutputDirectory) directoryTypeMap.get(index);
            if (previousDirectory != null) {
                previousDirectory.setNextDirectory(directory2);
            }
            previousDirectory = directory2;
        }
        TiffOutputDirectory rootDirectory = (TiffOutputDirectory) directoryTypeMap.get(new Integer(0));
        TiffOutputSummary tiffOutputSummary = new TiffOutputSummary(this.byteOrder, rootDirectory, directoryTypeMap);
        if (interoperabilityDirectory != null || interoperabilityDirectoryOffsetField == null) {
            if (interoperabilityDirectory != null) {
                if (exifDirectory == null) {
                    exifDirectory = outputSet.addExifDirectory();
                }
                if (interoperabilityDirectoryOffsetField == null) {
                    interoperabilityDirectoryOffsetField = TiffOutputField.createOffsetField(EXIF_TAG_INTEROP_OFFSET, this.byteOrder);
                    exifDirectory.add(interoperabilityDirectoryOffsetField);
                }
                tiffOutputSummary.add(interoperabilityDirectory, interoperabilityDirectoryOffsetField);
            }
            if (exifDirectory != null || exifDirectoryOffsetField == null) {
                if (exifDirectory != null) {
                    if (exifDirectoryOffsetField == null) {
                        exifDirectoryOffsetField = TiffOutputField.createOffsetField(EXIF_TAG_EXIF_OFFSET, this.byteOrder);
                        rootDirectory.add(exifDirectoryOffsetField);
                    }
                    tiffOutputSummary.add(exifDirectory, exifDirectoryOffsetField);
                }
                if (gpsDirectory != null || gpsDirectoryOffsetField == null) {
                    if (gpsDirectory != null) {
                        if (gpsDirectoryOffsetField == null) {
                            gpsDirectoryOffsetField = TiffOutputField.createOffsetField(EXIF_TAG_GPSINFO, this.byteOrder);
                            rootDirectory.add(gpsDirectoryOffsetField);
                        }
                        tiffOutputSummary.add(gpsDirectory, gpsDirectoryOffsetField);
                    }
                    return tiffOutputSummary;
                }
                throw new ImageWriteException("Output set has GPS Directory Offset field, but no GPS Directory");
            }
            throw new ImageWriteException("Output set has Exif Directory Offset field, but no Exif Directory");
        }
        throw new ImageWriteException("Output set has Interoperability Directory Offset field, but no Interoperability Directory");
    }

    /* access modifiers changed from: protected */
    public void writeImageFileHeader(BinaryOutputStream bos) throws IOException, ImageWriteException {
        writeImageFileHeader(bos, 8);
    }

    /* access modifiers changed from: protected */
    public void writeImageFileHeader(BinaryOutputStream bos, int offsetToFirstIFD) throws IOException, ImageWriteException {
        bos.write(this.byteOrder);
        bos.write(this.byteOrder);
        bos.write2Bytes(42);
        bos.write4Bytes(offsetToFirstIFD);
    }
}
