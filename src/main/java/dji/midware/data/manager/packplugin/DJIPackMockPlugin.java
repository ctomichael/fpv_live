package dji.midware.data.manager.packplugin;

import dji.midware.data.config.P3.DJICmdSetBase;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.manager.packplugin.MockCmdModel;
import dji.midware.data.manager.packplugin.MockStateWrapper;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.greenrobot.eventbus.EventBus;

public class DJIPackMockPlugin {
    private static final String TAG = "DJIPackMockPlugin";
    private static DJIPackMockPlugin instance = null;
    private Map<Integer, Map<Integer, Boolean>> mMockingCmds = new ConcurrentHashMap();
    private BehaviorSubject<MockStateWrapper> mStateSubject = BehaviorSubject.createDefault(new MockStateWrapper(MockStateWrapper.MockState.IDLE));

    public static synchronized DJIPackMockPlugin getInstance() {
        DJIPackMockPlugin dJIPackMockPlugin;
        synchronized (DJIPackMockPlugin.class) {
            if (instance == null) {
                instance = new DJIPackMockPlugin();
            }
            dJIPackMockPlugin = instance;
        }
        return dJIPackMockPlugin;
    }

    private DJIPackMockPlugin() {
    }

    public boolean startMock(int cmdSet, int cmdId, int senderType, int pos, List<Byte> data) {
        DataBase dataBase = DJICmdSetBase.getDataBaseObject(cmdSet, cmdId, senderType, 0);
        if (dataBase == null) {
            PackPluginLog.logWriteE(TAG, "data base parse error!!!");
            return false;
        }
        byte[] rawData = dataBase.getRecData();
        if (rawData == null) {
            PackPluginLog.logWriteE(TAG, "database recv data null");
            return false;
        }
        putMockingCmd(cmdSet, cmdId);
        byte[] changedData = replaceData(rawData, pos, data);
        dataBase.setNeedPushLosed(false);
        dataBase.setRecData(changedData);
        EventBus.getDefault().post(dataBase);
        this.mStateSubject.onNext(new MockStateWrapper(MockStateWrapper.MockState.MOCKING, MockCmdModel.Builder.aMockingCmdModel().CmdSet(cmdSet).CmdId(cmdId).StartPos(pos).MockingDatas(data).build()));
        return true;
    }

    private void putMockingCmd(int cmdSet, int cmdId) {
        this.mMockingCmds.clear();
        this.mMockingCmds.put(Integer.valueOf(cmdSet), new ConcurrentHashMap());
        this.mMockingCmds.get(Integer.valueOf(cmdSet)).put(Integer.valueOf(cmdId), true);
    }

    public boolean isCmdMocking(int cmdSet, int cmdId) {
        if (this.mMockingCmds.get(Integer.valueOf(cmdSet)) == null) {
            return false;
        }
        if (this.mMockingCmds.get(Integer.valueOf(cmdSet)).get(Integer.valueOf(cmdId)) == null) {
            return false;
        }
        return true;
    }

    public void stopMock() {
        this.mMockingCmds.clear();
        this.mStateSubject.onNext(new MockStateWrapper(MockStateWrapper.MockState.IDLE));
    }

    public Observable<MockStateWrapper> getStateSubject() {
        return this.mStateSubject.hide();
    }

    private static byte[] replaceData(byte[] rawData, int pos, List<Byte> datas) {
        byte[] tmpData = Arrays.copyOf(rawData, rawData.length);
        int i = pos;
        int j = 0;
        while (i < tmpData.length && j < datas.size()) {
            tmpData[i] = datas.get(j).byteValue();
            i++;
            j++;
        }
        return tmpData;
    }
}
