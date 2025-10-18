package com.google.common.collect;

import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public interface BiMap<K, V> extends Map<K, V> {
    V forcePut(K key, V value);

    BiMap<V, K> inverse();

    V put(K key, V value);

    void putAll(Map<? extends K, ? extends V> map);

    @Override // com.google.common.collect.BiMap
    Set<V> values();
}
