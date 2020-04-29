package dji.internal;

import com.dji.frame.util.V_DiskUtil;
import com.dji.frame.util.V_FileUtil;
import com.dji.frame.util.V_JsonUtil;
import dji.common.remotecontroller.ProfessionalRC;
import dji.common.remotecontroller.RcProConfigInfo;
import dji.common.remotecontroller.RcProDefaultConfig;
import dji.common.remotecontroller.RcProUsersConfig;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataRcSetAppSpecialControl;
import dji.midware.data.model.P3.DataRcSetCustomFuction;
import dji.midware.data.model.P3.DataRcSetMCU407;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;
import dji.midware.util.ContextUtil;
import dji.sdkcache.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@EXClassNullAway
public class RcProUsersConfigHelper {
    private static final String TAG = "RcProUsersConfigHelper";
    protected static String[] defaultNames = {"User 1", "User 2", "User 3", "User 4", "User 5", "User 6"};
    private static RcProUsersConfigHelper instance = null;
    protected RcProUsersConfig data;
    private String filePath = V_DiskUtil.getExternalCacheDirPath(ContextUtil.getContext(), "/RC_PRO_CONFIG/");
    private boolean isCached = false;
    private RcProUsersConfig lastCommiteddata;
    private String rcFirmVer = "Not initial";
    protected byte[] recvData = new byte[1026];
    /* access modifiers changed from: private */
    public int retryTimes = 0;

    static /* synthetic */ int access$008(RcProUsersConfigHelper x0) {
        int i = x0.retryTimes;
        x0.retryTimes = i + 1;
        return i;
    }

    public RcProUsersConfig saveLastCommitData() {
        RcProUsersConfig rcProUsersConfig = this.data;
        this.lastCommiteddata = rcProUsersConfig;
        return rcProUsersConfig;
    }

    public void restoreToLastCommitData() {
        this.data = this.lastCommiteddata;
    }

    public static synchronized RcProUsersConfigHelper getInstance() {
        RcProUsersConfigHelper rcProUsersConfigHelper;
        synchronized (RcProUsersConfigHelper.class) {
            if (instance == null) {
                instance = new RcProUsersConfigHelper();
            }
            rcProUsersConfigHelper = instance;
        }
        return rcProUsersConfigHelper;
    }

    protected RcProUsersConfigHelper() {
        fromFile("test.json");
    }

    public RcProUsersConfig getData() {
        return this.data;
    }

    public String toFile(String filename) {
        String str = V_JsonUtil.toJson(this.data);
        if (!new File(this.filePath).mkdirs()) {
            return null;
        }
        V_FileUtil.fileWrite(this.filePath, filename, str);
        return this.filePath + filename;
    }

    public void fromFile(String filename) {
        String str;
        if (V_FileUtil.fileExist(this.filePath + filename).booleanValue()) {
            str = V_FileUtil.fileGetContent(this.filePath, filename);
        } else {
            str = V_FileUtil.rawfileGetContent(ContextUtil.getContext(), R.raw.rc_pro_user);
        }
        this.data = (RcProUsersConfig) V_JsonUtil.toBean(str, RcProUsersConfig.class);
    }

    public void fromDefaultRaw() {
        this.data = (RcProUsersConfig) V_JsonUtil.toBean(V_FileUtil.rawfileGetContent(ContextUtil.getContext(), R.raw.rc_pro_user), RcProUsersConfig.class);
        setCached(true);
    }

    public String[] getFileList() {
        File dirFile = new File(this.filePath);
        if (!dirFile.exists()) {
            return null;
        }
        return dirFile.list();
    }

    public boolean deleteFile(String filename) {
        return V_FileUtil.deleteFile(this.filePath, filename);
    }

    public String generateDefaultUsername() {
        for (int i = 0; i < defaultNames.length; i++) {
            if (find(defaultNames[i]) == -1) {
                return defaultNames[i];
            }
        }
        return defaultNames[5];
    }

    public void addUser() {
        addUserWithName(generateDefaultUsername());
    }

