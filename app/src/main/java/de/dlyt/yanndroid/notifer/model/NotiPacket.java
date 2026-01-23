package de.dlyt.yanndroid.notifer.model;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import org.json.JSONException;
import org.json.JSONObject;

import de.dlyt.yanndroid.notifer.R;
import de.dlyt.yanndroid.notifer.utils.ColorUtil;

public class NotiPacket {

    public int id;
    public String packageName;
    public long time;

    public int color;

    public boolean ongoing;
    public boolean removed;
    public String template;

    public CharSequence label;
    public String title;
    public String text;
    public String subText;
    public String titleBig;
    public String textBig;

    public boolean progressIndeterminate;
    public int progressMax;
    public int progress;

    public int dnd;

    private NotiPacket() {
    }

    public NotiPacket(Context context, StatusBarNotification sbn, int color, boolean removed, int dnd) {
        Bundle bundle = sbn.getNotification().extras;
        String packageName = sbn.getPackageName();

        try {
            PackageManager pm = context.getPackageManager();
            this.label = pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0));
        } catch (PackageManager.NameNotFoundException e) {
            this.label = packageName;
        }

        this.id = sbn.getId();
        this.packageName = packageName;
        this.time = sbn.getPostTime();
        this.color = color;
        this.ongoing = sbn.isOngoing();
        this.removed = removed;
        this.template = sbn.getNotification().extras.getString(android.app.Notification.EXTRA_TEMPLATE);
        this.title = bundle.getString("android.title");
        this.text = bundle.getString("android.text");
        this.subText = bundle.getString("android.subText");
        this.titleBig = bundle.getString("android.title.big");
        this.textBig = bundle.getString("android.bigText");
        this.progressIndeterminate = bundle.getBoolean("android.progressIndeterminate");
        this.progressMax = bundle.getInt("android.progressMax");
        this.progress = bundle.getInt("android.progress");
        this.dnd = dnd;
    }

    public static NotiPacket testNotiPacket(Context context) {
        NotiPacket notiPacket = new NotiPacket();
        notiPacket.id = 0;
        notiPacket.packageName = context.getPackageName();
        notiPacket.time = System.currentTimeMillis();
        notiPacket.color = Color.RED;
        notiPacket.ongoing = false;
        notiPacket.removed = false;
        notiPacket.template = null;
        notiPacket.label = context.getString(R.string.app_name);
        notiPacket.title = "Test Notification";
        notiPacket.text = "This is a test notification";
        notiPacket.subText = null;
        notiPacket.titleBig = null;
        notiPacket.textBig = null;
        notiPacket.progressIndeterminate = false;
        notiPacket.progressMax = 0;
        notiPacket.progress = 0;
        notiPacket.dnd = NotificationManager.INTERRUPTION_FILTER_ALL;

        return notiPacket;
    }

    public String toJsonString(boolean inclContent) throws JSONException {
        JSONObject body = new JSONObject();

        JSONObject colors = new JSONObject();
        colors.put("hex", ColorUtil.toHex(color));
        colors.put("rgb", ColorUtil.toRGB(color));
        colors.put("hsv", ColorUtil.toHSV(color));
        colors.put("int", color);

        body.put("color", colors);

        body.put("id", id);
        body.put("time", time);
        body.put("ongoing", ongoing);
        body.put("removed", removed);
        body.put("progress_indeterminate", progressIndeterminate);
        body.put("progress_max", progressMax);
        body.put("progress", progress);
        body.put("dnd", dnd);

        body.put("package", packageName);

        if (inclContent) {
            body.put("label", label);
            body.put("template", template);
            body.put("title", title);
            body.put("text", text);
            body.put("sub_text", subText);
            body.put("title_big", titleBig);
            body.put("text_big", textBig);
        }

        return body.toString();
    }
}
