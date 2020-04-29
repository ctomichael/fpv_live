package dji.internal.diagnostics.handler;

import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DJIDiagnosticsImpl;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.logics.CommonUtil;
import dji.midware.data.model.P3.DataDm368_gGetPushCheckStatus;
import java.util.HashSet;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LightbridgeDiagnosticsHandler extends DiagnosticsBaseHandler {
    private boolean isConnectToDeserializerError;
    private boolean isVideoDecoderEncryptionError;
    private boolean lastIsConnectToDeserializerError;
    private boolean lastIsVideoDecoderEncryptionError;

    public LightbridgeDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        if (DataDm368_gGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataDm368_gGetPushCheckStatus.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        if (this.isVideoDecoderEncryptionError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.LIGHT_BRIDGE, DJIDiagnosticsError.VideoDecoder.ENCRYPTION_ERROR));
        }
        if (this.isConnectToDeserializerError) {
            diagnosisList.add(new DJIDiagnosticsImpl(DJIDiagnosticsType.LIGHT_BRIDGE, DJIDiagnosticsError.VideoDecoder.CONNECT_TO_DESERIALIZER_ERROR));
        }
        return diagnosisList;
    }

    public void reset(int componentIndex) {
        this.isVideoDecoderEncryptionError = false;
        this.lastIsVideoDecoderEncryptionError = false;
        this.isConnectToDeserializerError = false;
        this.lastIsConnectToDeserializerError = false;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataDm368_gGetPushCheckStatus status) {
        if (!CommonUtil.isWM240(null)) {
            postToDiagnosticBackgroudThread(new LightbridgeDiagnosticsHandler$$Lambda$0(this, status));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$0$LightbridgeDiagnosticsHandler(DataDm368_gGetPushCheckStatus status) {
        boolean isConnectToDeserializerError2 = status.get68013ConnectStatus();
        boolean isVideoDecoderEncryptionError2 = status.getEncryptStatus();
        if (!DiagnosticsBaseHandler.compareBooleans(new boolean[]{isVideoDecoderEncryptionError2, isConnectToDeserializerError2}, new boolean[]{this.lastIsVideoDecoderEncryptionError, this.lastIsConnectToDeserializerError})) {
            resetLBCheckStatus();
            this.isConnectToDeserializerError = isConnectToDeserializerError2;
            this.isVideoDecoderEncryptionError = isVideoDecoderEncryptionError2;
            notifyChange();
        }
        this.lastIsConnectToDeserializerError = isConnectToDeserializerError2;
        this.lastIsVideoDecoderEncryptionError = isVideoDecoderEncryptionError2;
    }

    private void resetLBCheckStatus() {
        this.isConnectToDeserializerError = false;
        this.isVideoDecoderEncryptionError = false;
    }
}