    public void addUserWithName(String name) {
        this.data.getUsers().add(new ProfessionalRC.ProRCUserBean(name));
        DataRcSetCustomFuction.ProCustomButton[] btnList = DataRcSetCustomFuction.ProCustomButton.values();
        int len = btnList.length;
        int[] resetValues = new int[len];
        for (int i = 0; i < len; i++) {
            resetValues[i] = ProfessionalRC.ButtonAction.NOT_DEFINED.value();
            String func = RcProDefaultConfig.getInstance().getFunc(btnList[i].toString());
            if (func != null) {
                resetValues[i] = RcProConfigInfo.getInstance().getFunctionValueByName(func);
            }
        }
        List<ProfessionalRC.ProRCConfigBean> configs = new ArrayList<>();
        configs.add(new ProfessionalRC.ProRCConfigBean(resetValues));
        configs.add(new ProfessionalRC.ProRCConfigBean(resetValues));
        configs.add(new ProfessionalRC.ProRCConfigBean(resetValues));
        ProfessionalRC.ProRCUserBean user = this.data.getUsers().get(find(name));
        user.setSingle(0);
        user.setMaster(1);
        user.setSlave(2);
        user.setConfigIndex(0);
        user.setAuto_mode(true);
        user.setConfigs(configs);
    }

    public boolean deleteUser(String username) {
        int index = find(username);
        if (index == -1 || this.data.getUsers().size() <= 1) {
            return false;
        }
        this.data.getUsers().remove(index);
        if (!this.data.getUsernameNow().equals(username)) {
            return true;
        }
        changeUser(getUsernamesWithoutNull()[0]);
        return true;
    }

    public void changeUser(String username) {
        this.data.setCurrentUsername(username);
    }

    public void changeUsername(String ori, String dest) {
        String str;
        if (find(ori) != -1 && ori != null && !"".equals(ori)) {
            if (this.data.getUsernameNow().equals(ori)) {
                if ("".equals(dest)) {
                    str = generateDefaultUsername();
                } else {
                    str = dest;
                }
                changeUser(str);
            }
            ProfessionalRC.ProRCUserBean proRCUserBean = this.data.getUsers().get(find(ori));
            if ("".equals(dest)) {
                dest = generateDefaultUsername();
            }
            proRCUserBean.setUsername(dest);
        }
    }

    public int find(String username) {
        String[] s = getUsernamesWithoutNull();
        for (int i = 0; i < s.length; i++) {
            if (s[i].equals(username)) {
                return i;
            }
        }
        return -1;
    }

    public ProfessionalRC.ProRCUserBean getUser(String username) {
        int index = find(username);
        if (index == -1 || this.data.getUsers().size() <= 0) {
            return null;
        }
        return this.data.getUsers().get(index);
    }

    public String[] getUsernames() {
        String[] s = new String[5];
        int i = 0;
        while (i < this.data.getUsers().size()) {
            s[i] = this.data.getUsers().get(i).getUsername();
            i++;
        }
        while (i < 5) {
            s[i] = "";
            i++;
        }
        return s;
    }

    public ProfessionalRC.ProRCUserBean getUserNow() {
        return this.data.getUserNow();
    }

    public String getUsernameNow() {
        return this.data.getUsernameNow();
    }

    public int getUserIndexNow() {
        return find(this.data.getUsernameNow());
    }

    public String[] getUsernamesWithoutNull() {
        List<ProfessionalRC.ProRCUserBean> list = this.data.getUsers();
        int length = list.size();
        String[] s = new String[length];
        for (int i = 0; i < length; i++) {
            s[i] = list.get(i).getUsername();
        }
        return s;
    }

    public List<ProfessionalRC.ProRCUserBean> getUsers() {
        return this.data.getUsers();
    }

    public int getConfigNow(String username) {
        return this.data.getUsers().get(find(username)).getConfigIndex();
    }

    public void changeConfig(String username, int index) {
        this.data.getUsers().get(find(username)).setConfigIndex(index);
    }

