package dji.sdksharedlib.hardware.abstractions.flightcontroller.merge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKMergeGet;
import java.util.List;

@EXClassNullAway
public class MergeGetFlycParamInfo extends DJISDKMergeGet {
    private static MergeGetFlycParamInfo instance = null;

    public static synchronized MergeGetFlycParamInfo getInstance() {
        MergeGetFlycParamInfo mergeGetFlycParamInfo;
        synchronized (MergeGetFlycParamInfo.class) {
            if (instance == null) {
                instance = new MergeGetFlycParamInfo();
            }
            mergeGetFlycParamInfo = instance;
        }
        return mergeGetFlycParamInfo;
    }

    public void getInfo(String index, ParamInfoCallBack callback) {
        addCommand(new FcCommand(index, callback));
    }

    /* access modifiers changed from: protected */
    public void execute(List<Object> list) {
        int len = list.size();
        String[] indexs = new String[len];
        FcCommand[] fcCommands = new FcCommand[len];
        for (int i = 0; i < len; i++) {
            fcCommands[i] = (FcCommand) list.get(i);
            indexs[i] = fcCommands[i].index;
        }
        DataFlycGetParams.getInstance().setInfos(indexs).start(new FcCallBack(fcCommands));
    }

    public static class FcCommand {
        public ParamInfoCallBack callBack;
        public String index;

        public FcCommand(String index2, ParamInfoCallBack callBack2) {
            this.index = index2;
            this.callBack = callBack2;
        }
    }

    private class FcCallBack implements DJIDataCallBack {
        private FcCommand[] list;

        public FcCallBack(FcCommand[] list2) {
            this.list = list2;
        }

        public void onSuccess(Object model) {
            FcCommand[] fcCommandArr = this.list;
            for (FcCommand command : fcCommandArr) {
                command.callBack.onSuccess(DJIFlycParamInfoManager.read(command.index));
            }
        }

        public void onFailure(Ccode ccode) {
            for (FcCommand command : this.list) {
                command.callBack.onFailure(ccode);
            }
        }
    }
}
