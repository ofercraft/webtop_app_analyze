package com.google.common.base;

import com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

@DoNotMock("Use Optional.of(value) or Optional.absent()")
/* loaded from: classes.dex */
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID = 0;

    public abstract Set<T> asSet();

    public abstract boolean equals(Object object);

    public abstract T get();

    public abstract int hashCode();

    public abstract boolean isPresent();

    public abstract Optional<T> or(Optional<? extends T> secondChoice);

    public abstract T or(Supplier<? extends T> supplier);

    public abstract T or(T defaultValue);

    public abstract T orNull();

    public abstract String toString();

    public abstract <V> Optional<V> transform(Function<? super T, V> function);

    public static <T> Optional<T> absent() {
        return Absent.withType();
    }

    public static <T> Optional<T> of(T reference) {
        return new Present(Preconditions.checkNotNull(reference));
    }

    public static <T> Optional<T> fromNullable(T nullableReference) {
        return nullableReference == null ? absent() : new Present(nullableReference);
    }

    public static <T> Optional<T> fromJavaUtil(java.util.Optional<T> javaUtilOptional) {
        if (javaUtilOptional == null) {
            return null;
        }
        return fromNullable(javaUtilOptional.orElse(null));
    }

    public static <T> java.util.Optional<T> toJavaUtil(Optional<T> googleOptional) {
        if (googleOptional == null) {
            return null;
        }
        return googleOptional.toJavaUtil();
    }

    public java.util.Optional<T> toJavaUtil() {
        return java.util.Optional.ofNullable(orNull());
    }

    Optional() {
    }

    public static <T> Iterable<T> presentInstances(final Iterable<? extends Optional<? extends T>> optionals) {
        Preconditions.checkNotNull(optionals);
        return new Iterable() { // from class: com.google.common.base.Optional$$ExternalSyntheticLambda0
            @Override // java.lang.Iterable
            public final Iterator iterator() {
                return Optional.lambda$presentInstances$0(optionals);
            }
        };
    }

    static /* synthetic */ Iterator lambda$presentInstances$0(Iterable iterable) {
        return new AbstractIterator<T>(iterable) { // from class: com.google.common.base.Optional.1
            private final Iterator<? extends Optional<? extends T>> iterator;
            final /* synthetic */ Iterable val$optionals;

            {
                this.val$optionals = iterable;
                this.iterator = (Iterator) Preconditions.checkNotNull(iterable.iterator());
            }

            @Override // com.google.common.base.AbstractIterator
            protected T computeNext() {
                while (this.iterator.hasNext()) {
                    Optional<? extends T> next = this.iterator.next();
                    if (next.isPresent()) {
                        return next.get();
                    }
                }
                return endOfData();
            }
        };
    }
}
