package com.google.android.gms.internal.gcm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
final class zzt extends WeakReference<Throwable> {
    private final int zzdv;

    public zzt(Throwable th, ReferenceQueue<Throwable> referenceQueue) {
        super(th, referenceQueue);
        if (th == null) {
            throw new NullPointerException("The referent cannot be null");
        }
        this.zzdv = System.identityHashCode(th);
    }

    public final int hashCode() {
        return this.zzdv;
    }

    public final boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            if (this == obj) {
                return true;
            }
            zzt zztVar = (zzt) obj;
            if (this.zzdv == zztVar.zzdv && get() == zztVar.get()) {
                return true;
            }
        }
        return false;
    }
}
