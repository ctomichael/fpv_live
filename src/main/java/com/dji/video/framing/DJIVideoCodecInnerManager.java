package com.dji.video.framing;

import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.decoder.ExtraImageReaderManager;
import com.dji.video.framing.internal.decoder.decoderinterface.IDecoderStateListener;

public class DJIVideoCodecInnerManager {
    private static final String TAG = "DJIVideoCodecInnerManager";
    /* access modifiers changed from: private */
    public DJIVideoDecodeDisconnectListener mDJIVideoDecodeDisconnectListener;
    /* access modifiers changed from: private */
    public DJIVideoDecodeInnerListener mDJIVideoDecodeInnerListener;
    private DJIVideoDecoderInterface mDJIVideoDecoderInterface;
    private IDecoderStateListener mIDecoderStateListener = new IDecoderStateListener() {
        /* class com.dji.video.framing.DJIVideoCodecInnerManager.AnonymousClass1 */

        public void onStateChange(DJIVideoDecoder.VideoDecoderState decoderState) {
            VideoLog.w(DJIVideoCodecInnerManager.TAG, "onStateChange() decoderState:" + decoderState, new Object[0]);
            if (DJIVideoCodecInnerManager.this.mDJIVideoDecodeInnerListener != null && decoderState == DJIVideoDecoder.VideoDecoderState.Decoding) {
                DJIVideoCodecInnerManager.this.mDJIVideoDecodeInnerListener.onDecoderInit(true);
            }
            if (DJIVideoCodecInnerManager.this.mDJIVideoDecodeDisconnectListener != null) {
                if (DJIVideoCodecInnerManager.this.mLastDecoderStatus == DJIVideoDecoder.VideoDecoderState.Decoding && decoderState == DJIVideoDecoder.VideoDecoderState.VideoFrameInput) {
                    DJIVideoCodecInnerManager.this.mDJIVideoDecodeDisconnectListener.onDecoderDisconnect();
                }
                if (DJIVideoCodecInnerManager.this.mLastDecoderStatus == DJIVideoDecoder.VideoDecoderState.VideoFrameInput && decoderState == DJIVideoDecoder.VideoDecoderState.Initialized) {
                    DJIVideoCodecInnerManager.this.mDJIVideoDecodeDisconnectListener.onDecoderInputDisconnect();
                }
                if ((DJIVideoCodecInnerManager.this.mLastDecoderStatus == DJIVideoDecoder.VideoDecoderState.VideoFrameInput || DJIVideoCodecInnerManager.this.mLastDecoderStatus == DJIVideoDecoder.VideoDecoderState.Initialized) && decoderState == DJIVideoDecoder.VideoDecoderState.Decoding) {
                    DJIVideoCodecInnerManager.this.mDJIVideoDecodeDisconnectListener.onDecoderReconnect();
                }
                DJIVideoDecoder.VideoDecoderState unused = DJIVideoCodecInnerManager.this.mLastDecoderStatus = decoderState;
            }
        }
    };
    /* access modifiers changed from: private */
    public DJIVideoDecoder.VideoDecoderState mLastDecoderStatus = DJIVideoDecoder.VideoDecoderState.Initialized;

    public interface DJIVideoDecodeDisconnectListener {
        void onDecoderDisconnect();

        void onDecoderInputDisconnect();

        void onDecoderReconnect();
    }

    public interface DJIVideoDecodeInnerListener {
        void onDecoderInit(boolean z);
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIVideoCodecInnerManager INSTANCE = new DJIVideoCodecInnerManager();

        private SingletonHolder() {
        }
    }

    public static DJIVideoCodecInnerManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setDJIVideoDecoderInterface(DJIVideoDecoderInterface decoderinterface) {
        this.mDJIVideoDecoderInterface = decoderinterface;
        if (this.mDJIVideoDecoderInterface != null && this.mDJIVideoDecoderInterface.getDJIVideoDecoder() != null) {
            this.mDJIVideoDecoderInterface.getDJIVideoDecoder().removeDecoderStateListener(this.mIDecoderStateListener);
            this.mDJIVideoDecoderInterface.getDJIVideoDecoder().addDecoderStateListener(this.mIDecoderStateListener);
        } else if (this.mDJIVideoDecodeInnerListener != null) {
            this.mDJIVideoDecodeInnerListener.onDecoderInit(false);
        }
    }

    public DJIVideoDecoderInterface getDJIVideoDecoderInterface() {
        return this.mDJIVideoDecoderInterface;
    }

    public void addDJIVideoDecodeInnerListener(DJIVideoDecodeInnerListener listener) {
        this.mDJIVideoDecodeInnerListener = listener;
    }

    public void releaseDJIVideoDecodeInnerListener() {
        this.mDJIVideoDecodeInnerListener = null;
    }

    public void addDJIVideoDecodeDisconnectListener(DJIVideoDecodeDisconnectListener listener) {
        this.mDJIVideoDecodeDisconnectListener = listener;
    }

    public void releaseDJIVideoDecodeDisconnectListener() {
        this.mDJIVideoDecodeDisconnectListener = null;
    }

    public boolean startQuickShotImageGet(ExtraImageReaderManager.ExtraImageReaderCallback callback) {
        if (this.mDJIVideoDecoderInterface == null || this.mDJIVideoDecoderInterface.getDJIVideoDecoder() == null) {
            return false;
        }
        return this.mDJIVideoDecoderInterface.getDJIVideoDecoder().startGetAsyncRgbaData(callback);
    }

    public void stopQuickShotImageGet() {
        if (this.mDJIVideoDecoderInterface != null && this.mDJIVideoDecoderInterface.getDJIVideoDecoder() != null) {
            this.mDJIVideoDecoderInterface.getDJIVideoDecoder().stopGetAsyncRgbaData(false);
        }
    }
}
