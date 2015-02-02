package com.dolphin.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtils {
    public static void copyToClipboard(Context context, String str) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", str);
        clipboardManager.setPrimaryClip(clipData);
    }
}
