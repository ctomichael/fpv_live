package kotlin.collections;

import dji.pilot.fpv.util.DJIFlurryReport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.TypeCastException;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0001\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0018\n\u0002\u0010\u0005\n\u0002\u0010\u0012\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0013\n\u0002\u0010\u0007\n\u0002\u0010\u0014\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\u0010\t\n\u0002\u0010\u0016\n\u0002\u0010\n\n\u0002\u0010\u0017\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000e\n\u0002\b\u0017\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0005\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\f\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010\u0004\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00050\u0001*\u00020\u0006\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00070\u0001*\u00020\b\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\t0\u0001*\u00020\n\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0001*\u00020\f\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\r0\u0001*\u00020\u000e\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0001*\u00020\u0010\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00110\u0001*\u00020\u0012\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00130\u0001*\u00020\u0014\u001aU\u0010\u0015\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010\u001c\u001a9\u0010\u0015\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010\u001d\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\n2\u0006\u0010\u0016\u001a\u00020\t2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\r2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a2\u0010\u001e\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\f¢\u0006\u0004\b \u0010!\u001a\"\u0010\"\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0004\b#\u0010$\u001a\"\u0010%\u001a\u00020&\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0004\b'\u0010(\u001a0\u0010)\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\f¢\u0006\u0002\u0010!\u001a\u0015\u0010)\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006H\f\u001a\u0015\u0010)\u001a\u00020\u0005*\u00020\b2\u0006\u0010\u001f\u001a\u00020\bH\f\u001a\u0015\u0010)\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u001f\u001a\u00020\nH\f\u001a\u0015\u0010)\u001a\u00020\u0005*\u00020\f2\u0006\u0010\u001f\u001a\u00020\fH\f\u001a\u0015\u0010)\u001a\u00020\u0005*\u00020\u000e2\u0006\u0010\u001f\u001a\u00020\u000eH\f\u001a\u0015\u0010)\u001a\u00020\u0005*\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0010H\f\u001a\u0015\u0010)\u001a\u00020\u0005*\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0012H\f\u001a\u0015\u0010)\u001a\u00020\u0005*\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0014H\f\u001a \u0010*\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u0010$\u001a\r\u0010*\u001a\u00020\u000f*\u00020\u0006H\b\u001a\r\u0010*\u001a\u00020\u000f*\u00020\bH\b\u001a\r\u0010*\u001a\u00020\u000f*\u00020\nH\b\u001a\r\u0010*\u001a\u00020\u000f*\u00020\fH\b\u001a\r\u0010*\u001a\u00020\u000f*\u00020\u000eH\b\u001a\r\u0010*\u001a\u00020\u000f*\u00020\u0010H\b\u001a\r\u0010*\u001a\u00020\u000f*\u00020\u0012H\b\u001a\r\u0010*\u001a\u00020\u000f*\u00020\u0014H\b\u001a \u0010+\u001a\u00020&\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u0010(\u001a\r\u0010+\u001a\u00020&*\u00020\u0006H\b\u001a\r\u0010+\u001a\u00020&*\u00020\bH\b\u001a\r\u0010+\u001a\u00020&*\u00020\nH\b\u001a\r\u0010+\u001a\u00020&*\u00020\fH\b\u001a\r\u0010+\u001a\u00020&*\u00020\u000eH\b\u001a\r\u0010+\u001a\u00020&*\u00020\u0010H\b\u001a\r\u0010+\u001a\u00020&*\u00020\u0012H\b\u001a\r\u0010+\u001a\u00020&*\u00020\u0014H\b\u001aQ\u0010,\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010-\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00032\b\b\u0002\u0010.\u001a\u00020\u000f2\b\b\u0002\u0010/\u001a\u00020\u000f2\b\b\u0002\u00100\u001a\u00020\u000fH\u0007¢\u0006\u0002\u00101\u001a2\u0010,\u001a\u00020\u0006*\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\b\b\u0002\u0010.\u001a\u00020\u000f2\b\b\u0002\u0010/\u001a\u00020\u000f2\b\b\u0002\u00100\u001a\u00020\u000fH\u0007\u001a2\u0010,\u001a\u00020\b*\u00020\b2\u0006\u0010-\u001a\u00020\b2\b\b\u0002\u0010.\u001a\u00020\u000f2\b\b\u0002\u0010/\u001a\u00020\u000f2\b\b\u0002\u00100\u001a\u00020\u000fH\u0007\u001a2\u0010,\u001a\u00020\n*\u00020\n2\u0006\u0010-\u001a\u00020\n2\b\b\u0002\u0010.\u001a\u00020\u000f2\b\b\u0002\u0010/\u001a\u00020\u000f2\b\b\u0002\u00100\u001a\u00020\u000fH\u0007\u001a2\u0010,\u001a\u00020\f*\u00020\f2\u0006\u0010-\u001a\u00020\f2\b\b\u0002\u0010.\u001a\u00020\u000f2\b\b\u0002\u0010/\u001a\u00020\u000f2\b\b\u0002\u00100\u001a\u00020\u000fH\u0007\u001a2\u0010,\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010-\u001a\u00020\u000e2\b\b\u0002\u0010.\u001a\u00020\u000f2\b\b\u0002\u0010/\u001a\u00020\u000f2\b\b\u0002\u00100\u001a\u00020\u000fH\u0007\u001a2\u0010,\u001a\u00020\u0010*\u00020\u00102\u0006\u0010-\u001a\u00020\u00102\b\b\u0002\u0010.\u001a\u00020\u000f2\b\b\u0002\u0010/\u001a\u00020\u000f2\b\b\u0002\u00100\u001a\u00020\u000fH\u0007\u001a2\u0010,\u001a\u00020\u0012*\u00020\u00122\u0006\u0010-\u001a\u00020\u00122\b\b\u0002\u0010.\u001a\u00020\u000f2\b\b\u0002\u0010/\u001a\u00020\u000f2\b\b\u0002\u00100\u001a\u00020\u000fH\u0007\u001a2\u0010,\u001a\u00020\u0014*\u00020\u00142\u0006\u0010-\u001a\u00020\u00142\b\b\u0002\u0010.\u001a\u00020\u000f2\b\b\u0002\u0010/\u001a\u00020\u000f2\b\b\u0002\u00100\u001a\u00020\u000fH\u0007\u001a$\u00102\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u00103\u001a.\u00102\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u00104\u001a\u00020\u000fH\b¢\u0006\u0002\u00105\u001a\r\u00102\u001a\u00020\u0006*\u00020\u0006H\b\u001a\u0015\u00102\u001a\u00020\u0006*\u00020\u00062\u0006\u00104\u001a\u00020\u000fH\b\u001a\r\u00102\u001a\u00020\b*\u00020\bH\b\u001a\u0015\u00102\u001a\u00020\b*\u00020\b2\u0006\u00104\u001a\u00020\u000fH\b\u001a\r\u00102\u001a\u00020\n*\u00020\nH\b\u001a\u0015\u00102\u001a\u00020\n*\u00020\n2\u0006\u00104\u001a\u00020\u000fH\b\u001a\r\u00102\u001a\u00020\f*\u00020\fH\b\u001a\u0015\u00102\u001a\u00020\f*\u00020\f2\u0006\u00104\u001a\u00020\u000fH\b\u001a\r\u00102\u001a\u00020\u000e*\u00020\u000eH\b\u001a\u0015\u00102\u001a\u00020\u000e*\u00020\u000e2\u0006\u00104\u001a\u00020\u000fH\b\u001a\r\u00102\u001a\u00020\u0010*\u00020\u0010H\b\u001a\u0015\u00102\u001a\u00020\u0010*\u00020\u00102\u0006\u00104\u001a\u00020\u000fH\b\u001a\r\u00102\u001a\u00020\u0012*\u00020\u0012H\b\u001a\u0015\u00102\u001a\u00020\u0012*\u00020\u00122\u0006\u00104\u001a\u00020\u000fH\b\u001a\r\u00102\u001a\u00020\u0014*\u00020\u0014H\b\u001a\u0015\u00102\u001a\u00020\u0014*\u00020\u00142\u0006\u00104\u001a\u00020\u000fH\b\u001a6\u00106\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0004\b7\u00108\u001a\"\u00106\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0002\b7\u001a\"\u00106\u001a\u00020\b*\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0002\b7\u001a\"\u00106\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0002\b7\u001a\"\u00106\u001a\u00020\f*\u00020\f2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0002\b7\u001a\"\u00106\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0002\b7\u001a\"\u00106\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0002\b7\u001a\"\u00106\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0002\b7\u001a\"\u00106\u001a\u00020\u0014*\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0002\b7\u001a5\u00109\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0001¢\u0006\u0004\b6\u00108\u001a!\u00109\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0001¢\u0006\u0002\b6\u001a!\u00109\u001a\u00020\b*\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0001¢\u0006\u0002\b6\u001a!\u00109\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0001¢\u0006\u0002\b6\u001a!\u00109\u001a\u00020\f*\u00020\f2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0001¢\u0006\u0002\b6\u001a!\u00109\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0001¢\u0006\u0002\b6\u001a!\u00109\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0001¢\u0006\u0002\b6\u001a!\u00109\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0001¢\u0006\u0002\b6\u001a!\u00109\u001a\u00020\u0014*\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0001¢\u0006\u0002\b6\u001a(\u0010:\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010;\u001a\u00020\u000fH\b¢\u0006\u0002\u0010<\u001a\u0015\u0010:\u001a\u00020\u0005*\u00020\u00062\u0006\u0010;\u001a\u00020\u000fH\b\u001a\u0015\u0010:\u001a\u00020\u0007*\u00020\b2\u0006\u0010;\u001a\u00020\u000fH\b\u001a\u0015\u0010:\u001a\u00020\t*\u00020\n2\u0006\u0010;\u001a\u00020\u000fH\b\u001a\u0015\u0010:\u001a\u00020\u000b*\u00020\f2\u0006\u0010;\u001a\u00020\u000fH\b\u001a\u0015\u0010:\u001a\u00020\r*\u00020\u000e2\u0006\u0010;\u001a\u00020\u000fH\b\u001a\u0015\u0010:\u001a\u00020\u000f*\u00020\u00102\u0006\u0010;\u001a\u00020\u000fH\b\u001a\u0015\u0010:\u001a\u00020\u0011*\u00020\u00122\u0006\u0010;\u001a\u00020\u000fH\b\u001a\u0015\u0010:\u001a\u00020\u0013*\u00020\u00142\u0006\u0010;\u001a\u00020\u000fH\b\u001a7\u0010=\u001a\u00020>\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010?\u001a&\u0010=\u001a\u00020>*\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010=\u001a\u00020>*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010=\u001a\u00020>*\u00020\n2\u0006\u0010\u0016\u001a\u00020\t2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010=\u001a\u00020>*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010=\u001a\u00020>*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\r2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010=\u001a\u00020>*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010=\u001a\u00020>*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010=\u001a\u00020>*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a-\u0010@\u001a\b\u0012\u0004\u0012\u0002HA0\u0001\"\u0004\b\u0000\u0010A*\u0006\u0012\u0002\b\u00030\u00032\f\u0010B\u001a\b\u0012\u0004\u0012\u0002HA0C¢\u0006\u0002\u0010D\u001aA\u0010E\u001a\u0002HF\"\u0010\b\u0000\u0010F*\n\u0012\u0006\b\u0000\u0012\u0002HA0G\"\u0004\b\u0001\u0010A*\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010-\u001a\u0002HF2\f\u0010B\u001a\b\u0012\u0004\u0012\u0002HA0C¢\u0006\u0002\u0010H\u001a,\u0010I\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u0002H\u0002¢\u0006\u0002\u0010J\u001a4\u0010I\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u000e\u0010K\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0002¢\u0006\u0002\u0010L\u001a2\u0010I\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\f\u0010K\u001a\b\u0012\u0004\u0012\u0002H\u00020MH\u0002¢\u0006\u0002\u0010N\u001a\u0015\u0010I\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u0005H\u0002\u001a\u0015\u0010I\u001a\u00020\u0006*\u00020\u00062\u0006\u0010K\u001a\u00020\u0006H\u0002\u001a\u001b\u0010I\u001a\u00020\u0006*\u00020\u00062\f\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00050MH\u0002\u001a\u0015\u0010I\u001a\u00020\b*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0007H\u0002\u001a\u0015\u0010I\u001a\u00020\b*\u00020\b2\u0006\u0010K\u001a\u00020\bH\u0002\u001a\u001b\u0010I\u001a\u00020\b*\u00020\b2\f\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00070MH\u0002\u001a\u0015\u0010I\u001a\u00020\n*\u00020\n2\u0006\u0010\u0016\u001a\u00020\tH\u0002\u001a\u0015\u0010I\u001a\u00020\n*\u00020\n2\u0006\u0010K\u001a\u00020\nH\u0002\u001a\u001b\u0010I\u001a\u00020\n*\u00020\n2\f\u0010K\u001a\b\u0012\u0004\u0012\u00020\t0MH\u0002\u001a\u0015\u0010I\u001a\u00020\f*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000bH\u0002\u001a\u0015\u0010I\u001a\u00020\f*\u00020\f2\u0006\u0010K\u001a\u00020\fH\u0002\u001a\u001b\u0010I\u001a\u00020\f*\u00020\f2\f\u0010K\u001a\b\u0012\u0004\u0012\u00020\u000b0MH\u0002\u001a\u0015\u0010I\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\rH\u0002\u001a\u0015\u0010I\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010K\u001a\u00020\u000eH\u0002\u001a\u001b\u0010I\u001a\u00020\u000e*\u00020\u000e2\f\u0010K\u001a\b\u0012\u0004\u0012\u00020\r0MH\u0002\u001a\u0015\u0010I\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000fH\u0002\u001a\u0015\u0010I\u001a\u00020\u0010*\u00020\u00102\u0006\u0010K\u001a\u00020\u0010H\u0002\u001a\u001b\u0010I\u001a\u00020\u0010*\u00020\u00102\f\u0010K\u001a\b\u0012\u0004\u0012\u00020\u000f0MH\u0002\u001a\u0015\u0010I\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0011H\u0002\u001a\u0015\u0010I\u001a\u00020\u0012*\u00020\u00122\u0006\u0010K\u001a\u00020\u0012H\u0002\u001a\u001b\u0010I\u001a\u00020\u0012*\u00020\u00122\f\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00110MH\u0002\u001a\u0015\u0010I\u001a\u00020\u0014*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0013H\u0002\u001a\u0015\u0010I\u001a\u00020\u0014*\u00020\u00142\u0006\u0010K\u001a\u00020\u0014H\u0002\u001a\u001b\u0010I\u001a\u00020\u0014*\u00020\u00142\f\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00130MH\u0002\u001a,\u0010O\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u0002H\b¢\u0006\u0002\u0010J\u001a\u001d\u0010P\u001a\u00020>\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010Q\u001a*\u0010P\u001a\u00020>\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020R*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u0010S\u001a1\u0010P\u001a\u00020>\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010T\u001a\n\u0010P\u001a\u00020>*\u00020\b\u001a\u001e\u0010P\u001a\u00020>*\u00020\b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010P\u001a\u00020>*\u00020\n\u001a\u001e\u0010P\u001a\u00020>*\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010P\u001a\u00020>*\u00020\f\u001a\u001e\u0010P\u001a\u00020>*\u00020\f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010P\u001a\u00020>*\u00020\u000e\u001a\u001e\u0010P\u001a\u00020>*\u00020\u000e2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010P\u001a\u00020>*\u00020\u0010\u001a\u001e\u0010P\u001a\u00020>*\u00020\u00102\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010P\u001a\u00020>*\u00020\u0012\u001a\u001e\u0010P\u001a\u00020>*\u00020\u00122\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010P\u001a\u00020>*\u00020\u0014\u001a\u001e\u0010P\u001a\u00020>*\u00020\u00142\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a9\u0010U\u001a\u00020>\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0019¢\u0006\u0002\u0010V\u001aM\u0010U\u001a\u00020>\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010W\u001a-\u0010X\u001a\b\u0012\u0004\u0012\u0002H\u00020Y\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020R*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010Z\u001a?\u0010X\u001a\b\u0012\u0004\u0012\u0002H\u00020Y\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0019¢\u0006\u0002\u0010[\u001a\u0010\u0010X\u001a\b\u0012\u0004\u0012\u00020\u00050Y*\u00020\u0006\u001a\u0010\u0010X\u001a\b\u0012\u0004\u0012\u00020\u00070Y*\u00020\b\u001a\u0010\u0010X\u001a\b\u0012\u0004\u0012\u00020\t0Y*\u00020\n\u001a\u0010\u0010X\u001a\b\u0012\u0004\u0012\u00020\u000b0Y*\u00020\f\u001a\u0010\u0010X\u001a\b\u0012\u0004\u0012\u00020\r0Y*\u00020\u000e\u001a\u0010\u0010X\u001a\b\u0012\u0004\u0012\u00020\u000f0Y*\u00020\u0010\u001a\u0010\u0010X\u001a\b\u0012\u0004\u0012\u00020\u00110Y*\u00020\u0012\u001a\u0010\u0010X\u001a\b\u0012\u0004\u0012\u00020\u00130Y*\u00020\u0014\u001a\u0015\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u00050\u0003*\u00020\u0006¢\u0006\u0002\u0010]\u001a\u0015\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u00070\u0003*\u00020\b¢\u0006\u0002\u0010^\u001a\u0015\u0010\\\u001a\b\u0012\u0004\u0012\u00020\t0\u0003*\u00020\n¢\u0006\u0002\u0010_\u001a\u0015\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003*\u00020\f¢\u0006\u0002\u0010`\u001a\u0015\u0010\\\u001a\b\u0012\u0004\u0012\u00020\r0\u0003*\u00020\u000e¢\u0006\u0002\u0010a\u001a\u0015\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0003*\u00020\u0010¢\u0006\u0002\u0010b\u001a\u0015\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u00110\u0003*\u00020\u0012¢\u0006\u0002\u0010c\u001a\u0015\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u00130\u0003*\u00020\u0014¢\u0006\u0002\u0010d¨\u0006e"}, d2 = {"asList", "", "T", "", "([Ljava/lang/Object;)Ljava/util/List;", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "binarySearch", "element", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "fromIndex", "toIndex", "([Ljava/lang/Object;Ljava/lang/Object;Ljava/util/Comparator;II)I", "([Ljava/lang/Object;Ljava/lang/Object;II)I", "contentDeepEquals", "other", "contentDeepEqualsInline", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepHashCode", "contentDeepHashCodeInline", "([Ljava/lang/Object;)I", "contentDeepToString", "", "contentDeepToStringInline", "([Ljava/lang/Object;)Ljava/lang/String;", "contentEquals", "contentHashCode", "contentToString", "copyInto", "destination", "destinationOffset", "startIndex", "endIndex", "([Ljava/lang/Object;[Ljava/lang/Object;III)[Ljava/lang/Object;", "copyOf", "([Ljava/lang/Object;)[Ljava/lang/Object;", "newSize", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "copyOfRange", "copyOfRangeInline", "([Ljava/lang/Object;II)[Ljava/lang/Object;", "copyOfRangeImpl", "elementAt", DJIFlurryReport.NativeExplore.V2_EXPLORE_SMALLBANNER_SUBKEY_INDEX, "([Ljava/lang/Object;I)Ljava/lang/Object;", "fill", "", "([Ljava/lang/Object;Ljava/lang/Object;II)V", "filterIsInstance", "R", "klass", "Ljava/lang/Class;", "([Ljava/lang/Object;Ljava/lang/Class;)Ljava/util/List;", "filterIsInstanceTo", "C", "", "([Ljava/lang/Object;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "plus", "([Ljava/lang/Object;Ljava/lang/Object;)[Ljava/lang/Object;", "elements", "([Ljava/lang/Object;[Ljava/lang/Object;)[Ljava/lang/Object;", "", "([Ljava/lang/Object;Ljava/util/Collection;)[Ljava/lang/Object;", "plusElement", "sort", "([Ljava/lang/Object;)V", "", "([Ljava/lang/Comparable;)V", "([Ljava/lang/Object;II)V", "sortWith", "([Ljava/lang/Object;Ljava/util/Comparator;)V", "([Ljava/lang/Object;Ljava/util/Comparator;II)V", "toSortedSet", "Ljava/util/SortedSet;", "([Ljava/lang/Comparable;)Ljava/util/SortedSet;", "([Ljava/lang/Object;Ljava/util/Comparator;)Ljava/util/SortedSet;", "toTypedArray", "([Z)[Ljava/lang/Boolean;", "([B)[Ljava/lang/Byte;", "([C)[Ljava/lang/Character;", "([D)[Ljava/lang/Double;", "([F)[Ljava/lang/Float;", "([I)[Ljava/lang/Integer;", "([J)[Ljava/lang/Long;", "([S)[Ljava/lang/Short;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 15}, xi = 1, xs = "kotlin/collections/ArraysKt")
/* compiled from: _ArraysJvm.kt */
class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt {
    @InlineOnly
    private static final <T> T elementAt(@NotNull T[] $this$elementAt, int index) {
        return $this$elementAt[index];
    }

    @InlineOnly
    private static final byte elementAt(@NotNull byte[] $this$elementAt, int index) {
        return $this$elementAt[index];
    }

    @InlineOnly
    private static final short elementAt(@NotNull short[] $this$elementAt, int index) {
        return $this$elementAt[index];
    }

    @InlineOnly
    private static final int elementAt(@NotNull int[] $this$elementAt, int index) {
        return $this$elementAt[index];
    }

    @InlineOnly
    private static final long elementAt(@NotNull long[] $this$elementAt, int index) {
        return $this$elementAt[index];
    }

    @InlineOnly
    private static final float elementAt(@NotNull float[] $this$elementAt, int index) {
        return $this$elementAt[index];
    }

    @InlineOnly
    private static final double elementAt(@NotNull double[] $this$elementAt, int index) {
        return $this$elementAt[index];
    }

    @InlineOnly
    private static final boolean elementAt(@NotNull boolean[] $this$elementAt, int index) {
        return $this$elementAt[index];
    }

    @InlineOnly
    private static final char elementAt(@NotNull char[] $this$elementAt, int index) {
        return $this$elementAt[index];
    }

    @NotNull
    public static final <R> List<R> filterIsInstance(@NotNull Object[] $this$filterIsInstance, @NotNull Class<R> klass) {
        Intrinsics.checkParameterIsNotNull($this$filterIsInstance, "$this$filterIsInstance");
        Intrinsics.checkParameterIsNotNull(klass, "klass");
        return (List) ArraysKt.filterIsInstanceTo($this$filterIsInstance, new ArrayList(), klass);
    }

    @NotNull
    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(@NotNull Object[] $this$filterIsInstanceTo, @NotNull C destination, @NotNull Class<R> klass) {
        Intrinsics.checkParameterIsNotNull($this$filterIsInstanceTo, "$this$filterIsInstanceTo");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(klass, "klass");
        for (Object element : $this$filterIsInstanceTo) {
            if (klass.isInstance(element)) {
                destination.add(element);
            }
        }
        return destination;
    }

    @NotNull
    public static final <T> List<T> asList(@NotNull Object[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        List<T> asList = ArraysUtilJVM.asList($this$asList);
        Intrinsics.checkExpressionValueIsNotNull(asList, "ArraysUtilJVM.asList(this)");
        return asList;
    }

    @NotNull
    public static final List<Byte> asList(@NotNull byte[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new ArraysKt___ArraysJvmKt$asList$1($this$asList);
    }

    @NotNull
    public static final List<Short> asList(@NotNull short[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new ArraysKt___ArraysJvmKt$asList$2($this$asList);
    }

    @NotNull
    public static final List<Integer> asList(@NotNull int[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new ArraysKt___ArraysJvmKt$asList$3($this$asList);
    }

    @NotNull
    public static final List<Long> asList(@NotNull long[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new ArraysKt___ArraysJvmKt$asList$4($this$asList);
    }

    @NotNull
    public static final List<Float> asList(@NotNull float[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new ArraysKt___ArraysJvmKt$asList$5($this$asList);
    }

    @NotNull
    public static final List<Double> asList(@NotNull double[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new ArraysKt___ArraysJvmKt$asList$6($this$asList);
    }

    @NotNull
    public static final List<Boolean> asList(@NotNull boolean[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new ArraysKt___ArraysJvmKt$asList$7($this$asList);
    }

    @NotNull
    public static final List<Character> asList(@NotNull char[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new ArraysKt___ArraysJvmKt$asList$8($this$asList);
    }

    public static /* synthetic */ int binarySearch$default(Object[] objArr, Object obj, Comparator comparator, int i, int i2, int i3, Object obj2) {
        if ((i3 & 4) != 0) {
            i = 0;
        }
        if ((i3 & 8) != 0) {
            i2 = objArr.length;
        }
        return ArraysKt.binarySearch(objArr, obj, comparator, i, i2);
    }

    public static final <T> int binarySearch(@NotNull T[] $this$binarySearch, T element, @NotNull Comparator<? super T> comparator, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return Arrays.binarySearch($this$binarySearch, fromIndex, toIndex, element, comparator);
    }

    public static /* synthetic */ int binarySearch$default(Object[] objArr, Object obj, int i, int i2, int i3, Object obj2) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        return ArraysKt.binarySearch(objArr, obj, i, i2);
    }

    public static final <T> int binarySearch(@NotNull T[] $this$binarySearch, T element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        return Arrays.binarySearch($this$binarySearch, fromIndex, toIndex, element);
    }

    public static /* synthetic */ int binarySearch$default(byte[] bArr, byte b, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        return ArraysKt.binarySearch(bArr, b, i, i2);
    }

    public static final int binarySearch(@NotNull byte[] $this$binarySearch, byte element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        return Arrays.binarySearch($this$binarySearch, fromIndex, toIndex, element);
    }

    public static /* synthetic */ int binarySearch$default(short[] sArr, short s, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = sArr.length;
        }
        return ArraysKt.binarySearch(sArr, s, i, i2);
    }

    public static final int binarySearch(@NotNull short[] $this$binarySearch, short element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        return Arrays.binarySearch($this$binarySearch, fromIndex, toIndex, element);
    }

    public static /* synthetic */ int binarySearch$default(int[] iArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = iArr.length;
        }
        return ArraysKt.binarySearch(iArr, i, i2, i3);
    }

    public static final int binarySearch(@NotNull int[] $this$binarySearch, int element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        return Arrays.binarySearch($this$binarySearch, fromIndex, toIndex, element);
    }

    public static /* synthetic */ int binarySearch$default(long[] jArr, long j, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = jArr.length;
        }
        return ArraysKt.binarySearch(jArr, j, i, i2);
    }

    public static final int binarySearch(@NotNull long[] $this$binarySearch, long element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        return Arrays.binarySearch($this$binarySearch, fromIndex, toIndex, element);
    }

    public static /* synthetic */ int binarySearch$default(float[] fArr, float f, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = fArr.length;
        }
        return ArraysKt.binarySearch(fArr, f, i, i2);
    }

    public static final int binarySearch(@NotNull float[] $this$binarySearch, float element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        return Arrays.binarySearch($this$binarySearch, fromIndex, toIndex, element);
    }

    public static /* synthetic */ int binarySearch$default(double[] dArr, double d, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = dArr.length;
        }
        return ArraysKt.binarySearch(dArr, d, i, i2);
    }

    public static final int binarySearch(@NotNull double[] $this$binarySearch, double element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        return Arrays.binarySearch($this$binarySearch, fromIndex, toIndex, element);
    }

    public static /* synthetic */ int binarySearch$default(char[] cArr, char c, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = cArr.length;
        }
        return ArraysKt.binarySearch(cArr, c, i, i2);
    }

    public static final int binarySearch(@NotNull char[] $this$binarySearch, char element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        return Arrays.binarySearch($this$binarySearch, fromIndex, toIndex, element);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    @JvmName(name = "contentDeepEqualsInline")
    private static final <T> boolean contentDeepEqualsInline(@NotNull T[] $this$contentDeepEquals, T[] other) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.contentDeepEquals($this$contentDeepEquals, other);
        }
        return Arrays.deepEquals($this$contentDeepEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    @JvmName(name = "contentDeepHashCodeInline")
    private static final <T> int contentDeepHashCodeInline(@NotNull T[] $this$contentDeepHashCode) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.contentDeepHashCode($this$contentDeepHashCode);
        }
        return Arrays.deepHashCode($this$contentDeepHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    @JvmName(name = "contentDeepToStringInline")
    private static final <T> String contentDeepToStringInline(@NotNull T[] $this$contentDeepToString) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.contentDeepToString($this$contentDeepToString);
        }
        String deepToString = Arrays.deepToString($this$contentDeepToString);
        Intrinsics.checkExpressionValueIsNotNull(deepToString, "java.util.Arrays.deepToString(this)");
        return deepToString;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> boolean contentEquals(@NotNull T[] $this$contentEquals, T[] other) {
        return Arrays.equals($this$contentEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull byte[] $this$contentEquals, byte[] other) {
        return Arrays.equals($this$contentEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull short[] $this$contentEquals, short[] other) {
        return Arrays.equals($this$contentEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull int[] $this$contentEquals, int[] other) {
        return Arrays.equals($this$contentEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull long[] $this$contentEquals, long[] other) {
        return Arrays.equals($this$contentEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull float[] $this$contentEquals, float[] other) {
        return Arrays.equals($this$contentEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull double[] $this$contentEquals, double[] other) {
        return Arrays.equals($this$contentEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull boolean[] $this$contentEquals, boolean[] other) {
        return Arrays.equals($this$contentEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull char[] $this$contentEquals, char[] other) {
        return Arrays.equals($this$contentEquals, other);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> int contentHashCode(@NotNull T[] $this$contentHashCode) {
        return Arrays.hashCode($this$contentHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull byte[] $this$contentHashCode) {
        return Arrays.hashCode($this$contentHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull short[] $this$contentHashCode) {
        return Arrays.hashCode($this$contentHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull int[] $this$contentHashCode) {
        return Arrays.hashCode($this$contentHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull long[] $this$contentHashCode) {
        return Arrays.hashCode($this$contentHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull float[] $this$contentHashCode) {
        return Arrays.hashCode($this$contentHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull double[] $this$contentHashCode) {
        return Arrays.hashCode($this$contentHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull boolean[] $this$contentHashCode) {
        return Arrays.hashCode($this$contentHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull char[] $this$contentHashCode) {
        return Arrays.hashCode($this$contentHashCode);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> String contentToString(@NotNull T[] $this$contentToString) {
        String arrays = Arrays.toString($this$contentToString);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull byte[] $this$contentToString) {
        String arrays = Arrays.toString($this$contentToString);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull short[] $this$contentToString) {
        String arrays = Arrays.toString($this$contentToString);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull int[] $this$contentToString) {
        String arrays = Arrays.toString($this$contentToString);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull long[] $this$contentToString) {
        String arrays = Arrays.toString($this$contentToString);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull float[] $this$contentToString) {
        String arrays = Arrays.toString($this$contentToString);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull double[] $this$contentToString) {
        String arrays = Arrays.toString($this$contentToString);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull boolean[] $this$contentToString) {
        String arrays = Arrays.toString($this$contentToString);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull char[] $this$contentToString) {
        String arrays = Arrays.toString($this$contentToString);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    public static /* synthetic */ Object[] copyInto$default(Object[] objArr, Object[] objArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = objArr.length;
        }
        return ArraysKt.copyInto(objArr, objArr2, i, i2, i3);
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final <T> T[] copyInto(@NotNull T[] $this$copyInto, @NotNull T[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyInto, "$this$copyInto");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        System.arraycopy($this$copyInto, startIndex, destination, destinationOffset, endIndex - startIndex);
        return destination;
    }

    public static /* synthetic */ byte[] copyInto$default(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = bArr.length;
        }
        return ArraysKt.copyInto(bArr, bArr2, i, i2, i3);
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final byte[] copyInto(@NotNull byte[] $this$copyInto, @NotNull byte[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyInto, "$this$copyInto");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        System.arraycopy($this$copyInto, startIndex, destination, destinationOffset, endIndex - startIndex);
        return destination;
    }

    public static /* synthetic */ short[] copyInto$default(short[] sArr, short[] sArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = sArr.length;
        }
        return ArraysKt.copyInto(sArr, sArr2, i, i2, i3);
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final short[] copyInto(@NotNull short[] $this$copyInto, @NotNull short[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyInto, "$this$copyInto");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        System.arraycopy($this$copyInto, startIndex, destination, destinationOffset, endIndex - startIndex);
        return destination;
    }

    public static /* synthetic */ int[] copyInto$default(int[] iArr, int[] iArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = iArr.length;
        }
        return ArraysKt.copyInto(iArr, iArr2, i, i2, i3);
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final int[] copyInto(@NotNull int[] $this$copyInto, @NotNull int[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyInto, "$this$copyInto");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        System.arraycopy($this$copyInto, startIndex, destination, destinationOffset, endIndex - startIndex);
        return destination;
    }

    public static /* synthetic */ long[] copyInto$default(long[] jArr, long[] jArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = jArr.length;
        }
        return ArraysKt.copyInto(jArr, jArr2, i, i2, i3);
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final long[] copyInto(@NotNull long[] $this$copyInto, @NotNull long[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyInto, "$this$copyInto");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        System.arraycopy($this$copyInto, startIndex, destination, destinationOffset, endIndex - startIndex);
        return destination;
    }

    public static /* synthetic */ float[] copyInto$default(float[] fArr, float[] fArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = fArr.length;
        }
        return ArraysKt.copyInto(fArr, fArr2, i, i2, i3);
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final float[] copyInto(@NotNull float[] $this$copyInto, @NotNull float[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyInto, "$this$copyInto");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        System.arraycopy($this$copyInto, startIndex, destination, destinationOffset, endIndex - startIndex);
        return destination;
    }

    public static /* synthetic */ double[] copyInto$default(double[] dArr, double[] dArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = dArr.length;
        }
        return ArraysKt.copyInto(dArr, dArr2, i, i2, i3);
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final double[] copyInto(@NotNull double[] $this$copyInto, @NotNull double[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyInto, "$this$copyInto");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        System.arraycopy($this$copyInto, startIndex, destination, destinationOffset, endIndex - startIndex);
        return destination;
    }

    public static /* synthetic */ boolean[] copyInto$default(boolean[] zArr, boolean[] zArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = zArr.length;
        }
        return ArraysKt.copyInto(zArr, zArr2, i, i2, i3);
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final boolean[] copyInto(@NotNull boolean[] $this$copyInto, @NotNull boolean[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyInto, "$this$copyInto");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        System.arraycopy($this$copyInto, startIndex, destination, destinationOffset, endIndex - startIndex);
        return destination;
    }

    public static /* synthetic */ char[] copyInto$default(char[] cArr, char[] cArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = cArr.length;
        }
        return ArraysKt.copyInto(cArr, cArr2, i, i2, i3);
    }

    @NotNull
    @SinceKotlin(version = "1.3")
    public static final char[] copyInto(@NotNull char[] $this$copyInto, @NotNull char[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyInto, "$this$copyInto");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        System.arraycopy($this$copyInto, startIndex, destination, destinationOffset, endIndex - startIndex);
        return destination;
    }

    @InlineOnly
    private static final <T> T[] copyOf(@NotNull T[] $this$copyOf) {
        T[] copyOf = Arrays.copyOf($this$copyOf, $this$copyOf.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final byte[] copyOf(@NotNull byte[] $this$copyOf) {
        byte[] copyOf = Arrays.copyOf($this$copyOf, $this$copyOf.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final short[] copyOf(@NotNull short[] $this$copyOf) {
        short[] copyOf = Arrays.copyOf($this$copyOf, $this$copyOf.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final int[] copyOf(@NotNull int[] $this$copyOf) {
        int[] copyOf = Arrays.copyOf($this$copyOf, $this$copyOf.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final long[] copyOf(@NotNull long[] $this$copyOf) {
        long[] copyOf = Arrays.copyOf($this$copyOf, $this$copyOf.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final float[] copyOf(@NotNull float[] $this$copyOf) {
        float[] copyOf = Arrays.copyOf($this$copyOf, $this$copyOf.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final double[] copyOf(@NotNull double[] $this$copyOf) {
        double[] copyOf = Arrays.copyOf($this$copyOf, $this$copyOf.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final boolean[] copyOf(@NotNull boolean[] $this$copyOf) {
        boolean[] copyOf = Arrays.copyOf($this$copyOf, $this$copyOf.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final char[] copyOf(@NotNull char[] $this$copyOf) {
        char[] copyOf = Arrays.copyOf($this$copyOf, $this$copyOf.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final byte[] copyOf(@NotNull byte[] $this$copyOf, int newSize) {
        byte[] copyOf = Arrays.copyOf($this$copyOf, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final short[] copyOf(@NotNull short[] $this$copyOf, int newSize) {
        short[] copyOf = Arrays.copyOf($this$copyOf, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final int[] copyOf(@NotNull int[] $this$copyOf, int newSize) {
        int[] copyOf = Arrays.copyOf($this$copyOf, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final long[] copyOf(@NotNull long[] $this$copyOf, int newSize) {
        long[] copyOf = Arrays.copyOf($this$copyOf, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final float[] copyOf(@NotNull float[] $this$copyOf, int newSize) {
        float[] copyOf = Arrays.copyOf($this$copyOf, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final double[] copyOf(@NotNull double[] $this$copyOf, int newSize) {
        double[] copyOf = Arrays.copyOf($this$copyOf, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final boolean[] copyOf(@NotNull boolean[] $this$copyOf, int newSize) {
        boolean[] copyOf = Arrays.copyOf($this$copyOf, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final char[] copyOf(@NotNull char[] $this$copyOf, int newSize) {
        char[] copyOf = Arrays.copyOf($this$copyOf, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final <T> T[] copyOf(@NotNull T[] $this$copyOf, int newSize) {
        T[] copyOf = Arrays.copyOf($this$copyOf, newSize);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    @JvmName(name = "copyOfRangeInline")
    private static final <T> T[] copyOfRangeInline(@NotNull T[] $this$copyOfRange, int fromIndex, int toIndex) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        }
        if (toIndex > $this$copyOfRange.length) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $this$copyOfRange.length);
        }
        T[] copyOfRange = Arrays.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    @JvmName(name = "copyOfRangeInline")
    private static final byte[] copyOfRangeInline(@NotNull byte[] $this$copyOfRange, int fromIndex, int toIndex) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        }
        if (toIndex > $this$copyOfRange.length) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $this$copyOfRange.length);
        }
        byte[] copyOfRange = Arrays.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    @JvmName(name = "copyOfRangeInline")
    private static final short[] copyOfRangeInline(@NotNull short[] $this$copyOfRange, int fromIndex, int toIndex) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        }
        if (toIndex > $this$copyOfRange.length) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $this$copyOfRange.length);
        }
        short[] copyOfRange = Arrays.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    @JvmName(name = "copyOfRangeInline")
    private static final int[] copyOfRangeInline(@NotNull int[] $this$copyOfRange, int fromIndex, int toIndex) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        }
        if (toIndex > $this$copyOfRange.length) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $this$copyOfRange.length);
        }
        int[] copyOfRange = Arrays.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    @JvmName(name = "copyOfRangeInline")
    private static final long[] copyOfRangeInline(@NotNull long[] $this$copyOfRange, int fromIndex, int toIndex) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        }
        if (toIndex > $this$copyOfRange.length) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $this$copyOfRange.length);
        }
        long[] copyOfRange = Arrays.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    @JvmName(name = "copyOfRangeInline")
    private static final float[] copyOfRangeInline(@NotNull float[] $this$copyOfRange, int fromIndex, int toIndex) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        }
        if (toIndex > $this$copyOfRange.length) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $this$copyOfRange.length);
        }
        float[] copyOfRange = Arrays.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    @JvmName(name = "copyOfRangeInline")
    private static final double[] copyOfRangeInline(@NotNull double[] $this$copyOfRange, int fromIndex, int toIndex) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        }
        if (toIndex > $this$copyOfRange.length) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $this$copyOfRange.length);
        }
        double[] copyOfRange = Arrays.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    @JvmName(name = "copyOfRangeInline")
    private static final boolean[] copyOfRangeInline(@NotNull boolean[] $this$copyOfRange, int fromIndex, int toIndex) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        }
        if (toIndex > $this$copyOfRange.length) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $this$copyOfRange.length);
        }
        boolean[] copyOfRange = Arrays.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    @JvmName(name = "copyOfRangeInline")
    private static final char[] copyOfRangeInline(@NotNull char[] $this$copyOfRange, int fromIndex, int toIndex) {
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
            return ArraysKt.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        }
        if (toIndex > $this$copyOfRange.length) {
            throw new IndexOutOfBoundsException("toIndex: " + toIndex + ", size: " + $this$copyOfRange.length);
        }
        char[] copyOfRange = Arrays.copyOfRange($this$copyOfRange, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    @NotNull
    @JvmName(name = "copyOfRange")
    public static final <T> T[] copyOfRange(@NotNull Object[] $this$copyOfRangeImpl, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt.copyOfRangeToIndexCheck(toIndex, $this$copyOfRangeImpl.length);
        T[] copyOfRange = Arrays.copyOfRange($this$copyOfRangeImpl, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    @NotNull
    @JvmName(name = "copyOfRange")
    public static final byte[] copyOfRange(@NotNull byte[] $this$copyOfRangeImpl, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt.copyOfRangeToIndexCheck(toIndex, $this$copyOfRangeImpl.length);
        byte[] copyOfRange = Arrays.copyOfRange($this$copyOfRangeImpl, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    @NotNull
    @JvmName(name = "copyOfRange")
    public static final short[] copyOfRange(@NotNull short[] $this$copyOfRangeImpl, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt.copyOfRangeToIndexCheck(toIndex, $this$copyOfRangeImpl.length);
        short[] copyOfRange = Arrays.copyOfRange($this$copyOfRangeImpl, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    @NotNull
    @JvmName(name = "copyOfRange")
    public static final int[] copyOfRange(@NotNull int[] $this$copyOfRangeImpl, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt.copyOfRangeToIndexCheck(toIndex, $this$copyOfRangeImpl.length);
        int[] copyOfRange = Arrays.copyOfRange($this$copyOfRangeImpl, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    @NotNull
    @JvmName(name = "copyOfRange")
    public static final long[] copyOfRange(@NotNull long[] $this$copyOfRangeImpl, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt.copyOfRangeToIndexCheck(toIndex, $this$copyOfRangeImpl.length);
        long[] copyOfRange = Arrays.copyOfRange($this$copyOfRangeImpl, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    @NotNull
    @JvmName(name = "copyOfRange")
    public static final float[] copyOfRange(@NotNull float[] $this$copyOfRangeImpl, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt.copyOfRangeToIndexCheck(toIndex, $this$copyOfRangeImpl.length);
        float[] copyOfRange = Arrays.copyOfRange($this$copyOfRangeImpl, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    @NotNull
    @JvmName(name = "copyOfRange")
    public static final double[] copyOfRange(@NotNull double[] $this$copyOfRangeImpl, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt.copyOfRangeToIndexCheck(toIndex, $this$copyOfRangeImpl.length);
        double[] copyOfRange = Arrays.copyOfRange($this$copyOfRangeImpl, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    @NotNull
    @JvmName(name = "copyOfRange")
    public static final boolean[] copyOfRange(@NotNull boolean[] $this$copyOfRangeImpl, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt.copyOfRangeToIndexCheck(toIndex, $this$copyOfRangeImpl.length);
        boolean[] copyOfRange = Arrays.copyOfRange($this$copyOfRangeImpl, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @SinceKotlin(version = "1.3")
    @PublishedApi
    @NotNull
    @JvmName(name = "copyOfRange")
    public static final char[] copyOfRange(@NotNull char[] $this$copyOfRangeImpl, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$copyOfRangeImpl, "$this$copyOfRangeImpl");
        ArraysKt.copyOfRangeToIndexCheck(toIndex, $this$copyOfRangeImpl.length);
        char[] copyOfRange = Arrays.copyOfRange($this$copyOfRangeImpl, fromIndex, toIndex);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    public static /* synthetic */ void fill$default(Object[] objArr, Object obj, int i, int i2, int i3, Object obj2) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        ArraysKt.fill(objArr, obj, i, i2);
    }

    public static final <T> void fill(@NotNull T[] $this$fill, T element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$fill, "$this$fill");
        Arrays.fill($this$fill, fromIndex, toIndex, element);
    }

    public static /* synthetic */ void fill$default(byte[] bArr, byte b, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        ArraysKt.fill(bArr, b, i, i2);
    }

    public static final void fill(@NotNull byte[] $this$fill, byte element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$fill, "$this$fill");
        Arrays.fill($this$fill, fromIndex, toIndex, element);
    }

    public static /* synthetic */ void fill$default(short[] sArr, short s, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = sArr.length;
        }
        ArraysKt.fill(sArr, s, i, i2);
    }

    public static final void fill(@NotNull short[] $this$fill, short element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$fill, "$this$fill");
        Arrays.fill($this$fill, fromIndex, toIndex, element);
    }

    public static /* synthetic */ void fill$default(int[] iArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = iArr.length;
        }
        ArraysKt.fill(iArr, i, i2, i3);
    }

    public static final void fill(@NotNull int[] $this$fill, int element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$fill, "$this$fill");
        Arrays.fill($this$fill, fromIndex, toIndex, element);
    }

    public static /* synthetic */ void fill$default(long[] jArr, long j, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = jArr.length;
        }
        ArraysKt.fill(jArr, j, i, i2);
    }

    public static final void fill(@NotNull long[] $this$fill, long element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$fill, "$this$fill");
        Arrays.fill($this$fill, fromIndex, toIndex, element);
    }

    public static /* synthetic */ void fill$default(float[] fArr, float f, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = fArr.length;
        }
        ArraysKt.fill(fArr, f, i, i2);
    }

    public static final void fill(@NotNull float[] $this$fill, float element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$fill, "$this$fill");
        Arrays.fill($this$fill, fromIndex, toIndex, element);
    }

    public static /* synthetic */ void fill$default(double[] dArr, double d, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = dArr.length;
        }
        ArraysKt.fill(dArr, d, i, i2);
    }

    public static final void fill(@NotNull double[] $this$fill, double element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$fill, "$this$fill");
        Arrays.fill($this$fill, fromIndex, toIndex, element);
    }

    public static /* synthetic */ void fill$default(boolean[] zArr, boolean z, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = zArr.length;
        }
        ArraysKt.fill(zArr, z, i, i2);
    }

    public static final void fill(@NotNull boolean[] $this$fill, boolean element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$fill, "$this$fill");
        Arrays.fill($this$fill, fromIndex, toIndex, element);
    }

    public static /* synthetic */ void fill$default(char[] cArr, char c, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = cArr.length;
        }
        ArraysKt.fill(cArr, c, i, i2);
    }

    public static final void fill(@NotNull char[] $this$fill, char element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$fill, "$this$fill");
        Arrays.fill($this$fill, fromIndex, toIndex, element);
    }

    @NotNull
    public static final <T> T[] plus(@NotNull T[] $this$plus, T element) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        int index = $this$plus.length;
        Object[] result = Arrays.copyOf($this$plus, index + 1);
        result[index] = element;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final byte[] plus(@NotNull byte[] $this$plus, byte element) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        int index = $this$plus.length;
        byte[] result = Arrays.copyOf($this$plus, index + 1);
        result[index] = element;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final short[] plus(@NotNull short[] $this$plus, short element) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        int index = $this$plus.length;
        short[] result = Arrays.copyOf($this$plus, index + 1);
        result[index] = element;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final int[] plus(@NotNull int[] $this$plus, int element) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        int index = $this$plus.length;
        int[] result = Arrays.copyOf($this$plus, index + 1);
        result[index] = element;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final long[] plus(@NotNull long[] $this$plus, long element) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        int index = $this$plus.length;
        long[] result = Arrays.copyOf($this$plus, index + 1);
        result[index] = element;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final float[] plus(@NotNull float[] $this$plus, float element) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        int index = $this$plus.length;
        float[] result = Arrays.copyOf($this$plus, index + 1);
        result[index] = element;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final double[] plus(@NotNull double[] $this$plus, double element) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        int index = $this$plus.length;
        double[] result = Arrays.copyOf($this$plus, index + 1);
        result[index] = element;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final boolean[] plus(@NotNull boolean[] $this$plus, boolean element) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        int index = $this$plus.length;
        boolean[] result = Arrays.copyOf($this$plus, index + 1);
        result[index] = element;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final char[] plus(@NotNull char[] $this$plus, char element) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        int index = $this$plus.length;
        char[] result = Arrays.copyOf($this$plus, index + 1);
        result[index] = element;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final <T> T[] plus(@NotNull T[] $this$plus, @NotNull Collection<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int index = $this$plus.length;
        Object[] result = Arrays.copyOf($this$plus, elements.size() + index);
        for (Object element : elements) {
            result[index] = element;
            index++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final byte[] plus(@NotNull byte[] $this$plus, @NotNull Collection<Byte> elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int index = $this$plus.length;
        byte[] result = Arrays.copyOf($this$plus, elements.size() + index);
        for (Byte b : elements) {
            result[index] = b.byteValue();
            index++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final short[] plus(@NotNull short[] $this$plus, @NotNull Collection<Short> elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int index = $this$plus.length;
        short[] result = Arrays.copyOf($this$plus, elements.size() + index);
        for (Short sh : elements) {
            result[index] = sh.shortValue();
            index++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final int[] plus(@NotNull int[] $this$plus, @NotNull Collection<Integer> elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int index = $this$plus.length;
        int[] result = Arrays.copyOf($this$plus, elements.size() + index);
        for (Integer num : elements) {
            result[index] = num.intValue();
            index++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final long[] plus(@NotNull long[] $this$plus, @NotNull Collection<Long> elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int index = $this$plus.length;
        long[] result = Arrays.copyOf($this$plus, elements.size() + index);
        for (Long l : elements) {
            result[index] = l.longValue();
            index++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final float[] plus(@NotNull float[] $this$plus, @NotNull Collection<Float> elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int index = $this$plus.length;
        float[] result = Arrays.copyOf($this$plus, elements.size() + index);
        for (Float f : elements) {
            result[index] = f.floatValue();
            index++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final double[] plus(@NotNull double[] $this$plus, @NotNull Collection<Double> elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int index = $this$plus.length;
        double[] result = Arrays.copyOf($this$plus, elements.size() + index);
        for (Double d : elements) {
            result[index] = d.doubleValue();
            index++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final boolean[] plus(@NotNull boolean[] $this$plus, @NotNull Collection<Boolean> elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int index = $this$plus.length;
        boolean[] result = Arrays.copyOf($this$plus, elements.size() + index);
        for (Boolean bool : elements) {
            result[index] = bool.booleanValue();
            index++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final char[] plus(@NotNull char[] $this$plus, @NotNull Collection<Character> elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int index = $this$plus.length;
        char[] result = Arrays.copyOf($this$plus, elements.size() + index);
        for (Character ch : elements) {
            result[index] = ch.charValue();
            index++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final <T> T[] plus(@NotNull T[] $this$plus, @NotNull T[] elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int thisSize = $this$plus.length;
        int arraySize = elements.length;
        Object[] result = Arrays.copyOf($this$plus, thisSize + arraySize);
        System.arraycopy(elements, 0, result, thisSize, arraySize);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final byte[] plus(@NotNull byte[] $this$plus, @NotNull byte[] elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int thisSize = $this$plus.length;
        int arraySize = elements.length;
        byte[] result = Arrays.copyOf($this$plus, thisSize + arraySize);
        System.arraycopy(elements, 0, result, thisSize, arraySize);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final short[] plus(@NotNull short[] $this$plus, @NotNull short[] elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int thisSize = $this$plus.length;
        int arraySize = elements.length;
        short[] result = Arrays.copyOf($this$plus, thisSize + arraySize);
        System.arraycopy(elements, 0, result, thisSize, arraySize);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final int[] plus(@NotNull int[] $this$plus, @NotNull int[] elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int thisSize = $this$plus.length;
        int arraySize = elements.length;
        int[] result = Arrays.copyOf($this$plus, thisSize + arraySize);
        System.arraycopy(elements, 0, result, thisSize, arraySize);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final long[] plus(@NotNull long[] $this$plus, @NotNull long[] elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int thisSize = $this$plus.length;
        int arraySize = elements.length;
        long[] result = Arrays.copyOf($this$plus, thisSize + arraySize);
        System.arraycopy(elements, 0, result, thisSize, arraySize);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final float[] plus(@NotNull float[] $this$plus, @NotNull float[] elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int thisSize = $this$plus.length;
        int arraySize = elements.length;
        float[] result = Arrays.copyOf($this$plus, thisSize + arraySize);
        System.arraycopy(elements, 0, result, thisSize, arraySize);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final double[] plus(@NotNull double[] $this$plus, @NotNull double[] elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int thisSize = $this$plus.length;
        int arraySize = elements.length;
        double[] result = Arrays.copyOf($this$plus, thisSize + arraySize);
        System.arraycopy(elements, 0, result, thisSize, arraySize);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final boolean[] plus(@NotNull boolean[] $this$plus, @NotNull boolean[] elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int thisSize = $this$plus.length;
        int arraySize = elements.length;
        boolean[] result = Arrays.copyOf($this$plus, thisSize + arraySize);
        System.arraycopy(elements, 0, result, thisSize, arraySize);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final char[] plus(@NotNull char[] $this$plus, @NotNull char[] elements) {
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int thisSize = $this$plus.length;
        int arraySize = elements.length;
        char[] result = Arrays.copyOf($this$plus, thisSize + arraySize);
        System.arraycopy(elements, 0, result, thisSize, arraySize);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.collections.ArraysKt___ArraysJvmKt.plus(java.lang.Object[], java.lang.Object):T[]
     arg types: [T[], T]
     candidates:
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(byte[], byte):byte[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(byte[], java.util.Collection<java.lang.Byte>):byte[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(byte[], byte[]):byte[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(char[], char):char[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(char[], java.util.Collection<java.lang.Character>):char[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(char[], char[]):char[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(double[], double):double[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(double[], java.util.Collection<java.lang.Double>):double[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(double[], double[]):double[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(float[], float):float[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(float[], java.util.Collection<java.lang.Float>):float[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(float[], float[]):float[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(int[], int):int[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(int[], java.util.Collection<java.lang.Integer>):int[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(int[], int[]):int[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(long[], long):long[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(long[], java.util.Collection<java.lang.Long>):long[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(long[], long[]):long[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(java.lang.Object[], java.util.Collection):T[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(java.lang.Object[], java.lang.Object[]):T[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(short[], java.util.Collection<java.lang.Short>):short[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(short[], short):short[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(short[], short[]):short[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(boolean[], java.util.Collection<java.lang.Boolean>):boolean[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(boolean[], boolean):boolean[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(boolean[], boolean[]):boolean[]
      kotlin.collections.ArraysKt___ArraysJvmKt.plus(java.lang.Object[], java.lang.Object):T[] */
    @InlineOnly
    private static final <T> T[] plusElement(@NotNull T[] $this$plusElement, T element) {
        return ArraysKt.plus((Object[]) $this$plusElement, (Object) element);
    }

    public static final void sort(@NotNull int[] $this$sort) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        if ($this$sort.length > 1) {
            Arrays.sort($this$sort);
        }
    }

    public static final void sort(@NotNull long[] $this$sort) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        if ($this$sort.length > 1) {
            Arrays.sort($this$sort);
        }
    }

    public static final void sort(@NotNull byte[] $this$sort) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        if ($this$sort.length > 1) {
            Arrays.sort($this$sort);
        }
    }

    public static final void sort(@NotNull short[] $this$sort) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        if ($this$sort.length > 1) {
            Arrays.sort($this$sort);
        }
    }

    public static final void sort(@NotNull double[] $this$sort) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        if ($this$sort.length > 1) {
            Arrays.sort($this$sort);
        }
    }

    public static final void sort(@NotNull float[] $this$sort) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        if ($this$sort.length > 1) {
            Arrays.sort($this$sort);
        }
    }

    public static final void sort(@NotNull char[] $this$sort) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        if ($this$sort.length > 1) {
            Arrays.sort($this$sort);
        }
    }

    @InlineOnly
    private static final <T extends Comparable<? super T>> void sort(@NotNull Comparable[] $this$sort) {
        if ($this$sort == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
        }
        ArraysKt.sort((Object[]) $this$sort);
    }

    public static final <T> void sort(@NotNull Object[] $this$sort) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        if ($this$sort.length > 1) {
            Arrays.sort($this$sort);
        }
    }

    public static /* synthetic */ void sort$default(Object[] objArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = objArr.length;
        }
        ArraysKt.sort(objArr, i, i2);
    }

    public static final <T> void sort(@NotNull T[] $this$sort, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        Arrays.sort($this$sort, fromIndex, toIndex);
    }

    public static /* synthetic */ void sort$default(byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        ArraysKt.sort(bArr, i, i2);
    }

    public static final void sort(@NotNull byte[] $this$sort, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        Arrays.sort($this$sort, fromIndex, toIndex);
    }

    public static /* synthetic */ void sort$default(short[] sArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = sArr.length;
        }
        ArraysKt.sort(sArr, i, i2);
    }

    public static final void sort(@NotNull short[] $this$sort, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        Arrays.sort($this$sort, fromIndex, toIndex);
    }

    public static /* synthetic */ void sort$default(int[] iArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = iArr.length;
        }
        ArraysKt.sort(iArr, i, i2);
    }

    public static final void sort(@NotNull int[] $this$sort, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        Arrays.sort($this$sort, fromIndex, toIndex);
    }

    public static /* synthetic */ void sort$default(long[] jArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = jArr.length;
        }
        ArraysKt.sort(jArr, i, i2);
    }

    public static final void sort(@NotNull long[] $this$sort, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        Arrays.sort($this$sort, fromIndex, toIndex);
    }

    public static /* synthetic */ void sort$default(float[] fArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = fArr.length;
        }
        ArraysKt.sort(fArr, i, i2);
    }

    public static final void sort(@NotNull float[] $this$sort, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        Arrays.sort($this$sort, fromIndex, toIndex);
    }

    public static /* synthetic */ void sort$default(double[] dArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = dArr.length;
        }
        ArraysKt.sort(dArr, i, i2);
    }

    public static final void sort(@NotNull double[] $this$sort, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        Arrays.sort($this$sort, fromIndex, toIndex);
    }

    public static /* synthetic */ void sort$default(char[] cArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = cArr.length;
        }
        ArraysKt.sort(cArr, i, i2);
    }

    public static final void sort(@NotNull char[] $this$sort, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$sort, "$this$sort");
        Arrays.sort($this$sort, fromIndex, toIndex);
    }

    public static final <T> void sortWith(@NotNull T[] $this$sortWith, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull($this$sortWith, "$this$sortWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        if ($this$sortWith.length > 1) {
            Arrays.sort($this$sortWith, comparator);
        }
    }

    public static /* synthetic */ void sortWith$default(Object[] objArr, Comparator comparator, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        ArraysKt.sortWith(objArr, comparator, i, i2);
    }

    public static final <T> void sortWith(@NotNull T[] $this$sortWith, @NotNull Comparator<? super T> comparator, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$sortWith, "$this$sortWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        Arrays.sort($this$sortWith, fromIndex, toIndex, comparator);
    }

    @NotNull
    public static final Byte[] toTypedArray(@NotNull byte[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        Byte[] result = new Byte[$this$toTypedArray.length];
        int length = $this$toTypedArray.length;
        for (int i = 0; i < length; i++) {
            result[i] = Byte.valueOf($this$toTypedArray[i]);
        }
        return result;
    }

    @NotNull
    public static final Short[] toTypedArray(@NotNull short[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        Short[] result = new Short[$this$toTypedArray.length];
        int length = $this$toTypedArray.length;
        for (int i = 0; i < length; i++) {
            result[i] = Short.valueOf($this$toTypedArray[i]);
        }
        return result;
    }

    @NotNull
    public static final Integer[] toTypedArray(@NotNull int[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        Integer[] result = new Integer[$this$toTypedArray.length];
        int length = $this$toTypedArray.length;
        for (int i = 0; i < length; i++) {
            result[i] = Integer.valueOf($this$toTypedArray[i]);
        }
        return result;
    }

    @NotNull
    public static final Long[] toTypedArray(@NotNull long[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        Long[] result = new Long[$this$toTypedArray.length];
        int length = $this$toTypedArray.length;
        for (int i = 0; i < length; i++) {
            result[i] = Long.valueOf($this$toTypedArray[i]);
        }
        return result;
    }

    @NotNull
    public static final Float[] toTypedArray(@NotNull float[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        Float[] result = new Float[$this$toTypedArray.length];
        int length = $this$toTypedArray.length;
        for (int i = 0; i < length; i++) {
            result[i] = Float.valueOf($this$toTypedArray[i]);
        }
        return result;
    }

    @NotNull
    public static final Double[] toTypedArray(@NotNull double[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        Double[] result = new Double[$this$toTypedArray.length];
        int length = $this$toTypedArray.length;
        for (int i = 0; i < length; i++) {
            result[i] = Double.valueOf($this$toTypedArray[i]);
        }
        return result;
    }

    @NotNull
    public static final Boolean[] toTypedArray(@NotNull boolean[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        Boolean[] result = new Boolean[$this$toTypedArray.length];
        int length = $this$toTypedArray.length;
        for (int i = 0; i < length; i++) {
            result[i] = Boolean.valueOf($this$toTypedArray[i]);
        }
        return result;
    }

    @NotNull
    public static final Character[] toTypedArray(@NotNull char[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        Character[] result = new Character[$this$toTypedArray.length];
        int length = $this$toTypedArray.length;
        for (int i = 0; i < length; i++) {
            result[i] = Character.valueOf($this$toTypedArray[i]);
        }
        return result;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(@NotNull T[] $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, new TreeSet());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.collections.ArraysKt___ArraysKt.toCollection(byte[], java.util.Collection):C
     arg types: [byte[], java.util.TreeSet]
     candidates:
      kotlin.collections.ArraysKt___ArraysKt.toCollection(char[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(double[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(float[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(int[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(long[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(java.lang.Object[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(short[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(boolean[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(byte[], java.util.Collection):C */
    @NotNull
    public static final SortedSet<Byte> toSortedSet(@NotNull byte[] $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, (Collection) new TreeSet());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.collections.ArraysKt___ArraysKt.toCollection(short[], java.util.Collection):C
     arg types: [short[], java.util.TreeSet]
     candidates:
      kotlin.collections.ArraysKt___ArraysKt.toCollection(byte[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(char[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(double[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(float[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(int[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(long[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(java.lang.Object[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(boolean[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(short[], java.util.Collection):C */
    @NotNull
    public static final SortedSet<Short> toSortedSet(@NotNull short[] $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, (Collection) new TreeSet());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.collections.ArraysKt___ArraysKt.toCollection(int[], java.util.Collection):C
     arg types: [int[], java.util.TreeSet]
     candidates:
      kotlin.collections.ArraysKt___ArraysKt.toCollection(byte[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(char[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(double[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(float[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(long[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(java.lang.Object[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(short[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(boolean[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(int[], java.util.Collection):C */
    @NotNull
    public static final SortedSet<Integer> toSortedSet(@NotNull int[] $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, (Collection) new TreeSet());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.collections.ArraysKt___ArraysKt.toCollection(long[], java.util.Collection):C
     arg types: [long[], java.util.TreeSet]
     candidates:
      kotlin.collections.ArraysKt___ArraysKt.toCollection(byte[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(char[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(double[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(float[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(int[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(java.lang.Object[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(short[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(boolean[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(long[], java.util.Collection):C */
    @NotNull
    public static final SortedSet<Long> toSortedSet(@NotNull long[] $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, (Collection) new TreeSet());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.collections.ArraysKt___ArraysKt.toCollection(float[], java.util.Collection):C
     arg types: [float[], java.util.TreeSet]
     candidates:
      kotlin.collections.ArraysKt___ArraysKt.toCollection(byte[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(char[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(double[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(int[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(long[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(java.lang.Object[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(short[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(boolean[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(float[], java.util.Collection):C */
    @NotNull
    public static final SortedSet<Float> toSortedSet(@NotNull float[] $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, (Collection) new TreeSet());
    }

    @NotNull
    public static final SortedSet<Double> toSortedSet(@NotNull double[] $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, new TreeSet());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.collections.ArraysKt___ArraysKt.toCollection(boolean[], java.util.Collection):C
     arg types: [boolean[], java.util.TreeSet]
     candidates:
      kotlin.collections.ArraysKt___ArraysKt.toCollection(byte[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(char[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(double[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(float[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(int[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(long[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(java.lang.Object[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(short[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(boolean[], java.util.Collection):C */
    @NotNull
    public static final SortedSet<Boolean> toSortedSet(@NotNull boolean[] $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, (Collection) new TreeSet());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.collections.ArraysKt___ArraysKt.toCollection(char[], java.util.Collection):C
     arg types: [char[], java.util.TreeSet]
     candidates:
      kotlin.collections.ArraysKt___ArraysKt.toCollection(byte[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(double[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(float[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(int[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(long[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(java.lang.Object[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(short[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(boolean[], java.util.Collection):C
      kotlin.collections.ArraysKt___ArraysKt.toCollection(char[], java.util.Collection):C */
    @NotNull
    public static final SortedSet<Character> toSortedSet(@NotNull char[] $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, (Collection) new TreeSet());
    }

    @NotNull
    public static final <T> SortedSet<T> toSortedSet(@NotNull T[] $this$toSortedSet, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return (SortedSet) ArraysKt.toCollection($this$toSortedSet, new TreeSet(comparator));
    }
}
