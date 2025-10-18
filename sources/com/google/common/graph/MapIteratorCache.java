package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
class MapIteratorCache<K, V> {
    private final Map<K, V> backingMap;
    private volatile transient Map.Entry<K, V> cacheEntry;

    MapIteratorCache(Map<K, V> backingMap) {
        this.backingMap = (Map) Preconditions.checkNotNull(backingMap);
    }

    final V put(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        clearCache();
        return this.backingMap.put(key, value);
    }

    final V remove(Object key) {
        Preconditions.checkNotNull(key);
        clearCache();
        return this.backingMap.remove(key);
    }

    final void clear() {
        clearCache();
        this.backingMap.clear();
    }

    V get(Object key) {
        Preconditions.checkNotNull(key);
        V ifCached = getIfCached(key);
        return ifCached == null ? getWithoutCaching(key) : ifCached;
    }

    final V getWithoutCaching(Object key) {
        Preconditions.checkNotNull(key);
        return this.backingMap.get(key);
    }

    final boolean containsKey(Object key) {
        return getIfCached(key) != null || this.backingMap.containsKey(key);
    }

    final Set<K> unmodifiableKeySet() {
        return new AbstractSet<K>() { // from class: com.google.common.graph.MapIteratorCache.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator<K> iterator() {
                final Iterator<Map.Entry<K, V>> it = MapIteratorCache.this.backingMap.entrySet().iterator();
                return new UnmodifiableIterator<K>(this) { // from class: com.google.common.graph.MapIteratorCache.1.1
                    final /* synthetic */ AnonymousClass1 this$1;

                    {
                        this.this$1 = this;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override // java.util.Iterator
                    public K next() {
                        Map.Entry entry = (Map.Entry) it.next();
                        MapIteratorCache.this.cacheEntry = entry;
                        return (K) entry.getKey();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return MapIteratorCache.this.backingMap.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object key) {
                return MapIteratorCache.this.containsKey(key);
            }
        };
    }

    V getIfCached(Object key) {
        Map.Entry<K, V> entry = this.cacheEntry;
        if (entry == null || entry.getKey() != key) {
            return null;
        }
        return entry.getValue();
    }

    void clearCache() {
        this.cacheEntry = null;
    }
}
