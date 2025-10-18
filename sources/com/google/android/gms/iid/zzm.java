package com.google.android.gms.iid;

import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.cloudmessaging.IMessengerCompat;

/* loaded from: classes.dex */
public final class zzm extends com.google.android.gms.internal.gcm.zzd implements zzl {
    zzm(IBinder iBinder) {
        super(iBinder, IMessengerCompat.DESCRIPTOR);
    }

    @Override // com.google.android.gms.iid.zzl
    public final void send(Message message) throws RemoteException {
        Parcel parcelZzd = zzd();
        com.google.android.gms.internal.gcm.zze.zzd(parcelZzd, message);
        zze(1, parcelZzd);
    }
}
