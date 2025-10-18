package com.google.android.gms.gcm;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public final class zzh extends com.google.android.gms.internal.gcm.zzd implements zzg {
    zzh(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.gcm.INetworkTaskCallback");
    }

    @Override // com.google.android.gms.gcm.zzg
    public final void zzf(int i) throws RemoteException {
        Parcel parcelZzd = zzd();
        parcelZzd.writeInt(i);
        zzd(2, parcelZzd);
    }
}
