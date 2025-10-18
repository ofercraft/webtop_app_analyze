package com.google.android.gms.internal.cloudmessaging;

import android.os.Handler;
import android.os.Looper;

/* compiled from: com.google.android.gms:play-services-cloud-messaging@@17.2.0 */
/* loaded from: classes.dex */
public class zzf extends Handler {
    private final Looper zza;

    public zzf() {
        this.zza = Looper.getMainLooper();
    }

    public zzf(Looper looper) {
        super(looper);
        this.zza = Looper.getMainLooper();
    }

    public zzf(Looper looper, Handler.Callback callback) {
        super(looper, callback);
        this.zza = Looper.getMainLooper();
    }
}
