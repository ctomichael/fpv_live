package com.dji.component.fpv.widget.histogram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.dji.component.fpv.base.AbstractViewGate;
import com.dji.component.fpv.base.BulletinBoardProvider;
import com.dji.component.fpv.base.IFpvContainer;
import com.dji.component.fpv.widget.preview.R;

public class HistogramShellGate extends AbstractViewGate {
    private HistogramShell mHistogramShell;

    public HistogramShellGate(Context context, IFpvContainer fpvContainer, BulletinBoardProvider bridge) {
        super(context, fpvContainer, bridge);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mFPVContainer.removeView(this.mHistogramShell);
    }

    /* access modifiers changed from: protected */
    public void inflateShell(@NonNull Context context, @NonNull BulletinBoardProvider bridge) {
        HistogramShellViewModel viewModel = new HistogramShellViewModel(bridge);
        this.mHistogramShell = (HistogramShell) LayoutInflater.from(this.mContext).inflate(R.layout.histogram_shell_layout, (ViewGroup) null);
        this.mHistogramShell.bind(viewModel);
    }

    /* access modifiers changed from: protected */
    public void onShellFinishInflate() {
        this.mFPVContainer.addHistogramView(this.mHistogramShell);
    }
}
