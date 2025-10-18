package com.google.android.gms.iid;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
abstract class zzz<T> {
    final int what;
    final int zzcp;
    final TaskCompletionSource<T> zzcq = new TaskCompletionSource<>();
    final Bundle zzcr;

    zzz(int i, int i2, Bundle bundle) {
        this.zzcp = i;
        this.what = i2;
        this.zzcr = bundle;
    }

    abstract void zzh(Bundle bundle);

    abstract boolean zzw();

    final void zzd(zzaa zzaaVar) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String strValueOf = String.valueOf(this);
            String strValueOf2 = String.valueOf(zzaaVar);
            Log.d("MessengerIpcClient", new StringBuilder(String.valueOf(strValueOf).length() + 14 + String.valueOf(strValueOf2).length()).append("Failing ").append(strValueOf).append(" with ").append(strValueOf2).toString());
        }
        this.zzcq.setException(zzaaVar);
    }

    public String toString() {
        int i = this.what;
        int i2 = this.zzcp;
        zzw();
        return new StringBuilder(55).append("Request { what=").append(i).append(" id=").append(i2).append(" oneWay=false}").toString();
    }
}
