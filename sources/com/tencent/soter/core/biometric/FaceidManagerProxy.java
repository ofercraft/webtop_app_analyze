package com.tencent.soter.core.biometric;

import android.content.Context;
import android.os.CancellationSignal;
import android.os.Handler;
import com.tencent.soter.core.biometric.FaceManager;
import com.tencent.soter.core.model.SLogger;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

/* loaded from: classes2.dex */
final class FaceidManagerProxy {
    public static final String FACEMANAGER_FACTORY_CLASS_NAME = "com.tencent.soter.core.biometric.SoterFaceManagerFactory";
    private static final String TAG = "Soter.FaceidManagerProxy";

    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int i, CharSequence charSequence) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int i, CharSequence charSequence) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
        }
    }

    FaceidManagerProxy() {
    }

    private static FaceManager getFaceManager(Context context) {
        try {
            return (FaceManager) Class.forName(FACEMANAGER_FACTORY_CLASS_NAME).getDeclaredMethod("getFaceManager", Context.class).invoke(null, context);
        } catch (Exception e) {
            SLogger.e(TAG, "soter: FaceManager init failed, maybe not support." + e.toString(), new Object[0]);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasEnrolledFaceids(Context context) {
        try {
            FaceManager faceManager = getFaceManager(context);
            if (faceManager == null) {
                SLogger.e(TAG, "soter: facemanager is null in hasEnrolledBiometric! Should never happen", new Object[0]);
                return false;
            }
            return faceManager.hasEnrolledFaces();
        } catch (Exception unused) {
            SLogger.e(TAG, "soter: triggered SecurityException in hasEnrolledBiometric! Make sure you declared USE_FACEID in AndroidManifest.xml", new Object[0]);
            return false;
        }
    }

    public static boolean isHardwareDetected(Context context) {
        try {
            FaceManager faceManager = getFaceManager(context);
            if (faceManager == null) {
                SLogger.e(TAG, "soter: facemanager is null in isHardwareDetected! Should never happen", new Object[0]);
                return false;
            }
            return faceManager.isHardwareDetected();
        } catch (Exception unused) {
            SLogger.e(TAG, "soter: triggered SecurityException in isHardwareDetected! Make sure you declared USE_FACEID in AndroidManifest.xml", new Object[0]);
            return false;
        }
    }

    public static void authenticate(Context context, CryptoObject cryptoObject, int i, Object obj, AuthenticationCallback authenticationCallback, Handler handler) {
        try {
            FaceManager faceManager = getFaceManager(context);
            if (faceManager == null) {
                SLogger.e(TAG, "soter: facemanager is null in authenticate! Should never happen", new Object[0]);
            } else {
                faceManager.authenticate(wrapCryptoObject(cryptoObject), (CancellationSignal) obj, i, wrapCallback(authenticationCallback), handler);
            }
        } catch (Exception unused) {
            SLogger.e(TAG, "soter: triggered SecurityException in authenticate! Make sure you declared USE_FACEID in AndroidManifest.xml", new Object[0]);
        }
    }

    private static FaceManager.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        if (cryptoObject.getCipher() != null) {
            return new FaceManager.CryptoObject(cryptoObject.getCipher());
        }
        if (cryptoObject.getSignature() != null) {
            return new FaceManager.CryptoObject(cryptoObject.getSignature());
        }
        if (cryptoObject.getMac() != null) {
            return new FaceManager.CryptoObject(cryptoObject.getMac());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static CryptoObject unwrapCryptoObject(FaceManager.CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        if (cryptoObject.getCipher() != null) {
            return new CryptoObject(cryptoObject.getCipher());
        }
        if (cryptoObject.getSignature() != null) {
            return new CryptoObject(cryptoObject.getSignature());
        }
        if (cryptoObject.getMac() != null) {
            return new CryptoObject(cryptoObject.getMac());
        }
        return null;
    }

    private static FaceManager.AuthenticationCallback wrapCallback(final AuthenticationCallback authenticationCallback) {
        return new FaceManager.AuthenticationCallback() { // from class: com.tencent.soter.core.biometric.FaceidManagerProxy.1
            @Override // com.tencent.soter.core.biometric.FaceManager.AuthenticationCallback
            public void onAuthenticationError(int i, CharSequence charSequence) {
                SLogger.d(FaceidManagerProxy.TAG, "hy: lowest level return onAuthenticationError", new Object[0]);
                authenticationCallback.onAuthenticationError(i, charSequence);
            }

            @Override // com.tencent.soter.core.biometric.FaceManager.AuthenticationCallback
            public void onAuthenticationHelp(int i, CharSequence charSequence) {
                SLogger.d(FaceidManagerProxy.TAG, "hy: lowest level return onAuthenticationHelp", new Object[0]);
                authenticationCallback.onAuthenticationHelp(i, charSequence);
            }

            @Override // com.tencent.soter.core.biometric.FaceManager.AuthenticationCallback
            public void onAuthenticationSucceeded(FaceManager.AuthenticationResult authenticationResult) {
                SLogger.d(FaceidManagerProxy.TAG, "hy: lowest level return onAuthenticationSucceeded", new Object[0]);
                authenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(FaceidManagerProxy.unwrapCryptoObject(authenticationResult.getCryptoObject())));
            }

            @Override // com.tencent.soter.core.biometric.FaceManager.AuthenticationCallback
            public void onAuthenticationFailed() {
                SLogger.d(FaceidManagerProxy.TAG, "hy: lowest level return onAuthenticationFailed", new Object[0]);
                authenticationCallback.onAuthenticationFailed();
            }
        };
    }

    public static String getBiometricName(Context context) {
        try {
            FaceManager faceManager = getFaceManager(context);
            if (faceManager == null) {
                SLogger.e(TAG, "soter: faceid manager is null! no biometric name returned.", new Object[0]);
                return null;
            }
            return faceManager.getBiometricName(context);
        } catch (Exception unused) {
            SLogger.e(TAG, "soter: triggered SecurityException in getBiometricName! Make sure you declared USE_FACEID in AndroidManifest.xml", new Object[0]);
            return null;
        }
    }

    public static class CryptoObject {
        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;

        public CryptoObject(Signature signature) {
            this.mSignature = signature;
            this.mCipher = null;
            this.mMac = null;
        }

        public CryptoObject(Cipher cipher) {
            this.mCipher = cipher;
            this.mSignature = null;
            this.mMac = null;
        }

        public CryptoObject(Mac mac) {
            this.mMac = mac;
            this.mCipher = null;
            this.mSignature = null;
        }

        public Signature getSignature() {
            return this.mSignature;
        }

        public Cipher getCipher() {
            return this.mCipher;
        }

        public Mac getMac() {
            return this.mMac;
        }
    }

    public static final class AuthenticationResult {
        private CryptoObject mCryptoObject;

        public AuthenticationResult(CryptoObject cryptoObject) {
            this.mCryptoObject = cryptoObject;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }
}
