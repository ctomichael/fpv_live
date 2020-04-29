package com.dji.component.fpv.widget.preview;

import dji.utils.Optional;
import io.reactivex.functions.Function;

final /* synthetic */ class PreviewShellModel$$Lambda$2 implements Function {
    static final Function $instance = new PreviewShellModel$$Lambda$2();

    private PreviewShellModel$$Lambda$2() {
    }

    public Object apply(Object obj) {
        return ((Optional) obj).get();
    }
}
