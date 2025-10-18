package com.hihonor.android.facerecognition;

import android.content.Context;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;

/* compiled from: HwFaceManagerFactory.kt */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lcom/hihonor/android/facerecognition/HwFaceManagerFactory;", "", "<init>", "()V", "TAG", "", "getFaceManager", "Lcom/hihonor/android/facerecognition/FaceManager;", "context", "Landroid/content/Context;", "biometric_release"}, k = 1, mv = {2, 1, 0}, xi = 48)
/* loaded from: classes2.dex */
public final class HwFaceManagerFactory {
    public static final HwFaceManagerFactory INSTANCE = new HwFaceManagerFactory();
    private static final String TAG = "FaceRecognize";

    private HwFaceManagerFactory() {
    }

    @JvmStatic
    public static final synchronized FaceManager getFaceManager(Context context) {
        try {
            try {
                try {
                    return (FaceManager) Class.forName("com.hihonor.android.facerecognition.FaceManagerFactory").getDeclaredMethod("getFaceManager", Context.class).invoke(null, context);
                } catch (IllegalAccessException unused) {
                    Log.i(TAG, "Throw exception: IllegalAccessException");
                    return null;
                } catch (InvocationTargetException unused2) {
                    Log.i(TAG, "Throw exception: InvocationTargetException");
                    return null;
                }
            } catch (ClassNotFoundException unused3) {
                Log.i(TAG, "Throw exception: ClassNotFoundException");
                return null;
            }
        } catch (NoSuchMethodException unused4) {
            Log.i(TAG, "Throw exception: NoSuchMethodException");
            return null;
        }
    }
}
