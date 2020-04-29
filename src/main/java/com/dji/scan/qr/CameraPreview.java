package com.dji.scan.qr;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.dji.pubmodule.R;
import com.dji.scan.qr.camera.CameraInstance;
import com.dji.scan.qr.camera.CameraSettings;
import com.dji.scan.qr.camera.CameraSurface;
import com.dji.scan.qr.camera.CenterCropStrategy;
import com.dji.scan.qr.camera.DisplayConfiguration;
import com.dji.scan.qr.camera.FitCenterStrategy;
import com.dji.scan.qr.camera.FitXYStrategy;
import com.dji.scan.qr.camera.PreviewScalingStrategy;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class CameraPreview extends ViewGroup {
    private static final int ROTATION_LISTENER_DELAY_MS = 250;
    /* access modifiers changed from: private */
    public static final String TAG = CameraPreview.class.getSimpleName();
    private CameraInstance cameraInstance;
    private CameraSettings cameraSettings = new CameraSettings();
    private Size containerSize;
    /* access modifiers changed from: private */
    public Size currentSurfaceSize;
    private DisplayConfiguration displayConfiguration;
    /* access modifiers changed from: private */
    public final StateListener fireState = new StateListener() {
        /* class com.dji.scan.qr.CameraPreview.AnonymousClass5 */

        public void previewSized() {
            for (StateListener listener : CameraPreview.this.stateListeners) {
                listener.previewSized();
            }
        }

        public void previewStarted() {
            for (StateListener listener : CameraPreview.this.stateListeners) {
                listener.previewStarted();
            }
        }

        public void previewStopped() {
            for (StateListener listener : CameraPreview.this.stateListeners) {
                listener.previewStopped();
            }
        }

        public void cameraError(Exception error) {
            for (StateListener listener : CameraPreview.this.stateListeners) {
                listener.cameraError(error);
            }
        }

        public void cameraClosed() {
            for (StateListener listener : CameraPreview.this.stateListeners) {
                listener.cameraClosed();
            }
        }
    };
    private Rect framingRect = null;
    private Size framingRectSize = null;
    private double marginFraction = 0.1d;
    private int openedOrientation = -1;
    private boolean previewActive = false;
    private Rect previewFramingRect = null;
    private PreviewScalingStrategy previewScalingStrategy = null;
    private Size previewSize;
    private RotationCallback rotationCallback = new RotationCallback() {
        /* class com.dji.scan.qr.CameraPreview.AnonymousClass4 */

        public void onRotationChanged(int rotation) {
            CameraPreview.this.stateHandler.postDelayed(new Runnable() {
                /* class com.dji.scan.qr.CameraPreview.AnonymousClass4.AnonymousClass1 */

                public void run() {
                    CameraPreview.this.rotationChanged();
                }
            }, 250);
        }
    };
    private RotationListener rotationListener;
    private final Handler.Callback stateCallback = new Handler.Callback() {
        /* class com.dji.scan.qr.CameraPreview.AnonymousClass3 */

        public boolean handleMessage(Message message) {
            if (message.what == R.id.zxing_prewiew_size_ready) {
                CameraPreview.this.previewSized((Size) message.obj);
                return true;
            }
            if (message.what == R.id.zxing_camera_error) {
                Exception error = (Exception) message.obj;
                if (CameraPreview.this.isActive()) {
                    CameraPreview.this.pause();
                    CameraPreview.this.fireState.cameraError(error);
                }
            } else if (message.what == R.id.zxing_camera_closed) {
                CameraPreview.this.fireState.cameraClosed();
            }
            return false;
        }
    };
    /* access modifiers changed from: private */
    public Handler stateHandler;
    /* access modifiers changed from: private */
    public List<StateListener> stateListeners = new ArrayList();
    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        /* class com.dji.scan.qr.CameraPreview.AnonymousClass2 */

        public void surfaceCreated(SurfaceHolder holder) {
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            Size unused = CameraPreview.this.currentSurfaceSize = null;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (holder == null) {
                Log.e(CameraPreview.TAG, "*** WARNING *** surfaceChanged() gave us a null surface!");
                return;
            }
            Size unused = CameraPreview.this.currentSurfaceSize = new Size(width, height);
            CameraPreview.this.startPreviewIfReady();
        }
    };
    private Rect surfaceRect;
    private SurfaceView surfaceView;
    private TextureView textureView;
    private boolean torchOn = false;
    private boolean useTextureView = false;
    private WindowManager windowManager;

    public interface StateListener {
        void cameraClosed();

        void cameraError(Exception exc);

        void previewSized();

        void previewStarted();

        void previewStopped();
    }

    @TargetApi(14)
    private TextureView.SurfaceTextureListener surfaceTextureListener() {
        return new TextureView.SurfaceTextureListener() {
            /* class com.dji.scan.qr.CameraPreview.AnonymousClass1 */

            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                onSurfaceTextureSizeChanged(surface, width, height);
            }

            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                Size unused = CameraPreview.this.currentSurfaceSize = new Size(width, height);
                CameraPreview.this.startPreviewIfReady();
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        };
    }

    public CameraPreview(Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (getBackground() == null) {
            setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        }
        initializeAttributes(attrs);
        this.windowManager = (WindowManager) context.getSystemService("window");
        this.stateHandler = new Handler(this.stateCallback);
        this.rotationListener = new RotationListener();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupSurfaceView();
    }

    /* access modifiers changed from: protected */
    public void initializeAttributes(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.zxing_camera_preview);
        int framingRectWidth = (int) styledAttributes.getDimension(R.styleable.zxing_camera_preview_zxing_framing_rect_width, -1.0f);
        int framingRectHeight = (int) styledAttributes.getDimension(R.styleable.zxing_camera_preview_zxing_framing_rect_height, -1.0f);
        if (framingRectWidth > 0 && framingRectHeight > 0) {
            this.framingRectSize = new Size(framingRectWidth, framingRectHeight);
        }
        this.useTextureView = styledAttributes.getBoolean(R.styleable.zxing_camera_preview_zxing_use_texture_view, true);
        int scalingStrategyNumber = styledAttributes.getInteger(R.styleable.zxing_camera_preview_zxing_preview_scaling_strategy, -1);
        if (scalingStrategyNumber == 1) {
            this.previewScalingStrategy = new CenterCropStrategy();
        } else if (scalingStrategyNumber == 2) {
            this.previewScalingStrategy = new FitCenterStrategy();
        } else if (scalingStrategyNumber == 3) {
            this.previewScalingStrategy = new FitXYStrategy();
        }
        styledAttributes.recycle();
    }

    /* access modifiers changed from: private */
    public void rotationChanged() {
        if (isActive() && getDisplayRotation() != this.openedOrientation) {
            pause();
            resume();
        }
    }

    @SuppressLint({"NewAPI"})
    private void setupSurfaceView() {
        if (!this.useTextureView || Build.VERSION.SDK_INT < 14) {
            this.surfaceView = new SurfaceView(getContext());
            if (Build.VERSION.SDK_INT < 11) {
                this.surfaceView.getHolder().setType(3);
            }
            this.surfaceView.getHolder().addCallback(this.surfaceCallback);
            addView(this.surfaceView);
            return;
        }
        this.textureView = new TextureView(getContext());
        this.textureView.setSurfaceTextureListener(surfaceTextureListener());
        addView(this.textureView);
    }

    public void addStateListener(StateListener listener) {
        this.stateListeners.add(listener);
    }

    private void calculateFrames() {
        if (this.containerSize == null || this.previewSize == null || this.displayConfiguration == null) {
            this.previewFramingRect = null;
            this.framingRect = null;
            this.surfaceRect = null;
            throw new IllegalStateException("containerSize or previewSize is not set yet");
        }
        int previewWidth = this.previewSize.width;
        int previewHeight = this.previewSize.height;
        int width = this.containerSize.width;
        int height = this.containerSize.height;
        this.surfaceRect = this.displayConfiguration.scalePreview(this.previewSize);
        this.framingRect = calculateFramingRect(new Rect(0, 0, width, height), this.surfaceRect);
        Rect frameInPreview = new Rect(this.framingRect);
        frameInPreview.offset(-this.surfaceRect.left, -this.surfaceRect.top);
        this.previewFramingRect = new Rect((frameInPreview.left * previewWidth) / this.surfaceRect.width(), (frameInPreview.top * previewHeight) / this.surfaceRect.height(), (frameInPreview.right * previewWidth) / this.surfaceRect.width(), (frameInPreview.bottom * previewHeight) / this.surfaceRect.height());
        if (this.previewFramingRect.width() <= 0 || this.previewFramingRect.height() <= 0) {
            this.previewFramingRect = null;
            this.framingRect = null;
            Log.w(TAG, "Preview frame is too small");
            return;
        }
        this.fireState.previewSized();
    }

    public void setTorch(boolean on) {
        this.torchOn = on;
        if (this.cameraInstance != null) {
            this.cameraInstance.setTorch(on);
        }
    }

    private void containerSized(Size containerSize2) {
        this.containerSize = containerSize2;
        if (this.cameraInstance != null && this.cameraInstance.getDisplayConfiguration() == null) {
            this.displayConfiguration = new DisplayConfiguration(getDisplayRotation(), containerSize2);
            this.displayConfiguration.setPreviewScalingStrategy(getPreviewScalingStrategy());
            this.cameraInstance.setDisplayConfiguration(this.displayConfiguration);
            this.cameraInstance.configureCamera();
            if (this.torchOn) {
                this.cameraInstance.setTorch(this.torchOn);
            }
        }
    }

    public void setPreviewScalingStrategy(PreviewScalingStrategy previewScalingStrategy2) {
        this.previewScalingStrategy = previewScalingStrategy2;
    }

    public PreviewScalingStrategy getPreviewScalingStrategy() {
        if (this.previewScalingStrategy != null) {
            return this.previewScalingStrategy;
        }
        if (this.textureView != null) {
            return new CenterCropStrategy();
        }
        return new FitCenterStrategy();
    }

    /* access modifiers changed from: private */
    public void previewSized(Size size) {
        this.previewSize = size;
        if (this.containerSize != null) {
            calculateFrames();
            requestLayout();
            startPreviewIfReady();
        }
    }

    /* access modifiers changed from: protected */
    public Matrix calculateTextureTransform(Size textureSize, Size previewSize2) {
        float scaleX;
        float scaleY;
        float ratioTexture = ((float) textureSize.width) / ((float) textureSize.height);
        float ratioPreview = ((float) previewSize2.width) / ((float) previewSize2.height);
        if (ratioTexture < ratioPreview) {
            scaleX = ratioPreview / ratioTexture;
            scaleY = 1.0f;
        } else {
            scaleX = 1.0f;
            scaleY = ratioTexture / ratioPreview;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
        matrix.postTranslate((((float) textureSize.width) - (((float) textureSize.width) * scaleX)) / 2.0f, (((float) textureSize.height) - (((float) textureSize.height) * scaleY)) / 2.0f);
        return matrix;
    }

    /* access modifiers changed from: private */
    public void startPreviewIfReady() {
        if (this.currentSurfaceSize != null && this.previewSize != null && this.surfaceRect != null) {
            if (this.surfaceView != null && this.currentSurfaceSize.equals(new Size(this.surfaceRect.width(), this.surfaceRect.height()))) {
                startCameraPreview(new CameraSurface(this.surfaceView.getHolder()));
            } else if (this.textureView != null && Build.VERSION.SDK_INT >= 14 && this.textureView.getSurfaceTexture() != null) {
                if (this.previewSize != null) {
                    this.textureView.setTransform(calculateTextureTransform(new Size(this.textureView.getWidth(), this.textureView.getHeight()), this.previewSize));
                }
                startCameraPreview(new CameraSurface(this.textureView.getSurfaceTexture()));
            }
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        containerSized(new Size(r - l, b - t));
        if (this.surfaceView != null) {
            if (this.surfaceRect == null) {
                this.surfaceView.layout(0, 0, getWidth(), getHeight());
            } else {
                this.surfaceView.layout(this.surfaceRect.left, this.surfaceRect.top, this.surfaceRect.right, this.surfaceRect.bottom);
            }
        } else if (this.textureView != null && Build.VERSION.SDK_INT >= 14) {
            this.textureView.layout(0, 0, getWidth(), getHeight());
        }
    }

    public Rect getFramingRect() {
        return this.framingRect;
    }

    public Rect getPreviewFramingRect() {
        return this.previewFramingRect;
    }

    public CameraSettings getCameraSettings() {
        return this.cameraSettings;
    }

    public void setCameraSettings(CameraSettings cameraSettings2) {
        this.cameraSettings = cameraSettings2;
    }

    public void resume() {
        Util.validateMainThread();
        Log.d(TAG, "resume()");
        initCamera();
        if (this.currentSurfaceSize != null) {
            startPreviewIfReady();
        } else if (this.surfaceView != null) {
            this.surfaceView.getHolder().addCallback(this.surfaceCallback);
        } else if (this.textureView != null && Build.VERSION.SDK_INT >= 14) {
            if (this.textureView.isAvailable()) {
                surfaceTextureListener().onSurfaceTextureAvailable(this.textureView.getSurfaceTexture(), this.textureView.getWidth(), this.textureView.getHeight());
            } else {
                this.textureView.setSurfaceTextureListener(surfaceTextureListener());
            }
        }
        requestLayout();
        this.rotationListener.listen(getContext(), this.rotationCallback);
    }

    public void pause() {
        Util.validateMainThread();
        Log.d(TAG, "pause()");
        this.openedOrientation = -1;
        if (this.cameraInstance != null) {
            this.cameraInstance.close();
            this.cameraInstance = null;
            this.previewActive = false;
        } else {
            this.stateHandler.sendEmptyMessage(R.id.zxing_camera_closed);
        }
        if (this.currentSurfaceSize == null && this.surfaceView != null) {
            this.surfaceView.getHolder().removeCallback(this.surfaceCallback);
        }
        if (this.currentSurfaceSize == null && this.textureView != null && Build.VERSION.SDK_INT >= 14) {
            this.textureView.setSurfaceTextureListener(null);
        }
        this.containerSize = null;
        this.previewSize = null;
        this.previewFramingRect = null;
        this.rotationListener.stop();
        this.fireState.previewStopped();
    }

    public void pauseAndWait() {
        CameraInstance instance = getCameraInstance();
        pause();
        long startTime = System.nanoTime();
        while (instance != null && !instance.isCameraClosed() && System.nanoTime() - startTime <= 2000000000) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public Size getFramingRectSize() {
        return this.framingRectSize;
    }

    public void setFramingRectSize(Size framingRectSize2) {
        this.framingRectSize = framingRectSize2;
    }

    public double getMarginFraction() {
        return this.marginFraction;
    }

    public void setMarginFraction(double marginFraction2) {
        if (marginFraction2 >= 0.5d) {
            throw new IllegalArgumentException("The margin fraction must be less than 0.5");
        }
        this.marginFraction = marginFraction2;
    }

    public boolean isUseTextureView() {
        return this.useTextureView;
    }

    public void setUseTextureView(boolean useTextureView2) {
        this.useTextureView = useTextureView2;
    }

    /* access modifiers changed from: protected */
    public boolean isActive() {
        return this.cameraInstance != null;
    }

    private int getDisplayRotation() {
        return this.windowManager.getDefaultDisplay().getRotation();
    }

    private void initCamera() {
        if (this.cameraInstance != null) {
            Log.w(TAG, "initCamera called twice");
            return;
        }
        this.cameraInstance = createCameraInstance();
        this.cameraInstance.setReadyHandler(this.stateHandler);
        this.cameraInstance.open();
        this.openedOrientation = getDisplayRotation();
    }

    /* access modifiers changed from: protected */
    public CameraInstance createCameraInstance() {
        CameraInstance cameraInstance2 = new CameraInstance(getContext());
        cameraInstance2.setCameraSettings(this.cameraSettings);
        return cameraInstance2;
    }

    private void startCameraPreview(CameraSurface surface) {
        if (!this.previewActive && this.cameraInstance != null) {
            Log.i(TAG, "Starting preview");
            this.cameraInstance.setSurface(surface);
            this.cameraInstance.startPreview();
            this.previewActive = true;
            previewStarted();
            this.fireState.previewStarted();
        }
    }

    /* access modifiers changed from: protected */
    public void previewStarted() {
    }

    public CameraInstance getCameraInstance() {
        return this.cameraInstance;
    }

    public boolean isPreviewActive() {
        return this.previewActive;
    }

    /* access modifiers changed from: protected */
    public Rect calculateFramingRect(Rect container, Rect surface) {
        Rect intersection = new Rect(container);
        boolean intersect = intersection.intersect(surface);
        if (this.framingRectSize != null) {
            intersection.inset(Math.max(0, (intersection.width() - this.framingRectSize.width) / 2), Math.max(0, (intersection.height() - this.framingRectSize.height) / 2));
        } else {
            int margin = (int) Math.min(((double) intersection.width()) * this.marginFraction, ((double) intersection.height()) * this.marginFraction);
            intersection.inset(margin, margin);
            if (intersection.height() > intersection.width()) {
                intersection.inset(0, (intersection.height() - intersection.width()) / 2);
            }
        }
        return intersection;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle myState = new Bundle();
        myState.putParcelable("super", superState);
        myState.putBoolean("torch", this.torchOn);
        return myState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof Bundle)) {
            super.onRestoreInstanceState(state);
            return;
        }
        Bundle myState = (Bundle) state;
        super.onRestoreInstanceState(myState.getParcelable("super"));
        setTorch(myState.getBoolean("torch"));
    }

    public boolean isCameraClosed() {
        return this.cameraInstance == null || this.cameraInstance.isCameraClosed();
    }
}
