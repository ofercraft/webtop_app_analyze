package com.samsung.android.sdk.pass;

import android.content.DialogInterface;
import com.samsung.android.sdk.pass.SpassFingerprint;

/* loaded from: classes2.dex */
final class c implements DialogInterface.OnDismissListener {
    private final /* synthetic */ SpassFingerprint.c a;

    c(SpassFingerprint.c cVar) {
        this.a = cVar;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.a.a();
    }
}
