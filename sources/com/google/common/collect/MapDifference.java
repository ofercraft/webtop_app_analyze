package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Map;

@DoNotMock("Use Maps.difference")
/* loaded from: classes.dex */
public interface MapDifference<K, V> {

    @DoNotMock("Use Maps.difference")
    public interface ValueDifference<V> {
        boolean equals(Object other);

        int hashCode();

        V leftValue();

        V rightValue();
    }

    boolean areEqual();

    Map<K, ValueDifference<V>> entriesDiffering();

    Map<K, V> entriesInCommon();

    Map<K, V> entriesOnlyOnLeft();

    Map<K, V> entriesOnlyOnRight();

    boolean equals(Object object);

    int hashCode();
}
