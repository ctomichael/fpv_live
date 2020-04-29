package dji.component.mediaprovider;

import android.content.Context;
import android.net.Uri;
import com.amap.location.common.model.AmapLoc;
import dji.component.accountcenter.IMemberProtocol;

public final class DJIMediaStore {
    public static String AUTHORITY;
    /* access modifiers changed from: private */
    public static String CONTENT_AUTHORITY_SLASH;

    public interface FileColumns {
        public static final String AUDIO_TYPE = "audio_type";
        public static final String CAPTURE_TYPE = "capture_type";
        public static final String CREATE_TIME = "create_time";
        public static final String CREATE_TIME_ORG = "create_time_org";
        public static final String DATA_SOURCE = "data_source";
        public static final String DURATION = "duration";
        public static final String ENCODE_TYPE = "encode_type";
        public static final String EXT_EXIF = "ext_exif";
        public static final String FILE_GUID = "file_guid";
        public static final String FILE_NAME = "file_name";
        public static final String FILE_NAME_OLD = "file_name_old";
        public static final String FILE_PATH = "file_path";
        public static final String FILE_PATH_HD = "file_path_hd";
        public static final String FILE_PRODUCT_TYPE = "file_product_type";
        public static final String FILE_TYPE = "file_type";
        public static final String FRAME_RATE = "frame_rate";
        public static final String FRAME_RATE_SCALE = "frame_rate_scale";
        public static final String GROUP_NUM = "group_num";
        public static final String GROUP_RESULT = "group_result";
        public static final String GROUP_TYPE = "group_type";
        public static final String HAS_EXT = "has_ext";
        public static final String HAS_ORI_PHOTO = "has_ori_photo";
        public static final String HEIGHT = "height";
        public static final String IS_SYNC = "is_sync";
        public static final String LAST_MODIFY = "last_modify";
        public static final String LENGTH = "length";
        public static final String MEDIA_TYPE = "media_type";
        public static final int MEDIA_TYPE_AUDIO = 2;
        public static final int MEDIA_TYPE_IMAGE = 1;
        public static final int MEDIA_TYPE_NONE = 0;
        public static final int MEDIA_TYPE_VIDEO = 3;
        public static final String PATH_LENGTH = "path_length";
        public static final String PHOTO_GROUP_ID = "photo_group_id";
        public static final String RESOLUTION = "resolution";
        public static final String ROTATION = "rotation";
        public static final String SAMPLING_BIT = "sampling_bit";
        public static final String SAMPLING_FREQUENCY = "sampling_frequency";
        public static final String SAMPLING_TYPE = "sampling_type";
        public static final String STAR_TAG = "star_tag";
        public static final String SUB_INDEX = "sub_index";
        public static final String SUB_VIDEO_TYPE = "sub_video_type";
        public static final String VIDEO_TYPE = "video_type";
        public static final String WIDTH = "width";
        public static final String _ID = "_id";
        public static final String _INDEX = "_index";
    }

    public static void initAuthority(Context context) {
        AUTHORITY = context.getPackageName() + ".provider.media";
        CONTENT_AUTHORITY_SLASH = "content://" + AUTHORITY + IMemberProtocol.PARAM_SEPERATOR;
    }

    public static final class Files {
        public static Uri getContentUri() {
            return Uri.parse(DJIMediaStore.CONTENT_AUTHORITY_SLASH + AmapLoc.TYPE_OFFLINE_CELL);
        }

        public static final Uri getContentUri(long rowId) {
            return Uri.parse(DJIMediaStore.CONTENT_AUTHORITY_SLASH + "file/" + rowId);
        }
    }

    public static final class Video {
        public static Uri getContentUri() {
            return Uri.parse(DJIMediaStore.CONTENT_AUTHORITY_SLASH + "video");
        }

        public static final Uri getContentUri(long rowId) {
            return Uri.parse(DJIMediaStore.CONTENT_AUTHORITY_SLASH + "video/" + rowId);
        }
    }

    public static final class Image {
        public static Uri getContentUri() {
            return Uri.parse(DJIMediaStore.CONTENT_AUTHORITY_SLASH + "image");
        }

        public static final Uri getContentUri(long rowId) {
            return Uri.parse(DJIMediaStore.CONTENT_AUTHORITY_SLASH + "image/" + rowId);
        }
    }
}
