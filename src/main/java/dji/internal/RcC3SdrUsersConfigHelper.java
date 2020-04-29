package dji.internal;

import android.text.TextUtils;
import dji.common.remotecontroller.ProfessionalRC;
import dji.common.remotecontroller.RcProUsersConfig;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataC3SdrGetUsers;
import dji.midware.data.model.P3.DataC3SdrSetUsers;
import dji.midware.data.model.P3.DataRcSetCustomFuction;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RcC3SdrUsersConfigHelper extends RcProUsersConfigHelper {
    private static final int DATA_LEN = 312;
    private static final int ERR_COUNT = 4;
    private static final int USER_CONFIG_LEN = 62;
    private static final int USER_COUNT = 5;
    /* access modifiers changed from: private */
    public final String TAG;

    public static synchronized RcC3SdrUsersConfigHelper getInstance() {
        RcC3SdrUsersConfigHelper access$000;
        synchronized (RcC3SdrUsersConfigHelper.class) {
            access$000 = Holder.INSTANCE;
        }
        return access$000;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static RcC3SdrUsersConfigHelper INSTANCE = new RcC3SdrUsersConfigHelper();

        private Holder() {
        }
    }

    private RcC3SdrUsersConfigHelper() {
        this.TAG = RcC3SdrUsersConfigHelper.class.getSimpleName();
    }

    public void fromDefaultRaw() {
    }

    public void reset(final DJIDataCallBack callback) {
        byte[] sendData = usersConfig2Bytes(true);
        DataC3SdrSetUsers setter = new DataC3SdrSetUsers();
        setter.setUsersData(sendData);
        setter.start(new DJIDataCallBack() {
            /* class dji.internal.RcC3SdrUsersConfigHelper.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    RcC3SdrUsersConfigHelper.this.getPackage(0, callback);
                }
            }

            public void onFailure(Ccode ccode) {
                DJILog.e(RcC3SdrUsersConfigHelper.this.TAG, "send error " + ccode, new Object[0]);
                RcC3SdrUsersConfigHelper.this.callbackOnFailure(callback, ccode);
            }
        });
    }

    public synchronized void sendPackage(final int tryCnt, final DJIDataCallBack callback) {
        if (tryCnt > 4) {
            callbackOnFailure(callback, Ccode.SET_PARAM_FAILED);
        } else {
            byte[] sendData = usersConfig2Bytes(false);
            if (checkSendDataLen(sendData)) {
                DataC3SdrSetUsers setter = new DataC3SdrSetUsers();
                setter.setUsersData(sendData);
                setter.start(new DJIDataCallBack() {
                    /* class dji.internal.RcC3SdrUsersConfigHelper.AnonymousClass2 */

                    public void onSuccess(Object model) {
                        RcC3SdrUsersConfigHelper.this.callbackOnSuccess(callback, model);
                    }

                    public void onFailure(Ccode ccode) {
                        DJILog.e(RcC3SdrUsersConfigHelper.this.TAG, "send error " + ccode, new Object[0]);
                        RcC3SdrUsersConfigHelper.this.sendPackage(tryCnt + 1, callback);
                    }
                });
            } else {
                sendPackage(tryCnt + 1, callback);
            }
        }
    }

    public synchronized void getPackage(final int tryCnt, final DJIDataCallBack callback) {
        if (tryCnt > 4) {
            callbackOnFailure(callback, Ccode.GET_PARAM_FAILED);
        } else {
            DataC3SdrGetUsers getter = new DataC3SdrGetUsers();
            getter.clear();
            getter.start(new DJIDataCallBack() {
                /* class dji.internal.RcC3SdrUsersConfigHelper.AnonymousClass3 */

                public void onSuccess(Object model) {
                    byte[] buf = ((DataC3SdrGetUsers) model).getRecData();
                    if (buf == null || buf.length != RcC3SdrUsersConfigHelper.DATA_LEN) {
                        RcC3SdrUsersConfigHelper.this.getPackage(tryCnt + 1, callback);
                        return;
                    }
                    System.arraycopy(buf, 0, RcC3SdrUsersConfigHelper.this.recvData, 0, buf.length);
                    if (RcC3SdrUsersConfigHelper.this.decodeRecvData()) {
                        RcC3SdrUsersConfigHelper.this.callbackOnSuccess(callback, model);
                    } else {
                        RcC3SdrUsersConfigHelper.this.getPackage(tryCnt + 1, callback);
                    }
                }

                public void onFailure(Ccode ccode) {
                    DJILog.e(RcC3SdrUsersConfigHelper.this.TAG, "getData fail!" + ccode, new Object[0]);
                    RcC3SdrUsersConfigHelper.this.getPackage(tryCnt + 1, callback);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void callbackOnSuccess(DJIDataCallBack callBack, Object model) {
        if (callBack != null) {
            callBack.onSuccess(model);
        }
    }

    /* access modifiers changed from: private */
    public void callbackOnFailure(DJIDataCallBack callBack, Ccode ccode) {
        if (callBack != null) {
            callBack.onFailure(ccode);
        }
    }

    /* access modifiers changed from: private */
    public boolean decodeRecvData() {
        byte b = this.recvData[1];
        RcProUsersConfig tmp = new RcProUsersConfig();
        for (int i = 0; i < 5; i++) {
            tmp.getUsers().add(decodeUser(BytesUtil.readBytes(this.recvData, (i * 62) + 2, 62)));
        }
        List<ProfessionalRC.ProRCUserBean> users = tmp.getUsers();
        if (b < users.size()) {
            tmp.setCurrentUsername(users.get(b).getUsername());
        }
        if (!checkUserData(tmp)) {
            DJILog.e("Cendence", "Data Error, Reinitialized", new Object[0]);
            return false;
        }
        this.data = tmp;
        return true;
    }

    private ProfessionalRC.ProRCUserBean decodeUser(byte[] buf) {
        boolean z = false;
        String usrname = BytesUtil.getString(buf, 0, 12);
        DJILog.i(this.TAG, "username =" + usrname, new Object[0]);
        ProfessionalRC.ProRCUserBean usr = new ProfessionalRC.ProRCUserBean(usrname);
        usr.setConfigIndex(BytesUtil.getInt(BytesUtil.readBytes(buf, 12, 1)));
        int autoSettings = BytesUtil.getInt(BytesUtil.readBytes(buf, 13, 1));
        if (((autoSettings & 64) >> 6) == 1) {
            z = true;
        }
        usr.setAuto_mode(z);
        usr.setSingle((autoSettings & 48) >> 4);
        usr.setMaster((autoSettings & 12) >> 2);
        usr.setSlave(autoSettings & 3);
        List<ProfessionalRC.ProRCConfigBean> configs = new ArrayList<>(1);
        configs.add(new C3SDRConfigBean(BytesUtil.readBytes(buf, 14, 48)));
        usr.setConfigs(configs);
        return usr;
    }

    private boolean checkUserData(RcProUsersConfig usersConfig) {
        if (usersConfig.getUsers() == null || usersConfig.getUsers().size() <= 0 || usersConfig.getUsers().get(0) == null || usersConfig.getUserNow() == null) {
            return false;
        }
        for (int i = 0; i < usersConfig.getUsers().size(); i++) {
            ProfessionalRC.ProRCUserBean userBean = usersConfig.getUsers().get(i);
            if (TextUtils.isEmpty(userBean.getUsername()) || userBean.getUsername().charAt(0) == 0) {
                userBean.setUsername(defaultNames[i]);
            }
            for (ProfessionalRC.ProRCConfigBean configBean : userBean.getConfigs()) {
                if (!checkOneConfig(configBean)) {
                    return false;
                }
            }
        }
        return true;
    }

    public synchronized void sendCurrentConfig(DJIDataCallBack callback) {
        sendPackage(0, callback);
    }

    private boolean checkSendDataLen(byte[] sendData) {
        return sendData != null && sendData.length == DATA_LEN;
    }

    private byte[] usersConfig2Bytes(boolean reset) {
        int i;
        List<ProfessionalRC.ProRCUserBean> users = this.data.getUsers();
        byte[] usersConfig = new byte[((users.size() * 62) + 2)];
        int curIndex = this.data.getCurUserIndex();
        if (reset) {
            i = 1;
        } else {
            i = 0;
        }
        usersConfig[0] = (byte) i;
        if (curIndex < 0) {
            curIndex = 0;
        }
        usersConfig[1] = (byte) curIndex;
        for (int i2 = 0; i2 < users.size(); i2++) {
            System.arraycopy(user2Bytes(users.get(i2)), 0, usersConfig, (i2 * 62) + 2, 62);
        }
        return usersConfig;
    }

    private byte[] user2Bytes(ProfessionalRC.ProRCUserBean user) {
        byte[] headers = user.getUserInfoHeader();
        byte[] config = config2Bytes((C3SDRConfigBean) user.getConfigs().get(0));
        byte[] userData = new byte[62];
        if (headers.length + config.length > 62) {
            DJILog.e(this.TAG, "user 2 bytes error", new Object[0]);
            return new byte[0];
        }
        System.arraycopy(headers, 0, userData, 0, headers.length);
        System.arraycopy(config, 0, userData, headers.length, config.length);
        for (int i = (headers.length + config.length) - 1; i < userData.length; i++) {
            userData[i] = -1;
        }
        return userData;
    }

    private byte[] config2Bytes(C3SDRConfigBean configBean) {
        List<ProfessionalRC.ProRCFuncMapBean> funcBeans = configBean.getFuncMaps();
        List<ProfessionalRC.ProRCFuncMapBean> extraMap = configBean.getExtraMap();
        byte[] configs = new byte[(funcBeans.size() + extraMap.size())];
        for (int i = 0; i < funcBeans.size(); i++) {
            configs[i] = (byte) funcBeans.get(i).getFuncValue();
        }
        int funcSize = funcBeans.size();
        for (int i2 = 0; i2 < extraMap.size(); i2++) {
            configs[i2 + funcSize] = (byte) extraMap.get(i2).getFuncValue();
        }
        return configs;
    }

    public static class C3SDRConfigBean extends ProfessionalRC.ProRCConfigBean {
        private List<ProfessionalRC.ProRCFuncMapBean> extraMap;

        public C3SDRConfigBean(byte[] values) {
            super(BytesUtil.readBytes(values, 0, 22));
            this.extraMap = extraBtns(BytesUtil.readBytes(values, 22, 26));
        }

        private List<ProfessionalRC.ProRCFuncMapBean> extraBtns(byte[] btnBuf) {
            int btnIdStart = DataRcSetCustomFuction.ProCustomButton.values().length;
            DataRcSetCustomFuction.C3SDRExtCustomButton[] extrasBtns = DataRcSetCustomFuction.C3SDRExtCustomButton.values();
            List<ProfessionalRC.ProRCFuncMapBean> extrasMaps = new LinkedList<>();
            for (int i = 0; i < extrasBtns.length; i++) {
                extrasMaps.add(new ProfessionalRC.ProRCFuncMapBean(extrasBtns[i].ordinal() + btnIdStart, btnBuf[i]));
            }
            return extrasMaps;
        }

        public List<ProfessionalRC.ProRCFuncMapBean> getExtraMap() {
            return this.extraMap;
        }
    }
}
