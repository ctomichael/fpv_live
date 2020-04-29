package dji.sdksharedlib.hardware.extension;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJISDKCacheCommonMergeSetCommand {
    public DJISDKCacheCommonMergeCallback callback;
    public String index;
    public Number value;

    public DJISDKCacheCommonMergeSetCommand(String index2, Number value2, DJISDKCacheCommonMergeCallback callBack) {
        this.index = index2;
        this.value = value2;
        this.callback = callBack;
    }
}
