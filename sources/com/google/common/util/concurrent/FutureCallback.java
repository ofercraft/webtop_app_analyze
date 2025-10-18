package com.google.common.util.concurrent;

/* loaded from: classes2.dex */
public interface FutureCallback<V> {
    void onFailure(Throwable t);

    void onSuccess(V result);
}
