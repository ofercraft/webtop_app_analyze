package com.google.firebase.heartbeatinfo;

import android.content.Context;
import androidx.core.graphics.TypefaceCompat$$ExternalSyntheticBackport0;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import com.google.firebase.datastorage.JavaDataStorage;
import com.google.firebase.datastorage.JavaDataStorageKt;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* loaded from: classes2.dex */
class HeartBeatInfoStorage {
    private static final String HEARTBEAT_PREFERENCES_NAME = "FirebaseHeartBeat";
    private static final int HEART_BEAT_COUNT_LIMIT = 30;
    private static final String PREFERENCES_NAME = "FirebaseAppHeartBeat";
    private static HeartBeatInfoStorage instance;
    private final JavaDataStorage firebaseDataStore;
    private static final Preferences.Key<Long> GLOBAL = PreferencesKeys.longKey("fire-global");
    private static final Preferences.Key<Long> HEART_BEAT_COUNT_TAG = PreferencesKeys.longKey("fire-count");
    private static final Preferences.Key<String> LAST_STORED_DATE = PreferencesKeys.stringKey("last-used-date");

    public HeartBeatInfoStorage(Context context, String str) {
        this.firebaseDataStore = new JavaDataStorage(context, HEARTBEAT_PREFERENCES_NAME + str);
    }

    HeartBeatInfoStorage(JavaDataStorage javaDataStorage) {
        this.firebaseDataStore = javaDataStorage;
    }

    int getHeartBeatCount() {
        return ((Long) this.firebaseDataStore.getSync(HEART_BEAT_COUNT_TAG, 0L)).intValue();
    }

