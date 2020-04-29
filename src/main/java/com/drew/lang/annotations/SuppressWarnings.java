package com.drew.lang.annotations;

public @interface SuppressWarnings {
    @NotNull
    String justification();

    @NotNull
    String value();
}
