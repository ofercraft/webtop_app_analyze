package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: classes2.dex */
final class Platform {
    static boolean isInstanceOfThrowableClass(Throwable t, Class<? extends Throwable> expectedClass) {
        return expectedClass.isInstance(t);
    }

    static void restoreInterruptIfIsInterruptedException(Throwable t) {
        Preconditions.checkNotNull(t);
        if (t instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    static void interruptCurrentThread() {
        Thread.currentThread().interrupt();
    }

    static void rethrowIfErrorOtherThanStackOverflow(Throwable t) {
        Preconditions.checkNotNull(t);
        if ((t instanceof Error) && !(t instanceof StackOverflowError)) {
            throw ((Error) t);
        }
    }

    static <V> V get(AbstractFuture<V> future) throws ExecutionException, InterruptedException {
        return future.blockingGet();
    }

    static <V> V get(AbstractFuture<V> future, long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return future.blockingGet(timeout, unit);
    }

    private Platform() {
    }
}
