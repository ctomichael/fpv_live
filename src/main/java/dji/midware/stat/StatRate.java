package dji.midware.stat;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class StatRate extends StatBase {
    float count = 0.0f;
    long start_time = System.currentTimeMillis();

    public StatRate(String name) {
        super(name);
    }

    public synchronized void addEvent(double value) {
        this.count = (float) (((double) this.count) + value);
    }

    public synchronized double getValue() {
        double d;
        long duration = System.currentTimeMillis() - this.start_time;
        if (duration == 0) {
            d = 0.0d;
        } else {
            d = (double) (this.count / ((float) (duration / 1000)));
        }
        return d;
    }

    public synchronized double getValueAndReset() {
        double re;
        re = getValue();
        this.count = 0.0f;
        this.start_time = System.currentTimeMillis();
        return re;
    }
}
