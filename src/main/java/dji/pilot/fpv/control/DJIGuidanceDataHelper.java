package dji.pilot.fpv.control;

import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataFlycGetPushAvoid;
import dji.pilot.publics.objects.DjiSharedPreferencesManager;
import dji.pilot.publics.util.DJICommonUtils;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJIGuidanceDataHelper {
    private static final String KEY_SHOW_QUESTION = "key_show_question";
    private static final String KEY_VPSSHOW_TIP = "key_vpsshow_tip";
    public static final String STR_CFG_BRAKE = "g_config.avoid_obstacle_limit_cfg.avoid_obstacle_enable_0";
    public static final String STR_CFG_BRAKE_USER = "g_config.avoid_obstacle_limit_cfg.user_avoid_enable_0";
    public static final String STR_CFG_GUIDANCE = "g_config.mvo_cfg.mvo_func_en_0";
    private static volatile boolean mbAss = false;
    private static volatile boolean mbGuidance = false;
    private static boolean mbShowDlg = false;
    private static boolean mbShowQuestionDlg = true;
    private final Context mContext;

    public enum GUIDANCE_TIP {
        REMOVE_ALL
    }

    public static boolean isGuidanceEnable() {
        return mbGuidance;
    }

    public static boolean isAssWork() {
        return mbAss;
    }

    public static void updateGuidance(boolean guidance) {
        updateGuidance(guidance, true);
    }

    public static void updateGuidance(boolean guidance, boolean showTip) {
        if (mbGuidance != guidance) {
            mbGuidance = guidance;
            if (!guidance) {
                EventBus.getDefault().post(GUIDANCE_TIP.REMOVE_ALL);
            }
        }
    }

    public static void updateAss(boolean ass) {
        updateAss(ass, true);
    }

    public static void updateAss(boolean ass, boolean showTip) {
        if (mbAss != ass) {
            mbAss = ass;
        }
    }

    public static boolean canShowTip() {
        return mbShowDlg;
    }

    public static void updateShowTip(Context context, boolean show) {
        if (mbShowDlg != show) {
            mbShowDlg = show;
            DjiSharedPreferencesManager.putBoolean(context, DJICommonUtils.generateProductSpKey(KEY_VPSSHOW_TIP), show);
            if (!show) {
                EventBus.getDefault().post(DataFlycGetPushAvoid.getInstance());
            }
        }
    }

    public static boolean needShowDlg() {
        return mbShowQuestionDlg;
    }

    public static void updateShowDlg(Context context, boolean show) {
        if (mbShowQuestionDlg != show) {
            mbShowQuestionDlg = show;
            DjiSharedPreferencesManager.putBoolean(context, DJICommonUtils.generateProductSpKey(KEY_SHOW_QUESTION), show);
        }
    }

    public void handleProductTypeChanged(ProductType type) {
        if (DJICommonUtils.hasGuidance(type)) {
            mbShowDlg = DjiSharedPreferencesManager.getBoolean(this.mContext, DJICommonUtils.generateProductSpKey(KEY_VPSSHOW_TIP), false);
            mbShowQuestionDlg = DjiSharedPreferencesManager.getBoolean(this.mContext, DJICommonUtils.generateProductSpKey(KEY_SHOW_QUESTION), true);
        }
    }

    public DJIGuidanceDataHelper(Context context) {
        this.mContext = context;
        handleProductTypeChanged(DJIProductManager.getInstance().getType());
    }
}
