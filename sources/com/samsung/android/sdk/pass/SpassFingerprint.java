package com.samsung.android.sdk.pass;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.fingerprint.FingerprintIdentifyDialog;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.fingerprint.IFingerprintClient;
import com.samsung.android.sdk.pass.support.IFingerprintManagerProxy;
import com.samsung.android.sdk.pass.support.SdkSupporter;
import com.samsung.android.sdk.pass.support.v1.FingerprintManagerProxyFactory;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class SpassFingerprint {
    public static final String ACTION_FINGERPRINT_ADDED = "com.samsung.android.intent.action.FINGERPRINT_ADDED";
    public static final String ACTION_FINGERPRINT_REMOVED = "com.samsung.android.intent.action.FINGERPRINT_REMOVED";
    public static final String ACTION_FINGERPRINT_RESET = "com.samsung.android.intent.action.FINGERPRINT_RESET";
    public static final int STATUS_AUTHENTIFICATION_FAILED = 16;
    public static final int STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS = 100;
    public static final int STATUS_AUTHENTIFICATION_SUCCESS = 0;
    public static final int STATUS_BUTTON_PRESSED = 9;
    public static final int STATUS_OPERATION_DENIED = 51;
    public static final int STATUS_QUALITY_FAILED = 12;
    public static final int STATUS_SENSOR_FAILED = 7;
    public static final int STATUS_TIMEOUT_FAILED = 4;
    public static final int STATUS_USER_CANCELLED = 8;
    public static final int STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE = 13;
    public static final String TAG = "SpassFingerprintSDK";
    private static int n = 0;
    private static boolean o = false;
    private static boolean p = false;
    private static boolean q = false;
    private static boolean r = false;
    private IFingerprintManagerProxy a;
    private Context b;
    private int c = -1;
    private String d = null;
    private ArrayList e = null;
    private String f = null;
    private int g = -1;
    private String h = null;
    private int i = -1;
    private boolean j = false;
    private String k = null;
    private String l = null;
    private boolean m = false;
    private Dialog s = null;
    private b t = null;
    private b u = null;
    private IBinder v = null;
    private Handler w;

    public interface IdentifyListener {
        void onCompleted();

        void onFinished(int i);

        void onReady();

        void onStarted();
    }

    public interface RegisterListener {
        void onFinished();
    }

    private static class a {
        private Bundle a;

        public a() {
            Bundle bundle = new Bundle();
            this.a = bundle;
            bundle.putString("sdk_version", "Pass-v1.2.6");
        }

        public final Bundle a() {
            return this.a;
        }

        public final a a(int[] iArr) {
            if (iArr.length > 0) {
                this.a.putIntArray("request_template_index_list", iArr);
            }
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class b extends IFingerprintClient.Stub {
        private IdentifyListener a;
        private final int b;

        private b(IdentifyListener identifyListener) {
            this.b = SpassFingerprint.o ? 16 : 13;
            this.a = identifyListener;
        }

        /* synthetic */ b(SpassFingerprint spassFingerprint, IdentifyListener identifyListener, byte b) {
            this(identifyListener);
        }

        public final IdentifyListener a() {
            return this.a;
        }

        public final void a(IdentifyListener identifyListener) {
            this.a = identifyListener;
        }

        public final void onFingerprintEvent(FingerprintEvent fingerprintEvent) throws RemoteException {
            String str;
            if (fingerprintEvent == null) {
                str = "onFingerprintEvent: null event will be ignored!";
            } else {
                try {
                    Log.d(SpassFingerprint.TAG, "evt : " + fingerprintEvent.eventId + ", " + fingerprintEvent.eventResult + ", " + fingerprintEvent.eventStatus);
                    IdentifyListener identifyListener = this.a;
                    if (fingerprintEvent.eventId == this.b) {
                        Log.d(SpassFingerprint.TAG, "onFingerprintEvent : completed = " + this.b);
                        SpassFingerprint.this.t = null;
                        SpassFingerprint.this.f();
                    }
                    if (identifyListener != null && SpassFingerprint.this.w != null) {
                        SpassFingerprint.this.w.post(new e(this, fingerprintEvent, identifyListener));
                        return;
                    }
                    return;
                } catch (Exception e) {
                    str = "onFingerprintEvent: Error : " + e;
                }
            }
            Log.w(SpassFingerprint.TAG, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class c implements FingerprintIdentifyDialog.FingerprintListener {
        private IdentifyListener a;
        private FingerprintEvent b;

        private c(IdentifyListener identifyListener) {
            this.a = identifyListener;
        }

        /* synthetic */ c(SpassFingerprint spassFingerprint, IdentifyListener identifyListener, byte b) {
            this(identifyListener);
        }

        public final void a() {
            FingerprintEvent fingerprintEvent = this.b;
            IdentifyListener identifyListener = this.a;
            if (fingerprintEvent == null || identifyListener == null || SpassFingerprint.this.w == null) {
                return;
            }
            SpassFingerprint.this.w.post(new g(this, fingerprintEvent, identifyListener));
            this.a = null;
            this.b = null;
        }

        public final void onEvent(FingerprintEvent fingerprintEvent) {
            try {
                if (fingerprintEvent.eventId == 13 || SpassFingerprint.this.w == null) {
                    this.b = fingerprintEvent;
                } else {
                    SpassFingerprint.this.w.post(new f(this, fingerprintEvent));
                }
            } catch (Exception e) {
                Log.w(SpassFingerprint.TAG, "onFingerprintEvent: Error : " + e);
            }
        }
    }

    public SpassFingerprint(Context context) throws NoSuchFieldException {
        if (context == null) {
            throw new IllegalArgumentException("context is null.");
        }
        this.b = context;
        if (!p) {
            q = context.getPackageManager().hasSystemFeature("com.sec.feature.fingerprint_manager_service");
            r = g();
            p = true;
        }
        if (q) {
            this.a = FingerprintManagerProxyFactory.create(this.b);
            this.w = new Handler(context.getMainLooper());
            if (this.a != null) {
                try {
                    if (this.a.getSensorType() == FingerprintManager.class.getField("SENSOR_TYPE_TOUCH").getInt(null)) {
                        o = true;
                    }
                } catch (Exception e) {
                    Log.i(TAG, "SpassFingerprint : " + e.toString());
                }
                n = this.a.getVersion();
            }
        }
        Log.i(TAG, "SpassFingerprint : 1.2.6, " + n + ", " + o);
    }

    static void a(Context context, String str) {
        if (g()) {
            if (context.checkCallingOrSelfPermission("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY") != 0) {
                Log.d(TAG, "insertLog :  No permission");
                return;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", SpassFingerprint.class.getPackage().getName());
            contentValues.put("feature", String.valueOf(context.getPackageName()) + "#12");
            if (str != null) {
                contentValues.put("extra", str);
            }
            Intent intent = new Intent();
            intent.setAction("com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY");
            intent.putExtra("data", contentValues);
            intent.setPackage("com.samsung.android.providers.context");
            context.sendBroadcast(intent);
            Log.i(TAG, "insertLog : " + contentValues.toString());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0049 A[PHI: r0
  0x0049: PHI (r0v2 int) = (r0v1 int), (r0v3 int), (r0v4 int) binds: [B:11:0x0024, B:21:0x0036, B:23:0x003a] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static /* synthetic */ void a(com.samsung.android.sdk.pass.SpassFingerprint r3, com.samsung.android.sdk.pass.SpassFingerprint.IdentifyListener r4, com.samsung.android.fingerprint.FingerprintEvent r5, int r6) {
        /*
            java.lang.String r0 = ""
            r3.d = r0
            r0 = 0
            if (r5 != 0) goto La
            r3.c = r0
            goto L4a
        La:
            int r6 = r5.getFingerIndex()
            r3.c = r6
            int r6 = r5.eventStatus
            r1 = 12
            if (r6 == r1) goto L1c
            int r6 = r5.eventStatus
            r2 = 11
            if (r6 != r2) goto L22
        L1c:
            java.lang.String r6 = r5.getImageQualityFeedback()
            r3.d = r6
        L22:
            int r5 = r5.eventStatus
            if (r5 == 0) goto L49
            r6 = 4
            if (r5 == r6) goto L4a
            r6 = 51
            if (r5 == r6) goto L4a
            r6 = 100
            if (r5 == r6) goto L4a
            r6 = 7
            if (r5 == r6) goto L4a
            r0 = 8
            if (r5 == r0) goto L49
            r0 = 9
            if (r5 == r0) goto L49
            switch(r5) {
                case 11: goto L45;
                case 12: goto L43;
                case 13: goto L40;
                default: goto L3f;
            }
        L3f:
            goto L4a
        L40:
            r5 = 13
            goto L47
        L43:
            r6 = r1
            goto L4a
        L45:
            r5 = 16
        L47:
            r6 = r5
            goto L4a
        L49:
            r6 = r0
        L4a:
            r4.onFinished(r6)
            r4 = -1
            r3.c = r4
            r4 = 0
            r3.d = r4
            boolean r4 = r3.m
            if (r4 != 0) goto L61
            r4 = 1
            r3.m = r4
            android.content.Context r3 = r3.b
            java.lang.String r4 = "IdentifyListener.onFinished"
            a(r3, r4)
        L61:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.sdk.pass.SpassFingerprint.a(com.samsung.android.sdk.pass.SpassFingerprint, com.samsung.android.sdk.pass.SpassFingerprint$IdentifyListener, com.samsung.android.fingerprint.FingerprintEvent, int):void");
    }

    static boolean a() {
        return n >= 16843008;
    }

    private boolean a(String str) throws PackageManager.NameNotFoundException {
        String packageName = this.b.getPackageName();
        try {
            Resources resourcesForApplication = this.b.getPackageManager().getResourcesForApplication(packageName);
            if (resourcesForApplication == null) {
                return false;
            }
            try {
                int identifier = resourcesForApplication.getIdentifier(str, "drawable", packageName);
                if (identifier != 0 && identifier != -1) {
                    return BitmapFactory.decodeResource(resourcesForApplication, identifier) != null;
                }
            } catch (Resources.NotFoundException unused) {
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private synchronized void e() throws UnsupportedOperationException {
        if (!q) {
            throw new UnsupportedOperationException("Fingerprint Service is not supported in the platform.");
        }
        if (this.a == null) {
            Log.i(TAG, "ensureServiceSupported : proxy is null, retry to create proxy");
            IFingerprintManagerProxy iFingerprintManagerProxyCreate = FingerprintManagerProxyFactory.create(this.b);
            this.a = iFingerprintManagerProxyCreate;
            if (iFingerprintManagerProxyCreate == null) {
                throw new UnsupportedOperationException("Fingerprint Service is not running on the device.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        this.a.unregisterClient(this.v);
        this.v = null;
        b bVar = this.u;
        if (bVar != null) {
            bVar.a((IdentifyListener) null);
        }
    }

    private static boolean g() throws ClassNotFoundException {
        if (p) {
            return r;
        }
        boolean zBooleanValue = false;
        try {
            Class<?> cls = Class.forName("com.samsung.android.feature.FloatingFeature");
            zBooleanValue = ((Boolean) cls.getMethod("getEnableStatus", String.class).invoke(cls.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]), "SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE")).booleanValue();
        } catch (Exception e) {
            Log.d(TAG, "Survey Mode : " + e.toString());
            try {
                Class<?> cls2 = Class.forName("com.samsung.android.feature.SemFloatingFeature");
                zBooleanValue = ((Boolean) cls2.getMethod("getBoolean", String.class).invoke(cls2.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]), "SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE")).booleanValue();
            } catch (Exception e2) {
                Log.d(TAG, "Survey Mode : " + e2.toString());
            }
        }
        Log.i(TAG, "Survey Mode : " + zBooleanValue);
        return zBooleanValue;
    }

    final boolean b() throws UnsupportedOperationException, NoSuchMethodException, SecurityException {
        e();
        try {
            Class.forName(SdkSupporter.CLASSNAME_FINGERPRINT_MANAGER).getMethod("isSupportFingerprintIds", new Class[0]);
            return this.a.isSupportFingerprintIds();
        } catch (Exception e) {
            Log.w(TAG, e);
            return a();
        }
    }

    final boolean c() throws UnsupportedOperationException, NoSuchMethodException, SecurityException {
        e();
        try {
            Class.forName(SdkSupporter.CLASSNAME_FINGERPRINT_MANAGER).getMethod("isSupportBackupPassword", new Class[0]);
            return this.a.isSupportBackupPassword();
        } catch (Exception e) {
            Log.w(TAG, e);
            return true;
        }
    }

    public void cancelIdentify() throws UnsupportedOperationException {
        Handler handler;
        e();
        IBinder iBinder = this.v;
        if (iBinder == null && this.t == null && this.s == null) {
            throw new IllegalStateException("No Identify request.");
        }
        if (iBinder == null) {
            if (this.t == null && this.s == null) {
                return;
            }
            this.a.notifyAppActivityState(4, null);
            this.t = null;
            this.s = null;
            return;
        }
        b bVar = this.u;
        IdentifyListener identifyListenerA = bVar != null ? bVar.a() : null;
        f();
        if (identifyListenerA == null || (handler = this.w) == null) {
            return;
        }
        handler.postDelayed(new com.samsung.android.sdk.pass.a(this, identifyListenerA), 100L);
    }

    public void changeStandbyString(String str) throws UnsupportedOperationException {
        e();
        if (c()) {
            throw new IllegalStateException("setStandbyString is not supported.");
        }
        if (str == null) {
            throw new IllegalArgumentException("the standby text passed is null.");
        }
        if (str.length() > 100) {
            throw new IllegalArgumentException("the standby text passed is longer than 100 characters.");
        }
        this.l = str;
    }

    public String getGuideForPoorQuality() throws UnsupportedOperationException {
        e();
        String str = this.d;
        if (str != null) {
            return str;
        }
        throw new IllegalStateException("FingerprintGuide is Invalid. This API must be called inside IdentifyListener.onFinished() with STATUS_QUALITY_FAILED only.");
    }

    public int getIdentifiedFingerprintIndex() throws UnsupportedOperationException {
        e();
        int i = this.c;
        if (i != -1) {
            return i;
        }
        throw new IllegalStateException("FingerprintIndex is Invalid. This API must be called inside IdentifyListener.onFinished() only.");
    }

    public SparseArray getRegisteredFingerprintName() throws UnsupportedOperationException {
        e();
        SparseArray sparseArray = new SparseArray();
        int enrolledFingers = this.a.getEnrolledFingers();
        if (enrolledFingers <= 0) {
            return null;
        }
        for (int i = 1; i <= 10; i++) {
            if (((1 << i) & enrolledFingers) != 0) {
                sparseArray.put(i, this.a.getIndexName(i));
            }
        }
        return sparseArray;
    }

    public SparseArray getRegisteredFingerprintUniqueID() throws UnsupportedOperationException {
        e();
        if (!b()) {
            throw new IllegalStateException("getRegisteredFingerprintUniqueID is not supported.");
        }
        SparseArray sparseArray = new SparseArray();
        int enrolledFingers = this.a.getEnrolledFingers();
        if (enrolledFingers <= 0) {
            return null;
        }
        for (int i = 1; i <= 10; i++) {
            if (((1 << i) & enrolledFingers) != 0) {
                sparseArray.put(i, this.a.getFingerprintId(i));
            }
        }
        return sparseArray;
    }

    public boolean hasRegisteredFinger() throws UnsupportedOperationException {
        e();
        return this.a.getEnrolledFingers() != 0;
    }

    public void registerFinger(Context context, RegisterListener registerListener) throws UnsupportedOperationException {
        e();
        if (context == null) {
            throw new IllegalArgumentException("activityContext passed is null.");
        }
        if (registerListener == null) {
            throw new IllegalArgumentException("listener passed is null.");
        }
        if (this.a.isEnrolling()) {
            this.a.notifyEnrollEnd();
        }
        try {
            context.getPackageManager();
            try {
                this.a.startEnrollActivity(context, new d(registerListener), toString());
            } catch (UndeclaredThrowableException unused) {
                throw new IllegalArgumentException("activityContext is invalid");
            }
        } catch (NullPointerException unused2) {
            throw new IllegalArgumentException("activityContext is invalid");
        }
    }

    public void setCanceledOnTouchOutside(boolean z) throws UnsupportedOperationException {
        e();
        if (!a()) {
            throw new IllegalStateException("setCanceledOnTouchOutside is not supported.");
        }
        this.j = z;
    }

    public void setDialogBgTransparency(int i) throws UnsupportedOperationException {
        e();
        if (!a()) {
            throw new IllegalStateException("setDialogBGTransparency is not supported.");
        }
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException("the transparency passed is not valid.");
        }
        this.i = i;
    }

    public void setDialogButton(String str) throws UnsupportedOperationException {
        e();
        if (c()) {
            throw new IllegalStateException("setDialogButton is not supported.");
        }
        if (str == null) {
            throw new IllegalArgumentException("the buttonText passed is null.");
        }
        if (str.length() > 32) {
            throw new IllegalArgumentException("the title text passed is longer than 32 characters.");
        }
        this.k = str;
    }

    public void setDialogIcon(String str) throws UnsupportedOperationException {
        e();
        if (!a()) {
            throw new IllegalStateException("setDialogIcon is not supported.");
        }
        if (str == null) {
            throw new IllegalArgumentException("the iconName passed is null.");
        }
        if (!a(str)) {
            throw new IllegalArgumentException("the iconName passed is not valid.");
        }
        this.h = str;
    }

    public void setDialogTitle(String str, int i) throws UnsupportedOperationException {
        e();
        if (!a()) {
            throw new IllegalStateException("setDialogTitle is not supported.");
        }
        if (str == null) {
            throw new IllegalArgumentException("the titletext passed is null.");
        }
        if (str.length() > 256) {
            throw new IllegalArgumentException("the title text passed is longer than 256 characters.");
        }
        if ((i >>> 24) != 0) {
            throw new IllegalArgumentException("alpha value is not supported in the titleColor.");
        }
        this.f = str;
        this.g = i - 16777216;
    }

    public void setIntendedFingerprintIndex(ArrayList arrayList) throws UnsupportedOperationException {
        e();
        if (arrayList == null) {
            Log.w(TAG, "requestedIndex is null. Identify is carried out for all indexes.");
            return;
        }
        if (!a()) {
            throw new IllegalStateException("setIntendedFingerprintIndex is not supported.");
        }
        this.e = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            this.e.add((Integer) arrayList.get(i));
        }
    }

    public void startIdentify(IdentifyListener identifyListener) throws UnsupportedOperationException {
        a aVar = new a();
        ArrayList arrayList = this.e;
        byte b2 = 0;
        if (arrayList != null && arrayList.size() > 0) {
            int[] iArr = new int[this.e.size()];
            for (int i = 0; i < this.e.size(); i++) {
                iArr[i] = ((Integer) this.e.get(i)).intValue();
            }
            this.e = null;
            aVar.a(iArr);
        }
        e();
        if (this.a.getEnrolledFingers() == 0) {
            throw new IllegalStateException("Identify operation is failed.");
        }
        if (this.v != null) {
            throw new IllegalStateException("Identify request is denied because a previous request is still in progress.");
        }
        if (identifyListener == null) {
            throw new IllegalArgumentException("listener passed is null.");
        }
        if (this.u == null) {
            this.u = new b(this, identifyListener, b2);
        }
        Bundle bundleA = aVar.a();
        bundleA.putString("appName", this.b.getPackageName());
        IBinder iBinderRegisterClient = this.a.registerClient(this.u, bundleA);
        this.v = iBinderRegisterClient;
        if (iBinderRegisterClient == null) {
            IBinder iBinderRegisterClient2 = this.a.registerClient(this.u, bundleA);
            this.v = iBinderRegisterClient2;
            if (iBinderRegisterClient2 == null) {
                Handler handler = this.w;
                if (handler == null) {
                    throw new IllegalStateException("failed because registerClient returned null.");
                }
                handler.post(new com.samsung.android.sdk.pass.b(this, identifyListener));
                return;
            }
        }
        int iIdentify = this.a.identify(this.v, null);
        if (iIdentify == 0) {
            this.u.a(identifyListener);
            return;
        }
        f();
        Log.i(TAG, "startIdentify : failed, " + iIdentify);
        if (iIdentify == -2) {
            throw new IllegalStateException("Identify request is denied because a previous request is still in progress.");
        }
        if (iIdentify != 51) {
            throw new IllegalStateException("Identify operation is failed.");
        }
        throw new SpassInvalidStateException("Identify request is denied because 5 identify attempts are failed.", 1);
    }

    public void startIdentifyWithDialog(Context context, IdentifyListener identifyListener, boolean z) throws UnsupportedOperationException {
        int[] iArr;
        e();
        if (context == null) {
            throw new IllegalArgumentException("activityContext passed is null.");
        }
        if (identifyListener == null) {
            throw new IllegalArgumentException("listener passed is null.");
        }
        try {
            context.getPackageManager();
            if (!(context instanceof Activity)) {
                Log.w(TAG, "startIdentifyWithDialog : No Actvity Context");
            }
            boolean z2 = false;
            byte b2 = 0;
            byte b3 = 0;
            if (!a()) {
                c cVar = new c(this, identifyListener, b2 == true ? 1 : 0);
                Dialog dialogShowIdentifyDialog = this.a.showIdentifyDialog(context, cVar, null, z);
                this.s = dialogShowIdentifyDialog;
                if (dialogShowIdentifyDialog == null) {
                    throw new IllegalStateException("Identify operation is failed.");
                }
                dialogShowIdentifyDialog.setOnDismissListener(new com.samsung.android.sdk.pass.c(cVar));
                this.s.show();
                return;
            }
            ArrayList arrayList = this.e;
            if (arrayList == null || arrayList.size() <= 0) {
                iArr = null;
            } else {
                iArr = new int[this.e.size()];
                for (int i = 0; i < this.e.size(); i++) {
                    iArr[i] = ((Integer) this.e.get(i)).intValue();
                }
            }
            this.t = new b(this, identifyListener, b3 == true ? 1 : 0);
            try {
                Bundle bundle = new Bundle();
                bundle.putBoolean("password", z);
                bundle.putString("packageName", context.getPackageName());
                bundle.putString("sdk_version", "Pass-v1.2.6");
                bundle.putBoolean("demandExtraEvent", true);
                if (iArr != null) {
                    bundle.putIntArray("request_template_index_list", iArr);
                }
                String str = this.f;
                if (str != null) {
                    bundle.putString("titletext", str);
                }
                int i2 = this.g;
                if (i2 != -1) {
                    bundle.putInt("titlecolor", i2);
                }
                String str2 = this.h;
                if (str2 != null) {
                    bundle.putString("iconname", str2);
                }
                int i3 = this.i;
                if (i3 != -1) {
                    bundle.putInt("transparency", i3);
                }
                boolean z3 = this.j;
                if (z3) {
                    bundle.putBoolean("touchoutside", z3);
                }
                String str3 = this.k;
                if (str3 != null) {
                    bundle.putString("button_name", str3);
                }
                String str4 = this.l;
                if (str4 != null) {
                    bundle.putString("standby_string", str4);
                }
                if (this.a.identifyWithDialog(context, this.t, bundle) != 0) {
                    throw new IllegalStateException("Identify operation is failed.");
                }
            } finally {
                this.e = null;
                this.f = null;
                this.g = -1;
                this.i = -1;
                this.h = null;
                this.j = false;
                this.l = null;
                this.k = null;
            }
        } catch (NullPointerException unused) {
            throw new IllegalArgumentException("activityContext is invalid");
        }
    }
}
