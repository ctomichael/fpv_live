package dji.midware.data.model.base;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@EXClassNullAway
public abstract class DJICommonDataBase extends DataBase {
    public DJICommonDataBase() {
    }

    public DJICommonDataBase(boolean isRegist) {
        super(isRegist);
    }
}
