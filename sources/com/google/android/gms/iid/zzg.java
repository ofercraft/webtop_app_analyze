package com.google.android.gms.iid;

import android.content.BroadcastReceiver;
import android.content.Intent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
final class zzg {
    final Intent intent;
    private final BroadcastReceiver.PendingResult zzbi;
    private boolean zzbj = false;
    private final ScheduledFuture<?> zzbk;

    zzg(Intent intent, BroadcastReceiver.PendingResult pendingResult, ScheduledExecutorService scheduledExecutorService) {
        this.intent = intent;
        this.zzbi = pendingResult;
        this.zzbk = scheduledExecutorService.schedule(new zzh(this, intent), 9500L, TimeUnit.MILLISECONDS);
    }

    final synchronized void finish() {
        if (!this.zzbj) {
            this.zzbi.finish();
            this.zzbk.cancel(false);
            this.zzbj = true;
        }
    }
}
