package dji.publics.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.Dpad.DpadUtil;

@EXClassNullAway
public class CenterTextAlertDialogBuilder extends AlertDialog.Builder {
    private final Context mContext;

    public CenterTextAlertDialogBuilder(Context arg0, int arg1) {
        super(arg0, arg1);
        this.mContext = arg0;
    }

    public CenterTextAlertDialogBuilder(Context arg0) {
        super(arg0);
        this.mContext = arg0;
    }

    @Deprecated
    public AlertDialog create() {
        return super.create();
    }

    public AlertDialog show() {
        AlertDialog dialog = super.show();
        DpadUtil.setDlgCenter(dialog);
        TextView messageView = (TextView) dialog.findViewById(16908299);
        if (messageView == null) {
            DJILogHelper.getInstance().LOGI("Lyric", "messageView == null");
        } else {
            ViewGroup.LayoutParams lp = messageView.getLayoutParams();
            lp.width = -1;
            lp.height = -1;
            messageView.setLayoutParams(lp);
            messageView.setGravity(17);
        }
        return dialog;
    }
}
