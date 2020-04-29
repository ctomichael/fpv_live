package dji.sdksharedlib.hardware.abstractions.flightcontroller.merge;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.params.P3.ParamInfo;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeGetCommand;
import dji.sdksharedlib.hardware.extension.DJISDKMergeGet;
import dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public class MergeGetNewFlyParamInfo extends DJISDKMergeGet {
    private static MergeGetNewFlyParamInfo instance = null;

    public static synchronized MergeGetNewFlyParamInfo getInstance() {
        MergeGetNewFlyParamInfo mergeGetNewFlyParamInfo;
        synchronized (MergeGetNewFlyParamInfo.class) {
            if (instance == null) {
                instance = new MergeGetNewFlyParamInfo();
            }
            mergeGetNewFlyParamInfo = instance;
        }
        return mergeGetNewFlyParamInfo;
    }

    public void getInfo(String key, DJISDKCacheCommonMergeCallback callback) {
        addCommand(new DJISDKCacheCommonMergeGetCommand(key, callback));
    }

    /* access modifiers changed from: protected */
    public void execute(List<Object> list) {
        DJISDKCacheCommonMergeGetCommand[] commands = (DJISDKCacheCommonMergeGetCommand[]) list.toArray(new DJISDKCacheCommonMergeGetCommand[list.size()]);
        ArrayList<String> keys = new ArrayList<>();
        for (DJISDKCacheCommonMergeGetCommand command : commands) {
            keys.add(command.index);
        }
        DJISDKCacheProductConfigManager.getInstance().readConfig(keys, new NewFlyParamInfoMergeGetContainClass(commands));
    }

    private class NewFlyParamInfoMergeGetContainClass implements DJISDKCacheHWAbstraction.InnerCallback {
        private DJISDKCacheCommonMergeGetCommand[] newFlyParamInfoCommands;

        public NewFlyParamInfoMergeGetContainClass(DJISDKCacheCommonMergeGetCommand[] newFlyParamInfoCommands2) {
            this.newFlyParamInfoCommands = newFlyParamInfoCommands2;
        }

        public void onSuccess(Object model) {
            if (model != null) {
                DJISDKCacheCommonMergeGetCommand[] dJISDKCacheCommonMergeGetCommandArr = this.newFlyParamInfoCommands;
                for (DJISDKCacheCommonMergeGetCommand command : dJISDKCacheCommonMergeGetCommandArr) {
                    ParamInfo info = DJISDKCacheProductConfigManager.getInstance().getParamInfo(command.index);
                    if (info != null) {
                        command.callback.onSuccess(info.value);
                    }
                }
            }
        }

        public void onFails(DJIError error) {
        }
    }
}
