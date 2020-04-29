package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import dji.common.error.DJIAccessLockerError;
import dji.common.error.DJIError;
import dji.common.flightcontroller.accesslocker.AccessLockerState;
import dji.common.flightcontroller.accesslocker.FormattingProgressState;
import dji.common.flightcontroller.accesslocker.FormattingState;
import dji.common.flightcontroller.accesslocker.UserAccountInfo;
import dji.common.util.CallbackUtils;
import dji.log.DJILog;
import dji.log.GlobalConfig;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCommonDataLocker;
import dji.midware.data.model.P3.DataCommonDataLockerChangeSecurityCode;
import dji.midware.data.model.P3.DataCommonDataLockerCommonRequest;
import dji.midware.data.model.P3.DataCommonDataLockerEnabled;
import dji.midware.data.model.P3.DataCommonDataLockerRequestFormatState;
import dji.midware.data.model.P3.DataCommonDataLockerRequestStatus;
import dji.midware.data.model.P3.DataCommonDataLockerRequestVersion;
import dji.midware.data.model.P3.DataCommonDataLockerResetting;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.keycatalog.AccessLockerKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.sdksharedlib.util.CallbackResult;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Action1;
import java.util.regex.Pattern;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AccessLockerAbstraction extends DJISubComponentHWAbstraction {
    private static final boolean DEBUG = GlobalConfig.LOG_DEBUGGABLE;
    private static final int MSG_DELAY_TIME = 500;
    private static final int MSG_PUSH_DATA_TIME_OUT = 2000;
    private static final int MSG_REQUEST_FORMATTING_STATE = 2;
    private static final int MSG_UPDATE_STATE = 1;
    private static final int SUCCESS_RETURN_CODE = 240;
    private static final String TAG = "DataLocker";
    private Handler handler = new Handler(BackgroundLooper.getLooper()) {
        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass1 */

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    AccessLockerAbstraction.this.computeCurrentDataProtectionState(false);
                    return;
                case 2:
                    AccessLockerAbstraction.this.computeFormattingState(false);
                    return;
                default:
                    return;
            }
        }
    };

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(AccessLockerKeys.class, getClass());
    }

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
        }
        super.destroy();
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        sendMsgDelayed(2);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCommonDataLockerCommonRequest request) {
        switch (request.getDataProtectionCmdType()) {
            case VERIFYING:
                if (this.handler != null && this.handler.hasMessages(1)) {
                    this.handler.removeMessages(1);
                }
                computeCurrentDataProtectionState(true);
                this.handler.sendEmptyMessageDelayed(1, 2000);
                return;
            case FORMATING:
                if (this.handler != null && this.handler.hasMessages(2)) {
                    this.handler.removeMessages(2);
                }
                computeFormattingState(true);
                this.handler.sendEmptyMessageDelayed(2, 2000);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void sendMsgDelayed(int MsgID) {
        if (this.handler != null) {
            if (this.handler.hasMessages(MsgID)) {
                this.handler.removeMessages(MsgID);
            }
            this.handler.sendEmptyMessageDelayed(MsgID, 500);
        }
    }

    @Getter(AccessLockerKeys.ACCESS_LOCKER_VERSION)
    public void getDataProtectionVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonDataLockerRequestVersion getter = new DataCommonDataLockerRequestVersion();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                Integer version = Integer.valueOf(getter.getVersion());
                AccessLockerAbstraction.LOG("get version:");
                CallbackUtils.onSuccess(callback, version);
            }

            public void onFailure(Ccode ccode) {
                AccessLockerAbstraction.LOG("get version failed:" + ccode);
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(AccessLockerKeys.ACCESS_LOCKER_USERNAME)
    public void getDataProtectionAccount(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (((AccessLockerState) CacheHelper.getAccessLockerAssistant(AccessLockerKeys.ACCESS_LOCKER_STATE)) == AccessLockerState.NOT_INITIALIZED) {
            CallbackUtils.onFailure(callback, DJIAccessLockerError.NOT_SET_UP_ERROR);
            return;
        }
        final DataCommonDataLockerRequestStatus getter = new DataCommonDataLockerRequestStatus();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                if (getter.getEnabledState() == 1) {
                    CallbackUtils.onSuccess(callback, getter.getUsername());
                } else {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Action(AccessLockerKeys.SET_UP_USER)
    public void setPassword(final DJISDKCacheHWAbstraction.InnerCallback callback, final UserAccountInfo settings) {
        if (settings == null || TextUtils.isEmpty(settings.getUsername())) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        AccessLockerState state = (AccessLockerState) CacheHelper.getAccessLockerAssistant(AccessLockerKeys.ACCESS_LOCKER_STATE);
        if (state != AccessLockerState.NOT_INITIALIZED && state != AccessLockerState.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIAccessLockerError.INVALID_STATE);
        } else if (TextUtils.isEmpty(settings.getSecurityCode()) || !isSecurityCodeValid(settings.getSecurityCode())) {
            CallbackUtils.onFailure(callback, DJIAccessLockerError.SECURITY_CODE_FORMAT_INVALID);
        } else {
            final DataCommonDataLockerEnabled setter = new DataCommonDataLockerEnabled(DataCommonDataLocker.DataLockerCmdType.ENABLE_DATA_LOCKER);
            setter.setUsername(settings.getUsername()).setCode(settings.getSecurityCode()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    AccessLockerAbstraction.this.sendMsgDelayed(1);
                    AccessLockerAbstraction.LOG("Set Password success:" + setter.getResult());
                    if (setter.getResult() == AccessLockerAbstraction.SUCCESS_RETURN_CODE) {
                        CallbackUtils.onSuccess(callback, (Object) null);
                        AccessLockerAbstraction.this.notifyValueChangeForKeyPath(settings.getUsername(), AccessLockerAbstraction.this.convertKeyToPath(AccessLockerKeys.ACCESS_LOCKER_USERNAME));
                        return;
                    }
                    CallbackUtils.onFailure(callback, DJIAccessLockerError.getDJIError(setter.getResult()));
                }

                public void onFailure(Ccode ccode) {
                    AccessLockerAbstraction.LOG("set Password failed:" + DJIError.getDJIError(ccode));
                    AccessLockerAbstraction.this.sendMsgDelayed(1);
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    @Action("login")
    public void verifyPassword(DJISDKCacheHWAbstraction.InnerCallback callback, UserAccountInfo settings) {
        if (settings == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        AccessLockerState state = (AccessLockerState) CacheHelper.getAccessLockerAssistant(AccessLockerKeys.ACCESS_LOCKER_STATE);
        if (state == AccessLockerState.NOT_INITIALIZED) {
            CallbackUtils.onFailure(callback, DJIAccessLockerError.NOT_SET_UP_ERROR);
        } else if (state == AccessLockerState.UNLOCKED) {
            CallbackUtils.onFailure(callback, DJIAccessLockerError.ALREADY_UNLOCKED);
        } else {
            String userAccount = (String) CacheHelper.getAccessLockerAssistant(AccessLockerKeys.ACCESS_LOCKER_USERNAME);
            if (TextUtils.isEmpty(userAccount)) {
                queryUserAccount().filter(new AccessLockerAbstraction$$Lambda$0(this, settings, callback)).flatMap(new AccessLockerAbstraction$$Lambda$1(this, settings)).subscribe(new AccessLockerAbstraction$$Lambda$2(this, callback));
            } else if (!userAccount.equals(settings.getUsername())) {
                CallbackUtils.onFailure(callback, DJIAccessLockerError.USERNAME_NOT_EXIST);
            } else {
                verifyUserInfo(settings, DataCommonDataLocker.DataLockerCmdType.VERIFY_SECURITY_CODE).subscribe(new AccessLockerAbstraction$$Lambda$3(this, callback));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Boolean lambda$verifyPassword$0$AccessLockerAbstraction(UserAccountInfo settings, DJISDKCacheHWAbstraction.InnerCallback callback, CallbackResult callbackResult) {
        return Boolean.valueOf(isCurrentUserAccount(callbackResult, settings.getUsername(), callback));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Observable lambda$verifyPassword$1$AccessLockerAbstraction(UserAccountInfo settings, CallbackResult callbackResult) {
        return verifyUserInfo(settings, DataCommonDataLocker.DataLockerCmdType.VERIFY_SECURITY_CODE);
    }

    @Action(AccessLockerKeys.RESET_USER_ACCOUNT)
    public void clearPassword(DJISDKCacheHWAbstraction.InnerCallback callback, UserAccountInfo settings) {
        if (settings == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (((AccessLockerState) CacheHelper.getAccessLockerAssistant(AccessLockerKeys.ACCESS_LOCKER_STATE)) == AccessLockerState.NOT_INITIALIZED) {
            CallbackUtils.onFailure(callback, DJIAccessLockerError.NOT_SET_UP_ERROR);
        } else {
            String userAccount = (String) CacheHelper.getAccessLockerAssistant(AccessLockerKeys.ACCESS_LOCKER_USERNAME);
            if (TextUtils.isEmpty(userAccount)) {
                queryUserAccount().filter(new AccessLockerAbstraction$$Lambda$4(this, settings, callback)).flatMap(new AccessLockerAbstraction$$Lambda$5(this, settings)).subscribe(new AccessLockerAbstraction$$Lambda$6(this, callback));
            } else if (!userAccount.equals(settings.getUsername())) {
                CallbackUtils.onFailure(callback, DJIAccessLockerError.USERNAME_NOT_EXIST);
            } else {
                verifyUserInfo(settings, DataCommonDataLocker.DataLockerCmdType.CLEAR_SECURITY_CODE).subscribe(new AccessLockerAbstraction$$Lambda$7(this, callback));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Boolean lambda$clearPassword$4$AccessLockerAbstraction(UserAccountInfo settings, DJISDKCacheHWAbstraction.InnerCallback callback, CallbackResult callbackResult) {
        return Boolean.valueOf(isCurrentUserAccount(callbackResult, settings.getUsername(), callback));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Observable lambda$clearPassword$5$AccessLockerAbstraction(UserAccountInfo settings, CallbackResult callbackResult) {
        return verifyUserInfo(settings, DataCommonDataLocker.DataLockerCmdType.CLEAR_SECURITY_CODE);
    }

    @Action(AccessLockerKeys.MODIFY_USER_ACCOUNT)
    public void changePassword(DJISDKCacheHWAbstraction.InnerCallback callback, UserAccountInfo currentUserInfo, UserAccountInfo newUserInfo) {
        if (currentUserInfo == null || newUserInfo == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (TextUtils.isEmpty(currentUserInfo.getUsername()) || TextUtils.isEmpty(newUserInfo.getUsername())) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (((AccessLockerState) CacheHelper.getAccessLockerAssistant(AccessLockerKeys.ACCESS_LOCKER_STATE)) == AccessLockerState.NOT_INITIALIZED) {
            CallbackUtils.onFailure(callback, DJIAccessLockerError.NOT_SET_UP_ERROR);
        } else {
            String userAccount = (String) CacheHelper.getAccessLockerAssistant(AccessLockerKeys.ACCESS_LOCKER_USERNAME);
            if (TextUtils.isEmpty(userAccount)) {
                queryUserAccount().filter(new AccessLockerAbstraction$$Lambda$8(this, currentUserInfo, callback)).flatMap(new AccessLockerAbstraction$$Lambda$9(this, currentUserInfo, newUserInfo)).subscribe(new AccessLockerAbstraction$$Lambda$10(this, callback));
            } else if (!userAccount.equals(currentUserInfo.getUsername())) {
                CallbackUtils.onFailure(callback, DJIAccessLockerError.USERNAME_NOT_EXIST);
            } else {
                changeUserInfo(currentUserInfo.getSecurityCode(), newUserInfo).subscribe(new AccessLockerAbstraction$$Lambda$11(this, callback));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Boolean lambda$changePassword$8$AccessLockerAbstraction(UserAccountInfo currentUserInfo, DJISDKCacheHWAbstraction.InnerCallback callback, CallbackResult callbackResult) {
        return Boolean.valueOf(isCurrentUserAccount(callbackResult, currentUserInfo.getUsername(), callback));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Observable lambda$changePassword$9$AccessLockerAbstraction(UserAccountInfo currentUserInfo, UserAccountInfo newUserInfo, CallbackResult callbackResult) {
        return changeUserInfo(currentUserInfo.getSecurityCode(), newUserInfo);
    }

    @Action(AccessLockerKeys.FORMAT)
    public void resetFlight(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (((AccessLockerState) CacheHelper.getAccessLockerAssistant(AccessLockerKeys.ACCESS_LOCKER_STATE)) == AccessLockerState.NOT_INITIALIZED) {
            CallbackUtils.onFailure(callback, DJIAccessLockerError.NOT_SET_UP_ERROR);
            return;
        }
        final DataCommonDataLockerResetting setter = new DataCommonDataLockerResetting(DataCommonDataLocker.DataLockerCmdType.RESETTING);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                AccessLockerAbstraction.LOG("reset success:" + setter.getResult());
                AccessLockerAbstraction.this.sendMsgDelayed(2);
                if (setter.getResult() == AccessLockerAbstraction.SUCCESS_RETURN_CODE) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                } else {
                    CallbackUtils.onFailure(callback, DJIAccessLockerError.getDJIError(setter.getResult()));
                }
            }

            public void onFailure(Ccode ccode) {
                AccessLockerAbstraction.LOG("reset failed:" + DJIError.getDJIError(ccode));
                AccessLockerAbstraction.this.sendMsgDelayed(2);
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: private */
    public void computeCurrentDataProtectionState(boolean isFromPush) {
        if (isFromPush) {
            notifyValueChangeForKeyPath(AccessLockerState.LOCKED, convertKeyToPath(AccessLockerKeys.ACCESS_LOCKER_STATE));
        } else {
            getDataLockerState().retryWhen(AccessLockerAbstraction$$Lambda$12.$instance).subscribe(new AccessLockerAbstraction$$Lambda$13(this));
        }
    }

    static final /* synthetic */ Object lambda$null$12$AccessLockerAbstraction(Throwable throwable, Integer integer) {
        return integer;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$computeCurrentDataProtectionState$15$AccessLockerAbstraction(Object o) {
        notifyValueChangeForKeyPath(o, convertKeyToPath(AccessLockerKeys.ACCESS_LOCKER_STATE));
    }

    /* access modifiers changed from: private */
    public void computeFormattingState(boolean isFromPush) {
        if (isFromPush) {
            notifyValueChangeForKeyPath(new FormattingState(FormattingProgressState.FORMATTING, null), convertKeyToPath(AccessLockerKeys.FORMATTING_STATE));
        } else {
            Observable.concat(getDataLockerState().retryWhen(AccessLockerAbstraction$$Lambda$14.$instance), getFormattingState()).subscribe(new Action1<Object>() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass6 */

                public void call(Object o) {
                    if (o instanceof AccessLockerState) {
                        AccessLockerAbstraction.this.notifyValueChangeForKeyPath(o, AccessLockerAbstraction.this.convertKeyToPath(AccessLockerKeys.ACCESS_LOCKER_STATE));
                    }
                    if (o instanceof FormattingState) {
                        AccessLockerAbstraction.this.notifyValueChangeForKeyPath(o, AccessLockerAbstraction.this.convertKeyToPath(AccessLockerKeys.FORMATTING_STATE));
                    }
                }
            });
        }
    }

    static final /* synthetic */ Object lambda$null$16$AccessLockerAbstraction(Throwable throwable, Integer integer) {
        return integer;
    }

    private Observable<CallbackResult<String>> queryUserAccount() {
        return Observable.create(new AccessLockerAbstraction$$Lambda$15(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$queryUserAccount$19$AccessLockerAbstraction(final Subscriber subscriber) {
        getDataProtectionAccount(new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass7 */

            public void onSuccess(Object o) {
                subscriber.onNext(new CallbackResult((String) o, null));
                subscriber.onCompleted();
            }

            public void onFails(DJIError error) {
                subscriber.onNext(new CallbackResult("", error));
                subscriber.onCompleted();
            }
        });
    }

    private Observable<DJIError> verifyUserInfo(final UserAccountInfo settings, final DataCommonDataLocker.DataLockerCmdType cmdType) {
        return Observable.create(new Observable.OnSubscribe<DJIError>() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass8 */

            public /* bridge */ /* synthetic */ void call(Object obj) {
                call((Subscriber<? super DJIError>) ((Subscriber) obj));
            }

            public void call(final Subscriber<? super DJIError> subscriber) {
                final DataCommonDataLockerEnabled setter = new DataCommonDataLockerEnabled(cmdType);
                setter.setUsername(settings.getUsername()).setCode(settings.getSecurityCode()).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass8.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        AccessLockerAbstraction.LOG("verify user info sucess:" + setter.getResult() + " cmdType:" + cmdType);
                        if (setter.getResult() == AccessLockerAbstraction.SUCCESS_RETURN_CODE) {
                            subscriber.onNext(null);
                        } else {
                            subscriber.onNext(DJIAccessLockerError.getDJIError(setter.getResult()));
                        }
                        subscriber.onCompleted();
                    }

                    public void onFailure(Ccode ccode) {
                        AccessLockerAbstraction.LOG("verify user info failed: ccode:" + ccode + " cmdType:" + cmdType);
                        subscriber.onNext(DJIError.getDJIError(ccode));
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }

    private Observable<DJIError> changeUserInfo(String oldCode, UserAccountInfo newUserInfo) {
        return Observable.create(new AccessLockerAbstraction$$Lambda$16(this, newUserInfo, oldCode));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$changeUserInfo$20$AccessLockerAbstraction(final UserAccountInfo newUserInfo, String oldCode, final Subscriber subscriber) {
        if (TextUtils.isEmpty(newUserInfo.getSecurityCode()) || !isSecurityCodeValid(newUserInfo.getSecurityCode())) {
            subscriber.onNext(DJIAccessLockerError.SECURITY_CODE_FORMAT_INVALID);
            subscriber.onCompleted();
            return;
        }
        final DataCommonDataLockerChangeSecurityCode setter = new DataCommonDataLockerChangeSecurityCode(DataCommonDataLocker.DataLockerCmdType.CHANGE_SECURITY_CODE);
        setter.setUsername(newUserInfo.getUsername()).setOldCode(oldCode).setNewCode(newUserInfo.getSecurityCode()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass9 */

            public void onSuccess(Object model) {
                AccessLockerAbstraction.LOG("change User info sucess:" + setter.getResult());
                if (setter.getResult() == AccessLockerAbstraction.SUCCESS_RETURN_CODE) {
                    subscriber.onNext(null);
                    AccessLockerAbstraction.this.notifyValueChangeForKeyPath(newUserInfo.getUsername(), AccessLockerAbstraction.this.convertKeyToPath(AccessLockerKeys.ACCESS_LOCKER_USERNAME));
                } else {
                    subscriber.onNext(DJIAccessLockerError.getDJIError(setter.getResult()));
                }
                subscriber.onCompleted();
            }

            public void onFailure(Ccode ccode) {
                AccessLockerAbstraction.LOG("change User info failed: Ccode:" + ccode);
                subscriber.onNext(DJIError.getDJIError(ccode));
                subscriber.onCompleted();
            }
        });
    }

    private Observable<Object> getDataLockerState() {
        return Observable.create(new AccessLockerAbstraction$$Lambda$17(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$getDataLockerState$21$AccessLockerAbstraction(final Subscriber subscriber) {
        final DataCommonDataLockerRequestStatus getter = new DataCommonDataLockerRequestStatus();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass10 */

            public void onSuccess(Object model) {
                AccessLockerState resState = AccessLockerState.UNKNOWN;
                AccessLockerAbstraction.LOG("get enabled:" + getter.getEnabledState() + "get verified:" + getter.isverified());
                switch (getter.getEnabledState()) {
                    case 0:
                        resState = AccessLockerState.NOT_INITIALIZED;
                        AccessLockerAbstraction.this.notifyValueChangeForKeyPath("", AccessLockerAbstraction.this.convertKeyToPath(AccessLockerKeys.ACCESS_LOCKER_USERNAME));
                        break;
                    case 1:
                        if (!getter.isverified()) {
                            resState = AccessLockerState.LOCKED;
                            break;
                        } else {
                            resState = AccessLockerState.UNLOCKED;
                            break;
                        }
                }
                subscriber.onNext(resState);
                subscriber.onCompleted();
            }

            public void onFailure(Ccode ccode) {
                AccessLockerAbstraction.LOG("get failure:" + getter.getEnabledState() + "get verified:" + getter.isverified());
                subscriber.onError(new Throwable());
            }
        });
    }

    private Observable<Object> getFormattingState() {
        return Observable.create(new AccessLockerAbstraction$$Lambda$18(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$getFormattingState$22$AccessLockerAbstraction(final Subscriber subscriber) {
        final DataCommonDataLockerRequestFormatState getter = new DataCommonDataLockerRequestFormatState();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.AccessLockerAbstraction.AnonymousClass11 */

            public void onSuccess(Object model) {
                FormattingProgressState resState = FormattingProgressState.NONE;
                DJIError djiDataLockerError = null;
                AccessLockerAbstraction.LOG("get formatting state:" + getter.getFormattingState() + "get error:" + getter.getErrorCode());
                switch (getter.getFormattingState()) {
                    case 0:
                        resState = FormattingProgressState.NONE;
                        break;
                    case 1:
                        resState = FormattingProgressState.FORMATTING;
                        break;
                    case 2:
                        resState = FormattingProgressState.SUCCESSFUL;
                        break;
                    case 3:
                        resState = FormattingProgressState.FAILURE;
                        djiDataLockerError = DJIAccessLockerError.getDJIError(getter.getErrorCode());
                        break;
                }
                subscriber.onNext(new FormattingState(resState, djiDataLockerError));
                subscriber.onCompleted();
            }

            public void onFailure(Ccode ccode) {
                AccessLockerAbstraction.LOG("get formatting state failure:" + ccode);
                subscriber.onNext(new FormattingState(FormattingProgressState.NONE, null));
                subscriber.onCompleted();
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: invokeInnerCallback */
    public void lambda$verifyPassword$3$AccessLockerAbstraction(DJIError djiError, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (djiError == null) {
            CallbackUtils.onSuccess(callback, (Object) null);
        } else {
            CallbackUtils.onFailure(callback, djiError);
        }
        sendMsgDelayed(1);
    }

    private boolean isCurrentUserAccount(CallbackResult result, String userAccount, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (result.getError() != null) {
            CallbackUtils.onFailure(callback, result.getError());
            return false;
        } else if (result.getValue().equals(userAccount)) {
            return true;
        } else {
            CallbackUtils.onFailure(callback, DJIAccessLockerError.USERNAME_NOT_EXIST);
            return false;
        }
    }

    private boolean isSecurityCodeValid(String code) {
        return Pattern.matches("^[0-9a-zA-Z]{6,8}$", code);
    }

    /* access modifiers changed from: private */
    public static void LOG(String msg) {
        if (DEBUG) {
            DJILog.logWriteD(TAG, msg, TAG, new Object[0]);
        }
    }
}
