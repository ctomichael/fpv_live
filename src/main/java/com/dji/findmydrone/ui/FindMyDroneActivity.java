package com.dji.findmydrone.ui;

import android.app.Activity;
import android.app.Presentation;
import android.media.MediaRouter;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dji.component.fpv.base.BulletinBoard;
import com.dji.component.fpv.base.BulletinBoardProvider;
import com.dji.findmydrone.ui.view.right.camera.CameraLiveView;
import com.dji.permission.Action;
import com.dji.permission.AndPermission;
import com.dji.permission.DJIPermissionSettingDialog;
import com.dji.permission.DefaultRationale;
import com.dji.permission.Permission;
import com.dji.permission.PermissionHelper;
import com.dji.relay.R;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.publics.DJIObject.DJICrashExecutor;
import dji.publics.DJIObject.DJICrashHandler;
import java.util.List;

@Keep
@EXClassNullAway
public class FindMyDroneActivity extends Activity implements BulletinBoardProvider {
    public final String TAG = "FindMyDroneActivity";
    private boolean hasNecessaryPermissions;
    private BulletinBoard mBulletinBoard;
    private TextView mHintView = null;
    private CameraLiveView mLCDLiveView = null;
    private MediaRouter mMediaRouter = null;
    private Presentation mPresentation = null;
    private ViewGroup mRootView = null;
    /* access modifiers changed from: private */
    public DJIPermissionSettingDialog mSetting;
    /* access modifiers changed from: private */
    public DefaultRationale rationale;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.fmd_main_activity);
        this.mRootView = (ViewGroup) findViewById(R.id.fmd_root_view);
        this.mLCDLiveView = (CameraLiveView) findViewById(R.id.lcd_camera_liveview);
        this.mHintView = (TextView) findViewById(R.id.hint);
        init();
        this.mBulletinBoard = new BulletinBoard();
        this.mBulletinBoard.onCreate(savedInstanceState);
        this.mMediaRouter = (MediaRouter) getSystemService("media_router");
    }

    private void init() {
        this.hasNecessaryPermissions = PermissionHelper.checkPermissions(this, Permission.WRITE_EXTERNAL_STORAGE, Permission.ACCESS_FINE_LOCATION);
        if (!this.hasNecessaryPermissions) {
            DJICrashHandler.getInstance().init(getApplicationContext());
            this.rationale = new DefaultRationale(new FindMyDroneActivity$$Lambda$0(this));
            this.mSetting = new DJIPermissionSettingDialog(this);
            requestPermission(Permission.WRITE_EXTERNAL_STORAGE, Permission.ACCESS_FINE_LOCATION);
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$init$0$FindMyDroneActivity() {
        DJICrashExecutor.killMySelf(this);
    }

    public BulletinBoard getBulletinBoard() {
        return this.mBulletinBoard;
    }

    private void requestPermission(String... permissions) {
        AndPermission.with((Activity) this).permission(permissions).rationale(this.rationale).onGranted(new Action() {
            /* class com.dji.findmydrone.ui.FindMyDroneActivity.AnonymousClass2 */

            public void onAction(List<String> list) {
                DJILog.saveAsync("ApplicationNew", "TotalTime : " + (System.currentTimeMillis() - System.currentTimeMillis()));
            }
        }).onDenied(new Action() {
            /* class com.dji.findmydrone.ui.FindMyDroneActivity.AnonymousClass1 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: com.dji.permission.AndPermission.hasAlwaysDeniedPermission(android.app.Activity, java.util.List<java.lang.String>):boolean
             arg types: [com.dji.findmydrone.ui.FindMyDroneActivity, java.util.List<java.lang.String>]
             candidates:
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(android.app.Activity, java.lang.String[]):boolean
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(android.app.Fragment, java.util.List<java.lang.String>):boolean
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(android.app.Fragment, java.lang.String[]):boolean
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(android.content.Context, java.util.List<java.lang.String>):boolean
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(android.content.Context, java.lang.String[]):boolean
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(android.support.v4.app.Fragment, java.util.List<java.lang.String>):boolean
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(android.support.v4.app.Fragment, java.lang.String[]):boolean
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(com.dji.permission.source.Source, java.util.List<java.lang.String>):boolean
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(com.dji.permission.source.Source, java.lang.String[]):boolean
              com.dji.permission.AndPermission.hasAlwaysDeniedPermission(android.app.Activity, java.util.List<java.lang.String>):boolean */
            public void onAction(List<String> permissions) {
                if (!FindMyDroneActivity.this.rationale.isShown()) {
                    FindMyDroneActivity.this.mSetting.showSetting(permissions, new FindMyDroneActivity$1$$Lambda$0(this));
                } else if (AndPermission.hasAlwaysDeniedPermission((Activity) FindMyDroneActivity.this, permissions)) {
                    FindMyDroneActivity.this.mSetting.showSetting(permissions, new FindMyDroneActivity$1$$Lambda$1(this));
                } else {
                    DJICrashExecutor.killMySelf(FindMyDroneActivity.this);
                }
            }

            /* access modifiers changed from: package-private */
            public final /* synthetic */ void lambda$onAction$0$FindMyDroneActivity$1() {
                DJICrashExecutor.killMySelf(FindMyDroneActivity.this);
            }

            /* access modifiers changed from: package-private */
            public final /* synthetic */ void lambda$onAction$1$FindMyDroneActivity$1() {
                DJICrashExecutor.killMySelf(FindMyDroneActivity.this);
            }
        }).start();
    }
}
