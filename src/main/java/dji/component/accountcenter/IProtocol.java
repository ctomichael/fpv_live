package dji.component.accountcenter;

public interface IProtocol {
    public static final int DEFAULT_PAGESIZE = 48;
    public static final int MSG_ID_FAIL = 65537;
    public static final int MSG_ID_START = 65538;
    public static final int MSG_ID_SUCCESS = 65536;
    public static final int MSG_ID_UPDATE = 65539;
    public static final int STATUS_CODE_OK = 0;
    public static final int STATUS_CODE_OTHER = 1;
    public static final int STATUS_UNKNOWN = 255;

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

        public String toString() {
            return "ProtocolResult{arg1=" + this.arg1 + ", arg2=" + this.arg2 + ", objArg=" + this.objArg + ", mResult=" + this.mResult + '}';
        }
    }
}
