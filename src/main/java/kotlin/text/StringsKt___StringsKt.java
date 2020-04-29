package kotlin.text;

import dji.pilot.fpv.util.DJIFlurryReport;
import dji.publics.LogReport.base.Fields;
import dji.thirdparty.sanselan.formats.tiff.constants.GPSTagConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.Grouping;
import kotlin.collections.IndexedValue;
import kotlin.collections.IndexingIterable;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.collections.SlidingWindowKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Ü\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u001f\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\u000f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a!\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\u0010\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b*\u00020\u0002\u001a\u0010\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\n*\u00020\u0002\u001aE\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\b\u001a3\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u00020\u00050\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\b\u001aM\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\b\u001aN\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u00020\u00050\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\b¢\u0006\u0002\u0010\u0018\u001ah\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\b¢\u0006\u0002\u0010\u0019\u001a`\u0010\u001a\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\b¢\u0006\u0002\u0010\u0018\u001a3\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\b\u001aN\u0010\u001d\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\u000e\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u00020\u0005\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\b¢\u0006\u0002\u0010\u0018\u001a\u001a\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001f*\u00020\u00022\u0006\u0010!\u001a\u00020\"H\u0007\u001a4\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\u001a\u0010$\u001a\b\u0012\u0004\u0012\u00020 0\n*\u00020\u00022\u0006\u0010!\u001a\u00020\"H\u0007\u001a4\u0010$\u001a\b\u0012\u0004\u0012\u0002H#0\n\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\r\u0010%\u001a\u00020\"*\u00020\u0002H\b\u001a!\u0010%\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010&\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010(\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010(\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a!\u0010)\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a!\u0010)\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a!\u0010*\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a!\u0010*\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a)\u0010+\u001a\u00020\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u0004H\b\u001a\u001c\u0010.\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"H\b¢\u0006\u0002\u0010/\u001a!\u00100\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a!\u00100\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a6\u00101\u001a\u00020\u0002*\u00020\u00022'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000102H\b\u001a6\u00101\u001a\u00020 *\u00020 2'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000102H\b\u001aQ\u00105\u001a\u0002H6\"\f\b\u0000\u00106*\u000607j\u0002`8*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000102H\b¢\u0006\u0002\u00109\u001a!\u0010:\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a!\u0010:\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a<\u0010;\u001a\u0002H6\"\f\b\u0000\u00106*\u000607j\u0002`8*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b¢\u0006\u0002\u0010<\u001a<\u0010=\u001a\u0002H6\"\f\b\u0000\u00106*\u000607j\u0002`8*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b¢\u0006\u0002\u0010<\u001a(\u0010>\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b¢\u0006\u0002\u0010?\u001a(\u0010@\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b¢\u0006\u0002\u0010?\u001a\n\u0010A\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010A\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\u0011\u0010B\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a(\u0010B\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b¢\u0006\u0002\u0010?\u001a3\u0010D\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H#0\b0\u0004H\b\u001aL\u0010E\u001a\u0002H6\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H#0\b0\u0004H\b¢\u0006\u0002\u0010G\u001aI\u0010H\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2'\u0010J\u001a#\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\b¢\u0006\u0002\u0010L\u001a^\u0010M\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0NH\b¢\u0006\u0002\u0010O\u001aI\u0010P\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2'\u0010J\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u0002H#02H\b¢\u0006\u0002\u0010L\u001a^\u0010Q\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u0002H#0NH\b¢\u0006\u0002\u0010O\u001a!\u0010R\u001a\u00020S*\u00020\u00022\u0012\u0010T\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020S0\u0004H\b\u001a6\u0010U\u001a\u00020S*\u00020\u00022'\u0010T\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020S02H\b\u001a)\u0010V\u001a\u00020\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u0004H\b\u001a\u0019\u0010W\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"¢\u0006\u0002\u0010/\u001a9\u0010X\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001f0\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\b\u001aS\u0010X\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u001f0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\b\u001aR\u0010Y\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u001c\b\u0001\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050Z0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\b¢\u0006\u0002\u0010\u0018\u001al\u0010Y\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u001c\b\u0002\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0Z0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\b¢\u0006\u0002\u0010\u0019\u001a5\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\\\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0014\b\u0004\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\b\u001a!\u0010]\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a!\u0010^\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\n\u0010_\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010_\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\u0011\u0010`\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a(\u0010`\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b¢\u0006\u0002\u0010?\u001a-\u0010a\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\b\u001aB\u0010b\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\b\u001aH\u0010c\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\b\b\u0000\u0010#*\u00020d*\u00020\u00022)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#02H\b\u001aa\u0010e\u001a\u0002H6\"\b\b\u0000\u0010#*\u00020d\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#02H\b¢\u0006\u0002\u0010f\u001a[\u0010g\u001a\u0002H6\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\b¢\u0006\u0002\u0010f\u001a3\u0010h\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\b\b\u0000\u0010#*\u00020d*\u00020\u00022\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#0\u0004H\b\u001aL\u0010i\u001a\u0002H6\"\b\b\u0000\u0010#*\u00020d\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#0\u0004H\b¢\u0006\u0002\u0010G\u001aF\u0010j\u001a\u0002H6\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\b¢\u0006\u0002\u0010G\u001a\u0011\u0010k\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a8\u0010l\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010#*\b\u0012\u0004\u0012\u0002H#0m*\u00020\u00022\u0012\u0010n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\b¢\u0006\u0002\u0010?\u001a-\u0010o\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010p\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050qj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`r¢\u0006\u0002\u0010s\u001a\u0011\u0010t\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a8\u0010u\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010#*\b\u0012\u0004\u0012\u0002H#0m*\u00020\u00022\u0012\u0010n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\b¢\u0006\u0002\u0010?\u001a-\u0010v\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010p\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050qj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`r¢\u0006\u0002\u0010s\u001a\n\u0010w\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010w\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a0\u0010x\u001a\u0002Hy\"\b\b\u0000\u0010y*\u00020\u0002*\u0002Hy2\u0012\u0010T\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020S0\u0004H\b¢\u0006\u0002\u0010z\u001a-\u0010{\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a-\u0010{\u001a\u000e\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020 0\u0010*\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\r\u0010|\u001a\u00020\u0005*\u00020\u0002H\b\u001a\u0014\u0010|\u001a\u00020\u0005*\u00020\u00022\u0006\u0010|\u001a\u00020}H\u0007\u001a6\u0010~\u001a\u00020\u0005*\u00020\u00022'\u0010J\u001a#\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000502H\b\u001aK\u0010\u001a\u00020\u0005*\u00020\u00022<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050NH\b\u001a7\u0010\u0001\u001a\u00020\u0005*\u00020\u00022'\u0010J\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u000502H\b\u001aL\u0010\u0001\u001a\u00020\u0005*\u00020\u00022<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u00050NH\b\u001a\u000b\u0010\u0001\u001a\u00020\u0002*\u00020\u0002\u001a\u000e\u0010\u0001\u001a\u00020 *\u00020 H\b\u001a\u000b\u0010\u0001\u001a\u00020\u0005*\u00020\u0002\u001a\"\u0010\u0001\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\u0012\u0010\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a)\u0010\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b¢\u0006\u0002\u0010?\u001a\u001a\u0010\u0001\u001a\u00020\u0002*\u00020\u00022\r\u0010\u0001\u001a\b\u0012\u0004\u0012\u00020\"0\b\u001a\u0015\u0010\u0001\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0001\u001a\u00030\u0001\u001a\u001d\u0010\u0001\u001a\u00020 *\u00020 2\r\u0010\u0001\u001a\b\u0012\u0004\u0012\u00020\"0\bH\b\u001a\u0015\u0010\u0001\u001a\u00020 *\u00020 2\b\u0010\u0001\u001a\u00030\u0001\u001a\"\u0010\u0001\u001a\u00020\"*\u00020\u00022\u0012\u0010n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\"0\u0004H\b\u001a$\u0010\u0001\u001a\u00030\u0001*\u00020\u00022\u0013\u0010n\u001a\u000f\u0012\u0004\u0012\u00020\u0005\u0012\u0005\u0012\u00030\u00010\u0004H\b\u001a\u0013\u0010\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u0001\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u0001\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\"\u0010\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\"\u0010\u0001\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\"\u0010\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a\"\u0010\u0001\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\b\u001a+\u0010\u0001\u001a\u0002H6\"\u0010\b\u0000\u00106*\n\u0012\u0006\b\u0000\u0012\u00020\u00050F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H6¢\u0006\u0003\u0010\u0001\u001a\u001d\u0010\u0001\u001a\u0014\u0012\u0004\u0012\u00020\u00050\u0001j\t\u0012\u0004\u0012\u00020\u0005`\u0001*\u00020\u0002\u001a\u0011\u0010\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050\u001f*\u00020\u0002\u001a\u0011\u0010\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050Z*\u00020\u0002\u001a\u0012\u0010\u0001\u001a\t\u0012\u0004\u0012\u00020\u00050\u0001*\u00020\u0002\u001a1\u0010\u0001\u001a\b\u0012\u0004\u0012\u00020 0\u001f*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u0001\u001a\u00020\"2\t\b\u0002\u0010\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u0001\u001a\u00020\"2\t\b\u0002\u0010\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a1\u0010\u0001\u001a\b\u0012\u0004\u0012\u00020 0\n*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u0001\u001a\u00020\"2\t\b\u0002\u0010\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\n\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010\u0001\u001a\u00020\"2\t\b\u0002\u0010\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\u0018\u0010\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0004\u0012\u00020\u00050\u00010\b*\u00020\u0002\u001a)\u0010\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001f*\u00020\u00022\u0007\u0010\u0001\u001a\u00020\u0002H\u0004\u001a]\u0010\u0001\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u001f\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0007\u0010\u0001\u001a\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b( \u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(¡\u0001\u0012\u0004\u0012\u0002H\u000e02H\b\u001a\u001f\u0010¢\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001f*\u00020\u0002H\u0007\u001aT\u0010¢\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b( \u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(¡\u0001\u0012\u0004\u0012\u0002H#02H\b¨\u0006£\u0001"}, d2 = {"all", "", "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "associate", "", "K", GPSTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_INTEROPERABILITY, "transform", "Lkotlin/Pair;", "associateBy", "keySelector", "valueTransform", "associateByTo", "M", "", "destination", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "associateTo", "associateWith", "valueSelector", "associateWithTo", "chunked", "", "", "size", "", "R", "chunkedSequence", "count", "drop", "n", "dropLast", "dropLastWhile", "dropWhile", "elementAtOrElse", DJIFlurryReport.NativeExplore.V2_EXPLORE_SMALLBANNER_SUBKEY_INDEX, "defaultValue", "elementAtOrNull", "(Ljava/lang/CharSequence;I)Ljava/lang/Character;", "filter", "filterIndexed", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "filterIndexedTo", "C", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function2;)Ljava/lang/Appendable;", "filterNot", "filterNotTo", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Appendable;", "filterTo", "find", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Character;", "findLast", "first", "firstOrNull", "(Ljava/lang/CharSequence;)Ljava/lang/Character;", "flatMap", "flatMapTo", "", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "fold", "initial", "operation", "acc", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "foldIndexed", "Lkotlin/Function3;", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "foldRight", "foldRightIndexed", "forEach", "", "action", "forEachIndexed", "getOrElse", "getOrNull", "groupBy", "groupByTo", "", "groupingBy", "Lkotlin/collections/Grouping;", "indexOfFirst", "indexOfLast", "last", "lastOrNull", "map", "mapIndexed", "mapIndexedNotNull", "", "mapIndexedNotNullTo", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function2;)Ljava/util/Collection;", "mapIndexedTo", "mapNotNull", "mapNotNullTo", "mapTo", "max", "maxBy", "", "selector", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/CharSequence;Ljava/util/Comparator;)Ljava/lang/Character;", "min", "minBy", "minWith", "none", "onEach", "S", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/CharSequence;", "partition", "random", "Lkotlin/random/Random;", "reduce", "reduceIndexed", "reduceRight", "reduceRightIndexed", "reversed", "single", "singleOrNull", "slice", "indices", "Lkotlin/ranges/IntRange;", "sumBy", "sumByDouble", "", "take", "takeLast", "takeLastWhile", "takeWhile", "toCollection", "(Ljava/lang/CharSequence;Ljava/util/Collection;)Ljava/util/Collection;", "toHashSet", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "toList", "toMutableList", "toSet", "", "windowed", Fields.Dgo_update.STEP, "partialWindows", "windowedSequence", "withIndex", "Lkotlin/collections/IndexedValue;", "zip", "other", "a", "b", "zipWithNext", "kotlin-stdlib"}, k = 5, mv = {1, 1, 15}, xi = 1, xs = "kotlin/text/StringsKt")
/* compiled from: _Strings.kt */
class StringsKt___StringsKt extends StringsKt___StringsJvmKt {
    @InlineOnly
    private static final char elementAtOrElse(@NotNull CharSequence $this$elementAtOrElse, int index, Function1<? super Integer, Character> defaultValue) {
        return (index < 0 || index > StringsKt.getLastIndex($this$elementAtOrElse)) ? defaultValue.invoke(Integer.valueOf(index)).charValue() : $this$elementAtOrElse.charAt(index);
    }

