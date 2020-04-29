package com.dji.widget2.dialog.builder;

import android.content.DialogInterface;

final /* synthetic */ class ShadowSelectableListDialog$$Lambda$1 implements DialogInterface.OnMultiChoiceClickListener {
    private final ShadowSelectableListDialog arg$1;

    ShadowSelectableListDialog$$Lambda$1(ShadowSelectableListDialog shadowSelectableListDialog) {
        this.arg$1 = shadowSelectableListDialog;
    }

    public void onClick(DialogInterface dialogInterface, int i, boolean z) {
        this.arg$1.lambda$generateContentView$1$ShadowSelectableListDialog(dialogInterface, i, z);
    }
}
