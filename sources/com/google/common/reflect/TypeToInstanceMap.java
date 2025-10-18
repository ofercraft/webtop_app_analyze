package com.google.common.reflect;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Map;

@DoNotMock("Use ImmutableTypeToInstanceMap or MutableTypeToInstanceMap")
/* loaded from: classes2.dex */
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B> {
    <T extends B> T getInstance(TypeToken<T> type);

    <T extends B> T getInstance(Class<T> type);

    <T extends B> T putInstance(TypeToken<T> type, T value);

    <T extends B> T putInstance(Class<T> type, T value);
}
