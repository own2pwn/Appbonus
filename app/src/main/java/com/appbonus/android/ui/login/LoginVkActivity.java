package com.appbonus.android.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.appbonus.android.R;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginVkActivity extends Activity {
    public static final String REDIRECTED_URL = "http://oauth.vk.com/blank.html";
    public static final String REDIRECTED_URLS = "http://oauth.vk.com/blank.html";

    WebView webview;
    ProgressBar progress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vk_web);

        //Получаем элементы
        webview = (WebView) findViewById(R.id.web);
        progress = (ProgressBar) findViewById(R.id.progress);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);
        webview.clearCache(true);

        //Чтобы получать уведомления об окончании загрузки страницы
        webview.setWebViewClient(new VkWebViewClient());

        String url = "http://oauth.vk.com/authorize?client_id=" + getString(R.string.vk_id) +
                "&scope=wall,offline&redirect_uri=" + URLEncoder.encode(REDIRECTED_URL) + "&response_type=token";
        webview.loadUrl(url);
        webview.setVisibility(View.VISIBLE);
    }

    class VkWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress.setVisibility(View.VISIBLE);
            parseUrl(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.startsWith("http://oauth.vk.com/authorize") || url.startsWith("http://oauth.vk.com/oauth/authorize")) {
                progress.setVisibility(View.GONE);
            }
        }
    }

    private void parseUrl(String url) {
        try {
            if (url == null) {
                return;
            }
            if (url.startsWith(REDIRECTED_URL) || url.startsWith(REDIRECTED_URLS)) {
                if (!url.contains("error")) {
                    String[] auth = VKUtil.parseRedirectUrl(url);
                    webview.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);

                    //Строим данные
                    Intent intent = new Intent();
                    intent.putExtra("token", auth[0]);
                    intent.putExtra("uid", auth[1]);

                    //Возвращаем данные
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            } else if (url.contains("error?err")) {
                setResult(RESULT_CANCELED);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    public static class VKUtil {
        public static String[] parseRedirectUrl(String url) throws Exception {
            String access_token = extractPattern(url, "access_token=(.*?)&");
            String user_id = extractPattern(url, "user_id=(\\d*)");
            if (user_id == null || user_id.length() == 0 || access_token == null || access_token.length() == 0) {
                throw new Exception("Failed to parse redirect url " + url);
            }
            return new String[]{access_token, user_id};
        }

        public static String extractPattern(String string, String pattern) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(string);
            if (!m.find())
                return null;
            return m.toMatchResult().group(1);
        }
    }
}
