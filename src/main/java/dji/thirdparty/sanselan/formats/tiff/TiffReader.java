package dji.thirdparty.sanselan.formats.tiff;

import dji.thirdparty.sanselan.FormatCompliance;
import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.SanselanConstants;
import dji.thirdparty.sanselan.common.BinaryFileParser;
import dji.thirdparty.sanselan.common.byteSources.ByteSource;
import dji.thirdparty.sanselan.formats.tiff.TiffDirectory;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;
import dji.thirdparty.sanselan.util.Debug;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TiffReader extends BinaryFileParser implements TiffConstants {
    private final boolean strict;

    public interface Listener {
        boolean addDirectory(TiffDirectory tiffDirectory);

        boolean addField(TiffField tiffField);

        boolean readImageData();

        boolean readOffsetDirectories();

        boolean setTiffHeader(TiffHeader tiffHeader);
    }

    public TiffReader(boolean strict2) {
        this.strict = strict2;
    }

    private TiffHeader readTiffHeader(ByteSource byteSource, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        InputStream is = null;
        try {
            is = byteSource.getInputStream();
            TiffHeader readTiffHeader = readTiffHeader(is, formatCompliance);
            if (is != null) {
                try {
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
            }
            return readTiffHeader;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                }
            }
        }
    }

    private TiffHeader readTiffHeader(InputStream is, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        int BYTE_ORDER_1 = readByte("BYTE_ORDER_1", is, "Not a Valid TIFF File");
        setByteOrder(BYTE_ORDER_1, readByte("BYTE_ORDER_2", is, "Not a Valid TIFF File"));
        int tiffVersion = read2Bytes("tiffVersion", is, "Not a Valid TIFF File");
        if (tiffVersion != 42) {
            throw new ImageReadException("Unknown Tiff Version: " + tiffVersion);
        }
        int offsetToFirstIFD = read4Bytes("offsetToFirstIFD", is, "Not a Valid TIFF File");
        skipBytes(is, offsetToFirstIFD - 8, "Not a Valid TIFF File: couldn't find IFDs");
        if (this.debug) {
            System.out.println("");
        }
        return new TiffHeader(BYTE_ORDER_1, tiffVersion, offsetToFirstIFD);
    }

    private void readDirectories(ByteSource byteSource, FormatCompliance formatCompliance, Listener listener) throws ImageReadException, IOException {
        TiffHeader tiffHeader = readTiffHeader(byteSource, formatCompliance);
        if (listener.setTiffHeader(tiffHeader)) {
            readDirectory(byteSource, tiffHeader.offsetToFirstIFD, 0, formatCompliance, listener, new ArrayList());
        }
    }

    private boolean readDirectory(ByteSource byteSource, int offset, int dirType, FormatCompliance formatCompliance, Listener listener, List visited) throws ImageReadException, IOException {
        return readDirectory(byteSource, offset, dirType, formatCompliance, listener, false, visited);
    }

    private boolean readDirectory(ByteSource byteSource, int offset, int dirType, FormatCompliance formatCompliance, Listener listener, boolean ignoreNextDirectory, List visited) throws ImageReadException, IOException {
        int subDirectoryType;
        Integer num = new Integer(offset);
        if (visited.contains(num)) {
            return false;
        }
        visited.add(num);
        InputStream is = null;
        try {
            is = byteSource.getInputStream();
            if (offset > 0) {
                is.skip((long) offset);
            }
            ArrayList fields = new ArrayList();
            if (((long) offset) < byteSource.getLength()) {
                int entryCount = read2Bytes("DirectoryEntryCount", is, "Not a Valid TIFF File");
                for (int i = 0; i < entryCount; i++) {
                    int tag = read2Bytes(TiffField.Attribute_Tag, is, "Not a Valid TIFF File");
                    int type = read2Bytes("Type", is, "Not a Valid TIFF File");
                    int length = read4Bytes("Length", is, "Not a Valid TIFF File");
                    byte[] valueOffsetBytes = readByteArray("ValueOffset", 4, is, "Not a Valid TIFF File");
                    int valueOffset = convertByteArrayToInt("ValueOffset", valueOffsetBytes);
                    if (tag != 0) {
                        TiffField field = new TiffField(tag, dirType, type, length, valueOffset, valueOffsetBytes, getByteOrder());
                        field.setSortHint(i);
                        field.fillInValue(byteSource);
                        fields.add(field);
                        if (!listener.addField(field)) {
                            if (is == null) {
                                return true;
                            }
                            try {
                                is.close();
                                return true;
                            } catch (Exception e) {
                                Debug.debug((Throwable) e);
                                return true;
                            }
                        }
                    }
                }
                TiffDirectory tiffDirectory = new TiffDirectory(dirType, fields, offset, read4Bytes("nextDirectoryOffset", is, "Not a Valid TIFF File"));
                if (listener.readImageData() && tiffDirectory.hasJpegImageData()) {
                    tiffDirectory.setJpegImageData(getJpegRawImageData(byteSource, tiffDirectory));
                }
                if (listener.addDirectory(tiffDirectory)) {
                    if (listener.readOffsetDirectories()) {
                        ArrayList arrayList = new ArrayList();
                        for (int j = 0; j < fields.size(); j++) {
                            TiffField entry = (TiffField) fields.get(j);
                            if (entry.tag == TiffConstants.EXIF_TAG_EXIF_OFFSET.tag || entry.tag == TiffConstants.EXIF_TAG_GPSINFO.tag || entry.tag == TiffConstants.EXIF_TAG_INTEROP_OFFSET.tag) {
                                int subDirectoryOffset = ((Number) entry.getValue()).intValue();
                                if (entry.tag == TiffConstants.EXIF_TAG_EXIF_OFFSET.tag) {
                                    subDirectoryType = -2;
                                } else if (entry.tag == TiffConstants.EXIF_TAG_GPSINFO.tag) {
                                    subDirectoryType = -3;
                                } else if (entry.tag == TiffConstants.EXIF_TAG_INTEROP_OFFSET.tag) {
                                    subDirectoryType = -4;
                                } else {
                                    throw new ImageReadException("Unknown subdirectory type.");
                                }
                                if (!readDirectory(byteSource, subDirectoryOffset, subDirectoryType, formatCompliance, listener, true, visited)) {
                                    arrayList.add(entry);
                                }
                            }
                        }
                        fields.removeAll(arrayList);
                    }
                    if (!ignoreNextDirectory && tiffDirectory.nextDirectoryOffset > 0) {
                        readDirectory(byteSource, tiffDirectory.nextDirectoryOffset, dirType + 1, formatCompliance, listener, visited);
                    }
                    if (is == null) {
                        return true;
                    }
                    try {
                        is.close();
                        return true;
                    } catch (Exception e2) {
                        Debug.debug((Throwable) e2);
                        return true;
                    }
                } else if (is == null) {
                    return true;
                } else {
                    try {
                        is.close();
                        return true;
                    } catch (Exception e3) {
                        Debug.debug((Throwable) e3);
                        return true;
                    }
                }
            } else if (is == null) {
                return true;
            } else {
                try {
                    is.close();
                    return true;
                } catch (Exception e4) {
                    Debug.debug((Throwable) e4);
                    return true;
                }
            }
        } catch (IOException e5) {
            if (this.strict) {
                throw e5;
            } else if (is == null) {
                return true;
            } else {
                try {
                    is.close();
                    return true;
                } catch (Exception e6) {
                    Debug.debug((Throwable) e6);
                    return true;
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e7) {
                    Debug.debug((Throwable) e7);
                }
            }
            throw th;
        }
    }

    private static class Collector implements Listener {
        private ArrayList directories;
        private ArrayList fields;
        private final boolean readThumbnails;
        private TiffHeader tiffHeader;

        public Collector() {
            this(null);
        }

        public Collector(Map params) {
            this.tiffHeader = null;
            this.directories = new ArrayList();
            this.fields = new ArrayList();
            boolean readThumbnails2 = true;
            if (params != null && params.containsKey(SanselanConstants.PARAM_KEY_READ_THUMBNAILS)) {
                readThumbnails2 = Boolean.TRUE.equals(params.get(SanselanConstants.PARAM_KEY_READ_THUMBNAILS));
            }
            this.readThumbnails = readThumbnails2;
        }

        public boolean setTiffHeader(TiffHeader tiffHeader2) {
            this.tiffHeader = tiffHeader2;
            return true;
        }

        public boolean addDirectory(TiffDirectory directory) {
            this.directories.add(directory);
            return true;
        }

        public boolean addField(TiffField field) {
            this.fields.add(field);
            return true;
        }

        public boolean readImageData() {
            return this.readThumbnails;
        }

        public boolean readOffsetDirectories() {
            return true;
        }

        public TiffContents getContents() {
            return new TiffContents(this.tiffHeader, this.directories);
        }
    }

    private static class FirstDirectoryCollector extends Collector {
        private final boolean readImageData;

        public FirstDirectoryCollector(boolean readImageData2) {
            this.readImageData = readImageData2;
        }

        public boolean addDirectory(TiffDirectory directory) {
            super.addDirectory(directory);
            return false;
        }

        public boolean readImageData() {
            return this.readImageData;
        }
    }

    private static class DirectoryCollector extends Collector {
        private final boolean readImageData;

        public DirectoryCollector(boolean readImageData2) {
            this.readImageData = readImageData2;
        }

        public boolean addDirectory(TiffDirectory directory) {
            super.addDirectory(directory);
            return false;
        }

        public boolean readImageData() {
            return this.readImageData;
        }
    }

    public TiffContents readFirstDirectory(ByteSource byteSource, Map params, boolean readImageData, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        Collector collector = new FirstDirectoryCollector(readImageData);
        read(byteSource, params, formatCompliance, collector);
        TiffContents contents = collector.getContents();
        if (contents.directories.size() >= 1) {
            return contents;
        }
        throw new ImageReadException("Image did not contain any directories.");
    }

    public TiffContents readDirectories(ByteSource byteSource, boolean readImageData, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        Collector collector = new FirstDirectoryCollector(readImageData);
        readDirectories(byteSource, formatCompliance, collector);
        TiffContents contents = collector.getContents();
        if (contents.directories.size() >= 1) {
            return contents;
        }
        throw new ImageReadException("Image did not contain any directories.");
    }

    public TiffContents readContents(ByteSource byteSource, Map params, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        Collector collector = new Collector(params);
        read(byteSource, params, formatCompliance, collector);
        return collector.getContents();
    }

    public void read(ByteSource byteSource, Map params, FormatCompliance formatCompliance, Listener listener) throws ImageReadException, IOException {
        readDirectories(byteSource, formatCompliance, listener);
    }

    private JpegImageData getJpegRawImageData(ByteSource byteSource, TiffDirectory directory) throws ImageReadException, IOException {
        TiffDirectory.ImageDataElement element = directory.getJpegRawImageDataElement();
        int offset = element.offset;
        int length = element.length;
        if (((long) (offset + length)) == byteSource.getLength() + 1) {
            length--;
        }
        return new JpegImageData(offset, length, byteSource.getBlock(offset, length));
    }
}
