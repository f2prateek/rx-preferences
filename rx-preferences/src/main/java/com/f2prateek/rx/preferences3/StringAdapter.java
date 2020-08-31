package com.f2prateek.rx.preferences3;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

final class StringAdapter implements RealPreference.Adapter<String> {
    static final StringAdapter INSTANCE = new StringAdapter();

    @NonNull
    @Override
    public String get(@NonNull String key,
                      @NonNull SharedPreferences preferences,
                      @NonNull String defaultValue) {
        //noinspection ConstantConditions
        return preferences.getString(key, defaultValue);
    }

    @Override
    public void set(@NonNull String key,
                    @NonNull String value,
                    @NonNull SharedPreferences.Editor editor) {
        editor.putString(key, value);
    }
}
