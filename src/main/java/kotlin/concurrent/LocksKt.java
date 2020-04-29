package kotlin.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a&\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\b¢\u0006\u0002\u0010\u0005\u001a&\u0010\u0006\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00072\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\b¢\u0006\u0002\u0010\b\u001a&\u0010\t\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\b¢\u0006\u0002\u0010\u0005¨\u0006\n"}, d2 = {"read", "T", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "action", "Lkotlin/Function0;", "(Ljava/util/concurrent/locks/ReentrantReadWriteLock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "withLock", "Ljava/util/concurrent/locks/Lock;", "(Ljava/util/concurrent/locks/Lock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "write", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
@JvmName(name = "LocksKt")
/* compiled from: Locks.kt */
public final class LocksKt {
    @InlineOnly
    private static final <T> T withLock(@NotNull Lock $this$withLock, Function0<? extends T> action) {
        $this$withLock.lock();
        try {
            return action.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            $this$withLock.unlock();
            InlineMarker.finallyEnd(1);
        }
    }

    @InlineOnly
    private static final <T> T read(@NotNull ReentrantReadWriteLock $this$read, Function0<? extends T> action) {
        ReentrantReadWriteLock.ReadLock rl = $this$read.readLock();
        rl.lock();
        try {
            return action.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            rl.unlock();
            InlineMarker.finallyEnd(1);
        }
    }

    /*  JADX ERROR: StackOverflowError in pass: MarkFinallyVisitor
        java.lang.StackOverflowError
        	at jadx.core.dex.nodes.InsnNode.isSame(InsnNode.java:294)
        	at jadx.core.dex.instructions.IfNode.isSame(IfNode.java:123)
        	at jadx.core.dex.visitors.MarkFinallyVisitor.sameInsns(MarkFinallyVisitor.java:451)
        	at jadx.core.dex.visitors.MarkFinallyVisitor.compareBlocks(MarkFinallyVisitor.java:436)
        	at jadx.core.dex.visitors.MarkFinallyVisitor.checkBlocksTree(MarkFinallyVisitor.java:408)
        	at jadx.core.dex.visitors.MarkFinallyVisitor.checkBlocksTree(MarkFinallyVisitor.java:411)
        */
    @kotlin.internal.InlineOnly
    private static final <T> T write(@org.jetbrains.annotations.NotNull java.util.concurrent.locks.ReentrantReadWriteLock r10, kotlin.jvm.functions.Function0<? extends T> r11) {
        /*
            r9 = 1
            r7 = 0
            r2 = 0
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r5 = r10.readLock()
            int r8 = r10.getWriteHoldCount()
            if (r8 != 0) goto L_0x001b
            int r4 = r10.getReadHoldCount()
        L_0x0011:
            r3 = r7
        L_0x0012:
            if (r3 >= r4) goto L_0x001d
            r0 = 0
            r5.unlock()
            int r3 = r3 + 1
            goto L_0x0012
        L_0x001b:
            r4 = r7
            goto L_0x0011
        L_0x001d:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r6 = r10.writeLock()
            r6.lock()
            java.lang.Object r8 = r11.invoke()     // Catch:{ all -> 0x003d }
            kotlin.jvm.internal.InlineMarker.finallyStart(r9)
            r3 = r7
        L_0x002d:
            if (r3 >= r4) goto L_0x0036
            r1 = 0
            r5.lock()
            int r3 = r3 + 1
            goto L_0x002d
        L_0x0036:
            r6.unlock()
            kotlin.jvm.internal.InlineMarker.finallyEnd(r9)
            return r8
        L_0x003d:
            r8 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r9)
            r3 = r7
        L_0x0042:
            if (r3 >= r4) goto L_0x004b
            r1 = 0
            r5.lock()
            int r3 = r3 + 1
            goto L_0x0042
        L_0x004b:
            r6.unlock()
            kotlin.jvm.internal.InlineMarker.finallyEnd(r9)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.concurrent.LocksKt.write(java.util.concurrent.locks.ReentrantReadWriteLock, kotlin.jvm.functions.Function0):java.lang.Object");
    }
}
