package com.google.common.collect;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface ListMultimap<K, V> extends Multimap<K, V> {
    Map<K, Collection<V>> asMap();

    boolean equals(Object obj);

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    List<V> get(K key);

    @Override // 
    List<V> removeAll(Object key);

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    List<V> replaceValues(K key, Iterable<? extends V> values);

    /* JADX WARN: Multi-variable type inference failed */
    /* bridge */ /* synthetic */ default Collection get(Object key) {
        return get((ListMultimap<K, V>) key);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* bridge */ /* synthetic */ default Collection replaceValues(Object key, Iterable values) {
        return replaceValues((ListMultimap<K, V>) key, values);
    }
}
