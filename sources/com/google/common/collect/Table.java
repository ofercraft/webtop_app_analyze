package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@DoNotMock("Use ImmutableTable, HashBasedTable, or another implementation")
/* loaded from: classes.dex */
public interface Table<R, C, V> {

    public interface Cell<R, C, V> {
        boolean equals(Object obj);

        C getColumnKey();

        R getRowKey();

        V getValue();

        int hashCode();
    }

    Set<Cell<R, C, V>> cellSet();

    void clear();

    Map<R, V> column(C columnKey);

    Set<C> columnKeySet();

    Map<C, Map<R, V>> columnMap();

    boolean contains(Object rowKey, Object columnKey);

    boolean containsColumn(Object columnKey);

    boolean containsRow(Object rowKey);

    boolean containsValue(Object value);

    boolean equals(Object obj);

    V get(Object rowKey, Object columnKey);

    int hashCode();

    boolean isEmpty();

    V put(R rowKey, C columnKey, V value);

    void putAll(Table<? extends R, ? extends C, ? extends V> table);

    V remove(Object rowKey, Object columnKey);

    Map<C, V> row(R rowKey);

    Set<R> rowKeySet();

    Map<R, Map<C, V>> rowMap();

    int size();

    Collection<V> values();
}
