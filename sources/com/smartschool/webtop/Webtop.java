package com.smartschool.webtop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.MimeTypeMap;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.net.MailTo;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import com.google.android.gms.common.internal.ImagesContract;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.smartschool.webtop.ActivityLauncher;
import dev.skomlach.biometric.compat.AuthenticationResult;
import dev.skomlach.biometric.compat.BiometricApi;
import dev.skomlach.biometric.compat.BiometricAuthException;
import dev.skomlach.biometric.compat.BiometricAuthRequest;
import dev.skomlach.biometric.compat.BiometricConfirmation;
import dev.skomlach.biometric.compat.BiometricManagerCompat;
import dev.skomlach.biometric.compat.BiometricPromptCompat;
import dev.skomlach.biometric.compat.BiometricType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class Webtop extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    protected String urlToLoad;
    protected final ActivityLauncher activityLauncher = ActivityLauncher.registerActivityForResult(this);
    protected Uri uri = null;
    protected WebView clientWebView = null;
    protected ValueCallback<Uri[]> mUploadMessage = null;

    /* JADX WARN: Removed duplicated region for block: B:8:0x000f A[Catch: Exception -> 0x00b8, TryCatch #0 {Exception -> 0x00b8, blocks: (B:6:0x0009, B:9:0x0016, B:8:0x000f), top: B:12:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected static android.app.Notification displayNotification(java.lang.String r6, java.lang.String r7, android.content.Context r8, java.lang.String r9) {
        /*
            java.lang.String r0 = "my_channel_01"
            r1 = 0
            if (r6 == 0) goto Lb8
            if (r7 == 0) goto Lb8
            if (r9 == 0) goto Lf
            boolean r2 = r9.isEmpty()     // Catch: java.lang.Exception -> Lb8
            if (r2 == 0) goto L16
        Lf:
            r9 = r8
            com.smartschool.webtop.Webtop r9 = (com.smartschool.webtop.Webtop) r9     // Catch: java.lang.Exception -> Lb8
            java.lang.String r9 = r9.getBaseURL()     // Catch: java.lang.Exception -> Lb8
        L16:
            android.content.Intent r2 = new android.content.Intent     // Catch: java.lang.Exception -> Lb8
            java.lang.Class<com.smartschool.webtop.Webtop> r3 = com.smartschool.webtop.Webtop.class
            r2.<init>(r8, r3)     // Catch: java.lang.Exception -> Lb8
            java.lang.String r3 = "loadUrlFromPush"
            r2.setAction(r3)     // Catch: java.lang.Exception -> Lb8
            r3 = 536870912(0x20000000, float:1.0842022E-19)
            r2.setFlags(r3)     // Catch: java.lang.Exception -> Lb8
            android.net.Uri$Builder r3 = new android.net.Uri$Builder     // Catch: java.lang.Exception -> Lb8
            r3.<init>()     // Catch: java.lang.Exception -> Lb8
            java.lang.String r4 = "data"
            android.net.Uri$Builder r3 = r3.scheme(r4)     // Catch: java.lang.Exception -> Lb8
            java.lang.String r4 = "url"
            android.net.Uri$Builder r9 = r3.appendQueryParameter(r4, r9)     // Catch: java.lang.Exception -> Lb8
            android.net.Uri r9 = r9.build()     // Catch: java.lang.Exception -> Lb8
            r2.setData(r9)     // Catch: java.lang.Exception -> Lb8
            int r9 = com.smartschool.webtop.CommonUtilities.notificationID     // Catch: java.lang.Exception -> Lb8
            r3 = 335544320(0x14000000, float:6.4623485E-27)
            android.app.PendingIntent r9 = android.app.PendingIntent.getActivity(r8, r9, r2, r3)     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$Builder r2 = new androidx.core.app.NotificationCompat$Builder     // Catch: java.lang.Exception -> Lb8
            r2.<init>(r8, r0)     // Catch: java.lang.Exception -> Lb8
            r3 = 1
            androidx.core.app.NotificationCompat$Builder r4 = r2.setAutoCancel(r3)     // Catch: java.lang.Exception -> Lb8
            r5 = -1
            androidx.core.app.NotificationCompat$Builder r4 = r4.setDefaults(r5)     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$Builder r3 = r4.setColorized(r3)     // Catch: java.lang.Exception -> Lb8
            int r4 = com.smartschool.webtop.R.color.turquoise     // Catch: java.lang.Exception -> Lb8
            int r4 = androidx.core.content.ContextCompat.getColor(r8, r4)     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$Builder r3 = r3.setColor(r4)     // Catch: java.lang.Exception -> Lb8
            long r4 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$Builder r3 = r3.setWhen(r4)     // Catch: java.lang.Exception -> Lb8
            int r4 = com.smartschool.webtop.R.drawable.ic_notification_foreground     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$Builder r3 = r3.setSmallIcon(r4)     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$Builder r3 = r3.setTicker(r6)     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$Builder r6 = r3.setContentTitle(r6)     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$Builder r6 = r6.setContentText(r7)     // Catch: java.lang.Exception -> Lb8
            r3 = 2
            androidx.core.app.NotificationCompat$Builder r6 = r6.setPriority(r3)     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$BigTextStyle r3 = new androidx.core.app.NotificationCompat$BigTextStyle     // Catch: java.lang.Exception -> Lb8
            r3.<init>()     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$BigTextStyle r7 = r3.bigText(r7)     // Catch: java.lang.Exception -> Lb8
            androidx.core.app.NotificationCompat$Builder r6 = r6.setStyle(r7)     // Catch: java.lang.Exception -> Lb8
            r7 = 5
            androidx.core.app.NotificationCompat$Builder r6 = r6.setDefaults(r7)     // Catch: java.lang.Exception -> Lb8
            r6.setContentIntent(r9)     // Catch: java.lang.Exception -> Lb8
            java.lang.String r6 = "notification"
            java.lang.Object r6 = r8.getSystemService(r6)     // Catch: java.lang.Exception -> Lb8
            android.app.NotificationManager r6 = (android.app.NotificationManager) r6     // Catch: java.lang.Exception -> Lb8
            android.app.NotificationChannel r7 = new android.app.NotificationChannel     // Catch: java.lang.Exception -> Lb8
            java.lang.String r8 = "כללי"
            r9 = 3
            r7.<init>(r0, r8, r9)     // Catch: java.lang.Exception -> Lb8
            r6.createNotificationChannel(r7)     // Catch: java.lang.Exception -> Lb8
            android.app.Notification r1 = r2.build()     // Catch: java.lang.Exception -> Lb8
            int r7 = com.smartschool.webtop.CommonUtilities.notificationID     // Catch: java.lang.Exception -> Lb8
            int r8 = r7 + 1
            com.smartschool.webtop.CommonUtilities.notificationID = r8     // Catch: java.lang.Exception -> Lb8
            r6.notify(r7, r1)     // Catch: java.lang.Exception -> Lb8
        Lb8:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.smartschool.webtop.Webtop.displayNotification(java.lang.String, java.lang.String, android.content.Context, java.lang.String):android.app.Notification");
    }

    public static List<String> getPermissionsRequest(Context context, String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            if (str != null && ContextCompat.checkSelfPermission(context, str) != 0) {
                arrayList.add(strArr[i]);
            }
        }
        return arrayList;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 1 && iArr.length > 0) {
            int i2 = iArr[0];
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 31) {
            SplashScreen.installSplashScreen(this);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(bundle);
        setContentView(R.layout.main);
        if (r3.widthPixels / getResources().getDisplayMetrics().density >= 840.0f) {
            setRequestedOrientation(10);
        } else {
            setRequestedOrientation(1);
        }
        handleIntent(getIntent());
        initUI();
        Window window = getWindow();
        window.clearFlags(AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL);
        window.addFlags(Integer.MIN_VALUE);
        requestAppPermissions();
    }

    private void requestAppPermissions() {
        String[] strArr = new String[7];
        strArr[0] = "android.permission.CAMERA";
        strArr[1] = "android.permission.READ_EXTERNAL_STORAGE";
        strArr[2] = "android.permission.WRITE_EXTERNAL_STORAGE";
        strArr[3] = "android.permission.FOREGROUND_SERVICE";
        strArr[4] = Build.VERSION.SDK_INT >= 33 ? "android.permission.POST_NOTIFICATIONS" : null;
        strArr[5] = Build.VERSION.SDK_INT >= 30 ? "android.permission.USE_BIOMETRIC" : null;
        strArr[6] = "android.permission.READ_PHONE_STATE";
        List<String> permissionsRequest = getPermissionsRequest(this, strArr);
        if (permissionsRequest.isEmpty()) {
            return;
        }
        ActivityCompat.requestPermissions(this, (String[]) permissionsRequest.toArray(new String[0]), 1);
    }

    protected void handleIntent(Intent intent) {
        try {
            if (intent.getAction() != null && intent.getAction().equals("loadUrlFromPush")) {
                this.urlToLoad = URLDecoder.decode(intent.getData().getQueryParameter(ImagesContract.URL), "utf-8");
            } else if (intent.getExtras() == null) {
                return;
            } else {
                this.urlToLoad = URLDecoder.decode(intent.getExtras().getString(ImagesContract.URL), "utf-8");
            }
            String str = this.urlToLoad;
            if (str == null || str.isEmpty()) {
                return;
            }
            String userToken = getUserToken(null);
            if (!userToken.isEmpty()) {
                if (this.urlToLoad.indexOf("?") > 0) {
                    this.urlToLoad += "&token=" + userToken;
                } else {
                    this.urlToLoad += "?token=" + userToken;
                }
            }
            WebView webView = this.clientWebView;
            if (webView != null) {
                webView.loadUrl(this.urlToLoad);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), 1);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        intent.addFlags(32);
        super.onNewIntent(intent);
        this.urlToLoad = null;
        handleIntent(intent);
    }

    public String getBaseURL() {
        String userToken = getUserToken(null);
        if (!userToken.isEmpty()) {
            return "https://webtop.smartschool.co.il/account/login?token=" + userToken;
        }
        return "https://webtop.smartschool.co.il/account/login";
    }

    private String getUserToken(String str) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.smartschool.webtop", 0);
        if (str == null) {
            str = "auth";
        }
        String string = sharedPreferences.getString(str, "");
        return (true ^ string.isEmpty()) & (string != null) ? string : "";
    }

    private void clearNotifications() {
        ((NotificationManager) getSystemService("notification")).cancelAll();
    }

    private String getCookie(String str, String str2) {
        for (String str3 : CookieManager.getInstance().getCookie(str).split(";")) {
            if (str3.contains(str2)) {
                return str3.split("=")[1];
            }
        }
        return null;
    }

    public void startActivityForResultCompat(Intent intent, final int i, final ActivityLauncher.OnActivityResult onActivityResult) {
        this.activityLauncher.launch(intent, new ActivityResultCallback() { // from class: com.smartschool.webtop.Webtop$$ExternalSyntheticLambda1
            @Override // androidx.activity.result.ActivityResultCallback
            public final void onActivityResult(Object obj) {
                ActivityResult activityResult = (ActivityResult) obj;
                onActivityResult.onActivityResultCallback(i, activityResult.getResultCode(), activityResult.getData());
            }
        });
    }

    protected void initUI() {
        if (this.clientWebView == null) {
            WebView webView = (WebView) findViewById(R.id.WebView);
            this.clientWebView = webView;
            webView.setLayerType(2, null);
            this.clientWebView.setBackgroundColor(-1);
            this.clientWebView.setScrollBarStyle(0);
            this.clientWebView.setScrollbarFadingEnabled(true);
            this.clientWebView.setDownloadListener(new DownloadListener() { // from class: com.smartschool.webtop.Webtop$$ExternalSyntheticLambda0
                @Override // android.webkit.DownloadListener
                public final void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                    this.f$0.m386lambda$initUI$1$comsmartschoolwebtopWebtop(str, str2, str3, str4, j);
                }
            });
            this.clientWebView.setWebChromeClient(new AnonymousClass1());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(this.clientWebView, true);
            this.clientWebView.setInitialScale(1);
            WebView.setWebContentsDebuggingEnabled(true);
            WebSettings settings = this.clientWebView.getSettings();
            settings.setUserAgentString(settings.getUserAgentString() + " Webtop/ (Build -1)");
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setLoadsImagesAutomatically(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setSupportMultipleWindows(true);
            settings.setDatabaseEnabled(true);
            settings.setSupportZoom(false);
            settings.setTextZoom(100);
            settings.setBuiltInZoomControls(false);
            settings.setDisplayZoomControls(false);
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setMediaPlaybackRequiresUserGesture(false);
            settings.setAllowContentAccess(true);
            settings.getAllowFileAccess();
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setCacheMode(-1);
            this.clientWebView.addJavascriptInterface(new WebAppInterface(this), "MobileDevice");
            this.clientWebView.setWebViewClient(new AnonymousClass2(ProgressDialog.progressDialog(this)));
        }
        WebView webView2 = this.clientWebView;
        String baseURL = this.urlToLoad;
        if (baseURL == null) {
            baseURL = getBaseURL();
        }
        webView2.loadUrl(baseURL);
    }

    /* renamed from: lambda$initUI$1$com-smartschool-webtop-Webtop, reason: not valid java name */
    /* synthetic */ void m386lambda$initUI$1$comsmartschoolwebtopWebtop(String str, String str2, String str3, String str4, long j) {
        try {
            this.clientWebView.loadUrl(new WebAppInterface(this).getBase64StringFromBlobUrl(str, getCookie("https://webtop.smartschool.co.il/account/login", "fileName"), str4));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
        }
    }

    /* renamed from: com.smartschool.webtop.Webtop$1, reason: invalid class name */
    class AnonymousClass1 extends WebChromeClient {
        AnonymousClass1() {
        }

        @Override // android.webkit.WebChromeClient
        public void onPermissionRequest(PermissionRequest permissionRequest) {
            permissionRequest.grant(permissionRequest.getResources());
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
            return super.onJsAlert(webView, str, str2, jsResult);
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
            return super.onJsPrompt(webView, str, str2, str3, jsPromptResult);
        }

        @Override // android.webkit.WebChromeClient
        public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
            WebView webView2 = new WebView(webView.getContext());
            webView2.getSettings().setJavaScriptEnabled(true);
            final Dialog dialog = new Dialog(webView.getContext());
            dialog.setContentView(webView2);
            webView2.setWebChromeClient(new WebChromeClient() { // from class: com.smartschool.webtop.Webtop.1.1
                @Override // android.webkit.WebChromeClient
                public void onCloseWindow(WebView webView3) {
                    dialog.dismiss();
                }
            });
            ((WebView.WebViewTransport) message.obj).setWebView(webView2);
            message.sendToTarget();
            return true;
        }

        @Override // android.webkit.WebChromeClient
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            Webtop.this.mUploadMessage = valueCallback;
            Webtop.this.uri = null;
            final Intent[] intentArr = {fileChooserParams.createIntent()};
            AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext(), R.style.MyDialogTheme);
            builder.setTitle("כיצד תרצה לבחור קובץ?").setMessage("אנא בחר/י כיצד לבחור את הקובץ הרצוי.");
            builder.setPositiveButton("בחירה ממאגר", new DialogInterface.OnClickListener() { // from class: com.smartschool.webtop.Webtop$1$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    this.f$0.m388lambda$onShowFileChooser$1$comsmartschoolwebtopWebtop$1(intentArr, dialogInterface, i);
                }
            });
            builder.setNeutralButton("צילום תמונה", new DialogInterface.OnClickListener() { // from class: com.smartschool.webtop.Webtop$1$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    this.f$0.m390lambda$onShowFileChooser$3$comsmartschoolwebtopWebtop$1(intentArr, dialogInterface, i);
                }
            });
            builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() { // from class: com.smartschool.webtop.Webtop$1$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
            return true;
        }

        /* renamed from: lambda$onShowFileChooser$1$com-smartschool-webtop-Webtop$1, reason: not valid java name */
        /* synthetic */ void m388lambda$onShowFileChooser$1$comsmartschoolwebtopWebtop$1(Intent[] intentArr, DialogInterface dialogInterface, int i) {
            try {
                Webtop.this.activityLauncher.launch(intentArr[0], new ActivityResultCallback() { // from class: com.smartschool.webtop.Webtop$1$$ExternalSyntheticLambda4
                    @Override // androidx.activity.result.ActivityResultCallback
                    public final void onActivityResult(Object obj) {
                        this.f$0.m387lambda$onShowFileChooser$0$comsmartschoolwebtopWebtop$1((ActivityResult) obj);
                    }
                });
            } catch (Exception unused) {
            }
        }

        /* renamed from: lambda$onShowFileChooser$0$com-smartschool-webtop-Webtop$1, reason: not valid java name */
        /* synthetic */ void m387lambda$onShowFileChooser$0$comsmartschoolwebtopWebtop$1(ActivityResult activityResult) {
            if (Webtop.this.mUploadMessage == null || activityResult.getData() == null) {
                return;
            }
            Webtop.this.uri = Uri.parse("https://webtop.smartschool.co.il/account/login");
            Webtop.this.mUploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(activityResult.getResultCode(), activityResult.getData()));
            Webtop.this.mUploadMessage = null;
        }

        /* renamed from: lambda$onShowFileChooser$3$com-smartschool-webtop-Webtop$1, reason: not valid java name */
        /* synthetic */ void m390lambda$onShowFileChooser$3$comsmartschoolwebtopWebtop$1(Intent[] intentArr, DialogInterface dialogInterface, int i) {
            intentArr[0] = new Intent("android.media.action.IMAGE_CAPTURE");
            try {
                String str = "WEBTOP_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                ContentResolver contentResolver = Webtop.this.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put("_display_name", str);
                contentValues.put("mime_type", "image/jpg");
                contentValues.put("relative_path", Environment.DIRECTORY_PICTURES);
                Webtop.this.uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intentArr[0].putExtra("output", Webtop.this.uri);
                Webtop.this.activityLauncher.launch(intentArr[0], new ActivityResultCallback() { // from class: com.smartschool.webtop.Webtop$1$$ExternalSyntheticLambda3
                    @Override // androidx.activity.result.ActivityResultCallback
                    public final void onActivityResult(Object obj) {
                        this.f$0.m389lambda$onShowFileChooser$2$comsmartschoolwebtopWebtop$1((ActivityResult) obj);
                    }
                });
            } catch (Exception unused) {
            }
        }

        /* renamed from: lambda$onShowFileChooser$2$com-smartschool-webtop-Webtop$1, reason: not valid java name */
        /* synthetic */ void m389lambda$onShowFileChooser$2$comsmartschoolwebtopWebtop$1(ActivityResult activityResult) {
            try {
                if (Webtop.this.mUploadMessage == null) {
                    return;
                }
                if (activityResult.getResultCode() != -1) {
                    Webtop.this.uri = null;
                }
                Webtop.this.mUploadMessage.onReceiveValue(Webtop.this.uri != null ? new Uri[]{Webtop.this.uri} : null);
                Webtop.this.mUploadMessage = null;
            } catch (Exception unused) {
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
        }
    }

    /* renamed from: com.smartschool.webtop.Webtop$2, reason: invalid class name */
    class AnonymousClass2 extends WebViewClient {
        private boolean loaded = false;
        final /* synthetic */ Dialog val$progressBar;

        AnonymousClass2(Dialog dialog) {
            this.val$progressBar = dialog;
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
            if (this.val$progressBar.isShowing()) {
                this.val$progressBar.dismiss();
            }
            if (Webtop.this.getSharedPreferences("com.smartschool.webtop", 0).getString("sslSkip", "") != null) {
                sslErrorHandler.proceed();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(Webtop.this);
            builder.setMessage(R.string.notification_error_ssl_cert_invalid + "\n" + sslError.toString());
            builder.setPositiveButton("המשך", new DialogInterface.OnClickListener() { // from class: com.smartschool.webtop.Webtop$2$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    this.f$0.m391lambda$onReceivedSslError$0$comsmartschoolwebtopWebtop$2(sslErrorHandler, dialogInterface, i);
                }
            });
            builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() { // from class: com.smartschool.webtop.Webtop$2$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    sslErrorHandler.cancel();
                }
            });
            builder.create().show();
        }

        /* renamed from: lambda$onReceivedSslError$0$com-smartschool-webtop-Webtop$2, reason: not valid java name */
        /* synthetic */ void m391lambda$onReceivedSslError$0$comsmartschoolwebtopWebtop$2(SslErrorHandler sslErrorHandler, DialogInterface dialogInterface, int i) {
            Webtop.this.getSharedPreferences("com.smartschool.webtop", 0).edit().putString("sslSkip", "1").apply();
            sslErrorHandler.proceed();
        }

        private boolean isValidURL(String str) {
            return str.startsWith("http");
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
        }

        @Override // android.webkit.WebViewClient
        public void onFormResubmission(WebView webView, Message message, Message message2) {
            if (webView.canGoBack()) {
                webView.goBack();
            }
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            if (this.val$progressBar.isShowing()) {
                this.val$progressBar.dismiss();
            }
            if (!this.loaded) {
                if (str.startsWith("file")) {
                    return;
                }
                this.loaded = true;
                webView.clearHistory();
            }
            Webtop.this.setRegistrationID();
            CookieManager.getInstance().flush();
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            onReceivedError(webView, webResourceError.getErrorCode(), webResourceError.getDescription().toString(), webResourceRequest.getUrl().toString());
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            String string = webResourceRequest.getUrl().toString();
            if (string.startsWith("blob:")) {
                webView.loadUrl(string.substring(5));
                return true;
            }
            if (string.startsWith(MailTo.MAILTO_SCHEME)) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.EMAIL", new String[]{string.substring(7)});
                intent.putExtra("android.intent.extra.TEXT", "");
                intent.putExtra("android.intent.extra.SUBJECT", "");
                intent.setType("message/rfc822");
                Webtop.this.startActivity(intent);
                return true;
            }
            if (string.startsWith("tel:")) {
                Webtop.this.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(string)));
                return true;
            }
            if (string.endsWith(".mp4")) {
                Uri uri = Uri.parse(string);
                Intent intent2 = new Intent("android.intent.action.VIEW");
                intent2.setDataAndType(uri, "video/mp4");
                Webtop.this.startActivity(intent2);
                return true;
            }
            if (string.endsWith(".3gp")) {
                Uri uri2 = Uri.parse(string);
                Intent intent3 = new Intent("android.intent.action.VIEW");
                intent3.setDataAndType(uri2, "video/3gp");
                Webtop.this.startActivity(intent3);
                return true;
            }
            if (string.endsWith(".mp3")) {
                Uri uri3 = Uri.parse(string);
                Intent intent4 = new Intent("android.intent.action.VIEW");
                intent4.setDataAndType(uri3, "audio/mp3");
                Webtop.this.startActivity(intent4);
                return true;
            }
            if (!string.contains("webtop.co.il/DOWNLOAD")) {
                return false;
            }
            String strSubstring = string.substring(string.lastIndexOf("~"));
            try {
                strSubstring = new String(Base64.decode(strSubstring, 0), "UTF-8");
            } catch (Exception unused) {
            }
            Toast.makeText(Webtop.this.getApplicationContext(), "מוריד את הקובץ: " + strSubstring, 1).show();
            Webtop webtop = Webtop.this;
            webtop.downloadFile(webtop, string, strSubstring);
            return true;
        }
    }

    public void downloadFile(Activity activity, String str, String str2) {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestAppPermissions();
        }
        try {
            if (Build.VERSION.SDK_INT >= 33) {
                activity.registerReceiver(new BroadcastReceiver() { // from class: com.smartschool.webtop.Webtop.3
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        if ("android.intent.action.DOWNLOAD_COMPLETE".equals(intent.getAction())) {
                            Webtop.this.openDownloadedAttachment(context, intent.getLongExtra("extra_download_id", 0L));
                        }
                    }
                }, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"), 4);
            } else {
                activity.registerReceiver(new BroadcastReceiver() { // from class: com.smartschool.webtop.Webtop.4
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        if ("android.intent.action.DOWNLOAD_COMPLETE".equals(intent.getAction())) {
                            Webtop.this.openDownloadedAttachment(context, intent.getLongExtra("extra_download_id", 0L));
                        }
                    }
                }, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
            }
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
            request.setMimeType(getMimeType(str2));
            request.setTitle(str2).setDescription("מוריד...");
            request.addRequestHeader("WebtopDownloader", "1");
            request.setAllowedOverMetered(true).setAllowedOverRoaming(true);
            request.setAllowedNetworkTypes(3);
            request.setNotificationVisibility(1);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, str2);
            ((DownloadManager) activity.getSystemService("download")).enqueue(request);
        } catch (IllegalStateException unused) {
            Toast.makeText(activity, "Please insert an SD card to download file", 1).show();
        }
    }

    private String getMimeType(String str) {
        String strSubstring = str.substring(str.lastIndexOf(".") + 1);
        if (strSubstring != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(strSubstring);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openDownloadedAttachment(Context context, long j) {
        Uri uriForDownloadedFile;
        DownloadManager downloadManager = (DownloadManager) context.getSystemService("download");
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(j);
        Cursor cursorQuery = downloadManager.query(query);
        if (cursorQuery.moveToFirst() && cursorQuery.getInt(cursorQuery.getColumnIndex("total_size")) == cursorQuery.getInt(cursorQuery.getColumnIndex("bytes_so_far")) && (uriForDownloadedFile = downloadManager.getUriForDownloadedFile(j)) != null) {
            openDownloadedAttachment(context, uriForDownloadedFile, cursorQuery.getString(Math.max(0, cursorQuery.getColumnIndex("media_type"))));
        }
        cursorQuery.close();
    }

    private void openDownloadedAttachment(Context context, Uri uri, String str) {
        if (uri != null) {
            if ("file".equals(uri.getScheme())) {
                uri = FileProvider.getUriForFile(context, "com.smartschool.webtop.provider", new File(uri.getPath()));
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uri, str);
            intent.setFlags(1073741825);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, e.toString(), 1).show();
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.clientWebView.saveState(bundle);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.clientWebView.restoreState(bundle);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.fontScale = 1.0f;
        applyOverrideConfiguration(configuration);
    }

    public WebView getWebView() {
        return this.clientWebView;
    }

    public void handleOnBackPressed() {
        if (this.clientWebView.canGoBack()) {
            this.clientWebView.goBack();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        WebView webView = this.clientWebView;
        if (webView != null) {
            webView.stopLoading();
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        handleOnBackPressed();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            handleOnBackPressed();
            return false;
        }
        return super.onKeyDown(i, keyEvent);
    }

    protected boolean isBiometricDevice() {
        BiometricAuthRequest biometricAuthRequest = new BiometricAuthRequest(BiometricApi.AUTO, BiometricType.BIOMETRIC_ANY, BiometricConfirmation.ANY);
        return BiometricManagerCompat.isHardwareDetected(biometricAuthRequest) && BiometricManagerCompat.hasEnrolled(biometricAuthRequest) && !BiometricManagerCompat.isBiometricSensorPermanentlyLocked(biometricAuthRequest, true) && !BiometricManagerCompat.isLockOut(biometricAuthRequest, true);
    }

    protected void biometricAuthentication(final String str) {
        BiometricAuthRequest biometricAuthRequest = new BiometricAuthRequest(BiometricApi.AUTO, BiometricType.BIOMETRIC_ANY, BiometricConfirmation.ANY);
        if (!isBiometricDevice()) {
            getWebView().loadUrl("javascript:try{window.loginComponentReference.noBiometricOnDevice('" + str.replace("'", "'") + "')}catch(e){}");
        } else {
            new BiometricPromptCompat.Builder(biometricAuthRequest, this).setDeviceCredentialFallbackAllowed(false).setTitle("חיבור לאפליקציה באמצעי ביומטרי").setSubtitle("התחברות באמצעי זיהוי קיים").setEnabledNotification(false).setEnabledBackgroundBiometricIcons(false).build().authenticate(new BiometricPromptCompat.AuthenticationCallback() { // from class: com.smartschool.webtop.Webtop.5
                @Override // dev.skomlach.biometric.compat.BiometricPromptCompat.AuthenticationCallback
                public void onFailed(Set<AuthenticationResult> set) {
                    super.onFailed(set);
                    Webtop.this.getWebView().loadUrl("javascript:try{window.loginComponentReference.bioReadFailed('" + str.replace("'", "'") + "')}catch(e){}");
                }

                @Override // dev.skomlach.biometric.compat.BiometricPromptCompat.AuthenticationCallback
                public void onCanceled(Set<AuthenticationResult> set) {
                    super.onCanceled(set);
                    Webtop.this.getWebView().loadUrl("javascript:try{window.loginComponentReference.bioReadCanceled('" + str.replace("'", "'") + "')}catch(e){}");
                }

                @Override // dev.skomlach.biometric.compat.BiometricPromptCompat.AuthenticationCallback
                public void onSucceeded(Set set) throws BiometricAuthException {
                    super.onSucceeded(set);
                    Webtop.this.getWebView().loadUrl("javascript:try{window.loginComponentReference.biometricReadSuccess('" + str.replace("'", "'") + "')}catch(e){}");
                }
            });
        }
    }

    protected void setRegistrationID() {
        WebView webView;
        String userToken = getUserToken("token");
        if (userToken.equals("") || (webView = this.clientWebView) == null) {
            return;
        }
        webView.loadUrl("javascript:try{window.loginComponentReference.setRegistrationID('android','" + userToken.replace("'", "'") + "')}catch(e){}");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDeviceID() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") == 0) {
            return ((TelephonyManager) getSystemService("phone")).getImei();
        }
        return "device";
    }

    public class WebAppInterface {
        private final Context context;

        WebAppInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void getBase64FromBlobData(String str, String str2, String str3) throws IOException {
            convertBase64StringToPdfAndStoreIt(str, str2, str3);
        }

        public String getBase64StringFromBlobUrl(String str, String str2, String str3) {
            if (str.startsWith("blob")) {
                return "javascript: var xhr = new XMLHttpRequest();xhr.open('GET', '" + str + "', true);xhr.setRequestHeader('Content-type','application/pdf');xhr.responseType = 'blob';xhr.onload = function(e) {    if (this.status == 200) {        var blobPdf = this.response;        var reader = new FileReader();        reader.readAsDataURL(blobPdf);        reader.onloadend = function() {            base64data = reader.result;            MobileDevice.getBase64FromBlobData(base64data,'" + str2 + "','" + str3 + "');        }    }};xhr.send();";
            }
            return "javascript:console.log('It is not a Blob URL');";
        }

        private void convertBase64StringToPdfAndStoreIt(String str, String str2, String str3) throws IOException {
            PendingIntent activity;
            try {
                DateFormat.getDateTimeInstance().format(new Date());
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + str2);
                String[] strArrSplit = str2.split("\\.");
                String str4 = strArrSplit[strArrSplit.length - 1];
                int i = 1;
                while (file.exists()) {
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + str2.replace("." + str4, "-" + i + "." + str4));
                    i++;
                }
                byte[] bArrDecode = Base64.decode(str.replaceFirst("^data:" + str3 + ";base64,", ""), 0);
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                fileOutputStream.write(bArrDecode);
                fileOutputStream.flush();
                if (file.exists()) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setDataAndType(FileProvider.getUriForFile(this.context, this.context.getApplicationContext().getPackageName() + ".provider", file), MimeTypeMap.getSingleton().getMimeTypeFromExtension(str4));
                    intent.addFlags(1);
                    PendingIntent.getActivity(this.context, 1, intent, 335544320);
                    if (Build.VERSION.SDK_INT >= 34) {
                        activity = PendingIntent.getActivity(this.context, 0, intent, 50331648);
                    } else {
                        activity = PendingIntent.getActivity(this.context, 0, intent, 1140850688);
                    }
                    NotificationManager notificationManager = (NotificationManager) this.context.getSystemService("notification");
                    NotificationChannel notificationChannel = new NotificationChannel("my_channel_01", "name", 2);
                    Notification notificationBuild = new Notification.Builder(this.context, "my_channel_01").setContentText("יש עבורך קובץ חדש שממתין!").setContentTitle("הקובץ ירד בהצלחה.").setContentIntent(activity).setChannelId("my_channel_01").setSmallIcon(android.R.drawable.sym_action_chat).build();
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(notificationChannel);
                        notificationManager.notify(1, notificationBuild);
                    }
                    try {
                        this.context.startActivity(intent);
                    } catch (ActivityNotFoundException unused) {
                        Toast.makeText(this.context, "קובץ ירד בהצלחה.\nלא ניתן לפתוח את הקובץ, לא נמצאה במכשיר אפליקציה שיכולה לפתוח את סוג קובץ זה.", 1).show();
                        return;
                    }
                }
                Toast.makeText(this.context, "קובץ ירד בהצלחה!", 1).show();
            } catch (Exception e) {
                Toast.makeText(this.context, e.getMessage(), 1).show();
            }
        }

        @JavascriptInterface
        public void downloadFile(String str, String str2) {
            Context context = this.context;
            ((Webtop) context).downloadFile((Activity) context, str, str2);
        }

        @JavascriptInterface
        public void changeStatusBarColor(String str) {
            ((Webtop) this.context).getWindow().setStatusBarColor(Color.parseColor(str));
        }

        @JavascriptInterface
        public void displayNotification(String str, String str2, String str3) {
            Webtop.displayNotification(str, str2, this.context, str3);
        }

        @JavascriptInterface
        public boolean isBiometricDevice() {
            return ((Webtop) this.context).isBiometricDevice();
        }

        @JavascriptInterface
        public void biometricFingerprintPrompt(final String str) {
            new Handler(this.context.getMainLooper()).post(new Runnable() { // from class: com.smartschool.webtop.Webtop$WebAppInterface$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m392xa8753a0e(str);
                }
            });
        }

        /* renamed from: lambda$biometricFingerprintPrompt$0$com-smartschool-webtop-Webtop$WebAppInterface, reason: not valid java name */
        /* synthetic */ void m392xa8753a0e(String str) {
            ((Webtop) this.context).biometricAuthentication(str);
        }

        @JavascriptInterface
        public void readQR() {
            try {
                Webtop.this.activityLauncher.launch(new ScanContract().createIntent(this.context, new ScanOptions().setDesiredBarcodeFormats("QR_CODE").setPrompt("").setCameraId(0).setOrientationLocked(true).setBeepEnabled(true).setBarcodeImageEnabled(false)), new ActivityResultCallback() { // from class: com.smartschool.webtop.Webtop$WebAppInterface$$ExternalSyntheticLambda1
                    @Override // androidx.activity.result.ActivityResultCallback
                    public final void onActivityResult(Object obj) {
                        this.f$0.m393lambda$readQR$1$comsmartschoolwebtopWebtop$WebAppInterface((ActivityResult) obj);
                    }
                });
            } catch (Exception unused) {
            }
        }

        /* renamed from: lambda$readQR$1$com-smartschool-webtop-Webtop$WebAppInterface, reason: not valid java name */
        /* synthetic */ void m393lambda$readQR$1$comsmartschoolwebtopWebtop$WebAppInterface(ActivityResult activityResult) {
            if (activityResult == null || activityResult.getResultCode() != -1) {
                Webtop.this.getWebView().loadUrl("javascript:try{window.onQRClose()}catch(e){}");
                return;
            }
            ScanIntentResult activityResult2 = ScanIntentResult.parseActivityResult(activityResult.getResultCode(), activityResult.getData());
            if (activityResult2 == null || activityResult2.getContents() != null) {
                Webtop.this.getWebView().loadUrl("javascript:try{window.onQRClose()}catch(e){}");
            } else {
                Webtop.this.getWebView().loadUrl("javascript:try{window.onQRRead('" + activityResult2.getContents().replace("'", "'") + "')}catch(e){}");
            }
        }

        @JavascriptInterface
        public String getDeviceID() {
            return ((Webtop) this.context).getDeviceID();
        }

        @JavascriptInterface
        public void clearCache() {
            ((Webtop) this.context).getWebView().clearCache(true);
        }

        @JavascriptInterface
        public void logoutUser() {
            Webtop.this.getSharedPreferences("com.smartschool.webtop", 0).edit().remove("auth").apply();
        }

        @JavascriptInterface
        public void loginUser(String str) {
            Webtop.this.getSharedPreferences("com.smartschool.webtop", 0).edit().putString("auth", str).apply();
        }
    }
}