    synchronized void deleteAllHeartBeats() {
        this.firebaseDataStore.editSync(new Function1() { // from class: com.google.firebase.heartbeatinfo.HeartBeatInfoStorage$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return this.f$0.m345xe97e9b5a((MutablePreferences) obj);
            }
        });
    }

    /* renamed from: lambda$deleteAllHeartBeats$0$com-google-firebase-heartbeatinfo-HeartBeatInfoStorage, reason: not valid java name */
    /* synthetic */ Unit m345xe97e9b5a(MutablePreferences mutablePreferences) {
        long j = 0;
        for (Map.Entry<Preferences.Key<?>, Object> entry : mutablePreferences.asMap().entrySet()) {
            if (entry.getValue() instanceof Set) {
                Preferences.Key<?> key = entry.getKey();
                Set set = (Set) entry.getValue();
                String formattedDate = getFormattedDate(System.currentTimeMillis());
                if (set.contains(formattedDate)) {
                    mutablePreferences.set(key, TypefaceCompat$$ExternalSyntheticBackport0.m51m(new Object[]{formattedDate}));
                    j++;
                } else {
                    mutablePreferences.remove(key);
                }
            }
        }
        if (j == 0) {
            mutablePreferences.remove(HEART_BEAT_COUNT_TAG);
            return null;
        }
        mutablePreferences.set(HEART_BEAT_COUNT_TAG, Long.valueOf(j));
        return null;
    }

    synchronized List<HeartBeatResult> getAllHeartBeats() {
        ArrayList arrayList;
        arrayList = new ArrayList();
        String formattedDate = getFormattedDate(System.currentTimeMillis());
        for (Map.Entry<Preferences.Key<?>, Object> entry : this.firebaseDataStore.getAllSync().entrySet()) {
            if (entry.getValue() instanceof Set) {
                HashSet hashSet = new HashSet((Set) entry.getValue());
                hashSet.remove(formattedDate);
                if (!hashSet.isEmpty()) {
                    arrayList.add(HeartBeatResult.create(entry.getKey().getName(), new ArrayList(hashSet)));
                }
            }
        }
        updateGlobalHeartBeat(System.currentTimeMillis());
        return arrayList;
    }

    private synchronized Preferences.Key<Set<String>> getStoredUserAgentString(MutablePreferences mutablePreferences, String str) {
        for (Map.Entry<Preferences.Key<?>, Object> entry : mutablePreferences.asMap().entrySet()) {
            if (entry.getValue() instanceof Set) {
                Iterator it = ((Set) entry.getValue()).iterator();
                while (it.hasNext()) {
                    if (str.equals((String) it.next())) {
                        return PreferencesKeys.stringSetKey(entry.getKey().getName());
                    }
                }
            }
        }
        return null;
    }

    private synchronized void updateStoredUserAgent(MutablePreferences mutablePreferences, Preferences.Key<Set<String>> key, String str) {
        removeStoredDate(mutablePreferences, str);
        HashSet hashSet = new HashSet((Collection) JavaDataStorageKt.getOrDefault(mutablePreferences, key, new HashSet()));
        hashSet.add(str);
        mutablePreferences.set(key, hashSet);
    }

    private synchronized void removeStoredDate(MutablePreferences mutablePreferences, String str) {
        Preferences.Key<Set<String>> storedUserAgentString = getStoredUserAgentString(mutablePreferences, str);
        if (storedUserAgentString == null) {
            return;
        }
        HashSet hashSet = new HashSet((Collection) JavaDataStorageKt.getOrDefault(mutablePreferences, storedUserAgentString, new HashSet()));
        hashSet.remove(str);
        if (hashSet.isEmpty()) {
            mutablePreferences.remove(storedUserAgentString);
        } else {
            mutablePreferences.set(storedUserAgentString, hashSet);
        }
    }

    synchronized void postHeartBeatCleanUp() {
        final String formattedDate = getFormattedDate(System.currentTimeMillis());
        this.firebaseDataStore.editSync(new Function1() { // from class: com.google.firebase.heartbeatinfo.HeartBeatInfoStorage$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return this.f$0.m346x44e9f764(formattedDate, (MutablePreferences) obj);
            }
        });
    }

    /* renamed from: lambda$postHeartBeatCleanUp$1$com-google-firebase-heartbeatinfo-HeartBeatInfoStorage, reason: not valid java name */
    /* synthetic */ Unit m346x44e9f764(String str, MutablePreferences mutablePreferences) {
        mutablePreferences.set(LAST_STORED_DATE, str);
        removeStoredDate(mutablePreferences, str);
        return null;
    }

    private synchronized String getFormattedDate(long j) {
        return new Date(j).toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    synchronized void storeHeartBeat(long j, final String str) {
        final String formattedDate = getFormattedDate(j);
        final Preferences.Key<Set<String>> keyStringSetKey = PreferencesKeys.stringSetKey(str);
        this.firebaseDataStore.editSync(new Function1() { // from class: com.google.firebase.heartbeatinfo.HeartBeatInfoStorage$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return this.f$0.m347x76ccc570(formattedDate, str, keyStringSetKey, (MutablePreferences) obj);
            }
        });
    }

    /* renamed from: lambda$storeHeartBeat$2$com-google-firebase-heartbeatinfo-HeartBeatInfoStorage, reason: not valid java name */
    /* synthetic */ Unit m347x76ccc570(String str, String str2, Preferences.Key key, MutablePreferences mutablePreferences) {
        Preferences.Key<String> key2 = LAST_STORED_DATE;
        if (((String) JavaDataStorageKt.getOrDefault(mutablePreferences, key2, "")).equals(str)) {
            Preferences.Key<Set<String>> storedUserAgentString = getStoredUserAgentString(mutablePreferences, str);
            if (storedUserAgentString == null || storedUserAgentString.getName().equals(str2)) {
                return null;
            }
            updateStoredUserAgent(mutablePreferences, key, str);
            return null;
        }
        Preferences.Key<Long> key3 = HEART_BEAT_COUNT_TAG;
        long jLongValue = ((Long) JavaDataStorageKt.getOrDefault(mutablePreferences, key3, 0L)).longValue();
        if (jLongValue + 1 == 30) {
            jLongValue = cleanUpStoredHeartBeats(mutablePreferences);
        }
        HashSet hashSet = new HashSet((Collection) JavaDataStorageKt.getOrDefault(mutablePreferences, key, new HashSet()));
        hashSet.add(str);
        mutablePreferences.set(key, hashSet);
        mutablePreferences.set(key3, Long.valueOf(jLongValue + 1));
        mutablePreferences.set(key2, str);
        return null;
    }

    private synchronized long cleanUpStoredHeartBeats(MutablePreferences mutablePreferences) {
        long j;
        long jLongValue = ((Long) JavaDataStorageKt.getOrDefault(mutablePreferences, HEART_BEAT_COUNT_TAG, 0L)).longValue();
        String name = "";
        Set hashSet = new HashSet();
        String str = null;
        for (Map.Entry<Preferences.Key<?>, Object> entry : mutablePreferences.asMap().entrySet()) {
            if (entry.getValue() instanceof Set) {
                Set<String> set = (Set) entry.getValue();
                for (String str2 : set) {
                    if (str == null || str.compareTo(str2) > 0) {
                        name = entry.getKey().getName();
                        hashSet = set;
                        str = str2;
                    }
                }
            }
        }
        HashSet hashSet2 = new HashSet(hashSet);
        hashSet2.remove(str);
        mutablePreferences.set(PreferencesKeys.stringSetKey(name), hashSet2);
        j = jLongValue - 1;
        mutablePreferences.set(HEART_BEAT_COUNT_TAG, Long.valueOf(j));
        return j;
    }

    synchronized long getLastGlobalHeartBeat() {
        return ((Long) this.firebaseDataStore.getSync(GLOBAL, -1L)).longValue();
    }

    synchronized void updateGlobalHeartBeat(final long j) {
        this.firebaseDataStore.editSync(new Function1() { // from class: com.google.firebase.heartbeatinfo.HeartBeatInfoStorage$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return HeartBeatInfoStorage.lambda$updateGlobalHeartBeat$3(j, (MutablePreferences) obj);
            }
        });
    }

    static /* synthetic */ Unit lambda$updateGlobalHeartBeat$3(long j, MutablePreferences mutablePreferences) {
        mutablePreferences.set(GLOBAL, Long.valueOf(j));
        return null;
    }

    synchronized boolean isSameDateUtc(long j, long j2) {
        return getFormattedDate(j).equals(getFormattedDate(j2));
    }

    synchronized boolean shouldSendSdkHeartBeat(Preferences.Key<Long> key, long j) {
        if (isSameDateUtc(((Long) this.firebaseDataStore.getSync(key, -1L)).longValue(), j)) {
            return false;
        }
        this.firebaseDataStore.putSync(key, Long.valueOf(j));
        return true;
    }

    synchronized boolean shouldSendGlobalHeartBeat(long j) {
        return shouldSendSdkHeartBeat(GLOBAL, j);
    }
}
