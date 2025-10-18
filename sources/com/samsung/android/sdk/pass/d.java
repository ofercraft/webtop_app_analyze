package com.samsung.android.sdk.pass;

import android.util.Log;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.sdk.pass.SpassFingerprint;

/* loaded from: classes2.dex */
final class d implements FingerprintManager.EnrollFinishListener {
    private final /* synthetic */ SpassFingerprint.RegisterListener a;

    d(SpassFingerprint.RegisterListener registerListener) {
        this.a = registerListener;
    }

    public final void onEnrollFinish() {
        Log.d(SpassFingerprint.TAG, "onEnrollFinish called");
        this.a.onFinished();
    }
}
