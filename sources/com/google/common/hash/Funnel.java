package com.google.common.hash;

import com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;

@DoNotMock("Implement with a lambda")
/* loaded from: classes.dex */
public interface Funnel<T> extends Serializable {
    void funnel(T from, PrimitiveSink into);
}
