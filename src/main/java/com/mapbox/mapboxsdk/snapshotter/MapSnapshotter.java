package com.mapbox.mapboxsdk.snapshotter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.attribution.AttributionLayout;
import com.mapbox.mapboxsdk.attribution.AttributionMeasure;
import com.mapbox.mapboxsdk.attribution.AttributionParser;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.TelemetryDefinition;
import com.mapbox.mapboxsdk.storage.FileSource;
import com.mapbox.mapboxsdk.utils.FontUtils;
import com.mapbox.mapboxsdk.utils.ThreadUtils;

@UiThread
public class MapSnapshotter {
    private static final int LOGO_MARGIN_DP = 4;
    private static final String TAG = "Mbgl-MapSnapshotter";
    /* access modifiers changed from: private */
    @Nullable
    public SnapshotReadyCallback callback;
    private final Context context;
    @Nullable
    private ErrorHandler errorHandler;
    @Keep
    private long nativePtr = 0;

    public interface ErrorHandler {
        void onError(String str);
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReady(MapSnapshot mapSnapshot);
    }

    /* access modifiers changed from: protected */
    @Keep
    public native void finalize() throws Throwable;

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeCancel();

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeInitialize(MapSnapshotter mapSnapshotter, FileSource fileSource, float f, int i, int i2, String str, String str2, LatLngBounds latLngBounds, CameraPosition cameraPosition, boolean z, String str3);

    /* access modifiers changed from: protected */
    @Keep
    public native void nativeStart();

    @Keep
    public native void setCameraPosition(CameraPosition cameraPosition);

    @Keep
    public native void setRegion(LatLngBounds latLngBounds);

    @Keep
    public native void setSize(int i, int i2);

    @Keep
    public native void setStyleJson(String str);

    @Keep
    public native void setStyleUrl(String str);

    public static class Options {
        private String apiBaseUrl;
        /* access modifiers changed from: private */
        public CameraPosition cameraPosition;
        /* access modifiers changed from: private */
        public int height;
        /* access modifiers changed from: private */
        public String localIdeographFontFamily = MapboxConstants.DEFAULT_FONT;
        /* access modifiers changed from: private */
        public float pixelRatio = 1.0f;
        /* access modifiers changed from: private */
        public LatLngBounds region;
        /* access modifiers changed from: private */
        public boolean showLogo = true;
        /* access modifiers changed from: private */
        public String styleJson;
        /* access modifiers changed from: private */
        public String styleUri = Style.MAPBOX_STREETS;
        /* access modifiers changed from: private */
        public int width;

        public Options(int width2, int height2) {
            if (width2 == 0 || height2 == 0) {
                throw new IllegalArgumentException("Unable to create a snapshot with width or height set to 0");
            }
            this.width = width2;
            this.height = height2;
        }

        @NonNull
        public Options withStyle(String uri) {
            this.styleUri = uri;
            return this;
        }

        @NonNull
        public Options withStyleJson(String styleJson2) {
            this.styleJson = styleJson2;
            return this;
        }

        @NonNull
        public Options withRegion(LatLngBounds region2) {
            this.region = region2;
            return this;
        }

        @NonNull
        public Options withPixelRatio(float pixelRatio2) {
            this.pixelRatio = pixelRatio2;
            return this;
        }

        @NonNull
        public Options withCameraPosition(CameraPosition cameraPosition2) {
            this.cameraPosition = cameraPosition2;
            return this;
        }

        @NonNull
        public Options withLogo(boolean showLogo2) {
            this.showLogo = showLogo2;
            return this;
        }

        @NonNull
        public Options withLocalIdeographFontFamily(String fontFamily) {
            this.localIdeographFontFamily = FontUtils.extractValidFont(fontFamily);
            return this;
        }

        @NonNull
        public Options withLocalIdeographFontFamily(String... fontFamilies) {
            this.localIdeographFontFamily = FontUtils.extractValidFont(fontFamilies);
            return this;
        }

        @Deprecated
        @NonNull
        public Options withApiBaseUrl(String apiBaseUrl2) {
            this.apiBaseUrl = apiBaseUrl2;
            return this;
        }

