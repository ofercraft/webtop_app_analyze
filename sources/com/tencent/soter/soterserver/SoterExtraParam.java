package com.tencent.soter.soterserver;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class SoterExtraParam implements Parcelable {
    public static final Parcelable.Creator<SoterExtraParam> CREATOR = new Parcelable.Creator<SoterExtraParam>() { // from class: com.tencent.soter.soterserver.SoterExtraParam.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SoterExtraParam createFromParcel(Parcel parcel) {
            return new SoterExtraParam(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SoterExtraParam[] newArray(int i) {
            return new SoterExtraParam[i];
        }
    };
    public Object result;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected SoterExtraParam(Parcel parcel) {
        this.result = parcel.readValue(getClass().getClassLoader());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.result);
    }
}
