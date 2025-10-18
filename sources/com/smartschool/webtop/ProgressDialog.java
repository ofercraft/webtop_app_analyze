package com.smartschool.webtop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/* loaded from: classes2.dex */
public class ProgressDialog {
    public static Dialog progressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(LayoutInflater.from(context).inflate(R.layout.progress_bar, (ViewGroup) null));
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return dialog;
    }
}
