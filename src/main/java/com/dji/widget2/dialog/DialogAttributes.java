package com.dji.widget2.dialog;

import android.support.annotation.Nullable;
import com.dji.widget2.R;
import java.io.Serializable;

public interface DialogAttributes {

    public enum Theme implements Serializable {
        LIGHT(0),
        FPV(1);
        
        int value;

        private Theme(int value2) {
            this.value = value2;
        }

        @Nullable
        public static Theme find(int val) {
            if (val == LIGHT.value) {
                return LIGHT;
            }
            if (val == FPV.value) {
                return FPV;
            }
            return null;
        }
    }

    public enum Size implements Serializable {
        M(R.dimen.s_280_dp, R.dimen.s_300_dp),
        L(R.dimen.s_320_dp, R.dimen.s_300_dp),
        XL(R.dimen.s_460_dp, R.dimen.s_315_dp);
        
        public int maxHeight;
        public int width;

        private Size(int width2, int maxHeight2) {
            this.width = width2;
            this.maxHeight = maxHeight2;
        }
    }
}
