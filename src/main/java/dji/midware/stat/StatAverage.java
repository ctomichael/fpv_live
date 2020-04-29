package dji.midware.stat;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class StatAverage extends StatBase {
    float sum = 0.0f;
    int times = 0;

    public StatAverage(String name) {
        super(name);
    }

    public synchronized void addEvent(double value) {
        this.sum = (float) (((double) this.sum) + value);
        this.times++;
    }

    public synchronized double getValue() {
        double d;
        if (this.times == 0) {
            d = 0.0d;
        } else {
            d = (double) (this.sum / ((float) this.times));
        }
        return d;
    }

    public synchronized double getValueAndReset() {
        float re;
        if (this.times == 0) {
            re = 0.0f;
        } else {
            re = this.sum / ((float) this.times);
        }
        this.times = 0;
        this.sum = (float) 0;
        return (double) re;
    }
}
