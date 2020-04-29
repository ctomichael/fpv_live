package com.mapbox.android.telemetry;

public class AttachmentMetadata {
    private String created = TelemetryUtils.obtainCurrentDate();
    private String endTime;
    private String fileId;
    private String format;
    private String name;
    private String sessionId;
    private Integer size;
    private String startTime;
    private String type;

    public AttachmentMetadata(String name2, String fileId2, String format2, String type2, String sessionId2) {
        this.name = name2;
        this.fileId = fileId2;
        this.format = format2;
        this.type = type2;
        this.sessionId = sessionId2;
    }

    public void setStartTime(String startTime2) {
        this.startTime = startTime2;
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public void setSize(int size2) {
        this.size = Integer.valueOf(size2);
    }

    public String getName() {
        return this.name;
    }

    public String getCreated() {
        return this.created;
    }

    public String getFileId() {
        return this.fileId;
    }

    public String getFormat() {
        return this.format;
    }

    public String getType() {
        return this.type;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public Integer getSize() {
        return this.size;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }
}