        @NonNull
        public Options withApiBaseUri(String apiBaseUri) {
            this.apiBaseUrl = apiBaseUri;
            return this;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public float getPixelRatio() {
            return this.pixelRatio;
        }

        @Nullable
        public LatLngBounds getRegion() {
            return this.region;
        }

        @Deprecated
        public String getStyleUrl() {
            return this.styleUri;
        }

        public String getStyleUri() {
            return this.styleUri;
        }

        @Nullable
        public CameraPosition getCameraPosition() {
            return this.cameraPosition;
        }

        public String getLocalIdeographFontFamily() {
            return this.localIdeographFontFamily;
        }

        @Nullable
        @Deprecated
        public String getApiBaseUrl() {
            return this.apiBaseUrl;
        }

        @Nullable
        public String getApiBaseUri() {
            return this.apiBaseUrl;
        }
    }

    public MapSnapshotter(@NonNull Context context2, @NonNull Options options) {
        checkThread();
        this.context = context2.getApplicationContext();
        TelemetryDefinition telemetry = Mapbox.getTelemetry();
        if (telemetry != null) {
            telemetry.onAppUserTurnstileEvent();
        }
        FileSource fileSource = FileSource.getInstance(context2);
        String apiBaseUrl = options.getApiBaseUrl();
        if (!TextUtils.isEmpty(apiBaseUrl)) {
            fileSource.setApiBaseUrl(apiBaseUrl);
        }
        nativeInitialize(this, fileSource, options.pixelRatio, options.width, options.height, options.styleUri, options.styleJson, options.region, options.cameraPosition, options.showLogo, options.localIdeographFontFamily);
    }

    public void start(@NonNull SnapshotReadyCallback callback2) {
        start(callback2, null);
    }

    public void start(@NonNull SnapshotReadyCallback callback2, ErrorHandler errorHandler2) {
        if (this.callback != null) {
            throw new IllegalStateException("Snapshotter was already started");
        }
        checkThread();
        this.callback = callback2;
        this.errorHandler = errorHandler2;
        nativeStart();
    }

    public void cancel() {
        checkThread();
        reset();
        nativeCancel();
    }

    /* access modifiers changed from: protected */
    public void addOverlay(@NonNull MapSnapshot mapSnapshot) {
        Bitmap snapshot = mapSnapshot.getBitmap();
        drawOverlay(mapSnapshot, snapshot, new Canvas(snapshot), ((int) this.context.getResources().getDisplayMetrics().density) * 4);
    }

    private void drawOverlay(@NonNull MapSnapshot mapSnapshot, @NonNull Bitmap snapshot, @NonNull Canvas canvas, int margin) {
        AttributionMeasure measure = getAttributionMeasure(mapSnapshot, snapshot, margin);
        AttributionLayout layout = measure.measure();
        drawLogo(mapSnapshot, canvas, margin, layout);
        drawAttribution(mapSnapshot, canvas, measure, layout);
    }

    @NonNull
    private AttributionMeasure getAttributionMeasure(@NonNull MapSnapshot mapSnapshot, @NonNull Bitmap snapshot, int margin) {
        Logo logo = createScaledLogo(snapshot);
        TextView longText = createTextView(mapSnapshot, false, logo.getScale());
        return new AttributionMeasure.Builder().setSnapshot(snapshot).setLogo(logo.getLarge()).setLogoSmall(logo.getSmall()).setTextView(longText).setTextViewShort(createTextView(mapSnapshot, true, logo.getScale())).setMarginPadding((float) margin).build();
    }

    private void drawLogo(MapSnapshot mapSnapshot, @NonNull Canvas canvas, int margin, @NonNull AttributionLayout layout) {
        if (mapSnapshot.isShowLogo()) {
            drawLogo(mapSnapshot.getBitmap(), canvas, margin, layout);
        }
    }

    private void drawLogo(@NonNull Bitmap snapshot, @NonNull Canvas canvas, int margin, AttributionLayout placement) {
        Bitmap selectedLogo = placement.getLogo();
        if (selectedLogo != null) {
            canvas.drawBitmap(selectedLogo, (float) margin, (float) ((snapshot.getHeight() - selectedLogo.getHeight()) - margin), (Paint) null);
        }
    }

