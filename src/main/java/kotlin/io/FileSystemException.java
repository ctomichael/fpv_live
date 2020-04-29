package kotlin.io;

import com.amap.location.common.model.AmapLoc;
import dji.publics.LogReport.base.Fields;
import java.io.File;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0016\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\r"}, d2 = {"Lkotlin/io/FileSystemException;", "Ljava/io/IOException;", AmapLoc.TYPE_OFFLINE_CELL, "Ljava/io/File;", "other", Fields.Dgo_hyperlapse_end.reason, "", "(Ljava/io/File;Ljava/io/File;Ljava/lang/String;)V", "getFile", "()Ljava/io/File;", "getOther", "getReason", "()Ljava/lang/String;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: Exceptions.kt */
public class FileSystemException extends IOException {
    @NotNull
    private final File file;
    @Nullable
    private final File other;
    @Nullable
    private final String reason;

    @NotNull
    public final File getFile() {
        return this.file;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ FileSystemException(java.io.File r4, java.io.File r5, java.lang.String r6, int r7, kotlin.jvm.internal.DefaultConstructorMarker r8) {
        /*
            r3 = this;
            r1 = 0
            r0 = r7 & 2
            if (r0 == 0) goto L_0x0014
            r0 = r1
            java.io.File r0 = (java.io.File) r0
        L_0x0008:
            r2 = r7 & 4
            if (r2 == 0) goto L_0x0012
            java.lang.String r1 = (java.lang.String) r1
        L_0x000e:
            r3.<init>(r4, r0, r1)
            return
        L_0x0012:
            r1 = r6
            goto L_0x000e
        L_0x0014:
            r0 = r5
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FileSystemException.<init>(java.io.File, java.io.File, java.lang.String, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    @Nullable
    public final File getOther() {
        return this.other;
    }

    @Nullable
    public final String getReason() {
        return this.reason;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FileSystemException(@NotNull File file2, @Nullable File other2, @Nullable String reason2) {
        super(ExceptionsKt.constructMessage(file2, other2, reason2));
        Intrinsics.checkParameterIsNotNull(file2, AmapLoc.TYPE_OFFLINE_CELL);
        this.file = file2;
        this.other = other2;
        this.reason = reason2;
    }
}
