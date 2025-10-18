package com.samsung.android.sdk.pass;

import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.sdk.pass.SpassFingerprint;

/* loaded from: classes2.dex */
final class f implements Runnable {
    private /* synthetic */ SpassFingerprint.c a;
    private final /* synthetic */ FingerprintEvent b;

    f(SpassFingerprint.c cVar, FingerprintEvent fingerprintEvent) {
        this.a = cVar;
        this.b = fingerprintEvent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.a.a != null) {
            int i = this.b.eventId;
            if (i == 11) {
                this.a.a.onReady();
            } else {
                if (i != 12) {
                    return;
                }
                this.a.a.onStarted();
            }
        }
    }
}
