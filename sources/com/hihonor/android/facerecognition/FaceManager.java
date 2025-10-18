package com.hihonor.android.facerecognition;

import android.os.CancellationSignal;
import android.os.Handler;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: FaceManager.kt */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b&\u0018\u0000 \u00172\u00020\u0001:\u0004\u0014\u0015\u0016\u0017B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H&J8\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H&R\u0012\u0010\u0006\u001a\u00020\u0005X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0018"}, d2 = {"Lcom/hihonor/android/facerecognition/FaceManager;", "", "<init>", "()V", "hasEnrolledTemplates", "", "isHardwareDetected", "()Z", "authenticate", "", "var1", "Lcom/hihonor/android/facerecognition/FaceManager$CryptoObject;", "var2", "Landroid/os/CancellationSignal;", "var3", "", "var4", "Lcom/hihonor/android/facerecognition/FaceManager$AuthenticationCallback;", "var5", "Landroid/os/Handler;", "AuthenticationCallback", "AuthenticationResult", "CryptoObject", "Companion", "biometric_release"}, k = 1, mv = {2, 1, 0}, xi = 48)
/* loaded from: classes2.dex */
public abstract class FaceManager {
    public static final int FACE_ACQUIRED_GOOD = 0;
    public static final int FACE_ACQUIRED_INSUFFICIENT = 1;
    public static final int FACE_ACQUIRED_NOT_DETECTED = 12;
    public static final int FACE_ACQUIRED_POOR_GAZE = 11;
    public static final int FACE_ACQUIRED_TOO_BRIGHT = 2;
    public static final int FACE_ACQUIRED_TOO_CLOSE = 4;
    public static final int FACE_ACQUIRED_TOO_DARK = 3;
    public static final int FACE_ACQUIRED_TOO_FAR = 5;
    public static final int FACE_ACQUIRED_TOO_HIGH = 6;
    public static final int FACE_ACQUIRED_TOO_LEFT = 9;
    public static final int FACE_ACQUIRED_TOO_LOW = 7;
    public static final int FACE_ACQUIRED_TOO_MUCH_MOTION = 10;
    public static final int FACE_ACQUIRED_TOO_RIGHT = 8;
    public static final int FACE_ACQUIRED_VENDOR = 13;
    public static final int FACE_ERROR_CANCELED = 5;
    public static final int FACE_ERROR_HW_NOT_PRESENT = 12;
    public static final int FACE_ERROR_HW_UNAVAILABLE = 1;
    public static final int FACE_ERROR_LOCKOUT = 7;
    public static final int FACE_ERROR_LOCKOUT_PERMANENT = 9;
    public static final int FACE_ERROR_NOT_ENROLLED = 11;
    public static final int FACE_ERROR_NO_SPACE = 4;
    public static final int FACE_ERROR_TIMEOUT = 3;
    public static final int FACE_ERROR_UNABLE_TO_PROCESS = 2;
    public static final int FACE_ERROR_UNABLE_TO_REMOVE = 6;
    public static final int FACE_ERROR_USER_CANCELED = 10;
    public static final int FACE_ERROR_VENDOR = 8;
    public static final int FACE_ERROR_VENDOR_BASE = 1000;
    private static final String TAG = "Facerecognition.FaceManager";

    /* compiled from: FaceManager.kt */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u001a\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\u001a\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00072\b\u0010\f\u001a\u0004\u0018\u00010\tH\u0016J\u0012\u0010\r\u001a\u00020\u00052\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0005H\u0016¨\u0006\u0011"}, d2 = {"Lcom/hihonor/android/facerecognition/FaceManager$AuthenticationCallback;", "", "<init>", "()V", "onAuthenticationError", "", "errorCode", "", "errString", "", "onAuthenticationHelp", "helpCode", "helpString", "onAuthenticationSucceeded", "result", "Lcom/hihonor/android/facerecognition/FaceManager$AuthenticationResult;", "onAuthenticationFailed", "biometric_release"}, k = 1, mv = {2, 1, 0}, xi = 48)
    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int errorCode, CharSequence errString) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult result) {
        }
    }

    public abstract void authenticate(CryptoObject var1, CancellationSignal var2, int var3, AuthenticationCallback var4, Handler var5);

    public abstract boolean hasEnrolledTemplates();

    public abstract boolean isHardwareDetected();

    /* compiled from: FaceManager.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/hihonor/android/facerecognition/FaceManager$AuthenticationResult;", "", "cryptoObject", "Lcom/hihonor/android/facerecognition/FaceManager$CryptoObject;", "<init>", "(Lcom/hihonor/android/facerecognition/FaceManager$CryptoObject;)V", "getCryptoObject", "()Lcom/hihonor/android/facerecognition/FaceManager$CryptoObject;", "biometric_release"}, k = 1, mv = {2, 1, 0}, xi = 48)
    public static final class AuthenticationResult {
        private final CryptoObject cryptoObject;

        public AuthenticationResult(CryptoObject cryptoObject) {
            Intrinsics.checkNotNullParameter(cryptoObject, "cryptoObject");
            this.cryptoObject = cryptoObject;
        }

        public final CryptoObject getCryptoObject() {
            return this.cryptoObject;
        }
    }

    /* compiled from: FaceManager.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0011\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005B\u0011\b\u0016\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\u0004\u0010\bB\u0011\b\u0016\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0004\b\u0004\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u00038F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u00078F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\t\u001a\u0004\u0018\u00010\n8F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0013"}, d2 = {"Lcom/hihonor/android/facerecognition/FaceManager$CryptoObject;", "", "signature", "Ljava/security/Signature;", "<init>", "(Ljava/security/Signature;)V", "cipher", "Ljavax/crypto/Cipher;", "(Ljavax/crypto/Cipher;)V", "mac", "Ljavax/crypto/Mac;", "(Ljavax/crypto/Mac;)V", "mCrypto", "getSignature", "()Ljava/security/Signature;", "getCipher", "()Ljavax/crypto/Cipher;", "getMac", "()Ljavax/crypto/Mac;", "biometric_release"}, k = 1, mv = {2, 1, 0}, xi = 48)
    public static final class CryptoObject {
        private final Object mCrypto;

        public CryptoObject(Signature signature) {
            Intrinsics.checkNotNullParameter(signature, "signature");
            this.mCrypto = signature;
        }

        public CryptoObject(Cipher cipher) {
            Intrinsics.checkNotNullParameter(cipher, "cipher");
            this.mCrypto = cipher;
        }

        public CryptoObject(Mac mac) {
            Intrinsics.checkNotNullParameter(mac, "mac");
            this.mCrypto = mac;
        }

        public final Signature getSignature() {
            Object obj = this.mCrypto;
            if (obj instanceof Signature) {
                return (Signature) obj;
            }
            return null;
        }

        public final Cipher getCipher() {
            Object obj = this.mCrypto;
            if (obj instanceof Cipher) {
                return (Cipher) obj;
            }
            return null;
        }

        public final Mac getMac() {
            Object obj = this.mCrypto;
            if (obj instanceof Mac) {
                return (Mac) obj;
            }
            return null;
        }
    }
}
