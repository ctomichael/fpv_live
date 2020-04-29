package dji.logic.album.model;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DJIAlbumDirInfo {
    public int dataLength;
    public int fileCount;
    public ArrayList<DJIAlbumFileInfo> fileInfoList = new ArrayList<>();

    public static DJIAlbumDirInfo copyFrom(@NonNull DJIAlbumDirInfo dirInfo) {
        DJIAlbumDirInfo copy = new DJIAlbumDirInfo();
        copy.fileCount = dirInfo.fileCount;
        copy.dataLength = dirInfo.dataLength;
        copy.fileInfoList.addAll(dirInfo.fileInfoList);
        return copy;
    }

    public String toString() {
        return "fileCount: " + this.fileCount + ", dataLength: " + this.dataLength + ", fileInfoList: " + this.fileInfoList;
    }
}
