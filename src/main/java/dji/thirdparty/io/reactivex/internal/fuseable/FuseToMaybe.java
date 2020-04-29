package dji.thirdparty.io.reactivex.internal.fuseable;

import dji.thirdparty.io.reactivex.Maybe;

public interface FuseToMaybe<T> {
    Maybe<T> fuseToMaybe();
}
