package com.drew.imaging;

import com.drew.imaging.bmp.BmpMetadataReader;
import com.drew.imaging.gif.GifMetadataReader;
import com.drew.imaging.ico.IcoMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.pcx.PcxMetadataReader;
import com.drew.imaging.png.PngMetadataReader;
import com.drew.imaging.psd.PsdMetadataReader;
import com.drew.imaging.raf.RafMetadataReader;
import com.drew.imaging.tiff.TiffMetadataReader;
import com.drew.imaging.webp.WebpMetadataReader;
import com.drew.lang.RandomAccessStreamReader;
import com.drew.lang.StringUtil;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.file.FileMetadataReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ImageMetadataReader {
    @NotNull
    public static Metadata readMetadata(@NotNull InputStream inputStream) throws ImageProcessingException, IOException {
        return readMetadata(inputStream, -1);
    }

    @NotNull
    public static Metadata readMetadata(@NotNull InputStream inputStream, long streamLength) throws ImageProcessingException, IOException {
        BufferedInputStream bufferedInputStream = inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream);
        FileType fileType = FileTypeDetector.detectFileType(bufferedInputStream);
        if (fileType == FileType.Jpeg) {
            return JpegMetadataReader.readMetadata(bufferedInputStream);
        }
        if (fileType == FileType.Tiff || fileType == FileType.Arw || fileType == FileType.Cr2 || fileType == FileType.Nef || fileType == FileType.Orf || fileType == FileType.Rw2) {
            return TiffMetadataReader.readMetadata(new RandomAccessStreamReader(bufferedInputStream, 2048, streamLength));
        }
        if (fileType == FileType.Psd) {
            return PsdMetadataReader.readMetadata(bufferedInputStream);
        }
        if (fileType == FileType.Png) {
            return PngMetadataReader.readMetadata(bufferedInputStream);
        }
        if (fileType == FileType.Bmp) {
            return BmpMetadataReader.readMetadata(bufferedInputStream);
        }
        if (fileType == FileType.Gif) {
            return GifMetadataReader.readMetadata(bufferedInputStream);
        }
        if (fileType == FileType.Ico) {
            return IcoMetadataReader.readMetadata(bufferedInputStream);
        }
        if (fileType == FileType.Pcx) {
            return PcxMetadataReader.readMetadata(bufferedInputStream);
        }
        if (fileType == FileType.Riff) {
            return WebpMetadataReader.readMetadata(bufferedInputStream);
        }
        if (fileType == FileType.Raf) {
            return RafMetadataReader.readMetadata(bufferedInputStream);
        }
        throw new ImageProcessingException("File format is not supported");
    }

    /* JADX INFO: finally extract failed */
    @NotNull
    public static Metadata readMetadata(@NotNull File file) throws ImageProcessingException, IOException {
        InputStream inputStream = new FileInputStream(file);
        try {
            Metadata metadata = readMetadata(inputStream, file.length());
            inputStream.close();
            new FileMetadataReader().read(file, metadata);
            return metadata;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    private ImageMetadataReader() throws Exception {
        throw new Exception("Not intended for instantiation");
    }

    public static void main(@NotNull String[] args) throws MetadataException, IOException {
        String model;
        Collection<String> argList = new ArrayList<>(Arrays.asList(args));
        boolean markdownFormat = argList.remove("-markdown");
        boolean showHex = argList.remove("-hex");
        if (argList.size() < 1) {
            String version = ImageMetadataReader.class.getPackage().getImplementationVersion();
            System.out.println("metadata-extractor version " + version);
            System.out.println();
            PrintStream printStream = System.out;
            Object[] objArr = new Object[1];
            if (version == null) {
                version = "a.b.c";
            }
            objArr[0] = version;
            printStream.println(String.format("Usage: java -jar metadata-extractor-%s.jar <filename> [<filename>] [-thumb] [-markdown] [-hex]", objArr));
            System.exit(1);
        }
        for (String filePath : argList) {
            long startTime = System.nanoTime();
            File file = new File(filePath);
            if (!markdownFormat && argList.size() > 1) {
                System.out.printf("\n***** PROCESSING: %s%n%n", filePath);
            }
            Metadata metadata = null;
            try {
                metadata = readMetadata(file);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }
            long took = System.nanoTime() - startTime;
            if (!markdownFormat) {
                System.out.printf("Processed %.3f MB file in %.2f ms%n%n", Double.valueOf(((double) file.length()) / 1048576.0d), Double.valueOf(((double) took) / 1000000.0d));
            }
            if (markdownFormat) {
                String fileName = file.getName();
                String urlName = StringUtil.urlEncode(filePath);
                ExifIFD0Directory exifIFD0Directory = (ExifIFD0Directory) metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                String make = exifIFD0Directory == null ? "" : exifIFD0Directory.getString(271);
                if (exifIFD0Directory == null) {
                    model = "";
                } else {
                    model = exifIFD0Directory.getString(272);
                }
                System.out.println();
                System.out.println("---");
                System.out.println();
                System.out.printf("# %s - %s%n", make, model);
                System.out.println();
                System.out.printf("<a href=\"https://raw.githubusercontent.com/drewnoakes/metadata-extractor-images/master/%s\">%n", urlName);
                System.out.printf("<img src=\"https://raw.githubusercontent.com/drewnoakes/metadata-extractor-images/master/%s\" width=\"300\"/><br/>%n", urlName);
                System.out.println(fileName);
                System.out.println("</a>");
                System.out.println();
                System.out.println("Directory | Tag Id | Tag Name | Extracted Value");
                System.out.println(":--------:|-------:|----------|----------------");
            }
            for (Directory directory : metadata.getDirectories()) {
                String directoryName = directory.getName();
                for (Tag tag : directory.getTags()) {
                    String tagName = tag.getTagName();
                    String description = tag.getDescription();
                    if (description != null && description.length() > 1024) {
                        description = description.substring(0, 1024) + "...";
                    }
                    if (markdownFormat) {
                        System.out.printf("%s|0x%s|%s|%s%n", directoryName, Integer.toHexString(tag.getTagType()), tagName, description);
                    } else if (showHex) {
                        System.out.printf("[%s - %s] %s = %s%n", directoryName, tag.getTagTypeHex(), tagName, description);
                    } else {
                        System.out.printf("[%s] %s = %s%n", directoryName, tagName, description);
                    }
                }
                Iterator i$ = directory.getErrors().iterator();
                while (i$.hasNext()) {
                    System.err.println("ERROR: " + i$.next());
                }
            }
        }
    }
}
