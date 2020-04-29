package dji.thirdparty.rx.functions;

public final class Actions {
    private static final EmptyAction EMPTY_ACTION = new EmptyAction();

    private Actions() {
        throw new IllegalStateException("No instances!");
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8> EmptyAction<T0, T1, T2, T3, T4, T5, T6, T7, T8> empty() {
        return EMPTY_ACTION;
    }

    private static final class EmptyAction<T0, T1, T2, T3, T4, T5, T6, T7, T8> implements Action0, Action1<T0>, Action2<T0, T1>, Action3<T0, T1, T2>, Action4<T0, T1, T2, T3>, Action5<T0, T1, T2, T3, T4>, Action6<T0, T1, T2, T3, T4, T5>, Action7<T0, T1, T2, T3, T4, T5, T6>, Action8<T0, T1, T2, T3, T4, T5, T6, T7>, Action9<T0, T1, T2, T3, T4, T5, T6, T7, T8>, ActionN {
        EmptyAction() {
        }

        public void call() {
        }

        public void call(T0 t0) {
        }

        public void call(T0 t0, T1 t1) {
        }

        public void call(T0 t0, T1 t1, T2 t2) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        }

        public void call(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        }

        public void call(Object... args) {
        }
    }

    public static Func0<Void> toFunc(Action0 action) {
        return toFunc(action, (Object) null);
    }

    public static <T1> Func1<T1, Void> toFunc(Action1<T1> action) {
        return toFunc(action, (Object) null);
    }

    public static <T1, T2> Func2<T1, T2, Void> toFunc(Action2<T1, T2> action) {
        return toFunc(action, (Object) null);
    }

    public static <T1, T2, T3> Func3<T1, T2, T3, Void> toFunc(Action3<T1, T2, T3> action) {
        return toFunc(action, (Object) null);
    }

    public static <T1, T2, T3, T4> Func4<T1, T2, T3, T4, Void> toFunc(Action4<T1, T2, T3, T4> action) {
        return toFunc(action, (Object) null);
    }

    public static <T1, T2, T3, T4, T5> Func5<T1, T2, T3, T4, T5, Void> toFunc(Action5<T1, T2, T3, T4, T5> action) {
        return toFunc(action, (Object) null);
    }

    public static <T1, T2, T3, T4, T5, T6> Func6<T1, T2, T3, T4, T5, T6, Void> toFunc(Action6<T1, T2, T3, T4, T5, T6> action) {
        return toFunc(action, (Object) null);
    }

    public static <T1, T2, T3, T4, T5, T6, T7> Func7<T1, T2, T3, T4, T5, T6, T7, Void> toFunc(Action7<T1, T2, T3, T4, T5, T6, T7> action) {
        return toFunc(action, (Object) null);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8> Func8<T1, T2, T3, T4, T5, T6, T7, T8, Void> toFunc(Action8<T1, T2, T3, T4, T5, T6, T7, T8> action) {
        return toFunc(action, (Object) null);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Void> toFunc(Action9<T1, T2, T3, T4, T5, T6, T7, T8, T9> action) {
        return toFunc(action, (Object) null);
    }

    public static FuncN<Void> toFunc(ActionN action) {
        return toFunc(action, (Object) null);
    }

    public static <R> Func0<R> toFunc(final Action0 action, final R result) {
        return new Func0<R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass1 */

            public R call() {
                action.call();
                return result;
            }
        };
    }

    public static <T1, R> Func1<T1, R> toFunc(final Action1<T1> action, final R result) {
        return new Func1<T1, R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass2 */

            public R call(T1 t1) {
                action.call(t1);
                return result;
            }
        };
    }

    public static <T1, T2, R> Func2<T1, T2, R> toFunc(final Action2<T1, T2> action, final R result) {
        return new Func2<T1, T2, R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass3 */

            public R call(T1 t1, T2 t2) {
                action.call(t1, t2);
                return result;
            }
        };
    }

    public static <T1, T2, T3, R> Func3<T1, T2, T3, R> toFunc(final Action3<T1, T2, T3> action, final R result) {
        return new Func3<T1, T2, T3, R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass4 */

            public R call(T1 t1, T2 t2, T3 t3) {
                action.call(t1, t2, t3);
                return result;
            }
        };
    }

    public static <T1, T2, T3, T4, R> Func4<T1, T2, T3, T4, R> toFunc(final Action4<T1, T2, T3, T4> action, final R result) {
        return new Func4<T1, T2, T3, T4, R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass5 */

            public R call(T1 t1, T2 t2, T3 t3, T4 t4) {
                action.call(t1, t2, t3, t4);
                return result;
            }
        };
    }

    public static <T1, T2, T3, T4, T5, R> Func5<T1, T2, T3, T4, T5, R> toFunc(final Action5<T1, T2, T3, T4, T5> action, final R result) {
        return new Func5<T1, T2, T3, T4, T5, R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass6 */

            public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
                action.call(t1, t2, t3, t4, t5);
                return result;
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, R> Func6<T1, T2, T3, T4, T5, T6, R> toFunc(final Action6<T1, T2, T3, T4, T5, T6> action, final R result) {
        return new Func6<T1, T2, T3, T4, T5, T6, R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass7 */

            public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
                action.call(t1, t2, t3, t4, t5, t6);
                return result;
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> Func7<T1, T2, T3, T4, T5, T6, T7, R> toFunc(final Action7<T1, T2, T3, T4, T5, T6, T7> action, final R result) {
        return new Func7<T1, T2, T3, T4, T5, T6, T7, R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass8 */

            public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
                action.call(t1, t2, t3, t4, t5, t6, t7);
                return result;
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> toFunc(final Action8<T1, T2, T3, T4, T5, T6, T7, T8> action, final R result) {
        return new Func8<T1, T2, T3, T4, T5, T6, T7, T8, R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass9 */

            public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
                action.call(t1, t2, t3, t4, t5, t6, t7, t8);
                return result;
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> toFunc(final Action9<T1, T2, T3, T4, T5, T6, T7, T8, T9> action, final R result) {
        return new Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass10 */

            public R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
                action.call(t1, t2, t3, t4, t5, t6, t7, t8, t9);
                return result;
            }
        };
    }

    public static <R> FuncN<R> toFunc(final ActionN action, final R result) {
        return new FuncN<R>() {
            /* class dji.thirdparty.rx.functions.Actions.AnonymousClass11 */

            public R call(Object... args) {
                action.call(args);
                return result;
            }
        };
    }
}
