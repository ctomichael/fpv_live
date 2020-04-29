package com.lmax.disruptor;

public class Foo {
    int a;
    int b;
    short c;
    short d;

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Foo foo = (Foo) o;
        if (this.a != foo.a || this.b != foo.b || this.c != foo.c) {
            return false;
        }
        if (this.d != foo.d) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((this.a * 31) + this.b) * 31) + this.c) * 31) + this.d;
    }
}
