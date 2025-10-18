package com.tencent.soter.core.sotercore;

import android.content.Context;
import com.tencent.soter.core.keystore.KeyGenParameterSpecCompatBuilder;
import com.tencent.soter.core.keystore.KeyPropertiesCompact;
import com.tencent.soter.core.model.ConstantsSoter;
import com.tencent.soter.core.model.SLogger;
import com.tencent.soter.core.model.SoterCoreData;
import com.tencent.soter.core.model.SoterCoreResult;
import com.tencent.soter.core.model.SoterCoreUtil;
import com.tencent.soter.core.model.SoterDelegate;
import com.tencent.soter.core.model.SoterErrCode;
import com.tencent.soter.core.model.SoterPubKeyModel;
import com.tencent.soter.soterserver.SoterSessionResult;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/* loaded from: classes2.dex */
public class SoterCoreBeforeTreble extends SoterCoreBase implements ConstantsSoter, SoterErrCode {
    private static final String MAGIC_SOTER_PWD = "from_soter_ui";
    private static final String TAG = "Soter.SoterCoreBeforeTreble";
    private static boolean isAlreadyCheckedSetUp = false;
    protected String providerName;

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public SoterSessionResult initSigh(String str, String str2) {
        return null;
    }

    public static void setUp() {
        try {
            try {
                try {
                    Method method = Class.forName("android.security.keystore.SoterKeyStoreProvider").getMethod("install", new Class[0]);
                    method.setAccessible(true);
                    method.invoke(null, new Object[0]);
                } catch (IllegalAccessException unused) {
                    SLogger.i(TAG, "soter: cannot access", new Object[0]);
                } catch (NoSuchMethodException unused2) {
                    SLogger.i(TAG, "soter: function not found", new Object[0]);
                }
            } catch (ClassNotFoundException unused3) {
                SLogger.i(TAG, "soter: no SoterProvider found", new Object[0]);
            } catch (InvocationTargetException unused4) {
                SLogger.i(TAG, "soter: InvocationTargetException", new Object[0]);
            }
        } finally {
            isAlreadyCheckedSetUp = true;
        }
    }

