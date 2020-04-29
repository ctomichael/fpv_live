package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import android.os.Handler;
import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.remotecontroller.ProfessionalRC;
import dji.common.remotecontroller.RCMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.RcProUsersConfigHelper;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.data.model.P3.DataRcGetPushRcProCustomButtonsStatus;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.Iterator;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIRCProfessionalAbstraction extends DJIRCInspire2Abstraction {
    private static final int MAX_RETRY_TIME = 4;
    private static final int MAX_USER_SIZE = 5;
    private static final int MIN_USER_SIZE = 1;
    private static final long RETRY_PERIOD = 500;
    private static final String TAG = "DJIRCProfessionalAbstraction";
    private final boolean commitImmediately = true;
    /* access modifiers changed from: private */
    public String defaultUserName;
    /* access modifiers changed from: private */
    public Handler handler = new Handler(BackgroundLooper.getLooper());
    protected RcProUsersConfigHelper helper = RcProUsersConfigHelper.getInstance();
    /* access modifiers changed from: private */
    public boolean initSuccess = false;
    private final boolean isIndexBoundedWithRCMode = true;
    private int leftDialValue = -1;
    private int leftWheelValue = -1;
    private RCMode rcMode;
    /* access modifiers changed from: private */
    public int retryInitTime = 1;
    private int rightDialValue = -1;
    private int rightWheelValue = -1;

    static /* synthetic */ int access$208(DJIRCProfessionalAbstraction x0) {
        int i = x0.retryInitTime;
        x0.retryInitTime = i + 1;
        return i;
    }

    public DJIRCProfessionalAbstraction() {
        this.hardwareState.getTransformationSwitch().setPresent(false);
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNameCendence;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        try {
            initProRCData(null);
        } catch (Exception e) {
            DJILog.e(TAG, "init professional rc failed!", new Object[0]);
            DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
        }
        CacheHelper.addRemoteControllerListener(this, "Mode");
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        super.onValueChange(key, oldValue, newValue);
        if (newValue != null && newValue.getData() != null && key.getParamKey().equals("Mode")) {
            this.rcMode = (RCMode) newValue.getData();
            switchMode(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushRcProCustomButtonsStatus params) {
        if (params.isGetted()) {
            ProfessionalRC.Event buttonEvent = new ProfessionalRC.Event(ProfessionalRC.ButtonAction.find(params.getCustomTypeIndex()));
            DJILog.d(TAG, "event=" + buttonEvent.toString(), new Object[0]);
            notifyValueChangeForKeyPath(buttonEvent, genKeyPath(RemoteControllerKeys.BUTTON_EVENT_OF_PROFESSIONAL_RC));
        }
    }

    public void onEvent3BackgroundThread(DataRcGetPushParams params) {
        super.onEvent3BackgroundThread(params);
        if (params.isGetted()) {
            int value = params.getLeftDialValue() - 1024;
            if (this.leftDialValue != value) {
                this.leftDialValue = value;
                notifyValueChangeForKeyPath(new ProfessionalRC.Event(ProfessionalRC.CustomizableButton.LD, ProfessionalRC.ProfessionalRCEventType.ROTATE, this.leftDialValue, -660, 660), genKeyPath(RemoteControllerKeys.BUTTON_EVENT_OF_PROFESSIONAL_RC));
            }
            int value2 = params.getRightDialValue() - 1024;
            if (this.rightDialValue != value2) {
                this.rightDialValue = value2;
                notifyValueChangeForKeyPath(new ProfessionalRC.Event(ProfessionalRC.CustomizableButton.RD, ProfessionalRC.ProfessionalRCEventType.ROTATE, this.rightDialValue, -660, 660), genKeyPath(RemoteControllerKeys.BUTTON_EVENT_OF_PROFESSIONAL_RC));
            }
            int value3 = params.getRightGyroValue() - 1024;
            if (this.rightWheelValue != value3) {
                this.rightWheelValue = value3;
                notifyValueChangeForKeyPath(new ProfessionalRC.Event(ProfessionalRC.CustomizableButton.RW, ProfessionalRC.ProfessionalRCEventType.ROTATE, this.rightWheelValue, -660, 660), genKeyPath(RemoteControllerKeys.BUTTON_EVENT_OF_PROFESSIONAL_RC));
            }
            int value4 = params.getGyroValue() - 1024;
            if (this.leftWheelValue != value4) {
                this.leftWheelValue = value4;
                notifyValueChangeForKeyPath(new ProfessionalRC.Event(ProfessionalRC.CustomizableButton.LW, ProfessionalRC.ProfessionalRCEventType.ROTATE, this.leftWheelValue, -660, 660), genKeyPath(RemoteControllerKeys.BUTTON_EVENT_OF_PROFESSIONAL_RC));
            }
        }
    }

    public void destroy() {
        this.retryInitTime = 1;
        this.initSuccess = false;
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
        }
        super.destroy();
    }

    /* access modifiers changed from: private */
    public void initProRCData(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.helper.getMCUData(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCProfessionalAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                String unused = DJIRCProfessionalAbstraction.this.defaultUserName = DJIRCProfessionalAbstraction.this.helper.getUsernameNow();
                boolean unused2 = DJIRCProfessionalAbstraction.this.initSuccess = !TextUtils.isEmpty(DJIRCProfessionalAbstraction.this.defaultUserName);
                DJIRCProfessionalAbstraction.this.helper.saveLastCommitData();
                DJILog.d(DJIRCProfessionalAbstraction.TAG, "get data from RC:" + DJIRCProfessionalAbstraction.this.helper.getData().toString(), new Object[0]);
                DJIRCProfessionalAbstraction.this.switchMode(new DJISDKCacheHWAbstraction.InnerCallback() {
                    /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCProfessionalAbstraction.AnonymousClass1.AnonymousClass1 */

                    public void onSuccess(Object o) {
                        CallbackUtils.onSuccess(callback, (Object) null);
                    }

                    public void onFails(DJIError error) {
                        boolean unused = DJIRCProfessionalAbstraction.this.initSuccess = false;
                        if (DJIRCProfessionalAbstraction.this.retryInitTime <= 4) {
                            DJIRCProfessionalAbstraction.access$208(DJIRCProfessionalAbstraction.this);
                            DJIRCProfessionalAbstraction.this.handler.postDelayed(new Runnable() {
                                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCProfessionalAbstraction.AnonymousClass1.AnonymousClass1.C00051 */

                                public void run() {
                                    DJIRCProfessionalAbstraction.this.initProRCData(null);
                                }
                            }, ((long) DJIRCProfessionalAbstraction.this.retryInitTime) * 500);
                            return;
                        }
                        CallbackUtils.onFailure(callback, error);
                    }
                });
            }

            public void onFailure(Ccode ccode) {
                boolean unused = DJIRCProfessionalAbstraction.this.initSuccess = false;
                if (DJIRCProfessionalAbstraction.this.retryInitTime <= 4) {
                    DJIRCProfessionalAbstraction.access$208(DJIRCProfessionalAbstraction.this);
                    DJIRCProfessionalAbstraction.this.handler.postDelayed(new Runnable() {
                        /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCProfessionalAbstraction.AnonymousClass1.AnonymousClass2 */

                        public void run() {
                            DJIRCProfessionalAbstraction.this.initProRCData(null);
                        }
                    }, ((long) DJIRCProfessionalAbstraction.this.retryInitTime) * 500);
                    return;
                }
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Action(RemoteControllerKeys.RESET_BUTTON_CONFIG)
    public void restoreToDefault(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.helper.fromDefaultRaw();
        DJILog.d(TAG, "restoring get data from json file data= " + this.helper.getData().toString(), new Object[0]);
        this.defaultUserName = this.helper.getUsernameNow();
        switchModeInData();
        this.helper.setMCUData(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCProfessionalAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                DJIRCProfessionalAbstraction.this.helper.saveLastCommitData();
                boolean unused = DJIRCProfessionalAbstraction.this.initSuccess = !TextUtils.isEmpty(DJIRCProfessionalAbstraction.this.defaultUserName);
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                DJIRCProfessionalAbstraction.this.helper.restoreToLastCommitData();
                if (DJIRCProfessionalAbstraction.this.helper.getData() != null) {
                    String unused = DJIRCProfessionalAbstraction.this.defaultUserName = DJIRCProfessionalAbstraction.this.helper.getUsernameNow();
                } else {
                    String unused2 = DJIRCProfessionalAbstraction.this.defaultUserName = null;
                }
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(RemoteControllerKeys.BUTTON_CONFIG)
    public void setFullConfig(ProfessionalRC.ButtonConfiguration config, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        this.helper.getUser(this.defaultUserName).setConfigNow(new ProfessionalRC.ProRCConfigBean(config));
        updateToRC(callback);
    }

    @Getter(RemoteControllerKeys.BUTTON_CONFIG)
    public void getFullConfig(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            callback.onSuccess(new ProfessionalRC.ButtonConfiguration(this.helper.getUser(this.defaultUserName).getConfigNow()));
        }
    }

    @Action(RemoteControllerKeys.CUSTOMIZE_BUTTON)
    public void setSingleConfig(DJISDKCacheHWAbstraction.InnerCallback callback, ProfessionalRC.CustomizableButton customizableButton, ProfessionalRC.ButtonAction functionID) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        ProfessionalRC.ProRCUserBean userBean = this.helper.getUser(this.defaultUserName);
        Iterator<ProfessionalRC.ProRCFuncMapBean> iterator = userBean.getConfigs().get(this.helper.getConfigNow(this.defaultUserName)).getFuncMaps().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                break;
            }
            ProfessionalRC.ProRCFuncMapBean singleBean = iterator.next();
            if (singleBean.getBtnID() == customizableButton.ordinal()) {
                singleBean.setFuncValue(functionID.value());
                break;
            }
        }
        updateToRC(callback);
    }

    @Action(RemoteControllerKeys.FETCH_CUSTOMIZED_ACTION_OF_BUTTON)
    public void getSingleConfig(DJISDKCacheHWAbstraction.InnerCallback callback, ProfessionalRC.CustomizableButton customizableButton) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        ProfessionalRC.ProRCUserBean userBean = this.helper.getUser(this.defaultUserName);
        for (ProfessionalRC.ProRCFuncMapBean singleBean : userBean.getConfigs().get(this.helper.getConfigNow(this.defaultUserName)).getFuncMaps()) {
            if (singleBean.getBtnID() == customizableButton.ordinal()) {
                CallbackUtils.onSuccess(callback, ProfessionalRC.ButtonAction.find(singleBean.getFuncValue()));
                return;
            }
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
    }

    @Action(RemoteControllerKeys.COMMIT_CHANGES_FOR_PROFESSIONAL_RC)
    public void updateToRC(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        switchModeInData();
        this.helper.setMCUData(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCProfessionalAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                DJIRCProfessionalAbstraction.this.helper.saveLastCommitData();
                String unused = DJIRCProfessionalAbstraction.this.defaultUserName = DJIRCProfessionalAbstraction.this.helper.getUsernameNow();
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                DJIRCProfessionalAbstraction.this.helper.restoreToLastCommitData();
                String unused = DJIRCProfessionalAbstraction.this.defaultUserName = DJIRCProfessionalAbstraction.this.helper.getUsernameNow();
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Action(RemoteControllerKeys.ADD_BUTTON_PROFILE_GROUP)
    public void addProRCUser(DJISDKCacheHWAbstraction.InnerCallback callback, String userIDString) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (TextUtils.isEmpty(userIDString)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (this.helper.getUsers() == null || this.helper.getUsers().size() != 5) {
            for (String name : this.helper.getUsernames()) {
                if (userIDString.equals(name)) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                    return;
                }
            }
            this.helper.addUserWithName(userIDString);
            updateToRC(callback);
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Action(RemoteControllerKeys.REMOVE_BUTTON_PROFILE_GROUP)
    public void deleteProRCUser(DJISDKCacheHWAbstraction.InnerCallback callback, String userIDString) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (TextUtils.isEmpty(userIDString)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (this.helper.getUsers() == null || this.helper.getUsers().size() != 1) {
            boolean found = false;
            String[] usernames = this.helper.getUsernames();
            int length = usernames.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (userIDString.equals(usernames[i])) {
                    found = true;
                    break;
                } else {
                    i++;
                }
            }
            if (!found) {
                CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                return;
            }
            this.helper.deleteUser(userIDString);
            updateToRC(callback);
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Action(RemoteControllerKeys.RENAME_BUTTON_PROFILE_GROUP)
    public void changeProRcUserName(DJISDKCacheHWAbstraction.InnerCallback callback, String from, String to) {
        int i = 0;
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (TextUtils.isEmpty(from) || TextUtils.isEmpty(to)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            boolean found = false;
            String[] usernames = this.helper.getUsernames();
            int length = usernames.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                } else if (from.equals(usernames[i2])) {
                    found = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (!found) {
                CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                return;
            }
            boolean found2 = false;
            String[] usernames2 = this.helper.getUsernames();
            int length2 = usernames2.length;
            while (true) {
                if (i >= length2) {
                    break;
                } else if (to.equals(usernames2[i])) {
                    found2 = true;
                    break;
                } else {
                    i++;
                }
            }
            if (found2) {
                CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                return;
            }
            this.helper.changeUsername(from, to);
            updateToRC(callback);
        }
    }

    @Getter(RemoteControllerKeys.SELECT_BUTTON_PROFILE_GROUP)
    public void getCurrentUserOfProRC(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            callback.onSuccess(this.defaultUserName);
        }
    }

    @Setter(RemoteControllerKeys.SELECT_BUTTON_PROFILE_GROUP)
    public void setCurrentUserOfProRC(String userIDString, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (TextUtils.isEmpty(userIDString)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            boolean found = false;
            String[] usernames = this.helper.getUsernames();
            int length = usernames.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (userIDString.equals(usernames[i])) {
                    found = true;
                    break;
                } else {
                    i++;
                }
            }
            if (!found) {
                CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                return;
            }
            this.helper.changeUser(userIDString);
            updateToRC(callback);
        }
    }

    @Setter(RemoteControllerKeys.SELECT_BUTTON_PROFILE)
    public void setConfigIndexForCurrentUser(int configIndex, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        ProfessionalRC.ProRCUserBean userBean = this.helper.getUser(this.defaultUserName);
        if (configIndex == userBean.getMaster() || configIndex == userBean.getSingle() || configIndex == userBean.getSlave()) {
            this.helper.changeConfig(this.defaultUserName, configIndex);
            updateToRC(callback);
            return;
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
    }

    @Getter(RemoteControllerKeys.SELECT_BUTTON_PROFILE)
    public void getConfigIndexForCurrentUser(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            callback.onSuccess(Integer.valueOf(this.helper.getConfigNow(this.defaultUserName)));
        }
    }

    @Getter(RemoteControllerKeys.BUTTON_PROFILE_GROUPS)
    public void getButtonProfileGroups(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            callback.onSuccess(this.helper.getUsernamesWithoutNull());
        }
    }

    /* access modifiers changed from: protected */
    public void switchModeInData() {
        if (this.initSuccess) {
            int index = -1;
            ProfessionalRC.ProRCUserBean userBean = this.helper.getUser(this.defaultUserName);
            if (RCMode.MASTER == this.rcMode) {
                index = userBean.getMaster();
            } else if (RCMode.SLAVE == this.rcMode) {
                index = userBean.getSlave();
            } else if (RCMode.NORMAL == this.rcMode) {
                index = userBean.getSingle();
            }
            if (index != -1) {
                this.helper.changeConfig(this.defaultUserName, index);
                DJILog.d(TAG, "change rc mode in data= " + this.rcMode.name() + " index=" + index, new Object[0]);
            }
        }
    }

    /* access modifiers changed from: private */
    public void switchMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.rcMode = (RCMode) CacheHelper.getRemoteController("Mode");
        if (this.rcMode == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
        } else if (!this.initSuccess) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            switchModeInData();
            this.helper.sendCurrentConfig(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCProfessionalAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    DJIRCProfessionalAbstraction.this.helper.saveLastCommitData();
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    DJIRCProfessionalAbstraction.this.helper.restoreToLastCommitData();
                    CallbackUtils.onFailure(callback);
                }
            });
        }
    }
}
