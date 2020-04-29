package dji.midware.data.manager.Dpad;

import android.app.Dialog;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DpadUtil {
    public static void setDlgCenter(Dialog dialog) {
        if (DpadProductManager.getInstance().isDpad()) {
            dialog.getWindow().setGravity(17);
        }
    }
}
