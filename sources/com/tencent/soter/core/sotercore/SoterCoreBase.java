package com.tencent.soter.core.sotercore;

import android.content.Context;
import android.util.Base64;
import com.tencent.soter.core.model.SLogger;
import com.tencent.soter.core.model.SoterCoreResult;
import com.tencent.soter.core.model.SoterPubKeyModel;
import com.tencent.soter.soterserver.SoterSessionResult;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

/* loaded from: classes2.dex */
public abstract class SoterCoreBase {
    protected static final int RAW_LENGTH_PREFIX = 4;
    protected static final String TAG = "Soter.SoterCoreBase";

    public abstract byte[] finishSign(long j) throws Exception;

    public abstract SoterCoreResult generateAppGlobalSecureKey();

    public abstract SoterCoreResult generateAuthKey(String str);

    public abstract SoterPubKeyModel getAppGlobalSecureKeyModel();

    public abstract Signature getAuthInitAndSign(String str);

    public abstract SoterPubKeyModel getAuthKeyModel(String str);

    public abstract boolean hasAppGlobalSecureKey();

    public abstract boolean hasAuthKey(String str);

    public abstract Signature initAuthKeySignature(String str) throws NoSuchAlgorithmException, InvalidKeyException, IOException, KeyStoreException, CertificateException, NoSuchProviderException, UnrecoverableEntryException;

    public abstract SoterSessionResult initSigh(String str, String str2);

    public abstract boolean initSoter(Context context);

    public abstract boolean isAppGlobalSecureKeyValid();

    public abstract boolean isAuthKeyValid(String str, boolean z);

    public abstract boolean isNativeSupportSoter();

    public boolean isTrebleServiceConnected() {
        return true;
    }

    public void releaseTrebleServiceConnection() {
    }

    public abstract SoterCoreResult removeAppGlobalSecureKey();

    public abstract SoterCoreResult removeAuthKey(String str, boolean z);

    public void setTrebleServiceListener(SoterCoreTrebleServiceListener soterCoreTrebleServiceListener) {
    }

    public void triggerTrebleServiceConnecting() {
    }

    public void updateExtraParam() {
    }

    protected static int toInt(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            i += (bArr[i2] & 255) << (i2 * 8);
        }
        return i;
    }

    protected static SoterPubKeyModel retrieveJsonFromExportedData(byte[] bArr) {
        if (bArr == null) {
            SLogger.e(TAG, "soter: raw data is null", new Object[0]);
            return null;
        }
        if (bArr.length < 4) {
            SLogger.e(TAG, "soter: raw data length smaller than RAW_LENGTH_PREFIX", new Object[0]);
        }
        byte[] bArr2 = new byte[4];
        System.arraycopy(bArr, 0, bArr2, 0, 4);
        int i = toInt(bArr2);
        SLogger.d(TAG, "soter: parsed raw length: " + i, new Object[0]);
        if (i > 1048576) {
            SLogger.e(TAG, "soter: too large json result!", new Object[0]);
            return null;
        }
        byte[] bArr3 = new byte[i];
        int i2 = i + 4;
        if (bArr.length < i2) {
            SLogger.e(TAG, "length not correct 2", new Object[0]);
            return null;
        }
        System.arraycopy(bArr, 4, bArr3, 0, i);
        String str = new String(bArr3);
        SLogger.d(TAG, "soter: to convert json: ".concat(str), new Object[0]);
        SoterPubKeyModel soterPubKeyModel = new SoterPubKeyModel(str, "");
        int length = bArr.length - i2;
        SLogger.d(TAG, "soter: signature length: " + length, new Object[0]);
        if (length != 0) {
            byte[] bArr4 = new byte[length];
            System.arraycopy(bArr, i2, bArr4, 0, length);
            soterPubKeyModel.setSignature(Base64.encodeToString(bArr4, 2));
        }
        return soterPubKeyModel;
    }
}
