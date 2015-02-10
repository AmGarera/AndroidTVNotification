package com.felkertech.n.tvnotification;

/**
 * Created by N on 1/16/2015.
 */
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.felkertech.n.tvnotification.services.Chathead;
import com.felkertech.n.tvnotification.utils.Alert;
import com.felkertech.n.tvnotification.utils.Popup;

import java.util.ArrayList;

public class NotificationListener extends NotificationListenerService {

    private String TAG = "tvnotification::"+this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.felkertech.n.tvnotification.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.i(TAG,"**********  onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + " " + sbn.getNotification().tickerText + " " + sbn.getPackageName());
        Intent i = new  Intent("com.felkertech.n.tvnotification.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "n");
        sendBroadcast(i);
        Log.d(TAG, ""+sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString());
        Popup p = new Popup(sbn);
        Log.d(TAG, p.category+"-C");
        Log.d(TAG, p.toString());
        Alert note = p.publicAlert();
        Log.d(TAG, note.toString());
        Intent popup = new Intent(getApplicationContext(), Chathead.class);
        popup.putExtra("VALUE", note.toBundle());
        startService(popup);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText +"t" + sbn.getPackageName());
        Intent i = new  Intent("com.felkertech.n.tvnotification.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "n");

        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NotificationListener.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new  Intent("com.felkertech.n.tvnotification.NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                ArrayList<Popup> p = new ArrayList<>();
                try {
                    for (StatusBarNotification sbn : getActiveNotifications()) {
                        Popup pp = new Popup(sbn, i);
                        p.add(pp);
                        Intent i2 = new Intent("com.felkertech.n.tvnotification.NOTIFICATION_LISTENER_EXAMPLE");
                        String output = pp.toString();
                        i2.putExtra("notification_event", output);
                        sendBroadcast(i2);
                        i++;
                    }
                } catch (Exception e) {
                    Intent i2 = new Intent("com.felkertech.n.tvnotification.NOTIFICATION_LISTENER_EXAMPLE");
                    String output = e.getMessage();
                    i2.putExtra("notification_event", output);
                    sendBroadcast(i2);
                }
                Intent i3 = new  Intent("com.felkertech.n.tvnotification.NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);

            }

        }
    }

}