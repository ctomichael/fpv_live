package dji.midware.stat;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class StatSum extends StatBase {
    double count = 0.0d;

    public StatSum(String name) {
        super(name);
    }

    public synchronized void addEvent(double value) {
        this.count += value;
    }

    public synchronized double getValue() {
        return this.count;
    }

    public synchronized double getValueAndReset() {
        return getValue();
    }
}
