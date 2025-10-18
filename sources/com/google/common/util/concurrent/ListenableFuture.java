package com.google.common.util.concurrent;

import com.google.errorprone.annotations.DoNotMock;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import org.jspecify.annotations.NullMarked;

@DoNotMock("Use the methods in Futures (like immediateFuture) or SettableFuture")
@NullMarked
/* loaded from: classes2.dex */
public interface ListenableFuture<V> extends Future<V> {
    void addListener(Runnable listener, Executor executor);
}
