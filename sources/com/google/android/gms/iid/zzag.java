package com.google.android.gms.iid;

import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
final class zzag extends com.google.android.gms.internal.gcm.zzj {
    private final /* synthetic */ zzaf zzdc;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzag(zzaf zzafVar, Looper looper) {
        super(looper);
        this.zzdc = zzafVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        this.zzdc.zze(message);
    }
}
