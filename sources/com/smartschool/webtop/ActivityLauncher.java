package com.smartschool.webtop;

import android.content.Intent;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;

/* loaded from: classes2.dex */
public class ActivityLauncher {
    private ActivityResultCallback<ActivityResult> activityResultCallback;
    private final ActivityResultLauncher<Intent> launcher;

    public interface OnActivityResult {
        void onActivityResultCallback(int i, int i2, Intent intent);
    }

    private ActivityLauncher(ActivityResultCaller activityResultCaller, ActivityResultContract<Intent, ActivityResult> activityResultContract, ActivityResultCallback<ActivityResult> activityResultCallback) {
        this.activityResultCallback = activityResultCallback;
        this.launcher = activityResultCaller.registerForActivityResult(activityResultContract, new ActivityResultCallback() { // from class: com.smartschool.webtop.ActivityLauncher$$ExternalSyntheticLambda0
            @Override // androidx.activity.result.ActivityResultCallback
            public final void onActivityResult(Object obj) {
                this.f$0.onActivityResult((ActivityResult) obj);
            }
        });
    }

    public static ActivityLauncher registerActivityForResult(ActivityResultCaller activityResultCaller) {
        return new ActivityLauncher(activityResultCaller, new ActivityResultContracts.StartActivityForResult(), null);
    }

    public void launch(Intent intent, ActivityResultCallback<ActivityResult> activityResultCallback) {
        if (activityResultCallback != null) {
            this.activityResultCallback = activityResultCallback;
        }
        this.launcher.launch(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onActivityResult(ActivityResult activityResult) {
        ActivityResultCallback<ActivityResult> activityResultCallback = this.activityResultCallback;
        if (activityResultCallback != null) {
            activityResultCallback.onActivityResult(activityResult);
        }
    }
}
