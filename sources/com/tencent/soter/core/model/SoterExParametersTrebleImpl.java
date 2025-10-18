package com.tencent.soter.core.model;

/* loaded from: classes2.dex */
public class SoterExParametersTrebleImpl implements ISoterExParameters {
    private static int[] fingerprintPosition;
    private static int fingerprintType;

    @Override // com.tencent.soter.core.model.ISoterExParameters
    public Object getParam(String str, Object obj) {
        synchronized (SoterExParametersTrebleImpl.class) {
            if (ISoterExParameters.FINGERPRINT_TYPE.equals(str)) {
                int i = fingerprintType;
                if (i != 0) {
                    obj = Integer.valueOf(i);
                }
                return obj;
            }
            if (!ISoterExParameters.FINGERPRINT_HARDWARE_POSITION.equals(str)) {
                return null;
            }
            int[] iArr = fingerprintPosition;
            if (iArr != null) {
                obj = iArr;
            }
            return obj;
        }
    }

    public static void setParam(String str, Object obj) {
        synchronized (SoterExParametersTrebleImpl.class) {
            if (ISoterExParameters.FINGERPRINT_TYPE.equals(str)) {
                fingerprintType = ((Integer) obj).intValue();
            } else if (ISoterExParameters.FINGERPRINT_HARDWARE_POSITION.equals(str)) {
                fingerprintPosition = (int[]) obj;
            }
        }
    }
}
