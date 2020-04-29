package dji.midware.interfaces;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@EXClassNullAway
public interface CmdIdInterface {
    DataBase getDataBase();

    Class<? extends DataBase> getDataModel();

    boolean isBlock();

    boolean isMix();

    boolean isNeedCcode();

    int value();
}
