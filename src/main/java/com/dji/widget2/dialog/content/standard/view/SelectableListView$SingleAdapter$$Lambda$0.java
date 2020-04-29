package com.dji.widget2.dialog.content.standard.view;

import android.view.View;
import com.dji.widget2.dialog.content.standard.view.SelectableListView;

final /* synthetic */ class SelectableListView$SingleAdapter$$Lambda$0 implements View.OnClickListener {
    private final SelectableListView.SingleAdapter arg$1;
    private final SelectableListView.SingleAdapter.ViewHolder arg$2;
    private final int arg$3;

    SelectableListView$SingleAdapter$$Lambda$0(SelectableListView.SingleAdapter singleAdapter, SelectableListView.SingleAdapter.ViewHolder viewHolder, int i) {
        this.arg$1 = singleAdapter;
        this.arg$2 = viewHolder;
        this.arg$3 = i;
    }

    public void onClick(View view) {
        this.arg$1.lambda$applySingleListener$0$SelectableListView$SingleAdapter(this.arg$2, this.arg$3, view);
    }
}
