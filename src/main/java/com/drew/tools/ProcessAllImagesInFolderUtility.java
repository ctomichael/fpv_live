package com.drew.tools;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.lang.StringUtil;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.drew.metadata.file.FileMetadataDirectory;
import com.drew.metadata.xmp.XmpDirectory;
import com.mapzen.android.lost.internal.Clock;
import dji.component.accountcenter.IMemberProtocol;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCAbstraction;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProcessAllImagesInFolderUtility {

    interface FileHandler {
        void onBeforeExtraction(@NotNull File file, @NotNull PrintStream printStream, @NotNull String str);

        void onExtractionError(@NotNull File file, @NotNull Throwable th, @NotNull PrintStream printStream);

        void onExtractionSuccess(@NotNull File file, @NotNull Metadata metadata, @NotNull String str, @NotNull PrintStream printStream);

        void onScanCompleted(@NotNull PrintStream printStream);

        void onStartingDirectory(@NotNull File file);

        boolean shouldProcess(@NotNull File file);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.PrintStream.<init>(java.io.OutputStream, boolean):void}
     arg types: [java.io.FileOutputStream, int]
     candidates:
      ClspMth{java.io.PrintStream.<init>(java.io.File, java.lang.String):void throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException}
      ClspMth{java.io.PrintStream.<init>(java.lang.String, java.lang.String):void throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException}
      ClspMth{java.io.PrintStream.<init>(java.io.OutputStream, boolean):void} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException} */
    public static void main(String[] args) throws IOException, JpegProcessingException {
        List<String> directories = new ArrayList<>();
        FileHandler handler = null;
        PrintStream log = System.out;
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            if (arg.equalsIgnoreCase("--text")) {
                handler = new TextFileOutputHandler();
            } else if (arg.equalsIgnoreCase("--markdown")) {
                handler = new MarkdownTableOutputHandler();
            } else if (arg.equalsIgnoreCase("--unknown")) {
                handler = new UnknownTagHandler();
            } else if (arg.equalsIgnoreCase("--log-file")) {
                if (i == args.length - 1) {
                    printUsage();
                    System.exit(1);
                }
                i++;
                log = new PrintStream((OutputStream) new FileOutputStream(args[i], false), true);
            } else {
                directories.add(arg);
            }
            i++;
        }
        if (directories.isEmpty()) {
            System.err.println("Expects one or more directories as arguments.");
            printUsage();
            System.exit(1);
        }
        if (handler == null) {
            handler = new BasicFileHandler();
        }
        long start = System.nanoTime();
        for (String directory : directories) {
            processDirectory(new File(directory), handler, "", log);
        }
        handler.onScanCompleted(log);
        System.out.println(String.format("Completed in %d ms", Long.valueOf((System.nanoTime() - start) / Clock.MS_TO_NS)));
        if (log != System.out) {
            log.close();
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println();
        System.out.println("  java com.drew.tools.ProcessAllImagesInFolderUtility [--text|--markdown|--unknown] [--log-file <file-name>]");
    }

    private static void processDirectory(@NotNull File path, @NotNull FileHandler handler, @NotNull String relativePath, PrintStream log) {
        handler.onStartingDirectory(path);
        String[] pathItems = path.list();
        if (pathItems != null) {
            Arrays.sort(pathItems);
            String[] arr$ = pathItems;
            for (String pathItem : arr$) {
                File file = new File(path, pathItem);
                if (file.isDirectory()) {
                    if (relativePath.length() != 0) {
                        pathItem = relativePath + IMemberProtocol.PARAM_SEPERATOR + pathItem;
                    }
                    processDirectory(file, handler, pathItem, log);
                } else if (handler.shouldProcess(file)) {
                    handler.onBeforeExtraction(file, log, relativePath);
                    try {
                        handler.onExtractionSuccess(file, ImageMetadataReader.readMetadata(file), relativePath, log);
                    } catch (Throwable t) {
                        handler.onExtractionError(file, t, log);
                    }
                }
            }
        }
    }

    static abstract class FileHandlerBase implements FileHandler {
        private int _errorCount = 0;
        private int _exceptionCount = 0;
        private long _processedByteCount = 0;
        private int _processedFileCount = 0;
        private final Set<String> _supportedExtensions = new HashSet(Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "ico", "webp", "pcx", "ai", "eps", "nef", "crw", "cr2", "orf", "arw", "raf", "srw", "x3f", "rw2", "rwl", "tif", "tiff", "psd", "dng"));

        FileHandlerBase() {
        }

        public void onStartingDirectory(@NotNull File directoryPath) {
        }

        public boolean shouldProcess(@NotNull File file) {
            String extension = getExtension(file);
            return extension != null && this._supportedExtensions.contains(extension.toLowerCase());
        }

        public void onBeforeExtraction(@NotNull File file, @NotNull PrintStream log, @NotNull String relativePath) {
            this._processedFileCount++;
            this._processedByteCount += file.length();
        }

        public void onExtractionError(@NotNull File file, @NotNull Throwable throwable, @NotNull PrintStream log) {
            this._exceptionCount++;
            log.printf("\t[%s] %s\n", throwable.getClass().getName(), throwable.getMessage());
        }

        public void onExtractionSuccess(@NotNull File file, @NotNull Metadata metadata, @NotNull String relativePath, @NotNull PrintStream log) {
            if (metadata.hasErrors()) {
                log.print(file);
                log.print(10);
                for (Directory directory : metadata.getDirectories()) {
                    if (directory.hasErrors()) {
                        Iterator i$ = directory.getErrors().iterator();
                        while (i$.hasNext()) {
                            log.printf("\t[%s] %s\n", directory.getName(), i$.next());
                            this._errorCount++;
                        }
                    }
                }
            }
        }

        public void onScanCompleted(@NotNull PrintStream log) {
            if (this._processedFileCount > 0) {
                log.print(String.format("Processed %,d files (%,d bytes) with %,d exceptions and %,d file errors\n", Integer.valueOf(this._processedFileCount), Long.valueOf(this._processedByteCount), Integer.valueOf(this._exceptionCount), Integer.valueOf(this._errorCount)));
            }
        }

        /* access modifiers changed from: protected */
        @Nullable
        public String getExtension(@NotNull File file) {
            String fileName = file.getName();
            int i = fileName.lastIndexOf(46);
            if (i == -1 || i == fileName.length() - 1) {
                return null;
            }
            return fileName.substring(i + 1);
        }
    }

    static class TextFileOutputHandler extends FileHandlerBase {
        private static final String NEW_LINE = "\n";

        TextFileOutputHandler() {
        }

        public void onStartingDirectory(@NotNull File directoryPath) {
            super.onStartingDirectory(directoryPath);
            File metadataDirectory = new File(directoryPath + "/metadata");
            if (metadataDirectory.exists()) {
                deleteRecursively(metadataDirectory);
            }
        }

        private static void deleteRecursively(@NotNull File directory) {
            String[] list;
            if (!directory.isDirectory()) {
                throw new IllegalArgumentException("Must be a directory.");
            }
            if (directory.exists() && (list = directory.list()) != null) {
                for (String item : list) {
                    File file = new File(item);
                    if (file.isDirectory()) {
                        deleteRecursively(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }

        public void onBeforeExtraction(@NotNull File file, @NotNull PrintStream log, @NotNull String relativePath) {
            super.onBeforeExtraction(file, log, relativePath);
            log.print(file.getAbsoluteFile());
            log.print(NEW_LINE);
        }

        public void onExtractionSuccess(@NotNull File file, @NotNull Metadata metadata, @NotNull String relativePath, @NotNull PrintStream log) {
            super.onExtractionSuccess(file, metadata, relativePath, log);
            PrintWriter writer = null;
            try {
                writer = openWriter(file);
                if (metadata.hasErrors()) {
                    for (Directory directory : metadata.getDirectories()) {
                        if (directory.hasErrors()) {
                            Iterator i$ = directory.getErrors().iterator();
                            while (i$.hasNext()) {
                                writer.format("[ERROR: %s] %s%s", directory.getName(), i$.next(), NEW_LINE);
                            }
                        }
                    }
                    writer.write(NEW_LINE);
                }
                for (Directory directory2 : metadata.getDirectories()) {
                    String directoryName = directory2.getName();
                    for (Tag tag : directory2.getTags()) {
                        String tagName = tag.getTagName();
                        String description = tag.getDescription();
                        if (description == null) {
                            description = "";
                        }
                        if ((directory2 instanceof FileMetadataDirectory) && tag.getTagType() == 3) {
                            description = "<omitted for regression testing as checkout dependent>";
                        }
                        writer.format("[%s - %s] %s = %s%s", directoryName, tag.getTagTypeHex(), tagName, description, NEW_LINE);
                    }
                    if (directory2.getTagCount() != 0) {
                        writer.write(NEW_LINE);
                    }
                    if (directory2 instanceof XmpDirectory) {
                        boolean wrote = false;
                        try {
                            XMPIterator iterator = ((XmpDirectory) directory2).getXMPMeta().iterator();
                            while (iterator.hasNext()) {
                                XMPPropertyInfo prop = (XMPPropertyInfo) iterator.next();
                                String ns = prop.getNamespace();
                                String path = prop.getPath();
                                String value = prop.getValue();
                                if (ns == null) {
                                    ns = "";
                                }
                                if (path == null) {
                                    path = "";
                                }
                                if (value == null) {
                                    value = "";
                                } else if (value.length() > 512) {
                                    value = String.format("%s <truncated from %d characters>", value.substring(0, 512), Integer.valueOf(value.length()));
                                }
                                writer.format("[XMPMeta - %s] %s = %s%s", ns, path, value, NEW_LINE);
                                wrote = true;
                            }
                        } catch (XMPException e) {
                            e.printStackTrace();
                        }
                        if (wrote) {
                            writer.write(NEW_LINE);
                        }
                    }
                }
                writeHierarchyLevel(metadata, writer, null, 0);
                writer.write(NEW_LINE);
                closeWriter(writer);
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (Throwable th) {
                closeWriter(writer);
                throw th;
            }
        }

        private static void writeHierarchyLevel(@NotNull Metadata metadata, @NotNull PrintWriter writer, @Nullable Directory parent, int level) {
            for (Directory child : metadata.getDirectories()) {
                if (parent == null) {
                    if (child.getParent() != null) {
                    }
                } else if (!parent.equals(child.getParent())) {
                }
                for (int i = 0; i < level * 4; i++) {
                    writer.write(32);
                }
                writer.write("- ");
                writer.write(child.getName());
                writer.write(NEW_LINE);
                writeHierarchyLevel(metadata, writer, child, level + 1);
            }
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onExtractionError(@com.drew.lang.annotations.NotNull java.io.File r7, @com.drew.lang.annotations.NotNull java.lang.Throwable r8, @com.drew.lang.annotations.NotNull java.io.PrintStream r9) {
            /*
                r6 = this;
                super.onExtractionError(r7, r8, r9)
                r1 = 0
                java.io.PrintWriter r1 = openWriter(r7)     // Catch:{ all -> 0x0034 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0034 }
                r2.<init>()     // Catch:{ all -> 0x0034 }
                java.lang.String r3 = "EXCEPTION: "
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x0034 }
                java.lang.String r3 = r8.getMessage()     // Catch:{ all -> 0x0034 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x0034 }
                java.lang.String r3 = "\n"
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x0034 }
                java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0034 }
                r1.write(r2)     // Catch:{ all -> 0x0034 }
                java.lang.String r2 = "\n"
                r1.write(r2)     // Catch:{ all -> 0x0034 }
                closeWriter(r1)     // Catch:{ IOException -> 0x0039 }
            L_0x0033:
                return
            L_0x0034:
                r2 = move-exception
                closeWriter(r1)     // Catch:{ IOException -> 0x0039 }
                throw r2     // Catch:{ IOException -> 0x0039 }
            L_0x0039:
                r0 = move-exception
                java.lang.String r2 = "IO exception writing metadata file: %s%s"
                r3 = 2
                java.lang.Object[] r3 = new java.lang.Object[r3]
                r4 = 0
                java.lang.String r5 = r0.getMessage()
                r3[r4] = r5
                r4 = 1
                java.lang.String r5 = "\n"
                r3[r4] = r5
                r9.printf(r2, r3)
                goto L_0x0033
            */
            throw new UnsupportedOperationException("Method not decompiled: com.drew.tools.ProcessAllImagesInFolderUtility.TextFileOutputHandler.onExtractionError(java.io.File, java.lang.Throwable, java.io.PrintStream):void");
        }

        /* JADX WARNING: Removed duplicated region for block: B:14:0x009f  */
        @com.drew.lang.annotations.NotNull
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static java.io.PrintWriter openWriter(@com.drew.lang.annotations.NotNull java.io.File r11) throws java.io.IOException {
            /*
                r10 = 1
                r9 = 0
                java.io.File r1 = new java.io.File
                java.lang.String r6 = "%s/metadata"
                java.lang.Object[] r7 = new java.lang.Object[r10]
                java.lang.String r8 = r11.getParent()
                r7[r9] = r8
                java.lang.String r6 = java.lang.String.format(r6, r7)
                r1.<init>(r6)
                boolean r6 = r1.exists()
                if (r6 != 0) goto L_0x001f
                r1.mkdir()
            L_0x001f:
                java.lang.String r6 = "%s/metadata/%s.txt"
                r7 = 2
                java.lang.Object[] r7 = new java.lang.Object[r7]
                java.lang.String r8 = r11.getParent()
                r7[r9] = r8
                java.lang.String r8 = r11.getName()
                r7[r10] = r8
                java.lang.String r2 = java.lang.String.format(r6, r7)
                java.io.OutputStreamWriter r5 = new java.io.OutputStreamWriter
                java.io.FileOutputStream r6 = new java.io.FileOutputStream
                r6.<init>(r2)
                java.lang.String r7 = "UTF-8"
                r5.<init>(r6, r7)
                java.lang.StringBuilder r6 = new java.lang.StringBuilder
                r6.<init>()
                java.lang.String r7 = "FILE: "
                java.lang.StringBuilder r6 = r6.append(r7)
                java.lang.String r7 = r11.getName()
                java.lang.StringBuilder r6 = r6.append(r7)
                java.lang.String r7 = "\n"
                java.lang.StringBuilder r6 = r6.append(r7)
                java.lang.String r6 = r6.toString()
                r5.write(r6)
                r3 = 0
                java.io.BufferedInputStream r4 = new java.io.BufferedInputStream     // Catch:{ all -> 0x009c }
                java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ all -> 0x009c }
                r6.<init>(r11)     // Catch:{ all -> 0x009c }
                r4.<init>(r6)     // Catch:{ all -> 0x009c }
                com.drew.imaging.FileType r0 = com.drew.imaging.FileTypeDetector.detectFileType(r4)     // Catch:{ all -> 0x00a3 }
                java.lang.String r6 = "TYPE: %s\n"
                r7 = 1
                java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x00a3 }
                r8 = 0
                java.lang.String r9 = r0.toString()     // Catch:{ all -> 0x00a3 }
                java.lang.String r9 = r9.toUpperCase()     // Catch:{ all -> 0x00a3 }
                r7[r8] = r9     // Catch:{ all -> 0x00a3 }
                java.lang.String r6 = java.lang.String.format(r6, r7)     // Catch:{ all -> 0x00a3 }
                r5.write(r6)     // Catch:{ all -> 0x00a3 }
                java.lang.String r6 = "\n"
                r5.write(r6)     // Catch:{ all -> 0x00a3 }
                if (r4 == 0) goto L_0x0096
                r4.close()
            L_0x0096:
                java.io.PrintWriter r6 = new java.io.PrintWriter
                r6.<init>(r5)
                return r6
            L_0x009c:
                r6 = move-exception
            L_0x009d:
                if (r3 == 0) goto L_0x00a2
                r3.close()
            L_0x00a2:
                throw r6
            L_0x00a3:
                r6 = move-exception
                r3 = r4
                goto L_0x009d
            */
            throw new UnsupportedOperationException("Method not decompiled: com.drew.tools.ProcessAllImagesInFolderUtility.TextFileOutputHandler.openWriter(java.io.File):java.io.PrintWriter");
        }

        private static void closeWriter(@Nullable Writer writer) throws IOException {
            if (writer != null) {
                writer.write("Generated using metadata-extractor\n");
                writer.write("https://drewnoakes.com/code/exif/\n");
                writer.flush();
                writer.close();
            }
        }
    }

    static class MarkdownTableOutputHandler extends FileHandlerBase {
        private final Map<String, String> _extensionEquivalence = new HashMap();
        private final Map<String, List<Row>> _rowListByExtension = new HashMap();

        static class Row {
            /* access modifiers changed from: private */
            @Nullable
            public String exifVersion;
            final File file;
            /* access modifiers changed from: private */
            @Nullable
            public String makernote;
            /* access modifiers changed from: private */
            @Nullable
            public String manufacturer;
            final Metadata metadata;
            /* access modifiers changed from: private */
            @Nullable
            public String model;
            @NotNull
            final String relativePath;
            /* access modifiers changed from: private */
            @Nullable
            public String thumbnail;

            Row(@NotNull File file2, @NotNull Metadata metadata2, @NotNull String relativePath2) {
                this.file = file2;
                this.metadata = metadata2;
                this.relativePath = relativePath2;
                ExifIFD0Directory ifd0Dir = (ExifIFD0Directory) metadata2.getFirstDirectoryOfType(ExifIFD0Directory.class);
                ExifSubIFDDirectory subIfdDir = (ExifSubIFDDirectory) metadata2.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                ExifThumbnailDirectory thumbDir = (ExifThumbnailDirectory) metadata2.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
                if (ifd0Dir != null) {
                    this.manufacturer = ifd0Dir.getDescription(271);
                    this.model = ifd0Dir.getDescription(272);
                }
                boolean hasMakernoteData = false;
                if (subIfdDir != null) {
                    this.exifVersion = subIfdDir.getDescription(ExifDirectoryBase.TAG_EXIF_VERSION);
                    hasMakernoteData = subIfdDir.containsTag(ExifDirectoryBase.TAG_MAKERNOTE);
                }
                if (thumbDir != null) {
                    Integer width = thumbDir.getInteger(256);
                    Integer height = thumbDir.getInteger(257);
                    this.thumbnail = (width == null || height == null) ? "Yes" : String.format("Yes (%s x %s)", width, height);
                }
                Iterator i$ = metadata2.getDirectories().iterator();
                while (true) {
                    if (!i$.hasNext()) {
                        break;
                    }
                    Directory directory = i$.next();
                    if (directory.getClass().getName().contains("Makernote")) {
                        this.makernote = directory.getName().replace("Makernote", "").trim();
                        break;
                    }
                }
                if (this.makernote == null) {
                    this.makernote = hasMakernoteData ? "(Unknown)" : DJIRCAbstraction.DEFAULT_FIRMWARE_VERSION;
                }
            }
        }

        public MarkdownTableOutputHandler() {
            this._extensionEquivalence.put("jpeg", "jpg");
        }

        public void onExtractionSuccess(@NotNull File file, @NotNull Metadata metadata, @NotNull String relativePath, @NotNull PrintStream log) {
            super.onExtractionSuccess(file, metadata, relativePath, log);
            String extension = getExtension(file);
            if (extension != null) {
                String extension2 = extension.toLowerCase();
                if (this._extensionEquivalence.containsKey(extension2)) {
                    extension2 = this._extensionEquivalence.get(extension2);
                }
                List<Row> list = this._rowListByExtension.get(extension2);
                if (list == null) {
                    list = new ArrayList<>();
                    this._rowListByExtension.put(extension2, list);
                }
                list.add(new Row(file, metadata, relativePath));
            }
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
         arg types: [java.lang.String, int]
         candidates:
          ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
          ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException} */
        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: ClspMth{java.io.PrintStream.<init>(java.io.OutputStream, boolean):void}
         arg types: [java.io.OutputStream, int]
         candidates:
          ClspMth{java.io.PrintStream.<init>(java.io.File, java.lang.String):void throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException}
          ClspMth{java.io.PrintStream.<init>(java.lang.String, java.lang.String):void throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException}
          ClspMth{java.io.PrintStream.<init>(java.io.OutputStream, boolean):void} */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0034  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x0039 A[SYNTHETIC, Splitter:B:21:0x0039] */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0045  */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x004a A[SYNTHETIC, Splitter:B:29:0x004a] */
        /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onScanCompleted(@com.drew.lang.annotations.NotNull java.io.PrintStream r8) {
            /*
                r7 = this;
                super.onScanCompleted(r8)
                r1 = 0
                r3 = 0
                java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x002e }
                java.lang.String r5 = "../wiki/ImageDatabaseSummary.md"
                r6 = 0
                r2.<init>(r5, r6)     // Catch:{ IOException -> 0x002e }
                java.io.PrintStream r4 = new java.io.PrintStream     // Catch:{ IOException -> 0x005a, all -> 0x0053 }
                r5 = 0
                r4.<init>(r2, r5)     // Catch:{ IOException -> 0x005a, all -> 0x0053 }
                r7.writeOutput(r4)     // Catch:{ IOException -> 0x005d, all -> 0x0056 }
                r4.flush()     // Catch:{ IOException -> 0x005d, all -> 0x0056 }
                if (r4 == 0) goto L_0x001f
                r4.close()
            L_0x001f:
                if (r2 == 0) goto L_0x0061
                r2.close()     // Catch:{ IOException -> 0x0027 }
                r3 = r4
                r1 = r2
            L_0x0026:
                return
            L_0x0027:
                r0 = move-exception
                r0.printStackTrace()
                r3 = r4
                r1 = r2
                goto L_0x0026
            L_0x002e:
                r0 = move-exception
            L_0x002f:
                r0.printStackTrace()     // Catch:{ all -> 0x0042 }
                if (r3 == 0) goto L_0x0037
                r3.close()
            L_0x0037:
                if (r1 == 0) goto L_0x0026
                r1.close()     // Catch:{ IOException -> 0x003d }
                goto L_0x0026
            L_0x003d:
                r0 = move-exception
                r0.printStackTrace()
                goto L_0x0026
            L_0x0042:
                r5 = move-exception
            L_0x0043:
                if (r3 == 0) goto L_0x0048
                r3.close()
            L_0x0048:
                if (r1 == 0) goto L_0x004d
                r1.close()     // Catch:{ IOException -> 0x004e }
            L_0x004d:
                throw r5
            L_0x004e:
                r0 = move-exception
                r0.printStackTrace()
                goto L_0x004d
            L_0x0053:
                r5 = move-exception
                r1 = r2
                goto L_0x0043
            L_0x0056:
                r5 = move-exception
                r3 = r4
                r1 = r2
                goto L_0x0043
            L_0x005a:
                r0 = move-exception
                r1 = r2
                goto L_0x002f
            L_0x005d:
                r0 = move-exception
                r3 = r4
                r1 = r2
                goto L_0x002f
            L_0x0061:
                r3 = r4
                r1 = r2
                goto L_0x0026
            */
            throw new UnsupportedOperationException("Method not decompiled: com.drew.tools.ProcessAllImagesInFolderUtility.MarkdownTableOutputHandler.onScanCompleted(java.io.PrintStream):void");
        }

        private void writeOutput(@NotNull PrintStream stream) throws IOException {
            Writer writer = new OutputStreamWriter(stream);
            writer.write("# Image Database Summary\n\n");
            for (Map.Entry<String, List<Row>> entry : this._rowListByExtension.entrySet()) {
                writer.write("## " + ((String) entry.getKey()).toUpperCase() + " Files\n\n");
                writer.write("File|Manufacturer|Model|Dir Count|Exif?|Makernote|Thumbnail|All Data\n");
                writer.write("----|------------|-----|---------|-----|---------|---------|--------\n");
                List<Row> rows = entry.getValue();
                Collections.sort(rows, new Comparator<Row>() {
                    /* class com.drew.tools.ProcessAllImagesInFolderUtility.MarkdownTableOutputHandler.AnonymousClass1 */

                    public int compare(Row o1, Row o2) {
                        int c1 = StringUtil.compare(o1.manufacturer, o2.manufacturer);
                        return c1 != 0 ? c1 : StringUtil.compare(o1.model, o2.model);
                    }
                });
                for (Row row : rows) {
                    Object[] objArr = new Object[11];
                    objArr[0] = row.file.getName();
                    objArr[1] = row.relativePath;
                    objArr[2] = StringUtil.urlEncode(row.file.getName());
                    objArr[3] = row.manufacturer == null ? "" : row.manufacturer;
                    objArr[4] = row.model == null ? "" : row.model;
                    objArr[5] = Integer.valueOf(row.metadata.getDirectoryCount());
                    objArr[6] = row.exifVersion == null ? "" : row.exifVersion;
                    objArr[7] = row.makernote == null ? "" : row.makernote;
                    objArr[8] = row.thumbnail == null ? "" : row.thumbnail;
                    objArr[9] = row.relativePath;
                    objArr[10] = StringUtil.urlEncode(row.file.getName()).toLowerCase();
                    writer.write(String.format("[%s](https://raw.githubusercontent.com/drewnoakes/metadata-extractor-images/master/%s/%s)|%s|%s|%d|%s|%s|%s|[metadata](https://raw.githubusercontent.com/drewnoakes/metadata-extractor-images/master/%s/metadata/%s.txt)\n", objArr));
                }
                writer.write(10);
            }
            writer.flush();
        }
    }

    static class UnknownTagHandler extends FileHandlerBase {
        private HashMap<String, HashMap<Integer, Integer>> _occurrenceCountByTagByDirectory = new HashMap<>();

        UnknownTagHandler() {
        }

        public void onExtractionSuccess(@NotNull File file, @NotNull Metadata metadata, @NotNull String relativePath, @NotNull PrintStream log) {
            super.onExtractionSuccess(file, metadata, relativePath, log);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (!tag.hasTagName()) {
                        HashMap<Integer, Integer> occurrenceCountByTag = this._occurrenceCountByTagByDirectory.get(directory.getName());
                        if (occurrenceCountByTag == null) {
                            occurrenceCountByTag = new HashMap<>();
                            this._occurrenceCountByTagByDirectory.put(directory.getName(), occurrenceCountByTag);
                        }
                        Integer count = (Integer) occurrenceCountByTag.get(Integer.valueOf(tag.getTagType()));
                        if (count == null) {
                            count = 0;
                            occurrenceCountByTag.put(Integer.valueOf(tag.getTagType()), 0);
                        }
                        occurrenceCountByTag.put(Integer.valueOf(tag.getTagType()), Integer.valueOf(count.intValue() + 1));
                    }
                }
            }
        }

        public void onScanCompleted(@NotNull PrintStream log) {
            super.onScanCompleted(log);
            for (Map.Entry<String, HashMap<Integer, Integer>> pair1 : this._occurrenceCountByTagByDirectory.entrySet()) {
                String directoryName = (String) pair1.getKey();
                List<Map.Entry<Integer, Integer>> counts = new ArrayList<>(((HashMap) pair1.getValue()).entrySet());
                Collections.sort(counts, new Comparator<Map.Entry<Integer, Integer>>() {
                    /* class com.drew.tools.ProcessAllImagesInFolderUtility.UnknownTagHandler.AnonymousClass1 */

                    public /* bridge */ /* synthetic */ int compare(Object x0, Object x1) {
                        return compare((Map.Entry<Integer, Integer>) ((Map.Entry) x0), (Map.Entry<Integer, Integer>) ((Map.Entry) x1));
                    }

                    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                for (Map.Entry<Integer, Integer> pair2 : counts) {
                    log.format("%s, 0x%04X, %d\n", directoryName, (Integer) pair2.getKey(), (Integer) pair2.getValue());
                }
            }
        }
    }

    static class BasicFileHandler extends FileHandlerBase {
        BasicFileHandler() {
        }

        public void onExtractionSuccess(@NotNull File file, @NotNull Metadata metadata, @NotNull String relativePath, @NotNull PrintStream log) {
            super.onExtractionSuccess(file, metadata, relativePath, log);
            for (Directory directory : metadata.getDirectories()) {
                directory.getName();
                for (Tag tag : directory.getTags()) {
                    tag.getTagName();
                    tag.getDescription();
                }
            }
        }
    }
}
