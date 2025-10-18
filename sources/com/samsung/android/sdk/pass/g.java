package com.samsung.android.sdk.pass;

import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.sdk.pass.SpassFingerprint;

/* loaded from: classes2.dex */
final class g implements Runnable {
    private /* synthetic */ SpassFingerprint.c a;
    private final /* synthetic */ FingerprintEvent b;
    private final /* synthetic */ SpassFingerprint.IdentifyListener c;

    g(SpassFingerprint.c cVar, FingerprintEvent fingerprintEvent, SpassFingerprint.IdentifyListener identifyListener) {
        this.a = cVar;
        this.b = fingerprintEvent;
        this.c = identifyListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.b.eventId == 13) {
            SpassFingerprint.a(SpassFingerprint.this, this.c, this.b, -1);
            this.c.onCompleted();
        }
    }
}
