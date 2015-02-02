package com.dolphin.helper;

import android.content.Intent;
import android.net.Uri;


public class IntentHelper {
    public static Intent sendTextEmail(String text, String subject, String[] emails) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        if (emails != null)
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
        if (subject != null)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (text != null)
            intent.putExtra(Intent.EXTRA_TEXT, text);

        return Intent.createChooser(intent, "Send Email");
    }

    public static Intent openLink(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        return intent;
    }
}
