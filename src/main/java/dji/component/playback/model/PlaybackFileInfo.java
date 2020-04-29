package dji.component.playback.model;

import dji.component.playback.model.audio.PlaybackAudioFrequency;
import dji.component.playback.model.audio.PlaybackAudioSampleBit;
import dji.component.playback.model.audio.PlaybackAudioType;
import dji.component.playback.model.photo.PlaybackPhotoGroupType;
import dji.component.playback.model.photo.PlaybackPhotoType;
import dji.component.playback.model.video.PlaybackFileResolution;
import dji.component.playback.model.video.PlaybackVideoEncodeType;
import dji.component.playback.model.video.PlaybackVideoSubType;
import dji.component.playback.model.video.PlaybackVideoType;
import dji.midware.data.config.P3.ProductType;
import java.io.Serializable;

public class PlaybackFileInfo implements Serializable {
    public PlaybackAudioType audioType;
    public PlaybackPhotoType captureType;
    public long createTime;
    public long createTimeOrg;
    public int dataSource;
    public int duration;
    public PlaybackVideoEncodeType encodeType;
    public PlaybackExif extExif;
    public long fileGuid;
    public String fileName;
    public PlaybackFileType fileType;
    public int frameRate;
    public int frameRateScale = 0;
    public int groupNum = 3;
    public int groupResult;
    public PlaybackPhotoGroupType groupType;
    public boolean hasEXT;
    public boolean hasOrigPhoto = false;
    public int index;
    public boolean isSync = false;
    public long length;
    public int pathLength;
    public String pathStr;
    public int photoGroupId;
    public ProductType productType;
    public PlaybackFileResolution resolution;
    public int rotation;
    public PlaybackAudioSampleBit samplingBit;
    public PlaybackAudioFrequency samplingFrequency;
    public boolean starTag;
    public int subIndex;
    public PlaybackVideoSubType subVideoType;
    public PlaybackVideoType videoType;

    public PlaybackFileInfo clone() {
        PlaybackFileInfo clone = new PlaybackFileInfo();
        clone.length = this.length;
        clone.createTime = this.createTime;
        clone.createTimeOrg = this.createTimeOrg;
        clone.index = this.index;
        clone.subIndex = this.subIndex;
        clone.duration = this.duration;
        clone.rotation = this.rotation;
        clone.frameRate = this.frameRate;
        clone.resolution = this.resolution;
        clone.fileType = this.fileType;
        clone.pathLength = this.pathLength;
        clone.pathStr = this.pathStr;
        clone.hasEXT = this.hasEXT;
        clone.fileGuid = this.fileGuid;
        clone.captureType = this.captureType;
        clone.photoGroupId = this.photoGroupId;
        clone.starTag = this.starTag;
        clone.groupType = this.groupType;
        clone.groupNum = this.groupNum;
        clone.groupResult = this.groupResult;
        clone.videoType = this.videoType;
        clone.subVideoType = this.subVideoType;
        clone.encodeType = this.encodeType;
        clone.frameRateScale = this.frameRateScale;
        clone.isSync = this.isSync;
        clone.hasOrigPhoto = this.hasOrigPhoto;
        clone.extExif = this.extExif == null ? null : new PlaybackExif(this.extExif.getData());
        clone.fileName = this.fileName;
        clone.audioType = this.audioType;
        clone.samplingBit = this.samplingBit;
        clone.samplingFrequency = this.samplingFrequency;
        clone.dataSource = this.dataSource;
        clone.productType = this.productType;
        return clone;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof PlaybackFileInfo)) {
            return false;
        }
        return this.index == ((PlaybackFileInfo) obj).index && this.subIndex == ((PlaybackFileInfo) obj).subIndex;
    }

    public int hashCode() {
        return (this.index * 29) + this.subIndex;
    }
}
