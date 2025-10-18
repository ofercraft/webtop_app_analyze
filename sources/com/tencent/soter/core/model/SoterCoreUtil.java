package com.tencent.soter.core.model;

import com.google.common.base.Ascii;
import com.tencent.soter.core.keystore.KeyPropertiesCompact;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes2.dex */
public class SoterCoreUtil {
    public static boolean isNullOrNil(String str) {
        return str == null || str.length() <= 0;
    }

    public static boolean isNullOrNil(byte[] bArr) {
        return bArr == null || bArr.length <= 0;
    }

    public static boolean isNullOrNil(int[] iArr) {
        return iArr == null || iArr.length <= 0;
    }

    public static String nullAsNil(String str) {
        return str == null ? "" : str;
    }

    public static long getCurrentTicks() {
        return System.nanoTime();
    }

    public static long ticksToNowInMs(long j) {
        return ((System.nanoTime() - j) / 1000) / 1000;
    }

    public static String getMessageDigest(byte[] bArr) throws NoSuchAlgorithmException {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KeyPropertiesCompact.DIGEST_MD5);
            messageDigest.update(bArr);
            byte[] bArrDigest = messageDigest.digest();
            char[] cArr2 = new char[bArrDigest.length * 2];
            int i = 0;
            for (byte b : bArrDigest) {
                int i2 = i + 1;
                cArr2[i] = cArr[(b >>> 4) & 15];
                i += 2;
                cArr2[i2] = cArr[b & Ascii.SI];
            }
            return new String(cArr2);
        } catch (Exception unused) {
            return null;
        }
    }
}
