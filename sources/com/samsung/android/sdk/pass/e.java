package com.samsung.android.sdk.pass;

import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.sdk.pass.SpassFingerprint;

/* loaded from: classes2.dex */
final class e implements Runnable {
    private /* synthetic */ SpassFingerprint.b a;
    private final /* synthetic */ FingerprintEvent b;
    private final /* synthetic */ SpassFingerprint.IdentifyListener c;

    e(SpassFingerprint.b bVar, FingerprintEvent fingerprintEvent, SpassFingerprint.IdentifyListener identifyListener) {
        this.a = bVar;
        this.b = fingerprintEvent;
        this.c = identifyListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i = this.b.eventId;
        if (i == 16) {
            if (SpassFingerprint.o) {
                this.c.onCompleted();
            }
        } else {
            if (i == 100000) {
                this.c.onFinished(7);
                this.c.onCompleted();
                return;
            }
            switch (i) {
                case 11:
                    this.c.onReady();
                    break;
                case 12:
                    this.c.onStarted();
                    break;
                case 13:
                    SpassFingerprint.a(SpassFingerprint.this, this.c, this.b, -1);
                    if (!SpassFingerprint.o) {
                        this.c.onCompleted();
                        break;
                    }
                    break;
            }
        }
    }
}
