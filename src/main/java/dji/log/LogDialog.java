package dji.log;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Keep;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dji.frame.util.V_DisplayUtil;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.R;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCameraGetPushLog;
import dji.midware.data.model.P3.DataCenterGetPushLog;
import dji.midware.data.model.P3.DataFlycGetPushLog;
import dji.midware.data.model.P3.DataGimbalGetPushLog;
import dji.midware.data.model.P3.DataOsdGetPushLog;
import dji.midware.data.model.P3.DataRcGetPushLog;
import dji.midware.usbhost.P3.NativeRcController;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Keep
@EXClassNullAway
public class LogDialog extends Dialog {
    private RadioButton appButton;
    private RadioButton cameraButton;
    private RadioButton centerButton;
    /* access modifiers changed from: private */
    public DeviceType deviceType = DeviceType.APP;
    private RadioButton flycButton;
    private RadioButton gimbalButton;
    private Handler handler = new Handler(new Handler.Callback() {
        /* class dji.log.LogDialog.AnonymousClass4 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LogDialog.this.mRadioGroup.addView((RadioButton) msg.obj, msg.arg1);
                    return false;
                default:
                    return false;
            }
        }
    });
    /* access modifiers changed from: private */
    public int mCheckedId = DeviceType.APP.value();
    /* access modifiers changed from: private */
    public boolean mClosed = true;
    /* access modifiers changed from: private */
    public int mDisplayHeight = 0;
    private ImageView mImgControl = null;
    private ImageView mImgLock = null;
    /* access modifiers changed from: private */
    public boolean mLock = false;
    private TextView mLogView = null;
    /* access modifiers changed from: private */
    public RadioGroup mRadioGroup = null;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    /* access modifiers changed from: private */
    public ScrollView mScrollView = null;
    private Button mSetSreBtn;
    /* access modifiers changed from: private */
    public NumberPicker mSreValue;
    private View.OnClickListener mWidgetClickListener = null;
    private RadioButton osdButton;
    private RadioButton rcButton;

    public LogDialog(Context context) {
        super(context, R.style.LogDialog);
        init();
    }

    private void init() {
        setContentView(R.layout.log_dialog_view);
        this.mScrollView = (ScrollView) findViewById(R.id.log_dlg_scroll);
        this.mLogView = (TextView) findViewById(R.id.log_dlg_content);
        this.mImgControl = (ImageView) findViewById(R.id.log_dlg_control);
        this.mImgLock = (ImageView) findViewById(R.id.log_dlg_lock);
        this.mRadioGroup = (RadioGroup) findViewById(R.id.log_dlg_rg);
        this.mSetSreBtn = (Button) findViewById(R.id.set_sre_button);
        this.mSreValue = (NumberPicker) findViewById(R.id.numberPicker1);
        this.mSreValue.setMinValue(0);
        this.mSreValue.setMaxValue(10);
        this.mSreValue.setValue(4);
        this.mSetSreBtn.setOnClickListener(new View.OnClickListener() {
            /* class dji.log.LogDialog.AnonymousClass1 */

            public void onClick(View v) {
                NativeRcController.rc_set_sre(LogDialog.this.mSreValue.getValue());
            }
        });
        addButton(this.appButton, DeviceType.APP, 0);
        this.mRadioGroup.check(DeviceType.APP.value());
        this.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /* class dji.log.LogDialog.AnonymousClass2 */

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (LogDialog.this.mCheckedId != checkedId) {
                    int unused = LogDialog.this.mCheckedId = checkedId;
                    DeviceType unused2 = LogDialog.this.deviceType = DeviceType.find(checkedId);
                    LogHelper.getIntance().updateLog();
                }
            }
        });
        this.mWidgetClickListener = new View.OnClickListener() {
            /* class dji.log.LogDialog.AnonymousClass3 */

            public void onClick(View v) {
                boolean z = true;
                if (v.getId() == R.id.log_dlg_control) {
                    if (LogDialog.this.mClosed) {
                        WindowManager.LayoutParams attrs = LogDialog.this.getWindow().getAttributes();
                        attrs.height = LogDialog.this.mDisplayHeight;
                        LogDialog.this.getWindow().setAttributes(attrs);
                    } else {
                        WindowManager.LayoutParams attrs2 = LogDialog.this.getWindow().getAttributes();
                        attrs2.height = V_DisplayUtil.dip2px(LogDialog.this.getContext(), 30.0f);
                        LogDialog.this.getWindow().setAttributes(attrs2);
                    }
                    if (!LogDialog.this.mLock) {
                        LogDialog.this.mScrollView.fullScroll(NikonType2MakernoteDirectory.TAG_ADAPTER);
                    }
                    LogDialog logDialog = LogDialog.this;
                    if (LogDialog.this.mClosed) {
                        z = false;
                    }
                    boolean unused = logDialog.mClosed = z;
                    v.setSelected(LogDialog.this.mClosed);
                } else if (v.getId() == R.id.log_dlg_lock) {
                    LogDialog logDialog2 = LogDialog.this;
                    if (LogDialog.this.mLock) {
                        z = false;
                    }
                    boolean unused2 = logDialog2.mLock = z;
                    v.setSelected(LogDialog.this.mLock);
                }
            }
        };
        this.mImgControl.setOnClickListener(this.mWidgetClickListener);
        this.mImgLock.setOnClickListener(this.mWidgetClickListener);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        Context context = getContext();
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        DisplayMetrics d = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(d);
        this.mScreenWidth = d.widthPixels;
        this.mScreenHeight = d.heightPixels;
        attrs.y = V_DisplayUtil.dip2px(context, 40.0f);
        attrs.dimAmount = 0.0f;
        attrs.flags &= -3;
        attrs.flags &= -262145;
        attrs.flags |= 32;
        attrs.flags |= 8;
        attrs.gravity = 49;
        getWindow().setAttributes(attrs);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public void onAttachedToWindow() {
        int width;
        int height;
        super.onAttachedToWindow();
        Log.d("", "click onAttachedToWindow");
        int orientation = getContext().getResources().getConfiguration().orientation;
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        int i = this.mScreenWidth;
        int i2 = this.mScreenHeight;
        if (orientation == 1) {
            width = this.mScreenHeight > this.mScreenWidth ? this.mScreenWidth : this.mScreenHeight;
            height = this.mScreenHeight > this.mScreenWidth ? this.mScreenHeight : this.mScreenWidth;
        } else {
            width = this.mScreenHeight > this.mScreenWidth ? this.mScreenHeight : this.mScreenWidth;
            height = this.mScreenHeight > this.mScreenWidth ? this.mScreenWidth : this.mScreenHeight;
        }
        this.mDisplayHeight = (int) (((float) height) * 0.7f);
        attrs.width = (int) (((float) width) * 0.8f);
        this.mClosed = true;
        attrs.height = V_DisplayUtil.dip2px(getContext(), 30.0f);
        getWindow().setAttributes(attrs);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("", "click onDetachedFromWindow");
    }

