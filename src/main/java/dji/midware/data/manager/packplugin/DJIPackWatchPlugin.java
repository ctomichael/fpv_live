package dji.midware.data.manager.packplugin;

import dji.log.DJILog;
import dji.midware.data.manager.packplugin.WatchCmdModel;
import dji.midware.data.manager.packplugin.WatchStateWrapper;
import dji.midware.data.manager.packplugin.record.DJIPackRecordPlugin;
import dji.midware.data.packages.P3.Pack;
import dji.midware.util.BytesUtil;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import java.util.ArrayList;
import java.util.List;

public class DJIPackWatchPlugin {
    private static final String TAG = "DJIPackWatchPlugin";
    private static DJIPackWatchPlugin instance = null;
    private BehaviorSubject<WatchStateWrapper> mStateSubject = BehaviorSubject.createDefault(new WatchStateWrapper(WatchStateWrapper.WatchState.IDLE));

    public static synchronized DJIPackWatchPlugin getInstance() {
        DJIPackWatchPlugin dJIPackWatchPlugin;
        synchronized (DJIPackWatchPlugin.class) {
            if (instance == null) {
                instance = new DJIPackWatchPlugin();
            }
            dJIPackWatchPlugin = instance;
        }
        return dJIPackWatchPlugin;
    }

    public void startWatch(int cmdSet, int cmdId, int pos, int length, int senderType, DJIPackRecordPlugin.PackType4Plugin packType) {
        this.mStateSubject.onNext(new WatchStateWrapper(WatchCmdModel.Builder.aWatchCmdModel().CmdSet(cmdSet).CmdId(cmdId).StartPos(pos).Length(length).SenderType(senderType).PackType(packType).build(), WatchStateWrapper.WatchState.WATCHING));
    }

    public void onCmdCome(Pack pack, DJIPackRecordPlugin.PackType4Plugin packType) {
        WatchCmdModel cmdModel = this.mStateSubject.getValue().getWatchCmd();
        if (cmdModel.getCmdSet() != pack.cmdSet || cmdModel.getCmdId() != pack.cmdId || cmdModel.getPackType() != packType) {
            return;
        }
        if (cmdModel.getSenderType() != -1 && cmdModel.getSenderType() != pack.senderType) {
            return;
        }
        if (cmdModel.getStartPos() >= pack.data.length) {
            DJILog.logWriteE(TAG, "start pos not right, data length: " + pack.data.length, new Object[0]);
            return;
        }
        DJILog.i(TAG, "watch cmdset : " + pack.cmdSet + ", cmdid: " + pack.cmdId + ", bytes: " + BytesUtil.byte2hex(pack.data), new Object[0]);
        cmdModel.setWatchBytes(getDataFromArray(pack.data, cmdModel.getStartPos(), cmdModel.getLength()));
        DJILog.i(TAG, "watch bytes: " + cmdModel.getWatchBytes(), new Object[0]);
        this.mStateSubject.onNext(this.mStateSubject.getValue());
    }

    public void stopWatch() {
        this.mStateSubject.onNext(new WatchStateWrapper(WatchStateWrapper.WatchState.IDLE));
    }

    private List<Byte> getDataFromArray(byte[] datas, int pos, int length) {
        List<Byte> retData = new ArrayList<>();
        if (datas != null && datas.length > pos) {
            int i = pos;
            int j = 0;
            while (i < datas.length && j < length) {
                retData.add(Byte.valueOf(datas[i]));
                i++;
                j++;
            }
        }
        return retData;
    }

    public Observable<WatchStateWrapper> getStateSubject() {
        return this.mStateSubject.hide();
    }
}
