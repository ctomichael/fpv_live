package com.dji.component.fpv.widget.preview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.dji.component.fpv.base.AbstractViewGate;
import com.dji.component.fpv.base.BulletinBoardProvider;
import com.dji.component.fpv.base.IFpvContainer;
import com.dji.component.fpv.widget.countdown.CountDownView;
import com.dji.component.fpv.widget.countdown.CountDownViewModel;

public class PreviewShellGate extends AbstractViewGate {
    private PreviewShell mPreviewShell;

    public PreviewShellGate(Context context, IFpvContainer fpvContainer, BulletinBoardProvider bridge) {
        super(context, fpvContainer, bridge);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mFPVContainer.removePreviewShell(this.mPreviewShell);
    }

    /* access modifiers changed from: protected */
    public void inflateShell(@NonNull Context context, @NonNull BulletinBoardProvider bridge) {
        this.mPreviewShell = (PreviewShell) LayoutInflater.from(this.mContext).inflate(R.layout.fpv_shell_preview, (ViewGroup) null);
        this.mPreviewShell.bind(new PreviewShellModel(bridge));
        ((CountDownView) this.mPreviewShell.findViewById(R.id.count_down_view)).bind(new CountDownViewModel(bridge));
    }

    /* access modifiers changed from: protected */
    public void onShellFinishInflate() {
        this.mFPVContainer.addPreviewShell(this.mPreviewShell);
    }
}
