package com.samsung.android.sdk.pass;

import android.content.Context;
import android.util.Log;
import com.samsung.android.sdk.SsdkInterface;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.SsdkVendorCheck;

/* loaded from: classes2.dex */
public class Spass implements SsdkInterface {
    public static final int DEVICE_FINGERPRINT = 0;
    public static final int DEVICE_FINGERPRINT_AVAILABLE_PASSWORD = 4;
    public static final int DEVICE_FINGERPRINT_CUSTOMIZED_DIALOG = 2;
    public static final int DEVICE_FINGERPRINT_FINGER_INDEX = 1;
    public static final int DEVICE_FINGERPRINT_UNIQUE_ID = 3;
    private static boolean a = false;
    private static boolean b = false;
    private static boolean c = false;
    private static boolean d = false;
    private Context e;

    @Override // com.samsung.android.sdk.SsdkInterface
    public int getVersionCode() {
        return 12;
    }

    @Override // com.samsung.android.sdk.SsdkInterface
    public String getVersionName() {
        return "1.2.6";
    }

    @Override // com.samsung.android.sdk.SsdkInterface
    public void initialize(Context context) throws SsdkUnsupportedException {
        if (this.e != null) {
            return;
        }
        if (context == null) {
            throw new IllegalArgumentException("context passed is null.");
        }
        if (!SsdkVendorCheck.isSamsungDevice()) {
            throw new SsdkUnsupportedException("This is not Samsung device.", 0);
        }
        try {
            boolean zHasSystemFeature = context.getPackageManager().hasSystemFeature("com.sec.feature.fingerprint_manager_service");
            a = zHasSystemFeature;
            if (!zHasSystemFeature) {
                throw new SsdkUnsupportedException("This device does not provide FingerprintService.", 1);
            }
            SpassFingerprint spassFingerprint = new SpassFingerprint(context);
            c = SpassFingerprint.a();
            b = spassFingerprint.b();
            d = spassFingerprint.c();
            this.e = context;
            Log.i(SpassFingerprint.TAG, "initialize : BP=" + d + ",CD=" + c + ",ID=" + b + ",GT=false");
            SpassFingerprint.a(context, null);
        } catch (NullPointerException unused) {
            throw new IllegalArgumentException("context is not valid.");
        }
    }

    @Override // com.samsung.android.sdk.SsdkInterface
    public boolean isFeatureEnabled(int i) {
        if (this.e == null) {
            throw new IllegalStateException("initialize() is not Called first.");
        }
        if (i == 0) {
            return a;
        }
        if (i == 1 || i == 2) {
            return c;
        }
        if (i == 3) {
            return b;
        }
        if (i == 4) {
            return d;
        }
        throw new IllegalArgumentException("type passed is not valid");
    }
}
