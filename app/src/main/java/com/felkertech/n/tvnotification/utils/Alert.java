package com.felkertech.n.tvnotification.utils;

import android.app.Notification;
import android.graphics.Bitmap;

/**
 * Created by N on 1/16/2015.
 */
public class Alert {
    private String title;
    private String text;
    private int color;
    private int priority;
    private String category;
    private int visibility;
    private int icon;
    private Bitmap bitmap;
    private Notification.Action[] actions;

    public Alert(String title, String text, int color, int priority, String category, int visibility, Notification.Action[] actions, int icon, Bitmap bitmap) {
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

    public Notification.Action[] getActions() {
        return actions;
    }
    public boolean hasActions() {
        if(actions == null)
            return false;
        return actions.length > 0;
    }
}
