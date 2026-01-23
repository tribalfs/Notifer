package de.dlyt.yanndroid.notifer.service;

import android.app.NotificationManager;
import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.HashMap;
import java.util.List;

import de.dlyt.yanndroid.notifer.model.NotiPacket;
import de.dlyt.yanndroid.notifer.utils.HttpRequest;
import de.dlyt.yanndroid.notifer.utils.Preferences;

public class NotificationListener extends NotificationListenerService {

    private Preferences mPreferences;
    private HashMap<String, Integer> mEnabledPackages;
    private List<Preferences.ServerInfo> mServers;

    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mPreferences = new Preferences(this);

        mEnabledPackages = mPreferences.getEnabledPackages(enabledPackages -> mEnabledPackages = enabledPackages);
        mServers = mPreferences.getServers(servers -> mServers = servers);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        transmitNotification(sbn, false);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        transmitNotification(sbn, true);
    }

    private void transmitNotification(StatusBarNotification sbn, boolean removed) {
        String packageName = sbn.getPackageName();
        if (mPreferences.isServiceEnabled() && mEnabledPackages.containsKey(packageName)) {
            HttpRequest.postAll(mServers, new NotiPacket(this, sbn, mEnabledPackages.get(packageName), removed, mNotificationManager.getCurrentInterruptionFilter()));
        }
    }

}
