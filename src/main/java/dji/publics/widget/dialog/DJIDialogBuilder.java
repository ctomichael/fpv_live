package dji.publics.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import dji.publics.widget.dialog.DJIDialog;

public class DJIDialogBuilder {
    public static DJIDialog createDJIDialog(Context context) {
        return new DJIDialog(context, DJIDialogType.SMALL, DJIDialog.DJIDialogTheme.BLACK);
    }

    public static DJIDialog createDJIDialog(Context context, DJIDialogType type, DJIDialog.DJIDialogTheme theme) {
        return new DJIDialog(context, type, theme);
    }

    public static DJIDialog createDJIViewDialog(Context context, DJIDialogType type, DJIDialog.DJIDialogTheme theme, View mainView, View topView) {
        DJIDialog dialog = new DJIDialog(context, type, theme);
        if (mainView != null) {
            dialog.setCustomView(mainView);
        }
        if (topView != null) {
            dialog.addTopView(topView);
        }
        return dialog;
    }

    public static DJIProgressDialog createDJIProgressDialog(Context context) {
        return new DJIProgressDialog(context, DJIDialogType.SMALL, DJIDialog.DJIDialogTheme.BLACK);
    }

    public static DJIProgressDialog createDJIProgressDialog(Context context, DJIDialogType type, DJIDialog.DJIDialogTheme theme) {
        return new DJIProgressDialog(context, type, theme);
    }

    public static DJILargeCustomDialog createDJILargeCustomDialog(Context context) {
        return createDJILargeCustomDialog(context, DJIDialogType.LARGE, DJIDialog.DJIDialogTheme.BLACK);
    }

    public static DJILargeCustomDialog createDJILargeCustomDialog(Context context, DJIDialogType type, DJIDialog.DJIDialogTheme theme) {
        return new DJILargeCustomDialog(context, type, theme);
    }

    public static DJICustomDialog createDJICustomDialog(Context context) {
        return createDJICustomDialog(context, DJIDialogType.MEDIUM, DJIDialog.DJIDialogTheme.BLACK);
    }

    public static DJICustomDialog createDJICustomDialog(Context context, DJIDialogType type, DJIDialog.DJIDialogTheme theme) {
        return new DJICustomDialog(context, type, theme);
    }

    public static DJIEditDialog createDJIEditDialog(Context context) {
        return new DJIEditDialog(context, DJIDialogType.MEDIUM, DJIDialog.DJIDialogTheme.BLACK);
    }

    public static DJIEditDialog createDJIEditDialog(Context context, DJIDialogType type, DJIDialog.DJIDialogTheme theme) {
        return new DJIEditDialog(context, type, theme);
    }

    public static DJIListDialog createDJIListDialog(Context context) {
        return new DJIListDialog(context, DJIDialogType.MEDIUM, DJIDialog.DJIDialogTheme.BLACK);
    }

    public static DJIListDialog createDJIListDialog(Context context, DJIDialogType type, DJIDialog.DJIDialogTheme theme) {
        return new DJIListDialog(context, type, theme);
    }

    public static DJIListExtDialog createDJIListExtDialog(Context context) {
        return new DJIListExtDialog(context, DJIDialogType.MEDIUM, DJIDialog.DJIDialogTheme.BLACK);
    }

    public static DJIListExtDialog createDJIListExtDialog(Context context, DJIDialogType type, DJIDialog.DJIDialogTheme theme) {
        return new DJIListExtDialog(context, type, theme);
    }

    public static DJITopImageDialog createDJITopImageDialog(Context context) {
        return new DJITopImageDialog(context, DJIDialogType.MEDIUM, DJIDialog.DJIDialogTheme.WHITE);
    }

    public static DJITopImageDialog createDJITopImageDialog(Context context, DJIDialogType type, DJIDialog.DJIDialogTheme theme) {
        return new DJITopImageDialog(context, type, theme);
    }

    public static DJIDialog showNormalConfirmDialog(Context context, int titleId, int contentId) {
        return showNormalConfirmDialog(context, titleId == 0 ? "" : context.getString(titleId), contentId == 0 ? "" : context.getString(contentId));
    }

