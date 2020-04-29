package kotlin.text;

import dji.component.mediaprovider.DJIMediaStore;
import dji.pilot.fpv.util.DJIFlurryReport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000|\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0019\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\u001a\u001c\u0010\t\u001a\u00020\n*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010\u000e\u001a\u00020\n*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001f\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\rH\u0002\u001a\u001f\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\rH\u0002\u001a\u0015\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\n\u001a\u001c\u0010\u0014\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010\u0014\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a:\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001aE\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\rH\u0002¢\u0006\u0002\b\u001c\u001a:\u0010\u001d\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0012\u0010\u001e\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u0006\u001a4\u0010 \u001a\u0002H!\"\f\b\u0000\u0010\"*\u00020\u0002*\u0002H!\"\u0004\b\u0001\u0010!*\u0002H\"2\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$H\b¢\u0006\u0002\u0010%\u001a4\u0010&\u001a\u0002H!\"\f\b\u0000\u0010\"*\u00020\u0002*\u0002H!\"\u0004\b\u0001\u0010!*\u0002H\"2\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$H\b¢\u0006\u0002\u0010%\u001a&\u0010'\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a;\u0010'\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u001b\u001a\u00020\rH\u0002¢\u0006\u0002\b)\u001a&\u0010'\u001a\u00020\u0006*\u00020\u00022\u0006\u0010*\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u0010+\u001a\u00020\u0006*\u00020\u00022\u0006\u0010,\u001a\u00020-2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a,\u0010+\u001a\u00020\u0006*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\r\u0010.\u001a\u00020\r*\u00020\u0002H\b\u001a\r\u0010/\u001a\u00020\r*\u00020\u0002H\b\u001a\r\u00100\u001a\u00020\r*\u00020\u0002H\b\u001a \u00101\u001a\u00020\r*\u0004\u0018\u00010\u0002H\b\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a \u00102\u001a\u00020\r*\u0004\u0018\u00010\u0002H\b\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\r\u00103\u001a\u000204*\u00020\u0002H\u0002\u001a&\u00105\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u00105\u001a\u00020\u0006*\u00020\u00022\u0006\u0010*\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u00106\u001a\u00020\u0006*\u00020\u00022\u0006\u0010,\u001a\u00020-2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a,\u00106\u001a\u00020\u0006*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0010\u00107\u001a\b\u0012\u0004\u0012\u00020\n08*\u00020\u0002\u001a\u0010\u00109\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u0002\u001a\u0015\u0010;\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\f\u001a\u000f\u0010<\u001a\u00020\n*\u0004\u0018\u00010\nH\b\u001a\u001c\u0010=\u001a\u00020\u0002*\u00020\u00022\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001a\u001c\u0010=\u001a\u00020\n*\u00020\n2\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001a\u001c\u0010@\u001a\u00020\u0002*\u00020\u00022\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001a\u001c\u0010@\u001a\u00020\n*\u00020\n2\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001aG\u0010A\u001a\b\u0012\u0004\u0012\u00020\u000108*\u00020\u00022\u000e\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0C2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006H\u0002¢\u0006\u0004\bE\u0010F\u001a=\u0010A\u001a\b\u0012\u0004\u0012\u00020\u000108*\u00020\u00022\u0006\u0010B\u001a\u00020-2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006H\u0002¢\u0006\u0002\bE\u001a4\u0010G\u001a\u00020\r*\u00020\u00022\u0006\u0010H\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010I\u001a\u00020\u00062\u0006\u0010>\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rH\u0000\u001a\u0012\u0010J\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u0002\u001a\u0012\u0010J\u001a\u00020\n*\u00020\n2\u0006\u0010K\u001a\u00020\u0002\u001a\u001a\u0010L\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u0006\u001a\u0012\u0010L\u001a\u00020\u0002*\u00020\u00022\u0006\u0010M\u001a\u00020\u0001\u001a\u001d\u0010L\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u0006H\b\u001a\u0015\u0010L\u001a\u00020\n*\u00020\n2\u0006\u0010M\u001a\u00020\u0001H\b\u001a\u0012\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010N\u001a\u00020\n*\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010P\u001a\u00020\u0002\u001a\u001a\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010O\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u0002\u001a\u001a\u0010O\u001a\u00020\n*\u00020\n2\u0006\u0010K\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a+\u0010Q\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0014\b\b\u0010R\u001a\u000e\u0012\u0004\u0012\u00020T\u0012\u0004\u0012\u00020\u00020SH\b\u001a\u001d\u0010Q\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010U\u001a\u00020\nH\b\u001a$\u0010V\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010V\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010X\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010X\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Y\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Y\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Z\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Z\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001d\u0010[\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010U\u001a\u00020\nH\b\u001a\"\u0010\\\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\u0006\u0010U\u001a\u00020\u0002\u001a\u001a\u0010\\\u001a\u00020\u0002*\u00020\u00022\u0006\u0010M\u001a\u00020\u00012\u0006\u0010U\u001a\u00020\u0002\u001a%\u0010\\\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\u0006\u0010U\u001a\u00020\u0002H\b\u001a\u001d\u0010\\\u001a\u00020\n*\u00020\n2\u0006\u0010M\u001a\u00020\u00012\u0006\u0010U\u001a\u00020\u0002H\b\u001a=\u0010]\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\u0012\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0C\"\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006¢\u0006\u0002\u0010^\u001a0\u0010]\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\n\u0010B\u001a\u00020-\"\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006\u001a/\u0010]\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\u0006\u0010P\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010D\u001a\u00020\u0006H\u0002¢\u0006\u0002\b_\u001a%\u0010]\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010D\u001a\u00020\u0006H\b\u001a=\u0010`\u001a\b\u0012\u0004\u0012\u00020\n08*\u00020\u00022\u0012\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0C\"\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006¢\u0006\u0002\u0010a\u001a0\u0010`\u001a\b\u0012\u0004\u0012\u00020\n08*\u00020\u00022\n\u0010B\u001a\u00020-\"\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006\u001a\u001c\u0010b\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010b\u001a\u00020\r*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a$\u0010b\u001a\u00020\r*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0012\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010M\u001a\u00020\u0001\u001a\u001d\u0010c\u001a\u00020\u0002*\u00020\n2\u0006\u0010d\u001a\u00020\u00062\u0006\u0010e\u001a\u00020\u0006H\b\u001a\u001f\u0010f\u001a\u00020\n*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010(\u001a\u00020\u0006H\b\u001a\u0012\u0010f\u001a\u00020\n*\u00020\u00022\u0006\u0010M\u001a\u00020\u0001\u001a\u0012\u0010f\u001a\u00020\n*\u00020\n2\u0006\u0010M\u001a\u00020\u0001\u001a\u001c\u0010g\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010g\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010h\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010h\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010i\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010i\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010j\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010j\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\n\u0010k\u001a\u00020\u0002*\u00020\u0002\u001a!\u0010k\u001a\u00020\u0002*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\b\u001a\u0016\u0010k\u001a\u00020\u0002*\u00020\u00022\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\r\u0010k\u001a\u00020\n*\u00020\nH\b\u001a!\u0010k\u001a\u00020\n*\u00020\n2\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\b\u001a\u0016\u0010k\u001a\u00020\n*\u00020\n2\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\n\u0010m\u001a\u00020\u0002*\u00020\u0002\u001a!\u0010m\u001a\u00020\u0002*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\b\u001a\u0016\u0010m\u001a\u00020\u0002*\u00020\u00022\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\r\u0010m\u001a\u00020\n*\u00020\nH\b\u001a!\u0010m\u001a\u00020\n*\u00020\n2\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\b\u001a\u0016\u0010m\u001a\u00020\n*\u00020\n2\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\n\u0010n\u001a\u00020\u0002*\u00020\u0002\u001a!\u0010n\u001a\u00020\u0002*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\b\u001a\u0016\u0010n\u001a\u00020\u0002*\u00020\u00022\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\r\u0010n\u001a\u00020\n*\u00020\nH\b\u001a!\u0010n\u001a\u00020\n*\u00020\n2\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\b\u001a\u0016\u0010n\u001a\u00020\n*\u00020\n2\n\u0010,\u001a\u00020-\"\u00020\u0011\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006o"}, d2 = {"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/lang/CharSequence;)Lkotlin/ranges/IntRange;", "lastIndex", "", "getLastIndex", "(Ljava/lang/CharSequence;)I", "commonPrefixWith", "", "other", "ignoreCase", "", "commonSuffixWith", "contains", "char", "", "regex", "Lkotlin/text/Regex;", "endsWith", "suffix", "findAnyOf", "Lkotlin/Pair;", "strings", "", "startIndex", "last", "findAnyOf$StringsKt__StringsKt", "findLastAnyOf", "hasSurrogatePairAt", DJIFlurryReport.NativeExplore.V2_EXPLORE_SMALLBANNER_SUBKEY_INDEX, "ifBlank", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "ifEmpty", "indexOf", "endIndex", "indexOf$StringsKt__StringsKt", "string", "indexOfAny", "chars", "", "isEmpty", "isNotBlank", "isNotEmpty", "isNullOrBlank", "isNullOrEmpty", "iterator", "Lkotlin/collections/CharIterator;", "lastIndexOf", "lastIndexOfAny", "lineSequence", "Lkotlin/sequences/Sequence;", "lines", "", "matches", "orEmpty", "padEnd", DJIMediaStore.FileColumns.LENGTH, "padChar", "padStart", "rangesDelimitedBy", "delimiters", "", "limit", "rangesDelimitedBy$StringsKt__StringsKt", "(Ljava/lang/CharSequence;[Ljava/lang/String;IZI)Lkotlin/sequences/Sequence;", "regionMatchesImpl", "thisOffset", "otherOffset", "removePrefix", "prefix", "removeRange", "range", "removeSuffix", "removeSurrounding", "delimiter", "replace", "transform", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "replacement", "replaceAfter", "missingDelimiterValue", "replaceAfterLast", "replaceBefore", "replaceBeforeLast", "replaceFirst", "replaceRange", "split", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Ljava/util/List;", "split$StringsKt__StringsKt", "splitToSequence", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Lkotlin/sequences/Sequence;", "startsWith", "subSequence", "start", "end", "substring", "substringAfter", "substringAfterLast", "substringBefore", "substringBeforeLast", "trim", "predicate", "trimEnd", "trimStart", "kotlin-stdlib"}, k = 5, mv = {1, 1, 15}, xi = 1, xs = "kotlin/text/StringsKt")
/* compiled from: Strings.kt */
class StringsKt__StringsKt extends StringsKt__StringsJVMKt {
    @NotNull
    public static final CharSequence trim(@NotNull CharSequence $this$trim, @NotNull Function1<? super Character, Boolean> predicate) {
        int index;
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int startIndex = 0;
        int endIndex = $this$trim.length() - 1;
        boolean startFound = false;
        while (startIndex <= endIndex) {
            if (!startFound) {
                index = startIndex;
            } else {
                index = endIndex;
            }
            boolean match = predicate.invoke(Character.valueOf($this$trim.charAt(index))).booleanValue();
            if (!startFound) {
                if (!match) {
                    startFound = true;
                } else {
                    startIndex++;
                }
            } else if (!match) {
                break;
            } else {
                endIndex--;
            }
        }
        return $this$trim.subSequence(startIndex, endIndex + 1);
    }

