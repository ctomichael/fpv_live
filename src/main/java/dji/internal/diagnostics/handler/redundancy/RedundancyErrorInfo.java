package dji.internal.diagnostics.handler.redundancy;

import java.util.Objects;

public final class RedundancyErrorInfo {
    private int colorStatus;
    private boolean isRealInAir;
    private boolean isUsrShowEnable;
    private int mDiagnosticsCode;
    private int mImuIndex;
    private String mReason;
    private String mSolution;

    public RedundancyErrorInfo(int diagnosticsCode, int imuIndex) {
        this.mDiagnosticsCode = diagnosticsCode;
        this.mImuIndex = imuIndex;
    }

    public String getReason() {
        return this.mReason;
    }

    public String getSolution() {
        return this.mSolution;
    }

    /* access modifiers changed from: protected */
    public void setReason(String reason) {
        this.mReason = reason;
    }

    /* access modifiers changed from: protected */
    public void setSolution(String solution) {
        this.mSolution = solution;
    }

    public int getDiagnosticsCode() {
        return this.mDiagnosticsCode;
    }

    /* access modifiers changed from: protected */
    public void setDiagnosticsCode(int diagnosticsCode) {
        this.mDiagnosticsCode = diagnosticsCode;
    }

    public int getImuIndex() {
        return this.mImuIndex;
    }

    /* access modifiers changed from: protected */
    public void setImuIndex(int imuIndex) {
        this.mImuIndex = imuIndex;
    }

    public int getColorStatus() {
        return this.colorStatus;
    }

    /* access modifiers changed from: protected */
    public void setColorStatus(int colorStatus2) {
        this.colorStatus = colorStatus2;
    }

    public boolean isRealInAir() {
        return this.isRealInAir;
    }

    /* access modifiers changed from: protected */
    public void setRealInAir(boolean realInAir) {
        this.isRealInAir = realInAir;
    }

    public boolean isUsrShowEnable() {
        return this.isUsrShowEnable;
    }

    /* access modifiers changed from: protected */
    public void setUsrShowEnable(boolean usrShowEnable) {
        this.isUsrShowEnable = usrShowEnable;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RedundancyErrorInfo)) {
            return false;
        }
        RedundancyErrorInfo that = (RedundancyErrorInfo) o;
        if (this.mDiagnosticsCode == that.mDiagnosticsCode && this.mImuIndex == that.mImuIndex) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mDiagnosticsCode), Integer.valueOf(this.mImuIndex));
    }

    public String toString() {
        return this.mImuIndex + "";
    }
}
