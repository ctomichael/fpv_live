package dji.publics.protocol;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class UserLevelJsonBean {
    public LevelInfo level;
    public MedalInfo medal;
    public String msg;
    public int ret;

    @Keep
    public static class LevelInfo {
        public int exp = 0;
        public int lastexp;
        public int level = 0;
        public int nextexp = 0;
    }

    @Keep
    public static class MedalInfo {
        public LevelInfo director;
        public LevelInfo level;
        public LevelInfo photographer;
        public LevelInfo share;
    }
}
