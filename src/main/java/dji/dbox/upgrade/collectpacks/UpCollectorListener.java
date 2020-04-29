package dji.dbox.upgrade.collectpacks;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface UpCollectorListener {
    void onStrategyCollectListOver();

    void onStrategyCollectVersionOver();
}
