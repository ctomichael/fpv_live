package dji.sdksharedlib.hardware.abstractions.flightcontroller.merge;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeSetCommand;
import dji.sdksharedlib.hardware.extension.DJISDKMergeGet;
import dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public class MergeSetFlyParamInfo extends DJISDKMergeGet {
    public void getInfo(String key, Number value, DJISDKCacheCommonMergeCallback callback) {
        addCommand(new DJISDKCacheCommonMergeSetCommand(key, value, callback));
    }

    /* access modifiers changed from: protected */
    public void execute(List<Object> list) {
        DJISDKCacheCommonMergeSetCommand[] commands = (DJISDKCacheCommonMergeSetCommand[]) list.toArray(new DJISDKCacheCommonMergeSetCommand[list.size()]);
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<Number> values = new ArrayList<>();
        for (DJISDKCacheCommonMergeSetCommand command : commands) {
            keys.add(command.index);
            values.add(command.value);
        }
        DJISDKCacheProductConfigManager.getInstance().writeConfig(keys, values, new NewFlyParamInfoMergeSetContainClass(commands));
    }

    private class NewFlyParamInfoMergeSetContainClass implements DJISDKCacheHWAbstraction.InnerCallback {
        private DJISDKCacheCommonMergeSetCommand[] newFlyParamInfoCommands;

        public NewFlyParamInfoMergeSetContainClass(DJISDKCacheCommonMergeSetCommand[] newFlyParamInfoCommands2) {
            this.newFlyParamInfoCommands = newFlyParamInfoCommands2;
        }

        public void onSuccess(Object model) {
            if (model != null) {
                DJISDKCacheCommonMergeSetCommand[] dJISDKCacheCommonMergeSetCommandArr = this.newFlyParamInfoCommands;
                for (DJISDKCacheCommonMergeSetCommand command : dJISDKCacheCommonMergeSetCommandArr) {
                    if (command.callback != null) {
                        command.callback.onSuccess(null);
                    }
                }
            }
        }

        public void onFails(DJIError error) {
        }
    }
}
