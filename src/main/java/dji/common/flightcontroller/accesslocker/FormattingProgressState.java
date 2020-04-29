package dji.common.flightcontroller.accesslocker;

public enum FormattingProgressState {
    NONE(0),
    FORMATTING(1),
    SUCCESSFUL(2),
    FAILURE(3);
    
    private final int data;

    private FormattingProgressState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static FormattingProgressState find(int b) {
        FormattingProgressState result = NONE;
        FormattingProgressState[] values = values();
        for (FormattingProgressState tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}
