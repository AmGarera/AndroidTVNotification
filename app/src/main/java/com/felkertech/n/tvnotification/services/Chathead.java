package com.felkertech.n.tvnotification.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felkertech.n.tvnotification.R;
import com.felkertech.n.tvnotification.utils.Alert;

/**
 * Created by N on 2/8/2015.
 */
public class Chathead extends Service {

    private WindowManager windowManager;
    private View chatHead;
    private Alert alert;
    private static final String TAG = "tvnotification::Chathead";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public Chathead(String name) {
        //super(name);
    }
    public Chathead() {
        //super("Nope");
    }

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }


    protected void onHandleIntent(Intent intent) {

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int a = super.onStartCommand(intent,flags,startId);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.d(TAG, intent.toString());
        Log.d(TAG, "Incoming");
        alert = new Alert(intent.getBundleExtra("VALUE"));
        Log.d(TAG, alert.toString()+"");
        Log.d(TAG, alert.getTitle()+"");
        post();
        return a;
    }

    public void post() {

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Log.d(TAG, "Posting");
        //TODO Get SettingsManager, pick the proper layout
        if(true) {
            chatHead = LayoutInflater.from(this).inflate(R.layout.popup_honeycomb, null);
            /*((ImageView) chatHead.findViewById(R.id.notification_icon)).setImageResource(alert.getIcon());
            ((TextView) chatHead.findViewById(R.id.notification_title)).setText(alert.getTitle());*/
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        Log.d(TAG, "w="+params.width);
        params.x = 0;
        params.y = 0;

        windowManager.addView(chatHead, params);
        Handler killr = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message in) {
//                stopSelf();
            }
        };
        //TODO Intro/Outro animations
//        killr.sendEmptyMessageDelayed(0, 10000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroying head");
        if (chatHead != null) windowManager.removeView(chatHead);
    }
}
