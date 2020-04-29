package com.dji.findmydrone.ui.view.right.camera;

import android.app.Presentation;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Display;
import com.dji.relay.R;

public class CameraHDMIPresentation extends Presentation {
    public CameraHDMIPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = getContext().getResources();
        setContentView((int) R.layout.fmd_hdmi_view);
    }
}
