package kotlin.random;

import dji.publics.LogReport.base.Fields;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import org.bouncycastle.asn1.cmc.BodyPartID;
import org.jetbrains.annotations.NotNull;

@SinceKotlin(version = "1.3")
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b'\u0018\u0000 \u00182\u00020\u0001:\u0002\u0017\u0018B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H&J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016J$\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u0004H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0004H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0004H\u0016J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\u0018\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016¨\u0006\u0019"}, d2 = {"Lkotlin/random/Random;", "", "()V", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", Fields.Dgo_quiz.FROM, "nextFloat", "", "nextInt", "nextLong", "", "Companion", "Default", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: Random.kt */
public abstract class Random {
    @NotNull
    @JvmField
    public static final Companion Companion = Companion.INSTANCE;
    public static final Default Default = new Default(null);
    /* access modifiers changed from: private */
    public static final Random defaultRandom = PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();

    public abstract int nextBits(int i);

    public int nextInt() {
        return nextBits(32);
    }

    public int nextInt(int until) {
        return nextInt(0, until);
    }

    public int nextInt(int from, int until) {
        int bits;
        int v;
        int rnd;
        RandomKt.checkRangeBounds(from, until);
        int n = until - from;
        if (n > 0 || n == Integer.MIN_VALUE) {
            if (((-n) & n) == n) {
                rnd = nextBits(RandomKt.fastLog2(n));
            } else {
                do {
                    bits = nextInt() >>> 1;
                    v = bits % n;
                } while ((bits - v) + (n - 1) < 0);
                rnd = v;
            }
            return from + rnd;
        }
        while (true) {
            int rnd2 = nextInt();
            if (from <= rnd2 && until > rnd2) {
                return rnd2;
            }
        }
    }

    public long nextLong() {
        return (((long) nextInt()) << 32) + ((long) nextInt());
    }

    public long nextLong(long until) {
        return nextLong(0, until);
    }

    public long nextLong(long from, long until) {
        long bits;
        long v;
        long rnd;
        RandomKt.checkRangeBounds(from, until);
        long n = until - from;
        if (n > 0) {
            if (((-n) & n) == n) {
                int nLow = (int) n;
                int nHigh = (int) (n >>> 32);
                if (nLow != 0) {
                    rnd = ((long) nextBits(RandomKt.fastLog2(nLow))) & BodyPartID.bodyIdMax;
                } else if (nHigh == 1) {
                    rnd = ((long) nextInt()) & BodyPartID.bodyIdMax;
                } else {
                    rnd = (((long) nextBits(RandomKt.fastLog2(nHigh))) << 32) + ((long) nextInt());
                }
            } else {
                do {
                    bits = nextLong() >>> 1;
                    v = bits % n;
                } while ((bits - v) + (n - 1) < 0);
                rnd = v;
            }
            return from + rnd;
        }
        while (true) {
            long rnd2 = nextLong();
            if (from <= rnd2 && until > rnd2) {
                return rnd2;
            }
        }
    }

    public boolean nextBoolean() {
        return nextBits(1) != 0;
    }

    public double nextDouble() {
        return PlatformRandomKt.doubleFromParts(nextBits(26), nextBits(27));
    }

