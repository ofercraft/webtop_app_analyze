package com.google.common.base;

import java.lang.Throwable;

/* loaded from: classes.dex */
final class SneakyThrows<T extends Throwable> {
    static Error sneakyThrow(Throwable t) {
        throw new SneakyThrows().throwIt(t);
    }

    private Error throwIt(Throwable t) throws Throwable {
        throw t;
    }

    private SneakyThrows() {
    }
}
