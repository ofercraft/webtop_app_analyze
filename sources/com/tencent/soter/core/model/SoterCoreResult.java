package com.tencent.soter.core.model;

/* loaded from: classes2.dex */
public class SoterCoreResult implements SoterErrCode {
    public int errCode;
    public String errMsg;

    public SoterCoreResult(int i, String str) {
        this(i);
        if (SoterCoreUtil.isNullOrNil(str)) {
            return;
        }
        this.errMsg = str;
    }

    public SoterCoreResult(int i) {
        this.errCode = i;
        if (i == 0) {
            this.errMsg = "ok";
        } else if (i == 2) {
            this.errMsg = "device not support soter";
        } else {
            this.errMsg = "errmsg not specified";
        }
    }

    public boolean isSuccess() {
        return this.errCode == 0;
    }

    public boolean equals(Object obj) {
        return (obj instanceof SoterCoreResult) && ((SoterCoreResult) obj).errCode == this.errCode;
    }

    public int getErrCode() {
        return this.errCode;
    }

    public void setErrCode(int i) {
        this.errCode = i;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String str) {
        this.errMsg = str;
    }

    public String toString() {
        return "SoterCoreResult{errCode=" + this.errCode + ", errMsg='" + this.errMsg + "'}";
    }
}