    public double nextDouble(double until) {
        return nextDouble(0.0d, until);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public double nextDouble(double r16, double r18) {
        /*
            r15 = this;
            kotlin.random.RandomKt.checkRangeBounds(r16, r18)
            double r6 = r18 - r16
            boolean r8 = java.lang.Double.isInfinite(r6)
            if (r8 == 0) goto L_0x0051
            boolean r8 = java.lang.Double.isInfinite(r16)
            if (r8 != 0) goto L_0x004d
            boolean r8 = java.lang.Double.isNaN(r16)
            if (r8 != 0) goto L_0x004d
            r8 = 1
        L_0x0018:
            if (r8 == 0) goto L_0x0051
            boolean r8 = java.lang.Double.isInfinite(r18)
            if (r8 != 0) goto L_0x004f
            boolean r8 = java.lang.Double.isNaN(r18)
            if (r8 != 0) goto L_0x004f
            r8 = 1
        L_0x0027:
            if (r8 == 0) goto L_0x0051
            double r8 = r15.nextDouble()
            r10 = 2
            double r10 = (double) r10
            double r10 = r18 / r10
            r12 = 2
            double r12 = (double) r12
            double r12 = r16 / r12
            double r10 = r10 - r12
            double r4 = r8 * r10
            double r8 = r16 + r4
            double r2 = r8 + r4
        L_0x003c:
            int r8 = (r2 > r18 ? 1 : (r2 == r18 ? 0 : -1))
            if (r8 < 0) goto L_0x004c
            kotlin.jvm.internal.DoubleCompanionObject r8 = kotlin.jvm.internal.DoubleCompanionObject.INSTANCE
            double r8 = r8.getNEGATIVE_INFINITY()
            r0 = r18
            double r2 = java.lang.Math.nextAfter(r0, r8)
        L_0x004c:
            return r2
        L_0x004d:
            r8 = 0
            goto L_0x0018
        L_0x004f:
            r8 = 0
            goto L_0x0027
        L_0x0051:
            double r8 = r15.nextDouble()
            double r8 = r8 * r6
            double r2 = r16 + r8
            goto L_0x003c
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.random.Random.nextDouble(double, double):double");
    }

    public float nextFloat() {
        return ((float) nextBits(24)) / ((float) 16777216);
    }

    public static /* synthetic */ byte[] nextBytes$default(Random random, byte[] bArr, int i, int i2, int i3, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: nextBytes");
        }
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        return random.nextBytes(bArr, i, i2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x000e  */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] nextBytes(@org.jetbrains.annotations.NotNull byte[] r13, int r14, int r15) {
        /*
            r12 = this;
            r10 = 1
            r3 = 0
            java.lang.String r11 = "array"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r13, r11)
            int r11 = r13.length
            if (r14 >= 0) goto L_0x004c
        L_0x000b:
            r11 = r3
        L_0x000c:
            if (r11 != 0) goto L_0x0055
            r1 = 0
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "fromIndex ("
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r14)
            java.lang.String r11 = ") or toIndex ("
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r15)
            java.lang.String r11 = ") are out of range: 0.."
            java.lang.StringBuilder r10 = r10.append(r11)
            int r11 = r13.length
            java.lang.StringBuilder r10 = r10.append(r11)
            r11 = 46
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = r10.toString()
            java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            java.lang.Throwable r10 = (java.lang.Throwable) r10
            throw r10
        L_0x004c:
            if (r11 < r14) goto L_0x000b
            int r11 = r13.length
            if (r15 < 0) goto L_0x000b
            if (r11 < r15) goto L_0x000b
            r11 = r10
            goto L_0x000c
        L_0x0055:
            if (r14 > r15) goto L_0x008c
        L_0x0057:
            if (r10 != 0) goto L_0x008e
            r2 = 0
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "fromIndex ("
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r14)
            java.lang.String r11 = ") must be not greater than toIndex ("
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r15)
            java.lang.String r11 = ")."
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = r10.toString()
            java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            java.lang.Throwable r10 = (java.lang.Throwable) r10
            throw r10
        L_0x008c:
            r10 = r3
            goto L_0x0057
        L_0x008e:
            int r10 = r15 - r14
            int r7 = r10 / 4
            r5 = r14
            r4 = r3
        L_0x0094:
            if (r4 >= r7) goto L_0x00ba
            r0 = 0
            int r8 = r12.nextInt()
            byte r10 = (byte) r8
            r13[r5] = r10
            int r10 = r5 + 1
            int r11 = r8 >>> 8
            byte r11 = (byte) r11
            r13[r10] = r11
            int r10 = r5 + 2
            int r11 = r8 >>> 16
            byte r11 = (byte) r11
            r13[r10] = r11
            int r10 = r5 + 3
            int r11 = r8 >>> 24
            byte r11 = (byte) r11
            r13[r10] = r11
            int r5 = r5 + 4
            int r4 = r4 + 1
            goto L_0x0094
        L_0x00ba:
            int r6 = r15 - r5
            int r10 = r6 * 8
            int r9 = r12.nextBits(r10)
        L_0x00c2:
            if (r3 >= r6) goto L_0x00d0
            int r10 = r5 + r3
            int r11 = r3 * 8
            int r11 = r9 >>> r11
            byte r11 = (byte) r11
            r13[r10] = r11
            int r3 = r3 + 1
            goto L_0x00c2
        L_0x00d0:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.random.Random.nextBytes(byte[], int, int):byte[]");
    }

    @NotNull
    public byte[] nextBytes(@NotNull byte[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        return nextBytes(array, 0, array.length);
    }

    @NotNull
    public byte[] nextBytes(int size) {
        return nextBytes(new byte[size]);
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0016J \u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\bH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\bH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0016J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\bH\u0016J\u0010\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\bH\u0016J\u0018\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\bH\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0014\u001a\u00020\u001aH\u0016J\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u001a2\u0006\u0010\u0014\u001a\u00020\u001aH\u0016R\u0016\u0010\u0003\u001a\u00020\u00048\u0006X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0002R\u000e\u0010\u0006\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lkotlin/random/Random$Default;", "Lkotlin/random/Random;", "()V", "Companion", "Lkotlin/random/Random$Companion;", "Companion$annotations", "defaultRandom", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", Fields.Dgo_quiz.FROM, "nextFloat", "", "nextInt", "nextLong", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
    /* compiled from: Random.kt */
    public static final class Default extends Random {
        @Deprecated(level = DeprecationLevel.HIDDEN, message = "Use Default companion object instead")
        public static /* synthetic */ void Companion$annotations() {
        }

        private Default() {
        }

        public /* synthetic */ Default(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public int nextBits(int bitCount) {
            return Random.defaultRandom.nextBits(bitCount);
        }

        public int nextInt() {
            return Random.defaultRandom.nextInt();
        }

        public int nextInt(int until) {
            return Random.defaultRandom.nextInt(until);
        }

        public int nextInt(int from, int until) {
            return Random.defaultRandom.nextInt(from, until);
        }

        public long nextLong() {
            return Random.defaultRandom.nextLong();
        }

        public long nextLong(long until) {
            return Random.defaultRandom.nextLong(until);
        }

        public long nextLong(long from, long until) {
            return Random.defaultRandom.nextLong(from, until);
        }

        public boolean nextBoolean() {
            return Random.defaultRandom.nextBoolean();
        }

        public double nextDouble() {
            return Random.defaultRandom.nextDouble();
        }

        public double nextDouble(double until) {
            return Random.defaultRandom.nextDouble(until);
        }

        public double nextDouble(double from, double until) {
            return Random.defaultRandom.nextDouble(from, until);
        }

        public float nextFloat() {
            return Random.defaultRandom.nextFloat();
        }

        @NotNull
        public byte[] nextBytes(@NotNull byte[] array) {
            Intrinsics.checkParameterIsNotNull(array, "array");
            return Random.defaultRandom.nextBytes(array);
        }

        @NotNull
        public byte[] nextBytes(int size) {
            return Random.defaultRandom.nextBytes(size);
        }

        @NotNull
        public byte[] nextBytes(@NotNull byte[] array, int fromIndex, int toIndex) {
            Intrinsics.checkParameterIsNotNull(array, "array");
            return Random.defaultRandom.nextBytes(array, fromIndex, toIndex);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0016¨\u0006\u0006"}, d2 = {"Lkotlin/random/Random$Companion;", "Lkotlin/random/Random;", "()V", "nextBits", "", "bitCount", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Use Default companion object instead")
    /* compiled from: Random.kt */
    public static final class Companion extends Random {
        public static final Companion INSTANCE = new Companion();

        private Companion() {
        }

        public int nextBits(int bitCount) {
            return Random.Default.nextBits(bitCount);
        }
    }
}
