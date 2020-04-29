package com.dji.widget2.dialog.content.standard.view;

import android.view.View;
import com.dji.widget2.dialog.content.standard.view.SelectableListView;

final /* synthetic */ class SelectableListView$SingleAdapter$$Lambda$1 implements View.OnClickListener {
    private final SelectableListView.SingleAdapter arg$1;
    private final int arg$2;

    SelectableListView$SingleAdapter$$Lambda$1(SelectableListView.SingleAdapter singleAdapter, int i) {
        this.arg$1 = singleAdapter;
        this.arg$2 = i;
    }

    public void onClick(View view) {
        this.arg$1.lambda$applyMultiListener$1$SelectableListView$SingleAdapter(this.arg$2, view);
    }
}
