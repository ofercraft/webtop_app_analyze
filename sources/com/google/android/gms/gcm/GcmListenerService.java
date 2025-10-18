package com.google.android.gms.gcm;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import com.google.firebase.messaging.Constants;
import java.util.Iterator;
import java.util.List;

@Deprecated
/* loaded from: classes.dex */
public class GcmListenerService extends com.google.android.gms.iid.zze {
    private com.google.android.gms.internal.gcm.zzl zzg = com.google.android.gms.internal.gcm.zzm.zzdk;

    public void onDeletedMessages() {
    }

    public void onMessageReceived(String str, Bundle bundle) {
    }

    public void onMessageSent(String str) {
    }

    public void onSendError(String str, String str2) {
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        com.google.android.gms.internal.gcm.zzm.zzab();
        this.zzg = com.google.android.gms.internal.gcm.zzm.zzdk;
    }

    @Override // com.google.android.gms.iid.zze
    public void handleIntent(Intent intent) {
        String stringExtra;
        if (!"com.google.android.c2dm.intent.RECEIVE".equals(intent.getAction())) {
            String strValueOf = String.valueOf(intent.getAction());
            Log.w("GcmListenerService", strValueOf.length() != 0 ? "Unknown intent action: ".concat(strValueOf) : new String("Unknown intent action: "));
        }
        stringExtra = intent.getStringExtra(Constants.MessagePayloadKeys.MESSAGE_TYPE);
        if (stringExtra == null) {
            stringExtra = "gcm";
        }
        stringExtra.hashCode();
        switch (stringExtra) {
            case "deleted_messages":
                onDeletedMessages();
                break;
            case "gcm":
                Bundle extras = intent.getExtras();
                extras.remove(Constants.MessagePayloadKeys.MESSAGE_TYPE);
                extras.remove("androidx.contentpager.content.wakelockid");
                if ("1".equals(zzd.zzd(extras, Constants.MessageNotificationKeys.ENABLE_NOTIFICATION)) || zzd.zzd(extras, Constants.MessageNotificationKeys.ICON) != null) {
                    if (!((KeyguardManager) getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
                        int iMyPid = Process.myPid();
                        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) getSystemService("activity")).getRunningAppProcesses();
                        if (runningAppProcesses != null) {
                            Iterator<ActivityManager.RunningAppProcessInfo> it = runningAppProcesses.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    ActivityManager.RunningAppProcessInfo next = it.next();
                                    if (next.pid == iMyPid) {
                                        if (next.importance == 100) {
                                            Bundle bundle = new Bundle();
                                            Iterator<String> it2 = extras.keySet().iterator();
                                            while (it2.hasNext()) {
                                                String next2 = it2.next();
                                                String string = extras.getString(next2);
                                                if (next2.startsWith(Constants.MessageNotificationKeys.NOTIFICATION_PREFIX_OLD)) {
                                                    next2 = next2.replace(Constants.MessageNotificationKeys.NOTIFICATION_PREFIX_OLD, Constants.MessageNotificationKeys.NOTIFICATION_PREFIX);
                                                }
                                                if (next2.startsWith(Constants.MessageNotificationKeys.NOTIFICATION_PREFIX)) {
                                                    if (!Constants.MessageNotificationKeys.ENABLE_NOTIFICATION.equals(next2)) {
                                                        bundle.putString(next2.substring(6), string);
                                                    }
                                                    it2.remove();
                                                }
                                            }
                                            String string2 = bundle.getString("sound2");
                                            if (string2 != null) {
                                                bundle.remove("sound2");
                                                bundle.putString("sound", string2);
                                            }
                                            if (!bundle.isEmpty()) {
                                                extras.putBundle("notification", bundle);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    zzd.zzd(this).zze(extras);
                    break;
                }
                String string3 = extras.getString(Constants.MessagePayloadKeys.FROM);
                extras.remove(Constants.MessagePayloadKeys.FROM);
                zzd(extras);
                this.zzg.zzl("onMessageReceived");
                onMessageReceived(string3, extras);
                break;
            case "send_error":
                String stringExtra2 = intent.getStringExtra(Constants.MessagePayloadKeys.MSGID);
                if (stringExtra2 == null) {
                    stringExtra2 = intent.getStringExtra(Constants.MessagePayloadKeys.MSGID_SERVER);
                }
                onSendError(stringExtra2, intent.getStringExtra(Constants.IPC_BUNDLE_KEY_SEND_ERROR));
                break;
            case "send_event":
                onMessageSent(intent.getStringExtra(Constants.MessagePayloadKeys.MSGID));
                break;
            default:
                String strValueOf2 = String.valueOf(stringExtra);
                Log.w("GcmListenerService", strValueOf2.length() != 0 ? "Received message with unknown type: ".concat(strValueOf2) : new String("Received message with unknown type: "));
                break;
        }
    }

    static void zzd(Bundle bundle) {
        Iterator<String> it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next != null && next.startsWith(Constants.MessagePayloadKeys.RESERVED_CLIENT_LIB_PREFIX)) {
                it.remove();
            }
        }
    }
}
