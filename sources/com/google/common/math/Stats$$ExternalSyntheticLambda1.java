package com.google.common.math;

import java.util.function.ObjLongConsumer;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Stats$$ExternalSyntheticLambda1 implements ObjLongConsumer {
    @Override // java.util.function.ObjLongConsumer
    public final void accept(Object obj, long j) {
        ((StatsAccumulator) obj).add(j);
    }
}
