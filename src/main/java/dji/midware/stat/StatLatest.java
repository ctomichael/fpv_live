package dji.midware.stat;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class StatLatest extends StatBase {
    private static final double LATEST_INITIAL_VALUE = -1.0d;
    double latest = LATEST_INITIAL_VALUE;

    public StatLatest(String name) {
        super(name);
    }

    public synchronized void addEvent(double value) {
        this.latest = value;
    }

    public synchronized double getValue() {
        return this.latest;
    }

    public synchronized double getValueAndReset() {
        double re;
        re = this.latest;
        this.latest = LATEST_INITIAL_VALUE;
        return re;
    }
}
