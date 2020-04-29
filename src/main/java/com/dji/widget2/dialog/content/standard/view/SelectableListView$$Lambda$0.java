package com.dji.widget2.dialog.content.standard.view;

import android.content.DialogInterface;

final /* synthetic */ class SelectableListView$$Lambda$0 implements DialogInterface.OnClickListener {
    private final SelectableListView arg$1;

    SelectableListView$$Lambda$0(SelectableListView selectableListView) {
        this.arg$1 = selectableListView;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$setData$0$SelectableListView(dialogInterface, i);
    }
}
