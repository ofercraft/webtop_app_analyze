package com.google.android.gms.iid;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.common.base.Ascii;
import dev.skomlach.biometric.compat.engine.internal.face.oppo.OppoFaceUnlockModule;
import java.io.IOException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Deprecated
/* loaded from: classes.dex */
public class InstanceID {
    public static final String ERROR_MAIN_THREAD = "MAIN_THREAD";
    public static final String ERROR_MISSING_INSTANCEID_SERVICE = "MISSING_INSTANCEID_SERVICE";
    public static final String ERROR_SERVICE_NOT_AVAILABLE = "SERVICE_NOT_AVAILABLE";
    public static final String ERROR_TIMEOUT = "TIMEOUT";
    private static final zzaj<Boolean> zzbu = zzai.zzy().zzd("gcm_check_for_different_iid_in_token", true);
    private static Map<String, InstanceID> zzbv = new ArrayMap();
    private static final long zzbw = TimeUnit.DAYS.toMillis(7);
    private static zzak zzbx;
    private static zzaf zzby;
    private static String zzbz;
    private String zzca;
    private Context zzl;

    private InstanceID(Context context, String str) {
        this.zzca = "";
        this.zzl = context.getApplicationContext();
        this.zzca = str;
    }

    static int zzg(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            String strValueOf = String.valueOf(e);
            Log.w("InstanceID", new StringBuilder(String.valueOf(strValueOf).length() + 38).append("Never happens: can't find own package ").append(strValueOf).toString());
            return 0;
        }
    }

    static String zzh(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            String strValueOf = String.valueOf(e);
            Log.w("InstanceID", new StringBuilder(String.valueOf(strValueOf).length() + 38).append("Never happens: can't find own package ").append(strValueOf).toString());
            return null;
        }
    }

    @Deprecated
    public static InstanceID getInstance(Context context) {
        return getInstance(context, null);
    }

    public static synchronized InstanceID getInstance(Context context, Bundle bundle) {
        InstanceID instanceID;
        String string = bundle == null ? "" : bundle.getString("subtype");
        if (string == null) {
            string = "";
        }
        Context applicationContext = context.getApplicationContext();
        if (zzbx == null) {
            String packageName = applicationContext.getPackageName();
            Log.w("InstanceID", new StringBuilder(String.valueOf(packageName).length() + 73).append("Instance ID SDK is deprecated, ").append(packageName).append(" should update to use Firebase Instance ID").toString());
            zzbx = new zzak(applicationContext);
            zzby = new zzaf(applicationContext);
        }
        zzbz = Integer.toString(zzg(applicationContext));
        instanceID = zzbv.get(string);
        if (instanceID == null) {
            instanceID = new InstanceID(applicationContext, string);
            zzbv.put(string, instanceID);
        }
        return instanceID;
    }

    private final KeyPair getKeyPair() {
        return zzbx.zzj(this.zzca).getKeyPair();
    }

    public String getSubtype() {
        return this.zzca;
    }

    @Deprecated
    public String getId() {
        return zzd(getKeyPair());
    }

    static String zzd(KeyPair keyPair) {
        try {
            byte[] bArrDigest = MessageDigest.getInstance("SHA1").digest(keyPair.getPublic().getEncoded());
            bArrDigest[0] = (byte) ((bArrDigest[0] & Ascii.SI) + OppoFaceUnlockModule.FACE_ACQUIRED_NO_FOCUS);
            return Base64.encodeToString(bArrDigest, 0, 8, 11);
        } catch (NoSuchAlgorithmException unused) {
            Log.w("InstanceID", "Unexpected error, device missing required algorithms");
            return null;
        }
    }

    @Deprecated
    public long getCreationTime() {
        return zzbx.zzj(this.zzca).getCreationTime();
    }

    @Deprecated
    public void deleteInstanceID() throws IOException {
        zzd("*", "*", null);
        zzo();
    }

    final void zzo() {
        zzbx.zzk(this.zzca);
    }

    @Deprecated
    public void deleteToken(String str, String str2) throws IOException {
        zzd(str, str2, null);
    }

    public final void zzd(String str, String str2, Bundle bundle) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        zzbx.zzh(this.zzca, str, str2);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("sender", str);
        if (str2 != null) {
            bundle.putString("scope", str2);
        }
        bundle.putString("subscription", str);
        bundle.putString("delete", "1");
        bundle.putString("X-delete", "1");
        bundle.putString("subtype", "".equals(this.zzca) ? str : this.zzca);
        if (!"".equals(this.zzca)) {
            str = this.zzca;
        }
        bundle.putString("X-subtype", str);
        zzaf.zzi(zzby.zzd(bundle, getKeyPair()));
    }

    public static zzak zzp() {
        return zzbx;
    }

    @Deprecated
    public String getToken(String str, String str2) throws IOException {
        return getToken(str, str2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0041  */
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getToken(java.lang.String r7, java.lang.String r8, android.os.Bundle r9) throws java.io.IOException {
        /*
            r6 = this;
            android.os.Looper r0 = android.os.Looper.getMainLooper()
            android.os.Looper r1 = android.os.Looper.myLooper()
            if (r0 == r1) goto L96
            com.google.android.gms.iid.zzak r0 = com.google.android.gms.iid.InstanceID.zzbx
            java.lang.String r1 = "appVersion"
            java.lang.String r0 = r0.get(r1)
            if (r0 == 0) goto L41
            java.lang.String r1 = com.google.android.gms.iid.InstanceID.zzbz
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L1d
            goto L41
        L1d:
            com.google.android.gms.iid.zzak r0 = com.google.android.gms.iid.InstanceID.zzbx
            java.lang.String r1 = r6.zzca
            long r0 = r0.zzg(r1, r7, r8)
            r2 = 0
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L2c
            goto L41
        L2c:
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r0
            long r0 = com.google.android.gms.iid.InstanceID.zzbw
            int r0 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r0 < 0) goto L38
            goto L41
        L38:
            com.google.android.gms.iid.zzak r0 = com.google.android.gms.iid.InstanceID.zzbx
            java.lang.String r1 = r6.zzca
            java.lang.String r0 = r0.zzf(r1, r7, r8)
            goto L42
        L41:
            r0 = 0
        L42:
            if (r0 != 0) goto L95
            if (r9 != 0) goto L4b
            android.os.Bundle r9 = new android.os.Bundle
            r9.<init>()
        L4b:
            java.lang.String r4 = r6.zze(r7, r8, r9)
            com.google.android.gms.iid.zzaj<java.lang.Boolean> r9 = com.google.android.gms.iid.InstanceID.zzbu
            java.lang.Object r9 = r9.get()
            java.lang.Boolean r9 = (java.lang.Boolean) r9
            boolean r9 = r9.booleanValue()
            if (r9 == 0) goto L87
            java.lang.String r9 = ":"
            boolean r0 = r4.contains(r9)
            if (r0 == 0) goto L87
            java.lang.String r0 = r6.getId()
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.lang.String r9 = r0.concat(r9)
            boolean r9 = r4.startsWith(r9)
            if (r9 == 0) goto L78
            goto L87
        L78:
            android.content.Context r6 = r6.zzl
            com.google.android.gms.iid.zzak r7 = com.google.android.gms.iid.InstanceID.zzbx
            com.google.android.gms.iid.InstanceIDListenerService.zzd(r6, r7)
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r7 = "SERVICE_NOT_AVAILABLE"
            r6.<init>(r7)
            throw r6
        L87:
            if (r4 == 0) goto L94
            com.google.android.gms.iid.zzak r0 = com.google.android.gms.iid.InstanceID.zzbx
            java.lang.String r1 = r6.zzca
            java.lang.String r5 = com.google.android.gms.iid.InstanceID.zzbz
            r2 = r7
            r3 = r8
            r0.zzd(r1, r2, r3, r4, r5)
        L94:
            return r4
        L95:
            return r0
        L96:
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r7 = "MAIN_THREAD"
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.iid.InstanceID.getToken(java.lang.String, java.lang.String, android.os.Bundle):java.lang.String");
    }

    public final String zze(String str, String str2, Bundle bundle) throws IOException {
        if (str2 != null) {
            bundle.putString("scope", str2);
        }
        bundle.putString("sender", str);
        String str3 = "".equals(this.zzca) ? str : this.zzca;
        if (!bundle.containsKey("legacy.register")) {
            bundle.putString("subscription", str);
            bundle.putString("subtype", str3);
            bundle.putString("X-subscription", str);
            bundle.putString("X-subtype", str3);
        }
        String strZzi = zzaf.zzi(zzby.zzd(bundle, getKeyPair()));
        if (!"RST".equals(strZzi) && !strZzi.startsWith("RST|")) {
            return strZzi;
        }
        InstanceIDListenerService.zzd(this.zzl, zzbx);
        throw new IOException("SERVICE_NOT_AVAILABLE");
    }
}
