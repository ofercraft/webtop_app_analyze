package com.google.android.gms.gcm;

import android.os.Trace;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.iid.zzai;
import com.google.android.gms.iid.zzaj;
import java.io.Closeable;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* loaded from: classes.dex */
public final class zzp implements Closeable {
    private static final zzaj<Boolean> zzba = zzai.zzy().zzd("nts.enable_tracing", true);
    private final boolean enabled;

    public zzp(String str) {
        boolean z = PlatformVersion.isAtLeastJellyBeanMR2() && zzba.get().booleanValue();
        this.enabled = z;
        if (z) {
            Trace.beginSection(str.length() > 127 ? str.substring(0, WorkQueueKt.MASK) : str);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        if (this.enabled) {
            Trace.endSection();
        }
    }
}
