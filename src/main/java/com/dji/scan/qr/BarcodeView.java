package com.dji.scan.qr;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import com.dji.pubmodule.R;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPoint;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Keep
@EXClassNullAway
public class BarcodeView extends CameraPreview {
    /* access modifiers changed from: private */
    public BarcodeCallback callback = null;
    /* access modifiers changed from: private */
    public DecodeMode decodeMode = DecodeMode.NONE;
    private DecoderFactory decoderFactory;
    private DecoderThread decoderThread;
    private final Handler.Callback resultCallback = new Handler.Callback() {
        /* class com.dji.scan.qr.BarcodeView.AnonymousClass1 */

        public boolean handleMessage(Message message) {
            if (message.what == R.id.zxing_decode_succeeded) {
                BarcodeResult result = (BarcodeResult) message.obj;
                if (result == null || BarcodeView.this.callback == null || BarcodeView.this.decodeMode == DecodeMode.NONE) {
                    return true;
                }
                BarcodeView.this.callback.barcodeResult(result);
                if (BarcodeView.this.decodeMode != DecodeMode.SINGLE) {
                    return true;
                }
                BarcodeView.this.stopDecoding();
                return true;
            } else if (message.what == R.id.zxing_decode_failed) {
                return true;
            } else {
                if (message.what != R.id.zxing_possible_result_points) {
                    return false;
                }
                List<ResultPoint> resultPoints = (List) message.obj;
                if (BarcodeView.this.callback == null || BarcodeView.this.decodeMode == DecodeMode.NONE) {
                    return true;
                }
                BarcodeView.this.callback.possibleResultPoints(resultPoints);
                return true;
            }
        }
    };
    private Handler resultHandler;

    @Keep
    private enum DecodeMode {
        NONE,
        SINGLE,
        CONTINUOUS
    }

    public BarcodeView(Context context) {
        super(context);
        initialize();
    }

    public BarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public BarcodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        this.decoderFactory = new DefaultDecoderFactory();
        this.resultHandler = new Handler(this.resultCallback);
    }

    public void setDecoderFactory(DecoderFactory decoderFactory2) {
        Util.validateMainThread();
        this.decoderFactory = decoderFactory2;
        if (this.decoderThread != null) {
            this.decoderThread.setDecoder(createDecoder());
        }
    }

    private Decoder createDecoder() {
        if (this.decoderFactory == null) {
            this.decoderFactory = createDefaultDecoderFactory();
        }
        DecoderResultPointCallback callback2 = new DecoderResultPointCallback();
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, callback2);
        hints.put(DecodeHintType.PURE_BARCODE, Void.class);
        Decoder decoder = this.decoderFactory.createDecoder(hints);
        callback2.setDecoder(decoder);
        return decoder;
    }

    public DecoderFactory getDecoderFactory() {
        return this.decoderFactory;
    }

    public void decodeSingle(BarcodeCallback callback2) {
        this.decodeMode = DecodeMode.SINGLE;
        this.callback = callback2;
        startDecoderThread();
    }

    public void decodeContinuous(BarcodeCallback callback2) {
        this.decodeMode = DecodeMode.CONTINUOUS;
        this.callback = callback2;
        startDecoderThread();
    }

    public void stopDecoding() {
        this.decodeMode = DecodeMode.NONE;
        this.callback = null;
        stopDecoderThread();
    }

    /* access modifiers changed from: protected */
    public DecoderFactory createDefaultDecoderFactory() {
        return new DefaultDecoderFactory();
    }

    private void startDecoderThread() {
        stopDecoderThread();
        if (this.decodeMode != DecodeMode.NONE && isPreviewActive()) {
            this.decoderThread = new DecoderThread(getCameraInstance(), createDecoder(), this.resultHandler);
            this.decoderThread.setCropRect(getPreviewFramingRect());
            this.decoderThread.start();
        }
    }

    /* access modifiers changed from: protected */
    public void previewStarted() {
        super.previewStarted();
        startDecoderThread();
    }

    private void stopDecoderThread() {
        if (this.decoderThread != null) {
            this.decoderThread.stop();
            this.decoderThread = null;
        }
    }

    public void pause() {
        stopDecoderThread();
        super.pause();
    }
}
