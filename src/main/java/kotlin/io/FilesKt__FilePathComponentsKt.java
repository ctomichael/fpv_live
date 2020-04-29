package kotlin.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\f*\u00020\bH\u0002¢\u0006\u0002\b\r\u001a\u001c\u0010\u000e\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0000\u001a\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0002H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0018\u0010\u0004\u001a\u00020\u0002*\u00020\u00028@X\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\"\u0018\u0010\u0007\u001a\u00020\b*\u00020\u00028@X\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u0013"}, d2 = {"isRooted", "", "Ljava/io/File;", "(Ljava/io/File;)Z", "root", "getRoot", "(Ljava/io/File;)Ljava/io/File;", "rootName", "", "getRootName", "(Ljava/io/File;)Ljava/lang/String;", "getRootLength", "", "getRootLength$FilesKt__FilePathComponentsKt", "subPath", "beginIndex", "endIndex", "toComponents", "Lkotlin/io/FilePathComponents;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 15}, xi = 1, xs = "kotlin/io/FilesKt")
/* compiled from: FilePathComponents.kt */
class FilesKt__FilePathComponentsKt {
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int
     arg types: [java.lang.String, char, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, java.lang.String, int, boolean, int, java.lang.Object):int
      kotlin.text.StringsKt__StringsKt.indexOf$default(java.lang.CharSequence, char, int, boolean, int, java.lang.Object):int */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean
     arg types: [java.lang.String, int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, java.lang.CharSequence, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsJVMKt.endsWith$default(java.lang.String, java.lang.String, boolean, int, java.lang.Object):boolean
      kotlin.text.StringsKt__StringsKt.endsWith$default(java.lang.CharSequence, char, boolean, int, java.lang.Object):boolean */
    private static final int getRootLength$FilesKt__FilePathComponentsKt(@NotNull String $this$getRootLength) {
        int first = StringsKt.indexOf$default((CharSequence) $this$getRootLength, File.separatorChar, 0, false, 4, (Object) null);
        if (first == 0) {
            if ($this$getRootLength.length() <= 1 || $this$getRootLength.charAt(1) != File.separatorChar || (first = StringsKt.indexOf$default((CharSequence) $this$getRootLength, File.separatorChar, 2, false, 4, (Object) null)) < 0) {
                return 1;
            }
            int first2 = StringsKt.indexOf$default((CharSequence) $this$getRootLength, File.separatorChar, first + 1, false, 4, (Object) null);
            if (first2 >= 0) {
                return first2 + 1;
            }
            return $this$getRootLength.length();
        } else if (first > 0 && $this$getRootLength.charAt(first - 1) == ':') {
            return first + 1;
        } else {
            if (first != -1 || !StringsKt.endsWith$default((CharSequence) $this$getRootLength, ':', false, 2, (Object) null)) {
                return 0;
            }
            return $this$getRootLength.length();
        }
    }

    @NotNull
    public static final String getRootName(@NotNull File $this$rootName) {
        Intrinsics.checkParameterIsNotNull($this$rootName, "$this$rootName");
        String path = $this$rootName.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "path");
        String path2 = $this$rootName.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path2, "path");
        int rootLength$FilesKt__FilePathComponentsKt = getRootLength$FilesKt__FilePathComponentsKt(path2);
        if (path == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String substring = path.substring(0, rootLength$FilesKt__FilePathComponentsKt);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return substring;
    }

    @NotNull
    public static final File getRoot(@NotNull File $this$root) {
        Intrinsics.checkParameterIsNotNull($this$root, "$this$root");
        return new File(FilesKt.getRootName($this$root));
    }

    public static final boolean isRooted(@NotNull File $this$isRooted) {
        Intrinsics.checkParameterIsNotNull($this$isRooted, "$this$isRooted");
        String path = $this$isRooted.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "path");
        return getRootLength$FilesKt__FilePathComponentsKt(path) > 0;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: kotlin.text.StringsKt__StringsKt.split$default(java.lang.CharSequence, char[], boolean, int, int, java.lang.Object):java.util.List
     arg types: [java.lang.String, char[], int, int, int, ?[OBJECT, ARRAY]]
     candidates:
      kotlin.text.StringsKt__StringsKt.split$default(java.lang.CharSequence, java.lang.String[], boolean, int, int, java.lang.Object):java.util.List
      kotlin.text.StringsKt__StringsKt.split$default(java.lang.CharSequence, char[], boolean, int, int, java.lang.Object):java.util.List */
    @NotNull
    public static final FilePathComponents toComponents(@NotNull File $this$toComponents) {
        List list;
        Intrinsics.checkParameterIsNotNull($this$toComponents, "$this$toComponents");
        String path = $this$toComponents.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "path");
        int rootLength = getRootLength$FilesKt__FilePathComponentsKt(path);
        String rootName = path.substring(0, rootLength);
        Intrinsics.checkExpressionValueIsNotNull(rootName, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        String subPath = path.substring(rootLength);
        Intrinsics.checkExpressionValueIsNotNull(subPath, "(this as java.lang.String).substring(startIndex)");
        if (subPath.length() == 0) {
            list = CollectionsKt.emptyList();
        } else {
            Iterable<String> $this$map$iv = StringsKt.split$default((CharSequence) subPath, new char[]{File.separatorChar}, false, 0, 6, (Object) null);
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            for (String str : $this$map$iv) {
                destination$iv$iv.add(new File(str));
            }
            list = (List) destination$iv$iv;
        }
        return new FilePathComponents(new File(rootName), list);
    }

    @NotNull
    public static final File subPath(@NotNull File $this$subPath, int beginIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$subPath, "$this$subPath");
        return FilesKt.toComponents($this$subPath).subPath(beginIndex, endIndex);
    }
}
