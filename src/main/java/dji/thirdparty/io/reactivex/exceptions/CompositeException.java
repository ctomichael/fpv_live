package dji.thirdparty.io.reactivex.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class CompositeException extends RuntimeException {
    private static final long serialVersionUID = 3026362227162912146L;
    private Throwable cause;
    private final List<Throwable> exceptions;
    private final String message;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public CompositeException(java.lang.Throwable... r5) {
        /*
            r4 = this;
            if (r5 != 0) goto L_0x0018
            r0 = 1
            java.lang.NullPointerException[] r0 = new java.lang.NullPointerException[r0]
            r1 = 0
            java.lang.NullPointerException r2 = new java.lang.NullPointerException
            java.lang.String r3 = "exceptions was null"
            r2.<init>(r3)
            r0[r1] = r2
            java.util.List r0 = java.util.Arrays.asList(r0)
        L_0x0014:
            r4.<init>(r0)
            return
        L_0x0018:
            java.util.List r0 = java.util.Arrays.asList(r5)
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.exceptions.CompositeException.<init>(java.lang.Throwable[]):void");
    }

    public CompositeException(Iterable<? extends Throwable> errors) {
        Set<Throwable> deDupedExceptions = new LinkedHashSet<>();
        List<Throwable> localExceptions = new ArrayList<>();
        if (errors != null) {
            for (Throwable ex : errors) {
                if (ex instanceof CompositeException) {
                    deDupedExceptions.addAll(((CompositeException) ex).getExceptions());
                } else if (ex != null) {
                    deDupedExceptions.add(ex);
                } else {
                    deDupedExceptions.add(new NullPointerException("Throwable was null!"));
                }
            }
        } else {
            deDupedExceptions.add(new NullPointerException("errors was null"));
        }
        if (deDupedExceptions.isEmpty()) {
            throw new IllegalArgumentException("errors is empty");
        }
        localExceptions.addAll(deDupedExceptions);
        this.exceptions = Collections.unmodifiableList(localExceptions);
        this.message = this.exceptions.size() + " exceptions occurred. ";
    }

    public List<Throwable> getExceptions() {
        return this.exceptions;
    }

    public String getMessage() {
        return this.message;
    }

    public synchronized Throwable getCause() {
        if (this.cause == null) {
            Throwable localCause = new CompositeExceptionCausalChain();
            Set<Throwable> seenCauses = new HashSet<>();
            Throwable chain = localCause;
            Iterator<Throwable> it2 = this.exceptions.iterator();
            while (it2.hasNext()) {
                Throwable e = it2.next();
                if (!seenCauses.contains(e)) {
                    seenCauses.add(e);
                    for (Throwable child : getListOfCauses(e)) {
                        if (seenCauses.contains(child)) {
                            e = new RuntimeException("Duplicate found in causal chain so cropping to prevent loop ...");
                        } else {
                            seenCauses.add(child);
                        }
                    }
                    try {
                        chain.initCause(e);
                    } catch (Throwable th) {
                    }
                    chain = getRootCause(chain);
                }
            }
            this.cause = localCause;
        }
        return this.cause;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream s) {
        printStackTrace(new WrappedPrintStream(s));
    }

    public void printStackTrace(PrintWriter s) {
        printStackTrace(new WrappedPrintWriter(s));
    }

    /* JADX INFO: Multiple debug info for r4v2 java.util.Iterator<java.lang.Throwable>: [D('i$' java.util.Iterator), D('i$' int)] */
    private void printStackTrace(PrintStreamOrWriter s) {
        StringBuilder b = new StringBuilder(128);
        b.append(this).append(10);
        for (StackTraceElement myStackElement : getStackTrace()) {
            b.append("\tat ").append(myStackElement).append(10);
        }
        int i = 1;
        for (Throwable ex : this.exceptions) {
            b.append("  ComposedException ").append(i).append(" :\n");
            appendStackTrace(b, ex, "\t");
            i++;
        }
        s.println(b.toString());
    }

    private void appendStackTrace(StringBuilder b, Throwable ex, String prefix) {
        b.append(prefix).append(ex).append(10);
        for (StackTraceElement stackElement : ex.getStackTrace()) {
            b.append("\t\tat ").append(stackElement).append(10);
        }
        if (ex.getCause() != null) {
            b.append("\tCaused by: ");
            appendStackTrace(b, ex.getCause(), "");
        }
    }

    static abstract class PrintStreamOrWriter {
        /* access modifiers changed from: package-private */
        public abstract void println(Object obj);

        PrintStreamOrWriter() {
        }
    }

    static final class WrappedPrintStream extends PrintStreamOrWriter {
        private final PrintStream printStream;

        WrappedPrintStream(PrintStream printStream2) {
            this.printStream = printStream2;
        }

        /* access modifiers changed from: package-private */
        public void println(Object o) {
            this.printStream.println(o);
        }
    }

    static final class WrappedPrintWriter extends PrintStreamOrWriter {
        private final PrintWriter printWriter;

        WrappedPrintWriter(PrintWriter printWriter2) {
            this.printWriter = printWriter2;
        }

        /* access modifiers changed from: package-private */
        public void println(Object o) {
            this.printWriter.println(o);
        }
    }

    static final class CompositeExceptionCausalChain extends RuntimeException {
        static final String MESSAGE = "Chain of Causes for CompositeException In Order Received =>";
        private static final long serialVersionUID = 3875212506787802066L;

        CompositeExceptionCausalChain() {
        }

        public String getMessage() {
            return MESSAGE;
        }
    }

    private List<Throwable> getListOfCauses(Throwable ex) {
        List<Throwable> list = new ArrayList<>();
        Throwable root = ex.getCause();
        if (root != null && root != ex) {
            while (true) {
                list.add(root);
                Throwable cause2 = root.getCause();
                if (cause2 == null || cause2 == root) {
                    break;
                }
                root = cause2;
            }
        }
        return list;
    }

    public int size() {
        return this.exceptions.size();
    }

    private Throwable getRootCause(Throwable e) {
        Throwable root = e.getCause();
        if (root == null || this.cause == root) {
            return e;
        }
        while (true) {
            Throwable cause2 = root.getCause();
            if (cause2 != null && cause2 != root) {
                root = cause2;
            }
        }
        return root;
    }
}
