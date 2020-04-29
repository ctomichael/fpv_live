package com.mapbox.android.telemetry;

import okhttp3.MediaType;

public class FileAttachment {
    private AttachmentMetadata attachmentMetadata;
    private String filePath;
    private MediaType mediaType;

    FileAttachment(AttachmentMetadata attachmentMetadata2, String filePath2, MediaType mediaType2) {
        this.attachmentMetadata = attachmentMetadata2;
        this.filePath = filePath2;
        this.mediaType = mediaType2;
    }

    public AttachmentMetadata getAttachmentMetadata() {
        return this.attachmentMetadata;
    }

    public FileData getFileData() {
        return new FileData(this.filePath, this.mediaType);
    }
}
