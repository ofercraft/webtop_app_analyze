package com.google.android.gms.iid;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.messaging.Constants;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class zzaf {
    private PendingIntent zzaf;
    private Messenger zzaj;
    private Map<String, Object> zzcz = new ArrayMap();
    private Messenger zzda;
    private MessengerCompat zzdb;
    private Context zzl;
    private static final zzaj<Boolean> zzct = zzai.zzy().zzd("gcm_iid_use_messenger_ipc", true);
    private static String zzcu = null;
    private static boolean zzcv = false;
    private static int zzcw = 0;
    private static int zzcx = 0;
    private static int zzcp = 0;
    private static BroadcastReceiver zzcy = null;

    public zzaf(Context context) {
        this.zzl = context;
    }

    public static boolean zzk(Context context) {
        if (zzcu != null) {
            zzl(context);
        }
        return zzcv;
    }

    public static String zzl(Context context) {
        String str = zzcu;
        if (str != null) {
            return str;
        }
        zzcw = Process.myUid();
        PackageManager packageManager = context.getPackageManager();
        if (!PlatformVersion.isAtLeastO()) {
            Iterator<ResolveInfo> it = packageManager.queryIntentServices(new Intent("com.google.android.c2dm.intent.REGISTER"), 0).iterator();
            while (it.hasNext()) {
                if (zzd(packageManager, it.next().serviceInfo.packageName, "com.google.android.c2dm.intent.REGISTER")) {
                    zzcv = false;
                    return zzcu;
                }
            }
        }
        Iterator<ResolveInfo> it2 = packageManager.queryBroadcastReceivers(new Intent("com.google.iid.TOKEN_REQUEST"), 0).iterator();
        while (it2.hasNext()) {
            if (zzd(packageManager, it2.next().activityInfo.packageName, "com.google.iid.TOKEN_REQUEST")) {
                zzcv = true;
                return zzcu;
            }
        }
        Log.w("InstanceID", "Failed to resolve IID implementation package, falling back");
        if (zzd(packageManager, "com.google.android.gms")) {
            zzcv = PlatformVersion.isAtLeastO();
            return zzcu;
        }
        if (!PlatformVersion.isAtLeastLollipop() && zzd(packageManager, "com.google.android.gsf")) {
            zzcv = false;
            return zzcu;
        }
        Log.w("InstanceID", "Google Play services is missing, unable to get tokens");
        return null;
    }

    private static boolean zzd(PackageManager packageManager, String str, String str2) {
        if (packageManager.checkPermission("com.google.android.c2dm.permission.SEND", str) == 0) {
            return zzd(packageManager, str);
        }
        Log.w("InstanceID", new StringBuilder(String.valueOf(str).length() + 56 + String.valueOf(str2).length()).append("Possible malicious package ").append(str).append(" declares ").append(str2).append(" without permission").toString());
        return false;
    }

    private static boolean zzd(PackageManager packageManager, String str) throws PackageManager.NameNotFoundException {
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
            zzcu = applicationInfo.packageName;
            zzcx = applicationInfo.uid;
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private static int zzm(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(zzl(context), 0).versionCode;
        } catch (PackageManager.NameNotFoundException unused) {
            return -1;
        }
    }

    public final void zze(Message message) {
        if (message == null) {
            return;
        }
        if (message.obj instanceof Intent) {
            Intent intent = (Intent) message.obj;
            intent.setExtrasClassLoader(MessengerCompat.class.getClassLoader());
            if (intent.hasExtra("google.messenger")) {
                Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
                if (parcelableExtra instanceof MessengerCompat) {
                    this.zzdb = (MessengerCompat) parcelableExtra;
                }
                if (parcelableExtra instanceof Messenger) {
                    this.zzda = (Messenger) parcelableExtra;
                }
            }
            zzh((Intent) message.obj);
            return;
        }
        Log.w("InstanceID", "Dropping invalid message");
    }

    private final synchronized void zzg(Intent intent) {
        if (this.zzaf == null) {
            Intent intent2 = new Intent();
            intent2.setPackage("com.google.example.invalidpackage");
            this.zzaf = PendingIntent.getBroadcast(this.zzl, 0, intent2, 0);
        }
        intent.putExtra("app", this.zzaf);
    }

    static String zzi(Bundle bundle) throws IOException {
        if (bundle == null) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
        String string = bundle.getString("registration_id");
        if (string == null) {
            string = bundle.getString("unregistered");
        }
        if (string != null) {
            return string;
        }
        String string2 = bundle.getString(Constants.IPC_BUNDLE_KEY_SEND_ERROR);
        if (string2 != null) {
            throw new IOException(string2);
        }
        String strValueOf = String.valueOf(bundle);
        Log.w("InstanceID", new StringBuilder(String.valueOf(strValueOf).length() + 29).append("Unexpected response from GCM ").append(strValueOf).toString(), new Throwable());
        throw new IOException("SERVICE_NOT_AVAILABLE");
    }

    private final void zzd(String str, Object obj) {
        synchronized (getClass()) {
            Object obj2 = this.zzcz.get(str);
            this.zzcz.put(str, obj);
            zzd(obj2, obj);
        }
    }

    private static void zzd(Object obj, Object obj2) throws RemoteException {
        if (obj instanceof ConditionVariable) {
            ((ConditionVariable) obj).open();
        }
        if (obj instanceof Messenger) {
            Messenger messenger = (Messenger) obj;
            Message messageObtain = Message.obtain();
            messageObtain.obj = obj2;
            try {
                messenger.send(messageObtain);
            } catch (RemoteException e) {
                String strValueOf = String.valueOf(e);
                Log.w("InstanceID", new StringBuilder(String.valueOf(strValueOf).length() + 24).append("Failed to send response ").append(strValueOf).toString());
            }
        }
    }

    public final void zzh(Intent intent) {
        String strSubstring;
        if (intent == null) {
            if (Log.isLoggable("InstanceID", 3)) {
                Log.d("InstanceID", "Unexpected response: null");
                return;
            }
            return;
        }
        String action = intent.getAction();
        if (!"com.google.android.c2dm.intent.REGISTRATION".equals(action) && !"com.google.android.gms.iid.InstanceID".equals(action)) {
            if (Log.isLoggable("InstanceID", 3)) {
                String strValueOf = String.valueOf(intent.getAction());
                Log.d("InstanceID", strValueOf.length() != 0 ? "Unexpected response ".concat(strValueOf) : new String("Unexpected response "));
                return;
            }
            return;
        }
        String stringExtra = intent.getStringExtra("registration_id");
        if (stringExtra == null) {
            stringExtra = intent.getStringExtra("unregistered");
        }
        if (stringExtra == null) {
            String stringExtra2 = intent.getStringExtra(Constants.IPC_BUNDLE_KEY_SEND_ERROR);
            if (stringExtra2 == null) {
                String strValueOf2 = String.valueOf(intent.getExtras());
                Log.w("InstanceID", new StringBuilder(String.valueOf(strValueOf2).length() + 49).append("Unexpected response, no error or registration id ").append(strValueOf2).toString());
                return;
            }
            if (Log.isLoggable("InstanceID", 3)) {
                String strValueOf3 = String.valueOf(stringExtra2);
                Log.d("InstanceID", strValueOf3.length() != 0 ? "Received InstanceID error ".concat(strValueOf3) : new String("Received InstanceID error "));
            }
            String str = null;
            if (stringExtra2.startsWith("|")) {
                String[] strArrSplit = stringExtra2.split("\\|");
                if (!"ID".equals(strArrSplit[1])) {
                    String strValueOf4 = String.valueOf(stringExtra2);
                    Log.w("InstanceID", strValueOf4.length() != 0 ? "Unexpected structured response ".concat(strValueOf4) : new String("Unexpected structured response "));
                }
                if (strArrSplit.length > 2) {
                    String str2 = strArrSplit[2];
                    strSubstring = strArrSplit[3];
                    if (strSubstring.startsWith(":")) {
                        strSubstring = strSubstring.substring(1);
                    }
                    str = str2;
                } else {
                    strSubstring = "UNKNOWN";
                }
                stringExtra2 = strSubstring;
                intent.putExtra(Constants.IPC_BUNDLE_KEY_SEND_ERROR, stringExtra2);
            }
            if (str == null) {
                synchronized (getClass()) {
                    for (String str3 : this.zzcz.keySet()) {
                        Object obj = this.zzcz.get(str3);
                        this.zzcz.put(str3, stringExtra2);
                        zzd(obj, stringExtra2);
                    }
                }
                return;
            }
            zzd(str, (Object) stringExtra2);
            return;
        }
        Matcher matcher = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)").matcher(stringExtra);
        if (!matcher.matches()) {
            if (Log.isLoggable("InstanceID", 3)) {
                String strValueOf5 = String.valueOf(stringExtra);
                Log.d("InstanceID", strValueOf5.length() != 0 ? "Unexpected response string: ".concat(strValueOf5) : new String("Unexpected response string: "));
                return;
            }
            return;
        }
        String strGroup = matcher.group(1);
        String strGroup2 = matcher.group(2);
        Bundle extras = intent.getExtras();
        extras.putString("registration_id", strGroup2);
        zzd(strGroup, (Object) extras);
    }

    final Bundle zzd(Bundle bundle, KeyPair keyPair) throws IOException {
        int iZzm = zzm(this.zzl);
        bundle.putString("gmsv", Integer.toString(iZzm));
        bundle.putString("osv", Integer.toString(Build.VERSION.SDK_INT));
        bundle.putString("app_ver", Integer.toString(InstanceID.zzg(this.zzl)));
        bundle.putString("app_ver_name", InstanceID.zzh(this.zzl));
        bundle.putString("cliv", "iid-12451000");
        bundle.putString("appid", InstanceID.zzd(keyPair));
        if (iZzm >= 12000000 && zzct.get().booleanValue()) {
            try {
                return (Bundle) Tasks.await(new zzr(this.zzl).zzd(1, bundle));
            } catch (InterruptedException | ExecutionException e) {
                if (Log.isLoggable("InstanceID", 3)) {
                    String strValueOf = String.valueOf(e);
                    Log.d("InstanceID", new StringBuilder(String.valueOf(strValueOf).length() + 22).append("Error making request: ").append(strValueOf).toString());
                }
                if ((e.getCause() instanceof zzaa) && ((zzaa) e.getCause()).getErrorCode() == 4) {
                    return zzj(bundle);
                }
                return null;
            }
        }
        return zzj(bundle);
    }

    private final Bundle zzj(Bundle bundle) throws RemoteException, IOException {
        Bundle bundleZzk = zzk(bundle);
        if (bundleZzk == null || !bundleZzk.containsKey("google.messenger")) {
            return bundleZzk;
        }
        Bundle bundleZzk2 = zzk(bundle);
        if (bundleZzk2 == null || !bundleZzk2.containsKey("google.messenger")) {
            return bundleZzk2;
        }
        return null;
    }

    private static synchronized String zzx() {
        int i;
        i = zzcp;
        zzcp = i + 1;
        return Integer.toString(i);
    }

    private final Bundle zzk(Bundle bundle) throws RemoteException, IOException {
        Bundle bundle2;
        ConditionVariable conditionVariable = new ConditionVariable();
        String strZzx = zzx();
        synchronized (getClass()) {
            this.zzcz.put(strZzx, conditionVariable);
        }
        if (this.zzaj == null) {
            zzl(this.zzl);
            this.zzaj = new Messenger(new zzag(this, Looper.getMainLooper()));
        }
        if (zzcu == null) {
            throw new IOException(InstanceID.ERROR_MISSING_INSTANCEID_SERVICE);
        }
        Intent intent = new Intent(zzcv ? "com.google.iid.TOKEN_REQUEST" : "com.google.android.c2dm.intent.REGISTER");
        intent.setPackage(zzcu);
        intent.putExtras(bundle);
        zzg(intent);
        intent.putExtra("kid", new StringBuilder(String.valueOf(strZzx).length() + 5).append("|ID|").append(strZzx).append("|").toString());
        intent.putExtra("X-kid", new StringBuilder(String.valueOf(strZzx).length() + 5).append("|ID|").append(strZzx).append("|").toString());
        boolean zEquals = "com.google.android.gsf".equals(zzcu);
        String stringExtra = intent.getStringExtra("useGsf");
        if (stringExtra != null) {
            zEquals = "1".equals(stringExtra);
        }
        if (Log.isLoggable("InstanceID", 3)) {
            String strValueOf = String.valueOf(intent.getExtras());
            Log.d("InstanceID", new StringBuilder(String.valueOf(strValueOf).length() + 8).append("Sending ").append(strValueOf).toString());
        }
        if (this.zzda != null) {
            intent.putExtra("google.messenger", this.zzaj);
            Message messageObtain = Message.obtain();
            messageObtain.obj = intent;
            try {
                this.zzda.send(messageObtain);
            } catch (RemoteException unused) {
                if (Log.isLoggable("InstanceID", 3)) {
                    Log.d("InstanceID", "Messenger failed, fallback to startService");
                }
            }
        } else if (zEquals) {
            synchronized (zzaf.class) {
                if (zzcy == null) {
                    zzcy = new zzah(this);
                    if (Log.isLoggable("InstanceID", 3)) {
                        Log.d("InstanceID", "Registered GSF callback receiver");
                    }
                    IntentFilter intentFilter = new IntentFilter("com.google.android.c2dm.intent.REGISTRATION");
                    intentFilter.addCategory(this.zzl.getPackageName());
                    this.zzl.registerReceiver(zzcy, intentFilter, "com.google.android.c2dm.permission.SEND", null);
                }
            }
            this.zzl.sendBroadcast(intent);
        } else {
            intent.putExtra("google.messenger", this.zzaj);
            intent.putExtra("messenger2", "1");
            if (this.zzdb != null) {
                Message messageObtain2 = Message.obtain();
                messageObtain2.obj = intent;
                try {
                    this.zzdb.send(messageObtain2);
                } catch (RemoteException unused2) {
                    if (Log.isLoggable("InstanceID", 3)) {
                        Log.d("InstanceID", "Messenger failed, fallback to startService");
                    }
                }
            } else if (zzcv) {
                this.zzl.sendBroadcast(intent);
            } else {
                this.zzl.startService(intent);
            }
        }
        conditionVariable.block(30000L);
        synchronized (getClass()) {
            Object objRemove = this.zzcz.remove(strZzx);
            if (objRemove instanceof Bundle) {
                bundle2 = (Bundle) objRemove;
            } else {
                if (objRemove instanceof String) {
                    throw new IOException((String) objRemove);
                }
                String strValueOf2 = String.valueOf(objRemove);
                Log.w("InstanceID", new StringBuilder(String.valueOf(strValueOf2).length() + 12).append("No response ").append(strValueOf2).toString());
                throw new IOException("TIMEOUT");
            }
        }
        return bundle2;
    }
}
