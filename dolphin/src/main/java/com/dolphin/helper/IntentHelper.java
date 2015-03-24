package com.dolphin.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public class IntentHelper {
    private static final String TWITTER = "twitter";

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

    public static Intent[] createSharingIntent(Context context, String text, String twitterText) {
        List<Intent> targetedShareIntents = new ArrayList<>();
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfo) {
                String packageName = resolveInfo.activityInfo.packageName;
                Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
                targetedShareIntent.setType("text/plain");
                if (packageName.contains(TWITTER)) {
                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, twitterText);
                } else {
                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                }

                targetedShareIntent.setPackage(packageName);
                targetedShareIntents.add(targetedShareIntent);
            }
            return targetedShareIntents.toArray(new Intent[targetedShareIntents.size()]);
        }
        return null;
    }
}