    public void updateLog(String log) {
        this.mLogView.setText(log);
        if (!this.mLock) {
            this.mScrollView.fullScroll(NikonType2MakernoteDirectory.TAG_ADAPTER);
        }
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x006d, code lost:
        return r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized android.widget.RadioButton addButton(android.widget.RadioButton r8, dji.midware.data.config.P3.DeviceType r9, int r10) {
        /*
            r7 = this;
            monitor-enter(r7)
            if (r8 != 0) goto L_0x006c
            android.widget.RadioButton r0 = new android.widget.RadioButton     // Catch:{ all -> 0x0070 }
            android.content.Context r3 = r7.getContext()     // Catch:{ all -> 0x0070 }
            r0.<init>(r3)     // Catch:{ all -> 0x0070 }
            android.widget.RadioGroup$LayoutParams r2 = new android.widget.RadioGroup$LayoutParams     // Catch:{ all -> 0x0073 }
            r3 = -1
            r4 = -2
            r2.<init>(r3, r4)     // Catch:{ all -> 0x0073 }
            r3 = 0
            r4 = 10
            r5 = 0
            r6 = 10
            r2.setMargins(r3, r4, r5, r6)     // Catch:{ all -> 0x0073 }
            r0.setLayoutParams(r2)     // Catch:{ all -> 0x0073 }
            int r3 = r9.value()     // Catch:{ all -> 0x0073 }
            r0.setId(r3)     // Catch:{ all -> 0x0073 }
            r3 = 1086324736(0x40c00000, float:6.0)
            r0.setTextSize(r3)     // Catch:{ all -> 0x0073 }
            java.lang.String r1 = r9.toString()     // Catch:{ all -> 0x0073 }
            int r3 = r1.length()     // Catch:{ all -> 0x0073 }
            r4 = 2
            if (r3 <= r4) goto L_0x006e
            r3 = 0
            r4 = 3
            java.lang.String r3 = r1.substring(r3, r4)     // Catch:{ all -> 0x0073 }
        L_0x003c:
            r0.setText(r3)     // Catch:{ all -> 0x0073 }
            int r3 = dji.midware.R.drawable.btn_radio_selector     // Catch:{ all -> 0x0073 }
            r0.setButtonDrawable(r3)     // Catch:{ all -> 0x0073 }
            android.os.Handler r3 = r7.handler     // Catch:{ all -> 0x0073 }
            android.os.Handler r4 = r7.handler     // Catch:{ all -> 0x0073 }
            r5 = 0
            r6 = 0
            android.os.Message r4 = r4.obtainMessage(r5, r10, r6, r0)     // Catch:{ all -> 0x0073 }
            r3.sendMessage(r4)     // Catch:{ all -> 0x0073 }
            java.lang.String r3 = ""
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0073 }
            r4.<init>()     // Catch:{ all -> 0x0073 }
            java.lang.String r5 = "devieceName="
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x0073 }
            java.lang.StringBuilder r4 = r4.append(r1)     // Catch:{ all -> 0x0073 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0073 }
            android.util.Log.d(r3, r4)     // Catch:{ all -> 0x0073 }
            r8 = r0
        L_0x006c:
            monitor-exit(r7)
            return r8
        L_0x006e:
            r3 = r1
            goto L_0x003c
        L_0x0070:
            r3 = move-exception
        L_0x0071:
            monitor-exit(r7)
            throw r3
        L_0x0073:
            r3 = move-exception
            r8 = r0
            goto L_0x0071
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.log.LogDialog.addButton(android.widget.RadioButton, dji.midware.data.config.P3.DeviceType, int):android.widget.RadioButton");
    }

    private void addLog(DeviceType deviceType2, int type, String log) {
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushLog getPushLog) {
        this.cameraButton = addButton(this.cameraButton, DeviceType.CAMERA, 1);
        addLog(DeviceType.CAMERA, getPushLog.getType(), getPushLog.getLog());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushLog getPushLog) {
        this.osdButton = addButton(this.osdButton, DeviceType.OSD, 1);
        addLog(DeviceType.OSD, getPushLog.getType(), getPushLog.getLog());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushLog getPushLog) {
        this.flycButton = addButton(this.flycButton, DeviceType.FLYC, 1);
        addLog(DeviceType.FLYC, getPushLog.getType(), getPushLog.getLog());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushLog getPushLog) {
        this.rcButton = addButton(this.rcButton, DeviceType.RC, 1);
        addLog(DeviceType.RC, getPushLog.getType(), getPushLog.getLog());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCenterGetPushLog getPushLog) {
        this.centerButton = addButton(this.centerButton, DeviceType.CENTER, 1);
        addLog(DeviceType.CENTER, getPushLog.getType(), getPushLog.getLog());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushLog getPushLog) {
        this.gimbalButton = addButton(this.gimbalButton, DeviceType.GIMBAL, 1);
        addLog(DeviceType.GIMBAL, getPushLog.getType(), getPushLog.getLog());
    }
}
