package com.dji.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import dji.publics.widget.dialog.DJIDialog;
import dji.publics.widget.dialog.DJIDialogBuilder;
import dji.publics.widget.dialog.DJIDialogType;
import java.util.List;

public class DJIPermissionSettingDialog {
    private final Context context;

    public interface OnCancelListener {
        void onCancel();
    }

    public DJIPermissionSettingDialog(Context context2) {
        this.context = context2;
    }

    public void showSetting(List<String> permissions, @Nullable final OnCancelListener listener) {
        final SettingService settingService = AndPermission.permissionSetting(this.context);
        List<String> permissionNames = PermissionHelper.transformText(this.context, permissions);
        String message = this.context.getString(R.string.runtime_permissions_rationale_content, TextUtils.join("\n", permissionNames));
        DJIDialog dialog = DJIDialogBuilder.createDJIDialog(this.context, DJIDialogType.MEDIUM, DJIDialog.DJIDialogTheme.WHITE);
        dialog.setContent(message).setLeftBtn(this.context.getResources().getString(R.string.runtime_permissions_rationale_cancel), new DialogInterface.OnClickListener() {
            /* class com.dji.permission.DJIPermissionSettingDialog.AnonymousClass2 */

            public void onClick(DialogInterface dialog, int which) {
                settingService.cancel();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        }).setRightBtn(this.context.getResources().getString(R.string.runtime_permissions_rationale_goto_settings), new DialogInterface.OnClickListener() {
            /* class com.dji.permission.DJIPermissionSettingDialog.AnonymousClass1 */

            public void onClick(DialogInterface dialog, int which) {
                settingService.execute();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
