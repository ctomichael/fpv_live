package kotlin.text;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.bouncycastle.asn1.cmc.BodyPartID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\f\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000f\u001a\u0014\u0010\u0010\u001a\u00020\u0002*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0010\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a\u0011\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u001a\u0014\u0010\u0014\u001a\u00020\u0007*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a\u001c\u0010\u0014\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0016\u001a\u0011\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u001a\u0014\u0010\u0018\u001a\u00020\n*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0019\u001a\u001c\u0010\u0018\u001a\u00020\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001a\u001a\u0011\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u001a\u0014\u0010\u001c\u001a\u00020\r*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001d\u001a\u001c\u0010\u001c\u001a\u00020\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001e\u001a\u0011\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006 "}, d2 = {"toString", "", "Lkotlin/UByte;", "radix", "", "toString-LxnNnR4", "(BI)Ljava/lang/String;", "Lkotlin/UInt;", "toString-V7xB4Y4", "(II)Ljava/lang/String;", "Lkotlin/ULong;", "toString-JSWoG40", "(JI)Ljava/lang/String;", "Lkotlin/UShort;", "toString-olVBNx4", "(SI)Ljava/lang/String;", "toUByte", "(Ljava/lang/String;)B", "(Ljava/lang/String;I)B", "toUByteOrNull", "toUInt", "(Ljava/lang/String;)I", "(Ljava/lang/String;I)I", "toUIntOrNull", "toULong", "(Ljava/lang/String;)J", "(Ljava/lang/String;I)J", "toULongOrNull", "toUShort", "(Ljava/lang/String;)S", "(Ljava/lang/String;I)S", "toUShortOrNull", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
@JvmName(name = "UStringsKt")
/* compiled from: UStrings.kt */
public final class UStringsKt {
    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: toString-LxnNnR4  reason: not valid java name */
    public static final String m918toStringLxnNnR4(byte $this$toString, int radix) {
        String num = Integer.toString($this$toString & 255, CharsKt.checkRadix(radix));
        Intrinsics.checkExpressionValueIsNotNull(num, "java.lang.Integer.toStri…(this, checkRadix(radix))");
        return num;
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: toString-olVBNx4  reason: not valid java name */
    public static final String m920toStringolVBNx4(short $this$toString, int radix) {
        String num = Integer.toString(65535 & $this$toString, CharsKt.checkRadix(radix));
        Intrinsics.checkExpressionValueIsNotNull(num, "java.lang.Integer.toStri…(this, checkRadix(radix))");
        return num;
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: toString-V7xB4Y4  reason: not valid java name */
    public static final String m919toStringV7xB4Y4(int $this$toString, int radix) {
        String l = Long.toString(((long) $this$toString) & BodyPartID.bodyIdMax, CharsKt.checkRadix(radix));
        Intrinsics.checkExpressionValueIsNotNull(l, "java.lang.Long.toString(this, checkRadix(radix))");
        return l;
    }

    @ExperimentalUnsignedTypes
    @NotNull
    @SinceKotlin(version = "1.3")
    /* renamed from: toString-JSWoG40  reason: not valid java name */
    public static final String m917toStringJSWoG40(long $this$toString, int radix) {
        return UnsignedKt.ulongToString($this$toString, CharsKt.checkRadix(radix));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final byte toUByte(@NotNull String $this$toUByte) {
        Intrinsics.checkParameterIsNotNull($this$toUByte, "$this$toUByte");
        UByte uByteOrNull = toUByteOrNull($this$toUByte);
        if (uByteOrNull != null) {
            return uByteOrNull.m62unboximpl();
        }
        StringsKt.numberFormatError($this$toUByte);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final byte toUByte(@NotNull String $this$toUByte, int radix) {
        Intrinsics.checkParameterIsNotNull($this$toUByte, "$this$toUByte");
        UByte uByteOrNull = toUByteOrNull($this$toUByte, radix);
        if (uByteOrNull != null) {
            return uByteOrNull.m62unboximpl();
        }
        StringsKt.numberFormatError($this$toUByte);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final short toUShort(@NotNull String $this$toUShort) {
        Intrinsics.checkParameterIsNotNull($this$toUShort, "$this$toUShort");
        UShort uShortOrNull = toUShortOrNull($this$toUShort);
        if (uShortOrNull != null) {
            return uShortOrNull.m295unboximpl();
        }
        StringsKt.numberFormatError($this$toUShort);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final short toUShort(@NotNull String $this$toUShort, int radix) {
        Intrinsics.checkParameterIsNotNull($this$toUShort, "$this$toUShort");
        UShort uShortOrNull = toUShortOrNull($this$toUShort, radix);
        if (uShortOrNull != null) {
            return uShortOrNull.m295unboximpl();
        }
        StringsKt.numberFormatError($this$toUShort);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final int toUInt(@NotNull String $this$toUInt) {
        Intrinsics.checkParameterIsNotNull($this$toUInt, "$this$toUInt");
        UInt uIntOrNull = toUIntOrNull($this$toUInt);
        if (uIntOrNull != null) {
            return uIntOrNull.m131unboximpl();
        }
        StringsKt.numberFormatError($this$toUInt);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final int toUInt(@NotNull String $this$toUInt, int radix) {
        Intrinsics.checkParameterIsNotNull($this$toUInt, "$this$toUInt");
        UInt uIntOrNull = toUIntOrNull($this$toUInt, radix);
        if (uIntOrNull != null) {
            return uIntOrNull.m131unboximpl();
        }
        StringsKt.numberFormatError($this$toUInt);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final long toULong(@NotNull String $this$toULong) {
        Intrinsics.checkParameterIsNotNull($this$toULong, "$this$toULong");
        ULong uLongOrNull = toULongOrNull($this$toULong);
        if (uLongOrNull != null) {
            return uLongOrNull.m200unboximpl();
        }
        StringsKt.numberFormatError($this$toULong);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    public static final long toULong(@NotNull String $this$toULong, int radix) {
        Intrinsics.checkParameterIsNotNull($this$toULong, "$this$toULong");
        ULong uLongOrNull = toULongOrNull($this$toULong, radix);
        if (uLongOrNull != null) {
            return uLongOrNull.m200unboximpl();
        }
        StringsKt.numberFormatError($this$toULong);
        throw null;
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UByte toUByteOrNull(@NotNull String $this$toUByteOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toUByteOrNull, "$this$toUByteOrNull");
        return toUByteOrNull($this$toUByteOrNull, 10);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UByte toUByteOrNull(@NotNull String $this$toUByteOrNull, int radix) {
        Intrinsics.checkParameterIsNotNull($this$toUByteOrNull, "$this$toUByteOrNull");
        UInt uIntOrNull = toUIntOrNull($this$toUByteOrNull, radix);
        if (uIntOrNull == null) {
            return null;
        }
        int r0 = uIntOrNull.m131unboximpl();
        if (UnsignedKt.uintCompare(r0, UInt.m88constructorimpl(255)) > 0) {
            return null;
        }
        return UByte.m15boximpl(UByte.m21constructorimpl((byte) r0));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UShort toUShortOrNull(@NotNull String $this$toUShortOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toUShortOrNull, "$this$toUShortOrNull");
        return toUShortOrNull($this$toUShortOrNull, 10);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UShort toUShortOrNull(@NotNull String $this$toUShortOrNull, int radix) {
        Intrinsics.checkParameterIsNotNull($this$toUShortOrNull, "$this$toUShortOrNull");
        UInt uIntOrNull = toUIntOrNull($this$toUShortOrNull, radix);
        if (uIntOrNull == null) {
            return null;
        }
        int r0 = uIntOrNull.m131unboximpl();
        if (UnsignedKt.uintCompare(r0, UInt.m88constructorimpl(65535)) > 0) {
            return null;
        }
        return UShort.m248boximpl(UShort.m254constructorimpl((short) r0));
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UInt toUIntOrNull(@NotNull String $this$toUIntOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toUIntOrNull, "$this$toUIntOrNull");
        return toUIntOrNull($this$toUIntOrNull, 10);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final UInt toUIntOrNull(@NotNull String $this$toUIntOrNull, int radix) {
        int start;
        Intrinsics.checkParameterIsNotNull($this$toUIntOrNull, "$this$toUIntOrNull");
        CharsKt.checkRadix(radix);
        int length = $this$toUIntOrNull.length();
        if (length == 0) {
            return null;
        }
        char firstChar = $this$toUIntOrNull.charAt(0);
        if (firstChar >= '0') {
            start = 0;
        } else if (length == 1 || firstChar != '+') {
            return null;
        } else {
            start = 1;
        }
        int uradix = UInt.m88constructorimpl(radix);
        int limitBeforeMul = UnsignedKt.m314uintDivideJ1ME1BU(-1, uradix);
        int result = 0;
        while (start < length) {
            int digit = CharsKt.digitOf($this$toUIntOrNull.charAt(start), radix);
            if (digit < 0 || UnsignedKt.uintCompare(result, limitBeforeMul) > 0) {
                return null;
            }
            int result2 = UInt.m88constructorimpl(result * uradix);
            int beforeAdding = result2;
            result = UInt.m88constructorimpl(UInt.m88constructorimpl(digit) + result2);
            if (UnsignedKt.uintCompare(result, beforeAdding) < 0) {
                return null;
            }
            start++;
        }
        return UInt.m82boximpl(result);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final ULong toULongOrNull(@NotNull String $this$toULongOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toULongOrNull, "$this$toULongOrNull");
        return toULongOrNull($this$toULongOrNull, 10);
    }

    @ExperimentalUnsignedTypes
    @SinceKotlin(version = "1.3")
    @Nullable
    public static final ULong toULongOrNull(@NotNull String $this$toULongOrNull, int radix) {
        int start;
        Intrinsics.checkParameterIsNotNull($this$toULongOrNull, "$this$toULongOrNull");
        CharsKt.checkRadix(radix);
        int length = $this$toULongOrNull.length();
        if (length == 0) {
            return null;
        }
        char firstChar = $this$toULongOrNull.charAt(0);
        if (firstChar >= '0') {
            start = 0;
        } else if (length == 1 || firstChar != '+') {
            return null;
        } else {
            start = 1;
        }
        int uradix = UInt.m88constructorimpl(radix);
        long limitBeforeMul = UnsignedKt.m316ulongDivideeb3DHEI(-1, ULong.m157constructorimpl(((long) uradix) & BodyPartID.bodyIdMax));
        long result = 0;
        while (start < length) {
            int digit = CharsKt.digitOf($this$toULongOrNull.charAt(start), radix);
            if (digit < 0 || UnsignedKt.ulongCompare(result, limitBeforeMul) > 0) {
                return null;
            }
            long result2 = ULong.m157constructorimpl(ULong.m157constructorimpl(((long) uradix) & BodyPartID.bodyIdMax) * result);
            long beforeAdding = result2;
            result = ULong.m157constructorimpl(ULong.m157constructorimpl(((long) UInt.m88constructorimpl(digit)) & BodyPartID.bodyIdMax) + result2);
            if (UnsignedKt.ulongCompare(result, beforeAdding) < 0) {
                return null;
            }
            start++;
        }
        return ULong.m151boximpl(result);
    }
}
