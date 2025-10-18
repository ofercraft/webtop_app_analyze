package com.google.android.gms.common.internal;

import java.util.concurrent.ConcurrentHashMap;

/* compiled from: com.google.android.gms:play-services-basement@@18.3.0 */
@Deprecated
/* loaded from: classes.dex */
public class LibraryVersion {
    private static final GmsLogger zza = new GmsLogger("LibraryVersion", "");
    private static final LibraryVersion zzb = new LibraryVersion();
    private final ConcurrentHashMap zzc = new ConcurrentHashMap();

    protected LibraryVersion() {
    }

    public static LibraryVersion getInstance() {
        return zzb;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x008e  */
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getVersion(java.lang.String r9) throws java.lang.Throwable {
        /*
            r8 = this;
            java.lang.String r0 = "LibraryVersion"
            java.lang.String r1 = "Failed to get app version for libraryName: "
            java.lang.String r2 = "Please provide a valid libraryName"
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r9, r2)
            java.util.concurrent.ConcurrentHashMap r2 = r8.zzc
            boolean r2 = r2.containsKey(r9)
            if (r2 == 0) goto L1a
            java.util.concurrent.ConcurrentHashMap r8 = r8.zzc
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            return r8
        L1a:
            java.util.Properties r2 = new java.util.Properties
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "/%s.properties"
            java.lang.Object[] r5 = new java.lang.Object[]{r9}     // Catch: java.lang.Throwable -> L6f java.io.IOException -> L71
            java.lang.String r4 = java.lang.String.format(r4, r5)     // Catch: java.lang.Throwable -> L6f java.io.IOException -> L71
            java.lang.Class<com.google.android.gms.common.internal.LibraryVersion> r5 = com.google.android.gms.common.internal.LibraryVersion.class
            java.io.InputStream r4 = r5.getResourceAsStream(r4)     // Catch: java.lang.Throwable -> L6f java.io.IOException -> L71
            if (r4 == 0) goto L55
            r2.load(r4)     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            java.lang.String r5 = "version"
            java.lang.String r3 = r2.getProperty(r5, r3)     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            com.google.android.gms.common.internal.GmsLogger r2 = com.google.android.gms.common.internal.LibraryVersion.zza     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            r5.<init>()     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            r5.append(r9)     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            java.lang.String r6 = " version is "
            r5.append(r6)     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            r5.append(r3)     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            r2.v(r0, r5)     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            goto L87
        L55:
            com.google.android.gms.common.internal.GmsLogger r2 = com.google.android.gms.common.internal.LibraryVersion.zza     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            r5.<init>(r1)     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            r5.append(r9)     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            r2.w(r0, r5)     // Catch: java.lang.Throwable -> L67 java.io.IOException -> L6a
            goto L87
        L67:
            r8 = move-exception
            r3 = r4
            goto L9d
        L6a:
            r2 = move-exception
            r7 = r4
            r4 = r3
            r3 = r7
            goto L73
        L6f:
            r8 = move-exception
            goto L9d
        L71:
            r2 = move-exception
            r4 = r3
        L73:
            com.google.android.gms.common.internal.GmsLogger r5 = com.google.android.gms.common.internal.LibraryVersion.zza     // Catch: java.lang.Throwable -> L6f
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6f
            r6.<init>(r1)     // Catch: java.lang.Throwable -> L6f
            r6.append(r9)     // Catch: java.lang.Throwable -> L6f
            java.lang.String r1 = r6.toString()     // Catch: java.lang.Throwable -> L6f
            r5.e(r0, r1, r2)     // Catch: java.lang.Throwable -> L6f
            r7 = r4
            r4 = r3
            r3 = r7
        L87:
            if (r4 == 0) goto L8c
            com.google.android.gms.common.util.IOUtils.closeQuietly(r4)
        L8c:
            if (r3 != 0) goto L97
            com.google.android.gms.common.internal.GmsLogger r1 = com.google.android.gms.common.internal.LibraryVersion.zza
            java.lang.String r2 = ".properties file is dropped during release process. Failure to read app version is expected during Google internal testing where locally-built libraries are used"
            r1.d(r0, r2)
            java.lang.String r3 = "UNKNOWN"
        L97:
            java.util.concurrent.ConcurrentHashMap r8 = r8.zzc
            r8.put(r9, r3)
            return r3
        L9d:
            if (r3 == 0) goto La2
            com.google.android.gms.common.util.IOUtils.closeQuietly(r3)
        La2:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.LibraryVersion.getVersion(java.lang.String):java.lang.String");
    }
}
