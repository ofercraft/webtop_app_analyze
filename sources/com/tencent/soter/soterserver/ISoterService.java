package com.tencent.soter.soterserver;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface ISoterService extends IInterface {

    public static class Default implements ISoterService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public SoterSignResult finishSign(long j) throws RemoteException {
            return null;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public int generateAppSecureKey(int i) throws RemoteException {
            return 0;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public int generateAuthKey(int i, String str) throws RemoteException {
            return 0;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public SoterExportResult getAppSecureKey(int i) throws RemoteException {
            return null;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public SoterExportResult getAuthKey(int i, String str) throws RemoteException {
            return null;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public SoterDeviceResult getDeviceId() throws RemoteException {
            return null;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public SoterExtraParam getExtraParam(String str) throws RemoteException {
            return null;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public int getVersion() throws RemoteException {
            return 0;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public boolean hasAskAlready(int i) throws RemoteException {
            return false;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public boolean hasAuthKey(int i, String str) throws RemoteException {
            return false;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public SoterSessionResult initSigh(int i, String str, String str2) throws RemoteException {
            return null;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public int removeAllAuthKey(int i) throws RemoteException {
            return 0;
        }

        @Override // com.tencent.soter.soterserver.ISoterService
        public int removeAuthKey(int i, String str) throws RemoteException {
            return 0;
        }
    }

    SoterSignResult finishSign(long j) throws RemoteException;

    int generateAppSecureKey(int i) throws RemoteException;

    int generateAuthKey(int i, String str) throws RemoteException;

    SoterExportResult getAppSecureKey(int i) throws RemoteException;

    SoterExportResult getAuthKey(int i, String str) throws RemoteException;

    SoterDeviceResult getDeviceId() throws RemoteException;

    SoterExtraParam getExtraParam(String str) throws RemoteException;

    int getVersion() throws RemoteException;

    boolean hasAskAlready(int i) throws RemoteException;

    boolean hasAuthKey(int i, String str) throws RemoteException;

    SoterSessionResult initSigh(int i, String str, String str2) throws RemoteException;

    int removeAllAuthKey(int i) throws RemoteException;

    int removeAuthKey(int i, String str) throws RemoteException;

    public static abstract class Stub extends Binder implements ISoterService {
        private static final String DESCRIPTOR = "com.tencent.soter.soterserver.ISoterService";
        static final int TRANSACTION_finishSign = 10;
        static final int TRANSACTION_generateAppSecureKey = 1;
        static final int TRANSACTION_generateAuthKey = 4;
        static final int TRANSACTION_getAppSecureKey = 2;
        static final int TRANSACTION_getAuthKey = 6;
        static final int TRANSACTION_getDeviceId = 11;
        static final int TRANSACTION_getExtraParam = 13;
        static final int TRANSACTION_getVersion = 12;
        static final int TRANSACTION_hasAskAlready = 3;
        static final int TRANSACTION_hasAuthKey = 8;
        static final int TRANSACTION_initSigh = 9;
        static final int TRANSACTION_removeAllAuthKey = 7;
        static final int TRANSACTION_removeAuthKey = 5;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISoterService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof ISoterService)) {
                return (ISoterService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    int iGenerateAppSecureKey = generateAppSecureKey(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(iGenerateAppSecureKey);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    SoterExportResult appSecureKey = getAppSecureKey(parcel.readInt());
                    parcel2.writeNoException();
                    if (appSecureKey != null) {
                        parcel2.writeInt(1);
                        appSecureKey.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zHasAskAlready = hasAskAlready(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(zHasAskAlready ? 1 : 0);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    int iGenerateAuthKey = generateAuthKey(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(iGenerateAuthKey);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    int iRemoveAuthKey = removeAuthKey(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(iRemoveAuthKey);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    SoterExportResult authKey = getAuthKey(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    if (authKey != null) {
                        parcel2.writeInt(1);
                        authKey.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    int iRemoveAllAuthKey = removeAllAuthKey(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(iRemoveAllAuthKey);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zHasAuthKey = hasAuthKey(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(zHasAuthKey ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    SoterSessionResult soterSessionResultInitSigh = initSigh(parcel.readInt(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    if (soterSessionResultInitSigh != null) {
                        parcel2.writeInt(1);
                        soterSessionResultInitSigh.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    SoterSignResult soterSignResultFinishSign = finishSign(parcel.readLong());
                    parcel2.writeNoException();
                    if (soterSignResultFinishSign != null) {
                        parcel2.writeInt(1);
                        soterSignResultFinishSign.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    SoterDeviceResult deviceId = getDeviceId();
                    parcel2.writeNoException();
                    if (deviceId != null) {
                        parcel2.writeInt(1);
                        deviceId.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    int version = getVersion();
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    SoterExtraParam extraParam = getExtraParam(parcel.readString());
                    parcel2.writeNoException();
                    if (extraParam != null) {
                        parcel2.writeInt(1);
                        extraParam.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static class Proxy implements ISoterService {
            public static ISoterService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public int generateAppSecureKey(int i) throws RemoteException {
                int iGenerateAppSecureKey;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        iGenerateAppSecureKey = Stub.getDefaultImpl().generateAppSecureKey(i);
                    } else {
                        parcelObtain2.readException();
                        iGenerateAppSecureKey = parcelObtain2.readInt();
                    }
                    return iGenerateAppSecureKey;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public SoterExportResult getAppSecureKey(int i) throws RemoteException {
                SoterExportResult soterExportResultCreateFromParcel;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(2, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        soterExportResultCreateFromParcel = Stub.getDefaultImpl().getAppSecureKey(i);
                    } else {
                        parcelObtain2.readException();
                        soterExportResultCreateFromParcel = parcelObtain2.readInt() != 0 ? SoterExportResult.CREATOR.createFromParcel(parcelObtain2) : null;
                    }
                    return soterExportResultCreateFromParcel;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public boolean hasAskAlready(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(3, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasAskAlready(i);
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public int generateAuthKey(int i, String str) throws RemoteException {
                int iGenerateAuthKey;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(4, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        iGenerateAuthKey = Stub.getDefaultImpl().generateAuthKey(i, str);
                    } else {
                        parcelObtain2.readException();
                        iGenerateAuthKey = parcelObtain2.readInt();
                    }
                    return iGenerateAuthKey;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public int removeAuthKey(int i, String str) throws RemoteException {
                int iRemoveAuthKey;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(5, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        iRemoveAuthKey = Stub.getDefaultImpl().removeAuthKey(i, str);
                    } else {
                        parcelObtain2.readException();
                        iRemoveAuthKey = parcelObtain2.readInt();
                    }
                    return iRemoveAuthKey;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public SoterExportResult getAuthKey(int i, String str) throws RemoteException {
                SoterExportResult soterExportResultCreateFromParcel;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(6, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        soterExportResultCreateFromParcel = Stub.getDefaultImpl().getAuthKey(i, str);
                    } else {
                        parcelObtain2.readException();
                        soterExportResultCreateFromParcel = parcelObtain2.readInt() != 0 ? SoterExportResult.CREATOR.createFromParcel(parcelObtain2) : null;
                    }
                    return soterExportResultCreateFromParcel;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public int removeAllAuthKey(int i) throws RemoteException {
                int iRemoveAllAuthKey;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(7, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        iRemoveAllAuthKey = Stub.getDefaultImpl().removeAllAuthKey(i);
                    } else {
                        parcelObtain2.readException();
                        iRemoveAllAuthKey = parcelObtain2.readInt();
                    }
                    return iRemoveAllAuthKey;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public boolean hasAuthKey(int i, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(8, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasAuthKey(i, str);
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public SoterSessionResult initSigh(int i, String str, String str2) throws RemoteException {
                SoterSessionResult soterSessionResultCreateFromParcel;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    if (!this.mRemote.transact(9, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        soterSessionResultCreateFromParcel = Stub.getDefaultImpl().initSigh(i, str, str2);
                    } else {
                        parcelObtain2.readException();
                        soterSessionResultCreateFromParcel = parcelObtain2.readInt() != 0 ? SoterSessionResult.CREATOR.createFromParcel(parcelObtain2) : null;
                    }
                    return soterSessionResultCreateFromParcel;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public SoterSignResult finishSign(long j) throws RemoteException {
                SoterSignResult soterSignResultCreateFromParcel;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeLong(j);
                    if (!this.mRemote.transact(10, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        soterSignResultCreateFromParcel = Stub.getDefaultImpl().finishSign(j);
                    } else {
                        parcelObtain2.readException();
                        soterSignResultCreateFromParcel = parcelObtain2.readInt() != 0 ? SoterSignResult.CREATOR.createFromParcel(parcelObtain2) : null;
                    }
                    return soterSignResultCreateFromParcel;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public SoterDeviceResult getDeviceId() throws RemoteException {
                SoterDeviceResult soterDeviceResultCreateFromParcel;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(11, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        soterDeviceResultCreateFromParcel = Stub.getDefaultImpl().getDeviceId();
                    } else {
                        parcelObtain2.readException();
                        soterDeviceResultCreateFromParcel = parcelObtain2.readInt() != 0 ? SoterDeviceResult.CREATOR.createFromParcel(parcelObtain2) : null;
                    }
                    return soterDeviceResultCreateFromParcel;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public int getVersion() throws RemoteException {
                int version;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(12, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        version = Stub.getDefaultImpl().getVersion();
                    } else {
                        parcelObtain2.readException();
                        version = parcelObtain2.readInt();
                    }
                    return version;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.tencent.soter.soterserver.ISoterService
            public SoterExtraParam getExtraParam(String str) throws RemoteException {
                SoterExtraParam soterExtraParamCreateFromParcel;
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(13, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        soterExtraParamCreateFromParcel = Stub.getDefaultImpl().getExtraParam(str);
                    } else {
                        parcelObtain2.readException();
                        soterExtraParamCreateFromParcel = parcelObtain2.readInt() != 0 ? SoterExtraParam.CREATOR.createFromParcel(parcelObtain2) : null;
                    }
                    return soterExtraParamCreateFromParcel;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISoterService iSoterService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (iSoterService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iSoterService;
            return true;
        }

        public static ISoterService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
