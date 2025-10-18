package com.google.android.gms.gcm;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.firebase.messaging.Constants;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes.dex */
final class zzd {
    static zzd zzk;
    private final Context zzl;
    private String zzm;
    private final AtomicInteger zzn = new AtomicInteger((int) SystemClock.elapsedRealtime());

    static synchronized zzd zzd(Context context) {
        if (zzk == null) {
            zzk = new zzd(context);
        }
        return zzk;
    }

    static String zzd(Bundle bundle, String str) {
        String string = bundle.getString(str);
        return string == null ? bundle.getString(str.replace(Constants.MessageNotificationKeys.NOTIFICATION_PREFIX, Constants.MessageNotificationKeys.NOTIFICATION_PREFIX_OLD)) : string;
    }

    private zzd(Context context) {
        this.zzl = context.getApplicationContext();
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x019e  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01d1  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0233  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0256  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x026e  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0283  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final boolean zze(android.os.Bundle r14) {
        /*
            Method dump skipped, instructions count: 673
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.gcm.zzd.zze(android.os.Bundle):boolean");
    }

    private final String zze(Bundle bundle, String str) {
        String strZzd = zzd(bundle, str);
        if (!TextUtils.isEmpty(strZzd)) {
            return strZzd;
        }
        String strValueOf = String.valueOf(str);
        String strValueOf2 = String.valueOf(Constants.MessageNotificationKeys.TEXT_RESOURCE_SUFFIX);
        String strZzd2 = zzd(bundle, strValueOf2.length() != 0 ? strValueOf.concat(strValueOf2) : new String(strValueOf));
        if (TextUtils.isEmpty(strZzd2)) {
            return null;
        }
        Resources resources = this.zzl.getResources();
        int identifier = resources.getIdentifier(strZzd2, "string", this.zzl.getPackageName());
        if (identifier == 0) {
            String strValueOf3 = String.valueOf(str);
            String strValueOf4 = String.valueOf(Constants.MessageNotificationKeys.TEXT_RESOURCE_SUFFIX);
            String strSubstring = (strValueOf4.length() != 0 ? strValueOf3.concat(strValueOf4) : new String(strValueOf3)).substring(6);
            Log.w("GcmNotification", new StringBuilder(String.valueOf(strSubstring).length() + 49 + String.valueOf(strZzd2).length()).append(strSubstring).append(" resource not found: ").append(strZzd2).append(" Default value will be used.").toString());
            return null;
        }
        String strValueOf5 = String.valueOf(str);
        String strValueOf6 = String.valueOf(Constants.MessageNotificationKeys.TEXT_ARGS_SUFFIX);
        String strZzd3 = zzd(bundle, strValueOf6.length() != 0 ? strValueOf5.concat(strValueOf6) : new String(strValueOf5));
        if (TextUtils.isEmpty(strZzd3)) {
            return resources.getString(identifier);
        }
        try {
            JSONArray jSONArray = new JSONArray(strZzd3);
            int length = jSONArray.length();
            Object[] objArr = new String[length];
            for (int i = 0; i < length; i++) {
                objArr[i] = jSONArray.opt(i);
            }
            return resources.getString(identifier, objArr);
        } catch (MissingFormatArgumentException e) {
            Log.w("GcmNotification", new StringBuilder(String.valueOf(strZzd2).length() + 58 + String.valueOf(strZzd3).length()).append("Missing format argument for ").append(strZzd2).append(": ").append(strZzd3).append(" Default value will be used.").toString(), e);
            return null;
        } catch (JSONException unused) {
            String strValueOf7 = String.valueOf(str);
            String strValueOf8 = String.valueOf(Constants.MessageNotificationKeys.TEXT_ARGS_SUFFIX);
            String strSubstring2 = (strValueOf8.length() != 0 ? strValueOf7.concat(strValueOf8) : new String(strValueOf7)).substring(6);
            Log.w("GcmNotification", new StringBuilder(String.valueOf(strSubstring2).length() + 41 + String.valueOf(strZzd3).length()).append("Malformed ").append(strSubstring2).append(": ").append(strZzd3).append("  Default value will be used.").toString());
            return null;
        }
    }

    private final Bundle zzf() throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = this.zzl.getPackageManager().getApplicationInfo(this.zzl.getPackageName(), 128);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        if (applicationInfo != null && applicationInfo.metaData != null) {
            return applicationInfo.metaData;
        }
        return Bundle.EMPTY;
    }
}
