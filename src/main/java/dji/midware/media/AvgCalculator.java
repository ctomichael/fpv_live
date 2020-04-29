package dji.midware.media;

import android.util.Log;
import java.util.concurrent.ArrayBlockingQueue;

public class AvgCalculator {
    private static final String TAG = "AvgCalculator";
    private double currentAvg = 0.0d;
    private ArrayBlockingQueue<Number> dataQueue;
    private long firstInputTime = 0;
    private long lastInputTime = 0;
    private int maxCalcNum;
    private long maxCalcTimeInMs;
    private boolean needRemoveLast = false;
    private double validValMax;
    private double validValMin;

    public AvgCalculator(double validValMin2, double validValMax2, int maxCalcNum2) {
        this.validValMin = validValMin2;
        this.validValMax = validValMax2;
        this.maxCalcNum = maxCalcNum2;
        this.dataQueue = new ArrayBlockingQueue<>(maxCalcNum2 + 1);
    }

    public void feedData(double val) {
        if (val <= this.validValMax && val >= this.validValMin) {
            long time = System.currentTimeMillis();
            if (this.lastInputTime == 0) {
                this.firstInputTime = time;
            }
            int calcNum = getCalcNum();
            Number headData = this.dataQueue.peek();
            if (headData == null || (calcNum < this.maxCalcNum && !this.needRemoveLast)) {
                this.currentAvg = ((((double) calcNum) / ((double) (calcNum + 1))) * this.currentAvg) + (val / ((double) (calcNum + 1)));
            } else {
                this.dataQueue.poll();
                this.currentAvg = (this.currentAvg - (headData.doubleValue() / ((double) calcNum))) + (val / ((double) calcNum));
            }
            if (!this.dataQueue.offer(Double.valueOf(val))) {
                Log.e(TAG, "feedData: offer queue failed!");
            }
            this.lastInputTime = time;
        }
    }

    public double getAvgValue() {
        return this.currentAvg;
    }

    public void clear() {
        if (this.dataQueue != null) {
            this.dataQueue.clear();
        }
        this.currentAvg = 0.0d;
        this.firstInputTime = 0;
        this.lastInputTime = 0;
        this.needRemoveLast = false;
    }

    public int getCalcNum() {
        return this.dataQueue.size();
    }
}
