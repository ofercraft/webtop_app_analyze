package com.google.common.graph;

import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
interface GraphConnections<N, V> {
    void addPredecessor(N node, V value);

    V addSuccessor(N node, V value);

    Set<N> adjacentNodes();

    Iterator<EndpointPair<N>> incidentEdgeIterator(N thisNode);

    Set<N> predecessors();

    void removePredecessor(N node);

    V removeSuccessor(N node);

    Set<N> successors();

    V value(N node);
}