    @InlineOnly
    private static final Character elementAtOrNull(@NotNull CharSequence $this$elementAtOrNull, int index) {
        return StringsKt.getOrNull($this$elementAtOrNull, index);
    }

    @InlineOnly
    private static final Character find(@NotNull CharSequence $this$find, Function1<? super Character, Boolean> predicate) {
        CharSequence $this$firstOrNull$iv = $this$find;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$firstOrNull$iv.length()) {
                return null;
            }
            char element$iv = $this$firstOrNull$iv.charAt(i2);
            if (predicate.invoke(Character.valueOf(element$iv)).booleanValue()) {
                return Character.valueOf(element$iv);
            }
            i = i2 + 1;
        }
    }

    @InlineOnly
    private static final Character findLast(@NotNull CharSequence $this$findLast, Function1<? super Character, Boolean> predicate) {
        CharSequence $this$lastOrNull$iv = $this$findLast;
        for (int length = $this$lastOrNull$iv.length() - 1; length >= 0; length--) {
            char element$iv = $this$lastOrNull$iv.charAt(length);
            if (predicate.invoke(Character.valueOf(element$iv)).booleanValue()) {
                return Character.valueOf(element$iv);
            }
        }
        return null;
    }

    public static final char first(@NotNull CharSequence $this$first) {
        boolean z;
        Intrinsics.checkParameterIsNotNull($this$first, "$this$first");
        if ($this$first.length() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return $this$first.charAt(0);
        }
        throw new NoSuchElementException("Char sequence is empty.");
    }

    public static final char first(@NotNull CharSequence $this$first, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$first, "$this$first");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < $this$first.length()) {
                char element = $this$first.charAt(i2);
                if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                    return element;
                }
                i = i2 + 1;
            } else {
                throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
            }
        }
    }

    @Nullable
    public static final Character firstOrNull(@NotNull CharSequence $this$firstOrNull) {
        Intrinsics.checkParameterIsNotNull($this$firstOrNull, "$this$firstOrNull");
        if ($this$firstOrNull.length() == 0) {
            return null;
        }
        return Character.valueOf($this$firstOrNull.charAt(0));
    }

    @Nullable
    public static final Character firstOrNull(@NotNull CharSequence $this$firstOrNull, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$firstOrNull, "$this$firstOrNull");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$firstOrNull.length()) {
                return null;
            }
            char element = $this$firstOrNull.charAt(i2);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                return Character.valueOf(element);
            }
            i = i2 + 1;
        }
    }

    @InlineOnly
    private static final char getOrElse(@NotNull CharSequence $this$getOrElse, int index, Function1<? super Integer, Character> defaultValue) {
        return (index < 0 || index > StringsKt.getLastIndex($this$getOrElse)) ? defaultValue.invoke(Integer.valueOf(index)).charValue() : $this$getOrElse.charAt(index);
    }

    @Nullable
    public static final Character getOrNull(@NotNull CharSequence $this$getOrNull, int index) {
        Intrinsics.checkParameterIsNotNull($this$getOrNull, "$this$getOrNull");
        if (index < 0 || index > StringsKt.getLastIndex($this$getOrNull)) {
            return null;
        }
        return Character.valueOf($this$getOrNull.charAt(index));
    }

    public static final int indexOfFirst(@NotNull CharSequence $this$indexOfFirst, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$indexOfFirst, "$this$indexOfFirst");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = $this$indexOfFirst.length();
        for (int i = 0; i < length; i++) {
            if (predicate.invoke(Character.valueOf($this$indexOfFirst.charAt(i))).booleanValue()) {
                return i;
            }
        }
        return -1;
    }

    public static final int indexOfLast(@NotNull CharSequence $this$indexOfLast, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$indexOfLast, "$this$indexOfLast");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int length = $this$indexOfLast.length() - 1; length >= 0; length--) {
            if (predicate.invoke(Character.valueOf($this$indexOfLast.charAt(length))).booleanValue()) {
                return length;
            }
        }
        return -1;
    }

    public static final char last(@NotNull CharSequence $this$last) {
        Intrinsics.checkParameterIsNotNull($this$last, "$this$last");
        if (!($this$last.length() == 0)) {
            return $this$last.charAt(StringsKt.getLastIndex($this$last));
        }
        throw new NoSuchElementException("Char sequence is empty.");
    }

    public static final char last(@NotNull CharSequence $this$last, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$last, "$this$last");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int length = $this$last.length() - 1; length >= 0; length--) {
            char element = $this$last.charAt(length);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                return element;
            }
        }
        throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
    }

    @Nullable
    public static final Character lastOrNull(@NotNull CharSequence $this$lastOrNull) {
        Intrinsics.checkParameterIsNotNull($this$lastOrNull, "$this$lastOrNull");
        if ($this$lastOrNull.length() == 0) {
            return null;
        }
        return Character.valueOf($this$lastOrNull.charAt($this$lastOrNull.length() - 1));
    }

    @Nullable
    public static final Character lastOrNull(@NotNull CharSequence $this$lastOrNull, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$lastOrNull, "$this$lastOrNull");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int length = $this$lastOrNull.length() - 1; length >= 0; length--) {
            char element = $this$lastOrNull.charAt(length);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                return Character.valueOf(element);
            }
        }
        return null;
    }

    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final char random(@NotNull CharSequence $this$random) {
        return StringsKt.random($this$random, Random.Default);
    }

    @SinceKotlin(version = "1.3")
    public static final char random(@NotNull CharSequence $this$random, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!($this$random.length() == 0)) {
            return $this$random.charAt(random.nextInt($this$random.length()));
        }
        throw new NoSuchElementException("Char sequence is empty.");
    }

    public static final char single(@NotNull CharSequence $this$single) {
        Intrinsics.checkParameterIsNotNull($this$single, "$this$single");
        switch ($this$single.length()) {
            case 0:
                throw new NoSuchElementException("Char sequence is empty.");
            case 1:
                return $this$single.charAt(0);
            default:
                throw new IllegalArgumentException("Char sequence has more than one element.");
        }
    }

    public static final char single(@NotNull CharSequence $this$single, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$single, "$this$single");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Character single = null;
        boolean found = false;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < $this$single.length()) {
                char element = $this$single.charAt(i2);
                if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                    if (found) {
                        throw new IllegalArgumentException("Char sequence contains more than one matching element.");
                    }
                    single = Character.valueOf(element);
                    found = true;
                }
                i = i2 + 1;
            } else if (!found) {
                throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
            } else if (single != null) {
                return single.charValue();
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Char");
            }
        }
    }

    @Nullable
    public static final Character singleOrNull(@NotNull CharSequence $this$singleOrNull) {
        Intrinsics.checkParameterIsNotNull($this$singleOrNull, "$this$singleOrNull");
        if ($this$singleOrNull.length() == 1) {
            return Character.valueOf($this$singleOrNull.charAt(0));
        }
        return null;
    }

    @Nullable
    public static final Character singleOrNull(@NotNull CharSequence $this$singleOrNull, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$singleOrNull, "$this$singleOrNull");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Character single = null;
        boolean found = false;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < $this$singleOrNull.length()) {
                char element = $this$singleOrNull.charAt(i2);
                if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                    if (found) {
                        return null;
                    }
                    single = Character.valueOf(element);
                    found = true;
                }
                i = i2 + 1;
            } else if (found) {
                return single;
            } else {
                return null;
            }
        }
    }

    @NotNull
    public static final CharSequence drop(@NotNull CharSequence $this$drop, int n) {
        Intrinsics.checkParameterIsNotNull($this$drop, "$this$drop");
        if (n >= 0) {
            return $this$drop.subSequence(RangesKt.coerceAtMost(n, $this$drop.length()), $this$drop.length());
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    @NotNull
    public static final String drop(@NotNull String $this$drop, int n) {
        Intrinsics.checkParameterIsNotNull($this$drop, "$this$drop");
        if (!(n >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        String substring = $this$drop.substring(RangesKt.coerceAtMost(n, $this$drop.length()));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }

    @NotNull
    public static final CharSequence dropLast(@NotNull CharSequence $this$dropLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$dropLast, "$this$dropLast");
        if (n >= 0) {
            return StringsKt.take($this$dropLast, RangesKt.coerceAtLeast($this$dropLast.length() - n, 0));
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    @NotNull
    public static final String dropLast(@NotNull String $this$dropLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$dropLast, "$this$dropLast");
        if (n >= 0) {
            return StringsKt.take($this$dropLast, RangesKt.coerceAtLeast($this$dropLast.length() - n, 0));
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    @NotNull
    public static final CharSequence dropLastWhile(@NotNull CharSequence $this$dropLastWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$dropLastWhile, "$this$dropLastWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int lastIndex = StringsKt.getLastIndex($this$dropLastWhile); lastIndex >= 0; lastIndex--) {
            if (!predicate.invoke(Character.valueOf($this$dropLastWhile.charAt(lastIndex))).booleanValue()) {
                return $this$dropLastWhile.subSequence(0, lastIndex + 1);
            }
        }
        return "";
    }

    @NotNull
    public static final String dropLastWhile(@NotNull String $this$dropLastWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$dropLastWhile, "$this$dropLastWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int lastIndex = StringsKt.getLastIndex($this$dropLastWhile); lastIndex >= 0; lastIndex--) {
            if (!predicate.invoke(Character.valueOf($this$dropLastWhile.charAt(lastIndex))).booleanValue()) {
                String substring = $this$dropLastWhile.substring(0, lastIndex + 1);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                return substring;
            }
        }
        return "";
    }

    @NotNull
    public static final CharSequence dropWhile(@NotNull CharSequence $this$dropWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$dropWhile, "$this$dropWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = $this$dropWhile.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf($this$dropWhile.charAt(i))).booleanValue()) {
                return $this$dropWhile.subSequence(i, $this$dropWhile.length());
            }
        }
        return "";
    }

    @NotNull
    public static final String dropWhile(@NotNull String $this$dropWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$dropWhile, "$this$dropWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = $this$dropWhile.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf($this$dropWhile.charAt(i))).booleanValue()) {
                String substring = $this$dropWhile.substring(i);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                return substring;
            }
        }
        return "";
    }

    @NotNull
    public static final CharSequence filter(@NotNull CharSequence $this$filter, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$filter, "$this$filter");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Appendable destination$iv = new StringBuilder();
        int length = $this$filter.length();
        for (int i = 0; i < length; i++) {
            char element$iv = $this$filter.charAt(i);
            if (predicate.invoke(Character.valueOf(element$iv)).booleanValue()) {
                destination$iv.append(element$iv);
            }
        }
        return (CharSequence) destination$iv;
    }

    @NotNull
    public static final String filter(@NotNull String $this$filter, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$filter, "$this$filter");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$filter;
        Appendable destination$iv = new StringBuilder();
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            char element$iv = charSequence.charAt(i);
            if (predicate.invoke(Character.valueOf(element$iv)).booleanValue()) {
                destination$iv.append(element$iv);
            }
        }
        String sb = ((StringBuilder) destination$iv).toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "filterTo(StringBuilder(), predicate).toString()");
        return sb;
    }

    @NotNull
    public static final CharSequence filterIndexed(@NotNull CharSequence $this$filterIndexed, @NotNull Function2<? super Integer, ? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$filterIndexed, "$this$filterIndexed");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Appendable destination$iv = new StringBuilder();
        CharSequence $this$forEachIndexed$iv$iv = $this$filterIndexed;
        int index$iv$iv = 0;
        int i = 0;
        while (i < $this$forEachIndexed$iv$iv.length()) {
            char item$iv$iv = $this$forEachIndexed$iv$iv.charAt(i);
            int index$iv$iv2 = index$iv$iv + 1;
            if (predicate.invoke(Integer.valueOf(index$iv$iv), Character.valueOf(item$iv$iv)).booleanValue()) {
                destination$iv.append(item$iv$iv);
            }
            i++;
            index$iv$iv = index$iv$iv2;
        }
        return (CharSequence) destination$iv;
    }

    @NotNull
    public static final String filterIndexed(@NotNull String $this$filterIndexed, @NotNull Function2<? super Integer, ? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$filterIndexed, "$this$filterIndexed");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence $this$forEachIndexed$iv$iv = $this$filterIndexed;
        Appendable destination$iv = new StringBuilder();
        int index$iv$iv = 0;
        int i = 0;
        while (i < $this$forEachIndexed$iv$iv.length()) {
            char item$iv$iv = $this$forEachIndexed$iv$iv.charAt(i);
            int index$iv$iv2 = index$iv$iv + 1;
            if (predicate.invoke(Integer.valueOf(index$iv$iv), Character.valueOf(item$iv$iv)).booleanValue()) {
                destination$iv.append(item$iv$iv);
            }
            i++;
            index$iv$iv = index$iv$iv2;
        }
        String sb = ((StringBuilder) destination$iv).toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "filterIndexedTo(StringBu…(), predicate).toString()");
        return sb;
    }

    @NotNull
    public static final <C extends Appendable> C filterIndexedTo(@NotNull CharSequence $this$filterIndexedTo, @NotNull C destination, @NotNull Function2<? super Integer, ? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$filterIndexedTo, "$this$filterIndexedTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence $this$forEachIndexed$iv = $this$filterIndexedTo;
        int index$iv = 0;
        int i = 0;
        while (i < $this$forEachIndexed$iv.length()) {
            char item$iv = $this$forEachIndexed$iv.charAt(i);
            int index$iv2 = index$iv + 1;
            if (predicate.invoke(Integer.valueOf(index$iv), Character.valueOf(item$iv)).booleanValue()) {
                destination.append(item$iv);
            }
            i++;
            index$iv = index$iv2;
        }
        return destination;
    }

    @NotNull
    public static final CharSequence filterNot(@NotNull CharSequence $this$filterNot, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$filterNot, "$this$filterNot");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Appendable destination$iv = new StringBuilder();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$filterNot.length()) {
                return (CharSequence) destination$iv;
            }
            char element$iv = $this$filterNot.charAt(i2);
            if (!predicate.invoke(Character.valueOf(element$iv)).booleanValue()) {
                destination$iv.append(element$iv);
            }
            i = i2 + 1;
        }
    }

    @NotNull
    public static final String filterNot(@NotNull String $this$filterNot, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$filterNot, "$this$filterNot");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        CharSequence charSequence = $this$filterNot;
        Appendable destination$iv = new StringBuilder();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < charSequence.length()) {
                char element$iv = charSequence.charAt(i2);
                if (!predicate.invoke(Character.valueOf(element$iv)).booleanValue()) {
                    destination$iv.append(element$iv);
                }
                i = i2 + 1;
            } else {
                String sb = ((StringBuilder) destination$iv).toString();
                Intrinsics.checkExpressionValueIsNotNull(sb, "filterNotTo(StringBuilder(), predicate).toString()");
                return sb;
            }
        }
    }

    @NotNull
    public static final <C extends Appendable> C filterNotTo(@NotNull CharSequence $this$filterNotTo, @NotNull C destination, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$filterNotTo, "$this$filterNotTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$filterNotTo.length()) {
                return destination;
            }
            char element = $this$filterNotTo.charAt(i2);
            if (!predicate.invoke(Character.valueOf(element)).booleanValue()) {
                destination.append(element);
            }
            i = i2 + 1;
        }
    }

    @NotNull
    public static final <C extends Appendable> C filterTo(@NotNull CharSequence $this$filterTo, @NotNull C destination, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$filterTo, "$this$filterTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = $this$filterTo.length();
        for (int i = 0; i < length; i++) {
            char element = $this$filterTo.charAt(i);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                destination.append(element);
            }
        }
        return destination;
    }

    @NotNull
    public static final CharSequence slice(@NotNull CharSequence $this$slice, @NotNull IntRange indices) {
        Intrinsics.checkParameterIsNotNull($this$slice, "$this$slice");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        if (indices.isEmpty()) {
            return "";
        }
        return StringsKt.subSequence($this$slice, indices);
    }

    @NotNull
    public static final String slice(@NotNull String $this$slice, @NotNull IntRange indices) {
        Intrinsics.checkParameterIsNotNull($this$slice, "$this$slice");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        if (indices.isEmpty()) {
            return "";
        }
        return StringsKt.substring($this$slice, indices);
    }

    @NotNull
    public static final CharSequence slice(@NotNull CharSequence $this$slice, @NotNull Iterable<Integer> indices) {
        Intrinsics.checkParameterIsNotNull($this$slice, "$this$slice");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        int size = CollectionsKt.collectionSizeOrDefault(indices, 10);
        if (size == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder(size);
        for (Integer num : indices) {
            result.append($this$slice.charAt(num.intValue()));
        }
        return result;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt___StringsKt.slice(java.lang.CharSequence, java.lang.Iterable<java.lang.Integer>):java.lang.CharSequence
     arg types: [java.lang.String, java.lang.Iterable<java.lang.Integer>]
     candidates:
      kotlin.text.StringsKt___StringsKt.slice(java.lang.CharSequence, kotlin.ranges.IntRange):java.lang.CharSequence
      kotlin.text.StringsKt___StringsKt.slice(java.lang.String, java.lang.Iterable<java.lang.Integer>):java.lang.String
      kotlin.text.StringsKt___StringsKt.slice(java.lang.String, kotlin.ranges.IntRange):java.lang.String
      kotlin.text.StringsKt___StringsKt.slice(java.lang.CharSequence, java.lang.Iterable<java.lang.Integer>):java.lang.CharSequence */
    @InlineOnly
    private static final String slice(@NotNull String $this$slice, Iterable<Integer> indices) {
        if ($this$slice != null) {
            return StringsKt.slice((CharSequence) $this$slice, indices).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    @NotNull
    public static final CharSequence take(@NotNull CharSequence $this$take, int n) {
        Intrinsics.checkParameterIsNotNull($this$take, "$this$take");
        if (n >= 0) {
            return $this$take.subSequence(0, RangesKt.coerceAtMost(n, $this$take.length()));
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    @NotNull
    public static final String take(@NotNull String $this$take, int n) {
        Intrinsics.checkParameterIsNotNull($this$take, "$this$take");
        if (!(n >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        String substring = $this$take.substring(0, RangesKt.coerceAtMost(n, $this$take.length()));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return substring;
    }

    @NotNull
    public static final CharSequence takeLast(@NotNull CharSequence $this$takeLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$takeLast, "$this$takeLast");
        if (!(n >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        int length = $this$takeLast.length();
        return $this$takeLast.subSequence(length - RangesKt.coerceAtMost(n, length), length);
    }

    @NotNull
    public static final String takeLast(@NotNull String $this$takeLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$takeLast, "$this$takeLast");
        if (!(n >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
        }
        int length = $this$takeLast.length();
        String substring = $this$takeLast.substring(length - RangesKt.coerceAtMost(n, length));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }

    @NotNull
    public static final CharSequence takeLastWhile(@NotNull CharSequence $this$takeLastWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$takeLastWhile, "$this$takeLastWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int lastIndex = StringsKt.getLastIndex($this$takeLastWhile); lastIndex >= 0; lastIndex--) {
            if (!predicate.invoke(Character.valueOf($this$takeLastWhile.charAt(lastIndex))).booleanValue()) {
                return $this$takeLastWhile.subSequence(lastIndex + 1, $this$takeLastWhile.length());
            }
        }
        return $this$takeLastWhile.subSequence(0, $this$takeLastWhile.length());
    }

    @NotNull
    public static final String takeLastWhile(@NotNull String $this$takeLastWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$takeLastWhile, "$this$takeLastWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int lastIndex = StringsKt.getLastIndex($this$takeLastWhile); lastIndex >= 0; lastIndex--) {
            if (!predicate.invoke(Character.valueOf($this$takeLastWhile.charAt(lastIndex))).booleanValue()) {
                String $this$takeLastWhile2 = $this$takeLastWhile.substring(lastIndex + 1);
                Intrinsics.checkExpressionValueIsNotNull($this$takeLastWhile2, "(this as java.lang.String).substring(startIndex)");
                return $this$takeLastWhile2;
            }
        }
        return $this$takeLastWhile;
    }

    @NotNull
    public static final CharSequence takeWhile(@NotNull CharSequence $this$takeWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$takeWhile, "$this$takeWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = $this$takeWhile.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf($this$takeWhile.charAt(i))).booleanValue()) {
                return $this$takeWhile.subSequence(0, i);
            }
        }
        return $this$takeWhile.subSequence(0, $this$takeWhile.length());
    }

    @NotNull
    public static final String takeWhile(@NotNull String $this$takeWhile, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$takeWhile, "$this$takeWhile");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = $this$takeWhile.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf($this$takeWhile.charAt(i))).booleanValue()) {
                String $this$takeWhile2 = $this$takeWhile.substring(0, i);
                Intrinsics.checkExpressionValueIsNotNull($this$takeWhile2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                return $this$takeWhile2;
            }
        }
        return $this$takeWhile;
    }

    @NotNull
    public static final CharSequence reversed(@NotNull CharSequence $this$reversed) {
        Intrinsics.checkParameterIsNotNull($this$reversed, "$this$reversed");
        StringBuilder reverse = new StringBuilder($this$reversed).reverse();
        Intrinsics.checkExpressionValueIsNotNull(reverse, "StringBuilder(this).reverse()");
        return reverse;
    }

    @InlineOnly
    private static final String reversed(@NotNull String $this$reversed) {
        if ($this$reversed != null) {
            return StringsKt.reversed((CharSequence) $this$reversed).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    @NotNull
    public static final <K, V> Map<K, V> associate(@NotNull CharSequence $this$associate, @NotNull Function1<? super Character, ? extends Pair<? extends K, ? extends V>> transform) {
        Intrinsics.checkParameterIsNotNull($this$associate, "$this$associate");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Map destination$iv = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associate.length()), 16));
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$associate.length()) {
                return destination$iv;
            }
            Pair pair = (Pair) transform.invoke(Character.valueOf($this$associate.charAt(i2)));
            destination$iv.put(pair.getFirst(), pair.getSecond());
            i = i2 + 1;
        }
    }

    @NotNull
    public static final <K> Map<K, Character> associateBy(@NotNull CharSequence $this$associateBy, @NotNull Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull($this$associateBy, "$this$associateBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Map destination$iv = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associateBy.length()), 16));
        for (int i = 0; i < $this$associateBy.length(); i++) {
            char element$iv = $this$associateBy.charAt(i);
            destination$iv.put(keySelector.invoke(Character.valueOf(element$iv)), Character.valueOf(element$iv));
        }
        return destination$iv;
    }

    @NotNull
    public static final <K, V> Map<K, V> associateBy(@NotNull CharSequence $this$associateBy, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        Intrinsics.checkParameterIsNotNull($this$associateBy, "$this$associateBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        Map destination$iv = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associateBy.length()), 16));
        for (int i = 0; i < $this$associateBy.length(); i++) {
            char element$iv = $this$associateBy.charAt(i);
            destination$iv.put(keySelector.invoke(Character.valueOf(element$iv)), valueTransform.invoke(Character.valueOf(element$iv)));
        }
        return destination$iv;
    }

    @NotNull
    public static final <K, M extends Map<? super K, ? super Character>> M associateByTo(@NotNull CharSequence $this$associateByTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull($this$associateByTo, "$this$associateByTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        for (int i = 0; i < $this$associateByTo.length(); i++) {
            char element = $this$associateByTo.charAt(i);
            destination.put(keySelector.invoke(Character.valueOf(element)), Character.valueOf(element));
        }
        return destination;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateByTo(@NotNull CharSequence $this$associateByTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        Intrinsics.checkParameterIsNotNull($this$associateByTo, "$this$associateByTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        for (int i = 0; i < $this$associateByTo.length(); i++) {
            char element = $this$associateByTo.charAt(i);
            destination.put(keySelector.invoke(Character.valueOf(element)), valueTransform.invoke(Character.valueOf(element)));
        }
        return destination;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateTo(@NotNull CharSequence $this$associateTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends Pair<? extends K, ? extends V>> transform) {
        Intrinsics.checkParameterIsNotNull($this$associateTo, "$this$associateTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$associateTo.length()) {
                return destination;
            }
            Pair pair = (Pair) transform.invoke(Character.valueOf($this$associateTo.charAt(i2)));
            destination.put(pair.getFirst(), pair.getSecond());
            i = i2 + 1;
        }
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final <V> Map<Character, V> associateWith(@NotNull CharSequence $this$associateWith, @NotNull Function1<? super Character, ? extends V> valueSelector) {
        Intrinsics.checkParameterIsNotNull($this$associateWith, "$this$associateWith");
        Intrinsics.checkParameterIsNotNull(valueSelector, "valueSelector");
        LinkedHashMap result = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associateWith.length()), 16));
        CharSequence $this$associateWithTo$iv = $this$associateWith;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$associateWithTo$iv.length()) {
                return result;
            }
            char element$iv = $this$associateWithTo$iv.charAt(i2);
            result.put(Character.valueOf(element$iv), valueSelector.invoke(Character.valueOf(element$iv)));
            i = i2 + 1;
        }
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final <V, M extends Map<? super Character, ? super V>> M associateWithTo(@NotNull CharSequence $this$associateWithTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends V> valueSelector) {
        Intrinsics.checkParameterIsNotNull($this$associateWithTo, "$this$associateWithTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(valueSelector, "valueSelector");
        for (int i = 0; i < $this$associateWithTo.length(); i++) {
            char element = $this$associateWithTo.charAt(i);
            destination.put(Character.valueOf(element), valueSelector.invoke(Character.valueOf(element)));
        }
        return destination;
    }

    @NotNull
    public static final <C extends Collection<? super Character>> C toCollection(@NotNull CharSequence $this$toCollection, @NotNull C destination) {
        Intrinsics.checkParameterIsNotNull($this$toCollection, "$this$toCollection");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        for (int i = 0; i < $this$toCollection.length(); i++) {
            destination.add(Character.valueOf($this$toCollection.charAt(i)));
        }
        return destination;
    }

    @NotNull
    public static final HashSet<Character> toHashSet(@NotNull CharSequence $this$toHashSet) {
        Intrinsics.checkParameterIsNotNull($this$toHashSet, "$this$toHashSet");
        return (HashSet) StringsKt.toCollection($this$toHashSet, new HashSet(MapsKt.mapCapacity($this$toHashSet.length())));
    }

    @NotNull
    public static final List<Character> toList(@NotNull CharSequence $this$toList) {
        Intrinsics.checkParameterIsNotNull($this$toList, "$this$toList");
        switch ($this$toList.length()) {
            case 0:
                return CollectionsKt.emptyList();
            case 1:
                return CollectionsKt.listOf(Character.valueOf($this$toList.charAt(0)));
            default:
                return StringsKt.toMutableList($this$toList);
        }
    }

    @NotNull
    public static final List<Character> toMutableList(@NotNull CharSequence $this$toMutableList) {
        Intrinsics.checkParameterIsNotNull($this$toMutableList, "$this$toMutableList");
        return (List) StringsKt.toCollection($this$toMutableList, new ArrayList($this$toMutableList.length()));
    }

    @NotNull
    public static final Set<Character> toSet(@NotNull CharSequence $this$toSet) {
        Intrinsics.checkParameterIsNotNull($this$toSet, "$this$toSet");
        switch ($this$toSet.length()) {
            case 0:
                return SetsKt.emptySet();
            case 1:
                return SetsKt.setOf(Character.valueOf($this$toSet.charAt(0)));
            default:
                return (Set) StringsKt.toCollection($this$toSet, new LinkedHashSet(MapsKt.mapCapacity($this$toSet.length())));
        }
    }

    @NotNull
    public static final <R> List<R> flatMap(@NotNull CharSequence $this$flatMap, @NotNull Function1<? super Character, ? extends Iterable<? extends R>> transform) {
        Intrinsics.checkParameterIsNotNull($this$flatMap, "$this$flatMap");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Collection destination$iv = new ArrayList();
        for (int i = 0; i < $this$flatMap.length(); i++) {
            CollectionsKt.addAll(destination$iv, (Iterable) transform.invoke(Character.valueOf($this$flatMap.charAt(i))));
        }
        return (List) destination$iv;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C flatMapTo(@NotNull CharSequence $this$flatMapTo, @NotNull C destination, @NotNull Function1<? super Character, ? extends Iterable<? extends R>> transform) {
        Intrinsics.checkParameterIsNotNull($this$flatMapTo, "$this$flatMapTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (int i = 0; i < $this$flatMapTo.length(); i++) {
            CollectionsKt.addAll(destination, (Iterable) transform.invoke(Character.valueOf($this$flatMapTo.charAt(i))));
        }
        return destination;
    }

    @NotNull
    public static final <K> Map<K, List<Character>> groupBy(@NotNull CharSequence $this$groupBy, @NotNull Function1<? super Character, ? extends K> keySelector) {
        ArrayList arrayList;
        Intrinsics.checkParameterIsNotNull($this$groupBy, "$this$groupBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Map linkedHashMap = new LinkedHashMap();
        for (int i = 0; i < $this$groupBy.length(); i++) {
            char element$iv = $this$groupBy.charAt(i);
            Object key$iv = keySelector.invoke(Character.valueOf(element$iv));
            Map $this$getOrPut$iv$iv = linkedHashMap;
            Object value$iv$iv = $this$getOrPut$iv$iv.get(key$iv);
            if (value$iv$iv == null) {
                ArrayList answer$iv$iv = new ArrayList();
                $this$getOrPut$iv$iv.put(key$iv, answer$iv$iv);
                arrayList = answer$iv$iv;
            } else {
                arrayList = value$iv$iv;
            }
            ((List) arrayList).add(Character.valueOf(element$iv));
        }
        return linkedHashMap;
    }

    @NotNull
    public static final <K, V> Map<K, List<V>> groupBy(@NotNull CharSequence $this$groupBy, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        ArrayList arrayList;
        Intrinsics.checkParameterIsNotNull($this$groupBy, "$this$groupBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        Map linkedHashMap = new LinkedHashMap();
        for (int i = 0; i < $this$groupBy.length(); i++) {
            char element$iv = $this$groupBy.charAt(i);
            Object key$iv = keySelector.invoke(Character.valueOf(element$iv));
            Map $this$getOrPut$iv$iv = linkedHashMap;
            Object value$iv$iv = $this$getOrPut$iv$iv.get(key$iv);
            if (value$iv$iv == null) {
                ArrayList answer$iv$iv = new ArrayList();
                $this$getOrPut$iv$iv.put(key$iv, answer$iv$iv);
                arrayList = answer$iv$iv;
            } else {
                arrayList = value$iv$iv;
            }
            ((List) arrayList).add(valueTransform.invoke(Character.valueOf(element$iv)));
        }
        return linkedHashMap;
    }

    @NotNull
    public static final <K, M extends Map<? super K, List<Character>>> M groupByTo(@NotNull CharSequence $this$groupByTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector) {
        ArrayList arrayList;
        Intrinsics.checkParameterIsNotNull($this$groupByTo, "$this$groupByTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        for (int i = 0; i < $this$groupByTo.length(); i++) {
            char element = $this$groupByTo.charAt(i);
            Object key = keySelector.invoke(Character.valueOf(element));
            Map $this$getOrPut$iv = destination;
            Object value$iv = $this$getOrPut$iv.get(key);
            if (value$iv == null) {
                ArrayList answer$iv = new ArrayList();
                $this$getOrPut$iv.put(key, answer$iv);
                arrayList = answer$iv;
            } else {
                arrayList = value$iv;
            }
            ((List) arrayList).add(Character.valueOf(element));
        }
        return destination;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, List<V>>> M groupByTo(@NotNull CharSequence $this$groupByTo, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        ArrayList arrayList;
        Intrinsics.checkParameterIsNotNull($this$groupByTo, "$this$groupByTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        for (int i = 0; i < $this$groupByTo.length(); i++) {
            char element = $this$groupByTo.charAt(i);
            Object key = keySelector.invoke(Character.valueOf(element));
            Map $this$getOrPut$iv = destination;
            Object value$iv = $this$getOrPut$iv.get(key);
            if (value$iv == null) {
                ArrayList answer$iv = new ArrayList();
                $this$getOrPut$iv.put(key, answer$iv);
                arrayList = answer$iv;
            } else {
                arrayList = value$iv;
            }
            ((List) arrayList).add(valueTransform.invoke(Character.valueOf(element)));
        }
        return destination;
    }

    @NotNull
    @SinceKotlin(version = "1.1")
    public static final <K> Grouping<Character, K> groupingBy(@NotNull CharSequence $this$groupingBy, @NotNull Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull($this$groupingBy, "$this$groupingBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        return new StringsKt___StringsKt$groupingBy$1($this$groupingBy, keySelector);
    }

    @NotNull
    public static final <R> List<R> map(@NotNull CharSequence $this$map, @NotNull Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$map, "$this$map");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Collection destination$iv = new ArrayList($this$map.length());
        for (int i = 0; i < $this$map.length(); i++) {
            destination$iv.add(transform.invoke(Character.valueOf($this$map.charAt(i))));
        }
        return (List) destination$iv;
    }

    @NotNull
    public static final <R> List<R> mapIndexed(@NotNull CharSequence $this$mapIndexed, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$mapIndexed, "$this$mapIndexed");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Collection destination$iv = new ArrayList($this$mapIndexed.length());
        int index$iv = 0;
        for (int i = 0; i < $this$mapIndexed.length(); i++) {
            char item$iv = $this$mapIndexed.charAt(i);
            Integer valueOf = Integer.valueOf(index$iv);
            index$iv++;
            destination$iv.add(transform.invoke(valueOf, Character.valueOf(item$iv)));
        }
        return (List) destination$iv;
    }

    @NotNull
    public static final <R> List<R> mapIndexedNotNull(@NotNull CharSequence $this$mapIndexedNotNull, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$mapIndexedNotNull, "$this$mapIndexedNotNull");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Collection destination$iv = new ArrayList();
        CharSequence $this$forEachIndexed$iv$iv = $this$mapIndexedNotNull;
        int index$iv$iv = 0;
        int i = 0;
        while (i < $this$forEachIndexed$iv$iv.length()) {
            int index$iv$iv2 = index$iv$iv + 1;
            Function2<? super Integer, ? super Character, ? extends R> function2 = transform;
            Object it$iv = function2.invoke(Integer.valueOf(index$iv$iv), Character.valueOf($this$forEachIndexed$iv$iv.charAt(i)));
            if (it$iv != null) {
                destination$iv.add(it$iv);
            }
            i++;
            index$iv$iv = index$iv$iv2;
        }
        return (List) destination$iv;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedNotNullTo(@NotNull CharSequence $this$mapIndexedNotNullTo, @NotNull C destination, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$mapIndexedNotNullTo, "$this$mapIndexedNotNullTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence $this$forEachIndexed$iv = $this$mapIndexedNotNullTo;
        int index$iv = 0;
        int i = 0;
        while (i < $this$forEachIndexed$iv.length()) {
            int index$iv2 = index$iv + 1;
            Object it2 = transform.invoke(Integer.valueOf(index$iv), Character.valueOf($this$forEachIndexed$iv.charAt(i)));
            if (it2 != null) {
                destination.add(it2);
            }
            i++;
            index$iv = index$iv2;
        }
        return destination;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedTo(@NotNull CharSequence $this$mapIndexedTo, @NotNull C destination, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$mapIndexedTo, "$this$mapIndexedTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int index = 0;
        for (int i = 0; i < $this$mapIndexedTo.length(); i++) {
            char item = $this$mapIndexedTo.charAt(i);
            Integer valueOf = Integer.valueOf(index);
            index++;
            destination.add(transform.invoke(valueOf, Character.valueOf(item)));
        }
        return destination;
    }

    @NotNull
    public static final <R> List<R> mapNotNull(@NotNull CharSequence $this$mapNotNull, @NotNull Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$mapNotNull, "$this$mapNotNull");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        Collection destination$iv = new ArrayList();
        CharSequence $this$forEach$iv$iv = $this$mapNotNull;
        for (int i = 0; i < $this$forEach$iv$iv.length(); i++) {
            Object it$iv = transform.invoke(Character.valueOf($this$forEach$iv$iv.charAt(i)));
            if (it$iv != null) {
                destination$iv.add(it$iv);
            }
        }
        return (List) destination$iv;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapNotNullTo(@NotNull CharSequence $this$mapNotNullTo, @NotNull C destination, @NotNull Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$mapNotNullTo, "$this$mapNotNullTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        CharSequence $this$forEach$iv = $this$mapNotNullTo;
        for (int i = 0; i < $this$forEach$iv.length(); i++) {
            Object it2 = transform.invoke(Character.valueOf($this$forEach$iv.charAt(i)));
            if (it2 != null) {
                destination.add(it2);
            }
        }
        return destination;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapTo(@NotNull CharSequence $this$mapTo, @NotNull C destination, @NotNull Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$mapTo, "$this$mapTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (int i = 0; i < $this$mapTo.length(); i++) {
            destination.add(transform.invoke(Character.valueOf($this$mapTo.charAt(i))));
        }
        return destination;
    }

    @NotNull
    public static final Iterable<IndexedValue<Character>> withIndex(@NotNull CharSequence $this$withIndex) {
        Intrinsics.checkParameterIsNotNull($this$withIndex, "$this$withIndex");
        return new IndexingIterable(new StringsKt___StringsKt$withIndex$1($this$withIndex));
    }

    public static final boolean all(@NotNull CharSequence $this$all, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$all, "$this$all");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < $this$all.length(); i++) {
            if (!predicate.invoke(Character.valueOf($this$all.charAt(i))).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final boolean any(@NotNull CharSequence $this$any) {
        Intrinsics.checkParameterIsNotNull($this$any, "$this$any");
        return !($this$any.length() == 0);
    }

    public static final boolean any(@NotNull CharSequence $this$any, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$any, "$this$any");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < $this$any.length(); i++) {
            if (predicate.invoke(Character.valueOf($this$any.charAt(i))).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    @InlineOnly
    private static final int count(@NotNull CharSequence $this$count) {
        return $this$count.length();
    }

    public static final int count(@NotNull CharSequence $this$count, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$count, "$this$count");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int count = 0;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$count.length()) {
                return count;
            }
            if (predicate.invoke(Character.valueOf($this$count.charAt(i2))).booleanValue()) {
                count++;
            }
            i = i2 + 1;
        }
    }

    public static final <R> R fold(@NotNull CharSequence $this$fold, R initial, @NotNull Function2<? super R, ? super Character, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull($this$fold, "$this$fold");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        Object accumulator = initial;
        for (int i = 0; i < $this$fold.length(); i++) {
            accumulator = operation.invoke(accumulator, Character.valueOf($this$fold.charAt(i)));
        }
        return accumulator;
    }

    public static final <R> R foldIndexed(@NotNull CharSequence $this$foldIndexed, R initial, @NotNull Function3<? super Integer, ? super R, ? super Character, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull($this$foldIndexed, "$this$foldIndexed");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = 0;
        Object accumulator = initial;
        for (int i = 0; i < $this$foldIndexed.length(); i++) {
            char element = $this$foldIndexed.charAt(i);
            Integer valueOf = Integer.valueOf(index);
            index++;
            accumulator = operation.invoke(valueOf, accumulator, Character.valueOf(element));
        }
        return accumulator;
    }

    public static final <R> R foldRight(@NotNull CharSequence $this$foldRight, R initial, @NotNull Function2<? super Character, ? super R, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull($this$foldRight, "$this$foldRight");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        Object accumulator = initial;
        for (int index = StringsKt.getLastIndex($this$foldRight); index >= 0; index--) {
            accumulator = operation.invoke(Character.valueOf($this$foldRight.charAt(index)), accumulator);
        }
        return accumulator;
    }

    public static final <R> R foldRightIndexed(@NotNull CharSequence $this$foldRightIndexed, R initial, @NotNull Function3<? super Integer, ? super Character, ? super R, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull($this$foldRightIndexed, "$this$foldRightIndexed");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        Object accumulator = initial;
        for (int index = StringsKt.getLastIndex($this$foldRightIndexed); index >= 0; index--) {
            accumulator = operation.invoke(Integer.valueOf(index), Character.valueOf($this$foldRightIndexed.charAt(index)), accumulator);
        }
        return accumulator;
    }

    public static final void forEach(@NotNull CharSequence $this$forEach, @NotNull Function1<? super Character, Unit> action) {
        Intrinsics.checkParameterIsNotNull($this$forEach, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(action, "action");
        for (int i = 0; i < $this$forEach.length(); i++) {
            action.invoke(Character.valueOf($this$forEach.charAt(i)));
        }
    }

    public static final void forEachIndexed(@NotNull CharSequence $this$forEachIndexed, @NotNull Function2<? super Integer, ? super Character, Unit> action) {
        Intrinsics.checkParameterIsNotNull($this$forEachIndexed, "$this$forEachIndexed");
        Intrinsics.checkParameterIsNotNull(action, "action");
        int index = 0;
        for (int i = 0; i < $this$forEachIndexed.length(); i++) {
            char item = $this$forEachIndexed.charAt(i);
            Integer valueOf = Integer.valueOf(index);
            index++;
            action.invoke(valueOf, Character.valueOf(item));
        }
    }

    @Nullable
    public static final Character max(@NotNull CharSequence $this$max) {
        int i = 1;
        Intrinsics.checkParameterIsNotNull($this$max, "$this$max");
        if ($this$max.length() == 0) {
            return null;
        }
        char max = $this$max.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$max);
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$max.charAt(i);
                if (max < e) {
                    max = e;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(max);
    }

    @Nullable
    public static final <R extends Comparable<? super R>> Character maxBy(@NotNull CharSequence $this$maxBy, @NotNull Function1<? super Character, ? extends R> selector) {
        boolean z;
        int i = 1;
        Intrinsics.checkParameterIsNotNull($this$maxBy, "$this$maxBy");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        if ($this$maxBy.length() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return null;
        }
        char maxElem = $this$maxBy.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$maxBy);
        if (lastIndex == 0) {
            return Character.valueOf(maxElem);
        }
        Comparable maxValue = (Comparable) selector.invoke(Character.valueOf(maxElem));
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$maxBy.charAt(i);
                Comparable v = (Comparable) selector.invoke(Character.valueOf(e));
                if (maxValue.compareTo(v) < 0) {
                    maxElem = e;
                    maxValue = v;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(maxElem);
    }

    @Nullable
    public static final Character maxWith(@NotNull CharSequence $this$maxWith, @NotNull Comparator<? super Character> comparator) {
        int i = 1;
        Intrinsics.checkParameterIsNotNull($this$maxWith, "$this$maxWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        if ($this$maxWith.length() == 0) {
            return null;
        }
        char max = $this$maxWith.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$maxWith);
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$maxWith.charAt(i);
                if (comparator.compare(Character.valueOf(max), Character.valueOf(e)) < 0) {
                    max = e;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(max);
    }

    @Nullable
    public static final Character min(@NotNull CharSequence $this$min) {
        int i = 1;
        Intrinsics.checkParameterIsNotNull($this$min, "$this$min");
        if ($this$min.length() == 0) {
            return null;
        }
        char min = $this$min.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$min);
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$min.charAt(i);
                if (min > e) {
                    min = e;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(min);
    }

    @Nullable
    public static final <R extends Comparable<? super R>> Character minBy(@NotNull CharSequence $this$minBy, @NotNull Function1<? super Character, ? extends R> selector) {
        boolean z;
        int i = 1;
        Intrinsics.checkParameterIsNotNull($this$minBy, "$this$minBy");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        if ($this$minBy.length() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return null;
        }
        char minElem = $this$minBy.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$minBy);
        if (lastIndex == 0) {
            return Character.valueOf(minElem);
        }
        Comparable minValue = (Comparable) selector.invoke(Character.valueOf(minElem));
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$minBy.charAt(i);
                Comparable v = (Comparable) selector.invoke(Character.valueOf(e));
                if (minValue.compareTo(v) > 0) {
                    minElem = e;
                    minValue = v;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(minElem);
    }

    @Nullable
    public static final Character minWith(@NotNull CharSequence $this$minWith, @NotNull Comparator<? super Character> comparator) {
        int i = 1;
        Intrinsics.checkParameterIsNotNull($this$minWith, "$this$minWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        if ($this$minWith.length() == 0) {
            return null;
        }
        char min = $this$minWith.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$minWith);
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$minWith.charAt(i);
                if (comparator.compare(Character.valueOf(min), Character.valueOf(e)) > 0) {
                    min = e;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(min);
    }

    public static final boolean none(@NotNull CharSequence $this$none) {
        Intrinsics.checkParameterIsNotNull($this$none, "$this$none");
        return $this$none.length() == 0;
    }

    public static final boolean none(@NotNull CharSequence $this$none, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$none, "$this$none");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < $this$none.length(); i++) {
            if (predicate.invoke(Character.valueOf($this$none.charAt(i))).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    @SinceKotlin(version = "1.1")
    public static final <S extends CharSequence> S onEach(@NotNull S $this$onEach, @NotNull Function1<? super Character, Unit> action) {
        Intrinsics.checkParameterIsNotNull($this$onEach, "$this$onEach");
        Intrinsics.checkParameterIsNotNull(action, "action");
        CharSequence $this$apply = $this$onEach;
        for (int i = 0; i < $this$apply.length(); i++) {
            action.invoke(Character.valueOf($this$apply.charAt(i)));
        }
        return $this$onEach;
    }

    public static final char reduce(@NotNull CharSequence $this$reduce, @NotNull Function2<? super Character, ? super Character, Character> operation) {
        boolean z;
        int i = 1;
        Intrinsics.checkParameterIsNotNull($this$reduce, "$this$reduce");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        if ($this$reduce.length() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator = $this$reduce.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$reduce);
        if (1 <= lastIndex) {
            while (true) {
                accumulator = operation.invoke(Character.valueOf(accumulator), Character.valueOf($this$reduce.charAt(i))).charValue();
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return accumulator;
    }

    public static final char reduceIndexed(@NotNull CharSequence $this$reduceIndexed, @NotNull Function3<? super Integer, ? super Character, ? super Character, Character> operation) {
        boolean z;
        int i = 1;
        Intrinsics.checkParameterIsNotNull($this$reduceIndexed, "$this$reduceIndexed");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        if ($this$reduceIndexed.length() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator = $this$reduceIndexed.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$reduceIndexed);
        if (1 <= lastIndex) {
            while (true) {
                accumulator = operation.invoke(Integer.valueOf(i), Character.valueOf(accumulator), Character.valueOf($this$reduceIndexed.charAt(i))).charValue();
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return accumulator;
    }

    public static final char reduceRight(@NotNull CharSequence $this$reduceRight, @NotNull Function2<? super Character, ? super Character, Character> operation) {
        Intrinsics.checkParameterIsNotNull($this$reduceRight, "$this$reduceRight");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = StringsKt.getLastIndex($this$reduceRight);
        if (index < 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator = $this$reduceRight.charAt(index);
        for (int index2 = index - 1; index2 >= 0; index2--) {
            accumulator = operation.invoke(Character.valueOf($this$reduceRight.charAt(index2)), Character.valueOf(accumulator)).charValue();
        }
        return accumulator;
    }

    public static final char reduceRightIndexed(@NotNull CharSequence $this$reduceRightIndexed, @NotNull Function3<? super Integer, ? super Character, ? super Character, Character> operation) {
        Intrinsics.checkParameterIsNotNull($this$reduceRightIndexed, "$this$reduceRightIndexed");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int index = StringsKt.getLastIndex($this$reduceRightIndexed);
        if (index < 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char accumulator = $this$reduceRightIndexed.charAt(index);
        for (int index2 = index - 1; index2 >= 0; index2--) {
            accumulator = operation.invoke(Integer.valueOf(index2), Character.valueOf($this$reduceRightIndexed.charAt(index2)), Character.valueOf(accumulator)).charValue();
        }
        return accumulator;
    }

    public static final int sumBy(@NotNull CharSequence $this$sumBy, @NotNull Function1<? super Character, Integer> selector) {
        Intrinsics.checkParameterIsNotNull($this$sumBy, "$this$sumBy");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        int sum = 0;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$sumBy.length()) {
                return sum;
            }
            sum += selector.invoke(Character.valueOf($this$sumBy.charAt(i2))).intValue();
            i = i2 + 1;
        }
    }

    public static final double sumByDouble(@NotNull CharSequence $this$sumByDouble, @NotNull Function1<? super Character, Double> selector) {
        Intrinsics.checkParameterIsNotNull($this$sumByDouble, "$this$sumByDouble");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        double sum = 0.0d;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$sumByDouble.length()) {
                return sum;
            }
            sum += selector.invoke(Character.valueOf($this$sumByDouble.charAt(i2))).doubleValue();
            i = i2 + 1;
        }
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final List<String> chunked(@NotNull CharSequence $this$chunked, int size) {
        Intrinsics.checkParameterIsNotNull($this$chunked, "$this$chunked");
        return StringsKt.windowed($this$chunked, size, size, true);
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final <R> List<R> chunked(@NotNull CharSequence $this$chunked, int size, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$chunked, "$this$chunked");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return StringsKt.windowed($this$chunked, size, size, true, transform);
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final Sequence<String> chunkedSequence(@NotNull CharSequence $this$chunkedSequence, int size) {
        Intrinsics.checkParameterIsNotNull($this$chunkedSequence, "$this$chunkedSequence");
        return StringsKt.chunkedSequence($this$chunkedSequence, size, StringsKt___StringsKt$chunkedSequence$1.INSTANCE);
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final <R> Sequence<R> chunkedSequence(@NotNull CharSequence $this$chunkedSequence, int size, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$chunkedSequence, "$this$chunkedSequence");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return StringsKt.windowedSequence($this$chunkedSequence, size, size, true, transform);
    }

    @NotNull
    public static final Pair<CharSequence, CharSequence> partition(@NotNull CharSequence $this$partition, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$partition, "$this$partition");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= $this$partition.length()) {
                return new Pair<>(first, second);
            }
            char element = $this$partition.charAt(i2);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                first.append(element);
            } else {
                second.append(element);
            }
            i = i2 + 1;
        }
    }

    @NotNull
    public static final Pair<String, String> partition(@NotNull String $this$partition, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull($this$partition, "$this$partition");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        int length = $this$partition.length();
        for (int i = 0; i < length; i++) {
            char element = $this$partition.charAt(i);
            if (predicate.invoke(Character.valueOf(element)).booleanValue()) {
                first.append(element);
            } else {
                second.append(element);
            }
        }
        return new Pair<>(first.toString(), second.toString());
    }

    public static /* synthetic */ List windowed$default(CharSequence charSequence, int i, int i2, boolean z, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowed(charSequence, i, i2, z);
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final List<String> windowed(@NotNull CharSequence $this$windowed, int size, int step, boolean partialWindows) {
        Intrinsics.checkParameterIsNotNull($this$windowed, "$this$windowed");
        return StringsKt.windowed($this$windowed, size, step, partialWindows, StringsKt___StringsKt$windowed$1.INSTANCE);
    }

    public static /* synthetic */ List windowed$default(CharSequence charSequence, int i, int i2, boolean z, Function1 function1, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowed(charSequence, i, i2, z, function1);
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final <R> List<R> windowed(@NotNull CharSequence $this$windowed, int size, int step, boolean partialWindows, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        int coercedEnd;
        Intrinsics.checkParameterIsNotNull($this$windowed, "$this$windowed");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        SlidingWindowKt.checkWindowSizeStep(size, step);
        int thisSize = $this$windowed.length();
        ArrayList result = new ArrayList(((thisSize + step) - 1) / step);
        int index = 0;
        while (index < thisSize) {
            int end = index + size;
            if (end > thisSize) {
                if (!partialWindows) {
                    break;
                }
                coercedEnd = thisSize;
            } else {
                coercedEnd = end;
            }
            result.add(transform.invoke($this$windowed.subSequence(index, coercedEnd)));
            index += step;
        }
        return result;
    }

    public static /* synthetic */ Sequence windowedSequence$default(CharSequence charSequence, int i, int i2, boolean z, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowedSequence(charSequence, i, i2, z);
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final Sequence<String> windowedSequence(@NotNull CharSequence $this$windowedSequence, int size, int step, boolean partialWindows) {
        Intrinsics.checkParameterIsNotNull($this$windowedSequence, "$this$windowedSequence");
        return StringsKt.windowedSequence($this$windowedSequence, size, step, partialWindows, StringsKt___StringsKt$windowedSequence$1.INSTANCE);
    }

    public static /* synthetic */ Sequence windowedSequence$default(CharSequence charSequence, int i, int i2, boolean z, Function1 function1, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowedSequence(charSequence, i, i2, z, function1);
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final <R> Sequence<R> windowedSequence(@NotNull CharSequence $this$windowedSequence, int size, int step, boolean partialWindows, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$windowedSequence, "$this$windowedSequence");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        SlidingWindowKt.checkWindowSizeStep(size, step);
        return SequencesKt.map(CollectionsKt.asSequence(RangesKt.step(partialWindows ? StringsKt.getIndices($this$windowedSequence) : RangesKt.until(0, ($this$windowedSequence.length() - size) + 1), step)), new StringsKt___StringsKt$windowedSequence$2($this$windowedSequence, transform, size));
    }

    @NotNull
    public static final List<Pair<Character, Character>> zip(@NotNull CharSequence $this$zip, @NotNull CharSequence other) {
        Intrinsics.checkParameterIsNotNull($this$zip, "$this$zip");
        Intrinsics.checkParameterIsNotNull(other, "other");
        CharSequence $this$zip$iv = $this$zip;
        int length$iv = Math.min($this$zip$iv.length(), other.length());
        ArrayList list$iv = new ArrayList(length$iv);
        for (int i = 0; i < length$iv; i++) {
            list$iv.add(TuplesKt.to(Character.valueOf($this$zip$iv.charAt(i)), Character.valueOf(other.charAt(i))));
        }
        return list$iv;
    }

    @NotNull
    public static final <V> List<V> zip(@NotNull CharSequence $this$zip, @NotNull CharSequence other, @NotNull Function2<? super Character, ? super Character, ? extends V> transform) {
        Intrinsics.checkParameterIsNotNull($this$zip, "$this$zip");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int length = Math.min($this$zip.length(), other.length());
        ArrayList list = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            list.add(transform.invoke(Character.valueOf($this$zip.charAt(i)), Character.valueOf(other.charAt(i))));
        }
        return list;
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final List<Pair<Character, Character>> zipWithNext(@NotNull CharSequence $this$zipWithNext) {
        Intrinsics.checkParameterIsNotNull($this$zipWithNext, "$this$zipWithNext");
        CharSequence $this$zipWithNext$iv = $this$zipWithNext;
        int size$iv = $this$zipWithNext$iv.length() - 1;
        if (size$iv < 1) {
            return CollectionsKt.emptyList();
        }
        ArrayList result$iv = new ArrayList(size$iv);
        for (int i = 0; i < size$iv; i++) {
            result$iv.add(TuplesKt.to(Character.valueOf($this$zipWithNext$iv.charAt(i)), Character.valueOf($this$zipWithNext$iv.charAt(i + 1))));
        }
        return result$iv;
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final <R> List<R> zipWithNext(@NotNull CharSequence $this$zipWithNext, @NotNull Function2<? super Character, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull($this$zipWithNext, "$this$zipWithNext");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int size = $this$zipWithNext.length() - 1;
        if (size < 1) {
            return CollectionsKt.emptyList();
        }
        ArrayList result = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            result.add(transform.invoke(Character.valueOf($this$zipWithNext.charAt(i)), Character.valueOf($this$zipWithNext.charAt(i + 1))));
        }
        return result;
    }

    @NotNull
    public static final Iterable<Character> asIterable(@NotNull CharSequence $this$asIterable) {
        Intrinsics.checkParameterIsNotNull($this$asIterable, "$this$asIterable");
        if ($this$asIterable instanceof String) {
            if ($this$asIterable.length() == 0) {
                return CollectionsKt.emptyList();
            }
        }
        return new StringsKt___StringsKt$asIterable$$inlined$Iterable$1($this$asIterable);
    }

    @NotNull
    public static final Sequence<Character> asSequence(@NotNull CharSequence $this$asSequence) {
        Intrinsics.checkParameterIsNotNull($this$asSequence, "$this$asSequence");
        if ($this$asSequence instanceof String) {
            if ($this$asSequence.length() == 0) {
                return SequencesKt.emptySequence();
            }
        }
        return new StringsKt___StringsKt$asSequence$$inlined$Sequence$1($this$asSequence);
    }
}
