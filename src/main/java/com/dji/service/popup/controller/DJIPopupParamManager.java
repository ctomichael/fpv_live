package com.dji.service.popup.controller;

import android.content.Context;
import android.text.TextUtils;
import com.dji.service.popup.IDJIPopupInject;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.DjiSharedPreferencesManager;

@EXClassNullAway
public class DJIPopupParamManager {
    private static volatile DJIPopupParamManager instance = null;
    private static String mCurSn;
    private static String mFirmware;
    private static IDJIPopupInject mInject;
    private static String mPType;
    private static String mSubType;
    private final String KEY_LAST_FIRMWARE = "djiPopupKeyFirmware";
    private final String KEY_LAST_P_TYPE = "djiPopupKeyPType";
    private final String KEY_LAST_SN = "djiPopupKeyLastSn";
    private final String KEY_LAST_SUB_TYPE = "djiPopupKeySubType";
    private DJIPopupGetter getter = new DJIPopupGetter();
    private Context mContext;
    private boolean mIsDevicePopped = false;
    private boolean mIsStartPopped = false;

    public static DJIPopupParamManager getInstance() {
        if (instance == null) {
            synchronized (DJIPopupParamManager.class) {
                if (instance == null) {
                    instance = new DJIPopupParamManager();
                }
            }
        }
        return instance;
    }

    private DJIPopupParamManager() {
    }

    public synchronized void init() {
        if (!mInject.isDeviceConnect()) {
            mCurSn = DjiSharedPreferencesManager.getString(this.mContext, "djiPopupKeyLastSn", "");
            mPType = DjiSharedPreferencesManager.getString(this.mContext, "djiPopupKeyPType", "");
            mSubType = DjiSharedPreferencesManager.getString(this.mContext, "djiPopupKeySubType", "");
            mFirmware = DjiSharedPreferencesManager.getString(this.mContext, "djiPopupKeyFirmware", "");
            getDevices();
        }
        getStartUp();
    }

    public void injectDependency(Context context, IDJIPopupInject inject) {
        if (inject == null) {
            throw new NullPointerException("Popup inject can not be null");
        }
        this.mContext = context;
        this.getter.initParam(context, inject);
        mInject = inject;
    }

    public synchronized void onDroneConnect(boolean isConnect, String sn, String pType, String subType) {
        if ((!sn.equals(mCurSn) || !this.mIsDevicePopped) && isConnect) {
            clearData();
            mCurSn = sn;
            mPType = pType;
            mSubType = subType;
            getDevices();
        }
    }

    public synchronized void onFirmwareVersionGet(String sn, String firmware) {
        if (sn.equals(mCurSn)) {
            mFirmware = firmware;
            getDevices();
        }
    }

    public synchronized void onLoginSuccess() {
        getStartUp();
        getDevices();
    }

    public synchronized void onNetworkChange(boolean isConnect) {
        if (isConnect) {
            getStartUp();
            getDevices();
        }
    }

    private void clearData() {
        mCurSn = null;
        mPType = null;
        mSubType = null;
        mFirmware = null;
        this.mIsDevicePopped = false;
    }

    private synchronized void getDevices() {
        if (mInject != null && !TextUtils.isEmpty(mCurSn) && !TextUtils.isEmpty(mPType) && !TextUtils.isEmpty(mSubType) && !TextUtils.isEmpty(mFirmware) && mInject.isLoggedIn() && mInject.isNetworkOK() && !mInject.isInActive() && !this.mIsDevicePopped) {
            this.mIsDevicePopped = true;
            this.getter.get(false, mCurSn, mPType, mSubType, mFirmware);
            DjiSharedPreferencesManager.putString(this.mContext, "djiPopupKeyLastSn", mCurSn);
            DjiSharedPreferencesManager.putString(this.mContext, "djiPopupKeyPType", mPType);
            DjiSharedPreferencesManager.putString(this.mContext, "djiPopupKeySubType", mSubType);
            DjiSharedPreferencesManager.putString(this.mContext, "djiPopupKeyFirmware", mFirmware);
            String tempSn = mCurSn;
            clearData();
            mCurSn = tempSn;
        }
    }

    private synchronized void getStartUp() {
        if (mInject != null && !this.mIsStartPopped && mInject.isLoggedIn() && mInject.isNetworkOK()) {
            this.mIsStartPopped = true;
            this.getter.get(true, "", "", "", "");
        }
    }
}
