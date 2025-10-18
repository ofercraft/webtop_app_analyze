package com.tencent.soter.core.model;

/* loaded from: classes2.dex */
public interface ISoterExParameters {
    public static final String FINGERPRINT_HARDWARE_POSITION = "fingerprint_hardware_position";
    public static final String FINGERPRINT_TYPE = "fingerprint_type";
    public static final int FINGERPRINT_TYPE_NORMAL = 1;
    public static final int FINGERPRINT_TYPE_UNDEFINE = 0;
    public static final int FINGERPRINT_TYPE_UNDER_SCREEN = 2;

    Object getParam(String str, Object obj);
}
