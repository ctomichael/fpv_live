package com.dji.widget2.dialog.content.standard.view;

import android.content.DialogInterface;

final /* synthetic */ class SelectableListView$$Lambda$1 implements DialogInterface.OnMultiChoiceClickListener {
    private final SelectableListView arg$1;

    SelectableListView$$Lambda$1(SelectableListView selectableListView) {
        this.arg$1 = selectableListView;
    }

    public void onClick(DialogInterface dialogInterface, int i, boolean z) {
        this.arg$1.lambda$setData$1$SelectableListView(dialogInterface, i, z);
    }
}