    public static DJIDialog showNormalConfirmDialog(Context context, String title, String content) {
        return showNormalConfirmDialog(context, title, content, new DialogInterface.OnClickListener() {
            /* class dji.publics.widget.dialog.DJIDialogBuilder.AnonymousClass1 */

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static DJIDialog showNormalConfirmDialog(Context context, int titleId, int contentId, DialogInterface.OnClickListener okListener) {
        return showNormalConfirmDialog(context, titleId == 0 ? "" : context.getString(titleId), contentId == 0 ? "" : context.getString(contentId), okListener);
    }

    public static DJIDialog showNormalConfirmDialog(Context context, String title, String content, DialogInterface.OnClickListener okListener) {
        return showNormalConfirmDialog(context, DJIDialogType.SMALL, DJIDialog.DJIDialogTheme.BLACK, title, content, okListener);
    }

    public static DJIDialog showNormalConfirmDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme, String title, String content, DialogInterface.OnClickListener okListener) {
        return showNormalDialog(context, dialogType, theme, title, content, context.getResources().getString(17039370), "", okListener, null);
    }

    public static DJIDialog showNormalCancelDialog(Context context, int titleId, int contentId) {
        return showNormalCancelDialog(context, titleId == 0 ? "" : context.getString(titleId), contentId == 0 ? "" : context.getString(contentId));
    }

    public static DJIDialog showNormalCancelDialog(Context context, String title, String content) {
        return showNormalCancelDialog(context, title, content, new DialogInterface.OnClickListener() {
            /* class dji.publics.widget.dialog.DJIDialogBuilder.AnonymousClass2 */

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static DJIDialog showNormalCancelDialog(Context context, int titleId, int contentId, DialogInterface.OnClickListener cancelListener) {
        return showNormalCancelDialog(context, titleId == 0 ? "" : context.getString(titleId), contentId == 0 ? "" : context.getString(contentId), cancelListener);
    }

    public static DJIDialog showNormalCancelDialog(Context context, String title, String content, DialogInterface.OnClickListener cancelListener) {
        return showNormalCancelDialog(context, DJIDialogType.SMALL, DJIDialog.DJIDialogTheme.BLACK, title, content, cancelListener);
    }

    public static DJIDialog showNormalCancelDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme, String title, String content, DialogInterface.OnClickListener cancelListener) {
        return showNormalDialog(context, dialogType, theme, title, content, context.getResources().getString(17039360), "", cancelListener, null);
    }

    public static DJIDialog showNormalDialog(Context context, int titleId, int contentId, int leftBtnId, int rightBtnId, DialogInterface.OnClickListener cancelListener, DialogInterface.OnClickListener okListener) {
        String string;
        String string2;
        DJIDialogType dJIDialogType = DJIDialogType.SMALL;
        DJIDialog.DJIDialogTheme dJIDialogTheme = DJIDialog.DJIDialogTheme.BLACK;
        if (titleId == 0) {
            string = "";
        } else {
            string = context.getString(titleId);
        }
        if (contentId == 0) {
            string2 = "";
        } else {
            string2 = context.getString(contentId);
        }
        return showNormalDialog(context, dJIDialogType, dJIDialogTheme, string, string2, leftBtnId == 0 ? context.getResources().getString(17039360) : context.getString(leftBtnId), rightBtnId == 0 ? context.getResources().getString(17039370) : context.getString(rightBtnId), cancelListener, okListener);
    }

    public static DJIDialog showNormalDialog(Context context, String title, String content, String leftBtnStr, String rightBtnStr, DialogInterface.OnClickListener cancelListener, DialogInterface.OnClickListener okListener) {
        String str;
        String str2;
        DJIDialogType dJIDialogType = DJIDialogType.SMALL;
        DJIDialog.DJIDialogTheme dJIDialogTheme = DJIDialog.DJIDialogTheme.BLACK;
        if (TextUtils.isEmpty(leftBtnStr)) {
            str = context.getResources().getString(17039360);
        } else {
            str = leftBtnStr;
        }
        if (TextUtils.isEmpty(rightBtnStr)) {
            str2 = context.getResources().getString(17039370);
        } else {
            str2 = rightBtnStr;
        }
        return showNormalDialog(context, dJIDialogType, dJIDialogTheme, title, content, str, str2, cancelListener, okListener);
    }

    public static DJIDialog showNormalDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme, String title, String content, String leftBtnStr, String rightBtnStr, DialogInterface.OnClickListener cancelListener, DialogInterface.OnClickListener okListener) {
        DJIDialog dialog = new DJIDialog(context, dialogType, theme);
        dialog.setTitle(title);
        dialog.setContent(content);
        if (cancelListener != null) {
            dialog.setLeftBtn(leftBtnStr, cancelListener);
        }
        if (okListener != null) {
            dialog.setRightBtn(rightBtnStr, okListener);
        }
        dialog.show();
        return dialog;
    }

    public static DJIDialog showNormalDialog(Context context, DJIDialogType dialogType, DJIDialog.DJIDialogTheme theme, String title, int icon, String content, String leftBtnStr, String rightBtnStr, DialogInterface.OnClickListener cancelListener, DialogInterface.OnClickListener okListener, boolean showClose, boolean leftHighlight, boolean rightHighlight) {
        DJIDialog dialog = new DJIDialog(context, dialogType, theme);
        dialog.setCloseVisibility(showClose);
        dialog.setTitle(title);
        if (icon > 0 && icon < 10) {
            dialog.setIconType(icon);
        } else if (icon != 0) {
            dialog.setTitleIcon(icon);
        }
        dialog.setContent(content);
        if (cancelListener != null) {
            dialog.setLeftBtn(leftBtnStr, cancelListener, leftHighlight);
        }
        if (okListener != null) {
            dialog.setRightBtn(rightBtnStr, okListener, rightHighlight);
        }
        dialog.show();
        return dialog;
    }
}