    public void changeFuncMap(String username, int configIndex, DataRcSetCustomFuction.ProCustomButton btn, int funcValue) {
        if (funcValue != -1) {
            List<ProfessionalRC.ProRCFuncMapBean> maps = this.data.getUsers().get(find(username)).getConfigs().get(configIndex).getFuncMaps();
            int i = 0;
            while (i < maps.size()) {
                if (maps.get(i).getBtnID() == btn.ordinal()) {
                    maps.get(i).setFuncValue(funcValue);
                    return;
                }
                i++;
            }
            if (i == maps.size()) {
                maps.add(new ProfessionalRC.ProRCFuncMapBean(btn.ordinal(), funcValue));
            }
        }
    }

    public int getBtnIdByFuncId(int value) {
        for (ProfessionalRC.ProRCFuncMapBean map : this.data.getUserNow().getConfigNow().getFuncMaps()) {
            if (map.getFuncValue() == value) {
                return map.getBtnID();
            }
        }
        return -1;
    }

    public synchronized void sendConfigToRcPro(DJIDataCallBack callback) {
        setMCUData(callback);
    }

    public synchronized void setMCUData(DJIDataCallBack callback) {
        sendPackage(0, callback);
    }

    /* access modifiers changed from: protected */
    public synchronized void sendPackage(final int num, final DJIDataCallBack callback) {
        byte[] sendData = this.data.toBytes();
        int sendDataLength = sendData.length;
        int from = num * 192;
        int length = from + 192 > sendDataLength ? sendDataLength - from : 192;
        final DataRcSetMCU407 setter = new DataRcSetMCU407();
        setter.clear();
        setter.setMode(DataRcSetMCU407.MODE.SET);
        setter.setData(BytesUtil.readBytes(sendData, from, length));
        setter.setOffset((short) from);
        setter.setLen((short) length);
        setter.start(new DJIDataCallBack() {
            /* class dji.internal.RcProUsersConfigHelper.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (setter.getSendDataForTest().equals(BytesUtil.byte2hex(setter.getBuf()))) {
                    if (num <= 4) {
                        RcProUsersConfigHelper.this.sendPackage(num + 1, callback);
                        return;
                    }
                    int unused = RcProUsersConfigHelper.this.retryTimes = 0;
                    RcProUsersConfigHelper.this.sendRcProSync(callback);
                } else if (RcProUsersConfigHelper.this.retryTimes <= 5) {
                    RcProUsersConfigHelper.access$008(RcProUsersConfigHelper.this);
                    RcProUsersConfigHelper.this.sendPackage(num, callback);
                } else {
                    int unused2 = RcProUsersConfigHelper.this.retryTimes = 0;
                    if (callback != null) {
                        callback.onFailure(null);
                    }
                }
            }

            public void onFailure(Ccode ccode) {
                if (RcProUsersConfigHelper.this.retryTimes <= 5) {
                    RcProUsersConfigHelper.access$008(RcProUsersConfigHelper.this);
                    RcProUsersConfigHelper.this.sendPackage(num, callback);
                    return;
                }
                int unused = RcProUsersConfigHelper.this.retryTimes = 0;
                if (callback != null) {
                    callback.onFailure(ccode);
                }
            }
        });
    }

    public synchronized void getMCUData(DJIDataCallBack callback) {
        getPackage(0, callback);
    }

    /* access modifiers changed from: protected */
    public synchronized void getPackage(final int num, final DJIDataCallBack callback) {
        final int from = num * 192;
        int length = from + 192 > this.recvData.length ? this.recvData.length - from : 192;
        DataRcSetMCU407 setter = new DataRcSetMCU407();
        setter.clear();
        setter.setMode(DataRcSetMCU407.MODE.GET);
        setter.setOffset((short) from);
        setter.setLen((short) length);
        setter.start(new DJIDataCallBack() {
            /* class dji.internal.RcProUsersConfigHelper.AnonymousClass2 */

            public void onSuccess(Object model) {
                byte[] buf = ((DataRcSetMCU407) model).getBuf();
                System.arraycopy(buf, 0, RcProUsersConfigHelper.this.recvData, from, buf.length);
                if (num <= 4) {
                    RcProUsersConfigHelper.this.getPackage(num + 1, callback);
                    return;
                }
                int unused = RcProUsersConfigHelper.this.retryTimes = 0;
                if (RcProUsersConfigHelper.this.decodeRecvData(callback)) {
                    RcProUsersConfigHelper.this.setCached(true);
                    if (callback != null) {
                        callback.onSuccess(model);
                    }
                }
            }

            public void onFailure(Ccode ccode) {
                if (RcProUsersConfigHelper.this.retryTimes <= 5) {
                    RcProUsersConfigHelper.access$008(RcProUsersConfigHelper.this);
                    RcProUsersConfigHelper.this.getPackage(num, callback);
                    return;
                }
                int unused = RcProUsersConfigHelper.this.retryTimes = 0;
                if (callback != null) {
                    callback.onFailure(ccode);
                }
            }
        });
    }

    private boolean checkData(RcProUsersConfig usersConfig) {
        if (usersConfig.getUsers() == null || usersConfig.getUsers().size() <= 0 || usersConfig.getUsers().get(0) == null || usersConfig.getUserNow() == null) {
            return false;
        }
        for (ProfessionalRC.ProRCUserBean userBean : usersConfig.getUsers()) {
            if (userBean.getUsername() == null || userBean.getUsername().length() <= 0 || userBean.getUsername().charAt(0) == 0) {
                return false;
            }
            Iterator<ProfessionalRC.ProRCConfigBean> it2 = userBean.getConfigs().iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (!checkOneConfig(it2.next())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean checkOneConfig(ProfessionalRC.ProRCConfigBean config) {
        int sum = 0;
        for (ProfessionalRC.ProRCFuncMapBean b : config.getFuncMaps()) {
            sum += b.getFuncValue();
        }
        return sum != 0;
    }

    /* access modifiers changed from: private */
    public boolean decodeRecvData(final DJIDataCallBack callback) {
        RcProUsersConfig tmp = new RcProUsersConfig();
        for (int i = 0; i < 5; i++) {
            ProfessionalRC.ProRCUserBean usr = decodeOneUsr(BytesUtil.readBytes(this.recvData, ((i * 3) + 1) * 64, 192));
            if (usr != null) {
                tmp.getUsers().add(usr);
            }
        }
        tmp.setCurrentUsername(BytesUtil.getString(this.recvData, 0, 12));
        if (!checkData(tmp)) {
            fromDefaultRaw();
            setMCUData(new DJIDataCallBack() {
                /* class dji.internal.RcProUsersConfigHelper.AnonymousClass3 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(model);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFailure(ccode);
                    }
                }
            });
            return false;
        }
        this.data = tmp;
        return true;
    }

    private ProfessionalRC.ProRCUserBean decodeOneUsr(byte[] buf) {
        boolean z = true;
        try {
            if (buf[0] == -1) {
                return null;
            }
            ProfessionalRC.ProRCUserBean usr = new ProfessionalRC.ProRCUserBean(BytesUtil.getString(buf, 0, 12));
            usr.setConfigIndex(BytesUtil.getInt(BytesUtil.readBytes(buf, 12, 1)));
            int autoSettings = BytesUtil.getInt(BytesUtil.readBytes(buf, 13, 1));
            if (((autoSettings & 64) >> 6) != 1) {
                z = false;
            }
            usr.setAuto_mode(z);
            usr.setSingle((autoSettings & 48) >> 4);
            usr.setMaster((autoSettings & 12) >> 2);
            usr.setSlave(autoSettings & 3);
            List<ProfessionalRC.ProRCConfigBean> configs = new LinkedList<>();
            for (int j = 0; j < 3; j++) {
                configs.add(new ProfessionalRC.ProRCConfigBean(BytesUtil.readBytes(buf, (j * 64) + 14, 22)));
            }
            usr.setConfigs(configs);
            return usr;
        } catch (Exception e) {
            DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
            return null;
        }
    }

    public synchronized void sendUser(final int index, final DJIDataCallBack callback) {
        byte[] sendData = this.data.toBytes();
        int from = (index * 192) + 64;
        final DataRcSetMCU407 setter = new DataRcSetMCU407();
        setter.clear();
        setter.setMode(DataRcSetMCU407.MODE.SET);
        setter.setData(BytesUtil.readBytes(sendData, from, 192));
        setter.setOffset((short) from);
        setter.setLen(192);
        setter.start(new DJIDataCallBack() {
            /* class dji.internal.RcProUsersConfigHelper.AnonymousClass4 */

            public void onSuccess(Object model) {
                if (setter.getSendDataForTest().equals(BytesUtil.byte2hex(setter.getBuf()))) {
                    int unused = RcProUsersConfigHelper.this.retryTimes = 0;
                    RcProUsersConfigHelper.this.sendCurrentConfig(callback);
                } else if (RcProUsersConfigHelper.this.retryTimes <= 5) {
                    RcProUsersConfigHelper.access$008(RcProUsersConfigHelper.this);
                    RcProUsersConfigHelper.this.sendUser(index, callback);
                } else {
                    int unused2 = RcProUsersConfigHelper.this.retryTimes = 0;
                    if (callback != null) {
                        callback.onFailure(null);
                    }
                }
            }

            public void onFailure(Ccode ccode) {
                if (RcProUsersConfigHelper.this.retryTimes <= 5) {
                    RcProUsersConfigHelper.access$008(RcProUsersConfigHelper.this);
                    RcProUsersConfigHelper.this.sendUser(index, callback);
                    return;
                }
                int unused = RcProUsersConfigHelper.this.retryTimes = 0;
                if (callback != null) {
                    callback.onFailure(ccode);
                }
            }
        });
    }

    public synchronized void sendCurrentConfig(final DJIDataCallBack callback) {
        byte[] sendData = this.data.toBytes();
        final DataRcSetMCU407 setter = new DataRcSetMCU407();
        setter.clear();
        setter.setMode(DataRcSetMCU407.MODE.SET);
        setter.setData(BytesUtil.readBytes(sendData, 0, 64));
        setter.setOffset(0);
        setter.setLen(64);
        setter.start(new DJIDataCallBack() {
            /* class dji.internal.RcProUsersConfigHelper.AnonymousClass5 */

            public void onSuccess(Object model) {
                if (setter.getSendDataForTest().equals(BytesUtil.byte2hex(setter.getBuf()))) {
                    int unused = RcProUsersConfigHelper.this.retryTimes = 0;
                    RcProUsersConfigHelper.this.sendRcProSync(callback);
                } else if (RcProUsersConfigHelper.this.retryTimes <= 5) {
                    RcProUsersConfigHelper.access$008(RcProUsersConfigHelper.this);
                    RcProUsersConfigHelper.this.sendCurrentConfig(callback);
                } else {
                    int unused2 = RcProUsersConfigHelper.this.retryTimes = 0;
                    if (callback != null) {
                        callback.onFailure(null);
                    }
                }
            }

            public void onFailure(Ccode ccode) {
                if (RcProUsersConfigHelper.this.retryTimes <= 5) {
                    RcProUsersConfigHelper.access$008(RcProUsersConfigHelper.this);
                    RcProUsersConfigHelper.this.sendCurrentConfig(callback);
                    return;
                }
                int unused = RcProUsersConfigHelper.this.retryTimes = 0;
                if (callback != null) {
                    callback.onFailure(ccode);
                }
            }
        });
    }

    public synchronized void sendRcProSync(final DJIDataCallBack callback) {
        DataRcSetAppSpecialControl setAppSpecialControl = new DataRcSetAppSpecialControl();
        setAppSpecialControl.setCmdType(DataRcSetAppSpecialControl.CmdType.RC_PRO_SYNC).setValue((byte) 0);
        setAppSpecialControl.start(new DJIDataCallBack() {
            /* class dji.internal.RcProUsersConfigHelper.AnonymousClass6 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(callback);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFailure(ccode);
                }
            }
        });
    }

    public boolean checkValidLength(String s) {
        if (s == null || s.length() > 12 || s.length() <= 0) {
            return false;
        }
        char[] carr = s.toCharArray();
        for (char c : carr) {
            if (c < ' ' || c > '~') {
                return false;
            }
        }
        return true;
    }

    public boolean isCached() {
        return this.isCached;
    }

    public void setCached(boolean cached) {
        this.isCached = cached;
    }

    public void logData() {
        DJILog.i("RcPro", BytesUtil.byte2hex(this.data.toBytes()), new Object[0]);
    }

    public String getRCFirmwareVersion() {
        return this.rcFirmVer;
    }
}
