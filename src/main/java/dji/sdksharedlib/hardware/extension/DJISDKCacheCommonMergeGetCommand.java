package dji.sdksharedlib.hardware.extension;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJISDKCacheCommonMergeGetCommand {
    public DJISDKCacheCommonMergeCallback callback;
    public String index;

    public DJISDKCacheCommonMergeGetCommand(String index2, DJISDKCacheCommonMergeCallback callBack) {
        this.index = index2;
        this.callback = callBack;
    }
}
