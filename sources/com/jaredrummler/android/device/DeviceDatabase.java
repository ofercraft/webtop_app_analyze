package com.jaredrummler.android.device;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.jaredrummler.android.device.DeviceName;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class DeviceDatabase extends SQLiteOpenHelper {
    private static final String COLUMN_CODENAME = "codename";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_NAME = "name";
    private static final String NAME = "android-devices.db";
    private static final String TABLE_DEVICES = "devices";
    private static final int VERSION = 1;
    private final Context context;
    private final File file;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    public DeviceDatabase(Context context) throws SQLException {
        super(context, NAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.context = context.getApplicationContext();
        File databasePath = context.getDatabasePath(NAME);
        this.file = databasePath;
        if (databasePath.exists()) {
            return;
        }
        create();
    }

    public String query(String str, String str2) throws IOException {
        String[] strArr;
        String str3;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        String[] strArr2 = {COLUMN_NAME};
        if (str != null && str2 != null) {
            strArr = new String[]{str, str2};
            str3 = "codename LIKE ? OR model LIKE ?";
        } else {
            if (str == null) {
                if (str2 != null) {
                    strArr = new String[]{str2};
                    str3 = "model LIKE ?";
                }
                return string;
            }
            strArr = new String[]{str};
            str3 = "codename LIKE ?";
        }
        Cursor cursorQuery = readableDatabase.query(TABLE_DEVICES, strArr2, str3, strArr, null, null, null);
        string = cursorQuery.moveToFirst() ? cursorQuery.getString(cursorQuery.getColumnIndexOrThrow(COLUMN_NAME)) : null;
        close(cursorQuery);
        close(readableDatabase);
        return string;
    }

    public DeviceName.DeviceInfo queryToDevice(String str, String str2) throws IOException {
        String[] strArr;
        String str3;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        String[] strArr2 = {COLUMN_NAME, COLUMN_CODENAME, COLUMN_MODEL};
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            strArr = new String[]{str, str2};
            str3 = "codename LIKE ? OR model LIKE ?";
        } else if (!TextUtils.isEmpty(str)) {
            strArr = new String[]{str};
            str3 = "codename LIKE ?";
        } else {
            if (TextUtils.isEmpty(str2)) {
                strArr = new String[]{str2};
                str3 = "model LIKE ?";
            }
            return deviceInfo;
        }
        Cursor cursorQuery = readableDatabase.query(TABLE_DEVICES, strArr2, str3, strArr, null, null, null);
        deviceInfo = cursorQuery.moveToFirst() ? new DeviceName.DeviceInfo(cursorQuery.getString(cursorQuery.getColumnIndexOrThrow(COLUMN_NAME)), cursorQuery.getString(cursorQuery.getColumnIndexOrThrow(COLUMN_CODENAME)), cursorQuery.getString(cursorQuery.getColumnIndexOrThrow(COLUMN_MODEL))) : null;
        close(cursorQuery);
        close(readableDatabase);
        return deviceInfo;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) throws SQLException {
        if (i2 > i) {
            if (this.context.deleteDatabase(NAME) || this.file.delete() || !this.file.exists()) {
                create();
            }
        }
    }

    private void create() throws SQLException {
        try {
            getReadableDatabase();
            close();
            transferDatabaseAsset();
        } catch (IOException e) {
            throw new SQLException("Error creating android-devices.db database", e);
        }
    }

    private void transferDatabaseAsset() throws IOException {
        InputStream inputStreamOpen = this.context.getAssets().open(NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        byte[] bArr = new byte[2048];
        while (true) {
            int i = inputStreamOpen.read(bArr);
            if (i > 0) {
                fileOutputStream.write(bArr, 0, i);
            } else {
                fileOutputStream.flush();
                close(fileOutputStream);
                close(inputStreamOpen);
                return;
            }
        }
    }

    private void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
