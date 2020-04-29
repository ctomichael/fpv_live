package dji.common.gimbal;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum BalanceTestResult {
    PASS,
    MARGINAL,
    FAIL,
    UNKNOWN
}
