package kotlin.collections;

import dji.component.accountcenter.IMemberProtocol;
import java.util.Arrays;
import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\t\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0004ø\u0001\u0000¢\u0006\u0004\b\u0007\u0010\bJ\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\t2\u0006\u0010\u0006\u001a\u00020\tH\u0004ø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\f2\u0006\u0010\u0006\u001a\u00020\fH\u0004ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000eJ\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u000fH\u0004ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\tH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0016\u0010\u0017J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0019J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\tH\u0007ø\u0001\u0000¢\u0006\u0004\b \u0010!J\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\fH\u0007ø\u0001\u0000¢\u0006\u0004\b\"\u0010#J\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b$\u0010%J\u001e\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010&\u001a\u00020(H\u0007ø\u0001\u0000¢\u0006\u0004\b)\u0010*J\u001e\u0010&\u001a\u00020+*\u00020\t2\u0006\u0010&\u001a\u00020(H\u0007ø\u0001\u0000¢\u0006\u0004\b,\u0010-J\u001e\u0010&\u001a\u00020.*\u00020\f2\u0006\u0010&\u001a\u00020(H\u0007ø\u0001\u0000¢\u0006\u0004\b/\u00100J\u001e\u0010&\u001a\u000201*\u00020\u000f2\u0006\u0010&\u001a\u00020(H\u0007ø\u0001\u0000¢\u0006\u0004\b2\u00103J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020'05*\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b6\u00107J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020+05*\u00020\tH\u0007ø\u0001\u0000¢\u0006\u0004\b8\u00109J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020.05*\u00020\fH\u0007ø\u0001\u0000¢\u0006\u0004\b:\u0010;J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020105*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b<\u0010=\u0002\u0004\n\u0002\b\u0019¨\u0006>"}, d2 = {"Lkotlin/collections/UArraysKt;", "", "()V", "contentEquals", "", "Lkotlin/UByteArray;", "other", "contentEquals-kdPth3s", "([B[B)Z", "Lkotlin/UIntArray;", "contentEquals-ctEhBpI", "([I[I)Z", "Lkotlin/ULongArray;", "contentEquals-us8wMrg", "([J[J)Z", "Lkotlin/UShortArray;", "contentEquals-mazbYpA", "([S[S)Z", "contentHashCode", "", "contentHashCode-GBYM_sE", "([B)I", "contentHashCode--ajY-9A", "([I)I", "contentHashCode-QwZRm1k", "([J)I", "contentHashCode-rL5Bavg", "([S)I", "contentToString", "", "contentToString-GBYM_sE", "([B)Ljava/lang/String;", "contentToString--ajY-9A", "([I)Ljava/lang/String;", "contentToString-QwZRm1k", "([J)Ljava/lang/String;", "contentToString-rL5Bavg", "([S)Ljava/lang/String;", "random", "Lkotlin/UByte;", "Lkotlin/random/Random;", "random-oSF2wD8", "([BLkotlin/random/Random;)B", "Lkotlin/UInt;", "random-2D5oskM", "([ILkotlin/random/Random;)I", "Lkotlin/ULong;", "random-JzugnMA", "([JLkotlin/random/Random;)J", "Lkotlin/UShort;", "random-s5X_as8", "([SLkotlin/random/Random;)S", "toTypedArray", "", "toTypedArray-GBYM_sE", "([B)[Lkotlin/UByte;", "toTypedArray--ajY-9A", "([I)[Lkotlin/UInt;", "toTypedArray-QwZRm1k", "([J)[Lkotlin/ULong;", "toTypedArray-rL5Bavg", "([S)[Lkotlin/UShort;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
@Deprecated(level = DeprecationLevel.HIDDEN, message = "Provided for binary compatibility")
/* compiled from: UArraysKt.kt */
public final class UArraysKt {
    public static final UArraysKt INSTANCE = new UArraysKt();

    private UArraysKt() {
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: random-2D5oskM  reason: not valid java name */
    public static final int m346random2D5oskM(@NotNull int[] $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!UIntArray.m142isEmptyimpl($this$random)) {
            return UIntArray.m139getimpl($this$random, random.nextInt(UIntArray.m140getSizeimpl($this$random)));
        }
        throw new NoSuchElementException("Array is empty.");
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: random-JzugnMA  reason: not valid java name */
    public static final long m347randomJzugnMA(@NotNull long[] $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!ULongArray.m211isEmptyimpl($this$random)) {
            return ULongArray.m208getimpl($this$random, random.nextInt(ULongArray.m209getSizeimpl($this$random)));
        }
        throw new NoSuchElementException("Array is empty.");
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: random-oSF2wD8  reason: not valid java name */
    public static final byte m348randomoSF2wD8(@NotNull byte[] $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!UByteArray.m73isEmptyimpl($this$random)) {
            return UByteArray.m70getimpl($this$random, random.nextInt(UByteArray.m71getSizeimpl($this$random)));
        }
        throw new NoSuchElementException("Array is empty.");
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: random-s5X_as8  reason: not valid java name */
    public static final short m349randoms5X_as8(@NotNull short[] $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!UShortArray.m306isEmptyimpl($this$random)) {
            return UShortArray.m303getimpl($this$random, random.nextInt(UShortArray.m304getSizeimpl($this$random)));
        }
        throw new NoSuchElementException("Array is empty.");
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: contentEquals-ctEhBpI  reason: not valid java name */
    public static final boolean m334contentEqualsctEhBpI(@NotNull int[] $this$contentEquals, @NotNull int[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($this$contentEquals, other);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: contentEquals-us8wMrg  reason: not valid java name */
    public static final boolean m337contentEqualsus8wMrg(@NotNull long[] $this$contentEquals, @NotNull long[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($this$contentEquals, other);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: contentEquals-kdPth3s  reason: not valid java name */
    public static final boolean m335contentEqualskdPth3s(@NotNull byte[] $this$contentEquals, @NotNull byte[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($this$contentEquals, other);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: contentEquals-mazbYpA  reason: not valid java name */
    public static final boolean m336contentEqualsmazbYpA(@NotNull short[] $this$contentEquals, @NotNull short[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($this$contentEquals, other);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: contentHashCode--ajY-9A  reason: not valid java name */
    public static final int m338contentHashCodeajY9A(@NotNull int[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        return Arrays.hashCode($this$contentHashCode);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: contentHashCode-QwZRm1k  reason: not valid java name */
    public static final int m340contentHashCodeQwZRm1k(@NotNull long[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        return Arrays.hashCode($this$contentHashCode);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: contentHashCode-GBYM_sE  reason: not valid java name */
    public static final int m339contentHashCodeGBYM_sE(@NotNull byte[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        return Arrays.hashCode($this$contentHashCode);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    /* renamed from: contentHashCode-rL5Bavg  reason: not valid java name */
    public static final int m341contentHashCoderL5Bavg(@NotNull short[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        return Arrays.hashCode($this$contentHashCode);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    @NotNull
    /* renamed from: contentToString--ajY-9A  reason: not valid java name */
    public static final String m342contentToStringajY9A(@NotNull int[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(Intrinsics.checkParameterIsNotNull($this$contentToString, "v"), ", ", IMemberProtocol.STRING_SEPERATOR_LEFT, IMemberProtocol.STRING_SEPERATOR_RIGHT, 0, null, null, 56, null);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    @NotNull
    /* renamed from: contentToString-QwZRm1k  reason: not valid java name */
    public static final String m344contentToStringQwZRm1k(@NotNull long[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(Intrinsics.checkParameterIsNotNull($this$contentToString, "v"), ", ", IMemberProtocol.STRING_SEPERATOR_LEFT, IMemberProtocol.STRING_SEPERATOR_RIGHT, 0, null, null, 56, null);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    @NotNull
    /* renamed from: contentToString-GBYM_sE  reason: not valid java name */
    public static final String m343contentToStringGBYM_sE(@NotNull byte[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(Intrinsics.checkParameterIsNotNull($this$contentToString, "v"), ", ", IMemberProtocol.STRING_SEPERATOR_LEFT, IMemberProtocol.STRING_SEPERATOR_RIGHT, 0, null, null, 56, null);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    @NotNull
    /* renamed from: contentToString-rL5Bavg  reason: not valid java name */
    public static final String m345contentToStringrL5Bavg(@NotNull short[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(Intrinsics.checkParameterIsNotNull($this$contentToString, "v"), ", ", IMemberProtocol.STRING_SEPERATOR_LEFT, IMemberProtocol.STRING_SEPERATOR_RIGHT, 0, null, null, 56, null);
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    @NotNull
    /* renamed from: toTypedArray--ajY-9A  reason: not valid java name */
    public static final UInt[] m350toTypedArrayajY9A(@NotNull int[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int r2 = UIntArray.m140getSizeimpl($this$toTypedArray);
        UInt[] uIntArr = new UInt[r2];
        for (int i = 0; i < r2; i++) {
            uIntArr[i] = UInt.m82boximpl(UIntArray.m139getimpl($this$toTypedArray, i));
        }
        return uIntArr;
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    @NotNull
    /* renamed from: toTypedArray-QwZRm1k  reason: not valid java name */
    public static final ULong[] m352toTypedArrayQwZRm1k(@NotNull long[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int r2 = ULongArray.m209getSizeimpl($this$toTypedArray);
        ULong[] uLongArr = new ULong[r2];
        for (int i = 0; i < r2; i++) {
            uLongArr[i] = ULong.m151boximpl(ULongArray.m208getimpl($this$toTypedArray, i));
        }
        return uLongArr;
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    @NotNull
    /* renamed from: toTypedArray-GBYM_sE  reason: not valid java name */
    public static final UByte[] m351toTypedArrayGBYM_sE(@NotNull byte[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int r2 = UByteArray.m71getSizeimpl($this$toTypedArray);
        UByte[] uByteArr = new UByte[r2];
        for (int i = 0; i < r2; i++) {
            uByteArr[i] = UByte.m15boximpl(UByteArray.m70getimpl($this$toTypedArray, i));
        }
        return uByteArr;
    }

    @ExperimentalUnsignedTypes
    @JvmStatic
    @NotNull
    /* renamed from: toTypedArray-rL5Bavg  reason: not valid java name */
    public static final UShort[] m353toTypedArrayrL5Bavg(@NotNull short[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int r2 = UShortArray.m304getSizeimpl($this$toTypedArray);
        UShort[] uShortArr = new UShort[r2];
        for (int i = 0; i < r2; i++) {
            uShortArr[i] = UShort.m248boximpl(UShortArray.m303getimpl($this$toTypedArray, i));
        }
        return uShortArr;
    }
}
