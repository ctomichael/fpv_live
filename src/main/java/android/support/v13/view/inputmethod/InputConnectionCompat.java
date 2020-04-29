package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle);
    }

    /* JADX WARN: Type inference failed for: r9v4, types: [android.os.Parcelable], assign insn: 0x0014: INVOKE  (r9v4 ? I:android.os.Parcelable) = 
      (r14v0 'data' android.os.Bundle A[D('data' android.os.Bundle)])
      (wrap: java.lang.String : ?: SGET   android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT_RESULT_RECEIVER java.lang.String)
     type: VIRTUAL call: android.os.Bundle.getParcelable(java.lang.String):android.os.Parcelable */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean handlePerformPrivateCommand(@android.support.annotation.Nullable java.lang.String r13, @android.support.annotation.NonNull android.os.Bundle r14, @android.support.annotation.NonNull android.support.v13.view.inputmethod.InputConnectionCompat.OnCommitContentListener r15) {
        /*
            r12 = 0
            r10 = 1
            r11 = 0
            java.lang.String r9 = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT"
            boolean r9 = android.text.TextUtils.equals(r9, r13)
            if (r9 != 0) goto L_0x000d
        L_0x000c:
            return r11
        L_0x000d:
            if (r14 == 0) goto L_0x000c
            r8 = 0
            r7 = 0
            java.lang.String r9 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER"
            android.os.Parcelable r9 = r14.getParcelable(r9)     // Catch:{ all -> 0x005c }
            r0 = r9
            android.os.ResultReceiver r0 = (android.os.ResultReceiver) r0     // Catch:{ all -> 0x005c }
            r8 = r0
            java.lang.String r9 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI"
            android.os.Parcelable r1 = r14.getParcelable(r9)     // Catch:{ all -> 0x005c }
            android.net.Uri r1 = (android.net.Uri) r1     // Catch:{ all -> 0x005c }
            java.lang.String r9 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION"
            android.os.Parcelable r2 = r14.getParcelable(r9)     // Catch:{ all -> 0x005c }
            android.content.ClipDescription r2 = (android.content.ClipDescription) r2     // Catch:{ all -> 0x005c }
            java.lang.String r9 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI"
            android.os.Parcelable r5 = r14.getParcelable(r9)     // Catch:{ all -> 0x005c }
            android.net.Uri r5 = (android.net.Uri) r5     // Catch:{ all -> 0x005c }
            java.lang.String r9 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS"
            int r3 = r14.getInt(r9)     // Catch:{ all -> 0x005c }
            java.lang.String r9 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS"
            android.os.Parcelable r6 = r14.getParcelable(r9)     // Catch:{ all -> 0x005c }
            android.os.Bundle r6 = (android.os.Bundle) r6     // Catch:{ all -> 0x005c }
            android.support.v13.view.inputmethod.InputContentInfoCompat r4 = new android.support.v13.view.inputmethod.InputContentInfoCompat     // Catch:{ all -> 0x005c }
            r4.<init>(r1, r2, r5)     // Catch:{ all -> 0x005c }
            boolean r7 = r15.onCommitContent(r4, r3, r6)     // Catch:{ all -> 0x005c }
            if (r8 == 0) goto L_0x0058
            if (r7 == 0) goto L_0x005a
            r9 = r10
        L_0x0055:
            r8.send(r9, r12)
        L_0x0058:
            r11 = r7
            goto L_0x000c
        L_0x005a:
            r9 = r11
            goto L_0x0055
        L_0x005c:
            r9 = move-exception
            if (r8 == 0) goto L_0x0064
            if (r7 == 0) goto L_0x0065
        L_0x0061:
            r8.send(r10, r12)
        L_0x0064:
            throw r9
        L_0x0065:
            r10 = r11
            goto L_0x0061
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v13.view.inputmethod.InputConnectionCompat.handlePerformPrivateCommand(java.lang.String, android.os.Bundle, android.support.v13.view.inputmethod.InputConnectionCompat$OnCommitContentListener):boolean");
    }

    public static boolean commitContent(@NonNull InputConnection inputConnection, @NonNull EditorInfo editorInfo, @NonNull InputContentInfoCompat inputContentInfo, int flags, @Nullable Bundle opts) {
        ClipDescription description = inputContentInfo.getDescription();
        boolean supported = false;
        String[] contentMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        int length = contentMimeTypes.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if (description.hasMimeType(contentMimeTypes[i])) {
                supported = true;
                break;
            } else {
                i++;
            }
        }
        if (!supported) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo) inputContentInfo.unwrap(), flags, opts);
        }
        Bundle params = new Bundle();
        params.putParcelable(COMMIT_CONTENT_CONTENT_URI_KEY, inputContentInfo.getContentUri());
        params.putParcelable(COMMIT_CONTENT_DESCRIPTION_KEY, inputContentInfo.getDescription());
        params.putParcelable(COMMIT_CONTENT_LINK_URI_KEY, inputContentInfo.getLinkUri());
        params.putInt(COMMIT_CONTENT_FLAGS_KEY, flags);
        params.putParcelable(COMMIT_CONTENT_OPTS_KEY, opts);
        return inputConnection.performPrivateCommand(COMMIT_CONTENT_ACTION, params);
    }

    @NonNull
    public static InputConnection createWrapper(@NonNull InputConnection inputConnection, @NonNull EditorInfo editorInfo, @NonNull OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        } else if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        } else if (onCommitContentListener == null) {
            throw new IllegalArgumentException("onCommitContentListener must be non-null");
        } else if (Build.VERSION.SDK_INT >= 25) {
            final OnCommitContentListener listener = onCommitContentListener;
            return new InputConnectionWrapper(inputConnection, false) {
                /* class android.support.v13.view.inputmethod.InputConnectionCompat.AnonymousClass1 */

                public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
                    if (listener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), flags, opts)) {
                        return true;
                    }
                    return super.commitContent(inputContentInfo, flags, opts);
                }
            };
        } else if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
            return inputConnection;
        } else {
            final OnCommitContentListener listener2 = onCommitContentListener;
            return new InputConnectionWrapper(inputConnection, false) {
                /* class android.support.v13.view.inputmethod.InputConnectionCompat.AnonymousClass2 */

                public boolean performPrivateCommand(String action, Bundle data) {
                    if (InputConnectionCompat.handlePerformPrivateCommand(action, data, listener2)) {
                        return true;
                    }
                    return super.performPrivateCommand(action, data);
                }
            };
        }
    }
}
