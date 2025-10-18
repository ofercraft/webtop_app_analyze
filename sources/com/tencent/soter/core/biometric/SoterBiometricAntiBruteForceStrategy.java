package com.tencent.soter.core.biometric;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.tencent.soter.core.model.SLogger;

/* loaded from: classes2.dex */
class SoterBiometricAntiBruteForceStrategy {
    private static final long DEFAULT_FREEZE_TIME = -1;
    private static final int FREEZE_SECOND = 30;
    private static final String KEY_FAIL_TIMES = "key_fail_times";
    private static final String KEY_LAST_FREEZE_TIME = "key_last_freeze_time";
    private static final int MAX_FAIL_NUM = 5;
    private static final String TAG = "Soter.SoterBiometricAntiBruteForceStrategy";

    static boolean isSystemHasAntiBruteForce() {
        return true;
    }

    SoterBiometricAntiBruteForceStrategy() {
    }

    private static int getCurrentFailTime(Context context) {
        Integer numValueOf = Integer.valueOf(getCurrentFailTimeInDB(context));
        SLogger.i(TAG, "soter: current retry time: " + numValueOf, new Object[0]);
        return numValueOf.intValue();
    }

    private static void setCurrentFailTime(Context context, int i) {
        SLogger.i(TAG, "soter: setting to time: " + i, new Object[0]);
        if (i < 0) {
            SLogger.w(TAG, "soter: illegal fail time", new Object[0]);
        } else {
            setCurrentFailTimeInDB(context, i);
        }
    }

    private static long getLastFreezeTime(Context context) {
        Long lValueOf = Long.valueOf(getLastFreezeTimeInDB(context));
        SLogger.i(TAG, "soter: current last freeze time: " + lValueOf, new Object[0]);
        return lValueOf.longValue();
    }

    private static void setLastFreezeTime(Context context, long j) {
        SLogger.i(TAG, "soter: setting last freeze time: " + j, new Object[0]);
        if (j < -1) {
            SLogger.w(TAG, "soter: illegal setLastFreezeTime", new Object[0]);
        } else {
            setLastFreezeTimeInDB(context, j);
        }
    }

    static void freeze(Context context) {
        setCurrentFailTime(context, 6);
        setLastFreezeTime(context, System.currentTimeMillis());
    }

    static void unFreeze(Context context) {
        setLastFreezeTime(context, -1L);
        setCurrentFailTime(context, 0);
    }

    static void addFailTime(Context context) {
        setCurrentFailTime(context, Integer.valueOf(Integer.valueOf(getCurrentFailTime(context)).intValue() + 1).intValue());
    }

    static boolean isCurrentTweenTimeAvailable(Context context) {
        int iCurrentTimeMillis = (int) ((System.currentTimeMillis() - getLastFreezeTime(context)) / 1000);
        SLogger.i(TAG, "soter: tween sec after last freeze: " + iCurrentTimeMillis, new Object[0]);
        if (iCurrentTimeMillis <= 30) {
            return false;
        }
        SLogger.d(TAG, "soter: after last freeze", new Object[0]);
        return true;
    }

    static boolean isCurrentFailTimeAvailable(Context context) {
        if (getCurrentFailTime(context) >= 5) {
            return false;
        }
        SLogger.i(TAG, "soter: fail time available", new Object[0]);
        return true;
    }

    private static void setCurrentFailTimeInDB(Context context, int i) {
        if (context == null) {
            SLogger.e(TAG, "soter: context is null", new Object[0]);
            return;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putInt(KEY_FAIL_TIMES, i);
        editorEdit.apply();
    }

    private static int getCurrentFailTimeInDB(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_FAIL_TIMES, 0);
    }

    private static long getLastFreezeTimeInDB(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(KEY_LAST_FREEZE_TIME, -1L);
    }

    private static void setLastFreezeTimeInDB(Context context, long j) {
        if (context == null) {
            SLogger.e(TAG, "soter: context is null", new Object[0]);
            return;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putLong(KEY_LAST_FREEZE_TIME, j);
        editorEdit.apply();
    }
}
