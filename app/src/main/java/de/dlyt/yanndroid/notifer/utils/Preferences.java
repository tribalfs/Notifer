package de.dlyt.yanndroid.notifer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

import de.dlyt.yanndroid.notifer.R;

public class Preferences {

    public interface PreferencesListener<T> {
        void onChange(T value);
    }

    public static class ServerInfo {
        public String name;
        public String url;
        public ColorUtil.ColorFormat colorFormat;

        public ServerInfo(String name, String url, ColorUtil.ColorFormat colorFormat) {
            this.name = name;
            this.url = url;
            this.colorFormat = colorFormat;
        }
    }

    private static final String ENABLED_PACKAGES_KEY = "enabled_packages";
    private static final String SERVERS_KEY = "servers";

    private final Context mContext;
    private final SharedPreferences mSharedPreferences;

    public Preferences(Context context) {
        this.mContext = context;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isServiceEnabled() {
        return mSharedPreferences.getBoolean(mContext.getString(R.string.preference_service_enabled_key), false);
    }

    public void setServiceEnabled(boolean enabled) {
        mSharedPreferences.edit().putBoolean(mContext.getString(R.string.preference_service_enabled_key), enabled).apply();
    }

    public HashMap<String, Integer> getEnabledPackages(PreferencesListener<HashMap<String, Integer>> listener) {
        if (listener != null) {
            mSharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
                if (ENABLED_PACKAGES_KEY.equals(key))
                    listener.onChange(loadEnabledPackages());
            });
        }
        return loadEnabledPackages();
    }

    private HashMap<String, Integer> loadEnabledPackages() {
        return new Gson().fromJson(mSharedPreferences.getString(ENABLED_PACKAGES_KEY, "{}"), new TypeToken<HashMap<String, Integer>>() {
        }.getType());
    }

    public void setEnabledPackages(HashMap<String, Integer> enabledPackages) {
        mSharedPreferences.edit().putString(ENABLED_PACKAGES_KEY, new Gson().toJson(enabledPackages)).apply();
    }

    public List<ServerInfo> getServers(PreferencesListener<List<ServerInfo>> listener) {
        if (listener != null) {
            mSharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
                if (SERVERS_KEY.equals(key))
                    listener.onChange(loadServers());
            });
        }
        return loadServers();
    }

    private List<ServerInfo> loadServers() {
        return new Gson().fromJson(mSharedPreferences.getString(SERVERS_KEY, "[]"), new TypeToken<List<ServerInfo>>() {
        }.getType());
    }

    public void setServers(List<ServerInfo> servers) {
        mSharedPreferences.edit().putString(SERVERS_KEY, new Gson().toJson(servers)).apply();
    }

}
