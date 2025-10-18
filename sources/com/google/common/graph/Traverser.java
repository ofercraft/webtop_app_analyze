package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.DoNotMock;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

@DoNotMock("Call forGraph or forTree, passing a lambda or a Graph with the desired edges (built with GraphBuilder)")
/* loaded from: classes.dex */
public abstract class Traverser<N> {
    private final SuccessorsFunction<N> successorFunction;

    private enum InsertionOrder {
        FRONT { // from class: com.google.common.graph.Traverser.InsertionOrder.1
            @Override // com.google.common.graph.Traverser.InsertionOrder
            <T> void insertInto(Deque<T> deque, T value) {
                deque.addFirst(value);
            }
        },
        BACK { // from class: com.google.common.graph.Traverser.InsertionOrder.2
            @Override // com.google.common.graph.Traverser.InsertionOrder
            <T> void insertInto(Deque<T> deque, T value) {
                deque.addLast(value);
            }
        };

        abstract <T> void insertInto(Deque<T> deque, T value);
    }

    abstract Traversal<N> newTraversal();

    private Traverser(SuccessorsFunction<N> successorFunction) {
        this.successorFunction = (SuccessorsFunction) Preconditions.checkNotNull(successorFunction);
    }

    public static <N> Traverser<N> forGraph(final SuccessorsFunction<N> graph) {
        return new Traverser<N>(graph) { // from class: com.google.common.graph.Traverser.1
            @Override // com.google.common.graph.Traverser
            Traversal<N> newTraversal() {
                return Traversal.inGraph(graph);
            }
        };
    }

    public static <N> Traverser<N> forTree(final SuccessorsFunction<N> tree) {
        if (tree instanceof BaseGraph) {
            Preconditions.checkArgument(((BaseGraph) tree).isDirected(), "Undirected graphs can never be trees.");
        }
        if (tree instanceof Network) {
            Preconditions.checkArgument(((Network) tree).isDirected(), "Undirected networks can never be trees.");
        }
        return new Traverser<N>(tree) { // from class: com.google.common.graph.Traverser.2
            @Override // com.google.common.graph.Traverser
            Traversal<N> newTraversal() {
                return Traversal.inTree(tree);
            }
        };
    }

    public final Iterable<N> breadthFirst(N startNode) {
        return breadthFirst((Iterable) ImmutableSet.of(startNode));
    }

    public final Iterable<N> breadthFirst(Iterable<? extends N> startNodes) {
        final ImmutableSet<N> immutableSetValidate = validate(startNodes);
        return new Iterable() { // from class: com.google.common.graph.Traverser$$ExternalSyntheticLambda1
            @Override // java.lang.Iterable
            public final Iterator iterator() {
                return this.f$0.m306lambda$breadthFirst$0$comgooglecommongraphTraverser(immutableSetValidate);
            }
        };
    }

    /* renamed from: lambda$breadthFirst$0$com-google-common-graph-Traverser, reason: not valid java name */
    /* synthetic */ Iterator m306lambda$breadthFirst$0$comgooglecommongraphTraverser(ImmutableSet immutableSet) {
        return newTraversal().breadthFirst(immutableSet.iterator());
    }

    public final Iterable<N> depthFirstPreOrder(N startNode) {
        return depthFirstPreOrder((Iterable) ImmutableSet.of(startNode));
    }

    public final Iterable<N> depthFirstPreOrder(Iterable<? extends N> startNodes) {
        final ImmutableSet<N> immutableSetValidate = validate(startNodes);
        return new Iterable() { // from class: com.google.common.graph.Traverser$$ExternalSyntheticLambda0
            @Override // java.lang.Iterable
            public final Iterator iterator() {
                return this.f$0.m308lambda$depthFirstPreOrder$0$comgooglecommongraphTraverser(immutableSetValidate);
            }
        };
    }

    /* renamed from: lambda$depthFirstPreOrder$0$com-google-common-graph-Traverser, reason: not valid java name */
    /* synthetic */ Iterator m308lambda$depthFirstPreOrder$0$comgooglecommongraphTraverser(ImmutableSet immutableSet) {
        return newTraversal().preOrder(immutableSet.iterator());
    }

    public final Iterable<N> depthFirstPostOrder(N startNode) {
        return depthFirstPostOrder((Iterable) ImmutableSet.of(startNode));
    }

    public final Iterable<N> depthFirstPostOrder(Iterable<? extends N> startNodes) {
        final ImmutableSet<N> immutableSetValidate = validate(startNodes);
        return new Iterable() { // from class: com.google.common.graph.Traverser$$ExternalSyntheticLambda2
            @Override // java.lang.Iterable
            public final Iterator iterator() {
                return this.f$0.m307lambda$depthFirstPostOrder$0$comgooglecommongraphTraverser(immutableSetValidate);
            }
        };
    }

