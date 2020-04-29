package com.dji.component.fpv.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public interface IFpvContainer {
    void addAttitudeBarShell(View view);

    void addErrorPopShell(View view);

    void addGimbalShell(View view);

    void addHistogramView(View view);

    void addLeftBarShell(View view);

    void addMapShell(View view);

    void addMenuItem(View view, RelativeLayout.LayoutParams layoutParams, String str);

    void addMenuLayer(View view);

    void addMeteringShell(View view);

    void addMissionShell(View view);

    void addOSDShell(View view);

    void addPreviewShell(View view);

    void addRadarShell(View view);

    void addRightBarShell(View view);

    void addRightBottomShell(View view);

    void addToMaskLayer(View view);

    void addTopBarShell(View view);

    void addTopOSDShell(View view);

    void addTouchLayerShell(View view);

    ViewGroup getFpvRootView();

    View getMapShell();

    ViewGroup getOSDShell();

    RelativeLayout getPreviewLayer();

    View getPreviewLayout();

    FrameLayout getTopOSDShell();

    void removeAttitudeBarShell(View view);

    void removeFromMaskLayer(View view);

    void removeMapShell(View view);

    void removeMenuItem(View view);

    void removeMeteringShell(View view);

    void removeMissionShell(View view);

    void removePreviewShell(View view);

    void removeTouchLayerShell(View view);

    void removeView(View view);
}
