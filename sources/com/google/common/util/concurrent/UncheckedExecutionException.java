package com.google.common.util.concurrent;

/* loaded from: classes2.dex */
public class UncheckedExecutionException extends RuntimeException {
    private static final long serialVersionUID = 0;

    @Deprecated
    protected UncheckedExecutionException() {
    }

    @Deprecated
    protected UncheckedExecutionException(String message) {
        super(message);
    }

    public UncheckedExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UncheckedExecutionException(Throwable cause) {
        super(cause);
    }
}
