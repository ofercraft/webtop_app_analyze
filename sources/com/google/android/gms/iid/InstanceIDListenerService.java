package com.google.android.gms.iid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

@Deprecated
/* loaded from: classes.dex */
public class InstanceIDListenerService extends zze {
    public void onTokenRefresh() {
    }

    @Override // com.google.android.gms.iid.zze
    public void handleIntent(Intent intent) {
        Bundle bundle;
        if ("com.google.android.gms.iid.InstanceID".equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("subtype");
            if (stringExtra != null) {
                bundle = new Bundle();
                bundle.putString("subtype", stringExtra);
            } else {
                bundle = null;
            }
            InstanceID instanceID = InstanceID.getInstance(this, bundle);
            String stringExtra2 = intent.getStringExtra("CMD");
            if (Log.isLoggable("InstanceID", 3)) {
                Log.d("InstanceID", new StringBuilder(String.valueOf(stringExtra).length() + 34 + String.valueOf(stringExtra2).length()).append("Service command. subtype:").append(stringExtra).append(" command:").append(stringExtra2).toString());
            }
            if ("RST".equals(stringExtra2)) {
                instanceID.zzo();
                onTokenRefresh();
                return;
            }
            if ("RST_FULL".equals(stringExtra2)) {
                if (InstanceID.zzp().isEmpty()) {
                    return;
                }
                InstanceID.zzp().zzz();
                onTokenRefresh();
                return;
            }
            if ("SYNC".equals(stringExtra2)) {
                zzak zzakVarZzp = InstanceID.zzp();
                String strValueOf = String.valueOf(stringExtra);
                String strValueOf2 = String.valueOf("|T|");
                zzakVarZzp.zzi(strValueOf2.length() != 0 ? strValueOf.concat(strValueOf2) : new String(strValueOf));
                String strValueOf3 = String.valueOf(stringExtra);
                String strValueOf4 = String.valueOf("|T-timestamp|");
                zzakVarZzp.zzi(strValueOf4.length() != 0 ? strValueOf3.concat(strValueOf4) : new String(strValueOf3));
                onTokenRefresh();
            }
        }
    }

    static void zzd(Context context, zzak zzakVar) {
        zzakVar.zzz();
        Intent intent = new Intent("com.google.android.gms.iid.InstanceID");
        intent.putExtra("CMD", "RST");
        intent.setClassName(context, "com.google.android.gms.gcm.GcmReceiver");
        context.sendBroadcast(intent);
    }
}
