package kotlin.io;

import dji.publics.LogReport.base.Fields;
import dji.publics.protocol.ResponseBase;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.collections.AbstractIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\u001a\u001b\u001cB\u0019\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006B\u0001\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\b\u0012\u0014\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b\u00128\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\r\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014¢\u0006\u0002\u0010\u0015J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0017H\u0002J\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0014J\u001a\u0010\u0007\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\bJ \u0010\f\u001a\u00020\u00002\u0018\u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u000b0\rJ\u001a\u0010\n\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b0\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0004¢\u0006\u0002\n\u0000R@\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\rX\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0002X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lkotlin/io/FileTreeWalk;", "Lkotlin/sequences/Sequence;", "Ljava/io/File;", "start", Fields.Dgo_quickshot.direction, "Lkotlin/io/FileWalkDirection;", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;)V", "onEnter", "Lkotlin/Function1;", "", "onLeave", "", "onFail", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "f", "Ljava/io/IOException;", "e", "maxDepth", "", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;I)V", "iterator", "", "depth", "function", "DirectoryState", "FileTreeWalkIterator", "WalkState", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: FileTreeWalk.kt */
public final class FileTreeWalk implements Sequence<File> {
    /* access modifiers changed from: private */
    public final FileWalkDirection direction;
    /* access modifiers changed from: private */
    public final int maxDepth;
    /* access modifiers changed from: private */
    public final Function1<File, Boolean> onEnter;
    /* access modifiers changed from: private */
    public final Function2<File, IOException, Unit> onFail;
    /* access modifiers changed from: private */
    public final Function1<File, Unit> onLeave;
    /* access modifiers changed from: private */
    public final File start;

