package com.google.zxing.aztec;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;

/* loaded from: classes2.dex */
public final class AztecReader implements Reader {
    @Override // com.google.zxing.Reader
    public void reset() {
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return decode(binaryBitmap, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0071 A[LOOP:0: B:39:0x006f->B:40:0x0071, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00a1  */
    @Override // com.google.zxing.Reader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.google.zxing.Result decode(com.google.zxing.BinaryBitmap r11, java.util.Map<com.google.zxing.DecodeHintType, ?> r12) throws com.google.zxing.NotFoundException, com.google.zxing.FormatException {
        /*
            r10 = this;
            com.google.zxing.aztec.detector.Detector r10 = new com.google.zxing.aztec.detector.Detector
            com.google.zxing.common.BitMatrix r11 = r11.getBlackMatrix()
            r10.<init>(r11)
            r11 = 0
            r1 = 0
            com.google.zxing.aztec.AztecDetectorResult r0 = r10.detect(r1)     // Catch: com.google.zxing.FormatException -> L2e com.google.zxing.NotFoundException -> L36
            com.google.zxing.ResultPoint[] r2 = r0.getPoints()     // Catch: com.google.zxing.FormatException -> L2e com.google.zxing.NotFoundException -> L36
            int r3 = r0.getErrorsCorrected()     // Catch: com.google.zxing.FormatException -> L2a com.google.zxing.NotFoundException -> L2c
            com.google.zxing.aztec.decoder.Decoder r4 = new com.google.zxing.aztec.decoder.Decoder     // Catch: com.google.zxing.FormatException -> L26 com.google.zxing.NotFoundException -> L28
            r4.<init>()     // Catch: com.google.zxing.FormatException -> L26 com.google.zxing.NotFoundException -> L28
            com.google.zxing.common.DecoderResult r0 = r4.decode(r0)     // Catch: com.google.zxing.FormatException -> L26 com.google.zxing.NotFoundException -> L28
            r4 = r3
            r3 = r11
            r11 = r0
            r0 = r2
            r2 = r3
            goto L3e
        L26:
            r0 = move-exception
            goto L31
        L28:
            r0 = move-exception
            goto L39
        L2a:
            r0 = move-exception
            goto L30
        L2c:
            r0 = move-exception
            goto L38
        L2e:
            r0 = move-exception
            r2 = r11
        L30:
            r3 = r1
        L31:
            r4 = r3
            r3 = r0
            r0 = r2
            r2 = r11
            goto L3e
        L36:
            r0 = move-exception
            r2 = r11
        L38:
            r3 = r1
        L39:
            r4 = r2
            r2 = r0
            r0 = r4
            r4 = r3
            r3 = r11
        L3e:
            if (r11 != 0) goto L60
            r11 = 1
            com.google.zxing.aztec.AztecDetectorResult r10 = r10.detect(r11)     // Catch: java.lang.Throwable -> L57
            com.google.zxing.ResultPoint[] r0 = r10.getPoints()     // Catch: java.lang.Throwable -> L57
            int r4 = r10.getErrorsCorrected()     // Catch: java.lang.Throwable -> L57
            com.google.zxing.aztec.decoder.Decoder r11 = new com.google.zxing.aztec.decoder.Decoder     // Catch: java.lang.Throwable -> L57
            r11.<init>()     // Catch: java.lang.Throwable -> L57
            com.google.zxing.common.DecoderResult r11 = r11.decode(r10)     // Catch: java.lang.Throwable -> L57
            goto L60
        L57:
            r0 = move-exception
            r10 = r0
            if (r2 != 0) goto L5f
            if (r3 == 0) goto L5e
            throw r3
        L5e:
            throw r10
        L5f:
            throw r2
        L60:
            r6 = r0
            r10 = r4
            if (r12 == 0) goto L79
            com.google.zxing.DecodeHintType r0 = com.google.zxing.DecodeHintType.NEED_RESULT_POINT_CALLBACK
            java.lang.Object r12 = r12.get(r0)
            com.google.zxing.ResultPointCallback r12 = (com.google.zxing.ResultPointCallback) r12
            if (r12 == 0) goto L79
            int r0 = r6.length
        L6f:
            if (r1 >= r0) goto L79
            r2 = r6[r1]
            r12.foundPossibleResultPoint(r2)
            int r1 = r1 + 1
            goto L6f
        L79:
            com.google.zxing.Result r2 = new com.google.zxing.Result
            java.lang.String r3 = r11.getText()
            byte[] r4 = r11.getRawBytes()
            int r5 = r11.getNumBits()
            com.google.zxing.BarcodeFormat r7 = com.google.zxing.BarcodeFormat.AZTEC
            long r8 = java.lang.System.currentTimeMillis()
            r2.<init>(r3, r4, r5, r6, r7, r8)
            java.util.List r12 = r11.getByteSegments()
            if (r12 == 0) goto L9b
            com.google.zxing.ResultMetadataType r0 = com.google.zxing.ResultMetadataType.BYTE_SEGMENTS
            r2.putMetadata(r0, r12)
        L9b:
            java.lang.String r12 = r11.getECLevel()
            if (r12 == 0) goto La6
            com.google.zxing.ResultMetadataType r0 = com.google.zxing.ResultMetadataType.ERROR_CORRECTION_LEVEL
            r2.putMetadata(r0, r12)
        La6:
            java.lang.Integer r12 = r11.getErrorsCorrected()
            int r12 = r12.intValue()
            int r10 = r10 + r12
            com.google.zxing.ResultMetadataType r12 = com.google.zxing.ResultMetadataType.ERRORS_CORRECTED
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)
            r2.putMetadata(r12, r10)
            com.google.zxing.ResultMetadataType r10 = com.google.zxing.ResultMetadataType.SYMBOLOGY_IDENTIFIER
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r0 = "]z"
            r12.<init>(r0)
            int r11 = r11.getSymbologyModifier()
            java.lang.StringBuilder r11 = r12.append(r11)
            java.lang.String r11 = r11.toString()
            r2.putMetadata(r10, r11)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.aztec.AztecReader.decode(com.google.zxing.BinaryBitmap, java.util.Map):com.google.zxing.Result");
    }
}
