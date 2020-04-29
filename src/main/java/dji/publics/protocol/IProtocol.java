package dji.publics.protocol;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IProtocol {
    public static final int CMDID_ACCOUNT_GET_FLIGHTS = 24576;
    public static final int CMDID_ALBUM_DEL_CLOUDS = 20483;
    public static final int CMDID_ALBUM_EDIT_CLOUD = 20482;
    public static final int CMDID_ALBUM_GET_CLOUDS = 20481;
    public static final int CMDID_ALBUM_GET_PAGECLOUDS = 20480;
    public static final int CMDID_PHOTO_GET_LASTEST = 4096;
    public static final int CMDID_PHOTO_GET_POPULAR = 4097;
    public static final int CMDID_PHOTO_GET_SEARCH = 4098;
    public static final int CMDID_PREVIEW_GET_COMMENTS = 12288;
    public static final int CMDID_PREVIEW_LIKE = 12290;
    public static final int CMDID_PREVIEW_LOOK = 12289;
    public static final int CMDID_PREVIEW_PRAISE = 12291;
    public static final int CMDID_VIDEO_GET_LASTEST = 8192;
    public static final int CMDID_VIDEO_GET_POPULAR = 8193;
    public static final int CMDID_VIDEO_GET_SEARCH = 8194;
    public static final int DEFAULT_PAGESIZE = 48;
    public static final int MSG_ID_FAIL = 65537;
    public static final int MSG_ID_START = 65538;
    public static final int MSG_ID_SUCCESS = 65536;
    public static final int MSG_ID_UPDATE = 65539;
    public static final int STATUS_CODE_OK = 0;
    public static final int STATUS_CODE_OTHER = 1;
    public static final int STATUS_UNKNOWN = 255;

    public interface OnDataResultListener {
        void onFail(int i, int i2, int i3, Object obj);

        void onStart(int i, boolean z, int i2, Object obj);

        void onSuccess(int i, int i2, int i3, Object obj, Object obj2);

        void onUpate(int i, long j, long j2, int i2, Object obj);
    }

    public static class ProtocolResult {
        public int arg1 = 0;
        public int arg2 = 0;
        public Object mResult = null;
        public Object objArg = null;

        public static ProtocolResult generateResult(int arg12, int arg22, Object arg, Object result) {
            ProtocolResult data = new ProtocolResult();
            data.arg1 = arg12;
            data.arg2 = arg22;
            data.objArg = arg;
            data.mResult = result;
            return data;
        }
    }
}
