package org.bouncycastle.crypto.prng;

public class ThreadedSeedGenerator {

    private class SeedGenerator implements Runnable {
        private volatile int counter;
        private volatile boolean stop;

        private SeedGenerator() {
            this.counter = 0;
            this.stop = false;
        }

        public byte[] generateSeed(int i, boolean z) {
            Thread thread = new Thread(this);
            byte[] bArr = new byte[i];
            this.counter = 0;
            this.stop = false;
            thread.start();
            if (!z) {
                i *= 8;
            }
            int i2 = 0;
            int i3 = 0;
            while (i2 < i) {
                while (this.counter == i3) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
                int i4 = this.counter;
                if (z) {
                    bArr[i2] = (byte) (i4 & 255);
                } else {
                    int i5 = i2 / 8;
                    bArr[i5] = (byte) ((bArr[i5] << 1) | (i4 & 1));
                }
                i2++;
                i3 = i4;
            }
            this.stop = true;
            return bArr;
        }

        public void run() {
            while (!this.stop) {
                this.counter++;
            }
        }
    }

    public byte[] generateSeed(int i, boolean z) {
        return new SeedGenerator().generateSeed(i, z);
    }
}
