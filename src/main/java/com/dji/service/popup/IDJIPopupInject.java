package com.dji.service.popup;

import android.content.DialogInterface;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IDJIPopupInject {
    boolean isDeviceConnect();

    boolean isInActive();

    boolean isInFPV();

    boolean isInMainPage();

    boolean isLoggedIn();

    boolean isMotorUp();

    boolean isNetworkOK();

    void showDialog(String str, String str2, String str3, String str4, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2);
}
