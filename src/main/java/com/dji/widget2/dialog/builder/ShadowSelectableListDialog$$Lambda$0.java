package com.dji.widget2.dialog.builder;

import android.content.DialogInterface;

final /* synthetic */ class ShadowSelectableListDialog$$Lambda$0 implements DialogInterface.OnClickListener {
    private final ShadowSelectableListDialog arg$1;

    ShadowSelectableListDialog$$Lambda$0(ShadowSelectableListDialog shadowSelectableListDialog) {
        this.arg$1 = shadowSelectableListDialog;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$generateContentView$0$ShadowSelectableListDialog(dialogInterface, i);
    }
}
