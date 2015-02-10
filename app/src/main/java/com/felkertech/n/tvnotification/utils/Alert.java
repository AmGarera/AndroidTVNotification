package com.felkertech.n.tvnotification.utils;

import android.app.Notification;
import android.graphics.Bitmap;
import android.os.Bundle;

/**
 * Created by N on 1/16/2015.
 */
public class Alert {
    private String packageName;
    private String title;
    private String text;
    private int color;
    private int priority;
    private String category;
    private int visibility;
    private int icon;
    private Bitmap bitmap;
    private Notification.Action[] actions;

    public Alert(String packageName, String title, String text, int color, int priority, String category, int visibility, Notification.Action[] actions, int icon, Bitmap bitmap) {
        this.packageName = packageName;
        this.title = title;
        this.text = text;
        this.color = color;
        this.priority = priority;
        this.category = category;
        this.visibility = visibility;
        this.actions = actions;
        this.icon = icon;
        this.bitmap = bitmap;
    }
    public Alert(Bundle stuff) {
        packageName = stuff.getString("PACKAGE_NAME");
        title = stuff.getString("TITLE");
        text = stuff.getString("TEXT");
        color = stuff.getInt("COLOR");
        priority = stuff.getInt("PRIORITY");
        category = stuff.getString("CATEGORY");
        visibility = stuff.getInt("VISIBILITY");
//      stuff b.putParcelable("ACTIONS", actions);
        icon = stuff.getInt("ICON");
        bitmap = stuff.getParcelable("BITMAP");
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public int getPriority() {
        return priority;
    }

    public String getCategory() {
        return category;
    }

    public int getVisibility() {
        return visibility;
    }

    public int getIcon() {
        return icon;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getPackageName() {
        return packageName;
    }

    public Notification.Action[] getActions() {
        return actions;
    }
    public boolean hasActions() {
        if(actions == null)
            return false;
        return actions.length > 0;
    }
    //TODO Actions are currently not supported
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("PACKAGE_NAME", packageName);
        b.putString("TITLE", title);
        b.putString("TEXT", text);
        b.putInt("COLOR", color);
        b.putInt("PRIORITY", priority);
        b.putString("CATEGORY", category);
        b.putInt("VISIBILITY", visibility);
//        b.putParcelable("ACTIONS", actions);
        b.putInt("ICON", icon);
        b.putParcelable("BITMAP", bitmap);
        return b;
    }
    @Override
    public String toString() {
        return "Alert {title="+title+", text="+text+", color="+color+", priority="+priority+", category="+category+", visibility="+visibility+", icon="+icon+"}";
    }
}
