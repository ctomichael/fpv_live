package com.drew.metadata.xmp;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.SequentialByteArrayReader;
import com.drew.lang.SequentialReader;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.StringValue;
import java.io.IOException;
import java.util.Collections;

public class XmpReader implements JpegSegmentMetadataReader {
    @NotNull
    private static final String ATTRIBUTE_EXTENDED_XMP = "xmpNote:HasExtendedXMP";
    private static final int EXTENDED_XMP_GUID_LENGTH = 32;
    private static final int EXTENDED_XMP_INT_LENGTH = 4;
    @NotNull
    private static final String SCHEMA_XMP_NOTES = "http://ns.adobe.com/xmp/note/";
    @NotNull
    private static final String XMP_EXTENSION_JPEG_PREAMBLE = "http://ns.adobe.com/xmp/extension/\u0000";
    @NotNull
    private static final String XMP_JPEG_PREAMBLE = "http://ns.adobe.com/xap/1.0/\u0000";

    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes() {
        return Collections.singletonList(JpegSegmentType.APP1);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType) {
        int preambleLength = XMP_JPEG_PREAMBLE.length();
        int extensionPreambleLength = XMP_EXTENSION_JPEG_PREAMBLE.length();
        String extendedXMPGUID = null;
        byte[] extendedXMPBuffer = null;
        for (byte[] segmentBytes : segments) {
            if (segmentBytes.length >= preambleLength && (XMP_JPEG_PREAMBLE.equalsIgnoreCase(new String(segmentBytes, 0, preambleLength)) || "XMP".equalsIgnoreCase(new String(segmentBytes, 0, 3)))) {
                byte[] xmlBytes = new byte[(segmentBytes.length - preambleLength)];
                System.arraycopy(segmentBytes, preambleLength, xmlBytes, 0, xmlBytes.length);
                extract(xmlBytes, metadata);
                extendedXMPGUID = getExtendedXMPGUID(metadata);
            } else if (extendedXMPGUID != null && segmentBytes.length >= extensionPreambleLength && XMP_EXTENSION_JPEG_PREAMBLE.equalsIgnoreCase(new String(segmentBytes, 0, extensionPreambleLength))) {
                extendedXMPBuffer = processExtendedXMPChunk(metadata, segmentBytes, extendedXMPGUID, extendedXMPBuffer);
            }
        }
        if (extendedXMPBuffer != null) {
            extract(extendedXMPBuffer, metadata);
        }
    }

    public void extract(@NotNull byte[] xmpBytes, @NotNull Metadata metadata) {
        extract(xmpBytes, metadata, (Directory) null);
    }

