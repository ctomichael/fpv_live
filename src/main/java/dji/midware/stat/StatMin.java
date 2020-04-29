package dji.midware.stat;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class StatMin extends StatBase {
    private static final double MIN_INITIAL_VALUE = 1000000.0d;
    double min = 1000000.0d;

    public StatMin(String name) {
        super(name);
    }

    public synchronized void addEvent(double value) {
        if (value < this.min) {
            this.min = value;
        }
    }

    public synchronized double getValue() {
        return this.min;
    }

    public synchronized double getValueAndReset() {
        double re;
        re = getValue();
        this.min = 1000000.0d;
        return re;
    }
}
