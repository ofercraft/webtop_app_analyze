package com.google.common.collect;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@DoNotMock("Use ImmutableMultimap, HashMultimap, or another implementation")
/* loaded from: classes.dex */
public interface Multimap<K, V> {
    Map<K, Collection<V>> asMap();

    void clear();

    boolean containsEntry(Object key, Object value);

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    Collection<Map.Entry<K, V>> entries();

    boolean equals(Object obj);

    Collection<V> get(K key);

    int hashCode();

    boolean isEmpty();

    Set<K> keySet();

    Multiset<K> keys();

    boolean put(K key, V value);

    boolean putAll(Multimap<? extends K, ? extends V> multimap);

    boolean putAll(K key, Iterable<? extends V> values);

    boolean remove(Object key, Object value);

    Collection<V> removeAll(Object key);

    Collection<V> replaceValues(K key, Iterable<? extends V> values);

    int size();

    Collection<V> values();
}
