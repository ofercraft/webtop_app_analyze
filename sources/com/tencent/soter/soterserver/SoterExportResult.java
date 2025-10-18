package com.tencent.soter.soterserver;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class SoterExportResult implements Parcelable {
    public static final Parcelable.Creator<SoterExportResult> CREATOR = new Parcelable.Creator<SoterExportResult>() { // from class: com.tencent.soter.soterserver.SoterExportResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SoterExportResult createFromParcel(Parcel parcel) {
            return new SoterExportResult(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SoterExportResult[] newArray(int i) {
            return new SoterExportResult[i];
        }
    };
    public byte[] exportData;
    public int exportDataLength;
    public int resultCode;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public SoterExportResult() {
    }

    public SoterExportResult(Parcel parcel) {
        this.resultCode = parcel.readInt();
        this.exportData = parcel.createByteArray();
        this.exportDataLength = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.resultCode);
        parcel.writeByteArray(this.exportData);
        parcel.writeInt(this.exportDataLength);
    }
}