    private void drawAttribution(@NonNull MapSnapshot mapSnapshot, @NonNull Canvas canvas, @NonNull AttributionMeasure measure, AttributionLayout layout) {
        PointF anchorPoint = layout.getAnchorPoint();
        if (anchorPoint != null) {
            drawAttribution(canvas, measure, anchorPoint);
            return;
        }
        Bitmap snapshot = mapSnapshot.getBitmap();
        Logger.e(TAG, String.format("Could not generate attribution for snapshot size: %s x %s. You are required to provide your own attribution for the used sources: %s", Integer.valueOf(snapshot.getWidth()), Integer.valueOf(snapshot.getHeight()), mapSnapshot.getAttributions()));
    }

    private void drawAttribution(Canvas canvas, AttributionMeasure measure, PointF anchorPoint) {
        canvas.save();
        canvas.translate(anchorPoint.x, anchorPoint.y);
        measure.getTextView().draw(canvas);
        canvas.restore();
    }

    @NonNull
    private TextView createTextView(@NonNull MapSnapshot mapSnapshot, boolean shortText, float scale) {
        int textColor = ResourcesCompat.getColor(this.context.getResources(), R.color.mapbox_gray_dark, this.context.getTheme());
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        TextView textView = new TextView(this.context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        textView.setSingleLine(true);
        textView.setTextSize(10.0f * scale);
        textView.setTextColor(textColor);
        textView.setBackgroundResource(R.drawable.mapbox_rounded_corner);
        textView.setText(Html.fromHtml(createAttributionString(mapSnapshot, shortText)));
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        return textView;
    }

    @NonNull
    private String createAttributionString(MapSnapshot mapSnapshot, boolean shortText) {
        return new AttributionParser.Options(this.context).withAttributionData(mapSnapshot.getAttributions()).withCopyrightSign(false).withImproveMap(false).build().createAttributionString(shortText);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap}
     arg types: [android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, int]
     candidates:
      ClspMth{android.graphics.Bitmap.createBitmap(android.util.DisplayMetrics, int[], int, int, int, int, android.graphics.Bitmap$Config):android.graphics.Bitmap}
      ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap} */
    private Logo createScaledLogo(@NonNull Bitmap snapshot) {
        Bitmap logo = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.mapbox_logo_icon, null);
        float scale = calculateLogoScale(snapshot, logo);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap helmet = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.mapbox_logo_helmet, null);
        return new Logo(Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true), Bitmap.createBitmap(helmet, 0, 0, helmet.getWidth(), helmet.getHeight(), matrix, true), scale);
    }

    private float calculateLogoScale(Bitmap snapshot, Bitmap logo) {
        DisplayMetrics displayMetrics = this.context.getResources().getDisplayMetrics();
        float widthRatio = (float) (displayMetrics.widthPixels / snapshot.getWidth());
        float heightRatio = (float) (displayMetrics.heightPixels / snapshot.getHeight());
        float calculatedScale = Math.min((((float) logo.getWidth()) / widthRatio) / ((float) logo.getWidth()), (((float) logo.getHeight()) / heightRatio) / ((float) logo.getHeight())) * 2.0f;
        if (calculatedScale > 1.0f) {
            return 1.0f;
        }
        if (calculatedScale < 0.6f) {
            return 0.6f;
        }
        return calculatedScale;
    }

    /* access modifiers changed from: protected */
    @Keep
    public void onSnapshotReady(@NonNull final MapSnapshot snapshot) {
        new Handler().post(new Runnable() {
            /* class com.mapbox.mapboxsdk.snapshotter.MapSnapshotter.AnonymousClass1 */

            public void run() {
                if (MapSnapshotter.this.callback != null) {
                    MapSnapshotter.this.addOverlay(snapshot);
                    MapSnapshotter.this.callback.onSnapshotReady(snapshot);
                    MapSnapshotter.this.reset();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    @Keep
    public void onSnapshotFailed(String reason) {
        if (this.errorHandler != null) {
            this.errorHandler.onError(reason);
            reset();
        }
    }

    private void checkThread() {
        ThreadUtils.checkThread(TAG);
    }

    /* access modifiers changed from: protected */
    public void reset() {
        this.callback = null;
        this.errorHandler = null;
    }

    private class Logo {
        private Bitmap large;
        private float scale;
        private Bitmap small;

        public Logo(Bitmap large2, Bitmap small2, float scale2) {
            this.large = large2;
            this.small = small2;
            this.scale = scale2;
        }

        public Bitmap getLarge() {
            return this.large;
        }

        public Bitmap getSmall() {
            return this.small;
        }

        public float getScale() {
            return this.scale;
        }
    }
}
