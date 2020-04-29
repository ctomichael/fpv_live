package dji.thirdparty.rx.functions;

public final class Functions {
    private Functions() {
        throw new IllegalStateException("No instances!");
    }

    public static <R> FuncN<R> fromFunc(final Func0 func0) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass1 */

            public R call(Object... args) {
                if (args.length == 0) {
                    return func0.call();
                }
                throw new RuntimeException("Func0 expecting 0 arguments.");
            }
        };
    }

    public static <T0, R> FuncN<R> fromFunc(final Func1 func1) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass2 */

            public R call(Object... args) {
                if (args.length == 1) {
                    return func1.call(args[0]);
                }
                throw new RuntimeException("Func1 expecting 1 argument.");
            }
        };
    }

    public static <T0, T1, R> FuncN<R> fromFunc(final Func2 func2) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass3 */

            public R call(Object... args) {
                if (args.length == 2) {
                    return func2.call(args[0], args[1]);
                }
                throw new RuntimeException("Func2 expecting 2 arguments.");
            }
        };
    }

    public static <T0, T1, T2, R> FuncN<R> fromFunc(final Func3 func3) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass4 */

            public R call(Object... args) {
                if (args.length == 3) {
                    return func3.call(args[0], args[1], args[2]);
                }
                throw new RuntimeException("Func3 expecting 3 arguments.");
            }
        };
    }

    public static <T0, T1, T2, T3, R> FuncN<R> fromFunc(final Func4 func4) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass5 */

            public R call(Object... args) {
                if (args.length == 4) {
                    return func4.call(args[0], args[1], args[2], args[3]);
                }
                throw new RuntimeException("Func4 expecting 4 arguments.");
            }
        };
    }

    public static <T0, T1, T2, T3, T4, R> FuncN<R> fromFunc(final Func5 func5) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass6 */

            public R call(Object... args) {
                if (args.length == 5) {
                    return func5.call(args[0], args[1], args[2], args[3], args[4]);
                }
                throw new RuntimeException("Func5 expecting 5 arguments.");
            }
        };
    }

    public static <T0, T1, T2, T3, T4, T5, R> FuncN<R> fromFunc(final Func6 func6) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass7 */

            public R call(Object... args) {
                if (args.length == 6) {
                    return func6.call(args[0], args[1], args[2], args[3], args[4], args[5]);
                }
                throw new RuntimeException("Func6 expecting 6 arguments.");
            }
        };
    }

    public static <T0, T1, T2, T3, T4, T5, T6, R> FuncN<R> fromFunc(final Func7 func7) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass8 */

            public R call(Object... args) {
                if (args.length == 7) {
                    return func7.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
                }
                throw new RuntimeException("Func7 expecting 7 arguments.");
            }
        };
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, R> FuncN<R> fromFunc(final Func8 func8) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass9 */

            public R call(Object... args) {
                if (args.length == 8) {
                    return func8.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
                }
                throw new RuntimeException("Func8 expecting 8 arguments.");
            }
        };
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, R> FuncN<R> fromFunc(final Func9 func9) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass10 */

            public R call(Object... args) {
                if (args.length == 9) {
                    return func9.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
                }
                throw new RuntimeException("Func9 expecting 9 arguments.");
            }
        };
    }

    public static FuncN<Void> fromAction(final Action0 f) {
        return new FuncN<Void>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass11 */

            public Void call(Object... args) {
                if (args.length != 0) {
                    throw new RuntimeException("Action0 expecting 0 arguments.");
                }
                f.call();
                return null;
            }
        };
    }

    public static <T0> FuncN<Void> fromAction(final Action1<? super T0> f) {
        return new FuncN<Void>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass12 */

            public Void call(Object... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Action1 expecting 1 argument.");
                }
                f.call(args[0]);
                return null;
            }
        };
    }

    public static <T0, T1> FuncN<Void> fromAction(final Action2<? super T0, ? super T1> f) {
        return new FuncN<Void>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass13 */

            public Void call(Object... args) {
                if (args.length != 2) {
                    throw new RuntimeException("Action3 expecting 2 arguments.");
                }
                f.call(args[0], args[1]);
                return null;
            }
        };
    }

    public static <T0, T1, T2> FuncN<Void> fromAction(final Action3<? super T0, ? super T1, ? super T2> f) {
        return new FuncN<Void>() {
            /* class dji.thirdparty.rx.functions.Functions.AnonymousClass14 */

            public Void call(Object... args) {
                if (args.length != 3) {
                    throw new RuntimeException("Action3 expecting 3 arguments.");
                }
                f.call(args[0], args[1], args[2]);
                return null;
            }
        };
    }
}
