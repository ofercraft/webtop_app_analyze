package com.smartschool.webtop;

import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

/* loaded from: classes2.dex */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage.getNotification(), remoteMessage.getData());
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onNewToken(String str) {
        getApplicationContext();
        getSharedPreferences("com.smartschool.webtop", 0).edit().putString("token", str).apply();
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> map) {
        Webtop.displayNotification(map.get("title"), map.get("message"), getApplicationContext(), map.get(ImagesContract.URL));
    }
}
