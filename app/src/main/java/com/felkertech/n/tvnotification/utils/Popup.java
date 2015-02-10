package com.felkertech.n.tvnotification.utils;

import android.app.Notification;
import android.graphics.Bitmap;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.felkertech.n.tvnotification.R;

/**
 * Created by N on 1/16/2015.
 */
public class Popup {
    StatusBarNotification statusBarNotification;
    Notification generic;
    Notification pub;
    Notification.Action[] actions;
    public static final String TAG = "tvnotification::Popup";

    //Attributes
    int order;
    int priority;
    int color;
    public String category;
    public String title;
    String publicTitle;
    String bigText;
    String publicBigText;
    int visibility;
    String pack;
    int icon;
    Bitmap largeIcon;

    //Constants
    final static int PRIORITY_DEFAULT = 0;
    final static int PRIORITY_HIGH = 1;
    final static int PRIORITY_LOW = -1;
    final static int PRIORITY_MAX = 2;
    final static int PRIORITY_MIN = -2;

    final static int VISIBILITY_PRIVATE = 0;
    final static int VISIBILITY_PUBLIC = 1;
    final static int VISIBILITY_SECRET = -1;

    public Popup(StatusBarNotification sbn) {
        this(sbn, 1);
    }
    public Popup(StatusBarNotification sbn, int o) {
        generic = sbn.getNotification();
        priority = PRIORITY_DEFAULT;
        visibility = VISIBILITY_PRIVATE;
        color = R.color.accent_material_dark;
        category = "status";
        order = o;
        pack = sbn.getPackageName();
        icon = generic.icon;
        largeIcon = generic.largeIcon;
        title = "5";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pub = sbn.getNotification().publicVersion;
            try {
                priority = generic.priority;
            } catch(Exception ignored) {}
            try {
                visibility = generic.visibility;
            } catch (Exception ignored) {}
            try {
                color = generic.color;
            } catch(Exception ignored) {}
            try {
                category = generic.category;
            } catch(Exception ignored) {}
        }
        //TODO Try/Catch all
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(generic.extras.containsKey(Notification.EXTRA_BIG_TEXT)) {
                        bigText = generic.extras.getCharSequence(Notification.EXTRA_BIG_TEXT).toString();
                        publicBigText = bigText;
                    }
                }
                if(generic.extras.containsKey(Notification.EXTRA_TITLE)) {
                    title = generic.extras.getCharSequence(Notification.EXTRA_TITLE).toString();
                    publicTitle = title;
                    Log.d(TAG, title);
                }
//                Log.d("tvnotification", title+"  "+bigText);
                if(bigText == null && generic.extras.containsKey(Notification.EXTRA_TEXT)) {
                    bigText = generic.extras.getCharSequence(Notification.EXTRA_TEXT).toString();
                    publicBigText = bigText;
                }
//                Log.d("tvnotification", title+"  "+bigText);
            } catch(Exception e) {
                Log.d(TAG, "KK "+e.getMessage());
            }

            try {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && pub != null) {
                    Log.d("tvnotification", title+"w  "+bigText);
                    if(pub.extras.containsKey(Notification.EXTRA_BIG_TEXT)) {
                        publicBigText = pub.extras.getCharSequence(Notification.EXTRA_BIG_TEXT).toString();
                    }
                    Log.d("tvnotification", title+"x  "+bigText);
                    if(pub.extras.containsKey(Notification.EXTRA_TITLE)) {
                        publicTitle = pub.extras.getCharSequence(Notification.EXTRA_TITLE).toString();
                    }
                    Log.d("tvnotification", title+"y  "+bigText);
                    if(publicBigText == null && pub.extras.containsKey(Notification.EXTRA_TEXT)) {
                        publicBigText = pub.extras.getCharSequence(Notification.EXTRA_TEXT).toString();
                    }
                    Log.d("tvnotification", title+"z  "+bigText);
                }
            } catch(Exception e) {
                Log.d(TAG, "LP "+e.getMessage());
            }
            try {
                actions = generic.actions;
            } catch(Exception e) {}
            Log.d(TAG, "Done building "+title);
        }
        Log.d(TAG, "Done building 2"+title);
    }
    public String toString() {
        Log.d(TAG, title+"a  "+bigText);
        String s = order+" "+pack+"\n"+title+"; Priority "+priority+"; Category "+category+"\n"+bigText+"\n";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasActions()) {
                for (Notification.Action a : actions) {
                    s = s + "Action " + a.title + "\n";
                }
            }
        }
        return s+"\n\n";
    }
    public boolean hasActions() {
        if(actions == null)
            return false;
        return actions.length > 0;
    }

    public String getTitle() {
        return title;
    }

    /**
     * A popup is a large-scale class that contains what may be seen as conflicting info. There's
     * a public title, and a private title. A public notification (maybe) and a generic note.
     * This is a lot of info to manage.
     * The Alert class is a compiled version of these data. Whichever algorithm is used to determine
     * what should be displayed, an Alert can be formed with just that data.
     * @return Alert object
     */
    public Alert publicAlert() {
        return new Alert(pack, publicTitle, publicBigText, color, priority, category, visibility, actions, icon, largeIcon);
    }
}
