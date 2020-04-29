package com.dji.component.fpv.widget.preview;

import dji.utils.Optional;
import io.reactivex.functions.Function;

final /* synthetic */ class PreviewShellModel$$Lambda$3 implements Function {
    static final Function $instance = new PreviewShellModel$$Lambda$3();

    private PreviewShellModel$$Lambda$3() {
    }

    public Object apply(Object obj) {
        return ((Optional) obj).get();
    }
}
