package com.felkertech.n.tvnotification.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
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
    /**Total length of time the chathead service will be present**/
    private static final int POPUP_DISPLAY_TIME = 5000;
    /**How long is a 'step' for animation purposes?**/
    private static final int FRAME = 33; //
    private static final String TAG = "tvnotification::Chathead";

    /*
     * Here are the different types of styles
     */
    private PopupParams STYLE_HONEYCOMB = new PopupParams(500, 500, 64, R.layout.popup_honeycomb, new PopupParams.PopupHandler() {
        @Override
        public void animateIn(int ANIMATION_OPEN, Handler loop) {
            chatHead.setAlpha(chatHead.getAlpha()+1f/(ANIMATION_OPEN/FRAME));
            if(chatHead.getAlpha() < 1)
                loop.sendEmptyMessageDelayed(0, FRAME);
        }

        @Override
        public void animateOut(int ANIMATION_OUT, Handler loop) {
            chatHead.setAlpha(chatHead.getAlpha()-1f/(ANIMATION_OUT/FRAME));
            if(chatHead.getAlpha() > 0)
                loop.sendEmptyMessageDelayed(0, FRAME);
        }

        @Override
        public void populate() {
            try {
                Context p = createPackageContext(alert.getPackageName(), 0);
                try {
                    Bitmap icon = BitmapFactory.decodeResource(p.getResources(),
                            alert.getIcon());
                    ((ImageView) chatHead.findViewById(R.id.notification_icon)).setImageBitmap(icon);
                } catch(Exception e) {
                    Log.d(TAG, "Alert icon not found");
                }
                try {
//                    if(alert.getIcon() != 0)
//                        ((ImageView) chatHead.findViewById(R.id.notification_icon)).setImageBitmap(alert.getBitmap());
                } catch(Exception e) {
                    Log.d(TAG, "Alert bitmap not found");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            try {
                ((TextView) chatHead.findViewById(R.id.notification_title)).setText(alert.getTitle() + "");
            } catch(Exception e) {
                Log.d(TAG, "Alert title not found");
            }
            try { //For large devices
                ((TextView) chatHead.findViewById(R.id.notification_subtext)).setText(alert.getText()+"");
            } catch(Exception ignored) {

            }
        }
    });

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
        //TODO Get SettingsManager, pick the proper layout, use a class for this and a few other attributes
        PopupParams style;
        style = STYLE_HONEYCOMB;
        chatHead = LayoutInflater.from(this).inflate(style.res, null);
        style.populatePopup.sendEmptyMessageDelayed(0, 0);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        params.x = 0;
        //TODO this fnc doesn't work, pass in height
        params.y = height-24-dpToPx(style.layoutHeight);
        Log.d(TAG, dpToPx(style.layoutHeight)+"");
        chatHead.setMinimumWidth(width);
        chatHead.setMinimumHeight(dpToPx(style.layoutHeight));
        chatHead.setAlpha((float) 0.02);

        windowManager.addView(chatHead, params);
        Handler killr = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message in) {
                stopSelf();
            }
        };
        //TODO Intro/Outro animations
        killr.sendEmptyMessageDelayed(0, POPUP_DISPLAY_TIME);

        style.animateIn.sendEmptyMessageDelayed(0, FRAME);
        style.animateOut.sendEmptyMessageDelayed(0, POPUP_DISPLAY_TIME-style.ANIMATION_CLOSE-FRAME);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroying head");
        if (chatHead != null) windowManager.removeView(chatHead);
    }

    public int dpToPx(int px) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (px * scale + 0.5f);
        return pixels;
    }

    /**
     * This class holds many different functions for a variety of popup styles
     */
    public static class PopupParams {
        /**The length of time when the popup will animate in**/
        private static int ANIMATION_OPEN = 500;
        /**The length of time when the popup will animate out**/
        private static int ANIMATION_CLOSE = 500; //
        private Handler animateIn;
        private Handler animateOut;
        private int layoutHeight = 64;
        private Handler populatePopup;
        private PopupHandler interfacer;
        private int res;

        public interface PopupHandler {
            public void animateIn(int in_duration, Handler loop);
            public void animateOut(int out_duration, Handler loop);
            public void populate();
        }
        public PopupParams(int in_duration, int out_duration, int height, int resource, final PopupHandler ph) {
            ANIMATION_OPEN = in_duration;
            ANIMATION_CLOSE = out_duration;
            layoutHeight = height;
            res = resource;
            animateIn = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message in) {
                    ph.animateIn(ANIMATION_OPEN, this);
                }
            };
            animateOut = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message in) {
                    ph.animateOut(ANIMATION_CLOSE, this);
                }
            };
            populatePopup = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message in) {
                    ph.populate();
                }
            };
        }
    }
}
