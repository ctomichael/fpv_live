package com.dji.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.dialog.DJIDialogBuilder;
import dji.publics.widget.dialog.DJIDialogType;
import java.util.List;

public class DefaultRationale implements Rationale {
    private boolean mIsShown;
    /* access modifiers changed from: private */
    public OnCancelListener mOnCancelListener;

    public interface OnCancelListener {
        void onCancel();
    }

    public DefaultRationale(OnCancelListener listener) {
        this.mOnCancelListener = listener;
    }

    public DefaultRationale() {
    }

    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        List<String> permissionNames = PermissionHelper.transformText(context, permissions);
        String message = context.getString(R.string.runtime_permissions_rationale_content, TextUtils.join(", ", permissionNames));
        DJIDialog dialog = DJIDialogBuilder.createDJIDialog(context, DJIDialogType.MEDIUM, DJIDialog.DJIDialogTheme.WHITE);
        dialog.setContent(message).setLeftBtn(context.getResources().getString(R.string.runtime_permissions_rationale_cancel), new DialogInterface.OnClickListener() {
            /* class com.dji.permission.DefaultRationale.AnonymousClass2 */

            public void onClick(DialogInterface dialog, int which) {
                executor.cancel();
                if (DefaultRationale.this.mOnCancelListener != null) {
                    DefaultRationale.this.mOnCancelListener.onCancel();
                }
            }
        }).setRightBtn(context.getResources().getString(R.string.runtime_permissions_rationale_goto_settings), new DialogInterface.OnClickListener() {
            /* class com.dji.permission.DefaultRationale.AnonymousClass1 */

            public void onClick(DialogInterface dialog, int which) {
                executor.execute();
            }
        }).setContentTextGravity(GravityCompat.START);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        this.mIsShown = true;
    }

    public boolean isShown() {
        return this.mIsShown;
    }

    public boolean setShown(boolean shown) {
        this.mIsShown = shown;
        return shown;
    }
}
