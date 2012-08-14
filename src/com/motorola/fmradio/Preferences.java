package com.motorola.fmradio;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Preferences {
    private static final String KEY_VOLUME = "volume";
    private static final String KEY_LAST_FREQUENCY = "last_frequency";
    private static final String KEY_LAST_CHANNEL = "last_channel";
    private static final String KEY_SCANNED = "scanned";
    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_IGNORE_AIRPLANE_MODE = "ignore_airplane_mode";
    private static final String KEY_IGNORE_NO_HEADSET = "ignore_no_headset";
    private static final String KEY_SEEK_SENSITIVITY = "seek_sensitivity";
    private static final String KEY_MEDIA_BUTTON_BEHAVIOUR = "media_button_behaviour";
    private static final String KEY_HIDE_ACTIONBAR = "hide_actionbar";
    private static final String KEY_USE_LOUDSPEAKER = "use_loudspeaker";

    private static final int DEFAULT_VOLUME = 0;
    private static final int DEFAULT_FREQUENCY = FMUtil.MIN_FREQUENCY;
    private static final int DEFAULT_SENSITIVITY = 12;

    static public int getVolume(Context context) {
        return getPrefs(context).getInt(KEY_VOLUME, DEFAULT_VOLUME);
    }
    static public void setVolume(Context context, int volume) {
        getPrefs(context).edit().putInt(KEY_VOLUME, volume).commit();
    }

    static public int getLastFrequency(Context context) {
        return getPrefs(context).getInt(KEY_LAST_FREQUENCY, DEFAULT_FREQUENCY);
    }
    static public void setLastFrequency(Context context, int frequency) {
        if (frequency > 0) {
            getPrefs(context).edit().putInt(KEY_LAST_FREQUENCY, frequency).commit();
        }
    }

    static public boolean isScanned(Context context) {
        return getPrefs(context).getBoolean(KEY_SCANNED, false);
    }
    static public void setScanned(Context context, boolean scanned) {
        getPrefs(context).edit().putBoolean(KEY_SCANNED, scanned).commit();
    }

    static public int getSeekSensitivityThreshold(Context context) {
        String value = getPrefs(context).getString(KEY_SEEK_SENSITIVITY, null);
        if (value == null) {
            return DEFAULT_SENSITIVITY;
        }
        return Integer.parseInt(value);
    }

    static public boolean isAirplaneModeIgnored(Context context) {
        return getPrefs(context).getBoolean(KEY_IGNORE_AIRPLANE_MODE, false);
    }
    static public boolean isHeadsetRequired(Context context) {
        return !getPrefs(context).getBoolean(KEY_IGNORE_NO_HEADSET, false);
    }

    static public boolean mediaButtonPrevNextSwitchesPresets(Context context) {
        String value = getPrefs(context).getString(KEY_MEDIA_BUTTON_BEHAVIOUR, null);
        return !TextUtils.equals(value, "seek");
    }

    static public boolean useSpeakerAsOutput(Context context) {
        return getPrefs(context).getBoolean(KEY_USE_LOUDSPEAKER, false);
    }
    static public void setUseSpeaker(Context context, boolean useSpeaker) {
        getPrefs(context).edit().putBoolean(KEY_USE_LOUDSPEAKER, useSpeaker).commit();
    }

    static public SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
