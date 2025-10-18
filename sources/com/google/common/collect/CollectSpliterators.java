package com.google.common.collect;

import _COROUTINE.ArtificialStackFrames;
import com.google.common.base.Preconditions;
import com.google.common.collect.CollectSpliterators;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/* loaded from: classes.dex */
final class CollectSpliterators {
    private CollectSpliterators() {
    }

    static <T> Spliterator<T> indexed(int size, int extraCharacteristics, IntFunction<T> function) {
        return indexed(size, extraCharacteristics, function, null);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [java.util.Spliterator$OfInt] */
    static <T> Spliterator<T> indexed(int size, int extraCharacteristics, IntFunction<T> function, Comparator<? super T> comparator) {
        if (comparator != null) {
            Preconditions.checkArgument((extraCharacteristics & 4) != 0);
        }
        return new C1WithCharacteristics(IntStream.range(0, size).spliterator(), function, extraCharacteristics, comparator);
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.CollectSpliterators$1WithCharacteristics, reason: invalid class name */
    class C1WithCharacteristics<T> implements Spliterator<T> {
        private final Spliterator.OfInt delegate;
        final /* synthetic */ Comparator val$comparator;
        final /* synthetic */ int val$extraCharacteristics;
        final /* synthetic */ IntFunction val$function;

        C1WithCharacteristics(Spliterator.OfInt delegate, final IntFunction val$function, final int val$extraCharacteristics, final Comparator val$comparator) {
            this.val$function = val$function;
            this.val$extraCharacteristics = val$extraCharacteristics;
            this.val$comparator = val$comparator;
            this.delegate = delegate;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(final Consumer<? super T> action) {
            Spliterator.OfInt ofInt = this.delegate;
            final IntFunction intFunction = this.val$function;
            return ofInt.tryAdvance(new IntConsumer() { // from class: com.google.common.collect.CollectSpliterators$1WithCharacteristics$$ExternalSyntheticLambda1
                @Override // java.util.function.IntConsumer
                public final void accept(int i) {
                    action.accept(intFunction.apply(i));
                }
            });
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(final Consumer<? super T> action) {
            Spliterator.OfInt ofInt = this.delegate;
            final IntFunction intFunction = this.val$function;
            ofInt.forEachRemaining(new IntConsumer() { // from class: com.google.common.collect.CollectSpliterators$1WithCharacteristics$$ExternalSyntheticLambda0
                @Override // java.util.function.IntConsumer
                public final void accept(int i) {
                    action.accept(intFunction.apply(i));
                }
            });
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            Spliterator.OfInt ofIntTrySplit = this.delegate.trySplit();
            if (ofIntTrySplit == null) {
                return null;
            }
            return new C1WithCharacteristics(ofIntTrySplit, this.val$function, this.val$extraCharacteristics, this.val$comparator);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.delegate.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.val$extraCharacteristics | 16464;
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            if (hasCharacteristics(4)) {
                return this.val$comparator;
            }
            throw new IllegalStateException();
        }
    }

    static <InElementT, OutElementT> Spliterator<OutElementT> map(Spliterator<InElementT> fromSpliterator, Function<? super InElementT, ? extends OutElementT> function) {
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new AnonymousClass1(fromSpliterator, function);
    }

    /* JADX INFO: Add missing generic type declarations: [OutElementT] */
    /* renamed from: com.google.common.collect.CollectSpliterators$1, reason: invalid class name */
    class AnonymousClass1<OutElementT> implements Spliterator<OutElementT> {
        final /* synthetic */ Spliterator val$fromSpliterator;
        final /* synthetic */ Function val$function;

        AnonymousClass1(final Spliterator val$fromSpliterator, final Function val$function) {
            this.val$fromSpliterator = val$fromSpliterator;
            this.val$function = val$function;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(final Consumer<? super OutElementT> action) {
            Spliterator spliterator = this.val$fromSpliterator;
            final Function function = this.val$function;
            return spliterator.tryAdvance(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$1$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    action.accept(function.apply(obj));
                }
            });
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(final Consumer<? super OutElementT> action) {
            Spliterator spliterator = this.val$fromSpliterator;
            final Function function = this.val$function;
            spliterator.forEachRemaining(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    action.accept(function.apply(obj));
                }
            });
        }

        @Override // java.util.Spliterator
        public Spliterator<OutElementT> trySplit() {
            Spliterator spliteratorTrySplit = this.val$fromSpliterator.trySplit();
            if (spliteratorTrySplit != null) {
                return CollectSpliterators.map(spliteratorTrySplit, this.val$function);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.val$fromSpliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.val$fromSpliterator.characteristics() & (-262);
        }
    }

    static <T> Spliterator<T> filter(Spliterator<T> fromSpliterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(predicate);
        return new C1Splitr(fromSpliterator, predicate);
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.CollectSpliterators$1Splitr, reason: invalid class name */
    class C1Splitr<T> implements Spliterator<T>, Consumer<T> {
        T holder = null;
        final /* synthetic */ Spliterator val$fromSpliterator;
        final /* synthetic */ Predicate val$predicate;

        C1Splitr(final Spliterator val$fromSpliterator, final Predicate val$predicate) {
            this.val$fromSpliterator = val$fromSpliterator;
            this.val$predicate = val$predicate;
        }

        @Override // java.util.function.Consumer
        public void accept(T t) {
            this.holder = t;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            while (this.val$fromSpliterator.tryAdvance(this)) {
                try {
                    ArtificialStackFrames artificialStackFrames = (Object) NullnessCasts.uncheckedCastNullableTToT(this.holder);
                    if (this.val$predicate.test(artificialStackFrames)) {
                        consumer.accept(artificialStackFrames);
                        this.holder = null;
                        return true;
                    }
                } finally {
                    this.holder = null;
                }
            }
            return false;
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            Spliterator<T> spliteratorTrySplit = this.val$fromSpliterator.trySplit();
            if (spliteratorTrySplit == null) {
                return null;
            }
            return CollectSpliterators.filter(spliteratorTrySplit, this.val$predicate);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.val$fromSpliterator.estimateSize() / 2;
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            return this.val$fromSpliterator.getComparator();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.val$fromSpliterator.characteristics() & 277;
        }
    }

    static <InElementT, OutElementT> Spliterator<OutElementT> flatMap(Spliterator<InElementT> fromSpliterator, Function<? super InElementT, Spliterator<OutElementT>> function, int topCharacteristics, long topSize) {
        Preconditions.checkArgument((topCharacteristics & 16384) == 0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 4) == 0, "flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfObject(null, fromSpliterator, function, topCharacteristics, topSize);
    }

    static <InElementT> Spliterator.OfInt flatMapToInt(Spliterator<InElementT> fromSpliterator, Function<? super InElementT, Spliterator.OfInt> function, int topCharacteristics, long topSize) {
        Preconditions.checkArgument((topCharacteristics & 16384) == 0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 4) == 0, "flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfInt(null, fromSpliterator, function, topCharacteristics, topSize);
    }

    static <InElementT> Spliterator.OfLong flatMapToLong(Spliterator<InElementT> fromSpliterator, Function<? super InElementT, Spliterator.OfLong> function, int topCharacteristics, long topSize) {
        Preconditions.checkArgument((topCharacteristics & 16384) == 0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 4) == 0, "flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfLong(null, fromSpliterator, function, topCharacteristics, topSize);
    }

    static <InElementT> Spliterator.OfDouble flatMapToDouble(Spliterator<InElementT> fromSpliterator, Function<? super InElementT, Spliterator.OfDouble> function, int topCharacteristics, long topSize) {
        Preconditions.checkArgument((topCharacteristics & 16384) == 0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 4) == 0, "flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfDouble(null, fromSpliterator, function, topCharacteristics, topSize);
    }

    static abstract class FlatMapSpliterator<InElementT, OutElementT, OutSpliteratorT extends Spliterator<OutElementT>> implements Spliterator<OutElementT> {
        int characteristics;
        long estimatedSize;
        final Factory<InElementT, OutSpliteratorT> factory;
        final Spliterator<InElementT> from;
        final Function<? super InElementT, OutSpliteratorT> function;
        OutSpliteratorT prefix;

        interface Factory<InElementT, OutSpliteratorT extends Spliterator<?>> {
            OutSpliteratorT newFlatMapSpliterator(OutSpliteratorT prefix, Spliterator<InElementT> fromSplit, Function<? super InElementT, OutSpliteratorT> function, int splitCharacteristics, long estSplitSize);
        }

        FlatMapSpliterator(OutSpliteratorT prefix, Spliterator<InElementT> from, Function<? super InElementT, OutSpliteratorT> function, Factory<InElementT, OutSpliteratorT> factory, int characteristics, long estimatedSize) {
            this.prefix = prefix;
            this.from = from;
            this.function = function;
            this.factory = factory;
            this.characteristics = characteristics;
            this.estimatedSize = estimatedSize;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super OutElementT> action) {
            do {
                OutSpliteratorT outspliteratort = this.prefix;
                if (outspliteratort != null && outspliteratort.tryAdvance(action)) {
                    long j = this.estimatedSize;
                    if (j == Long.MAX_VALUE) {
                        return true;
                    }
                    this.estimatedSize = j - 1;
                    return true;
                }
                this.prefix = null;
            } while (this.from.tryAdvance(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$FlatMapSpliterator$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m280xf1ca5dcf(obj);
                }
            }));
            return false;
        }

        /* renamed from: lambda$tryAdvance$0$com-google-common-collect-CollectSpliterators$FlatMapSpliterator, reason: not valid java name */
        /* synthetic */ void m280xf1ca5dcf(Object obj) {
            this.prefix = this.function.apply(obj);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(final Consumer<? super OutElementT> action) {
            OutSpliteratorT outspliteratort = this.prefix;
            if (outspliteratort != null) {
                outspliteratort.forEachRemaining(action);
                this.prefix = null;
            }
            this.from.forEachRemaining(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$FlatMapSpliterator$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m279xe354510a(action, obj);
                }
            });
            this.estimatedSize = 0L;
        }

        /* renamed from: lambda$forEachRemaining$0$com-google-common-collect-CollectSpliterators$FlatMapSpliterator, reason: not valid java name */
        /* synthetic */ void m279xe354510a(Consumer consumer, Object obj) {
            OutSpliteratorT outspliteratortApply = this.function.apply(obj);
            if (outspliteratortApply != null) {
                outspliteratortApply.forEachRemaining(consumer);
            }
        }

        @Override // java.util.Spliterator
        public final OutSpliteratorT trySplit() {
            Spliterator<InElementT> spliteratorTrySplit = this.from.trySplit();
            if (spliteratorTrySplit != null) {
                int i = this.characteristics & (-65);
                long jEstimateSize = estimateSize();
                if (jEstimateSize < Long.MAX_VALUE) {
                    jEstimateSize /= 2;
                    this.estimatedSize -= jEstimateSize;
                    this.characteristics = i;
                }
                OutSpliteratorT outspliteratort = (OutSpliteratorT) this.factory.newFlatMapSpliterator(this.prefix, spliteratorTrySplit, this.function, i, jEstimateSize);
                this.prefix = null;
                return outspliteratort;
            }
            OutSpliteratorT outspliteratort2 = this.prefix;
            if (outspliteratort2 == null) {
                return null;
            }
            this.prefix = null;
            return outspliteratort2;
        }

        @Override // java.util.Spliterator
        public final long estimateSize() {
            OutSpliteratorT outspliteratort = this.prefix;
            if (outspliteratort != null) {
                this.estimatedSize = Math.max(this.estimatedSize, outspliteratort.estimateSize());
            }
            return Math.max(this.estimatedSize, 0L);
        }

        @Override // java.util.Spliterator
        public final int characteristics() {
            return this.characteristics;
        }
    }

    static final class FlatMapSpliteratorOfObject<InElementT, OutElementT> extends FlatMapSpliterator<InElementT, OutElementT, Spliterator<OutElementT>> {
        FlatMapSpliteratorOfObject(Spliterator<OutElementT> prefix, Spliterator<InElementT> from, Function<? super InElementT, Spliterator<OutElementT>> function, int characteristics, long estimatedSize) {
            super(prefix, from, function, new FlatMapSpliterator.Factory() { // from class: com.google.common.collect.CollectSpliterators$FlatMapSpliteratorOfObject$$ExternalSyntheticLambda0
                @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliterator.Factory
                public final Spliterator newFlatMapSpliterator(Spliterator spliterator, Spliterator spliterator2, Function function2, int i, long j) {
                    return new CollectSpliterators.FlatMapSpliteratorOfObject(spliterator, spliterator2, function2, i, j);
                }
            }, characteristics, estimatedSize);
        }
    }

    static abstract class FlatMapSpliteratorOfPrimitive<InElementT, OutElementT, OutConsumerT, OutSpliteratorT extends Spliterator.OfPrimitive<OutElementT, OutConsumerT, OutSpliteratorT>> extends FlatMapSpliterator<InElementT, OutElementT, OutSpliteratorT> implements Spliterator.OfPrimitive<OutElementT, OutConsumerT, OutSpliteratorT> {
        @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
            return (Spliterator.OfPrimitive) super.trySplit();
        }

        FlatMapSpliteratorOfPrimitive(OutSpliteratorT prefix, Spliterator<InElementT> from, Function<? super InElementT, OutSpliteratorT> function, FlatMapSpliterator.Factory<InElementT, OutSpliteratorT> factory, int characteristics, long estimatedSize) {
            super(prefix, from, function, factory, characteristics, estimatedSize);
        }

        @Override // java.util.Spliterator.OfPrimitive
        public final boolean tryAdvance(OutConsumerT action) {
            do {
                if (this.prefix != 0 && ((Spliterator.OfPrimitive) this.prefix).tryAdvance((Spliterator.OfPrimitive) action)) {
                    if (this.estimatedSize == Long.MAX_VALUE) {
                        return true;
                    }
                    this.estimatedSize--;
                    return true;
                }
                this.prefix = null;
            } while (this.from.tryAdvance(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$FlatMapSpliteratorOfPrimitive$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m282xff99a381(obj);
                }
            }));
            return false;
        }

        /* renamed from: lambda$tryAdvance$0$com-google-common-collect-CollectSpliterators$FlatMapSpliteratorOfPrimitive, reason: not valid java name */
        /* synthetic */ void m282xff99a381(Object obj) {
            this.prefix = (OutSpliteratorT) this.function.apply(obj);
        }

        @Override // java.util.Spliterator.OfPrimitive
        public final void forEachRemaining(final OutConsumerT action) {
            if (this.prefix != 0) {
                ((Spliterator.OfPrimitive) this.prefix).forEachRemaining((Spliterator.OfPrimitive) action);
                this.prefix = null;
            }
            this.from.forEachRemaining(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$FlatMapSpliteratorOfPrimitive$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.m281x545aed66(action, obj);
                }
            });
            this.estimatedSize = 0L;
        }

        /* renamed from: lambda$forEachRemaining$0$com-google-common-collect-CollectSpliterators$FlatMapSpliteratorOfPrimitive, reason: not valid java name */
        /* synthetic */ void m281x545aed66(Object obj, Object obj2) {
            Spliterator.OfPrimitive ofPrimitive = (Spliterator.OfPrimitive) this.function.apply(obj2);
            if (ofPrimitive != null) {
                ofPrimitive.forEachRemaining((Spliterator.OfPrimitive) obj);
            }
        }
    }

    static final class FlatMapSpliteratorOfInt<InElementT> extends FlatMapSpliteratorOfPrimitive<InElementT, Integer, IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt {
        @Override // java.util.Spliterator.OfInt
        public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer action) {
            super.forEachRemaining((FlatMapSpliteratorOfInt<InElementT>) action);
        }

        @Override // java.util.Spliterator.OfInt
        public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer action) {
            return super.tryAdvance((FlatMapSpliteratorOfInt<InElementT>) action);
        }

        @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliteratorOfPrimitive, com.google.common.collect.CollectSpliterators.FlatMapSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
            return (Spliterator.OfInt) super.trySplit();
        }

        FlatMapSpliteratorOfInt(Spliterator.OfInt prefix, Spliterator<InElementT> from, Function<? super InElementT, Spliterator.OfInt> function, int characteristics, long estimatedSize) {
            super(prefix, from, function, new FlatMapSpliterator.Factory() { // from class: com.google.common.collect.CollectSpliterators$FlatMapSpliteratorOfInt$$ExternalSyntheticLambda0
                @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliterator.Factory
                public final Spliterator newFlatMapSpliterator(Spliterator spliterator, Spliterator spliterator2, Function function2, int i, long j) {
                    return new CollectSpliterators.FlatMapSpliteratorOfInt((Spliterator.OfInt) spliterator, spliterator2, function2, i, j);
                }
            }, characteristics, estimatedSize);
        }
    }

    static final class FlatMapSpliteratorOfLong<InElementT> extends FlatMapSpliteratorOfPrimitive<InElementT, Long, LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong {
        @Override // java.util.Spliterator.OfLong
        public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer action) {
            super.forEachRemaining((FlatMapSpliteratorOfLong<InElementT>) action);
        }

        @Override // java.util.Spliterator.OfLong
        public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer action) {
            return super.tryAdvance((FlatMapSpliteratorOfLong<InElementT>) action);
        }

        @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliteratorOfPrimitive, com.google.common.collect.CollectSpliterators.FlatMapSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
            return (Spliterator.OfLong) super.trySplit();
        }

        FlatMapSpliteratorOfLong(Spliterator.OfLong prefix, Spliterator<InElementT> from, Function<? super InElementT, Spliterator.OfLong> function, int characteristics, long estimatedSize) {
            super(prefix, from, function, new FlatMapSpliterator.Factory() { // from class: com.google.common.collect.CollectSpliterators$FlatMapSpliteratorOfLong$$ExternalSyntheticLambda0
                @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliterator.Factory
                public final Spliterator newFlatMapSpliterator(Spliterator spliterator, Spliterator spliterator2, Function function2, int i, long j) {
                    return new CollectSpliterators.FlatMapSpliteratorOfLong((Spliterator.OfLong) spliterator, spliterator2, function2, i, j);
                }
            }, characteristics, estimatedSize);
        }
    }

    static final class FlatMapSpliteratorOfDouble<InElementT> extends FlatMapSpliteratorOfPrimitive<InElementT, Double, DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble {
        @Override // java.util.Spliterator.OfDouble
        public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer action) {
            super.forEachRemaining((FlatMapSpliteratorOfDouble<InElementT>) action);
        }

        @Override // java.util.Spliterator.OfDouble
        public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer action) {
            return super.tryAdvance((FlatMapSpliteratorOfDouble<InElementT>) action);
        }

        @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliteratorOfPrimitive, com.google.common.collect.CollectSpliterators.FlatMapSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
            return (Spliterator.OfDouble) super.trySplit();
        }

        FlatMapSpliteratorOfDouble(Spliterator.OfDouble prefix, Spliterator<InElementT> from, Function<? super InElementT, Spliterator.OfDouble> function, int characteristics, long estimatedSize) {
            super(prefix, from, function, new FlatMapSpliterator.Factory() { // from class: com.google.common.collect.CollectSpliterators$FlatMapSpliteratorOfDouble$$ExternalSyntheticLambda0
                @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliterator.Factory
                public final Spliterator newFlatMapSpliterator(Spliterator spliterator, Spliterator spliterator2, Function function2, int i, long j) {
                    return new CollectSpliterators.FlatMapSpliteratorOfDouble((Spliterator.OfDouble) spliterator, spliterator2, function2, i, j);
                }
            }, characteristics, estimatedSize);
        }
    }
}
