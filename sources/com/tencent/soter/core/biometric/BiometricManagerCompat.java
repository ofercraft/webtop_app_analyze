package com.tencent.soter.core.biometric;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import com.tencent.soter.core.SoterCore;
import com.tencent.soter.core.biometric.FaceidManagerProxy;
import com.tencent.soter.core.biometric.FingerprintManagerProxy;
import com.tencent.soter.core.model.SLogger;
import java.security.Signature;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.Mac;

/* loaded from: classes2.dex */
public class BiometricManagerCompat {
    static final Map<Integer, IBiometricManager> IMPL_PROVIDER;
    private static final String TAG = "Soter.BiometricManagerCompat";
    private Integer mBiometricType;
    private Context mContext;

    public static abstract class AuthenticationCallback {
        public void onAuthenticationCancelled() {
        }

        public void onAuthenticationError(int i, CharSequence charSequence) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int i, CharSequence charSequence) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
        }
    }

    private interface IBiometricManager {
        void authenticate(Context context, CryptoObject cryptoObject, int i, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler, Bundle bundle);

        String getBiometricName(Context context);

        boolean hasEnrolledBiometric(Context context);

        boolean isHardwareDetected(Context context);
    }

    private static class LegacyFingerprintManagerImpl implements IBiometricManager {
        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public void authenticate(Context context, CryptoObject cryptoObject, int i, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler, Bundle bundle) {
        }

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public String getBiometricName(Context context) {
            return null;
        }

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public boolean hasEnrolledBiometric(Context context) {
            return false;
        }

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public boolean isHardwareDetected(Context context) {
            return false;
        }
    }

    public static BiometricManagerCompat from(Context context, Integer num) {
        return new BiometricManagerCompat(context, num);
    }

    private BiometricManagerCompat(Context context, Integer num) {
        this.mContext = context;
        this.mBiometricType = num;
    }

    static {
        IBiometricManager legacyFingerprintManagerImpl;
        HashMap map = new HashMap();
        IMPL_PROVIDER = map;
        if (SoterCore.isNativeSupportSoter()) {
            legacyFingerprintManagerImpl = new FingerprintManagerImpl();
        } else {
            legacyFingerprintManagerImpl = new LegacyFingerprintManagerImpl();
        }
        map.put(1, legacyFingerprintManagerImpl);
        if (SoterCore.isNativeSupportSoter() && isNativeSupportFaceid()) {
            map.put(2, new FaceidManagerImpl());
        }
    }

    public static boolean isNativeSupportFaceid() throws ClassNotFoundException {
        try {
            Class.forName(FaceidManagerProxy.FACEMANAGER_FACTORY_CLASS_NAME);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean hasEnrolledBiometric() {
        IBiometricManager iBiometricManager = IMPL_PROVIDER.get(this.mBiometricType);
        if (iBiometricManager == null) {
            SLogger.i(TAG, "soter: Biometric provider not initialized type[" + this.mBiometricType + "]", new Object[0]);
            return false;
        }
        return iBiometricManager.hasEnrolledBiometric(this.mContext);
    }

    public String getBiometricName() {
        IBiometricManager iBiometricManager = IMPL_PROVIDER.get(this.mBiometricType);
        if (iBiometricManager == null) {
            SLogger.i(TAG, "soter: Biometric provider not initialized type[" + this.mBiometricType + "]", new Object[0]);
            return null;
        }
        return iBiometricManager.getBiometricName(this.mContext);
    }

    public boolean isHardwareDetected() {
        IBiometricManager iBiometricManager = IMPL_PROVIDER.get(this.mBiometricType);
        if (iBiometricManager == null) {
            SLogger.i(TAG, "soter: Biometric provider not initialized type[" + this.mBiometricType + "]", new Object[0]);
            return false;
        }
        return iBiometricManager.isHardwareDetected(this.mContext);
    }

    public boolean isCurrentFailTimeAvailable() {
        return SoterBiometricAntiBruteForceStrategy.isCurrentFailTimeAvailable(this.mContext);
    }

    public boolean isCurrentTweenTimeAvailable(Context context) {
        return SoterBiometricAntiBruteForceStrategy.isCurrentTweenTimeAvailable(this.mContext);
    }

    public void authenticate(CryptoObject cryptoObject, int i, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler, Bundle bundle) {
        IBiometricManager iBiometricManager = IMPL_PROVIDER.get(this.mBiometricType);
        if (iBiometricManager == null) {
            SLogger.i(TAG, "soter: Biometric provider not initialized type[" + this.mBiometricType + "]", new Object[0]);
            authenticationCallback.onAuthenticationCancelled();
        }
        iBiometricManager.authenticate(this.mContext, cryptoObject, i, cancellationSignal, authenticationCallback, handler, bundle);
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

    private static class FingerprintManagerImpl implements IBiometricManager {
        private static final String TAG = "Soter.BiometricManagerCompat.Fingerprint";

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public boolean hasEnrolledBiometric(Context context) {
            return FingerprintManagerProxy.hasEnrolledFingerprints(context);
        }

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public boolean isHardwareDetected(Context context) {
            return FingerprintManagerProxy.isHardwareDetected(context);
        }

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public String getBiometricName(Context context) {
            return "fingerprint";
        }

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public void authenticate(Context context, CryptoObject cryptoObject, int i, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler, Bundle bundle) {
            FingerprintManagerProxy.authenticate(context, wrapCryptoObject(cryptoObject), i, cancellationSignal, wrapCallback(context, authenticationCallback), handler, bundle);
        }

        private static FingerprintManagerProxy.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            }
            if (cryptoObject.getCipher() != null) {
                return new FingerprintManagerProxy.CryptoObject(cryptoObject.getCipher());
            }
            if (cryptoObject.getSignature() != null) {
                return new FingerprintManagerProxy.CryptoObject(cryptoObject.getSignature());
            }
            if (cryptoObject.getMac() != null) {
                return new FingerprintManagerProxy.CryptoObject(cryptoObject.getMac());
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static CryptoObject unwrapCryptoObject(FingerprintManagerProxy.CryptoObject cryptoObject) {
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

        private static FingerprintManagerProxy.AuthenticationCallback wrapCallback(final Context context, final AuthenticationCallback authenticationCallback) {
            return new FingerprintManagerProxy.AuthenticationCallback() { // from class: com.tencent.soter.core.biometric.BiometricManagerCompat.FingerprintManagerImpl.1
                private boolean mMarkPermanentlyCallbacked = false;

                @Override // com.tencent.soter.core.biometric.FingerprintManagerProxy.AuthenticationCallback
                public void onAuthenticationError(int i, CharSequence charSequence) {
                    SLogger.d(FingerprintManagerImpl.TAG, "soter: basic onAuthenticationError", new Object[0]);
                    if (this.mMarkPermanentlyCallbacked) {
                        return;
                    }
                    this.mMarkPermanentlyCallbacked = true;
                    if (i == 5 || i == 10) {
                        SLogger.i(FingerprintManagerImpl.TAG, "soter: user cancelled fingerprint authen", new Object[0]);
                        authenticationCallback.onAuthenticationCancelled();
                        return;
                    }
                    if (i == 7 || i == 9) {
                        SLogger.i(FingerprintManagerImpl.TAG, "soter: system call too many trial.", new Object[0]);
                        if (!SoterBiometricAntiBruteForceStrategy.isCurrentFailTimeAvailable(context) && !SoterBiometricAntiBruteForceStrategy.isCurrentTweenTimeAvailable(context) && !SoterBiometricAntiBruteForceStrategy.isSystemHasAntiBruteForce()) {
                            SoterBiometricAntiBruteForceStrategy.freeze(context);
                        }
                        this.mMarkPermanentlyCallbacked = false;
                        if (i == 7) {
                            onAuthenticationError(10308, "Too many failed times");
                            return;
                        } else {
                            onAuthenticationError(10309, "Too many failed times");
                            return;
                        }
                    }
                    authenticationCallback.onAuthenticationError(i, charSequence);
                }

                @Override // com.tencent.soter.core.biometric.FingerprintManagerProxy.AuthenticationCallback
                public void onAuthenticationHelp(int i, CharSequence charSequence) {
                    SLogger.d(FingerprintManagerImpl.TAG, "soter: basic onAuthenticationHelp", new Object[0]);
                    if (this.mMarkPermanentlyCallbacked || FingerprintManagerImpl.shouldInformTooManyTrial(this, context)) {
                        return;
                    }
                    authenticationCallback.onAuthenticationHelp(i, charSequence);
                }

                @Override // com.tencent.soter.core.biometric.FingerprintManagerProxy.AuthenticationCallback
                public void onAuthenticationSucceeded(FingerprintManagerProxy.AuthenticationResultInternal authenticationResultInternal) {
                    SLogger.d(FingerprintManagerImpl.TAG, "soter: basic onAuthenticationSucceeded", new Object[0]);
                    if (this.mMarkPermanentlyCallbacked || FingerprintManagerImpl.shouldInformTooManyTrial(this, context)) {
                        return;
                    }
                    if (!SoterBiometricAntiBruteForceStrategy.isSystemHasAntiBruteForce()) {
                        SoterBiometricAntiBruteForceStrategy.unFreeze(context);
                    }
                    this.mMarkPermanentlyCallbacked = true;
                    authenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(FingerprintManagerImpl.unwrapCryptoObject(authenticationResultInternal.getCryptoObject())));
                }

                @Override // com.tencent.soter.core.biometric.FingerprintManagerProxy.AuthenticationCallback
                public void onAuthenticationFailed() {
                    SLogger.d(FingerprintManagerImpl.TAG, "soter: basic onAuthenticationFailed", new Object[0]);
                    if (this.mMarkPermanentlyCallbacked || FingerprintManagerImpl.shouldInformTooManyTrial(this, context)) {
                        return;
                    }
                    if (!SoterBiometricAntiBruteForceStrategy.isSystemHasAntiBruteForce()) {
                        SoterBiometricAntiBruteForceStrategy.addFailTime(context);
                        if (!SoterBiometricAntiBruteForceStrategy.isCurrentFailTimeAvailable(context)) {
                            SLogger.w(FingerprintManagerImpl.TAG, "soter: too many fail trials", new Object[0]);
                            SoterBiometricAntiBruteForceStrategy.freeze(context);
                            FingerprintManagerImpl.informTooManyTrial(this);
                            return;
                        }
                    }
                    authenticationCallback.onAuthenticationFailed();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean shouldInformTooManyTrial(FingerprintManagerProxy.AuthenticationCallback authenticationCallback, Context context) {
            if (SoterBiometricAntiBruteForceStrategy.isSystemHasAntiBruteForce()) {
                SLogger.v(TAG, "soter: using system anti brute force strategy", new Object[0]);
                return false;
            }
            if (SoterBiometricAntiBruteForceStrategy.isCurrentTweenTimeAvailable(context)) {
                if (!SoterBiometricAntiBruteForceStrategy.isCurrentFailTimeAvailable(context)) {
                    SLogger.v(TAG, "soter: unfreeze former frozen status", new Object[0]);
                    SoterBiometricAntiBruteForceStrategy.unFreeze(context);
                }
                return false;
            }
            if (SoterBiometricAntiBruteForceStrategy.isCurrentFailTimeAvailable(context)) {
                SLogger.v(TAG, "soter: failure time available", new Object[0]);
                return false;
            }
            informTooManyTrial(authenticationCallback);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void informTooManyTrial(FingerprintManagerProxy.AuthenticationCallback authenticationCallback) {
            SLogger.w(TAG, "soter: too many fail fingerprint callback. inform it.", new Object[0]);
            authenticationCallback.onAuthenticationError(10308, "Too many failed times");
        }
    }

    private static class FaceidManagerImpl implements IBiometricManager {
        private static final String TAG = "Soter.BiometricManagerCompat.Faceid";

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public boolean hasEnrolledBiometric(Context context) {
            return FaceidManagerProxy.hasEnrolledFaceids(context);
        }

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public boolean isHardwareDetected(Context context) {
            return FaceidManagerProxy.isHardwareDetected(context);
        }

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public String getBiometricName(Context context) {
            return FaceidManagerProxy.getBiometricName(context);
        }

        @Override // com.tencent.soter.core.biometric.BiometricManagerCompat.IBiometricManager
        public void authenticate(Context context, CryptoObject cryptoObject, int i, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler, Bundle bundle) {
            FaceidManagerProxy.authenticate(context, wrapCryptoObject(cryptoObject), i, cancellationSignal, wrapCallback(context, authenticationCallback), handler);
        }

        private static FaceidManagerProxy.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            }
            if (cryptoObject.getCipher() != null) {
                return new FaceidManagerProxy.CryptoObject(cryptoObject.getCipher());
            }
            if (cryptoObject.getSignature() != null) {
                return new FaceidManagerProxy.CryptoObject(cryptoObject.getSignature());
            }
            if (cryptoObject.getMac() != null) {
                return new FaceidManagerProxy.CryptoObject(cryptoObject.getMac());
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static CryptoObject unwrapCryptoObject(FaceidManagerProxy.CryptoObject cryptoObject) {
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

        private static FaceidManagerProxy.AuthenticationCallback wrapCallback(final Context context, final AuthenticationCallback authenticationCallback) {
            return new FaceidManagerProxy.AuthenticationCallback() { // from class: com.tencent.soter.core.biometric.BiometricManagerCompat.FaceidManagerImpl.1
                private boolean mMarkPermanentlyCallbacked = false;

                @Override // com.tencent.soter.core.biometric.FaceidManagerProxy.AuthenticationCallback
                public void onAuthenticationError(int i, CharSequence charSequence) {
                    SLogger.d(FaceidManagerImpl.TAG, "soter: basic onAuthenticationError code[%d], msg[%s] entered.", Integer.valueOf(i), charSequence);
                    if (this.mMarkPermanentlyCallbacked) {
                        SLogger.d(FaceidManagerImpl.TAG, "soter: basic onAuthenticationError code[%d], msg[%s] returned cause permanently callback.", Integer.valueOf(i), charSequence);
                        return;
                    }
                    this.mMarkPermanentlyCallbacked = true;
                    if (i == 5) {
                        SLogger.i(FaceidManagerImpl.TAG, "soter: basic onAuthenticationError code[%d], msg[%s] callbacked and returned cause FACE_ERROR_CANCELED got.", Integer.valueOf(i), charSequence);
                        authenticationCallback.onAuthenticationCancelled();
                    } else {
                        if (i == 7) {
                            SLogger.i(FaceidManagerImpl.TAG, "soter: basic onAuthenticationError code[%d], msg[%s] callbacked and returned cause FACE_ERROR_LOCKOUT got.", Integer.valueOf(i), charSequence);
                            if (!SoterBiometricAntiBruteForceStrategy.isCurrentFailTimeAvailable(context) && !SoterBiometricAntiBruteForceStrategy.isCurrentTweenTimeAvailable(context) && !SoterBiometricAntiBruteForceStrategy.isSystemHasAntiBruteForce()) {
                                SoterBiometricAntiBruteForceStrategy.freeze(context);
                            }
                            authenticationCallback.onAuthenticationError(10308, "Too many failed times");
                            return;
                        }
                        SLogger.d(FaceidManagerImpl.TAG, "soter: basic onAuthenticationError code[%d], msg[%s] callbacked and returned.", Integer.valueOf(i), charSequence);
                        authenticationCallback.onAuthenticationError(i, charSequence);
                    }
                }

                @Override // com.tencent.soter.core.biometric.FaceidManagerProxy.AuthenticationCallback
                public void onAuthenticationHelp(int i, CharSequence charSequence) {
                    SLogger.d(FaceidManagerImpl.TAG, "soter: basic onAuthenticationHelp helpMsgId[%d], helpString[%s]", Integer.valueOf(i), charSequence);
                    System.currentTimeMillis();
                    if (this.mMarkPermanentlyCallbacked || FaceidManagerImpl.shouldInformTooManyTrial(this, context)) {
                        return;
                    }
                    authenticationCallback.onAuthenticationHelp(i, charSequence);
                }

                @Override // com.tencent.soter.core.biometric.FaceidManagerProxy.AuthenticationCallback
                public void onAuthenticationSucceeded(FaceidManagerProxy.AuthenticationResult authenticationResult) {
                    SLogger.d(FaceidManagerImpl.TAG, "soter: basic onAuthenticationSucceeded", new Object[0]);
                    if (this.mMarkPermanentlyCallbacked) {
                        return;
                    }
                    this.mMarkPermanentlyCallbacked = true;
                    if (FaceidManagerImpl.shouldInformTooManyTrial(this, context)) {
                        return;
                    }
                    if (!SoterBiometricAntiBruteForceStrategy.isSystemHasAntiBruteForce()) {
                        SoterBiometricAntiBruteForceStrategy.unFreeze(context);
                    }
                    authenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(FaceidManagerImpl.unwrapCryptoObject(authenticationResult.getCryptoObject())));
                }

                @Override // com.tencent.soter.core.biometric.FaceidManagerProxy.AuthenticationCallback
                public void onAuthenticationFailed() {
                    SLogger.d(FaceidManagerImpl.TAG, "soter: basic onAuthenticationFailed", new Object[0]);
                    if (this.mMarkPermanentlyCallbacked) {
                        return;
                    }
                    this.mMarkPermanentlyCallbacked = true;
                    if (!FaceidManagerImpl.shouldInformTooManyTrial(this, context) && !SoterBiometricAntiBruteForceStrategy.isSystemHasAntiBruteForce()) {
                        SoterBiometricAntiBruteForceStrategy.addFailTime(context);
                        if (!SoterBiometricAntiBruteForceStrategy.isCurrentFailTimeAvailable(context)) {
                            SLogger.w(FaceidManagerImpl.TAG, "soter: too many fail trials", new Object[0]);
                            SoterBiometricAntiBruteForceStrategy.freeze(context);
                            FaceidManagerImpl.informTooManyTrial(this);
                            return;
                        }
                    }
                    authenticationCallback.onAuthenticationFailed();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean shouldInformTooManyTrial(FaceidManagerProxy.AuthenticationCallback authenticationCallback, Context context) {
            if (SoterBiometricAntiBruteForceStrategy.isSystemHasAntiBruteForce()) {
                SLogger.v(TAG, "soter: using system anti brute force strategy", new Object[0]);
                return false;
            }
            if (SoterBiometricAntiBruteForceStrategy.isCurrentTweenTimeAvailable(context)) {
                if (!SoterBiometricAntiBruteForceStrategy.isCurrentFailTimeAvailable(context)) {
                    SLogger.v(TAG, "soter: unfreeze former frozen status", new Object[0]);
                    SoterBiometricAntiBruteForceStrategy.unFreeze(context);
                }
                return false;
            }
            if (SoterBiometricAntiBruteForceStrategy.isCurrentFailTimeAvailable(context)) {
                SLogger.v(TAG, "soter: failure time available", new Object[0]);
                return false;
            }
            informTooManyTrial(authenticationCallback);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void informTooManyTrial(FaceidManagerProxy.AuthenticationCallback authenticationCallback) {
            SLogger.w(TAG, "soter: too many fail callback. inform it.", new Object[0]);
            authenticationCallback.onAuthenticationError(10308, "Too many failed times");
        }
    }
}
