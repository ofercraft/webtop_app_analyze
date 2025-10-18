package com.google.common.cache;

/* loaded from: classes.dex */
public interface Weigher<K, V> {
    int weigh(K key, V value);
}
