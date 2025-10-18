package com.google.android.gms.internal.gcm;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
public class zzj extends Handler {
    private static volatile zzk zzdj;

    public zzj() {
    }

    public zzj(Looper looper) {
        super(looper);
    }

    public zzj(Looper looper, Handler.Callback callback) {
        super(looper, callback);
    }

    @Override // android.os.Handler
    public final void dispatchMessage(Message message) {
        super.dispatchMessage(message);
    }
}
