package dji.thirdparty.sanselan.formats.tiff.constants;

import dji.thirdparty.sanselan.formats.tiff.constants.TiffDirectoryConstants;

public class TagConstantsUtils implements TiffDirectoryConstants {
    public static TagInfo[] mergeTagLists(TagInfo[][] lists) {
        int count = 0;
        for (TagInfo[] tagInfoArr : lists) {
            count += tagInfoArr.length;
        }
        TagInfo[] result = new TagInfo[count];
        int index = 0;
        for (int i = 0; i < lists.length; i++) {
            System.arraycopy(lists[i], 0, result, index, lists[i].length);
            index += lists[i].length;
        }
        return result;
    }

    public static TiffDirectoryConstants.ExifDirectoryType getExifDirectoryType(int type) {
        for (int i = 0; i < EXIF_DIRECTORIES.length; i++) {
            if (EXIF_DIRECTORIES[i].directoryType == type) {
                return EXIF_DIRECTORIES[i];
            }
        }
        return EXIF_DIRECTORY_UNKNOWN;
    }
}
