package com.google.common.util.concurrent;

/* loaded from: classes2.dex */
public final class Runnables {
    private static final Runnable EMPTY_RUNNABLE = new Runnable() { // from class: com.google.common.util.concurrent.Runnables$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            Runnables.lambda$static$0();
        }
    };

    static /* synthetic */ void lambda$static$0() {
    }

    public static Runnable doNothing() {
        return EMPTY_RUNNABLE;
    }

    private Runnables() {
    }
}
