package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use Interners.new*Interner")
/* loaded from: classes.dex */
public interface Interner<E> {
    E intern(E sample);
}
