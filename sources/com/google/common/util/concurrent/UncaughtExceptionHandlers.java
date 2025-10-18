package com.google.common.util.concurrent;

import java.lang.Thread;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;

/* loaded from: classes2.dex */
public final class UncaughtExceptionHandlers {

    interface RuntimeWrapper {
        void exit(int status);
    }

    private UncaughtExceptionHandlers() {
    }

    public static Thread.UncaughtExceptionHandler systemExit() {
        final Runtime runtime = Runtime.getRuntime();
        Objects.requireNonNull(runtime);
        return new Exiter(new RuntimeWrapper() { // from class: com.google.common.util.concurrent.UncaughtExceptionHandlers$$ExternalSyntheticLambda0
            @Override // com.google.common.util.concurrent.UncaughtExceptionHandlers.RuntimeWrapper
            public final void exit(int i) {
                runtime.exit(i);
            }
        });
    }

    static final class Exiter implements Thread.UncaughtExceptionHandler {
        private static final LazyLogger logger = new LazyLogger(Exiter.class);
        private final RuntimeWrapper runtime;

        Exiter(RuntimeWrapper runtime) {
            this.runtime = runtime;
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread t, Throwable e) {
            try {
                logger.get().log(Level.SEVERE, String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", t), e);
            } finally {
                try {
                } finally {
                }
            }
        }
    }
}