    @NotNull
    public static final String trim(@NotNull String $this$trim, @NotNull Function1<? super Character, Boolean> predicate) {
        int index$iv;
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence $this$trim$iv = $this$trim;
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim$iv.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            if (!startFound$iv) {
                index$iv = startIndex$iv;
            } else {
                index$iv = endIndex$iv;
            }
            boolean match$iv = predicate.invoke(Character.valueOf($this$trim$iv.charAt(index$iv))).booleanValue();
            if (!startFound$iv) {
                if (!match$iv) {
                    startFound$iv = true;
                } else {
                    startIndex$iv++;
                }
            } else if (!match$iv) {
                break;
            } else {
                endIndex$iv--;
            }
        }
        return $this$trim$iv.subSequence(startIndex$iv, endIndex$iv + 1).toString();
    }

    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence $this$trimStart, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = $this$trimStart.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf($this$trimStart.charAt(i))).booleanValue()) {
                return $this$trimStart.subSequence(i, $this$trimStart.length());
            }
        }
        return "";
    }

    @NotNull
    public static final String trimStart(@NotNull String $this$trimStart, @NotNull Function1<? super Character, Boolean> predicate) {
        CharSequence charSequence;
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence $this$trimStart$iv = $this$trimStart;
        int i = 0;
        int length = $this$trimStart$iv.length();
        while (true) {
            if (i >= length) {
                break;
            } else if (!predicate.invoke(Character.valueOf($this$trimStart$iv.charAt(i))).booleanValue()) {
                charSequence = $this$trimStart$iv.subSequence(i, $this$trimStart$iv.length());
                break;
            } else {
                i++;
            }
        }
        return charSequence.toString();
    }

    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence $this$trimEnd, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int length = $this$trimEnd.length() - 1; length >= 0; length--) {
            if (!predicate.invoke(Character.valueOf($this$trimEnd.charAt(length))).booleanValue()) {
                return $this$trimEnd.subSequence(0, length + 1);
            }
        }
        return "";
    }

    @NotNull
    public static final String trimEnd(@NotNull String $this$trimEnd, @NotNull Function1<? super Character, Boolean> predicate) {
        CharSequence charSequence;
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence $this$trimEnd$iv = $this$trimEnd;
        int length = $this$trimEnd$iv.length() - 1;
        while (true) {
            if (length < 0) {
                break;
            } else if (!predicate.invoke(Character.valueOf($this$trimEnd$iv.charAt(length))).booleanValue()) {
                charSequence = $this$trimEnd$iv.subSequence(0, length + 1);
                break;
            } else {
                length--;
            }
        }
        return charSequence.toString();
    }

    @NotNull
    public static final CharSequence trim(@NotNull CharSequence $this$trim, @NotNull char... chars) {
        int index$iv;
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        CharSequence $this$trim$iv = $this$trim;
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim$iv.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            if (!startFound$iv) {
                index$iv = startIndex$iv;
            } else {
                index$iv = endIndex$iv;
            }
            boolean match$iv = ArraysKt.contains(chars, $this$trim$iv.charAt(index$iv));
            if (!startFound$iv) {
                if (!match$iv) {
                    startFound$iv = true;
                } else {
                    startIndex$iv++;
                }
            } else if (!match$iv) {
                break;
            } else {
                endIndex$iv--;
            }
        }
        return $this$trim$iv.subSequence(startIndex$iv, endIndex$iv + 1);
    }

    @NotNull
    public static final String trim(@NotNull String $this$trim, @NotNull char... chars) {
        int index$iv$iv;
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        CharSequence $this$trim$iv$iv = $this$trim;
        int startIndex$iv$iv = 0;
        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
        boolean startFound$iv$iv = false;
        while (startIndex$iv$iv <= endIndex$iv$iv) {
            if (!startFound$iv$iv) {
                index$iv$iv = startIndex$iv$iv;
            } else {
                index$iv$iv = endIndex$iv$iv;
            }
            boolean match$iv$iv = ArraysKt.contains(chars, $this$trim$iv$iv.charAt(index$iv$iv));
            if (!startFound$iv$iv) {
                if (!match$iv$iv) {
                    startFound$iv$iv = true;
                } else {
                    startIndex$iv$iv++;
                }
            } else if (!match$iv$iv) {
                break;
            } else {
                endIndex$iv$iv--;
            }
        }
        return $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
    }

    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence $this$trimStart, @NotNull char... chars) {
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        CharSequence $this$trimStart$iv = $this$trimStart;
        int length = $this$trimStart$iv.length();
        for (int i = 0; i < length; i++) {
            if (!ArraysKt.contains(chars, $this$trimStart$iv.charAt(i))) {
                return $this$trimStart$iv.subSequence(i, $this$trimStart$iv.length());
            }
        }
        return "";
    }

    @NotNull
    public static final String trimStart(@NotNull String $this$trimStart, @NotNull char... chars) {
        CharSequence charSequence;
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        CharSequence $this$trimStart$iv$iv = $this$trimStart;
        int i = 0;
        int length = $this$trimStart$iv$iv.length();
        while (true) {
            if (i >= length) {
                break;
            } else if (!ArraysKt.contains(chars, $this$trimStart$iv$iv.charAt(i))) {
                charSequence = $this$trimStart$iv$iv.subSequence(i, $this$trimStart$iv$iv.length());
                break;
            } else {
                i++;
            }
        }
        return charSequence.toString();
    }

    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence $this$trimEnd, @NotNull char... chars) {
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        CharSequence $this$trimEnd$iv = $this$trimEnd;
        for (int length = $this$trimEnd$iv.length() - 1; length >= 0; length--) {
            if (!ArraysKt.contains(chars, $this$trimEnd$iv.charAt(length))) {
                return $this$trimEnd$iv.subSequence(0, length + 1);
            }
        }
        return "";
    }

    @NotNull
    public static final String trimEnd(@NotNull String $this$trimEnd, @NotNull char... chars) {
        CharSequence charSequence;
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        CharSequence $this$trimEnd$iv$iv = $this$trimEnd;
        int length = $this$trimEnd$iv$iv.length() - 1;
        while (true) {
            if (length < 0) {
                break;
            } else if (!ArraysKt.contains(chars, $this$trimEnd$iv$iv.charAt(length))) {
                charSequence = $this$trimEnd$iv$iv.subSequence(0, length + 1);
                break;
            } else {
                length--;
            }
        }
        return charSequence.toString();
    }

    @NotNull
    public static final CharSequence trim(@NotNull CharSequence $this$trim) {
        int index$iv;
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        CharSequence $this$trim$iv = $this$trim;
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim$iv.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            if (!startFound$iv) {
                index$iv = startIndex$iv;
            } else {
                index$iv = endIndex$iv;
            }
            boolean match$iv = CharsKt.isWhitespace($this$trim$iv.charAt(index$iv));
            if (!startFound$iv) {
                if (!match$iv) {
                    startFound$iv = true;
                } else {
                    startIndex$iv++;
                }
            } else if (!match$iv) {
                break;
            } else {
                endIndex$iv--;
            }
        }
        return $this$trim$iv.subSequence(startIndex$iv, endIndex$iv + 1);
    }

    @InlineOnly
    private static final String trim(@NotNull String $this$trim) {
        if ($this$trim != null) {
            return StringsKt.trim((CharSequence) $this$trim).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence $this$trimStart) {
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        CharSequence $this$trimStart$iv = $this$trimStart;
        int length = $this$trimStart$iv.length();
        for (int i = 0; i < length; i++) {
            if (!CharsKt.isWhitespace($this$trimStart$iv.charAt(i))) {
                return $this$trimStart$iv.subSequence(i, $this$trimStart$iv.length());
            }
        }
        return "";
    }

    @InlineOnly
    private static final String trimStart(@NotNull String $this$trimStart) {
        if ($this$trimStart != null) {
            return StringsKt.trimStart((CharSequence) $this$trimStart).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence $this$trimEnd) {
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        CharSequence $this$trimEnd$iv = $this$trimEnd;
        for (int length = $this$trimEnd$iv.length() - 1; length >= 0; length--) {
            if (!CharsKt.isWhitespace($this$trimEnd$iv.charAt(length))) {
                return $this$trimEnd$iv.subSequence(0, length + 1);
            }
        }
        return "";
    }

    @InlineOnly
    private static final String trimEnd(@NotNull String $this$trimEnd) {
        if ($this$trimEnd != null) {
            return StringsKt.trimEnd((CharSequence) $this$trimEnd).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static /* synthetic */ CharSequence padStart$default(CharSequence charSequence, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padStart(charSequence, i, c);
    }

    @NotNull
    public static final CharSequence padStart(@NotNull CharSequence $this$padStart, int length, char padChar) {
        Intrinsics.checkParameterIsNotNull($this$padStart, "$this$padStart");
        if (length < 0) {
            throw new IllegalArgumentException("Desired length " + length + " is less than zero.");
        } else if (length <= $this$padStart.length()) {
            return $this$padStart.subSequence(0, $this$padStart.length());
        } else {
            StringBuilder sb = new StringBuilder(length);
            int i = 1;
            int length2 = length - $this$padStart.length();
            if (1 <= length2) {
                while (true) {
                    sb.append(padChar);
                    if (i == length2) {
                        break;
                    }
                    i++;
                }
            }
            sb.append($this$padStart);
            return sb;
        }
    }

    public static /* synthetic */ String padStart$default(String str, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padStart(str, i, c);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.padStart(java.lang.CharSequence, int, char):java.lang.CharSequence
     arg types: [java.lang.String, int, char]
     candidates:
      kotlin.text.StringsKt__StringsKt.padStart(java.lang.String, int, char):java.lang.String
      kotlin.text.StringsKt__StringsKt.padStart(java.lang.CharSequence, int, char):java.lang.CharSequence */
    @NotNull
    public static final String padStart(@NotNull String $this$padStart, int length, char padChar) {
        Intrinsics.checkParameterIsNotNull($this$padStart, "$this$padStart");
        return StringsKt.padStart((CharSequence) $this$padStart, length, padChar).toString();
    }

    public static /* synthetic */ CharSequence padEnd$default(CharSequence charSequence, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padEnd(charSequence, i, c);
    }

    @NotNull
    public static final CharSequence padEnd(@NotNull CharSequence $this$padEnd, int length, char padChar) {
        Intrinsics.checkParameterIsNotNull($this$padEnd, "$this$padEnd");
        if (length < 0) {
            throw new IllegalArgumentException("Desired length " + length + " is less than zero.");
        } else if (length <= $this$padEnd.length()) {
            return $this$padEnd.subSequence(0, $this$padEnd.length());
        } else {
            StringBuilder sb = new StringBuilder(length);
            sb.append($this$padEnd);
            int i = 1;
            int length2 = length - $this$padEnd.length();
            if (1 <= length2) {
                while (true) {
                    sb.append(padChar);
                    if (i == length2) {
                        break;
                    }
                    i++;
                }
            }
            return sb;
        }
    }

    public static /* synthetic */ String padEnd$default(String str, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padEnd(str, i, c);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.padEnd(java.lang.CharSequence, int, char):java.lang.CharSequence
     arg types: [java.lang.String, int, char]
     candidates:
      kotlin.text.StringsKt__StringsKt.padEnd(java.lang.String, int, char):java.lang.String
      kotlin.text.StringsKt__StringsKt.padEnd(java.lang.CharSequence, int, char):java.lang.CharSequence */
    @NotNull
    public static final String padEnd(@NotNull String $this$padEnd, int length, char padChar) {
        Intrinsics.checkParameterIsNotNull($this$padEnd, "$this$padEnd");
        return StringsKt.padEnd((CharSequence) $this$padEnd, length, padChar).toString();
    }

    @InlineOnly
    private static final boolean isNullOrEmpty(@Nullable CharSequence $this$isNullOrEmpty) {
        return $this$isNullOrEmpty == null || $this$isNullOrEmpty.length() == 0;
    }

    @InlineOnly
    private static final boolean isEmpty(@NotNull CharSequence $this$isEmpty) {
        return $this$isEmpty.length() == 0;
    }

    @InlineOnly
    private static final boolean isNotEmpty(@NotNull CharSequence $this$isNotEmpty) {
        return $this$isNotEmpty.length() > 0;
    }

    @InlineOnly
    private static final boolean isNotBlank(@NotNull CharSequence $this$isNotBlank) {
        return !StringsKt.isBlank($this$isNotBlank);
    }

    @InlineOnly
    private static final boolean isNullOrBlank(@Nullable CharSequence $this$isNullOrBlank) {
        return $this$isNullOrBlank == null || StringsKt.isBlank($this$isNullOrBlank);
    }

    @NotNull
    public static final CharIterator iterator(@NotNull CharSequence $this$iterator) {
        Intrinsics.checkParameterIsNotNull($this$iterator, "$this$iterator");
        return new StringsKt__StringsKt$iterator$1($this$iterator);
    }

    @InlineOnly
    private static final String orEmpty(@Nullable String $this$orEmpty) {
        return $this$orEmpty != null ? $this$orEmpty : "";
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [R, C, java.lang.CharSequence], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @kotlin.SinceKotlin(version = "1.3")
    @kotlin.internal.InlineOnly
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final <C extends java.lang.CharSequence & R, R> R ifEmpty(C r2, kotlin.jvm.functions.Function0<? extends R> r3) {
        /*
            r0 = 0
            int r1 = r2.length()
            if (r1 != 0) goto L_0x000f
            r1 = 1
        L_0x0008:
            if (r1 == 0) goto L_0x000e
            java.lang.Object r2 = r3.invoke()
        L_0x000e:
            return r2
        L_0x000f:
            r1 = 0
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringsKt.ifEmpty(java.lang.CharSequence, kotlin.jvm.functions.Function0):java.lang.Object");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [R, C, java.lang.CharSequence], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    @kotlin.SinceKotlin(version = "1.3")
    @kotlin.internal.InlineOnly
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final <C extends java.lang.CharSequence & R, R> R ifBlank(C r2, kotlin.jvm.functions.Function0<? extends R> r3) {
        /*
            r0 = 0
            boolean r1 = kotlin.text.StringsKt.isBlank(r2)
            if (r1 == 0) goto L_0x000b
            java.lang.Object r2 = r3.invoke()
        L_0x000b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringsKt.ifBlank(java.lang.CharSequence, kotlin.jvm.functions.Function0):java.lang.Object");
    }

    @NotNull
    public static final IntRange getIndices(@NotNull CharSequence $this$indices) {
        Intrinsics.checkParameterIsNotNull($this$indices, "$this$indices");
        return new IntRange(0, $this$indices.length() - 1);
    }

    public static final int getLastIndex(@NotNull CharSequence $this$lastIndex) {
        Intrinsics.checkParameterIsNotNull($this$lastIndex, "$this$lastIndex");
        return $this$lastIndex.length() - 1;
    }

    public static final boolean hasSurrogatePairAt(@NotNull CharSequence $this$hasSurrogatePairAt, int index) {
        Intrinsics.checkParameterIsNotNull($this$hasSurrogatePairAt, "$this$hasSurrogatePairAt");
        return index >= 0 && $this$hasSurrogatePairAt.length() + -2 >= index && Character.isHighSurrogate($this$hasSurrogatePairAt.charAt(index)) && Character.isLowSurrogate($this$hasSurrogatePairAt.charAt(index + 1));
    }

    @NotNull
    public static final String substring(@NotNull String $this$substring, @NotNull IntRange range) {
        Intrinsics.checkParameterIsNotNull($this$substring, "$this$substring");
        Intrinsics.checkParameterIsNotNull(range, "range");
        String substring = $this$substring.substring(range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return substring;
    }

    @NotNull
    public static final CharSequence subSequence(@NotNull CharSequence $this$subSequence, @NotNull IntRange range) {
        Intrinsics.checkParameterIsNotNull($this$subSequence, "$this$subSequence");
        Intrinsics.checkParameterIsNotNull(range, "range");
        return $this$subSequence.subSequence(range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
    }

    @Deprecated(message = "Use parameters named startIndex and endIndex.", replaceWith = @ReplaceWith(expression = "subSequence(startIndex = start, endIndex = end)", imports = {}))
    @InlineOnly
    private static final CharSequence subSequence(@NotNull String $this$subSequence, int start, int end) {
        return $this$subSequence.subSequence(start, end);
    }

    @InlineOnly
    private static final String substring(@NotNull CharSequence $this$substring, int startIndex, int endIndex) {
        return $this$substring.subSequence(startIndex, endIndex).toString();
    }

    static /* synthetic */ String substring$default(CharSequence $this$substring, int startIndex, int endIndex, int i, Object obj) {
        if ((i & 2) != 0) {
            endIndex = $this$substring.length();
        }
        return $this$substring.subSequence(startIndex, endIndex).toString();
    }

    @NotNull
    public static final String substring(@NotNull CharSequence $this$substring, @NotNull IntRange range) {
        Intrinsics.checkParameterIsNotNull($this$substring, "$this$substring");
        Intrinsics.checkParameterIsNotNull(range, "range");
        return $this$substring.subSequence(range.getStart().intValue(), range.getEndInclusive().intValue() + 1).toString();
    }

    public static /* synthetic */ String substringBefore$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return StringsKt.substringBefore(str, c, str2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int */
    @NotNull
    public static final String substringBefore(@NotNull String $this$substringBefore, char delimiter, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringBefore, "$this$substringBefore");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$substringBefore, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String missingDelimiterValue2 = $this$substringBefore.substring(0, index);
        Intrinsics.checkExpressionValueIsNotNull(missingDelimiterValue2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return missingDelimiterValue2;
    }

    public static /* synthetic */ String substringBefore$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return StringsKt.substringBefore(str, str2, str3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    @NotNull
    public static final String substringBefore(@NotNull String $this$substringBefore, @NotNull String delimiter, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringBefore, "$this$substringBefore");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$substringBefore, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String missingDelimiterValue2 = $this$substringBefore.substring(0, index);
        Intrinsics.checkExpressionValueIsNotNull(missingDelimiterValue2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return missingDelimiterValue2;
    }

    public static /* synthetic */ String substringAfter$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return StringsKt.substringAfter(str, c, str2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int */
    @NotNull
    public static final String substringAfter(@NotNull String $this$substringAfter, char delimiter, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringAfter, "$this$substringAfter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$substringAfter, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String missingDelimiterValue2 = $this$substringAfter.substring(index + 1, $this$substringAfter.length());
        Intrinsics.checkExpressionValueIsNotNull(missingDelimiterValue2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return missingDelimiterValue2;
    }

    public static /* synthetic */ String substringAfter$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return StringsKt.substringAfter(str, str2, str3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    @NotNull
    public static final String substringAfter(@NotNull String $this$substringAfter, @NotNull String delimiter, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringAfter, "$this$substringAfter");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$substringAfter, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String missingDelimiterValue2 = $this$substringAfter.substring(delimiter.length() + index, $this$substringAfter.length());
        Intrinsics.checkExpressionValueIsNotNull(missingDelimiterValue2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return missingDelimiterValue2;
    }

    public static /* synthetic */ String substringBeforeLast$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return StringsKt.substringBeforeLast(str, c, str2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int */
    @NotNull
    public static final String substringBeforeLast(@NotNull String $this$substringBeforeLast, char delimiter, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringBeforeLast, "$this$substringBeforeLast");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$substringBeforeLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String missingDelimiterValue2 = $this$substringBeforeLast.substring(0, index);
        Intrinsics.checkExpressionValueIsNotNull(missingDelimiterValue2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return missingDelimiterValue2;
    }

    public static /* synthetic */ String substringBeforeLast$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return StringsKt.substringBeforeLast(str, str2, str3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    @NotNull
    public static final String substringBeforeLast(@NotNull String $this$substringBeforeLast, @NotNull String delimiter, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringBeforeLast, "$this$substringBeforeLast");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$substringBeforeLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String missingDelimiterValue2 = $this$substringBeforeLast.substring(0, index);
        Intrinsics.checkExpressionValueIsNotNull(missingDelimiterValue2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return missingDelimiterValue2;
    }

    public static /* synthetic */ String substringAfterLast$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return StringsKt.substringAfterLast(str, c, str2);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int */
    @NotNull
    public static final String substringAfterLast(@NotNull String $this$substringAfterLast, char delimiter, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringAfterLast, "$this$substringAfterLast");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$substringAfterLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String missingDelimiterValue2 = $this$substringAfterLast.substring(index + 1, $this$substringAfterLast.length());
        Intrinsics.checkExpressionValueIsNotNull(missingDelimiterValue2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return missingDelimiterValue2;
    }

    public static /* synthetic */ String substringAfterLast$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return StringsKt.substringAfterLast(str, str2, str3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    @NotNull
    public static final String substringAfterLast(@NotNull String $this$substringAfterLast, @NotNull String delimiter, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringAfterLast, "$this$substringAfterLast");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$substringAfterLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String missingDelimiterValue2 = $this$substringAfterLast.substring(delimiter.length() + index, $this$substringAfterLast.length());
        Intrinsics.checkExpressionValueIsNotNull(missingDelimiterValue2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return missingDelimiterValue2;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.StringBuilder.append(java.lang.CharSequence, int, int):java.lang.StringBuilder}
     arg types: [java.lang.CharSequence, int, int]
     candidates:
      ClspMth{java.lang.StringBuilder.append(java.lang.CharSequence, int, int):java.lang.Appendable throws java.io.IOException}
      ClspMth{java.lang.StringBuilder.append(char[], int, int):java.lang.StringBuilder}
      ClspMth{java.lang.Appendable.append(java.lang.CharSequence, int, int):java.lang.Appendable throws java.io.IOException}
      ClspMth{java.lang.StringBuilder.append(java.lang.CharSequence, int, int):java.lang.StringBuilder} */
    @NotNull
    public static final CharSequence replaceRange(@NotNull CharSequence $this$replaceRange, int startIndex, int endIndex, @NotNull CharSequence replacement) {
        Intrinsics.checkParameterIsNotNull($this$replaceRange, "$this$replaceRange");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        if (endIndex < startIndex) {
            throw new IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
        }
        StringBuilder sb = new StringBuilder();
        sb.append($this$replaceRange, 0, startIndex);
        sb.append(replacement);
        sb.append($this$replaceRange, endIndex, $this$replaceRange.length());
        return sb;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, int, int, java.lang.CharSequence]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, int, int, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence */
    @InlineOnly
    private static final String replaceRange(@NotNull String $this$replaceRange, int startIndex, int endIndex, CharSequence replacement) {
        if ($this$replaceRange != null) {
            return StringsKt.replaceRange((CharSequence) $this$replaceRange, startIndex, endIndex, replacement).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    @NotNull
    public static final CharSequence replaceRange(@NotNull CharSequence $this$replaceRange, @NotNull IntRange range, @NotNull CharSequence replacement) {
        Intrinsics.checkParameterIsNotNull($this$replaceRange, "$this$replaceRange");
        Intrinsics.checkParameterIsNotNull(range, "range");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        return StringsKt.replaceRange($this$replaceRange, range.getStart().intValue(), range.getEndInclusive().intValue() + 1, replacement);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, kotlin.ranges.IntRange, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, kotlin.ranges.IntRange, java.lang.CharSequence]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, kotlin.ranges.IntRange, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, kotlin.ranges.IntRange, java.lang.CharSequence):java.lang.CharSequence */
    @InlineOnly
    private static final String replaceRange(@NotNull String $this$replaceRange, IntRange range, CharSequence replacement) {
        if ($this$replaceRange != null) {
            return StringsKt.replaceRange((CharSequence) $this$replaceRange, range, replacement).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.StringBuilder.append(java.lang.CharSequence, int, int):java.lang.StringBuilder}
     arg types: [java.lang.CharSequence, int, int]
     candidates:
      ClspMth{java.lang.StringBuilder.append(java.lang.CharSequence, int, int):java.lang.Appendable throws java.io.IOException}
      ClspMth{java.lang.StringBuilder.append(char[], int, int):java.lang.StringBuilder}
      ClspMth{java.lang.Appendable.append(java.lang.CharSequence, int, int):java.lang.Appendable throws java.io.IOException}
      ClspMth{java.lang.StringBuilder.append(java.lang.CharSequence, int, int):java.lang.StringBuilder} */
    @NotNull
    public static final CharSequence removeRange(@NotNull CharSequence $this$removeRange, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$removeRange, "$this$removeRange");
        if (endIndex < startIndex) {
            throw new IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
        } else if (endIndex == startIndex) {
            return $this$removeRange.subSequence(0, $this$removeRange.length());
        } else {
            StringBuilder sb = new StringBuilder($this$removeRange.length() - (endIndex - startIndex));
            sb.append($this$removeRange, 0, startIndex);
            sb.append($this$removeRange, endIndex, $this$removeRange.length());
            return sb;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.removeRange(java.lang.CharSequence, int, int):java.lang.CharSequence
     arg types: [java.lang.String, int, int]
     candidates:
      kotlin.text.StringsKt__StringsKt.removeRange(java.lang.String, int, int):java.lang.String
      kotlin.text.StringsKt__StringsKt.removeRange(java.lang.CharSequence, int, int):java.lang.CharSequence */
    @InlineOnly
    private static final String removeRange(@NotNull String $this$removeRange, int startIndex, int endIndex) {
        if ($this$removeRange != null) {
            return StringsKt.removeRange((CharSequence) $this$removeRange, startIndex, endIndex).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    @NotNull
    public static final CharSequence removeRange(@NotNull CharSequence $this$removeRange, @NotNull IntRange range) {
        Intrinsics.checkParameterIsNotNull($this$removeRange, "$this$removeRange");
        Intrinsics.checkParameterIsNotNull(range, "range");
        return StringsKt.removeRange($this$removeRange, range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.removeRange(java.lang.CharSequence, kotlin.ranges.IntRange):java.lang.CharSequence
     arg types: [java.lang.String, kotlin.ranges.IntRange]
     candidates:
      kotlin.text.StringsKt__StringsKt.removeRange(java.lang.String, kotlin.ranges.IntRange):java.lang.String
      kotlin.text.StringsKt__StringsKt.removeRange(java.lang.CharSequence, kotlin.ranges.IntRange):java.lang.CharSequence */
    @InlineOnly
    private static final String removeRange(@NotNull String $this$removeRange, IntRange range) {
        if ($this$removeRange != null) {
            return StringsKt.removeRange((CharSequence) $this$removeRange, range).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.CharSequence, java.lang.CharSequence, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.startsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean */
    @NotNull
    public static final CharSequence removePrefix(@NotNull CharSequence $this$removePrefix, @NotNull CharSequence prefix) {
        Intrinsics.checkParameterIsNotNull($this$removePrefix, "$this$removePrefix");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (StringsKt.startsWith$default($this$removePrefix, prefix, false, 2, (Object) null)) {
            return $this$removePrefix.subSequence(prefix.length(), $this$removePrefix.length());
        }
        return $this$removePrefix.subSequence(0, $this$removePrefix.length());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.String, java.lang.CharSequence, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.startsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean */
    @NotNull
    public static final String removePrefix(@NotNull String $this$removePrefix, @NotNull CharSequence prefix) {
        Intrinsics.checkParameterIsNotNull($this$removePrefix, "$this$removePrefix");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!StringsKt.startsWith$default((CharSequence) $this$removePrefix, prefix, false, 2, (Object) null)) {
            return $this$removePrefix;
        }
        String $this$removePrefix2 = $this$removePrefix.substring(prefix.length());
        Intrinsics.checkExpressionValueIsNotNull($this$removePrefix2, "(this as java.lang.String).substring(startIndex)");
        return $this$removePrefix2;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.CharSequence, java.lang.CharSequence, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.endsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean */
    @NotNull
    public static final CharSequence removeSuffix(@NotNull CharSequence $this$removeSuffix, @NotNull CharSequence suffix) {
        Intrinsics.checkParameterIsNotNull($this$removeSuffix, "$this$removeSuffix");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if (StringsKt.endsWith$default($this$removeSuffix, suffix, false, 2, (Object) null)) {
            return $this$removeSuffix.subSequence(0, $this$removeSuffix.length() - suffix.length());
        }
        return $this$removeSuffix.subSequence(0, $this$removeSuffix.length());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.String, java.lang.CharSequence, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.endsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean */
    @NotNull
    public static final String removeSuffix(@NotNull String $this$removeSuffix, @NotNull CharSequence suffix) {
        Intrinsics.checkParameterIsNotNull($this$removeSuffix, "$this$removeSuffix");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if (!StringsKt.endsWith$default((CharSequence) $this$removeSuffix, suffix, false, 2, (Object) null)) {
            return $this$removeSuffix;
        }
        String $this$removeSuffix2 = $this$removeSuffix.substring(0, $this$removeSuffix.length() - suffix.length());
        Intrinsics.checkExpressionValueIsNotNull($this$removeSuffix2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return $this$removeSuffix2;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.CharSequence, java.lang.CharSequence, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.startsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.CharSequence, java.lang.CharSequence, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.endsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean */
    @NotNull
    public static final CharSequence removeSurrounding(@NotNull CharSequence $this$removeSurrounding, @NotNull CharSequence prefix, @NotNull CharSequence suffix) {
        Intrinsics.checkParameterIsNotNull($this$removeSurrounding, "$this$removeSurrounding");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if ($this$removeSurrounding.length() < prefix.length() + suffix.length() || !StringsKt.startsWith$default($this$removeSurrounding, prefix, false, 2, (Object) null) || !StringsKt.endsWith$default($this$removeSurrounding, suffix, false, 2, (Object) null)) {
            return $this$removeSurrounding.subSequence(0, $this$removeSurrounding.length());
        }
        return $this$removeSurrounding.subSequence(prefix.length(), $this$removeSurrounding.length() - suffix.length());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.String, java.lang.CharSequence, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.startsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.String, java.lang.CharSequence, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.endsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean */
    @NotNull
    public static final String removeSurrounding(@NotNull String $this$removeSurrounding, @NotNull CharSequence prefix, @NotNull CharSequence suffix) {
        Intrinsics.checkParameterIsNotNull($this$removeSurrounding, "$this$removeSurrounding");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if ($this$removeSurrounding.length() < prefix.length() + suffix.length() || !StringsKt.startsWith$default((CharSequence) $this$removeSurrounding, prefix, false, 2, (Object) null) || !StringsKt.endsWith$default((CharSequence) $this$removeSurrounding, suffix, false, 2, (Object) null)) {
            return $this$removeSurrounding;
        }
        String $this$removeSurrounding2 = $this$removeSurrounding.substring(prefix.length(), $this$removeSurrounding.length() - suffix.length());
        Intrinsics.checkExpressionValueIsNotNull($this$removeSurrounding2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return $this$removeSurrounding2;
    }

    @NotNull
    public static final CharSequence removeSurrounding(@NotNull CharSequence $this$removeSurrounding, @NotNull CharSequence delimiter) {
        Intrinsics.checkParameterIsNotNull($this$removeSurrounding, "$this$removeSurrounding");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        return StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
    }

    @NotNull
    public static final String removeSurrounding(@NotNull String $this$removeSurrounding, @NotNull CharSequence delimiter) {
        Intrinsics.checkParameterIsNotNull($this$removeSurrounding, "$this$removeSurrounding");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        return StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
    }

    public static /* synthetic */ String replaceBefore$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return StringsKt.replaceBefore(str, c, str2, str3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, int, int, java.lang.String]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, int, int, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence */
    @NotNull
    public static final String replaceBefore(@NotNull String $this$replaceBefore, char delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceBefore, "$this$replaceBefore");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$replaceBefore, delimiter, 0, false, 6, (Object) null);
        return index == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBefore, 0, index, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceBefore$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return StringsKt.replaceBefore(str, str2, str3, str4);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, int, int, java.lang.String]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, int, int, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence */
    @NotNull
    public static final String replaceBefore(@NotNull String $this$replaceBefore, @NotNull String delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceBefore, "$this$replaceBefore");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$replaceBefore, delimiter, 0, false, 6, (Object) null);
        return index == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBefore, 0, index, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceAfter$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return StringsKt.replaceAfter(str, c, str2, str3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, int, int, java.lang.String]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, int, int, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence */
    @NotNull
    public static final String replaceAfter(@NotNull String $this$replaceAfter, char delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceAfter, "$this$replaceAfter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$replaceAfter, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        int length = $this$replaceAfter.length();
        return StringsKt.replaceRange((CharSequence) $this$replaceAfter, index + 1, length, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceAfter$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return StringsKt.replaceAfter(str, str2, str3, str4);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, int, int, java.lang.String]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, int, int, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence */
    @NotNull
    public static final String replaceAfter(@NotNull String $this$replaceAfter, @NotNull String delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceAfter, "$this$replaceAfter");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$replaceAfter, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        int length = $this$replaceAfter.length();
        return StringsKt.replaceRange((CharSequence) $this$replaceAfter, delimiter.length() + index, length, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceAfterLast$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return StringsKt.replaceAfterLast(str, str2, str3, str4);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, int, int, java.lang.String]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, int, int, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence */
    @NotNull
    public static final String replaceAfterLast(@NotNull String $this$replaceAfterLast, @NotNull String delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceAfterLast, "$this$replaceAfterLast");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$replaceAfterLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        int length = $this$replaceAfterLast.length();
        return StringsKt.replaceRange((CharSequence) $this$replaceAfterLast, delimiter.length() + index, length, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceAfterLast$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return StringsKt.replaceAfterLast(str, c, str2, str3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, int, int, java.lang.String]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, int, int, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence */
    @NotNull
    public static final String replaceAfterLast(@NotNull String $this$replaceAfterLast, char delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceAfterLast, "$this$replaceAfterLast");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$replaceAfterLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        int length = $this$replaceAfterLast.length();
        return StringsKt.replaceRange((CharSequence) $this$replaceAfterLast, index + 1, length, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceBeforeLast$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return StringsKt.replaceBeforeLast(str, c, str2, str3);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, int, int, java.lang.String]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, int, int, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence */
    @NotNull
    public static final String replaceBeforeLast(@NotNull String $this$replaceBeforeLast, char delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceBeforeLast, "$this$replaceBeforeLast");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$replaceBeforeLast, delimiter, 0, false, 6, (Object) null);
        return index == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBeforeLast, 0, index, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceBeforeLast$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return StringsKt.replaceBeforeLast(str, str2, str3, str4);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence
     arg types: [java.lang.String, int, int, java.lang.String]
     candidates:
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.String, int, int, java.lang.CharSequence):java.lang.String
      kotlin.text.StringsKt__StringsKt.replaceRange(java.lang.CharSequence, int, int, java.lang.CharSequence):java.lang.CharSequence */
    @NotNull
    public static final String replaceBeforeLast(@NotNull String $this$replaceBeforeLast, @NotNull String delimiter, @NotNull String replacement, @NotNull String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceBeforeLast, "$this$replaceBeforeLast");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$replaceBeforeLast, delimiter, 0, false, 6, (Object) null);
        return index == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBeforeLast, 0, index, (CharSequence) replacement).toString();
    }

    @InlineOnly
    private static final String replace(@NotNull CharSequence $this$replace, Regex regex, String replacement) {
        return regex.replace($this$replace, replacement);
    }

    @InlineOnly
    private static final String replace(@NotNull CharSequence $this$replace, Regex regex, Function1<? super MatchResult, ? extends CharSequence> transform) {
        return regex.replace($this$replace, transform);
    }

    @InlineOnly
    private static final String replaceFirst(@NotNull CharSequence $this$replaceFirst, Regex regex, String replacement) {
        return regex.replaceFirst($this$replaceFirst, replacement);
    }

    @InlineOnly
    private static final boolean matches(@NotNull CharSequence $this$matches, Regex regex) {
        return regex.matches($this$matches);
    }

    public static final boolean regionMatchesImpl(@NotNull CharSequence $this$regionMatchesImpl, int thisOffset, @NotNull CharSequence other, int otherOffset, int length, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$regionMatchesImpl, "$this$regionMatchesImpl");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (otherOffset < 0 || thisOffset < 0 || thisOffset > $this$regionMatchesImpl.length() - length || otherOffset > other.length() - length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!CharsKt.equals($this$regionMatchesImpl.charAt(thisOffset + i), other.charAt(otherOffset + i), ignoreCase)) {
                return false;
            }
        }
        return true;
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, char c, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.startsWith(charSequence, c, z);
    }

    public static final boolean startsWith(@NotNull CharSequence $this$startsWith, char c, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        return $this$startsWith.length() > 0 && CharsKt.equals($this$startsWith.charAt(0), c, ignoreCase);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, char c, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.endsWith(charSequence, c, z);
    }

    public static final boolean endsWith(@NotNull CharSequence $this$endsWith, char c, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$endsWith, "$this$endsWith");
        return $this$endsWith.length() > 0 && CharsKt.equals($this$endsWith.charAt(StringsKt.getLastIndex($this$endsWith)), c, ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, z);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsJVMKt.startsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.String, java.lang.String, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.startsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean */
    public static final boolean startsWith(@NotNull CharSequence $this$startsWith, @NotNull CharSequence prefix, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!ignoreCase && ($this$startsWith instanceof String) && (prefix instanceof String)) {
            return StringsKt.startsWith$default((String) $this$startsWith, (String) prefix, false, 2, (Object) null);
        }
        return StringsKt.regionMatchesImpl($this$startsWith, 0, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, int i, boolean z, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, i, z);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsJVMKt.startsWith$default(java.lang.String, java.lang.String, int, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.String, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.startsWith$default(java.lang.CharSequence, java.lang.CharSequence, int, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.startsWith$default(java.lang.String, java.lang.String, int, boolean, int, java.lang.Object):boolean */
    public static final boolean startsWith(@NotNull CharSequence $this$startsWith, @NotNull CharSequence prefix, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!ignoreCase && ($this$startsWith instanceof String) && (prefix instanceof String)) {
            return StringsKt.startsWith$default((String) $this$startsWith, (String) prefix, startIndex, false, 4, (Object) null);
        }
        return StringsKt.regionMatchesImpl($this$startsWith, startIndex, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.endsWith(charSequence, charSequence2, z);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsJVMKt.endsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.String, java.lang.String, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.endsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean */
    public static final boolean endsWith(@NotNull CharSequence $this$endsWith, @NotNull CharSequence suffix, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$endsWith, "$this$endsWith");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if (!ignoreCase && ($this$endsWith instanceof String) && (suffix instanceof String)) {
            return StringsKt.endsWith$default((String) $this$endsWith, (String) suffix, false, 2, (Object) null);
        }
        return StringsKt.regionMatchesImpl($this$endsWith, $this$endsWith.length() - suffix.length(), suffix, 0, suffix.length(), ignoreCase);
    }

    public static /* synthetic */ String commonPrefixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.commonPrefixWith(charSequence, charSequence2, z);
    }

    @NotNull
    public static final String commonPrefixWith(@NotNull CharSequence $this$commonPrefixWith, @NotNull CharSequence other, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$commonPrefixWith, "$this$commonPrefixWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int shortestLength = Math.min($this$commonPrefixWith.length(), other.length());
        int i = 0;
        while (i < shortestLength && CharsKt.equals($this$commonPrefixWith.charAt(i), other.charAt(i), ignoreCase)) {
            i++;
        }
        if (StringsKt.hasSurrogatePairAt($this$commonPrefixWith, i - 1) || StringsKt.hasSurrogatePairAt(other, i - 1)) {
            i--;
        }
        return $this$commonPrefixWith.subSequence(0, i).toString();
    }

    public static /* synthetic */ String commonSuffixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.commonSuffixWith(charSequence, charSequence2, z);
    }

    @NotNull
    public static final String commonSuffixWith(@NotNull CharSequence $this$commonSuffixWith, @NotNull CharSequence other, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$commonSuffixWith, "$this$commonSuffixWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int thisLength = $this$commonSuffixWith.length();
        int otherLength = other.length();
        int shortestLength = Math.min(thisLength, otherLength);
        int i = 0;
        while (i < shortestLength && CharsKt.equals($this$commonSuffixWith.charAt((thisLength - i) - 1), other.charAt((otherLength - i) - 1), ignoreCase)) {
            i++;
        }
        if (StringsKt.hasSurrogatePairAt($this$commonSuffixWith, (thisLength - i) - 1) || StringsKt.hasSurrogatePairAt(other, (otherLength - i) - 1)) {
            i--;
        }
        return $this$commonSuffixWith.subSequence(thisLength - i, thisLength).toString();
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, char[] cArr, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOfAny(charSequence, cArr, i, z);
    }

    public static final int indexOfAny(@NotNull CharSequence $this$indexOfAny, @NotNull char[] chars, int startIndex, boolean ignoreCase) {
        boolean z;
        Intrinsics.checkParameterIsNotNull($this$indexOfAny, "$this$indexOfAny");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        if (ignoreCase || chars.length != 1 || !($this$indexOfAny instanceof String)) {
            int coerceAtLeast = RangesKt.coerceAtLeast(startIndex, 0);
            int lastIndex = StringsKt.getLastIndex($this$indexOfAny);
            if (coerceAtLeast <= lastIndex) {
                while (true) {
                    char charAtIndex = $this$indexOfAny.charAt(coerceAtLeast);
                    char[] $this$any$iv = chars;
                    int length = $this$any$iv.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            z = false;
                            break;
                        } else if (CharsKt.equals($this$any$iv[i], charAtIndex, ignoreCase)) {
                            z = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (!z) {
                        if (coerceAtLeast == lastIndex) {
                            break;
                        }
                        coerceAtLeast++;
                    } else {
                        return coerceAtLeast;
                    }
                }
            }
            return -1;
        }
        return ((String) $this$indexOfAny).indexOf(ArraysKt.single(chars), startIndex);
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, char[] cArr, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, cArr, i, z);
    }

    public static final int lastIndexOfAny(@NotNull CharSequence $this$lastIndexOfAny, @NotNull char[] chars, int startIndex, boolean ignoreCase) {
        boolean z;
        Intrinsics.checkParameterIsNotNull($this$lastIndexOfAny, "$this$lastIndexOfAny");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        if (ignoreCase || chars.length != 1 || !($this$lastIndexOfAny instanceof String)) {
            for (int coerceAtMost = RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$lastIndexOfAny)); coerceAtMost >= 0; coerceAtMost--) {
                char charAtIndex = $this$lastIndexOfAny.charAt(coerceAtMost);
                char[] $this$any$iv = chars;
                int length = $this$any$iv.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = false;
                        break;
                    } else if (CharsKt.equals($this$any$iv[i], charAtIndex, ignoreCase)) {
                        z = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (z) {
                    return coerceAtMost;
                }
            }
            return -1;
        }
        return ((String) $this$lastIndexOfAny).lastIndexOf(ArraysKt.single(chars), startIndex);
    }

    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(CharSequence charSequence, CharSequence charSequence2, int i, int i2, boolean z, boolean z2, int i3, Object obj) {
        return indexOf$StringsKt__StringsKt(charSequence, charSequence2, i, i2, z, (i3 & 16) != 0 ? false : z2);
    }

    private static final int indexOf$StringsKt__StringsKt(@NotNull CharSequence $this$indexOf, CharSequence other, int startIndex, int endIndex, boolean ignoreCase, boolean last) {
        IntProgression indices;
        if (!last) {
            indices = new IntRange(RangesKt.coerceAtLeast(startIndex, 0), RangesKt.coerceAtMost(endIndex, $this$indexOf.length()));
        } else {
            indices = RangesKt.downTo(RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$indexOf)), RangesKt.coerceAtLeast(endIndex, 0));
        }
        if (!($this$indexOf instanceof String) || !(other instanceof String)) {
            int first = indices.getFirst();
            int last2 = indices.getLast();
            int step = indices.getStep();
            if (step < 0 ? first >= last2 : first <= last2) {
                while (true) {
                    if (!StringsKt.regionMatchesImpl(other, 0, $this$indexOf, first, other.length(), ignoreCase)) {
                        if (first == last2) {
                            break;
                        }
                        first += step;
                    } else {
                        return first;
                    }
                }
            }
        } else {
            int first2 = indices.getFirst();
            int last3 = indices.getLast();
            int step2 = indices.getStep();
            if (step2 < 0 ? first2 >= last3 : first2 <= last3) {
                while (!StringsKt.regionMatches((String) other, 0, (String) $this$indexOf, first2, other.length(), ignoreCase)) {
                    if (first2 != last3) {
                        first2 += step2;
                    }
                }
                return first2;
            }
        }
        return -1;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v1, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v3, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v4, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v7, resolved type: java.lang.String} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.CharSequence, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.CharSequence, java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.lastIndexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final kotlin.Pair<java.lang.Integer, java.lang.String> findAnyOf$StringsKt__StringsKt(@org.jetbrains.annotations.NotNull java.lang.CharSequence r20, java.util.Collection<java.lang.String> r21, int r22, boolean r23, boolean r24) {
        /*
            if (r23 != 0) goto L_0x0037
            int r1 = r21.size()
            r4 = 1
            if (r1 != r4) goto L_0x0037
            java.lang.Iterable r21 = (java.lang.Iterable) r21
            java.lang.Object r2 = kotlin.collections.CollectionsKt.single(r21)
            java.lang.String r2 = (java.lang.String) r2
            if (r24 != 0) goto L_0x0022
            r4 = 0
            r5 = 4
            r6 = 0
            r1 = r20
            r3 = r22
            int r15 = kotlin.text.StringsKt.indexOf$default(r1, r2, r3, r4, r5, r6)
        L_0x001e:
            if (r15 >= 0) goto L_0x002e
            r1 = 0
        L_0x0021:
            return r1
        L_0x0022:
            r4 = 0
            r5 = 4
            r6 = 0
            r1 = r20
            r3 = r22
            int r15 = kotlin.text.StringsKt.lastIndexOf$default(r1, r2, r3, r4, r5, r6)
            goto L_0x001e
        L_0x002e:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r15)
            kotlin.Pair r1 = kotlin.TuplesKt.to(r1, r2)
            goto L_0x0021
        L_0x0037:
            if (r24 != 0) goto L_0x009c
            r1 = 0
            r0 = r22
            int r4 = kotlin.ranges.RangesKt.coerceAtLeast(r0, r1)
            kotlin.ranges.IntRange r1 = new kotlin.ranges.IntRange
            int r5 = r20.length()
            r1.<init>(r4, r5)
            kotlin.ranges.IntProgression r1 = (kotlin.ranges.IntProgression) r1
            r16 = r1
        L_0x004d:
            r0 = r20
            boolean r1 = r0 instanceof java.lang.String
            if (r1 == 0) goto L_0x00b8
            int r6 = r16.getFirst()
            int r1 = r16.getLast()
            int r9 = r16.getStep()
            if (r9 < 0) goto L_0x00ac
            if (r6 > r1) goto L_0x00ae
        L_0x0063:
            r13 = r21
            java.lang.Iterable r13 = (java.lang.Iterable) r13
            r12 = 0
            java.util.Iterator r18 = r13.iterator()
        L_0x006c:
            boolean r4 = r18.hasNext()
            if (r4 == 0) goto L_0x00b1
            java.lang.Object r14 = r18.next()
            r3 = r14
            java.lang.String r3 = (java.lang.String) r3
            r10 = 0
            r4 = 0
            r5 = r20
            java.lang.String r5 = (java.lang.String) r5
            int r7 = r3.length()
            r8 = r23
            boolean r4 = kotlin.text.StringsKt.regionMatches(r3, r4, r5, r6, r7, r8)
            if (r4 == 0) goto L_0x006c
            r17 = r14
        L_0x008d:
            java.lang.String r17 = (java.lang.String) r17
            if (r17 == 0) goto L_0x00b4
            java.lang.Integer r1 = java.lang.Integer.valueOf(r6)
            r0 = r17
            kotlin.Pair r1 = kotlin.TuplesKt.to(r1, r0)
            goto L_0x0021
        L_0x009c:
            int r1 = kotlin.text.StringsKt.getLastIndex(r20)
            r0 = r22
            int r1 = kotlin.ranges.RangesKt.coerceAtMost(r0, r1)
            r4 = 0
            kotlin.ranges.IntProgression r16 = kotlin.ranges.RangesKt.downTo(r1, r4)
            goto L_0x004d
        L_0x00ac:
            if (r6 >= r1) goto L_0x0063
        L_0x00ae:
            r1 = 0
            goto L_0x0021
        L_0x00b1:
            r17 = 0
            goto L_0x008d
        L_0x00b4:
            if (r6 == r1) goto L_0x00ae
            int r6 = r6 + r9
            goto L_0x0063
        L_0x00b8:
            int r7 = r16.getFirst()
            int r1 = r16.getLast()
            int r18 = r16.getStep()
            if (r18 < 0) goto L_0x0103
            if (r7 > r1) goto L_0x00ae
        L_0x00c8:
            r13 = r21
            java.lang.Iterable r13 = (java.lang.Iterable) r13
            r12 = 0
            java.util.Iterator r19 = r13.iterator()
        L_0x00d1:
            boolean r4 = r19.hasNext()
            if (r4 == 0) goto L_0x0106
            java.lang.Object r14 = r19.next()
            r3 = r14
            java.lang.String r3 = (java.lang.String) r3
            r11 = 0
            r4 = r3
            java.lang.CharSequence r4 = (java.lang.CharSequence) r4
            r5 = 0
            int r8 = r3.length()
            r6 = r20
            r9 = r23
            boolean r4 = kotlin.text.StringsKt.regionMatchesImpl(r4, r5, r6, r7, r8, r9)
            if (r4 == 0) goto L_0x00d1
            r17 = r14
        L_0x00f3:
            java.lang.String r17 = (java.lang.String) r17
            if (r17 == 0) goto L_0x0109
            java.lang.Integer r1 = java.lang.Integer.valueOf(r7)
            r0 = r17
            kotlin.Pair r1 = kotlin.TuplesKt.to(r1, r0)
            goto L_0x0021
        L_0x0103:
            if (r7 < r1) goto L_0x00ae
            goto L_0x00c8
        L_0x0106:
            r17 = 0
            goto L_0x00f3
        L_0x0109:
            if (r7 == r1) goto L_0x00ae
            int r7 = r7 + r18
            goto L_0x00c8
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(java.lang.CharSequence, java.util.Collection, int, boolean, boolean):kotlin.Pair");
    }

    public static /* synthetic */ Pair findAnyOf$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.findAnyOf(charSequence, collection, i, z);
    }

    @Nullable
    public static final Pair<Integer, String> findAnyOf(@NotNull CharSequence $this$findAnyOf, @NotNull Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$findAnyOf, "$this$findAnyOf");
        Intrinsics.checkParameterIsNotNull(strings, "strings");
        return findAnyOf$StringsKt__StringsKt($this$findAnyOf, strings, startIndex, ignoreCase, false);
    }

    public static /* synthetic */ Pair findLastAnyOf$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.findLastAnyOf(charSequence, collection, i, z);
    }

    @Nullable
    public static final Pair<Integer, String> findLastAnyOf(@NotNull CharSequence $this$findLastAnyOf, @NotNull Collection<String> strings, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$findLastAnyOf, "$this$findLastAnyOf");
        Intrinsics.checkParameterIsNotNull(strings, "strings");
        return findAnyOf$StringsKt__StringsKt($this$findLastAnyOf, strings, startIndex, ignoreCase, true);
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOfAny(charSequence, collection, i, z);
    }

    public static final int indexOfAny(@NotNull CharSequence $this$indexOfAny, @NotNull Collection<String> strings, int startIndex, boolean ignoreCase) {
        Integer first;
        Intrinsics.checkParameterIsNotNull($this$indexOfAny, "$this$indexOfAny");
        Intrinsics.checkParameterIsNotNull(strings, "strings");
        Pair<Integer, String> findAnyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt($this$indexOfAny, strings, startIndex, ignoreCase, false);
        if (findAnyOf$StringsKt__StringsKt == null || (first = findAnyOf$StringsKt__StringsKt.getFirst()) == null) {
            return -1;
        }
        return first.intValue();
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, collection, i, z);
    }

    public static final int lastIndexOfAny(@NotNull CharSequence $this$lastIndexOfAny, @NotNull Collection<String> strings, int startIndex, boolean ignoreCase) {
        Integer first;
        Intrinsics.checkParameterIsNotNull($this$lastIndexOfAny, "$this$lastIndexOfAny");
        Intrinsics.checkParameterIsNotNull(strings, "strings");
        Pair<Integer, String> findAnyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt($this$lastIndexOfAny, strings, startIndex, ignoreCase, true);
        if (findAnyOf$StringsKt__StringsKt == null || (first = findAnyOf$StringsKt__StringsKt.getFirst()) == null) {
            return -1;
        }
        return first.intValue();
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, char c, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOf(charSequence, c, i, z);
    }

    public static final int indexOf(@NotNull CharSequence $this$indexOf, char c, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$indexOf, "$this$indexOf");
        if (!ignoreCase && ($this$indexOf instanceof String)) {
            return ((String) $this$indexOf).indexOf(c, startIndex);
        }
        return StringsKt.indexOfAny($this$indexOf, new char[]{c}, startIndex, ignoreCase);
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOf(charSequence, str, i, z);
    }

    public static final int indexOf(@NotNull CharSequence $this$indexOf, @NotNull String string, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$indexOf, "$this$indexOf");
        Intrinsics.checkParameterIsNotNull(string, "string");
        if (!ignoreCase && ($this$indexOf instanceof String)) {
            return ((String) $this$indexOf).indexOf(string, startIndex);
        }
        return indexOf$StringsKt__StringsKt$default($this$indexOf, string, startIndex, $this$indexOf.length(), ignoreCase, false, 16, null);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, char c, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOf(charSequence, c, i, z);
    }

    public static final int lastIndexOf(@NotNull CharSequence $this$lastIndexOf, char c, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$lastIndexOf, "$this$lastIndexOf");
        if (!ignoreCase && ($this$lastIndexOf instanceof String)) {
            return ((String) $this$lastIndexOf).lastIndexOf(c, startIndex);
        }
        return StringsKt.lastIndexOfAny($this$lastIndexOf, new char[]{c}, startIndex, ignoreCase);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOf(charSequence, str, i, z);
    }

    public static final int lastIndexOf(@NotNull CharSequence $this$lastIndexOf, @NotNull String string, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$lastIndexOf, "$this$lastIndexOf");
        Intrinsics.checkParameterIsNotNull(string, "string");
        if (!ignoreCase && ($this$lastIndexOf instanceof String)) {
            return ((String) $this$lastIndexOf).lastIndexOf(string, startIndex);
        }
        return indexOf$StringsKt__StringsKt($this$lastIndexOf, string, startIndex, 0, ignoreCase, true);
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.contains(charSequence, charSequence2, z);
    }

    public static final boolean contains(@NotNull CharSequence $this$contains, @NotNull CharSequence other, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (other instanceof String) {
            return StringsKt.indexOf$default($this$contains, (String) other, 0, ignoreCase, 2, null) >= 0;
        }
        return indexOf$StringsKt__StringsKt$default($this$contains, other, 0, $this$contains.length(), ignoreCase, false, 16, null) >= 0;
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, char c, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.contains(charSequence, c, z);
    }

    public static final boolean contains(@NotNull CharSequence $this$contains, char c, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return StringsKt.indexOf$default($this$contains, c, 0, ignoreCase, 2, null) >= 0;
    }

    @InlineOnly
    private static final boolean contains(@NotNull CharSequence $this$contains, Regex regex) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return regex.containsMatchIn($this$contains);
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, char[] cArr, int i, boolean z, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        if ((i3 & 8) != 0) {
            i2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, cArr, i, z, i2);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(@NotNull CharSequence $this$rangesDelimitedBy, char[] delimiters, int startIndex, boolean ignoreCase, int limit) {
        if (limit >= 0) {
            return new DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, new StringsKt__StringsKt$rangesDelimitedBy$2(delimiters, ignoreCase));
        }
        throw new IllegalArgumentException(("Limit must be non-negative, but was " + limit + '.').toString());
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, String[] strArr, int i, boolean z, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        if ((i3 & 8) != 0) {
            i2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, strArr, i, z, i2);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(@NotNull CharSequence $this$rangesDelimitedBy, String[] delimiters, int startIndex, boolean ignoreCase, int limit) {
        if (limit >= 0) {
            return new DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, new StringsKt__StringsKt$rangesDelimitedBy$4(ArraysKt.asList(delimiters), ignoreCase));
        }
        throw new IllegalArgumentException(("Limit must be non-negative, but was " + limit + '.').toString());
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, String[] strArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.splitToSequence(charSequence, strArr, z, i);
    }

    @NotNull
    public static final Sequence<String> splitToSequence(@NotNull CharSequence $this$splitToSequence, @NotNull String[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkParameterIsNotNull($this$splitToSequence, "$this$splitToSequence");
        Intrinsics.checkParameterIsNotNull(delimiters, "delimiters");
        return SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, (Object) null), new StringsKt__StringsKt$splitToSequence$1($this$splitToSequence));
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, String[] strArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.split(charSequence, strArr, z, i);
    }

    @NotNull
    public static final List<String> split(@NotNull CharSequence $this$split, @NotNull String[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkParameterIsNotNull($this$split, "$this$split");
        Intrinsics.checkParameterIsNotNull(delimiters, "delimiters");
        if (delimiters.length == 1) {
            String delimiter = delimiters[0];
            if (!(delimiter.length() == 0)) {
                return split$StringsKt__StringsKt($this$split, delimiter, ignoreCase, limit);
            }
        }
        Iterable<IntRange> $this$map$iv = SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, (Object) null));
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (IntRange intRange : $this$map$iv) {
            destination$iv$iv.add(StringsKt.substring($this$split, intRange));
        }
        return (List) destination$iv$iv;
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, char[] cArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.splitToSequence(charSequence, cArr, z, i);
    }

    @NotNull
    public static final Sequence<String> splitToSequence(@NotNull CharSequence $this$splitToSequence, @NotNull char[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkParameterIsNotNull($this$splitToSequence, "$this$splitToSequence");
        Intrinsics.checkParameterIsNotNull(delimiters, "delimiters");
        return SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, (Object) null), new StringsKt__StringsKt$splitToSequence$2($this$splitToSequence));
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, char[] cArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.split(charSequence, cArr, z, i);
    }

    @NotNull
    public static final List<String> split(@NotNull CharSequence $this$split, @NotNull char[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkParameterIsNotNull($this$split, "$this$split");
        Intrinsics.checkParameterIsNotNull(delimiters, "delimiters");
        if (delimiters.length == 1) {
            return split$StringsKt__StringsKt($this$split, String.valueOf(delimiters[0]), ignoreCase, limit);
        }
        Iterable<IntRange> $this$map$iv = SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, (Object) null));
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (IntRange intRange : $this$map$iv) {
            destination$iv$iv.add(StringsKt.substring($this$split, intRange));
        }
        return (List) destination$iv$iv;
    }

    private static final List<String> split$StringsKt__StringsKt(@NotNull CharSequence $this$split, String delimiter, boolean ignoreCase, int limit) {
        int i;
        boolean isLimited = true;
        if (!(limit >= 0)) {
            throw new IllegalArgumentException(("Limit must be non-negative, but was " + limit + '.').toString());
        }
        int currentOffset = 0;
        int nextIndex = StringsKt.indexOf($this$split, delimiter, 0, ignoreCase);
        if (nextIndex == -1 || limit == 1) {
            return CollectionsKt.listOf($this$split.toString());
        }
        if (limit <= 0) {
            isLimited = false;
        }
        if (isLimited) {
            i = RangesKt.coerceAtMost(limit, 10);
        } else {
            i = 10;
        }
        ArrayList result = new ArrayList(i);
        do {
            result.add($this$split.subSequence(currentOffset, nextIndex).toString());
            currentOffset = nextIndex + delimiter.length();
            if (isLimited && result.size() == limit - 1) {
                break;
            }
            nextIndex = StringsKt.indexOf($this$split, delimiter, currentOffset, ignoreCase);
        } while (nextIndex != -1);
        result.add($this$split.subSequence(currentOffset, $this$split.length()).toString());
        return result;
    }

    @InlineOnly
    private static final List<String> split(@NotNull CharSequence $this$split, Regex regex, int limit) {
        return regex.split($this$split, limit);
    }

    static /* synthetic */ List split$default(CharSequence $this$split, Regex regex, int limit, int i, Object obj) {
        if ((i & 2) != 0) {
            limit = 0;
        }
        return regex.split($this$split, limit);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.splitToSequence$default(java.lang.CharSequence, java.lang.String[], boolean, int, int, java.lang.Object):kotlin.sequences.Sequence
     arg types: [java.lang.CharSequence, java.lang.String[], int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.splitToSequence$default(java.lang.CharSequence, char[], boolean, int, int, java.lang.Object):kotlin.sequences.Sequence
      kotlin.text.StringsKt__StringsKt.splitToSequence$default(java.lang.CharSequence, java.lang.String[], boolean, int, int, java.lang.Object):kotlin.sequences.Sequence */
    @NotNull
    public static final Sequence<String> lineSequence(@NotNull CharSequence $this$lineSequence) {
        Intrinsics.checkParameterIsNotNull($this$lineSequence, "$this$lineSequence");
        return StringsKt.splitToSequence$default($this$lineSequence, new String[]{"\r\n", "\n", "\r"}, false, 0, 6, (Object) null);
    }

    @NotNull
    public static final List<String> lines(@NotNull CharSequence $this$lines) {
        Intrinsics.checkParameterIsNotNull($this$lines, "$this$lines");
        return SequencesKt.toList(StringsKt.lineSequence($this$lines));
    }
}
