package com.google.common.util.concurrent;

/* loaded from: classes2.dex */
public class ExecutionError extends Error {
    private static final long serialVersionUID = 0;

    @Deprecated
    protected ExecutionError() {
    }

    @Deprecated
    protected ExecutionError(String message) {
        super(message);
    }

    public ExecutionError(String message, Error cause) {
        super(message, cause);
    }

    public ExecutionError(Error cause) {
        super(cause);
    }
}
