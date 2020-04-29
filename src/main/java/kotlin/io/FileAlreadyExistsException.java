package kotlin.io;

import com.amap.location.common.model.AmapLoc;
import dji.publics.LogReport.base.Fields;
import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007¨\u0006\b"}, d2 = {"Lkotlin/io/FileAlreadyExistsException;", "Lkotlin/io/FileSystemException;", AmapLoc.TYPE_OFFLINE_CELL, "Ljava/io/File;", "other", Fields.Dgo_hyperlapse_end.reason, "", "(Ljava/io/File;Ljava/io/File;Ljava/lang/String;)V", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: Exceptions.kt */
public final class FileAlreadyExistsException extends FileSystemException {
    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ FileAlreadyExistsException(java.io.File r4, java.io.File r5, java.lang.String r6, int r7, kotlin.jvm.internal.DefaultConstructorMarker r8) {
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
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FileAlreadyExistsException.<init>(java.io.File, java.io.File, java.lang.String, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FileAlreadyExistsException(@NotNull File file, @Nullable File other, @Nullable String reason) {
        super(file, other, reason);
        Intrinsics.checkParameterIsNotNull(file, AmapLoc.TYPE_OFFLINE_CELL);
    }
}
