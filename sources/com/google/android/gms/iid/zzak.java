package com.google.android.gms.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes.dex */
public final class zzak {
    private SharedPreferences zzde;
    private final zzn zzdf;
    private final Map<String, zzo> zzdg;
    private Context zzl;

    public zzak(Context context) {
        this(context, new zzn());
    }

    private zzak(Context context, zzn zznVar) {
        this.zzdg = new ArrayMap();
        this.zzl = context;
        this.zzde = context.getSharedPreferences("com.google.android.gms.appid", 0);
        this.zzdf = zznVar;
        File file = new File(ContextCompat.getNoBackupFilesDir(this.zzl), "com.google.android.gms.appid-no-backup");
        if (file.exists()) {
            return;
        }
        try {
            if (!file.createNewFile() || isEmpty()) {
                return;
            }
            Log.i("InstanceID/Store", "App restored, clearing state");
            InstanceIDListenerService.zzd(this.zzl, this);
        } catch (IOException e) {
            if (Log.isLoggable("InstanceID/Store", 3)) {
                String strValueOf = String.valueOf(e.getMessage());
                Log.d("InstanceID/Store", strValueOf.length() != 0 ? "Error creating file in no backup dir: ".concat(strValueOf) : new String("Error creating file in no backup dir: "));
            }
        }
    }

    public final boolean isEmpty() {
        return this.zzde.getAll().isEmpty();
    }

    private static String zzd(String str, String str2, String str3) {
        return new StringBuilder(String.valueOf(str).length() + 4 + String.valueOf(str2).length() + String.valueOf(str3).length()).append(str).append("|T|").append(str2).append("|").append(str3).toString();
    }

    private static String zze(String str, String str2, String str3) {
        return new StringBuilder(String.valueOf(str).length() + 14 + String.valueOf(str2).length() + String.valueOf(str3).length()).append(str).append("|T-timestamp|").append(str2).append("|").append(str3).toString();
    }

    final synchronized String get(String str) {
        return this.zzde.getString(str, null);
    }

    public final synchronized void zzi(String str) {
        SharedPreferences.Editor editorEdit = this.zzde.edit();
        for (String str2 : this.zzde.getAll().keySet()) {
            if (str2.startsWith(str)) {
                editorEdit.remove(str2);
            }
        }
        editorEdit.commit();
    }

    public final synchronized void zzz() {
        this.zzdg.clear();
        zzn.zzi(this.zzl);
        this.zzde.edit().clear().commit();
    }

    public final synchronized String zzf(String str, String str2, String str3) {
        return this.zzde.getString(zzd(str, str2, str3), null);
    }

    final synchronized long zzg(String str, String str2, String str3) {
        return this.zzde.getLong(zze(str, str2, str3), -1L);
    }

    public final synchronized void zzd(String str, String str2, String str3, String str4, String str5) {
        String strZzd = zzd(str, str2, str3);
        String strZze = zze(str, str2, str3);
        SharedPreferences.Editor editorEdit = this.zzde.edit();
        editorEdit.putString(strZzd, str4);
        editorEdit.putLong(strZze, System.currentTimeMillis());
        editorEdit.putString("appVersion", str5);
        editorEdit.commit();
    }

    public final synchronized void zzh(String str, String str2, String str3) {
        SharedPreferences.Editor editorEdit = this.zzde.edit();
        editorEdit.remove(zzd(str, str2, str3));
        editorEdit.remove(zze(str, str2, str3));
        editorEdit.commit();
    }

    public final synchronized zzo zzj(String str) {
        zzo zzoVarZzf;
        zzo zzoVar = this.zzdg.get(str);
        if (zzoVar != null) {
            return zzoVar;
        }
        try {
            zzoVarZzf = this.zzdf.zze(this.zzl, str);
        } catch (zzp unused) {
            Log.w("InstanceID/Store", "Stored data is corrupt, generating new identity");
            InstanceIDListenerService.zzd(this.zzl, this);
            zzoVarZzf = this.zzdf.zzf(this.zzl, str);
        }
        this.zzdg.put(str, zzoVarZzf);
        return zzoVarZzf;
    }

    final void zzk(String str) {
        synchronized (this) {
            this.zzdg.remove(str);
        }
        zzn.zzg(this.zzl, str);
        zzi(String.valueOf(str).concat("|"));
    }

    static String zzh(String str, String str2) {
        return new StringBuilder(String.valueOf(str).length() + 3 + String.valueOf(str2).length()).append(str).append("|S|").append(str2).toString();
    }
}