    public SoterCoreBeforeTreble(String str) {
        this.providerName = str;
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public boolean initSoter(Context context) {
        setUp();
        return true;
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public boolean isNativeSupportSoter() {
        if (!isAlreadyCheckedSetUp) {
            setUp();
        }
        if (SoterDelegate.isTriggeredOOM()) {
            SLogger.w(TAG, "hy: the device has already triggered OOM. mark as not support", new Object[0]);
            return false;
        }
        Provider[] providers = Security.getProviders();
        if (providers == null) {
            SLogger.e(TAG, "soter: no provider supported", new Object[0]);
            return false;
        }
        for (Provider provider : providers) {
            String name = provider.getName();
            if (name != null && name.startsWith(ConstantsSoter.SOTER_PROVIDER_NAME)) {
                SLogger.i(TAG, "soter: found soter provider", new Object[0]);
                return true;
            }
        }
        SLogger.i(TAG, "soter: soter provider not found", new Object[0]);
        return false;
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public SoterCoreResult generateAppGlobalSecureKey() throws NoSuchAlgorithmException, IOException, CertificateException, NoSuchProviderException, InvalidAlgorithmParameterException {
        SLogger.i(TAG, "soter: start generate ask", new Object[0]);
        if (isNativeSupportSoter()) {
            try {
                KeyStore.getInstance(this.providerName).load(null);
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyPropertiesCompact.KEY_ALGORITHM_RSA, ConstantsSoter.SOTER_PROVIDER_NAME);
                keyPairGenerator.initialize(KeyGenParameterSpecCompatBuilder.newInstance(SoterCoreData.getInstance().getAskName() + ".addcounter.auto_signed_when_get_pubkey_attk", 4).setDigests(KeyPropertiesCompact.DIGEST_SHA256).setSignaturePaddings(KeyPropertiesCompact.SIGNATURE_PADDING_RSA_PSS).build());
                long currentTicks = SoterCoreUtil.getCurrentTicks();
                keyPairGenerator.generateKeyPair();
                SLogger.i(TAG, "soter: generate successfully. cost: %d ms", Long.valueOf(SoterCoreUtil.ticksToNowInMs(currentTicks)));
                SoterDelegate.reset();
                return new SoterCoreResult(0);
            } catch (Exception e) {
                SLogger.e(TAG, "soter: generateAppGlobalSecureKey " + e.toString(), new Object[0]);
                SLogger.printErrStackTrace(TAG, e, "soter: generateAppGlobalSecureKey error");
                return new SoterCoreResult(4, e.toString());
            } catch (OutOfMemoryError e2) {
                SLogger.printErrStackTrace(TAG, e2, "soter: out of memory when generate ASK!! maybe no attk inside");
                SoterDelegate.onTriggerOOM();
            }
        } else {
            SLogger.e(TAG, "soter: not support soter", new Object[0]);
        }
        return new SoterCoreResult(2);
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public SoterCoreResult removeAppGlobalSecureKey() throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
        SLogger.i(TAG, "soter: start remove app global secure key", new Object[0]);
        if (isNativeSupportSoter()) {
            try {
                KeyStore keyStore = KeyStore.getInstance(this.providerName);
                keyStore.load(null);
                keyStore.deleteEntry(SoterCoreData.getInstance().getAskName());
                return new SoterCoreResult(0);
            } catch (Exception e) {
                SLogger.e(TAG, "soter: removeAppGlobalSecureKey " + e.toString(), new Object[0]);
                return new SoterCoreResult(5, e.toString());
            }
        }
        SLogger.e(TAG, "soter: not support soter", new Object[0]);
        return new SoterCoreResult(2);
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public boolean hasAppGlobalSecureKey() throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
        try {
            KeyStore keyStore = KeyStore.getInstance(this.providerName);
            keyStore.load(null);
            return keyStore.getCertificate(SoterCoreData.getInstance().getAskName()) != null;
        } catch (Exception e) {
            SLogger.e(TAG, "soter: hasAppGlobalSecureKey exception: " + e.toString(), new Object[0]);
            return false;
        }
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public boolean isAppGlobalSecureKeyValid() {
        return hasAppGlobalSecureKey() && getAppGlobalSecureKeyModel() != null;
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public SoterPubKeyModel getAppGlobalSecureKeyModel() throws NoSuchAlgorithmException, UnrecoverableKeyException, IOException, KeyStoreException, CertificateException {
        SLogger.i(TAG, "soter: start get app global secure key pub", new Object[0]);
        if (isNativeSupportSoter()) {
            try {
                KeyStore keyStore = KeyStore.getInstance(this.providerName);
                keyStore.load(null);
                try {
                    Key key = keyStore.getKey(SoterCoreData.getInstance().getAskName(), MAGIC_SOTER_PWD.toCharArray());
                    if (key != null) {
                        SoterDelegate.reset();
                        return retrieveJsonFromExportedData(key.getEncoded());
                    }
                    SLogger.e(TAG, "soter: key can not be retrieved", new Object[0]);
                    return null;
                } catch (ClassCastException e) {
                    SLogger.e(TAG, "soter: cast error: " + e.toString(), new Object[0]);
                    return null;
                }
            } catch (Exception e2) {
                SLogger.printErrStackTrace(TAG, e2, "soter: error when get ask");
            } catch (OutOfMemoryError e3) {
                SLogger.printErrStackTrace(TAG, e3, "soter: out of memory when getting ask!! maybe no attk inside");
                SoterDelegate.onTriggerOOM();
            }
        } else {
            SLogger.e(TAG, "soter: not support soter", new Object[0]);
        }
        return null;
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public SoterCoreResult generateAuthKey(String str) throws NoSuchAlgorithmException, IOException, CertificateException, NoSuchProviderException, InvalidAlgorithmParameterException {
        if (SoterCoreUtil.isNullOrNil(str)) {
            SLogger.e(TAG, "soter: auth key name is null or nil. abort.", new Object[0]);
            return new SoterCoreResult(1, "no authKeyName");
        }
        if (isNativeSupportSoter()) {
            try {
                try {
                    if (!hasAppGlobalSecureKey()) {
                        return new SoterCoreResult(3, "app secure key not exist");
                    }
                    KeyStore.getInstance(this.providerName).load(null);
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyPropertiesCompact.KEY_ALGORITHM_RSA, this.providerName);
                    try {
                        keyPairGenerator.initialize(KeyGenParameterSpecCompatBuilder.newInstance(str + String.format(".addcounter.auto_signed_when_get_pubkey(%s).secmsg_and_counter_signed_when_sign", SoterCoreData.getInstance().getAskName()), 4).setDigests(KeyPropertiesCompact.DIGEST_SHA256).setUserAuthenticationRequired(true).setSignaturePaddings(KeyPropertiesCompact.SIGNATURE_PADDING_RSA_PSS).build());
                        long currentTicks = SoterCoreUtil.getCurrentTicks();
                        keyPairGenerator.generateKeyPair();
                        SLogger.i(TAG, "soter: generate successfully, cost: %d ms", Long.valueOf(SoterCoreUtil.ticksToNowInMs(currentTicks)));
                        SoterDelegate.reset();
                        return new SoterCoreResult(0);
                    } catch (Exception e) {
                        SLogger.e(TAG, "soter: cause exception. maybe reflection exception: " + e.toString(), new Object[0]);
                        return new SoterCoreResult(6, e.toString());
                    }
                } catch (Exception e2) {
                    SLogger.e(TAG, "soter: generate auth key failed: " + e2.toString(), new Object[0]);
                    return new SoterCoreResult(6, e2.toString());
                }
            } catch (OutOfMemoryError e3) {
                SLogger.printErrStackTrace(TAG, e3, "soter: out of memory when generate AuthKey!! maybe no attk inside");
                SoterDelegate.onTriggerOOM();
            }
        } else {
            SLogger.e(TAG, "soter: not support soter", new Object[0]);
        }
        return new SoterCoreResult(2);
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public SoterCoreResult removeAuthKey(String str, boolean z) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
        if (SoterCoreUtil.isNullOrNil(str)) {
            SLogger.e(TAG, "soter: auth key name is null or nil. abort.", new Object[0]);
            return new SoterCoreResult(1, "no authKeyName");
        }
        SLogger.i(TAG, "soter: start remove key: " + str, new Object[0]);
        if (isNativeSupportSoter()) {
            try {
                KeyStore keyStore = KeyStore.getInstance(this.providerName);
                keyStore.load(null);
                keyStore.deleteEntry(str);
                if (z) {
                    SLogger.i(TAG, "soter: auto delete ask", new Object[0]);
                    if (hasAppGlobalSecureKey()) {
                        removeAppGlobalSecureKey();
                    }
                }
                return new SoterCoreResult(0);
            } catch (Exception e) {
                SLogger.e(TAG, "soter: removeAuthKey " + e.toString(), new Object[0]);
                return new SoterCoreResult(7, e.toString());
            }
        }
        SLogger.e(TAG, "soter: not support soter", new Object[0]);
        return new SoterCoreResult(2);
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public boolean hasAuthKey(String str) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
        if (SoterCoreUtil.isNullOrNil(str)) {
            SLogger.e(TAG, "soter: authkey name not correct", new Object[0]);
            return false;
        }
        try {
            KeyStore keyStore = KeyStore.getInstance(this.providerName);
            keyStore.load(null);
            return keyStore.getCertificate(str) != null;
        } catch (Exception e) {
            SLogger.e(TAG, "soter: hasAppGlobalSecureKey exception: " + e.toString(), new Object[0]);
            return false;
        }
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public byte[] finishSign(long j) throws Exception {
        return new byte[0];
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public boolean isAuthKeyValid(String str, boolean z) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
        SLogger.i(TAG, String.format("soter: checking key valid: auth key name: %s, autoDelIfNotValid: %b ", str, Boolean.valueOf(z)), new Object[0]);
        if (SoterCoreUtil.isNullOrNil(str)) {
            SLogger.e(TAG, "soter: checking key valid: authkey name not correct", new Object[0]);
            return false;
        }
        try {
            initAuthKeySignature(str);
            SLogger.i(TAG, "soter: key valid", new Object[0]);
            SoterDelegate.reset();
            return true;
        } catch (Exception e) {
            SLogger.e(TAG, "soter: occurs other exceptions: %s", e.toString());
            SLogger.printErrStackTrace(TAG, e, "soter: occurs other exceptions");
            return false;
        } catch (OutOfMemoryError e2) {
            SLogger.printErrStackTrace(TAG, e2, "soter: out of memory when isAuthKeyValid!! maybe no attk inside");
            SoterDelegate.onTriggerOOM();
            return false;
        } catch (InvalidKeyException | UnrecoverableEntryException unused) {
            SLogger.e(TAG, "soter: key invalid.", new Object[0]);
            if (z) {
                removeAuthKey(str, false);
            }
            return false;
        }
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public SoterPubKeyModel getAuthKeyModel(String str) throws NoSuchAlgorithmException, UnrecoverableKeyException, IOException, KeyStoreException, CertificateException {
        if (SoterCoreUtil.isNullOrNil(str)) {
            SLogger.e(TAG, "soter: auth key name is null or nil. abort.", new Object[0]);
            return null;
        }
        if (isNativeSupportSoter()) {
            try {
                KeyStore keyStore = KeyStore.getInstance(this.providerName);
                keyStore.load(null);
                try {
                    Key key = keyStore.getKey(str, MAGIC_SOTER_PWD.toCharArray());
                    SoterDelegate.reset();
                    if (key != null) {
                        return retrieveJsonFromExportedData(key.getEncoded());
                    }
                    SLogger.e(TAG, "soter: key can not be retrieved", new Object[0]);
                    return null;
                } catch (ClassCastException e) {
                    SLogger.e(TAG, "soter: cast error: " + e.toString(), new Object[0]);
                    return null;
                }
            } catch (Exception e2) {
                SLogger.printErrStackTrace(TAG, e2, "soter: error in get auth key model");
            } catch (OutOfMemoryError e3) {
                SLogger.printErrStackTrace(TAG, e3, "soter: out of memory when getAuthKeyModel!! maybe no attk inside");
                SoterDelegate.onTriggerOOM();
            }
        } else {
            SLogger.e(TAG, "soter: not support soter " + this.providerName, new Object[0]);
        }
        return null;
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public Signature getAuthInitAndSign(String str) {
        if (SoterCoreUtil.isNullOrNil(str)) {
            SLogger.e(TAG, "soter: auth key name is null or nil. abort.", new Object[0]);
            return null;
        }
        if (isNativeSupportSoter()) {
            try {
                SoterDelegate.reset();
                return initAuthKeySignature(str);
            } catch (OutOfMemoryError e) {
                SLogger.printErrStackTrace(TAG, e, "soter: out of memory when getAuthInitAndSign!! maybe no attk inside");
                SoterDelegate.onTriggerOOM();
                return null;
            } catch (InvalidKeyException | UnrecoverableEntryException unused) {
                SLogger.e(TAG, "soter: key invalid. Advice remove the key", new Object[0]);
                return null;
            } catch (Exception e2) {
                SLogger.e(TAG, "soter: exception when getSignatureResult: " + e2.toString(), new Object[0]);
                SLogger.printErrStackTrace(TAG, e2, "soter: exception when getSignatureResult");
                return null;
            }
        }
        SLogger.e(TAG, "soter: not support soter" + this.providerName, new Object[0]);
        return null;
    }

    @Override // com.tencent.soter.core.sotercore.SoterCoreBase
    public Signature initAuthKeySignature(String str) throws NoSuchAlgorithmException, IOException, InvalidKeyException, KeyStoreException, CertificateException, NoSuchProviderException, UnrecoverableEntryException {
        if (SoterCoreUtil.isNullOrNil(str)) {
            SLogger.e(TAG, "soter: auth key name is null or nil. abort.", new Object[0]);
            return null;
        }
        Signature signature = Signature.getInstance("SHA256withRSA/PSS", "AndroidKeyStoreBCWorkaround");
        KeyStore keyStore = KeyStore.getInstance(this.providerName);
        keyStore.load(null);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(str, null);
        if (privateKeyEntry != null) {
            signature.initSign(privateKeyEntry.getPrivateKey());
            return signature;
        }
        SLogger.e(TAG, "soter: entry not exists", new Object[0]);
        return null;
    }
}
