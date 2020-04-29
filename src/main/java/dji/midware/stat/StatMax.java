package dji.midware.stat;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class StatMax extends StatBase {
    private static final double MAX_INITIAL_VALUE = -1000000.0d;
    double max = MAX_INITIAL_VALUE;

    public StatMax(String name) {
        super(name);
    }

    public synchronized void addEvent(double value) {
        if (value > this.max) {
            this.max = value;
        }
    }

    public synchronized double getValue() {
        return this.max;
    }

    public synchronized double getValueAndReset() {
        double re;
        re = getValue();
        this.max = MAX_INITIAL_VALUE;
        return re;
    }
}