    /* JADX WARN: Type inference failed for: r3v0, types: [kotlin.jvm.functions.Function1<java.io.File, java.lang.Boolean>, kotlin.jvm.functions.Function1<? super java.io.File, java.lang.Boolean>], assign insn: null */
    /* JADX WARN: Type inference failed for: r4v0, types: [kotlin.jvm.functions.Function1<java.io.File, kotlin.Unit>, kotlin.jvm.functions.Function1<? super java.io.File, kotlin.Unit>], assign insn: null */
    /* JADX WARN: Type inference failed for: r5v0, types: [kotlin.jvm.functions.Function2<? super java.io.File, ? super java.io.IOException, kotlin.Unit>, kotlin.jvm.functions.Function2<java.io.File, java.io.IOException, kotlin.Unit>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private FileTreeWalk(java.io.File r1, kotlin.io.FileWalkDirection r2, kotlin.jvm.functions.Function1<? super java.io.File, java.lang.Boolean> r3, kotlin.jvm.functions.Function1<? super java.io.File, kotlin.Unit> r4, kotlin.jvm.functions.Function2<? super java.io.File, ? super java.io.IOException, kotlin.Unit> r5, int r6) {
        /*
            r0 = this;
            r0.<init>()
            r0.start = r1
            r0.direction = r2
            r0.onEnter = r3
            r0.onLeave = r4
            r0.onFail = r5
            r0.maxDepth = r6
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FileTreeWalk.<init>(java.io.File, kotlin.io.FileWalkDirection, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function2, int):void");
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    /* synthetic */ FileTreeWalk(java.io.File r8, kotlin.io.FileWalkDirection r9, kotlin.jvm.functions.Function1 r10, kotlin.jvm.functions.Function1 r11, kotlin.jvm.functions.Function2 r12, int r13, int r14, kotlin.jvm.internal.DefaultConstructorMarker r15) {
        /*
            r7 = this;
            r0 = r14 & 2
            if (r0 == 0) goto L_0x0018
            kotlin.io.FileWalkDirection r2 = kotlin.io.FileWalkDirection.TOP_DOWN
        L_0x0006:
            r0 = r14 & 32
            if (r0 == 0) goto L_0x0016
            r6 = 2147483647(0x7fffffff, float:NaN)
        L_0x000d:
            r0 = r7
            r1 = r8
            r3 = r10
            r4 = r11
            r5 = r12
            r0.<init>(r1, r2, r3, r4, r5, r6)
            return
        L_0x0016:
            r6 = r13
            goto L_0x000d
        L_0x0018:
            r2 = r9
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FileTreeWalk.<init>(java.io.File, kotlin.io.FileWalkDirection, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function2, int, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public FileTreeWalk(@NotNull File start2, @NotNull FileWalkDirection direction2) {
        this(start2, direction2, null, null, null, 0, 32, null);
        Intrinsics.checkParameterIsNotNull(start2, "start");
        Intrinsics.checkParameterIsNotNull(direction2, Fields.Dgo_quickshot.direction);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ FileTreeWalk(File file, FileWalkDirection fileWalkDirection, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(file, (i & 2) != 0 ? FileWalkDirection.TOP_DOWN : fileWalkDirection);
    }

    @NotNull
    public Iterator<File> iterator() {
        return new FileTreeWalkIterator();
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H&R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\b"}, d2 = {"Lkotlin/io/FileTreeWalk$WalkState;", "", "root", "Ljava/io/File;", "(Ljava/io/File;)V", "getRoot", "()Ljava/io/File;", Fields.Dgo_update.STEP, "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
    /* compiled from: FileTreeWalk.kt */
    private static abstract class WalkState {
        @NotNull
        private final File root;

        @Nullable
        public abstract File step();

        public WalkState(@NotNull File root2) {
            Intrinsics.checkParameterIsNotNull(root2, "root");
            this.root = root2;
        }

        @NotNull
        public final File getRoot() {
            return this.root;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lkotlin/io/FileTreeWalk$DirectoryState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootDir", "Ljava/io/File;", "(Ljava/io/File;)V", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
    /* compiled from: FileTreeWalk.kt */
    private static abstract class DirectoryState extends WalkState {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public DirectoryState(@NotNull File rootDir) {
            super(rootDir);
            Intrinsics.checkParameterIsNotNull(rootDir, "rootDir");
            if (_Assertions.ENABLED) {
                boolean isDirectory = rootDir.isDirectory();
                if (_Assertions.ENABLED && !isDirectory) {
                    throw new AssertionError("rootDir must be verified to be directory beforehand.");
                }
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\r\u000e\u000fB\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0014J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0002J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0010R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;", "Lkotlin/collections/AbstractIterator;", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk;)V", ResponseBase.STRING_STATE, "Ljava/util/ArrayDeque;", "Lkotlin/io/FileTreeWalk$WalkState;", "computeNext", "", "directoryState", "Lkotlin/io/FileTreeWalk$DirectoryState;", "root", "gotoNext", "BottomUpDirectoryState", "SingleFileState", "TopDownDirectoryState", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
    /* compiled from: FileTreeWalk.kt */
    private final class FileTreeWalkIterator extends AbstractIterator<File> {
        private final ArrayDeque<WalkState> state = new ArrayDeque<>();

        public FileTreeWalkIterator() {
            if (FileTreeWalk.this.start.isDirectory()) {
                this.state.push(directoryState(FileTreeWalk.this.start));
            } else if (FileTreeWalk.this.start.isFile()) {
                this.state.push(new SingleFileState(this, FileTreeWalk.this.start));
            } else {
                done();
            }
        }

        /* access modifiers changed from: protected */
        public void computeNext() {
            File nextFile = gotoNext();
            if (nextFile != null) {
                setNext(nextFile);
            } else {
                done();
            }
        }

        private final DirectoryState directoryState(File root) {
            switch (FileTreeWalk.this.direction) {
                case TOP_DOWN:
                    return new TopDownDirectoryState(this, root);
                case BOTTOM_UP:
                    return new BottomUpDirectoryState(this, root);
                default:
                    throw new NoWhenBranchMatchedException();
            }
        }

        private final File gotoNext() {
            while (true) {
                WalkState topState = this.state.peek();
                if (topState == null) {
                    return null;
                }
                File file = topState.step();
                if (file == null) {
                    this.state.pop();
                } else if (Intrinsics.areEqual(file, topState.getRoot()) || !file.isDirectory() || this.state.size() >= FileTreeWalk.this.maxDepth) {
                    return file;
                } else {
                    this.state.push(directoryState(file));
                }
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\r\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\nX\u000e¢\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$BottomUpDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "failed", "", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", Fields.Dgo_update.STEP, "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
        /* compiled from: FileTreeWalk.kt */
        private final class BottomUpDirectoryState extends DirectoryState {
            private boolean failed;
            private int fileIndex;
            private File[] fileList;
            private boolean rootVisited;
            final /* synthetic */ FileTreeWalkIterator this$0;

            /* JADX INFO: super call moved to the top of the method (can break code semantics) */
            public BottomUpDirectoryState(@NotNull FileTreeWalkIterator $outer, File rootDir) {
                super(rootDir);
                Intrinsics.checkParameterIsNotNull(rootDir, "rootDir");
                this.this$0 = $outer;
            }

            @Nullable
            public File step() {
                if (!this.failed && this.fileList == null) {
                    Function1 access$getOnEnter$p = FileTreeWalk.this.onEnter;
                    if (access$getOnEnter$p != null && !((Boolean) access$getOnEnter$p.invoke(getRoot())).booleanValue()) {
                        return null;
                    }
                    this.fileList = getRoot().listFiles();
                    if (this.fileList == null) {
                        Function2 access$getOnFail$p = FileTreeWalk.this.onFail;
                        if (access$getOnFail$p != null) {
                            Unit unit = (Unit) access$getOnFail$p.invoke(getRoot(), new AccessDeniedException(getRoot(), null, "Cannot list files in a directory", 2, null));
                        }
                        this.failed = true;
                    }
                }
                if (this.fileList != null) {
                    int i = this.fileIndex;
                    File[] fileArr = this.fileList;
                    if (fileArr == null) {
                        Intrinsics.throwNpe();
                    }
                    if (i < fileArr.length) {
                        File[] fileArr2 = this.fileList;
                        if (fileArr2 == null) {
                            Intrinsics.throwNpe();
                        }
                        int i2 = this.fileIndex;
                        this.fileIndex = i2 + 1;
                        return fileArr2[i2];
                    }
                }
                if (!this.rootVisited) {
                    this.rootVisited = true;
                    return getRoot();
                }
                Function1 access$getOnLeave$p = FileTreeWalk.this.onLeave;
                if (access$getOnLeave$p == null) {
                    return null;
                }
                Unit unit2 = (Unit) access$getOnLeave$p.invoke(getRoot());
                return null;
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\bX\u000e¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$TopDownDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "", Fields.Dgo_update.STEP, "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
        /* compiled from: FileTreeWalk.kt */
        private final class TopDownDirectoryState extends DirectoryState {
            private int fileIndex;
            private File[] fileList;
            private boolean rootVisited;
            final /* synthetic */ FileTreeWalkIterator this$0;

            /* JADX INFO: super call moved to the top of the method (can break code semantics) */
            public TopDownDirectoryState(@NotNull FileTreeWalkIterator $outer, File rootDir) {
                super(rootDir);
                Intrinsics.checkParameterIsNotNull(rootDir, "rootDir");
                this.this$0 = $outer;
            }

            /* JADX WARNING: Code restructure failed: missing block: B:27:0x0078, code lost:
                if (r0.length == 0) goto L_0x007a;
             */
            @org.jetbrains.annotations.Nullable
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.io.File step() {
                /*
                    r8 = this;
                    r2 = 0
                    boolean r0 = r8.rootVisited
                    if (r0 != 0) goto L_0x0028
                    kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r8.this$0
                    kotlin.io.FileTreeWalk r0 = kotlin.io.FileTreeWalk.this
                    kotlin.jvm.functions.Function1 r0 = r0.onEnter
                    if (r0 == 0) goto L_0x0020
                    java.io.File r1 = r8.getRoot()
                    java.lang.Object r0 = r0.invoke(r1)
                    java.lang.Boolean r0 = (java.lang.Boolean) r0
                    boolean r0 = r0.booleanValue()
                    if (r0 != 0) goto L_0x0020
                L_0x001f:
                    return r2
                L_0x0020:
                    r0 = 1
                    r8.rootVisited = r0
                    java.io.File r2 = r8.getRoot()
                    goto L_0x001f
                L_0x0028:
                    java.io.File[] r0 = r8.fileList
                    if (r0 == 0) goto L_0x0038
                    int r0 = r8.fileIndex
                    java.io.File[] r1 = r8.fileList
                    if (r1 != 0) goto L_0x0035
                    kotlin.jvm.internal.Intrinsics.throwNpe()
                L_0x0035:
                    int r1 = r1.length
                    if (r0 >= r1) goto L_0x009f
                L_0x0038:
                    java.io.File[] r0 = r8.fileList
                    if (r0 != 0) goto L_0x008f
                    java.io.File r0 = r8.getRoot()
                    java.io.File[] r0 = r0.listFiles()
                    r8.fileList = r0
                    java.io.File[] r0 = r8.fileList
                    if (r0 != 0) goto L_0x006c
                    kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r8.this$0
                    kotlin.io.FileTreeWalk r0 = kotlin.io.FileTreeWalk.this
                    kotlin.jvm.functions.Function2 r6 = r0.onFail
                    if (r6 == 0) goto L_0x006c
                    java.io.File r7 = r8.getRoot()
                    kotlin.io.AccessDeniedException r0 = new kotlin.io.AccessDeniedException
                    java.io.File r1 = r8.getRoot()
                    java.lang.String r3 = "Cannot list files in a directory"
                    r4 = 2
                    r5 = r2
                    r0.<init>(r1, r2, r3, r4, r5)
                    java.lang.Object r0 = r6.invoke(r7, r0)
                    kotlin.Unit r0 = (kotlin.Unit) r0
                L_0x006c:
                    java.io.File[] r0 = r8.fileList
                    if (r0 == 0) goto L_0x007a
                    java.io.File[] r0 = r8.fileList
                    if (r0 != 0) goto L_0x0077
                    kotlin.jvm.internal.Intrinsics.throwNpe()
                L_0x0077:
                    int r0 = r0.length
                    if (r0 != 0) goto L_0x008f
                L_0x007a:
                    kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r8.this$0
                    kotlin.io.FileTreeWalk r0 = kotlin.io.FileTreeWalk.this
                    kotlin.jvm.functions.Function1 r0 = r0.onLeave
                    if (r0 == 0) goto L_0x001f
                    java.io.File r1 = r8.getRoot()
                    java.lang.Object r0 = r0.invoke(r1)
                    kotlin.Unit r0 = (kotlin.Unit) r0
                    goto L_0x001f
                L_0x008f:
                    java.io.File[] r0 = r8.fileList
                    if (r0 != 0) goto L_0x0096
                    kotlin.jvm.internal.Intrinsics.throwNpe()
                L_0x0096:
                    int r1 = r8.fileIndex
                    int r2 = r1 + 1
                    r8.fileIndex = r2
                    r2 = r0[r1]
                    goto L_0x001f
                L_0x009f:
                    kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r8.this$0
                    kotlin.io.FileTreeWalk r0 = kotlin.io.FileTreeWalk.this
                    kotlin.jvm.functions.Function1 r0 = r0.onLeave
                    if (r0 == 0) goto L_0x001f
                    java.io.File r1 = r8.getRoot()
                    java.lang.Object r0 = r0.invoke(r1)
                    kotlin.Unit r0 = (kotlin.Unit) r0
                    goto L_0x001f
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FileTreeWalk.FileTreeWalkIterator.TopDownDirectoryState.step():java.io.File");
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$SingleFileState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootFile", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "visited", "", Fields.Dgo_update.STEP, "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
        /* compiled from: FileTreeWalk.kt */
        private final class SingleFileState extends WalkState {
            final /* synthetic */ FileTreeWalkIterator this$0;
            private boolean visited;

            /* JADX INFO: super call moved to the top of the method (can break code semantics) */
            public SingleFileState(@NotNull FileTreeWalkIterator $outer, File rootFile) {
                super(rootFile);
                Intrinsics.checkParameterIsNotNull(rootFile, "rootFile");
                this.this$0 = $outer;
                if (_Assertions.ENABLED) {
                    boolean isFile = rootFile.isFile();
                    if (_Assertions.ENABLED && !isFile) {
                        throw new AssertionError("rootFile must be verified to be file beforehand.");
                    }
                }
            }

            @Nullable
            public File step() {
                if (this.visited) {
                    return null;
                }
                this.visited = true;
                return getRoot();
            }
        }
    }

    @NotNull
    public final FileTreeWalk onEnter(@NotNull Function1<? super File, Boolean> function) {
        Intrinsics.checkParameterIsNotNull(function, "function");
        return new FileTreeWalk(this.start, this.direction, function, this.onLeave, this.onFail, this.maxDepth);
    }

    @NotNull
    public final FileTreeWalk onLeave(@NotNull Function1<? super File, Unit> function) {
        Intrinsics.checkParameterIsNotNull(function, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, function, this.onFail, this.maxDepth);
    }

    @NotNull
    public final FileTreeWalk onFail(@NotNull Function2<? super File, ? super IOException, Unit> function) {
        Intrinsics.checkParameterIsNotNull(function, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, function, this.maxDepth);
    }

    @NotNull
    public final FileTreeWalk maxDepth(int depth) {
        if (depth > 0) {
            return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, this.onFail, depth);
        }
        throw new IllegalArgumentException("depth must be positive, but was " + depth + '.');
    }
}