    public void extract(@NotNull byte[] xmpBytes, @NotNull Metadata metadata, @Nullable Directory parentDirectory) {
        extract(xmpBytes, 0, xmpBytes.length, metadata, parentDirectory);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x001c  */
    /* JADX WARNING: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void extract(@com.drew.lang.annotations.NotNull byte[] r7, int r8, int r9, @com.drew.lang.annotations.NotNull com.drew.metadata.Metadata r10, @com.drew.lang.annotations.Nullable com.drew.metadata.Directory r11) {
        /*
            r6 = this;
            com.drew.metadata.xmp.XmpDirectory r1 = new com.drew.metadata.xmp.XmpDirectory
            r1.<init>()
            if (r11 == 0) goto L_0x000a
            r1.setParent(r11)
        L_0x000a:
            if (r8 != 0) goto L_0x0020
            int r4 = r7.length     // Catch:{ XMPException -> 0x002e }
            if (r9 != r4) goto L_0x0020
            com.adobe.xmp.XMPMeta r3 = com.adobe.xmp.XMPMetaFactory.parseFromBuffer(r7)     // Catch:{ XMPException -> 0x002e }
        L_0x0013:
            r1.setXMPMeta(r3)     // Catch:{ XMPException -> 0x002e }
        L_0x0016:
            boolean r4 = r1.isEmpty()
            if (r4 != 0) goto L_0x001f
            r10.addDirectory(r1)
        L_0x001f:
            return
        L_0x0020:
            com.adobe.xmp.impl.ByteBuffer r0 = new com.adobe.xmp.impl.ByteBuffer     // Catch:{ XMPException -> 0x002e }
            r0.<init>(r7, r8, r9)     // Catch:{ XMPException -> 0x002e }
            java.io.InputStream r4 = r0.getByteStream()     // Catch:{ XMPException -> 0x002e }
            com.adobe.xmp.XMPMeta r3 = com.adobe.xmp.XMPMetaFactory.parse(r4)     // Catch:{ XMPException -> 0x002e }
            goto L_0x0013
        L_0x002e:
            r2 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Error processing XMP data: "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = r2.getMessage()
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r1.addError(r4)
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: com.drew.metadata.xmp.XmpReader.extract(byte[], int, int, com.drew.metadata.Metadata, com.drew.metadata.Directory):void");
    }

    public void extract(@NotNull String xmpString, @NotNull Metadata metadata) {
        extract(xmpString, metadata, (Directory) null);
    }

    public void extract(@NotNull StringValue xmpString, @NotNull Metadata metadata) {
        extract(xmpString.getBytes(), metadata, (Directory) null);
    }

    public void extract(@NotNull String xmpString, @NotNull Metadata metadata, @Nullable Directory parentDirectory) {
        XmpDirectory directory = new XmpDirectory();
        if (parentDirectory != null) {
            directory.setParent(parentDirectory);
        }
        try {
            directory.setXMPMeta(XMPMetaFactory.parseFromString(xmpString));
        } catch (XMPException e) {
            directory.addError("Error processing XMP data: " + e.getMessage());
        }
        if (!directory.isEmpty()) {
            metadata.addDirectory(directory);
        }
    }

    @Nullable
    private static String getExtendedXMPGUID(@NotNull Metadata metadata) {
        for (XmpDirectory directory : metadata.getDirectoriesOfType(XmpDirectory.class)) {
            try {
                XMPIterator itr = directory.getXMPMeta().iterator("http://ns.adobe.com/xmp/note/", null, null);
                if (itr != null) {
                    while (itr.hasNext()) {
                        XMPPropertyInfo pi = (XMPPropertyInfo) itr.next();
                        if (ATTRIBUTE_EXTENDED_XMP.equals(pi.getPath())) {
                            return pi.getValue();
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            } catch (XMPException e) {
            }
        }
        return null;
    }

    @Nullable
    private static byte[] processExtendedXMPChunk(@NotNull Metadata metadata, @NotNull byte[] segmentBytes, @NotNull String extendedXMPGUID, @Nullable byte[] extendedXMPBuffer) {
        int extensionPreambleLength = XMP_EXTENSION_JPEG_PREAMBLE.length();
        int segmentLength = segmentBytes.length;
        int totalOffset = extensionPreambleLength + 32 + 4 + 4;
        if (segmentLength >= totalOffset) {
            try {
                SequentialReader reader = new SequentialByteArrayReader(segmentBytes);
                reader.skip((long) extensionPreambleLength);
                if (extendedXMPGUID.equals(reader.getString(32))) {
                    int fullLength = (int) reader.getUInt32();
                    int chunkOffset = (int) reader.getUInt32();
                    if (extendedXMPBuffer == null) {
                        extendedXMPBuffer = new byte[fullLength];
                    }
                    if (extendedXMPBuffer.length == fullLength) {
                        System.arraycopy(segmentBytes, totalOffset, extendedXMPBuffer, chunkOffset, segmentLength - totalOffset);
                    } else {
                        XmpDirectory directory = new XmpDirectory();
                        directory.addError(String.format("Inconsistent length for the Extended XMP buffer: %d instead of %d", Integer.valueOf(fullLength), Integer.valueOf(extendedXMPBuffer.length)));
                        metadata.addDirectory(directory);
                    }
                }
            } catch (IOException ex) {
                XmpDirectory directory2 = new XmpDirectory();
                directory2.addError(ex.getMessage());
                metadata.addDirectory(directory2);
            }
        }
        return extendedXMPBuffer;
    }
}
