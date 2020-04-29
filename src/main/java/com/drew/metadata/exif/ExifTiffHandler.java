package com.drew.metadata.exif;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.tiff.TiffProcessingException;
import com.drew.imaging.tiff.TiffReader;
import com.drew.lang.Charsets;
import com.drew.lang.RandomAccessReader;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.StringValue;
import com.drew.metadata.exif.makernotes.AppleMakernoteDirectory;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.exif.makernotes.CasioType1MakernoteDirectory;
import com.drew.metadata.exif.makernotes.CasioType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import com.drew.metadata.exif.makernotes.KodakMakernoteDirectory;
import com.drew.metadata.exif.makernotes.KyoceraMakernoteDirectory;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import com.drew.metadata.exif.makernotes.LeicaType5MakernoteDirectory;
import com.drew.metadata.exif.makernotes.NikonType1MakernoteDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusCameraSettingsMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusEquipmentMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusFocusInfoMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusImageProcessingMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusRawDevelopment2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusRawDevelopmentMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusRawInfoMakernoteDirectory;
import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import com.drew.metadata.exif.makernotes.PentaxMakernoteDirectory;
import com.drew.metadata.exif.makernotes.ReconyxHyperFireMakernoteDirectory;
import com.drew.metadata.exif.makernotes.ReconyxUltraFireMakernoteDirectory;
import com.drew.metadata.exif.makernotes.RicohMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SamsungType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.SanyoMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SigmaMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import com.drew.metadata.exif.makernotes.SonyType6MakernoteDirectory;
import com.drew.metadata.iptc.IptcReader;
import com.drew.metadata.tiff.DirectoryTiffHandler;
import com.drew.metadata.xmp.XmpReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class ExifTiffHandler extends DirectoryTiffHandler {
    public ExifTiffHandler(@NotNull Metadata metadata, @Nullable Directory parentDirectory) {
        super(metadata);
        if (parentDirectory != null) {
            this._currentDirectory.setParent(parentDirectory);
        }
    }

    public void setTiffMarker(int marker) throws TiffProcessingException {
        switch (marker) {
            case 42:
            case 20306:
            case 21330:
                pushDirectory(ExifIFD0Directory.class);
                return;
            case 85:
                pushDirectory(PanasonicRawIFD0Directory.class);
                return;
            default:
                throw new TiffProcessingException(String.format("Unexpected TIFF marker: 0x%X", Integer.valueOf(marker)));
        }
    }

    public boolean tryEnterSubIfd(int tagId) {
        if (tagId == 330) {
            pushDirectory(ExifSubIFDDirectory.class);
            return true;
        }
        if ((this._currentDirectory instanceof ExifIFD0Directory) || (this._currentDirectory instanceof PanasonicRawIFD0Directory)) {
            if (tagId == 34665) {
                pushDirectory(ExifSubIFDDirectory.class);
                return true;
            } else if (tagId == 34853) {
                pushDirectory(GpsDirectory.class);
                return true;
            }
        }
        if (!(this._currentDirectory instanceof ExifSubIFDDirectory) || tagId != 40965) {
            if (this._currentDirectory instanceof OlympusMakernoteDirectory) {
                switch (tagId) {
                    case 8208:
                        pushDirectory(OlympusEquipmentMakernoteDirectory.class);
                        return true;
                    case 8224:
                        pushDirectory(OlympusCameraSettingsMakernoteDirectory.class);
                        return true;
                    case OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT /*8240*/:
                        pushDirectory(OlympusRawDevelopmentMakernoteDirectory.class);
                        return true;
                    case OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT_2 /*8241*/:
                        pushDirectory(OlympusRawDevelopment2MakernoteDirectory.class);
                        return true;
                    case OlympusMakernoteDirectory.TAG_IMAGE_PROCESSING /*8256*/:
                        pushDirectory(OlympusImageProcessingMakernoteDirectory.class);
                        return true;
                    case OlympusMakernoteDirectory.TAG_FOCUS_INFO /*8272*/:
                        pushDirectory(OlympusFocusInfoMakernoteDirectory.class);
                        return true;
                    case 12288:
                        pushDirectory(OlympusRawInfoMakernoteDirectory.class);
                        return true;
                    case 16384:
                        pushDirectory(OlympusMakernoteDirectory.class);
                        return true;
                }
            }
            return false;
        }
        pushDirectory(ExifInteropDirectory.class);
        return true;
    }

    public boolean hasFollowerIfd() {
        if ((this._currentDirectory instanceof ExifIFD0Directory) || (this._currentDirectory instanceof ExifImageDirectory)) {
            if (this._currentDirectory.containsTag(ExifDirectoryBase.TAG_PAGE_NUMBER)) {
                pushDirectory(ExifImageDirectory.class);
                return true;
            }
            pushDirectory(ExifThumbnailDirectory.class);
            return true;
        } else if (!(this._currentDirectory instanceof ExifThumbnailDirectory)) {
            return false;
        } else {
            return true;
        }
    }

    @Nullable
    public Long tryCustomProcessFormat(int tagId, int formatCode, long componentCount) {
        if (formatCode == 13) {
            return Long.valueOf(4 * componentCount);
        }
        if (formatCode == 0) {
            return 0L;
        }
        return null;
    }

    public boolean customProcessTag(int tagOffset, @NotNull Set<Integer> processedIfdOffsets, int tiffHeaderOffset, @NotNull RandomAccessReader reader, int tagId, int byteCount) throws IOException {
        if (tagId == 0) {
            if (this._currentDirectory.containsTag(tagId)) {
                return false;
            }
            if (byteCount == 0) {
                return true;
            }
        }
        if (tagId == 37500 && (this._currentDirectory instanceof ExifSubIFDDirectory)) {
            return processMakernote(tagOffset, processedIfdOffsets, tiffHeaderOffset, reader);
        }
        if (tagId != 33723 || !(this._currentDirectory instanceof ExifIFD0Directory)) {
            if (tagId == 700 && (this._currentDirectory instanceof ExifIFD0Directory)) {
                new XmpReader().extract(reader.getNullTerminatedBytes(tagOffset, byteCount), this._metadata, this._currentDirectory);
                return true;
            } else if (HandlePrintIM(this._currentDirectory, tagId).booleanValue()) {
                PrintIMDirectory printIMDirectory = new PrintIMDirectory();
                printIMDirectory.setParent(this._currentDirectory);
                this._metadata.addDirectory(printIMDirectory);
                ProcessPrintIM(printIMDirectory, tagOffset, reader, byteCount);
                return true;
            } else {
                if (this._currentDirectory instanceof OlympusMakernoteDirectory) {
                    switch (tagId) {
                        case 8208:
                            pushDirectory(OlympusEquipmentMakernoteDirectory.class);
                            TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset);
                            return true;
                        case 8224:
                            pushDirectory(OlympusCameraSettingsMakernoteDirectory.class);
                            TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset);
                            return true;
                        case OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT /*8240*/:
                            pushDirectory(OlympusRawDevelopmentMakernoteDirectory.class);
                            TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset);
                            return true;
                        case OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT_2 /*8241*/:
                            pushDirectory(OlympusRawDevelopment2MakernoteDirectory.class);
                            TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset);
                            return true;
                        case OlympusMakernoteDirectory.TAG_IMAGE_PROCESSING /*8256*/:
                            pushDirectory(OlympusImageProcessingMakernoteDirectory.class);
                            TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset);
                            return true;
                        case OlympusMakernoteDirectory.TAG_FOCUS_INFO /*8272*/:
                            pushDirectory(OlympusFocusInfoMakernoteDirectory.class);
                            TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset);
                            return true;
                        case 12288:
                            pushDirectory(OlympusRawInfoMakernoteDirectory.class);
                            TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset);
                            return true;
                        case 16384:
                            pushDirectory(OlympusMakernoteDirectory.class);
                            TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset);
                            return true;
                    }
                }
                if (this._currentDirectory instanceof PanasonicRawIFD0Directory) {
                    switch (tagId) {
                        case 19:
                            PanasonicRawWbInfoDirectory dirWbInfo = new PanasonicRawWbInfoDirectory();
                            dirWbInfo.setParent(this._currentDirectory);
                            this._metadata.addDirectory(dirWbInfo);
                            ProcessBinary(dirWbInfo, tagOffset, reader, byteCount, false, 2);
                            return true;
                        case 39:
                            PanasonicRawWbInfo2Directory dirWbInfo2 = new PanasonicRawWbInfo2Directory();
                            dirWbInfo2.setParent(this._currentDirectory);
                            this._metadata.addDirectory(dirWbInfo2);
                            ProcessBinary(dirWbInfo2, tagOffset, reader, byteCount, false, 3);
                            return true;
                        case 281:
                            PanasonicRawDistortionDirectory dirDistort = new PanasonicRawDistortionDirectory();
                            dirDistort.setParent(this._currentDirectory);
                            this._metadata.addDirectory(dirDistort);
                            ProcessBinary(dirDistort, tagOffset, reader, byteCount, true, 1);
                            return true;
                    }
                }
                if (tagId == 46 && (this._currentDirectory instanceof PanasonicRawIFD0Directory)) {
                    try {
                        for (Directory directory : JpegMetadataReader.readMetadata(new ByteArrayInputStream(reader.getBytes(tagOffset, byteCount))).getDirectories()) {
                            directory.setParent(this._currentDirectory);
                            this._metadata.addDirectory(directory);
                        }
                        return true;
                    } catch (JpegProcessingException e) {
                        this._currentDirectory.addError("Error processing JpgFromRaw: " + e.getMessage());
                    } catch (IOException e2) {
                        this._currentDirectory.addError("Error reading JpgFromRaw: " + e2.getMessage());
                    }
                }
                return false;
            }
        } else if (reader.getInt8(tagOffset) != 28) {
            return false;
        } else {
            byte[] iptcBytes = reader.getBytes(tagOffset, byteCount);
            new IptcReader().extract(new SequentialByteArrayReader(iptcBytes), this._metadata, (long) iptcBytes.length, this._currentDirectory);
            return true;
        }
    }

    private static void ProcessBinary(@NotNull Directory directory, int tagValueOffset, @NotNull RandomAccessReader reader, int byteCount, Boolean issigned, int arrayLength) throws IOException {
        int i = 0;
        while (i < byteCount) {
            if (directory.hasTagName(i)) {
                if (i >= byteCount - 1 || !directory.hasTagName(i + 1)) {
                    if (issigned.booleanValue()) {
                        short[] val = new short[arrayLength];
                        for (int j = 0; j < val.length; j++) {
                            val[j] = reader.getInt16(((i + j) * 2) + tagValueOffset);
                        }
                        directory.setObjectArray(i, val);
                    } else {
                        int[] val2 = new int[arrayLength];
                        for (int j2 = 0; j2 < val2.length; j2++) {
                            val2[j2] = reader.getUInt16(((i + j2) * 2) + tagValueOffset);
                        }
                        directory.setObjectArray(i, val2);
                    }
                    i += arrayLength - 1;
                } else if (issigned.booleanValue()) {
                    directory.setObject(i, Short.valueOf(reader.getInt16((i * 2) + tagValueOffset)));
                } else {
                    directory.setObject(i, Integer.valueOf(reader.getUInt16((i * 2) + tagValueOffset)));
                }
            }
            i++;
        }
    }

    private boolean processMakernote(int makernoteOffset, @NotNull Set<Integer> processedIfdOffsets, int tiffHeaderOffset, @NotNull RandomAccessReader reader) throws IOException {
        String cameraMake;
        Directory ifd0Directory = this._metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (ifd0Directory == null) {
            cameraMake = null;
        } else {
            cameraMake = ifd0Directory.getString(271);
        }
        String firstTwoChars = reader.getString(makernoteOffset, 2, Charsets.UTF_8);
        String firstThreeChars = reader.getString(makernoteOffset, 3, Charsets.UTF_8);
        String firstFourChars = reader.getString(makernoteOffset, 4, Charsets.UTF_8);
        String firstFiveChars = reader.getString(makernoteOffset, 5, Charsets.UTF_8);
        String firstSixChars = reader.getString(makernoteOffset, 6, Charsets.UTF_8);
        String firstSevenChars = reader.getString(makernoteOffset, 7, Charsets.UTF_8);
        String firstEightChars = reader.getString(makernoteOffset, 8, Charsets.UTF_8);
        String firstNineChars = reader.getString(makernoteOffset, 9, Charsets.UTF_8);
        String firstTenChars = reader.getString(makernoteOffset, 10, Charsets.UTF_8);
        String firstTwelveChars = reader.getString(makernoteOffset, 12, Charsets.UTF_8);
        boolean byteOrderBefore = reader.isMotorolaByteOrder();
        if ("OLYMP\u0000".equals(firstSixChars) || "EPSON".equals(firstFiveChars) || "AGFA".equals(firstFourChars)) {
            pushDirectory(OlympusMakernoteDirectory.class);
            TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, tiffHeaderOffset);
        } else if ("OLYMPUS\u0000II".equals(firstTenChars)) {
            pushDirectory(OlympusMakernoteDirectory.class);
            TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 12, makernoteOffset);
        } else if (cameraMake != null && cameraMake.toUpperCase().startsWith("MINOLTA")) {
            pushDirectory(OlympusMakernoteDirectory.class);
            TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset);
        } else if (cameraMake == null || !cameraMake.trim().toUpperCase().startsWith("NIKON")) {
            if ("SONY CAM".equals(firstEightChars) || "SONY DSC".equals(firstEightChars)) {
                pushDirectory(SonyType1MakernoteDirectory.class);
                TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 12, tiffHeaderOffset);
            } else if (cameraMake != null && cameraMake.startsWith("SONY") && !Arrays.equals(reader.getBytes(makernoteOffset, 2), new byte[]{1, 0})) {
                pushDirectory(SonyType1MakernoteDirectory.class);
                TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset);
            } else if ("SEMC MS\u0000\u0000\u0000\u0000\u0000".equals(firstTwelveChars)) {
                reader.setMotorolaByteOrder(true);
                pushDirectory(SonyType6MakernoteDirectory.class);
                TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 20, tiffHeaderOffset);
            } else if ("SIGMA\u0000\u0000\u0000".equals(firstEightChars) || "FOVEON\u0000\u0000".equals(firstEightChars)) {
                pushDirectory(SigmaMakernoteDirectory.class);
                TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 10, tiffHeaderOffset);
            } else if ("KDK".equals(firstThreeChars)) {
                reader.setMotorolaByteOrder(firstSevenChars.equals("KDK INFO"));
                KodakMakernoteDirectory directory = new KodakMakernoteDirectory();
                this._metadata.addDirectory(directory);
                processKodakMakernote(directory, makernoteOffset, reader);
            } else if ("Canon".equalsIgnoreCase(cameraMake)) {
                pushDirectory(CanonMakernoteDirectory.class);
                TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset);
            } else if (cameraMake == null || !cameraMake.toUpperCase().startsWith("CASIO")) {
                if ("FUJIFILM".equals(firstEightChars) || "Fujifilm".equalsIgnoreCase(cameraMake)) {
                    reader.setMotorolaByteOrder(false);
                    int ifdStart = makernoteOffset + reader.getInt32(makernoteOffset + 8);
                    pushDirectory(FujifilmMakernoteDirectory.class);
                    TiffReader.processIfd(this, reader, processedIfdOffsets, ifdStart, makernoteOffset);
                } else if ("KYOCERA".equals(firstSevenChars)) {
                    pushDirectory(KyoceraMakernoteDirectory.class);
                    TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 22, tiffHeaderOffset);
                } else if ("LEICA".equals(firstFiveChars)) {
                    reader.setMotorolaByteOrder(false);
                    if ("LEICA\u0000\u0001\u0000".equals(firstEightChars) || "LEICA\u0000\u0004\u0000".equals(firstEightChars) || "LEICA\u0000\u0005\u0000".equals(firstEightChars) || "LEICA\u0000\u0006\u0000".equals(firstEightChars) || "LEICA\u0000\u0007\u0000".equals(firstEightChars)) {
                        pushDirectory(LeicaType5MakernoteDirectory.class);
                        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, makernoteOffset);
                    } else if ("Leica Camera AG".equals(cameraMake)) {
                        pushDirectory(LeicaMakernoteDirectory.class);
                        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, tiffHeaderOffset);
                    } else if (!"LEICA".equals(cameraMake)) {
                        return false;
                    } else {
                        pushDirectory(PanasonicMakernoteDirectory.class);
                        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, tiffHeaderOffset);
                    }
                } else if ("Panasonic\u0000\u0000\u0000".equals(reader.getString(makernoteOffset, 12, Charsets.UTF_8))) {
                    pushDirectory(PanasonicMakernoteDirectory.class);
                    TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 12, tiffHeaderOffset);
                } else if ("AOC\u0000".equals(firstFourChars)) {
                    pushDirectory(CasioType2MakernoteDirectory.class);
                    TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 6, makernoteOffset);
                } else if (cameraMake != null && (cameraMake.toUpperCase().startsWith("PENTAX") || cameraMake.toUpperCase().startsWith("ASAHI"))) {
                    pushDirectory(PentaxMakernoteDirectory.class);
                    TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, makernoteOffset);
                } else if ("SANYO\u0000\u0001\u0000".equals(firstEightChars)) {
                    pushDirectory(SanyoMakernoteDirectory.class);
                    TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, makernoteOffset);
                } else if (cameraMake == null || !cameraMake.toLowerCase().startsWith("ricoh")) {
                    if (firstTenChars.equals("Apple iOS\u0000")) {
                        boolean orderBefore = reader.isMotorolaByteOrder();
                        reader.setMotorolaByteOrder(true);
                        pushDirectory(AppleMakernoteDirectory.class);
                        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 14, makernoteOffset);
                        reader.setMotorolaByteOrder(orderBefore);
                    } else if (reader.getUInt16(makernoteOffset) == 61697) {
                        ReconyxHyperFireMakernoteDirectory directory2 = new ReconyxHyperFireMakernoteDirectory();
                        this._metadata.addDirectory(directory2);
                        processReconyxHyperFireMakernote(directory2, makernoteOffset, reader);
                    } else if (firstNineChars.equalsIgnoreCase("RECONYXUF")) {
                        ReconyxUltraFireMakernoteDirectory directory3 = new ReconyxUltraFireMakernoteDirectory();
                        this._metadata.addDirectory(directory3);
                        processReconyxUltraFireMakernote(directory3, makernoteOffset, reader);
                    } else if (!"SAMSUNG".equals(cameraMake)) {
                        return false;
                    } else {
                        pushDirectory(SamsungType2MakernoteDirectory.class);
                        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset);
                    }
                } else if (firstTwoChars.equals("Rv") || firstThreeChars.equals("Rev")) {
                    return false;
                } else {
                    if (firstFiveChars.equalsIgnoreCase("Ricoh")) {
                        reader.setMotorolaByteOrder(true);
                        pushDirectory(RicohMakernoteDirectory.class);
                        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, makernoteOffset);
                    }
                }
            } else if ("QVC\u0000\u0000\u0000".equals(firstSixChars)) {
                pushDirectory(CasioType2MakernoteDirectory.class);
                TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 6, tiffHeaderOffset);
            } else {
                pushDirectory(CasioType1MakernoteDirectory.class);
                TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset);
            }
        } else if ("Nikon".equals(firstFiveChars)) {
            switch (reader.getUInt8(makernoteOffset + 6)) {
                case 1:
                    pushDirectory(NikonType1MakernoteDirectory.class);
                    TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, tiffHeaderOffset);
                    break;
                case 2:
                    pushDirectory(NikonType2MakernoteDirectory.class);
                    TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 18, makernoteOffset + 10);
                    break;
                default:
                    this._currentDirectory.addError("Unsupported Nikon makernote data ignored.");
                    break;
            }
        } else {
            pushDirectory(NikonType2MakernoteDirectory.class);
            TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset);
        }
        reader.setMotorolaByteOrder(byteOrderBefore);
        return true;
    }

    private static Boolean HandlePrintIM(@NotNull Directory directory, int tagId) {
        if (tagId == 50341) {
            return true;
        }
        if (tagId != 3584 || (!(directory instanceof CasioType2MakernoteDirectory) && !(directory instanceof KyoceraMakernoteDirectory) && !(directory instanceof NikonType2MakernoteDirectory) && !(directory instanceof OlympusMakernoteDirectory) && !(directory instanceof PanasonicMakernoteDirectory) && !(directory instanceof PentaxMakernoteDirectory) && !(directory instanceof RicohMakernoteDirectory) && !(directory instanceof SanyoMakernoteDirectory) && !(directory instanceof SonyType1MakernoteDirectory))) {
            return false;
        }
        return true;
    }

    private static void ProcessPrintIM(@NotNull PrintIMDirectory directory, int tagValueOffset, @NotNull RandomAccessReader reader, int byteCount) throws IOException {
        boolean z;
        Boolean resetByteOrder = null;
        if (byteCount == 0) {
            directory.addError("Empty PrintIM data");
        } else if (byteCount <= 15) {
            directory.addError("Bad PrintIM data");
        } else {
            String header = reader.getString(tagValueOffset, 12, Charsets.UTF_8);
            if (!header.startsWith("PrintIM")) {
                directory.addError("Invalid PrintIM header");
                return;
            }
            int num = reader.getUInt16(tagValueOffset + 14);
            if (byteCount < (num * 6) + 16) {
                resetByteOrder = Boolean.valueOf(reader.isMotorolaByteOrder());
                if (!reader.isMotorolaByteOrder()) {
                    z = true;
                } else {
                    z = false;
                }
                reader.setMotorolaByteOrder(z);
                num = reader.getUInt16(tagValueOffset + 14);
                if (byteCount < (num * 6) + 16) {
                    directory.addError("Bad PrintIM size");
                    return;
                }
            }
            directory.setObject(0, header.substring(8, 12));
            for (int n = 0; n < num; n++) {
                int pos = tagValueOffset + 16 + (n * 6);
                directory.setObject(reader.getUInt16(pos), Long.valueOf(reader.getUInt32(pos + 2)));
            }
            if (resetByteOrder != null) {
                reader.setMotorolaByteOrder(resetByteOrder.booleanValue());
            }
        }
    }

    private static void processKodakMakernote(@NotNull KodakMakernoteDirectory directory, int tagValueOffset, @NotNull RandomAccessReader reader) {
        int dataOffset = tagValueOffset + 8;
        try {
            directory.setStringValue(0, reader.getStringValue(dataOffset, 8, Charsets.UTF_8));
            directory.setInt(9, reader.getUInt8(dataOffset + 9));
            directory.setInt(10, reader.getUInt8(dataOffset + 10));
            directory.setInt(12, reader.getUInt16(dataOffset + 12));
            directory.setInt(14, reader.getUInt16(dataOffset + 14));
            directory.setInt(16, reader.getUInt16(dataOffset + 16));
            directory.setByteArray(18, reader.getBytes(dataOffset + 18, 2));
            directory.setByteArray(20, reader.getBytes(dataOffset + 20, 4));
            directory.setInt(24, reader.getUInt16(dataOffset + 24));
            directory.setInt(27, reader.getUInt8(dataOffset + 27));
            directory.setInt(28, reader.getUInt8(dataOffset + 28));
            directory.setInt(29, reader.getUInt8(dataOffset + 29));
            directory.setInt(30, reader.getUInt16(dataOffset + 30));
            directory.setLong(32, reader.getUInt32(dataOffset + 32));
            directory.setInt(36, reader.getInt16(dataOffset + 36));
            directory.setInt(56, reader.getUInt8(dataOffset + 56));
            directory.setInt(64, reader.getUInt8(dataOffset + 64));
            directory.setInt(92, reader.getUInt8(dataOffset + 92));
            directory.setInt(93, reader.getUInt8(dataOffset + 93));
            directory.setInt(94, reader.getUInt16(dataOffset + 94));
            directory.setInt(96, reader.getUInt16(dataOffset + 96));
            directory.setInt(98, reader.getUInt16(dataOffset + 98));
            directory.setInt(100, reader.getUInt16(dataOffset + 100));
            directory.setInt(102, reader.getUInt16(dataOffset + 102));
            directory.setInt(104, reader.getUInt16(dataOffset + 104));
            directory.setInt(107, reader.getInt8(dataOffset + 107));
        } catch (IOException ex) {
            directory.addError("Error processing Kodak makernote data: " + ex.getMessage());
        }
    }

    private static void processReconyxHyperFireMakernote(@NotNull ReconyxHyperFireMakernoteDirectory directory, int makernoteOffset, @NotNull RandomAccessReader reader) throws IOException {
        Integer build;
        directory.setObject(0, Integer.valueOf(reader.getUInt16(makernoteOffset)));
        int major = reader.getUInt16(makernoteOffset + 2);
        int minor = reader.getUInt16(makernoteOffset + 2 + 2);
        int revision = reader.getUInt16(makernoteOffset + 2 + 4);
        String buildYearAndDate = String.format("%04X", Integer.valueOf(reader.getUInt16(makernoteOffset + 2 + 6))) + String.format("%04X", Integer.valueOf(reader.getUInt16(makernoteOffset + 2 + 8)));
        try {
            build = Integer.valueOf(Integer.parseInt(buildYearAndDate));
        } catch (NumberFormatException e) {
            build = null;
        }
        if (build != null) {
            directory.setString(2, String.format("%d.%d.%d.%s", Integer.valueOf(major), Integer.valueOf(minor), Integer.valueOf(revision), build));
        } else {
            directory.setString(2, String.format("%d.%d.%d", Integer.valueOf(major), Integer.valueOf(minor), Integer.valueOf(revision)));
            directory.addError("Error processing Reconyx HyperFire makernote data: build '" + buildYearAndDate + "' is not in the expected format and will be omitted from Firmware Version.");
        }
        directory.setString(12, String.valueOf((char) reader.getUInt16(makernoteOffset + 12)));
        directory.setIntArray(14, new int[]{reader.getUInt16(makernoteOffset + 14), reader.getUInt16(makernoteOffset + 14 + 2)});
        directory.setInt(18, (reader.getUInt16(makernoteOffset + 18) << 16) + reader.getUInt16(makernoteOffset + 18 + 2));
        int seconds = reader.getUInt16(makernoteOffset + 22);
        int minutes = reader.getUInt16(makernoteOffset + 22 + 2);
        int hour = reader.getUInt16(makernoteOffset + 22 + 4);
        int month = reader.getUInt16(makernoteOffset + 22 + 6);
        int day = reader.getUInt16(makernoteOffset + 22 + 8);
        int year = reader.getUInt16(makernoteOffset + 22 + 10);
        if (seconds < 0 || seconds >= 60 || minutes < 0 || minutes >= 60 || hour < 0 || hour >= 24 || month < 1 || month >= 13 || day < 1 || day >= 32 || year < 1 || year > 9999) {
            directory.addError("Error processing Reconyx HyperFire makernote data: Date/Time Original " + year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds + " is not a valid date/time.");
        } else {
            directory.setString(22, String.format("%4d:%2d:%2d %2d:%2d:%2d", Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hour), Integer.valueOf(minutes), Integer.valueOf(seconds)));
        }
        directory.setInt(36, reader.getUInt16(makernoteOffset + 36));
        directory.setInt(38, reader.getInt16(makernoteOffset + 38));
        directory.setInt(40, reader.getInt16(makernoteOffset + 40));
        directory.setStringValue(42, new StringValue(reader.getBytes(makernoteOffset + 42, 28), Charsets.UTF_16LE));
        directory.setInt(72, reader.getUInt16(makernoteOffset + 72));
        directory.setInt(74, reader.getUInt16(makernoteOffset + 74));
        directory.setInt(76, reader.getUInt16(makernoteOffset + 76));
        directory.setInt(78, reader.getUInt16(makernoteOffset + 78));
        directory.setInt(80, reader.getUInt16(makernoteOffset + 80));
        directory.setInt(82, reader.getUInt16(makernoteOffset + 82));
        directory.setDouble(84, ((double) reader.getUInt16(makernoteOffset + 84)) / 1000.0d);
        directory.setString(86, reader.getNullTerminatedString(makernoteOffset + 86, 44, Charsets.UTF_8));
    }

    private static void processReconyxUltraFireMakernote(@NotNull ReconyxUltraFireMakernoteDirectory directory, int makernoteOffset, @NotNull RandomAccessReader reader) throws IOException {
        directory.setString(0, reader.getString(makernoteOffset, 9, Charsets.UTF_8));
        directory.setString(52, reader.getString(makernoteOffset + 52, 1, Charsets.UTF_8));
        directory.setIntArray(53, new int[]{reader.getByte(makernoteOffset + 53), reader.getByte(makernoteOffset + 53 + 1)});
        byte b = reader.getByte(makernoteOffset + 59);
        byte b2 = reader.getByte(makernoteOffset + 59 + 1);
        byte b3 = reader.getByte(makernoteOffset + 59 + 2);
        byte b4 = reader.getByte(makernoteOffset + 59 + 3);
        byte b5 = reader.getByte(makernoteOffset + 59 + 4);
        directory.setInt(67, reader.getByte(makernoteOffset + 67));
        directory.setInt(72, reader.getByte(makernoteOffset + 72));
        directory.setStringValue(75, new StringValue(reader.getBytes(makernoteOffset + 75, 14), Charsets.UTF_8));
        directory.setString(80, reader.getNullTerminatedString(makernoteOffset + 80, 20, Charsets.UTF_8));
    }
}
