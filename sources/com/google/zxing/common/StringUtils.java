package com.google.zxing.common;

import com.google.zxing.DecodeHintType;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

/* loaded from: classes2.dex */
public final class StringUtils {
    private static final boolean ASSUME_SHIFT_JIS;
    private static final Charset EUC_JP;
    public static final String GB2312 = "GB2312";
    public static final Charset GB2312_CHARSET;
    private static final Charset PLATFORM_DEFAULT_ENCODING = Charset.defaultCharset();
    public static final String SHIFT_JIS = "SJIS";
    public static final Charset SHIFT_JIS_CHARSET;

    static {
        Charset charsetForName;
        Charset charsetForName2;
        Charset charsetForName3 = null;
        try {
            charsetForName = Charset.forName(SHIFT_JIS);
        } catch (UnsupportedCharsetException unused) {
            charsetForName = null;
        }
        SHIFT_JIS_CHARSET = charsetForName;
        try {
            charsetForName2 = Charset.forName(GB2312);
        } catch (UnsupportedCharsetException unused2) {
            charsetForName2 = null;
        }
        GB2312_CHARSET = charsetForName2;
        try {
            charsetForName3 = Charset.forName("EUC_JP");
        } catch (UnsupportedCharsetException unused3) {
        }
        EUC_JP = charsetForName3;
        Charset charset = SHIFT_JIS_CHARSET;
        ASSUME_SHIFT_JIS = (charset != null && charset.equals(PLATFORM_DEFAULT_ENCODING)) || (charsetForName3 != null && charsetForName3.equals(PLATFORM_DEFAULT_ENCODING));
    }

    private StringUtils() {
    }

    public static String guessEncoding(byte[] bArr, Map<DecodeHintType, ?> map) {
        Charset charsetGuessCharset = guessCharset(bArr, map);
        if (charsetGuessCharset.equals(SHIFT_JIS_CHARSET)) {
            return SHIFT_JIS;
        }
        if (charsetGuessCharset.equals(StandardCharsets.UTF_8)) {
            return "UTF8";
        }
        if (charsetGuessCharset.equals(StandardCharsets.ISO_8859_1)) {
            return "ISO8859_1";
        }
        return charsetGuessCharset.name();
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x0118 A[PHI: r19
  0x0118: PHI (r19v6 boolean) = (r19v5 boolean), (r19v5 boolean), (r19v5 boolean), (r19v7 boolean), (r19v7 boolean), (r19v7 boolean) binds: [B:87:0x00e2, B:89:0x00e6, B:91:0x00ea, B:80:0x00d2, B:81:0x00d4, B:83:0x00d8] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.nio.charset.Charset guessCharset(byte[] r21, java.util.Map<com.google.zxing.DecodeHintType, ?> r22) {
        /*
            Method dump skipped, instructions count: 367
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.common.StringUtils.guessCharset(byte[], java.util.Map):java.nio.charset.Charset");
    }
}
