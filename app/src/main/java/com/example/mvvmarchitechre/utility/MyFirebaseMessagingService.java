/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mvvmarchitechre.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.mvvmarchitechre.R;
import com.example.mvvmarchitechre.view.login.ActivityLogin;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import java.util.Map;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static final String MyPREFERENCES = "MyPrefs" ;
    String tittle , message;


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = s;
        Log.d(TAG, "FCM Refreshed token: " + refreshedToken);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message notification payload: " + remoteMessage.getNotification().toString());
        Log.d(TAG, "Message notification payload: " + remoteMessage.getNotification().getBody() +remoteMessage.getNotification().getTitle());

        tittle = remoteMessage.getNotification().getTitle();
        message = remoteMessage.getNotification().getBody();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            try {
                sendNotification(remoteMessage.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification: " + remoteMessage.getNotification());
        }
    }

    private void sendNotification(Map<String, String> tempObj) {
        if (tempObj != null) {
            String message = tempObj.get("message");
            try {
                String jsondata = new Gson().toJson(tempObj);
                String newJson =  jsondata.replace("=",":");

                String split[] = newJson.split("\"data\":\"");

                String jsonsplit = split[1];
                String newstr = jsonsplit.replace("\\", "");
                String newstr_newstr = newstr.replace("}\"", "");

//                Gson gson = new Gson();
//                FCMResponse response = gson.fromJson(newstr_newstr, FCMResponse.class);
//
//                showNotification(response.getNotificationType(),response.getNotificationSubType(), response.getKey1());
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public void showNotification(String notificationType,String status,int orderId) {
        Intent intent;

        if(notificationType.equals("Order")) {
                intent = new Intent(this, ActivityLogin.class);
                intent.putExtra("isPush","yes");
        }
        else{
            intent = new Intent(this, ActivityLogin.class);
            intent.putExtra("isPush","no");
        }

        Intent localIntent = new Intent("hii");
        LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this).sendBroadcast(localIntent);


        Bitmap bitmap;
        bitmap = null;
        PendingIntent pIntent = getPendingIntentWithStackBuilder(intent);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(tittle)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND).setVibrate(new long[]{1000, 1000})
                        .setSound(defaultSoundUri)
                        .setAutoCancel(true).setContentIntent(pIntent);

        if (bitmap != null) {
            notificationBuilder.setLargeIcon(bitmap)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null));
        } else {
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(new Random().nextInt() /* ID of notification */, notificationBuilder.build());
    }

    private PendingIntent getPendingIntentWithStackBuilder(Intent intent) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        return stackBuilder.getPendingIntent((int) System.currentTimeMillis(), PendingIntent.FLAG_ONE_SHOT);
    }

}