    /* renamed from: lambda$depthFirstPostOrder$0$com-google-common-graph-Traverser, reason: not valid java name */
    /* synthetic */ Iterator m307lambda$depthFirstPostOrder$0$comgooglecommongraphTraverser(ImmutableSet immutableSet) {
        return newTraversal().postOrder(immutableSet.iterator());
    }

    private ImmutableSet<N> validate(Iterable<? extends N> startNodes) {
        ImmutableSet<N> immutableSetCopyOf = ImmutableSet.copyOf(startNodes);
        UnmodifiableIterator<N> it = immutableSetCopyOf.iterator();
        while (it.hasNext()) {
            this.successorFunction.successors(it.next());
        }
        return immutableSetCopyOf;
    }

    private static abstract class Traversal<N> {
        final SuccessorsFunction<N> successorFunction;

        abstract N visitNext(Deque<Iterator<? extends N>> horizon);

        Traversal(SuccessorsFunction<N> successorFunction) {
            this.successorFunction = successorFunction;
        }

        static <N> Traversal<N> inGraph(SuccessorsFunction<N> graph) {
            final HashSet hashSet = new HashSet();
            return new Traversal<N>(graph) { // from class: com.google.common.graph.Traverser.Traversal.1
                @Override // com.google.common.graph.Traverser.Traversal
                N visitNext(Deque<Iterator<? extends N>> horizon) {
                    Iterator<? extends N> first = horizon.getFirst();
                    while (first.hasNext()) {
                        N next = first.next();
                        Objects.requireNonNull(next);
                        if (hashSet.add(next)) {
                            return next;
                        }
                    }
                    horizon.removeFirst();
                    return null;
                }
            };
        }

        static <N> Traversal<N> inTree(SuccessorsFunction<N> tree) {
            return new Traversal<N>(tree) { // from class: com.google.common.graph.Traverser.Traversal.2
                @Override // com.google.common.graph.Traverser.Traversal
                N visitNext(Deque<Iterator<? extends N>> deque) {
                    Iterator<? extends N> first = deque.getFirst();
                    if (first.hasNext()) {
                        return (N) Preconditions.checkNotNull(first.next());
                    }
                    deque.removeFirst();
                    return null;
                }
            };
        }

        final Iterator<N> breadthFirst(Iterator<? extends N> startNodes) {
            return topDown(startNodes, InsertionOrder.BACK);
        }

        final Iterator<N> preOrder(Iterator<? extends N> startNodes) {
            return topDown(startNodes, InsertionOrder.FRONT);
        }

        private Iterator<N> topDown(Iterator<? extends N> startNodes, final InsertionOrder order) {
            final ArrayDeque arrayDeque = new ArrayDeque();
            arrayDeque.add(startNodes);
            return new AbstractIterator<N>(this) { // from class: com.google.common.graph.Traverser.Traversal.3
                final /* synthetic */ Traversal this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.common.collect.AbstractIterator
                protected N computeNext() {
                    do {
                        N n = (N) this.this$0.visitNext(arrayDeque);
                        if (n != null) {
                            Iterator<? extends N> it = this.this$0.successorFunction.successors(n).iterator();
                            if (it.hasNext()) {
                                order.insertInto(arrayDeque, it);
                            }
                            return n;
                        }
                    } while (!arrayDeque.isEmpty());
                    return endOfData();
                }
            };
        }

        final Iterator<N> postOrder(Iterator<? extends N> startNodes) {
            final ArrayDeque arrayDeque = new ArrayDeque();
            final ArrayDeque arrayDeque2 = new ArrayDeque();
            arrayDeque2.add(startNodes);
            return new AbstractIterator<N>(this) { // from class: com.google.common.graph.Traverser.Traversal.4
                final /* synthetic */ Traversal this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.common.collect.AbstractIterator
                protected N computeNext() {
                    while (true) {
                        N n = (N) this.this$0.visitNext(arrayDeque2);
                        if (n != null) {
                            Iterator<? extends N> it = this.this$0.successorFunction.successors(n).iterator();
                            if (!it.hasNext()) {
                                return n;
                            }
                            arrayDeque2.addFirst(it);
                            arrayDeque.push(n);
                        } else {
                            if (!arrayDeque.isEmpty()) {
                                return (N) arrayDeque.pop();
                            }
                            return endOfData();
                        }
                    }
                }
            };
        }
    }
}
