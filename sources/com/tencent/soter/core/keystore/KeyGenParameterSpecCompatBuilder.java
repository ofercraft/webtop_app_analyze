package com.tencent.soter.core.keystore;

import android.security.keystore.KeyGenParameterSpec;
import com.tencent.soter.core.SoterCore;
import com.tencent.soter.core.model.SLogger;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;
import javax.security.auth.x500.X500Principal;

/* loaded from: classes2.dex */
public abstract class KeyGenParameterSpecCompatBuilder {
    private static final String TAG = "Soter.KeyGenParameterSpecCompatBuilder";

    public abstract AlgorithmParameterSpec build() throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, InvocationTargetException;

    public abstract KeyGenParameterSpecCompatBuilder setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec);

    public abstract KeyGenParameterSpecCompatBuilder setBlockModes(String... strArr);

    public abstract KeyGenParameterSpecCompatBuilder setCertificateNotAfter(Date date);

    public abstract KeyGenParameterSpecCompatBuilder setCertificateNotBefore(Date date);

    public abstract KeyGenParameterSpecCompatBuilder setCertificateSerialNumber(BigInteger bigInteger);

    public abstract KeyGenParameterSpecCompatBuilder setCertificateSubject(X500Principal x500Principal);

    public abstract KeyGenParameterSpecCompatBuilder setDigests(String... strArr);

    public abstract KeyGenParameterSpecCompatBuilder setEncryptionPaddings(String... strArr);

    public abstract KeyGenParameterSpecCompatBuilder setKeySize(int i);

    public abstract KeyGenParameterSpecCompatBuilder setKeyValidityEnd(Date date);

    public abstract KeyGenParameterSpecCompatBuilder setKeyValidityStart(Date date);

    public abstract KeyGenParameterSpecCompatBuilder setRandomizedEncryptionRequired(boolean z);

    public abstract KeyGenParameterSpecCompatBuilder setSignaturePaddings(String... strArr);

    public abstract KeyGenParameterSpecCompatBuilder setUserAuthenticationRequired(boolean z);

    public abstract KeyGenParameterSpecCompatBuilder setUserAuthenticationValidityDurationSeconds(int i);

    public KeyGenParameterSpecCompatBuilder(String str, int i) {
    }

    public static KeyGenParameterSpecCompatBuilder newInstance(String str, int i) {
        if (SoterCore.isNativeSupportSoter()) {
            return new NormalKeyGenParameterSpecCompatBuilder(str, i);
        }
        SLogger.e(TAG, "soter: not support soter. return dummy", new Object[0]);
        return new DummyKeyGenParameterSpecCompatBuilder(str, i);
    }

    private static class NormalKeyGenParameterSpecCompatBuilder extends KeyGenParameterSpecCompatBuilder {
        private KeyGenParameterSpec.Builder builder;

        public NormalKeyGenParameterSpecCompatBuilder(String str, int i) {
            super(str, i);
            this.builder = null;
            this.builder = new KeyGenParameterSpec.Builder(str, i);
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public AlgorithmParameterSpec build() throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, InvocationTargetException {
            return this.builder.build();
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setKeySize(int i) {
            this.builder.setKeySize(i);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {
            this.builder.setAlgorithmParameterSpec(algorithmParameterSpec);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateSubject(X500Principal x500Principal) {
            this.builder.setCertificateSubject(x500Principal);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateSerialNumber(BigInteger bigInteger) {
            this.builder.setCertificateSerialNumber(bigInteger);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateNotBefore(Date date) {
            this.builder.setCertificateNotBefore(date);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateNotAfter(Date date) {
            this.builder.setCertificateNotAfter(date);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setKeyValidityStart(Date date) {
            this.builder.setKeyValidityStart(date);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setKeyValidityEnd(Date date) {
            this.builder.setKeyValidityEnd(date);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setDigests(String... strArr) {
            this.builder.setDigests(strArr);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setSignaturePaddings(String... strArr) {
            this.builder.setSignaturePaddings(strArr);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setEncryptionPaddings(String... strArr) {
            this.builder.setEncryptionPaddings(strArr);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setBlockModes(String... strArr) {
            this.builder.setBlockModes(strArr);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setRandomizedEncryptionRequired(boolean z) {
            this.builder.setRandomizedEncryptionRequired(z);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setUserAuthenticationRequired(boolean z) {
            this.builder.setUserAuthenticationRequired(z);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setUserAuthenticationValidityDurationSeconds(int i) {
            this.builder.setUserAuthenticationValidityDurationSeconds(i);
            return this;
        }
    }

    static class ReflectKeyGenParameterSpecCompatBuilder extends KeyGenParameterSpecCompatBuilder {
        private static final String CLASSNAME = "android.security.keystore.KeyGenParameterSpec";
        private String[] mBlockModes;
        private Date mCertificateNotAfter;
        private Date mCertificateNotBefore;
        private BigInteger mCertificateSerialNumber;
        private X500Principal mCertificateSubject;
        private String[] mDigests;
        private String[] mEncryptionPaddings;
        private int mKeySize;
        private Date mKeyValidityForConsumptionEnd;
        private Date mKeyValidityForOriginationEnd;
        private Date mKeyValidityStart;
        private final String mKeystoreAlias;
        private int mPurposes;
        private boolean mRandomizedEncryptionRequired;
        private String[] mSignaturePaddings;
        private AlgorithmParameterSpec mSpec;
        private boolean mUserAuthenticationRequired;
        private int mUserAuthenticationValidityDurationSeconds;

        public ReflectKeyGenParameterSpecCompatBuilder(String str, int i) {
            super(str, i);
            this.mKeySize = -1;
            this.mRandomizedEncryptionRequired = true;
            this.mUserAuthenticationValidityDurationSeconds = -1;
            if (str == null) {
                throw new NullPointerException("keystoreAlias == null");
            }
            if (str.isEmpty()) {
                throw new IllegalArgumentException("keystoreAlias must not be empty");
            }
            this.mKeystoreAlias = str;
            this.mPurposes = i;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setKeySize(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("keySize < 0");
            }
            this.mKeySize = i;
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {
            if (algorithmParameterSpec == null) {
                throw new NullPointerException("spec == null");
            }
            this.mSpec = algorithmParameterSpec;
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateSubject(X500Principal x500Principal) {
            if (x500Principal == null) {
                throw new NullPointerException("subject == null");
            }
            this.mCertificateSubject = x500Principal;
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateSerialNumber(BigInteger bigInteger) {
            if (bigInteger == null) {
                throw new NullPointerException("serialNumber == null");
            }
            this.mCertificateSerialNumber = bigInteger;
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateNotBefore(Date date) {
            if (date == null) {
                throw new NullPointerException("date == null");
            }
            this.mCertificateNotBefore = cloneIfNotNull(date);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateNotAfter(Date date) {
            if (date == null) {
                throw new NullPointerException("date == null");
            }
            this.mCertificateNotAfter = cloneIfNotNull(date);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setKeyValidityStart(Date date) {
            this.mKeyValidityStart = cloneIfNotNull(date);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setKeyValidityEnd(Date date) {
            setKeyValidityForOriginationEnd(date);
            setKeyValidityForConsumptionEnd(date);
            return this;
        }

        public KeyGenParameterSpecCompatBuilder setKeyValidityForOriginationEnd(Date date) {
            this.mKeyValidityForOriginationEnd = cloneIfNotNull(date);
            return this;
        }

        public KeyGenParameterSpecCompatBuilder setKeyValidityForConsumptionEnd(Date date) {
            this.mKeyValidityForConsumptionEnd = cloneIfNotNull(date);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setDigests(String... strArr) {
            this.mDigests = cloneIfNotEmpty(strArr);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setEncryptionPaddings(String... strArr) {
            this.mEncryptionPaddings = cloneIfNotEmpty(strArr);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setSignaturePaddings(String... strArr) {
            this.mSignaturePaddings = cloneIfNotEmpty(strArr);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setBlockModes(String... strArr) {
            this.mBlockModes = cloneIfNotEmpty(strArr);
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setRandomizedEncryptionRequired(boolean z) {
            this.mRandomizedEncryptionRequired = z;
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setUserAuthenticationRequired(boolean z) {
            this.mUserAuthenticationRequired = z;
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setUserAuthenticationValidityDurationSeconds(int i) {
            if (i < -1) {
                throw new IllegalArgumentException("seconds must be -1 or larger");
            }
            this.mUserAuthenticationValidityDurationSeconds = i;
            return this;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public AlgorithmParameterSpec build() throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, InvocationTargetException {
            return (AlgorithmParameterSpec) Class.forName(CLASSNAME).getConstructor(String.class, Integer.TYPE, AlgorithmParameterSpec.class, X500Principal.class, BigInteger.class, Date.class, Date.class, Date.class, Date.class, Date.class, Integer.TYPE, String[].class, String[].class, String[].class, String[].class, Boolean.TYPE, Boolean.TYPE, Integer.TYPE).newInstance(this.mKeystoreAlias, Integer.valueOf(this.mKeySize), this.mSpec, this.mCertificateSubject, this.mCertificateSerialNumber, this.mCertificateNotBefore, this.mCertificateNotAfter, this.mKeyValidityStart, this.mKeyValidityForOriginationEnd, this.mKeyValidityForConsumptionEnd, Integer.valueOf(this.mPurposes), this.mDigests, this.mEncryptionPaddings, this.mSignaturePaddings, this.mBlockModes, Boolean.valueOf(this.mRandomizedEncryptionRequired), Boolean.valueOf(this.mUserAuthenticationRequired), Integer.valueOf(this.mUserAuthenticationValidityDurationSeconds));
        }
    }

    static class DummyKeyGenParameterSpecCompatBuilder extends KeyGenParameterSpecCompatBuilder {
        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public AlgorithmParameterSpec build() {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setBlockModes(String... strArr) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateNotAfter(Date date) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateNotBefore(Date date) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateSerialNumber(BigInteger bigInteger) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setCertificateSubject(X500Principal x500Principal) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setDigests(String... strArr) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setEncryptionPaddings(String... strArr) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setKeySize(int i) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setKeyValidityEnd(Date date) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setKeyValidityStart(Date date) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setRandomizedEncryptionRequired(boolean z) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setSignaturePaddings(String... strArr) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setUserAuthenticationRequired(boolean z) {
            return null;
        }

        @Override // com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder
        public KeyGenParameterSpecCompatBuilder setUserAuthenticationValidityDurationSeconds(int i) {
            return null;
        }

        public DummyKeyGenParameterSpecCompatBuilder(String str, int i) {
            super(str, i);
        }
    }

    static Date cloneIfNotNull(Date date) {
        if (date != null) {
            return (Date) date.clone();
        }
        return null;
    }

    public static String[] cloneIfNotEmpty(String[] strArr) {
        return (strArr == null || strArr.length <= 0) ? strArr : (String[]) strArr.clone();
    }

    public static byte[] cloneIfNotEmpty(byte[] bArr) {
        return (bArr == null || bArr.length <= 0) ? bArr : (byte[]) bArr.clone();
    }
}
