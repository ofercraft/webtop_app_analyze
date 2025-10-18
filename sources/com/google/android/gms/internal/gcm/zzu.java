package com.google.android.gms.internal.gcm;

/* loaded from: classes.dex */
final class zzu extends zzr {
    private final zzs zzdw = new zzs();

    zzu() {
    }

    @Override // com.google.android.gms.internal.gcm.zzr
    public final void zzd(Throwable th, Throwable th2) {
        if (th2 == th) {
            throw new IllegalArgumentException("Self suppression is not allowed.", th2);
        }
        if (th2 == null) {
            throw new NullPointerException("The suppressed exception cannot be null.");
        }
        this.zzdw.zzd(th, true).add(th2);
    }
}
