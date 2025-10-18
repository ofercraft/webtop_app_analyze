package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public interface Multiset<E> extends Collection<E> {

    public interface Entry<E> {
        boolean equals(Object o);

        int getCount();

        E getElement();

        int hashCode();

        String toString();
    }

    int add(E element, int occurrences);

    boolean add(E element);

    boolean contains(Object element);

    @Override // java.util.Collection
    boolean containsAll(Collection<?> elements);

    int count(Object element);

    Set<E> elementSet();

    Set<Entry<E>> entrySet();

    boolean equals(Object object);

    int hashCode();

    Iterator<E> iterator();

    int remove(Object element, int occurrences);

    boolean remove(Object element);

    boolean removeAll(Collection<?> c);

    boolean retainAll(Collection<?> c);

    int setCount(E element, int count);

    boolean setCount(E element, int oldCount, int newCount);

    int size();

    String toString();
}
